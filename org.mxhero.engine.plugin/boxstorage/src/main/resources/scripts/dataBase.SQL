SET FOREIGN_KEY_CHECKS=0;
SET AUTOCOMMIT=0;
START TRANSACTION;

DROP SCHEMA IF EXISTS attachments_box_client;

CREATE SCHEMA attachments_box_client;

USE attachments_box_client;

DROP TABLE IF EXISTS user_box_file;
DROP TABLE IF EXISTS user_box_token;

CREATE TABLE user_box_token
(
	email VARCHAR(150) NOT NULL,
	token VARCHAR(255) NOT NULL,
	PRIMARY KEY (email)
)ENGINE=InnoDB;

CREATE TABLE user_box_file
(
	file_path VARCHAR(255) NOT NULL,
	email VARCHAR(150) NOT NULL,
	public_url VARCHAR(255) NULL,
	id_file_box VARCHAR(150) NOT NULL,
	proccess_date TIMESTAMP NOT NULL default now(),
	proccessed TINYINT NOT NULL default true,
	PRIMARY KEY (file_path),
	CONSTRAINT FOREIGN KEY(email) REFERENCES user_box_token(email)
)ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS=1;

COMMIT;