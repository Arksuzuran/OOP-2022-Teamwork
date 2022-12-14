package com.example.teamproject.controller;

import com.example.teamproject.brush.SelectorBrush;
import com.example.teamproject.io.Open;
import com.example.teamproject.structure.Layer;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;


/**
 *  控制单个图层UI
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


    /**
     *  设置对应的图层
     */
    public void setLayer(Layer layer){
        this.layer = layer;
    }
    public Layer getLayer() {
        return layer;
    }
    public VBox getLayerPane() {
        return LayerBox;
    }

    /**
     *  选择图层
     */
    @FXML
    public void OnLayerSelectButtonClick(){
        //选区笔不工作时才允许切换图层
        if(!SelectorBrush.getSelectorBrush().hasSelected()) {
            mdc.setActiveLayer(layer);
            mdc.refreshLayers();
        }
        else{
            ControllerSet.muc.sendMessage("[切换图层] 您不能在已有选区的情况下切换图层，请先确认选区。");
        }

    }

    /**
     *  删除图层
     */
    @FXML
    public void OnLayerDeleteButtonClick(){
        if(muc!=null){
            muc.DeleteLayer(this);
        }
    }

    /**
     *  按下切换课可见与不可见按钮
     */
    @FXML
    public void onVisibleCheckBoxChanged(){
        layer.setVisible(VisibleCheckBox.isSelected());
    }

    /**
     * 按下导入图片按钮
     */
    @FXML
    public void OnImportImageButtonClick(){
        File file = Open.getInputFile();
        if (file!=null)
            layer.importImageToCanvas(file);
        else
            ControllerSet.muc.sendMessage("[打开文件] 文件未成功打开。可能是您手动取消了导入，或者文件格式不支持。");
    }


    /**
     *  设置名称
     */
    public void setLayerNameLabel(String s){
        LayerNameLabel.setText(s);
    }

}
