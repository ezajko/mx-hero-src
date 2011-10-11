package mxHero::SpamAssassin;

use strict;
use warnings;

use mxHero::Tools;

# distribution => package name
my %PKG_NAME = (
	"debian" => "spamassassin",
	"ubuntu" => "spamassassin",
	"redhat" => "spamassassin"
	# TODO: redhat, suse
);

sub install
{
	my $errorRef = $_[0];
	
	my $distri = lc( &mxHero::Tools::getDistri() );
	
	# in future add dialogue for external SpamAssassin (ex. get IP and Port)

	# Install binary if needed
	if ( ! &mxHero::Tools::packageCheck( $PKG_NAME{$distri} ) ) {
		if ( ! &mxHero::Tools::packageInstall( $PKG_NAME{$distri} ) ) {
			$$errorRef = "Failed to install $PKG_NAME{$distri} package";
			return 0;
		}
	}
	
	return &configure( $errorRef );
}

sub upgrade
{
	my $errorRef = $_[0];
	
	return 1;
}

sub configure
{
	my $errorRef = $_[0];
	
	my $distri = &mxHero::Tools::getDistri();
	if ( $distri =~ /Redhat/ ) { # TODO: check if anything to do in the case of Redhat?
		return 1;
	}

	my $file = "/etc/default/spamassassin";
	
	if ( ! open(F, $file) ) {
		warn $!;
		return 0;
	}
	
	my $line;
	my $content = "";
	my $changed;
	
	my %entry = ( "ENABLED" => 1, "CRON" =>  1);
	
	my %found;
	for my $k ( keys %entry ) {
		$found{ $k } = 0;
	}
	
	while ( $line = <F> ) {
		if ($line =~ /^\s*$/ or $line =~ /^\s*\#/) { # comment line or white space, write out
			$content .= $line;
			next;
		}
		
		my ($key, $value) = split ( /\s*=\s*/, $line, 2);
		chomp( $value );
		
		my $setContent;
		for my $k ( keys %entry ) {
			if ( $key eq $k ) {
				$setContent = 1;
				if ( $value eq $entry{ $k } ) { # same value
					$content .= $line; # keep original
				} else { # different value
					# could comment out original line here
					$content .= "$k=$entry{$k}\n"; # set new
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
			$content .= "$k=$entry{$k}\n";
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

## PRIVATE






1;
