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
//
//	/**
//	 * Adds a new Configuration to the Configurations repository
//	 *
//	 * @param configuration
//	 * @return configuration
//	 */
//	public String create(final File configuration) throws SDKException {
//		return requestPost(configuration);
//	}
//
//	/**
//	 * Removes the Configuration from the Configurations repository
//	 *
//	 * @param id
//	 *            : the id of configuration to be removed
//	 */
//	public void delete(final String id) throws SDKException {
//		requestDelete(id);
//	}
//
//	/**
//	 * Returns the list of the Configurations available
//	 *
//	 * @return List<Configuration>: The list of Configurations available
//	 */
//	public String findAll() throws SDKException {
//		return requestGet(null);
//	}
//
//	/**
//	 * Returns the Configuration selected by id
//	 *
//	 * @param id
//	 *            : The id of the Configuration
//	 * @return Configuration: The Configuration selected
//	 */
//	public String findById(final String id) throws SDKException {
//		return requestGet(id);
//	}
//
//	/**
//	 * Updates the Configuration
//	 *
//	 * @param configuration
//	 *            : The Configuration to be updated
//	 * @param id
//	 *            : The id of the Configuration
//	 * @return Configuration The Configuration updated
//	 */
//	public String update(final Configuration configuration, final String id) throws SDKException {
//		return requestPut(id, configuration);
//	}

}
