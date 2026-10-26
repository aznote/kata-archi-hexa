package org.example.domaine.panier.model;

import org.example.domaine.panier.service.CalculerPrixService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


import static org.junit.jupiter.api.Assertions.*;

class ArticlePanierTest {

    private Produit produit;
    private Produit produit100;
    private Produit produit120;
    @BeforeEach
    void setUp() {
        produit = new Produit("P01","Produit 1", BigDecimal.valueOf(10));
        produit100 = new Produit("P02","Produit 2", BigDecimal.valueOf(100));
        produit120 = new Produit("P03","Produit 3", BigDecimal.valueOf(120));
    }

//    @Test
//    void given-xxx-when-xxx-then-
//
//    xxxx() {
//        int idProduit = 1;
//        int quantite = 2;
//        PrixUnitaireProduitRepository prixUnitaireProduitRepository = new PrixUnitaireProduitRepositoryStub(10);
//        CalculerPrixServiceIn calculerPrixService = new CalculerPrixService(prixUnitaireProduitRepository);
//
//        int montantTotal = calculerPrixService.calculerTotal(idProduit, quantite);
//
//        assertEquals(20, montantTotal);
//    }


    @Test
    void calculerSousTotal() {

       Optional<Promotion> promotion = Optional.of(new Promotion(BigDecimal.valueOf(2), PromotionType.fix));
       ArticlePanier articlePanier = new ArticlePanier(produit,1,promotion);

        assertThat(articlePanier.calculerSousTotal()).isEqualTo(BigDecimal.valueOf(8));
    }

    @Test
    void calculerReductionFix() {
        // Given
        Optional<Promotion> promotion = Optional.of(new Promotion(BigDecimal.valueOf(15), PromotionType.fix));
        ArticlePanier articlePanier = new ArticlePanier(produit100,1,promotion);

        // When
        BigDecimal sousTotal = articlePanier.calculerSousTotal();

        // Then
        assertThat(sousTotal).isEqualTo(BigDecimal.valueOf(85));
    }

    @Test
    void calculerReductionEnPourcentage() {

        Optional<Promotion> promotion = Optional.of(new Promotion(BigDecimal.valueOf(20), PromotionType.percent));
        ArticlePanier articlePanier = new ArticlePanier(produit120,1,promotion);
        assertThat(articlePanier.calculerSousTotal()).isEqualTo(BigDecimal.valueOf(96));
    }

    @Test
    void produit() {
    }

    @Test
    void quantite() {
    }
}