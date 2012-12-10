#!/usr/bin/perl

#	mxHero Zimbra SP Edition - Zimbra data collector
#	Copyright (C) 2012 mxHero, Inc.
#	
#	This program is free software: you can redistribute it and/or modify
#	it under the terms of the GNU General Public License as published by
#	the Free Software Foundation, either version 3 of the License, or
#	(at your option) any later version.
#	
#	This program is distributed in the hope that it will be useful,
#	but WITHOUT ANY WARRANTY; without even the implied warranty of
#	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#	GNU General Public License for more details.
#	
#	You should have received a copy of the GNU General Public License
#	along with this program.  If not, see <http://www.gnu.org/licenses/>.

use strict;
use warnings;

#use lib '../../installer-lib';
use lib '/opt/mxhero/installer-lib';

use DBI;
use Net::LDAP;
#use Data::Dumper;

use mxHero::Config;
use mxHero::Tools;

#
## Definitions
#

# If at least one flag match, the version will be considered
my %zimbraEditionsFlags = (
	'Professional' => {
		'zimbraFeatureMobileSyncEnabled' => 'TRUE',
		'zimbraFeatureMAPIConnectorEnabled' => 'TRUE',
		'zimbraArchiveEnabled' => 'TRUE'
	},
	'Standard' => {
		'zimbraFeatureAdvancedSearchEnabled' => 'TRUE',
		'zimbraFeatureSharingEnabled' => 'TRUE',
		'zimbraFeatureTaggingEnabled' => 'TRUE'
	},
	'BusinessEmailPlus' => {
		'zimbraFeatureCalendarEnabled' => 'TRUE',
		'zimbraFeatureManageZimlets' => 'TRUE'
	},
	'BusinessEmail' => {
	}
);

# From the most specific going down
my @zimbraEditionsHierarchy = ('Professional', 'Standard', 'BusinessEmailPlus', 'BusinessEmail');

#
## Actions
#

print "mxHero Zimbra SP Edition - collecting Zimbra data...\n";

my %properties;

&mxHero::Tools::loadProperties ("$myConfig{MXHERO_PATH}/configuration/properties", \%properties);
#&mxHero::Tools::loadProperties ("/home/bruno/mxhero/svn/trunk/installer/binaries/1.8.0.RELEASE/mxhero/configuration/properties", \%properties);

$properties{'org.mxhero.engine.plugin.dbpool.cfg'}->{jdbcUrl} =~ m|jdbc\:(.+?)\://(.+?)/(.+)|;
my %dbInfo = (
	type => $1, host => $2, name => $3,
	username => $properties{'org.mxhero.engine.plugin.dbpool.cfg'}->{username},
	password => $properties{'org.mxhero.engine.plugin.dbpool.cfg'}->{password}
);

my $dbh = &connectToDatabase(\%dbInfo);
my $zProp = &getZimbraProperties($dbh); # will exit if not Zimbra SP Edition

my $mailboxes = &getMailboxServers($zProp);
my $ldapProp = &getLdapProperties($zProp);
my $zCos = &getCosInformation($zProp);

my $zData = &getAccountsCosAndType ($ldapProp, $zCos, \%zimbraEditionsFlags, \@zimbraEditionsHierarchy);
&loadAccountsAndQuotaUsage($zProp, $mailboxes, $zData);

&saveZDataToDatabase($dbh, $zData);
&cleanupTasks($zProp);

print "Finished!\n";

#print Dumper ($zData);

#
## Subs
#

sub cleanupTasks
{
	my $zProp = shift;

	print "Deleting Zimbra private key temporary file.\n";
	unlink ($zProp->{pkFilePath});
}

sub connectToDatabase
{
	my $dbInfo = shift;

	print "Connecting to mxHero database.\n";

	my $dbi = $dbInfo->{type} . ':' . $dbInfo->{name} . ';' . 'host=' . $dbInfo->{host};
	return DBI->connect ('DBI:' . $dbi, $dbInfo->{username}, $dbInfo->{password}) || die "Could not connect to database: $DBI::errstr";
}

