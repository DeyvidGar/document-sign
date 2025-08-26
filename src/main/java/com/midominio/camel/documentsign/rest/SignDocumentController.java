package com.midominio.camel.documentsign.rest;

import com.midominio.camel.documentsign.models.SignDocumentRequest;
import com.midominio.camel.documentsign.models.SignDocumentResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/** Sing document controller class to simulate external petition. */
@RestController
@RequestMapping("/api")
public class SignDocumentController {

    /**
     * Method to response with SignDocumentResponse model.
     * @param request object to create SignDocumentResponse object.
     * @return SignDocumentResponse object.
     */
    @PostMapping("/sign-document")
    public SignDocumentResponse signDocumentResponse(@RequestBody SignDocumentRequest request) {

        SignDocumentResponse signDocumentResponse = new SignDocumentResponse();
        signDocumentResponse.setSignedDocument(request.getDocument());
        signDocumentResponse.setStatus("Signed document");
        signDocumentResponse.setMessage("Status OK");
        signDocumentResponse.setTimestamp(LocalDateTime.now());

        return signDocumentResponse;
    }
}
