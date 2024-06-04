package com.example.checkhrbackend_v2.controller;

import com.example.checkhrbackend_v2.model.Notification;
import com.example.checkhrbackend_v2.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin("*")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/all")
    List<Notification> getNotifications(){
        return this.notificationService.getAllNotifications();
    }
    @GetMapping("/unread")
    List<Notification> getUnreadNotifcations(){
        return  this.notificationService.getUnreadNotifications();
    }
    @PostMapping("/makeRead/{id}")
    Notification makeNotificationRead(@PathVariable Long id) throws ClassNotFoundException{
        return this.notificationService.makeNotificationRead(id);
    }
}
