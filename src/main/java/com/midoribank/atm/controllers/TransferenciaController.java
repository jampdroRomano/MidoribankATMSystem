package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import com.midoribank.atm.models.UserProfile;
import com.midoribank.atm.services.SessionManager;
import com.midoribank.atm.utils.AnimationUtils;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class TransferenciaController {

    @FXML
    private Label labelNumeroConta;
    @FXML
    private Label labelSaldoAtual;
    @FXML
    private TextField agenciaField;
    @FXML
    private TextField contaField;
    @FXML
    private TextField digitoField;
    @FXML
    private TextField cpfCnpjField;
    @FXML
    private ComboBox<String> tipoContaComboBox;
    @FXML
    private Pane paneContinuar;
    @FXML
    private Pane paneVoltar;

    private UserProfile currentUser;

    @FXML
    public void initialize() {
        this.currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {
            labelNumeroConta.setText(currentUser.getNumeroConta());
            labelSaldoAtual.setText(String.format("R$ %.2f", currentUser.getSaldo()));
        }

        tipoContaComboBox.setItems(FXCollections.observableArrayList(
                "Conta Corrente",
                "Conta Poupança"
        ));

        AnimationUtils.setupNodeHoverEffects(paneContinuar);
        AnimationUtils.setupNodeHoverEffects(paneVoltar);

        paneVoltar.setOnMouseClicked(e -> handleVoltar());
    }

    @FXML
    private void handleVoltar() {
        try {
            App.setRoot("home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}