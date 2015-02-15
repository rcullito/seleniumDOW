Eeeeeee!

example site to run the script on 

https://secure.defenders.org/site/Advocacy?cmd=display&page=UserAction&id=2839
https://secure.defenders.org/site/Advocacy?cmd=display&page=UserAction&id=2833


Steps to run full Application


### 1. from home directory or wherever the selenium server standalone jar is located
	$ java -jar selenium-server-standalone-2.44.0.jar -role hub

### 2. run from anywhere as long as phantomjs is on path, site build doesnt work but homebrew installation does
	$ phantomjs --webdriver=8080 --webdriver-selenium-grid-hub=http://127.0.0.1:4444

### 3. run from cassandra home directory dsc-cassandra-2.0.11
	$ sudo bin/cassandra
	
### 4. start play application server
	$ activator ~run

