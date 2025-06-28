package com.midominio.camel.documentsign.route;

import org.apache.camel.builder.RouteConfigurationBuilder;
import org.apache.camel.builder.endpoint.dsl.SqlEndpointBuilderFactory.SqlEndpointBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.sql;

@Component
public class ErrorHandlingConfiguration extends RouteConfigurationBuilder {

    public static final SqlEndpointBuilder SQL =
            sql("update docsign_log set errorDetails=:#${exception.message}, \"timestamp\"=:#${date:now} " +
                "where id=:#${header." + ReadDocumentRoute.DB_LOG_ID + "}");
    @Override
    public void configuration() throws Exception {
        routeConfiguration()
                .onException(Exception.class)
                .handled(false)
                .choice()
                    .when(exchange ->
                            StringUtils.hasLength(
                                    exchange.getMessage().getHeader(ReadDocumentRoute.DB_LOG_ID, String.class)))
                        .to(SQL)
        ;


    }
}