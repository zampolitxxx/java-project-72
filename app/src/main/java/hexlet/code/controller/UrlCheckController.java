package hexlet.code.controller;

import com.fasterxml.jackson.databind.util.Named;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.FlashEnum;
import hexlet.code.util.NamedRoutes;
import hexlet.code.repository.UrlCheckRepository;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

public class UrlCheckController {
    private static final String ERROR_MESSAGE = "Некорректный адрес";
    private static final String SUCCESSFUL_MESSAGE = "Страница успешно добавлена";
    public static void create(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id).orElseThrow(NotFoundResponse::new);
        try {
            var response = Unirest.get(url.getName()).asString();
            var status = response.getStatus();
            var ts = Timestamp.from(ZonedDateTime.now().toInstant());
            //Заглушка для данных urlId, statusCode, h1, title, description, createdAt
            //Long urlId = 1L;
            int statusCode = 200;
            String h1 = "h1";
            String title = "title";
            String description = "desc";
            var urlCheck = new UrlCheck(id, statusCode, title, h1, description, ts);
            try {
                UrlCheckRepository.save(urlCheck);
            } catch (SQLException e) {
                throw new InternalServerErrorResponse();
            }

            ctx.sessionAttribute("flash", SUCCESSFUL_MESSAGE);
            ctx.sessionAttribute("flashType", FlashEnum.success.toString());
        } catch (UnirestException e) {
            ctx.sessionAttribute("flash", ERROR_MESSAGE);
            ctx.sessionAttribute("flashType", FlashEnum.danger.toString());
        }
        ctx.redirect(NamedRoutes.urlPath(url.getId()));
    }
}
