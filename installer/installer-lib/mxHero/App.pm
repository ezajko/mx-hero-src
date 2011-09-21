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
	my $error = ${$_[0]};

	if (-f ("$myConfig{INSTALLER_PATH}/$MXHERO_PATH/$MXHERO_FILENAME"))
	{
		print "Using mxHero file $MXHERO_FILENAME.\n";
	}
	else
	{
		print "Downloading mxHero file $MXHERO_DOWNLOAD_PATH/$MXHERO_FILENAME...\n";
		mkdir ("$myConfig{INSTALLER_PATH}/$MXHERO_PATH");
		my $http_response = getstore "$MXHERO_DOWNLOAD_PATH/$MXHERO_FILENAME", "$myConfig{INSTALLER_PATH}/$MXHERO_PATH/$MXHERO_FILENAME";
		# Evaluate response
		if ( $http_response !~ /^2\d\d/ ) {
			$error = "Failed to download $MXHERO_DOWNLOAD_PATH/$MXHERO_FILENAME\nHTTP Response: $http_response\n";
			return 0;
		}
	}
}

sub isUpgrade
{
	return 1;
}

sub install
{
	return 1;
}

sub upgrade
{
	return 1;
}

sub configureBE
{
	return 1;
}

sub configureFE
{
	return 1;
}

1;
