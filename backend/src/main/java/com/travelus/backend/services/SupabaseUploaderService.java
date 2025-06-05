package com.travelus.backend.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class SupabaseUploaderService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucketName;

    private WebClient webClient;

    @PostConstruct
    public void initWebClient() {
        this.webClient = WebClient.builder()
                .baseUrl(supabaseUrl + "/storage/v1")
                .defaultHeader("apikey", supabaseKey)
                .defaultHeader("Authorization", "Bearer " + supabaseKey)
                .build();
    }


    public String uploadPdf(String filename, MultipartFile file) throws IOException {
        try {
            byte[] fileBytes = file.getBytes();

            webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/object/" + bucketName + "/" + filename)
                            .queryParam("name", filename)
                            .build())
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .header("x-upsert", "true")
                    .bodyValue(fileBytes)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            return supabaseUrl + "/storage/v1/object/public/" +
                    bucketName + "/" +
                    URLEncoder.encode(filename, StandardCharsets.UTF_8);

        } catch (Exception e) {
            System.err.println("UPLOAD ERROR: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Upload failed", e);
        }
    }

    public void deletePdf(String fileName) {
        try {
            String filePath = bucketName + "/" + fileName;

            System.out.println("🗑 DELETE request to Supabase:");
            System.out.println("➡️ URL: " + supabaseUrl + "/storage/v1/object/delete");
            System.out.println("➡️ File path: " + filePath);

            webClient.post()
                    .uri("/object/delete")
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .bodyValue(List.of(filePath)) // ✅ Just an array, not a map
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            System.out.println("✅ Successfully deleted from Supabase: " + filePath);

        } catch (Exception e) {
            System.err.println("❌ DELETE ERROR: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete file: " + fileName);
        }
    }

}