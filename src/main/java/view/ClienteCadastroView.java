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

public class ClienteCadastroView extends Application {
    
    private TextField txtNome;
    private TextField txtCpf;
    private TextField txtEmail;
    private TextField txtTelefone;
    private TextField txtEndereco;
    private Button btnSalvar;
    private Button btnCancelar;
    
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        
        // Título
        Label lblTitulo = new Label("Cadastro de Cliente");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Formulário
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(15);
        form.setPadding(new Insets(20, 0, 20, 0));
        
        // Nome
        Label lblNome = new Label("Nome:");
        txtNome = new TextField();
        txtNome.setPromptText("Nome completo");
        form.add(lblNome, 0, 0);
        form.add(txtNome, 1, 0);
        
        // CPF
        Label lblCpf = new Label("CPF:");
        txtCpf = new TextField();
        txtCpf.setPromptText("000.000.000-00");
        form.add(lblCpf, 0, 1);
        form.add(txtCpf, 1, 1);
        
        // Email
        Label lblEmail = new Label("Email:");
        txtEmail = new TextField();
        txtEmail.setPromptText("exemplo@email.com");
        form.add(lblEmail, 0, 2);
        form.add(txtEmail, 1, 2);

        // Telefone
        Label lblTelefone = new Label("Telefone:");
        txtTelefone = new TextField();
        txtTelefone.setPromptText("(00) 00000-0000");
        form.add(lblTelefone, 0, 3);
        form.add(txtTelefone, 1, 3);

        // Endereço
        Label lblEndereco = new Label("Endereço:");
        txtEndereco = new TextField();
        txtEndereco.setPromptText("Rua, número, bairro, cidade, estado");
        form.add(lblEndereco, 0, 4);
        form.add(txtEndereco, 1, 4);

        // Ajusta largura dos campos
        txtNome.setPrefWidth(300);
        txtCpf.setPrefWidth(300);
        txtEmail.setPrefWidth(300);
        txtTelefone.setPrefWidth(300);
        txtEndereco.setPrefWidth(300);
        
        // Botões
        btnSalvar = new Button("Salvar");
        btnSalvar.setPrefWidth(100);
        btnSalvar.setOnAction(e -> salvarCliente(primaryStage));
        
        btnCancelar = new Button("Cancelar");
        btnCancelar.setPrefWidth(100);
        btnCancelar.setOnAction(e -> primaryStage.close());
        
        HBox botoesBox = new HBox(10, btnSalvar, btnCancelar);
        botoesBox.setAlignment(Pos.CENTER_RIGHT);
        
        // Layout
        VBox conteudo = new VBox(20, lblTitulo, form, botoesBox);
        root.setCenter(conteudo);
        
        Scene scene = new Scene(root, 500, 400);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Shopping Cart - Cadastro de Cliente");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void salvarCliente(Stage stage) {
        // Validação básica
        if (txtNome.getText().isEmpty() || txtCpf.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos Obrigatórios");
            alert.setHeaderText("Preencha os campos obrigatórios");
            alert.setContentText("Nome e CPF são campos obrigatórios.");
            alert.showAndWait();
            return;
        }
        
        // Cria o cliente
        Cliente cliente = new Cliente();
        cliente.setNome(txtNome.getText());
        cliente.setCpf(txtCpf.getText());
        cliente.setEmail(txtEmail.getText());
        cliente.setTelefone(txtTelefone.getText());
        cliente.setEndereco(txtEndereco.getText());

        
        // Salva no banco de dados
        try {
            DAOCliente.inserirCliente(cliente);
            
            // Exibe confirmação
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cliente Cadastrado");
            alert.setHeaderText("Cliente cadastrado com sucesso!");
            alert.setContentText("O cliente " + cliente.getNome() + " foi cadastrado com sucesso.");
            alert.showAndWait();
            
            // Fecha a janela
            stage.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao cadastrar cliente");
            alert.setContentText("Ocorreu um erro ao cadastrar o cliente: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
