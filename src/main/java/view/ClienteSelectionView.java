package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Cliente;
import dao.DAOCliente;
import java.util.List;

public class ClienteSelectionView extends Application {
    
    private TableView<Cliente> tableClientes;
    private Button btnSelecionar;
    private Button btnNovo;
    private TextField txtPesquisa;
    private Cliente clienteSelecionado;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        
        // Título
        Label lblTitulo = new Label("Selecione um Cliente");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Tabela de clientes
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
        
        TableColumn<Cliente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getTelefone()));
        colTelefone.setPrefWidth(120);
        
        tableClientes.getColumns().addAll(colId, colNome, colCpf, colTelefone);
        
        // Botões
        btnSelecionar = new Button("Selecionar Cliente");
        btnSelecionar.setDisable(true);
        btnSelecionar.setPrefWidth(150);
        btnSelecionar.setOnAction(e -> selecionarCliente());
        
        btnNovo = new Button("Novo Cliente");
        btnNovo.setPrefWidth(150);
        btnNovo.setOnAction(e -> novoCliente());
        
        HBox botoesBox = new HBox(10, btnNovo, btnSelecionar);
        botoesBox.setAlignment(Pos.CENTER_RIGHT);
        
        // Barra de pesquisa
        txtPesquisa = new TextField();
        txtPesquisa.setPromptText("Pesquisar cliente...");
        txtPesquisa.setPrefWidth(300);
        txtPesquisa.textProperty().addListener((obs, oldVal, newVal) -> {
            pesquisarClientes(newVal);
        });
        
        HBox pesquisaBox = new HBox(10, new Label("Pesquisar:"), txtPesquisa);
        pesquisaBox.setAlignment(Pos.CENTER_LEFT);
        
        // Layout
        VBox topBox = new VBox(20, lblTitulo, pesquisaBox);
        
        root.setTop(topBox);
        root.setCenter(tableClientes);
        root.setBottom(botoesBox);
        
        // Evento de seleção na tabela
        tableClientes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    btnSelecionar.setDisable(newSelection == null);
                });
        
        // Carrega os clientes
        carregarClientes();
        
        Scene scene = new Scene(root, 600, 500);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Shopping Cart - Seleção de Cliente");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void carregarClientes() {
        List<Cliente> clientes = DAOCliente.listarClientes();
        tableClientes.getItems().clear();
        tableClientes.getItems().addAll(clientes);
    }

    private void pesquisarClientes(String termo) {
        if (termo == null || termo.isEmpty()) {
            carregarClientes();
        } else {
            List<Cliente> clientesFiltrados = DAOCliente.pesquisarClientes(termo);
            tableClientes.getItems().clear();
            tableClientes.getItems().addAll(clientesFiltrados);
        }
    }

    private void selecionarCliente() {
        clienteSelecionado = tableClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            // Abre a tela de produtos com o cliente selecionado
            Stage produtosStage = new Stage();
            ProdutosView produtosView = new ProdutosView(clienteSelecionado);
            try {
                produtosView.start(produtosStage);
                // Fecha a tela atual
                ((Stage) btnSelecionar.getScene().getWindow()).close();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao abrir tela de produtos");
                alert.setContentText("Ocorreu um erro ao tentar abrir a tela de produtos: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void novoCliente() {
        // Abre a tela de cadastro de cliente
        Stage cadastroStage = new Stage();
        ClienteManagementView cadastroView = new ClienteManagementView();
        try {
            cadastroView.start(cadastroStage);
            cadastroStage.setOnHidden(e -> carregarClientes()); // Recarrega a lista quando a tela de cadastro for fechada
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao abrir tela de cadastro");
            alert.setContentText("Ocorreu um erro ao tentar abrir a tela de cadastro: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
