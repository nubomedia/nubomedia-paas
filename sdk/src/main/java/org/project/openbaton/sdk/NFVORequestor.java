package org.project.openbaton.sdk;

import org.project.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.project.openbaton.catalogue.mano.descriptor.VNFForwardingGraphDescriptor;
import org.project.openbaton.catalogue.mano.descriptor.VirtualLinkDescriptor;
import org.project.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.project.openbaton.catalogue.nfvo.Configuration;
import org.project.openbaton.catalogue.nfvo.NFVImage;
import org.project.openbaton.catalogue.nfvo.VimInstance;
import org.project.openbaton.sdk.api.util.AbstractRestAgent;

/**
 * OpenBaton api requestor. Can be extended with security features to provide instances only only to granted requestors.
 * The Class is implemented in a static way to avoid any dependencies to spring and to create a corresponding small lib size.
 */
public final class NFVORequestor {


	private static RequestFactory factory;

	public NFVORequestor(String version){
		this.factory = RequestFactory.getInstance(null,null, "localhost", "8080", version);
	}

	/**
	 * The public constructor taking as params
	 * @param username: the NFVO username
	 * @param password: the NFVO password
	 * @param version: the NFVO api url
	 */
	public NFVORequestor(String username, String password, String version) {
		this.factory = RequestFactory.getInstance(username,password, "localhost", "8080", version);
	}

	public NFVORequestor(String username, String password, String nfvoIp, String nfvoPort, String version) {
		this.factory = RequestFactory.getInstance(username,password, nfvoIp, nfvoPort, version);
	}

	/**
	 * Gets the configuration requester
	 *
	 * @return configurationRequest: The (final) static configuration requester
	 */
	public AbstractRestAgent<Configuration> getConfigurationAgent() {
		return factory.getConfigurationAgent();
	}

	/**
	 * Gets the image requester
	 *
	 * @return image: The (final) static image requester
	 */
	public AbstractRestAgent<NFVImage> getImageAgent() {
		return factory.getImageAgent();
	}

	/**
	 * Gets the networkServiceDescriptor requester
	 *
	 * @return networkServiceDescriptorRequest: The (final) static networkServiceDescriptor requester
	 */
	public AbstractRestAgent<NetworkServiceDescriptor> getNetworkServiceDescriptorAgent() {
		return factory.getNetworkServiceDescriptorAgent();
	}
	/**
	 * Gets the networkServiceRecord requester
	 *
	 * @return networkServiceRecordRequest: The (final) static networkServiceRecord requester
	 */

	public AbstractRestAgent<NetworkServiceRecord> getNetworkServiceRecordAgent() {
		return factory.getNetworkServiceRecordAgent();
	}

	/**
	 * Gets the vimInstance requester
	 *
	 * @return vimInstanceRequest: The (final) static vimInstance requester
	 */
	public AbstractRestAgent<VimInstance> getVimInstanceAgent() {
		return factory.getVimInstanceAgent();
	}

	/**
	 * Gets the virtualLink requester
	 *
	 * @return virtualLinkRequest: The (final) static virtualLink requester
	 */
	public AbstractRestAgent<VirtualLinkDescriptor> getVirtualLinkAgent() {
		return factory.getVirtualLinkAgent();
	}

	/**
	 * Gets the VNFFG requester
	 *
	 * @return vNFFGRequest: The (final) static vNFFG requester
	 */
	public AbstractRestAgent<VNFForwardingGraphDescriptor> getVNFFGAgent() {
		return factory.getVNFForwardingGraphAgent();
	}

}
