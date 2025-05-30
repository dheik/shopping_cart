package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionPostgreSQL {
    private static final String URL = "jdbc:postgresql://localhost:5432/shopping_cart";
    private static final String USER = "postgres";
    private static final String PASSWORD = "senha";

    public static Connection connect() {
        try {

            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (conn == null) {
                System.err.println("Erro: conexão retornou nula.");
            }
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL não encontrado: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao PostgreSQL: " + e.getMessage());
        }
        return null;
    }
}
