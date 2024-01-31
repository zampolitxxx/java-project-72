package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepositoty {
    private static final String FETCH_ALL = """
        SELECT urls.id as id,
        urls.name as name,
        urls.created_at as created_at,
        (SELECT url_checks.created_at
         FROM url_checks
         WHERE url_id = urls.id
         ORDER BY id
         DESC LIMIT 1) as last_check_date,
         (SELECT status_code
         FROM url_checks
         WHERE url_id = urls.id
         ORDER BY id
         DESC LIMIT 1) as last_check_status_code
         FROM urls;
            """;
    private static final  String CHECK_IF_EXISTS = "SELECT 1 FROM urls WHERE name = ?;";
    private static final String SAVE_ONE = "INSERT INTO urls (name, created_at) VALUES (?, ?);";
    private static final String FETCH_ONE_TEMPLATE = "SELECT id, name, created_at FROM urls WHERE id = ? LIMIT 1;";

    public static Boolean exists(String name) throws SQLException {
        try (var conn = dataSource.getConnection();
            var prepareStatement = conn.prepareStatement(CHECK_IF_EXISTS)) {
            prepareStatement.setString(1, name);
            prepareStatement.execute();
            var resultSet = prepareStatement.getResultSet();
            return resultSet.next();
        }
    }

    public static void save(Url url) throws SQLException {
        try (var conn = dataSource.getConnection();
            var prepareStatement = conn.prepareStatement(SAVE_ONE, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement.setString(1, url.getName());
            prepareStatement.setTimestamp(2, url.getCreatedAt());
            prepareStatement.executeUpdate();

            ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned id");
            }
        }
    }

    public static List<Url> getEntities() throws SQLException {
        try (var conn = dataSource.getConnection();
            var prepareStatement = conn.prepareStatement(FETCH_ALL)) {
            prepareStatement.execute();
            var resultSet = prepareStatement.getResultSet();
            List<Url> result = new ArrayList<>();

            while (resultSet.next()) {
                Url url = getUrlModelForEntities(resultSet);
                result.add(url);
            }
            return result;
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        try (var conn = dataSource.getConnection();
            var prepareStatement = conn.prepareStatement(FETCH_ONE_TEMPLATE)) {
            prepareStatement.setLong(1, id);
            prepareStatement.execute();

            ResultSet resultSet = prepareStatement.getResultSet();
            Url url = null;

            if (resultSet.next()) {
                url = getUrlModelFromResulSet(resultSet);
            }
            return Optional.ofNullable(url);
        }
    }

    private static Url getUrlModelForEntities(ResultSet resultSet) throws SQLException {
        var url = getUrlModelFromResulSet(resultSet);
        var lastCheckDate = resultSet.getTimestamp("last_check_date");
        if (!resultSet.wasNull()) {
            url.setLastCheckDate(lastCheckDate);
        }

        var status = resultSet.getInt("last_check_status_code");

        if (!resultSet.wasNull()) {
            url.setStatus(status);
        }
        return url;
    }

    private static Url getUrlModelFromResulSet(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("id");
        var name = resultSet.getString("name");
        var createdAt = resultSet.getTimestamp("created_at");
        Url url = new Url(name, createdAt);
        url.setId(id);
        return url;
    }
}
