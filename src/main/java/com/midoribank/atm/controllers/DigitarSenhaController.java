package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import com.midoribank.atm.services.SessionManager;
import com.midoribank.atm.models.UserProfile;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;
import com.midoribank.atm.dao.ContaDAO;

public class DigitarSenhaController {

    @FXML private PasswordField senhaField;
    @FXML private Pane button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    @FXML private Node paneConfirmar;
    @FXML private Node paneVoltar;
    @FXML private Pane buttonApagar;
    @FXML private Pane buttonC;

    private UserProfile currentUser;
    private final int MAX_SENHA_LENGTH = 4;

    private ContaDAO contaDAO;

    @FXML
    public void initialize() {
        this.currentUser = SessionManager.getCurrentUser();

        this.contaDAO = new ContaDAO();

        if (currentUser == null) {
            try { App.setRoot("Login"); } catch (IOException e) { e.printStackTrace(); }
            return;
        }
        configurarBotoesNumericos();
        configurarControles();
        configurarBotoesEdicao();
    }

    private void configurarBotoesNumericos() {
        Pane[] panes = {button0, button1, button2, button3, button4, button5, button6, button7, button8, button9};
        for (int i = 0; i < panes.length; i++) {
            Pane pane = panes[i];
            if (pane != null) {
                final String numero = String.valueOf(i);
                pane.setOnMouseClicked(e -> adicionarDigito(numero));
                setupNodeHoverEffects(pane);
            } else {
                System.err.println("Aviso: Um painel numérico (button" + i + ") não foi encontrado no FXML.");
            }
        }
    }

    private void configurarControles() {
        if (paneConfirmar != null) {
            paneConfirmar.setOnMouseClicked(e -> handleConfirmarSenha());
            setupNodeHoverEffects(paneConfirmar);
        } else {
            System.err.println("Aviso: paneConfirmar não encontrado no FXML.");
        }

        if (paneVoltar != null) {
            paneVoltar.setOnMouseClicked(e -> handleVoltar());
            setupNodeHoverEffects(paneVoltar);
        } else {
            System.err.println("Aviso: paneVoltar não encontrado no FXML.");
        }
    }

    private void configurarBotoesEdicao() {
        if (buttonApagar != null) {
            buttonApagar.setOnMouseClicked(e -> apagarDigito());
            setupNodeHoverEffects(buttonApagar);
        } else {
            System.err.println("Aviso: buttonApagar não encontrado no FXML.");
        }

        if (buttonC != null) {
            buttonC.setOnMouseClicked(e -> limparSenha());
            setupNodeHoverEffects(buttonC);
        } else {
            System.err.println("Aviso: buttonC não encontrado no FXML.");
        }
    }

    private void adicionarDigito(String digito) {
        if (senhaField.getText().length() < MAX_SENHA_LENGTH) {
            senhaField.appendText(digito);
        }
    }

    private void apagarDigito() {
        String currentText = senhaField.getText();
        if (!currentText.isEmpty()) {
            senhaField.setText(currentText.substring(0, currentText.length() - 1));
        }
    }

    private void limparSenha() {
        senhaField.clear();
    }

    private void handleVoltar() {
        try {
            App.setRoot("confirmar-operacao");
        } catch (IOException e) {
            e.printStackTrace();
            exibirMensagemErro("Não foi possível voltar para a tela anterior.");
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

    private void handleConfirmarSenha() {
        String senhaDigitada = senhaField.getText();

        if (senhaDigitada.length() != MAX_SENHA_LENGTH) {
            exibirMensagemErro("A senha do cartão deve ter " + MAX_SENHA_LENGTH + " dígitos.");
            return;
        }

        if (currentUser.validarSenhaCartao(senhaDigitada)) {
            executarOperacao();
        } else {
            exibirMensagemErro("Senha do cartão incorreta!");
            limparSenha();
        }
    }

    private void executarOperacao() {

        String tipo = SessionManager.getCurrentTransactionType();
        double valor = SessionManager.getCurrentTransactionAmount();

        double saldoAtual = currentUser.getSaldo();
        String numeroConta = currentUser.getNumeroConta();

        double novoSaldo;
        boolean sucessoNoBanco = false;

        if ("Saque".equals(tipo)) {
            if (valor <= 0 || valor > saldoAtual) {
                exibirMensagemErro("Saldo insuficiente. Operação cancelada.");
                SessionManager.clearTransaction();
                try { App.setRoot("home"); } catch (IOException e) { e.printStackTrace(); }
                return;
            }
            novoSaldo = saldoAtual - valor;

        } else if ("Deposito".equals(tipo)) {

             if (valor <= 0) {
                exibirMensagemErro("Valor de depósito deve ser positivo.");
                SessionManager.clearTransaction();
                try { App.setRoot("home"); } catch (IOException e) { e.printStackTrace(); }
                return;
            }
            novoSaldo = saldoAtual + valor;

        } else {
            exibirMensagemErro("Tipo de operação desconhecido: " + tipo);
            return;
        }

        sucessoNoBanco = this.contaDAO.atualizarSaldo(numeroConta, novoSaldo);

        if (sucessoNoBanco) {

            currentUser.setSaldo(novoSaldo);

            try {
                App.setRoot("ConclusaoOperacao");
            } catch (IOException e) {
                e.printStackTrace();
                exibirMensagemErro("Não foi possível carregar a tela de conclusão.");
            }
        } else {
            exibirMensagemErro("Falha de comunicação com o banco. A operação foi cancelada. Tente mais tarde.");
            SessionManager.clearTransaction();
            try { App.setRoot("home"); } catch (IOException e) { e.printStackTrace(); }
        }
    }
}
