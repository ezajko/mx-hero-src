package mxHero::ClamAV;

use strict;
use warnings;
no warnings qw(uninitialized);

use Term::UI;
use Term::ReadLine;

sub download
{
	return 1;
}

sub install
{
	# Note: if machine already has ClamAV use existing instance.
	
	if ( &_zimbraCheck ) {
		# If has Zimbra - ask to use Zimbra ClamAV instance
		print "*** NOTE FOR ZIMBRA INSTALLATIONS ***\n\n";
		print "mxHero can use the ClamAV (antivirus) service provided by your Zimbra installation.\n";
		my $term = Term::ReadLine->new( 'mxHero' );
		my $bool = $term->ask_yn( prompt   => "Have mxHero use Zimbra's ClamAV service:",
							   default  => 'y' );
		if ( $bool ) {
			# TODO - set mxHero to use Zimbra's ClamAV
		} else {
			# TODO - call standard installation
		}
	} else {
		# TODO - call standard installation
	}

	
	return 1;
}

sub upgrade
{
	return 1;
}

sub configure
{
	return 1;
}

## PRIVATE

sub _zimbraCheck
{
	return 0 if ! -d "/opt/zimbra";

	return 1;
}




1;
