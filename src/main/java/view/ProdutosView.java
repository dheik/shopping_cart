package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Cliente;
import model.Produto;
import model.Carrinho;
import dao.DAOProduto;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.ComboBoxTableCell;

public class ProdutosView extends Application {
    
    private TableView<Produto> tableProdutos;
    private Button btnAdicionar;
    private Button btnVerCarrinho;
    private Spinner<Integer> spinnerQuantidade;
    private Cliente cliente;
    private Carrinho carrinho;
    private ObservableList<String> categorias = FXCollections.observableArrayList(
        "Eletrônicos",
        "Roupas",
        "Alimentos",
        "Livros",
        "Móveis",
        "Brinquedos",
        "Esportes",
        "Beleza",
        "Outros"
    );
    
    public ProdutosView(Cliente cliente) {
        this.cliente = cliente;
        this.carrinho = new Carrinho();
        this.carrinho.setCliente(cliente);
    }
    
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        
        // Título e informações do cliente
        VBox headerBox = new VBox(10);
        Label lblTitulo = new Label("Selecione os Produtos");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label lblCliente = new Label("Cliente: " + cliente.getNome());
        lblCliente.setStyle("-fx-font-size: 16px;");
        
        headerBox.getChildren().addAll(lblTitulo, lblCliente);
        
        // Tabela de produtos
        tableProdutos = new TableView<>();
        tableProdutos.setPlaceholder(new Label("Nenhum produto cadastrado"));
        
