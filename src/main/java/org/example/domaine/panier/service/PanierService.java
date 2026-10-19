package org.example.domaine.panier.service;

import org.example.domaine.panier.model.Panier;
import org.example.domaine.panier.model.Produit;
import org.example.domaine.panier.port.in.GestionPanierPort;
import org.example.domaine.panier.port.out.CatalogueProduitPort;
import org.example.domaine.panier.port.out.PanierPort;
import org.springframework.stereotype.Service;

@Service
public class PanierService implements GestionPanierPort {

    private final CatalogueProduitPort catalogueProduitsRepository;
    private final PanierPort panierRepository;

    public PanierService(
            CatalogueProduitPort catalogueProduitsRepository,
            PanierPort panierRepository
    ) {
        this.catalogueProduitsRepository = catalogueProduitsRepository;
        this.panierRepository = panierRepository;
    }

    @Override
    public void ajouterProduit(
            String identifiantPanier,
            String identifiantProduit,
            int quantite
    ) {
        Produit produit = catalogueProduitsRepository
                .rechercherParIdentifiant(identifiantProduit)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Produit introuvable : " + identifiantProduit
                ));

        Panier panier = panierRepository
                .rechercherParIdentifiant(identifiantPanier)
                .orElseGet(Panier::new);

        panier.ajouterProduit(produit, quantite);

        panierRepository.enregistrer(identifiantPanier, panier);
    }

    @Override
    public void retirerProduit(
            String identifiantPanier,
            String identifiantProduit,
            int quantite
    ) {
        Produit produit = catalogueProduitsRepository
                .rechercherParIdentifiant(identifiantProduit)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Produit introuvable : " + identifiantProduit
                ));

        Panier panier = panierRepository
                .rechercherParIdentifiant(identifiantPanier)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Panier introuvable : " + identifiantPanier
                ));

        panier.retirerProduit(produit, quantite);

        panierRepository.enregistrer(identifiantPanier, panier);
    }

    @Override
    public Panier consulterPanier(String identifiantPanier) {
        return panierRepository
                .rechercherParIdentifiant(identifiantPanier)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Panier introuvable : " + identifiantPanier
                ));
    }
}