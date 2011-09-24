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
	my $error = $_[0];

	# Process all sql files - from first version to last
	my @sqlFiles;
	if ( @sqlFiles = &_versionOrderedSqlFiles() ) {
		# TODO: execute SQL
	} else {
		$$error = "Failed to find SQL files.";
		return 0;
	}

	return 1;
}

sub upgrade
{
	my $error = $_[0];

	# TODO
	# Determine Version of mxHero installed
	my $mxHeroVersion = &mxHero::Tools::mxHeroVersion();
	# Determine Version mxHero for this installation script
	my $installerVersion = &mxHero::Tools::mxHeroInstallerVersion();
	# Sanity check
	if ( ! $mxHeroVersion || ! $installerVersion ) {
		$$error = "mxHero Version information incomplete.";
		return 0;
	}
	# list sql files and order by version precedence
	my @sqlFiles;
	if ( @sqlFiles = &_versionOrderedSqlFiles( $mxHeroVersion ) ) {
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

# optional parameter 'version' that files need to be greater than.
# returns ordered array of sql files - oldest first.
sub _versionOrderedSqlFiles
{
	my $mxHeroVersion = $_[0]; # if no parameter, will process all files in version order.
	
	my %files;
	
	if ( ! opendir(DIR,"$myConfig{INSTALLER_PATH}/scripts/sql") ) {
		warn $!;
		return undef;
	}
	
	my @files = grep ( /\d+\D+\d+\D+\d+\D*.*\.sql/, readdir( DIR ) );
	my @sqlFiles;
	
	# Only version files above current installed version
	while my $file ( @files ) {
		if ( $file =~ /(\d+\D+\d+\D+\d+)\D*.*\.sql/ ) {
			my $fileVersion = $1;
			# Only use SQL for versions newer than current installed version
			if ( ! $mxHeroVersion || &mxHero::Tools::mxheroVersionCompare($fileVersion, $mxHeroVersion) > 0 ) {
				push( @sqlFiles, $fileVersion );
			}
		}
	}
	
	my @sqlFilesSorted = sort { &mxHero::Tools::mxheroVersionCompare($a,$b) } @sqlFiles;
	
	return @sqlFilesSorted;
}







1;
