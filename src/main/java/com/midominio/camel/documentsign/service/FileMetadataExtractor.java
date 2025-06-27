package com.midominio.camel.documentsign.service;


import com.midominio.camel.documentsign.constants.AppConstants;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.stereotype.Service;

/** Service class. */
@Service("fileMetadataExtractor")
public class FileMetadataExtractor {

    /** Obtain values in file name. */
    public void parseFileName(Exchange exchange) {
        Message message = exchange.getMessage();

        String header = message.getHeader(Exchange.FILE_NAME, String.class);
        String[] split = header.split("_");

        if (split.length < 4) {
            throw new RuntimeException("Invalid format.");
        }

        message.setHeader(AppConstants.DOCUMENT_ID, split[0]);
        message.setHeader(AppConstants.OWNER_ID, split[1]);
        message.setHeader(AppConstants.DOCUMENT_TYPE, split[2]);
        message.setHeader(AppConstants.CLIENT_ID,  split[3].split(".pdf")[0]);
    }

}
