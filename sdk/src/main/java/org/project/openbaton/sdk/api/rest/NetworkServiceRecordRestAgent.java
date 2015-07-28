package org.project.openbaton.sdk.api.rest;

import org.project.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.project.openbaton.sdk.api.annotations.Help;
import org.project.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.sdk.api.util.AbstractRestAgent;

import java.io.File;

/**
 * OpenBaton image-related commands api requester.
 */
public class NetworkServiceRecordRestAgent extends AbstractRestAgent<NetworkServiceRecord> {

	/**
	 * Create a NetworkServiceRecord requester with a given url path
	 *
	 * @param path
	 * 				the url path used for the api requests
	 */
	public NetworkServiceRecordRestAgent(String username, String password, String nfvoIp, String nfvoPort, String path, String version) {
		super(username, password, nfvoIp, nfvoPort, path, version, NetworkServiceRecord.class);
	}

	@Help(help = "Create NetworkServiceRecord from NetworkServiceDescriptor id")
	public NetworkServiceRecord create(final String id) throws SDKException {
		String result = this.requestPost(id);
		return this.mapper.fromJson(result, NetworkServiceRecord.class);
	}

	/**
	 *
	 */
	@Help(help = "Get all the VirtualNetworkFunctionRecords of NetworkServiceRecord with specific id")
	public String getVirtualNetworkFunctionRecords(final String id) throws SDKException {
//		String url = this.url + "/" + id + "/vnfrecords";
//		return requestGetWithStatusAccepted(url);
		return null;
	}

	/**
	 *
	 */
	@Help(help = "Get the VirtualNetworkFunctionRecord of NetworkServiceRecord with specific id")
	public String getVirtualNetworkFunctionRecord(final String id, final String id_vnf) throws SDKException {
//		String url = this.url + "/" + id + "/vnfrecords" + "/" + id_vnf;
//		return requestGetWithStatusAccepted(url);
		return null;
	}

	/**
	 *
	 */
	@Help(help = "I dont know...")
	public void deleteVirtualNetworkFunctionDescriptor(final String id, final String id_vnf) throws SDKException {
//		String url = this.url + "/" + id + "/vnfrecords" + "/" + id_vnf;
//		requestDelete(url);
	}

	/**
	 *
	 */
	@Help(help = "create VirtualNetworkFunctionRecord")
	public String createVNFR(final File networkServiceRecord, final String id) throws SDKException {
//		String url = this.url + "/" + id + "/vnfrecords" + "/";
//		return requestPost(url, networkServiceRecord);
		return null;
	}

	/**
	 *
	 */
	@Help(help = "I don't know..")
	public String updateVNF(final File networkServiceRecord, final String id, final String id_vnf) throws SDKException {
//		String url = this.url + "/" + id + "/vnfrecords" + "/" + id_vnf;
//		return requestPut(url, networkServiceRecord);
		return null;
	}

	/**
	 *
	 */
	@Help(help = "Get all the VirtualNetworkFunctionRecord dependencies of NetworkServiceRecord with specific id")
	public String getVNFDependencies(final String id) throws SDKException {
//		String url = this.url + "/" + id + "/vnfdependencies";
//		return requestGetWithStatusAccepted(url);
		return null;
	}

	/**
	 *
	 */
	@Help(help = "Get the VirtualNetworkFunctionRecord Dependency of a NetworkServiceRecord with specific id")
	public String getVNFDependency(final String id, final String id_vnfr) throws SDKException {
//		String url = this.url + "/" + id + "/vnfdependencies" + "/" + id_vnfr;
//		return requestGetWithStatusAccepted(url);
		return null;
	}

	/**
	 *
	 */
	@Help(help = "Delete the VirtualNetworkFunctionRecord Dependency of a NetworkServiceRecord with specific id")
	public void deleteVNFDependency(final String id, final String id_vnfd) throws SDKException {
//		String url = this.url + "/" + id + "/vnfdependencies" + "/" + id_vnfd;
//		requestDelete(url);
	}

