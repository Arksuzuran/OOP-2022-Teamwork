package com.example.teamproject.controller;

import com.example.teamproject.brush.Brush;
import com.example.teamproject.layers.Layer;
import com.example.teamproject.tools.ImageFormConverter;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * @Description 画图的总控类 负责接受组件controller的信息 并传递给相应的处理类
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

    /**
     * 图层
     */
    //图层列表
    private final ArrayList<Layer> layerList = new ArrayList<>();
    //当前选中的图层
    private Layer activeLayer = null;
    //一共新建了多少个图层
    private int layerNumber = 0;


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



    /**
     * 初始化当前作品 调用该方法相当于重新建立一个作品
     * @param imageView 新作品的imageView
     * @param canvas    新作品的canvas
     * @param layer1    新作品的默认图层
     * @param mainUIController  UIController
     */
    public void initialize(ImageView imageView, Canvas canvas, Layer layer1, MainUIController mainUIController){
        layerList.clear();
        this.effectCanvas = canvas;
        this.imageView = imageView;
        this.mainUIController = mainUIController;
        activeBrush = null;
        addNewLayer(layer1);
        activeLayer = layer1;
        isActive = true;

        updateImageView();
    }


    /**
     * 在修改图层的image后调用。
     * 将图层image的变化，合并到总的image里去
     */
    public void updateImageView(){
        Image image = ImageFormConverter.mergeLayers(layerList);
        imageView.setImage(image);
        System.out.println("changes to imageView has merged");
    }

    /**
     * 添加新图层
     * @param layer 要添加的图层
     */
    public void addNewLayer(Layer layer){
        layerNumber++;
        layerList.add(layer);
        System.out.println("add new layer: "+layer);
    }

    /**
     * 设置当前图层
     * @param layer 将当前选中的图层设置为layer
     */
    public void setActiveLayer(Layer layer){
        isActive = true;
        this.activeLayer = layer;
        if(activeBrush!=null)
            activeBrush.setActiveLayer(layer);
        System.out.println("set activeLayer: "+layer);
    }

    /**
     * @return 返回当前选中的图层
     */
    public Layer getActiveLayer() {
        return activeLayer;
    }

    //删除图层
    public void delLayer(Layer layer){
        //如果当前选中的图层即是要被删除的图层
        if(activeLayer == layer){
            activeLayer = null;
            if(activeBrush!=null){
                activeBrush.setActiveLayer(null);
            }
        }
        layerList.remove(layer);
        System.out.println("del layer: "+layer);
        //更新图像表现
        updateImageView();
    }
    //获取当前一共建了多少图层
    public int getLayerNumber() {
        return layerNumber;
    }


    /**
     * 选中新的笔刷
     * @param brush 要选中的笔刷的引用
     */
    public void setActiveBrush(Brush brush){
        this.activeBrush = brush;
        brush.setActiveLayer(activeLayer);
        System.out.println("set new brush: "+brush);
    }


    /**
     * 控制笔刷从(x,y)处 开始画线
     * @param x x坐标
     * @param y y坐标
     */
    public void lineBegin(double x, double y){
        if(isActive && activeBrush!=null){
            activeBrush.drawBegin(x,y);
        }
    }

    /**
     * 在开始画线的前提下 使笔刷继续移动到指定位置(x,y)
     * @param x x坐标
     * @param y y坐标
     */
    public void lineGoto(double x, double y){
        if(isActive && activeBrush!=null){
            activeBrush.drawTo(x,y);
        }
    }

    /**
     * 使笔刷停止划线
     */
    public void stopDrawing(){
        if(isActive && activeBrush!=null){
            activeBrush.drawEnd();
        }
    }

}
