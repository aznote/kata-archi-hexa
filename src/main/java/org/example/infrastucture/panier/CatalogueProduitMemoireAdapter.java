package org.example.infrastucture.panier;


import org.example.domaine.panier.model.Produit;
import org.example.domaine.panier.port.out.CatalogueProduitPort;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class CatalogueProduitMemoireAdapter
        implements CatalogueProduitPort {

    private final Map<String, Produit> produits = new HashMap<>();

    @Override
    public Optional<Produit> rechercherParIdentifiant(String identifiant) {
        return Optional.ofNullable(produits.get(identifiant));
    }

    public void ajouterProduitCatalogue(Produit produit) {
        if (produit == null) {
            throw new IllegalArgumentException("Le produit est obligatoire");
        }

        produits.put(produit.identifiant(), produit);
    }
}