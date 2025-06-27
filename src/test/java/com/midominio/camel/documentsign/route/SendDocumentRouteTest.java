package com.midominio.camel.documentsign.route;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.midominio.camel.documentsign.models.ClientSendRequest;
import com.midominio.camel.documentsign.models.ClientSendResponse;
import com.midominio.camel.documentsign.models.SignDocumentResponse;
import com.midominio.camel.documentsign.service.ClientSendRequestMapper;
import com.midominio.camel.documentsign.utils.AdviceWithUtilConfigurable;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Base64;

import static org.apache.camel.builder.Builder.constant;

//import static org.apache.camel.support.builder.PredicateBuilder.constant;

@SpringBootTest
@CamelSpringBootTest
@ActiveProfiles({"sql", "jms"})
class SendDocumentRouteTest {

    private static final String START_TEST = "direct:test";
    private static final String MOCK_SQL = "mock:sql";
    private static final String MOCK_JMS = "mock:jms-sign-service";
    private static final String MOCK_END = "mock:at-final";

    @Produce(START_TEST)
    private ProducerTemplate producerTemplate;

    @EndpointInject(MOCK_JMS)
    private MockEndpoint jmsMockEndpoint;

    @EndpointInject(MOCK_SQL)
    private MockEndpoint sqlMockEndpoint;

    @EndpointInject(MOCK_END)
    private MockEndpoint endMockEndpoint;

    @Autowired
    private CamelContext context;

    @MockBean(name = "clientSendRequestMapper")
    private ClientSendRequestMapper mapper;

    @MockBean
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        AdviceWithUtilConfigurable adviceWith =
                new AdviceWithUtilConfigurable(context, SendDocumentRoute.ROUTE_ID);

        adviceWith.replaceFromWith(START_TEST);
        adviceWith.interceptAndSkipAndTo(
                SendDocumentRoute.JMS.getUri(),
                MOCK_JMS
        );
        adviceWith.interceptAndSkipAndTo(
                SendDocumentRoute.SQL_LOG_ENDPOINT.getUri(),
                MOCK_SQL
        );
        adviceWith.onCompletationTo(MOCK_END);
    }

    @Test
    void documentReadRoute_documentReadRouteBean() throws InterruptedException, JsonProcessingException {

        XmlMapper xmlMapper = new XmlMapper();

        String body = "body";
        String bodyEncoder = Base64.getEncoder().encodeToString(body.getBytes());

        SignDocumentResponse sResponse = new SignDocumentResponse();
        sResponse.setSignedDocument(bodyEncoder);
        sResponse.setMessage("Document signed");
        sResponse.setStatus("SignDocumentResponse: Status ok");
        sResponse.setTimestamp(LocalDateTime.now());

        byte[] responseBytes = xmlMapper.writeValueAsBytes(sResponse);

        ClientSendRequest cRequest = new ClientSendRequest();
        cRequest.setDocumentId("documentId");
        cRequest.setOwnerId("ownerId");
        cRequest.setDocument(bodyEncoder);
        cRequest.setClientId("clientId");

        byte[] requestBytes = xmlMapper.writeValueAsBytes(cRequest);

        ClientSendResponse cResponse = new ClientSendResponse();
        cResponse.setMessage("Document signed");
        cResponse.setStatus("SignDocumentResponse: Status ok");
        cResponse.setTimestamp(LocalDateTime.now());
        byte[] cResponseByte = xmlMapper.writeValueAsBytes(cResponse);


        jmsMockEndpoint.returnReplyBody(constant(xmlMapper.writeValueAsString(cResponse)));

        producerTemplate.sendBody(sResponse);

        jmsMockEndpoint.expectedMessageCount(1);
        jmsMockEndpoint.expectedBodiesReceived(cResponseByte);

        sqlMockEndpoint.expectedMessageCount(1);

        endMockEndpoint.expectedMessageCount(1);
        endMockEndpoint.expectedBodiesReceived(1);



    }
}