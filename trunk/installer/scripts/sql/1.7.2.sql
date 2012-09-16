ALTER TABLE attachments.attach MODIFY COLUMN file_name VARCHAR(255) NOT NULL;
GRANT ALL PRIVILEGES ON `attachments`.* TO 'mxhero'@'%' ;
GRANT ALL PRIVILEGES ON `statistics`.* TO 'mxhero'@'%' ;
GRANT ALL PRIVILEGES ON `threadlight`.* TO 'mxhero'@'%';