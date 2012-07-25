USE attachments;
ALTER TABLE `attachments`.`message` ADD COLUMN `msg_ack_download_html` TEXT NULL  AFTER `subject` ;

USE mxhero;
INSERT INTO `mxhero`.`system_properties` (`property_key`, `property_value`) VALUES ('documentation.url', 'http://wiki.mxhero.com:8080/display/docs/mxHero+User+Manual');
ALTER TABLE `mxhero`.`system_properties` CHANGE COLUMN `property_value` `property_value` TEXT NULL DEFAULT NULL  ;
ALTER TABLE `mxhero`.`app_users`
    ADD COLUMN `account` VARCHAR(255) NULL  AFTER `domain`, 
    ADD COLUMN `account_domain` VARCHAR(255) NULL  AFTER `account`,
    ADD CONSTRAINT `FK_app_users_email_accounts`
        FOREIGN KEY (`account` , `account_domain` )
        REFERENCES `mxhero`.`email_accounts` (`account` , `domain_id` )
        ON DELETE CASCADE
        ON UPDATE NO ACTION,
    ADD INDEX `FK_app_users_email_accounts` (`account` ASC, `account_domain` ASC);

ALTER TABLE `mxhero`.`app_users_authorities` 
    DROP FOREIGN KEY `FK_app_users_authorities_app_users` ;
    
INSERT INTO `mxhero`.`authorities` (`id`, `authority`) VALUES (3, 'ROLE_DOMAIN_ACCOUNT');

ALTER TABLE `mxhero`.`app_users_authorities` 
    ADD CONSTRAINT `FK_app_users_authorities_app_users`
        FOREIGN KEY (`app_users_id` )
        REFERENCES `mxhero`.`app_users` (`id` )
        ON DELETE CASCADE
        ON UPDATE NO ACTION;

USE statistics;
ALTER TABLE `statistics`.`mail_stats` CHANGE COLUMN `stat_key` `stat_key` VARCHAR(255) NOT NULL  ;
