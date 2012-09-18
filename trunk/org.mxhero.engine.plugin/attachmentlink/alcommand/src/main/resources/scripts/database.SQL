SET FOREIGN_KEY_CHECKS=0;
SET AUTOCOMMIT=0;
START TRANSACTION;

DROP SCHEMA IF EXISTS attachments;

CREATE SCHEMA attachments;

USE attachments;

DROP TABLE IF EXISTS history_access_attach;
DROP TABLE IF EXISTS message_attach;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS attach;


CREATE TABLE message
(
	message_id BIGINT NOT NULL auto_increment,
	message_platform_id text NOT NULL,
	processed BIT NOT NULL DEFAULT 0,
	sender_email VARCHAR(150) NOT NULL,
	process_ack_download BIT NOT NULL,
	msg_ack_download text NULL,
	subject varchar(255) null,
	msg_ack_download_html text NULL,
	PRIMARY KEY (message_id)
)ENGINE=InnoDB;

CREATE UNIQUE INDEX un_msg_msg_id ON message (message_platform_id(255));


CREATE TABLE attach
(
	attach_id BIGINT NOT NULL auto_increment,
	md5_checksum VARCHAR(255) NOT NULL,
	file_name VARCHAR(100) NOT NULL,
	size BIGINT NOT NULL,
	mime_type VARCHAR(255) NOT NULL,
	path VARCHAR(100) NOT NULL,
	PRIMARY KEY (attach_id),
	UNIQUE (md5_checksum)
)ENGINE=InnoDB;


CREATE TABLE message_attach
(
	message_attach_id BIGINT NOT NULL auto_increment,
	message_id BIGINT,
	attach_id BIGINT,
	recipient_email VARCHAR(150) NOT NULL,
	enable_to_download BIT NOT NULL DEFAULT 1,
	was_access_first_time BIT NOT NULL DEFAULT 0,
	creation_date TIMESTAMP NOT NULL DEFAULT now(),
	PRIMARY KEY (message_attach_id),
	UNIQUE (message_id, attach_id,recipient_email),
	KEY (attach_id),
	KEY (message_id)
)ENGINE=InnoDB;

CREATE TABLE history_access_attach
(
	history_id BIGINT NOT NULL auto_increment,
	message_attach_id BIGINT,
	access_time TIMESTAMP DEFAULT now(),
	could_download BIT NOT NULL,
	PRIMARY KEY (history_id),
	KEY (message_attach_id)
)ENGINE=InnoDB;


ALTER TABLE history_access_attach ADD CONSTRAINT FK_history_access_attach_message_attach 
	FOREIGN KEY (message_attach_id) REFERENCES message_attach (message_attach_id);

ALTER TABLE message_attach ADD CONSTRAINT FK_message_attach_attach 
	FOREIGN KEY (attach_id) REFERENCES attach (attach_id);

ALTER TABLE message_attach ADD CONSTRAINT FK_message_attach_message 
	FOREIGN KEY (message_id) REFERENCES message (message_id);

SET FOREIGN_KEY_CHECKS=1;

COMMIT;