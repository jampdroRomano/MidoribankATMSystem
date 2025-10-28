package com.midoribank.atm;

public class UserProfile {
    private String nome;
    private String numeroConta;
    private String agencia; 
    private String senhaConta;
    private double saldo;
    private String numeroCartao;
    private String senhaCartao; 

    // O construtor continua o MESMO. O UserDAO precisa dele.
    public UserProfile(String nome, String numeroConta, String agencia, String senhaConta, double saldo, String numeroCartao, String senhaCartao) {
        this.nome = nome;
        this.numeroConta = numeroConta;
        this.agencia = agencia; 
        this.senhaConta = senhaConta;
        this.saldo = saldo;
        this.numeroCartao = numeroCartao;
        this.senhaCartao = senhaCartao; 
    }

    // --- Getters (Continuam os mesmos) ---
    // (O aplicativo precisa LER os dados da fotografia)
    
    public String getNome() { return nome; }
    public String getNumeroConta() { return numeroConta; }
    public String getAgencia() { return agencia; } 
    public String getSenhaConta() { return senhaConta; }
    public double getSaldo() { return saldo; }
    public String getNumeroCartao() { return numeroCartao; }
    public String getSenhaCartao() { return senhaCartao; } 

    
    // --- Setter (A MUDANÇA IMPORTANTE) ---
    
    /**
     * Este método permite que o "Chefe" (Controller) ATUALIZE
     * o saldo na "fotografia" DEPOIS que o banco de dados
     * já foi salvo com sucesso.
     */
    public void setSaldo(double novoSaldo) {
        this.saldo = novoSaldo;
    }

    
    // --- Validação (Continua o mesmo) ---
    
    /**
     * O "Chefe da Segurança" (DigitarSenhaController) usa isso
     * para checar se a senha digitada bate com a senha da "fotografia".
     */
    public boolean validarSenhaCartao(String senhaDigitada) {
        return this.senhaCartao.equals(senhaDigitada);
    }

    
    // --- MÉTODOS REMOVIDOS ---
    
    /*
     * O método depositar(double valor) foi REMOVIDO.
     * A lógica (saldo += valor) estava errada, pois só mudava
     * a "fotografia" e não o banco.
     * A lógica real agora está no DigitarSenhaController.
     */
    
    /*
     * O método sacar(double valor) foi REMOVIDO.
     * A lógica (saldo -= valor) também só mudava a "fotografia".
     * A lógica real agora está no DigitarSenhaController.
     */
}