package com.example.teamproject.controller;

import com.example.teamproject.brush.SelectorBrush;
import com.example.teamproject.structure.Layer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


/**
 * @Description TODO
 * @Author
 * @Date
 **/
public class LayerController {

    @FXML
    private AnchorPane LayerPane;
    @FXML
    private Label LayerNameLabel;

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
    public AnchorPane getLayerPane() {
        return LayerPane;
    }

    @FXML
    public void OnLayerSelectButtonClick(){
        //选区笔不工作时才允许切换图层
        if(!SelectorBrush.getSelectorBrush().hasSelected())
            mdc.setActiveLayer(layer);
    }
    @FXML
    public void OnLayerDeleteButtonClick(){
        if(muc!=null){
            muc.DeleteLayer(this);
        }
    }

    public void setLayerNameLabel(String s){
        LayerNameLabel.setText(s);
    }

}
