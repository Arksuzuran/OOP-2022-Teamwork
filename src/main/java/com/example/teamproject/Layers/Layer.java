package com.example.teamproject.Layers;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * @Description 所有layer的父类 待定
 * @Author
 * @Date
 **/
public class Layer {

    final private Canvas canvas;
    final private GraphicsContext gc;
    private boolean isActive = false;

    public Layer(Canvas canvas){
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    //获取该图层的画布
    public Canvas getCanvas() {
        return canvas;
    }

}
