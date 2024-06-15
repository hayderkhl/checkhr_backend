package com.example.checkhrbackend_v2.service;


import com.example.checkhrbackend_v2.controller.ReportController;
import com.example.checkhrbackend_v2.model.Leaves;
import com.example.checkhrbackend_v2.model.Notification;
import com.example.checkhrbackend_v2.repository.LeaveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);


    public List<Leaves> getAllLeaves() {
        return leaveRepository.findAll();
    }
    public Leaves requestLeave(Leaves leave) {
        // Set the status to null when requesting leave
        leave.setStatus(null);
        return leaveRepository.save(leave);
    }
    public List<Leaves> getUncheckedLeaves() {
        return leaveRepository.findByStatusIsNull();
    }

    public Leaves approveLeaveById(Long id) {
        Leaves leave = leaveRepository.findById(id).orElseThrow();
        if (leave != null && leave.getStatus() == null) {
            leave.setStatus(true);
            this.sendWebSocketEvent();
            return leaveRepository.save(leave);
        }
        return null; // Leave not found or already approved/declined
    }

    public Leaves declineLeaveById(Long id) {
        Leaves leave = leaveRepository.findById(id).orElseThrow();
        if (leave != null && leave.getStatus() == null) {
            leave.setStatus(false);
            return leaveRepository.save(leave);
        }
        return null; // Leave not found or already approved/declined
    }
    public void sendWebSocketEvent() {
        Notification notif =new Notification();
        notif.setTitle("A New Leave");
        notif.setMessage("Someone Is Leaving ");
        System.out.println("notification sent");
        notificationService.addNotification(notif);
        messagingTemplate.convertAndSend("/topic/leaves-updates", notif);
    }

    public List<Leaves> getLeavesByUserId(Long userId) {
        return leaveRepository.findByUserId(userId);
    }

    public List<Leaves> getLeavesByUserIdAndYear(Long userId, int year) {
        return leaveRepository.findLeavesByUserIdAndYear(userId, year);
    }


    public String getUserFullNameById(Long userId) {
        List<String> fullNames = leaveRepository.findUserFullNamesByUserId(userId);

        if (fullNames.isEmpty()) {
            logger.warn("No full name found for user ID {}", userId);
            return null;
        } else if (fullNames.size() > 1) {
            logger.warn("Multiple full names found for user ID {}: {}", userId, fullNames);
            // Handle the case of multiple names, for example, return the first one
            return fullNames.get(0);
        } else {
            String fullName = fullNames.get(0);
            logger.info("Fetched full name for user ID {}: {}", userId, fullName);
            return fullName;
        }
    }
}
