package org.example.plzdrawing.domain.member;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.plzdrawing.domain.BaseTimeEntity;
import org.example.plzdrawing.domain.memberTag.MemberTag;
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
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToMany(mappedBy = "member")
    private Set<MemberTag> memberTags;

    @Builder
    private Member(String username, String password, String nickname, Gender gender, LocalDate birth) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.birth = birth;
        this.status = Status.ACTIVE;
        this.memberTags = new HashSet<>();
    }

    public void addMemberTag(MemberTag memberTag) {
        this.memberTags.add(memberTag);
    }
}
