package com.midoribank.atm;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent; 

public class CadastroUsuarioController {

    @FXML private TextField nomeField;
    @FXML private TextField emailFieldCadastro;
    @FXML private PasswordField senhaFieldCadastro;
    @FXML private PasswordField confirmeSenha;
    @FXML private Button cadastrarButton;
    @FXML private ImageView btnVoltarCadastrar;

    @FXML
    public void initialize() {
        // ... (código do initialize permanece o mesmo) ...
        if (cadastrarButton != null) {
            cadastrarButton.setOnAction(e -> handleCadastroClick());
            setupButtonHoverEffects(cadastrarButton); 
        } else {
            System.err.println("Aviso: cadastrarButton não encontrado no FXML.");
        }

        if (btnVoltarCadastrar != null) {
            btnVoltarCadastrar.setOnMouseClicked(e -> handleVoltarClick());
            setupNodeHoverEffects(btnVoltarCadastrar); 
        } else {
            System.err.println("Aviso: btnVoltarCadastrar não encontrado no FXML.");
        }
    }

    private void handleCadastroClick() {
        String nome = nomeField.getText();
        String email = emailFieldCadastro.getText();
        String senha = senhaFieldCadastro.getText();
        String confirmacaoSenha = confirmeSenha.getText();

        // 1. Validação de campos vazios
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmacaoSenha.isEmpty()) {
            exibirMensagemErro("Preencha todos os campos!");
            return;
        }

        // 2. Validação de senha
        if (!senha.equals(confirmacaoSenha)) {
            exibirMensagemErro("As senhas não conferem!");
            senhaFieldCadastro.clear();
            confirmeSenha.clear();
            return;
        }

        // --- MUDANÇA: Salvar dados na Sessão ---
        System.out.println("Validação de cadastro OK. Salvando dados na sessão...");
        SessionManager.setCadastroUsuario(nome, email, senha);
        // --- Fim da Mudança ---

        try {
            // Navega para a tela de cadastro de cartão
            App.setRoot("CadastroCartao");
        } catch (IOException e) {
            e.printStackTrace();
            exibirMensagemErro("Não foi possível carregar a tela de cadastro de cartão.");
        }
    }

    @FXML
    private void handleVoltarClick() {
        try {
            System.out.println("Botão Voltar (Cadastro) clicado!");
            App.setRoot("Login"); // Volta para a tela de Login
        } catch (IOException e) {
            System.err.println("Falha ao carregar Login.fxml!");
            e.printStackTrace();
        }
    }

    // --- Métodos de UI (Copie e cole os métodos setup... e exibir... que você já tem) ---

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
        alert.setTitle("Erro no Cadastro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}