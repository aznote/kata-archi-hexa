package org.example.domaine.panier.port.out;

import org.example.domaine.panier.model.Produit;

import java.util.Optional;

public interface CatalogueProduitPort {

    Optional<Produit> rechercherParIdentifiant(String identifiant);
}