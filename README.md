# Nubomedia PaaS Manager

The Nubomedia PaaS manager are the public APIs to build and deploy applications on Nubomedia platform.
This is a short guide to deploy and install the Nubomedia PaaS API.

## Getting Started

The PaaS Manager are implemented in java using the [spring.io] framework. This manager requires that all infrastructure is running:

* [Openbaton][orchestrator] is up and running
* The [MS-VNFM][vnfm] is up, running and registered to Openbaton
* The [PaaS][openshift] is [configured][os-config] and running (this API are tested with version 1.0.5 of Openshift binaries)
* A keystore with the PaaS SSL certificates exists and is available on the PaaS API machine (you can use this [guide][keytool] or this [software][portecle] to do that)

## Installation

You can install the Nubomedia PaaS manager either automatically by downloading and executing the bootstrap or manually.
Both options are described below.

### Automatic installation and start

The [bootstrap] repository contains the script to install and start the Nubomedia PaaS Manager automatically.
In order to do it you can run the following command:

```bash
bash <(curl -fsSkl https://raw.githubusercontent.com/fhg-fokus-nubomedia/bootstrap/master/bootstrap)
```

At the end of the installation process the Nubomedia PaaS Manager dashboard is reachable at localhost:8081

Afterwards the source code of the Nubomedia PaaS Manager is located in `/opt/nubomedia/nubomedia-paas`.
If the NFVO and/or the MS-VNFM is not installed and started. Otherwise the Nubomedia PaaS API and PaaS Manager start will fail and you need to start it manually when the NFVO and the MS-VNFM are up and running.

In case the Nubomedia PaaS Manager are already installed you can start them manually using the provided script as described [here](#start-the-nubomedia-paas-api-and-paas-manager-manually)

### Install the Nubomedia PaaS Manager manually

1. Download the source code from git:

```bash
git clone https://github.com/fhg-fokus-nubomedia/nubomedia-paas.git
```

2. Change the properties file to reflect your infrastructure configuration:

```bash
vim nubomedia-paas/src/main/resources/paas.properties
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

### Start the Nubomedia PaaS Manager Manually

The Nubomedia PaaS Manager can be started by executing the following command (in the directory nubomedia-paas)

```bash
./nubomedia-paas.sh start
```

Once the Nubomedia PaaS Manager is started you can access the screen session that is in another window with the ms-vnfm running:

```bash
screen -r nubomedia
```
and move to the windows named `nubomedia-paas`

## Configuration

The configuration can be fount in `/etc/nubomedia/paas.properties`.

Here you can configure:

* PaaS address
* NFVO
* PaaS API address
* MS-VNFM
* Vim (according to [ETSI][NFV MANO] Specification) properties
* NSD image
* Database
* Log Levels

After changing any configuration, you need to restart

[orchestrator]:http://openbaton.github.io
[vnfm]:https://github.com/tub-nubomedia/ms-vnfm
[openshift]:https://www.openshift.org/
[os-config]:https://docs.openshift.org/latest/install_config/index.html
[portecle]:http://portecle.sourceforge.net/
[keytool]:https://docs.oracle.com/javase/tutorial/security/toolsign/rstep2.html
[spring.io]:https://spring.io/
[bootstrap]:https://raw.githubusercontent.com/fhg-fokus-nubomedia/bootstrap/master/bootstrap
[NFV MANO]:http://www.etsi.org/deliver/etsi_gs/NFV-MAN/001_099/001/01.01.01_60/gs_nfv-man001v010101p.pdf
