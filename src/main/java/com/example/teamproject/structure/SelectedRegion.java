package com.example.teamproject.structure;

import javafx.scene.image.WritableImage;

/**
 * @Description 选区类 用以记录选区信息
 * @Author  CZX
 * @Date    2022.12.12
 **/
public class SelectedRegion {

    public WritableImage selectedImage;
    private boolean[][] inRegion;//标记当前坐标的像素是否在选区内

    public SelectedRegion(WritableImage selectedImage, boolean[][] inRegion){
        this.selectedImage = selectedImage;
        this.inRegion = inRegion;
    }

    public boolean pointInRegion(int x, int y){
        if(x<=inRegion.length && y<=inRegion[x].length)
            return inRegion[x][y];
        return false;
    }
}
