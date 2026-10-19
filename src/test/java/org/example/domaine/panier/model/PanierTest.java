package org.example.domaine.panier.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PanierTest {

    private Panier panier;
    private Produit clavier;
    private Produit souris;

    @BeforeEach
    void setUp() {
        panier = new Panier();

        clavier = new Produit(
                "P001", "Clavier", new BigDecimal("49.99")
        );

        souris = new Produit(
                "P002", "Souris", new BigDecimal("19.99")
        );
    }

    @Nested
    class AjoutProduit {

        @Test
        void devraitAjouterUnProduitAuPanier() {
            panier.ajouterProduit(clavier, 1);

            assertThat(panier.getArticles())
                    .containsExactly(new ArticlePanier(clavier, 1));
        }

        @Test
        void devraitCumulerLesQuantitesDuMemeProduit() {
            panier.ajouterProduit(clavier, 2);
            panier.ajouterProduit(clavier, 3);

            assertThat(panier.getArticles())
                    .containsExactly(new ArticlePanier(clavier, 5));
        }

        @Test
        void devraitAjouterPlusieursProduitsDifferents() {
            panier.ajouterProduit(clavier, 2);
            panier.ajouterProduit(souris, 1);

            assertThat(panier.getArticles())
                    .containsExactly(
                            new ArticlePanier(clavier, 2),
                            new ArticlePanier(souris, 1)
                    );
        }

        @Test
        void devraitRefuserUnProduitNull() {
            assertThatThrownBy(() -> panier.ajouterProduit(null, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Le produit est obligatoire");
        }

        @Test
        void devraitRefuserUneQuantiteNulle() {
            assertThatThrownBy(() -> panier.ajouterProduit(clavier, 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("La quantité doit être strictement positive");
        }

        @Test
        void devraitRefuserUneQuantiteNegative() {
            assertThatThrownBy(() -> panier.ajouterProduit(clavier, -1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("La quantité doit être strictement positive");
        }
    }

    @Nested
    class RetraitProduit {

        @BeforeEach
        void initialiserPanier() {
            panier.ajouterProduit(clavier, 5);
        }

        @Test
        void devraitDiminuerLaQuantite() {
            panier.retirerProduit(clavier, 2);

            assertThat(panier.getArticles())
                    .containsExactly(new ArticlePanier(clavier, 3));
        }

        @Test
        void devraitSupprimerUnArticleLorsqueSaQuantiteAtteintZero() {
            panier.retirerProduit(clavier, 5);

            assertThat(panier.getArticles()).isEmpty();
        }

        @Test
        void devraitSupprimerUnArticleSiLaQuantiteRetireeEstSuperieure() {
            panier.retirerProduit(clavier, 6);

            assertThat(panier.getArticles()).isEmpty();
        }

        @Test
        void neDevraitRienFaireSiLeProduitEstAbsent() {
            panier.retirerProduit(souris, 1);

            assertThat(panier.getArticles())
                    .containsExactly(new ArticlePanier(clavier, 5));
        }

        @Test
        void devraitRefuserUnProduitNull() {
            assertThatThrownBy(() -> panier.retirerProduit(null, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Le produit est obligatoire");
        }

        @Test
        void devraitRefuserUneQuantiteNulle() {
            assertThatThrownBy(() -> panier.retirerProduit(clavier, 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("La quantité doit être strictement positive");
        }

        @Test
        void devraitRefuserUneQuantiteNegative() {
            assertThatThrownBy(() -> panier.retirerProduit(clavier, -1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("La quantité doit être strictement positive");
        }
    }

    @Nested
    class ConsultationPanier {

        @Test
        void devraitEtreVideALaCreation() {
            assertThat(panier.estVide()).isTrue();
            assertThat(panier.getArticles()).isEmpty();
        }

        @Test
        void devraitNePlusEtreVideApresUnAjout() {
            panier.ajouterProduit(clavier, 1);

            assertThat(panier.estVide()).isFalse();
        }

        @Test
        void neDevraitPasPermettreDeModifierLesArticlesDepuisLexterieur() {
            panier.ajouterProduit(clavier, 1);

            assertThatThrownBy(() -> panier.getArticles().clear())
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }
}