sub getZimbraProperties
{
	my $dbh = shift;

	my %zimbra = (pkFilePath => '/tmp/mxhero.zk');

	my $sql = "SELECT property_key, property_value FROM system_properties WHERE property_key LIKE 'zimbra.%'";
	my $sth = $dbh->prepare ($sql);
	$sth->execute ();

	while (my $result = $sth->fetchrow_hashref ())
	{
		$zimbra{$result->{property_key}} = $result->{property_value};
	}

	if (!exists ($zimbra{'zimbra.installation'}) || $zimbra{'zimbra.installation'} ne 'true')
	{
		print "Not a Zimbra SP Edition install, exiting.\n";
		exit;
	}

	if (!exists ($zimbra{'zimbra.ldap.host'}) || $zimbra{'zimbra.ldap.host'} eq '')
	{
		print "LDAP host not given, exiting.\n";
		exit;
	}

	if (!exists ($zimbra{'zimbra.private.key'}) || $zimbra{'zimbra.private.key'} eq '')
	{
		print "Private key not given, exiting.\n";
		exit;
	}

	# Save zimbra.private.key for this session; will be deleted on exit

	print "Creating Zimbra private key temporary file.\n";

	umask (0077);
	open (PK, '>' . $zimbra{pkFilePath});
	print PK $zimbra{'zimbra.private.key'};
	close (PK);

	return \%zimbra;
}

sub getSSHCmd
{
	my $zProp = shift;
	return "ssh -o StrictHostKeyChecking=no -n -i $zProp->{pkFilePath} zimbra\@$zProp->{'zimbra.ldap.host'} ";
}

