package com.midominio.camel.documentsign.route;

import com.midominio.camel.documentsign.service.FileMetadataExtractor;
import com.midominio.camel.documentsign.utils.AdviceWithUtilConfigurable;
import com.midominio.camel.documentsign.utils.MockEndpointsUtil;
import org.apache.camel.*;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.sql.SqlConstants;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.ExcludeRoutes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

@SpringBootTest
@CamelSpringBootTest
@ActiveProfiles({"sftp", "sql"})
//@ExcludeRoutes({SignDocumentRoute.class})
class ReadDocumentRouteUnitTest {

    private static final String START_TEST = "direct:test";
    private static final String MOCK_SQL = "mock:sql";
    private static final String MOCK_END = "mock:at-final";

    @Produce(START_TEST)
    private ProducerTemplate producerTemplate;

    @EndpointInject(MOCK_SQL)
    private MockEndpoint sqlMockEndpoint;

    @EndpointInject(MOCK_END)
    private MockEndpoint endMockEndpoint;

    @Autowired
    private CamelContext context;

    @MockBean
    private DataSource dataSource;

    @MockBean(name = "fileMetadataExtractor")
    private FileMetadataExtractor fileMetadataExtractor;

    @BeforeEach
    void setUp() throws Exception {
        AdviceWithUtilConfigurable adviceWith = new AdviceWithUtilConfigurable(context, ReadDocumentRoute.ROUTE_ID);

        adviceWith.replaceFromWith(START_TEST);
        adviceWith.interceptAndSkipAndTo(
                ReadDocumentRoute.SQL_LOG_ENDPOINT.getUri(),
                MOCK_SQL
        );
        adviceWith.interceptAndSkipAndTo(
                SignDocumentRoute.ROUTE_START,
                MOCK_END
        );
    }

    /**
     * Given an exchange.
     * When RaadDocumentRoute is invokes.
     * Then verify mock endpoints.
     */
    @Test
    void documentReadRoute_documentReadRouteBean() throws InterruptedException {
        String body = "body";
        String headerValue = "jojo";

        producerTemplate.sendBodyAndHeader(body, Exchange.FILE_NAME, headerValue);

        Mockito.verify(fileMetadataExtractor, Mockito.times(1)).parseFileName(Mockito.any(Exchange.class));

        sqlMockEndpoint.expectedMessageCount(1);
        sqlMockEndpoint.expectedHeaderReceived(SqlConstants.SQL_RETRIEVE_GENERATED_KEYS, "true");

        endMockEndpoint.expectedMessageCount(1);
        endMockEndpoint.expectedMessagesMatches(exchange -> exchange.getMessage().getHeader(ReadDocumentRoute.DB_LOG_ID) != null);

        MockEndpointsUtil.checkAssertionsSatisfied(sqlMockEndpoint, endMockEndpoint);
    }

}