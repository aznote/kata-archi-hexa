package org.example.domaine.panier.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;


import static org.junit.jupiter.api.Assertions.*;

class ArticlePanierTest {

    private Produit produit;
    @BeforeEach
    void setUp() {
        produit = new Produit("P01","Produit 1", BigDecimal.valueOf(10));
    }

    @Test
    void calculerSousTotal() {

       Promotion promotion = new Promotion(BigDecimal.valueOf(2));
       ArticlePanier articlePanier = new ArticlePanier(produit,1,promotion);

        assertThat(articlePanier.calculerSousTotal()).isEqualTo(BigDecimal.valueOf(8));
    }

    @Test
    void produit() {
    }

    @Test
    void quantite() {
    }
}