package mxHero::HeroAttach;

use strict;
use warnings;
no warnings qw(uninitialized);

use Term::UI;
use Term::ReadLine;

use mxHero::Config;
use mxHero::Locale;

my $defaultMax = 256; # in MB

sub install
{
	my $errorRef = $_[0];
	
	my $term = Term::ReadLine->new( 'mxHero' );
	my $reply;
	
	$reply = $term->get_reply( prompt => T("What is the maximum size email in MegaBytes to allow for Hero Attach")." [$defaultMax] ",
				   default  => $defaultMax );
	
	$reply =~ s/[^\d]*(\d+)[^\d]*/$1/g; # Extract only numbers in case user adds MB to input.
	
	print T( "\n\nENSURE THAT YOUR USERS CAN SEND EMAILS TO YOUR SERVER OF UP TO" ) . "$defaultMax MB\n";
	print T( "Enter to continue" ) . "...\n";
	my $enter = <STDIN>;
	
	## ALTER main.cf established in Postfix.pm around lines 63-99
	### NEED TO EXPORT / GLOBAL VALUE IN Postfix.pm
	
	## Discover IP
	my $ip = '0.0.0.0'; # IP DISCOVERY HERE
	$reply = $term->get_reply(
					prompt => T("What is the external address or IP of this mxHero installation. Hit enter to use the auto-detected ip address ")." [$ip] ",
					default  => $ip );
	
	# SET IP ADDRESS for HERO ATTACH IN MXHERO
	
	return 1;
}


sub upgrade
{
	my $errorRef = $_[0];
	
	return 1;
}


sub configure
{
	my $errorRef = $_[0];

	return 1;
}

1;
