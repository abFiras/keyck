package com.example.notification.controller;

import com.example.notification.entity.Notification;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
    @RequiredArgsConstructor
    public class NotificationController {

        private final NotificationService notificationService;
   // @PreAuthorize("hasRole('ROLE_PROPRIETAIRE')")
        @GetMapping("/get")
        public List<Notification> Getnotification(@RequestParam("userid") String userid){
           return notificationService.Getnotification(userid);
        }
    //@PreAuthorize("hasRole('ROLE_PROPRIETAIRE')")
    @MessageMapping("/sendNotification")
    @SendTo("/topic/notification")
    public Notification sendNotificationViaWebSocket(Notification notification) {
        // Vous pouvez traiter la notification ici si nécessaire (par exemple, la sauvegarder dans la base de données)
        notificationService.saveAndSendNotification(notification);
        return notification;  // Cela enverra la notification aux abonnés du canal /topic/notification
    }
    @PostMapping("/sendNotification")
    public ResponseEntity<String> sendNotification(@RequestBody Notification notification) {
        notificationService.saveAndSendNotification(notification);
        return ResponseEntity.ok("Notification sent successfully!");
    }
}