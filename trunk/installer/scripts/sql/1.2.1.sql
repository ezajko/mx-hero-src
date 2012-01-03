ALTER TABLE `statistics`.`mail_records` DROP COLUMN `sent_date` , DROP COLUMN `received_date` , CHANGE COLUMN `bcc_recipeints` `bcc_recipients` LONGTEXT NULL DEFAULT NULL  , CHANGE COLUMN `cc_recipeints` `cc_recipients` LONGTEXT NULL DEFAULT NULL  , CHANGE COLUMN `from_recipeints` `from_recipients` VARCHAR(255) NULL DEFAULT NULL  , CHANGE COLUMN `ng_recipeints` `ng_recipients` LONGTEXT NULL DEFAULT NULL  , CHANGE COLUMN `to_recipeints` `to_recipients` LONGTEXT NULL DEFAULT NULL, ADD COLUMN `sender_group` VARCHAR(255) NULL  AFTER `flow` , ADD COLUMN `recipient_group` VARCHAR(255) NULL  AFTER `sender_group`;

ALTER TABLE `statistics`.`mail_records` ADD COLUMN `server_name` VARCHAR(255) NOT NULL DEFAULT 'MXHERO'  AFTER `record_sequence` , DROP PRIMARY KEY , ADD PRIMARY KEY (`insert_date`, `record_sequence`, `server_name`) ;

ALTER TABLE `statistics`.`mail_stats` ADD COLUMN `server_name` VARCHAR(255) NOT NULL DEFAULT 'MXHERO'  AFTER `record_sequence` , DROP PRIMARY KEY , ADD PRIMARY KEY (`stat_key`, `insert_date`, `record_sequence`, `server_name`, `phase`) ;


ALTER TABLE `mxhero`.`account_aliases` DROP INDEX `FK80BE02ECB52FDC6` , DROP INDEX `FK80BE02EC3ABFC16F` ;
ALTER TABLE `mxhero`.`account_aliases` ADD CONSTRAINT `FK_account_aliases_domain` FOREIGN KEY (`domain_id` ) REFERENCES `mxhero`.`domain` (`domain` ) ON DELETE NO ACTION ON UPDATE NO ACTION , ADD INDEX `FK_account_aliases_domain` (`domain_id` ASC) ;
ALTER TABLE `mxhero`.`account_aliases` ADD CONSTRAINT `FK_account_aliases_domains_aliases` FOREIGN KEY (`domain_id` , `domain_alias` ) REFERENCES `mxhero`.`domains_aliases` (`domain` , `alias` ) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `FK_account_aliases_domains_aliases` (`domain_id` ASC, `domain_alias` ASC) ;
ALTER TABLE `mxhero`.`account_aliases` ADD CONSTRAINT `FK_account_aliases_email_accounts` FOREIGN KEY (`account` , `domain_id` ) REFERENCES `mxhero`.`email_accounts` (`account` , `domain_id` ) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `FK_account_aliases_email_accounts` (`account` ASC, `domain_id` ASC) ;

ALTER TABLE `mxhero`.`app_users` DROP INDEX `FK6DEE8E6AA9DC1BA3`;
ALTER TABLE `mxhero`.`app_users` ADD CONSTRAINT `FK_app_users_domain` FOREIGN KEY (`domain` ) REFERENCES `mxhero`.`domain` (`domain` ) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `FK_app_users_domain` (`domain` ASC) ;

ALTER TABLE `mxhero`.`app_users_authorities` DROP INDEX `FK6EC5B0CC7C644C90` , DROP INDEX `FK6EC5B0CC9EA31A41`;
ALTER TABLE `mxhero`.`app_users_authorities` ADD CONSTRAINT `FK_app_users_authorities_app_users` FOREIGN KEY (`app_users_id` ) REFERENCES `mxhero`.`app_users` (`id` ) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `FK_app_users_authorities_app_users` (`app_users_id` ASC) ;
ALTER TABLE `mxhero`.`app_users_authorities` ADD CONSTRAINT `FK_app_users_authorities_authorities` FOREIGN KEY (`authorities_id` ) REFERENCES `mxhero`.`authorities` (`id` ) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `FK_app_users_authorities_authorities` (`authorities_id` ASC) ;

