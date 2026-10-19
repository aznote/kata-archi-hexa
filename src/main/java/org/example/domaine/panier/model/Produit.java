package org.example.domaine.panier.model;

import java.math.BigDecimal;

public record Produit(
        String identifiant,
        String nom,
        BigDecimal prixUnitaire
) {
    public Produit {
        if (identifiant == null || identifiant.isBlank()) {
            throw new IllegalArgumentException(
                    "L'identifiant est obligatoire"
            );
        }

        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom est obligatoire"
            );
        }

        if (prixUnitaire == null || prixUnitaire.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Le prix est obligatoire et ne peut pas être négatif"
            );
        }
    }
}