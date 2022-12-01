package com.example.teamproject.Brush;

import javafx.scene.canvas.Canvas;

/**
 * @Description 所有笔刷的父类
 * @Author
 * @Date
 **/
public abstract class Brush {
    private Canvas canvas = null;

    public abstract void moveTo(double x, double y);
}
