package dao;

import model.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAOProduto {
    public static String sqlInsert = "INSERT INTO Produto(nome, descricao, preco, estoque, categoria, codigoBarras) VALUES (?, ?, ?, ?, ?, ?)";
    public static String sqlDelete = "DELETE FROM Produto WHERE id = ?";
    public static String sqlUpdate = "UPDATE Produto SET nome = ?, descricao = ?, preco = ?, estoque = ?, categoria = ?, codigoBarras = ? WHERE id = ?";
    public static String sqlSelect = "SELECT id, nome, descricao, preco, estoque, categoria, codigoBarras FROM Produto";
    public static String sqlSelectById = "SELECT id, nome, descricao, preco, estoque, categoria, codigoBarras FROM Produto WHERE id = ?";
    public static String sqlSelectByTermo = "SELECT id, nome, descricao, preco, estoque, categoria, codigoBarras FROM Produto WHERE nome LIKE ? OR descricao LIKE ? OR categoria LIKE ?";

    public static List<Produto> listarProdutos() {
        List<Produto> listaProdutos = new ArrayList<>();
        Connection conn = ConnectionPostgreSQL.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sqlSelect);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setEstoque(rs.getInt("estoque"));
                produto.setCategoria(rs.getString("categoria"));
                produto.setCodigoBarras(rs.getString("codigoBarras"));
                listaProdutos.add(produto);
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
        return listaProdutos;
    }

    public static List<Produto> pesquisarProdutos(String termo) {
        List<Produto> listaProdutos = new ArrayList<>();
        Connection conn = ConnectionPostgreSQL.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sqlSelectByTermo);
            String termoBusca = "%" + termo + "%";
            stmt.setString(1, termoBusca);
            stmt.setString(2, termoBusca);
            stmt.setString(3, termoBusca);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setEstoque(rs.getInt("estoque"));
                produto.setCategoria(rs.getString("categoria"));
                produto.setCodigoBarras(rs.getString("codigoBarras"));
                listaProdutos.add(produto);
            }
        } catch (Exception e) {
            System.err.println("Erro ao pesquisar produtos: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
        return listaProdutos;
    }

    public static Produto getProdutoById(int id) {
        Produto produto = new Produto();
        Connection conn = ConnectionPostgreSQL.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sqlSelectById);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setEstoque(rs.getInt("estoque"));
                produto.setCategoria(rs.getString("categoria"));
                produto.setCodigoBarras(rs.getString("codigoBarras"));
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar produto por ID: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
        return produto;
    }

    public static void inserirProduto(Produto produto) {
        Connection conn = ConnectionPostgreSQL.connect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sqlInsert);
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getEstoque());
            stmt.setString(5, produto.getCategoria());
            stmt.setString(6, produto.getCodigoBarras());
            stmt.execute();
            System.out.println("Produto inserido com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao inserir produto: " + e.getMessage());
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }

    public static void atualizarProduto(Produto produto) {
        Connection conn = ConnectionPostgreSQL.connect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sqlUpdate);
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getEstoque());
            stmt.setString(5, produto.getCategoria());
            stmt.setString(6, produto.getCodigoBarras());
            stmt.setInt(7, produto.getId());
            stmt.execute();
            System.out.println("Produto atualizado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }

    public static void excluirProduto(int id) {
        Connection conn = ConnectionPostgreSQL.connect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sqlDelete);
            stmt.setInt(1, id);
            stmt.execute();
            System.out.println("Produto excluído com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao excluir produto: " + e.getMessage());
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }
    
    public static void atualizarEstoque(int produtoId, int quantidade) {
        Connection conn = ConnectionPostgreSQL.connect();
        PreparedStatement stmt = null;
        try {
            // Primeiro obtém o estoque atual
            Produto produto = getProdutoById(produtoId);
            int novoEstoque = produto.getEstoque() - quantidade;
            
            // Atualiza o estoque
            String sql = "UPDATE Produto SET estoque = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, novoEstoque);
            stmt.setInt(2, produtoId);
            stmt.execute();
            System.out.println("Estoque atualizado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao atualizar estoque: " + e.getMessage());
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }
}
