package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import com.midoribank.atm.services.SessionManager;
import com.midoribank.atm.models.UserProfile;
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
    @FXML private Pane paneConfirmar;
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
             paneConfirmar.setOnMouseClicked(e -> handleContinuarParaSenha());
             setupPaneHoverEffects(paneConfirmar);
        }
       if (paneVoltar != null) {
            paneVoltar.setOnMouseClicked(e -> handleVoltar());
             setupPaneHoverEffects(paneVoltar);
       }
    }

    private void handleContinuarParaSenha() {
        try {
            App.setRoot("DigitarSenha");
        } catch (IOException e) {
            e.printStackTrace();
            exibirMensagemErro("Não foi possível carregar a tela de senha.");
        }
    }

    private void handleVoltar() {
        try {
             String telaAnterior = "home";
             if ("Saque".equals(tipoOperacao)) {
                 telaAnterior = "sacar";
             } else if ("Deposito".equals(tipoOperacao)) {
                 telaAnterior = "depositar";
             }

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
