package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import com.midoribank.atm.dao.ContaDAO;
import com.midoribank.atm.models.UserProfile;
import com.midoribank.atm.services.SessionManager;
import com.midoribank.atm.utils.AnimationUtils;
import com.midoribank.atm.utils.LoadingUtils;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class TransferenciaController {

    @FXML private Pane rootPane;
    @FXML private TextField fieldAgencia;
    @FXML private TextField fieldDigitoAgencia;
    @FXML private TextField fieldConta;
    @FXML private TextField fieldDigitoConta;

    @FXML private Pane paneContinuar;
    @FXML private Pane paneVoltar;

    @FXML private Label errorLabelAgencia;
    @FXML private Label errorLabelDigitoAgencia;
    @FXML private Label errorLabelConta;
    @FXML private Label errorLabelDigitoConta;
    @FXML private Label labelErroGeral;

    @FXML private Label labelNumeroConta;
    @FXML private Label labelSaldoAtual;

    private ContaDAO contaDAO;
    private UserProfile currentUser;

    @FXML
    public void initialize() {
        this.contaDAO = new ContaDAO();
        this.currentUser = SessionManager.getCurrentUser();

        carregarDadosUsuario();
        limparErros();
        configurarEventos();
    }

    private void carregarDadosUsuario() {
        if (currentUser != null) {
            labelNumeroConta.setText(currentUser.getNumeroConta());
            labelSaldoAtual.setText(String.format("R$ %.2f", currentUser.getSaldo()));
        }
    }

    private void configurarEventos() {
        paneVoltar.setOnMouseClicked(e -> handleVoltar());
        paneContinuar.setOnMouseClicked(e -> handleContinuar());
        AnimationUtils.setupNodeHoverEffects(paneVoltar);
        AnimationUtils.setupNodeHoverEffects(paneContinuar);
    }

    private void limparErros() {
        errorLabelAgencia.setVisible(false);
        errorLabelDigitoAgencia.setVisible(false);
        errorLabelConta.setVisible(false);
        errorLabelDigitoConta.setVisible(false);
        labelErroGeral.setVisible(false);
    }

    private boolean validarCampos() {
        limparErros();
        boolean valido = true;

        if (fieldAgencia.getText().trim().isEmpty()) {
            errorLabelAgencia.setText("Agência é obrigatória.");
            errorLabelAgencia.setVisible(true);
            valido = false;
        }
        if (fieldDigitoAgencia.getText().trim().isEmpty()) {
            errorLabelDigitoAgencia.setText("Dígito é obrigatório.");
            errorLabelDigitoAgencia.setVisible(true);
            valido = false;
        }
        if (fieldConta.getText().trim().isEmpty()) {
            errorLabelConta.setText("Conta é obrigatória.");
            errorLabelConta.setVisible(true);
            valido = false;
        }
        if (fieldDigitoConta.getText().trim().isEmpty()) {
            errorLabelDigitoConta.setText("Dígito é obrigatório.");
            errorLabelDigitoConta.setVisible(true);
            valido = false;
        }

        return valido;
    }

    private void handleVoltar() {
        try {
            App.setRoot("home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleContinuar() {
        if (!validarCampos()) {
            return;
        }

        String agencia = fieldAgencia.getText().trim() + "-" + fieldDigitoAgencia.getText().trim();
        String conta = fieldConta.getText().trim() + "-" + fieldDigitoConta.getText().trim();

        if (agencia.equals(currentUser.getAgencia()) && conta.equals(currentUser.getNumeroConta())) {
            exibirErroGeral("Você não pode transferir para a sua própria conta.");
            return;
        }

        LoadingUtils.runWithLoading("Verificando conta...", () -> {
            return contaDAO.getProfileByConta(agencia, conta);
        }).thenAccept(contaDestino -> {
            if (contaDestino != null) {
                SessionManager.setContaDestino(contaDestino);
                try {
                    App.setRoot("ConfirmarContaDestino");
                } catch (IOException e) {
                    e.printStackTrace();
                    exibirErroGeral("Falha ao carregar a próxima tela.");
                }
            } else {
                exibirErroGeral("Conta de destino não encontrada.");
            }
        });
    }

    private void exibirErroGeral(String mensagem) {
        labelErroGeral.setText(mensagem);
        labelErroGeral.setVisible(true);
    }
}