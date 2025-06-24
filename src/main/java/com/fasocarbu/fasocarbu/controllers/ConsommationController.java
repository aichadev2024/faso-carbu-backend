package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.Consommation;
import com.fasocarbu.fasocarbu.repositories.ConsommationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consommation")
public class ConsommationController {
    @Autowired
    private ConsommationRepository consommationRepository;

    @GetMapping
    public List<Consommation> getAllConsommation(){

        return consommationRepository.findAll();
    }
    @PostMapping
    public Consommation createConsommation(@RequestBody Consommation consommation){
        return consommationRepository.save(consommation);
    }
}
