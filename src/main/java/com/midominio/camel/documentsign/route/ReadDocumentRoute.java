package com.midominio.camel.documentsign.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.dsl.SftpEndpointBuilderFactory;
import org.apache.camel.builder.endpoint.dsl.SqlEndpointBuilderFactory;
import org.springframework.stereotype.Component;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.sftp;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.sql;

/** ReadDocumentRoute builder. */
@Component
public class ReadDocumentRoute extends RouteBuilder {

    /** route id */
    public static final String ROUTE_ID = "RaadDocumentRoute";
    /** Database log id. */
    public static final String DB_LOG_ID = "databaseLogId";

    /** Initial component to receive File and reading */
    public static final SftpEndpointBuilderFactory.SftpEndpointConsumerBuilder SFTP_ENDPOINT =
//            sftp("docsend_sftp_user@localhost:2222/documents?password=docsend_sftp_password")
//    sftp("{{app.sftp.host}}:2222/documents")
            sftp("{{app.sftp.host}}:{{app.sftp.port}}/{{app.sftp.directoryName}}")
                .username("{{app.sftp.username}}")
                    .password("{{app.sftp.password}}")
//                .knownHostsFile("{{app.sftp.knownHosts}}")
//                    knownHosts
                    .preMove(".processing")
                    .move(".done")
                    .moveFailed(".error");

    /** Component to insert object in DB. */
    public static final SqlEndpointBuilderFactory.SqlEndpointBuilder SQL_LOG_ENDPOINT = sql("insert into docsign_log (documentId, ownerId, \"timestamp\", status) " +
            "values (:#${headerAs(documentId, Integer)}, :#${header.ownerId}, :#${date:now}, 'Read document')");

    /**
     * Reading files with File Transfer Protocol.
     * @throws Exception general exception.
     */
    @Override
    public void configure() throws Exception {

        from(SFTP_ENDPOINT)
                .routeId(ROUTE_ID)
                .log("Read document: ${body}")
                .log("Read header: ${header." + Exchange.FILE_NAME + "}")
//                .bean("exchangeTransformer")
//                .setHeader(SqlConstants.SQL_RETRIEVE_GENERATED_KEYS, constant("true"))
//                .to(SQL_LOG_ENDPOINT)
//                .setHeader(
//                        DB_LOG_ID,
//                        simple("${header." + SqlConstants.SQL_GENERATED_KEYS_DATA +".get(0).get('id')}")
//                )
//                .to(SignDocumentRoute.ROUTE_START)
        ;

    }
}

