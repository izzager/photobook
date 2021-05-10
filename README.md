# Photobook Application

## Description

Photobook is an application that allows users to share
their photos with the community.
If you have one you can just upload it and the rest will be
taken by the Photobook’s intelligent algorithms.
It’s analogues to Pinterest and DeviantArt.

## How to run application using Docker

1. Push database and application images:
```
$ docker pull mysql:8.0
$ docker pull telesina/photobook
```

It's run Docker container and created database.

2. Download and unzip project archive.

3. Move into project folder (`$ cd path/to/project`).

4. Run containers:

```
$ docker-compose up
```

5. Download Postman Collection [using this link](https://drive.google.com/file/d/1X8sOAMQFt5Qpza4WmzaxgnXxAiO64cRh/view?usp=sharing)
   and import it in Postman. There are several prepared requests for easier testing of the application.
   In case you can't open the link, DM me in Slack (@telesina), and I will send you this collection.

6. Application is ready to work and be tested!
