package org.example.infrastucture.panier;


import org.example.domaine.panier.model.Panier;
import org.example.domaine.panier.port.out.PanierPort;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class PanierMemoireAdapter implements PanierPort {

    private final Map<String, Panier> paniers = new HashMap<>();

    @Override
    public Optional<Panier> rechercherParIdentifiant(String identifiant) {
        return Optional.ofNullable(paniers.get(identifiant));
    }

    @Override
    public void enregistrer(String identifiant, Panier panier) {
        if (identifiant == null || identifiant.isBlank()) {
            throw new IllegalArgumentException(
                    "L'identifiant du panier est obligatoire"
            );
        }

        if (panier == null) {
            throw new IllegalArgumentException("Le panier est obligatoire");
        }

        paniers.put(identifiant, panier);
    }

    @Override
    public void supprimer(String identifiant) {
        paniers.remove(identifiant);
    }
}