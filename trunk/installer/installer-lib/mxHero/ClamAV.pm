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
	
	if ( $distri eq "redhat" ) {
		warn "ClamAV not yet included in Redhat installation\n";
		sleep(2);
		return 1;
	}
	
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
	
	my %entry = ( "TCPSocket" => 6665, "TCPAddr" =>  '127.0.0.1', "MaxConnectionQueueLength" => 80,
		"MaxFileSize" => "500M", "StreamMaxLength" => "500M"
	);
	
	my $file = "/etc/clamav/clamd.conf";
	
	if ( ! &mxHero::Tools::alterSimpleConfigFile( $file, \%entry ) ) {
		$$errorRef = "Failed to alter $file";
		return 0;
	}
	
	return 1;
}

## PRIVATE





1;