sub getMailboxServers
{
	my $zProp = shift;

	my $cmd = &getSSHCmd($zProp) . 'zmprov getAllServers mailbox';
	print "Getting mailbox servers ($cmd).\n";

	my @servers = map {s/^\s+|\s+$//g; $_} `$cmd`;
	return \@servers;
}

sub loadAccountsAndQuotaUsage
{
	my $zProp = shift;
	my $mailboxes = shift;
	my $zData = shift;

	for my $mailbox (@{$mailboxes})
	{
		my $cmd = &getSSHCmd($zProp) . "zmprov -s $mailbox getQuotaUsage $mailbox";
		print "Getting quota usage ($cmd).\n";
		my @output = map {s/^\s+|\s+$//g; $_} `$cmd`;

		for my $entry (@output)
		{
			my @values = split (/\s/, $entry);

			if (exists ($zData->{$values[0]}))
			{
				${$zData->{$values[0]}}{totalQuota} = $values[1];
				${$zData->{$values[0]}}{usedQuota} = $values[2];
			}
			else
			{
				print "Ignoring quota data for $values[0], system or invalid account!\n";
			}
		}
	}
}

sub getCosInformation
{
	my $zProp = shift;

	my %zCos;

	my $cmd = &getSSHCmd($zProp) . 'zmprov getAllCos';
	print "Getting COS list ($cmd).\n";
	my @coses = map {s/^\s+|\s+$//g; $_} `$cmd`;
	
	for my $cos (@coses)
	{
		my $cmd = &getSSHCmd($zProp) . "zmprov getCos $cos";
		print "Getting COS information ($cmd).\n";
		my @output = map {s/^\s+|\s+$//g; $_} `$cmd`;

		for my $entry (@output)
		{
			$entry =~ m/(.+?)(?:\:\s)(.+)/o;
			$zCos{$cos}->{$1} = $2;

			if ($1 eq 'zimbraId') #shortcut
			{
				$zCos{$2} = $cos;
			}
		}
	}

	return \%zCos;
}

sub getLdapProperties
{
	my $zProp = shift;
	my %ldapProp;

	my $cmd = &getSSHCmd($zProp) . 'zmlocalconfig -s | grep ldap';
	print "Getting Zimbra LDAP properties ($cmd).\n";
	my @output = map {s/^\s+|\s+$//g; $_} `$cmd`;

	for my $entry (@output)
	{
		$entry =~ m/(.+?)(?:\s\=\s)(.+)/o;
		$ldapProp{$1} = $2;
	}

	return \%ldapProp;
}

sub getZimbraEdition
{
	my $zFlags = shift;
	my $zimbraEditionsFlags = shift;
	my $zimbraEditionsHierarchy = shift;

	for my $edition (@{$zimbraEditionsHierarchy})
	{
		for my $flag (keys %{$zimbraEditionsFlags->{$edition}})
		{
			if (exists ($zFlags->{$flag}) && $zFlags->{$flag} eq $zimbraEditionsFlags->{$edition}->{$flag})
			{
				#print "Matched $flag = $zFlags->{$flag}, so edition is $edition\n";
				return $edition;
			}
		}
	}

	# If nothing matches, it's the last in the hierarchy
	return $zimbraEditionsHierarchy[-1];
}

sub saveZDataToDatabase
{
	my $dbh = shift;
	my $zData = shift;

	print "Saving information on mxHero database.\n";

	for my $entry (values %{$zData})
	{
		$entry->{totalQuota} = 0 unless (exists ($entry->{totalQuota}));
		$entry->{usedQuota} = 0 unless (exists ($entry->{usedQuota}));

		my $sql = 'INSERT INTO zimbra_provider_data SET ' .
			"insert_date = NOW(), " .
			"account = '$entry->{account}', " .
			"domain = '$entry->{domain}', " .
			"totalQuota = $entry->{totalQuota}, " .
			"usedQuota = $entry->{usedQuota}, " .
			"accountType = '$entry->{accountType}', " .
			"cos = '$entry->{cos}' " .
			"ON DUPLICATE KEY UPDATE " .
			"totalQuota = $entry->{totalQuota}, " .
			"usedQuota = $entry->{usedQuota}, " .
			"accountType = '$entry->{accountType}', " .
			"cos = '$entry->{cos}'";

		my $sth = $dbh->do ($sql);
	}
}

sub getZimbraEditionsAttrs
{
	my $zimbraEditionsFlags = shift;
	my @attrs;

	for my $entry (values %{$zimbraEditionsFlags})
	{
		for my $flag (keys %{$entry})
		{
			push (@attrs, $flag);
		}
	}

	return \@attrs;
}

sub getAccountFlags
{
	my $cosObj = shift;
	my $entryObj = shift;
	my $zimbraEditionsFlags = shift;

	my %zFlags = %{$cosObj};
	my $zAttrs = &getZimbraEditionsAttrs ($zimbraEditionsFlags);

	for my $attr (@{$zAttrs})
	{
		if ($entryObj->get_value($attr))
		{
			# Overload COS entries with account specific entries
			$zFlags{$attr} = $entryObj->get_value($attr);
			#print "Overloaded: $attr\n";
		}
	}

	return \%zFlags;
}

sub getAccountsCosAndType
{
	my $ldapProp = shift;
	my $zCos = shift;
	my $zimbraEditionsFlags = shift;
	my $zimbraEditionsHierarchy = shift;

	my %data;

	print "Getting COS and Type information.\n";

	# LDAP query is way faster than zmprov getAccount by each one
	my $ldap = Net::LDAP->new($ldapProp->{ldap_host}) || die "$@";
	$ldap->bind($ldapProp->{zimbra_ldap_userdn} , password => $ldapProp->{zimbra_ldap_password});

	my @attrs = ('zimbraMailDeliveryAddress', 'zimbraCOSId');
	push (@attrs, @{&getZimbraEditionsAttrs ($zimbraEditionsFlags)});

	my $ldapSearch = $ldap->search (
		scope => 'sub',
		filter => '(&(uid=*)(objectClass=zimbraAccount)(!(objectClass=zimbraCalendarResource))(!(zimbraIsSystemResource=TRUE)))',
		attrs => \@attrs
	);

	# if error
	$ldapSearch->code && die $ldapSearch->error;
	
	my @entries = $ldapSearch->entries if $ldapSearch->entries;
	return unless @entries;

	foreach my $entry (@entries)
	{
		next if (!$entry->get_value('zimbraMailDeliveryAddress') && !$entry->get_value('zimbraCOSId'));

		my $cos = 'default';
		$cos = $zCos->{$entry->get_value('zimbraCOSId')} if ($entry->get_value('zimbraCOSId'));

		my @email = split (/\@/, $entry->get_value('zimbraMailDeliveryAddress'));
		
		my %info = (
			account => $email[0],
			domain => $email[1],
			cos => $cos,
			accountType => &getZimbraEdition(&getAccountFlags ($zCos->{$cos}, $entry, $zimbraEditionsFlags), $zimbraEditionsFlags, $zimbraEditionsHierarchy)
		);
		
		$data{$entry->get_value('zimbraMailDeliveryAddress')} = \%info;
		
		print 'Account: ' . $entry->get_value('zimbraMailDeliveryAddress') . ", COS: $cos, Edition: " . $data{$entry->get_value('zimbraMailDeliveryAddress')}{accountType} . ".\n";
        }

	return \%data;
}
