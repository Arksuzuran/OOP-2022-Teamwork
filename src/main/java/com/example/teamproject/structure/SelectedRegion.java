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
    /**
     * @Description 标记当前坐标的像素是否在选区内
     **/
    private final boolean[][] inRegion;
    /**
     * @Description 选区内的颜色数组
     **/
    private final Color[][] colorRegion;
    /**
     * @Description 选区的边界
     **/
    public GeneralPath polygon;
    /**
     * @Description 该选区应当处于的位置
     **/
    public double x, y;
    /**
     * @Description 该选区矩形域的尺寸
     **/
    public double sizeX, sizeY;
    public SelectedRegion(boolean[][] inRegion, Color[][] colorRegion,
                          double x, double y, double sizeX, double sizeY, GeneralPath polygon){
        this.inRegion = inRegion;
        this.colorRegion = colorRegion;
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.polygon = polygon;
    }

    /**
     * @Description 查询点是否在区域内 要求使用相对坐标
     * @param x x
     * @param y y
     * @return  在区域内
     */
    public boolean pointInRegionRelative(int x, int y){
        if(x>=0 && y>=0 && x<inRegion.length && y<inRegion[x].length )
            return inRegion[x][y];
        return false;
    }

    /**
     * @Description 查询点是否在区域内 要求使用绝对坐标
     * @param x x
     * @param y y
     * @return  在区域内
     */
    public boolean pointInRegionAbsolute(int x, int y){
        x = x - (int)this.x;
        y = y - (int)this.y;
        if(x>=0 && y>=0 && x<inRegion.length && y<inRegion[x].length )
            return inRegion[x][y];
        return false;
    }

    /**
     *  @Description 设置当前选区所处的位置
     */
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Color[][] getColorRegion() {
        return colorRegion;
    }
}
