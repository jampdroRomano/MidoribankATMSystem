package com.midoribank.atm;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;

public class ConclusaoOperacaoController {

    @FXML private Label labelMensagemSucesso;
    @FXML private Label labelPergunta;
    @FXML private Label labelTituloOperacao;
    @FXML private Label labelSaldoAtual;
    @FXML private Label labelNumeroConta;
    @FXML private Node paneSim; 
    @FXML private Node paneNao; 

    private UserProfile currentUser;
    private String tipoOperacao;

    @FXML
    public void initialize() {
        this.currentUser = SessionManager.getCurrentUser();
        this.tipoOperacao = SessionManager.getCurrentTransactionType();

        if (currentUser == null || tipoOperacao == null) {
            try { App.setRoot("home"); } catch (IOException e) { e.printStackTrace(); }
            return;
        }

        configurarTela();
        configurarEventos();
    }

    private void configurarTela() {
        labelTituloOperacao.setText(tipoOperacao); 
        labelMensagemSucesso.setText(tipoOperacao + " concluído com sucesso!");
        labelPergunta.setText("Deseja realizar outro " + tipoOperacao.toLowerCase() + "?");

        labelNumeroConta.setText(currentUser.getNumeroConta());
        labelSaldoAtual.setText(String.format("R$ %.2f", currentUser.getSaldo()));
    }

    private void configurarEventos() {
        if (paneSim != null) {
            paneSim.setOnMouseClicked(e -> handleSim());
            setupNodeHoverEffects(paneSim);
        } else {
             System.err.println("Aviso: paneSim não encontrado no FXML.");
        }
        
        if (paneNao != null) {
            paneNao.setOnMouseClicked(e -> handleNao());
            setupNodeHoverEffects(paneNao);
        } else {
             System.err.println("Aviso: paneNao não encontrado no FXML.");
        }
    }

    private void handleSim() {
        SessionManager.clearTransaction(); 
        try { 
            if ("Saque".equals(tipoOperacao)) {
                App.setRoot("sacar");
            } else if ("Deposito".equals(tipoOperacao)) {
                App.setRoot("depositar");
            }
            else {
                 App.setRoot("home"); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleNao() {
        SessionManager.clearTransaction(); 
        try {
            App.setRoot("home"); 
        } catch (IOException e) {
            e.printStackTrace();
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
}

