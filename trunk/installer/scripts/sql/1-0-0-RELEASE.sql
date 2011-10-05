-- MySQL dump 10.13  Distrib 5.1.49, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: 
-- ------------------------------------------------------
-- Server version	5.1.49-1ubuntu8

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `mxhero`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `mxhero` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `mxhero`;

--
-- Table structure for table `account_aliases`
--

DROP TABLE IF EXISTS `account_aliases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_aliases` (
  `account_alias` varchar(255) NOT NULL DEFAULT '',
  `domain_alias` varchar(255) NOT NULL DEFAULT '',
  `created` datetime NOT NULL,
  `data_source` varchar(255) NOT NULL,
  `account` varchar(100) NOT NULL,
  `domain_id` varchar(255) NOT NULL,
  PRIMARY KEY (`account_alias`,`domain_alias`),
  KEY `FK80BE02ECB52FDC6` (`domain_alias`),
  KEY `FK80BE02EC3ABFC16F` (`account`,`domain_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_aliases`
--

LOCK TABLES `account_aliases` WRITE;
/*!40000 ALTER TABLE `account_aliases` DISABLE KEYS */;
/*!40000 ALTER TABLE `account_aliases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_users`
--

DROP TABLE IF EXISTS `app_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creation` datetime NOT NULL,
  `enabled` bit(1) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `last_password_update` datetime DEFAULT NULL,
  `locale` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `notify_email` varchar(255) DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  `sounds_enabled` bit(1) NOT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `domain` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userName` (`userName`),
  KEY `FK6DEE8E6AA9DC1BA3` (`domain`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_users`
--

LOCK TABLES `app_users` WRITE;
/*!40000 ALTER TABLE `app_users` DISABLE KEYS */;
INSERT INTO `app_users` VALUES (1,'2011-06-26 17:54:59','','MyLastName',NULL,'en_US','MyName','email@example.com','5f4dcc3b5aa765d61d8327deb882cf99','\0','admin',NULL);
/*!40000 ALTER TABLE `app_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_users_authorities`
--

DROP TABLE IF EXISTS `app_users_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_users_authorities` (
  `app_users_id` int(11) NOT NULL,
  `authorities_id` int(11) NOT NULL,
  PRIMARY KEY (`app_users_id`,`authorities_id`),
  KEY `FK6EC5B0CC7C644C90` (`app_users_id`),
  KEY `FK6EC5B0CC9EA31A41` (`authorities_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_users_authorities`
--

LOCK TABLES `app_users_authorities` WRITE;
/*!40000 ALTER TABLE `app_users_authorities` DISABLE KEYS */;
INSERT INTO `app_users_authorities` VALUES (1,1),(1,2);
/*!40000 ALTER TABLE `app_users_authorities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authorities`
--

DROP TABLE IF EXISTS `authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authorities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authority` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authorities`
--

LOCK TABLES `authorities` WRITE;
/*!40000 ALTER TABLE `authorities` DISABLE KEYS */;
INSERT INTO `authorities` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_DOMAIN_ADMIN');
/*!40000 ALTER TABLE `authorities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `domain`
--

DROP TABLE IF EXISTS `domain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `domain` (
  `domain` varchar(255) NOT NULL DEFAULT '',
  `creation` datetime NOT NULL,
  `server` varchar(255) DEFAULT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`domain`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `domain`
--

LOCK TABLES `domain` WRITE;
/*!40000 ALTER TABLE `domain` DISABLE KEYS */;
/*!40000 ALTER TABLE `domain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `domain_adldap`
--

DROP TABLE IF EXISTS `domain_adldap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `domain_adldap` (
  `domain` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `base` varchar(255) DEFAULT NULL,
  `last_error` varchar(255) DEFAULT NULL,
  `filter` varchar(255) DEFAULT NULL,
  `last_update` datetime DEFAULT NULL,
  `next_update` datetime DEFAULT NULL,
  `override_flag` bit(1) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `dn_authenticate` longtext DEFAULT NULL,
  `port` bigint(20) DEFAULT NULL,
  `ssl_flag` bit(1) DEFAULT NULL,
  `user` varchar(255) DEFAULT NULL,
  `directory_type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`domain`),
  KEY `FK35E5E225A9DC1BA3` (`domain`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `domain_adldap`
--

LOCK TABLES `domain_adldap` WRITE;
/*!40000 ALTER TABLE `domain_adldap` DISABLE KEYS */;
/*!40000 ALTER TABLE `domain_adldap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `domains_aliases`
--

DROP TABLE IF EXISTS `domains_aliases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `domains_aliases` (
  `alias` varchar(255) NOT NULL DEFAULT '',
  `created` datetime NOT NULL,
  `domain` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`alias`),
  KEY `FK7B201AEEA9DC1BA3` (`domain`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `domains_aliases`
--

LOCK TABLES `domains_aliases` WRITE;
/*!40000 ALTER TABLE `domains_aliases` DISABLE KEYS */;
/*!40000 ALTER TABLE `domains_aliases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_accounts`
--

DROP TABLE IF EXISTS `email_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_accounts` (
  `account` varchar(255) NOT NULL DEFAULT '',
  `domain_id` varchar(255) NOT NULL DEFAULT '',
  `created` datetime NOT NULL,
  `data_source` varchar(255) NOT NULL,
  `updated` datetime NOT NULL,
  `group_domain_id` varchar(255) DEFAULT NULL,
  `group_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`account`,`domain_id`),
  KEY `FK3B65F1291FCE7D55` (`domain_id`),
  KEY `FK3B65F12986881E05` (`group_domain_id`,`group_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_accounts`
--

LOCK TABLES `email_accounts` WRITE;
/*!40000 ALTER TABLE `email_accounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `email_accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `features`
--

DROP TABLE IF EXISTS `features`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `features` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `base_priority` bigint(20) NOT NULL,
  `component` varchar(100) NOT NULL,
  `default_admin_order` varchar(20) DEFAULT NULL,
  `description_key` varchar(50) NOT NULL,
  `explain_key` varchar(50) DEFAULT NULL,
  `label_key` varchar(50) NOT NULL,
  `module_report_url` varchar(100) NOT NULL,
  `module_url` varchar(100) NOT NULL,
  `version` int(11) NOT NULL,
  `category_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component` (`component`,`version`),
  KEY `FKEEACE43D7B4BC055` (`category_id`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `features`
--

LOCK TABLES `features` WRITE;
/*!40000 ALTER TABLE `features` DISABLE KEYS */;
INSERT INTO `features` VALUES (1,400000,'org.mxhero.feature.attachmentblock','before','attachment.block.description','attachment.block.explain','attachment.block.label','org/mxhero/feature/attachmentblock/Report.swf','org/mxhero/feature/attachmentblock/AttachmentBlock.swf',1,1),(3,5000,'org.mxhero.feature.redirect','before','redirect.description','redirect.explain','redirect.label','org/mxhero/feature/redirect/Report.swf','org/mxhero/feature/redirect/Redirect.swf',1,1),(4,100000,'org.mxhero.feature.spamassassin','before','spamassassin.description','spamassassin.explain','spamassassin.label','org/mxhero/feature/spamassassin/Report.swf','org/mxhero/feature/spamassassin/SpamAssassin.swf',1,2),(5,99000,'org.mxhero.feature.clamav','before','clamav.description','clamav.explain','clamav.label','org/mxhero/feature/clamav/Report.swf','org/mxhero/feature/clamav/Clamav.swf',1,2),(6,1000,'org.mxhero.feature.backupcopy','before','backup.copy.description','backup.copy.explain','backup.copy.label','org/mxhero/feature/backupcopy/Report.swf','org/mxhero/feature/backupcopy/BackupCopy.swf',1,2),(10,50000,'org.mxhero.feature.wiretapcontent','before','wiretap.content.description','wiretap.content.explain','wiretap.content.label','org/mxhero/feature/wiretapcontent/Report.swf','org/mxhero/feature/wiretapcontent/WiretapContent.swf',1,3),(11,4000,'org.mxhero.feature.wiretapsenderreceiver','before','wiretap.sender.receiver.description','wiretap.sender.receiver.explain','wiretap.sender.receiver.label','org/mxhero/feature/wiretapsenderreceiver/Report.swf','org/mxhero/feature/wiretapsenderreceiver/WiretapSenderReceiver.swf',1,3),(13,0,'org.mxhero.feature.emailsizelimiter','before','email.size.limiter.description','email.size.limiter.explain','email.size.limiter.label','org/mxhero/feature/emailsizelimiter/Report.swf','org/mxhero/feature/emailsizelimiter/EmailSizeLimiter.swf',1,1),(14,5000000,'org.mxhero.feature.limituserdestinations','before','limit.user.destination.description','limit.user.destination.explain','limit.user.destination.label','org/mxhero/feature/limituserdestination/Report.swf','org/mxhero/feature/limituserdestination/LimitUserDestination.swf',1,1),(15,110000,'org.mxhero.feature.externalantispam','before','external.antispam.description','external.antispam.explain','external.antispam.label','org/mxhero/feature/externalantispam/Report.swf','org/mxhero/feature/externalantispam/ExternalAntispam.swf',1,2),(16,5100000,'org.mxhero.feature.blocklist','before','block.list.description','block.list.explain','block.list.label','org/mxhero/feature/blocklist/Report.swf','org/mxhero/feature/blocklist/BlockList.swf',1,1),(17,5090000,'org.mxhero.feature.restricteddelivery','before','restricted.delivery.description','restricted.delivery.explain','restricted.delivery.label','org/mxhero/feature/restricteddelivery/Report.swf','org/mxhero/feature/restricteddelivery/RestrictedDelivery.swf',1,1);
/*!40000 ALTER TABLE `features` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `features_categories`
--

DROP TABLE IF EXISTS `features_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `features_categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `icon_source` varchar(200) NOT NULL,
  `label_key` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `features_categories`
--

LOCK TABLES `features_categories` WRITE;
/*!40000 ALTER TABLE `features_categories` DISABLE KEYS */;
INSERT INTO `features_categories` VALUES (1,'images/features/categories/policies.png','policies'),(2,'images/features/categories/security.png','security'),(3,'images/features/categories/monitoring.png','monitoring'),(4,'images/features/categories/unclassified.png','unclassified');
/*!40000 ALTER TABLE `features_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `features_rules`
--

DROP TABLE IF EXISTS `features_rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `features_rules` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_order` varchar(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  `enabled` bit(1) NOT NULL,
  `label` varchar(100) DEFAULT NULL,
  `two_ways` bit(1) NOT NULL,
  `updated` datetime NOT NULL,
  `domain_id` varchar(100) DEFAULT NULL,
  `feature_id` int(11) NOT NULL,
  `from_direction_id` int(11) DEFAULT NULL,
  `to_direction_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8DE7AE75E6EC4887` (`to_direction_id`),
  KEY `FK8DE7AE75F88AE3B8` (`from_direction_id`),
  KEY `FK8DE7AE757340D73F` (`feature_id`),
  KEY `FK8DE7AE751FCE7D55` (`domain_id`)
) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `features_rules`
--

LOCK TABLES `features_rules` WRITE;
/*!40000 ALTER TABLE `features_rules` DISABLE KEYS */;
/*!40000 ALTER TABLE `features_rules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `features_rules_directions`
--

DROP TABLE IF EXISTS `features_rules_directions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `features_rules_directions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(255) DEFAULT NULL,
  `directiom_type` varchar(100) NOT NULL,
  `domain` varchar(255) DEFAULT NULL,
  `free_value` varchar(255) DEFAULT NULL,
  `group_name` varchar(255) DEFAULT NULL,
  `rule_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1C8F64DE8CEB6B15` (`rule_id`)
) ENGINE=MyISAM AUTO_INCREMENT=49 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `features_rules_directions`
--

LOCK TABLES `features_rules_directions` WRITE;
/*!40000 ALTER TABLE `features_rules_directions` DISABLE KEYS */;
/*!40000 ALTER TABLE `features_rules_directions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `features_rules_properties`
--

DROP TABLE IF EXISTS `features_rules_properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `features_rules_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `property_key` varchar(50) NOT NULL,
  `property_value` varchar(255) NOT NULL,
  `rule_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD7FC093D8CEB6B15` (`rule_id`)
) ENGINE=MyISAM AUTO_INCREMENT=159 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `features_rules_properties`
--

LOCK TABLES `features_rules_properties` WRITE;
/*!40000 ALTER TABLE `features_rules_properties` DISABLE KEYS */;
/*!40000 ALTER TABLE `features_rules_properties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `domain_id` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) NOT NULL DEFAULT '',
  `created` datetime NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`domain_id`,`name`),
  KEY `FKB63DD9D41FCE7D55` (`domain_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_properties`
--

DROP TABLE IF EXISTS `system_properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `property_key` varchar(100) NOT NULL,
  `property_value` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_properties`
--

LOCK TABLES `system_properties` WRITE;
/*!40000 ALTER TABLE `system_properties` DISABLE KEYS */;
INSERT INTO `system_properties` VALUES (1,'mail.smtp.host','localhost'),(2,'mail.smtp.auth','false'),(3,'mail.smtp.port','25'),(4,'mail.smtp.ssl.enable','false'),(5,'mail.admin','admin@mxhero.com'),(6,'mail.smtp.user',NULL),(7,'mail.smtp.password',NULL),(8,'default.user.language','en_US'),(9,'feature.category.unclassified.id','0'),(10,'external.logo.path',NULL),(11,'news.feed.enabled','true');
/*!40000 ALTER TABLE `system_properties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'mxhero'
--

--
-- Dumping routines for database 'mxhero'
--

--
-- Current Database: `mysql`
--

USE `mysql`;

--
-- Table structure for table `event`
--

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES ('statistics','drop_stats_parts','CALL partition_drop(CURRENT_DATE()-INTERVAL 20 DAY,\'mail_stats\',\'statistics\')','root@localhost',NULL,1,'DAY','2011-08-15 13:47:42','2011-08-15 13:47:42',NULL,'2011-04-25 20:35:45',NULL,'ENABLED','DROP','','',0,'SYSTEM','latin1','latin1_swedish_ci','latin1_swedish_ci','CALL partition_drop(CURRENT_DATE()-INTERVAL 20 DAY,\'mail_stats\',\'statistics\')'),('statistics','drop_records_parts','CALL partition_drop(CURRENT_DATE()-INTERVAL 20 DAY,\'mail_records\',\'statistics\')','root@localhost',NULL,1,'DAY','2011-08-15 13:47:42','2011-08-15 13:47:42',NULL,'2011-04-25 20:35:45',NULL,'ENABLED','DROP','','',0,'SYSTEM','latin1','latin1_swedish_ci','latin1_swedish_ci','CALL partition_drop(CURRENT_DATE()-INTERVAL 20 DAY,\'mail_records\',\'statistics\')'),('statistics','add_stats_parts','CALL partition_add(CURRENT_DATE()+INTERVAL 7 DAY,\'mail_stats\',\'statistics\')','root@localhost',NULL,1,'DAY','2011-08-15 13:47:42','2011-08-15 13:47:42',NULL,'2011-04-25 20:35:45',NULL,'ENABLED','DROP','','',0,'SYSTEM','latin1','latin1_swedish_ci','latin1_swedish_ci','CALL partition_add(CURRENT_DATE()+INTERVAL 7 DAY,\'mail_stats\',\'statistics\')'),('statistics','add_records_parts','CALL partition_add (CURRENT_DATE()+INTERVAL 7 DAY, \'mail_records\',\'statistics\')','root@localhost',NULL,1,'DAY','2011-08-15 13:47:42','2011-08-15 13:47:42',NULL,'2011-04-25 20:35:45',NULL,'ENABLED','DROP','','',0,'SYSTEM','latin1','latin1_swedish_ci','latin1_swedish_ci','CALL partition_add (CURRENT_DATE()+INTERVAL 7 DAY, \'mail_records\',\'statistics\')'),('statistics','group_all_stats','CALL group_statistics (NOW())','root@localhost',NULL,10,'MINUTE','2011-08-15 13:47:42','2011-08-15 13:47:42',NULL,'2011-04-25 20:35:45',NULL,'ENABLED','DROP','','',0,'SYSTEM','latin1','latin1_swedish_ci','latin1_swedish_ci','CALL group_statistics (NOW())');
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

/* MXHERO: GRANT PRIVILEGES */
GRANT ALL PRIVILEGES ON mxhero.* TO mxhero@localhost IDENTIFIED BY 'mxhero';

--
-- Current Database: `statistics`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `statistics` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `statistics`;

--
-- Table structure for table `app_users_authorities`
--

DROP TABLE IF EXISTS `app_users_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_users_authorities` (
  `app_users_id` int(11) NOT NULL,
  `authorities_id` int(11) NOT NULL,
  PRIMARY KEY (`app_users_id`,`authorities_id`),
  KEY `FK6EC5B0CC7C644C90` (`app_users_id`),
  KEY `FK6EC5B0CC9EA31A41` (`authorities_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_users_authorities`
--

LOCK TABLES `app_users_authorities` WRITE;
/*!40000 ALTER TABLE `app_users_authorities` DISABLE KEYS */;
/*!40000 ALTER TABLE `app_users_authorities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mail_records`
--

DROP TABLE IF EXISTS `mail_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mail_records` (
  `insert_date` datetime NOT NULL,
  `record_sequence` bigint(20) NOT NULL,
  `bcc_recipeints` longtext,
  `bytes_size` int(11) DEFAULT NULL,
  `cc_recipeints` longtext,
  `from_recipeints` varchar(255) DEFAULT NULL,
  `message_id` longtext NOT NULL,
  `ng_recipeints` longtext,
  `phase` varchar(10) NOT NULL,
  `received_date` datetime DEFAULT NULL,
  `recipient` varchar(255) DEFAULT NULL,
  `recipient_domain_id` varchar(255) DEFAULT NULL,
  `recipient_id` varchar(255) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `sender_domain_id` varchar(255) DEFAULT NULL,
  `sender_id` varchar(255) DEFAULT NULL,
  `sent_date` datetime DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `state_reason` varchar(255) DEFAULT NULL,
  `subject` longtext,
  `to_recipeints` longtext,
  `flow` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`insert_date`,`record_sequence`),
  KEY `flow` (`flow`),
  KEY `recipient_domain_id` (`recipient_domain_id`),
  KEY `sender_domain_id` (`sender_domain_id`),
  KEY `state` (`state`),
  KEY `from_recipeints` (`from_recipeints`),
  KEY `sender` (`sender`),
  KEY `recipient` (`recipient`),
  KEY `recipient_id` (`recipient_id`),
  KEY `sender_id` (`sender_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
/*!50100 PARTITION BY RANGE (TO_DAYS(insert_date))
(PARTITION 2011_07_19 VALUES LESS THAN (734703) ENGINE = InnoDB,
 PARTITION 2011_07_20 VALUES LESS THAN (734704) ENGINE = InnoDB,
 PARTITION 2011_07_21 VALUES LESS THAN (734705) ENGINE = InnoDB,
 PARTITION 2011_07_22 VALUES LESS THAN (734706) ENGINE = InnoDB,
 PARTITION 2011_07_23 VALUES LESS THAN (734707) ENGINE = InnoDB,
 PARTITION 2011_07_24 VALUES LESS THAN (734708) ENGINE = InnoDB,
 PARTITION 2011_07_25 VALUES LESS THAN (734709) ENGINE = InnoDB,
 PARTITION 2011_07_26 VALUES LESS THAN (734710) ENGINE = InnoDB,
 PARTITION 2011_07_27 VALUES LESS THAN (734711) ENGINE = InnoDB,
 PARTITION 2011_07_28 VALUES LESS THAN (734712) ENGINE = InnoDB,
 PARTITION 2011_07_29 VALUES LESS THAN (734713) ENGINE = InnoDB,
 PARTITION 2011_07_30 VALUES LESS THAN (734714) ENGINE = InnoDB,
 PARTITION 2011_07_31 VALUES LESS THAN (734715) ENGINE = InnoDB,
 PARTITION 2011_08_01 VALUES LESS THAN (734716) ENGINE = InnoDB,
 PARTITION 2011_08_02 VALUES LESS THAN (734717) ENGINE = InnoDB,
 PARTITION 2011_08_03 VALUES LESS THAN (734718) ENGINE = InnoDB,
 PARTITION 2011_08_04 VALUES LESS THAN (734719) ENGINE = InnoDB,
 PARTITION 2011_08_05 VALUES LESS THAN (734720) ENGINE = InnoDB,
 PARTITION 2011_08_06 VALUES LESS THAN (734721) ENGINE = InnoDB,
 PARTITION 2011_08_07 VALUES LESS THAN (734722) ENGINE = InnoDB,
 PARTITION 2011_08_08 VALUES LESS THAN (734723) ENGINE = InnoDB,
 PARTITION 2011_08_09 VALUES LESS THAN (734724) ENGINE = InnoDB,
 PARTITION 2011_08_10 VALUES LESS THAN (734725) ENGINE = InnoDB,
 PARTITION 2011_08_11 VALUES LESS THAN (734726) ENGINE = InnoDB,
 PARTITION 2011_08_12 VALUES LESS THAN (734727) ENGINE = InnoDB,
 PARTITION 2011_08_13 VALUES LESS THAN (734728) ENGINE = InnoDB,
 PARTITION 2011_08_14 VALUES LESS THAN (734729) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mail_records`
--

LOCK TABLES `mail_records` WRITE;
/*!40000 ALTER TABLE `mail_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `mail_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mail_stats`
--

DROP TABLE IF EXISTS `mail_stats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mail_stats` (
  `stat_key` varchar(80) NOT NULL,
  `stat_value` longtext,
  `insert_date` datetime NOT NULL,
  `record_sequence` bigint(20) NOT NULL,
  `phase` varchar(10) NOT NULL,
  PRIMARY KEY (`stat_key`,`insert_date`,`record_sequence`,`phase`),
  KEY `FKC656C8173036DEAB` (`insert_date`,`record_sequence`),
  KEY `stats_indx` (`stat_key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
/*!50100 PARTITION BY RANGE (TO_DAYS(insert_date))
(PARTITION 2011_07_19 VALUES LESS THAN (734703) ENGINE = InnoDB,
 PARTITION 2011_07_20 VALUES LESS THAN (734704) ENGINE = InnoDB,
 PARTITION 2011_07_21 VALUES LESS THAN (734705) ENGINE = InnoDB,
 PARTITION 2011_07_22 VALUES LESS THAN (734706) ENGINE = InnoDB,
 PARTITION 2011_07_23 VALUES LESS THAN (734707) ENGINE = InnoDB,
 PARTITION 2011_07_24 VALUES LESS THAN (734708) ENGINE = InnoDB,
 PARTITION 2011_07_25 VALUES LESS THAN (734709) ENGINE = InnoDB,
 PARTITION 2011_07_26 VALUES LESS THAN (734710) ENGINE = InnoDB,
 PARTITION 2011_07_27 VALUES LESS THAN (734711) ENGINE = InnoDB,
 PARTITION 2011_07_28 VALUES LESS THAN (734712) ENGINE = InnoDB,
 PARTITION 2011_07_29 VALUES LESS THAN (734713) ENGINE = InnoDB,
 PARTITION 2011_07_30 VALUES LESS THAN (734714) ENGINE = InnoDB,
 PARTITION 2011_07_31 VALUES LESS THAN (734715) ENGINE = InnoDB,
 PARTITION 2011_08_01 VALUES LESS THAN (734716) ENGINE = InnoDB,
 PARTITION 2011_08_02 VALUES LESS THAN (734717) ENGINE = InnoDB,
 PARTITION 2011_08_03 VALUES LESS THAN (734718) ENGINE = InnoDB,
 PARTITION 2011_08_04 VALUES LESS THAN (734719) ENGINE = InnoDB,
 PARTITION 2011_08_05 VALUES LESS THAN (734720) ENGINE = InnoDB,
 PARTITION 2011_08_06 VALUES LESS THAN (734721) ENGINE = InnoDB,
 PARTITION 2011_08_07 VALUES LESS THAN (734722) ENGINE = InnoDB,
 PARTITION 2011_08_08 VALUES LESS THAN (734723) ENGINE = InnoDB,
 PARTITION 2011_08_09 VALUES LESS THAN (734724) ENGINE = InnoDB,
 PARTITION 2011_08_10 VALUES LESS THAN (734725) ENGINE = InnoDB,
 PARTITION 2011_08_11 VALUES LESS THAN (734726) ENGINE = InnoDB,
 PARTITION 2011_08_12 VALUES LESS THAN (734727) ENGINE = InnoDB,
 PARTITION 2011_08_13 VALUES LESS THAN (734728) ENGINE = InnoDB,
 PARTITION 2011_08_14 VALUES LESS THAN (734729) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mail_stats`
--

LOCK TABLES `mail_stats` WRITE;
/*!40000 ALTER TABLE `mail_stats` DISABLE KEYS */;
/*!40000 ALTER TABLE `mail_stats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mail_stats_grouped`
--

DROP TABLE IF EXISTS `mail_stats_grouped`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mail_stats_grouped` (
  `insert_date` datetime NOT NULL,
  `sender_domain_id` varchar(255) NOT NULL,
  `recipient_domain_id` varchar(255) NOT NULL,
  `stat_key` varchar(255) NOT NULL,
  `stat_value` varchar(255) NOT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`insert_date`,`sender_domain_id`,`recipient_domain_id`,`stat_key`,`stat_value`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mail_stats_grouped`
--

LOCK TABLES `mail_stats_grouped` WRITE;
/*!40000 ALTER TABLE `mail_stats_grouped` DISABLE KEYS */;
/*!40000 ALTER TABLE `mail_stats_grouped` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mail_stats_grouped_keys`
--

DROP TABLE IF EXISTS `mail_stats_grouped_keys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mail_stats_grouped_keys` (
  `stat_key` varchar(255) NOT NULL,
  `stat_value` varchar(255) NOT NULL,
  PRIMARY KEY (`stat_key`,`stat_value`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mail_stats_grouped_keys`
--

LOCK TABLES `mail_stats_grouped_keys` WRITE;
/*!40000 ALTER TABLE `mail_stats_grouped_keys` DISABLE KEYS */;
INSERT INTO `mail_stats_grouped_keys` VALUES ('email.blocked','true'),('spam.detected','true'),('virus.detected','true');
/*!40000 ALTER TABLE `mail_stats_grouped_keys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'statistics'
--
/*!50106 SET @save_time_zone= @@TIME_ZONE */ ;
/*!50106 DROP EVENT IF EXISTS `add_records_parts` */;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = latin1 */ ;;
/*!50003 SET character_set_results = latin1 */ ;;
/*!50003 SET collation_connection  = latin1_swedish_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = '' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE EVENT `add_records_parts` ON SCHEDULE EVERY 1 DAY STARTS '2011-04-25 17:35:45' ON COMPLETION NOT PRESERVE ENABLE DO CALL partition_add (CURRENT_DATE()+INTERVAL 7 DAY, 'mail_records','statistics') */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
/*!50106 DROP EVENT IF EXISTS `add_stats_parts` */;;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = latin1 */ ;;
/*!50003 SET character_set_results = latin1 */ ;;
/*!50003 SET collation_connection  = latin1_swedish_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = '' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE EVENT `add_stats_parts` ON SCHEDULE EVERY 1 DAY STARTS '2011-04-25 17:35:45' ON COMPLETION NOT PRESERVE ENABLE DO CALL partition_add(CURRENT_DATE()+INTERVAL 7 DAY,'mail_stats','statistics') */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
/*!50106 DROP EVENT IF EXISTS `drop_records_parts` */;;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = latin1 */ ;;
/*!50003 SET character_set_results = latin1 */ ;;
/*!50003 SET collation_connection  = latin1_swedish_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = '' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE EVENT `drop_records_parts` ON SCHEDULE EVERY 1 DAY STARTS '2011-04-25 17:35:45' ON COMPLETION NOT PRESERVE ENABLE DO CALL partition_drop(CURRENT_DATE()-INTERVAL 20 DAY,'mail_records','statistics') */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
/*!50106 DROP EVENT IF EXISTS `drop_stats_parts` */;;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = latin1 */ ;;
/*!50003 SET character_set_results = latin1 */ ;;
/*!50003 SET collation_connection  = latin1_swedish_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = '' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE EVENT `drop_stats_parts` ON SCHEDULE EVERY 1 DAY STARTS '2011-04-25 17:35:45' ON COMPLETION NOT PRESERVE ENABLE DO CALL partition_drop(CURRENT_DATE()-INTERVAL 20 DAY,'mail_stats','statistics') */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
/*!50106 DROP EVENT IF EXISTS `group_all_stats` */;;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = latin1 */ ;;
/*!50003 SET character_set_results = latin1 */ ;;
/*!50003 SET collation_connection  = latin1_swedish_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = '' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE EVENT `group_all_stats` ON SCHEDULE EVERY 10 MINUTE STARTS '2011-04-25 17:35:45' ON COMPLETION NOT PRESERVE ENABLE DO CALL group_statistics (NOW()) */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
DELIMITER ;
/*!50106 SET TIME_ZONE= @save_time_zone */ ;

--
-- Dumping routines for database 'statistics'
--
/*!50003 DROP PROCEDURE IF EXISTS `group_statistics` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `group_statistics`(IN P_DATE DATETIME)
BEGIN

DELETE FROM `statistics`.`mail_stats_grouped` 
WHERE insert_date >=  DATE_FORMAT(P_DATE, '%Y-%m-%d 00:00:00');

INSERT INTO mail_stats_grouped
SELECT DATE_FORMAT(s.insert_date, '%Y-%m-%d %H:00:00'), if(r.flow='both' or r.flow='out',r.sender_domain_id,''), if(r.flow='both' or r.flow='in',r.recipient_domain_id,''), s.stat_key, s.stat_value, count(*) amount
FROM `statistics`.mail_stats s
INNER JOIN `statistics`.mail_records r 
ON r.insert_date = s.insert_date 
AND r.record_sequence = s.record_sequence
INNER JOIN `statistics`.mail_stats_grouped_keys k
ON s.stat_key = k.stat_key 
AND s.stat_value like k.stat_value
WHERE s.insert_date >=  DATE_FORMAT(P_DATE, '%Y-%m-%d 00:00:00')
GROUP BY DATE_FORMAT(s.insert_date, '%Y-%m-%d %H:00:00') , if(r.flow='both' or r.flow='out',r.sender_domain_id,''), if(r.flow='both' or r.flow='in',r.recipient_domain_id,''), s.stat_key, s.stat_value;

COMMIT;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `partition_add` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `partition_add`(IN through_date date, IN tbl varchar(64), IN db varchar(64))
BEGIN
DECLARE add_me char(10);
DECLARE max_new_parts,add_cnt smallint unsigned default 0;
SELECT 1024-COUNT(*) AS max_new_parts,
SUM(CASE WHEN
 DATE(PARTITION_NAME)>=through_date then 1 else 0
 END)
INTO max_new_parts, add_cnt
FROM INFORMATION_SCHEMA.PARTITIONS
WHERE  TABLE_SCHEMA = db
 AND TABLE_NAME = tbl;

IF add_cnt=0 THEN
BEGIN
SELECT MAX(DATE(PARTITION_NAME)) INTO add_me
FROM INFORMATION_SCHEMA.PARTITIONS WHERE TABLE_NAME=tbl and TABLE_SCHEMA=db
AND DATE(PARTITION_NAME)<through_date;


IF DATEDIFF(through_date,add_me)+1 < max_new_parts THEN
BEGIN
WHILE add_me<through_date do BEGIN
SET add_me:=DATE_FORMAT(add_me + INTERVAL 1 DAY,"%Y_%m_%d");
SET @alter_stmt:=CONCAT("ALTER TABLE ",db,".",tbl," ADD PARTITION (PARTITION ",add_me," VALUES LESS THAN (TO_DAYS('",add_me+INTERVAL 1 DAY, "')))" );

PREPARE stmt_alter FROM @alter_stmt; EXECUTE stmt_alter; DEALLOCATE PREPARE stmt_alter;

END;
END WHILE;
END;
END IF;
END;
END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `partition_drop` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `partition_drop`(IN through_date date, IN tbl varchar(64), IN db varchar(64))
BEGIN
DECLARE delete_me varchar(64);
DECLARE notfound BOOL DEFAULT FALSE;
DECLARE pname CURSOR FOR SELECT PARTITION_NAME
FROM INFORMATION_SCHEMA.PARTITIONS WHERE TABLE_NAME=tbl AND TABLE_SCHEMA=db
AND DATE(PARTITION_NAME)!= 0
AND DATE(PARTITION_NAME) IS NOT NULL
AND DATE(PARTITION_NAME)<=through_date;

DECLARE CONTINUE HANDLER FOR NOT FOUND SET notfound:=TRUE;
OPEN pname;

cursor_loop: LOOP
FETCH pname INTO delete_me;
IF notfound THEN LEAVE cursor_loop; END IF;
SET @alter_stmt:=CONCAT("ALTER TABLE ",db,".",tbl," DROP PARTITION ",delete_me);



PREPARE stmt_alter FROM @alter_stmt; EXECUTE stmt_alter; DEALLOCATE PREPARE stmt_alter;

END LOOP;
CLOSE pname;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-08-15 11:04:38

/* STATISTICS: GRANT PRIVILEGES */
GRANT ALL PRIVILEGES ON statistics.* TO mxhero@localhost IDENTIFIED BY 'mxhero';
