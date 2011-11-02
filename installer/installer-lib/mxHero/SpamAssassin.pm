package mxHero::SpamAssassin;

use strict;
use warnings;

use mxHero::Tools;

# distribution => package name
my %PKG_NAME = (
	"debian" => "spamassassin",
	"ubuntu" => "spamassassin",
	"redhat" => "spamassassin"
	# TODO: redhat, suse
);

sub install
{
	my $errorRef = $_[0];
	
	my $distri = lc( &mxHero::Tools::getDistri() );
	
	# in future add dialogue for external SpamAssassin (ex. get IP and Port)

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
	if ( $distri =~ /Redhat/ ) { # TODO: check if anything to do in the case of Redhat?
		return 1;
	}

	my $file = "/etc/default/spamassassin";
	my %entry = ( "ENABLED" => 1, "CRON" =>  1);

	if ( ! &mxHero::Tools::alterSimpleConfigFile( $file, \%entry, '=' ) ) {
		$$errorRef = "Failed to alter $file";
		return 0;
	}

	return 1;
}

## PRIVATE






1;
