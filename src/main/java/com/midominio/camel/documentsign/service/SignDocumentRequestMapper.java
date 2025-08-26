package com.midominio.camel.documentsign.service;

import com.midominio.camel.documentsign.constants.ExchangeConstants;
import com.midominio.camel.documentsign.models.SignDocumentRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service("signDocumentRequestMapper")
public class SignDocumentRequestMapper {

    /** Sign type value. */
    private final String signDocumentSignType;

    /** Api key value. */
    private final String signDocumentApiKey;

    /**
     * Constructor to inject values from properties configuration.
     * @param signDocumentSignType inject value.
     * @param signDocumentApiKey inject value.
     */
    public SignDocumentRequestMapper(
            @Value("app.signDocument.signType") String signDocumentSignType,
            @Value("app.signDocument.apiKey") String signDocumentApiKey
    ) {
        this.signDocumentSignType = signDocumentSignType;
        this.signDocumentApiKey = signDocumentApiKey;
    }

    /**
     * Build SignDocumentRequest model to send in JSON object.
     * @param exchange in transit in Camel route.
     */
    public void makeSignDocumentRequest(Exchange exchange) {
        Message message = exchange.getMessage();
        SignDocumentRequest request = new SignDocumentRequest();

        request.setDocument(Base64.getEncoder().encodeToString(message.getBody(byte[].class)));
        request.setOwnerId(message.getHeader(ExchangeConstants.OWNER_ID, String.class));
        request.setSignType(signDocumentSignType);
        request.setApiKey(signDocumentApiKey);
        request.setDocumentType(message.getHeader(ExchangeConstants.DOCUMENT_TYPE, String.class));

        message.setBody(request);
    }
}
