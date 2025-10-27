package com.midoribank.atm;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class ConfirmarDadosController {

    @FXML private Label labelNome;
    @FXML private Label labelAgencia;
    @FXML private Label labelConta;
    @FXML private Label labelTipo;
    @FXML private Label labelValor;
    @FXML private Label labelData;
    @FXML private Pane paneConfirmar; // Certifique-se que o fx:id no FXML é este
    @FXML private Pane paneVoltar;

    private UserProfile currentUser;
    private double valorOperacao;
    private String tipoOperacao;

    @FXML
    public void initialize() {
        this.currentUser = SessionManager.getCurrentUser();
        this.valorOperacao = SessionManager.getCurrentTransactionAmount();
        this.tipoOperacao = SessionManager.getCurrentTransactionType();

        carregarDados();
        configurarEventos();
    }

    private void carregarDados() {
        if (currentUser != null) {
            labelNome.setText(currentUser.getNome());
            labelAgencia.setText(currentUser.getAgencia());
            labelConta.setText(currentUser.getNumeroConta());
        }

        labelTipo.setText(tipoOperacao != null ? tipoOperacao : "N/D");
        labelValor.setText(String.format("R$ %.2f", valorOperacao));

        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        labelData.setText(hoje.format(formatador));
    }

    private void configurarEventos() {
        if (paneConfirmar != null) {
             paneConfirmar.setOnMouseClicked(e -> handleContinuarParaSenha()); // Ação modificada
             setupPaneHoverEffects(paneConfirmar);
        }
       if (paneVoltar != null) {
            paneVoltar.setOnMouseClicked(e -> handleVoltar());
             setupPaneHoverEffects(paneVoltar);
       }
    }

    // Navega para a tela de digitar senha
    private void handleContinuarParaSenha() {
        try {
            // Garanta que o nome do FXML está correto (sem extensão .fxml)
            App.setRoot("DigitarSenha"); 
        } catch (IOException e) {
            e.printStackTrace();
            exibirMensagemErro("Não foi possível carregar a tela de senha.");
        }
    }

    // Volta para a tela anterior (Saque ou Depósito)
    private void handleVoltar() {
        try {
             String telaAnterior = "home"; // Default
             if ("Saque".equals(tipoOperacao)) {
                 telaAnterior = "sacar";
             } else if ("Deposito".equals(tipoOperacao)) { // Previsão para depósito
                 telaAnterior = "depositar";
             }
             // Adicionar mais "else if" para outras operações futuras
             
             // Garanta que os nomes dos FXMLs estão corretos
             App.setRoot(telaAnterior); 
        } catch (IOException e) {
            e.printStackTrace();
             exibirMensagemErro("Não foi possível voltar para a tela anterior.");
        }
    }

    private void setupPaneHoverEffects(Pane pane) {
        if (pane != null) {
            ColorAdjust hoverEffect = new ColorAdjust(0, 0, -0.1, 0);
            ColorAdjust clickEffect = new ColorAdjust(0, 0, -0.25, 0);

            pane.setOnMouseEntered(e -> {
                if (pane.getScene() != null) pane.getScene().setCursor(Cursor.HAND);
                pane.setEffect(hoverEffect);
            });

            pane.setOnMouseExited(e -> {
                if (pane.getScene() != null) pane.getScene().setCursor(Cursor.DEFAULT);
                pane.setEffect(null);
            });

            pane.setOnMousePressed(e -> pane.setEffect(clickEffect));

            pane.setOnMouseReleased(e -> {
                if (pane.isHover()) {
                    pane.setEffect(hoverEffect);
                } else {
                    pane.setEffect(null);
                }
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

