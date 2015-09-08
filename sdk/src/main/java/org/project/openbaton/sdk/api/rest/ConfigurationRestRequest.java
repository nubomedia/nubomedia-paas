package org.project.openbaton.sdk.api.rest;

import org.project.openbaton.catalogue.nfvo.Configuration;
import org.project.openbaton.sdk.api.util.AbstractRestAgent;

/**
 * OpenBaton configuration-related api requester.
 */
public class ConfigurationRestRequest extends AbstractRestAgent<Configuration> {

	/**
	 * Create a configuration requester with a given url path
	 *
	 * @param url
	 * 				the url path used for the api requests
	 */
	public ConfigurationRestRequest(String username, String password, String url, String  nfvoIp, String nfvoPort, String version) {
		super(username, password, nfvoIp, nfvoPort, url, version, Configuration.class);
	}
}
