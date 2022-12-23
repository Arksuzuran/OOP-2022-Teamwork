package com.example.teamproject.controller;

import com.example.teamproject.effect.*;
import com.example.teamproject.tools.ImageFormConverter;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class ProcessController {

    @FXML
    private Slider ContrastSlider;

    @FXML
    private Slider BrightnessSlider;

    @FXML
    private Slider RedSlider;

    @FXML
    private Slider BlueSlider;

    @FXML
    private Slider GreenSlider;

    @FXML
    private Slider BlurSlider;

    @FXML
    private Slider GammaSlider;

    MainDrawingController mdc = MainDrawingController.getMDC();

    @FXML
    void OnBrightnessSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new ContrastEffect(), ContrastSlider.getValue(), BrightnessSlider.getValue(),  0);
        }
    }
    @FXML
    void OnBrightnessSliderReleased(MouseEvent event) {

    }

    @FXML
    void OnContrastSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new ContrastEffect(), ContrastSlider.getValue(), BrightnessSlider.getValue(),  0);
        }
    }
    @FXML
    void OnContrastSliderReleased(MouseEvent event) {

    }


    @FXML
    void OnGammaSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new GammaCorrectionEffect(), GammaSlider.getValue(), 0, 0);
        }
    }
    @FXML
    void OnGammaSliderReleased(MouseEvent event) {

    }

    @FXML
    void OnBlurSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new GaussianBlurEffect(), BlurSlider.getValue(), 0, 0);
        }
    }
    @FXML
    void OnBlurSliderReleased(MouseEvent event) {

    }
    @FXML
    void OnRedSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new HueEffect(), RedSlider.getValue(), 1, 1);
        }
    }

    @FXML
    void OnRedSliderReleased(MouseEvent event) {

    }
    @FXML
    void OnGreenSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new HueEffect(), 1, GreenSlider.getValue(), 1);
        }
    }

    @FXML
    void OnGreenSliderReleased(MouseEvent event) {

    }
    @FXML
    void OnBlueSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new HueEffect(), 1, 1, BlueSlider.getValue());
        }
    }
    @FXML
    void OnBlueSliderReleased(MouseEvent event) {

    }
    @FXML
    void OnConfigClicked(MouseEvent event) {
        if(mdc.isActive()){
            ImageEffect.originalCopy = ImageFormConverter.imageToMat(ImageFormConverter.canvasToImage(MainDrawingController.getMDC().getActiveLayer().getCanvas()));

        }
    }

}
