package com.midoribank.atm.controllers;

import com.midoribank.atm.App;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SplashController {

    @FXML
    private Pane rootPane;

    @FXML
    private ImageView logoImage;
    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Text textMidori;
    @FXML
    private Text textBank;

    @FXML
    public void initialize() {
        logoImage.setOpacity(0.0);
        progressIndicator.setOpacity(0.0);

        if (textMidori != null) {
            textMidori.setOpacity(0.0);
        }
        if (textBank != null) {
            textBank.setOpacity(0.0);
        }

        startSplashAnimation();
    }

    private void startSplashAnimation() {

        FadeTransition fadeInLogo = new FadeTransition(Duration.millis(800), logoImage);
        fadeInLogo.setFromValue(0.0);
        fadeInLogo.setToValue(1.0);

        PauseTransition pauseAfterLogo = new PauseTransition(Duration.millis(200));

        FadeTransition fadeInMidori = new FadeTransition(Duration.millis(600), textMidori);
        fadeInMidori.setFromValue(0.0);
        fadeInMidori.setToValue(1.0);

        PauseTransition pauseBetweenTexts = new PauseTransition(Duration.millis(150));

        FadeTransition fadeInBank = new FadeTransition(Duration.millis(600), textBank);
        fadeInBank.setFromValue(0.0);
        fadeInBank.setToValue(1.0);

        FadeTransition fadeInProgress = new FadeTransition(Duration.millis(500), progressIndicator);
        fadeInProgress.setFromValue(0.0);
        fadeInProgress.setToValue(1.0);

        PauseTransition finalPause = new PauseTransition(Duration.millis(2000));

        FadeTransition fadeOutAll = new FadeTransition(Duration.millis(500), rootPane);
        fadeOutAll.setFromValue(1.0);
        fadeOutAll.setToValue(0.0);

        SequentialTransition sequence = new SequentialTransition(
                fadeInLogo,
                pauseAfterLogo,
                fadeInMidori,
                pauseBetweenTexts,
                fadeInBank,
                fadeInProgress,
                finalPause,
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