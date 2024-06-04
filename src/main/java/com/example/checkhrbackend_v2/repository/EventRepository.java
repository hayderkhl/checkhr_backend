package com.example.checkhrbackend_v2.repository;

import com.example.checkhrbackend_v2.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
