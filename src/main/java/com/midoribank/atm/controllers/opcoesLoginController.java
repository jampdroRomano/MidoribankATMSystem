package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import com.midoribank.atm.utils.AnimationUtils;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class opcoesLoginController {

    @FXML
    private Pane entrar_conta;

    @FXML
    private Pane recarga_cartao;

    @FXML
    private Pane recarga_celular;

    @FXML
    public void initialize() {
        recarga_cartao.setOnMouseClicked(e -> showInDevelopmentAlert());
        recarga_celular.setOnMouseClicked(e -> showInDevelopmentAlert());

        AnimationUtils.setupNodeHoverEffects(entrar_conta);
        AnimationUtils.setupNodeHoverEffects(recarga_cartao);
        AnimationUtils.setupNodeHoverEffects(recarga_celular);
    }

    @FXML
    private void handleEntrarComContaClick(MouseEvent event) {
        try {
            App.setRoot("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showInDevelopmentAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText("Esta função ainda está em desenvolvimento.");
        alert.showAndWait();
    }
}