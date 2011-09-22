package mxHero::Postfix;

use strict;
use warnings;
no warnings qw(uninitialized);

use File::Copy;
use Term::UI;
use Term::ReadLine;

use mxHero::LinuxOS;
use mxHero::Config;
use mxHero::Locale;

sub download
{
	return;
}

sub install
{
	# Don't install if postfix / Zimbra already exists.
	
	return;
}

sub upgrade
{
	return;
}

sub configure
{

	my $error = $_[0];

	# MASTER.CF
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


	return 1;
}


## PRIVATE SUBS

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
	my $wroteSmtp;

	while ($line = <MASTER>) {
		if ($line =~ /^\s*$/ or $line =~ /^\s*\#/) {
			# comment line or white space, write out
			print NEW $line;
			next;
		}

		if ($line =~ /^\w/) {
			if ($line =~ /^smtp\s+inet\s+/) { # begin smtp logical line
				$inSmtp = 1;
				# prepend line with '#'
				print NEW "# MXHERO comment out ...\n";
				print NEW '# '.$line;
			} elsif ($inSmtp) { # exiting smtp logical line
				$inSmtp = 0;
				# place mxHero smtp logical line
				print NEW $mxHero;
				$wroteSmtp = 1;
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

	if ( ! $wroteSmtp ) { # did not find a valid SMTP line, so add to end of file
		print NEW $mxHero;
	}

	close MASTER;
	close NEW;

	return 1;
}




1;
