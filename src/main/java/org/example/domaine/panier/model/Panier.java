package org.example.domaine.panier.model;

import java.util.ArrayList;
import java.util.List;

public class Panier {

    private final List<ArticlePanier> articles = new ArrayList<>();

    public void ajouterProduit(Produit produit, int quantite) {
        if (produit == null) {
            throw new IllegalArgumentException("Le produit est obligatoire");
        }

        if (quantite <= 0) {
            throw new IllegalArgumentException(
                    "La quantité doit être strictement positive"
            );
        }

        for (int i = 0; i < articles.size(); i++) {
            ArticlePanier article = articles.get(i);

            if (article.produit().equals(produit)) {
                articles.set(
                        i,
                        new ArticlePanier(produit, article.quantite() + quantite)
                );
                return;
            }
        }

        articles.add(new ArticlePanier(produit, quantite));
    }

    public void retirerProduit(Produit produit, int quantite) {
        if (produit == null) {
            throw new IllegalArgumentException("Le produit est obligatoire");
        }

        if (quantite <= 0) {
            throw new IllegalArgumentException(
                    "La quantité doit être strictement positive"
            );
        }

        for (int i = 0; i < articles.size(); i++) {
            ArticlePanier article = articles.get(i);

            if (article.produit().equals(produit)) {
                int nouvelleQuantite = article.quantite() - quantite;

                if (nouvelleQuantite <= 0) {
                    articles.remove(i);
                } else {
                    articles.set(
                            i,
                            new ArticlePanier(produit, nouvelleQuantite)
                    );
                }
                return;
            }
        }
    }

    public List<ArticlePanier> getArticles() {
        return List.copyOf(articles);
    }

    public boolean estVide() {
        return articles.isEmpty();
    }
}