# MusalaLogistics
is a java based application that manages the delivery of medications using drones

# Pre-requisites
- Docker
- Docker compose
- Java 17
- redis
- mssql server (or any SQL database of your choice)
- IntelliJ Idea for IDE

# How to run locally
Pull code with git via

    browse https://github.com/aosobu/HouseOfPrayerLogistics.git or unzip folder made available by Mirjana

 and import into any IDE of your choice.

To download dependencies execute maven command below

      mvn clean install 

#### Ensure your database and redis installations are up and running on default port, 1433 and 6379 respectively.

Note: MSSQL server is used for this project.
Update the jdbc configuration on the properties file i.e the database name, user and password details accordingly.

If you want to use the same redis and mssql installations you can execute

      docker compose -up d 

on the root directory of this application

Change 

You can use any SQL DBMS of your choice - however be sure to include the required dependency and change the jdbc configurations in the properties file

Note: The default collation on mssql does not support all unicode characters.
If all unicode characters are to be used, please ensure to change default collation on MSSQL Server.

You can use the script below

       CREATE DATABASE MusalaLogistics COLLATE Latin1_General_100_CI_AI_SC_UTF8

if you desire full range unicode support

Check the prerequisites section for information on java sdk.

For reference to api setup and usage you can import published postman collection

See section on postman for published details

# Important!
- Create a database with name MusalaLogistics and register on
- That the database password and username have been change in the application.properties file to suit you local setup.

# High Level Architecture
This POC is built as a monolith. However, it is modularized into packages that could make for independent microservices.

Modules include:
1. Commons - where shared files and project agnostic projects are kept
2. DroneService - contains all of the code that facilitates drone management functionality.
3. Workers - contains the job that performs lazy loading of drones and audit function. In a microservice architecture workers could be scale to meet the high pressing demands for drone loading




# #Postman Collection

The collection can be accessed at https://documenter.getpostman.com/view/20194362/2s8YekTFcj






