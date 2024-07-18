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
@Document(collection = "genres")
public class MongoGenre {

    @Id
    private String id;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MongoGenre mongoGenre = (MongoGenre) o;
        return Objects.equals(id, mongoGenre.id) && Objects.equals(name, mongoGenre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
