package com.example.teamproject.Controller;

import com.example.teamproject.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    public Button NewCanvasButton;
    @FXML
    private Label welcomeText;
    @FXML
    private VBox canvasBox;

    //绘图主控的引用
    MainDrawingController mdc = MainDrawingController.getMDC();
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }


    /**
     * @Description 按下“新文件”按钮时，生成新画布.
     * @Author  CZX
     * @Date    2022.12.1
     */
    @FXML
    protected void OnNewCanvasButtonClick(){
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("canvas-view.fxml"));
        AnchorPane tmp;
        CanvasController canvasController;
        try {
            //加载场景
            tmp= loader.load();
            canvasBox.getChildren().add(tmp);

            //获取画布框的控制类(注意！该方法必须在load之后使用！)
            canvasController = loader.getController();
            //主控类通过该控制类获取其中的画布
            mdc.setNewCanvas(canvasController.getMainCanvas());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}