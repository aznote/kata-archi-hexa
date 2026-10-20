package org.example.domaine.panier.model;

import java.math.BigDecimal;

public record ArticlePanier(
        Produit produit,
        int quantite,
        Promotion promotion
) {

    public ArticlePanier(Produit produit, int quantite) {
        this(produit, quantite, null);
    }

    public BigDecimal calculerSousTotal() {
        if (promotion != null ){
            return produit.prixUnitaire().subtract(promotion.valeur())
                    .multiply(BigDecimal.valueOf(quantite));
        }
        return produit.prixUnitaire()
                .multiply(BigDecimal.valueOf(quantite));
    }
}