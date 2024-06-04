package com.example.checkhrbackend_v2.controller;

import com.example.checkhrbackend_v2.model.Loan;
import com.example.checkhrbackend_v2.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
@CrossOrigin("*")

public class LoanController {
    @Autowired
    private LoanService loanService;

    @GetMapping
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    @GetMapping("/unchecked")
    public List<Loan> getUncheckedLoans() {
        return loanService.getUncheckedLoans();
    }

    @PostMapping("/approve/{id}")
    public Loan approveLoanById(@PathVariable Long id) {
        return loanService.approveLoanById(id);
    }

    @PostMapping("/decline/{id}")
    public Loan declineLoanById(@PathVariable Long id) {
        return loanService.declineLoanById(id);
    }

    @PostMapping("/request")
    public Loan requestLoan(@RequestBody Loan loan) {
        return loanService.requestLoan(loan);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Loan>> getLoansByUserId(@PathVariable Long userId) {
        List<Loan> loans = loanService.getLoansByUserId(userId);
        return ResponseEntity.ok(loans);
    }
}
