package com.midoribank.atm;

public class SessionManager {

    private static UserProfile currentUser;
    private static double currentTransactionAmount;
    private static String currentTransactionType;

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
}

