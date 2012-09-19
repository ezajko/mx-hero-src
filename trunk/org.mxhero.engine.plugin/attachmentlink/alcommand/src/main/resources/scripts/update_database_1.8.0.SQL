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

SET FOREIGN_KEY_CHECKS=1;

COMMIT;