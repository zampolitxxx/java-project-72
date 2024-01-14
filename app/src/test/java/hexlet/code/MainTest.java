package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.sql.SQLException;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

public class MainTest {

    Javalin app;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

//    @Test
//    public void testBuildSessionPage() {
//        JavalinTest.test(app, (server, client) -> {
//            var response = client.get("/sessions/build");
//            assertThat(response.code()).isEqualTo(200);
//        });
//    }
//
//    @Test
//    public void testUsersPage() {
//        JavalinTest.test(app, (server, client) -> {
//            var response = client.get("/users");
//            assertThat(response.code()).isEqualTo(200);
//        });
//    }
//
//    @Test
//    public void testCoursesPage() {
//        JavalinTest.test(app, (server, client) -> {
//            var response = client.get("/courses");
//            assertThat(response.code()).isEqualTo(200);
//        });
//    }
//
//    @Test
//    public void testBuildCourse() {
//        JavalinTest.test(app, (server, client) -> {
//            var response = client.get("/courses/build");
//            assertThat(response.code()).isEqualTo(200);
//        });
//    }
//
//    @Test
//    public void testCreateCourse() {
//        JavalinTest.test(app, (server, client) -> {
//            var requestBody = "name=coursename&desdcription=coursedescription";
//            var response = client.post("/courses", requestBody);
//            assertThat(response.code()).isEqualTo(200);
//            assertThat(response.body().string()).contains("coursename");
//        });
//    }
//
//    @Test
//    public void testPostsPage() {
//        JavalinTest.test(app, (server, client) -> {
//            var response = client.get("/posts");
//            assertThat(response.code()).isEqualTo(200);
//        });
//    }
//
//    @Test
//    public void testCarsPage() {
//        JavalinTest.test(app, (server, client) -> {
//            var response = client.get("/cars");
//            assertThat(response.code()).isEqualTo(200);
//        });
//    }
//
//    @Test
//    public void testBuildCarPage() {
//        JavalinTest.test(app, (server, client) -> {
//            var response = client.get("/cars/build");
//            assertThat(response.code()).isEqualTo(200);
//        });
//    }
//
//    @Test
//    public void testCarPage() throws SQLException {
//        var car = new Url("honda", "accord");
//        UrlRepository.save(car);
//        JavalinTest.test(app, (server, client) -> {
//            var response = client.get("/cars/" + car.getId());
//            assertThat(response.code()).isEqualTo(200);
//        });
//    }
//
//    @Test
//    void testCarNotFound() throws Exception {
//        JavalinTest.test(app, (server, client) -> {
//            var response = client.get("/cars/999999");
//            assertThat(response.code()).isEqualTo(404);
//        });
//    }
//
//    @Test
//    void testUserNotFound() throws Exception {
//        JavalinTest.test(app, (server, client) -> {
//            var response = client.get("/users/999999");
//            assertThat(response.code()).isEqualTo(404);
//        });
//    }
}
