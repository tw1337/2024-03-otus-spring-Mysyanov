package ru.otus.hw.models.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class JpaComment {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,
            generator="comments_seq")
    @SequenceGenerator(name="comments_seq",
            sequenceName="SEQ_COMMENTS", allocationSize=10)
    private long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private JpaBook book;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JpaComment jpaComment = (JpaComment) o;
        return id == jpaComment.id && Objects.equals(text, jpaComment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }
}
