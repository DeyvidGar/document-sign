package com.midominio.camel.documentsign.service;

import com.midominio.camel.documentsign.utils.CamelExchangeSupport;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.support.DefaultMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileMetadataExtractorTest extends CamelExchangeSupport {

    FileMetadataExtractor fileMetadataExtractor = new FileMetadataExtractor();

    /**
     * Given exchange message with a correct fileName in header.
     * When invoke parseFileMethod.
     * Then save the values of the fileName in headers.
     */
    @Test
    void parseFileName_test() {
        String documentId = "32144";
        String ownerId = "bigBank";
        String documentType = "taxReport";
        String clientId = "34582";

        String body = "Hello World";
        String fileName = documentId + "_" + ownerId + "_" + documentType + "_" + clientId + ".pdf";

        getMessage().setBody(body);
        getMessage().setHeader(Exchange.FILE_NAME, fileName);
        getExchange().setMessage(getMessage());

        fileMetadataExtractor.parseFileName(getExchange());

        Assertions.assertEquals(documentId, getMessage().getHeader("documentId"));
        Assertions.assertEquals(ownerId, getMessage().getHeader("ownerId"));
        Assertions.assertEquals(documentType, getMessage().getHeader("documentType"));
        Assertions.assertEquals(clientId, getMessage().getHeader("clientId"));
    }
}