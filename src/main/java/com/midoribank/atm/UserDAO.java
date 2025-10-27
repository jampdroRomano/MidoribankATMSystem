package com.midoribank.atm;

import java.util.HashMap;
import java.util.Map;

public class UserDAO {

    private static final Map<String, UserProfile> userProfiles = new HashMap<>();

    static {
        // Adicionando dados fictícios de cartão (número e PIN de 4 dígitos)
        userProfiles.put("alvaro@midori.com", new UserProfile("Alvaro Gatao", "2204-5", "0123-1", "alvaro123", 1250.75, "1111222233334444", "1234"));
        userProfiles.put("jao@midori.com", new UserProfile("Jao", "1234-5", "0123-1", "jao123", 380.00, "5555666677778888", "5678"));
        userProfiles.put("alemao@midori.com", new UserProfile("Alemao", "5678-9", "0456-2", "alemao123", 5600.20, "9999000011112222", "9012"));
    }

    // Autentica com email e SENHA DA CONTA (para login)
    public boolean autenticar(String email, String senhaConta) {
        UserProfile profile = userProfiles.get(email);
        if (profile != null) {
            return profile.getSenhaConta().equals(senhaConta);
        }
        return false;
    }

    public UserProfile getProfile(String email){
        return userProfiles.get(email);
    }
}

