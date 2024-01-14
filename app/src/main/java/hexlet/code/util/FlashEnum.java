package hexlet.code.util;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FlashEnum {
    success("success"),
    danger("danger"),
    info("info"),
    light("light"),
    dark("dark");

    private final String type;

    @Override
    public String toString() {
        return type;
    }
}
