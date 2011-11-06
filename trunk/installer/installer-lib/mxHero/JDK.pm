package mxHero::JDK;

# TODO: Must convert to use locales

use strict;
use warnings;
no warnings qw(uninitialized);

use File::Path qw(make_path);
use File::Find;
use File::Copy;
use Cwd;

use mxHero::Config;
use mxHero::Locale;

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

	myPrint T("Installing JDK...(takes time)"), "\n";
	# my $cwd = cwd();
	# chdir "$myConfig{INSTALLER_PATH}/binaries";
	# &_progressCopy( "$dirName", "$myConfig{MXHERO_PATH}/$dirName" );
	# chdir $cwd;

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

	if ((system ("chown -R mxhero: $myConfig{MXHERO_PATH}/$dirName")) != 0)
	{
		$$errorRef = T("Failed to chown java");
		return 0;
	}

	return &configure( $errorRef );
}

sub upgrade
{
	my $errorRef = $_[0];
	
	# TODO:  BRUNO - verify that JDK of the correct version is already installed 
	
	return 1;
}

sub configure
{
	my $errorRef = $_[0];
	
	return 1;
}

# my $progressCopyDir; # destination
# sub _progressCopy
# {
	# my $dir1 = $_[0];
	# $progressCopyDir = $_[1];
	
	# my @dirs;
	# push( @dirs, $dir1 );
	# find( {'wanted' => \&wanted, 'no_chdir' => 1, bydepth => 0}, @dirs );
	# print "\nDone\n";
# }

# my $count = 0;
# sub wanted
# {
	# my $dir = $progressCopyDir;
	# if ( -d $File::Find::name ) {
		# if  ( ! -d "$dir/$File::Find::name" ) {
			# make_path( "$dir/$File::Find::name" );
		# }
	# } else { # is file
		# if ( ! -d "$dir/$File::Find::dir" ) {
			# make_path( "$dir/$File::Find::dir" );
		# }
		# if ( ! copy( $File::Find::name, "$dir/$File::Find::name" ) ) {
			# warn "Copy failed: $!\n$File::Find::name => $dir/$File::Find::name\nExiting installation\n";
			# exit 1;
		# }
		# print "\tFiles: ".$count++."\r";
	# }
# }

1;
