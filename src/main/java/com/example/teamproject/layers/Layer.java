package com.example.teamproject.layers;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * @Description 所有layer的父类 待定
 * @Author
 * @Date
 **/
public class Layer{

    //画布
    final private Canvas canvas;
    //效果画布 用以显示绘图时的提示效果
    final private Canvas effectCanvas;
    //图层命名
    private String layerName;

    public Layer(Canvas canvas, Canvas effectCanvas){
        this.canvas = canvas;
        this.effectCanvas = effectCanvas;
    }

    //获取该图层的画布
    public Canvas getCanvas() {
        return canvas;
    }
    //获取该图层的效果画布
    public Canvas getEffectCanvas() {
        return effectCanvas;
    }

    //设置图层名称
    public void setLayerName(String s){
        layerName = s;
    }
}
