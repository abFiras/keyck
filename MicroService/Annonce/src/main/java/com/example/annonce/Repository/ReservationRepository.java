package com.example.annonce.Repository;

import com.example.annonce.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
@Query("select r from Reservation r where r.appartement.userId=:ownerId")
    List<Reservation> findByOwnerId(@Param("ownerId") String ownerId);
}