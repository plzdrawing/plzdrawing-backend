package org.example.plzdrawing.domain.content.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.domain.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public record ReviewStats(long reviewCount, float star) {}

    public Map<Long, ReviewStats> findSellingMemberStats(List<Long> memberIds) {
        if (memberIds.isEmpty()) return Map.of();

        return reviewRepository.findStatsBySellingMemberIds(memberIds).stream()
                .collect(Collectors.toMap(
                        ReviewRepository.ReviewStatsProjection::getSellingMemberId,
                        p -> new ReviewStats(
                                p.getReviewCount(),
                                p.getAvgRating() == null ? 0f : p.getAvgRating()
                        )
                ));
    }
}
