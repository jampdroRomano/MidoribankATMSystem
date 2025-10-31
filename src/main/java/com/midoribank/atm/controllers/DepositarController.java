package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import com.midoribank.atm.services.SessionManager;
import com.midoribank.atm.models.UserProfile;
import com.midoribank.atm.utils.AnimationUtils;
import com.midoribank.atm.utils.LoadingUtils;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class DepositarController {

    @FXML private Label labelSaldoAtual;
    @FXML private Label labelNumeroConta;
    @FXML private TextField valorField;
    @FXML private Pane paneVinte;
    @FXML private Pane paneCinquenta;
    @FXML private Pane paneCem;
    @FXML private Pane paneDuzentos;
    @FXML private Pane paneApagar;
    @FXML private Pane paneLimpar;
    @FXML private Pane paneContinuar;
    @FXML private Pane paneVoltar;

    private UserProfile currentUser;

    @FXML
    public void initialize() {
        this.currentUser = SessionManager.getCurrentUser();
        carregarDadosUsuario();
        configurarEventos();
        iniciarAnimacoes();
    }

    private void carregarDadosUsuario() {
        if (currentUser != null) {
            labelNumeroConta.setText(currentUser.getNumeroConta());
            labelSaldoAtual.setText(String.format("R$ %.2f", currentUser.getSaldo()));
        }
    }

    private void configurarEventos() {
        paneVinte.setOnMouseClicked(e -> {
            AnimationUtils.buttonClickAnimation(paneVinte);
            adicionarValor(20.0);
        });
        
        paneCinquenta.setOnMouseClicked(e -> {
            AnimationUtils.buttonClickAnimation(paneCinquenta);
            adicionarValor(50.0);
        });
        
        paneCem.setOnMouseClicked(e -> {
            AnimationUtils.buttonClickAnimation(paneCem);
            adicionarValor(100.0);
        });
        
        paneDuzentos.setOnMouseClicked(e -> {
            AnimationUtils.buttonClickAnimation(paneDuzentos);
            adicionarValor(200.0);
        });

        paneApagar.setOnMouseClicked(e -> {
            AnimationUtils.buttonClickAnimation(paneApagar);
            handleApagar();
        });
        
        paneLimpar.setOnMouseClicked(e -> {
            AnimationUtils.buttonClickAnimation(paneLimpar);
            AnimationUtils.shake(valorField);
            valorField.clear();
        });
        
        paneContinuar.setOnMouseClicked(e -> {
            AnimationUtils.buttonClickAnimation(paneContinuar);
            handleDeposito();
        });
        
        paneVoltar.setOnMouseClicked(e -> {
            AnimationUtils.buttonClickAnimation(paneVoltar);
            handleVoltar();
        });

        setupPaneHoverEffects(paneVinte);
        setupPaneHoverEffects(paneCinquenta);
        setupPaneHoverEffects(paneCem);
        setupPaneHoverEffects(paneDuzentos);
        setupPaneHoverEffects(paneApagar);
        setupPaneHoverEffects(paneLimpar);
        setupPaneHoverEffects(paneContinuar);
        setupPaneHoverEffects(paneVoltar);
    }

    private void adicionarValor(double valor) {
        String valorAtualTexto = valorField.getText();
        try {
            double valorAtual = valorAtualTexto.isEmpty() ? 0 : Double.parseDouble(valorAtualTexto);
            double novoValor = valorAtual + valor;
            valorField.setText(String.format("%.2f", novoValor));
        } catch (NumberFormatException e) {
            valorField.setText(String.format("%.2f", valor));
        }
    }

    private void handleApagar() {
        String texto = valorField.getText();
        if (texto != null && !texto.isEmpty()) {
            valorField.setText(texto.substring(0, texto.length() - 1));
        }
    }

    private void handleVoltar() {
        try {
            App.setRoot("home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeposito() {
        String valorTexto = valorField.getText().replace(",", ".");
        if (valorTexto.isEmpty()) {
            AnimationUtils.errorAnimation(valorField);
            exibirMensagemErro("Por favor, insira um valor para depositar.");
            return;
        }

        try {
            double valor = Double.parseDouble(valorTexto);
            if (valor <= 0) {
                AnimationUtils.errorAnimation(valorField);
                exibirMensagemErro("O valor do depósito deve ser positivo.");
                return;
            }

            if (valor % 10 != 0) {
                AnimationUtils.errorAnimation(valorField);
                exibirMensagemErro("O valor deve ser múltiplo de R$ 10,00.");
                return;
            }

            AnimationUtils.successAnimation(paneContinuar);
            SessionManager.setCurrentTransaction(valor, "Deposito");

            LoadingUtils.runWithLoading("Processando depósito...", () -> {
                javafx.application.Platform.runLater(() -> {
                    try {
                        Thread.sleep(1000);
                        App.setRoot("confirmar-operacao");
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                });
            });

        } catch (NumberFormatException e) {
            AnimationUtils.errorAnimation(valorField);
            exibirMensagemErro("Valor inválido. Por favor, insira apenas números.");
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

            pane.setOnMousePressed(e -> {
                pane.setEffect(clickEffect);
            });

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
        alert.setTitle("Erro na Operação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirMensagemInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void iniciarAnimacoes() {
        Node[] quickButtons = {paneVinte, paneCinquenta, paneCem, paneDuzentos};
        AnimationUtils.staggeredFadeIn(quickButtons, 100, 400);
        
        AnimationUtils.slideInFromTop(valorField, 500);
        AnimationUtils.slideInFromLeft(labelSaldoAtual, 600);
        AnimationUtils.slideInFromLeft(labelNumeroConta, 700);
        
        AnimationUtils.fadeIn(paneContinuar, 800);
        AnimationUtils.fadeIn(paneVoltar, 900);
        
        AnimationUtils.scaleIn(paneApagar, 500);
        AnimationUtils.scaleIn(paneLimpar, 600);
    }
}
