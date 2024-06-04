package com.example.checkhrbackend_v2.repository;

import com.example.checkhrbackend_v2.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
