package com.example.checkhrbackend_v2.service;


import com.example.checkhrbackend_v2.model.Event;
import com.example.checkhrbackend_v2.model.Notification;
import com.example.checkhrbackend_v2.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private NotificationService notificationService;
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow();
    }

    public Event createEvent(Event event) {
        try{
            Event ev=eventRepository.save(event);
            this.sendWebSocketEvent(ev);
            return ev;
        }catch (Error err){
            throw new RuntimeException("Cannot Create event");
        }
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Event existingEvent = eventRepository.findById(id).orElseThrow();
        if (existingEvent != null) {
            // Update the existing event with the new data
            existingEvent.setEvent_name(updatedEvent.getEvent_name());
            existingEvent.setDate(updatedEvent.getDate());
            existingEvent.setContent(updatedEvent.getContent());
            existingEvent.setObjective(updatedEvent.getObjective());
            return eventRepository.save(existingEvent);
        }
        return null; // Event not found
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
    public void sendWebSocketEvent(Event event) {
        Notification notif =new Notification();
        notif.setTitle("A New Event");
        notif.setMessage(event.getObjective());
        System.out.println("notification sent");
        notificationService.addNotification(notif);
        messagingTemplate.convertAndSend("/topic/event-updates", event);
    }
}
