package org.example.domaine.panier.service;

import org.example.domaine.panier.model.ArticlePanier;
import org.example.domaine.panier.model.Panier;
import org.example.domaine.panier.model.Produit;
import org.example.domaine.panier.port.out.CatalogueProduitPort;
import org.example.domaine.panier.port.out.PanierPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PanierServiceTest {

    @Mock
    private CatalogueProduitPort catalogueProduitsRepository;

    @Mock
    private PanierPort panierRepository;

    private PanierService panierService;

    private Produit clavier;

    @BeforeEach
    void setUp() {
        panierService = new PanierService(
                catalogueProduitsRepository,
                panierRepository
        );

        clavier = new Produit(
                "P001",
                "Clavier",
                new BigDecimal("49.99")
        );
    }

    @Nested
    class AjoutProduit {

        @Test
        void devraitAjouterUnProduitDansUnNouveauPanier() {
            when(catalogueProduitsRepository.rechercherParIdentifiant("P001"))
                    .thenReturn(Optional.of(clavier));

            when(panierRepository.rechercherParIdentifiant("PANIER-1"))
                    .thenReturn(Optional.empty());

            panierService.ajouterProduit("PANIER-1", "P001", 2);

            verify(panierRepository).enregistrer(
                    eq("PANIER-1"),
                    argThat(panier ->
                            panier.getArticles().size() == 1
                                    && panier.getArticles().get(0).produit().equals(clavier)
                                    && panier.getArticles().get(0).quantite() == 2
                    )
            );
        }

        @Test
        void devraitAjouterUnProduitDansUnPanierExistant() {
            Panier panier = new Panier();
            panier.ajouterProduit(clavier, 1);

            when(catalogueProduitsRepository.rechercherParIdentifiant("P001"))
                    .thenReturn(Optional.of(clavier));

            when(panierRepository.rechercherParIdentifiant("PANIER-1"))
                    .thenReturn(Optional.of(panier));

            panierService.ajouterProduit("PANIER-1", "P001", 2);

            assertThat(panier.getArticles())
                    .containsExactly(
                            new ArticlePanier(clavier, 3)
                    );

            verify(panierRepository).enregistrer("PANIER-1", panier);
        }

        @Test
        void devraitRefuserUnProduitIntrouvable() {
            when(catalogueProduitsRepository.rechercherParIdentifiant("INCONNU"))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() ->
                    panierService.ajouterProduit("PANIER-1", "INCONNU", 1)
            )
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Produit introuvable : INCONNU");

            verify(panierRepository, never())
                    .enregistrer(anyString(), any(Panier.class));
        }
    }

    @Nested
    class RetraitProduit {

        @Test
        void devraitRetirerUnProduitDuPanier() {
            Panier panier = new Panier();
            panier.ajouterProduit(clavier, 3);

            when(catalogueProduitsRepository.rechercherParIdentifiant("P001"))
                    .thenReturn(Optional.of(clavier));

            when(panierRepository.rechercherParIdentifiant("PANIER-1"))
                    .thenReturn(Optional.of(panier));

            panierService.retirerProduit("PANIER-1", "P001", 1);

            assertThat(panier.getArticles())
                    .containsExactly(
                            new ArticlePanier(clavier, 2)
                    );

            verify(panierRepository).enregistrer("PANIER-1", panier);
        }

        @Test
        void devraitRefuserLeRetraitSiLeProduitEstIntrouvable() {
            when(catalogueProduitsRepository.rechercherParIdentifiant("INCONNU"))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() ->
                    panierService.retirerProduit("PANIER-1", "INCONNU", 1)
            )
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Produit introuvable : INCONNU");

            verify(panierRepository, never())
                    .enregistrer(anyString(), any(Panier.class));
        }

        @Test
        void devraitRefuserLeRetraitSiLePanierEstIntrouvable() {
            when(catalogueProduitsRepository.rechercherParIdentifiant("P001"))
                    .thenReturn(Optional.of(clavier));

            when(panierRepository.rechercherParIdentifiant("PANIER-1"))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() ->
                    panierService.retirerProduit("PANIER-1", "P001", 1)
            )
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Panier introuvable : PANIER-1");

            verify(panierRepository, never())
                    .enregistrer(anyString(), any(Panier.class));
        }
    }
}