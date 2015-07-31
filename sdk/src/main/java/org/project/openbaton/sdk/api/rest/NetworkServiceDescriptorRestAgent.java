package org.project.openbaton.sdk.api.rest;

import org.project.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.project.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.project.openbaton.sdk.api.annotations.Help;
import org.project.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.sdk.api.util.AbstractRestAgent;

import java.io.File;

/**
 * OpenBaton network-service-descriptor-related api requester.
 */
public class NetworkServiceDescriptorRestAgent extends AbstractRestAgent<NetworkServiceDescriptor> {

	/**
	 * Create a NetworkServiceDescriptor requester with a given url path
	 *
	 * @param nfvoIp
	 * 				the url path used for the api requests
	 */
	public NetworkServiceDescriptorRestAgent(String username, String password, String nfvoIp, String nfvoPort, String path, String version) {
		super(username, password, nfvoIp, nfvoPort, path, version, NetworkServiceDescriptor.class);
	}

	/**
	 * Return the list of VirtualNetworkFunctionDescriptor into a NSD with id
	 *
	 * @param id
	 *            : The id of NSD
	 * @return List<VirtualNetworkFunctionDescriptor>: The List of
	 *         VirtualNetworkFunctionDescriptor into NSD
	 */
	@Help(help = "Get all the VirtualNetworkFunctionDescriptors of a NetworkServiceDescriptor with specific id")
	public VirtualNetworkFunctionDescriptor[] getVirtualNetworkFunctionDescriptors(final String networkServiceDescriptor_id) throws SDKException {
		String url = networkServiceDescriptor_id + "/vnfdescriptors";
		return (VirtualNetworkFunctionDescriptor[]) requestGetAll(url,VirtualNetworkFunctionDescriptor.class);
		
	}

	/**
	 * Return a VirtualNetworkFunctionDescriptor into a NSD with id
	 *
	 * @param id
	 *            : The id of NSD
	 * @param id_vfn
	 *            : The id of the VNF Descriptor
	 * @return List<VirtualNetworkFunctionDescriptor>: The List of
	 *         VirtualNetworkFunctionDescriptor into NSD
	 */
	@Help(help = "Get the VirtualNetworkFunctionDescriptor of a NetworkServiceDescriptor with specific id")
	public VirtualNetworkFunctionDescriptor getVirtualNetworkFunctionDescriptor(final String networkServiceDescriptor_id, final String id_vfn) throws SDKException {
		String url = networkServiceDescriptor_id + "/vnfdescriptors" + "/" + id_vfn;
 	    return (VirtualNetworkFunctionDescriptor) requestGetWithStatusAccepted(url,VirtualNetworkFunctionDescriptor.class);
		
	}

	/**
	 * Delete the VirtualNetworkFunctionDescriptor
	 *
	 * @param id
	 *            : The id of NSD
	 * @param id_vfn
	 *            : The id of the VNF Descriptor
	 */
	@Help(help = "Delete the VirtualNetworkFunctionDescriptor of a NetworkServiceDescriptor with specific id")
	public void deleteVirtualNetworkFunctionDescriptors(final String networkServiceDescriptor_id, final String id_vfn) throws SDKException {
		String url = networkServiceDescriptor_id + "/vnfdescriptors" + "/" + id_vfn;
		requestDelete(url);
	}

	/**
	 * Create a VirtualNetworkFunctionDescriptor
	 *
	 * @param virtualNetworkFunctionDescriptor
	 *            : : the Network Service Descriptor to be updated
	 * @param id
	 *            : The id of the networkServiceDescriptor the vnfd shall be created at
	 */
	@Help(help = "create the VirtualNetworkFunctionDescriptor of a NetworkServiceDescriptor with specific id")
	public VirtualNetworkFunctionDescriptor createVNFD(final String networkServiceDescriptor_id,final VirtualNetworkFunctionDescriptor virtualNetworkFunctionDescriptor) throws SDKException {
		String url = networkServiceDescriptor_id + "/vnfdescriptors" + "/";
		return (VirtualNetworkFunctionDescriptor) requestPost(url, virtualNetworkFunctionDescriptor);
	}

	/**
	 * Update the VirtualNetworkFunctionDescriptor
	 *
	 * @param virtualNetworkFunctionDescriptor
	 *            : : the Network Service Descriptor to be updated
	 * @param id
	 *            : The id of the (old) VNF Descriptor
	 * @param id_vfn
	 *            : The id of the VNF Descriptor
	 * @return List<VirtualNetworkFunctionDescriptor>: The updated virtualNetworkFunctionDescriptor
	 */
	@Help(help = "Update the VirtualNetworkFunctionDescriptor of a NetworkServiceDescriptor with specific id")
	public VirtualNetworkFunctionDescriptor updateVNFD(final String networkServiceDescriptor_id, final String id_vfn, final VirtualNetworkFunctionDescriptor virtualNetworkFunctionDescriptor) throws SDKException {
		String url = networkServiceDescriptor_id + "/vnfdescriptors" + "/" + id_vfn;
		return (VirtualNetworkFunctionDescriptor) requestPut(url, virtualNetworkFunctionDescriptor);
	}

