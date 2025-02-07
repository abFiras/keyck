package com.example.annonce.Entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

public class AppartementRequest {
    private int app;
    private String Addresse;
    private String ville;
    private String pays;
    private Date date_disponiblite;

    @ManyToMany(fetch = FetchType.EAGER ,cascade =  CascadeType.ALL)
    @JoinTable(name = "annonce_image" ,joinColumns = {@JoinColumn(name = "annonce_id")}
            ,inverseJoinColumns = {@JoinColumn (name = "image_id")})
    private Set<ImageModel> imageModels;
    private int nbrChambre;
    private String photos;
    private Type_logement typeLogement;

    public Set<ImageModel> getImageModels() {
        return imageModels;
    }

    public void setImageModels(Set<ImageModel> imageModels) {
        this.imageModels = imageModels;
    }

    public int getApp() {
        return app;
    }

    public void setApp(int app) {
        this.app = app;
    }

    private String description;

    private float montantContrubition;
    private int nbrPersonne;
    private String userId; // ID de l'utilisateur auquel cet appartement est affecté

    // Getters et Setters
    public String getAddresse() {
        return Addresse;
    }

    public void setAddresse(String addresse) {
        Addresse = addresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public Date getDate_disponiblite() {
        return date_disponiblite;
    }

    public void setDate_disponiblite(Date date_disponiblite) {
        this.date_disponiblite = date_disponiblite;
    }

    public int getNbrChambre() {
        return nbrChambre;
    }

    public void setNbrChambre(int nbrChambre) {
        this.nbrChambre = nbrChambre;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public Type_logement getTypeLogement() {
        return typeLogement;
    }

    public void setTypeLogement(Type_logement typeLogement) {
        this.typeLogement = typeLogement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getMontantContrubition() {
        return montantContrubition;
    }

    public void setMontantContrubition(float montantContrubition) {
        this.montantContrubition = montantContrubition;
    }

    public int getNbrPersonne() {
        return nbrPersonne;
    }

    public void setNbrPersonne(int nbrPersonne) {
        this.nbrPersonne = nbrPersonne;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
