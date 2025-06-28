package com.midominio.camel.documentsign.route;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midominio.camel.documentsign.models.SignDocumentRequest;
import com.midominio.camel.documentsign.models.SignDocumentResponse;
import com.midominio.camel.documentsign.service.SignDocumentRequestMapper;
import com.midominio.camel.documentsign.utils.AdviceWithUtilConfigurable;
import com.midominio.camel.documentsign.utils.MockEndpointsUtil;
import org.apache.camel.*;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.sql.SqlConstants;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.camel.builder.Builder.constant;

@SpringBootTest
@CamelSpringBootTest
@ActiveProfiles({"sftp", "sql"})
class SignDocumentRouteTest {

    private static final String START_TEST = "direct:test";
    private static final String MOCK_SQL = "mock:sql";
    private static final String MOCK_HTTP_SIGN_SERVICE = "mock:http-sign-service";
    private static final String MOCK_END = "mock:at-final";

    @Produce(START_TEST)
    private ProducerTemplate producerTemplate;

    @EndpointInject(MOCK_HTTP_SIGN_SERVICE)
    private MockEndpoint httpMockEndpoint;

    @EndpointInject(MOCK_SQL)
    private MockEndpoint sqlMockEndpoint;

    @EndpointInject(MOCK_END)
    private MockEndpoint endMockEndpoint;

    @Autowired
    private CamelContext context;

    @MockBean(name = "signDocumentRequestMapper")
    private SignDocumentRequestMapper mapper;

    @MockBean
    private DataSource dataSource;

    @Autowired
    private ObjectMapper objectMapper;

    /** Set up method. */
    @BeforeEach
    void setUp() throws Exception {
        AdviceWithUtilConfigurable adviceWith =
                new AdviceWithUtilConfigurable(context, SignDocumentRoute.ROUTE_ID);

        adviceWith.replaceFromWith(START_TEST);
        adviceWith.interceptAndSkipAndTo(
                SignDocumentRoute.HTTP_ENDPOINT.getUri(),
                MOCK_HTTP_SIGN_SERVICE
        );
        adviceWith.interceptAndSkipAndTo(
                SignDocumentRoute.SQL_LOG_ENDPOINT.getUri(),
                MOCK_SQL
        );
        adviceWith.interceptAndSkipAndTo(
                SendDocumentRoute.START_ROUTE,
                MOCK_END
        );
    }

    @Test
    void documentReadRoute_documentReadRouteBean() throws InterruptedException, JsonProcessingException {

        String body = "body";

        SignDocumentRequest expectedRequest = new SignDocumentRequest();
        expectedRequest.setDocument(Base64.getEncoder().encodeToString(body.getBytes()));
        expectedRequest.setOwnerId("ownerId");
        expectedRequest.setSignType("signDocumentSignType");
        expectedRequest.setApiKey("signDocumentApiKey");
        expectedRequest.setDocumentType("documentTypeValue");

        Map<String, Object> headers = new HashMap<>();
        headers.put(Exchange.FILE_NAME, "fileName");
        headers.put("ownerId", "ownerId");
        headers.put("documentType", "documentTypeValue");

        byte[] serializedExpectedRequest = objectMapper.writeValueAsBytes(expectedRequest);

        SignDocumentResponse expectedResponse = new SignDocumentResponse();
        expectedResponse.setSignedDocument(body);
        expectedResponse.setStatus("Status ok");
        expectedResponse.setMessage("Document signed");
        expectedResponse.setTimestamp(LocalDateTime.now());

        httpMockEndpoint.returnReplyBody(constant(objectMapper.writeValueAsBytes(expectedResponse)));

        producerTemplate.sendBodyAndHeaders(body, headers);

        Mockito.verify(mapper, Mockito.times(1)).makeSignDocumentRequest(Mockito.any(Exchange.class));

        httpMockEndpoint.expectedMessageCount(1);
        httpMockEndpoint.expectedBodiesReceived(List.of(serializedExpectedRequest));

        sqlMockEndpoint.expectedMessageCount(1);
        sqlMockEndpoint.expectedHeaderReceived(SqlConstants.SQL_RETRIEVE_GENERATED_KEYS, "true");

        endMockEndpoint.expectedMessageCount(1);
        endMockEndpoint.expectedMessagesMatches(exchange -> exchange.getMessage().getHeader(ReadDocumentRoute.DB_LOG_ID) != null);

        MockEndpointsUtil.checkAssertionsSatisfied(httpMockEndpoint, sqlMockEndpoint, endMockEndpoint);
    }

}