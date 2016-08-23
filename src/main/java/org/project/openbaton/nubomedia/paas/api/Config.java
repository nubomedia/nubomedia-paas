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

package org.project.openbaton.nubomedia.paas.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gca on 27/05/16.
 */
@RestController
@RequestMapping("/api/v1/nubomedia/config")
public class Config {

  @Value("${marketplace.ip}")
  private String marketplaceIp;

  @Value("${marketplace.port}")
  private String marketplacePort;

  /**
   * Returns the list of the Configurations available
   *
   * @return List<Configuration>: The list of Configurations available
   */
  @RequestMapping(method = RequestMethod.GET)
  public String getMarketplaceUrl() {
    return marketplaceIp + ":" + marketplacePort;
  }
}
