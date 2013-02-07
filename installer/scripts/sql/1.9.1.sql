USE mxhero; 

UPDATE `mxhero`.`features` SET `category_id` = 2 WHERE `component` = 'org.mxhero.feature.readonce';

COMMIT;
