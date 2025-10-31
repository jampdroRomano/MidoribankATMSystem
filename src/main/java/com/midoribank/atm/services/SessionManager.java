package com.midoribank.atm.services;

import com.midoribank.atm.models.UserProfile;

public class SessionManager {

    private static UserProfile currentUser;
    private static double currentTransactionAmount;
    private static String currentTransactionType;

    private static String cadastroNome;
    private static String cadastroEmail;
    private static String cadastroSenhaConta;
    private static String cadastroNumeroCartao;
    private static String cadastroCVV;
    private static String cadastroSenhaCartao;

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

    public static void setCadastroUsuario(String nome, String email, String senha) {
        cadastroNome = nome;
        cadastroEmail = email;
        cadastroSenhaConta = senha;
    }

    public static void setCadastroCartao(String numeroCartao, String cvv) {
        cadastroNumeroCartao = numeroCartao;
        cadastroCVV = cvv;
    }

    public static void setCadastroSenhaCartao(String senhaCartao) {
        cadastroSenhaCartao = senhaCartao;
    }

    public static String getCadastroNome() {
        return cadastroNome;
    }

    public static void clearCadastroData() {
        cadastroNome = null;
        cadastroEmail = null;
        cadastroSenhaConta = null;
        cadastroNumeroCartao = null;
        cadastroCVV = null;
        cadastroSenhaCartao = null;
    }

    public static void salvarCadastroCompletoNoBanco() {
        System.out.println("--- SALVANDO NO BANCO (Simulação) ---");
        System.out.println("Nome: " + cadastroNome);
        System.out.println("Email: " + cadastroEmail);
        System.out.println("Senha Conta: " + cadastroSenhaConta);
        System.out.println("Cartão: " + cadastroNumeroCartao);
        System.out.println("CVV: " + cadastroCVV);
        System.out.println("Senha Cartão: " + cadastroSenhaCartao);
        System.out.println("-------------------------------------");

        clearCadastroData();
    }

}