        TableColumn<Produto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(
                cellData.getValue().getId()).asObject());
        colId.setPrefWidth(50);
        
        TableColumn<Produto, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getNome()));
        colNome.setPrefWidth(200);
        
        TableColumn<Produto, String> colDescricao = new TableColumn<>("Descrição");
        colDescricao.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDescricao()));
        colDescricao.setPrefWidth(250);
        
        TableColumn<Produto, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(
                cellData.getValue().getPreco()).asObject());
        colPreco.setCellFactory(tc -> new TableCell<Produto, Double>() {
            @Override
            protected void updateItem(Double preco, boolean empty) {
                super.updateItem(preco, empty);
                if (empty || preco == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", preco));
                }
            }
        });
        colPreco.setPrefWidth(100);
        
        TableColumn<Produto, Integer> colEstoque = new TableColumn<>("Estoque");
        colEstoque.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(
                cellData.getValue().getEstoque()).asObject());
        colEstoque.setPrefWidth(80);
        
        TableColumn<Produto, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getCategoria()));
        colCategoria.setCellFactory(ComboBoxTableCell.forTableColumn(categorias));
        colCategoria.setEditable(true);
        colCategoria.setOnEditCommit(event -> {
            Produto produto = event.getRowValue();
            produto.setCategoria(event.getNewValue());
            try {
                DAOProduto.atualizarProduto(produto);
                carregarProdutos();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao atualizar categoria");
                alert.setContentText("Erro ao atualizar categoria: " + ex.getMessage());
                alert.showAndWait();
            }
        });
        colCategoria.setPrefWidth(120);
        
        TableColumn<Produto, String> colCodigoBarras = new TableColumn<>("Código de Barras");
        colCodigoBarras.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getCodigoBarras()));
        colCodigoBarras.setPrefWidth(150);
        
        tableProdutos.getColumns().addAll(colId, colNome, colDescricao, colPreco, colEstoque, colCategoria, colCodigoBarras);

        // Painel de quantidade
        HBox quantidadeBox = new HBox(10);
        quantidadeBox.setAlignment(Pos.CENTER_LEFT);
        
        Label lblQuantidade = new Label("Quantidade:");
        spinnerQuantidade = new Spinner<>(1, 100, 1);
        spinnerQuantidade.setEditable(true);
        spinnerQuantidade.setPrefWidth(100);
        
        quantidadeBox.getChildren().addAll(lblQuantidade, spinnerQuantidade);
        
        // Botões
        btnAdicionar = new Button("Adicionar ao Carrinho");
        btnAdicionar.setDisable(true);
        btnAdicionar.setPrefWidth(180);
        btnAdicionar.setOnAction(e -> adicionarAoCarrinho());
        
        btnVerCarrinho = new Button("Ver Carrinho");
        btnVerCarrinho.setPrefWidth(150);
        btnVerCarrinho.setOnAction(e -> verCarrinho());
        
        Label lblTotalItens = new Label("Total de itens: 0");
        
        HBox botoesBox = new HBox(15, quantidadeBox, btnAdicionar, lblTotalItens, btnVerCarrinho);
        botoesBox.setAlignment(Pos.CENTER);
        botoesBox.setPadding(new Insets(15, 0, 0, 0));
        
        // Barra de pesquisa
        TextField txtPesquisa = new TextField();
        txtPesquisa.setPromptText("Pesquisar produto...");
        txtPesquisa.setPrefWidth(300);
        txtPesquisa.textProperty().addListener((obs, oldVal, newVal) -> {
            pesquisarProdutos(newVal);
        });
        
        HBox pesquisaBox = new HBox(10, new Label("Pesquisar:"), txtPesquisa);
        pesquisaBox.setAlignment(Pos.CENTER_LEFT);
        pesquisaBox.setPadding(new Insets(10, 0, 10, 0));
        
        // Layout
        VBox topBox = new VBox(10, headerBox, pesquisaBox);
        
        root.setTop(topBox);
        root.setCenter(tableProdutos);
        root.setBottom(botoesBox);
        
        // Evento de seleção na tabela
        tableProdutos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    btnAdicionar.setDisable(newSelection == null);
                });
        
        // Atualiza o contador de itens quando o carrinho muda
        carrinho.getItens().addListener((javafx.collections.ListChangeListener.Change<? extends model.ItemCarrinho> c) -> {
            lblTotalItens.setText("Total de itens: " + carrinho.getTotalItens());
        });
        
        // Carrega os produtos
        carregarProdutos();
        
        tableProdutos.setEditable(true);
        
        Scene scene = new Scene(root, 700, 500);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Shopping Cart - Seleção de Produtos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void carregarProdutos() {
        List<Produto> produtos = DAOProduto.listarProdutos();
        tableProdutos.getItems().clear();
        tableProdutos.getItems().addAll(produtos);
    }
    
    private void pesquisarProdutos(String termo) {
        if (termo == null || termo.isEmpty()) {
            carregarProdutos();
        } else {
            List<Produto> produtosFiltrados = DAOProduto.pesquisarProdutos(termo);
            tableProdutos.getItems().clear();
            tableProdutos.getItems().addAll(produtosFiltrados);
        }
    }
    
    private void adicionarAoCarrinho() {
        Produto produtoSelecionado = tableProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            int quantidade = spinnerQuantidade.getValue();
            
            // Verifica se a quantidade é válida
            if (quantidade <= 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Quantidade Inválida");
                alert.setHeaderText("Quantidade deve ser maior que zero");
                alert.setContentText("Por favor, selecione uma quantidade válida.");
                alert.showAndWait();
                return;
            }
            
            // Verifica se há estoque suficiente
            if (quantidade > produtoSelecionado.getEstoque()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Estoque Insuficiente");
                alert.setHeaderText("Estoque insuficiente");
                alert.setContentText("Estoque disponível: " + produtoSelecionado.getEstoque());
                alert.showAndWait();
                return;
            }
            
            // Adiciona ao carrinho
            carrinho.adicionarItem(produtoSelecionado, quantidade);
            
            // Exibe confirmação
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Produto Adicionado");
            alert.setHeaderText("Produto adicionado ao carrinho");
            alert.setContentText(quantidade + "x " + produtoSelecionado.getNome() + " adicionado(s) ao carrinho.");
            alert.showAndWait();
        }
    }
    
    private void verCarrinho() {
        // Abre a tela do carrinho
        Stage carrinhoStage = new Stage();
        CarrinhoView carrinhoView = new CarrinhoView(carrinho);
        try {
            carrinhoView.start(carrinhoStage);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao abrir carrinho");
            alert.setContentText("Ocorreu um erro ao tentar abrir o carrinho: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    public static void main(String[] args) {
        // Este método não será usado diretamente, pois esta view será iniciada a partir da seleção de cliente
        launch(args);
    }
}
