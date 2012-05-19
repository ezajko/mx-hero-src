USE mxhero;

CREATE TABLE `domain_adldap_properties` (
  `property_name` varchar(255) NOT NULL,
  `domain` varchar(255) NOT NULL,
  `property_key` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`property_name`,`domain`),
  KEY `FK_domain_adldap_properties_domain_adldap` (`domain`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `email_accounts_properties` (
  `account` varchar(255) NOT NULL,
  `domain_id` varchar(255) NOT NULL,
  `property_name` varchar(255) NOT NULL,
  `property_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`property_name`,`domain_id`,`account`),
  KEY `FK_email_accounts` (`account`,`domain_id`),
  KEY `FK_domain_adldap_properties_email_accounts_properties` (`property_name`,`domain_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1; 

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
(1650000, 'org.mxhero.feature.usagehours', 'before', 'usagehours.description', 'usagehours.explain', 'usagehours.label', 'org/mxhero/feature/usagehours/Report.swf', 'org/mxhero/feature/usagehours/UsageHours.swf', 1, 1, true);

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
(90000, 'org.mxhero.feature.disclaimer', 'before', 'disclaimer.description', 'disclaimer.explain', 'disclaimer.label', 'org/mxhero/feature/disclaimer/Report.swf', 'org/mxhero/feature/disclaimer/Disclaimer.swf', 1, 1, true);

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
(95000, 'org.mxhero.feature.signature', 'before', 'signature.description', 'signature.explain', 'signature.label', 'org/mxhero/feature/signature/Report.swf', 'org/mxhero/feature/signature/Signature.swf', 1, 1, true);



USE statistics;

INSERT INTO `mail_stats_grouped_keys` (`stat_key`, `stat_value`) VALUES ('org.mxhero.feature.disclaimer', '%');
INSERT INTO `mail_stats_grouped_keys` (`stat_key`, `stat_value`) VALUES ('org.mxhero.feature.signature', '%');