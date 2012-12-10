package mxHero::HeroAttach;

use strict;
use warnings;
no warnings qw(uninitialized);

use Term::UI;
use Term::ReadLine;

use mxHero::Config;
use mxHero::Locale;
use mxHero::Tools;

my $defaultMax = 256; # in MB

sub install
{
	my $errorRef = $_[0];
	
	my $term = Term::ReadLine->new( 'mxHero' );
	my $reply;
	
	myPrint "\n\n*** " . T("HERO ATTACH SETUP"). " ***\n\n";
	
	$reply = $term->get_reply( prompt => T("What is the maximum size email in MegaBytes to allow for Hero Attach"),
				   default  => $defaultMax );
	
	$reply =~ s/[^\d]*(\d+)[^\d]*/$1/g; # Extract only numbers in case user adds MB to input.
	
	myPrint "\n\n";
	myPrint	T( "ENSURE THAT YOUR USERS CAN SEND EMAILS TO YOUR EMAIL SERVER OF UP TO" ) . " $reply MB\n";
	myPrint T( "Enter to continue" ) . "...\n";
	my $enter = <STDIN>;
	
	# Set message_size_limit (in postfix and mxhero)

	my $sizeInKBytes = int($reply) * 1024 * 1024;
	my %entry;

	if ( &mxHero::Tools::zimbraCheck() ) {
		print "\n*** NOTE FOR ZIMBRA INSTALLATIONS ***\n";
		print "You can change maximum size email configuration by running the command:\n";
		print "\t# su - zimbra\n";
		print "\t\$ zmprov mcf zimbraMtaMaxMessageSize $sizeInKBytes zimbraFileUploadMaxSize $sizeInKBytes\n\n";

		my $term = Term::ReadLine->new( 'mxHero' );
		my $bool = $term->ask_yn( prompt => T("The installer can submit this command now? "),
					   default  => 'y');

		if ( $bool ) {
			system( "su - zimbra -c 'zmprov mcf zimbraMtaMaxMessageSize $sizeInKBytes zimbraFileUploadMaxSize $sizeInKBytes'" );
		}
	}
	elsif (! -f $myConfig{CURRENT_POSTFIX_MAIN_CF})
	{
		myPrint "\n".T("Did not find a postfix 'main.cf' file.")."\n";
		my $file = $term->get_reply( prompt => T("Please enter full path to your main.cf".":"));
		if ( $file && -f $file ) {
			$myConfig{CURRENT_POSTFIX_MAIN_CF} = $file;
		} else {
			myPrint T("Failed to find file")." '$file' \n";
			myPrint T("Stopping installation")."\n";
			$$errorRef = T("Failed to find configuration file");
			return 0;
		}
	}

	if (! &mxHero::Tools::zimbraCheck())
	{
		%entry = ( "message_size_limit" => $sizeInKBytes );

		if ( ! &mxHero::Tools::alterSimpleConfigFile( $myConfig{CURRENT_POSTFIX_MAIN_CF}, \%entry, '=' ) ) {
			warn "Failed to add Postfix message_size_limit. Aborting installation.\n";
			exit;
		}
	}

	%entry = ( "messageMaxSize" => $sizeInKBytes );

	if ( ! &mxHero::Tools::alterSimpleConfigFile( $myConfig{MXHERO_POSTFIX_CONFIG}, \%entry, '=' ) ) {
		warn "Failed to add Hero Attach messageMaxSize config. Aborting installation.\n";
		exit;
	}
	
	## Discover IP
	my $command = `/sbin/ifconfig eth0 | grep 'inet addr'`;
	$command =~ m/inet addr\:(\S+)/s;
	my $ip = $1;
	$reply = $term->get_reply(
					prompt => T("What is the external address or IP of this mxHero installation. Hit enter to use the auto-detected ip address "),
					default  => $ip );

	%entry = (
		"http.file.server.attach"		=> "http://$reply:8080/fileserver/download",
		"url.file.server"			=> "http://$reply:8080/fileserver",
		"url.static.content.images.attach.html"	=> "http://$reply:8080/fileserver/images"
	);

	if ( ! &mxHero::Tools::alterSimpleConfigFile( $myConfig{MXHERO_HEROATTACH_CONFIG}, \%entry, '=' ) ) {
		warn "Failed to add Hero Attach link config. Aborting installation.\n";
		exit;
	}

#	&_fillTemplate ("http://$reply:8080");

	return 1;
}

sub upgrade
{
	my $errorRef = $_[0];

	my $oldVersion = &mxHero::Tools::mxHeroVersion();

	rename ("$myConfig{MXHERO_PATH}/attachments/templates", "$myConfig{MXHERO_PATH}/attachments/$oldVersion-templates");
	system ("cp -a $myConfig{INSTALLER_PATH}/binaries/$myConfig{MXHERO_INSTALL_VERSION}/mxhero/attachments/templates $myConfig{MXHERO_PATH}/attachments");

	# new flags on 1.8.0
	if (&mxHero::Tools::mxheroVersionCompare($oldVersion, '1.7.2.RELEASE') <= 0)
	{
		my %properties;
		&mxHero::Tools::loadProperties ("$myConfig{MXHERO_PATH}/configuration/properties", \%properties);
		
		$properties{'org.mxhero.engine.plugin.attachmentlink.cfg'}->{'http.file.server.attach'} =~ m|(https?://.+?)/|i;
		my $url = $1;
		
		my %entry = (
			"http.file.server.attach"		=> "$url/fileserver/download",
			"url.file.server"			=> "$url/fileserver",
			"url.static.content.images.attach.html"	=> "$url/fileserver/images"
		);
		
		if ( ! &mxHero::Tools::alterSimpleConfigFile( $myConfig{MXHERO_HEROATTACH_CONFIG}, \%entry, '=' ) ) {
			warn "Failed to add Hero Attach link config. Aborting installation.\n";
			exit;
		}
	}

	#&_fillTemplate ($url);

	return 1;
}


sub configure
{
	my $errorRef = $_[0];

	return 1;
}

#sub _fillTemplate
#{
#	my $url = $_[0];
#
#	# Change templates
#	`/usr/bin/perl -i -pe 's|\%FILE_SERVER\%|$url|g' $myConfig{MXHERO_PATH}/attachments/templates/attach_*.vm`;
#}

1;
