package hexlet.code.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public final class Url {
    private Long id;
    private String name;
    private Timestamp createdAt;
    private Timestamp lastCheckDate;
    private int status;

    public Url(String name) {
        this.name = name;
    }
}
