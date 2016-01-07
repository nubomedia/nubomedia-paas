Nubomedia REST API
=============================

This is a short guide to create and delete application on Nubomedia platform using REST API.

Create
---------------------

Create, build, deploy and make it avaliable is possible through an HTTP POST on http://80.96.122.80:8081/api/v1/nubomedia/paas/app sending a json file with these fields as body:

* gitURL: the gitRepository with application code (and eventually the dockerimage)
* appName: the simple application name
* projectName: the namespace where all application made by that developer will be deployed
* protocols: a list of protocols used from target application
* ports: the external ports that application will expose for external requests
* targetPorts: ports used normally by application
* flavor: size in terms of computational capability of media server required by application
* replicasNumber: maximum replicas of application

The response is a json object with two fields:

* id: the id of the application;
* route: the URL of the application visible from outside (typically appName.paas.nubomedia.eu)



Delete
-----------
The wipe of an existing application is possible using a HTTP DELETE on http://80.96.122.80:8081/api/v1/nubomedia/paas/app/{id} where id is the id of your application
