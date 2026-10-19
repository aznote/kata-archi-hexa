package org.example.infrastucture.panier;

import org.example.domaine.panier.model.Produit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CatalogueProduitMemoireAdapterTest {

    private CatalogueProduitMemoireAdapter catalogue;

    @BeforeEach
    void setUp() {
        catalogue = new CatalogueProduitMemoireAdapter();
    }

    @Nested
    class RechercheProduit {

        @Test
        void devraitRetrouverUnProduitExistant() {
            Produit produit = new Produit(
                    "P001", "Clavier", new BigDecimal("49.90")
            );
            catalogue.ajouterProduitCatalogue(produit);

            Optional<Produit> resultat =
                    catalogue.rechercherParIdentifiant("P001");

            assertThat(resultat).contains(produit);
        }

        @Test
        void devraitRetournerUnOptionalVideSiProduitAbsent() {
            Optional<Produit> resultat =
                    catalogue.rechercherParIdentifiant("INCONNU");

            assertThat(resultat).isEmpty();
        }

        @Test
        void devraitRetournerLeBonProduitParmiPlusieurs() {
            Produit clavier = new Produit(
                    "P001", "Clavier", new BigDecimal("49.90")
            );
            Produit souris = new Produit(
                    "P002", "Souris", new BigDecimal("19.90")
            );

            catalogue.ajouterProduitCatalogue(clavier);
            catalogue.ajouterProduitCatalogue(souris);

            assertThat(catalogue.rechercherParIdentifiant("P002"))
                    .contains(souris);
        }
    }

    @Nested
    class AjoutProduitCatalogue {

        @Test
        void devraitAjouterUnProduitAuCatalogue() {
            Produit produit = new Produit(
                    "P001", "Clavier", new BigDecimal("49.90")
            );

            catalogue.ajouterProduitCatalogue(produit);

            assertThat(catalogue.rechercherParIdentifiant("P001"))
                    .contains(produit);
        }

        @Test
        void devraitRefuserUnProduitNull() {
            assertThatThrownBy(
                    () -> catalogue.ajouterProduitCatalogue(null)
            )
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Le produit est obligatoire");
        }

        @Test
        void devraitRemplacerUnProduitAvecLeMemeIdentifiant() {
            Produit ancien = new Produit(
                    "P001", "Clavier", new BigDecimal("49.90")
            );
            Produit nouveau = new Produit(
                    "P001", "Clavier amélioré", new BigDecimal("59.90")
            );

            catalogue.ajouterProduitCatalogue(ancien);
            catalogue.ajouterProduitCatalogue(nouveau);

            assertThat(catalogue.rechercherParIdentifiant("P001"))
                    .contains(nouveau);
        }
    }
}