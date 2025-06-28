package com.midominio.camel.documentsign.route;

import com.midominio.camel.documentsign.service.FileMetadataExtractor;
import com.midominio.camel.documentsign.utils.AdviceWithUtilConfigurable;
import com.midominio.camel.documentsign.utils.MockEndpointsUtil;
import com.midominio.camel.documentsign.utils.TestConstants;
import org.apache.camel.*;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.sql.SqlConstants;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.ExcludeRoutes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

/** Unit test for ReadDocumentRoute */
@SpringBootTest
@CamelSpringBootTest
class ReadDocumentRouteUnitTest {

    /** Uris name for mock endpoints. */
    private static final String START_TEST = "direct:test";
    private static final String MOCK_SQL = "mock:sql";
    private static final String MOCK_END = "mock:at-final";
    private static final String MOCK_ERROR = "mock:on-exception-sql";

    /** Initial endpoint. */
    @Produce(START_TEST)
    private ProducerTemplate producerTemplate;

    /** Mock endpoints in route. */
    @EndpointInject(MOCK_SQL)
    private MockEndpoint sqlMockEndpoint;
    @EndpointInject(MOCK_END)
    private MockEndpoint endMockEndpoint;
    @EndpointInject(MOCK_ERROR)
    private MockEndpoint errorMockEndpoint;

    /** Mock bean in route. */
    @MockBean(name = "fileMetadataExtractor")
    private FileMetadataExtractor fileMetadataExtractor;

    @Autowired
    private CamelContext context;

    @MockBean
    private DataSource dataSource;

    /** Set up method. */
    @BeforeEach
    public void setUp() throws Exception {
        //rest
        AdviceWithUtilConfigurable adviceWith = new AdviceWithUtilConfigurable(context, ReadDocumentRoute.ROUTE_ID);

        adviceWith.replaceFromWith(START_TEST);
        adviceWith.interceptAndSkipAndTo(
                ReadDocumentRoute.SQL_LOG_ENDPOINT.getUri(),
                MOCK_SQL
        );
        adviceWith.interceptAndSkipAndTo(
                SignDocumentRoute.ROUTE_START, MOCK_END);
        adviceWith.interceptAndSkipAndTo(
                ErrorHandlingConfiguration.SQL.getUri(), MOCK_ERROR);
    }

    /**
     * Given an exchange.
     * When RaadDocumentRoute is invokes.
     * Then verify mock endpoints.
     */
    @Test
    void documentReadRoute_documentReadRouteBean() throws Exception {

        producerTemplate.sendBodyAndHeader(TestConstants.BODY, Exchange.FILE_NAME, TestConstants.HEADER_VALUE);

        Mockito.verify(fileMetadataExtractor, Mockito.times(TestConstants.ONE_INT))
                .parseFileName(Mockito.any(Exchange.class));

        sqlMockEndpoint.expectedMessageCount(TestConstants.ONE_INT);
        endMockEndpoint.expectedMessageCount(TestConstants.ONE_INT);

        errorMockEndpoint.expectedMessageCount(0);
        MockEndpointsUtil.checkAssertionsSatisfied(sqlMockEndpoint, endMockEndpoint, errorMockEndpoint);
    }

    @Test
    void documentReadRoute_exception() throws InterruptedException {
        RuntimeException runtimeException = new RuntimeException("Invalid format.");
        doThrow(runtimeException).when(fileMetadataExtractor).parseFileName(Mockito.any(Exchange.class));

        assertThrows(runtimeException.getClass(), () ->{
            producerTemplate.sendBodyAndHeader(TestConstants.BODY, Exchange.FILE_NAME, TestConstants.HEADER_VALUE);
        });

        sqlMockEndpoint.expectedMessageCount(0);
        endMockEndpoint.expectedMessageCount(0);
        errorMockEndpoint.expectedMessageCount(0);
        MockEndpointsUtil.checkAssertionsSatisfied(sqlMockEndpoint, endMockEndpoint, errorMockEndpoint);
    }

}