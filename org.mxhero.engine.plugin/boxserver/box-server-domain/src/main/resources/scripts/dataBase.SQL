SET FOREIGN_KEY_CHECKS=0;
SET AUTOCOMMIT=0;
START TRANSACTION;

DROP SCHEMA IF EXISTS attachments_box_service;

CREATE SCHEMA attachments_box_service;

USE attachments_box_service;

DROP TABLE IF EXISTS user_box_token;
DROP TABLE IF EXISTS applications;

CREATE TABLE user_box_token
(
	email VARCHAR(150) NOT NULL,
	token VARCHAR(255) NOT NULL,
	created_by_us TINYINT(1) NOT NULL DEFAULT false,
	application_id VARCHAR(150) NOT NULL,
	PRIMARY KEY (email),
	CONSTRAINT FOREIGN KEY(application_id) REFERENCES applications(name)
)ENGINE=InnoDB;

CREATE TABLE applications
(
	name VARCHAR(150) NOT NULL,
	enabled TINYINT(1) NOT NULL DEFAULT true,
	PRIMARY KEY (name)
)ENGINE=InnoDB;


SET FOREIGN_KEY_CHECKS=1;

COMMIT;