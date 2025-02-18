package com.example.annonce.Entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
    public class Appartement {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int idApp;
        private boolean status;
        private String Addresse;
        private String ville;
        private String pays;
    @Temporal(TemporalType.DATE)

    private Date date_disponiblite;
        private int nbrChambre;
    @ManyToMany(fetch = FetchType.EAGER ,cascade =  CascadeType.ALL)
    @JoinTable(name = "annonce_image" ,joinColumns = {@JoinColumn(name = "annonce_id")}
            ,inverseJoinColumns = {@JoinColumn (name = "image_id")})
    private Set<ImageModel> imageModels;

        @Enumerated(EnumType.STRING)
        private Type_logement typeLogement;

        private String description;
        private float montantContrubition;
        private int nbrPersonne;
        @OneToMany
        private Set<Reservation> reservations;
    private String userId; // ID de l'utilisateur provenant du microservice User

    public Set<ImageModel> getImageModels() {
        return imageModels;
    }

    public void setImageModels(Set<ImageModel> imageModels) {
        this.imageModels = imageModels;
    }

    public int getIdApp() {
        return idApp;
    }

    public void setIdApp(int idApp) {
        this.idApp = idApp;
    }

    // Getter and Setter for status
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // Getter and Setter for Addresse
    public String getAddresse() {
        return Addresse;
    }

    public void setAddresse(String addresse) {
        Addresse = addresse;
    }

    // Getter and Setter for ville
    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    // Getter and Setter for pays
    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    // Getter and Setter for date_disponiblite
    public Date getDate_disponiblite() {
        return date_disponiblite;
    }

    public void setDate_disponiblite(Date date_disponiblite) {
        this.date_disponiblite = date_disponiblite;
    }

    // Getter and Setter for nbrChambre
    public int getNbrChambre() {
        return nbrChambre;
    }

    public void setNbrChambre(int nbrChambre) {
        this.nbrChambre = nbrChambre;
    }



    // Getter and Setter for typeLogement
    public Type_logement getTypeLogement() {
        return typeLogement;
    }

    public void setTypeLogement(Type_logement typeLogement) {
        this.typeLogement = typeLogement;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and Setter for montantContrubition
    public float getMontantContrubition() {
        return montantContrubition;
    }

    public void setMontantContrubition(float montantContrubition) {
        this.montantContrubition = montantContrubition;
    }

    // Getter and Setter for nbrPersonne
    public int getNbrPersonne() {
        return nbrPersonne;
    }

    public void setNbrPersonne(int nbrPersonne) {
        this.nbrPersonne = nbrPersonne;
    }

    // Getter and Setter for userId
    public String getUserId() {
        return userId;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
