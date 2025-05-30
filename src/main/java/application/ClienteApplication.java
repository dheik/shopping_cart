package application;

import dao.DAOCliente;
import model.Cliente;
import java.util.List;

public class ClienteApplication {
    private DAOCliente clienteDAO;

    public ClienteApplication() {
        this.clienteDAO = new DAOCliente();
    }

    public void salvar(Cliente cliente) {
        DAOCliente.inserirCliente(cliente);
    }

    public void atualizar(Cliente cliente) {
        DAOCliente.atualizarCliente(cliente);
    }

    public void excluir(Long id) {
        DAOCliente.excluirCliente(id.intValue());
    }

    public Cliente buscarPorId(Long id) {
        return DAOCliente.getClienteById(id.intValue());
    }

    public List<Cliente> listarTodos() {
        return DAOCliente.listarClientes();
    }
} 