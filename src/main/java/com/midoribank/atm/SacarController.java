package com.midoribank.atm;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class SacarController {

    @FXML private Label labelSaldoAtual;
    @FXML private Label labelNumeroConta;
    @FXML private TextField valorField;
    @FXML private Pane paneVinte;
    @FXML private Pane paneCinquenta;
    @FXML private Pane paneCem;
    @FXML private Pane paneDuzentos;
    @FXML private Pane paneContinuar;
    @FXML private Pane paneCancelar;
    @FXML private Pane paneApagar;
    @FXML private Pane paneLimpar;
    
    private UserProfile currentUser;

    @FXML
    public void initialize() {
        this.currentUser = SessionManager.getCurrentUser();
        carregarDadosUsuario();
        configurarEventos();
    }

    private void carregarDadosUsuario() {
        if (currentUser != null) {
            labelNumeroConta.setText(currentUser.getNumeroConta());
            labelSaldoAtual.setText(String.format("R$ %.2f", currentUser.getSaldo()));
        }
    }
    
    private void configurarEventos() {
        paneVinte.setOnMouseClicked(e -> adicionarValor(20.0));
        paneCinquenta.setOnMouseClicked(e -> adicionarValor(50.0));
        paneCem.setOnMouseClicked(e -> adicionarValor(100.0));
        paneDuzentos.setOnMouseClicked(e -> adicionarValor(200.0));
        
        paneApagar.setOnMouseClicked(e -> handleApagar());
        paneLimpar.setOnMouseClicked(e -> valorField.clear());
        paneContinuar.setOnMouseClicked(e -> handleContinuar());
        paneCancelar.setOnMouseClicked(e -> handleCancelar());
        
        setupPaneHoverEffects(paneVinte);
        setupPaneHoverEffects(paneCinquenta);
        setupPaneHoverEffects(paneCem);
        setupPaneHoverEffects(paneDuzentos);
        setupPaneHoverEffects(paneContinuar);
        setupPaneHoverEffects(paneCancelar);
        setupPaneHoverEffects(paneApagar);
        setupPaneHoverEffects(paneLimpar);
    }
    
        private void adicionarValor(double valor) {
        String valorAtualTexto = valorField.getText();
        try {
            double valorAtual = valorAtualTexto.isEmpty() ? 0 : Double.parseDouble(valorAtualTexto);
            valorField.setText(String.valueOf(valorAtual + valor));
        } catch (NumberFormatException e) {
            valorField.setText(String.valueOf(valor));
        }
    }
    
    private void handleContinuar() {
        String valorTexto = valorField.getText().replace(",", "."); 
        if (valorTexto.isEmpty()) {
            exibirMensagemErro("Por favor, insira um valor para sacar.");
            return;
        }

        try {
            double valor = Double.parseDouble(valorTexto);
            if (valor <= 0) {
                exibirMensagemErro("O valor do saque deve ser positivo.");
                return;
            }
            if (valor > currentUser.getSaldo()) {
                exibirMensagemErro("Saldo insuficiente para realizar o saque.");
                return;
            }

            SessionManager.setCurrentTransaction(valor, "Saque");
            App.setRoot("confirmar-operacao");

        } catch (NumberFormatException e) {
            exibirMensagemErro("Valor inválido. Por favor, insira apenas números.");
        } catch (IOException e) {
            e.printStackTrace();
            exibirMensagemErro("Não foi possível carregar a tela de confirmação.");
        }
    }
    
        private void handleApagar() {
        String texto = valorField.getText();
        if (texto != null && !texto.isEmpty()) {
            valorField.setText(texto.substring(0, texto.length() - 1));
        }
    }

    private void handleCancelar() {
        try {
            App.setRoot("home");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void exibirMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro na Operação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    private void exibirMensagemInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

