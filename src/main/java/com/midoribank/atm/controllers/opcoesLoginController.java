package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.effect.ColorAdjust;
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

        setupPaneHoverEffects(entrar_conta);
        setupPaneHoverEffects(recarga_cartao);
        setupPaneHoverEffects(recarga_celular);
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

    @FXML
    private void handleEntrarComContaClick(MouseEvent event) {
        System.out.println("Botão 'Entrar com a conta' clicado!");

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
