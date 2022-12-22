package com.example.teamproject.brush;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

/**
 * @Description 图形绘制笔，可以绘制直线、矩形、圆形、椭圆形。该画笔不支持在选区中工作。
 * @Author
 * @Date    2022.12.22
 **/
public class ShapeBrush extends Brush{

    /**
     * 每种画笔都是单例模式
     */
    private static final ShapeBrush shapeBrush = new ShapeBrush();
    public static ShapeBrush getShapeBrush() {
        return shapeBrush;
    }
    private ShapeBrush(){}


    //当前选择的图形
    private int activeShape = 1;
    public static final int SHAPE_LINE= 1;
    public static final int SHAPE_RECT= 2;
    public static final int SHAPE_CIRCLE = 3;
    public static final int SHAPE_OVAL = 4;

    //是否填充区域
    private boolean fillShape = false;

    //颜色
    private Color color = Color.BLACK;
    //不透明度
    private double opacity = 0;

    //setter
    public void setActiveShape(int activeShape) {
        this.activeShape = activeShape;
    }

    public void setFillShape(boolean fillShape) {
        this.fillShape = fillShape;
    }
    public void setColor(Color color) {
        this.color = color;
        gc.setStroke(color);
    }
    //设置不透明度，范围0-1
    public void setOpacity(double opacity){
        this.opacity = opacity;
        this.color = new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), opacity);
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

                System.out.println("selectorBrush changes activeLayer to "+activeLayer);
                System.out.println("selectorBrush set canvas "+canvas);
            }
        }
    }



    double startX = 0, startY = 0;
    @Override
    public void drawBegin(double x, double y) {
        //当前存在选区时，禁止工作
        if(!SelectorBrush.getSelectorBrush().hasSelected()){
            isDrawing = true;
            startX = x;
            startY = y;
        }
    }

    @Override
    public void drawTo(double x, double y) {
        if(isDrawing){
            drawShape(x, y, effectCanvas);
        }
    }

    @Override
    public void drawEnd(double x, double y) {
        if(isDrawing){
            isDrawing = false;
            drawShape(x, y, canvas);
        }
    }

    private void drawShape(double x, double y, Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        effectGc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if(activeShape==SHAPE_LINE){
            gc.beginPath();
            gc.moveTo(startX, startY);
            gc.lineTo(x, y);
            gc.stroke();
            gc.closePath();
        }
        else{
            double maxX=Math.max(x,startX), minX=Math.min(x,startX), maxY=Math.max(y,startY), minY=Math.min(y,startY);
            double r = Math.max(maxX - minX, maxY - minY);
            //填充
            if(fillShape){
                if(activeShape==SHAPE_RECT){
                    gc.fillRect(minX, minY, maxX-minX, maxY-minY);
                }
                else if(activeShape==SHAPE_CIRCLE){
                    gc.fillOval(minX, minY, r, r);
                }
                else if(activeShape==SHAPE_OVAL){
                    gc.fillOval(minX, minY, maxX-minX, maxY-minY);
                }
            }
            //非填充
            else{
                if(activeShape==SHAPE_RECT){
                    gc.strokeRect(minX, minY, maxX-minX, maxY-minY);
                }
                else if(activeShape==SHAPE_CIRCLE){
                    gc.strokeOval(minX, minY, r, r);
                }
                else if(activeShape==SHAPE_OVAL){
                    gc.strokeOval(minX, minY, maxX-minX, maxY-minY);
                }
            }
        }



    }
}
