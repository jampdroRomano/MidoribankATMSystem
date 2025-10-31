package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import com.midoribank.atm.services.SessionManager;
import com.midoribank.atm.models.UserProfile;
import com.midoribank.atm.utils.AnimationUtils;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class HomeController {

    @FXML
    private Label labelNomeUsuario;
    @FXML
    private Label labelNumeroConta;
    @FXML
    private Pane paneSacar;
    @FXML
    private Pane paneDepositar;
    @FXML
    private Pane paneEncerrar;
    @FXML
    private Pane paneTransferir;
    @FXML
    private Pane paneExtrato;
    @FXML
    private Pane paneCartao;
    @FXML
    private Pane paneDetalhes;

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
            labelNomeUsuario.setText(currentUser.getNome());
            labelNumeroConta.setText(currentUser.getNumeroConta());
        }
    }

    private void configurarEventos() {
        paneSacar.setOnMouseClicked(e -> {
            AnimationUtils.buttonClickAnimation(paneSacar);
            AnimationUtils.pulse(paneSacar);
            abrirTelaSaque();
        });
        
        paneDepositar.setOnMouseClicked(e -> {
            AnimationUtils.buttonClickAnimation(paneDepositar);
            AnimationUtils.pulse(paneDepositar);
            abrirTelaDeposito();
        });
        
        paneEncerrar.setOnMouseClicked(e -> {
            AnimationUtils.buttonClickAnimation(paneEncerrar);
            handleEncerrar();
        });

        paneTransferir.setOnMouseClicked(e -> {
            AnimationUtils.shake(paneTransferir);
            showInDevelopmentAlert();
        });
        
        paneExtrato.setOnMouseClicked(e -> {
            AnimationUtils.shake(paneExtrato);
            showInDevelopmentAlert();
        });
        
        paneCartao.setOnMouseClicked(e -> {
            AnimationUtils.shake(paneCartao);
            showInDevelopmentAlert();
        });
        
        paneDetalhes.setOnMouseClicked(e -> {
            AnimationUtils.shake(paneDetalhes);
            showInDevelopmentAlert();
        });

        setupPaneHoverEffects(paneSacar);
        setupPaneHoverEffects(paneDepositar);
        setupPaneHoverEffects(paneEncerrar);
        setupPaneHoverEffects(paneTransferir);
        setupPaneHoverEffects(paneExtrato);
        setupPaneHoverEffects(paneCartao);
        setupPaneHoverEffects(paneDetalhes);
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

    private void abrirTelaSaque() {
        try {
            App.setRoot("sacar");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirTelaDeposito() {
        try {
            App.setRoot("depositar");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEncerrar() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Saída");
        alert.setHeaderText("Você está prestes a encerrar a sessão.");
        alert.setContentText("Deseja realmente sair?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Encerrando a aplicação...");
            Platform.exit();
        }
    }

    private void showInDevelopmentAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText("Esta função ainda está em desenvolvimento.");
        alert.showAndWait();
    }

    private void iniciarAnimacoes() {
        Node[] menuPanes = {paneSacar, paneDepositar, paneTransferir, paneExtrato, paneCartao, paneDetalhes};
        AnimationUtils.staggeredFadeIn(menuPanes, 80, 500);
        
        AnimationUtils.slideInFromLeft(labelNomeUsuario, 600);
        AnimationUtils.slideInFromLeft(labelNumeroConta, 700);
        AnimationUtils.fadeIn(paneEncerrar, 800);
    }
}
