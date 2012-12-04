SET FOREIGN_KEY_CHECKS=0;
SET AUTOCOMMIT=0;
START TRANSACTION;

USE attachments;

DROP TABLE IF EXISTS message_attach_ex_storage;

CREATE TABLE message_attach_ex_storage
(
	message_attach_ex_storage_id BIGINT NOT NULL auto_increment,
	message_attach_id BIGINT NOT NULL,
	email_to_synchro VARCHAR(255) NOT NULL,
	attach_cloud_url VARCHAR(255) NULL,
	was_proccessed TINYINT NOT NULL DEFAULT false,
	is_sender TINYINT NOT NULL DEFAULT false,
	is_recipient TINYINT NOT NULL DEFAULT true,
	PRIMARY KEY (message_attach_ex_storage_id),
	CONSTRAINT FOREIGN KEY(message_attach_id) REFERENCES message_attach(message_attach_id),
	UNIQUE (message_attach_id,email_to_synchro),
	KEY (was_proccessed),
	KEY (attach_cloud_url)
)ENGINE=InnoDB;

ALTER TABLE message ADD COLUMN email_date timestamp null default now();
ALTER TABLE attach modify COLUMN file_name VARCHAR(255) NOT NULL;

-- Bug Fix ENG-258
ALTER TABLE message CHANGE COLUMN processed processed TINYINT NOT NULL DEFAULT true;
ALTER TABLE message CHANGE COLUMN process_ack_download process_ack_download TINYINT NOT NULL;
ALTER TABLE message_attach CHANGE COLUMN enable_to_download enable_to_download TINYINT NOT NULL DEFAULT true;
ALTER TABLE message_attach CHANGE COLUMN was_access_first_time was_access_first_time TINYINT NOT NULL DEFAULT false;
ALTER TABLE history_access_attach CHANGE COLUMN could_download could_download TINYINT NOT NULL;
-- End Bug Fix ENG-258

SET FOREIGN_KEY_CHECKS=1;

COMMIT;

USE mxhero;

CREATE TABLE `zimbra_provider_data` (
  `insert_date` date NOT NULL,
  `account` varchar(255) NOT NULL,
  `domain` varchar(255) NOT NULL,
  `totalQuota` bigint(20) DEFAULT NULL,
  `usedQuota` bigint(20) DEFAULT NULL,
  `accountType` varchar(255) DEFAULT NULL,
  `bandwidth` bigint(20) DEFAULT NULL,
  `cos` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`insert_date`,`account`,`domain`)
) ENGINE=InnoDB;

COMMIT;


-- MySQL dump 10.13  Distrib 5.1.66, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: mxhero
-- ------------------------------------------------------
-- Server version       5.1.66-0ubuntu0.10.04.1
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping routines for database 'mxhero'
--
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 FUNCTION `strSplit`(x varchar(255), delim varchar(12), pos int) RETURNS varchar(255) CHARSET latin1
return replace(substring(substring_index(x, delim, pos), length(substring_index(x, delim, pos - 1)) + 1), delim, '') */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `update_bandwidth`(IN dateto Date)
BEGIN
        DECLARE domain_var VARCHAR(255);
        DECLARE done INT DEFAULT FALSE;
        DECLARE cur_domain CURSOR FOR SELECT domain FROM mxhero.domain;
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

        OPEN cur_domain;

        read_loop: LOOP
    FETCH cur_domain INTO domain_var;
        IF done THEN
                LEAVE read_loop;
        END IF;

                INSERT INTO mxhero.zimbra_provider_data (insert_date,account,domain,totalQuota,usedQuota,accountType,bandwidth)
                        (SELECT dateto, strSplit(sender_id,"@",1), sender_domain_id, null, null, null, SUM(bytes_size) as bandwidth
                        FROM statistics.mail_records r
                        WHERE sender_domain_id = domain_var
                        AND insert_date >= dateto
                        AND insert_date < DATE_ADD(dateto, INTERVAL 1 DAY)
                        GROUP BY sender_domain_id,sender_id)
                ON DUPLICATE KEY UPDATE bandwidth=VALUES(bandwidth);
                COMMIT;
                INSERT INTO mxhero.zimbra_provider_data (insert_date,account,domain,totalQuota,usedQuota,accountType,bandwidth)
                        (SELECT dateto, strSplit(recipient_id,"@",1), recipient_domain_id, null, null, null, SUM(bytes_size) as bandwidth
                        FROM statistics.mail_records r
                        WHERE recipient_domain_id = domain_var
                        AND insert_date >= dateto
                        AND insert_date < DATE_ADD(dateto, INTERVAL 1 DAY)
                        GROUP BY recipient_domain_id,recipient_id)
                ON DUPLICATE KEY UPDATE bandwidth=bandwidth+VALUES(bandwidth);
                COMMIT;

        END LOOP;

  CLOSE cur_domain;

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
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-11-09 17:23:19

CREATE EVENT update_bandwidth_event ON SCHEDULE EVERY 1 DAY STARTS NOW() ON COMPLETION NOT PRESERVE ENABLE DO CALL update_bandwidth(CURRENT_DATE());
