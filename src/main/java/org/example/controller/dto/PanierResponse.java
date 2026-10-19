package org.example.controller.dto;

import java.math.BigDecimal;
import java.util.List;

public record PanierResponse(
        String identifiantPanier,
        List<ArticleResponse> articles,
        BigDecimal total
) {
    public record ArticleResponse(
            String identifiantProduit,
            String nom,
            BigDecimal prixUnitaire,
            int quantite,
            BigDecimal sousTotal
    ) {
    }
}