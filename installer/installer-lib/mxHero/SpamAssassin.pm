package mxHero::SpamAssassin;

use strict;
use warnings;

sub download
{
	return 1;
}

sub install
{
	
	if ( &_zimbraCheck ) {
		print "*** NOTE FOR ZIMBRA INSTALLATIONS ***\n\n";
		print "Disable Zimbra's SpamAssassin once mxHero is up and running.\n";
		print "This can be accomplished by running the command: <zimbra command>\n"; # TODO - get zimbra command
		print "Hit <enter> to continue...\n";
		while ( <STDIN> ) { # wait for enter
			last;
		}
	}
	
	# TODO - install SpamAssassin
	# in future add dialogue for external SpamAssassin (ex. get IP and Port)
	
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
