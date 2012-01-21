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
	if (!-d $myConfig{MXHERO_PATH} && !mkdir ($myConfig{MXHERO_PATH}))
	{
		$$errorRef = T("Failed to create mxhero directory");
		return 0;
	}

	# Copying all mxhero backend files (cp, dirty way?)
	myPrint T("Installing mxHero backend files..."), "\n";
	if ((system ("cp -a $myConfig{INSTALLER_PATH}/binaries/$myConfig{MXHERO_INSTALL_VERSION}/mxhero/* $myConfig{MXHERO_PATH}")) != 0)
	{
		$$errorRef = T("Failed to copy mxhero files");
		return 0;
	}

	# Creating system user
	myPrint T("Creating mxHero user..."), "\n";

	my $result = system ("useradd -d $myConfig{MXHERO_PATH} -s /bin/bash mxhero");
	$result = ($result >> 8);

	if ($result != 0 && $result != 9) # 0 = OK, 9 = Username exists
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
	return 0 if (! &_installNewWARs($errorRef));

	return &configure( $errorRef );
}

sub confirmUpgrade
{
	my $errorRef = $_[0];

	my $oldVersion = &mxHero::Tools::mxHeroVersion();
	my $newVersion = &mxHero::Tools::mxHeroInstallerVersion();

	if (&mxHero::Tools::mxheroVersionCompare($newVersion, $oldVersion) < 0)
	{
		$$errorRef = T("mxHero downgrade is not supported");
		return 0;
	}

	my $term = Term::ReadLine->new( 'mxHero' );
	my $bool = $term->ask_yn (prompt => T("mxHero is going to be upgraded from $oldVersion to $newVersion. Continue?"), default  => 'y');

	if (! $bool)
	{
		$$errorRef = T("mxHero application upgrade cancelled by user");
		return 0;
	}

	return 1;
}

sub upgrade
{
	my $errorRef = $_[0];

	my $oldVersion = &mxHero::Tools::mxHeroVersion();

	## BACKEND

	myPrint T("Copying new mxHero files..."), "\n";

	`/etc/init.d/mxhero stop 2>&1 > /dev/null`;
	sleep (5);

	rename ("$myConfig{MXHERO_PATH}/bundles", "$myConfig{MXHERO_PATH}/$oldVersion-bundles");
	system ("cp -a $myConfig{INSTALLER_PATH}/binaries/$myConfig{MXHERO_INSTALL_VERSION}/mxhero/bundles $myConfig{MXHERO_PATH}");

	rename ("$myConfig{MXHERO_PATH}/plugins", "$myConfig{MXHERO_PATH}/$oldVersion-plugins");
	system ("cp -a $myConfig{INSTALLER_PATH}/binaries/$myConfig{MXHERO_INSTALL_VERSION}/mxhero/plugins $myConfig{MXHERO_PATH}");

	rename ("$myConfig{MXHERO_PATH}/configuration/config.ini", "$myConfig{MXHERO_PATH}/configuration/$oldVersion-config.ini");
	system ("cp -f $myConfig{INSTALLER_PATH}/binaries/$myConfig{MXHERO_INSTALL_VERSION}/mxhero/configuration/config.ini $myConfig{MXHERO_PATH}/configuration/config.ini");

	return 0 if (!&_cascadeUpgrade ($errorRef, $oldVersion));

	if ((system ("chown -R mxhero: $myConfig{MXHERO_PATH}")) != 0)
	{
		$$errorRef = T("Failed to chown mxhero files");
		return 0;
	}

	## FRONTEND
	return 0 if (! &_installNewWARs($errorRef));

	return 1;
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

sub _cascadeUpgrade
{
	my $errorRef = $_[0];
	my $oldVersion = $_[1];

	if (&mxHero::Tools::mxheroVersionCompare($oldVersion, '1.2.0.RELEASE') <= 0)
	{
		my %entry = ( "serverName" => "MXHERO" );

		if ( ! &mxHero::Tools::alterSimpleConfigFile( $myConfig{MXHERO_STATISTICS_CONFIG}, \%entry, '=' ) ) {
			$$errorRef = T("Failed to upgrade stats config file.");
			return 0;
		}
	}

	return 1;
}

sub _installNewWARs
{
	my $errorRef = $_[0];
	
	myPrint T("Installing mxHero frontend files..."), "\n";
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

	return 1;
}

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
	} elsif ( $distri eq "Redhat" ) {
		if ( ! copy ( "$myConfig{INSTALLER_PATH}/scripts/mxhero-init-redhat", "/etc/init.d/mxhero" ) ) {
			warn $!;
			return 0;
		}
		
		if ( ! chmod( 0755, '/etc/init.d/mxhero' ) ) {
			warn "Failed to chmod /etc/init.d/mxhero";
			return 0;
		}
		
		system( "/sbin/chkconfig mxhero on" );
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
