package mxHero::MySQL;

use strict;
use warnings;
no warnings qw(uninitialized);

use mxHero::Config;
use mxHero::Tools;

# distribution => package name
my %PKG_NAME = (
	"debian" => "mysql-server",
	"ubuntu" => "mysql-server"
	# TODO: redhat, suse
);

sub install
{
	my $errorRef = $_[0];
	
	my $distri = lc( &mxHero::Tools::getDistri() );
	
	# Install binary if needed
	if ( ! &mxHero::Tools::packageCheck( $PKG_NAME{$distri} ) ) {
		if ( ! &mxHero::Tools::packageInstall( $PKG_NAME{$distri} ) ) {
			$$errorRef = "Failed to intall $PKG_NAME{$distri} package";
			return 0;
		}
	}
	
	# Process all sql files - from first version to last
	print "Updating database ...\n";
	my @sqlFiles;
	if ( @sqlFiles = &_versionOrderedSqlFiles() ) {
		for my $file ( @sqlFiles ) {
			# BRUNO: execute SQL
			warn "TEST: sql file - $file\n"; ### TESTING
		}
	} else {
		$$errorRef = "Failed to find SQL files.";
		return 0;
	}

	return &configure( $errorRef );
}

sub upgrade
{
	my $errorRef = $_[0];

	# TODO
	# Determine Version of installed mxHero
	my $mxHeroVersion = &mxHero::Tools::mxHeroVersion();

	# Sanity check
	if ( ! $mxHeroVersion ) {
		$$errorRef = "mxHero Version information incomplete.";
		return 0;
	}

	# list sql files and order by version precedence
	# ONLY sql files GREATER THAN current installed mxHero version!
	print "Updating database ...\n";
	my @sqlFiles;
	if ( @sqlFiles = &_versionOrderedSqlFiles( $mxHeroVersion ) ) {
		for my $file ( @sqlFiles ) {
			# BRUNO: execute SQL
			warn "TEST: sql file - $file\n"; ### TESTING
		}
	} else {
		$$errorRef = "Failed to find SQL files.";
		return 0;
	}
	
	return &configure( $errorRef );
}

sub configure
{
	my $errorRef = $_[0];

	# BRUNO
	
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
	for my $file ( @files ) {
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
