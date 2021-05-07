# Photobook Application

## Description

Photobook is an application that allows users to share
their photos with the community.
If you have one you can just upload it and the rest will be
taken by the Photobook’s intelligent algorithms.
It’s analogues to Pinterest and DeviantArt.

## How to run application using Docker

1. Run the database as a Docker container. Write in terminal:
```
$ docker run \
--name db \
--detach \
-e MYSQL_ROOT_PASSWORD=“qwe123” \
-e MYSQL_ROOT_HOST=% \
-e MYSQL_DATABASE=photobook \
-v /tmp:/tmp \
-p 3306:3306 \
-d mysql/mysql-server:8.0 \
--lower_case_table_names=1 \
--init-connect=’GRANT CREATE USER ON *.* TO ‘root’@‘%’;FLUSH PRIVILEGES;CREATE DATABASE photobook;' 
```

It's run Docker container and created database.

2. Download and unzip project archive.

3. Download Postman Collection [using this link](https://drive.google.com/file/d/1X8sOAMQFt5Qpza4WmzaxgnXxAiO64cRh/view?usp=sharing)
   and import it in Postman. There are several prepared requests for easier testing of the application.
   In case you can't open the link, DM me in Slack (@telesina), and I will send you this collection.

4. Application is ready to work and be tested!
