package com.example.teamproject.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * @Description 监听画布上的所有操作 并把对画布的操作传递给MainDrawingController进行处理
 * @Author  CZX
 * @Date    2022.11.30
 **/
public class CanvasController {

    //UI中的各个控件 加入@FXML标签即可直接获取该控件 例如下面的MainCanvas就是UI中名为MainCanvas的Canvas组件
    @FXML
    public AnchorPane AnchorPane;

    MainDrawingController mdc = MainDrawingController.getMDC();
    @FXML
    private Canvas MainCanvas;

    public Canvas getMainCanvas(){
        return MainCanvas;
    }

    /**
     * 按下鼠标 进入开始划线的状态
     * @param mouseEvent 鼠标事件
     *
     */
    @FXML
    //鼠标按下 将信息传递到MainUIController
    private void OnCanvasMousePressed(MouseEvent mouseEvent){
        mdc.lineBegin(mouseEvent.getX(), mouseEvent.getY());
    }
    @FXML
    //鼠标拖拽 将信息传递到MainUIController
    private void OnCanvasMouseDragged(MouseEvent mouseEvent){
        mdc.lineGoto(mouseEvent.getX(), mouseEvent.getY());
    }
    @FXML
    //鼠标松开 将信息传递到MainUIController
    private void OnCanvasMouseReleased(){
        mdc.stopDrawing();
    }
}
