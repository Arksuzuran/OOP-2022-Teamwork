package com.example.teamproject.controller;

import com.example.teamproject.HelloApplication;
import com.example.teamproject.brush.Brush;
import com.example.teamproject.brush.PenBrush;
import com.example.teamproject.layers.Layer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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

    //Box
    @FXML
    protected VBox canvasBox;
    @FXML
    protected VBox LayerBox;

    //按钮
    @FXML
    protected Button NewWorkButton;
    @FXML
    protected Button NewLayerButton;

    /**
     * 画图层各部分的引用 在创建新画布后必须对此进行更新！否则后端无法工作！
     */
    protected boolean hasActiveWork = false;
    protected ImageView imageView = null;
    protected Canvas effectCanvas = null;
    protected CanvasController canvasController = null;

    //绘图主控的引用
    protected MainDrawingController mdc = MainDrawingController.getMDC();

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }


    /**
     *
     */

    /**
     * 创建新作品
     * 该方法会重置主控类
     * 创建新作品需要4步：
     * 1.创建画图板UI
     * 2.透过画图板UI的controller获取其中的canvas和imageView
     * 3.创建第一个图层layer1
     * 4.用canvas、imageView、layer1初始化主控类
     *
     * 后续优化：当前没有作品时会在画图板的位置显示其他UI
     */
    protected void createNewWork(){
        createNewCanvasField();
        hasActiveWork = true;
        Layer layer1 = createNewLayer();
        mdc.initialize(imageView, effectCanvas, layer1, this);
    }

    //生成新图层 并添加至总控类中
    @FXML
    protected void onNewLayerButtonClick() {
        Layer layer = createNewLayer();
        mdc.addNewLayer(layer);
    }

    /**
     * 该方法需要前端同学根据自己所写的fxml更改
     */
    //生成新图层 返回该图层的引用
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

            //获取控制类
            LayerController layerController = loader.getController();
            //生成Layer类
            Layer layer = new Layer(imageView, effectCanvas, layerController);

            return layer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 该方法需要前端同学根据自己所写的fxml更改
     */
    //生成新的画图板区域 该方法会重置imageView、canvasController、effectCanvas
    public void createNewCanvasField(){
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("canvas-view.fxml"));

        try {
            //加载场景
            AnchorPane tmp= loader.load();
            canvasBox.getChildren().add(tmp);

            //获取画布框的控制类(注意！该方法必须在load之后使用！)
            canvasController = loader.getController();

            //获取画布等的引用
            effectCanvas = canvasController.getEffectCanvas();
            imageView = canvasController.getImageView();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 后续需要更改 这里在传入前就需要设置好笔刷参数
     */
    //选中铅笔
    @FXML
    protected void onPenBrushButtonClick(){
        Brush penBrush = new PenBrush();

        //设置笔刷的代码




        //
        //只有主控激活时才能选择笔刷
        if(mdc.isActive()){
            System.out.println(penBrush);
            mdc.setActiveBrush(penBrush);
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
        createNewWork();
    }

}