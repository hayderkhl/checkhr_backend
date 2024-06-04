package com.example.checkhrbackend_v2.service;


import com.example.checkhrbackend_v2.model.Loan;
import com.example.checkhrbackend_v2.model.Notification;
import com.example.checkhrbackend_v2.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public List<Loan> getUncheckedLoans() {
        return loanRepository.findByStatusIsNull();
    }

    public Loan approveLoanById(Long id) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan != null && loan.getStatus() == null) {
            loan.setStatus(true);
            this.sendWebSocketEvent();
            return loanRepository.save(loan);
        }
        return null; // Loan not found or already approved/declined
    }

    public Loan declineLoanById(Long id) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan != null && loan.getStatus() == null) {
            loan.setStatus(false);
            return loanRepository.save(loan);
        }
        return null; // Loan not found or already approved/declined
    }

    public Loan requestLoan(Loan loan) {
        // Set the status to null when requesting a loan
        loan.setStatus(null);
        return loanRepository.save(loan);
    }
    public void sendWebSocketEvent() {
        Notification notif =new Notification();
        notif.setTitle("A new Loan Has Been validated");
        notif.setMessage(" A new Loan Has Been validated");
        System.out.println("notification sent");
        notificationService.addNotification(notif);
        messagingTemplate.convertAndSend("/topic/loans-updates", notif);
    }

    public List<Loan> getLoansByUserId(Long userId) {
        return loanRepository.findByUserId(userId);
    }
}
