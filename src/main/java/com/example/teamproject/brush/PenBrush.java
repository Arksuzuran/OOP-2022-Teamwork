package com.example.teamproject.brush;

/**
 * @Description 钢笔笔刷（即默认的直线），对于GraphicsContext的具体运动方式应当在笔刷类中实现
 * @Author  CZX
 * @Date    2022.11.30
 **/

public class PenBrush extends Brush{

    //开始画
    @Override
    public void drawBegin(double x, double y) {
        isDrawing = true;
        gc.beginPath();
        gc.moveTo(x, y);
        gc.stroke();
    }

    //画笔移动到指定位置
    @Override
    public void drawTo(double x, double y) {
        gc.lineTo(x, y);
        gc.stroke();
    }


}
