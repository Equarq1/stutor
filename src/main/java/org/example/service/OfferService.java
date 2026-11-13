package org.example.service;

import org.example.dto.CreateOfferDto;
import org.example.models.Offer;

import java.sql.SQLException;
import java.util.List;

public interface OfferService {
    List<Offer> getAllOffers();
    List<Offer> getLastOffersWithLimit(int n);
    Offer getOfferById(int id);
    void save(CreateOfferDto createOfferDto) throws SQLException;
}
