package mxHero::Postfix;

use strict;
use warnings;
no warnings qw(uninitialized);

use File::Copy;
use Term::UI;
use Term::ReadLine;

use mxHero::Tools;
use mxHero::Config;
use mxHero::Locale;

# distribution => mysql package name
my %PKG_NAME = (
	"debian" => "postfix",
	"ubuntu" => "postfix"
	# TODO: redhat, suse
);

sub install
{
	my $errorRef = $_[0];

	if ( &mxHero::Tools::zimbraCheck() ) {
		print "Zimbra found, not installing postfix\n";
		return &configure( $errorRef );
	}

	my $distri = lc( &mxHero::Tools::getDistri() );
	
	# Install binary if needed
	if ( ! &mxHero::Tools::packageCheck( $PKG_NAME{$distri} ) ) {
		if ( ! &mxHero::Tools::packageInstall( $PKG_NAME{$distri} ) ) {
			$$errorRef = "Failed to intall $PKG_NAME{$distri} package";
			return 0;
		}
	}

	return &configure( $errorRef );
}

sub upgrade
{
	my $errorRef = $_[0];
	
	# BRUNO - maybe nothing to do here

	return &configure( $errorRef );
}

sub configure
{
	my $errorRef = $_[0];

	# MASTER.CF
	# List of possible master.cf file locations - good for Ubuntu, Debian, Redhat.
	# TODO: suse the same?
	my @postfixCfFiles = qw(/etc/postfix/master.cf /opt/zimbra/postfix/conf/master.cf);
	my $term = Term::ReadLine->new( 'mxHero' );
	my $bool;
	
	for my $file ( @postfixCfFiles ) {
		if ( -f $file ) {
			$bool = $term->ask_yn( prompt => T("Alter this master.cf for mxHero?"),
							   default  => 'y',
							   print_me => T("Found postfix master.cf at")." [$file]." );
			if ( $bool ) {
				# set master.cf path for alteration routine
				if ( ! &_alterPostfixMasterCf( $file, "127.0.0.1",5555,5556) ) {
					$$errorRef = T("Failed to alter ")."'$file'";
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
				$$errorRef = T("Failed to alter ")."'$reply'";
				return 0;
			}
		} else {
			print T("Failed to find file")." '$reply' \n";
			print T("Stopping installation")."\n";
			$$errorRef = T("Failed to find configuration file");
			return 0;
		}
	}
	
	# Check if this is to be a relay installation
	$bool = $term->ask_yn( prompt => T("Alter main.cf for mxHero as relay?"),
					   default  => 'n',
					   print_me => T("Do you plan to use this mxHero installation as a relay for another email server?") );
	
	my $entry;
	if ( $bool ) {
		$entry = "relay_domains = mysql:/etc/postfix/mxhero/domains.sql\ntransport_maps = mysql:/etc/postfix/mxhero/transports.sql\n";
	}
	
	# MAIN.CF
	# List of possible main.cf file locations - good for Ubuntu, Debian, Redhat.
	# TODO: suse the same?
	@postfixCfFiles = qw(/etc/postfix/main.cf /opt/zimbra/postfix/conf/main.cf);
	
	for my $file ( @postfixCfFiles ) {
		if ( -f $file ) {
			$bool = $term->ask_yn( prompt => T("Alter this main.cf for mxHero?"),
							   default  => 'y',
							   print_me => T("Found postfix master.cf at")." [$file]." );
			if ( $bool ) {
				# set master.cf path for alteration routine
				if ( ! &_alterPostfixMainCf( $file, $entry ) ) {
					$$errorRef = T("Failed to alter ")."'$file'";
					return 0;
				}
				last;
			}
		}
	}
	
	if ( ! $bool ) {
		print "\n".T("Did not find a postfix 'main.cf' file.")."\n";
		my $reply = $term->get_reply( prompt => T("Please enter full path to your main.cf".":"));
		if ( $reply && -f $reply ) {
			# set master.cf path for alteration routine
			if ( ! &_alterPostfixMainCf( $reply, $entry ) ) {
				$$errorRef = T("Failed to alter ")."'$reply'";
				return 0;
			}
		} else {
			print T("Failed to find file")." '$reply' \n";
			print T("Stopping installation")."\n";
			$$errorRef = T("Failed to find configuration file");
			return 0;
		}
	}

	return 1;
}


## PRIVATE SUBS

sub _alterPostfixMasterCf {

	my $file = $_[0]; # master.cf path
	my $ip = $_[1];
	my $port1 = $_[2];
	my $port2 = $_[3];

###	warn "TEST: Altering master.cf [$file, $ip, $port1, $port2]\n"; ### TESTING
###	return 1; ### TESTING
	
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
	
	my $backup = &_backupFile( $file );
	if ( ! $backup ) {
		return 0;
	}
	
	return &_alterPostfixCf( $file, $backup, '^smtp\s+inet\s+', $mxHero );
}


# Adding (if user selected relay appliance)
sub _alterPostfixMainCf
{
	my $file = $_[0]; # main.cf path
	my $entry = $_[1];
	
	my $backup = &_backupFile( $file );
	return 0 if ! $backup;

	if ( ! &_alterPostfixCf( $file, $backup, '(^relay_domains\s*=.+|^transport_maps\s*=.+)', $entry ) )
	{
		return 0;
	}

	return 1;
}

# returns backupFile on success
sub _backupFile
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

# Base routine for altering postfix cf files
sub _alterPostfixCf
{
	my $file  = $_[0];
	my $oldFile = $_[1];
	my $regex = $_[2];
	my $entry = $_[3];
	
	if ( ! open (MASTER, $oldFile) || ! open (NEW, ">$file") ) {
		warn $!;
		return 0;
	}

	my $line;
	my $inLine;
	my $wroteLine;

	while ($line = <MASTER>) {
		if ($line =~ /^\s*$/ or $line =~ /^\s*\#/) {
			# comment line or white space, write out
			print NEW $line;
			next;
		}

		if ($line =~ /^\w/) {
			if ($line =~ /$regex/) { # begin logical line
				$inLine = 1;
				# prepend line with '#'
				print NEW "# MXHERO comment out ...\n";
				print NEW '# '.$line;
			} elsif ($inLine) { # exiting logical line
				$inLine = 0;
				# place mxHero logical line
				print NEW $entry if $entry;
				$wroteLine = 1;
				print NEW $line;
			} else {
				print NEW $line;
			}
		} elsif ($line =~ /^\s+/) { # logical line continuation
			if ($inLine) {
				# prepend line with '#'
				print NEW '# '.$line;
			} else {
				print NEW $line;
			}
		}
	}

	if ( ! $wroteLine && $entry ) { # did not find a valid line, so add to end of file
		print NEW $entry;
	}

	close MASTER;
	close NEW;

	return 1;
}




1;
