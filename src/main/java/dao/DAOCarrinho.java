package dao;

import model.Carrinho;
import model.ItemCarrinho;
import model.Cliente;
import model.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DAOCarrinho {
    // SQL para Carrinho
    public static String sqlInsertCarrinho = "INSERT INTO Carrinho(cliente_id, data_hora, valor_total, status) VALUES (?, ?, ?, ?)";
    public static String sqlUpdateCarrinho = "UPDATE Carrinho SET cliente_id = ?, data_hora = ?, valor_total = ?, status = ? WHERE id = ?";
    public static String sqlSelectCarrinho = "SELECT id, cliente_id, data_hora, valor_total, status FROM Carrinho";
    public static String sqlSelectCarrinhoById = "SELECT id, cliente_id, data_hora, valor_total, status FROM Carrinho WHERE id = ?";
    public static String sqlSelectCarrinhoByCliente = "SELECT id, cliente_id, data_hora, valor_total, status FROM Carrinho WHERE cliente_id = ?";
    
    // SQL para ItemCarrinho
    public static String sqlInsertItem = "INSERT INTO ItemCarrinho(carrinho_id, produto_id, quantidade, preco_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
    public static String sqlSelectItensByCarrinho = "SELECT id, carrinho_id, produto_id, quantidade, preco_unitario, subtotal FROM ItemCarrinho WHERE carrinho_id = ?";
    
    public static void salvarCarrinho(Carrinho carrinho) {
        Connection conn = ConnectionPostgreSQL.connect();
        PreparedStatement stmtCarrinho = null;
        PreparedStatement stmtItem = null;
        ResultSet rs = null;
        
        try {
            // Inicia transação
            conn.setAutoCommit(false);
            
            // Insere ou atualiza o carrinho
            if (carrinho.getId() == 0) {
                // Novo carrinho
                stmtCarrinho = conn.prepareStatement(sqlInsertCarrinho, Statement.RETURN_GENERATED_KEYS);
                stmtCarrinho.setInt(1, carrinho.getCliente().getId());
                stmtCarrinho.setTimestamp(2, Timestamp.valueOf(carrinho.getDataHora()));
                stmtCarrinho.setDouble(3, carrinho.getValorTotal());
                stmtCarrinho.setString(4, carrinho.getStatus());
                stmtCarrinho.executeUpdate();
                
                // Obtém o ID gerado
                rs = stmtCarrinho.getGeneratedKeys();
                if (rs.next()) {
                    carrinho.setId(rs.getInt(1));
                }
            } else {
                // Atualiza carrinho existente
                stmtCarrinho = conn.prepareStatement(sqlUpdateCarrinho);
                stmtCarrinho.setInt(1, carrinho.getCliente().getId());
                stmtCarrinho.setTimestamp(2, Timestamp.valueOf(carrinho.getDataHora()));
                stmtCarrinho.setDouble(3, carrinho.getValorTotal());
                stmtCarrinho.setString(4, carrinho.getStatus());
                stmtCarrinho.setInt(5, carrinho.getId());
                stmtCarrinho.executeUpdate();
                
                // Remove itens antigos
                PreparedStatement stmtDeleteItens = conn.prepareStatement("DELETE FROM ItemCarrinho WHERE carrinho_id = ?");
                stmtDeleteItens.setInt(1, carrinho.getId());
                stmtDeleteItens.executeUpdate();
                stmtDeleteItens.close();
            }
            
            // Insere os itens do carrinho
            stmtItem = conn.prepareStatement(sqlInsertItem);
            for (ItemCarrinho item : carrinho.getItens()) {
                stmtItem.setInt(1, carrinho.getId());
                stmtItem.setInt(2, item.getProduto().getId());
                stmtItem.setInt(3, item.getQuantidade());
                stmtItem.setDouble(4, item.getPrecoUnitario());
                stmtItem.setDouble(5, item.getSubtotal());
                stmtItem.addBatch();
                
                // Atualiza o estoque se o carrinho estiver finalizado
                if (carrinho.getStatus().equals("FINALIZADO")) {
                    DAOProduto.atualizarEstoque(item.getProduto().getId(), item.getQuantidade());
                }
            }
            stmtItem.executeBatch();
            
            // Confirma a transação
            conn.commit();
            
            System.out.println("Carrinho salvo com sucesso!");
        } catch (Exception e) {
            // Rollback em caso de erro
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                System.err.println("Erro ao fazer rollback: " + ex.getMessage());
            }
            System.err.println("Erro ao salvar carrinho: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmtItem != null) stmtItem.close(); } catch (Exception ignored) {}
            try { if (stmtCarrinho != null) stmtCarrinho.close(); } catch (Exception ignored) {}
            try { 
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close(); 
                }
            } catch (Exception ignored) {}
        }
    }
    
    public static Carrinho getCarrinhoById(int id) {
        Carrinho carrinho = null;
        Connection conn = ConnectionPostgreSQL.connect();
        PreparedStatement stmtCarrinho = null;
        PreparedStatement stmtItens = null;
        ResultSet rsCarrinho = null;
        ResultSet rsItens = null;
        
        try {
            // Busca o carrinho
            stmtCarrinho = conn.prepareStatement(sqlSelectCarrinhoById);
            stmtCarrinho.setInt(1, id);
            rsCarrinho = stmtCarrinho.executeQuery();
            
            if (rsCarrinho.next()) {
                // Cria o objeto Carrinho
                carrinho = new Carrinho();
                carrinho.setId(rsCarrinho.getInt("id"));
                
                // Busca o cliente
                int clienteId = rsCarrinho.getInt("cliente_id");
                Cliente cliente = DAOCliente.getClienteById(clienteId);
                carrinho.setCliente(cliente);
                
                // Define os outros atributos
                carrinho.setDataHora(rsCarrinho.getTimestamp("data_hora").toLocalDateTime());
                carrinho.setStatus(rsCarrinho.getString("status"));
                
                // Busca os itens do carrinho
                stmtItens = conn.prepareStatement(sqlSelectItensByCarrinho);
                stmtItens.setInt(1, id);
                rsItens = stmtItens.executeQuery();
                
                while (rsItens.next()) {
                    ItemCarrinho item = new ItemCarrinho();
                    item.setId(rsItens.getInt("id"));
                    
                    // Busca o produto
                    int produtoId = rsItens.getInt("produto_id");
                    Produto produto = DAOProduto.getProdutoById(produtoId);
                    item.setProduto(produto);
                    
                    item.setQuantidade(rsItens.getInt("quantidade"));
                    item.setPrecoUnitario(rsItens.getDouble("preco_unitario"));
                    
                    // Adiciona o item ao carrinho
                    carrinho.getItens().add(item);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar carrinho: " + e.getMessage());
        } finally {
            try { if (rsItens != null) rsItens.close(); } catch (Exception ignored) {}
            try { if (stmtItens != null) stmtItens.close(); } catch (Exception ignored) {}
            try { if (rsCarrinho != null) rsCarrinho.close(); } catch (Exception ignored) {}
            try { if (stmtCarrinho != null) stmtCarrinho.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
        
        return carrinho;
    }
    
    public static List<Carrinho> listarCarrinhosPorCliente(int clienteId) {
        List<Carrinho> carrinhos = new ArrayList<>();
        Connection conn = ConnectionPostgreSQL.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareStatement(sqlSelectCarrinhoByCliente);
            stmt.setInt(1, clienteId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                int carrinhoId = rs.getInt("id");
                // Busca o carrinho completo com seus itens
                Carrinho carrinho = getCarrinhoById(carrinhoId);
                carrinhos.add(carrinho);
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar carrinhos por cliente: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
        
        return carrinhos;
    }
    
    public static List<Carrinho> listarCarrinhos() {
        List<Carrinho> carrinhos = new ArrayList<>();
        Connection conn = ConnectionPostgreSQL.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareStatement(sqlSelectCarrinho);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                int carrinhoId = rs.getInt("id");
                // Busca o carrinho completo com seus itens
                Carrinho carrinho = getCarrinhoById(carrinhoId);
                carrinhos.add(carrinho);
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar carrinhos: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
        
        return carrinhos;
    }
}
