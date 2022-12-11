package com.example.teamproject.effect;

import com.example.teamproject.structure.SelectedRegion;
import com.example.teamproject.tools.Polygon;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @Description 将所给选区的选中部分染为指定颜色
 * @Author  CZX
 * @Date    2022.12.12
 **/
public class FillColorEffect {

    public static WritableImage FillSelectedRegion(SelectedRegion selectedRegion, Color color){
        WritableImage image = selectedRegion.selectedImage;
        PixelWriter pixelWriter = image.getPixelWriter();
        //遍历所有可能的像素点
        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                //在选区内 则染色
                if(selectedRegion.pointInRegion(x, y)){
                    pixelWriter.setColor(x, y, color);
                }
            }
        }
        return image;
    }

}
