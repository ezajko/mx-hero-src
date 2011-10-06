package mxHero::Tools;

use strict;
use warnings;
no warnings qw(uninitialized);

use Term::UI;
use Term::ReadLine;
use Debian::Dpkg::Version;
use File::Copy;

use mxHero::Config;
use mxHero::Locale;

my $LINUX_DISTRIBUTION; # package variable
my $DISTRIBUTION_VERSION;

# Super simple distri check. Return (Ubuntu, Debian, Redhat, [Suse])
sub getDistri
{
	if ( $LINUX_DISTRIBUTION ) {
		return $LINUX_DISTRIBUTION;
	}

	&_setOSInfo();
	
	return $LINUX_DISTRIBUTION;
}


sub getDistriVersion
{
	if ( $DISTRIBUTION_VERSION ) {
		return $DISTRIBUTION_VERSION;
	}

	&_setOSInfo();
	
	return $DISTRIBUTION_VERSION;
}


sub zimbraCheck
{
	return 0 if ! -d "/opt/zimbra";

	return 1;
}

# Returns mxHero version number or undef if no mxHero installed.
sub mxHeroVersion
{
	my $version = &_getVersion("$myConfig{MXHERO_PATH}/VERSION");
	
	#if ( ! $version && -d "/opt/mxhero" ) { # means first version (pre-installer)
	#	$version = "1.0.0";
	#}

	return $version;
}


sub mxHeroInstallerVersion
{
	return &_getVersion("$myConfig{INSTALLER_PATH}/VERSION");
}

# Check installed package, optionally against a minimum required version
# return 1 if package meets minimum, 0 if below minimum
sub packageCheck
{
	my $package = $_[0];
	#my $minimumVersion = $_[1]; # not yet supported with multipackage

	print "\n\n---------------------------\n";
	print "Checking packages: $package\n";
	
	my $distri = &getDistri();
	
	my @packages = split( /\s+/, $package );
	
	if ( $distri =~ /Ubuntu/i || $distri =~ /Debian/i ) {
		# Should return something like: "clamav::3.0-2::install ok installed"
		my $dpkgQuery = `/usr/bin/dpkg-query -W -f='\${Package}::\${Version}::\${Status}\\\n' $package 2>&1`;
		chomp ( $dpkgQuery );
		my $pkg;
		my $installedVersion;
		my $status;
		my @queryLines = split( /\n/, $dpkgQuery );

		for my $qLine ( @queryLines ) {
			($pkg, $installedVersion, $status ) = split (/::/, $qLine, 3);
			if ( ! $status || $status !~ /\s+installed$/ ) {
				return 0;
			}
			#if ( $installedVersion ) {
			#	if ( $minimumVersion ) {
			#		my $cmp = &_checkDebianPackageVersion( $installedVersion, $minimumVersion );
			#		return $cmp == -1 ? 0 : 1;
			#	}
			#	return 1;
			#} else {
			#	return 0;
			#}
		}
	} elsif ( $distri =~ /Redhat/i ) {
		my $rpmQuery = system( "/bin/rpm -q $package" );
		if ( ( $rpmQuery >> 8 ) != 0 ) {
			return 0;
		}
	}
	
	return 1;
}

# Install package: 1 on success, 0 on failure.
sub packageInstall
{
	my $package = $_[0];
	
	print "\n\n** INSTALLING package '$package' **\n\n";

	#my $term = Term::ReadLine->new( 'mxHero' );
	#my $bool;
	#$bool = $term->ask_yn( prompt => T("Continue?"),
	#					   default  => 'y',
	#					   print_me => T("\nInstalling binary package:")." '$package'" );
	#if ( ! $bool ) {
	#	print "INSTALL CANCELLED.\n";
	#	return 0;
	#}
	my $distri = &getDistri();
	
	if ( $distri =~ /Ubuntu/i || $distri =~ /Debian/i ) {
###		warn "TEST: installing '$package'\n"; ### TESTING
###		return 1; ### TESTING
		# apt-get return 0 on success, 100 on error
		sleep( 1 );
		my $ret = system("/usr/bin/apt-get -y install $package 2>/dev/null");
		if ( ($ret >> 8) == 0 ) {
			print "'$package' ... INSTALLED\n";
			return 1;
		} else {
			return 0;
		}
	} elsif ( $distri =~ /Redhat/ ) {
		sleep( 1 );
		my $ret = system("/usr/bin/yum -y install $package 2>/dev/null");
		if ( ($ret >> 8) == 0 ) {
			print "'$package' ... INSTALLED\n";
			my $service;
			if ( $package eq "mysql-server") {
				$service = "mysqld";
			} else {
				$service = $package;
			}
			system( "/sbin/chkconfig $service on" );
			return 1;
		} else {
			return 0;
		}
	}
	
	return 0;
}


