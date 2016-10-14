/*
 *
 *  * (C) Copyright 2016 NUBOMEDIA (http://www.nubomedia.eu)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *
 */

package org.project.openbaton.nubomedia.paas.main.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

/**
 * Created by mpa on 29.07.15.
 */
public class Utils {

  private final static Logger log = LoggerFactory.getLogger(Utils.class);

  public static boolean isNfvoStarted(String ip, String port) {
    int i = 600;
    log.info("Testing if NFVO is available...");
    while (!Utils.available(ip, port)) {
      log.warn(
          "NFVO is not available at "
              + ip
              + ":"
              + port
              + ". Waiting for "
              + i
              + "s before terminating the VNFM");
      i--;
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (i <= 0) {
        return false;
      }
    }
    log.info("NFVO is listening at " + ip + ":" + port);
    return true;
  }

  public static boolean available(String ip, String port) {
    try {
      Socket s = new Socket(ip, Integer.parseInt(port));
      s.close();
      return true;
    } catch (IOException ex) {
      // The remote host is not listening on this port
      return false;
    }
  }

  public static void loadExternalProperties(Properties properties) {
    if (properties.getProperty("external-properties-file") != null) {
      File externalPropertiesFile = new File(properties.getProperty("external-properties-file"));
      if (externalPropertiesFile.exists()) {
        log.debug(
            "Loading properties from external-properties-file: "
                + properties.getProperty("external-properties-file"));
        InputStream is = null;
        try {
          is = new FileInputStream(externalPropertiesFile);
          properties.load(is);
        } catch (FileNotFoundException e) {
          log.error(e.getMessage(), e);
        } catch (IOException e) {
          log.error(e.getMessage(), e);
        }
      } else {
        log.debug(
            "external-properties-file: "
                + properties.getProperty("external-properties-file")
                + " doesn't exist");
      }
    }
  }
}
