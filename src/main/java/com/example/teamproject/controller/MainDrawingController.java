package com.example.teamproject.controller;

import com.example.teamproject.brush.Brush;
import com.example.teamproject.brush.PenBrush;
import com.example.teamproject.layers.Layer;
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
    private ArrayList<Layer> layerList = new ArrayList<>();

    //当前选中的图层
    private Layer activeLayer = null;

    //当前选中的笔刷
    private Brush activeBrush;

    //当前该类是否在工作
    private boolean isActive = false;

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
        isActive = true;
        this.activeLayer = layer;
        if(activeBrush!=null)
            activeBrush.setActiveLayer(layer);
    }

    //删除图层
    public void delLayer(Layer layer){
        if(activeLayer == layer){
            activeLayer = null;
        }
        layerList.remove(layer);
        if(activeBrush!=null){
            activeBrush.setActiveLayer(null);
        }
    }

    //设置新的笔刷
    public void setActiveBrush(Brush brush){
        this.activeBrush = brush;
        brush.setActiveLayer(activeLayer);
    }

    //开始画线
    public void lineBegin(double x, double y){
        if(isActive && activeBrush!=null){
            activeBrush.drawBegin(x,y);
        }
    }

    //在开始画线的前提下 使画笔移动到指定位置
    public void lineGoto(double x, double y){
        if(isActive && activeBrush!=null){
            activeBrush.drawTo(x,y);
        }
    }
    //停止划线
    public void stopDrawing(){
        if(isActive && activeBrush!=null){
            activeBrush.drawEnd();
        }
    }

}
