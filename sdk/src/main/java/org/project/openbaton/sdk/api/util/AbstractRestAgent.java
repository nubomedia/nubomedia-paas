package org.project.openbaton.sdk.api.util;

import org.project.openbaton.sdk.api.exception.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lto on 03/07/15.
 */
public class AbstractRestAgent<T extends Serializable> extends RestRequest{

    private final Class<T> clazz;
    private Logger log = LoggerFactory.getLogger(this.getClass());



    public AbstractRestAgent(String username, String password, String nfvoIp, String nfvoPort, String path, String version, Class<T> tClass) {
        super(username, password, nfvoIp, nfvoPort, path, version);
        clazz = tClass;
    }


    /**
     * Adds a new VNF software Image to the object repository
     *
     * @param object
     *            : obj to add
     * @return string: The object filled with values from the api
     */
	public T create (final T object) throws SDKException {
        log.trace("CLAZZ: " + clazz.getSimpleName());
        return (T) requestPost(object);
	}

    /**
     * Removes the VNF software Image from the Image repository
     *
     * @param id
     *            : The Obj's id to be deleted
     */
	public void delete(final String id) throws SDKException {
        requestDelete(id);
	}

    /**
     * Returns the list of the VNF software images available
     *
     * @return List<Obj>: The list of VNF software images available
     */
	public List<T> findAll() throws SDKException, ClassNotFoundException {
        return (List<T>) requestGet(null, clazz);
	}

    /**
     * Returns the VNF software image selected by id
     *
     * @param id
     *            : The id of the VNF software image
     * @return image: The VNF software image selected
     */
	public T findById(final String id) throws SDKException, ClassNotFoundException {
        return (T) requestGet(id, clazz);
	}

    /**
     * Updates the VNF software object
     *
     * @param object
     *            : Image to add
     * @param id
     *            : the id of VNF software object
     * @return object: the VNF software object updated
     */
	public T update(final T object, final String id) throws SDKException {
        return (T) requestPut(id, object);
	}
}
