package com.midoribank.atm;

import java.io.IOException;
import java.util.Random; // Importe o Random
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
    
    // --- MUDANÇA: Labels que vamos preencher ---
    @FXML private Label numeroCartao;
    @FXML private Label nomeCartao;
    @FXML private Label cvvCartao;
    // --- Fim da Mudança ---

    @FXML
    public void initialize() {
        System.out.println("CadastroCartaoController inicializado.");

        // --- MUDANÇA: Preenche os dados na tela ---
        // 1. Busca o nome salvo na etapa anterior
        String nomeUsuario = SessionManager.getCadastroNome();
        if (nomeUsuario != null && !nomeUsuario.isEmpty()) {
            nomeCartao.setText(nomeUsuario.toUpperCase());
        } else {
            nomeCartao.setText("NOME DO CLIENTE"); // Texto padrão
        }
        
        // 2. Gera dados aleatórios para o cartão
        Random rand = new Random();
        String numAleatorio = String.format("%04d %04d %04d %04d", 
            rand.nextInt(10000), 
            rand.nextInt(10000), 
            rand.nextInt(10000), 
            rand.nextInt(10000));
        
        String cvvAleatorio = String.format("%03d", rand.nextInt(1000));
        
        // 3. Define os textos dos labels
        numeroCartao.setText(numAleatorio);
        cvvCartao.setText(cvvAleatorio);
        // --- Fim da Mudança ---
        
        if (cadastrarCartaoButton != null) {
            cadastrarCartaoButton.setOnAction(e -> handleCadastroCartaoClick());
            setupButtonHoverEffects(cadastrarCartaoButton);
        }
        
        if (btnVoltarCadastrarCartao != null) {
            btnVoltarCadastrarCartao.setOnMouseClicked(e -> handleVoltarClick());
            setupNodeHoverEffects(btnVoltarCadastrarCartao);
        }
    }

    private void handleCadastroCartaoClick() {
        // --- MUDANÇA: Salva os dados gerados na sessão ---
        SessionManager.setCadastroCartao(numeroCartao.getText(), cvvCartao.getText());
        // --- Fim da Mudança ---

        System.out.println("Navegando para a tela de criação de senha (CadastroSenha)...");
        try {
            App.setRoot("CadastroSenha"); 
        } catch (IOException e) {
            e.printStackTrace();
            exibirMensagemErro("Não foi possível carregar a tela de senha.");
        }
    }
    
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
    
    // --- Métodos de UI (Copie e cole os métodos setup... e exibir... que você já tem) ---
    // (Omitidos aqui por brevidade, mas cole-os do arquivo anterior)
    
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