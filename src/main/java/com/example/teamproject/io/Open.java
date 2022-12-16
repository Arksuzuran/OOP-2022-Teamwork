package com.example.teamproject.io;

import com.example.teamproject.brush.SelectorBrush;
import com.example.teamproject.controller.ControllerSet;
import com.example.teamproject.structure.SelectedRegion;
import com.example.teamproject.tools.Polygon;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;

public class Open {

    /**
     * 从指定文件获取Image对象
     * @param file  file
     * @return  读取到的图片
     */
    public static Image importImage(File file) {
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
            return new Image(file.toURI().toString());
        }
        return null;
    }

    public static File getInputFile(){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        File file = fileChooser.showOpenDialog(stage);
        if(file==null)
            return null;
        String s = file.getName();
        //确保可读入
        if(s.endsWith(".jpg") || s.endsWith(".png")){
            //去除可能存在的拓展名
            int index = s.lastIndexOf('.');
            if(index >= 0)
                ControllerSet.muc.setName(s.substring(0, s.lastIndexOf('.')));
            else
                ControllerSet.muc.setName(s);
            return file;
        }
        else
            return null;
    }

    /**
     * 从指定文件获取Image对象并放入选区
     * @param file file
     * @return  生成的选区
     */
    public static SelectedRegion importImageInSelectedRegion(File file){

        if(SelectorBrush.getSelectorBrush().hasSelected())
            return null;
        Image image = importImage(file);
        if(image==null)
            return null;
        PixelReader pixelReader= image.getPixelReader();

        //生成尺寸信息
        double width = image.getWidth(), height = image.getHeight();
        int w = (int)width, h = (int)height;

        //生成数组
        boolean[][] inRegion = new boolean[w+3][h+3];
        Color[][] colorRegion = new Color[w+3][h+3];
        for(int x=0; x<w; x++){
            for(int y=0; y<h; y++){
                Color color = pixelReader.getColor(x, y);
                inRegion[x][y] = true;
                colorRegion[x][y] = color;
            }
        }

        //生成边界
        ArrayList<Point2D.Double> points = new ArrayList<>();
        points.add(new Point2D.Double(0, 0));
        points.add(new Point2D.Double(0, h));
        points.add(new Point2D.Double(w, h));
        points.add(new Point2D.Double(w, 0));
        GeneralPath generalPath = Polygon.createPolygon(points);

        return new SelectedRegion(inRegion, colorRegion, 1, 1, width, height, generalPath);
    }
}
