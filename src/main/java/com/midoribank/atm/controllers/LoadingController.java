package com.midoribank.atm.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoadingController {

    @FXML
    private Label loadingLabel;

    public void setLoadingText(String text) {
        if (loadingLabel != null) {
            loadingLabel.setText(text);
        }
    }
}
