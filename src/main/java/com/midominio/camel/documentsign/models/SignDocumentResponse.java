package com.midominio.camel.documentsign.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.Objects;

/** POJO class for request model. */
public class SignDocumentResponse {
    private String signedDocument;
    private String status;
    private String message;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime timestamp;

    public SignDocumentResponse() {
    }

    public SignDocumentResponse(String signedDocument, String status, String message, LocalDateTime timestamp) {
        this.signedDocument = signedDocument;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSignedDocument() {
        return signedDocument;
    }

    public void setSignedDocument(String signedDocument) {
        this.signedDocument = signedDocument;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SignDocumentResponse that = (SignDocumentResponse) o;
        return Objects.equals(signedDocument, that.signedDocument) && Objects.equals(status, that.status) && Objects.equals(message, that.message) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(signedDocument, status, message, timestamp);
    }

    @Override
    public String toString() {
        return "SignDocumentResponse{" +
                "signedDocument='" + signedDocument + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
