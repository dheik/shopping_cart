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
import model.ItemCarrinho;
import dao.DAOCarrinho;

import java.text.NumberFormat;
import java.util.Locale;

public class CarrinhoView extends Application {
    
    private TableView<ItemCarrinho> tableItens;
    private Button btnRemover;
    private Button btnAtualizar;
    private Button btnFinalizar;
    private Button btnVoltar;
    private Spinner<Integer> spinnerQuantidade;
    private Carrinho carrinho;
    private Label lblTotal;
    private Label lblTotalItens;
    
    public CarrinhoView(Carrinho carrinho) {
        this.carrinho = carrinho;
    }
    
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        
        // Título e informações do cliente
        VBox headerBox = new VBox(10);
        Label lblTitulo = new Label("Carrinho de Compras");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label lblCliente = new Label("Cliente: " + carrinho.getCliente().getNome());
        lblCliente.setStyle("-fx-font-size: 16px;");
        
        headerBox.getChildren().addAll(lblTitulo, lblCliente);
        
        // Tabela de itens do carrinho
        tableItens = new TableView<>();
        tableItens.setPlaceholder(new Label("Carrinho vazio"));
        
        TableColumn<ItemCarrinho, String> colProduto = new TableColumn<>("Produto");
        colProduto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getProduto().getNome()));
        colProduto.setPrefWidth(200);
        
        TableColumn<ItemCarrinho, Integer> colQuantidade = new TableColumn<>("Quantidade");
        colQuantidade.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(
                cellData.getValue().getQuantidade()));
        colQuantidade.setPrefWidth(100);
        
        TableColumn<ItemCarrinho, Double> colPrecoUnitario = new TableColumn<>("Preço Unitário");
        colPrecoUnitario.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(
                cellData.getValue().getPrecoUnitario()));
        colPrecoUnitario.setCellFactory(tc -> new TableCell<>() {
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
        colPrecoUnitario.setPrefWidth(120);
        
        TableColumn<ItemCarrinho, Double> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(
                cellData.getValue().getSubtotal()));
        colSubtotal.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double subtotal, boolean empty) {
                super.updateItem(subtotal, empty);
                if (empty || subtotal == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", subtotal));
                }
            }
        });
        colSubtotal.setPrefWidth(120);
        
        tableItens.getColumns().addAll(colProduto, colQuantidade, colPrecoUnitario, colSubtotal);
        
        // Painel de quantidade
        HBox quantidadeBox = new HBox(10);
        quantidadeBox.setAlignment(Pos.CENTER_LEFT);
        
        Label lblQuantidade = new Label("Nova quantidade:");
        spinnerQuantidade = new Spinner<>(1, 100, 1);
        spinnerQuantidade.setEditable(true);
        spinnerQuantidade.setPrefWidth(100);
        
        quantidadeBox.getChildren().addAll(lblQuantidade, spinnerQuantidade);
        
        // Botões de ação
        btnAtualizar = new Button("Atualizar Quantidade");
        btnAtualizar.setDisable(true);
        btnAtualizar.setPrefWidth(180);
        btnAtualizar.setOnAction(e -> atualizarQuantidade());
        
        btnRemover = new Button("Remover Item");
        btnRemover.setDisable(true);
        btnRemover.setPrefWidth(150);
        btnRemover.setOnAction(e -> removerItem());
        
        HBox acoesBox = new HBox(15, quantidadeBox, btnAtualizar, btnRemover);
        acoesBox.setAlignment(Pos.CENTER_LEFT);
        acoesBox.setPadding(new Insets(15, 0, 15, 0));
        
        // Informações de totais
        lblTotalItens = new Label("Total de itens: " + carrinho.getTotalItens());
        lblTotalItens.setStyle("-fx-font-size: 14px;");
        
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        lblTotal = new Label("Valor total: " + currencyFormat.format(carrinho.getValorTotal()));
        lblTotal.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        VBox totaisBox = new VBox(10, lblTotalItens, lblTotal);
        totaisBox.setAlignment(Pos.CENTER_RIGHT);
        
        // Botões de navegação
        btnFinalizar = new Button("Finalizar Compra");
        btnFinalizar.setPrefWidth(150);
        btnFinalizar.getStyleClass().add("btnFinalizar");
        btnFinalizar.setOnAction(e -> finalizarCompra());
        
        btnVoltar = new Button("Voltar às Compras");
        btnVoltar.setPrefWidth(150);
        btnVoltar.setOnAction(e -> voltarAsCompras(primaryStage));
        
        HBox navegacaoBox = new HBox(15, btnVoltar, btnFinalizar);
        navegacaoBox.setAlignment(Pos.CENTER_RIGHT);
        
        // Layout do rodapé
        BorderPane footerPane = new BorderPane();
        footerPane.setLeft(acoesBox);
        footerPane.setRight(new VBox(15, totaisBox, navegacaoBox));
        
        // Layout principal
        root.setTop(headerBox);
        root.setCenter(tableItens);
        root.setBottom(footerPane);
        
        // Evento de seleção na tabela
        tableItens.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean itemSelecionado = newSelection != null;
                    btnRemover.setDisable(!itemSelecionado);
                    btnAtualizar.setDisable(!itemSelecionado);
                    if (itemSelecionado) {
                        spinnerQuantidade.getValueFactory().setValue(newSelection.getQuantidade());
                    }
                });
        
        // Carrega os itens do carrinho
        atualizarTabelaItens();
        
        Scene scene = new Scene(root, 700, 500);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Shopping Cart - Carrinho de Compras");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void atualizarTabelaItens() {
        tableItens.getItems().clear();
        tableItens.getItems().addAll(carrinho.getItens());
        
        // Atualiza os totais
        lblTotalItens.setText("Total de itens: " + carrinho.getTotalItens());
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        lblTotal.setText("Valor total: " + currencyFormat.format(carrinho.getValorTotal()));
        
        // Desabilita os botões se o carrinho estiver vazio
        boolean carrinhoVazio = carrinho.getItens().isEmpty();
        btnFinalizar.setDisable(carrinhoVazio);
    }
    
    private void atualizarQuantidade() {
        ItemCarrinho itemSelecionado = tableItens.getSelectionModel().getSelectedItem();
        if (itemSelecionado != null) {
            int novaQuantidade = spinnerQuantidade.getValue();
            
            // Verifica se a quantidade é válida
            if (novaQuantidade <= 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Quantidade Inválida");
                alert.setHeaderText("Quantidade deve ser maior que zero");
                alert.setContentText("Para remover o item, use o botão 'Remover Item'.");
                alert.showAndWait();
                return;
            }
            
            // Verifica se há estoque suficiente
            if (novaQuantidade > itemSelecionado.getProduto().getEstoque()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Estoque Insuficiente");
                alert.setHeaderText("Estoque insuficiente");
                alert.setContentText("Estoque disponível: " + itemSelecionado.getProduto().getEstoque());
                alert.showAndWait();
                return;
            }
            
            // Atualiza a quantidade
            carrinho.atualizarQuantidade(itemSelecionado.getProduto().getId(), novaQuantidade);
            
            // Atualiza a tabela
            atualizarTabelaItens();
        }
    }
    
    private void removerItem() {
        ItemCarrinho itemSelecionado = tableItens.getSelectionModel().getSelectedItem();
        if (itemSelecionado != null) {
            // Confirma a remoção
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Remoção");
            confirmacao.setHeaderText("Remover item do carrinho");
            confirmacao.setContentText("Deseja remover " + itemSelecionado.getProduto().getNome() + " do carrinho?");
            
            confirmacao.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Remove o item
                    carrinho.removerItem(itemSelecionado.getProduto().getId());
                    
                    // Atualiza a tabela
                    atualizarTabelaItens();
                }
            });
        }
    }
    
    private void finalizarCompra() {
        // Confirma a finalização
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Finalizar Compra");
        confirmacao.setHeaderText("Confirmar finalização da compra");
        confirmacao.setContentText("Deseja finalizar a compra no valor total de R$ " 
                + String.format("%.2f", carrinho.getValorTotal()) + "?");
        
        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Finaliza a compra
                carrinho.finalizarCompra();
                
                // Salva o carrinho no banco de dados
                DAOCarrinho.salvarCarrinho(carrinho);
                
                // Exibe confirmação
                Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
                sucesso.setTitle("Compra Finalizada");
                sucesso.setHeaderText("Compra finalizada com sucesso!");
                sucesso.setContentText("Obrigado por comprar conosco!");
                
                sucesso.showAndWait().ifPresent(r -> {
                    // Fecha a janela do carrinho
                    ((Stage) btnFinalizar.getScene().getWindow()).close();
                });
            }
        });
    }
    
    private void voltarAsCompras(Stage currentStage) {
        currentStage.close();
    }
    
    public static void main(String[] args) {
        //Este método não será usado diretamente, pois esta view será iniciada a partir da tela de produtos
        launch(args);
    }
}
