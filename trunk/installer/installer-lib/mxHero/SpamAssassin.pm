package mxHero::SpamAssassin;

use strict;
use warnings;

use mxHero::Tools;

# distribution => package name
my %PKG_NAME = (
	"debian" => "spamassassin",
	"ubuntu" => "spamassassin"
	# TODO: redhat, suse
);

sub install
{
	my $errorRef = $_[0];
	
	my $distri = lc( &mxHero::Tools::getDistri() );
	
	# Zimbra note
	if ( &mxHero::Tools::zimbraCheck() ) {
		print "\n*** NOTE FOR ZIMBRA INSTALLATIONS ***\n";
		print "Disable Zimbra's SpamAssassin once mxHero is up and running.\n";
		print "This can be accomplished by running the command: <zimbra command>\n"; # TODO - get <zimbra command> BRUNO ?
		print "Hit <enter> to continue...\n";
		while ( <STDIN> ) { # wait for enter
			last;
		}
	}
	
	# in future add dialogue for external SpamAssassin (ex. get IP and Port)

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
