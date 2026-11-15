package studio.yukilem.yukilem.libs.nixlib.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class QueryBuilder {
    private final StringBuilder sql = new StringBuilder();
    private final List<Object> params = new ArrayList<Object>();
    private boolean setStarted;

    private QueryBuilder() {
    }

    public static QueryBuilder select(String ... stringArray) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.sql.append("SELECT ");
        if (stringArray.length == 0) {
            queryBuilder.sql.append("*");
        } else {
            queryBuilder.sql.append(String.join((CharSequence)", ", stringArray));
        }
        return queryBuilder;
    }

    public static QueryBuilder insertInto(String string, String ... stringArray) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.sql.append("INSERT INTO ").append(string).append(" (").append(String.join((CharSequence)", ", stringArray)).append(") VALUES (");
        for (int n = 0; n < stringArray.length; ++n) {
            if (n > 0) {
                queryBuilder.sql.append(", ");
            }
            queryBuilder.sql.append('?');
        }
        queryBuilder.sql.append(')');
        return queryBuilder;
    }

    public static QueryBuilder update(String string) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.sql.append("UPDATE ").append(string);
        return queryBuilder;
    }

    public static QueryBuilder deleteFrom(String string) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.sql.append("DELETE FROM ").append(string);
        return queryBuilder;
    }

    public QueryBuilder from(String string) {
        this.sql.append(" FROM ").append(string);
        return this;
    }

    public QueryBuilder where(String string, Object ... objectArray) {
        this.sql.append(" WHERE ").append(string);
        this.params.addAll(Arrays.asList(objectArray));
        return this;
    }

    public QueryBuilder and(String string, Object ... objectArray) {
        this.sql.append(" AND ").append(string);
        this.params.addAll(Arrays.asList(objectArray));
        return this;
    }

    public QueryBuilder or(String string, Object ... objectArray) {
        this.sql.append(" OR ").append(string);
        this.params.addAll(Arrays.asList(objectArray));
        return this;
    }

    public QueryBuilder set(String string, Object object) {
        this.sql.append(this.setStarted ? ", " : " SET ").append(string);
        this.setStarted = true;
        this.params.add(object);
        return this;
    }

    public QueryBuilder orderBy(String string) {
        this.sql.append(" ORDER BY ").append(string);
        return this;
    }

    public QueryBuilder limit(int n) {
        this.sql.append(" LIMIT ").append(n);
        return this;
    }

    public QueryBuilder bind(Object object) {
        this.params.add(object);
        return this;
    }

    public String sql() {
        return this.sql.toString();
    }

    public Object[] parameters() {
        return this.params.toArray();
    }
}

