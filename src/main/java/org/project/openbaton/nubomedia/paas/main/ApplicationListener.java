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

package org.project.openbaton.nubomedia.paas.main;

import org.project.openbaton.nubomedia.paas.main.utils.Utils;
import org.project.openbaton.nubomedia.paas.properties.NfvoProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by mpa on 16.09.16.
 */
@Component
public class ApplicationListener implements org.springframework.context.ApplicationListener {

  private Logger log = LoggerFactory.getLogger(this.getClass());

  private String nfvoIp;

  private String nfvoPort;

  @Override
  public void onApplicationEvent(ApplicationEvent event) {
    log.trace(event.toString());
    if (event instanceof ApplicationEnvironmentPreparedEvent) {
      for (PropertySource<?> source :
          ((ApplicationEnvironmentPreparedEvent) event).getEnvironment().getPropertySources()) {
        if (source.getName().equals("applicationConfigurationProperties")) {
          if (source instanceof EnumerablePropertySource) {
            for (String name : ((EnumerablePropertySource) source).getPropertyNames()) {
              if (name.equals("nfvo.ip")) {
                nfvoIp = (String) ((EnumerablePropertySource) source).getProperty(name);
              }
              if (name.equals("nfvo.port")) {
                nfvoPort = (String) ((EnumerablePropertySource) source).getProperty(name);
              }
            }
          }
        }
      }
    }
    if (event instanceof ApplicationPreparedEvent) {
      if (!Utils.isNfvoStarted(nfvoIp, nfvoPort)) {
        System.exit(1);
      }
    } else if (event instanceof ContextClosedEvent) {
      log.warn("Received Ctrl+c. Exiting...");
    }
  }
}
