package org.project.openbaton.sdk.api.rest;

import org.project.openbaton.catalogue.mano.descriptor.VNFDependency;
import org.project.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.project.openbaton.catalogue.mano.record.PhysicalNetworkFunctionRecord;
import org.project.openbaton.catalogue.mano.record.VNFRecordDependency;
import org.project.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.openbaton.sdk.api.annotations.Help;
import org.project.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.sdk.api.util.AbstractRestAgent;

import java.util.Arrays;
import java.util.List;

/**
 * OpenBaton image-related commands api requester.
 */
public class NetworkServiceRecordRestAgent extends AbstractRestAgent<NetworkServiceRecord> {

    /**
     * Create a NetworkServiceRecord requester with a given url path
     *
     * @param path the url path used for the api requests
     */
    public NetworkServiceRecordRestAgent(String username, String password, String nfvoIp, String nfvoPort, String path, String version) {
        super(username, password, nfvoIp, nfvoPort, path, version, NetworkServiceRecord.class);
    }

    @Help(help = "Create NetworkServiceRecord from NetworkServiceDescriptor id")
    public NetworkServiceRecord create(final String id) throws SDKException {
        String result = this.requestPost("/" + id);
        return this.mapper.fromJson(result, NetworkServiceRecord.class);
    }

    /**
     *
     */
    @Help(help = "Get all the VirtualNetworkFunctionRecords of NetworkServiceRecord with specific id")
    public List<VirtualNetworkFunctionRecord> getVirtualNetworkFunctionRecords(final String id) throws SDKException {
        String url = id + "/vnfrecords";
        return Arrays.asList((VirtualNetworkFunctionRecord[]) requestGetAll(url, VirtualNetworkFunctionRecord.class));
    }

    /**
     *
     */
    @Help(help = "Get the VirtualNetworkFunctionRecord of NetworkServiceRecord with specific id")
    public VirtualNetworkFunctionRecord getVirtualNetworkFunctionRecord(final String id, final String id_vnf) throws SDKException {
        String url = id + "/vnfrecords" + "/" + id_vnf;
        return (VirtualNetworkFunctionRecord) requestGetWithStatusAccepted(url, VirtualNetworkFunctionRecord.class);
    }

    /**
     *
     */
    @Help(help = "Delete the VirtualNetworkFunctionRecord of NetworkServiceRecord with specific id")
    public void deleteVirtualNetworkFunctionRecord(final String id, final String id_vnf) throws SDKException {
        String url = id + "/vnfrecords" + "/" + id_vnf;
        requestDelete(url);
    }

    /**
     * TODO (check the orchestrator)
     */
    @Help(help = "create VirtualNetworkFunctionRecord")
    public VirtualNetworkFunctionRecord createVNFR(final String networkServiceRecord_id, final VirtualNetworkFunctionRecord virtualNetworkFunctionRecord) throws SDKException {
        String url = networkServiceRecord_id + "/vnfrecords";
        return (VirtualNetworkFunctionRecord) requestPost(url, virtualNetworkFunctionRecord);
    }

    /**
     * TODO (check the orchestrator)
     */
    @Help(help = "update VirtualNetworkFunctionRecord")
    public String updateVNFR(final String networkServiceRecord_id, final String id_vnfr, final VirtualNetworkFunctionRecord virtualNetworkFunctionRecord) throws SDKException {
        String url = networkServiceRecord_id + "/vnfrecords" + "/" + id_vnfr;
        return requestPut(url, virtualNetworkFunctionRecord).toString();

    }

    /**
     *
     */
    @Help(help = "Get all the VirtualNetworkFunctionRecord dependencies of NetworkServiceRecord with specific id")
    public List<VNFDependency> getVNFDependencies(final String networkServiceRecord_id) throws SDKException {
        String url = networkServiceRecord_id + "/vnfdependencies";
        return Arrays.asList((VNFDependency[]) requestGetAll(url, VNFDependency.class));

    }

    /**
     *
     */
    @Help(help = "Get the VirtualNetworkFunctionRecord Dependency of a NetworkServiceRecord with specific id")
    public VNFDependency getVNFDependency(final String networkServiceRecord_id, final String id_vnfd) throws SDKException {
        String url = networkServiceRecord_id + "/vnfdependencies" + "/" + id_vnfd;
        return (VNFDependency) requestGetWithStatusAccepted(url, VNFDependency.class);
    }

    /**
     *
     */
    @Help(help = "Delete the VirtualNetworkFunctionRecord Dependency of a NetworkServiceRecord with specific id")
    public void deleteVNFDependency(final String networkServiceRecord_id, final String id_vnfd) throws SDKException {
        String url = networkServiceRecord_id + "/vnfdependencies" + "/" + id_vnfd;
        requestDelete(url);
    }

