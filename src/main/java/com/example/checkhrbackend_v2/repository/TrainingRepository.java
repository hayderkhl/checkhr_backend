package com.example.checkhrbackend_v2.repository;

import com.example.checkhrbackend_v2.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {

    public List<Training> findByStatusIsNull();
}
