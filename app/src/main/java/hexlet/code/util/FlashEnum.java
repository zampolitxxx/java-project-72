package hexlet.code.util;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FlashEnum {
    SUCCESS("success"),
    DANGER("danger"),
    INFO("info"),
    LIGHT("light"),
    DARK("dark");

    private final String type;
}
