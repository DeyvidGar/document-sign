package com.midominio.camel.documentsign.route;

import com.midominio.camel.documentsign.constants.ExchangeConstants;
import com.midominio.camel.documentsign.models.ClientSendRequest;
import com.midominio.camel.documentsign.models.ClientSendResponse;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.dsl.JmsEndpointBuilderFactory.JmsEndpointProducerBuilder;
import org.apache.camel.builder.endpoint.dsl.SqlEndpointBuilderFactory.SqlEndpointBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.jms;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.sql;

/** Send document route builder. */
@Component
public class SendDocumentRoute extends RouteBuilder {

    /** Initial route. */
    public static final String START_ROUTE = "direct:send-document-route-start";

    /** Route id. */
    public static final String ROUTE_ID = "SendDocumentRoute";

    /** Component to connect with JMS ArtemisMQ service. */
    public static final JmsEndpointProducerBuilder JMS =
            jms("{{app.clientSend.clientSendRequestQueue}}")
                    .replyTo("{{app.clientSend.clientSendResponseQueue}}")
//                    .replyTo("{{app.clientSend.clientSendRequestQueue}}")
                    .requestTimeout(300000L);

    /** Component to update object in DB. */
    public static final SqlEndpointBuilder SQL_LOG_ENDPOINT =
            sql("update docsign_log set status='Document sent', \"timestamp\"=:#${date:now} " +
                "where id=:#${header." + ReadDocumentRoute.DB_LOG_ID + "}");

    /**
     * Send file to JMS ArtemisMQ service.
     * @throws Exception general exception.
     */
    @Override
    public void configure() throws Exception {
        from(START_ROUTE)
                .routeId(ROUTE_ID)
                .bean("clientSendRequestMapper")
                .log("Object to send to jms: ${body}")
                .log("Sending signed document to client: ${header." + ExchangeConstants.CLIENT_ID + "}")
                .marshal().jacksonxml(ClientSendRequest.class)
                .log("body: ${body}")
                .to(ExchangePattern.InOut, JMS)
                .unmarshal().jacksonxml(ClientSendResponse.class)
                .log("Document signed status: ${body.getStatus}")
                .log("Update id = ${header." + ReadDocumentRoute.DB_LOG_ID + "}")
                .to(SQL_LOG_ENDPOINT)
                .log("Processing document: ${header." + Exchange.FILE_NAME + "} finished.")
        ;

    }
}
