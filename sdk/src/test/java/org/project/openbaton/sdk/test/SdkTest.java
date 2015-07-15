package org.project.openbaton.sdk.test;

import org.junit.Test;
import org.project.openbaton.catalogue.mano.common.DeploymentFlavour;
import org.project.openbaton.catalogue.nfvo.NFVImage;
import org.project.openbaton.catalogue.nfvo.Network;
import org.project.openbaton.catalogue.nfvo.Subnet;
import org.project.openbaton.catalogue.nfvo.VimInstance;
import org.project.openbaton.sdk.NFVORequestor;
import org.project.openbaton.sdk.api.exception.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lto on 03/07/15.
 */

public class SdkTest {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void createTest() throws SDKException {
        NFVORequestor requestor = new NFVORequestor("admin","admin","localhost","8080", "1");

        VimInstance vimInstance = createVimInstance();

        VimInstance res = (VimInstance) requestor.abstractRestAgent(VimInstance.class,"/datacenters").create(vimInstance);

        log.debug(""+res);

    }

    private VimInstance createVimInstance() {
        VimInstance vimInstance = new VimInstance();
        vimInstance.setName("vim_instance");
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

    public static void main(String[] args) throws SDKException {
        new SdkTest().createTest();
    }
}
