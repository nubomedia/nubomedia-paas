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

package org.project.openbaton.nubomedia.paas.security.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
@ConfigurationProperties
public class ResourceServer extends ResourceServerConfigurerAdapter {

  @Value("paas.security.enabled:true")
  private String enabledSt;

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.headers().frameOptions().disable();

    log.info("Security is enabled");
    http.authorizeRequests()
        .regexMatchers(HttpMethod.POST, "/api/v1/")
        .access("#oauth2.hasScope('write')")
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.NEVER)
        .and()
        .exceptionHandling();

    http.authorizeRequests()
        .antMatchers(HttpMethod.POST, "/api/**")
        .access("#oauth2.hasScope('write')")
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.NEVER)
        .and()
        .exceptionHandling();
    http.authorizeRequests().regexMatchers(HttpMethod.GET, "/api/v1/").permitAll();
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.resourceId(OAuth2AuthorizationServerConfig.RESOURCE_ID);
  }
}
