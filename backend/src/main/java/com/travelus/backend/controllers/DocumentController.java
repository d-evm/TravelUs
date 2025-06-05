package com.travelus.backend.controllers;

import com.travelus.backend.models.Document;
import com.travelus.backend.security.UserDetailsImpl;
import com.travelus.backend.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/groups/{groupId}/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPdf(
            @PathVariable Long groupId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        String username = userDetails.getUsername();
        Document doc = documentService.uploadPdf(groupId, file, username);
        return ResponseEntity.ok(Map.of(
                "fileName", doc.getFileName(),
                "fileUrl", doc.getFileUrl()
        ));
    }


    @GetMapping
    public ResponseEntity<?> listGroupDocuments(@PathVariable Long groupId) {
        List<Map<String, Object>> docs = documentService.getGroupDocuments(groupId).stream()
                .map(d -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", d.getId());
                    map.put("fileName", d.getFileName());
                    map.put("fileUrl", d.getFileUrl());
                    return map;
                })
                .collect(Collectors.toList());


        return ResponseEntity.ok(docs);

    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<?> deleteDocument(
            @PathVariable Long groupId,
            @PathVariable Long documentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            documentService.deleteDocument(documentId, userDetails.getUsername());
            return ResponseEntity.ok(Map.of("message", "Document deleted successfully"));
        } catch (SecurityException se) {
            return ResponseEntity.status(403).body(Map.of("error", se.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to delete document"));
        }
    }

    @GetMapping("/{documentId}/view")
    public ResponseEntity<Void> viewDocument(
            @PathVariable Long groupId,
            @PathVariable Long documentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String fileUrl = documentService.getDocumentUrl(documentId);
        return ResponseEntity.status(302) // HTTP redirect
                .header("Location", fileUrl)
                .build();
    }



}
