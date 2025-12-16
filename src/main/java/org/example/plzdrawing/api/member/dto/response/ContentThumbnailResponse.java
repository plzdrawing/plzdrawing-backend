package org.example.plzdrawing.api.member.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.example.plzdrawing.domain.content.enums.TimeTaken;

public record ContentThumbnailResponse(
        Long contentId,
        LocalDate createAt,
        List<String> contentUrl,
        List<String> hashTag,
        String explanation,
        TimeTaken timeTaken,
        Long price,
        Long like
) {}
