package com.example.teamproject.controller;

import com.example.teamproject.brush.Brush;
import com.example.teamproject.brush.PenBrush;
import javafx.event.ActionEvent;
import com.example.teamproject.brush.ShapeBrush;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;

import javax.swing.*;
import javafx.scene.paint.Color;

/**
 * @Description 控制画笔UI
 * @Author  CZX FGL
 * @Date    2022.12.15
 **/
public class PenUIController {

    public ImageView b1;
    public ImageView b2;
    public ImageView b3;
    public ImageView b4;
    public ImageView b5;
    public ImageView b6;
    public ImageView b7;
    public ImageView b8;
    public ImageView b9;

    @FXML
    protected ColorPicker PenColorPicker;//画笔的选色器

    //画笔粗细滑动条
    @FXML
    protected Slider PenWidthSlider;
    //画笔粗细宽度显示标签
    @FXML
    protected Label PenWidthLabel;

    @FXML
    //抖动修正滑动条
    protected Slider SmoothLevelSlider;
    //抖动修正值显示标签
    @FXML
    protected Label SmoothLevelLabel;

    @FXML
    protected Slider PenOpacitySlider;
    //画笔浓度显示标签
    @FXML
    protected Label PenOpacityLabel;
    //绘图主控的引用
    protected MainDrawingController mdc = MainDrawingController.getMDC();


    public void init(int op){
        b1.setImage(IconController.b11);
        b2.setImage(IconController.b22);
        b3.setImage(IconController.b33);
        b4.setImage(IconController.b44);
        b5.setImage(IconController.b55);
        b6.setImage(IconController.b66);
        b7.setImage(IconController.b77);
        b8.setImage(IconController.b88);
        b9.setImage(IconController.b99);

        if(op == 1)
            b1.setImage(IconController.b1);
        if(op == 2)
            b2.setImage(IconController.b2);
        if(op == 3)
            b3.setImage(IconController.b3);
        if(op == 4)
            b4.setImage(IconController.b4);
        if(op == 5)
            b5.setImage(IconController.b5);
        if(op == 6)
            b6.setImage(IconController.b6);
        if(op == 7)
            b7.setImage(IconController.b7);
        if(op == 8)
            b8.setImage(IconController.b8);
        if(op == 9)
            b9.setImage(IconController.b9);
    }


    /**
     * 颜色选择器被操作
     * 画笔的颜色选择器
     */
    public void updatePenColor(){

        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof PenBrush){
                ((PenBrush) brush).setColor(PenColorPicker.getValue());
            }
        }
    }
    @FXML
    protected void OnPenColorPickerSet(){
        updatePenColor();
    }

    /**
     * 如果当前有作品且选中了钢笔笔刷 那么调节其粗细
     * 更新对应UI
     */

    public void updatePenWidth(){
        double penWidth = PenWidthSlider.getValue();
        PenWidthLabel.setText(Integer.toString((int)penWidth));
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof PenBrush){
                ((PenBrush) brush).setLineWidth(penWidth);
            }
        }
    }

    @FXML
    protected void OnPenWidthSliderSet(){
        updatePenWidth();
    }
    @FXML
    protected void OnPenWidthDecreaseButtonClick(){
        PenWidthSlider.adjustValue(PenWidthSlider.getValue()-1);
        updatePenWidth();

    }
    @FXML
    protected void OnPenWidthIncreaseButtonClick(){
        PenWidthSlider.adjustValue(PenWidthSlider.getValue()+1);
        updatePenWidth();
    }

    ///抖动修正相关
    @FXML
    protected void OnSmoothLevelSliderSet(){
        updateSmoothLevel();
    }
    @FXML
    protected void OnSmoothDecreaseButtonClick(){
        SmoothLevelSlider.adjustValue(SmoothLevelSlider.getValue()-1);
        updateSmoothLevel();
    }
    @FXML
    protected void OnSmoothIncreaseButtonClick(){
        SmoothLevelSlider.setValue(SmoothLevelSlider.getValue()+1);
        updateSmoothLevel();
    }
    /**
     * 如果当前有作品且选中了钢笔笔刷 那么调节其抖动修正级别
     * 更新对应UI
     */
    public void updateSmoothLevel(){
        int smoothLevel = (int)SmoothLevelSlider.getValue();
        updateSmoothLevelLabel(smoothLevel);

        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof PenBrush){
                ((PenBrush) brush).setSmoothLevel(smoothLevel);
            }
        }
    }
    private void updateSmoothLevelLabel(double penWidth){
        int width = (int)penWidth;
        if(width<23)
            SmoothLevelLabel.setText(Integer.toString(width));
        else
            SmoothLevelLabel.setText("S"+Integer.toString(width-23));
    }

    public void updatePenOpacity(){
        double penWidth = PenOpacitySlider.getValue();
        PenOpacityLabel.setText(String.format("%.1f", penWidth));
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof PenBrush){
                ((PenBrush) brush).setOpacity(penWidth);
            }
        }
    }

    @FXML
    protected void OnPenOpacitySliderSet(){
        updatePenOpacity();
    }
    @FXML
    protected void OnPenOpacityDecreaseButtonClick(){
        PenOpacitySlider.adjustValue(PenOpacitySlider.getValue()-0.1);
        updatePenOpacity();
    }
    @FXML
    protected void OnPenOpacityIncreaseButtonClick(){
        PenOpacitySlider.adjustValue(PenOpacitySlider.getValue()+0.1);
        updatePenOpacity();
    }


    public void updateUIbyPenBrush(){
        PenBrush penBrush = PenBrush.getPenBrush();
        if(mdc.isActive()){
            double penWidth = penBrush.getLineWidth();
            double smooth = penBrush.getSmoothLevel();
            double opacity = penBrush.getOpacity();
            Color color = penBrush.getColor();

            PenWidthLabel.setText(Integer.toString((int)penWidth));
            PenWidthSlider.setValue(penWidth);
            updateSmoothLevelLabel(smooth);
            SmoothLevelSlider.setValue(smooth);
            PenColorPicker.setValue(color);
        }
    }

    @FXML
    void B1Clicked(ActionEvent event) {
        init(1);
        PenWidthSlider.setValue(5);
        updatePenWidth();
    }

    @FXML
    void B2Clicked(ActionEvent event) {
        init(2);
        PenWidthSlider.setValue(10);
        updatePenWidth();
    }

    @FXML
    void B3Clicked(ActionEvent event) {
        init(3);
        PenWidthSlider.setValue(15);
        updatePenWidth();
    }

    @FXML
    void B4Clicked(ActionEvent event) {
        init(4);
        PenWidthSlider.setValue(20);
        updatePenWidth();
    }

    @FXML
    void B5Clicked(ActionEvent event) {
        init(5);
        PenWidthSlider.setValue(25);
        updatePenWidth();
    }

    @FXML
    void B6Clicked(ActionEvent event) {
        init(6);
        PenWidthSlider.setValue(30);
        updatePenWidth();
    }

    @FXML
    void B7Clicked(ActionEvent event) {
        init(7);
        PenWidthSlider.setValue(35);
        updatePenWidth();
    }

    @FXML
    void B8Clicked(ActionEvent event) {
        init(8);
        PenWidthSlider.setValue(40);
        updatePenWidth();
    }

    @FXML
    void B9Clicked(ActionEvent event) {
        init(9);
        PenWidthSlider.setValue(45);
        updatePenWidth();
    }

}
