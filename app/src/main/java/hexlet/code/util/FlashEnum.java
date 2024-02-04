package hexlet.code.util;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FlashEnum {
    success("SUCCESS"),
    danger("DANGER"),
    info("INFO"),
    light("LIGHT"),
    dark("DARK");

    private final String type;

    @Override
    public String toString() {
        return type;
    }
}
