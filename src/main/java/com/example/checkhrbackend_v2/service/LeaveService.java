package com.example.checkhrbackend_v2.service;


import com.example.checkhrbackend_v2.model.Leaves;
import com.example.checkhrbackend_v2.model.Notification;
import com.example.checkhrbackend_v2.repository.LeaveRepository;
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
}
