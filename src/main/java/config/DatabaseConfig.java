package config;

public class DatabaseConfig {

    public static final String DB_URL = "jdbc:postgresql://localhost:5432/shopping_cart";
    public static final String DB_USER = "postgres";
    public static final String DB_PASSWORD = "senha";

    public static final int INITIAL_POOL_SIZE = 5;
    public static final int MAX_POOL_SIZE = 10;
    public static final int MIN_POOL_SIZE = 2;
    public static final int MAX_IDLE_TIME = 30000; // 30 segundos

    public static final int CONNECTION_TIMEOUT = 30000; // 30 segundos
    public static final int VALIDATION_TIMEOUT = 5000; // 5 segundos
    
    private DatabaseConfig() {

    }
} 