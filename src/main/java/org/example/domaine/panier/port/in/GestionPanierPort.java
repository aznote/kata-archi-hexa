package org.example.domaine.panier.port.in;

import org.example.domaine.panier.model.Panier;

public interface GestionPanierPort {

    void ajouterProduit(String identifiantPanier,
                        String identifiantProduit,
                        int quantite);

    void retirerProduit(String identifiantPanier,
                        String identifiantProduit,
                        int quantite);

    Panier consulterPanier(String identifiantPanier);
}