package dao;

import model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAOCliente {
    public static String sqlInsert = "INSERT INTO Cliente(nome, cpf, email, telefone, endereco) VALUES (?, ?, ?, ?, ?)";
    public static String sqlDelete = "DELETE FROM Cliente WHERE id = ?";
    public static String sqlUpdate = "UPDATE Cliente SET nome = ?, cpf = ?, email = ?, telefone = ?, endereco = ? WHERE id = ?";
    public static String sqlSelect = "SELECT id, nome, cpf, email, telefone, endereco FROM Cliente";
    public static String sqlSelectById = "SELECT id, nome, cpf, email, telefone, endereco FROM Cliente WHERE id = ?";
    public static String sqlSelectByTermo = "SELECT id, nome, cpf, email, telefone, endereco FROM Cliente WHERE nome LIKE ? OR cpf LIKE ?";

    public static List<Cliente> listarClientes() {
        List<Cliente> listaClientes = new ArrayList<>();
        Connection conn = ConnectionPostgreSQL.connect();
        if (conn == null) {
            System.err.println("Erro: conexão com o banco de dados está nula.");
            return listaClientes;
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sqlSelect);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setEmail(rs.getString("email"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEndereco(rs.getString("endereco"));
                listaClientes.add(cliente);
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar clientes: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
        return listaClientes;
    }

    public static List<Cliente> pesquisarClientes(String termo) {
        List<Cliente> listaClientes = new ArrayList<>();
        Connection conn = ConnectionPostgreSQL.connect();
        if (conn == null) {
            System.err.println("Erro: conexão com o banco de dados está nula.");
            return listaClientes;
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sqlSelectByTermo);
            String termoBusca = "%" + termo + "%";
            stmt.setString(1, termoBusca);
            stmt.setString(2, termoBusca);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setEmail(rs.getString("email"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEndereco(rs.getString("endereco"));
                listaClientes.add(cliente);
            }
        } catch (Exception e) {
            System.err.println("Erro ao pesquisar clientes: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
        return listaClientes;
    }

    public static Cliente getClienteById(int id) {
        Cliente cliente = new Cliente();
        Connection conn = ConnectionPostgreSQL.connect();
        if (conn == null) {
            System.err.println("Erro: conexão com o banco de dados está nula.");
            return cliente;
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sqlSelectById);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setEmail(rs.getString("email"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEndereco(rs.getString("endereco"));
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar cliente por ID: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
        return cliente;
    }

    public static void inserirCliente(Cliente cliente) {
        Connection conn = ConnectionPostgreSQL.connect();
        if (conn == null) {
            System.err.println("Erro: conexão com o banco de dados está nula.");
            return;
        }
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sqlInsert);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefone());
            stmt.setString(5, cliente.getEndereco());
            stmt.execute();
            System.out.println("Cliente inserido com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao inserir cliente: " + e.getMessage());
            throw new RuntimeException("Erro ao inserir cliente: " + e.getMessage());
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    public static void atualizarCliente(Cliente cliente) {
        Connection conn = ConnectionPostgreSQL.connect();
        if (conn == null) {
            System.err.println("Erro: conexão com o banco de dados está nula.");
            return;
        }
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sqlUpdate);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefone());
            stmt.setString(5, cliente.getEndereco());
            stmt.setInt(6, cliente.getId());
            stmt.execute();
            System.out.println("Cliente atualizado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao atualizar cliente: " + e.getMessage());
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    public static void excluirCliente(int id) {
        Connection conn = ConnectionPostgreSQL.connect();
        if (conn == null) {
            System.err.println("Erro: conexão com o banco de dados está nula.");
            return;
        }
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sqlDelete);
            stmt.setInt(1, id);
            stmt.execute();
            System.out.println("Cliente excluído com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao excluir cliente: " + e.getMessage());
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
