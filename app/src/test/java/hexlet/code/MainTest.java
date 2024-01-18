package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    private static Javalin app;
    private static MockWebServer server;

    @BeforeAll
    public static void beforeAll() throws IOException {
        server = new MockWebServer();

        MockResponse mockedResponse1 = new MockResponse()
                .setBody(readFixt("urlCheck1.html")).setResponseCode(200);

        server.enqueue(mockedResponse1);
        server.start();
    }

    @AfterAll
    public static void afterAll() throws IOException {
        server.shutdown();
    }

    @BeforeEach
    public void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    private static String readFixt(String fileName) throws IOException {
        Path filePath = Paths.get("src", "test", "resources", "fixtures", fileName)
                .toAbsolutePath().normalize();;
        return Files.readString(filePath).trim();
    }

    @Test
    void testMainPage() {
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body()).isNotNull();
            assertThat(response.body().string()).contains("Анализатор страниц</h1>");
        }));
    }

    @Test
    public void testUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://example.com";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://example.com");


            requestBody = "url=http://example.com:8080";
            response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("http://example.com:8080");

            requestBody = "url=http://example.com:8081/a";
            response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("http://example.com:8081");
        });
    }

    @Test
    public void testBadUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=badUrl.com";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);

            response = client.get(NamedRoutes.urlsPath());
            assertThat(response.body().string()).doesNotContain("badUrl.com");


            response = client.post(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(400);
        });
    }

    @Test
    public void testUrlPage() throws SQLException {
        var url = new Url("https://example.com", Timestamp.from(ZonedDateTime.now().toInstant()));
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            var responseBody = response.body().string();
            assertThat(responseBody).contains(url.getName());
        });
    }

    @Test
    void testUrlNotFound() {
        JavalinTest.test(app, (((server, client) -> {
            var response = client.get(NamedRoutes.urlPath(99999L));
            assertThat(response.code()).isEqualTo(404);
        })));
    }
}