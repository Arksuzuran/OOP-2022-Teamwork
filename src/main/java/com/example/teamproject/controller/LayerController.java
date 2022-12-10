package com.example.teamproject.controller;

import com.example.teamproject.layers.Layer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


/**
 * @Description TODO
 * @Author
 * @Date
 **/
public class LayerController {
    @FXML
    private Label LayerNameLabel;

    private Layer layer = null;

    private MainDrawingController mdc = MainDrawingController.getMDC();
    //设置对应的图层
    public void setLayer(Layer layer){
        this.layer = layer;
    }

    @FXML
    public void OnLayerSelectButtonClick(){
        mdc.setActiveLayer(layer);
    }
    public void setLayerNameLabel(String s){
        LayerNameLabel.setText(s);
    }

}
