package com.example.teamproject.controller;

import com.example.teamproject.effect.*;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class ProcessController {

    @FXML
    private Slider ContrastSlider;

    @FXML
    private Slider SaturationSlider;

    @FXML
    private Slider ColorSlider;

    @FXML
    private Slider BlurSlider;

    @FXML
    private Slider GammaSlider;

    MainDrawingController mdc = MainDrawingController.getMDC();

    @FXML
    void OnSaturationSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new ContrastEffect(), ContrastSlider.getValue(), SaturationSlider.getValue(),  0);
        }
    }

    @FXML
    void OnContrastSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new ContrastEffect(), ContrastSlider.getValue(), SaturationSlider.getValue(),  0);
        }
    }

    @FXML
    void OnGammaSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new GammaCorrectionEffect(), GammaSlider.getValue(), 0, 0);
        }
    }

    @FXML
    void OnColorSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new HueEffect(), ColorSlider.getValue(), ColorSlider.getValue(), ColorSlider.getValue());
        }
    }

    @FXML
    void OnBlurSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new GaussianBlurEffect(), BlurSlider.getValue(), 0, 0);
        }
    }



}
