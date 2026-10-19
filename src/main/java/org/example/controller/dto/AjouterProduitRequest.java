package org.example.controller.dto;

public record AjouterProduitRequest(
        String identifiantPanier,
        String identifiantProduit,
        int quantite
) {
}