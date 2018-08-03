# ElipaPower API
Elipa power is an api that dispenses prepaid Kenya Power tokens. This api receives request in JSON format from a client application 
and prepares an xml message request that it forwards to the server dispensing the token. The response is received from the token 
dispensing server is stored, broken down and reassembled into a JSON string and returned to the client application. The expected 
response time is less than 5 seconds. A timeout has been set to 30 seconds.

## Prerequistes
- Open JDK 1.8.0_161
- Apache Tomcat 8.5.29
- Self Signed SSL certificate
- Database mysql Ver 15.1 Distrib 10.2.14-MariaDB, for debian-linux-gnu (x86_64) using readline 5.2

## Installation 
i. Execute the script script in the script folder to create the required tables

ii. Open the properies file and set the following 

 - keystore.pass =keystorepassword (there is an existing password. This can be changed by typing the following 
 ```
 		keytool -keypasswd  -alias <key_name> -keystore my.keystore
 ```
 - keystore.path = /path/to/keystore
 - errorLog.filepath=/path/to/logs
 - spring.datasource.username = username
 - pring.datasource.password = password
 - spring.datasource.url = jdbc:mariadb://localhost:3306/elipapower database url 
 
## SSL Keys 
 Copy ssl key from the SSLkeys folder to location specified by the **keystore.path** of the properties file
 
## Database
 
i. Create the database and ensure you change the **spring.datasource.url** in order to point to your database;
 
ii. Grant the appropriate rights to the database
 
iii. Execute scripts located in the Scripts folder
iv. It is also possible to create tables by setting the following parameter in the properties file


## Deploy the ElipaPower App
i. Start the terminal in linux
ii. navigate to *tomcat/bin* and stop tomcat if already running using **shutdown.sh**
iii. go to the war folder *cd war*
iv. Copy the *elipapower.war* to */tomcat/webapps*
iv. navigate to *tomcat/bin* and use **startup.sh** to start the tomcat
## Make Request
i. Startup postman
ii. Make a post method request to /tokenreq using the following payload
*{"refno": "your ref number", "meterno": "A83456510", "amount": "7500"}*
The response you get will be as follows

## Scenarios
i. Server does not respond within 30 seconds

ii. Server 





