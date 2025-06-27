package com.midominio.camel.documentsign.service;

import com.midominio.camel.documentsign.models.ClientSendRequest;
import com.midominio.camel.documentsign.models.SignDocumentResponse;
import com.midominio.camel.documentsign.utils.CamelExchangeSupport;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ClientSendRequestMapperTest extends CamelExchangeSupport {
    ClientSendRequestMapper clientSendRequestMapper = new ClientSendRequestMapper();

    @Test
    void test() {
        String body = "body";
        String bodyEncoder = Base64.getEncoder().encodeToString(body.getBytes());

        SignDocumentResponse sResponse = new SignDocumentResponse();
        sResponse.setSignedDocument(bodyEncoder);
        sResponse.setMessage("Document signed");
        sResponse.setStatus("SignDocumentResponse: Status ok");
        sResponse.setTimestamp(LocalDateTime.now());

        Map<String, Object> headers = new HashMap<>();
        headers.put("documentId", "documentId");
        headers.put("ownerId", "ownerId");
        headers.put("clientId", "clientId");

        getExchange().getMessage().setHeaders(headers);
        getExchange().getMessage().setBody(sResponse);

        clientSendRequestMapper.createClientSendRequest(getExchange());

        ClientSendRequest clientSendRequest = new ClientSendRequest();
        clientSendRequest.setDocumentId("documentId");
        clientSendRequest.setOwnerId("ownerId");
        clientSendRequest.setDocument(bodyEncoder);
        clientSendRequest.setClientId("clientId");

        assertEquals(clientSendRequest, getExchange().getMessage().getBody(ClientSendRequest.class));
    }

}