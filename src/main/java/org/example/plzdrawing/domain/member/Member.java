package org.example.plzdrawing.domain.member;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.plzdrawing.domain.BaseTimeEntity;
import org.example.plzdrawing.domain.MemberTag;
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

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToMany(mappedBy = "member")
    private Set<MemberTag> memberTags;

    @Builder
    private Member(String email, String password, Provider provider, String nickname) {
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.nickname = nickname;
        this.status = Status.ACTIVE;
        this.memberTags = new HashSet<>();
    }

    public void addMemberTag(MemberTag memberTag) {
        this.memberTags.add(memberTag);
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}