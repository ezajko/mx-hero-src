package mxHero::JDK;

# TODO: Must convert to use locales

use strict;
use warnings;
no warnings qw(uninitialized);

use mxHero::Config;
use LWP::Simple;

my $JDK_PATH = 'jdk';
my $JDK_DOWNLOAD_PATH = 'http://download.oracle.com/otn-pub/java/jdk/6u27-b07';
my $JDK_X86_FILENAME = 'jdk-6u27-linux-i586.bin';
my $JDK_X64_FILENAME = 'jdk-6u27-linux-x64.bin';

sub download
{
	my $errorRef = $_[0];

	my $downloadFile;

	if ($Config{archname} =~ m/x86_64/)
	{
		$downloadFile = $JDK_X64_FILENAME;
	}
	else
	{
		$downloadFile = $JDK_X86_FILENAME;
	}

	if (-f ("$myConfig{INSTALLER_PATH}/$JDK_PATH/$downloadFile"))
	{
		print "Using JDK file $downloadFile.\n";
	}
	else
	{
		print "Downloading JDK file $JDK_DOWNLOAD_PATH/$downloadFile...\n";
		mkdir ("$myConfig{INSTALLER_PATH}/$JDK_PATH");
		my $http_response;
###		$http_response = getstore "$JDK_DOWNLOAD_PATH/$downloadFile", "$myConfig{INSTALLER_PATH}/$JDK_PATH/$downloadFile";
		$http_response = 200; ### TESTING
		if ( $http_response !~ /^2\d\d/ ) {
			$$errorRef = "Failed to download $JDK_DOWNLOAD_PATH/$downloadFile\nHTTP Response: $http_response";
			return 0;
		}
	}
	
	return 1;
}

sub install
{
	my $errorRef = $_[0];
	
	# BRUNO
	
	return &configure( $errorRef );
}

sub upgrade
{
	my $errorRef = $_[0];
	
	# BRUNO - verify that JDK of the correct version is already installed 
	
	return &configure( $errorRef );
}

sub configure
{
	my $errorRef = $_[0];
	
	# BRUNO
	
	return 1;
}





1;
