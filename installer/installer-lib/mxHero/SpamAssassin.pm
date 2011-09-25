package mxHero::SpamAssassin;

use strict;
use warnings;

use mxHero::Tools;

# distribution => mysql package name
my %PKG_NAME = (
	"debian" => "spamassassin",
	"ubuntu" => "spamassassin"
	# TODO: redhad, suse
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
	
	if ( &mxHero::Tools::zimbraCheck() ) {
		print "*** NOTE FOR ZIMBRA INSTALLATIONS ***\n\n";
		print "Disable Zimbra's SpamAssassin once mxHero is up and running.\n";
		print "This can be accomplished by running the command: <zimbra command>\n"; # TODO - get <zimbra command>
		print "Hit <enter> to continue...\n";
		while ( <STDIN> ) { # wait for enter
			last;
		}
	}
	
	if ( ! &download() ) {
		return 0;
	}
	# in future add dialogue for external SpamAssassin (ex. get IP and Port)
	
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

## PRIVATE






1;
