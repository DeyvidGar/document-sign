package com.midominio.camel.documentsign.constants;

/** Class for constants values in the flow of exchange message in the Camel routes. */
public class ExchangeConstants {

    /**
     * Headers.
     */
    /** Header value 'documentId'. */
    public static String DOCUMENT_ID = "documentId";
    /** Header value 'ownerId'. */
    public static String OWNER_ID = "ownerId";
    /** Header value 'documentType'. */
    public static String DOCUMENT_TYPE = "documentType";
    /** Header value 'clientId'. */
    public static String CLIENT_ID = "clientId";

    /**
     * Characters.
     */
    /** Value to split the file name in header. */
    public static String UNDERSCORE = "_";

    /**
     * Strings.
     */
    /** Value to split the suffix in file name in header. */
    public static String SUFFIX_FILENAME = ".pdf";

    /**
     * Messages.
     */
    /** Message to throw exception in the format on file name. */
    public static String INVALID_FORMAT = "Invalid format.";

    /** Private Constructor for not instance. */
    private ExchangeConstants() {
    }
}
