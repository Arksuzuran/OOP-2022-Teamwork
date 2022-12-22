package com.example.teamproject.controller;

import com.example.teamproject.structure.Layer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

/**
 * @Description 控制绘图区   监听画布上的所有操作 并把对画布的操作传递给MainDrawingController进行处理
 * @Author  CZX
 * @Date    2022.11.30
 **/
public class CanvasController {

    //UI中的各个控件 加入@FXML标签即可直接获取该控件 例如下面的MainCanvas就是UI中名为MainCanvas的Canvas组件
    @FXML
    public Pane DrawingPane;
    @FXML
    private Canvas MainEffectCanvas;
    MainDrawingController mdc = MainDrawingController.getMDC();
    public Canvas getMainEffectCanvas(){
        return MainEffectCanvas;
    }
    public Pane getDrawingPane(){
        return DrawingPane;
    }

    /**
     * 在底部添加新的画布
     * @param canvas 添加的画布
     */
    public void addNewCanvasAtBack(Canvas canvas){
        DrawingPane.getChildren().add(canvas);
        canvas.toBack();
        MainEffectCanvas.toFront();
        DrawingPane.setStyle("-fx-background-color: WHITE");
//        canvas.setStyle("-fx-background-color: WHITE");
//        MainEffectCanvas.setStyle("-fx-background-color: WHITE");
    }

    /**
     * 删除指定图层的canvas
     * @param layer 要删除的图层
     */
    public void deleteLayer(Layer layer){
        if(DrawingPane.getChildren().size() <=1)
            return;
        Canvas canvas = layer.getCanvas();
        Canvas effectCanvas = layer.getEffectCanvas();
        DrawingPane.getChildren().remove(canvas);
        DrawingPane.getChildren().remove(effectCanvas);
    }

    /**
     * 获取当前顶层的画布属于哪个layer
     * @return 找到的layer
     */
    public Layer getTopLayer(){
        ArrayList<Layer> arrayList = mdc.getLayerList();
        Node node = DrawingPane.getChildren().get(1);
        for (Layer layer : arrayList){
            if(layer.getCanvas() == node || layer.getEffectCanvas() == node){
                return layer;
            }
        }
        return null;
    }


    /**
     * 按下鼠标 进入开始划线的状态
     * @param mouseEvent 鼠标事件
     */
    @FXML
    //鼠标按下 将信息传递到MainUIController
    private void OnCanvasMousePressed(MouseEvent mouseEvent){
        for (Node node : DrawingPane.getChildren()){
            System.out.println(node);
        }
        mdc.lineBegin(mouseEvent.getX(), mouseEvent.getY());
    }
    @FXML
    //鼠标拖拽 将信息传递到MainUIController
    private void OnCanvasMouseDragged(MouseEvent mouseEvent){
        mdc.lineGoto(mouseEvent.getX(), mouseEvent.getY());
    }
    @FXML
    //鼠标松开 将信息传递到MainUIController
    private void OnCanvasMouseReleased(MouseEvent mouseEvent){
        mdc.stopDrawing(mouseEvent.getX(), mouseEvent.getY());
    }

    @FXML
    private void updateCord(MouseEvent mouseEvent){
        ControllerSet.muc.updatePositionText((int)mouseEvent.getX() + ", " + (int)mouseEvent.getY() + "像素");
    }
}
