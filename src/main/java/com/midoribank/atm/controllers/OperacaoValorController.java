package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import com.midoribank.atm.models.UserProfile;
import com.midoribank.atm.services.SessionManager;
import com.midoribank.atm.utils.AnimationUtils;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class OperacaoValorController {

    @FXML private Label labelTituloOperacao;
    @FXML private Label labelSaldoAtual;
    @FXML private Label labelNumeroConta;
    @FXML private TextField valorField;
    @FXML private Pane paneVinte;
    @FXML private Pane paneCinquenta;
    @FXML private Pane paneCem;
    @FXML private Pane paneDuzentos;
    @FXML private Pane paneContinuar;
    @FXML private Pane paneCancelar;
    @FXML private Pane paneApagar;
    @FXML private Pane paneLimpar;

    private UserProfile currentUser;
    private String tipoOperacao;

    @FXML
    public void initialize() {
        this.currentUser = SessionManager.getCurrentUser();
        this.tipoOperacao = SessionManager.getOperacaoContext();

        carregarDadosUsuario();
        configurarEventos();
    }

    private void carregarDadosUsuario() {
        if (currentUser != null) {
            labelNumeroConta.setText(currentUser.getNumeroConta());
            labelSaldoAtual.setText(String.format("R$ %.2f", currentUser.getSaldo()));
        }

        if (tipoOperacao != null) {
            labelTituloOperacao.setText(tipoOperacao);
        }
    }

    private void configurarEventos() {
        paneVinte.setOnMouseClicked(e -> adicionarValor(20.0));
        paneCinquenta.setOnMouseClicked(e -> adicionarValor(50.0));
        paneCem.setOnMouseClicked(e -> adicionarValor(100.0));
        paneDuzentos.setOnMouseClicked(e -> adicionarValor(200.0));

        paneApagar.setOnMouseClicked(e -> handleApagar());
        paneLimpar.setOnMouseClicked(e -> valorField.clear());
        paneContinuar.setOnMouseClicked(e -> handleContinuar());
        paneCancelar.setOnMouseClicked(e -> handleVoltar());

        AnimationUtils.setupNodeHoverEffects(paneVinte);
        AnimationUtils.setupNodeHoverEffects(paneCinquenta);
        AnimationUtils.setupNodeHoverEffects(paneCem);
        AnimationUtils.setupNodeHoverEffects(paneDuzentos);
        AnimationUtils.setupNodeHoverEffects(paneContinuar);
        AnimationUtils.setupNodeHoverEffects(paneCancelar);
        AnimationUtils.setupNodeHoverEffects(paneApagar);
        AnimationUtils.setupNodeHoverEffects(paneLimpar);
    }

    private void handleContinuar() {
        String valorTexto = valorField.getText().replace(",", ".");
        if (valorTexto.isEmpty()) {
            exibirMensagemErro("Por favor, insira um valor.");
            return;
        }

        try {
            double valor = Double.parseDouble(valorTexto);
            if (valor <= 0) {
                exibirMensagemErro("O valor deve ser positivo.");
                return;
            }

            if (tipoOperacao.equals("Saque") || tipoOperacao.equals("Transferencia")) {
                if (valor > currentUser.getSaldo()) {
                    exibirMensagemErro("Saldo insuficiente para realizar esta operação.");
                    return;
                }
            }

            SessionManager.setCurrentTransaction(valor, this.tipoOperacao);

            if (tipoOperacao.equals("Transferencia")) {
                App.setRoot("TelaPin");
            } else {
                App.setRoot("confirmar-operacao");
            }

        } catch (NumberFormatException e) {
            exibirMensagemErro("Valor inválido. Por favor, insira apenas números.");
        } catch (IOException e) {
            e.printStackTrace();
            exibirMensagemErro("Não foi possível carregar a próxima tela.");
        }
    }

    private void handleVoltar() {
        try {
            if (tipoOperacao.equals("Transferencia")) {
                SessionManager.clearTransferenciaData();
                App.setRoot("ConfirmarContaDestino");
            } else {
                App.setRoot("home");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adicionarValor(double valor) {
        String valorAtualTexto = valorField.getText();
        try {
            double valorAtual = valorAtualTexto.isEmpty() ? 0 : Double.parseDouble(valorAtualTexto);
            valorField.setText(String.valueOf(valorAtual + valor));
        } catch (NumberFormatException e) {
            valorField.setText(String.valueOf(valor));
        }
    }

    private void handleApagar() {
        String texto = valorField.getText();
        if (texto != null && !texto.isEmpty()) {
            valorField.setText(texto.substring(0, texto.length() - 1));
        }
    }

    private void exibirMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro na Operação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}