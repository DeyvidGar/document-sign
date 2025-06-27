package com.midominio.camel.documentsign.service;

import com.midominio.camel.documentsign.models.SignDocumentRequest;
import com.midominio.camel.documentsign.utils.CamelExchangeSupport;
import org.apache.camel.Message;
import org.apache.camel.support.DefaultMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class SignDocumentRequestMapperTest extends CamelExchangeSupport {

    private SignDocumentRequestMapper mapper =
            new SignDocumentRequestMapper("signDocumentSignType", "signDocumentApiKey");

    @Test
    void makeSignDocumentRequest_test() {
        Message message = new DefaultMessage(getExchange());
        message.setHeader("ownerId", "ownerId");
        message.setHeader("documentType", "documentTypeValue");

//        byte[] body = new byte[]{};
        String body = "Hola mundo";
        message.setBody(body);

        getExchange().setMessage(message);

        SignDocumentRequest expectedRequest = new SignDocumentRequest();
        expectedRequest.setDocument(Base64.getEncoder().encodeToString(body.getBytes()));
        expectedRequest.setOwnerId("ownerId");
        expectedRequest.setSignType("signDocumentSignType");
        expectedRequest.setApiKey("signDocumentApiKey");
        expectedRequest.setDocumentType("documentTypeValue");

        mapper.makeSignDocumentRequest(getExchange());

        Assertions.assertEquals(expectedRequest, getExchange().getMessage().getBody());
    }

}