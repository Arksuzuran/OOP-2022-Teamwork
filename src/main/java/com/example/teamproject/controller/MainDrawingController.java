package com.example.teamproject.controller;

import com.example.teamproject.brush.*;
import com.example.teamproject.structure.Layer;
import com.example.teamproject.tools.ImageFormConverter;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.ArrayList;

/**
 * @Description 画图的总控类 负责接受组件controller的信息 并传递给相应的处理类 且存储当前工具选择、图层创建情况
 * @Author CZX
 * @Date 2022.11.30
 **/
public class MainDrawingController {

    /**
     * 总效果图层 用于接受鼠标输入
     */
    private Canvas mainEffectCanvas;
    public Canvas getMainEffectCanvas() {
        return mainEffectCanvas;
    }

    /**
     * MainUIController
     */
    public MainUIController mainUIController;
    public MainUIController getMainUIController() {
        return mainUIController;
    }

    /**
     * 尺寸信息
     */
    public double sizeX;
    public double sizeY;

    /**
     * 图层
     */
    //图层列表
    private final ArrayList<Layer> layerList = new ArrayList<>();
    //总共新建了多少图层 用于给新图层命名
    private int totalLayerNum = 0;
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



    /**
     * 初始化当前作品 调用该方法相当于重新建立一个作品
     * @param mainEffectCanvas    新作品的顶层效果canvas
     * @param layer1    新作品的默认图层
     * @param mainUIController  UIController
     */
    public void initialize(Canvas mainEffectCanvas, Layer layer1, MainUIController mainUIController, double sizeX, double sizeY){
        this.mainEffectCanvas = mainEffectCanvas;
        this.mainUIController = mainUIController;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        //一系列属性均还原
        layerList.clear();
        activeBrush = null;
        addNewLayer(layer1);
        activeLayer = layer1;//自动选择第一个图层
        isActive = true;
    }


//    /**
//     * 在修改图层的image后调用。
//     * 将图层image的变化，合并到总的image里去
//     */
//    public void updateImageView(){
//        Image image = ImageFormConverter.mergeLayers(layerList);
//        imageView.setImage(image);
//        System.out.println("changes to imageView has merged");
//    }

    /**
     * 添加新图层
     * @param layer 要添加的图层
     */
    public void addNewLayer(Layer layer){
        totalLayerNum++;
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
            activeBrush.updateActiveLayer();
        System.out.println("set activeLayer: "+layer);
    }

    /**
     * @return 返回当前选中的图层
     */
    public Layer getActiveLayer() {
        return activeLayer;
    }

    /**
     * 删除图层
     * @param layer 要删除的图层
     */
    public void delLayer(Layer layer){
        //如果当前选中的图层即是要被删除的图层
        if(activeLayer == layer){
            activeLayer = null;
            if(activeBrush!=null){
                activeBrush.updateActiveLayer();
            }
        }
        layerList.remove(layer);
        System.out.println("mdc delete layer: "+layer);
    }
    /**
     * 获取新图层的默认名
     * @return  图层默认名
     */
    public String getNewLayerName() {
        String name = "图层"+(totalLayerNum+1);
        return name;
    }
    /**
     * 获取图层列表
     * @return  图层列表
     */
    public ArrayList<Layer> getLayerList() {
        return layerList;
    }

    /**
     * 选中新的笔刷
     * @param brushType 要选中的笔刷的类型
     */
    public void setActiveBrush(BrushType brushType){
        switch (brushType){
            case PEN -> this.activeBrush = PenBrush.getPenBrush();
            case SELECTOR -> this.activeBrush = SelectorBrush.getSelectorBrush();
            case ERASER -> this.activeBrush = EraserBrush.getEraserBrush();
        }
        activeBrush.updateActiveLayer();
        System.out.println("set new brush: "+activeBrush);
    }
    public Brush getActiveBrush(){
        return activeBrush;
    }


    /**
     * 控制笔刷从(x,y)处 开始画线
     * @param x x坐标
     * @param y y坐标
     */
    public void lineBegin(double x, double y){
        if(isActive && activeBrush!=null){
            if(activeLayer.isVisible())
                activeBrush.drawBegin(x,y);
            else
                ControllerSet.muc.sendMessage("您不能在不可见的图层上作画。");
        }
    }

    /**
     * 在开始画线的前提下 使笔刷继续移动到指定位置(x,y)
     * @param x x坐标
     * @param y y坐标
     */
    public void lineGoto(double x, double y){
        if(isActive && activeBrush!=null){
            if(activeLayer.isVisible())
                activeBrush.drawTo(x,y);
        }
    }

    /**
     * 使笔刷停止划线
     */
    public void stopDrawing(){
        if(isActive && activeBrush!=null){
            if(activeLayer.isVisible())
                activeBrush.drawEnd();
        }
    }

    /**
     * 合并所有layers 输出合成后的图像
     * @return  合并后的图像
     */
    public Image mergeAllLayers(){
        //合并所有layers
        GraphicsContext mainEffectGc = mainEffectCanvas.getGraphicsContext2D();
        for(int i=layerList.size()-1; i>=0; i--){
            Layer layer = layerList.get(i);
            WritableImage image = ImageFormConverter.canvasToImage(layer.getCanvas());
            mainEffectGc.drawImage(image, 0, 0);
        }
        //读出合并结果 清空用于合并的effectCanvas
        WritableImage result = ImageFormConverter.canvasToImage(mainEffectCanvas);
        mainEffectGc.clearRect(0, 0, mainEffectCanvas.getWidth(), mainEffectCanvas.getHeight());
        return result;
    }

}
