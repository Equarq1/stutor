package org.example.repository;

import org.example.models.Offer;
import java.util.List;

public interface OfferRepository extends CrudRepository<Offer> {

    List<Offer> findAll();
    List<Offer> findAllLastWithLimit(int n);
    Offer findOfferById(long id);

}