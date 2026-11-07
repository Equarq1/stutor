package org.example.service;

import org.example.dto.OfferDto;
import org.example.models.Offer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface OfferService {
    List<Offer> getAllOffers();
    List<Offer> getLastOffersWithLimit(int n);
    Offer getOfferById(int id);
    void saveOffer(OfferDto offerDto) throws SQLException;
}
