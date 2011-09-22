package mxHero::Config;

use strict;
use warnings;
no warnings qw(uninitialized);

use File::Basename;
use File::Copy;
use Term::UI;
use Term::ReadLine;

use Config;
use Exporter;
our @ISA = qw(Exporter);
our @EXPORT = qw(%Config %myConfig);

use mxHero::LinuxOS;
use mxHero::Locale;

our %myConfig;

$myConfig{INSTALLER_PATH} = dirname($0);


sub install
{
	
	return 1;
}

sub upgrade
{
	# Nothing to do here at the moment.

	return 1;
}

sub uninstall
{

	return 1;
}

## Private subs




1;
