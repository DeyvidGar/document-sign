package com.midominio.camel.documentsign.service;

import com.midominio.camel.documentsign.models.ClientSendRequest;
import com.midominio.camel.documentsign.models.SignDocumentResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.stereotype.Service;

/** ClientSendRequestMapper service class. */
@Service("clientSendRequestMapper")
public class ClientSendRequestMapper {

    /**
     * Create ClientSendRequest to get xml object.
     * @param exchange
     */
    public void createClientSendRequest(Exchange exchange) {
        Message message = exchange.getMessage();
        SignDocumentResponse signDocumentResponse = message.getBody(SignDocumentResponse.class);

        ClientSendRequest clientSendRequest = new ClientSendRequest();
        clientSendRequest.setDocumentId(message.getHeader("documentId", String.class));
        clientSendRequest.setOwnerId(message.getHeader("ownerId", String.class));
        clientSendRequest.setDocument(signDocumentResponse.getSignedDocument());
        clientSendRequest.setClientId(message.getHeader("clientId", String.class));

        message.setBody(clientSendRequest);
    }
}
