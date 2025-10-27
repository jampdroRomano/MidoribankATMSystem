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
        
        labelTipo.setText(tipoOperacao);
        labelValor.setText(String.format("R$ %.2f", valorOperacao));

        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        labelData.setText(hoje.format(formatador));
    }

    private void configurarEventos() {
        paneConfirmar.setOnMouseClicked(e -> handleConfirmar());
        paneVoltar.setOnMouseClicked(e -> handleVoltar());
        
        setupPaneHoverEffects(paneConfirmar);
        setupPaneHoverEffects(paneVoltar);
    }

    private void handleConfirmar() {
        boolean sucesso = currentUser.sacar(valorOperacao);

        if (sucesso) {
            exibirMensagemInfo("Saque realizado com sucesso!");
            SessionManager.clearTransaction();
            try {
                App.setRoot("home");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            exibirMensagemErro("Ocorreu um erro inesperado. Saldo pode ser insuficiente.");
        }
    }

    private void handleVoltar() {
        try {
            App.setRoot("sacar");
        } catch (IOException e) {
            e.printStackTrace();
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

    private void exibirMensagemInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
