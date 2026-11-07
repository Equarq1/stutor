package org.example.service;

import org.example.dto.OfferDto;
import org.example.models.Category;
import org.example.models.FileInfo;
import org.example.models.Offer;
import org.example.models.User;
import org.example.models.enums.OfferStatus;
import org.example.repository.OfferRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    public OfferServiceImpl(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }


    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public List<Offer> getLastOffersWithLimit(int n) {
        return offerRepository.findAllLastWithLimit(n);
    }

    @Override
    public Offer getOfferById(int id) {
        return offerRepository.findOfferById(id);
    }

    @Override
    public void saveOffer(OfferDto offerDto) throws SQLException {
        User user = User.builder()
                .id(offerDto.getAuthorId())
                .build();

        Category category = Category.builder()
                .id(offerDto.getCategoryId())
                .build();

        FileInfo attachment = FileInfo.builder()
                .id(offerDto.getAttachmentId())
                .build();

        offerRepository.save(
                Offer.builder()
                        .title(offerDto.getTitle())
                        .description(offerDto.getDescription())
                        .price(offerDto.getPrice())
                        .createdAt(LocalDateTime.now())
                        .format(offerDto.getFormat())
                        .status(OfferStatus.ACTIVE)
                        .author(user)
                        .category(category)
                        .attachment(attachment)
                        .build()
        );
    }


}
