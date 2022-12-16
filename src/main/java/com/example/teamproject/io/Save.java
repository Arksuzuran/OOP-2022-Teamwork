package com.example.teamproject.io;

import com.example.teamproject.controller.MainDrawingController;
import com.example.teamproject.structure.Layer;
import com.example.teamproject.tools.ImageFormConverter;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;

import static org.opencv.imgcodecs.Imgcodecs.imencode;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

public class Save {

    public static File getOutputFile(){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
//        new FileChooser.ExtensionFilter("JPG", "*.jpg"),
        System.out.println(MainDrawingController.getMDC().getName());
        fileChooser.setInitialFileName(MainDrawingController.getMDC().getName());
        File file = fileChooser.showSaveDialog(stage);
        return file;
    }

    public static void outputImage(Image image, File file){
        if(MainDrawingController.getMDC().isActive()){
            try {
                File p = file.getParentFile();
                if(!p.exists())
                    p.mkdir();
                if(file.exists())
                    file.delete();
                Mat mat = ImageFormConverter.imageToMat(image);
                imwrite(file.toString(), mat);
//                MatOfByte mob = new MatOfByte();
//                imencode(".jpg", mat, mob);
//                byte[] byteArray = mob.toArray();
//                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
