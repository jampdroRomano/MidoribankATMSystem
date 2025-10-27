package com.midoribank.atm;

import java.util.HashMap;
import java.util.Map;

public class UserDAO {
    
    private static final Map<String, UserProfile> userProfiles = new HashMap<>();

    static {
        // Adicionando número da agência para cada usuário
        userProfiles.put("alvaro@midori.com", new UserProfile("Alvaro Gatao", "2204-5", "0123-1", "alvaro123", 1250.75));
        userProfiles.put("jao@midori.com", new UserProfile("Jao", "1234-5", "0123-1", "jao123", 380.00));
        userProfiles.put("alemao@midori.com", new UserProfile("Alemao", "5678-9", "0456-2", "alemao123", 5600.20));
    }
    
    public boolean autenticar(String email, String senha) {
        UserProfile profile = userProfiles.get(email);
        if (profile != null) {
            return profile.getSenha().equals(senha);
        }
        return false;
    }
    
    public UserProfile getProfile(String email){
        return userProfiles.get(email);
    }
}

