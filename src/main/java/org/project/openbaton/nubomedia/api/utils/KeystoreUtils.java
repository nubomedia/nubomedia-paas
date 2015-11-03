package org.project.openbaton.nubomedia.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by maa on 03.11.15.
 */
public class KeystoreUtils {

    private static Logger logger = LoggerFactory.getLogger(KeystoreUtils.class);

    public static String getKeystore(){ //very very bad
        String res = "resource/keystore";

        File keystore = new File("/etc/nubomedia/openshift-keystore");

        if(keystore.exists()){

            res = "/etc/nubomedia/openshift-keystore";

        }

        return res;
    }

}
