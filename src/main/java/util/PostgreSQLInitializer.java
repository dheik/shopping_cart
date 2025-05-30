package util;

import java.sql.Connection;
import java.sql.Statement;
import dao.ConnectionPostgreSQL;

public class PostgreSQLInitializer {
    
    public static void initializeDatabase() {
        Connection conn = null;
        Statement stmt = null;
        
        try {
            conn = ConnectionPostgreSQL.connect();
            stmt = conn.createStatement();
            
            // Criar tabela Cliente
            stmt.execute("CREATE TABLE IF NOT EXISTS Cliente (" +
                    "id SERIAL PRIMARY KEY," +
                    "nome VARCHAR(100) NOT NULL," +
                    "cpf VARCHAR(14) NOT NULL UNIQUE," +
                    "email VARCHAR(100)," +
                    "telefone VARCHAR(20)," +
                    "endereco VARCHAR(200)" +
                    ")");
            
            // Criar tabela Produto
            stmt.execute("CREATE TABLE IF NOT EXISTS Produto (" +
                    "id SERIAL PRIMARY KEY," +
                    "nome VARCHAR(100) NOT NULL," +
                    "descricao TEXT," +
                    "preco NUMERIC(10,2) NOT NULL," +
                    "estoque INTEGER NOT NULL," +
                    "categoria VARCHAR(50)," +
                    "codigoBarras VARCHAR(20) UNIQUE" +
                    ")");
            
            // Criar tabela Carrinho
            stmt.execute("CREATE TABLE IF NOT EXISTS Carrinho (" +
                    "id SERIAL PRIMARY KEY," +
                    "cliente_id INTEGER NOT NULL," +
                    "data_hora TIMESTAMP NOT NULL," +
                    "valor_total NUMERIC(10,2) NOT NULL," +
                    "status VARCHAR(20) NOT NULL," +
                    "FOREIGN KEY (cliente_id) REFERENCES Cliente(id)" +
                    ")");
            
            // Criar tabela ItemCarrinho
            stmt.execute("CREATE TABLE IF NOT EXISTS ItemCarrinho (" +
                    "id SERIAL PRIMARY KEY," +
                    "carrinho_id INTEGER NOT NULL," +
                    "produto_id INTEGER NOT NULL," +
                    "quantidade INTEGER NOT NULL," +
                    "preco_unitario NUMERIC(10,2) NOT NULL," +
                    "subtotal NUMERIC(10,2) NOT NULL," +
                    "FOREIGN KEY (carrinho_id) REFERENCES Carrinho(id)," +
                    "FOREIGN KEY (produto_id) REFERENCES Produto(id)" +
                    ")");
            
            System.out.println("Tabelas criadas com sucesso!");
            
            // Inserir dados de exemplo se necessário
            insertSampleData(conn);
            
        } catch (Exception e) {
            System.err.println("Erro ao inicializar banco de dados: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }
    
    private static void insertSampleData(Connection conn) {
        Statement stmt = null;
        
        try {
            stmt = conn.createStatement();
            
            // Verificar se já existem dados
            if (!hasData(conn, "Cliente")) {
                // Inserir clientes de exemplo
                stmt.execute("INSERT INTO Cliente (nome, cpf, email, telefone, endereco) VALUES " +
                        "('Victor Thiago', '123.456.789-00', 'vbitkill@email.com', '(11) 98765-4321', 'Rua A, 123')");
                stmt.execute("INSERT INTO Cliente (nome, cpf, email, telefone, endereco) VALUES " +
                        "('Maria Santos', '987.654.321-00', 'maria@email.com', '(11) 91234-5678', 'Rua B, 456')");
                
                System.out.println("Clientes de exemplo inseridos com sucesso!");
            }
            
            if (!hasData(conn, "Produto")) {
                // Inserir produtos de exemplo
                stmt.execute("INSERT INTO Produto (nome, descricao, preco, estoque, categoria, codigoBarras) VALUES " +
                        "('Notebook Dell', 'Notebook Dell Inspiron 15 8GB RAM 256GB SSD', 3499.99, 10, 'Informática', '7891234567890')");
                stmt.execute("INSERT INTO Produto (nome, descricao, preco, estoque, categoria, codigoBarras) VALUES " +
                        "('Smartphone Samsung', 'Samsung Galaxy S21 128GB', 2999.99, 15, 'Celulares', '7899876543210')");
                stmt.execute("INSERT INTO Produto (nome, descricao, preco, estoque, categoria, codigoBarras) VALUES " +
                        "('Smart TV LG', 'Smart TV LG 50\" 4K', 2599.99, 8, 'Eletrônicos', '7897654321098')");
                stmt.execute("INSERT INTO Produto (nome, descricao, preco, estoque, categoria, codigoBarras) VALUES " +
                        "('Fone de Ouvido JBL', 'Fone de Ouvido JBL Bluetooth', 199.99, 30, 'Acessórios', '7891234509876')");
                stmt.execute("INSERT INTO Produto (nome, descricao, preco, estoque, categoria, codigoBarras) VALUES " +
                        "('Mouse Logitech', 'Mouse sem fio Logitech', 89.99, 25, 'Informática', '7893216549870')");
                
                System.out.println("Produtos de exemplo inseridos com sucesso!");
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao inserir dados de exemplo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
        }
    }
    
    private static boolean hasData(Connection conn, String tableName) {
        Statement stmt = null;
        
        try {
            stmt = conn.createStatement();
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            System.err.println("Erro ao verificar dados: " + e.getMessage());
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
        }
        
        return false;
    }
}
