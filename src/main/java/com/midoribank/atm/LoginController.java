package com.midoribank.atm;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node; 
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

        cadastrarButton.setOnAction(event -> handleAbrirCadastro()); 

        // Aplica efeitos aos botões
        setupButtonHoverEffects(entrarButton);
        setupButtonHoverEffects(cadastrarButton); 

        // Aplica efeitos ao botão voltar (ImageView)
        if (btnVoltarLogin != null) {
             setupNodeHoverEffects(btnVoltarLogin); 
        } else {
             System.err.println("Aviso: btnVoltarLogin não encontrado no FXML.");
        }
    }

    // Método de clique do botão Voltar 
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
    
    /**
     * Chamado quando o botão "Cadastrar" da tela de Login é clicado.
     * Navega para a tela de CadastroUsuario.
     */
    private void handleAbrirCadastro() {
        try {
            System.out.println("Botão 'Cadastrar' clicado! Abrindo tela CadastroUsuario...");
            App.setRoot("CadastroUsuario"); // Navega para a nova tela de cadastro
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