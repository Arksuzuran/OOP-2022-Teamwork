package com.example.teamproject.brush;

import com.example.teamproject.controller.MainDrawingController;
import com.example.teamproject.structure.Layer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * @Description 所有笔刷的父类
 * @Author  CZX
 * @Date    2022.12.1
 **/
public abstract class Brush {

    MainDrawingController mdc = MainDrawingController.getMDC();

    //画布
    protected Canvas canvas = null;
    protected GraphicsContext gc = null;
    protected Canvas effectCanvas = null;
    protected GraphicsContext effectGc = null;
    protected Canvas mainEffectCanvas = null;
    protected GraphicsContext mainEffectGc = null;

    protected Layer activeLayer = null;

    //当前笔刷是否正在运动
    protected Boolean isDrawing = false;

    //画笔粗细 默认为1
    double lineWidth = 1;

    //画笔移动到新的图层
    public void updateActiveLayer(){
        if (mdc.isActive()){
            activeLayer = mdc.getActiveLayer();
        }
    }
    //开始画线
    public abstract void drawBegin(double x, double y);
    //正在画线 画线至
    public abstract void drawTo(double x, double y);
    //结束画线
    public abstract void drawEnd(double x, double y);

}



