package com.example.teamproject.controller;

import com.example.teamproject.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class LoaderController {

    static FXMLLoader loader1 = new FXMLLoader(HelloApplication.class.getResource("pen-view.fxml"));
    static FXMLLoader loader3 = new FXMLLoader(HelloApplication.class.getResource("eraser-view.fxml"));
    static FXMLLoader loader2 = new FXMLLoader(HelloApplication.class.getResource("selector-view.fxml"));
    static FXMLLoader loader4 = new FXMLLoader(HelloApplication.class.getResource("shape-view.fxml"));
    static FXMLLoader loader5 = new FXMLLoader(HelloApplication.class.getResource("process-view.fxml"));

    static VBox v1 = null;
    static VBox v3 = null;
    static VBox v2 = null;
    static VBox v4 = null;
    static VBox v5 = null;

    static {
        try {
            v1 = loader1.load();
            v2 = loader2.load();
            v3 = loader3.load();
            v4 = loader4.load();
            v5 = loader5.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
