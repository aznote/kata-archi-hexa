package org.example.infrastucture.panier;

import org.example.domaine.panier.model.Panier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PanierMemoireAdapterTest {

    private PanierMemoireAdapter stockage;

    @BeforeEach
    void setUp() {
        stockage = new PanierMemoireAdapter();
    }

    @Nested
    class RecherchePanier {

        @Test
        void devraitRetournerUnPanierEnregistre() {
            Panier panier = new Panier();

            stockage.enregistrer("PANIER-1", panier);

            Optional<Panier> resultat =
                    stockage.rechercherParIdentifiant("PANIER-1");

            assertThat(resultat).containsSame(panier);
        }

        @Test
        void devraitRetournerUnOptionalVideSiPanierAbsent() {
            Optional<Panier> resultat =
                    stockage.rechercherParIdentifiant("INCONNU");

            assertThat(resultat).isEmpty();
        }
    }

    @Nested
    class EnregistrementPanier {

        @Test
        void devraitEnregistrerPlusieursPaniersIndependamment() {
            Panier panier1 = new Panier();
            Panier panier2 = new Panier();

            stockage.enregistrer("PANIER-1", panier1);
            stockage.enregistrer("PANIER-2", panier2);

            assertThat(stockage.rechercherParIdentifiant("PANIER-1"))
                    .containsSame(panier1);

            assertThat(stockage.rechercherParIdentifiant("PANIER-2"))
                    .containsSame(panier2);
        }

        @Test
        void devraitRemplacerLePanierAvecLeMemeIdentifiant() {
            Panier ancien = new Panier();
            Panier nouveau = new Panier();

            stockage.enregistrer("PANIER-1", ancien);
            stockage.enregistrer("PANIER-1", nouveau);

            assertThat(stockage.rechercherParIdentifiant("PANIER-1"))
                    .containsSame(nouveau);
        }

        @Test
        void devraitRefuserUnIdentifiantNull() {
            assertThatThrownBy(
                    () -> stockage.enregistrer(null, new Panier())
            )
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("L'identifiant du panier est obligatoire");
        }

        @Test
        void devraitRefuserUnIdentifiantVide() {
            assertThatThrownBy(
                    () -> stockage.enregistrer(" ", new Panier())
            )
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("L'identifiant du panier est obligatoire");
        }

        @Test
        void devraitRefuserUnPanierNull() {
            assertThatThrownBy(
                    () -> stockage.enregistrer("PANIER-1", null)
            )
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Le panier est obligatoire");
        }
    }

    @Nested
    class SuppressionPanier {

        @Test
        void devraitSupprimerUnPanierExistant() {
            stockage.enregistrer("PANIER-1", new Panier());

            stockage.supprimer("PANIER-1");

            assertThat(stockage.rechercherParIdentifiant("PANIER-1"))
                    .isEmpty();
        }

        @Test
        void neDevraitPasEchouerSiLePanierEstAbsent() {
            stockage.supprimer("INCONNU");

            assertThat(stockage.rechercherParIdentifiant("INCONNU"))
                    .isEmpty();
        }
    }
}