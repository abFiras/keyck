package com.example.annonce.Controller;

import com.example.annonce.Entity.*;
import com.example.annonce.Service.AppartementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class AnnonceController {


    @Autowired
    private AppartementService appartementService;

    @PreAuthorize("hasRole('ROLE_PROPRIETAIRE')")
    @GetMapping()
    public List<Appartement> getallAnnonce() {
        return appartementService.getallAnnonce();
    }

    @PostMapping(value = {""}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Appartement> addAppartement(
            @RequestPart("request") AppartementRequest request,
            @RequestPart("imageFile") MultipartFile[] file,
            @RequestParam String userId
    ) {
        try {

            Set<ImageModel> imageModelSet = uploadImage(file);
            request.setImageModels(imageModelSet);
            Appartement appartement = appartementService.addAppartement(request, userId);
            return ResponseEntity.ok(appartement);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload des fichiers", e);
        }
    }

    public Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();
        for (MultipartFile file : multipartFiles) {
            System.out.println("Fichier reçu: " + file.getOriginalFilename());
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            ImageModel imageModel = new ImageModel();
            imageModel.setFilePath("C:\\xampp\\htdocs\\AnnoncePFE\\" + fileName);
            imageModel.setBytes(file.getBytes());

            // Sauvegarder physiquement le fichier sur le système de fichiers
            saveImageToFileSystem(file, fileName);
            imageModels.add(imageModel);
        }
        return imageModels;
    }

    public void saveImageToFileSystem(MultipartFile file, String fileName) throws IOException {
        String uploadDir = "C:\\xampp\\htdocs\\AnnoncePFE\\"; // Chemin vers le dossier de destination

        // Créer le dossier s'il n'existe pas déjà
        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);

        // Écrire le fichier sur le système de fichiers
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());
    }

    @GetMapping("/{pid}")
    public Appartement getAppartementById(@PathVariable int pid) {
        return appartementService.getAppartementById(pid);
    }


}
