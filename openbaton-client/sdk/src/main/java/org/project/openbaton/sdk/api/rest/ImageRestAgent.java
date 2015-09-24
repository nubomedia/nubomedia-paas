package org.project.openbaton.sdk.api.rest;

import org.project.openbaton.catalogue.nfvo.NFVImage;

/**
 * OpenBaton image-related api requester.
 */
public class ImageRestAgent extends org.project.openbaton.sdk.api.util.AbstractRestAgent<NFVImage> {

    /**
     * Create a image requester with a given url path
     *
     *
     */
    public ImageRestAgent(String username, String password, String nfvoIp, String nfvoPort, String path, String version) {
        super(username, password, nfvoIp, nfvoPort, path, version, NFVImage.class);
    }

}
