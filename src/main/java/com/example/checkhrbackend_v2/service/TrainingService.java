package com.example.checkhrbackend_v2.service;


import com.example.checkhrbackend_v2.model.Notification;
import com.example.checkhrbackend_v2.model.Training;
import com.example.checkhrbackend_v2.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {

    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    public List<Training> getUncheckedTrainings() {
        return trainingRepository.findByStatusIsNull();
    }

    public Training approveTrainingById(Long id) {
        Training training = trainingRepository.findById(id).orElse(null);
        if (training != null && training.getStatus() == null) {
            training.setStatus(true);
            this.sendWebSocketEvent();
            return trainingRepository.save(training);
        }
        return null; // Training not found or already approved/declined
    }

    public Training declineTrainingById(Long id) {
        Training training = trainingRepository.findById(id).orElse(null);
        if (training != null && training.getStatus() == null) {
            training.setStatus(false);
            return trainingRepository.save(training);
        }
        return null; // Training not found or already approved/declined
    }

    public Training requestTraining(Training training) {
        // Set the status to null when requesting a training
        training.setStatus(null);
        return trainingRepository.save(training);
    }
    public void sendWebSocketEvent() {
        Notification notif =new Notification();
        notif.setTitle("A new Training Has Been release");
        notif.setMessage(" A new Training Has Been release");
        System.out.println("notification sent");
        notificationService.addNotification(notif);
        messagingTemplate.convertAndSend("/topic/trainings-updates", notif);
    }
}
