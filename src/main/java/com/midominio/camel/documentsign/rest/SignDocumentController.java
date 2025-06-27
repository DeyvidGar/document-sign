package com.midominio.camel.documentsign.rest;

import com.midominio.camel.documentsign.models.SignDocumentRequest;
import com.midominio.camel.documentsign.models.SignDocumentResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class SignDocumentController {

    @PostMapping("/sign-document")
    public SignDocumentResponse signDocumentResponse(@RequestBody SignDocumentRequest request) {
        System.out.println("request = " + request);

        SignDocumentResponse signDocumentResponse = new SignDocumentResponse();
        signDocumentResponse.setSignedDocument(request.getDocument());
        signDocumentResponse.setStatus("Signed document");
        signDocumentResponse.setMessage("Status OK");
        signDocumentResponse.setTimestamp(LocalDateTime.now());
        System.out.println("signDocumentResponse = " + signDocumentResponse);

        return signDocumentResponse;
    }

    @PostMapping("/hello")
    public String hello(@RequestBody Object object) {
        System.out.println("object = " + object);
        return "Hello world";
    }
}
