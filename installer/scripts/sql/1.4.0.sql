USE `mxhero`;

INSERT INTO `statistics`.`mail_stats_grouped_keys` (`stat_key`, `stat_value`) VALUES ('org.mxhero.feature.replytimeout', '%');

USE `mxhero`;

INSERT INTO `mxhero`.`system_properties` (`property_key`, `property_value`) VALUES ('api.username', 'apiuser');

INSERT INTO `mxhero`.`system_properties` (`property_key`, `property_value`) VALUES ('api.password', '6x5ItobNGPW31x4');

ALTER TABLE `mxhero`.`features_rules_properties` CHANGE COLUMN `property_value` `property_value` TEXT NOT NULL  ;

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
(6000000, 'org.mxhero.feature.replytimeout', 'before', 'replytimeout.description', 'replytimeout.explain', 'replytimeout.label', 'org/mxhero/feature/replytimeout/Report.swf', 'org/mxhero/feature/replytimeout/ReplyTimeout.swf', 1, 5, true);



-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: threadlight
-- ------------------------------------------------------
-- Server version	5.5.18

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
-- Current Database: `threadlight`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `threadlight` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `threadlight`;

--
-- Table structure for table `thread_rows`
--

DROP TABLE IF EXISTS `thread_rows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `thread_rows` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message_id` text NOT NULL,
  `creation_time` datetime NOT NULL,
  `recipient_mail` varchar(255) NOT NULL,
  `sender_mail` varchar(255) NOT NULL,
  `message_subject` text,
  `reply_time` datetime DEFAULT NULL,
  `snooze_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQ_THREAD` (`message_id`(255),`recipient_mail`,`sender_mail`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thread_rows`
--

LOCK TABLES `thread_rows` WRITE;
/*!40000 ALTER TABLE `thread_rows` DISABLE KEYS */;
/*!40000 ALTER TABLE `thread_rows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thread_rows_followers`
--

DROP TABLE IF EXISTS `thread_rows_followers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `thread_rows_followers` (
  `thread_id` int(11) NOT NULL,
  `follower` varchar(255) NOT NULL,
  `follower_parameters` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`thread_id`,`follower`),
  KEY `fk_thread_rows_id` (`thread_id`),
  CONSTRAINT `fk_thread_rows_id` FOREIGN KEY (`thread_id`) REFERENCES `thread_rows` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thread_rows_followers`
--

LOCK TABLES `thread_rows_followers` WRITE;
/*!40000 ALTER TABLE `thread_rows_followers` DISABLE KEYS */;
/*!40000 ALTER TABLE `thread_rows_followers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'threadlight'
--
/*!50106 SET @save_time_zone= @@TIME_ZONE */ ;
/*!50106 DROP EVENT IF EXISTS `delete_old` */;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = utf8 */ ;;
/*!50003 SET character_set_results = utf8 */ ;;
/*!50003 SET collation_connection  = utf8_general_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE*/ /*!50117 DEFINER=`root`@`localhost`*/ /*!50106 EVENT `delete_old` ON SCHEDULE EVERY 1 HOUR STARTS '2012-02-17 10:17:11' ON COMPLETION NOT PRESERVE ENABLE DO CALL threadlight.delete_old_threads() */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
DELIMITER ;
/*!50106 SET TIME_ZONE= @save_time_zone */ ;

--
-- Dumping routines for database 'threadlight'
--
/*!50003 DROP PROCEDURE IF EXISTS `delete_old_threads` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `delete_old_threads`()
BEGIN

DELETE FROM thread_rows 
WHERE creation_time < now() -interval 4 month;
commit;

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

-- Dump completed on 2012-03-20 16:31:17

GRANT ALL PRIVILEGES ON threadlight.* TO mxhero@'localhost' IDENTIFIED BY 'mxhero';
