package org.example.models;

import lombok.Builder;
import lombok.Data;
import org.example.models.enums.OfferFormat;
import org.example.models.enums.OfferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Offer {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private OfferFormat format;
    private OfferStatus status;
    private User author;
    private Category category;
    private FileInfo attachment;
    private List<Subject> subjects;
}
