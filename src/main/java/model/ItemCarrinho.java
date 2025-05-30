package model;

import java.util.Objects;

public class ItemCarrinho {
    private int id;
    private model.Produto produto;
    private int quantidade;
    private double precoUnitario;
    private double subtotal;
    
    public ItemCarrinho() {
    }
    
    public ItemCarrinho(int id, model.Produto produto, int quantidade, double precoUnitario) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.calcularSubtotal();
    }
    
    public void calcularSubtotal() {
        this.subtotal = this.quantidade * this.precoUnitario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        calcularSubtotal();
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
        calcularSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, produto, quantidade, precoUnitario);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ItemCarrinho other = (ItemCarrinho) obj;
        return id == other.id && Objects.equals(produto, other.produto) 
                && quantidade == other.quantidade
                && Double.doubleToLongBits(precoUnitario) == Double.doubleToLongBits(other.precoUnitario);
    }

    @Override
    public String toString() {
        return "ItemCarrinho [id=" + id + ", produto=" + produto + ", quantidade=" + quantidade 
                + ", precoUnitario=" + precoUnitario + ", subtotal=" + subtotal + "]";
    }
}
