package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import com.midoribank.atm.services.SessionManager;
import com.midoribank.atm.utils.AnimationUtils;
import com.midoribank.atm.utils.LoadingUtils;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;

public class CadastroSenhaController {

    @FXML private PasswordField senhaField;
    @FXML private Pane button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    @FXML private Node paneConfirmar;
    @FXML private Node paneVoltar;
    @FXML private Pane buttonApagar;
    @FXML private Pane buttonC;
    @FXML private Label labelTitulo;

    private final int MAX_SENHA_LENGTH = 4;
    private String senhaCadastroTemporaria = null;

    @FXML
    public void initialize() {
        if (labelTitulo != null) {
            labelTitulo.setText("Digite uma senha para o seu cartão");
            labelTitulo.setLayoutX(320.0);
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
                AnimationUtils.setupNodeHoverEffects(pane);
            }
        }
    }

    private void configurarControles() {
        if (paneConfirmar != null) {
            paneConfirmar.setOnMouseClicked(e -> handleConfirmarSenha());
            AnimationUtils.setupNodeHoverEffects(paneConfirmar);
        }
        if (paneVoltar != null) {
            paneVoltar.setOnMouseClicked(e -> handleVoltarClick());
            AnimationUtils.setupNodeHoverEffects(paneVoltar);
        }
    }

    private void configurarBotoesEdicao() {
        if (buttonApagar != null) {
            buttonApagar.setOnMouseClicked(e -> apagarDigito());
            AnimationUtils.setupNodeHoverEffects(buttonApagar);
        }
        if (buttonC != null) {
            buttonC.setOnMouseClicked(e -> limparSenha());
            AnimationUtils.setupNodeHoverEffects(buttonC);
        }
    }

    @FXML
    private void handleVoltarClick() {
        try {
            App.setRoot("CadastroCartao");
        } catch (IOException e) {
            System.err.println("Falha ao carregar CadastroCartao.fxml!");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleConfirmarSenha() {
        String senhaDigitada = senhaField.getText();

        if (senhaCadastroTemporaria == null) {
            if (senhaDigitada.length() != MAX_SENHA_LENGTH) {
                exibirMensagemErro("A senha deve ter " + MAX_SENHA_LENGTH + " dígitos.");
                return;
            }
            this.senhaCadastroTemporaria = senhaDigitada;
            labelTitulo.setText("Digite novamente a senha");
            labelTitulo.setLayoutX(340.0);
            limparSenha();

        } else {
            if (senhaDigitada.equals(this.senhaCadastroTemporaria)) {
                SessionManager.setCadastroSenhaCartao(senhaDigitada);

                LoadingUtils.runWithLoading("Finalizando cadastro...", () -> {
                    boolean sucesso = SessionManager.salvarCadastroCompletoNoBanco();

                    Platform.runLater(() -> {
                        if(sucesso) {
                            exibirMensagemInfo("Sucesso", "Cadastro realizado! Faça o login.");
                            try {
                                App.setRoot("Login");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            exibirMensagemErro("Falha crítica no cadastro. Tente novamente mais tarde.");
                        }
                    });
                });

            } else {
                exibirMensagemErro("As senhas não conferem! Tente novamente.");
                this.senhaCadastroTemporaria = null;
                labelTitulo.setText("Digite uma senha para o seu cartão");
                labelTitulo.setLayoutX(320.0);
                limparSenha();
            }
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

    private void exibirMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirMensagemInfo(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}