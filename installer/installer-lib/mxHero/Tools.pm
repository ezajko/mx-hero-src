package mxHero::Tools;

use strict;
use warnings;

use mxHero::Config;

# Super simple distri check
sub getDistri
{
	# Detect: Ubuntu, Debian, Redhat, Suse
	my $distri;
	if ( -f "/etc/lsb-release" ) {
		$distri = &_processLsbRelease();
	} elsif ( -f "/etc/redhat-release" ) {
		$distri = "Redhat";
	} elsif ( -f "/etc/SuSE-release" ) {
		$distri = "Suse";
	}
	
	return $distri;
}

sub zimbraCheck
{
	return 0 if ! -d "/opt/zimbra";

	return 1;
}

# Returns mxHero version number or undef if no mxHero installed.
sub mxHeroVersion
{
	return &_getVersion("/opt/mxhero/VERSION");
}


sub mxHeroInstallerVersion
{
	return &_getVersion("$myConfig{INSTALLER_PATH}/VERSION");
}

# Check installed package, optionally against a minimum required version
# return 1 if package OK, 0 if not
sub packageCheck
{
	my $package = $_[0];
	my $minimumVersion = $_[1];

	my $distri = &getDistri();
	
	if ( $distri =~ /Ubuntu/i || $distri =~ /Debian/i ) {
		my $installedVersion = `/usr/bin/dpkg-query -W -f='\${Version}' $package 2>/dev/null`;
		if ( $installedVersion ) {
			if ( $minimumVersion ) {
				return &_checkDebianPackageVersion( $installedVersion, $minimumVersion );
			}
			return 1;
		} else {
			return 0;
		}
	}
	# TODO - other distributions ...
	
	return 1;
}

# Returns 1 if a > b, 0 if the same, -1 if b > a
sub mxheroVersionCompare
{
	my $a = $_[0];
	my $b = $_[1];
	my @a;
	my @b;

	$a =~ /(\d+)\D(\d+)\D(\d+)\D*.*/; # ex. 1.0.0RELEASE or 1.0.1-beta
	@a = ( $1, $2, $3 );

	$b =~ /(\d+)\D(\d+)\D(\d+)\D*.*/;
	@b = ( $1, $2, $3 );

	for my $i ( qw(0 1 2) ) {
		my $val;
		$val = $a[$i] <=> $b[$i];
		if ( $val ) {
				return $val;
		}
	}

	return 0;
}


## PRIVATE

sub _getVersion
{
	my $version;
	my $versionFilePath = $_[0];
	
	if ( -f $versionFilePath ) {
		if ( ! open(F,$versionFilePath) ) {
			warn $!;
			return 0;
		}
		while ( $version = <F> ) {
			next if $version =~ /^\s*\#/ || $version =~ /^\s*$/;
			last;
		}
		close F;
		chomp($version);
	}

	return $version;
}

# Check debian package version numbers
# return 1 if $installedVersion is equal to or greater than minimumVersion
sub _checkDebianPackageVersion
{
	my $installedVersion = $_[0];
	my $minimumVersion = $_[1];
	
	# TODO
	# use Debian::Dpkg::Version (will need to include in local lib)
	
	return 1;
}

sub _processLsbRelease
{
	if ( ! open(F,"/etc/lsb-release") ) {
		warn $!;
	}

	while (<F>) {
		if ( $_ =~ /DISTRIB_ID=(\w+)/ ) {
			return $1;
		}
	}

}



1;