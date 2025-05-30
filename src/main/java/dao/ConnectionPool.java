package dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static HikariDataSource dataSource;
    
    static {
        try {
            initializeDataSource();
        } catch (Exception e) {
            System.err.println("Erro ao inicializar pool de conexões: " + e.getMessage());
            throw new RuntimeException("Falha ao inicializar pool de conexões", e);
        }
    }
    
    private static void initializeDataSource() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(DatabaseConfig.DB_URL);
        config.setUsername(DatabaseConfig.DB_USER);
        config.setPassword(DatabaseConfig.DB_PASSWORD);

        config.setMinimumIdle(DatabaseConfig.MIN_POOL_SIZE);
        config.setMaximumPoolSize(DatabaseConfig.MAX_POOL_SIZE);
        config.setInitializationFailTimeout(DatabaseConfig.CONNECTION_TIMEOUT);
        config.setConnectionTimeout(DatabaseConfig.CONNECTION_TIMEOUT);
        config.setValidationTimeout(DatabaseConfig.VALIDATION_TIMEOUT);
        config.setIdleTimeout(DatabaseConfig.MAX_IDLE_TIME);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        config.setConnectionTestQuery("SELECT 1");
        
        dataSource = new HikariDataSource(config);
    }
    
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Pool de conexões não inicializado");
        }
        return dataSource.getConnection();
    }
    
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
} 