package mxHero::JDK;

# TODO: Must convert to use locales

use strict;
use warnings;
no warnings qw(uninitialized);

use mxHero::Config;
use mxhero::Locale;

sub install
{
	my $errorRef = $_[0];

	my $dirName;

	if ($Config{archname} =~ m/x86_64/)
	{
		$dirName = $myConfig{JDK_X64_DIRNAME};
	}
	else
	{
		$dirName = $myConfig{JDK_X86_DIRNAME};
	}

	if ((system ("cp -a $myConfig{INSTALLER_PATH}/binaries/$dirName $myConfig{MXHERO_PATH}/$dirName")) != 0)
	{
		$$errorRef = T("Failed to copy java files");
                return 0;
	}

	if (! symlink ("$myConfig{MXHERO_PATH}/$dirName", "$myConfig{MXHERO_PATH}/java"))
	{
		$$errorRef = T("Failed to symlink java dir");
                return 0;
	}

	if ((system ("chown -R root: $myConfig{MXHERO_PATH}/$dirName")) != 0)
	{
		$$errorRef = T("Failed to chown java");
		return 0;
	}

	return &configure( $errorRef );
}

sub upgrade
{
	my $errorRef = $_[0];
	
	# TODO:  verify that JDK of the correct version is already installed 
	
	return &configure( $errorRef );
}

sub configure
{
	my $errorRef = $_[0];
	
	return 1;
}

1;
