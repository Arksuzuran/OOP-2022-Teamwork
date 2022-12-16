package com.example.teamproject.controller;

import com.example.teamproject.brush.SelectorBrush;
import com.example.teamproject.io.Open;
import com.example.teamproject.structure.Layer;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.File;


/**
 * @Description 控制单个图层UI
 * @Author  CZX
 * @Date    2022.12.11
 **/
public class LayerUIController {

    @FXML
    private VBox LayerBox;
    @FXML
    private Label LayerNameLabel;

    @FXML
    private CheckBox VisibleCheckBox;

    private Layer layer = null;

    private MainDrawingController mdc = MainDrawingController.getMDC();

    public void setMuc(MainUIController muc) {
        this.muc = muc;
    }

    private MainUIController muc = null;


    //设置对应的图层
    public void setLayer(Layer layer){
        this.layer = layer;
    }
    public Layer getLayer() {
        return layer;
    }
    public VBox getLayerPane() {
        return LayerBox;
    }

    @FXML
    public void OnLayerSelectButtonClick(){
        //选区笔不工作时才允许切换图层
        if(!SelectorBrush.getSelectorBrush().hasSelected())
            mdc.setActiveLayer(layer);
        else
            System.out.println("cannot change layer when selecting");
    }
    @FXML
    public void OnLayerDeleteButtonClick(){
        if(muc!=null){
            muc.DeleteLayer(this);
        }
    }

    /**
     * 按下切换课可见与不可见按钮
     */
    @FXML
    public void onVisibleCheckBoxChanged(){
        layer.setVisible(VisibleCheckBox.isSelected());
    }

    /**
     *
     * 按下导入图片按钮
     */
    @FXML
    public void OnImportImageButtonClick(){
        File file = Open.getInputFile();
        layer.importImageToCanvas(file);
    }

    public void setLayerNameLabel(String s){
        LayerNameLabel.setText(s);
    }

}
