package com.example.checkhrbackend_v2.repository;

import com.example.checkhrbackend_v2.model.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentsRepository extends JpaRepository<Documents, Long> {

    @Query("SELECT d FROM Documents d WHERE d.user.id_user = :userId")
    List<Documents> findByUserId(Long userId);


}
