package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class VerificarCodigoEmailController {

    @FXML
    private TextField codigoField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleVerificarCodigo() throws IOException {
        String codigo = codigoField.getText();

        String codigoCorreto = "123456";

        if (codigo.isEmpty()) {
            errorLabel.setText("Por favor, digite o código de 6 dígitos.");
            return;
        }

        if (codigo.length() != 6) {
             errorLabel.setText("O código deve ter 6 dígitos.");
             return;
        }

        if (codigo.equals(codigoCorreto)) {
            System.out.println("SIMULAÇÃO: Código correto.");

            App.setRoot("AlterarSenha");
        } else {
            errorLabel.setText("Código inválido ou expirado. Tente novamente.");
        }
    }

    @FXML
    private void handleVoltar() throws IOException {

        App.setRoot("EnviarEmailRecuperacao");
    }
}
