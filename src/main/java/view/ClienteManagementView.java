package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Cliente;
import application.ClienteApplication;
import java.util.List;

public class ClienteManagementView extends Application {
    private TableView<Cliente> tableClientes;
    private TextField txtNome, txtCpf, txtEmail, txtTelefone, txtEndereco;
    private Button btnAdicionar, btnEditar, btnExcluir;
    private ClienteApplication clienteApplication;
    private Cliente clienteSelecionado;

    @Override
    public void start(Stage primaryStage) {
        clienteApplication = new ClienteApplication();
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        
        // Título
        Label lblTitulo = new Label("Gerenciamento de Clientes");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Formulário
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));
        
        formGrid.add(new Label("Nome:"), 0, 0);
        txtNome = new TextField();
        formGrid.add(txtNome, 1, 0);
        
        formGrid.add(new Label("CPF:"), 0, 1);
        txtCpf = new TextField();
        formGrid.add(txtCpf, 1, 1);
        
        formGrid.add(new Label("Email:"), 0, 2);
        txtEmail = new TextField();
        formGrid.add(txtEmail, 1, 2);
        
        formGrid.add(new Label("Telefone:"), 0, 3);
        txtTelefone = new TextField();
        formGrid.add(txtTelefone, 1, 3);
        
        formGrid.add(new Label("Endereço:"), 0, 4);
        txtEndereco = new TextField();
        formGrid.add(txtEndereco, 1, 4);
        
        // Botões
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        btnAdicionar = new Button("Adicionar");
        btnEditar = new Button("Editar");
        btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().add("btnExcluir");

        
        buttonBox.getChildren().addAll(btnAdicionar, btnEditar, btnExcluir);
        
        // Tabela
        tableClientes = new TableView<>();
        tableClientes.setPlaceholder(new Label("Nenhum cliente cadastrado"));
        
        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(
                cellData.getValue().getId()).asObject());
        colId.setPrefWidth(50);
        
        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getNome()));
        colNome.setPrefWidth(200);
        
        TableColumn<Cliente, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getCpf()));
        colCpf.setPrefWidth(120);
        
        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getEmail()));
        colEmail.setPrefWidth(200);
        
        TableColumn<Cliente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getTelefone()));
        colTelefone.setPrefWidth(120);
        
        TableColumn<Cliente, String> colEndereco = new TableColumn<>("Endereço");
        colEndereco.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getEndereco()));
        colEndereco.setPrefWidth(200);
        
        tableClientes.getColumns().addAll(colId, colNome, colCpf, colEmail, colTelefone, colEndereco);
        
        // Layout
        VBox topBox = new VBox(20, lblTitulo, formGrid, buttonBox);
        
        root.setTop(topBox);
        root.setCenter(tableClientes);
        
        // Eventos
        btnAdicionar.setOnAction(e -> adicionarCliente());
        btnEditar.setOnAction(e -> editarCliente());
        btnExcluir.setOnAction(e -> excluirCliente());
        
        tableClientes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        clienteSelecionado = newSelection;
                        preencherFormulario(clienteSelecionado);
                    }
                });
        
        // Carrega os clientes
        carregarClientes();
        
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Shopping Cart - Gerenciamento de Clientes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void carregarClientes() {
        List<Cliente> clientes = clienteApplication.listarTodos();
        tableClientes.getItems().clear();
        tableClientes.getItems().addAll(clientes);
    }

    private void preencherFormulario(Cliente cliente) {
        txtNome.setText(cliente.getNome());
        txtCpf.setText(cliente.getCpf());
        txtEmail.setText(cliente.getEmail());
        txtTelefone.setText(cliente.getTelefone());
        txtEndereco.setText(cliente.getEndereco());
    }

    private void limparFormulario() {
        txtNome.clear();
        txtCpf.clear();
        txtEmail.clear();
        txtTelefone.clear();
        txtEndereco.clear();
        clienteSelecionado = null;
        tableClientes.getSelectionModel().clearSelection();
    }

    private void adicionarCliente() {
        try {
            Cliente cliente = new Cliente();
            cliente.setNome(txtNome.getText());
            cliente.setCpf(txtCpf.getText());
            cliente.setEmail(txtEmail.getText());
            cliente.setTelefone(txtTelefone.getText());
            cliente.setEndereco(txtEndereco.getText());

            clienteApplication.salvar(cliente);
            limparFormulario();
            carregarClientes();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Cliente adicionado");
            alert.setContentText("Cliente adicionado com sucesso!");
            alert.showAndWait();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao adicionar cliente");
            alert.setContentText("Erro ao adicionar cliente: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void editarCliente() {
        if (clienteSelecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhum cliente selecionado");
            alert.setContentText("Por favor, selecione um cliente para editar.");
            alert.showAndWait();
            return;
        }

        try {
            clienteSelecionado.setNome(txtNome.getText());
            clienteSelecionado.setCpf(txtCpf.getText());
            clienteSelecionado.setEmail(txtEmail.getText());
            clienteSelecionado.setTelefone(txtTelefone.getText());
            clienteSelecionado.setEndereco(txtEndereco.getText());

            clienteApplication.atualizar(clienteSelecionado);
            limparFormulario();
            carregarClientes();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Cliente atualizado");
            alert.setContentText("Cliente atualizado com sucesso!");
            alert.showAndWait();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao atualizar cliente");
            alert.setContentText("Erro ao atualizar cliente: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void excluirCliente() {
        if (clienteSelecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhum cliente selecionado");
            alert.setContentText("Por favor, selecione um cliente para excluir.");
            alert.showAndWait();
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText("Excluir cliente");
        confirmacao.setContentText("Tem certeza que deseja excluir este cliente?");
        
        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            try {
                clienteApplication.excluir((long) clienteSelecionado.getId());
                limparFormulario();
                carregarClientes();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText("Cliente excluído");
                alert.setContentText("Cliente excluído com sucesso!");
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao excluir cliente");
                alert.setContentText("Erro ao excluir cliente: " + ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 