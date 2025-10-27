package com.midoribank.atm;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField senhaField;
    @FXML
    private Button cadastrarButton;
    @FXML
    private Button entrarButton;

    private UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    @FXML
    public void initialize() {
        System.out.println("LoginController inicializado.");
        
        entrarButton.setOnAction(event -> {
            System.out.println("Botão 'Entrar' foi clicado!");
            autenticar();
        });

        cadastrarButton.setOnAction(event -> showInDevelopmentAlert());

        setupButtonHoverEffects(entrarButton);
        setupButtonHoverEffects(cadastrarButton);
    }
    
    private void setupButtonHoverEffects(Button button) {
        if (button != null) {
            ColorAdjust hoverEffect = new ColorAdjust(0, 0, -0.1, 0);
            ColorAdjust clickEffect = new ColorAdjust(0, 0, -0.25, 0);

            button.setOnMouseEntered(e -> {
                if (button.getScene() != null) button.getScene().setCursor(Cursor.HAND);
                button.setEffect(hoverEffect);
            });

            button.setOnMouseExited(e -> {
                if (button.getScene() != null) button.getScene().setCursor(Cursor.DEFAULT);
                button.setEffect(null);
            });
            
            button.setOnMousePressed(e -> {
                button.setEffect(clickEffect);
            });
            
            button.setOnMouseReleased(e -> {
                if (button.isHover()) {
                    button.setEffect(hoverEffect);
                } else {
                    button.setEffect(null);
                }
            });
        }
    }

    private void autenticar() {
        System.out.println("Método autenticar() iniciado...");
        
        String email = emailField.getText();
        String senha = senhaField.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            System.out.println("Erro: Campos de email ou senha vazios.");
            exibirMensagemErro("Preencha todos os campos!");
            return;
        }

        System.out.println("Autenticando com email: " + email);
        boolean autentica = userDAO.autenticar(email, senha);

        if (autentica) {
            System.out.println("Autenticação bem-sucedida!");
            
            UserProfile userProfile = userDAO.getProfile(email);
            SessionManager.setCurrentUser(userProfile);
            
            try {
                System.out.println("Carregando tela 'home'...");
                App.setRoot("home");
            } catch(IOException e) {
                System.err.println("Falha ao carregar home.fxml!");
                e.printStackTrace();
            }
        } else {
            System.out.println("Autenticação falhou para o email: " + email);
            exibirMensagemErro("Email ou senha inválidos.");
        }
    }

    private void exibirMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    private void showInDevelopmentAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText("Esta função ainda está em desenvolvimento.");
        alert.showAndWait();
    }
}

