package mxHero::App;

# TODO: Must convert to use locales

use strict;
use warnings;
no warnings qw(uninitialized);

use File::Copy;

use mxHero::Config;
use mxHero::Tools;
use mxHero::Locale;

sub install
{
	my $errorRef = $_[0];

	## BACKEND

	# Creating system directory
	if (!-d $myConfig{MXHERO_PATH} || !mkdir ($myConfig{MXHERO_PATH}))
	{
		$$errorRef = T("Failed to create mxhero directory");
		return 0;
	}

	# Copying all mxhero backend files (cp, dirty way?)
	print T("Installing mxHero backend files..."), "\n";
	if ((system ("cp -a $myConfig{INSTALLER_PATH}/binaries/$myConfig{MXHERO_INSTALL_VERSION}/mxhero/* $myConfig{MXHERO_PATH}")) != 0)
	{
		$$errorRef = T("Failed to copy mxhero files");
		return 0;
	}

	# Creating system user
	print T("Creating mxHero user..."), "\n";
	my $result = system ("useradd -d $myConfig{MXHERO_PATH} -s /bin/bash mxhero");
	if ($result != 0 || $result != 9) # 0 = OK, 9 = Username exists
	{
		$$errorRef = T("Failed to create mxhero user");
		return 0;
	}

	# Copying default .profile (loads PATHs and mxhero env)
	if (! copy ("$myConfig{INSTALLER_PATH}/scripts/mxhero-profile", "$myConfig{MXHERO_PATH}/.profile"))
	{
		$$errorRef = T("Failed to copy profile file");
		return 0;
	}

	# Change owner of everything to mxhero (is there a beauty way to do it in perl, recursively and easily? :)
	if ((system ("chown -R mxhero: $myConfig{MXHERO_PATH}")) != 0)
	{
		$$errorRef = T("Failed to chown mxhero files");
		return 0;
	}
	
	## FRONTEND

	print T("Installing mxHero frontend files..."), "\n";
	if ((system ("cp -a $myConfig{INSTALLER_PATH}/binaries/$myConfig{MXHERO_INSTALL_VERSION}/web/*.war $myConfig{TOMCAT_WEBAPPS_PATH}")) != 0)
	{
		$$errorRef = T("Failed to install web interface");
		return 0;
	}

	if ((system ("chmod 644 $myConfig{TOMCAT_WEBAPPS_PATH}/*.war")) != 0)
	{
		$$errorRef = T("Failed to chmod mxhero frontend files");
		return 0;
	}

	return &configure( $errorRef );
}

sub upgrade
{
	my $errorRef = $_[0];
	
	return &install ($errorRef);
}

sub configure
{	
	my $errorRef = $_[0];

	return &configureBE && &configureFE;
}

sub configureBE
{	
	my $errorRef = $_[0];

	# INIT SCRIPT
	if ( ! &_addUpdateStartupScript() ) {
		$$errorRef = T("Failed to install startup script");
		return 0;
	}

	return 1;
}

sub configureFE
{
	my $errorRef = $_[0];

	return 1;
}


## PRIVATE SUBS

# Discover what the init.d mecanism is and copy associated init file and do update-rc.d or whatever appropriate.
sub _addUpdateStartupScript
{
	my $distri = &mxHero::Tools::getDistri();
	
	if ( $distri eq "Ubuntu" || $distri eq "Debian" ) {
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
		### warn "TEST: doing update-rc.d\n"; ### TESTING
		
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

1;
