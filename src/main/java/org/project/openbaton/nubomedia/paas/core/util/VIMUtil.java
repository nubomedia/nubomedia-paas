/*
 *
 *  * Copyright (c) 2016 Open Baton
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.project.openbaton.nubomedia.paas.core.util;

import org.openbaton.catalogue.nfvo.Location;
import org.openbaton.catalogue.nfvo.VimInstance;
import org.project.openbaton.nubomedia.paas.properties.VimProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by mpa on 15.09.16.
 */
public class VIMUtil {

  private static Logger logger = LoggerFactory.getLogger(VIMUtil.class);

  public static VimInstance getVimInstance(VimProperties vimProperties) {
    VimInstance vim = new VimInstance();
    logger.debug("Creating VIM Object " + vimProperties.getName());
    vim.setName(vimProperties.getName());
    vim.setAuthUrl(vimProperties.getAuthURL());
    vim.setKeyPair(vimProperties.getKeypair());
    vim.setPassword(vimProperties.getPassword());
    vim.setTenant(vimProperties.getTenant());
    vim.setUsername(vimProperties.getUsername());
    vim.setType(vimProperties.getType());
    Location location = new Location();
    location.setName(vimProperties.getLocationName());
    location.setLatitude(vimProperties.getLocationLatitude());
    location.setLongitude(vimProperties.getLocationLongitude());
    vim.setLocation(location);
    logger.debug("VIM Object generated " + vim);
    return vim;
  }
}
