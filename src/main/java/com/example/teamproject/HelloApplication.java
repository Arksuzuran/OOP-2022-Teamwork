package com.example.teamproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

import java.io.IOException;

/**
 * @Description 程序入口 目前项目的结构还没确定 暂定这里为入口
 * @Author  CZX
 * @Date    2022.11.30
 **/
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 900, 600);

        stage.setTitle("Hello!");

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}