package org.project.openbaton.sdk;

import org.project.openbaton.common.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.project.openbaton.common.catalogue.mano.descriptor.VNFForwardingGraphDescriptor;
import org.project.openbaton.common.catalogue.mano.descriptor.VirtualLinkDescriptor;
import org.project.openbaton.common.catalogue.mano.record.NetworkServiceRecord;
import org.project.openbaton.common.catalogue.nfvo.Configuration;
import org.project.openbaton.common.catalogue.nfvo.EventEndpoint;
import org.project.openbaton.common.catalogue.nfvo.NFVImage;
import org.project.openbaton.common.catalogue.nfvo.VimInstance;
import org.project.openbaton.sdk.api.rest.*;
import org.project.openbaton.sdk.api.util.AbstractRestAgent;
import org.project.openbaton.sdk.api.util.PropertyReader;

public class RequestFactory {
    
    private static RequestFactory instance;
	
	private static final String SDK_PROPERTIES_FILE = "sdk.api.properties";
	
	private static final PropertyReader propertyReader = new PropertyReader(SDK_PROPERTIES_FILE);

	// create the requester here, maybe shift this to a manager
	private static AbstractRestAgent<Configuration> configurationRequest = null;
	private static AbstractRestAgent<NFVImage> imageRequest = null;
	private static AbstractRestAgent<NetworkServiceDescriptor> networkServiceDescriptorRequest = null;
	private static AbstractRestAgent<NetworkServiceRecord> networkServiceRecordRequest = null;
	private static AbstractRestAgent<VimInstance> vimInstanceRequest = null;
	private static AbstractRestAgent<VirtualLinkDescriptor> virtualLinkRequest = null;
	private static AbstractRestAgent<VNFForwardingGraphDescriptor> vNFFGRequest = null;
	private static AbstractRestAgent<EventEndpoint> eventAgent = null;
	private static String username;
	private static String password;
	private final String nfvoPort;
	private final String nfvoIp;
	private final String version;

	public static RequestFactory getInstance(String username, String password, String nfvoIp, String nfvoPort, String version){
    	if (instance == null) {
    		return new RequestFactory(username, password, nfvoIp, nfvoPort, version);
    	}
    	else
    		return instance;
    }

    private RequestFactory(String username, String password, String nfvoIp, String nfvoPort, String version){
		this.username = username;
		this.password = password;
		this.nfvoPort = nfvoPort;
		this.nfvoIp = nfvoIp;
		this.version = version;
    }

    public AbstractRestAgent<Configuration> getConfigurationAgent(){
    	if (configurationRequest == null) {
    		configurationRequest = new ConfigurationRestRequest(username, password, nfvoIp, nfvoPort, propertyReader.getRestConfigurationUrl(), version);
    	}
    	return configurationRequest;
    }

    public AbstractRestAgent<NFVImage> getImageAgent(){
    	if (networkServiceDescriptorRequest == null) {
    		imageRequest = new ImageRestAgent(username, password, nfvoIp,nfvoPort,propertyReader.getRestImageUrl(), version);
    	}
    	return imageRequest;
    }

    public AbstractRestAgent<NetworkServiceDescriptor> getNetworkServiceDescriptorAgent(){
    	if (networkServiceRecordRequest == null) {
    		networkServiceDescriptorRequest = new NetworkServiceDescriptorRestAgent(username, password, nfvoIp,nfvoPort,propertyReader.getRestNetworkServiceDescriptorUrl(), version);
    	}
    	return networkServiceDescriptorRequest;
    }

    public AbstractRestAgent<NetworkServiceRecord> getNetworkServiceRecordAgent(){
    	if (configurationRequest == null) {
    		networkServiceRecordRequest = new NetworkServiceRecordRestAgent(username, password, nfvoIp,nfvoPort,propertyReader.getRestNetworkServiceRecordUrl(), version);
    	}
    	return networkServiceRecordRequest;
    }

    public AbstractRestAgent<VimInstance> getVimInstanceAgent(){
    	if (vimInstanceRequest == null) {
    		vimInstanceRequest = new VimInstanceRestAgent(username, password, nfvoIp,nfvoPort,propertyReader.getRestVimInstanceUrl(), version);
    	}
    	return vimInstanceRequest;
    }

    public AbstractRestAgent<VirtualLinkDescriptor> getVirtualLinkAgent(){
    	if (virtualLinkRequest == null) {
    		virtualLinkRequest = new VirtualLinkRestAgent(username, password, nfvoIp,nfvoPort,propertyReader.getRestVirtualLinkUrl(), version);
    	}
    	return virtualLinkRequest;
    }

    public AbstractRestAgent<VNFForwardingGraphDescriptor> getVNFForwardingGraphAgent(){
    	if (vNFFGRequest == null) {
    		vNFFGRequest = new VNFFGRestAgent(username, password, nfvoIp,nfvoPort,propertyReader.getRestVNFFGUrl(), version);
    	}
    	return vNFFGRequest;
    }

	public AbstractRestAgent<EventEndpoint> getEventAgent(){
		if (eventAgent == null){
			eventAgent = new EventAgent(username, password, nfvoIp,nfvoPort,propertyReader.getEventUrl(), version);
		}
		return eventAgent;
	}
}
