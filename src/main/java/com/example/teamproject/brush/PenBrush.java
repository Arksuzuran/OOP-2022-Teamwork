package com.example.teamproject.brush;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.LinkedList;

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

    /**
     * 实现抖动修正的数据结构
     */
    //存储路径点列表
    LinkedList<PathPoint> pointQueue = new LinkedList<>();
    //用于二阶抖动修正
    PathPoint startPoint, controlPoint, endPoint;
    //抖动修正等级
    private int smoothLevel = 1;
    public void setSmoothLevel(int level){
        smoothLevel = level;
    }
    /**
     * 开始绘画
     * @param x 起始点的x
     * @param y 起始点的y
     */
    @Override
    public void drawBegin(double x, double y) {
        initSmoothing();
        isDrawing = true;
        System.out.println("[pen] draw start. [color]"+color+" [smoothLevel]"+smoothLevel);
        if(smoothLevel <= 1){
            gc.beginPath();
            gc.moveTo(x, y);
            gc.stroke();
        }
        //等级2
        else if(smoothLevel == 2){
            startPoint = new PathPoint(x, y);
        }
        //等级3 4
        else{
            pointQueue.addLast(new PathPoint(x, y));
        }
    }

    /**
     * 绘制路径点
     * @param x 路径点x
     * @param y 路径点y
     */
    @Override
    public void drawTo(double x, double y) {
        if(isDrawing){
            if(smoothLevel <= 1){
                gc.lineTo(x, y);
                gc.stroke();
            }
            //等级2
            else if(smoothLevel == 2){
                //上一步刚刚推入起点p1 那么本步不需要画曲线 而是取p2作为控制点
                if(controlPoint==null){
                    controlPoint = new PathPoint(x, y);
                }
                else{
                    PathPoint inputPoint = new PathPoint(x, y);
                    endPoint = getMidPoint(controlPoint, inputPoint);
                    drawQuadraticCurve(startPoint, controlPoint, endPoint);
                    startPoint = endPoint;
                    controlPoint = inputPoint;
                }
            }
            //等级3
            else if(smoothLevel == 3){
                pointQueue.addLast(new PathPoint(x, y));
                //凑够三个路径点 就进行一次绘制
                if(pointQueue.size()==3){
                    drawQuadraticCurve(pointQueue.get(0), pointQueue.get(1), pointQueue.get(2));
                    pointQueue.removeFirst();   pointQueue.removeFirst();
                }
            }
            //等级4
            else if(smoothLevel == 4){
                pointQueue.addLast(new PathPoint(x, y));
                //凑够四个路径点 就进行一次绘制
                if(pointQueue.size()==4){
                    drawTripleCurve(pointQueue.get(0), pointQueue.get(1), pointQueue.get(2), pointQueue.get(3));
                    pointQueue.removeFirst();   pointQueue.removeFirst();   pointQueue.removeFirst();
                }
            }
        }
    }

    @Override
    public void drawEnd() {
        if(isDrawing){
            if(smoothLevel == 2){
                //此时没有了inputPoint 那么直接将剩下的startPoint和controlPoint相连即可
                drawLine(startPoint, controlPoint);
            }
            //三阶
            else if(smoothLevel>=3) {
                //根据余下路径点的数量进行绘制
                if(pointQueue.size()==3){
                    drawQuadraticCurve(pointQueue.get(0), pointQueue.get(1), pointQueue.get(2));
                }
                else if(pointQueue.size()==2){
                    drawLine(pointQueue.get(0), pointQueue.get(1));
                }
            }
            initSmoothing();
            System.out.println("[pen] draw end");
            isDrawing = false;
        }
    }

    /**
     * 初始化抖动修正所需的数据结构
     */
    private void initSmoothing(){
        startPoint = null;
        controlPoint = null;
        endPoint = null;
        pointQueue.clear();
    }
    private PathPoint getMidPoint(PathPoint p1, PathPoint p2){
        return new PathPoint((p1.x+ p2.x)/2, (p1.y+ p2.y)/2);
    }
    private void drawLine(PathPoint sp, PathPoint ep){
        if(sp==null || ep==null)
            return;
        gc.beginPath();
        gc.moveTo(sp.x, sp.y);
        gc.lineTo(ep.x, ep.y);
        gc.stroke();
        gc.closePath();
    }
    /**
     * 绘制二阶贝赛尔曲线
     * @param sp 起始点
     * @param cp 控制点
     * @param ep 结束点
     */
    private void drawQuadraticCurve(PathPoint sp, PathPoint cp, PathPoint ep){
        if(sp==null || ep==null || cp==null)
            return;
        gc.beginPath();
        gc.moveTo(sp.x, sp.y);
        gc.quadraticCurveTo(cp.x, cp.y, ep.x, ep.y);
        gc.stroke();
        gc.closePath();
    }
    /**
     * 绘制三阶贝塞尔曲线
     * @param sp    起始点
     * @param cp1   控制点1
     * @param cp2   控制点2
     * @param ep    结束点
     */
    private void drawTripleCurve(PathPoint sp, PathPoint cp1, PathPoint cp2, PathPoint ep){
        if(sp==null || ep==null || cp1==null || cp2==null)
            return;
        gc.beginPath();
        gc.moveTo(sp.x, sp.y);
        gc.bezierCurveTo(cp1.x, cp1.y, cp2.x, cp2.y, ep.x, ep.y);
        gc.stroke();
        gc.closePath();
    }

}
