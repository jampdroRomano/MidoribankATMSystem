package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import com.midoribank.atm.dao.UserDAO;
import com.midoribank.atm.services.SessionManager;
import com.midoribank.atm.models.UserProfile;
import com.midoribank.atm.utils.AnimationUtils;
import com.midoribank.atm.utils.LoadingUtils;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
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

    private UserDAO userDAO;

    @FXML
    private Label esqueciSenhaLabel;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    @FXML
    public void initialize() {
        System.out.println("LoginController inicializado.");

        entrarButton.setOnAction(event -> {
            System.out.println("Botão 'Entrar' foi clicado!");
            AnimationUtils.buttonClickAnimation(entrarButton);
            autenticar();
        });

        cadastrarButton.setOnAction(event -> {
            AnimationUtils.buttonClickAnimation(cadastrarButton);
            handleAbrirCadastro();
        });

        setupButtonHoverEffects(entrarButton);
        setupButtonHoverEffects(cadastrarButton);

        if (btnVoltarLogin != null) {
             setupNodeHoverEffects(btnVoltarLogin);
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
    	
    	iniciarAnimacoes();
    }

    @FXML
    private void handleEsqueciMinhaSenha() throws IOException {
        System.out.println("Iniciando fluxo de recuperação de senha...");
        App.setRoot("EnviarEmailRecuperacao");
    }

    @FXML
    private void handleVoltarClick() {
        try {
            System.out.println("Botão Voltar clicado!");
            App.setRoot("opcoesLogin");
        } catch (IOException e) {
            System.err.println("Falha ao carregar opcoesLogin.fxml!");
            e.printStackTrace();
            exibirMensagemErro("Não foi possível voltar para a tela anterior.");
        }
    }

    private void handleAbrirCadastro() {
        try {
            System.out.println("Botão 'Cadastrar' clicado! Abrindo tela CadastroUsuario...");
            App.setRoot("CadastroUsuario");
        } catch (IOException e) {
            System.err.println("Falha ao carregar CadastroUsuario.fxml!");
            e.printStackTrace();
            exibirMensagemErro("Não foi possível abrir a tela de cadastro.");
        }
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

     private void setupNodeHoverEffects(Node node) {
         if (node != null) {
             ColorAdjust hoverEffect = new ColorAdjust(0, 0, -0.1, 0);
             ColorAdjust clickEffect = new ColorAdjust(0, 0, -0.25, 0);

             node.setOnMouseEntered(e -> {
                 if (node.getScene() != null) node.getScene().setCursor(Cursor.HAND);
                 node.setEffect(hoverEffect);
             });

             node.setOnMouseExited(e -> {
                 if (node.getScene() != null) node.getScene().setCursor(Cursor.DEFAULT);
                 node.setEffect(null);
             });

             node.setOnMousePressed(e -> node.setEffect(clickEffect));

             node.setOnMouseReleased(e -> {
                 if (node.isHover()) node.setEffect(hoverEffect);
                 else node.setEffect(null);
             });
         }
     }

    private void autenticar() {
        System.out.println("Método autenticar() iniciado...");

        String email = emailField.getText();
        String senha = senhaField.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            System.out.println("Erro: Campos de email ou senha vazios.");
            AnimationUtils.shake(emailField);
            AnimationUtils.shake(senhaField);
            exibirMensagemErro("Preencha todos os campos!");
            return;
        }

        System.out.println("Autenticando com email: " + email);
        
        LoadingUtils.runWithLoading("Autenticando...", () -> {
            boolean autentica = userDAO.autenticar(email, senha);

            javafx.application.Platform.runLater(() -> {
                if (autentica) {
                    System.out.println("Autenticação bem-sucedida!");
                    AnimationUtils.successAnimation(entrarButton);

                    UserProfile userProfile = userDAO.getProfile(email);
                    SessionManager.setCurrentUser(userProfile);

                    try {
                        Thread.sleep(300);
                        System.out.println("Carregando tela 'home'...");
                        App.setRoot("home");
                    } catch(InterruptedException | IOException e) {
                        System.err.println("Falha ao carregar home.fxml!");
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Autenticação falhou para o email: " + email);
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

    private void showInDevelopmentAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText("Esta função ainda está em desenvolvimento.");
        alert.showAndWait();
    }

    private void iniciarAnimacoes() {
        AnimationUtils.fadeIn(entrarButton, 700);
        AnimationUtils.fadeIn(cadastrarButton, 800);
        if (esqueciSenhaLabel != null) {
            AnimationUtils.fadeIn(esqueciSenhaLabel, 900);
        }
    }

}
