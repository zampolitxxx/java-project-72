package hexlet.code.controller;

import io.javalin.http.Context;
import hexlet.code.dto.BasePage;
import hexlet.code.util.FlashEnum;

import java.util.Collections;

public class RootController {
    public static void index(Context ctx) {
        var page = new BasePage();

        var flash = (String) ctx.consumeSessionAttribute("flash");
        var flashType = (String) ctx.consumeSessionAttribute("flashType");

        if (flash != null && flashType != null) {
            page.setFlash(flash);
            page.setFlashType(FlashEnum.valueOf(flashType));
        }
        ctx.render("index.jte", Collections.singletonMap("page", page));
    }

}
