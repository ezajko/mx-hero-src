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
	
	my $entry1;
	my $entry2;
	if ( $bool ) {
		$entry1 = "relay_domains = mysql:/etc/postfix/mxhero/domains.sql\n";
		$entry2 = "transport_maps = mysql:/etc/postfix/mxhero/transports.sql\n";
	}
	
	# MAIN.CF
	# List of possible main.cf file locations - good for Ubuntu, Debian, Redhat.
	# TODO: suse the same?
	@postfixCfFiles = qw(/etc/postfix/main.cf /opt/zimbra/postfix/conf/main.cf);
	
	for my $file ( @postfixCfFiles ) {
		if ( -f $file ) {
			$bool = $term->ask_yn( prompt => T("Alter this main.cf for mxHero?"),
							   default  => 'y',
							   print_me => T("Found postfix main.cf at")." [$file]." );
			if ( $bool ) {
				# set master.cf path for alteration routine
				if ( ! &_alterPostfixMainCf( $file, $entry1, $entry2 ) ) {
					$$errorRef = T("Failed to alter ")."'$file'";
					return 0;
				}
				last;
			}
		}
	}
	
	if ( ! $bool ) {
		print "\n".T("Did not find a postfix 'main.cf' file.")."\n";
		my $file = $term->get_reply( prompt => T("Please enter full path to your main.cf".":"));
		if ( $file && -f $file ) {
			# set master.cf path for alteration routine
			if ( ! &_alterPostfixMainCf( $file, $entry1, $entry2 ) ) {
				$$errorRef = T("Failed to alter ")."'$file'";
				return 0;
			}
		} else {
			print T("Failed to find file")." '$file' \n";
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
END
	
	my $backup = &mxHero::Tools::backupFile( $file );
	if ( ! $backup ) {
		return 0;
	}
	
	return &_alterPostfixCf( $file, '^smtp\s+inet\s+', $mxHero );
}


# Adding (if user selected relay appliance)
#  relay_domains = mysql:/etc/postfix/mxhero/domains.sql
#  transport_maps = mysql:/etc/postfix/mxhero/transports.sql
sub _alterPostfixMainCf
{
	my $file = $_[0]; # main.cf path
	my $entry1 = $_[1];
	my $entry2 = $_[2];
	
	my $backup = &mxHero::Tools::backupFile( $file );
	return 0 if ! $backup;

	if ( ! &_alterPostfixCf( $file, '(^relay_domains\s*=.+)', $entry1 ) )
	{
		return 0;
	}
	
	if ( ! &_alterPostfixCf( $file, '(^transport_maps\s*=.+)', $entry2 ) )
	{
		return 0;
	}

	return 1;
}

# Base routine for altering postfix cf files
sub _alterPostfixCf
{
	my $file  = $_[0];
	my $regex = $_[1];
	my $entry = $_[2];
	
	if ( ! open(F, $file) ) {
		warn $!;
		return 0;
	}

	my $line;
	my $paramLine = "";
	my $content = "";
	my $inLine = 0;
	my $wroteEntry = 0;

	while ($line = <F>) {
		if ($line =~ /^\s*$/ or $line =~ /^\s*\#/) { # comment line or white space, write out
			$content .= $line;
			next;
		}

		if ( $line =~ /^\w/ ) { # begin logical line
			if ($line =~ /$regex/) { # begin entry logical line
				$inLine = 1;
				$paramLine .= $line;
				next;
			} elsif ( $inLine ) { # exiting entry logical line
				$inLine = 0;
				# place mxHero logical line
				if ( $wroteEntry ) { # must be another $entry line
					$content .= &_commentOut( $paramLine );
					$content .= $line;
					next;
				} elsif ( &_compareParamLineWithEntry( $paramLine, $entry ) == 0 ) { # the same
					close F;
					return 1; # already configured
				} else { # are different
					$content .= &_commentOut( $paramLine );
					$content .= $entry;
					$content .= $line;
					$wroteEntry = 1;
					next;
				}
			} else {
				$content .= $line;
				next;
			}
		} elsif ($line =~ /^\s+/) { # logical line continuation
			if ($inLine) {
				$paramLine .= $line;
			} else {
				$content .= $line;
			}
		}
	}

	if ( ! $wroteEntry ) { # did not find a valid line, so add to end of file
		if ( $paramLine ) {
			if ( &_compareParamLineWithEntry( $paramLine, $entry ) == 0 ) {
				close F;
				return 1;
			} else {
				$content .= &_commentOut( $paramLine );
				$content .= $entry;
				$wroteEntry = 1;			
			}
		} else {
			$content .= $entry;
		}
	}

	close F;
	
	if ( ! open(F, ">$file") ) {
		warn $!;
		return 0;
	} else {
		print F $content;
	}
	
	close F;

	return 1;
}

sub _compareParamLineWithEntry
{
	my $paramLine = $_[0]; # compare this with ...
	my $entry = $_[1]; # what it should be
	
	# need to normalize all \n and spaces
	# trim trailing returns
	$paramLine =~ s/\s*$//s;
	$entry =~ s/\s*$//s;
	
	$paramLine =~ s/\s+/ /sg;
	$entry =~ s/\s+/ /sg;
	
	if ( $paramLine eq $entry ) { # the same
		return 0;
	}
	
	return 1; # otherwise different
}

sub _commentOut
{
	my $line = shift;
	
	$line =~ s/\n+$//; # remove trailing \n
	
	$line =~ s/\n/\n\#/g;
	
	return "#$line\n";
}



1;
