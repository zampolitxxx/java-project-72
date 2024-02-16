package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.FlashEnum;
import hexlet.code.util.NamedRoutes;
import hexlet.code.repository.UrlCheckRepository;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.jsoup.Jsoup;


import java.sql.SQLException;

public class UrlCheckController {
    private static final String ERROR_MESSAGE = "Некорректный адрес";
    private static final String SUCCESSFUL_MESSAGE = "Страница успешно проверена";

    public static void create(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id).orElseThrow(NotFoundResponse::new);
        try {
            var response = Unirest.get(url.getName()).asString();
            var status = response.getStatus();
            var body = Jsoup.parse(response.getBody());
            var h1Element = body.selectFirst("h1");
            var descriptionElement = body.selectFirst("meta[name=\"description\"]");

            var h1 = h1Element != null ? h1Element.text() : "";
            var title = body.title();
            var description = descriptionElement != null ? descriptionElement.attr("content") : "";

            var urlCheck = new UrlCheck(id, status, title, h1, description);

            try {
                UrlCheckRepository.save(urlCheck);
            } catch (SQLException e) {
                throw new InternalServerErrorResponse();
            }

            ctx.sessionAttribute("flash", SUCCESSFUL_MESSAGE);
            ctx.sessionAttribute("flashType", FlashEnum.SUCCESS.getType().toUpperCase());
        } catch (UnirestException e) {
            System.out.println(e);
            ctx.sessionAttribute("flash", ERROR_MESSAGE);
            ctx.sessionAttribute("flashType", FlashEnum.DANGER.getType().toUpperCase());
        }
        ctx.redirect(NamedRoutes.urlPath(url.getId()));
    }
}
