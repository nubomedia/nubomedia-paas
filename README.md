<h1>Openbaton Client</h1>

<h2>Overview</h2>
Openbaton Client provides a command-line client, which enables you to access the project API through easy-to-use commands. 
For example, Openbaton Client provides the creation of a new Vim instance.
You can run the commands from the command line with the relative json that describe the Vim instance. 
Openbaton APIs are RESTful APIs, and use the HTTP protocol. They include methods, URIs, media types, and response codes.
Openbaton APIs are open-source Java clients, and can run on Linux or Mac OS X systems. 

<h2>Install the Openbaton Client</h2>

<h2>Set environment variables using the Openbaton Client properties</h2>
To set the required environment variables for the Openbaton Client, you must create an environment file cli.properties
In default mode Openbaton Client searches the environment file in /etc/Openbaton or you can specifie the path when you call a command with the prefix -c
<br>
$ openbaton.sh  -c /path/  command-name 
<br>
If the system doesn't found the environment file ask to enter the properties

<h3>Create and source the evenvironment file</h3>
In a text editor, create a file named cli.properties file and add the following authentication information:
<br>
#cli
nfvo-usr = username
nfvo-pwd = password
nfvo-ip = ip-address
nfvo-port = port-number
nfvo-version = version

