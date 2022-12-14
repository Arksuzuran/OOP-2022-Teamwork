package com.example.teamproject;

import com.example.teamproject.controller.IconController;
import com.example.teamproject.controller.MainDrawingController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

import java.io.IOException;
import java.util.Timer;

/**
 * @Description 程序入口
 * @Author  CZX
 * @Date    2022.12.5
 **/
public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        //链接OpenCV库！如果开发时这里出问题报错了，请发到群里
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);

        stage.setTitle("SAI Ver.lite");

        stage.setScene(scene);

        stage.setMinHeight(700);
        stage.setMinWidth(1000);

        stage.getIcons().add(IconController.title);

        stage.show();

        Timer t = new Timer();
        t.scheduleAtFixedRate(MainDrawingController.getMDC(),1000*60, 1000*10);
    }
    public static void main(String[] args) {
        launch();
    }
}