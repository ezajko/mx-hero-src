package mxHero::Tomcat;

use strict;
use warnings;
no warnings qw(uninitialized);

use mxHero::Config;
use mxHero::Tools;

# distribution => mysql package name
my %PKG_NAME = (
	"debian" => "tomcat6",
	"ubuntu" => "tomcat6"
	# TODO: redhat, suse
);

sub download
{
	my $error = $_[0];
	
	my $distri = lc( &mxHero::Tools::getDistri() );
	
	# Install binary if needed
	if ( ! &mxHero::Tools::packageCheck( $PKG_NAME{$distri} ) ) {
		if ( ! &mxHero::Tools::packageInstall( $PKG_NAME{$distri} ) ) {
			$$error = "Failed to intall $PKG_NAME{$distri} package";
			return 0;
		}
	}

	return 1;
}

sub install
{
	my $error = $_[0];

	# BRUNO
	
	return 1;
}

sub upgrade
{
	my $error = $_[0];

	# BRUNO
	
	return 1;
}

sub configure
{
	my $error = $_[0];

	# BRUNO
	
	return 1;
}

1;
