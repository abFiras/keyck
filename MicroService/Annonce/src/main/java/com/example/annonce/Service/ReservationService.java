package com.example.annonce.Service;
import com.example.annonce.Entity.Reservation;
import com.example.annonce.Entity.ReservationRequest;
import com.example.annonce.Entity.ReservationStatus;
import com.example.annonce.Repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation createReservation(ReservationRequest request) {
        Reservation reservation = new Reservation();
        reservation.setClientId(request.getClientId());
        reservation.setApartmentId(request.getApartmentId());
        reservation.setAnnonceurId(request.getAnnonceurId());
        reservation.setStartDate(request.getStartDate());
        reservation.setEndDate(request.getEndDate());
        reservation.setTotalPrice(request.getTotalPrice());
        reservation.setStatus(ReservationStatus.PENDING);

        return reservationRepository.save(reservation);
    }


    public Reservation updateReservationStatus(Long reservationId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }
    public List<Reservation> getReservationsByClient(Long clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    public List<Reservation> getReservationsByAnnonceur(Long annonceurId) {
        return reservationRepository.findByAnnonceurId(annonceurId);
    }

}