	/**
	 *
	 */
	@Help(help = "Create the VirtualNetworkFunctionRecord Dependency of a NetworkServiceRecord with specific id")
	public String postVNFDependency(final File vnfDependency, final String id) throws SDKException {
//		String url = this.url + "/" + id + "/vnfdependencies" + "/";
//		return requestPost(url, vnfDependency);
		return null;
	}

	/**
	 *
	 */
	@Help(help = "I don't know")
	public String updateVNFD(final File vnfDependency, final String id, final String id_vnfd) throws SDKException {
//		String url = this.url + "/" + id + "/vnfdependencies" + "/" + id_vnfd;
		return null;
//		return requestPut(url, vnfDependency);
	}

	/**
	 * Returns the set of PhysicalNetworkFunctionRecord into a NSD with id
	 *
	 * @param id
	 *            : The id of NSD
	 * @return Set<PhysicalNetworkFunctionRecord>: The Set of
	 *         PhysicalNetworkFunctionRecord into NSD
	 */
	@Help(help = "Get all the PhysicalNetworkFunctionRecords of a specific NetworkServiceRecord with id")
	public String getPhysicalNetworkFunctionRecords(final String id) throws SDKException {
//		String url = this.url + "/" + id + "/pnfrecords";
		return null;
//		return requestGetWithStatusAccepted(url);
	}

	/**
	 * Returns the PhysicalNetworkFunctionRecord
	 *
	 * @param id
	 *            : The NSD id
	 * @param id_pnf
	 *            : The PhysicalNetworkFunctionRecord id
	 * @return PhysicalNetworkFunctionRecord: The PhysicalNetworkFunctionRecord
	 *         selected
	 */
	@Help(help = "Get the PhysicalNetworkFunctionRecord of a NetworkServiceRecord with specific id")
	public String getPhysicalNetworkFunctionRecord(final String id, final String id_pnf) throws SDKException {
//		String url = this.url + "/" + id + "/pnfrecords" + "/" + id_pnf;
//		return requestGetWithStatusAccepted(url);
		return null;
	}

	/**
	 * Deletes the PhysicalNetworkFunctionRecord with the id_pnf
	 *
	 * @param id
	 *            : The NSD id
	 * @param id_pnf
	 *            : The PhysicalNetworkFunctionRecord id
	 */
	@Help(help = "Delete the PhysicalNetworkFunctionRecord of a NetworkServiceRecord with specific id")
	public void deletePhysicalNetworkFunctionRecord(final String id, final String id_pnf) throws SDKException {
//		String url = this.url + "/" + id + "/pnfrecords" + "/" + id_pnf;
//		requestDelete(url);
	}

	/**
	 * Stores the PhysicalNetworkFunctionRecord
	 *
	 * @param physicalNetworkFunctionRecord
	 *            : The PhysicalNetworkFunctionRecord to be stored
	 * @param id
	 *            : The NSD id
	 * @return PhysicalNetworkFunctionRecord: The PhysicalNetworkFunctionRecord
	 *         stored
	 */
	@Help(help = "Create the PhysicalNetworkFunctionRecord of a NetworkServiceRecord with specific id")
	public String postPhysicalNetworkFunctionRecord(final File physicalNetworkFunctionRecord, final String id) throws SDKException {
//		String url = this.url + "/" + id + "/pnfrecords" + "/";
//		return requestPost(url, physicalNetworkFunctionRecord);
		return null;
	}

	/**
	 * Edits the PhysicalNetworkFunctionRecord
	 *
	 * @param physicalNetworkFunctionRecord
	 *            : The PhysicalNetworkFunctionRecord to be edited
	 * @param id
	 *            : The NSD id
	 * @return PhysicalNetworkFunctionRecord: The PhysicalNetworkFunctionRecord
	 *         edited
	 */
	@Help(help = "Update the PhysicalNetworkFunctionRecord of a NetworkServiceRecord with specific id")
	public String updatePNFD(final File physicalNetworkFunctionRecord, final String id, final String id_pnf) throws SDKException {
//		String url = this.url + "/" + id + "/pnfrecords" + "/" + id_pnf;
//		return requestPut(url, physicalNetworkFunctionRecord);
		return null;
	}

}
