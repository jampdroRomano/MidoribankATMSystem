package com.midoribank.atm;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label; // <-- MUDANÇA: Trocado de Text para Label

public class CadastroSenhaController {

    @FXML private PasswordField senhaField;
    @FXML private Pane button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    @FXML private Node paneConfirmar;
    @FXML private Node paneVoltar;
    @FXML private Pane buttonApagar;
    @FXML private Pane buttonC;
    
    // --- MUDANÇA CRÍTICA: Corrigido de Text para Label ---
    @FXML private Label labelTitulo; 
    // --- Fim da Mudança ---

    // (Vou assumir que a validação de "54 dígitos" foi um erro de digitação e usar 4)
    private final int MAX_SENHA_LENGTH = 4; 
    
    private String senhaCadastroTemporaria = null;

    @FXML
    public void initialize() {
        System.out.println("CadastroSenhaController inicializado.");
        
        if (labelTitulo != null) {
            labelTitulo.setText("Digite uma senha para o seu cartão");
            labelTitulo.setLayoutX(320.0); // Ajusta a posição
        }

        configurarBotoesNumericos();
        configurarControles();
        configurarBotoesEdicao();
    }
    
    // ... (configurarBotoesNumericos, configurarControles, configurarBotoesEdicao) ...
    
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
     * Botão Voltar: Volta para a tela de Cadastro de Cartão.
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
     * Lógica de criar e confirmar a senha em duas etapas.
     */
    @FXML
    private void handleConfirmarSenha() {
        String senhaDigitada = senhaField.getText();

        if (senhaCadastroTemporaria == null) {
            // ETAPA 1: Criando a primeira senha
            if (senhaDigitada.length() != MAX_SENHA_LENGTH) {
                exibirMensagemErro("A senha deve ter " + MAX_SENHA_LENGTH + " dígitos.");
                return;
            }
            this.senhaCadastroTemporaria = senhaDigitada;
            labelTitulo.setText("Digite novamente a senha");
            labelTitulo.setLayoutX(340.0); // Ajusta posição
            limparSenha();
            
        } else {
            // ETAPA 2: Confirmando a senha
            if (senhaDigitada.equals(this.senhaCadastroTemporaria)) {
                // SUCESSO!
                System.out.println("Senha criada com sucesso!");
                
                // --- MUDANÇA: Salva a senha final e "commita" o cadastro ---
                SessionManager.setCadastroSenhaCartao(senhaDigitada);
                SessionManager.salvarCadastroCompletoNoBanco(); // Simula o salvamento no DB
                // --- Fim da Mudança ---

                exibirMensagemInfo("Sucesso", "Cadastro realizado! Faça o login.");
                
                try {
                    App.setRoot("Login"); // Manda para o Login
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // FALHA
                exibirMensagemErro("As senhas não conferem! Tente novamente.");
                this.senhaCadastroTemporaria = null;
                labelTitulo.setText("Digite uma senha para o seu cartão");
                labelTitulo.setLayoutX(320.0);
                limparSenha();
            }
        }
    }
    
    // --- Métodos de UI e Alertas (Sem alteração) ---

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