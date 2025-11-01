package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import com.midoribank.atm.services.RecuperacaoSenhaService;
import com.midoribank.atm.services.SessionManager;
import com.midoribank.atm.utils.AnimationUtils;
import com.midoribank.atm.utils.LoadingUtils;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;

public class AlterarSenhaController {

    @FXML
    private PasswordField novaSenhaField;
    @FXML
    private PasswordField confirmarSenhaField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button redefinirButton;
    @FXML
    private ImageView btnVoltar;

    private RecuperacaoSenhaService recuperacaoService;

    @FXML
    public void initialize() {
        this.recuperacaoService = new RecuperacaoSenhaService();
        AnimationUtils.setupButtonHoverEffects(redefinirButton);
        AnimationUtils.setupNodeHoverEffects(btnVoltar);
    }

    @FXML
    private void handleRedefinirSenha() {
        String novaSenha = novaSenhaField.getText();
        String confirmarSenha = confirmarSenhaField.getText();
        String email = SessionManager.getEmailRecuperacao();
        String codigoVerificado = SessionManager.getCodigoVerificado();

        if (email == null || codigoVerificado == null) {
            exibirMensagemErro("Sessão inválida. Por favor, reinicie o processo de recuperação.");
            try {
                App.setRoot("Login");
            } catch (IOException e) { e.printStackTrace(); }
            return;
        }

        if (novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
            errorLabel.setText("Por favor, preencha ambos os campos.");
            AnimationUtils.errorAnimation(novaSenhaField);
            AnimationUtils.errorAnimation(confirmarSenhaField);
            return;
        }

        if (novaSenha.length() < 6) {
            errorLabel.setText("A senha deve ter pelo menos 6 caracteres.");
            AnimationUtils.errorAnimation(novaSenhaField);
            return;
        }

        if (!novaSenha.equals(confirmarSenha)) {
            errorLabel.setText("As senhas não conferem.");
            AnimationUtils.errorAnimation(novaSenhaField);
            AnimationUtils.errorAnimation(confirmarSenhaField);
            return;
        }

        LoadingUtils.runWithLoading("Atualizando senha...", () -> {
            boolean sucesso = recuperacaoService.redefinirSenha(email, novaSenha);

            Platform.runLater(() -> {
                if (sucesso) {
                    exibirMensagemInfo("Sucesso", "Sua senha foi redefinida. Por favor, faça o login.");
                    SessionManager.clearRecuperacao();
                    try {
                        App.setRoot("Login");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    exibirMensagemErro("Ocorreu um erro ao atualizar sua senha. Tente novamente.");
                }
            });
        });
    }

    @FXML
    private void handleVoltar() throws IOException {
        SessionManager.clearRecuperacao();
        App.setRoot("Login");
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