### CASSANDRA STUFF

cd into cassandra and run 

	$ sudo bin/cassandra
	
in new terminal:

	$ $ bin/cqlsh
	
	
from cqlsh

CREATE TABLE accounts (
prefix text,
firstname text,
lastname text,
email text,
address1 text,
address2 text,
city text,
state text,
zip text,
PRIMARY KEY (email));