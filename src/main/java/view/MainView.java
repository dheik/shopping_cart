package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView extends Application {
    private Button btnGerenciarClientes;
    private Button btnGerenciarProdutos;
    private Button btnAcessarCarrinho;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        
        // Título
        Label lblTitulo = new Label("Shopping Cart");
        lblTitulo.getStyleClass().add("menu-title");
        
        // Botões do menu
        Button btnClientes = new Button("Gerenciar Clientes");
        btnClientes.setOnAction(e -> abrirGerenciamentoClientes());
        
        Button btnProdutos = new Button("Gerenciar Produtos");
        btnProdutos.setOnAction(e -> abrirGerenciamentoProdutos());
        
        Button btnNovaVenda = new Button("Nova Venda");
        btnNovaVenda.setOnAction(e -> abrirCarrinho());
        
        // Layout dos botões
        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.getChildren().addAll(lblTitulo, btnClientes, btnProdutos, btnNovaVenda);
        
        // Nomes dos programadores
        Label lblDevelopers = new Label("Desenvolvido por: Diogo, Victor, João Pedro e Gustavo");
        lblDevelopers.getStyleClass().add("developers");
        
        // Layout principal
        root.setCenter(menuBox);
        root.setBottom(lblDevelopers);
        BorderPane.setAlignment(lblDevelopers, Pos.CENTER_RIGHT);
        
        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Shopping Cart");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void abrirGerenciamentoClientes() {
        ClienteManagementView clienteView = new ClienteManagementView();
        Stage stage = new Stage();
        clienteView.start(stage);
    }

    private void abrirGerenciamentoProdutos() {
        ProdutoManagementView produtoView = new ProdutoManagementView();
        Stage stage = new Stage();
        produtoView.start(stage);
    }

    private void abrirCarrinho() {
        ClienteSelectionView clienteView = new ClienteSelectionView();
        Stage stage = new Stage();
        clienteView.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
} 