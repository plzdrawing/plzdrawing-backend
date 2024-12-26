package org.example.plzdrawing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "status = 'ACTIVE'")
public class Member extends BaseTimeEntity{
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
    private Status status;

    @OneToMany(mappedBy = "member")
    private List<MemberTag> memberTags = new ArrayList<>();

    public static Member createMember(String username, String password, String nickname) {
        Member member = new Member();
        member.username = username;
        member.password = password;
        member.nickname = nickname;

        member.status = Status.ACTIVE;

        return member;
    }

    public void addMemberTag(MemberTag memberTag) {
        this.memberTags.add(memberTag);
    }
}
