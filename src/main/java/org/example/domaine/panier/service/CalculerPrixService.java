package org.example.domaine.panier.service;

import org.example.domaine.panier.model.ArticlePanier;
import org.example.domaine.panier.model.Panier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculerPrixService {
    public BigDecimal calculerTotal(Panier panier) {
        if (panier == null) {
            throw new IllegalArgumentException("Le panier est obligatoire");
        }

        return panier.getArticles()
                .stream()
                .map(ArticlePanier::calculerSousTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
