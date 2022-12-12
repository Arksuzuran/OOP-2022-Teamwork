package com.example.teamproject.structure;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.geom.GeneralPath;

/**
 * @Description 选区类 用以记录选区信息
 * @Author  CZX
 * @Date    2022.12.12
 **/
public class SelectedRegion {

    public WritableImage selectedImage;
    private final boolean[][] inRegion;//标记当前坐标的像素是否在选区内

    private final Color[][] colorRegion;//选区内的颜色数组
    public GeneralPath polygon;//选区的边界
    //该选区应当处于的位置
    public double x, y;
    //该选区矩形域的尺寸
    public double sizeX, sizeY;
    public SelectedRegion(WritableImage selectedImage, boolean[][] inRegion, Color[][] colorRegion,
                          double x, double y, double sizeX, double sizeY, GeneralPath polygon){
        this.selectedImage = selectedImage;
        this.inRegion = inRegion;
        this.colorRegion = colorRegion;
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.polygon = polygon;
    }

    /**
     * 查询点是否在区域内 要求使用相对坐标
     * @param x x
     * @param y y
     * @return  在区域内
     */
    public boolean pointInRegion(int x, int y){
        if(x>=0 && y>=0 && x<inRegion.length && y<inRegion[x].length )
            return inRegion[x][y];
        return false;
    }

    /**
     *  设置当前选区所处的位置
     */
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }
    public Color[][] getColorRegion() {
        return colorRegion;
    }
}
