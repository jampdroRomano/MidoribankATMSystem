package com.midoribank.atm.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import com.midoribank.atm.App;
import com.midoribank.atm.controllers.LoadingController;

import java.util.concurrent.CompletableFuture;

public class LoadingUtils {

    private static Parent loadingNode;
    private static LoadingController controller;

    static {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/midoribank/atm/loading/loading.fxml"));
            loadingNode = loader.load();
            controller = loader.getController();
            loadingNode.setMouseTransparent(false);
        } catch (Exception e) {
            e.printStackTrace();
            loadingNode = null;
            controller = null;
        }
    }

    public static void showLoading(String message) {
        Platform.runLater(() -> {
            if (loadingNode != null) {
                if (controller != null) {
                    controller.setLoadingText(message);
                }

                if (!App.getRootPane().getChildren().contains(loadingNode)) {
                    App.getRootPane().getChildren().add(loadingNode);
                }
            } else {
                System.err.println("Erro: loadingNode não pôde ser carregado.");
            }
        });
    }

    public static void hideLoading() {
        Platform.runLater(() -> {
            if (loadingNode != null) {
                App.getRootPane().getChildren().remove(loadingNode);
            }
        });
    }

    public static <T> CompletableFuture<T> runWithLoading(String message, java.util.concurrent.Callable<T> task) {
        return CompletableFuture.supplyAsync(() -> {
            showLoading(message);
            try {
                return task.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                hideLoading();
            }
        });
    }

    public static CompletableFuture<Void> runWithLoading(String message, Runnable task) {
        return CompletableFuture.runAsync(() -> {
            showLoading(message);
            try {
                task.run();
            } finally {
                hideLoading();
            }
        });
    }
}