package com.midominio.camel.documentsign.service;

import com.midominio.camel.documentsign.constants.ExchangeConstants;
import com.midominio.camel.documentsign.constants.NumberConstants;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.stereotype.Service;

/** FileMetadataExtractor service class. */
@Service("fileMetadataExtractor")
public class FileMetadataExtractor {

    /**
     * Split file name value and to put the four values in exchange message headers.
     * @param exchange message in transit in Camel route.
     */
    public void parseFileName(Exchange exchange) {
        Message message = exchange.getMessage();

        String header = message.getHeader(Exchange.FILE_NAME, String.class);
        String[] split = header.split(ExchangeConstants.UNDERSCORE);

        if (split.length < NumberConstants.FOUR_INT) {
            throw new RuntimeException(ExchangeConstants.INVALID_FORMAT);
        }

        message.setHeader(ExchangeConstants.DOCUMENT_ID, split[NumberConstants.CERO_INT]);
        message.setHeader(ExchangeConstants.OWNER_ID, split[NumberConstants.ONE_INT]);
        message.setHeader(ExchangeConstants.DOCUMENT_TYPE, split[NumberConstants.TWO_INT]);
        message.setHeader(ExchangeConstants.CLIENT_ID,  split[NumberConstants.THREE_INT].split(ExchangeConstants.SUFFIX_FILENAME)[NumberConstants.CERO_INT]);
    }

}
