package com.example.teamproject.brush;

import com.example.teamproject.controller.MainDrawingController;
import com.example.teamproject.layers.Layer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @Description 钢笔笔刷（即默认的直线）。
 * 对于钢笔来说，其所作的绘画均在canvas上直接显示。
 * @Author  CZX
 * @Date    2022.12.10
 **/

public class PenBrush extends Brush{

    /**
     * 每种画笔都是单例模式
     */
    private static final PenBrush Pen = new PenBrush();
    public static PenBrush getPenBrush() {
        return Pen;
    }

    //画布 直接在图层画布上作画
    protected Canvas canvas = null;
    protected GraphicsContext gc = null;
    protected Canvas effectCanvas = null;
    protected GraphicsContext effectGc = null;
    //画笔颜色 默认为黑
    Color color = Color.BLACK;

    public void setColor(Color color) {
        this.color = color;
        gc.setStroke(color);
    }


    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
        gc.setLineWidth(lineWidth);
    }

    //更新当前选中的图层
    @Override
    public void updateActiveLayer(){
        if (mdc.isActive()){
            activeLayer = mdc.getActiveLayer();
            if(activeLayer != null){
                //更新笔刷的引用
                canvas = activeLayer.getCanvas();
                gc = canvas.getGraphicsContext2D();
                effectCanvas = activeLayer.getEffectCanvas();
                effectGc = effectCanvas.getGraphicsContext2D();
                //笔刷设置为当前选择的状态
                setColor(color);
                setLineWidth(lineWidth);

                System.out.println("pen changes activeLayer to "+activeLayer);
                System.out.println("pen set canvas "+canvas);
            }
        }
    }

    //开始画
    @Override
    public void drawBegin(double x, double y) {
        System.out.println("[pen] draw start. color:"+color);
        isDrawing = true;
        if(smoothLevel <= 1){
            gc.beginPath();
            gc.moveTo(x, y);
            gc.stroke();
        }
    }

    //画笔移动到指定位置
    @Override
    public void drawTo(double x, double y) {
        if(isDrawing){
            if(smoothLevel <= 1){
                gc.lineTo(x, y);
                gc.stroke();
            }
        }
    }

    @Override
    public void drawEnd() {
        if(isDrawing){
            System.out.println("[pen] draw end");
            isDrawing = false;
        }
    }


}
