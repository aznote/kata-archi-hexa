package org.example.domaine.panier.model;

import java.math.BigDecimal;

public record ArticlePanier(
        Produit produit,
        int quantite
) {
    public BigDecimal calculerSousTotal() {
        return produit.prixUnitaire()
                .multiply(BigDecimal.valueOf(quantite));
    }
}