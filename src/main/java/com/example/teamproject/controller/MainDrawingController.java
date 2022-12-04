package com.example.teamproject.controller;

import com.example.teamproject.brush.Brush;
import com.example.teamproject.layers.Layer;
import com.example.teamproject.tools.ImageFormConverter;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * @Description 画图的总控类 负责接受组件controller的信息 并传递给相应的处理类：笔刷、effect
 *
 * @Author CZX
 * @Date 2022.11.30
 **/
public class MainDrawingController {

    /**
     * 效果图层 用于接受鼠标输入
     */
    private Canvas effectCanvas;
    public Canvas getEffectCanvas() {
        return effectCanvas;
    }
    /**
     * 图片展示 用于在UI界面显示当前计算结果
     */
    public ImageView imageView;//
    public ImageView getImageView() {
        return imageView;
    }
    /**
     * MainUIController
     */
    public MainUIController mainUIController;
    public MainUIController getMainUIController() {
        return mainUIController;
    }

    //图层列表
    private final ArrayList<Layer> layerList = new ArrayList<>();

    //当前选中的图层
    private Layer activeLayer = null;

    //当前选中的笔刷
    private Brush activeBrush;



    //当前该类是否在工作
    private boolean isActive = false;
    public boolean isActive() {
        return isActive;
    }

    //单例模式
    private static final MainDrawingController MainDrawingController = new MainDrawingController();
    public static MainDrawingController getMDC() {
        return MainDrawingController;
    }


    //初始化当前作品
    public void initialize( ImageView imageView, Canvas canvas, Layer layer1, MainUIController mainUIController){
        layerList.clear();
        this.effectCanvas = canvas;
        this.imageView = imageView;
        this.mainUIController = mainUIController;
        activeBrush = null;
        addNewLayer(layer1);
        activeLayer = layer1;
        isActive = true;
    }

    //刷新对图层的更改
    public void updateImageView(){
        Image image = ImageFormConverter.mergeLayers(layerList);
        imageView.setImage(image);
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
