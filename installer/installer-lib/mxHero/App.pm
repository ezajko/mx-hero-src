package mxHero::App;

# TODO: Must convert to use locales

use strict;
use warnings;

use mxHero::Config;
use LWP::Simple;

my $MXHERO_PATH = 'app';
my $MXHERO_DOWNLOAD_PATH = 'http://www.mxhero.com/deploy';
my $MXHERO_FILENAME = '1.0.0.RELEASE.zip';

sub download
{
	if (-f ("$myConfig{INSTALLER_PATH}/$MXHERO_PATH/$MXHERO_FILENAME"))
	{
		print "Using mxHero file $MXHERO_FILENAME.\n";
	}
	else
	{
		print "Downloading mxHero file $MXHERO_DOWNLOAD_PATH/$MXHERO_FILENAME...\n";
		mkdir ("$myConfig{INSTALLER_PATH}/$MXHERO_PATH");
		getstore "$MXHERO_DOWNLOAD_PATH/$MXHERO_FILENAME", "$myConfig{INSTALLER_PATH}/$MXHERO_PATH/$MXHERO_FILENAME";
	}
}

sub isUpgrade
{
	return 0;
}

sub install
{
	return;
}

sub upgrade
{
	return;
}

sub configureBE
{
	return;
}

sub configureFE
{
	return;
}

1;
