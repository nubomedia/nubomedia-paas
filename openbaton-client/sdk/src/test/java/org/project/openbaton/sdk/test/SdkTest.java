package org.project.openbaton.sdk.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;
import org.project.openbaton.catalogue.mano.common.DeploymentFlavour;
import org.project.openbaton.catalogue.mano.common.Security;
import org.project.openbaton.catalogue.mano.common.VNFDependency;
import org.project.openbaton.catalogue.mano.common.VNFRecordDependency;
import org.project.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.project.openbaton.catalogue.mano.descriptor.PhysicalNetworkFunctionDescriptor;
import org.project.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.project.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.project.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.project.openbaton.catalogue.mano.record.PhysicalNetworkFunctionRecord;
import org.project.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.openbaton.catalogue.nfvo.NFVImage;
import org.project.openbaton.catalogue.nfvo.Network;
import org.project.openbaton.catalogue.nfvo.Subnet;
import org.project.openbaton.catalogue.nfvo.VimInstance;
import org.project.openbaton.sdk.NFVORequestor;
import org.project.openbaton.sdk.api.exception.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lto on 03/07/15.
 */

public class SdkTest {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private VimInstance vimInstance;
    private VimInstance res;
    private final static String descriptorFileName = "../../descriptors/network_service_descriptors/NetworkServiceDescriptor-with-dependencies.json";
    @Test
    public void createTest() throws SDKException, FileNotFoundException {

        NFVORequestor requestor = new NFVORequestor("1");

        vimInstance = createVimInstance();
        res = (VimInstance) requestor.abstractRestAgent(VimInstance.class,"/datacenters").create(vimInstance);
        log.debug("Result is: "+res);

//        NetworkServiceDescriptor networkServiceDescriptor = createNetworkServiceDescriptor();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson mapper = gsonBuilder.create();
        NetworkServiceDescriptor networkServiceDescriptor = mapper.fromJson(new FileReader(descriptorFileName), NetworkServiceDescriptor.class);
        log.debug("Sending: " + networkServiceDescriptor);
        NetworkServiceDescriptor res2 = requestor.getNetworkServiceDescriptorAgent().create(networkServiceDescriptor);
        log.debug("DESCRIPTOR: "+res2);
        
        
//SECURITY//
        
//        Security  security = new Security();
//        Security res = requestor.getNetworkServiceDescriptorAgent().createSecurity(res2.getId(),security); 
//        Security  security2 = new Security();
//        Security res3 = requestor.getNetworkServiceDescriptorAgent().updateSecurity(res2.getId(),res.getId(),security2);
//        
//        log.debug("Received: " + res3.toString());
        
        
//PHYSICALNETWORKFUNCTIONDESCRIPTOR//
        
//        PhysicalNetworkFunctionDescriptor origin = new  PhysicalNetworkFunctionDescriptor();
//        PhysicalNetworkFunctionDescriptor source = requestor.getNetworkServiceDescriptorAgent().createPhysicalNetworkFunctionDescriptor(res2.getId(),origin);
//        
//        PhysicalNetworkFunctionDescriptor response = requestor.getNetworkServiceDescriptorAgent().getPhysicalNetworkFunctionDescriptor(res2.getId(),source.getId());
//        log.debug("Received: " + response.toString());
        
      
//VNFDependency//
        
//        VNFDependency  vnfDependency = new VNFDependency();
//        VNFDependency res = requestor.getNetworkServiceDescriptorAgent().createVNFDependency(res2.getId(),vnfDependency);
//        VNFDependency  vnfDependency2 = new VNFDependency();
//        VNFDependency res3 = requestor.getNetworkServiceDescriptorAgent().updateVNFD(res2.getId(),res.getId(),vnfDependency2);
//        log.debug("Received update: " + res3.toString());

        
//VIRTUAL_NETWORK_FUNCTION_DESCRIPTOR

          //CREATE//
//        VirtualNetworkFunctionDescriptor virtualNetworkFunctionDescriptor = createVNFDescriptor();         
//        VirtualNetworkFunctionDescriptor res = requestor.getNetworkServiceDescriptorAgent().createVNFD(res2.getId(), virtualNetworkFunctionDescriptor);
//        log.debug("POST_VNFDescriptor: " + res.toString());
          
          //UPDATE//
//        VirtualNetworkFunctionDescriptor res = requestor.getNetworkServiceDescriptorAgent().updateVNFD(res2.getId(), res2.getVnfd().iterator().next().getId(),response);
//        log.debug("UPDATE_VNFDescriptor: " + res.toString());

        
//NETWORK_SERVICE_RECORD
        
//       NetworkServiceRecord networkServiceRecord = requestor.getNetworkServiceRecordAgent().create(res2.getId());
//        log.debug("RECORD: "+networkServiceRecord);
//
//        VirtualNetworkFunctionRecord[] response = requestor.getNetworkServiceRecordAgent().getVirtualNetworkFunctionRecords(networkServiceRecord.getId());
//
//        for (VirtualNetworkFunctionRecord virtualNetworkFunctionRecord : response)
//            log.debug("Received: " + virtualNetworkFunctionRecord.toString());
        
//       requestor.getNetworkServiceRecordAgent().deleteVirtualNetworkFunctionRecord(networkServiceRecord.getId(), networkServiceRecord.getVnfr().iterator().next().getId());

        
//VIRTUAL_NETWORK_FUNCTION_RECORD
        
          //-CREATE VNFR//
//        VirtualNetworkFunctionRecord virtualNetworkFunctionRecord = new VirtualNetworkFunctionRecord();
//        VirtualNetworkFunctionRecord response = requestor.getNetworkServiceRecordAgent().createVNFR(networkServiceRecord.getId(), virtualNetworkFunctionRecord);
//        log.debug("Received: " + response.toString());
//        
//        //-UPDATE VNFR//
//        VirtualNetworkFunctionRecord update = new VirtualNetworkFunctionRecord();
//        requestor.getNetworkServiceRecordAgent().updateVNFR(networkServiceRecord.getId(), networkServiceRecord.getVnfr().iterator().next().getId(), update);
 
        
//VIRTUAL_NETWORK_RECORD_DEPENDENCY
        
        
          //requestor.getNetworkServiceRecordAgent().deleteVNFDependency(networkServiceRecord.getId(), networkServiceRecord.getVnf_dependency().iterator().next().getId());
//        VNFRecordDependency vnfDependency = createVNFDependency();
//        
//        VNFRecordDependency res = requestor.getNetworkServiceRecordAgent().postVNFDependency(networkServiceRecord.getId(), vnfDependency);
//        log.debug("POST_VNFD: " + res.toString());
        
//         VNFRecordDependency res = requestor.getNetworkServiceRecordAgent().updateVNFDependency(networkServiceRecord.getId(), networkServiceRecord.getVnf_dependency().iterator().next().getId(), vnfDependency);
//         log.debug("UPDAPTE_VNFD: " + res.toString());
        
           
        
        
    }
    
