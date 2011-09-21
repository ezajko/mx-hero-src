package mxHero::JDK;

# TODO: Must convert to use locales

use strict;
use warnings;

use mxHero::Config;
use LWP::Simple;

my $JDK_PATH = 'jdk';
my $JDK_DOWNLOAD_PATH = 'http://download.oracle.com/otn-pub/java/jdk/6u27-b07';
my $JDK_X86_FILENAME = 'jdk-6u27-linux-i586.bin';
my $JDK_X64_FILENAME = 'jdk-6u27-linux-x64.bin';

sub download
{
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
		getstore "$JDK_DOWNLOAD_PATH/$downloadFile", "$myConfig{INSTALLER_PATH}/$JDK_PATH/$downloadFile";
	}
}

sub install
{
	return;
}

sub upgrade
{
	return;
}

sub configure
{
	return;
}

1;
