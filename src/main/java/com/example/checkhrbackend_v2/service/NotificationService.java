package com.example.checkhrbackend_v2.service;

import com.example.checkhrbackend_v2.model.Notification;
import com.example.checkhrbackend_v2.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getAllNotifications(){
        return this.notificationRepository.findAll();
    }
    public List<Notification> getUnreadNotifications(){
        return this.notificationRepository.findNotificationsByIsReadIs(false);
    }
    public Notification makeNotificationRead(Long id ) throws ClassNotFoundException{
        Optional<Notification> notif=this.notificationRepository.findById(id);
        if(notif.isPresent()){
            notif.get().setIsRead(true);
            return this.notificationRepository.save(notif.get());
        }else{
            throw new ClassNotFoundException("Cannot Find Notifications");
        }
    }
    public Notification addNotification (Notification notification){
        return this.notificationRepository.save(notification);
    }
}
