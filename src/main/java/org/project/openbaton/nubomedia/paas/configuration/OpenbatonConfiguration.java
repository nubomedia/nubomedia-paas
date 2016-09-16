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

package org.project.openbaton.nubomedia.paas.configuration;

import org.openbaton.catalogue.nfvo.Location;
import org.openbaton.catalogue.nfvo.VimInstance;
import org.openbaton.catalogue.security.Project;
import org.openbaton.sdk.NFVORequestor;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.paas.api.OpenbatonEventReceiver;
import org.project.openbaton.nubomedia.paas.main.utils.Utils;
import org.project.openbaton.nubomedia.paas.properties.NfvoProperties;
import org.project.openbaton.nubomedia.paas.properties.RabbitMQProperties;
import org.project.openbaton.nubomedia.paas.properties.VimProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * Created by maa on 13.10.15.
 */
@Configuration
@ComponentScan("org.project.openbaton.nubomedia.paas")
public class OpenbatonConfiguration {

  @Autowired private NfvoProperties nfvoProperties;

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  public static final String queueName_eventInstatiateFinish = "nfvo.paas.nsr.create";
  public static final String queueName_error = "nfvo.paas.nsr.error";

  @Autowired private RabbitMQProperties rabbitMQProperties;

  @Bean
  public NFVORequestor getNFVORequestor() throws SDKException {
    //    if (!Utils.isNfvoStarted(nfvoProperties.getIp(), nfvoProperties.getPort())) {
    //      logger.error("NFVO is not available");
    //      System.exit(1);
    //    }
    NFVORequestor nfvoRequestor =
        new NFVORequestor(
            nfvoProperties.getUsername(),
            nfvoProperties.getPassword(),
            "*",
            false,
            nfvoProperties.getIp(),
            nfvoProperties.getPort(),
            "1");
    this.logger.info("Starting the Open Baton Manager Bean");

    try {
      logger.info("Finding NUBOMEDIA project");
      boolean found = false;
      for (Project project : nfvoRequestor.getProjectAgent().findAll()) {
        if (project.getName().equals(nfvoProperties.getProject().getName())) {
          found = true;
          nfvoRequestor.setProjectId(project.getId());
          logger.info("Found NUBOMEDIA project");
        }
      }
      if (!found) {
        logger.info("Not found NUBOMEDIA project");
        logger.info("Creating NUBOMEDIA project");
        Project project = new Project();
        project.setDescription("NUBOMEDIA project");
        project.setName(nfvoProperties.getProject().getName());
        project = nfvoRequestor.getProjectAgent().create(project);
        nfvoRequestor.setProjectId(project.getId());
        logger.info("Created NUBOMEDIA project " + project);
      }
    } catch (SDKException e) {
      throw new SDKException(e.getMessage());
    } catch (ClassNotFoundException e) {
      throw new SDKException(e.getMessage());
    }
    return nfvoRequestor;
  }

  @Bean
  public ConnectionFactory getConnectionFactory(Environment env) {
    logger.debug("Created ConnectionFactory");
    CachingConnectionFactory factory = new CachingConnectionFactory(rabbitMQProperties.getHost());
    factory.setPassword(rabbitMQProperties.getPassword());
    factory.setUsername(rabbitMQProperties.getUsername());
    return factory;
  }

  @Bean
  public TopicExchange getTopic() {
    logger.debug("Created Topic Exchange");
    return new TopicExchange("openbaton-exchange");
  }

  @Bean
  public Queue getCreationQueue() {
    logger.debug("Created Queue for NSR Create event");
    return new Queue(queueName_eventInstatiateFinish, false, false, true);
  }

  @Bean
  public Queue getErrorQueue() {
    logger.debug("Created Queue for NSR error event");
    return new Queue(queueName_error, false, false, true);
  }

  @Bean
  public Binding setCreationBinding(
      @Qualifier("getCreationQueue") Queue queue, TopicExchange topicExchange) {
    logger.debug("Created Binding for NSR Creation event");
    return BindingBuilder.bind(queue).to(topicExchange).with("ns-creation");
  }

  @Bean
  public Binding setErrorBinding(
      @Qualifier("getErrorQueue") Queue queue, TopicExchange topicExchange) {
    logger.debug("Created Binding for NSR error event");
    return BindingBuilder.bind(queue).to(topicExchange).with("ns-error");
  }

  @Bean
  public MessageListenerAdapter setCreationMessageListenerAdapter(OpenbatonEventReceiver receiver) {
    return new MessageListenerAdapter(receiver, "receiveNewNsr");
  }

  @Bean
  public MessageListenerAdapter setErrorMessageListenerAdapter(OpenbatonEventReceiver receiver) {
    return new MessageListenerAdapter(receiver, "errorNsr");
  }

  @Bean
  public SimpleMessageListenerContainer setCreationMessageContainer(
      ConnectionFactory connectionFactory,
      @Qualifier("getCreationQueue") Queue queue,
      @Qualifier("setCreationMessageListenerAdapter") MessageListenerAdapter adapter) {
    logger.debug("Created MessageContainer for NSR Creation event");
    SimpleMessageListenerContainer res = new SimpleMessageListenerContainer();
    res.setConnectionFactory(connectionFactory);
    res.setQueues(queue);
    res.setMessageListener(adapter);
    return res;
  }

  @Bean
  public SimpleMessageListenerContainer setErrorMessageContainer(
      ConnectionFactory connectionFactory,
      @Qualifier("getErrorQueue") Queue queue,
      @Qualifier("setErrorMessageListenerAdapter") MessageListenerAdapter adapter) {
    logger.debug("Created MessageContainer for NSR error event");
    SimpleMessageListenerContainer res = new SimpleMessageListenerContainer();
    res.setConnectionFactory(connectionFactory);
    res.setQueues(queue);
    res.setMessageListener(adapter);
    return res;
  }

  //  @Bean
  //  public VirtualNetworkFunctionDescriptor getCloudRepository() {
  //    VirtualNetworkFunctionDescriptor vnfd = null;
  //    Gson mapper = new GsonBuilder().create();
  //    try {
  //      logger.debug("Reading cloud repository descriptor");
  //      FileReader vnfdFile = new FileReader("/etc/nubomedia/cloudrepo-vnfd.json");
  //      vnfd = mapper.fromJson(vnfdFile, VirtualNetworkFunctionDescriptor.class);
  //      logger.debug("CLOUD REPOSITORY IS " + vnfd.toString());
  //
  //    } catch (FileNotFoundException e) {
  //      logger.debug(
  //          "DO NOT REMOVE OR RENAME THE FILE /etc/nubomedia/cloudrepo-vnfd.json!!!!\nexiting");
  //    }
  //    return vnfd;
  //  }
  //
  //  @Bean
  //  public NetworkServiceDescriptor networkServiceDescriptorNubo() {
  //    logger.debug("Reading descriptor");
  //    NetworkServiceDescriptor nsd = new NetworkServiceDescriptor();
  //    Gson mapper = new GsonBuilder().create();
  //    try {
  //      logger.debug("Trying to read the descriptor");
  //      FileReader nsdFile = new FileReader("/etc/nubomedia/nubomedia-nsd.json");
  //      nsd = mapper.fromJson(nsdFile, NetworkServiceDescriptor.class);
  //      logger.debug("DESCRIPTOR " + nsd.toString());
  //    } catch (FileNotFoundException e) {
  //      logger.error(
  //          "DO NOT REMOVE OR RENAME THE FILE /etc/nubomedia/nubomedia-nsd.json!!!!\nexiting", e);
  //    }
  //    return nsd;
  //  }
}
