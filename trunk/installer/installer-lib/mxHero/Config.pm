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
				if ( ! &_alterPostfixMasterCf( $file, "127.0.0.1",5555,5556) ) {
					$$error = T("Failed to alter ")."'$file'";
					return 0;
				}
				last;
			}
		}
	}
	
	if ( ! $bool ) {
		print "\n".T("Did not find a postfix 'master.cf' file.")."\n";
		my $reply = $term->get_reply( prompt => T("Please enter full path to your master.cf".":"));
		if ( $reply && -f $reply ) {
			# set master.cf path for alteration routine
			if ( ! &_alterPostfixMasterCf( $reply, "127.0.0.1",5555,5556) ) {
				$$error = T("Failed to alter ")."'$reply'";
				return 0;
			}
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

sub _alterPostfixMasterCf {

	my $file = $_[0];
	my $ip = $_[1];
	my $port1 = $_[2];
	my $port2 = $_[3];

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
		return 0;
	}

	if ( ! open (MASTER, $oldFile) || ! open (NEW, ">$file") ) {
		warn $!;
		return 0;
	}

	my $mxHero = <<END;
# MXHERO ENTRY - START
smtp      inet  n       -       -       -       -       smtpd
	-o smtpd_proxy_filter=$ip:$port1
	$ip:$port2 inet     n       -       n       -       -       smtpd
	-o smtpd_authorized_xforward_hosts=$ip/24
	-o smtpd_client_restrictions=
	-o smtpd_helo_restrictions=
	-o smtpd_sender_restrictions=
	-o smtpd_recipient_restrictions=permit_mynetworks,reject
	-o smtpd_data_restrictions=
	-o mynetworks=$ip/24
	-o receive_override_options=no_unknown_recipient_checks
# MXHERO ENTRY - END
END

	my $line;
	my $inSmtp;
	my $foundSmtp;

	while ($line = <MASTER>) {
		if ($line =~ /^\s*$/ or $line =~ /^\s*\#/) {
			# comment line or white space, write out
			print NEW $line;
			next;
		}

		if ($line =~ /^\w/) {
			if ($line =~ /^smtp\s+inet\s+/) { # begin smtp logical line
				$foundSmtp = 1;
				$inSmtp = 1;
				# prepend line with '#'
				print NEW "# MXHERO comment out ...\n";
				print NEW '# '.$line;
			} elsif ($inSmtp) { # exiting smtp logical line
				$inSmtp = 0;
				# place mxHero smtp logical line
				print NEW $mxHero;
				print NEW $line;
			} else {
				print NEW $line;
			}
		} elsif ($line =~ /^\s+/) { # logical line continuation
			if ($inSmtp) {
				# prepend line with '#'
				print NEW '# '.$line;
			} else {
				print NEW $line;
			}
		}
	}

	if ( ! $foundSmtp ) { # did not find a valid SMTP line, so add to end of file
		print NEW $mxHero;
	}

	close MASTER;
	close NEW;

	return 1;
}



1;
