package com.example.teamproject.Controller;

import com.example.teamproject.HelloApplication;
import com.example.teamproject.Layers.Layer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
    protected Button NewCanvasButton;
    @FXML
    protected Label welcomeText;
    @FXML
    protected VBox canvasBox;

    @FXML
    protected VBox LayerBox;

    @FXML
    protected Button NewLayerButton;

    //绘图主控的引用
    protected MainDrawingController mdc = MainDrawingController.getMDC();
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    //生成新画布
    protected Canvas createNewCanvas(){
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("canvas-view.fxml"));
        AnchorPane tmp;
        CanvasController canvasController;
        Canvas canvas = null;
        try {
            //加载场景
            tmp= loader.load();
            canvasBox.getChildren().add(tmp);

            //获取画布框的控制类(注意！该方法必须在load之后使用！)
            canvasController = loader.getController();
            //主控类通过该控制类获取其中的画布
            canvas = canvasController.getMainCanvas();
            mdc.setNewCanvas(canvas);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return canvas;
    }
    //生成新图层
    @FXML
    protected void onNewLayerButtonClick() {

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("layer-view.fxml"));
        AnchorPane tmp;
        LayerController layerController;
        try {
            //生成左侧边栏的图层UI
            tmp= loader.load();
            LayerBox.getChildren().add(tmp);

            //生成一个canvas
            Canvas canvas = createNewCanvas();
            //生成Layer类
            Layer layer = new Layer(canvas);
            //获取画布框的控制类 并将该控制类内绑定layer
            layerController = loader.getController();
            layerController.setLayer(layer);
            //主控类获取该layer
            mdc.addNewLayer(layer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * @Description 按下“新文件”按钮时，生成新画布.
     * @Author  CZX
     * @Date    2022.12.1
     */
    @FXML
    protected void OnNewCanvasButtonClick(){
        createNewCanvas();
    }

}