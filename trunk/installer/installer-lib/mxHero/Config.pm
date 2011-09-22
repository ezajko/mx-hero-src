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
	my $error = $_[0];
	
	# POSTFIX ONLY - FOR NOW
	# List of possible master.cf file locations - good for Ubuntu, Debian, Redhat.
	my @masterCfFiles = qw(/etc/postfix/master.cf /opt/zimbra/postfix/conf/master.cf);
	my $term = Term::ReadLine->new( 'mxHero' );
	my $bool;
	
	for my $file ( @masterCfFiles ) {
		if ( -f $file ) {
			$bool = $term->ask_yn( prompt => T("Alter this master.cf for mxHero?"),
							   default  => 'y',
							   print_me => T("Found postfix master.cf at")." [$file]." );
			if ( $bool ) {
				# set master.cf path for alteration routine
				last;
			}
		}
	}
	
	if ( ! $bool ) {
		print "\n".T("Did not find a postfix 'master.cf' file.")."\n";
		my $reply = $term->get_reply( prompt => T("Please enter full path to your master.cf".":"));
		if ( $reply && -f $reply ) {
			# set master.cf path for alteration routine
		} else {
			print T("Failed to find file")." '$reply' \n";
			print T("Stopping installation")."\n";
			$$error = T("Failed to find configuration file");
			return 0;
		}
	}
	
	# Add startup script.
	if ( ! &_addUpdateStartupScript() ) {
		$$error = T("Failed to install startup script");
		return 0;
	}
	
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

# Discover what the init.d mecanism is and copy associated init file and do update-rc.d or whatever appropriate.
sub _addUpdateStartupScript
{
	my $distri = &mxHero::LinuxOS::getDistri();
	
	if ( $distri eq "Ubuntu" || $distri eq "Debian" ) {
		# TODO
		# copy init and do update-rc.d
		if ( ! copy ( "mxHero-init", "/etc/init.d/mxhero" ) ) {
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
