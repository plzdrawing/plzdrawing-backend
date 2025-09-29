package org.example.plzdrawing.domain.member;

import static org.example.plzdrawing.domain.Role.ROLE_MEMBER;
import static org.example.plzdrawing.domain.Role.ROLE_TEMP;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.plzdrawing.api.auth.dto.request.SignUpRequest;
import org.example.plzdrawing.domain.BaseTimeEntity;
import org.example.plzdrawing.domain.MemberTag;
import org.example.plzdrawing.domain.Role;
import org.example.plzdrawing.domain.Status;
import org.hibernate.annotations.Where;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "status = 'ACTIVE'")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(name = "nickname")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToMany(mappedBy = "member")
    private Set<MemberTag> memberTags;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "personal_info_consent")
    private Boolean personalInfoConsent = false;

    @Schema(description = "이용정책 동의")
    private Boolean acceptTermsOfUse = false;

    @Schema(description = "할인, 이벤트 소식 받기 동의")
    private Boolean marketingConsent = false;

    @Builder
    private Member(String email, String password, Provider provider, String nickname, Role role) {
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.nickname = nickname;
        this.status = Status.ACTIVE;
        this.memberTags = new HashSet<>();
        this.role = role;
    }

    public static Member createTempMember(String nickName, String email, Provider provider) {
        return Member.builder()
                .nickname(nickName)
                .email(email)
                .provider(provider)
                .role(ROLE_TEMP)
                .build();
    }

    public void addMemberTag(MemberTag memberTag) {
        this.memberTags.add(memberTag);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateUser(String email, String provider, String nickName) {
        this.email = email;
        if(Objects.equals(provider, "kakao")) {
            this.provider = Provider.KAKAO;
        }
        else {
            this.provider = Provider.EMAIL;
        }
        this.nickname = nickname;
    }

    public void onboarding(
            SignUpRequest signUpRequest
    ) {
        this.nickname = signUpRequest.getNickName();
        this.memberTags = new HashSet<>();
        this.role = ROLE_MEMBER;
        this.personalInfoConsent = signUpRequest.getPersonalInfoConsent();
        this.acceptTermsOfUse = signUpRequest.getAcceptTermsOfUse();
        this.marketingConsent = signUpRequest.getMarketingConsent();
    }
}