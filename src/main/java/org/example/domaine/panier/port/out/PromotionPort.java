package org.example.domaine.panier.port.out;

import org.example.domaine.panier.model.Promotion;

public interface PromotionPort {

    Promotion recupererPromotion(String identifiantProduit);

}