    private VirtualNetworkFunctionDescriptor createVNFDescriptor(){
    	
    	VirtualNetworkFunctionDescriptor virtualNetworkFunctionDescriptor = new VirtualNetworkFunctionDescriptor();
    	virtualNetworkFunctionDescriptor.setName("fokusland");
    	virtualNetworkFunctionDescriptor.setVendor("fokus");
    	
    	return virtualNetworkFunctionDescriptor;
    }
    
    private PhysicalNetworkFunctionRecord createPNFR()
    { 
    	PhysicalNetworkFunctionRecord physicalNetworkFunctionRecord = new PhysicalNetworkFunctionRecord();
    	
    	physicalNetworkFunctionRecord.setDescription("Warp 2000");
    	physicalNetworkFunctionRecord.setVendor("fokus");
    	
    	
    	return physicalNetworkFunctionRecord;
    }
    
    private VNFRecordDependency createVNFDependency(){
    	VirtualNetworkFunctionRecord source = new VirtualNetworkFunctionRecord();
    	source.setName("vnf-dummy-2");
    	
    	VirtualNetworkFunctionRecord target = new VirtualNetworkFunctionRecord();
    	target.setName("vnf-dummy-1");
    	
    	VNFRecordDependency vnfDependency = new VNFRecordDependency();
    	
    	vnfDependency.setSource(source);
    	vnfDependency.setTarget(target);
    	
    	
		return vnfDependency;
    	
    }

    private VimInstance createVimInstance() {
        VimInstance vimInstance = new VimInstance();
        vimInstance.setName("vim-instance");
        vimInstance.setType("test");
        vimInstance.setNetworks(new HashSet<Network>() {{
            Network network = new Network();
            network.setExtId("ext_id");
            network.setName("network_name");
            List<Subnet> subnets = new ArrayList<Subnet>();
            Subnet subnet = new Subnet();
            subnet.setName("subnet name");
            subnet.setCidr("cidrs");
            subnets.add(subnet);
            network.setSubnets(new HashSet<Subnet>());
            add(network);
        }});
        vimInstance.setFlavours(new HashSet<DeploymentFlavour>() {{
            DeploymentFlavour deploymentFlavour = new DeploymentFlavour();
            deploymentFlavour.setExtId("ext_id_1");
            deploymentFlavour.setFlavour_key("flavor_name");
            add(deploymentFlavour);

            deploymentFlavour = new DeploymentFlavour();
            deploymentFlavour.setExtId("ext_id_2");
            deploymentFlavour.setFlavour_key("m1.tiny");
            add(deploymentFlavour);
        }});
        vimInstance.setImages(new HashSet<NFVImage>() {{
            NFVImage image = new NFVImage();
            image.setExtId("ext_id_1");
            image.setName("ubuntu-14.04-server-cloudimg-amd64-disk1");
            add(image);

            image = new NFVImage();
            image.setExtId("ext_id_2");
            image.setName("image_name_1");
            add(image);
        }});
        return vimInstance;
    }

    private NetworkServiceDescriptor createNetworkServiceDescriptor(){
        NetworkServiceDescriptor networkServiceDescriptor = new NetworkServiceDescriptor();
        VirtualNetworkFunctionDescriptor vnfd = new VirtualNetworkFunctionDescriptor();

        vnfd.setName("" + Math.random());
        vnfd.setType("dummy");

        VirtualDeploymentUnit vdu = new VirtualDeploymentUnit();
        vdu.setVirtual_memory_resource_element("1024");
        vdu.setVirtual_network_bandwidth_resource("1000000");
        VimInstance instance = new VimInstance();
        instance.setId(null);
        instance.setName(vimInstance.getName());
        vdu.setVimInstance(instance);

        vdu.setVm_image(new HashSet<String>() {{
            add("image_name_1");
        }});
        vdu.setScale_in_out(3);
        vdu.setMonitoring_parameter(new HashSet<String>() {{
            add("cpu_utilization");
        }});
        vnfd.setVdu(new HashSet<VirtualDeploymentUnit>());
        vnfd.getVdu().add(vdu);

        networkServiceDescriptor.setVnfd(new HashSet<VirtualNetworkFunctionDescriptor>());
        networkServiceDescriptor.getVnfd().add(vnfd);

        networkServiceDescriptor.setVendor("fokus");
        networkServiceDescriptor.setVersion("1");
        return networkServiceDescriptor;
    }

    public static void main(String[] args) throws SDKException, FileNotFoundException {
        new SdkTest().createTest();
    }
}
