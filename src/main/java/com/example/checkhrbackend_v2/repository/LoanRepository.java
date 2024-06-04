package com.example.checkhrbackend_v2.repository;

import com.example.checkhrbackend_v2.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepository  extends JpaRepository<Loan, Long> {

    public List<Loan> findByStatusIsNull();

    @Query("SELECT l FROM Loan l WHERE l.user.id_user = :userId")
    List<Loan> findByUserId(Long userId);
}
