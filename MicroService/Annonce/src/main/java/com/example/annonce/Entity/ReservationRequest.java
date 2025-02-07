package com.example.annonce.Entity;

import lombok.Getter;

import java.time.LocalDate;
public class ReservationRequest {
    private Long clientId;
    private Long apartmentId;
    private Long annonceurId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalPrice;

    // Getters
    public Long getClientId() {
        return clientId;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public Long getAnnonceurId() {
        return annonceurId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    // Setters
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public void setAnnonceurId(Long annonceurId) {
        this.annonceurId = annonceurId;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}


