package application;

import dao.DAOProduto;
import model.Produto;
import java.util.List;

public class ProdutoApplication {
    private DAOProduto produtoDAO;

    public ProdutoApplication() {
        this.produtoDAO = new DAOProduto();
    }

    public void salvar(Produto produto) {
        DAOProduto.inserirProduto(produto);
    }

    public void atualizar(Produto produto) {
        DAOProduto.atualizarProduto(produto);
    }

    public void excluir(Long id) {
        DAOProduto.excluirProduto(id.intValue());
    }

    public Produto buscarPorId(Long id) {
        return DAOProduto.getProdutoById(id.intValue());
    }

    public List<Produto> listarTodos() {
        return DAOProduto.listarProdutos();
    }
} 