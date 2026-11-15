package org.example.service;

import org.example.dto.CreateOfferDto;
import org.example.models.*;
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
    public void save(CreateOfferDto createOfferDto) throws SQLException {
        User user = User.builder()
                .id(createOfferDto.getAuthorId())
                .build();

        Category category = Category.builder()
                .id(createOfferDto.getCategoryId())
                .build();

        FileInfo attachment = FileInfo.builder()
                .id(createOfferDto.getAttachmentId())
                .build();

        List<Subject> subjects = null;
        if (createOfferDto.getSubjectIds() != null && !createOfferDto.getSubjectIds().isEmpty()) {
            subjects = createOfferDto.getSubjectIds().stream()
                    .map(id -> Subject.builder().id(id).build())
                    .toList();
        }

        offerRepository.save(
                Offer.builder()
                        .title(createOfferDto.getTitle())
                        .description(createOfferDto.getDescription())
                        .price(createOfferDto.getPrice())
                        .createdAt(LocalDateTime.now())
                        .format(createOfferDto.getFormat())
                        .status(OfferStatus.ACTIVE)
                        .author(user)
                        .category(category)
                        .subjects(subjects)
                        .attachment(attachment)
                        .build()
        );
    }


}
