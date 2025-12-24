package org.example.plzdrawing.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.plzdrawing.domain.member.Member;
import org.hibernate.annotations.Where;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "status = 'ACTIVE'")
public class Review extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selling_member_id")
    private Member sellingMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buying_member_id")
    private Member buyingMember;

    @Column(nullable = false)
    private float totalRating;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    // TODO : 이미지 논의
    //    @OneToMany(mappedBy = "review")
    //    private List<ReviewImage> images = new ArrayList<>();

    @Builder
    public Review(Member sellingMember, Member buyingMember, float totalRating, Status status) {}

}
