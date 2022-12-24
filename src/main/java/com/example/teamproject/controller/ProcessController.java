package com.example.teamproject.controller;

import com.example.teamproject.effect.*;
import com.example.teamproject.tools.ImageFormConverter;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;


/**
 * @Description 所有笔刷的父类
 * @Author  FGL ZDW
 * @Date    2022.12.1
 **/
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

    /**
     * 亮度
     **/
    @FXML
    void OnBrightnessSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new ContrastEffect(), ContrastSlider.getValue(), BrightnessSlider.getValue(),  0);
        }
    }
    /**
     * 亮度
     **/
    @FXML
    void OnBrightnessSliderReleased(MouseEvent event) {

    }

    /**
     * 对比度
     **/
    @FXML
    void OnContrastSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new ContrastEffect(), ContrastSlider.getValue(), BrightnessSlider.getValue(),  0);
        }
    }
    /**
     * 对比度
     **/
    @FXML
    void OnContrastSliderReleased(MouseEvent event) {

    }

    /**
     * 伽马校正
     **/
    @FXML
    void OnGammaSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new GammaCorrectionEffect(), GammaSlider.getValue(), 0, 0);
        }
    }
    /**
     * 伽马校正
     **/
    @FXML
    void OnGammaSliderReleased(MouseEvent event) {

    }

    /**
     * 模糊
     **/
    @FXML
    void OnBlurSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new GaussianBlurEffect(), BlurSlider.getValue(), 0, 0);
        }
    }
    /**
     * 模糊
     **/
    @FXML
    void OnBlurSliderReleased(MouseEvent event) {

    }

    /**
     * 红色
     **/
    @FXML
    void OnRedSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new HueEffect(), RedSlider.getValue(), 1, 1);
        }
    }

    /**
     * 红色
     **/
    @FXML
    void OnRedSliderReleased(MouseEvent event) {

    }

    /**
     * 绿色
     **/
    @FXML
    void OnGreenSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new HueEffect(), 1, GreenSlider.getValue(), 1);
        }
    }

    /**
     * 绿色
     **/
    @FXML
    void OnGreenSliderReleased(MouseEvent event) {

    }

    /**
     * 蓝色
     **/
    @FXML
    void OnBlueSliderDragged(MouseEvent event) {
        if(mdc.isActive()){
            mdc.implementLayerEffect(new HueEffect(), 1, 1, BlueSlider.getValue());
        }
    }
    /**
     * 蓝色
     **/
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
