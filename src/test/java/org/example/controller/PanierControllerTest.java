package org.example.controller;

import org.example.domaine.panier.model.Panier;
import org.example.domaine.panier.model.Produit;
import org.example.domaine.panier.port.in.GestionPanierPort;
import org.example.domaine.panier.service.CalculerPrixService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PanierControllerTest {

    @Mock
    private GestionPanierPort gestionPanierPort;

    @Mock
    private CalculerPrixService calculerPrixService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        PanierController controller = new PanierController(
                gestionPanierPort,
                calculerPrixService
        );

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Nested
    class AjouterProduit {

        @Test
        void devraitAjouterUnProduit() throws Exception {
            mockMvc.perform(post("/api/paniers/PANIER-1/articles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "identifiantProduit": "P001",
                                      "quantite": 2
                                    }
                                    """))
                    .andExpect(status().isNoContent());

            verify(gestionPanierPort).ajouterProduit(
                    "PANIER-1", "P001", 2
            );
        }
    }

    @Nested
    class RetirerProduit {

        @Test
        void devraitRetirerUneQuantiteParDefautEgaleAUn()
                throws Exception {

            mockMvc.perform(delete(
                            "/api/paniers/PANIER-1/articles/P001"))
                    .andExpect(status().isNoContent());

            verify(gestionPanierPort).retirerProduit(
                    "PANIER-1", "P001", 1
            );
        }

        @Test
        void devraitRetirerLaQuantiteDemandee() throws Exception {
            mockMvc.perform(delete(
                            "/api/paniers/PANIER-1/articles/P001")
                            .param("quantite", "3"))
                    .andExpect(status().isNoContent());

            verify(gestionPanierPort).retirerProduit(
                    "PANIER-1", "P001", 3
            );
        }
    }

    @Nested
    class ConsulterPanier {

        @Test
        void devraitRetournerLePanierEtSonTotal() throws Exception {
            Produit produit = new Produit(
                    "P001",
                    "Clavier",
                    new BigDecimal("10.00")
            );

            Panier panier = new Panier();
            panier.ajouterProduit(produit, 2);

            when(gestionPanierPort.consulterPanier("PANIER-1"))
                    .thenReturn(panier);

            when(calculerPrixService.calculerTotal(panier))
                    .thenReturn(new BigDecimal("20.00"));

            mockMvc.perform(get("/api/paniers/PANIER-1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(
                            MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.identifiantPanier")
                            .value("PANIER-1"))
                    .andExpect(jsonPath("$.articles.length()")
                            .value(1))
                    .andExpect(jsonPath("$.articles[0].identifiantProduit")
                            .value("P001"))
                    .andExpect(jsonPath("$.articles[0].nom")
                            .value("Clavier"))
                    .andExpect(jsonPath("$.articles[0].quantite")
                            .value(2))
                    .andExpect(jsonPath("$.articles[0].sousTotal")
                            .value(20.00))
                    .andExpect(jsonPath("$.total").value(20.00));

            verify(gestionPanierPort)
                    .consulterPanier("PANIER-1");

            verify(calculerPrixService)
                    .calculerTotal(panier);
        }

        @Test
        void devraitRetournerUnPanierVideAvecUnTotalNul()
                throws Exception {

            Panier panier = new Panier();

            when(gestionPanierPort.consulterPanier("PANIER-VIDE"))
                    .thenReturn(panier);

            when(calculerPrixService.calculerTotal(panier))
                    .thenReturn(BigDecimal.ZERO);

            mockMvc.perform(get("/api/paniers/PANIER-VIDE"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.articles.length()")
                            .value(0))
                    .andExpect(jsonPath("$.total").value(0));
        }
    }
}