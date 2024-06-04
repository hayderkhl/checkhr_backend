package com.example.checkhrbackend_v2.controller;

import com.example.checkhrbackend_v2.model.Note;
import com.example.checkhrbackend_v2.model.Notification;
import com.example.checkhrbackend_v2.repository.NoteRepository;
import com.example.checkhrbackend_v2.service.NoteService;
import com.example.checkhrbackend_v2.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@CrossOrigin("*")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NoteRepository noteRepository;

    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id);
    }

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        try{
            Note createdNote= noteRepository.save(note);
            this.sendWebSocketNote(createdNote);
            return createdNote;
        }catch (Error err){
            throw new RuntimeException(err.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id, @RequestBody Note note) {
        return noteService.updateNote(id, note);
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
    }
    public void sendWebSocketNote(Note note) {
        Notification notif =new Notification();
        notif.setTitle("A New Note");
        notif.setMessage(note.getContent());
        System.out.println("notification sent");
        notificationService.addNotification(notif);
        messagingTemplate.convertAndSend("/topic/note-updates", note);
    }
}

