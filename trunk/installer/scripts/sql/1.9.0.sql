SET FOREIGN_KEY_CHECKS=0;
SET AUTOCOMMIT=0;
START TRANSACTION;

USE attachments;

-- Access remaining
ALTER TABLE message_attach ADD COLUMN download_tries_remaining int(11);
-- Access remaining

SET FOREIGN_KEY_CHECKS=1;
      
USE mxhero; 

-- New Apps   
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
(85005, 'org.mxhero.feature.sendaspersonal', 'before', 'sendaspersonal.description', 'sendaspersonal.explain', 'sendaspersonal.label', 'org/mxhero/feature/sendaspersonal/Report.swf', 'org/mxhero/feature/sendaspersonal/SendAsPersonal.swf', 1, 1, true);
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
(700000, 'org.mxhero.feature.readonce', 'before', 'readonce.description', 'readonce.explain', 'readonce.label', 'org/mxhero/feature/readonce/Report.swf', 'org/mxhero/feature/readonce/ReadOnce.swf', 1, 1, true);
-- New Apps


USE statistics; 

-- New Groupped stats
INSERT INTO mail_stats_grouped_keys (stat_key,stat_value) VALUES ('org.mxhero.feature.sendaspersonal','true');
INSERT INTO mail_stats_grouped_keys (stat_key,stat_value) VALUES ('org.mxhero.feature.readonce','true');

COMMIT;
-- MySQL dump 10.13  Distrib 5.1.67, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: readonce
-- ------------------------------------------------------
-- Server version	5.1.67-0ubuntu0.10.04.1

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
-- Current Database: `readonce`
--

/*!40000 DROP DATABASE IF EXISTS `readonce`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `readonce` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `readonce`;

--
-- Table structure for table `image_files`
--

DROP TABLE IF EXISTS `image_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image_files` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_name` text NOT NULL,
  `seconds_available` int(11) DEFAULT NULL,
  `remaining_access` int(11) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `recipient` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `accessed` datetime DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `message_id` text,
  `subject` text,
  `relative_path` text,
  `locale` varchar(255) DEFAULT NULL,
  `time_offset` int(11) DEFAULT NULL,
  `file_name_mobile` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=204 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'readonce'
--

--
-- Dumping routines for database 'readonce'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-01-25 19:44:19

GRANT ALL PRIVILEGES ON `readonce`.* TO 'mxhero'@'%' ;
