package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.Utilisateur;
import com.fasocarbu.fasocarbu.services.interfaces.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/ajouter")
    public void ajouterUtilisateur(@RequestBody Utilisateur utilisateur) {
        utilisateurService.enregistrerUtilisateur(utilisateur);
    }

    @GetMapping("/{id}")
    public Utilisateur getUtilisateurById(@PathVariable Integer id) {
        return utilisateurService.getUtilisateurById(id);
    }

    @GetMapping
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    @DeleteMapping("/{id}")
    public void supprimerUtilisateur(@PathVariable Integer id) {
        utilisateurService.supprimerUtilisateur(id);
    }
}
