# Nubomedia PaaS Manager

This project is part of the [NUBOMEDIA](http://www.nubomedia.eu/) research initiative.

The NUBOMEDIA PaaS manager is the manager for PaaS Platform that exposes REST API to allow NUBOMEDIA users to build and deploy applications on the NUBOMEDIA Platform.
This is a short guide to deploy and install the NUBOMEDIA PaaS Manager.

## Getting Started

The PaaS Manager is implemented in java using the [spring.io] framework. This manager requires that all infrastructure is running:

* [Openbaton][orchestrator] is up and running
* The [MS-VNFM][vnfm] is up, running and registered to Openbaton
* The [PaaS][openshift] is [configured][os-config] and running (this API are tested with version 1.1 of Openshift binaries)
* A keystore with the PaaS SSL certificates exists and is available on the PaaS API machine (you can use this [guide][keytool] or this [software][portecle] to do that)


## Installation

You can install the NUBOMEDIA PaaS manager either automatically by downloading and executing the bootstrap or manually.
Both options are described below.

### Automatic installation and start

The [bootstrap] repository contains the script to install and start the NUBOMEDIA PaaS Manager automatically.
In order to do it you can run the following command:

```bash
bash <(curl -fsSkl https://raw.githubusercontent.com/nubomedia/nubomedia-paas/master/bootstrap)
```

At the end of the installation process the NUBOMEDIA PaaS Manager dashboard is reachable at localhost:8081

Afterwards the source code of the NUBOMEDIA PaaS Manager is located in `/opt/nubomedia/nubomedia-paas`.
Check if the NFVO and/or the MS-VNFM is not installed and started, otherwise the NUBOMEDIA PaaS Manager start will fail and you need to start it manually when the NFVO and the MS-VNFM are up and running.

In case the NUBOMEDIA PaaS Manager are already installed you can start them manually using the provided script as described [here](#start-the-nubomedia-paas-manager-manually)

### Install the NUBOMEDIA PaaS Manager manually

1. Download the source code from git:

```bash
git clone https://github.com/fhg-fokus-nubomedia/nubomedia-paas.git
```

2. Change the properties file to reflect your infrastructure configuration:

```bash
vim NUBOMEDIA-paas/src/main/resources/paas.properties
```

3. Run the provided script to create the base folder for properties file (and copy the file in it)

```bash
cd nubomedia-paas/
./nubomedia-paas.sh init
```

**NOTE** if you are not root it will ask for sudo password

3. Compile the code using the provided script

```bash
cd nubomedia-paas/
./nubomedia-paas.sh compile
```

### Start the NUBOMEDIA PaaS Manager Manually

The NUBOMEDIA PaaS Manager can to started by executing the following command (in the directory nubomedia-paas)

```bash
./nubomedia-paas.sh start
```

Once the NUBOMEDIA PaaS Manager is started you can access the screen session that is in another window with the ms-vnfm running:

```bash
screen -r nubomedia
```
and move to the windows named `nubomedia-paas`

## Configuration

The configuration can to fount in `/etc/nubomedia/paas.properties`.

Here you can configure:

* PaaS address
* OpenShift
* NFVO
* VNF Manager (MS-VNFM aka EMM)
* Vim (according to [ETSI][NFV MANO] Specification) properties
* KMS image
* RabbitMQ
* Database
* Log Levels

After changing any configuration, you need to restart.

### Configuration parameters

The following table provides you with descriptions of the main properties to be modified in order to configure the PaaS Manager:

| parameter     | default value     | description                                               |
| ------------- |-------            |----------                                                 |
| paas.security.admin.password | nub0m3d14 | defines the admin password for the PaaS Manager |
| paas.security.project.name | default | defines the default project for the PaaS Manager |
| paas.port | 8081 | defines the URL of the PaaS Manager itself (used internally)   |
| openshift.baseURL | https://localhost:8443 | defines where your OpenShift instance is running |
| openshift.domainName |your.domain.com | defines your domain used to create the route|
| openshift.project | nubomedia | defines the project to be used in OpenShift (must exist)|
| openshift.token |-        | configure your token used to access the OpenShift instance (produced by OpenShift in [this way](#create-openshift-token))|
| kms.image | nubomedia/kurento-media-server | Specifies the image to be used for running KMS |
| nfvo.ip | localhost | defines the ip where the NFVO is running |
| nfvo.port| port | defines the port on which Marketplace is reachable |
| nfvo.username | admin | defines the username to be used for the NFVO |
| nfvo.password | openbaton | defines the password to be used for the NFVO |
| nfvo.project.name | nubomedia | defines the project to be used |
| vnfm.ip | localhost | defines the ip where the VNFM is running |
| vnfm.port | 9000 | defines the port on which VNFM is reachable |
| marketplace.ip | defines the ip where the Marketplace is running  | |
| marketplace.port| defines the port on which Marketplace is reachable | |
| vim.authURL | http://localhost:5000/v2.0 | defines the authentication URL of the cloud infrastructure to be used |
| vim.name | nubomedia-vim | defines the name of the VIM instance (used as an identifier) |
| vim.tenant | nubomedia | defines the tenant to be used for allocating resources in the cloud infrastructure |
| vim.username | nubomedia | defines the username to be used for authorizing against the cloud infrastructure |
| vim.password | nubomedia | defines the password to be used for authorizing against the cloud infrastructure |
| vim.keypair | nubomedia | defines the keypair to be used to access the VMs (must exist in cloud infrastructure)|
| vim.type | openstack | defines the type of cloud infrastructure |
| rabbitmq.host | localhost | defines the host where the RabbitMQ server is running |
| rabbitmq.username | admin | defines the username to authorize against the RabbitMQ server |
| rabbitmq.password | openbaton | defines the password to authorize against the RabbitMQ server |
| logging.level.* | - | defines the log levels of the specified packages |

### Create OpenShift token

The token created in this part is used internally to authorize against OpenShift.
It must be placed in the configuration file under 'openshift.token' in order to let the PaaS Manager communicate with the OpenShift server.

First you need to create a new service account by running this command from the OpenShift command line:
```bash
$ echo '{"kind":"ServiceAccount","apiVersion":"v1","metadata":{"name":"paas-user"}}' | oc create -n nubomedia -f -
serviceaccount "paas-develop" created
```

Afterwards you need to execute the following command in order to add the new service account the the project `nubomedia`
```bash
$ oc policy add-role-to-user edit --serviceaccount=paas-user -n nubomedia
```

To get the token created you can list first the names of the tokens that were added automatically during the step before.

```bash
$ oc get secrets
paas-develop-dockercfg-lejqo           kubernetes.io/dockercfg               1         23s
paas-develop-token-lljjd               kubernetes.io/service-account-token   2         23s
paas-develop-token-yt0zr               kubernetes.io/service-account-token   2         23s
```

Just execute one of the following commands to retrieve the token created. Either this:

```bash
$ oc describe secrets paas-develop-token-lljjd
Name:           paas-develop-token-lljjd
Namespace:      nubomedia
Labels:         <none>
Annotations:    kubernetes.io/service-account.name=paas-develop,kubernetes.io/service-account.uid=bd9ec0fe-48fb-11e6-b950-001a648f9cf6

Type:   kubernetes.io/service-account-token

Data
====
token:  eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJudWJvbWVkaW
EiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlY3JldC5uYW1lIjoicGFhcy1kZXZlbG9wLXRva2VuLWxsampkIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQub
mFtZSI6InBhYXMtZGV2ZWxvcCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6ImJkOWVjMGZlLTQ4ZmItMTFlNi1iOTUwLTAwMWE2NDhmOWNmNiIsInN1YiI6InN5
c3RlbTpzZXJ2aWNlYWNjb3VudDpudWJvbWVkaWE6cGFhcy1kZXZlbG9wIn0.LzBw_k6S1NnqDIy127nTRGHRVQBatiOHN0mrayo6Bg_aDk_qtxW9o8hF5_HYkE1xFQNarewHTu5F4f3ngHZG4aJ4GkHJVaPkEHI
gQGtp7pZbEJqwNuV8pPpvZmAV8zvFJvGgCBxRcyEL4tsArshcCX3D3z1vVIS5ZvDZr12qdgp-gKs1KOeLJM-B4CxE_hV43EicY3_tbyNFdlVVsPe_FYisG-KPYwqgKdkfTPuxx3WlKQ0JUgDDaPj0MqCoETVTQ0
THcJKr25lqyvzZUJm5qzPAKvaPn8xbI7lli4TjQd1ORVc3SsE4lpfUk0FADqVsLf9Fy4xeaQ3YwuKFeZhQ7B
ca.crt: 1066 bytes
```

or this:

```bash
$ oc describe secrets paas-develop-token-yt0zr
Name:           paas-develop-token-yt0zr
Namespace:      nubomedia
Labels:         <none>
Annotations:    kubernetes.io/service-account.name=paas-develop,kubernetes.io/service-account.uid=bd9ec0fe-48fb-11e6-b950-001a648f9cf6

Type:   kubernetes.io/service-account-token

Data
====
ca.crt: 1066 bytes
token:  eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJudWJvbWVkaW
EiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlY3JldC5uYW1lIjoicGFhcy1kZXZlbG9wLXRva2VuLXl0MHpyIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQub
mFtZSI6InBhYXMtZGV2ZWxvcCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6ImJkOWVjMGZlLTQ4ZmItMTFlNi1iOTUwLTAwMWE2NDhmOWNmNiIsInN1YiI6InN5
c3RlbTpzZXJ2aWNlYWNjb3VudDpudWJvbWVkaWE6cGFhcy1kZXZlbG9wIn0.dOe6d0fUkRK1gF2bjqZ6vsoic8hM0YjBoXzfg0Rn3ma_R1Vc0U0P6ytzJ8C_sPDLSE7HiLanIowKVy7AZifSDOWi53r3slxKP15
fbxw8_HunToMIR4WWIiKRjywsO184gqC1LZrmBfz0Y86-Xo91fFRMITx3rqXsHx-FkSwpNBKNFGC7cIF8Ch52LM6JXnZwazi_uP3lde3i9TdZ1tqDQKA9Eldu7Wgl3YRMiVN2xOrz4N4-vbZXYEvIQDKhCDF2QZ
Q9CJfruHrhczLeiY5yvpKfhMZ4JJxUjiDnbULtdA48GFxZszvNMpIyoX_fhqOiI3GoUFHyChcZ4B6_zZtegW
```

Copy & paste on of those tokens to your configuration file.

### Identity Management

The Identity Management includes the following entities:
* user: A user have his own credentials to login into the PaaS Manager. The user might be assigned to one or more projects with specific roles. Only the role:ADMIN has acces to all projects and can manage (create, delete) users and projects. The role:USER can manage applications in the assigned project and role:GUEST can browse only through the applications.
* project: A project has an isolated view on the applications that are running in this project. A project might be used by one or multiple projects.
* role: Roles are defining the set of actions that are permitted to execute. role:ADMIN can create and delete users and projects; and has access to all projects. role:USER can manage the applications in the assigned projects. role:GUEST can only browse through the applications of the assigned projects.

*NOTE* The password for the ADMIN is defined in the configuration file. Changing this password will be applied after a restart of the PaaS Manager.
*NOTE* admin user and admin project can not be deleted or done anything that limits the access of the admin user.
### Marketplace

The marketplace for NUBOMEDIA applications serves a store where the full configuration of applications can be stored. It is integrated directly in the NUBOMEDIA GUI. From the marketplace you can launch applications via your PaaS GUI without doing anything.
The marketplace itself is an centralized component that might be used by several PaaS Managers. In order to use it you need to define the IP and port in the configuration file.


### Front end setup
* Install nodejs, follow the link
```
  https://nodejs.org/en/download/package-manager/
```
* Install npm
```
  npm install npm -g
```
* Install gulp globally
```
  npm install --global gulp-cli
```
* In nubomedia-paas/src/main/resources/static run the followings:
```
  npm install
```
```
  bower install
```
* To compile sass run the following in .../nubomedia-paas/src/main/resources/static

**for normal css run**
```
  gulp
```
**for minified css run**
```
  gulp --production
```
the sass is compiled in nubomedia-paas/src/main/resources/static/assets/app.css

Support and Contribution
-------------------------

Need some support, wish to contribute? Then get in contact with us via our [mailinglist](mailto:nubomedia@fokus.fraunhofer.de)!

[orchestrator]:http://openbaton.github.io
[vnfm]:https://github.com/tub-nubomedia/ms-vnfm
[openshift]:https://www.openshift.org/
[os-config]:https://docs.openshift.org/latest/install_config/index.html
[portecle]:http://portecle.sourceforge.net/
[keytool]:https://docs.oracle.com/javase/tutorial/security/toolsign/rstep2.html
[spring.io]:https://spring.io/
[bootstrap]:https://raw.githubusercontent.com/fhg-fokus-nubomedia/nubomedia-paas/master/bootstrap
[NFV MANO]:http://www.etsi.org/deliver/etsi_gs/NFV-MAN/001_099/001/01.01.01_60/gs_nfv-man001v010101p.pdf

Issue tracker
-------------

Issues and bug reports should to posted to the [GitHub NUBOMEDIA-PaaS Issue List](https://github.com/fhg-fokus-nubomedia/nubomedia-paas/issues)

Licensing and distribution
--------------------------

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
