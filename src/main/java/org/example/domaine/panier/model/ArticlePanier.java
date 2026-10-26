package org.example.domaine.panier.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public record ArticlePanier(
        Produit produit,
        int quantite,
        Optional<Promotion> promotion
) {

    public static final int CENT_POURCENT = 100;

    public ArticlePanier(Produit produit, int quantite) {
        this(produit, quantite,Optional.empty());
    }

    public BigDecimal calculerSousTotal() {
        BigDecimal sousTotal = produit.prixUnitaire()
                .multiply(BigDecimal.valueOf(quantite));
        if (promotion.isPresent() ){
            if(promotion.get().type().equals(PromotionType.percent)){
                return sousTotal
                        .multiply(BigDecimal.valueOf(CENT_POURCENT).subtract(promotion.get().valeur()))
                        .divide(BigDecimal.valueOf(CENT_POURCENT));
            }
            return produit.prixUnitaire().subtract(promotion.get().valeur())
                    .multiply(BigDecimal.valueOf(quantite));
        }
        return sousTotal;
    }
}