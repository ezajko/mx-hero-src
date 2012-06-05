hosts=127.0.0.1
user=mxhero
password=mxhero
dbname=mxhero
query=SELECT DISTINCT CONCAT('smtp:[', SUBSTRING_INDEX(server, ':', 1),']:', IF(LOCATE(':', server), SUBSTRING_INDEX(server, ':', -1), 25)) FROM domain d LEFT JOIN domains_aliases a ON (d.domain = a.domain) WHERE alias = '%d'
