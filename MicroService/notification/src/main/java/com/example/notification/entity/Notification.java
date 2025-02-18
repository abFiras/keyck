package com.example.notification.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Column(nullable = false)
    private String message;
   // @Column(nullable = false)
    private String advertiserId; // ID de l'utilisateur qui reserve l appartement
   // @Column(nullable = false)
    private String propid; // id de user qui annonce l appartement
    private Long reservationId; // Ajout de l'ID de la réservation

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getPropid() {
        return propid;
    }

    public void setPropid(String propid) {
        this.propid = propid;
    }



    @Column(nullable = false)
    private boolean seen = false; // Si la notification a été vue

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Notification() {
    }

    public Notification(String advertiserId, String message) {
        this.advertiserId = advertiserId;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAdvertiserId() { return advertiserId; }
    public void setAdvertiserId(String advertiserId) { this.advertiserId = advertiserId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSeen() { return seen; }
    public void setSeen(boolean seen) { this.seen = seen; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
