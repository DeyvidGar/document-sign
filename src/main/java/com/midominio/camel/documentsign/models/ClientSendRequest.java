package com.midominio.camel.documentsign.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.Objects;

/** POJO class for xml request model. */
public class ClientSendRequest {
    private String documentId;
    private String ownerId;
    private String document;
    private String clientId;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime timestamp;

    public ClientSendRequest() {
    }

    @Override
    public String toString() {
        return "ClientSendRequest{" +
                "documentId='" + documentId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", document='" + document + '\'' +
                ", clientId='" + clientId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientSendRequest that = (ClientSendRequest) o;
        return Objects.equals(documentId, that.documentId) && Objects.equals(ownerId, that.ownerId) && Objects.equals(document, that.document) && Objects.equals(clientId, that.clientId) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentId, ownerId, document, clientId, timestamp);
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ClientSendRequest(String documentId, String ownerId, String document, String clientId, LocalDateTime timestamp) {
        this.documentId = documentId;
        this.ownerId = ownerId;
        this.document = document;
        this.clientId = clientId;
        this.timestamp = timestamp;
    }
}
