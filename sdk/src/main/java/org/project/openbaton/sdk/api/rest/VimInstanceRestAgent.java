package org.project.openbaton.sdk.api.rest;

import org.project.openbaton.catalogue.nfvo.VimInstance;
import org.project.openbaton.sdk.api.util.AbstractRestAgent;

/**
 * OpenBaton viminstance(datacenter)-related api requester.
 */
public class VimInstanceRestAgent extends AbstractRestAgent<VimInstance> {

	/**
	 * Create a VimInstance requester with a given url path
	 * @param username
	 * @param password
	 */
	public VimInstanceRestAgent(String username, String password, String nfvoIp, String nfvoPort, String path, String version) {
		super(username, password, nfvoIp, nfvoPort, path, version, VimInstance.class);
	}
}
