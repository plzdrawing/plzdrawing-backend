package org.example.plzdrawing.domain.content;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.plzdrawing.domain.Status;
import org.example.plzdrawing.domain.Tag;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "content_tag",
        uniqueConstraints = @UniqueConstraint(name="uk_content_tag", columnNames={"content_id","tag_id"})
)
public class ContentTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_tag_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="content_id", nullable=false)
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tag_id", nullable=false)
    private Tag tag;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Status status = Status.ACTIVE;
    // 생성자에서는 "연결"하지 말고 값만 세팅
    public ContentTag(Content content, Tag tag) {
        this.content = content;
        this.tag = tag;
        this.status = Status.ACTIVE;
    }

    public void activate() { this.status = Status.ACTIVE; }
    public void deactivate() { this.status = Status.INACTIVE; }

    public void setContent(Content content) { this.content = content; }
    public void setTag(Tag tag) { this.tag = tag; }
}
