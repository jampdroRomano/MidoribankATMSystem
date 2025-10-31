package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import com.midoribank.atm.services.SessionManager;
import com.midoribank.atm.utils.AnimationUtils;
import java.io.IOException;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class CadastroCartaoController {

    @FXML private Button cadastrarCartaoButton;
    @FXML private ImageView btnVoltarCadastrarCartao;
    @FXML private Label numeroCartao;
    @FXML private Label nomeCartao;
    @FXML private Label cvvCartao;

    @FXML
    public void initialize() {
        String nomeUsuario = SessionManager.getCadastroNome();
        if (nomeUsuario != null && !nomeUsuario.isEmpty()) {
            nomeCartao.setText(nomeUsuario.toUpperCase());
        } else {
            nomeCartao.setText("NOME DO CLIENTE");
        }

        Random rand = new Random();
        String numAleatorio = String.format("%04d %04d %04d %04d",
                rand.nextInt(10000),
                rand.nextInt(10000),
                rand.nextInt(10000),
                rand.nextInt(10000));

        String cvvAleatorio = String.format("%03d", rand.nextInt(1000));

        numeroCartao.setText(numAleatorio);
        cvvCartao.setText(cvvAleatorio);

        if (cadastrarCartaoButton != null) {
            cadastrarCartaoButton.setOnAction(e -> handleCadastroCartaoClick());
            AnimationUtils.setupButtonHoverEffects(cadastrarCartaoButton);
        }

        if (btnVoltarCadastrarCartao != null) {
            btnVoltarCadastrarCartao.setOnMouseClicked(e -> handleVoltarClick());
            AnimationUtils.setupNodeHoverEffects(btnVoltarCadastrarCartao);
        }
    }

    private void handleCadastroCartaoClick() {
        SessionManager.setCadastroCartao(numeroCartao.getText(), cvvCartao.getText());

        try {
            App.setRoot("CadastroSenha");
        } catch (IOException e) {
            e.printStackTrace();
            exibirMensagemErro("Não foi possível carregar a tela de senha.");
        }
    }

    @FXML
    private void handleVoltarClick() {
        try {
            App.setRoot("CadastroUsuario");
        } catch (IOException e) {
            System.err.println("Falha ao carregar CadastroUsuario.fxml!");
            e.printStackTrace();
        }
    }

    private void exibirMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}