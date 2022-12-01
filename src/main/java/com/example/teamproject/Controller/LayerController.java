package com.example.teamproject.Controller;

import com.example.teamproject.Layers.Layer;
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
    @FXML
    private Button LayerCloseButton;
    @FXML
    private Button LayerVisibleButton;

    private Layer layer = null;

    //设置对应的图层
    public void setLayer(Layer layer){
        this.layer = layer;
    }



}
