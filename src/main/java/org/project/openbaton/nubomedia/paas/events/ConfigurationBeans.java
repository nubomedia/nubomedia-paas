/*
 * Copyright (c) 2015-2016 Fraunhofer FOKUS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.project.openbaton.nubomedia.paas.events;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.project.openbaton.nubomedia.paas.utils.RabbitMQProperties;
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
 * Created by mob on 23.03.16.
 */
@Configuration
@ComponentScan("org.project.openbaton.nubomedia.paas")
public class ConfigurationBeans {
    public static final String queueName_eventInstatiateFinish = "nfvo.paas.nsr.create";
    public static final String queueName_eventResourcesReleaseFinish = "nfvo.paas.nsr.delete";
    public static final String queueName_error = "nfvo.paas.nsr.error";
    private Logger logger;

    @Autowired
    private RabbitMQProperties rabbitMQProperties;

    @PostConstruct
    private void init(){
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Bean
    public Gson getMapper(){
        return new GsonBuilder().serializeNulls().create();
    }

    @Bean
    public ConnectionFactory getConnectionFactory(Environment env){
        logger.debug("Created ConnectionFactory");
        CachingConnectionFactory factory = new CachingConnectionFactory(rabbitMQProperties.getHost());
        factory.setPassword(rabbitMQProperties.getPassword());
        factory.setUsername(rabbitMQProperties.getUsername());
        return factory;
    }

    @Bean
    public TopicExchange getTopic(){
        logger.debug("Created Topic Exchange");
        return new TopicExchange("openbaton-exchange");
    }

    @Bean
    public Queue getCreationQueue(){
        logger.debug("Created Queue for NSR Create event");
        return new Queue(queueName_eventInstatiateFinish,false,false,true);
    }

    @Bean
    public Queue getErrorQueue(){
        logger.debug("Created Queue for NSR error event");
        return new Queue(queueName_error,false,false,true);
    }
    @Bean
    public Queue getDeletionQueue(){
        logger.debug("Created Queue for NSR Delete Event");
        return new Queue(queueName_eventResourcesReleaseFinish,false,false,true);
    }

    @Bean
    public Binding setCreationBinding(@Qualifier("getCreationQueue") Queue queue, TopicExchange topicExchange){
        logger.debug("Created Binding for NSR Creation event");
        return BindingBuilder.bind(queue).to(topicExchange).with("ns-creation");
    }

    @Bean
    public Binding setErrorBinding(@Qualifier("getErrorQueue") Queue queue, TopicExchange topicExchange){
        logger.debug("Created Binding for NSR error event");
        return BindingBuilder.bind(queue).to(topicExchange).with("ns-error");
    }

    @Bean
    public Binding setDeletionBinding(@Qualifier("getDeletionQueue") Queue queue, TopicExchange topicExchange){
        logger.debug("Created Binding for NSR Deletion event");
        return BindingBuilder.bind(queue).to(topicExchange).with("ns-deletion");
    }

    @Bean
    public MessageListenerAdapter setCreationMessageListenerAdapter(OpenbatonEventReceiver receiver){
        return new MessageListenerAdapter(receiver,"receiveNewNsr");
    }


    @Bean
    public MessageListenerAdapter setErrorMessageListenerAdapter(OpenbatonEventReceiver receiver){
        return new MessageListenerAdapter(receiver,"errorNsr");
    }

    @Bean
    public MessageListenerAdapter setDeletionMessageListenerAdapter(OpenbatonEventReceiver receiver){
        return new MessageListenerAdapter(receiver,"deleteNsr");
    }

    @Bean
    public SimpleMessageListenerContainer setCreationMessageContainer(ConnectionFactory connectionFactory, @Qualifier("getCreationQueue") Queue queue, @Qualifier("setCreationMessageListenerAdapter") MessageListenerAdapter adapter){
        logger.debug("Created MessageContainer for NSR Creation event");
        SimpleMessageListenerContainer res = new SimpleMessageListenerContainer();
        res.setConnectionFactory(connectionFactory);
        res.setQueues(queue);
        res.setMessageListener(adapter);
        return res;
    }


    @Bean
    public SimpleMessageListenerContainer setErrorMessageContainer(ConnectionFactory connectionFactory, @Qualifier("getErrorQueue") Queue queue, @Qualifier("setErrorMessageListenerAdapter") MessageListenerAdapter adapter){
        logger.debug("Created MessageContainer for NSR error event");
        SimpleMessageListenerContainer res = new SimpleMessageListenerContainer();
        res.setConnectionFactory(connectionFactory);
        res.setQueues(queue);
        res.setMessageListener(adapter);
        return res;
    }

    @Bean
    public SimpleMessageListenerContainer setDeletionMessageContainer(ConnectionFactory connectionFactory, @Qualifier("getDeletionQueue") Queue queue, @Qualifier("setDeletionMessageListenerAdapter") MessageListenerAdapter messageListenerAdapter){
        logger.debug("Created MessageContainer for NSR Deletion event");
        SimpleMessageListenerContainer res = new SimpleMessageListenerContainer();
        res.setConnectionFactory(connectionFactory);
        res.setQueues(queue);
        res.setMessageListener(messageListenerAdapter);
        return res;
    }

}
