package org.example.domaine.panier.port.out;

import org.example.domaine.panier.model.Panier;

import java.util.Optional;

public interface PanierPort {

    Optional<Panier> rechercherParIdentifiant(String identifiant);

    void enregistrer(String identifiant, Panier panier);

    void supprimer(String identifiant);
}