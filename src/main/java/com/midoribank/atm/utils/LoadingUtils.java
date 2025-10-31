package com.midoribank.atm.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.midoribank.atm.App;
import com.midoribank.atm.controllers.LoadingController;

import java.util.concurrent.CompletableFuture;

public class LoadingUtils {

    private static Stage loadingStage;
    private static LoadingController controller;

    public static void showLoading(String message) {
        Platform.runLater(() -> {
            try {
                if (loadingStage == null) {
                    loadingStage = new Stage();
                    loadingStage.initStyle(StageStyle.TRANSPARENT);
                    loadingStage.initModality(Modality.APPLICATION_MODAL);

                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/midoribank/atm/loading/loading.fxml"));
                    Parent root = loader.load();
                    controller = loader.getController();

                    Scene scene = new Scene(root);
                    scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                    loadingStage.setScene(scene);
                }

                if (controller != null) {
                    controller.setLoadingText(message);
                }

                loadingStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void hideLoading() {
        Platform.runLater(() -> {
            if (loadingStage != null) {
                loadingStage.hide();
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
