package mxHero::Tomcat;

use strict;
use warnings;
no warnings qw(uninitialized);

use mxHero::Config;
use mxHero::Tools;

# distribution => package name
my %PKG_NAME = (
	"debian" => "tomcat6",
	"ubuntu" => "tomcat6",
	"redhat" => "tomcat6"
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
