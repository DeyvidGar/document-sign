package com.midominio.camel.documentsign.utils;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.support.DefaultMessage;
import org.junit.jupiter.api.BeforeEach;

/** Support class for exchange. */
public class CamelExchangeSupport {

    private CamelContext context = new DefaultCamelContext();

    private Exchange exchange;

    private Message message;

    /** SetUp method */
    @BeforeEach
    void setUp() {
        exchange = new DefaultExchange(context);
        message = new DefaultMessage(exchange);
    }

    public Exchange getExchange() {
        return exchange;
    }

    public Message getMessage() {
        return message;
    }
}
