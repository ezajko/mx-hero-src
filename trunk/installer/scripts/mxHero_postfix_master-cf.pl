#!/usr/bin/perl

# Alter postfix master.cf file for mxHero deployment installation
# Exit codes:
## -1: could not find master.cf
## -2: failed to write in config directory

use File::Copy;
use strict;

# Test subroutine call
&mxHeroPostfixMasterCf( "master.cf", "127.0.0.1",5555,5556);

sub mxHeroPostfixMasterCf {

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

	copy($file,$oldFile) or die "Copy failed: $!";

	open (MASTER, $oldFile) || exit -1;
	open (NEW, ">$file") || exit -2;

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

}