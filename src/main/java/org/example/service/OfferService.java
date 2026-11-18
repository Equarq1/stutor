package org.example.service;

import org.example.dto.CreateOfferDto;
import org.example.models.Offer;
import org.example.models.User;
import org.example.models.enums.OfferStatus;

import java.sql.SQLException;
import java.util.List;

public interface OfferService {
    List<Offer> getAllOffers();
    List<Offer> getLastOffersWithLimit(int n);
    Offer getOfferById(Long id);
    void save(CreateOfferDto createOfferDto) throws SQLException;
    void deleteById(Long id);
    List<Offer> getOffersByUserId(Long id);
    void changeStatus(Long id, OfferStatus status);
    void update(Long offerId, CreateOfferDto dto) throws SQLException;
}
