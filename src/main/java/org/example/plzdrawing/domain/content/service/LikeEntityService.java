package org.example.plzdrawing.domain.content.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.domain.LikeEntityRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeEntityService {
    private final LikeEntityRepository likeEntityRepository;

    public Map<Long, Long> countLikesByContentIds(List<Long> contentIds) {
        if (contentIds.isEmpty()) return Map.of();

        return likeEntityRepository.countLikesByContentIds(contentIds).stream()
                .collect(Collectors.toMap(
                        LikeEntityRepository.LikeCountProjection::getContentId,
                        LikeEntityRepository.LikeCountProjection::getLikeCount
                ));
    }
}
