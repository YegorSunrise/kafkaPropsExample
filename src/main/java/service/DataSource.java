package service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    private static final boolean isPostgres = false;

    static {
        if(isPostgres) {
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/test_kafka");
            config.setUsername("postgres");
            config.setPassword("admin");
            config.addDataSourceProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        } else {
            config.setJdbcUrl("jdbc:clickhouse://localhost:6666/default");
            config.setUsername("default");
//            config.setPassword("");
            config.addDataSourceProperty("dataSourceClassName", "org.");
        }


        ds = new HikariDataSource(config);
    }

    private DataSource() {
    }
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
