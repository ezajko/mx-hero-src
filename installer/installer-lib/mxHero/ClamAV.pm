package mxHero::ClamAV;

use strict;
use warnings;
no warnings qw(uninitialized);

use Term::UI;
use Term::ReadLine;

use mxHero::Tools;
use mxHero::Config;

# distribution => package name
my %PKG_NAME = (
	"debian" => "clamav clamav-daemon",
	"ubuntu" => "clamav clamav-daemon",
	"redhat" => "clamav clamd"
	# TODO: suse
);

sub install
{
	my $errorRef = $_[0];
	
	my $distri = lc( &mxHero::Tools::getDistri() );
	
	if ( $distri eq "redhat" )
	{
		my $RPMforge;
		if ($Config{archname} =~ m/x86_64/)
		{
			$RPMforge = $myConfig{RPMFORGE_EL6_X64};
		}
		else
		{
			$RPMforge = $myConfig{RPMFORGE_EL6_X86};
		}

		if (! &mxHero::Tools::packageCheck ($RPMforge))
		{
			my $term = Term::ReadLine->new( 'mxHero' );
			my $bool;
			$bool = $term->ask_yn( prompt => T("Continue?"),
				default  => 'y',
				print_me => T("\nTo install ClamAV, we need to add RPMforge repository and update packages list.") );
		
			if ($bool)
			{
				my $output = `/bin/rpm -i $myConfig{RPMFORGE_BASE_URL}/$RPMforge.rpm`;

				if (($? >> 8) != 0)
				{
					warn "Error trying to add RPMforge repository: $output\n";
					sleep(2);
					return 1;
				}

				&mxHero::Tools::packageListUpdate();
			}
			else
			{
				warn "ClamAV was not installed. Antivirus filtering will not work.\n";
				sleep(2);
				return 1;
			}
		}
	}
	
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
	
	my %entry = ( "TCPSocket" => 6665, "TCPAddr" =>  '127.0.0.1', "MaxConnectionQueueLength" => 80,
		"MaxFileSize" => "500M", "StreamMaxLength" => "500M"
	);

	my $file;
	my $distri = lc( &mxHero::Tools::getDistri() );
	
	if ( $distri eq "redhat" )
	{
		$file = "/etc/clamd.conf";
	}
	else
	{
		$file = "/etc/clamav/clamd.conf";
	}
	
	if ( ! &mxHero::Tools::alterSimpleConfigFile( $file, \%entry ) ) {
		$$errorRef = "Failed to alter $file";
		return 0;
	}
	
	return 1;
}

## PRIVATE





1;
