#User Authentication Server

##Purpose
Java project that performs basic user authentication

##Notes on implementation
The application is using Spring Boot framework and provides basic http user authentication.
For current implementation:
 - SSL was not enabled;
 - CSRF was disabled;
 - No user ROLEs have been assigned; 

##Api
/user
###Operations
/register

/login

/logout

/userDetails?username=<username>

/allUsers

/resetPassword <--- Not yet supported

/unlock <--- Not yet supported

##How to run
1). Clone the project locally: _https://github.com/LorenaUdroiu/user-authentication.git_

2). Build project: _mvn clean package_

3). Run on command line: _java -jar target/authentication-0.0.1-SNAPSHOT.jar_

or

3). Run using Maven: _mvn spring-boot:run_

##How to test
For testing the app please refer to below postman collection:
[user-authentication.postman_collection.json](user-authentication.postman_collection.json)

