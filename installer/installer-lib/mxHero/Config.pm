package mxHero::Config;

use strict;
use warnings;
no warnings qw(uninitialized);

use File::Basename;
use File::Copy;
use Term::UI;
use Term::ReadLine;
use mxHero::Locale;

use Config;
use Exporter;
our @ISA = qw(Exporter);
our @EXPORT = qw(%Config %myConfig);

our %myConfig;

### CUSTOMIZABLE

$myConfig{JDK_X86_DIRNAME} = 'jdk1.6.0_30-i586';
$myConfig{JDK_X64_DIRNAME} = 'jdk1.6.0_30-x64';
$myConfig{MXHERO_INSTALL_VERSION} = '1.4.0.RELEASE';
$myConfig{RPMFORGE_BASE_URL} = "http://packages.sw.be/rpmforge-release";
$myConfig{RPMFORGE_EL6_X86} = 'rpmforge-release-0.5.2-2.el6.rf.i686';
$myConfig{RPMFORGE_EL6_X64} = 'rpmforge-release-0.5.2-2.el6.rf.x86_64';

###

$myConfig{INSTALLER_PATH} = dirname($0);
$myConfig{MXHERO_PATH} = '/opt/mxhero';
$myConfig{TOMCAT_WEBAPPS_PATH} = '/var/lib/tomcat6/webapps';
$myConfig{MXHERO_POSTFIX_CONFIG} = $myConfig{MXHERO_PATH} . '/configuration/properties/org.mxhero.engine.plugin.postfixconnector.cfg';
$myConfig{MXHERO_HEROATTACH_CONFIG} = $myConfig{MXHERO_PATH} . '/configuration/properties/org.mxhero.engine.plugin.attachmentlink.cfg';
$myConfig{MXHERO_STATISTICS_CONFIG} = $myConfig{MXHERO_PATH} . '/configuration/properties/org.mxhero.engine.plugin.statistics.cfg';
$myConfig{CURRENT_POSTFIX_MAIN_CF} = '/etc/postfix/main.cf'; # only a default value

1;
