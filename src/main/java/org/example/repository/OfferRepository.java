package org.example.repository;

import org.example.models.Offer;
import org.example.models.enums.OfferStatus;

import java.util.List;

public interface OfferRepository extends CrudRepository<Offer> {

    List<Offer> findAll();
    List<Offer> findAllLastWithLimit(int n);
    Offer findOfferById(long id);
    void deleteById(long id);
    List<Offer> findByUserId(long id);
    void changeStatus(Long id, OfferStatus status);
    void update(Offer offer);
    List<Offer> findAllForAdmin();

}