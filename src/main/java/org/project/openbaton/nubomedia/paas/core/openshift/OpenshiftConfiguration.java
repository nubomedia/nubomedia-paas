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

package org.project.openbaton.nubomedia.paas.core.openshift;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.project.openbaton.nubomedia.paas.model.persistence.EnvironmentVariable;
import org.project.openbaton.nubomedia.paas.model.openshift.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by maa on 07.10.15.
 */
@Configuration
public class OpenshiftConfiguration {

  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplate();
  }

  @Bean
  public Gson getMapper() {
    return new GsonBuilder()
        .registerTypeAdapter(Metadata.class, new MetadataTypeAdapter())
        .registerTypeAdapter(Output.class, new OutputTypeAdapter())
        .registerTypeAdapter(SecretConfig.class, new SecretDeserializer())
        .registerTypeAdapter(Source.class, new SourceDeserializer())
        .registerTypeAdapter(Status.class, new StatusDeserializer())
        .registerTypeAdapter(Pod.class, new PodDeserializer())
        .registerTypeAdapter(Pods.class, new PodsDeserializer())
        .registerTypeAdapter(EnvironmentVariable.class, new EnvironmentVariableSerializer())
        .disableHtmlEscaping()
        .create();
  }

  @Bean
  public AuthenticationManager getAuthenticationManager() {
    return new AuthenticationManager();
  }
}
