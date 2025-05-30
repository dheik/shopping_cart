package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Produto;
import application.ProdutoApplication;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.ComboBoxTableCell;

public class ProdutoManagementView extends Application {
    private TableView<Produto> tableProdutos;
    private TextField txtNome, txtDescricao, txtPreco, txtEstoque, txtCodigoBarras;
    private ComboBox<String> cmbCategoria;
    private Button btnAdicionar, btnEditar, btnExcluir;
    private ProdutoApplication produtoApplication;
    private Produto produtoSelecionado;
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

    @Override
    public void start(Stage primaryStage) {
        produtoApplication = new ProdutoApplication();
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        
        // Título
        Label lblTitulo = new Label("Gerenciamento de Produtos");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Formulário
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));
        
        formGrid.add(new Label("Nome:"), 0, 0);
        txtNome = new TextField();
        formGrid.add(txtNome, 1, 0);
        
        formGrid.add(new Label("Descrição:"), 0, 1);
        txtDescricao = new TextField();
        formGrid.add(txtDescricao, 1, 1);
        
        formGrid.add(new Label("Preço:"), 0, 2);
        txtPreco = new TextField();
        formGrid.add(txtPreco, 1, 2);
        
        formGrid.add(new Label("Estoque:"), 0, 3);
        txtEstoque = new TextField();
        formGrid.add(txtEstoque, 1, 3);
        
        formGrid.add(new Label("Categoria:"), 0, 4);
        cmbCategoria = new ComboBox<>(categorias);
        cmbCategoria.setPrefWidth(200);
        formGrid.add(cmbCategoria, 1, 4);
        
        formGrid.add(new Label("Código de Barras:"), 0, 5);
        txtCodigoBarras = new TextField();
        formGrid.add(txtCodigoBarras, 1, 5);
        
        // Botões
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        btnAdicionar = new Button("Adicionar");
        btnEditar = new Button("Editar");
        btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().add("btnExcluir");
        
        buttonBox.getChildren().addAll(btnAdicionar, btnEditar, btnExcluir);
        
        // Tabela
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
        colDescricao.setPrefWidth(200);
        
        TableColumn<Produto, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(
                cellData.getValue().getPreco()).asObject());
        colPreco.setPrefWidth(100);
        
        TableColumn<Produto, Integer> colEstoque = new TableColumn<>("Estoque");
        colEstoque.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(
                cellData.getValue().getEstoque()).asObject());
        colEstoque.setPrefWidth(100);
        
        TableColumn<Produto, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getCategoria()));
        colCategoria.setCellFactory(ComboBoxTableCell.forTableColumn(categorias));
        colCategoria.setEditable(true);
        colCategoria.setOnEditCommit(event -> {
            Produto produto = event.getRowValue();
            produto.setCategoria(event.getNewValue());
            try {
                produtoApplication.atualizar(produto);
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
        
        // Layout
        VBox topBox = new VBox(20, lblTitulo, formGrid, buttonBox);
        
        root.setTop(topBox);
        root.setCenter(tableProdutos);
        
        // Eventos
        btnAdicionar.setOnAction(e -> adicionarProduto());
        btnEditar.setOnAction(e -> editarProduto());
        btnExcluir.setOnAction(e -> excluirProduto());
        
        tableProdutos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        produtoSelecionado = newSelection;
                        preencherFormulario(produtoSelecionado);
                    }
                });
        
        // Carrega os produtos
        carregarProdutos();
        
        tableProdutos.setEditable(true);
        
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Shopping Cart - Gerenciamento de Produtos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void carregarProdutos() {
        List<Produto> produtos = produtoApplication.listarTodos();
        tableProdutos.getItems().clear();
        tableProdutos.getItems().addAll(produtos);
    }

    private void preencherFormulario(Produto produto) {
        txtNome.setText(produto.getNome());
        txtDescricao.setText(produto.getDescricao());
        txtPreco.setText(String.valueOf(produto.getPreco()));
        txtEstoque.setText(String.valueOf(produto.getEstoque()));
        cmbCategoria.setValue(produto.getCategoria());
        txtCodigoBarras.setText(produto.getCodigoBarras());
    }

    private void limparFormulario() {
        txtNome.clear();
        txtDescricao.clear();
        txtPreco.clear();
        txtEstoque.clear();
        cmbCategoria.setValue(null);
        txtCodigoBarras.clear();
        produtoSelecionado = null;
        tableProdutos.getSelectionModel().clearSelection();
    }

    private void adicionarProduto() {
        try {
            Produto produto = new Produto();
            produto.setNome(txtNome.getText());
            produto.setDescricao(txtDescricao.getText());
            produto.setPreco(Double.parseDouble(txtPreco.getText()));
            produto.setEstoque(Integer.parseInt(txtEstoque.getText()));
            produto.setCategoria(cmbCategoria.getValue());
            produto.setCodigoBarras(txtCodigoBarras.getText());

            produtoApplication.salvar(produto);
            limparFormulario();
            carregarProdutos();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Produto adicionado");
            alert.setContentText("Produto adicionado com sucesso!");
            alert.showAndWait();
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro de formato");
            alert.setContentText("Por favor, insira valores numéricos válidos para preço e estoque.");
            alert.showAndWait();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao adicionar produto");
            alert.setContentText("Erro ao adicionar produto: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void editarProduto() {
        if (produtoSelecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhum produto selecionado");
            alert.setContentText("Por favor, selecione um produto para editar.");
            alert.showAndWait();
            return;
        }

        try {
            produtoSelecionado.setNome(txtNome.getText());
            produtoSelecionado.setDescricao(txtDescricao.getText());
            produtoSelecionado.setPreco(Double.parseDouble(txtPreco.getText()));
            produtoSelecionado.setEstoque(Integer.parseInt(txtEstoque.getText()));
            produtoSelecionado.setCategoria(cmbCategoria.getValue());
            produtoSelecionado.setCodigoBarras(txtCodigoBarras.getText());

            produtoApplication.atualizar(produtoSelecionado);
            limparFormulario();
            carregarProdutos();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Produto atualizado");
            alert.setContentText("Produto atualizado com sucesso!");
            alert.showAndWait();
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro de formato");
            alert.setContentText("Por favor, insira valores numéricos válidos para preço e estoque.");
            alert.showAndWait();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao atualizar produto");
            alert.setContentText("Erro ao atualizar produto: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void excluirProduto() {
        if (produtoSelecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Nenhum produto selecionado");
            alert.setContentText("Por favor, selecione um produto para excluir.");
            alert.showAndWait();
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText("Excluir produto");
        confirmacao.setContentText("Tem certeza que deseja excluir este produto?");
        
        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            try {
                produtoApplication.excluir((long) produtoSelecionado.getId());
                limparFormulario();
                carregarProdutos();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText("Produto excluído");
                alert.setContentText("Produto excluído com sucesso!");
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao excluir produto");
                alert.setContentText("Erro ao excluir produto: " + ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 