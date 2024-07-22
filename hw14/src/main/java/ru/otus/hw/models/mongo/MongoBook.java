package ru.otus.hw.models.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString(exclude = {"genres"})
@RequiredArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class MongoBook {

    @Id
    private String id;

    private String title;

    private MongoAuthor author;

    private MongoGenre genres;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MongoBook mongoBook = (MongoBook) o;
        return Objects.equals(id, mongoBook.id) && Objects.equals(title, mongoBook.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
