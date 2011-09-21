package mxHero::Config;

use strict;
use warnings;

use File::Basename;

use Config;
use Exporter;
our @ISA = qw(Exporter);
our @EXPORT = qw(%Config %myConfig);

our %myConfig;

$myConfig{INSTALLER_PATH} = dirname($0);

1;
