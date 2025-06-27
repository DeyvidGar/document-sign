package com.midominio.camel.documentsign.route;

import com.midominio.camel.documentsign.models.SignDocumentResponse;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.dsl.HttpEndpointBuilderFactory;
import org.apache.camel.builder.endpoint.dsl.HttpEndpointBuilderFactory.HttpEndpointBuilder;
import org.apache.camel.builder.endpoint.dsl.SqlEndpointBuilderFactory;
import org.apache.camel.builder.endpoint.dsl.SqlEndpointBuilderFactory.SqlEndpointBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.http;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.sql;

@Component
public class SignDocumentRoute extends RouteBuilder {

    /** Initial route. */
    public static final String ROUTE_START = "direct:sign-document-route-start";
    /** Route id. */
    public static final String ROUTE_ID = "SignDocumentRoute";

    /** Component to connect with http service. */
    public static final HttpEndpointBuilder HTTP_ENDPOINT =
            http("{{app.signDocument.url}}");

    /** Component to update object in DB. */
    public static final SqlEndpointBuilder SQL_LOG_ENDPOINT =
            sql("update docsign_log set status=:#${body.getStatus}, \"timestamp\"=:#${date:now} " +
                "where id=:#${header." + ReadDocumentRoute.DB_LOG_ID + "}");

    /**
     * Send file to SignDocument http service.
     * @throws Exception general exception.
     */
    @Override
    public void configure() throws Exception {
        from(ROUTE_START)
                .routeId(ROUTE_ID)
                .bean("signDocumentRequestMapper")
                .log("Sending document: ${header." + Exchange.FILE_NAME + "}")
                .log("Body: ${body} - to the Sign Document Service.")
                .marshal().json()
                .to(HTTP_ENDPOINT)
                .unmarshal().json(JsonLibrary.Jackson, SignDocumentResponse.class)
                .log("Received document from the Sign Document Service: ${body}")
                .log("Document signed status: ${body.getStatus}")
                .to(SQL_LOG_ENDPOINT)
                .to(SendDocumentRoute.START_ROUTE)
        ;

    }
}
