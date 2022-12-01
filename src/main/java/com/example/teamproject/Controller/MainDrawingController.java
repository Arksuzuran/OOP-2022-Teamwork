package com.example.teamproject.Controller;

import com.example.teamproject.Brush.Brush;
import com.example.teamproject.Brush.BrushPen;
import com.example.teamproject.Layers.Layer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 * @Description 画图的总控类 负责接受组件controller的信息 并传递给笔刷
 *              注意！目前仅为demo 因此对canvas的操作放在了此处
 *              实际对Canvas的操作并不在此类中，而应当下发到各笔刷中进行处理，以提高可拓展性
 * @Author CZX
 * @Date 2022.11.30
 **/
public class MainDrawingController {

    //图层列表
    ArrayList<Layer> layerList = new ArrayList<>();

    //当前选中的图层
    Layer activeLayer = null;

    Canvas activeCanvas = null;
    GraphicsContext activeGc = null;

    Brush activeBrush = new BrushPen();

    //当前该类是否在工作
    boolean isActive = false;
    boolean isDrawing = false;

    //单例模式
    private static final MainDrawingController MainDrawingController = new MainDrawingController();

    public static MainDrawingController getMDC() {
        return MainDrawingController;
    }

    //添加新图层
    public void addNewLayer(Layer layer){
        layerList.add(layer);
    }

    //设置当前图层
    public void setActiveLayer(Layer layer){
        this.activeLayer = layer;
        setNewCanvas(layer.getCanvas());
    }
    //删除图层
    public void delLayer(Layer layer){
        if(activeLayer == layer){
            activeLayer = null;
        }
        layerList.remove(layer);
    }

    //设置新的画布
    public void setNewCanvas(Canvas canvas){
        this.activeCanvas = canvas;
        this.activeGc = canvas.getGraphicsContext2D();

        isActive = true;
    }

    //设置新的笔刷
    public void setActiveBrush(Brush brush){
        this.activeBrush = activeBrush;
    }

    //开始划线
    public void lineBegin(double x, double y){
        if(isActive){
            isDrawing = true;
            activeGc.beginPath();
            activeGc.moveTo(x, y);
            activeGc.stroke();
        }
    }

    //在开始画线的前提下 使画笔移动到指定位置
    public void lineGoto(double x, double y){
        if(isActive && isDrawing){
            activeGc.lineTo(x, y);
            activeGc.stroke();
        }
    }
    //停止划线
    public void stopDrawing(){
        if(isActive){
            isDrawing = false;
        }
    }

}
