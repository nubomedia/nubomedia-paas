package org.project.openbaton.sdk.api.rest;

import org.project.openbaton.catalogue.mano.descriptor.VirtualLinkDescriptor;
import org.project.openbaton.sdk.api.util.AbstractRestAgent;

/**
 * OpenBaton VirtualLink -related api requester.
 */
public class VirtualLinkRestAgent extends AbstractRestAgent<VirtualLinkDescriptor> {
	/**
	 * Create a VirtualLink requester with a given url path
	 *
	 * @param path
	 * 				the url path used for the api requests
	 */
	public VirtualLinkRestAgent(String username, String password, String nfvoIp, String nfvoPort, String path, String version) {
		super(username, password, nfvoIp, nfvoPort, path, version, VirtualLinkDescriptor.class);
	}

}