	/**
	 * Return the list of VNFDependencies into a NSD
	 *
	 * @param id
	 *            : The id of the networkServiceDescriptor
	 * @return List<VNFDependency>:  The List of VNFDependency into NSD
	 */
	@Help(help = "Get all the VirtualNetworkFunctionDescriptor Dependency of a NetworkServiceDescriptor with specific id")
	public String getVNFDependencies(final String id) throws SDKException {
//		String url = this.url + "/" + id + "/vnfdependencies";
//		return requestGetWithStatusAccepted(url);
		return null;
	}

	/**
	 * Return a VNFDependency into a NSD
	 *
	 * @param id
	 *            : The id of the VNF Descriptor
	 * @param id_vnfd
	 *            : The VNFDependencies id
	 * @return VNFDependency:  The List of VNFDependency into NSD
	 */
	@Help(help = "get the VirtualNetworkFunctionDescriptor dependency of a NetworkServiceDescriptor with specific id")
	public String getVNFDependency(final String id, final String id_vnfd) throws SDKException {
//		String url = this.url + "/" + id + "/vnfdependencies" + "/" + id_vnfd;
//		return requestGetWithStatusAccepted(url);
		return null;
	}

	/**
	 * Delets a VNFDependency
	 *
	 * @param id
	 *            : The id of the networkServiceDescriptor
	 * @param id_vnfd
	 *            : The id of the VNFDependency
	 */
	@Help(help = "Delete the VirtualNetworkFunctionDescriptor dependency of a NetworkServiceDescriptor with specific id")
	public void deleteVNFDependency(final String id, final String id_vnfd) throws SDKException {
//		String url = this.url + "/" + id + "/vnfdependencies" + "/" + id_vnfd;
//		requestDelete(url);
	}

	/**
	 * Create a VNFDependency
	 *
	 * @param vnfDependency
	 *            : The VNFDependency to be updated
	 * @param id
	 *            : The id of the networkServiceDescriptor
	 */
	@Help(help = "Create the VirtualNetworkFunctionDescriptor dependency of a NetworkServiceDescriptor with specific id")
	public String createVNFDependency(final File vnfDependency, final String id) throws SDKException {
//		String url = this.url + "/" + id + "/vnfdependencies" + "/";
//		return requestPost(url, vnfDependency);
		return null;
	}

	/**
	 * Update the VNFDependency
	 *
	 * @param vnfDependency
	 *            : The VNFDependency to be updated
	 * @param id
	 *            : The id of the networkServiceDescriptor
	 * @param id_vnfd
	 *            : The id of the VNFDependency
	 * @return The updated VNFDependency
	 */
	@Help(help = "Update the VirtualNetworkFunctionDescriptor dependency of a NetworkServiceDescriptor with specific id")
	public String updateVNFD(final File vnfDependency, final String id, final String id_vnfd) throws SDKException {
//		String url = this.url + "/" + id + "/vnfdependencies" + "/" + id_vnfd;
//		return requestPut(url, vnfDependency);
		return null;
	}

	/**
	 * Return the list of PhysicalNetworkFunctionDescriptor into a NSD with id
	 *
	 * @param id
	 *            : The id of NSD
	 * @return List<PhysicalNetworkFunctionDescriptor>: The List of
	 *         PhysicalNetworkFunctionDescriptor into NSD
	 *
	 */
	@Help(help = "Get all the PhysicalNetworkFunctionDescriptors of a NetworkServiceDescriptor with specific id")
	public String getPhysicalNetworkFunctionDescriptors(final String id) throws SDKException {
//		String url = this.url + "/" + id + "/pnfdescriptors";
//		return requestGetWithStatusAccepted(url);
		return null;
	}

	/**
	 * Returns the PhysicalNetworkFunctionDescriptor into a NSD with id
	 *
	 * @param id
	 *            : The NSD id
	 * @param id_pnf
	 *            : The PhysicalNetworkFunctionDescriptor id
	 * @return PhysicalNetworkFunctionDescriptor: The
	 *         PhysicalNetworkFunctionDescriptor selected
	 *
	 */
	@Help(help = "Get the PhysicalNetworkFunctionDescriptor of a NetworkServiceDescriptor with specific id")
	public String getPhysicalNetworkFunctionDescriptor(final String id, final String id_pnf) throws SDKException {
//		String url = this.url + "/" + id + "/pnfdescriptors" + "/" + id_pnf;
//		return requestGetWithStatusAccepted(url);
		return null;
	}

