package org.example.plzdrawing.api.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "마이페이지 사용자 정보 응답")
public class ProfileInfoResponse {

    @Schema(description = "닉네임", example = "똥강아지")
    private String nickname;

    @Schema(description = "한 줄 소개", example = "그림 그리는 걸 좋아합니다.")
    private String introduction;

    @Schema(description = "해시태그 목록", example = "[\"귀여운\", \"낙서\", \"동물그림\"]")
    private List<String> hashtags;

    @Schema(description = "프로필 이미지 URL", example = "https://plzdrawing.s3.amazonaws.com/profile/abcd1234.png")
    private String profileImageUrl;
}
