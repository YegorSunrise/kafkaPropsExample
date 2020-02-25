package service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/test_kafka");
        config.setUsername("postgres");
        config.setPassword("admin");
        config.addDataSourceProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        ds = new HikariDataSource(config);
    }

    private DataSource() {
    }
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
