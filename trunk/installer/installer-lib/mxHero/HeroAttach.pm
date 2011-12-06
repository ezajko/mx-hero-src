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
	
	print "\n\n*** " . T("HERO ATTACH SETUP"). " ***\n\n";
	
	$reply = $term->get_reply( prompt => T("What is the maximum size email in MegaBytes to allow for Hero Attach")." [$defaultMax] ",
				   default  => $defaultMax );
	
	$reply =~ s/[^\d]*(\d+)[^\d]*/$1/g; # Extract only numbers in case user adds MB to input.
	
	print "\n\n" . T( "ENSURE THAT YOUR USERS CAN SEND EMAILS TO YOUR EMAIL SERVER OF UP TO" ) . " $reply MB\n";
	print T( "Enter to continue" ) . "...\n";
	my $enter = <STDIN>;
	
	# Set message_size_limit (in postfix and mxhero)

	my $sizeInBytes = int($reply) * 1024 * 1024;
	my %entry;

	%entry = ( "message_size_limit" => $sizeInBytes );

	if ( ! &mxHero::Tools::alterSimpleConfigFile( $myConfig{CURRENT_POSTFIX_MAIN_CF}, \%entry, '=' ) ) {
		warn "Failed to add Postfix message_size_limit. Aborting installation.\n";
		exit;
	}

	%entry = ( "messageMaxSize" => $sizeInBytes );

	if ( ! &mxHero::Tools::alterSimpleConfigFile( $myConfig{MXHERO_POSTFIX_CONFIG}, \%entry, '=' ) ) {
		warn "Failed to add Hero Attach messageMaxSize config. Aborting installation.\n";
		exit;
	}
	
	## Discover IP
	my $command = `/sbin/ifconfig eth0 | grep 'inet addr'`;
	$command =~ m/inet addr\:(\S+)/s;
	my $ip = $1;
	$reply = $term->get_reply(
					prompt => T("What is the external address or IP of this mxHero installation. Hit enter to use the auto-detected ip address ")." [$ip] ",
					default  => $ip );

	%entry = ( "http.file.server.attach" => "http://$reply:8080/fileserver/download" );

	if ( ! &mxHero::Tools::alterSimpleConfigFile( $myConfig{MXHERO_HEROATTACH_CONFIG}, \%entry, '=' ) ) {
		warn "Failed to add Hero Attach link config. Aborting installation.\n";
		exit;
	}

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
