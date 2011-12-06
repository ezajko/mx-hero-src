package mxHero::MySQL;

use strict;
use warnings;
no warnings qw(uninitialized);

use Term::UI;
use Term::ReadLine;

use mxHero::Config;
use mxHero::Tools;
use mxHero::Locale;

# distribution => package name
my %PKG_NAME = (
	"debian" => "mysql-server",
	"ubuntu" => "mysql-server",
	"redhat" => "mysql-server"
	# TODO: redhat, suse
);

my $DBPASS;

sub install
{
	my $errorRef = $_[0];
	
	my $distri = lc( &mxHero::Tools::getDistri() );
	
	# Install binary if needed
	if ( ! &mxHero::Tools::packageCheck( $PKG_NAME{$distri} ) ) {
		if ( ! &mxHero::Tools::packageInstall( $PKG_NAME{$distri} ) ) {
			$$errorRef = "Failed to install $PKG_NAME{$distri} package";
			return 0;
		}
	}
	
	# Check database connectivity (permissions)
	&_checkDBConnection();
	
	# TODO - check for existence of mxhero database. Query to remove.
	# Note: this should be part of the uninstall subroutine.
	if ( &_mxheroDatabaseExists ) {
		my $term = Term::ReadLine->new( 'mxHero' );
		my $bool;
		$bool = $term->ask_yn( prompt => T("Continue?"),
				default  => 'y',
				print_me => T("\nFound database 'mxhero'. Maybe from a broken installation. Will now delete database.") );
		if ( $bool ) {
			system( "/usr/bin/mysql $DBPASS -e 'drop database mxhero'" );
			system( "/usr/bin/mysql $DBPASS -e 'drop database statistics'" );
			system( "/usr/bin/mysql $DBPASS -D mysql -e \"delete from event where db = \'statistics\'\"" );
		}
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

	# Check database connectivity (permissions)
	&_checkDBConnection();
	
	# list sql files and order by version precedence
	# ONLY sql files GREATER THAN current installed mxHero version!
	myPrint "Updating database ...\n";
	my @sqlFiles;
	if ( @sqlFiles = &_versionOrderedSqlFiles( $mxHeroVersion ) ) {
		for my $file ( @sqlFiles ) {
			# NOTE: should get exit code to check for errors or use API
			system( "/usr/bin/mysql $DBPASS < $file" );
		}
	} else {
		$$errorRef = "Failed to find upgrade SQL files.";
		return 0;
	}
	
	return 1;
}

# [mysqld]
# event_scheduler = ON
sub configure
{
	my $errorRef = $_[0];
	my $cnf;
	
	my $distri = &mxHero::Tools::getDistri();
	if ( $distri =~ /Ubuntu/i || $distri =~ /Debian/i ) {
		$cnf = "/etc/mysql/my.cnf";
	} elsif ( $distri =~ /Redhat/ ) {
		$cnf = "/etc/my.cnf";
	}

	# Open /etc/mysql/my.cnf
	
	if ( ! open(F, $cnf) ) {
		$$errorRef = "Failed to open '$cnf'.";
		return 0;
	}
	
	my $line;
	my $content = "";
	my $inBlock = 0;
	my $paramSet = 0;
	
	while ( $line = <F> ) {
		if ( $line =~ /^#/ or $line =~ /^\s*$/ ) {
			$content .= $line;
			next;
		}
		
		if ( $inBlock && ! $paramSet ) {
			if ( $line =~ /^event_scheduler\s*=\s*(\w+)/ ) {
				if ( $1 eq "ON" ) {
					close F;
					return 1; # already set so discard operation
				} else {
					$content .= "event_scheduler = ON\n"; # replace line
					$paramSet = 1;
					next;
				}
			} elsif ( $line =~ /^\[\w+\]/ && ! $paramSet ) { # leaving block
				$content .= "event_scheduler = ON\n".$line; # append line to section block
				$paramSet = 1;
				next;
			}
		}
	
		if ( $line =~ /^\[(\w+)\]/ ) { # section
			if ( $1 eq "mysqld" ) {
				$inBlock = 1;
			} else {
				$inBlock = 0;
			}
			$content .= $line;
			next;
		}
	
		$content .= $line;
	}

	close F;
	
	my $backup = &mxHero::Tools::backupFile( $cnf );
	if ( ! $backup ) {
		return 0;
	}
	
	# Open for write
	if ( ! open(F, ">$cnf") ) {
		$$errorRef = "Failed to open '$cnf'.";
		return 0;
	}
	
	print F $content;
	close F;
	
	return 1;
}

sub createDatabase
{
	my $errorRef = $_[0];
	
	# Check database connectivity (permissions)
	&_checkDBConnection();
	
	# Process all sql files - from first version to last
	myPrint "\n".T("Creating database")." ...\n";
	my @sqlFiles;
	if ( @sqlFiles = &_versionOrderedSqlFiles() ) {
		for my $file ( @sqlFiles ) {
			system( "/usr/bin/mysql $DBPASS < $file" );
		}
	} else {
		$$errorRef = "Failed to find SQL files.";
		return 0;
	}
	
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
				push( @sqlFiles, $file );
			}
		}
	}
	
	my @sqlFilesSorted = sort { &mxHero::Tools::mxheroVersionCompare($a,$b) } @sqlFiles;
	# prepend file path
	for my $f (@sqlFilesSorted) {
		$f = "$myConfig{INSTALLER_PATH}/scripts/sql/$f";
	}
	
	return @sqlFilesSorted;
}

sub _mxheroDatabaseExists
{
	my $query = "\"SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'mxhero'\"";

	my $out = `/usr/bin/mysql $DBPASS -e $query`;
	chomp($out);

	if ( ! $out ) {
		return 0;
	}
	
	return 1;
}

sub _checkDBConnection
{
	my $exit = system ("/usr/bin/mysql $DBPASS -e ''");
	if ( ($exit >> 8) ) {
		myPrint T("\nFailed to connect to MySQL database\n");
		my $term = Term::ReadLine->new( 'mxHero' );
		my $reply = $term->get_reply( prompt => T("Enter the root password of your MySQL database").":" );
		chomp( $reply );
		if ( ! $reply ) {
			myPrint T("No password given").".\n";
			myPrint T("Exiting installer").".\n\n";
			exit 1;
		}
		$DBPASS = "-p$reply";
		$exit = system ("/usr/bin/mysql $DBPASS -e ''");
		if ( ($exit >> 8) ) {
			myPrint "\n".T("Failed to connect to MySQL database with password: $reply")."\n";
			myPrint T("Exiting installer").".\n\n";
			exit 1;
		}
	}
}



1;
