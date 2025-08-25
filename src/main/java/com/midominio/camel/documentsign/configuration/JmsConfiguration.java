package com.midominio.camel.documentsign.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;

/** ActiveMQ configuration class. */
@Configuration
public class JmsConfiguration {

    /** Artemis URL configuration value. */
    private final String artemisUrl;
    /** Artemis username configuration value. */
    private final String artemisUsername;
    /** Artemis password configuration value. */
    private final String artemisPassword;

    /**
     * Constructor to inject values from properties configuration.
     * @param artemisUrl inject value.
     * @param artemisUsername inject value.
     * @param artemisPassword inject value.
     */
    public JmsConfiguration(
            @Value("app.artemis.url") String artemisUrl,
            @Value("app.artemis.username") String artemisUsername,
            @Value("app.artemis.password") String artemisPassword
    ) {
        this.artemisUrl = artemisUrl;
        this.artemisUsername = artemisUsername;
        this.artemisPassword = artemisPassword;
    }

    /** Bean connection to Jms activeMq. */
    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(artemisUrl);
        activeMQConnectionFactory.setUserName(artemisUsername);
        activeMQConnectionFactory.setPassword(artemisPassword);
        return activeMQConnectionFactory;
    }

}
