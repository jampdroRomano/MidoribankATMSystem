package com.midoribank.atm;

public class SessionManager {

    // --- Sessão do Usuário Logado ---
    private static UserProfile currentUser;
    private static double currentTransactionAmount;
    private static String currentTransactionType;

    // --- MUDANÇA: Dados temporários para o fluxo de cadastro ---
    private static String cadastroNome;
    private static String cadastroEmail;
    private static String cadastroSenhaConta;
    private static String cadastroNumeroCartao;
    private static String cadastroCVV;
    private static String cadastroSenhaCartao;
    // --- Fim da Mudança ---

    public static void setCurrentUser(UserProfile user) {
        currentUser = user;
    }

    public static UserProfile getCurrentUser() {
        return currentUser;
    }
    
    public static void setCurrentTransaction(double amount, String type) {
        currentTransactionAmount = amount;
        currentTransactionType = type;
    }

    public static double getCurrentTransactionAmount() {
        return currentTransactionAmount;
    }

    public static String getCurrentTransactionType() {
        return currentTransactionType;
    }

    public static void clearSession() {
        currentUser = null;
    }
    
    public static void clearTransaction() {
        currentTransactionAmount = 0;
        currentTransactionType = null;
    }
    
    // --- MUDANÇA: Métodos para o fluxo de cadastro ---
    
    /** Salva os dados da primeira tela de cadastro */
    public static void setCadastroUsuario(String nome, String email, String senha) {
        cadastroNome = nome;
        cadastroEmail = email;
        cadastroSenhaConta = senha;
    }
    
    /** Salva os dados da segunda tela de cadastro (cartão) */
    public static void setCadastroCartao(String numeroCartao, String cvv) {
        cadastroNumeroCartao = numeroCartao;
        cadastroCVV = cvv;
    }
    
    /** Salva a senha final do cartão */
    public static void setCadastroSenhaCartao(String senhaCartao) {
        cadastroSenhaCartao = senhaCartao;
    }
    
    public static String getCadastroNome() {
        return cadastroNome;
    }
    
    /** Limpa todos os dados temporários de cadastro */
    public static void clearCadastroData() {
        cadastroNome = null;
        cadastroEmail = null;
        cadastroSenhaConta = null;
        cadastroNumeroCartao = null;
        cadastroCVV = null;
        cadastroSenhaCartao = null;
    }
    
    // (No futuro, você terá um método para pegar todos os dados e salvar no banco)
    public static void salvarCadastroCompletoNoBanco() {
        System.out.println("--- SALVANDO NO BANCO (Simulação) ---");
        System.out.println("Nome: " + cadastroNome);
        System.out.println("Email: " + cadastroEmail);
        System.out.println("Senha Conta: " + cadastroSenhaConta);
        System.out.println("Cartão: " + cadastroNumeroCartao);
        System.out.println("CVV: " + cadastroCVV);
        System.out.println("Senha Cartão: " + cadastroSenhaCartao);
        System.out.println("-------------------------------------");
        
        // Aqui você chamaria o seu UserDAO.cadastrarNovoUsuario(...)
        
        clearCadastroData(); // Limpa os dados após salvar
    }
    // --- Fim da Mudança ---
}