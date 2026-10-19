package org.example.controller;

import org.example.controller.dto.AjouterProduitRequest;
import org.example.controller.dto.PanierResponse;
import org.example.domaine.panier.model.ArticlePanier;
import org.example.domaine.panier.model.Panier;
import org.example.domaine.panier.port.in.GestionPanierPort;
import org.example.domaine.panier.service.CalculerPrixService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paniers")
public class PanierController {

    private final GestionPanierPort gestionPanierPort;
    private final CalculerPrixService calculerPrixService;

    public PanierController(
            GestionPanierPort gestionPanierPort,
            CalculerPrixService calculerPrixService
    ) {
        this.gestionPanierPort = gestionPanierPort;
        this.calculerPrixService = calculerPrixService;
    }

    @PostMapping("/{identifiantPanier}/articles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ajouterProduit(
            @PathVariable String identifiantPanier,
            @RequestBody AjouterProduitRequest requete
    ) {
        gestionPanierPort.ajouterProduit(
                identifiantPanier,
                requete.identifiantProduit(),
                requete.quantite()
        );
    }

    @DeleteMapping("/{identifiantPanier}/articles/{identifiantProduit}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void retirerProduit(
            @PathVariable String identifiantPanier,
            @PathVariable String identifiantProduit,
            @RequestParam(defaultValue = "1") int quantite
    ) {
        gestionPanierPort.retirerProduit(
                identifiantPanier,
                identifiantProduit,
                quantite
        );
    }

    @GetMapping("/{identifiantPanier}")
    public PanierResponse consulterPanier(
            @PathVariable String identifiantPanier
    ) {
        Panier panier =
                gestionPanierPort.consulterPanier(identifiantPanier);

        List<PanierResponse.ArticleResponse> articles =
                panier.getArticles().stream()
                        .map(this::convertirArticle)
                        .toList();

        return new PanierResponse(
                identifiantPanier,
                articles,
                calculerPrixService.calculerTotal(panier)
        );
    }

    private PanierResponse.ArticleResponse convertirArticle(
            ArticlePanier article
    ) {
        return new PanierResponse.ArticleResponse(
                article.produit().identifiant(),
                article.produit().nom(),
                article.produit().prixUnitaire(),
                article.quantite(),
                article.calculerSousTotal()
        );
    }
}