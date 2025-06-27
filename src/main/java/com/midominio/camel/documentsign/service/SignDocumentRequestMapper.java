package com.midominio.camel.documentsign.service;

import com.midominio.camel.documentsign.models.SignDocumentRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service("signDocumentRequestMapper")
public class SignDocumentRequestMapper {

    private final String signDocumentSignType;
    private final String signDocumentApiKey;

    public SignDocumentRequestMapper(
            @Value("app.signDocument.signType")
            String signDocumentSignType,
            @Value("app.signDocument.apiKey")
            String signDocumentApiKey) {
        this.signDocumentSignType = signDocumentSignType;
        this.signDocumentApiKey = signDocumentApiKey;
    }

    /** Build SignDocumentRequest. */
    public void makeSignDocumentRequest(Exchange exchange) {
        Message message = exchange.getMessage();
        SignDocumentRequest request = new SignDocumentRequest();
        request.setDocument(Base64.getEncoder().encodeToString(message.getBody(byte[].class)));
        request.setOwnerId(message.getHeader("ownerId", String.class));
        request.setSignType(signDocumentSignType);
        request.setApiKey(signDocumentApiKey);
        request.setDocumentType(message.getHeader("documentType", String.class));

        message.setBody(request);
    }
}
