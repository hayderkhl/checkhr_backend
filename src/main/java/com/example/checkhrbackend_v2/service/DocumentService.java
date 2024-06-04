package com.example.checkhrbackend_v2.service;


import com.example.checkhrbackend_v2.model.Documents;
import com.example.checkhrbackend_v2.model.User;
import com.example.checkhrbackend_v2.repository.DocumentsRepository;
import com.example.checkhrbackend_v2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {


    @Autowired
    private DocumentsRepository documentsRepository;

    @Autowired
    private UserRepository userRepository;

    public byte[] getDocumentContent(Long id) throws IOException {
        Optional<Documents> optionalDocument = documentsRepository.findById(id);
        if (optionalDocument.isPresent()) {
            Documents document = optionalDocument.get();

            // Here, you need to implement the logic to fetch the document's content.
            // The actual implementation depends on how you store your documents.
            // Below is a placeholder implementation for demonstration purposes.

            // Example: Read the document content from a file and return it as a byte array
            try {
                File file = new File(document.getPath());
                byte[] fileContent = Files.readAllBytes(file.toPath());
                return fileContent;
            } catch (IOException e) {
                // Handle exceptions, e.g., if the file does not exist or cannot be read
                throw new IOException("Error reading document content", e);
            }
        }
        return null;
    }

    public Documents uploadDocument(MultipartFile file, Long userId) throws IOException {
        // Check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Define the directory where you want to store uploaded files
        String uploadDir = "com/example/checkhr/uploads"; // Update with your actual directory path

        // Create the upload directory if it doesn't exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate a unique filename for the uploaded file
        String originalFilename = file.getOriginalFilename();
        String uniqueFileName = userId + "_" + System.currentTimeMillis() + "_" + originalFilename;

        // Build the path to save the uploaded file
        Path filePath = Path.of(uploadDir, uniqueFileName);

        try {
            // Copy the file to the specified path
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Create a new Documents entity to store in the database
            Documents document = new Documents();
            document.setDocname(originalFilename);
            document.setDocsize((double) file.getSize());
            document.setPath(filePath.toString());
            document.setDate(new Date());
            User us =  this.userRepository.getById(userId);
            document.setUser(us); // Replace with the appropriate user retrieval

            // Save the document entity to the database
            return documentsRepository.save(document);
        } catch (IOException e) {
            // Handle the exception appropriately (e.g., log, throw custom exception)
            throw new IOException("Failed to upload the file: " + e.getMessage());
        }
    }
    public List<Documents> getAllDocuments() {
        return documentsRepository.findAll();
    }
    public Documents getById(Long id) {
        return documentsRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        documentsRepository.deleteById(id);
    }
    public List<Documents> getDocumentsByUserId(Long userId) {
        return documentsRepository.findByUserId(userId);
    }
}
