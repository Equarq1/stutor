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
    public Offer getOfferById(Long id) {
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

    @Override
    public void deleteById(Long id) {
        offerRepository.deleteById(id);
    }

    @Override
    public List<Offer> getOffersByUserId(Long id) {
        return offerRepository.findByUserId(id);
    }

    @Override
    public void changeStatus(Long id, OfferStatus status) {
        offerRepository.changeStatus(id, status);
    }

    @Override
    public void update(Long offerId, CreateOfferDto dto) throws SQLException {
        Category category = Category.builder().id(dto.getCategoryId()).build();

        FileInfo attachment = null;
        if (dto.getAttachmentId() != null) {
            attachment = FileInfo.builder().id(dto.getAttachmentId()).build();
        }

        List<Subject> subjects = null;
        if (dto.getSubjectIds() != null && !dto.getSubjectIds().isEmpty()) {
            subjects = dto.getSubjectIds().stream()
                    .map(id -> Subject.builder().id(id).build())
                    .toList();
        }

        Offer offer = Offer.builder()
                .id(offerId)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .format(dto.getFormat())
                .category(category)
                .attachment(attachment)
                .subjects(subjects)
                .build();

        offerRepository.update(offer);
    }
}
