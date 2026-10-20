package org.example.domaine.panier.service;


import org.example.domaine.panier.model.Promotion;
import org.example.domaine.panier.port.out.PromotionPort;
import org.springframework.stereotype.Service;

@Service
public class PromotionService {

    private final PromotionPort promotionPort;

    public PromotionService(PromotionPort promotionPort) {
        this.promotionPort = promotionPort;
    }

    public Promotion recupererPromotion(String identifiantProduit){
        return promotionPort.recupererPromotion(identifiantProduit);
    }



}
