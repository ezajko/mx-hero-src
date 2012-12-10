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

	# XXX: hack: some old packages with 1.2.0.RELEASE was released as 1.1.0.XYZ
	# We have no upgrades before 1.2.0.RELEASE
	$version = '1.2.0.RELEASE' if ($version =~ /^1\.1\.0\./);

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

	myPrint "\n\n---------------------------\n";
	myPrint "Checking packages: $package\n";
	
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
	
	myPrint "\n\n** INSTALLING package '$package' **\n\n";
	sleep( 2 );

	my $distri = &getDistri();
	
	if ( $distri =~ /Ubuntu/i || $distri =~ /Debian/i ) {
		# apt-get return 0 on success, 100 on error
		my $ret = system("/usr/bin/apt-get -y install $package 2>/dev/null");
		if ( ($ret >> 8) == 0 ) {
			myPrint "'$package' ... INSTALLED\n";
			return 1;
		} else {
			return 0;
		}
	} elsif ( $distri =~ /Redhat/ ) {
		my $ret = system("/usr/bin/yum -y install $package");
		if ( ($ret >> 8) == 0 ) {
			myPrint "'$package' ... INSTALLED\n";
			my $service;
			if ( $package =~ /mysql-server/) { # TODO: fix this
				$service = "mysqld";
			} elsif ($package eq "clamav clamd") { # TODO: fix this
				$service = "clamd";
			} else {
				$service = $package;
			}
			system( "/sbin/chkconfig $service on" );
			$ret = system( "/etc/init.d/$service start" );
			if ( ($ret >> 8) == 0 ) {
				return 1;
			} else {
				warn "Failed to start $service. Please try to start it later...\n";
			}
		} else {
			return 0;
		}
	}
	
	return 0;
}

