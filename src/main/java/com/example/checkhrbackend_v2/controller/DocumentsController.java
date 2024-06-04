package com.example.checkhrbackend_v2.controller;

import com.example.checkhrbackend_v2.model.Documents;
import com.example.checkhrbackend_v2.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documents")
@CrossOrigin("*")

public class DocumentsController {

    @Autowired
    private DocumentService documentsService;

    @GetMapping
    public ResponseEntity<List<Documents>> getAll() {
        List<Documents> documents = documentsService.getAllDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Documents> getById(@PathVariable Long id) {
        Documents document = documentsService.getById(id);
        if (document != null) {
            return new ResponseEntity<>(document, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public List<Documents> getDocumentsByUserId(@PathVariable Long userId) {
        return documentsService.getDocumentsByUserId(userId);
    }

    @PostMapping("/upload")
    public ResponseEntity<Documents> uploadDocument(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) throws IOException {
        Documents uploadedDocument = documentsService.uploadDocument(file, userId);
        return new ResponseEntity<>(uploadedDocument, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        documentsService.deleteById(id);
        Documents docs=documentsService.getById(id);
        if (docs != null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        // Fetch the document by its ID from the database
        Documents document = documentsService.getById(id);

        if (document != null) {
            try {
                // Get the document content as a byte array
                byte[] documentContent = documentsService.getDocumentContent(id);

                // Create HttpHeaders to specify content type and disposition
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", document.getDocname());

                // Return the document content in the response entity
                return new ResponseEntity<>(documentContent, headers, HttpStatus.OK);
            } catch (Exception e) {
                // Handle any exceptions, e.g., if the document is not found
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

