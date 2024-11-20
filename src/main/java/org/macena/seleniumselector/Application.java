package org.macena.seleniumselector;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/org/macena/seleniumselector/MainView.fxml"));
        primaryStage.setScene(new Scene(loader.load()));

        primaryStage.setTitle("Chrome Driver Selector - by vmacena");
        primaryStage.getIcons().add(
                new Image(Objects
                        .requireNonNull(getClass()
                        .getResourceAsStream("/icons/owner.jpg"))));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}