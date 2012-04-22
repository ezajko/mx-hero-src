USE `mxhero`;
INSERT INTO `mxhero`.`features`
(
`base_priority`,
`component`,
`default_admin_order`,
`description_key`,
`explain_key`,
`label_key`,
`module_report_url`,
`module_url`,
`version`,
`category_id`,
`enabled`)
VALUES
(900000, 'org.mxhero.feature.addressprotection', 'before', 'address.protection.description', 'address.protection.explain', 'address.protection.label', 'org/mxhero/feature/addressprotection/Report.swf', 'org/mxhero/feature/addressprotection/AddressProtection.swf', 1, 1, true);

INSERT INTO `mxhero`.`features`
(
`base_priority`,
`component`,
`default_admin_order`,
`description_key`,
`explain_key`,
`label_key`,
`module_report_url`,
`module_url`,
`version`,
`category_id`,
`enabled`)
VALUES
(1200000, 'org.mxhero.feature.enhancedbcc', 'before', 'enhancedbcc.description', 'enhancedbcc.explain', 'enhancedbcc.label', 'org/mxhero/feature/enhancedbcc/Report.swf', 'org/mxhero/feature/enhancedbcc/EnhancedBcc.swf', 1, 5, true);

update features set  default_admin_order='before', base_priority=2000000 where component ='org.mxhero.feature.emailsizelimiter';
update features set  default_admin_order='before', base_priority=1900000 where component ='org.mxhero.feature.blocklist';
update features set  default_admin_order='before', base_priority=1800000 where component ='org.mxhero.feature.attachmentblock';
update features set  default_admin_order='before', base_priority=1700000 where component ='org.mxhero.feature.limituserdestinations';
update features set  default_admin_order='before', base_priority=1600000 where component ='org.mxhero.feature.restricteddelivery';
update features set  default_admin_order='before', base_priority=1500000 where component ='org.mxhero.feature.clamav';
update features set  default_admin_order='before', base_priority=1400000 where component ='org.mxhero.feature.externalantispam';
update features set  default_admin_order='before', base_priority=1300000 where component ='org.mxhero.feature.spamassassin';
update features set  default_admin_order='before', base_priority=1200000 where component ='org.mxhero.feature.enhancedbcc';
update features set  default_admin_order='before', base_priority=1100000 where component ='org.mxhero.feature.bccusagedetection';
update features set  default_admin_order='before', base_priority=1000000 where component ='org.mxhero.feature.bccpolicy';
update features set  default_admin_order='before', base_priority=900000 where component ='org.mxhero.feature.addressprotection';
update features set  default_admin_order='before', base_priority=800000 where component ='org.mxhero.feature.wiretapcontent';
update features set  default_admin_order='before', base_priority=700000 where component ='org.mxhero.feature.wiretapsenderreceiver';
update features set  default_admin_order='after', base_priority=600000 where component ='org.mxhero.feature.replytimeout';
update features set  default_admin_order='after', base_priority=500000 where component ='org.mxhero.feature.attachmenttrack';
update features set  default_admin_order='after', base_priority=400000 where component ='org.mxhero.feature.attachmentlink';
update features set  default_admin_order='after', base_priority=300000 where component ='org.mxhero.feature.instantalias';
update features set  default_admin_order='before', base_priority=200000 where component ='org.mxhero.feature.redirect';
update features set  default_admin_order='before', base_priority=100000 where component ='org.mxhero.feature.backupcopy';

CREATE TABLE `quarantine` (
  `domain_id` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`domain_id`),
  KEY `FK_DOMAIN` (`domain_id`),
  CONSTRAINT `FK_DOMAIN` FOREIGN KEY (`domain_id`) REFERENCES `domain` (`domain`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

USE `statistics`;

INSERT INTO `statistics`.`mail_stats_grouped_keys` (`stat_key`, `stat_value`) VALUES ('org.mxhero.feature.addressprotection', '%');
INSERT INTO `statistics`.`mail_stats_grouped_keys` (`stat_key`, `stat_value`) VALUES ('org.mxhero.feature.enhancedbcc', '%');
