package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;

public class App {

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    private static String getDatabaseUrl() {
        return System.getenv()
                .getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }

    public static void main(String[] args) {
        var app = getApp();
        app.start(getPort());
    }

    public static Javalin getApp() {

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());
        var dataSource = new HikariDataSource(hikariConfig);

        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });
        app.get("/", ctx -> {
            ctx.result("Hello World");
        });
        return app;
    }
}
