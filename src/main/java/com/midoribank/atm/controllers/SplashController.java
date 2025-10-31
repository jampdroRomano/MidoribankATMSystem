package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SplashController {

    @FXML
    private ImageView logoImage;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    public void initialize() {
        startSplashAnimation();
    }

    private void startSplashAnimation() {
        FadeTransition fadeInLogo = new FadeTransition(Duration.millis(1000), logoImage);
        fadeInLogo.setFromValue(0.0);
        fadeInLogo.setToValue(1.0);

        PauseTransition pause1 = new PauseTransition(Duration.millis(500));

        FadeTransition fadeInProgress = new FadeTransition(Duration.millis(500), progressIndicator);
        fadeInProgress.setFromValue(0.0);
        fadeInProgress.setToValue(1.0);

        PauseTransition pause2 = new PauseTransition(Duration.millis(2000));

        FadeTransition fadeOutAll = new FadeTransition(Duration.millis(500), logoImage.getParent());
        fadeOutAll.setFromValue(1.0);
        fadeOutAll.setToValue(0.0);

        SequentialTransition sequence = new SequentialTransition(
            fadeInLogo,
            pause1,
            fadeInProgress,
            pause2,
            fadeOutAll
        );

        sequence.setOnFinished(event -> {
            try {
                App.setRoot("opcoesLogin");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        sequence.play();
    }
}
