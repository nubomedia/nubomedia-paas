package org.project.openbaton.sdk.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.project.openbaton.catalogue.mano.common.DeploymentFlavour;
import org.project.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.project.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.project.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.project.openbaton.catalogue.mano.record.NetworkServiceRecord;
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
    private final static String descriptorFileName = "../../descriptors/network_service_descriptors/NetworkServiceDescriptor.json";
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

        NetworkServiceRecord networkServiceRecord = requestor.getNetworkServiceRecordAgent().create(res2.getId());
        log.debug("RECORD: "+networkServiceRecord);

        VirtualNetworkFunctionRecord[] response = requestor.getNetworkServiceRecordAgent().getVirtualNetworkFunctionRecords(networkServiceRecord.getId());

        for (VirtualNetworkFunctionRecord virtualNetworkFunctionRecord : response)
            log.debug("Received: " + virtualNetworkFunctionRecord.toString());
        
        requestor.getNetworkServiceRecordAgent().deleteVirtualNetworkFunctionRecord(networkServiceRecord.getId(), networkServiceRecord.getVnfr().iterator().next().getId());

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
