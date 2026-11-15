package studio.yukilem.yukilem.libs.nixlib.database;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {
    public T map(ResultSet resultSet1) throws SQLException;
}

