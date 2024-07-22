package ru.otus.hw.models.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@ToString(exclude = {"book"})
@RequiredArgsConstructor
@AllArgsConstructor
@Document(collection = "comments")
public class MongoComment {

    @Id
    private String id;

    private String text;

    @DBRef(lazy = true)
    private MongoBook book;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MongoComment mongoComment = (MongoComment) o;
        return Objects.equals(id, mongoComment.id) && Objects.equals(text, mongoComment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }
}
