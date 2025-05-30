package model;

import java.time.LocalDateTime;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Carrinho {
    private int id;
    private model.Cliente cliente;
    private ObservableList<model.ItemCarrinho> itens;
    private LocalDateTime dataHora;
    private double valorTotal;
    private String status; // "ABERTO", "FINALIZADO", "CANCELADO"
    
    public Carrinho() {
        this.itens = FXCollections.observableArrayList();
        this.dataHora = LocalDateTime.now();
        this.status = "ABERTO";
        this.valorTotal = 0.0;
    }
    
    public Carrinho(int id, model.Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.itens = FXCollections.observableArrayList();
        this.dataHora = LocalDateTime.now();
        this.status = "ABERTO";
        this.valorTotal = 0.0;
    }
    
    public void adicionarItem(model.Produto produto, int quantidade) {
        for (model.ItemCarrinho item : itens) {
            if (item.getProduto().getId() == produto.getId()) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                calcularValorTotal();
                return;
            }
        }
        model.ItemCarrinho novoItem = new model.ItemCarrinho();
        novoItem.setProduto(produto);
        novoItem.setQuantidade(quantidade);
        novoItem.setPrecoUnitario(produto.getPreco());
        itens.add(novoItem);
        calcularValorTotal();
    }
    
    public void removerItem(int produtoId) {
        itens.removeIf(item -> item.getProduto().getId() == produtoId);
        calcularValorTotal();
    }
    
    public void atualizarQuantidade(int produtoId, int novaQuantidade) {
        for (model.ItemCarrinho item : itens) {
            if (item.getProduto().getId() == produtoId) {
                if (novaQuantidade <= 0) {
                    removerItem(produtoId);
                } else {
                    item.setQuantidade(novaQuantidade);
                }
                calcularValorTotal();
                return;
            }
        }
    }
    
    public void calcularValorTotal() {
        this.valorTotal = 0.0;
        for (model.ItemCarrinho item : itens) {
            this.valorTotal += item.getSubtotal();
        }
    }
    
    public void finalizarCompra() {
        this.status = "FINALIZADO";
        this.dataHora = LocalDateTime.now();
    }
    
    public void cancelarCompra() {
        this.status = "CANCELADO";
    }
    
    public int getTotalItens() {
        int total = 0;
        for (model.ItemCarrinho item : itens) {
            total += item.getQuantidade();
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public model.Cliente getCliente() {
        return cliente;
    }

    public void setCliente(model.Cliente cliente) {
        this.cliente = cliente;
    }

    public ObservableList<model.ItemCarrinho> getItens() {
        return itens;
    }

    public void setItens(ObservableList<model.ItemCarrinho> itens) {
        this.itens = itens;
        calcularValorTotal();
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cliente, dataHora);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Carrinho other = (Carrinho) obj;
        return id == other.id && Objects.equals(cliente, other.cliente) && Objects.equals(dataHora, other.dataHora);
    }

    @Override
    public String toString() {
        return "Carrinho [id=" + id + ", cliente=" + cliente + ", itens=" + itens.size() 
                + ", dataHora=" + dataHora + ", valorTotal=" + valorTotal + ", status=" + status + "]";
    }
}
