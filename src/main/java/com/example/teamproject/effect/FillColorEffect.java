package com.example.teamproject.effect;

import com.example.teamproject.structure.SelectedRegion;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

/**
 * @Description 将所给选区的选中部分染为指定颜色
 * @Author  CZX
 * @Date    2022.12.12
 **/
public class FillColorEffect {

    /**
     * @Description 给选区染色
     * @param selectedRegion    选区
     * @param color 要染的颜色
     */
    public static void FillSelectedRegion(SelectedRegion selectedRegion, Color color) {
//        WritableImage image = selectedRegion.selectedImage;
//        PixelWriter pixelWriter = image.getPixelWriter();
        Color[][] colors = selectedRegion.getColorRegion();
        //遍历所有可能的像素点
        for (int x = 0; x < selectedRegion.sizeX; x++) {
            for (int y = 0; y < selectedRegion.sizeY; y++) {
                //在选区内 则染色
                if (selectedRegion.pointInRegionRelative(x, y)) {
                    colors[x][y] = color;
                }
            }
        }
    }
    /**
     * @Description 给选区外的区域染色
     * @param selectedRegion    选区
     * @param color 要染的颜色
     * @param canvas    画布
     */
    public static void FillUnselectedRegion(SelectedRegion selectedRegion, Color color, Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        PixelWriter pixelWriter = gc.getPixelWriter();
        Color[][] colors = selectedRegion.getColorRegion();
        //遍历所有可能的像素点
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                //不在选区内 则染色
                if (!selectedRegion.pointInRegionAbsolute(x, y)) {
                    pixelWriter.setColor(x, y, color);
                }
            }
        }
    }
}
