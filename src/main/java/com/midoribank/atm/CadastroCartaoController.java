package com.midoribank.atm;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.Cursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.control.Alert;

public class CadastroCartaoController {

    @FXML private Button cadastrarCartaoButton;
    @FXML private ImageView btnVoltarCadastrarCartao;
    
    @FXML private Label numeroCartao;
    @FXML private Label nomeCartao;
    @FXML private Label cvvCartao;

    @FXML
    public void initialize() {
        System.out.println("CadastroCartaoController inicializado.");
        
        if (cadastrarCartaoButton != null) {
            // Define a ação do botão Cadastrar
            cadastrarCartaoButton.setOnAction(e -> handleCadastroCartaoClick());
            setupButtonHoverEffects(cadastrarCartaoButton);
        }
        
        if (btnVoltarCadastrarCartao != null) {
            // Define a ação do botão Voltar
            btnVoltarCadastrarCartao.setOnMouseClicked(e -> handleVoltarClick());
            setupNodeHoverEffects(btnVoltarCadastrarCartao);
        }
    }

    /**
     * Chamado ao clicar em "Cadastrar" na tela do Cartão.
     * Navega para a nova tela de CRIAÇÃO de senha.
     */
    private void handleCadastroCartaoClick() {
        System.out.println("Navegando para a tela de criação de senha (CadastroSenha)...");
        try {
            // **IMPORTANTE**: Navega para a nova tela "CadastroSenha"
            // (Isso assume que você criou a pasta e o FXML como no passo 2)
            App.setRoot("CadastroSenha"); 
        } catch (IOException e) {
            e.printStackTrace();
            exibirMensagemErro("Não foi possível carregar a tela de senha.");
        }
    }
    
    /**
     * Chamado ao clicar em "Voltar" na tela do Cartão.
     * Volta para a tela de Cadastro de Usuário.
     */
    @FXML
    private void handleVoltarClick() {
        try {
            System.out.println("Botão Voltar (Cartao) clicado!");
            App.setRoot("CadastroUsuario"); // Volta para a tela anterior
        } catch (IOException e) {
            System.err.println("Falha ao carregar CadastroUsuario.fxml!");
            e.printStackTrace();
        }
    }
    
    // --- Métodos de UI (Efeitos de Hover/Clique) ---

    private void setupButtonHoverEffects(Button button) {
        if (button != null) {
            ColorAdjust hoverEffect = new ColorAdjust(0, 0, -0.1, 0);
            ColorAdjust clickEffect = new ColorAdjust(0, 0, -0.25, 0);

            button.setOnMouseEntered(e -> {
                if (button.getScene() != null) button.getScene().setCursor(Cursor.HAND);
                button.setEffect(hoverEffect);
            });
            button.setOnMouseExited(e -> {
                if (button.getScene() != null) button.getScene().setCursor(Cursor.DEFAULT);
                button.setEffect(null);
            });
            button.setOnMousePressed(e -> button.setEffect(clickEffect));
            button.setOnMouseReleased(e -> {
                if (button.isHover()) button.setEffect(hoverEffect);
                else button.setEffect(null);
            });
        }
    }

    private void setupNodeHoverEffects(Node node) {
        if (node != null) {
            ColorAdjust hoverEffect = new ColorAdjust(0, 0, -0.1, 0);
            ColorAdjust clickEffect = new ColorAdjust(0, 0, -0.25, 0);

            node.setOnMouseEntered(e -> {
                if (node.getScene() != null) node.getScene().setCursor(Cursor.HAND);
                node.setEffect(hoverEffect);
            });
            node.setOnMouseExited(e -> {
                if (node.getScene() != null) node.getScene().setCursor(Cursor.DEFAULT);
                node.setEffect(null);
            });
            node.setOnMousePressed(e -> node.setEffect(clickEffect));
            node.setOnMouseReleased(e -> {
                if (node.isHover()) node.setEffect(hoverEffect);
                else node.setEffect(null);
            });
        }
    }
    
    private void exibirMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}