sub packageListUpdate
{
	my $term = Term::ReadLine->new( 'mxHero' );
	
	my $bool = $term->ask_yn( prompt => T("Update? "),
				   default  => 'y',
				   print_me => T("\nWill update your package database before installation.") );
	
	return if ! $bool;
	
	my $distri = &getDistri();

	if ( $distri =~ /Ubuntu/i || $distri =~ /Debian/i ) {
		my $ret = system("/usr/bin/apt-get update 2>/dev/null");
		if ( ($ret >> 8) == 0 ) {
			myPrint "Package database updated\n";
			return 1;
		} else {
			return 0;
		}
	} elsif ( $distri =~ /Redhat/i ) {
		my $ret = system("/usr/bin/yum update");
		if ( ($ret >> 8) == 0 ) {
			myPrint "Package database updated\n";
			return 1;
		} else {
			return 0;
		}
	}
	
	return 1;
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

# Check if system is on UTC timezone
sub isUTC
{
	my $date = `/bin/date`;
	chomp( $date );
	if ( $date !~ /UTC/ ) {
		return 0;
	}
	
	return 1;
}

# Set system to UTC
sub setUTC
{
	my $term = Term::ReadLine->new( 'mxHero' );
	
	my $bool = $term->ask_yn( prompt => T("Set to UTC and continue installation? "),
				default  => 'y',
				print_me => T("\nmxHero realtime statistics require a UTC system timezone.") );
	
	if ( ! $bool ) {
		myPrint "Quitting installation.\n";
		exit;
	}
	
	my $distri = &getDistri();

	if ( $distri =~ /Ubuntu/i || $distri =~ /Debian/i ) {
		if ( ! copy( "/usr/share/zoneinfo/UTC", "/etc/localtime" ) ) {
			warn "Failed to copy UTC zonefile\n$!";
			exit;
		}
		if ( ! open( TZ, ">/etc/timezone" ) ) {
			warn "Failed to open timezone file.\n$!";
			exit;
		}
		print TZ "Etc/UTC\n";
		close TZ;
	} elsif ( $distri =~ /Redhat/i ) {
		if ( ! copy( "/usr/share/zoneinfo/UTC", "/etc/localtime" ) ) {
			warn "Failed to copy UTC zonefile\n$!";
			exit;
		}
		if ( ! open( TZ, ">/etc/sysconfig/clock" ) ) {
			warn "Failed to open timezone file.\n$!";
			exit;
		}
		print TZ "ZONE=\"UTC\"\n";
		close TZ;
	}
}

# To allow for expanded port access
#  disabling iptables and putting selinux into permissive mode
sub adjustRedhat
{
	my $distri = &getDistri();
	
	if ( $distri !~ /Redhat/i ) {
		return;
	}
	
	# Turn off iptables
	if ( -f "/etc/init.d/iptables" ) {
		system( "/etc/init.d/iptables stop" );
		system( "/sbin/chkconfig --del iptables" );
	}
	
	# Set selinux to permissive
	if ( ! &alterSimpleConfigFile( "/etc/sysconfig/selinux", { 'SELINUX' => 'disabled' }, '=' ) ) {
		warn "Failed to disable SELINUX. Please check manually if it exists and if yes, it is disabled.\n";
		exit;
	}
}

# alter basic configuration files. Type with key = value.
#  returns 0 on failure, 1 on success
sub alterSimpleConfigFile
{
	my $file = $_[0];
	my $hashRef = $_[1];
	my $delim = $_[2];
	
	if ( ! $delim ) {
		$delim = ' '; # default to space 
	}

	if ( ! open(F, $file) ) {
		warn $!;
		return 0;
	}
	
	my $line;
	my $content = "";
	my $changed;
	
	my %entry = %$hashRef;
	
	my %found;
	for my $k ( keys %entry ) {
		$found{ $k } = 0;
	}
	
	while ( $line = <F> ) {
		if ($line =~ /^\s*$/ or $line =~ /^\s*\#/) { # comment line or white space, write out
			$content .= $line;
			next;
		}
		
		my ($key, $value) = split ( /\s*$delim\s*/, $line, 2);
		chomp( $value );
		
		my $setContent;
		for my $k ( keys %entry ) {
			if ( $key eq $k ) {
				$setContent = 1;
				if ( $value eq $entry{ $k } ) { # same value
					$content .= $line; # keep original
				} else { # different value
					# could comment out original line here
					$content .= "$k".$delim."$entry{$k}\n"; # set new
					$changed = 1;
				}
				$found{$k} = 1;
				last;
			}
		}
		
		$content .= $line if ! $setContent;
	}
	
	for my $k ( keys %found ) {
		if ( ! $found{$k} ) {
			$content .= "$k".$delim."$entry{$k}\n";
			$changed = 1;
		}
	}
	close F;
	
	if ( ! $changed ) {
		return 1;
	}
	
	my $backup = &mxHero::Tools::backupFile( $file );
	return 0 if ! $backup;
	
	if ( ! open(F, ">$file") ) {
		warn $!;
		return 0;
	} else {
		print F $content;
	}
	
	close F;
	
	return 1;
}

sub loadProperties
{
	my $path = $_[0];
	my $properties = $_[1];

	#my $hasEntries = scalar keys %{$properties};

	opendir (P, $path);

	for my $file (readdir (P))
	{
		next unless (-f $path . '/' . $file);
		#next if ($hasEntries && !exists ($properties->{$file})); # we don't want old config files (but gsync etc should exists)

		open (F, $path . '/' . $file);

		while (my $line = <F>)
		{
			chomp ($line);
			next if ($line =~ /^#/ || $line =~ /^\s*$/);
			
			$line =~ s/[\r\n]//g;
			$line =~ /(\S+)\s*\=\s*(\S?.*)$/i;
			$properties->{$file}->{$1} = $2;			
		}

		close (F);
	}

	closedir (P);
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
		while ( <F> ) {
			next if $_ =~ /^\s*\#/ || $_ =~ /^\s*$/m;
			$_ =~ /[\w\.]+/m;
			$version = $&;
			last;
		}
		close F;
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
		} elsif ( $_ =~ /DISTRIB_RELEASE\s*=\s*(\S+)/ ) {
			$version = $1;
		}
	}

	return ($distri, $version);
}


1;
