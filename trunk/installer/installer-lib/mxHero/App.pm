package mxHero::App;

# TODO: Must convert to use locales

use strict;
use warnings;

use File::Copy;
use LWP::Simple;

use mxHero::Config;
use mxHero::Tools;

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
			$error = "Failed to download $MXHERO_DOWNLOAD_PATH/$MXHERO_FILENAME\nHTTP Response: $http_response";
			return 0;
		}
	}
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
	# INIT SCRIPT
	if ( ! &_addUpdateStartupScript() ) {
		$$error = T("Failed to install startup script");
		return 0;
	}

	return 1;
}

sub configureFE
{
	return 1;
}


## PRIVATE SUBS

# Discover what the init.d mecanism is and copy associated init file and do update-rc.d or whatever appropriate.
sub _addUpdateStartupScript
{
	my $distri = &mxHero::Tools::getDistri();
	
	if ( $distri eq "Ubuntu" || $distri eq "Debian" ) {
		# TODO
		# copy init and do update-rc.d
		if ( ! copy ( "$myConfig{INSTALLER_PATH}/scripts/mxhero-init-debian_ubuntu", "/etc/init.d/mxhero" ) ) {
			warn $!;
			return 0;
		}
		
		if ( ! chmod( 0755, '/etc/init.d/mxhero' ) ) {
			warn "Failed to chmod /etc/init.d/mxhero";
			return 0;
		}
		
		system("/usr/sbin/update-rc.d mxhero defaults");
		
	} elsif ( $distri eq "Redhat" ) {
		# TODO
	} elsif ( $distri eq "Suse" ) {
		# TODO
	} else {
		# could not identify Distribution
		warn "Unknown Linux Distribution.";
		return 0;
	}

	return 1;
}



1;
