# ElipaPower API
Elipa power is an api that dispenses prepaid Kenya Power tokens. This api receives request in JSON format from a client application 
and prepares an xml message request that it forwards to the server dispensing the token. The response is received from the token 
dispensing server is stored, broken down and reassembled into a JSON string and returned to the client application. The expected 
response time is less than 5 seconds. A timeout has been set to 30 seconds.

## Prerequistes
- Open JDK 1.8.0_161
- Self Signed SSL certificate
- Database mysql Ver 15.1 Distrib 10.2.14-MariaDB, for debian-linux-gnu (x86_64) using readline 5.2

## Installation 
1. Execute the script script in the script folder to create the required tables

2. Open the properies file and set the following 



