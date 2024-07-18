package ru.otus.hw.models.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "authors")
public class MongoAuthor {

    @Id
    private String id;

    private String fullName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MongoAuthor mongoAuthor = (MongoAuthor) o;
        return Objects.equals(id, mongoAuthor.id) && Objects.equals(fullName, mongoAuthor.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName);
    }
}
