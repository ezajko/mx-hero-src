package mxHero::MySQL;

use strict;
use warnings;
no warnings qw(uninitialized);

use mxHero::Config;
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
	my $error = $_[0];

	# TODO
	# Determine Version of mxHero installed
	my $mxheroVersion = &mxHero::Tools::mxHeroVersion();
	# Determine Version of mxHero of this installation script
	my $installerVersion = &mxHero::Tools::mxHeroInstallerVersion();
	# Sanity check
	if ( ! $mxheroVersion || ! $installerVersion ) {
		$$error = "mxHero Version information incomplete.";
		return 0;
	}
	# list sql files and order by version precedence
	my @sqlFiles;
	if ( @sqlFiles = &_versionOrderedSqlFiles( $mxheroVersion ) ) {
		# TODO: execute SQL
	} else {
		$$error = "Failed to find SQL files.";
		return 0;
	}
	
	return 1;
}

sub configure
{
	return 1;
}


## PRIVATE

# returns ordered array of sql files - oldest first.
# NOTE: IMPORTANT THAT VERSION NOT EXCEDE TWO DIGITS PER POINT LOCATION - EX. 99.99.99
sub _versionOrderedSqlFiles
{
	my $mxheroVersion = $_[0];
	
	my %files;
	
	if ( ! opendir(DIR,"$myConfig{INSTALLER_PATH}/scripts/sql") ) {
		warn $!;
		return undef;
	}
	
	my @files = grep ( /\d+\-\d+\-\d+\D*.*\.sql/, readdir( DIR ) );
	
	# Score installed version
	my $mxHeroVersionScore;
	if ( $mxheroVersion ) {
		if ( $mxheroVersion =~ /^(\d+)\.(\d+)\.(\d+)\D*.*/ ) {
			$score = ($1 * 10000) + ($2 * 100) + $3;
		} else {
			warn "mxHero version incorrect format: $mxheroVersion";
			return 0;
		}
	}
	
	# Score file versions
	while my $file ( @files ) {
		my $score;
		if ( $file =~ /(\d+)\-(\d+)\-(\d+)\D*.*\.sql/ ) {
			$score = ($1 * 10000) + ($2 * 100) + $3;
			# Only SQL after the current installed version
			if ( $score > $mxHeroVersionScore ) {
				${$file} = $score;
			}
		}
	}
	
	my @sqlFiles = sort { $files{$a} <=> $files{$b} } keys %files;
	
	return @sqlFiles;
}






1;
