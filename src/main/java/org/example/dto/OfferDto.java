package org.example.dto;

import lombok.Builder;
import lombok.Data;
import org.example.models.enums.OfferFormat;
import org.example.models.enums.OfferStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OfferDto {
    private String title;
    private String description;
    private BigDecimal price;
    private OfferFormat format;
    private Long authorId;
    private Integer categoryId;
    private Long attachmentId;
    private List<Integer> subjectIds;
}
