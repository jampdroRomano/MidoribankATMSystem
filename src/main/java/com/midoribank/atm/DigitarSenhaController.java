package com.midoribank.atm;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node; // Usar Node para aceitar Pane ou Button
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.effect.ColorAdjust;

public class DigitarSenhaController {

    @FXML private PasswordField senhaField; 

    // Botões numéricos
    @FXML private Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    
    // Botões de Ação (Confirmar/Voltar como Pane no FXML)
    @FXML private Node paneConfirmar; // Usar Node para aceitar Pane
    @FXML private Node paneVoltar;    // Usar Node para aceitar Pane
    
    // Botões de Edição (se existirem no FXML com estes fx:id)
    @FXML private Button buttonApagar; // Para o botão '#' ou backspace
    @FXML private Button buttonPonto;  // Botão '.' - Não usual para PIN, mas incluído se necessário

    private UserProfile currentUser;
    private final int MAX_SENHA_LENGTH = 4; 

    @FXML
    public void initialize() {
        this.currentUser = SessionManager.getCurrentUser();
        if (currentUser == null) {
            try { App.setRoot("Login"); } catch (IOException e) { e.printStackTrace(); }
            return;
        }
        configurarBotoesNumericos();
        configurarControles();
        configurarBotoesEdicao();
    }

    private void configurarBotoesNumericos() {
        Button[] buttons = {button0, button1, button2, button3, button4, button5, button6, button7, button8, button9};
        for (Button btn : buttons) {
            if (btn != null) {
                String numero = btn.getText(); // Pega o número do texto do botão
                btn.setOnAction(e -> adicionarDigito(numero));
                setupNodeHoverEffects(btn); // Aplica efeito de cor
            } else {
                System.err.println("Aviso: Um botão numérico não foi encontrado no FXML.");
            }
        }
    }
    
     private void configurarControles() {
         if (paneConfirmar != null) {
            paneConfirmar.setOnMouseClicked(e -> handleConfirmarSenha());
            setupNodeHoverEffects(paneConfirmar); // Aplica efeito de cor
         } else {
             System.err.println("Aviso: paneConfirmar não encontrado no FXML.");
         }
         
         if (paneVoltar != null) {
            paneVoltar.setOnMouseClicked(e -> handleVoltar());
             setupNodeHoverEffects(paneVoltar); // Aplica efeito de cor
         } else {
             System.err.println("Aviso: paneVoltar não encontrado no FXML.");
         }
    }

    private void configurarBotoesEdicao() {
        // Mapeia o botão '#' ou backspace para a função apagar
        if (buttonApagar != null) {
            buttonApagar.setOnAction(e -> apagarDigito());
            setupNodeHoverEffects(buttonApagar);
        } else {
             System.err.println("Aviso: buttonApagar não encontrado no FXML (esperado fx:id='buttonApagar').");
        }

        // Se houver um botão '.' (não comum para PIN, mas tratando o FXML)
        if (buttonPonto != null) {
            // Pode adicionar uma ação se necessário, ou desabilitar/remover do FXML
             buttonPonto.setOnAction(e -> System.out.println("Botão '.' pressionado - ação não definida para PIN."));
             setupNodeHoverEffects(buttonPonto);
             // buttonPonto.setDisable(true); // Desabilita se não for usado
        } else {
             System.err.println("Aviso: buttonPonto não encontrado no FXML (esperado fx:id='buttonPonto').");
        }
        
        // Se você adicionar um botão Limpar com fx:id="buttonLimpar"
        // @FXML private Button buttonLimpar; 
        // if (buttonLimpar != null) {
        //    buttonLimpar.setOnAction(e -> limparSenha());
        //    setupNodeHoverEffects(buttonLimpar);
        // }
    }


    private void adicionarDigito(String digito) {
        if (senhaField.getText().length() < MAX_SENHA_LENGTH) {
            senhaField.appendText(digito);
        }
    }

    private void apagarDigito() {
         String currentText = senhaField.getText();
        if (!currentText.isEmpty()) {
            senhaField.setText(currentText.substring(0, currentText.length() - 1));
        }
    }
    
    private void limparSenha() {
        senhaField.clear();
    }

    private void handleConfirmarSenha() {
        String senhaDigitada = senhaField.getText();

        if (senhaDigitada.length() != MAX_SENHA_LENGTH) {
             exibirMensagemErro("A senha do cartão deve ter " + MAX_SENHA_LENGTH + " dígitos.");
             return;
        }

        if (currentUser.validarSenhaCartao(senhaDigitada)) {
             executarOperacao();
        } else {
             exibirMensagemErro("Senha do cartão incorreta!");
             limparSenha(); 
        }
    }
    
    private void executarOperacao() {
         String tipo = SessionManager.getCurrentTransactionType();
         double valor = SessionManager.getCurrentTransactionAmount();
         boolean sucesso = false;

         if ("Saque".equals(tipo)) {
             sucesso = currentUser.sacar(valor);
         } else if ("Deposito".equals(tipo)) { 
             currentUser.depositar(valor);
             sucesso = true; 
         }
         // Adicionar lógica para outras operações (ex: Transferência) aqui

         if (sucesso) {
             try {
                 // Certifique-se que o nome do FXML está correto
                 App.setRoot("ConclusaoOperacao"); 
             } catch (IOException e) {
                 e.printStackTrace();
                 exibirMensagemErro("Não foi possível carregar a tela de conclusão.");
             }
         } else {
             // Caso raro (ex: saldo insuficiente não detectado antes no Saque)
              if ("Saque".equals(tipo)) {
                 exibirMensagemErro("Saldo insuficiente. Operação cancelada.");
             } else {
                 exibirMensagemErro("Não foi possível completar a operação.");
             }
             SessionManager.clearTransaction(); 
             try { App.setRoot("home"); } catch (IOException e) { e.printStackTrace(); }
         }
    }

    private void handleVoltar() {
        try {
            // Volta para a tela de confirmar dados
            // Certifique-se que o nome do FXML está correto
            App.setRoot("confirmar-operacao"); 
        } catch (IOException e) {
            e.printStackTrace();
             exibirMensagemErro("Não foi possível voltar para a tela anterior.");
        }
    }
    
    // --- Efeitos Visuais (adaptados para Node) ---
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

    private void exibirMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

