// ---------------------------------------------------------------------
// MIT License
// Copyright (c) 2018 Henrik Peters
// See LICENSE file in the project root for full license information.
// ---------------------------------------------------------------------
package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("MainWindow.fxml"));

        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();

        primaryStage.setTitle("FirstFollow-Generator");
        primaryStage.setMinWidth(380);
        primaryStage.setMinHeight(380);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("ui/MainWindow.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
