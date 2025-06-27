package com.midominio.camel.documentsign.models;

import java.util.Objects;

/** POJO class for request model. */
public class SignDocumentRequest {

    private String document;
    private String ownerId;
    private String signType;
    private String apiKey;
    private String documentType;

    public SignDocumentRequest() {
    }

    public SignDocumentRequest(String document, String ownerId, String signType, String apiKey, String documentType) {
        this.document = document;
        this.ownerId = ownerId;
        this.signType = signType;
        this.apiKey = apiKey;
        this.documentType = documentType;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SignDocumentRequest that = (SignDocumentRequest) o;
        return Objects.equals(document, that.document) && Objects.equals(ownerId, that.ownerId) && Objects.equals(signType, that.signType) && Objects.equals(apiKey, that.apiKey) && Objects.equals(documentType, that.documentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(document, ownerId, signType, apiKey, documentType);
    }

    @Override
    public String toString() {
        return "DocumentSignRequest{" +
                "document='" + document + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", signType='" + signType + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", documentType='" + documentType + '\'' +
                '}';
    }
}
