package com.example.teamproject.controller;

import com.example.teamproject.HelloApplication;
import com.example.teamproject.brush.Brush;
import com.example.teamproject.brush.BrushType;
import com.example.teamproject.brush.PenBrush;
import com.example.teamproject.layers.Layer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * @Description 目前所有界面上常驻的UI组件的Controller 根据后序开发进度 可以分设更多Controller
 * @Author  CZX
 * @Date    2022.11.30
 **/
public class MainUIController {

    @FXML
    protected Label welcomeText;

    @FXML
    protected VBox LayerBox;

    //按钮
    @FXML
    protected Button NewWorkButton;
    @FXML
    protected Button NewLayerButton;

    //调色板
    @FXML
    protected ColorPicker ColorChooser;
    //画笔粗细滑动条
    @FXML
    protected Slider PenWidthSlider;
    //画笔粗细宽度显示标签
    @FXML
    protected Label PenWidthLabel;
    //绘图区
    @FXML
    protected ScrollPane DrawingScrollPane;

    /**
     * 画图层各部分的引用 在创建新画布后必须对此进行更新！否则后端无法工作！
     */


    protected Canvas mainEffectCanvas = null;
    protected Pane mainDrawingPane = null;
    protected CanvasController canvasController = null;

    //画布尺寸
    protected double sizeX = 800;
    protected double sizeY = 600;
    protected boolean hasActiveWork = false;
    //绘图主控的引用
    protected MainDrawingController mdc = MainDrawingController.getMDC();


    /**
     * 创建新作品
     * 该方法会重置主控类
     * 创建新作品需要4步：
     * 1.加载画布Pane和mainEffectPane
     * 2.创建第一个图层layer1
     * 3.用canvas、layer1、this初始化主控类
     *
     * 后续优化：当前没有作品时会在画图板的位置显示其他UI
     */
    protected void createNewWork(){
        createNewCanvasField();
        hasActiveWork = true;
        Layer layer1 = createNewLayer();
        mdc.initialize(mainEffectCanvas, layer1, this, sizeX, sizeY);
        //layer1.setImage(new Image("E:\\JavaTeamwork\\OOP-2022-Teamwork\\8.png"));
    }

    /**
     * 生成新的画图板区域 该方法会直接重置画图区
     * 该方法需要前端根据自己所写的fxml更改
     */
    public void createNewCanvasField(){
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("canvas-view.fxml"));
        try {
            //加载画布区域
            Pane pane = loader.load();

            DrawingScrollPane.setContent(pane);//注意：ScrollPane内只允许有一个Node

            //获取画布区域的控制类(注意！该方法必须在load之后使用！)
            canvasController = loader.getController();

            //获取自带的效果画布 以及画图区域pane的引用
            mainEffectCanvas = canvasController.getMainEffectCanvas();
            mainDrawingPane = canvasController.getDrawingPane();

            //修改大小至预定值
            mainDrawingPane.setPrefWidth(sizeX);
            mainDrawingPane.setPrefHeight(sizeY);

            mainEffectCanvas.setWidth(sizeX);
            mainEffectCanvas.setHeight(sizeY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 点击“新建图层按钮“
     * 生成新图层 并添加至总控类中
     */
    @FXML
    protected void onNewLayerButtonClick() {
        Layer layer = createNewLayer();
        mdc.addNewLayer(layer);
    }

    /**
     * 新建一个图层对象 及其对应的边栏UI 并初始化该对象
     * 该方法需要前端同学根据自己所写的fxml更改这句话：FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("layer-view.fxml"));
     * @return  返回该图层的引用
     */
    //生成新图层
    public Layer createNewLayer(){
        //如果当前没有作品 那么是不可能创建图层的
        if(!hasActiveWork){
            System.out.println("No active work. Cannot create new layer");
            return null;
        }
        //载入图层UI的fxml
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("layer-view.fxml"));

        try {
            //生成左侧边栏的图层UI
            AnchorPane tmp;
            tmp= loader.load();
            LayerBox.getChildren().add(tmp);

            //添加两个Canvas

            //效果画布
            Canvas effectCanvas = createNewCanvasAddingToPane();
            //主画布
            Canvas canvas = createNewCanvasAddingToPane();

            //获取画布UI的控制类
            LayerController layerController = loader.getController();
            //UI控制类和layer互相持有对方的引用
            Layer layer = new Layer(canvas, effectCanvas, mainEffectCanvas, layerController);
            layerController.setLayer(layer);
            //生成Layer类
            return layer;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据当前选择的尺寸 创建新的画布 并将其加入mainDrawingPane的底部
     * @return 新画布的引用
     */
    public Canvas createNewCanvasAddingToPane(){
        Canvas canvas = new Canvas(sizeX, sizeY);
        canvas.setOpacity(1);
        canvas.setStyle("-fx-background-color: WHITE");
        canvasController.addNewCanvasAtBack(canvas);
        return canvas;
    }

    /**
     * 当按下“选择钢笔按钮时”
     * 后续需要更改 这里在传入前就需要设置好笔刷参数
     */
    //选中铅笔
    @FXML
    protected void onPenBrushButtonClick(){
        //只有主控激活时才能选择笔刷
        if(mdc.isActive()){

            mdc.setActiveBrush(BrushType.PEN);
            //根据当前UIController里 选中的笔刷信息 来设置笔刷对象的属性
            updatePenColor();
            updatePenWidth();
        }
    }

    /**
     * @Description 按下“新空作品”按钮时，生成新的空作品.
     * @Author  CZX
     * @Date    2022.12.4
     */

    @FXML
    protected void OnNewWorkButtonClick(){
        System.out.println("NewWorkButtonClick");
        if(!hasActiveWork)
            createNewWork();
    }

    /**
     * 颜色选择器被操作
     * 如果当前有作品且选中了钢笔笔刷 那么调节其颜色
     */
    public void updatePenColor(){
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof PenBrush){
                ((PenBrush) brush).setColor(ColorChooser.getValue());
            }
        }
    }
    @FXML
    protected void OnColorPickerSet(){
        updatePenColor();
    }

    /**
     * 如果当前有作品且选中了钢笔笔刷 那么调节其粗细
     * 更新对应UI
     */
    public void updatePenWidth(){
        double penWidth = PenWidthSlider.getValue();
        PenWidthLabel.setText(Integer.toString((int)penWidth));

        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof PenBrush){
                ((PenBrush) brush).setLineWidth(penWidth);
            }
        }
    }
    @FXML
    protected void OnPenWidthSliderSet(){
        updatePenWidth();
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}