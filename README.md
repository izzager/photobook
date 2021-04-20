# Photobook Application

## Description

Photobook is an application that allows users to share 
their photos with the community. 
If you have one you can just upload it and the rest will be 
taken by the Photobook’s intelligent algorithms. 
It’s analogues to Pinterest and DeviantArt.

## How to run application using Docker

1. Write in terminal:
```
docker run -p 3306:3306 -v /tmp:/tmp 
--name db --detach -e MYSQL_ROOT_PASSWORD=“qwe123” 
-e MYSQL_ROOT_HOST=% -e MYSQL_DATABASE=social 
-d mysql/mysql-server:5.6 --lower_case_table_names=1 
--init-connect=’GRANT CREATE USER ON *.* TO ‘root’@‘%’;FLUSH PRIVILEGES;CREATE DATABASE photobook;' 
```

It's run Docker container and created database.

2. Download and unzip project archive.

3. Open project in IntelliJ IDEA. Edit startup configurations
   with using environment variables:

```
database.username=root;database.password=qwe123
```

Then press "Apply" or "OK". It is required for actions with database.

4. Download Postman Collection and import it in Postman. There are several
   prepared requests for easier testing of the application. 

5. Application is ready to work and be tested!
