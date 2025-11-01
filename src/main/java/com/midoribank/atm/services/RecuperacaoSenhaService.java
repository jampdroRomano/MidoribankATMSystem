package com.midoribank.atm.services;

import com.midoribank.atm.dao.RecuperacaoSenhaDAO;
import com.midoribank.atm.dao.UserDAO;
import com.midoribank.atm.models.UserProfile;
import java.time.LocalDateTime;
import java.util.Random;

public class RecuperacaoSenhaService {

    private final UserDAO userDAO;
    private final RecuperacaoSenhaDAO recuperacaoDAO;
    private final EmailService emailService;
    private final Random random;

    public RecuperacaoSenhaService() {
        this.userDAO = new UserDAO();
        this.recuperacaoDAO = new RecuperacaoSenhaDAO();
        this.emailService = new EmailService();
        this.random = new Random();
    }

    private String gerarCodigoAleatorio() {
        int codigo = 100000 + random.nextInt(900000);
        return String.valueOf(codigo);
    }

    private String criarCorpoEmail(String nomeUsuario, String codigo) {
        return String.format(
                "Olá, %s!\n\n" +
                        "Recebemos uma solicitação para redefinir a senha da sua conta no MidoriBank.\n\n" +
                        "Seu código de verificação é: %s\n\n" +
                        "Este código expira em 15 minutos.\n\n" +
                        "Se você não solicitou isso, por favor, ignore este e-mail.\n\n" +
                        "Atenciosamente,\n" +
                        "Equipe MidoriBank",
                nomeUsuario, codigo
        );
    }

    public boolean iniciarRecuperacao(String email) {
        UserProfile user = userDAO.getProfileBasico(email);

        if (user == null) {
            System.err.println("Tentativa de recuperação para e-mail não cadastrado: " + email);
            return false;
        }

        int usuarioId = user.getId();
        String nome = user.getNome();
        String codigo = gerarCodigoAleatorio();
        LocalDateTime expiracao = LocalDateTime.now().plusMinutes(15);
        String corpoEmail = criarCorpoEmail(nome, codigo);
        String assunto = "MidoriBank - Código de Recuperação de Senha";

        recuperacaoDAO.invalidarCodigosAntigos(usuarioId);

        boolean salvoNoDb = recuperacaoDAO.salvarCodigo(usuarioId, codigo, expiracao);

        if (salvoNoDb) {
            return emailService.enviarEmail(email, assunto, corpoEmail);
        } else {
            System.err.println("Falha ao salvar o código no banco para o usuário: " + usuarioId);
            return false;
        }
    }

    public boolean validarCodigo(String email, String codigo) {
        UserProfile user = userDAO.getProfileBasico(email);
        if (user == null) {
            return false;
        }
        return recuperacaoDAO.validarCodigo(user.getId(), codigo);
    }

    public boolean redefinirSenha(String email, String novaSenha) {
        UserProfile user = userDAO.getProfileBasico(email);
        if (user == null) {
            return false;
        }

        int usuarioId = user.getId();
        boolean sucesso = recuperacaoDAO.atualizarSenha(usuarioId, novaSenha);

        if (sucesso) {
            recuperacaoDAO.invalidarCodigosAntigos(usuarioId);
        }
        return sucesso;
    }
}