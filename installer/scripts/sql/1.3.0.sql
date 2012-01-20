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
(5000000, 'org.mxhero.feature.bccpolicy', 'before', 'bccpolicy.description', 'bccpolicy.explain', 'bccpolicy.label', 'org/mxhero/feature/bccpolicy/Report.swf', 'org/mxhero/feature/bccpolicy/BCCPolicy.swf', 1, 1, true);

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
(100000, 'org.mxhero.feature.attachmenttrack', 'before', 'attachmenttrack.description', 'attachmenttrack.explain', 'attachmenttrack.label', 'org/mxhero/feature/attachmenttrack/Report.swf', 'org/mxhero/feature/attachmenttrack/AttachmentTrack.swf', 1, 5, true);

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
(100000, 'org.mxhero.feature.instantalias', 'before', 'instantalias.description', 'instantalias.explain', 'instantalias.label', 'org/mxhero/feature/instantalias/Report.swf', 'org/mxhero/feature/instantalias/InstantAlias.swf', 1, 5, true);

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
(6000000, 'org.mxhero.feature.bccusagedetection', 'before', 'bccusagedetection.description', 'bccusagedetection.explain', 'bccusagedetection.label', 'org/mxhero/feature/bccusagedetection/Report.swf', 'org/mxhero/feature/bccusagedetection/BCCUsageDetection.swf', 1, 3, true);

INSERT INTO `statistics`.`mail_stats_grouped_keys` (`stat_key`, `stat_value`) VALUES ('org.mxhero.feature.bccusagedetection', 'true');

INSERT INTO `statistics`.`mail_stats_grouped_keys` (`stat_key`, `stat_value`) VALUES ('org.mxhero.feature.bccpolicy', 'true');

INSERT INTO `statistics`.`mail_stats_grouped_keys` (`stat_key`, `stat_value`) VALUES ('org.mxhero.feature.attachmenttrack', 'true');

INSERT INTO `mxhero`.`system_properties` (`property_key`, `property_value`) VALUES ('preauth.key', '4e2816f16c44fab20ecdee39fb850c3b0bb54d03f1d8e073aaea376a4f407f0c');

INSERT INTO `mxhero`.`system_properties` (`property_key`, `property_value`) VALUES ('preauth.expires', '300000');


