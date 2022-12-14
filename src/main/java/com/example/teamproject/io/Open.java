package com.example.teamproject.io;

import com.example.teamproject.controller.MainUIController;
import com.example.teamproject.structure.Layer;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Open {
    MainUIController mainUIController = new MainUIController();//get mainUIController?
    Canvas canvas = null;
    public void importImage(File file) {
        /*
        前端：
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        file = fileChooser.showOpenDialog(stage);
        */

        if (file != null) {
            //新建layer并且把打开的图片放进这个layer的canvas里
            Image image = new Image(file.toURI().toString());
            Layer layer = mainUIController.createNewLayer();
            GraphicsContext graphicsContext = layer.getCanvas().getGraphicsContext2D();
            graphicsContext.drawImage(image, image.getRequestedWidth(), image.getRequestedHeight());//不确定
        }
    }
}
