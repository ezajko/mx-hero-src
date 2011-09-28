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
$myConfig{MXHERO_INSTALL_VERSION} = '1.0.0.RELEASE';

###

$myConfig{INSTALLER_PATH} = dirname($0);
$myConfig{MXHERO_PATH} = '/opt/mxhero';
$myConfig{TOMCAT_WEBAPPS_PATH} = '/var/lib/tomcat6/webapps';

1;