    /**
     * TODO (check the orchestrator)
     */
    @Help(help = "Create the VirtualNetworkFunctionRecord Dependency of a NetworkServiceRecord with specific id")
    public VNFRecordDependency postVNFDependency(final String networkServiceRecord_id, final VNFRecordDependency vnfDependency) throws SDKException {
        String url = networkServiceRecord_id + "/vnfdependencies" + "/";
        return (VNFRecordDependency) requestPost(url, vnfDependency);

    }

    /**
     *
     */
    @Help(help = "Update the VirtualNetworkFunctionRecord Dependency of a NetworkServiceRecord with specific id")
    public VNFRecordDependency updateVNFDependency(final String networkServiceRecord_id, final String id_vnfd, final VNFRecordDependency vnfDependency) throws SDKException {
        String url = networkServiceRecord_id + "/vnfdependencies" + "/" + id_vnfd;
        return (VNFRecordDependency) requestPut(url, vnfDependency);
    }

    /**
     * Returns the set of PhysicalNetworkFunctionRecord into a NSD with id
     *
     * @param id : The id of NSD
     * @return Set<PhysicalNetworkFunctionRecord>: The Set of
     * PhysicalNetworkFunctionRecord into NSD
     */
    @Help(help = "Get all the PhysicalNetworkFunctionRecords of a specific NetworkServiceRecord with id")
    public List<PhysicalNetworkFunctionRecord> getPhysicalNetworkFunctionRecords(final String networkServiceRecord_id) throws SDKException {
        String url = networkServiceRecord_id + "/pnfrecords";
        return Arrays.asList((PhysicalNetworkFunctionRecord[]) requestGetAll(url, PhysicalNetworkFunctionRecord.class));
    }

    /**
     * Returns the PhysicalNetworkFunctionRecord
     *
     * @param id     : The NSD id
     * @param id_pnf : The PhysicalNetworkFunctionRecord id
     * @return PhysicalNetworkFunctionRecord: The PhysicalNetworkFunctionRecord
     * selected
     */
    @Help(help = "Get the PhysicalNetworkFunctionRecord of a NetworkServiceRecord with specific id")
    public PhysicalNetworkFunctionRecord getPhysicalNetworkFunctionRecord(final String networkServiceRecord_id, final String id_pnf) throws SDKException {
        String url = networkServiceRecord_id + "/pnfrecords" + "/" + id_pnf;
        return (PhysicalNetworkFunctionRecord) requestGetWithStatusAccepted(url, PhysicalNetworkFunctionRecord.class);

    }

    /**
     * Deletes the PhysicalNetworkFunctionRecord with the id_pnf
     *
     * @param id     : The NSD id
     * @param id_pnf : The PhysicalNetworkFunctionRecord id
     */
    @Help(help = "Delete the PhysicalNetworkFunctionRecord of a NetworkServiceRecord with specific id")
    public void deletePhysicalNetworkFunctionRecord(final String networkServiceRecord_id, final String id_pnf) throws SDKException {
        String url = networkServiceRecord_id + "/pnfrecords" + "/" + id_pnf;
        requestDelete(url);
    }

    /**
     * Stores the PhysicalNetworkFunctionRecord
     *
     * @param physicalNetworkFunctionRecord : The PhysicalNetworkFunctionRecord to be stored
     * @param id                            : The NSD id
     * @return PhysicalNetworkFunctionRecord: The PhysicalNetworkFunctionRecord
     * stored
     */
    @Help(help = "Create the PhysicalNetworkFunctionRecord of a NetworkServiceRecord with specific id")
    public PhysicalNetworkFunctionRecord postPhysicalNetworkFunctionRecord(final String networkServiceRecord_id, final PhysicalNetworkFunctionRecord physicalNetworkFunctionRecord) throws SDKException {
        String url = networkServiceRecord_id + "/pnfrecords" + "/";
        return (PhysicalNetworkFunctionRecord) requestPost(url, physicalNetworkFunctionRecord);

    }

    /**
     * TODO (check the orchestrator)
     * <p/>
     * Edits the PhysicalNetworkFunctionRecord
     *
     * @param physicalNetworkFunctionRecord : The PhysicalNetworkFunctionRecord to be edited
     * @param id                            : The NSD id
     * @return PhysicalNetworkFunctionRecord: The PhysicalNetworkFunctionRecord
     * edited
     */
    @Help(help = "Update the PhysicalNetworkFunctionRecord of a NetworkServiceRecord with specific id")
    public PhysicalNetworkFunctionRecord updatePNFD(final String networkServiceRecord_id, final String id_pnf, final PhysicalNetworkFunctionRecord physicalNetworkFunctionRecord) throws SDKException {
        String url = networkServiceRecord_id + "/pnfrecords" + "/" + id_pnf;
        return (PhysicalNetworkFunctionRecord) requestPut(url, physicalNetworkFunctionRecord);

    }

}
