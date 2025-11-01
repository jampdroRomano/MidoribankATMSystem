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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

public class EnviarEmailRecuperacaoController {

    @FXML
    private TextField emailField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button entrarButton;
    @FXML
    private ImageView btnVoltarLogin;

    private RecuperacaoSenhaService recuperacaoService;

    @FXML
    public void initialize() {
        this.recuperacaoService = new RecuperacaoSenhaService();

        AnimationUtils.setupButtonHoverEffects(entrarButton);
        AnimationUtils.setupNodeHoverEffects(btnVoltarLogin);
    }

    @FXML
    private void handleEnviarCodigo() {
        String email = emailField.getText();

        if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
            errorLabel.setText("Formato de e-mail inválido.");
            AnimationUtils.errorAnimation(emailField);
            return;
        }

        SessionManager.clearRecuperacao();
        SessionManager.setEmailRecuperacao(email);

        LoadingUtils.runWithLoading("Enviando código...", () -> {
            recuperacaoService.iniciarRecuperacao(email);

            Platform.runLater(() -> {
                try {
                    App.setRoot("VerificarCodigoEmail");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @FXML
    private void handleVoltar() throws IOException {
        App.setRoot("Login");
    }

    private void exibirMensagemInfo(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}