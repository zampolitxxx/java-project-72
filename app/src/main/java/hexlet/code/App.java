package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;

public class App {

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    private static String getDatabaseUrl() {
        return System.getenv()
                .getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }

    public static void main(String[] args) {
        var app = getApp();
        app.start(getPort());
    }

    public static Javalin getApp() {
        JavalinJte.init(createTemplateEngine());

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());
        var dataSource = new HikariDataSource(hikariConfig);

        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });
        app.get("/", ctx -> {
            ctx.render("page.jte");
        });
        return app;
    }
}
