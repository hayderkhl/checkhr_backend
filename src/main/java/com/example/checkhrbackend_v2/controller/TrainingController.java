package com.example.checkhrbackend_v2.controller;

import com.example.checkhrbackend_v2.model.Training;
import com.example.checkhrbackend_v2.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainings")
@CrossOrigin("*")

public class TrainingController {
    @Autowired
    private TrainingService trainingService;

    @GetMapping
    public List<Training> getAllTrainings() {
        return trainingService.getAllTrainings();
    }

    @GetMapping("/unchecked")
    public List<Training> getUncheckedTrainings() {
        return trainingService.getUncheckedTrainings();
    }

    @PostMapping("/approve/{id}")
    public Training approveTrainingById(@PathVariable Long id) {
        return trainingService.approveTrainingById(id);
    }

    @PostMapping("/decline/{id}")
    public Training declineTrainingById(@PathVariable Long id) {
        return trainingService.declineTrainingById(id);
    }

    @PostMapping("/request")
    public Training requestTraining(@RequestBody Training training) {
        return trainingService.requestTraining(training);
    }
}
