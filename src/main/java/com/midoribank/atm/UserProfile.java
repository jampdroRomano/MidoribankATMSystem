package com.midoribank.atm;

public class UserProfile {
    private String nome;
    private String numeroConta;
    private String agencia; 
    private String senhaConta; // Renomeado para clareza
    private double saldo;
    private String numeroCartao; // Novo
    private String senhaCartao;  // Novo

    public UserProfile(String nome, String numeroConta, String agencia, String senhaConta, double saldo, String numeroCartao, String senhaCartao) {
        this.nome = nome;
        this.numeroConta = numeroConta;
        this.agencia = agencia; 
        this.senhaConta = senhaConta;
        this.saldo = saldo;
        this.numeroCartao = numeroCartao; // Adicionado
        this.senhaCartao = senhaCartao;   // Adicionado
    }

    // Getters
    public String getNome() { return nome; }
    public String getNumeroConta() { return numeroConta; }
    public String getAgencia() { return agencia; } 
    public String getSenhaConta() { return senhaConta; } // Renomeado
    public double getSaldo() { return saldo; }
    public String getNumeroCartao() { return numeroCartao; } // Novo
    public String getSenhaCartao() { return senhaCartao; }   // Novo

    // Método para validar a senha do cartão
    public boolean validarSenhaCartao(String senhaDigitada) {
        return this.senhaCartao.equals(senhaDigitada);
    }

    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
        }
    }

    public boolean sacar(double valor) {
        if (valor > 0 && valor <= this.saldo) {
            this.saldo -= valor;
            return true;
        }
        return false;
    }
}

