package mxHero::Locale;

use strict;
use warnings;

use Term::ANSIColor qw(:constants);
$Term::ANSIColor::AUTORESET = 1;

use Exporter;
our @ISA = qw(Exporter);
our @EXPORT = qw(&T &myPrint);

require mxHero::Locale::en_US;

my $T = \%mxHero::Locale::en_US::T; # Default
my $LANG = (split (/\./, $ENV{LANG}))[0];

# TODO: logic to dynamically use alternative language

sub T
{
	my $message = shift;

	if (exists ($T->{$message}))
	{
		return $T->{$message};
	}
	else
	{
		return $message;
	}
}

sub myPrint
{
	my $str = shift;
	print BOLD GREEN $str;
}

1;
