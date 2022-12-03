package com.example.teamproject.brush;

import com.example.teamproject.layers.Layer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * @Description 所有笔刷的父类
 * @Author
 * @Date
 **/
public abstract class Brush {


    protected Layer activeLayer;

    protected Canvas canvas;
    protected GraphicsContext gc;
    protected Canvas effectCanvas;
    protected GraphicsContext effectGc;

    //当前笔刷是否正在运动
    protected Boolean isDrawing = false;

    //画笔移动到新的图层
    public void setActiveLayer(Layer layer){
        activeLayer = layer;
        //如果移动到空图层 那么canvas和gc延迟设置
        if(layer!=null){
            this.canvas = layer.getCanvas();
            gc = canvas.getGraphicsContext2D();
            this.effectCanvas = layer.getEffectCanvas();
            effectGc = effectCanvas.getGraphicsContext2D();
        }
    }

    //开始画线
    public abstract void drawBegin(double x, double y);
    //正在画线 画线至
    public abstract void drawTo(double x, double y);
    //结束画线
    public void drawEnd(){
        isDrawing = false;
    }



}
