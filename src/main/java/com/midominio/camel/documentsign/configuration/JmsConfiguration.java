package com.midominio.camel.documentsign.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;

/** Jms configuration class. */
@Configuration
public class JmsConfiguration {

    /** Bean connection to Jms activeMq */
    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");//puerto indicado en docker
        activeMQConnectionFactory.setPassword("artemis");
        activeMQConnectionFactory.setUserName("artemis");
        return activeMQConnectionFactory;
    }

}
