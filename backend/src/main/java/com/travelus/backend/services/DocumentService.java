package com.travelus.backend.services;

import com.travelus.backend.models.Document;
import com.travelus.backend.models.Group;
import com.travelus.backend.models.User;
import com.travelus.backend.repositories.DocumentRepository;
import com.travelus.backend.repositories.GroupRepository;
import com.travelus.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepo;

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SupabaseUploaderService uploaderService;

    public Document uploadPdf(Long groupId, MultipartFile file, String username) throws IOException {
        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String fileUrl = uploaderService.uploadPdf(fileName, file);

        Document doc = new Document();
        doc.setFileName(fileName);
        doc.setFileUrl(fileUrl);
        doc.setGroup(group);
        doc.setUploadedBy(user);
        doc.setUploadedAt(LocalDateTime.now());

        return documentRepo.save(doc);
    }


    public List<Document> getGroupDocuments(Long groupId) {
        return documentRepo.findByGroupId(groupId);
    }

    public void deleteDocument(Long documentId, String requestingUsername) {
        Document doc = documentRepo.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        // only uploader can delete
        if (!doc.getUploadedBy().getUsername().equals(requestingUsername)) {
            throw new SecurityException("You are not allowed to delete this document");
        }

        uploaderService.deletePdf(doc.getFileName());

        documentRepo.delete(doc);
    }

    public String getDocumentUrl(Long documentId) {
        Document doc = documentRepo.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        return doc.getFileUrl();
    }


}
