package com.example.teamproject.Brush;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * @Description 钢笔笔刷（即默认的直线），对于GraphicsContext的具体运动方式应当在笔刷类中实现
 * @Author  CZX
 * @Date    2022.11.30
 **/

public class BrushPen extends Brush{

    //画布的引用
    Canvas canvas;

    GraphicsContext gc;

    Boolean isActive;

    public BrushPen(Canvas canvas, GraphicsContext gc){
        this.canvas = canvas;
        this.gc = gc;
        isActive = false;
    }



}
