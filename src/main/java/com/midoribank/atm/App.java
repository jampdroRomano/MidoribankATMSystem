package com.midoribank.atm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("splash"), 1050, 750);
        scene.setFill(Color.web("#29252D"));
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        System.out.println(App.class.getResource("/com/midoribank/atm/" + fxml + "/" + fxml + ".fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/midoribank/atm/" + fxml + "/" + fxml + ".fxml"));
        System.out.println(fxml + " carregado.");
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
