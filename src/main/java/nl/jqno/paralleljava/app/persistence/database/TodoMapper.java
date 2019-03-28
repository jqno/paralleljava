package nl.jqno.paralleljava.app.persistence.database;

import nl.jqno.paralleljava.app.domain.Todo;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TodoMapper implements RowMapper<Todo> {
    public Todo map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Todo(
                UUID.fromString(rs.getString("id")),
                rs.getString("title"),
                "",
                rs.getBoolean("completed"),
                rs.getInt("index"));
    }
}
