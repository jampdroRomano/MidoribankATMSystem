package com.midoribank.atm;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text; // Importe o Text

public class CadastroSenhaController {

    @FXML private PasswordField senhaField;
    @FXML private Pane button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    @FXML private Node paneConfirmar;
    @FXML private Node paneVoltar;
    @FXML private Pane buttonApagar;
    @FXML private Pane buttonC;
    @FXML private Text labelTitulo; // Para alterar o texto

    // (A validação de 54 dígitos parecia um erro de digitação, então usei 4)
    private final int MAX_SENHA_LENGTH = 4; 
    
    // Armazena a primeira senha digitada
    private String senhaCadastroTemporaria = null;

    @FXML
    public void initialize() {
        System.out.println("CadastroSenhaController inicializado.");
        
        // Define o texto inicial para o cadastro
        if (labelTitulo != null) {
            labelTitulo.setText("Digite uma senha para o seu cartão");
            labelTitulo.setLayoutX(320.0); // Ajusta a posição para o texto novo
        }

        // Configura todos os botões
        configurarBotoesNumericos();
        configurarControles();
        configurarBotoesEdicao();
    }
    
    // Configura os botões numéricos (Copiado do seu DigitarSenhaController)
    private void configurarBotoesNumericos() {
        Pane[] panes = {button0, button1, button2, button3, button4, button5, button6, button7, button8, button9};
        for (int i = 0; i < panes.length; i++) {
            Pane pane = panes[i];
            if (pane != null) {
                final String numero = String.valueOf(i);
                pane.setOnMouseClicked(e -> adicionarDigito(numero));
                setupNodeHoverEffects(pane);
            }
        }
    }

    // Configura os botões de controle (Confirmar e Voltar)
    private void configurarControles() {
        if (paneConfirmar != null) {
            paneConfirmar.setOnMouseClicked(e -> handleConfirmarSenha());
            setupNodeHoverEffects(paneConfirmar);
        }
        if (paneVoltar != null) {
            paneVoltar.setOnMouseClicked(e -> handleVoltarClick());
            setupNodeHoverEffects(paneVoltar);
        }
    }

    // Configura os botões de edição (Apagar e Limpar)
    private void configurarBotoesEdicao() {
        if (buttonApagar != null) {
            buttonApagar.setOnMouseClicked(e -> apagarDigito());
            setupNodeHoverEffects(buttonApagar);
        }
        if (buttonC != null) {
            buttonC.setOnMouseClicked(e -> limparSenha());
            setupNodeHoverEffects(buttonC);
        }
    }

    /**
     * Chamado quando a imagem "Voltar" é clicada.
     * Retorna para a tela de Cadastro de Cartão.
     */
    @FXML
    private void handleVoltarClick() {
        try {
            System.out.println("Botão Voltar (Cadastro Senha) clicado!");
            App.setRoot("CadastroCartao"); // Volta para a tela anterior
        } catch (IOException e) {
            System.err.println("Falha ao carregar CadastroCartao.fxml!");
            e.printStackTrace();
        }
    }

    /**
     * Lógica principal: ou captura a primeira senha ou confirma a segunda.
     */
    @FXML
    private void handleConfirmarSenha() {
        String senhaDigitada = senhaField.getText();

        if (senhaCadastroTemporaria == null) {
            // ETAPA 1: Criando a primeira senha
            
            // Validação de 4 dígitos
            if (senhaDigitada.length() != MAX_SENHA_LENGTH) {
                exibirMensagemErro("A senha deve ter " + MAX_SENHA_LENGTH + " dígitos.");
                return;
            }
            
            // Armazena a primeira senha
            this.senhaCadastroTemporaria = senhaDigitada;
            
            // Muda o label como você pediu
            labelTitulo.setText("Digite novamente a senha");
            labelTitulo.setLayoutX(340.0); // Ajusta posição do novo texto
            limparSenha();
            
        } else {
            // ETAPA 2: Confirmando a senha
            
            if (senhaDigitada.equals(this.senhaCadastroTemporaria)) {
                // SUCESSO!
                System.out.println("Senha criada com sucesso (temporariamente)!");
                // (Futuramente, aqui você salvaria a senha no banco de dados)
                
                exibirMensagemInfo("Sucesso", "Cadastro realizado! Faça o login.");
                
                try {
                    App.setRoot("Login"); // Envia para a tela de Login
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // FALHA NA CONFIRMAÇÃO
                exibirMensagemErro("As senhas não conferem! Tente novamente.");
                
                // Reinicia o processo
                this.senhaCadastroTemporaria = null;
                labelTitulo.setText("Digite uma senha para o seu cartão");
                labelTitulo.setLayoutX(320.0);
                limparSenha();
            }
        }
    }
    
    // --- Métodos de UI e Alertas ---

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
    
    private void exibirMensagemInfo(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}