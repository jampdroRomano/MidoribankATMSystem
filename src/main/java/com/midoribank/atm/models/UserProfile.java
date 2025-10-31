package com.midoribank.atm.models;

public class UserProfile {
    private String nome;
    private String numeroConta;
    private String agencia;
    private String senhaConta;
    private double saldo;
    private String numeroCartao;
    private String senhaCartao;

    public UserProfile(String nome, String numeroConta, String agencia, String senhaConta, double saldo, String numeroCartao, String senhaCartao) {
        this.nome = nome;
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.senhaConta = senhaConta;
        this.saldo = saldo;
        this.numeroCartao = numeroCartao;
        this.senhaCartao = senhaCartao;
    }

    public String getNome() { return nome; }
    public String getNumeroConta() { return numeroConta; }
    public String getAgencia() { return agencia; }
    public String getSenhaConta() { return senhaConta; }
    public double getSaldo() { return saldo; }
    public String getNumeroCartao() { return numeroCartao; }
    public String getSenhaCartao() { return senhaCartao; }

    public void setSaldo(double novoSaldo) {
        this.saldo = novoSaldo;
    }

    public boolean validarSenhaCartao(String senhaDigitada) {
        return this.senhaCartao.equals(senhaDigitada);
    }

}
