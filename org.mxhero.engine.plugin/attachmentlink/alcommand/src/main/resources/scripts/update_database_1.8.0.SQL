SET FOREIGN_KEY_CHECKS=0;
SET AUTOCOMMIT=0;
START TRANSACTION;

USE attachments;

DROP TABLE IF EXISTS message_attach_ex_storage;

CREATE TABLE message_attach_ex_storage
(
	message_attach_id BIGINT NOT NULL,
	was_proccessed TINYINT NOT NULL DEFAULT false,
	PRIMARY KEY (message_attach_id)
	KEY (was_proccessed)
)ENGINE=InnoDB;


SET FOREIGN_KEY_CHECKS=1;

COMMIT;