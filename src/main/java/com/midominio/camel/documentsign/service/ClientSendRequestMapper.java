package com.midominio.camel.documentsign.service;

import com.midominio.camel.documentsign.constants.ExchangeConstants;
import com.midominio.camel.documentsign.models.ClientSendRequest;
import com.midominio.camel.documentsign.models.SignDocumentResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.stereotype.Service;

/** ClientSendRequestMapper service class. */
@Service("clientSendRequestMapper")
public class ClientSendRequestMapper {

    /**
     * Create ClientSendRequest model to send in XML object.
     * @param exchange message in transit in Camel route.
     */
    public void createClientSendRequest(Exchange exchange) {
        Message message = exchange.getMessage();
        SignDocumentResponse signDocumentResponse = message.getBody(SignDocumentResponse.class);

        ClientSendRequest clientSendRequest = new ClientSendRequest();
        clientSendRequest.setDocumentId(message.getHeader(ExchangeConstants.DOCUMENT_ID, String.class));
        clientSendRequest.setOwnerId(message.getHeader(ExchangeConstants.OWNER_ID, String.class));
        clientSendRequest.setDocument(signDocumentResponse.getSignedDocument());
        clientSendRequest.setClientId(message.getHeader(ExchangeConstants.CLIENT_ID, String.class));

        message.setBody(clientSendRequest);
    }
}
