package mxHero::MySQL;

use strict;
use warnings;
no warnings qw(uninitialized);

use mxHero::Tools;

sub download
{
	return 1;
}

sub install
{
	return 1;
}

sub upgrade
{
	# TODO
	# Determine Version of mxHero installed
	my $mxheroVersion = &mxHero::Tools::mxHeroVersion();
	# Determine Version of mxHero of this installation script
	my $installerVersion = &mxHero::Tools::mxHeroInstallerVersion();
	# Sanity check
	if ( ! $mxheroVersion || ! $installerVersion ) {
		warn "mxHero Version information incomplete!";
	}
	# TODO: Step difference in version sql alter files from current to updated
	# list sql files and order by version precedence

	
	return 1;
}

sub configure
{
	return 1;
}

1;
