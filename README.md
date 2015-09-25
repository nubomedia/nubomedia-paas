# Openbaton Client

## Overview
Openbaton Client provides a command-line client, which enables you to access the project API through easy-to-use commands. 
For example, Openbaton Client provides the creation of a new Vim instance.

You can run the commands from the command line with the relative json that describe the Vim instance. 
Openbaton APIs are RESTful APIs, and use the HTTP protocol. 

They include methods, URIs, media types, and response codes.
Openbaton APIs are open-source Java clients, and can run on Linux or Mac OS X systems. 

## Install the Openbaton Client

## Set environment variables using the Openbaton Client properties
To set the required environment variables for the Openbaton Client, you must create an environment file cli.properties

In default mode Openbaton Client searches the environment file in /etc/Openbaton or you can specifie the path when you call a command with the prefix -c
```sh
$ openbaton.sh  -c /path/  command-name
```
If the system doesn't found the environment file ask to enter the properties

### Create and source the evenvironment file
In a text editor, create a file named cli.properties file and add the following authentication information:

* nfvo-usr = username
* nfvo-pwd = password
* nfvo-ip = ip-address
* nfvo-port = port-number
* nfvo-version = version

## Openbaton Client usage
    
    openbaton.sh [-c <path of properties file>] command-name [arg-1] [arg-2] [arg-3]
    
    openbaton.sh [-d #debug mode] command-name [arg-1] [arg-2] [arg-3]
    
    openbaton.sh command-name [help]
    
    openbaton.sh help
    

### For help on a specific openbaton command, enter:
```sh
 $ openbaton.sh COMMAND help
```

### **Vim Instance Subcommands**
* **create**
  * Create the object of type Vim Instance
* **delete**
  * Delete the object of type Vim Instance passing the id
* **update**
  * Update the object of type Vim nstance passing the new object and the id of the old object
* **findAll**
  * Find all the objects of type Vim Instance
* **findById**
  * Find the object of type Vim Instance through the id

### **Network Service Descriptor Subcommands**
* **create**
  * Create the object of type Network Service Descriptor
* **delete**
  * Delete the object of type Network Service Descriptor passing the id
* **update**
  * Update the object of type Network Service Descriptor passing the new object and the id of the old object
* **findAll**
  * Find all the objects of type Network Service Descriptor
* **findById**
  * Find the object of type Network Service Descriptor through the id
* **updatePNFD**                                            
  * Update the Physical Network FunctionDescriptor of a Network ServiceDescriptor with specific id
* **getVNFDependencies**                                  
  * Get all the Virtual Network Function Descriptor Dependency of a Network Service Descriptor with specific id
* **getVNFDependency**                                      
  * Get the VirtualNetwork Function Descriptor dependency with specific id of a Network Service Descriptor with specific id
* **deleteVNFDependency**                                   
  * Delete the Virtual Network Function Descriptor dependency of a Network Service Descriptor with specific id
* **getVirtualNetworkFunctionDescriptors**                  
  * Get all the VirtualNetworkFunctionDescriptors of a NetworkServiceDescriptor with specific id
* **updateVNFD**                                    
  * Update the VirtualNetwork Function Descriptor of a Network Service Descriptor with specific id
* **createVNFDependency**                                   
  * Create the VirtualNetworkFunctionDescriptor dependency of a NetworkServiceDescriptor with specific id
* **getVirtualNetworkFunctionDescriptor**                   
  * Get the VirtualNetworkFunctionDescriptor with specific id of a NetworkServiceDescriptor with specific id
* **deleteVirtualNetworkFunctionDescriptors**              
  * Delete the Virtual Network Function Descriptor of a Network Service Descriptor with specific id
* **createVNFD**                                        
  * Create the Virtual Network Function Descriptor of a NetworkServiceDescriptor with specific id
* **getPhysicalNetworkFunctionDescriptors**                 
  * Get all the Physical Network Function Descriptors of a Network Service Descriptor with specific id
* **getPhysicalNetworkFunctionDescriptor**                  
  * Get the Physical Network Function Descriptor with specific id of a Network Service Descriptor with specific id
* **deletePhysicalNetworkFunctionDescriptor**               
  * Delete the Physical Network Function Descriptor of a Network Service Descriptor with specific id
* **createPhysicalNetworkFunctionDescriptor**               
  * Create the Physical Network Function Descriptor of a Network Service Descriptor with specific id
* **getSecurities**                                         
  * Get all the Security of a Network Service Descriptor with specific id
* **deleteSecurity**                                       
  * Delete the Security of a NetworkServiceDescriptor with specific id
* **createSecurity**                                        
  * Create the Security of a NetworkServiceDescriptor with specific id
* **updateSecurity**                                       
  * Update the Security of a NetworkServiceDescriptor with specific id

### **Network Service Record Subcommands**
* **create**
  * Create the object of type Network Service Record
* **delete**
  * Delete the object of type Network Service Record passing the id
* **update**
  * Update the object of type Network Service Record passing the new object and the id of the old object
* **findAll**
  * Find all the objects of type Network Service Record
* **findById**
  * Find the object of type Network Service Record through the id
* **updateVNFDependency**                                       
  * Update the Virtual Network Function Record Dependency of a Network Service Record with specific id
* **getPhysicalNetworkFunctionRecords**                       
  * Get all the Physical Network Function Records of a specific Network Service Record with id
* **etPhysicalNetworkFunctionRecord**                          
  * Get the Physical Network Function Record with specific id of a Network Service Record with specific id
* **deletePhysicalNetworkFunctionRecord**                      
  * Delete the Physical Network Function Record of a Network Service Record with specific id
* **postPhysicalNetworkFunctionRecord**                        
  * Create the Physical Network Function Record of a Network Service Record with specific id
* **updatePNFD**                                                
  * Update the Physical Network Function Record of a Network Service Record with specific id
* **getVirtualNetworkFunctionRecords**                         
  * Get all the Virtual Network Function Records of Network Service Record with specific id
* **getVirtualNetworkFunctionRecord**                           
  * Get the Virtual Network Function Record with specific id of Network Service Record with specific id
* **deleteVirtualNetworkFunctionRecord**                        
  * Delete the Virtual Network Function Record of NetworkS ervice Record with specific id
* **createVNFR**                                              
  * create Virtual Network Function Record
* **updateVNFR**                                              
  * update Virtual Network Function Record
* **getVNFDependencies**                                      
  * Get all the Virtual Network Function Record dependencies of Network Service Record with specific id
* **getVNFDependency**                                         
  * Get the VirtualNetworkFunctionRecord Dependency with specific id of a Network Service Record with specific id
* **deleteVNFDependency**                                      
  * Delete the Virtual Network Function Record Dependency of a Network Service Record with specific id
* **postVNFDependency**                                         
  * Create the Virtual Network Function Record Dependency of a Network Service Record with specific id


### **Event Subcommands**
* **create**
  * Create the object of type Event
* **delete**
  * Delete the object of type Event passing the id
* **update**
  * Update the object of type Event passing the new object and the id of the old object
* **findAll**
  * Find all the objects of type Event
* **findById**
  * Find the object of type Event through the id

### **Configuration Subcommands**
* **create**
  * Create the object of type Configuration
* **delete**
  * Delete the object of type Configuration passing the id
* **update**
  * Update the object of type Configuration passing the new object and the id of the old object
* **findAll**
  * Find all the objects of type Configuration
* **findById**
  * Find the object of type Configuration through the id

### **VNFFG Subcommands**
* **create**
  * Create the object of type VNFFG
* **delete**
  * Delete the object of type VNFFG passing the id
* **update**
  * Update the object of type VNFFG passing the new object and the id of the old object
* **findAll**
  * Find all the objects of type VNFFG
* **findById**
  * Find the object of type VNFFG through the id

### **Image Subcommands**
* **create**
  * Create the object of type Image
* **delete**
  * Delete the object of type Image passing the id
* **update**
  * Update the object of type Image passing the new object and the id of the old object
* **findAll**
  * Find all the objects of type Image
* **findById**
  * Find the object of type Image through the id

### **VirtualLink Subcommands**
* **create**
  * Create the object of type VirtualLink
* **delete**
  * Delete the object of type VirtualLink passing the id
* **update**
  * Update the object of type VirtualLink passing the new object and the id of the old object
* **findAll**
  * Find all the objects of type VirtualLink
* **findById**
  * Find the object of type VirtualLink through the id