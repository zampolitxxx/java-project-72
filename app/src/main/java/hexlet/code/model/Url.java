package hexlet.code.model;

import java.sql.Timestamp;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public final class Url {
    private Long id;

    @ToString.Include
    private String name;
    private Timestamp createdAt;

    public Url(String name) {
        this.name = name;
    }

    public Instant getCreatedAtToInstant() {
        return this.createdAt.toInstant();
    }
}
