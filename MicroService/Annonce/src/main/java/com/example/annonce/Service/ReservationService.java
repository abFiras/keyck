package com.example.annonce.Service;
import com.example.annonce.Entity.*;
import com.example.annonce.Iservice.NotificationClient;
import com.example.annonce.Repository.AppartementRepository;
import com.example.annonce.Repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    @Autowired
    private NotificationClient notificationClient;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AppartementRepository appartementRepository;
  /*  private MessageSendingOperations<String> wsTemplate;

    public Reservation createReservation(ReservationRequest request,int idApp,String clientid) {
        Optional<Appartement> appartement=appartementRepository.findById(idApp);
        if (!appartement.isPresent()){
            return null;
        }
        Appartement appartement1=appartement.get();
        Reservation reservation = new Reservation();
        reservation.setClientId(clientid);
        reservation.setAppartement(appartement1);
        reservation.setStartDate(request.getStartDate());
        reservation.setEndDate(request.getEndDate());
       // reservation.setTotalPrice(request.getTotalPrice());
        reservation.setStatus(ReservationStatus.PENDING);
       // wsTemplate.convertAndSend("/topic/notification/" ,  "malek "+" a ajouté une nouvelle reclamtion");
        sendNotification(reservation);
        wsTemplate.convertAndSend("/topic/notification", notificationRequest);

        return reservationRepository.save(reservation);
    }

    private void sendNotification(Reservation reservation) {
        String advertiserId = reservation.getClientId();  // Retrieve the actual advertiser ID
        String message = "You have a new reservation! from: " + advertiserId;
        String userId=reservation.getAppartement().getUserId();
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setPropid(userId);
        notificationRequest.setAdvertiserId(advertiserId);
        notificationRequest.setMessage(message);
       // notificationRequest.setReservationId(String.valueOf(reservation.getId()));
        System.out.println(message);
        // Call the Notification Microservice to send the notification
        notificationClient.sendNotification(notificationRequest);
    }
*/
  @Autowired
  private SimpMessagingTemplate wsTemplate;  // Inject SimpMessagingTemplate for WebSocket messaging

    public Reservation createReservation(ReservationRequest request, int idApp, String clientid) {
        Optional<Appartement> appartement = appartementRepository.findById(idApp);
        if (!appartement.isPresent()) {
            return null;
        }
        Appartement appartement1 = appartement.get();
        Reservation reservation = new Reservation();
        reservation.setClientId(clientid);
        reservation.setAppartement(appartement1);
        reservation.setStartDate(request.getStartDate());
        reservation.setEndDate(request.getEndDate());
        reservation.setStatus(ReservationStatus.PENDING);
        Reservation savedReservation = reservationRepository.save(reservation);

        // 🔹 Maintenant, on peut envoyer la notification avec un ID non-null
        createNotificationRequest(savedReservation);

        // 🔹 Envoyer WebSocket après enregistrement
        //NotificationRequest notificationRequest = createNotificationRequest(savedReservation);
       // wsTemplate.convertAndSend("/topic/notification", notificationRequest);  // Ensure the message is sent to the WebSocket topic

        return reservationRepository.save(reservation);
    }



    private NotificationRequest createNotificationRequest(Reservation reservation) {
        String advertiserId = reservation.getClientId();
        String message = "You have a new reservation! from: " + advertiserId;
        String userId = reservation.getAppartement().getUserId();
        Long reservationId=reservation.getId();
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setPropid(userId);
        notificationRequest.setAdvertiserId(advertiserId);
        notificationRequest.setMessage(message);
        notificationRequest.setReservationId(reservationId);
        // Optional: notificationRequest.setReservationId(String.valueOf(reservation.getId()));
        notificationClient.sendNotification(notificationRequest);  // Send notification to the notification microservice

        return notificationRequest;
    }


    public Reservation updateReservationStatus(Long reservationId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }
    public List<Reservation> getReservationsByOwner(String ownerId) {

        return reservationRepository.findByOwnerId(ownerId);
    }

}
