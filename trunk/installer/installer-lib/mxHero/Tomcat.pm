package mxHero::Tomcat;

use strict;
use warnings;
no warnings qw(uninitialized);

use mxHero::Config;
use mxHero::Tools;

# distribution => package name
my %PKG_NAME = (
	"debian" => "tomcat6 libqt4-webkit libxext6 libfontconfig1 libxrender1",
	"ubuntu" => "tomcat6 libqt4-webkit libxext6 libfontconfig1 libxrender1",
	"redhat" => "tomcat6 libXrender fontconfig libXext"
	# TODO: redhat, suse
);

sub install
{
	my $errorRef = $_[0];
	
	my $distri = lc( &mxHero::Tools::getDistri() );
	
	#if ( $distri eq 'ubuntu' ) {
	#	my $version = &mxHero::Tools::getDistriVersion();
	#	if ( $version =~ /^8\./ ) {
	#		$PKG_NAME{$distri} = "tomcat5.5";
	#	}
	#}
	
	# Install binary if needed
	if ( ! &mxHero::Tools::packageCheck( $PKG_NAME{$distri} ) ) {
		if ( ! &mxHero::Tools::packageInstall( $PKG_NAME{$distri} ) ) {
			$$errorRef = "Failed to install $PKG_NAME{$distri} package";
			return 0;
		}
	}

	return &configure( $errorRef );
}

sub upgrade
{
	my $errorRef = $_[0];

	my $mxHeroVersion = &mxHero::Tools::mxHeroVersion();

	# Sanity check
	if ( ! $mxHeroVersion ) {
		$$errorRef = "mxHero Version information incomplete.";
		return 0;
	}

	# new package starting from 1.9.0
	if (&mxHero::Tools::mxheroVersionCompare($mxHeroVersion, '1.8.0.RELEASE') <= 0)
	{
		my %LIB_PKG_NAME = (
			"debian" => "libqt4-webkit libxext6 libfontconfig1 libxrender1",
			"ubuntu" => "libqt4-webkit libxext6 libfontconfig1 libxrender1",
			"redhat" => "libXrender fontconfig libXext"
			# TODO: redhat, suse
		);

		my $distri = lc( &mxHero::Tools::getDistri() );
	
		# Install binary if needed
		if ( ! &mxHero::Tools::packageCheck( $LIB_PKG_NAME{$distri} ) ) {
			if ( ! &mxHero::Tools::packageInstall( $LIB_PKG_NAME{$distri} ) ) {
				$$errorRef = "Failed to install $LIB_PKG_NAME{$distri} package";
				return 0;
			}
		}
	}

	return 1;
}

sub configure
{
	my $errorRef = $_[0];
	
	my $distri = &mxHero::Tools::getDistri();
	if ( $distri !~ /Redhat/i ) { # only go on if Redhat
		return 1;
	}
	
	# For Redhat need to set JAVA_HOME="/opt/mxhero/java" in /etc/tomcat6/tomcat6.conf
	
	my $file = "/etc/tomcat6/tomcat6.conf";
	
	my %entry = ( "JAVA_HOME" => '"/opt/mxhero/java"' );
	
	if ( ! &mxHero::Tools::alterSimpleConfigFile( $file, \%entry, '=' ) ) {
		$$errorRef = "Failed to alter $file";
		return 0;
	}
	
	return 1;
}

1;
