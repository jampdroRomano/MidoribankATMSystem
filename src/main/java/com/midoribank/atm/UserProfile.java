package com.midoribank.atm;

/**
 * Representa o perfil de um usuário, incluindo dados da conta e saldo.
 * Esta classe encapsula toda a informação e as operações básicas de uma conta.
 */

public class UserProfile {
    private String nome;
    private String numeroConta;
    private String agencia; // Novo campo
    private String senha;
    private double saldo;

    public UserProfile(String nome, String numeroConta, String agencia, String senha, double saldo) {
        this.nome = nome;
        this.numeroConta = numeroConta;
        this.agencia = agencia; // Adicionado ao construtor
        this.senha = senha;
        this.saldo = saldo;
    }

    // Getters
    public String getNome() { return nome; }
    public String getNumeroConta() { return numeroConta; }
    public String getAgencia() { return agencia; } // Novo getter
    public String getSenha() { return senha; }
    public double getSaldo() { return saldo; }

    // Métodos de Operação
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

