package com.example.annonce.Service;

import com.example.annonce.Entity.Appartement;
import com.example.annonce.Entity.AppartementRequest;
import com.example.annonce.Entity.ImageModel;
import com.example.annonce.Iservice.UserServiceClient;
import com.example.annonce.Repository.AppartementRepository;
import com.example.annonce.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppartementService {

    @Autowired
    private AppartementRepository appartementRepository;
    @Autowired
    private UserServiceClient userServiceClient;
    @Autowired
    private ImageRepository imageRepository;

    public Appartement addAppartement(AppartementRequest request, String userId) {
        Appartement appartement = new Appartement();

        appartement.setAddresse(request.getAddresse());
        appartement.setVille(request.getVille());
        appartement.setPays(request.getPays());
        appartement.setDate_disponiblite(request.getDate_disponiblite());
        appartement.setNbrChambre(request.getNbrChambre());
        appartement.setTypeLogement(request.getTypeLogement());
        appartement.setDescription(request.getDescription());
        appartement.setImageModels(request.getImageModels());
        appartement.setMontantContrubition(request.getMontantContrubition());
        appartement.setNbrPersonne(request.getNbrPersonne());
        appartement.setUserId(userId);

        return appartementRepository.save(appartement);
    }

    public Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();
        for (MultipartFile file : multipartFiles) {
            System.out.println("Fichier reçu: " + file.getOriginalFilename());
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            ImageModel imageModel = new ImageModel();
            imageModel.setFilePath("C:\\xampp\\htdocs\\Product\\" + fileName);
            imageModel.setBytes(file.getBytes());

            // Sauvegarder physiquement le fichier sur le système de fichiers
            saveImageToFileSystem(file, fileName);
            imageModels.add(imageModel);
        }
        return imageModels;
    }

    public void saveImageToFileSystem(MultipartFile file, String fileName) throws IOException {
        String uploadDir = "C:\\xampp\\htdocs\\Product\\"; // Chemin vers le dossier de destination

        // Créer le dossier s'il n'existe pas déjà
        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);

        // Écrire le fichier sur le système de fichiers
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());
    }
    public List<Appartement> getallAnnonce(){
        return appartementRepository.findAll();
    }

    public Appartement getAppartementById(int id) {

        return appartementRepository.findById(id).orElse(null);
    }

}
