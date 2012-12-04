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

use lib '../../installer-lib';
#use lib '/opt/mxhero/installer-lib';

use DBI;
use Net::LDAP;
use Data::Dumper;

use mxHero::Config;
use mxHero::Tools;

#
## Actions
#

my %properties;

#&mxHero::Tools::loadProperties ("$myConfig{MXHERO_PATH}/configuration/properties", \%properties);
&mxHero::Tools::loadProperties ("/home/bruno/mxhero/svn/trunk/installer/binaries/1.8.0.RELEASE/mxhero/configuration/properties", \%properties);

$properties{'org.mxhero.engine.plugin.dbpool.cfg'}->{jdbcUrl} =~ m|jdbc\:(.+?)\://(.+?)/(.+)|;
my %dbInfo = (
	type => $1, host => $2, name => $3,
	username => $properties{'org.mxhero.engine.plugin.dbpool.cfg'}->{username},
	password => $properties{'org.mxhero.engine.plugin.dbpool.cfg'}->{password}
);

my $dbh = &connectToDatabase(\%dbInfo);
my $zProp = &getZimbraProperties($dbh);
my $mailboxes = &getMailboxServers($zProp);
my $ldapProp = &getLdapProperties($zProp);
my $zCos = &getCosInformation($zProp);
my $zData = &getAccountsAndQuotaUsage($zProp, $mailboxes);
&loadAccountsCosAndType ($zProp, $ldapProp, $zData, $zCos);
&saveZDataToDatabase($zProp, $zData);

print Dumper ($zData);

#
## Subs
#

sub connectToDatabase
{
	my $dbInfo = shift;

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

	# Save zimbra.private.key for this session; will be deleted on exit

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

	my @servers = map {s/^\s+|\s+$//g; $_} `$cmd`;
	return \@servers;
}

sub getAccountsAndQuotaUsage
{
	my $zProp = shift;
	my $mailboxes = shift;

	my %data;

	for my $mailbox (@{$mailboxes})
	{
		my $cmd = &getSSHCmd($zProp) . "zmprov -s $mailbox getQuotaUsage $mailbox";
		my @output = map {s/^\s+|\s+$//g; $_} `$cmd`;

		for my $entry (@output)
		{
			my @values = split (/\s/, $entry);
			my @email = split (/\@/, $values[0]);

			my %info = (
				account => $email[0],
				domain => $email[1],
				totalQuota => $values[1],
				usedQuota => $values[2]
			);

			$data{$values[0]} = \%info;
		}
	}

	return \%data;
}

sub getCosInformation
{
	my $zProp = shift;

	my %zCos;

	my $cmd = &getSSHCmd($zProp) . 'zmprov getAllCos';
	my @coses = map {s/^\s+|\s+$//g; $_} `$cmd`;
	
	for my $cos (@coses)
	{
		my $cmd = &getSSHCmd($zProp) . "zmprov getCos $cos";
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
	my @output = map {s/^\s+|\s+$//g; $_} `$cmd`;

	for my $entry (@output)
	{
		$entry =~ m/(.+?)(?:\s\=\s)(.+)/o;
		$ldapProp{$1} = $2;
	}

	return \%ldapProp;
}

sub saveZDataToDatabase
{
	my $zProp = shift;
	my $zData = shift;
}

sub loadAccountsCosAndType
{
	my $zProp = shift;
	my $ldapProp = shift;
	my $zData = shift;
	my $zCos = shift;

	my $ldap = Net::LDAP->new($ldapProp->{ldap_host}) || die "$@";
	$ldap->bind($ldapProp->{zimbra_ldap_userdn} , password => $ldapProp->{zimbra_ldap_password});

	my $ldapSearch = $ldap->search (
		scope => 'sub',
		filter => '(&(uid=*)(objectClass=zimbraAccount))',
		attrs => "mail,zimbraCOSId",
	);

	my @entries = $ldapSearch->entries if $ldapSearch->entries;
	# if error
	$ldapSearch->code && die $ldapSearch->error;
	
	return unless @entries;
	
	foreach my $entry (@entries)
	{
		next if (!$entry->get_value('mail') && !$entry->get_value('zimbraCOSId'));
		
		my $cos = 'default';
		$cos = $zCos->{$entry->get_value('zimbraCOSId')} if ($entry->get_value('zimbraCOSId'));
		${$zData->{$entry->get_value('mail')}}{cos} = $cos;
        }
}

END
{
	unlink ($zProp->{pkFilePath}) if (defined ($zProp));
}
