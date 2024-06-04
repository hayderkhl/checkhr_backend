package com.example.checkhrbackend_v2.repository;

import com.example.checkhrbackend_v2.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findNotificationsByIsReadIs(Boolean type);
}
