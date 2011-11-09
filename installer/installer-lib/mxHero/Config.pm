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

$myConfig{JDK_X86_DIRNAME} = 'jdk1.6.0_27-i586';
$myConfig{JDK_X64_DIRNAME} = 'jdk1.6.0_27-x64';
$myConfig{MXHERO_INSTALL_VERSION} = '1.1.0.RC1';
$myConfig{RPMFORGE_BASE_URL} = "http://packages.sw.be/rpmforge-release";
$myConfig{RPMFORGE_EL6_X86} = 'rpmforge-release-0.5.2-2.el6.rf.i686';
$myConfig{RPMFORGE_EL6_X64} = 'rpmforge-release-0.5.2-2.el6.rf.x86_64';

###

$myConfig{INSTALLER_PATH} = dirname($0);
$myConfig{MXHERO_PATH} = '/opt/mxhero';
$myConfig{TOMCAT_WEBAPPS_PATH} = '/var/lib/tomcat6/webapps';

1;
