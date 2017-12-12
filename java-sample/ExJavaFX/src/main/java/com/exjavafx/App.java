package com.exjavafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends Application {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Creating the java button
        Button btn = new Button();
        // Setting text to button
        btn.setText("Hello World");
        //registering a handler for button
        btn.setOnAction((ActionEvent event) -> {
            // printing Hello World! to the console
            System.out.println("Hello World!");
        });
        // Initializing the StackPane class
        StackPane root = new StackPane();
        // Adding all the nodes to the FlowPane
        root.getChildren().add(btn);
        //Creating a scene object
        Scene scene = new Scene(root, 300, 250);
        //Adding the title to the window (primaryStage)
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        // show the window(primaryStage)
        primaryStage.show();
    }
}

