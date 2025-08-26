package com.midominio.camel.documentsign.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.dsl.SftpEndpointBuilderFactory;
import org.apache.camel.builder.endpoint.dsl.SftpEndpointBuilderFactory.SftpEndpointConsumerBuilder;
import org.apache.camel.builder.endpoint.dsl.SqlEndpointBuilderFactory;
import org.apache.camel.builder.endpoint.dsl.SqlEndpointBuilderFactory.SqlEndpointBuilder;
import org.apache.camel.component.sql.SqlConstants;
import org.springframework.stereotype.Component;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.sftp;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.sql;

/** Read document route builder. */
@Component
public class ReadDocumentRoute extends RouteBuilder {

    /** Route id. */
    public static final String ROUTE_ID = "RaadDocumentRoute";

    /** Header name with ID of the data when insert in database. */
    public static final String DB_LOG_ID = "databaseLogId";

    /** Initial component when upload and reading file from SFTP server. */
    public static final SftpEndpointConsumerBuilder SFTP_ENDPOINT =
            sftp("{{app.sftp.host}}:{{app.sftp.port}}/{{app.sftp.directoryName}}")
                    .username("{{app.sftp.username}}")
                    .password("{{app.sftp.password}}")
                    .preMove(".processing")
                    .move(".done")
                    .moveFailed(".error");

    /** Component to insert object in DB. */
    public static final SqlEndpointBuilder SQL_LOG_ENDPOINT =
            sql("insert into docsign_log (documentId, ownerId, \"timestamp\", status) " +
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
                .bean("fileMetadataExtractor")
                .setHeader(SqlConstants.SQL_RETRIEVE_GENERATED_KEYS, constant("true"))
                .log("Insert OwnerId: ${header.ownerId}")
                .to(SQL_LOG_ENDPOINT)
                .setHeader(
                        DB_LOG_ID,
                        simple("${header." + SqlConstants.SQL_GENERATED_KEYS_DATA +".get(0).get('id')}")
                )
                .to(SignDocumentRoute.ROUTE_START)
        ;

    }
}

