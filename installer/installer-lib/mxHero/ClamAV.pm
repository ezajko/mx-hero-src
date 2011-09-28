package mxHero::ClamAV;

use strict;
use warnings;
no warnings qw(uninitialized);

use Term::UI;
use Term::ReadLine;

use mxHero::Tools;

# distribution => package name
my %PKG_NAME = (
	"debian" => "clamav clamav-daemon",
	"ubuntu" => "clamav clamav-daemon"
	# TODO: redhat, suse
);

sub install
{
	my $errorRef = $_[0];
	
	my $distri = lc( &mxHero::Tools::getDistri() );
	
	# Install binary if needed
	if ( ! &mxHero::Tools::packageCheck( $PKG_NAME{$distri} ) ) {
		if ( ! &mxHero::Tools::packageInstall( $PKG_NAME{$distri} ) ) {
			$$errorRef = "Failed to intall $PKG_NAME{$distri} package";
			return 0;
		}
	}

	return &configure( $errorRef );
}

sub upgrade
{
	my $errorRef = $_[0];

	# BRUNO
	
	return &configure( $errorRef );
}

sub configure
{
	my $errorRef = $_[0];

	# BRUNO
	
	return 1;
}

## PRIVATE





1;