ALTER TABLE `mxhero`.`domain_adldap` DROP INDEX `FK35E5E225A9DC1BA3` ;
ALTER TABLE `mxhero`.`domain_adldap` ADD CONSTRAINT `FK_domain_adldap_domain` FOREIGN KEY (`domain` ) REFERENCES `mxhero`.`domain` (`domain` ) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `FK_domain_adldap_domain` (`domain` ASC) ;

ALTER TABLE `mxhero`.`domains_aliases` ADD CONSTRAINT `FK_domains_aliases_domin` FOREIGN KEY (`domain` ) REFERENCES `mxhero`.`domain` (`domain` ) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `FK_domains_aliases_domin` (`domain` ASC) , DROP INDEX `FK7B201AEEA9DC1BA3` ;

ALTER TABLE `mxhero`.`email_accounts` DROP INDEX `FK3B65F12986881E05` , DROP INDEX `FK3B65F1291FCE7D55` ;
ALTER TABLE `mxhero`.`email_accounts` ADD CONSTRAINT `FK_email_accounts_domain` FOREIGN KEY (`domain_id` ) REFERENCES `mxhero`.`domain` (`domain` ) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `FK_email_accounts_domain` (`domain_id` ASC) ;
ALTER TABLE `mxhero`.`email_accounts` ADD CONSTRAINT `FK_email_accounts_groups` FOREIGN KEY (`domain_id` , `group_name` ) REFERENCES `mxhero`.`groups` (`domain_id` , `name` ) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `FK_email_accounts_groups` (`domain_id` ASC, `group_name` ASC) ;

ALTER TABLE `mxhero`.`features` DROP INDEX `FKEEACE43D7B4BC055` ;
ALTER TABLE `mxhero`.`features` ADD CONSTRAINT `FK_features_features_categories` FOREIGN KEY (`category_id` ) REFERENCES `mxhero`.`features_categories` (`id` ) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `FK_features_features_categories` (`category_id` ASC) ;

ALTER TABLE `mxhero`.`features_rules` DROP INDEX `FK8DE7AE751FCE7D55` , DROP INDEX `FK8DE7AE757340D73F` , DROP INDEX `FK8DE7AE75F88AE3B8` , DROP INDEX `FK8DE7AE75E6EC4887` ;
ALTER TABLE `mxhero`.`features_rules` ADD CONSTRAINT `FK_features_rule_features` FOREIGN KEY (`feature_id` ) REFERENCES `mxhero`.`features` (`id` ) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `FK_features_rule_features` (`feature_id` ASC) ;
ALTER TABLE `mxhero`.`features_rules` ADD CONSTRAINT `FK_features_rules_domain` FOREIGN KEY (`domain_id` ) REFERENCES `mxhero`.`domain` (`domain` ) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `FK_features_rules_domain` (`domain_id` ASC) ;

ALTER TABLE `mxhero`.`features_rules_directions` DROP INDEX `FK1C8F64DE8CEB6B15` ;
ALTER TABLE `mxhero`.`features_rules_directions` ADD CONSTRAINT `FK_features_rules_directions_features_rules` FOREIGN KEY (`rule_id` ) REFERENCES `mxhero`.`features_rules` (`id` ) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `FK_features_rules_directions_features_rules` (`rule_id` ASC) ;

ALTER TABLE `mxhero`.`features_rules_properties` DROP INDEX `FKD7FC093D8CEB6B15` ;
ALTER TABLE `mxhero`.`features_rules_properties` ADD CONSTRAINT `FK_features_rules_properties_features_rules` FOREIGN KEY (`rule_id` ) REFERENCES `mxhero`.`features_rules` (`id` ) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `FK_features_rules_properties_features_rules` (`rule_id` ASC) ;

ALTER TABLE `mxhero`.`groups` DROP INDEX `FKB63DD9D41FCE7D55` ;
ALTER TABLE `mxhero`.`groups` ADD CONSTRAINT `FK_groups_domain` FOREIGN KEY (`domain_id` ) REFERENCES `mxhero`.`domain` (`domain` ) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `FK_groups_domain` (`domain_id` ASC) ;





