package org.example.plzdrawing.api.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProfileRequest {

    @Schema(description = "닉네임", example = "홍길동")
    private String nickname;

    @Schema(description = "한 줄 소개", example = "그림 그리는 걸 좋아합니다.")
    private String introduction;

    @Schema(description = "해시태그", example = "#귀여운 #낙서 #동물그림")
    private String hashtags;

    @Schema(description = "프로필 이미지 URL", example = "https://bucket.s3.ap-northeast-2.amazonaws.com/profile123.png")
    private String profileImageUrl;
}
