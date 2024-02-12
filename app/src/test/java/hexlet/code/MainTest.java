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
import java.util.List;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MainTest {

    private static Javalin app;
    private static MockWebServer server;

    @BeforeAll
    public static void beforeAll() throws IOException {
        server = new MockWebServer();

        MockResponse mockedResponse1 = new MockResponse()
                .setBody(readFixtures("urlCheck1.html")).setResponseCode(200);
        MockResponse mockedResponse3 = new MockResponse().setResponseCode(404);

        server.enqueue(mockedResponse1);
        server.enqueue(mockedResponse3);
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

    private static String readFixtures(String fileName) throws IOException {
        Path filePath = Paths.get("src", "test", "resources", "fixtures", fileName)
                .toAbsolutePath().normalize();
        return Files.readString(filePath).trim();
    }

    @Test
    void testMainPage() {
        JavalinTest.test(app, ((serv, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body()).isNotNull();
            assertThat(response.body().string()).contains("Анализатор страниц</h1>");
        }));
    }

    @Test
    public void testAddUrlAndCheckDB() {
        JavalinTest.test(app, (serv, client) -> {
            var requestBody = "url=https://example.com:8080/asdf";
            final Integer indexOfBeginningAddress = 4;
            final Integer indexOfFirstSlash = 13;
            var expectedResponse = requestBody
                    .substring(indexOfBeginningAddress, requestBody.indexOf("/", indexOfFirstSlash));
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains(expectedResponse);
            assertThat(UrlRepository.exists(expectedResponse)).isTrue();
        });
    }

    @Test
    public void testCheckListOfUrls() {
        JavalinTest.test(app, (serv, client) -> {
            List<String> list = List.of("https://example1.com", "https://example2.com");
            for (String val : list) {
                client.post(NamedRoutes.urlsPath(), "urls=" + val);
            }

            List<Url> res = UrlRepository.getEntities();
            for (Url em : res) {
                assertThat(list.contains(em.getName())).isTrue();
            }
        });
    }

    @Test
    public void testUrlCheck() throws SQLException {
        String urlss = server.url("/").toString();
        var url = new Url(server.url("").toString(), Timestamp.from(ZonedDateTime.now().toInstant()));
        UrlRepository.save(url);

        JavalinTest.test(app, ((server1, client) -> {
            var response1 = client.post(NamedRoutes.urlChecksPath(url.getId()));
            assertThat(response1.code()).isEqualTo(200);

            var responseUrlDetail = client.get(NamedRoutes.urlPath(url.getId()));

            var responseBody = responseUrlDetail.body().string();
            assertThat(responseBody)
                    .contains("200")
                    .doesNotContain("404")
                    .contains("Анализатор страниц");

            var responseUrlList = client.get(NamedRoutes.urlsPath());
            assertThat(responseUrlList.code()).isEqualTo(200);

            var responseBodyList = responseUrlList.body().string();
            assertThat(responseBodyList)
                    .doesNotContain("404")
                    .contains("200");
        }));
    }

    @Test
    public void testAddBadUrl() {
        JavalinTest.test(app, (serv, client) -> {
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

        JavalinTest.test(app, (serv, client) -> {
            var response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            var responseBody = response.body().string();
            assertThat(responseBody).contains(url.getName());
        });
    }

    @Test
    void testUrlNotFound() {
        JavalinTest.test(app, (((serv, client) -> {
            var response = client.get(NamedRoutes.urlPath(99999L));
            assertThat(response.code()).isEqualTo(404);
        })));
    }
}
