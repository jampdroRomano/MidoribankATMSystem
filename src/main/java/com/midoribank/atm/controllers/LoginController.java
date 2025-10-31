package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import com.midoribank.atm.dao.UserDAO;
import com.midoribank.atm.models.UserProfile;
import com.midoribank.atm.services.SessionManager;
import com.midoribank.atm.utils.AnimationUtils;
import com.midoribank.atm.utils.LoadingUtils;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField senhaField;
    @FXML
    private Button cadastrarButton;
    @FXML
    private Button entrarButton;
    @FXML
    private ImageView btnVoltarLogin;
    @FXML
    private Label esqueciSenhaLabel;

    private UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    @FXML
    public void initialize() {
        System.out.println("LoginController inicializado.");

        entrarButton.setOnAction(event -> autenticar());
        cadastrarButton.setOnAction(event -> handleAbrirCadastro());

        AnimationUtils.setupButtonHoverEffects(entrarButton);
        AnimationUtils.setupButtonHoverEffects(cadastrarButton);

        if (btnVoltarLogin != null) {
            AnimationUtils.setupNodeHoverEffects(btnVoltarLogin);
        } else {
            System.err.println("Aviso: btnVoltarLogin não encontrado no FXML.");
        }

        if (esqueciSenhaLabel != null) {
            esqueciSenhaLabel.setOnMouseEntered(e -> {
                if (esqueciSenhaLabel.getScene() != null) {
                    esqueciSenhaLabel.getScene().setCursor(Cursor.HAND);
                }
            });

            esqueciSenhaLabel.setOnMouseExited(e -> {
                if (esqueciSenhaLabel.getScene() != null) {
                    esqueciSenhaLabel.getScene().setCursor(Cursor.DEFAULT);
                }
            });
        }
    }

    @FXML
    private void handleEsqueciMinhaSenha() throws IOException {
        App.setRoot("EnviarEmailRecuperacao");
    }

    @FXML
    private void handleVoltarClick() {
        try {
            App.setRoot("opcoesLogin");
        } catch (IOException e) {
            System.err.println("Falha ao carregar opcoesLogin.fxml!");
            e.printStackTrace();
            exibirMensagemErro("Não foi possível voltar para a tela anterior.");
        }
    }

    private void handleAbrirCadastro() {
        try {
            App.setRoot("CadastroUsuario");
        } catch (IOException e) {
            System.err.println("Falha ao carregar CadastroUsuario.fxml!");
            e.printStackTrace();
            exibirMensagemErro("Não foi possível abrir a tela de cadastro.");
        }
    }

    private void autenticar() {
        String email = emailField.getText();
        String senha = senhaField.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            exibirMensagemErro("Preencha todos os campos!");
            AnimationUtils.errorAnimation(emailField);
            AnimationUtils.errorAnimation(senhaField);
            return;
        }

        LoadingUtils.runWithLoading("Autenticando...", () -> {
            boolean autentica = userDAO.autenticar(email, senha);

            Platform.runLater(() -> {
                if (autentica) {
                    AnimationUtils.successAnimation(entrarButton);
                    UserProfile userProfile = userDAO.getProfile(email);
                    SessionManager.setCurrentUser(userProfile);

                    try {
                        Thread.sleep(300);
                        App.setRoot("home");
                    } catch (InterruptedException | IOException e) {
                        System.err.println("Falha ao carregar home.fxml!");
                        e.printStackTrace();
                    }
                } else {
                    AnimationUtils.errorAnimation(emailField);
                    AnimationUtils.errorAnimation(senhaField);
                    exibirMensagemErro("Email ou senha inválidos.");
                }
            });
        });
    }

    private void exibirMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}