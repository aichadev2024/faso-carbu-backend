package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.Carburant;
import com.fasocarbu.fasocarbu.services.interfaces.CarburantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carburants")
public class CarburantController {

    @Autowired
    private CarburantService carburantService;

    @PostMapping("/ajouter")
    public Carburant ajouterCarburant(@RequestBody Carburant carburant) {
        return carburantService.ajouterCarburant(carburant);
    }

    @GetMapping
    public List<Carburant> getAll() {
        return carburantService.getAllCarburants();
    }

    @GetMapping("/{id}")
    public Carburant getById(@PathVariable Long id) {
        return carburantService.getCarburantById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        carburantService.supprimerCarburant(id);
    }
}
