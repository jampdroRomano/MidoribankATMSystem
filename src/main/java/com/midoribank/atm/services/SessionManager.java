package com.midoribank.atm.services;

import com.midoribank.atm.models.UserProfile;
import java.util.Random;

public class SessionManager {

    private static UserProfile currentUser;
    private static double currentTransactionAmount;
    private static String currentTransactionType;

    private static String cadastroNome;
    private static String cadastroEmail;
    private static String cadastroSenhaConta;

    private static String cadastroAgencia;
    private static String cadastroNumeroConta;

    private static String cadastroNumeroCartao;
    private static String cadastroCVV;
    private static String cadastroSenhaCartao;

    private static String emailRecuperacao;
    private static String codigoRecuperacaoVerificado;

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

        Random rand = new Random();
        cadastroAgencia = String.format("%04d", rand.nextInt(10000));
        cadastroNumeroConta = String.format("%05d-%d", rand.nextInt(100000), rand.nextInt(10));
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
        cadastroAgencia = null;
        cadastroNumeroConta = null;
        cadastroNumeroCartao = null;
        cadastroCVV = null;
        cadastroSenhaCartao = null;
    }

    public static boolean salvarCadastroCompletoNoBanco() {
        CadastroService cadastroService = new CadastroService();

        boolean sucesso = cadastroService.realizarCadastroCompleto(
                cadastroNome, cadastroEmail, cadastroSenhaConta,
                cadastroAgencia, cadastroNumeroConta,
                cadastroNumeroCartao, cadastroCVV, cadastroSenhaCartao
        );

        if (sucesso) {
            clearCadastroData();
        }

        return sucesso;
    }

    public static void setEmailRecuperacao(String email) {
        emailRecuperacao = email;
    }

    public static String getEmailRecuperacao() {
        return emailRecuperacao;
    }

    public static void setCodigoVerificado(String codigo) {
        codigoRecuperacaoVerificado = codigo;
    }

    public static String getCodigoVerificado() {
        return codigoRecuperacaoVerificado;
    }

    public static void clearRecuperacao() {
        emailRecuperacao = null;
        codigoRecuperacaoVerificado = null;
    }
}