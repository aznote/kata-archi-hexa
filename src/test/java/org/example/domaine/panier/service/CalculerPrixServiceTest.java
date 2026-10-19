package org.example.domaine.panier.service;

import org.example.domaine.panier.model.Panier;
import org.example.domaine.panier.model.Produit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalculerPrixServiceTest {

    private CalculerPrixService calculerPrixService;
    private Panier panier;

    private Produit clavier;
    private Produit souris;

    @BeforeEach
    void setUp() {
        calculerPrixService = new CalculerPrixService();
        panier = new Panier();

        clavier = new Produit(
                "P001",
                "Clavier",
                new BigDecimal("49.99")
        );

        souris = new Produit(
                "P002",
                "Souris",
                new BigDecimal("19.99")
        );
    }

    @Nested
    class CalculDuPrixTotal {

        @Test
        void devraitRetournerZeroPourUnPanierVide() {
            assertThat(calculerPrixService.calculerTotal(panier))
                    .isEqualByComparingTo("0");
        }

        @Test
        void devraitCalculerLePrixDUnProduit() {
            panier.ajouterProduit(clavier, 1);

            assertThat(calculerPrixService.calculerTotal(panier))
                    .isEqualByComparingTo("49.99");
        }

        @Test
        void devraitMultiplierLePrixUnitaireParLaQuantite() {
            panier.ajouterProduit(clavier, 3);

            assertThat(calculerPrixService.calculerTotal(panier))
                    .isEqualByComparingTo("149.97");
        }

        @Test
        void devraitAdditionnerLesPrixDePlusieursProduits() {
            panier.ajouterProduit(clavier, 2);
            panier.ajouterProduit(souris, 1);

            assertThat(calculerPrixService.calculerTotal(panier))
                    .isEqualByComparingTo("119.97");
        }

        @Test
        void devraitCalculerPrecisementLesMontantsDecimaux() {
            Produit stylo = new Produit(
                    "P003",
                    "Stylo",
                    new BigDecimal("0.10")
            );

            panier.ajouterProduit(stylo, 3);

            assertThat(calculerPrixService.calculerTotal(panier))
                    .isEqualByComparingTo("0.30");
        }
    }

    @Nested
    class Validation {

        @Test
        void devraitRefuserUnPanierNull() {
            assertThatThrownBy(
                    () -> calculerPrixService.calculerTotal(null)
            )
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Le panier est obligatoire");
        }
    }
}