# Returns 1 if a > b, 0 if the same, -1 if b > a
sub mxheroVersionCompare
{
	my $a = $_[0];
	my $b = $_[1];
	my @a;
	my @b;

	$a =~ /(\d+)\D(\d+)\D(\d+)\D*.*/; # ex. 1.0.0RELEASE or 1.0.1-beta
	@a = ( $1, $2, $3 );

	$b =~ /(\d+)\D(\d+)\D(\d+)\D*.*/;
	@b = ( $1, $2, $3 );

	for my $i ( qw(0 1 2) ) {
		my $val;
		$val = $a[$i] <=> $b[$i];
		if ( $val ) {
				return $val;
		}
	}

	return 0;
}

# returns backupFile on success
sub backupFile
{
	my $file = $_[0];
	
	# Copy original file to .old or .old.1, .old.2 etc...
	my $oldFile = $file.".old";
	if ( -f $oldFile ) {
		my $count = 1;
		while ( -f $oldFile.".".$count ) {
			$count++;
		}

		$oldFile = $oldFile.".".$count;
	}

	if ( ! copy($file,$oldFile) ) {
		warn $!;
		return undef;
	}
	
	return $oldFile;
}

## PRIVATE

sub _getVersion
{
	my $version;
	my $versionFilePath = $_[0];
	
	if ( -f $versionFilePath ) {
		if ( ! open(F,$versionFilePath) ) {
			warn $!;
			return 0;
		}
		while ( $version = <F> ) {
			next if $version =~ /^\s*\#/ || $version =~ /^\s*$/;
			last;
		}
		close F;
		chomp($version);
	}

	return $version;
}

# Check debian package version numbers
# return -1 if $installedVersion less, 0 the same, or 1 if greater than $minimumVersion
sub _checkDebianPackageVersion
{
	my $installedVersion = $_[0];
	my $minimumVersion = $_[1];
	
	return version_compare( $installedVersion, $minimumVersion );
}


# TODO: set Distri & Version for all distris
# Discover Operating System information
sub _setOSInfo
{

	# Detect: Ubuntu
	if ( ! ($LINUX_DISTRIBUTION && $DISTRIBUTION_VERSION) && -f "/etc/lsb-release" ) {
		($LINUX_DISTRIBUTION, $DISTRIBUTION_VERSION) = &_processLsbRelease();
	}
	
	# id Debian w/ issue file. Not best since easily change. Look for better.
	if ( ! $LINUX_DISTRIBUTION && -f "/etc/issue" ) {
		if ( ! open( F, "/etc/issue" ) ) {
			warn "$!\n";
		} else {
			my $issue = <F>;
			if ( $issue =~ /^Debian/ ) {
				$LINUX_DISTRIBUTION = "Debian";
			}
			close F;
		}
	}	
	
	if ( ! $LINUX_DISTRIBUTION &&  -f "/etc/redhat-release" ) {
		$LINUX_DISTRIBUTION = "Redhat";
	}
	
	if ( ! $LINUX_DISTRIBUTION &&  -f "/etc/SuSE-release" ) {
		$LINUX_DISTRIBUTION = "Suse";
	}

	my $term = Term::ReadLine->new( 'mxHero' );

	# Add question confirming distribution (?)
	if ( $LINUX_DISTRIBUTION ) {
		my $bool = $term->ask_yn( prompt => T("Correct?"),
							   default  => 'y',
							   print_me => T("\nThis Operating System distribution is:")." '$LINUX_DISTRIBUTION'" );
		if ( ! $bool ) {
			$LINUX_DISTRIBUTION = "";
		}
	}
	
	# Add question if can't find distribution
	if ( ! $LINUX_DISTRIBUTION ) {
		my $reply = $term->get_reply( prompt => T("Selection? "),
							   choices  => [ ('Ubuntu', 'Debian', 'Redhat/Centos/Fedora', 'None of the above') ],
							   print_me => T("\nWhat Operating System distribution is installed?") );
		if ( $reply =~ /^Redhat/ ) {
			$LINUX_DISTRIBUTION = "Redhat";
		} elsif ( $reply =~ /^None/ ) {
			warn "Installation cancelled.\n";
			exit;
		} else {
			$LINUX_DISTRIBUTION = $reply;
		}
	}

}


sub _processLsbRelease
{
	my $distri;
	my $version;

	if ( ! open(F,"/etc/lsb-release") ) {
		warn $!;
	}

	while (<F>) {
		if ( $_ =~ /DISTRIB_ID\s*=\s*(\w+)/ ) {
			$distri = $1;
		} elsif ( $_ =~ /DISTRIB_RELEASE\s*=\s*(\w+)/ ) {
			$version = $1;
		}
	}

	return ($distri, $version);
}


1;