	/**
	 * Delete the PhysicalNetworkFunctionDescriptor with the id_pnf
	 *
	 * @param id
	 *            : The NSD id
	 * @param id_pnf
	 *            : The PhysicalNetworkFunctionDescriptor id
	 */
	@Help(help = "Delete the PhysicalNetworkFunctionDescriptor of a NetworkServiceDescriptor with specific id")
	public void deletePhysicalNetworkFunctionDescriptor(final String id, final String id_pnf) throws SDKException {
//		String url = this.url + "/" + id + "/pnfdescriptors" + "/" + id_pnf;
//		requestDelete(url);
	}

	/**
	 * Store the PhysicalNetworkFunctionDescriptor
	 *
	 * @param pnf
	 *            : The PhysicalNetworkFunctionDescriptor to be stored
	 * @param id
	 *            : The NSD id
	 * @param id_pnf
	 *            : The PhysicalNetworkFunctionDescriptor id
	 * @return PhysicalNetworkFunctionDescriptor: The PhysicalNetworkFunctionDescriptor stored
	 */
	@Help(help = "Create the PhysicalNetworkFunctionDescriptor of a NetworkServiceDescriptor with specific id")
	public String createPhysicalNetworkFunctionDescriptor(final File pnf, final String id, final String id_pnf) throws SDKException {
//		String url = this.url + "/" + id + "/pnfdescriptors" + "/" + id_pnf;
//		return requestPost(url, pnf);
		return null;
	}

	/**
	 * Update the PhysicalNetworkFunctionDescriptor
	 *
	 * @param pnf
	 *            : The PhysicalNetworkFunctionDescriptor to be edited
	 * @param id
	 *            : The NSD id
	 * @param id_pnf
	 *            : The PhysicalNetworkFunctionDescriptor id
	 * @return PhysicalNetworkFunctionDescriptor: The
	 *         PhysicalNetworkFunctionDescriptor edited
	 * @
	 */
	@Help(help = "Update the PhysicalNetworkFunctionDescriptor of a NetworkServiceDescriptor with specific id")
	public String updatePNFD(final File pnf, final String id, final String id_pnf) throws SDKException {
//		String url = this.url + "/" + id + "/pnfdescriptors" + "/" + id_pnf;
//		return requestPut(url, pnf);
		return null;
	}

	/**
	 * Return the Security into a NSD
	 *
	 * @param id
	 *            : The id of NSD
	 * @return Security: The Security of PhysicalNetworkFunctionDescriptor into
	 *         NSD
	 */
	@Help(help = "Get all the Security of a NetworkServiceDescriptor with specific id")
	public String getSecurities(final String id) throws SDKException {
//		String url = this.url + "/" + id + "/security";
//		return requestGetWithStatusAccepted(url);
		return null;
	}

	/**
	 * Return the Security with the id_s
	 *
	 * @param id
	 *            : The NSD id
	 * @param id_s
	 *            : The Security id
	 * @return Security: The Security selected by id_s
	 */
	@Help(help = "Get the Security of a NetworkServiceDescriptor with specific id")
	public String getSecurity(final String id, final String id_s) throws SDKException {
//		String url = this.url + "/" + id + "/security" + "/" + id_s;
//		return requestGetWithStatusAccepted(url);
		return null;
	}

	/**
	 * Delete the Security with the id_s
	 *
	 * @param id
	 *            : The NSD id
	 * @param id_s
	 *            : The Security id
	 * @
	 */
	@Help(help = "Delete the Security of a NetworkServiceDescriptor with specific id")
	public void deleteSecurity(final String id, final String id_s) throws SDKException {
//		String url = this.url + "/" + id + "/security" + "/" + id_s;
//		requestDelete(url);
	}

	/**
	 * Store the Security into NSD
	 *
	 * @param security
	 *            : The Security to be stored
	 * @param id
	 *            : The id of NSD
	 * @return Security: The Security stored
	 */
	@Help(help = "create the Security of a NetworkServiceDescriptor with specific id")
	public String createSecurity(final File security, final String id) throws SDKException {
//		String url = this.url + "/" + id + "/security" + "/";
//		return requestPost(url, security);
		return null;
	}

	/**
	 * Update the Security into NSD
	 *
	 * @param security
	 *            : The Security to be stored
	 * @param id
	 *            : The id of NSD
	 * @param id_s
	 *            : The security id
	 * @return Security: The Security stored
	 */
	@Help(help = "Update the Security of a NetworkServiceDescriptor with specific id")
	public String updateSecurity(final File security, final String id, final String id_s) throws SDKException {
//		String url = this.url + "/" + id + "/security" + "/" + id_s;
//		return requestPut(url, security);
		return null;
	}

	/**
	 * Create a record into NSD
	 *
	 * @param networkServiceDescriptor
	 *            : the networkServiceDescriptor JSON File
	 */
	@Help(help = "create NetworkServiceRecord from a NetworkServiceDescriptor")
	public String createRecord(final File networkServiceDescriptor) throws SDKException {
//		String url = this.url + "/records";
//		return requestPost(url, networkServiceDescriptor);
		return null;
	}
}
