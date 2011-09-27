hosts=127.0.0.1
user=mxhero
password=mxhero
dbname=mxhero
query=SELECT DISTINCT CONCAT('smtp:[',server,']') FROM domain d LEFT JOIN domains_aliases a ON (d.domain = a.domain) WHERE alias = '%d'
