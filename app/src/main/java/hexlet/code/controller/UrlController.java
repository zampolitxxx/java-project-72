package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.FlashEnum;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.UrlFormatter;
import hexlet.code.model.Url;
import hexlet.code.dto.urls.UrlsPage;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;
import java.util.List;

public class UrlController {
    private static final String ERROR_FLASH_MESSAGE = "Некорректный URL";
    private static final String URL_EXISTS_FLASH_MESSAGE = "Страница уже существует";
    private static final String URL_SAVED_FLASH_MESSAGE = "Страница успешно добавлена";

    public static void create(Context ctx) throws SQLException {
        var name = ctx.formParamAsClass("url", String.class)
                .check(Objects::nonNull, "Required field is empty")
                .getOrThrow(stringMap -> new BadRequestResponse());
        String formattedURL;
        try {
            formattedURL = UrlFormatter.formatURL(name);
        } catch (IllegalArgumentException | URISyntaxException e) {
            ctx.sessionAttribute("flash", ERROR_FLASH_MESSAGE);
            ctx.sessionAttribute("flashType", FlashEnum.DANGER.getType().toUpperCase());
            ctx.redirect(NamedRoutes.rootPath());
            return; //formattedURL not initialized
        }
        // Если сайт уже есть в БД
        if (UrlRepository.exists(formattedURL)) {
            ctx.sessionAttribute("flash", URL_EXISTS_FLASH_MESSAGE);
            ctx.sessionAttribute("flashType", FlashEnum.INFO.getType().toUpperCase());
            ctx.redirect(NamedRoutes.urlsPath());
            return;
        }

        var url = new Url(formattedURL);

        UrlRepository.save(url);
        ctx.sessionAttribute("flash", URL_SAVED_FLASH_MESSAGE);
        ctx.sessionAttribute("flashType", FlashEnum.SUCCESS.getType().toUpperCase());
        ctx.redirect(NamedRoutes.urlsPath());
    }

    public static void index(Context ctx) throws SQLException {
        List<Url> urls = UrlRepository.getEntities();

        UrlsPage page = new UrlsPage(urls);
        var flash = (String) ctx.consumeSessionAttribute("flash");
        var flashType = (String) ctx.consumeSessionAttribute("flashType");

        if (flash != null && flashType != null) {
            page.setFlash(flash);
            page.setFlashType(FlashEnum.valueOf(flashType));
        }
        ctx.render("url/index.jte", Collections.singletonMap("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlRepository.find(id).orElseThrow(NotFoundResponse::new);
        var urlChecks = UrlCheckRepository.filterByUrlId(url.getId());
        UrlPage page = new UrlPage(url, urlChecks);

        var flash = (String) ctx.consumeSessionAttribute("flash");
        var flashType = (String) ctx.consumeSessionAttribute("flashType");

        if (flash != null && flashType != null) {
            page.setFlash(flash);
            page.setFlashType(FlashEnum.valueOf(flashType));
        }
        ctx.render("url/show.jte", Collections.singletonMap("page", page));
    }
}
