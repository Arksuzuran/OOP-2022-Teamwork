package com.example.teamproject.controller;

import com.example.teamproject.brush.Brush;
import com.example.teamproject.brush.PenBrush;
import com.example.teamproject.brush.SelectorBrush;
import com.example.teamproject.brush.ShapeBrush;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
/**
 * @Description ShapeUIController
 * @Author  CZX CR
 * @Date    2022.12.15
 **/
public class ShapeUIController {

    @FXML
    private ColorPicker ShapeColorPicker;

    @FXML
    private CheckBox FillShapeCheckBox;
    /**
     * @Description 画笔粗细滑动条
     */
    @FXML
    protected Slider PenWidthSlider;
    /**
     * @Description 画笔粗细宽度显示标签
     */
    @FXML
    protected Label PenWidthLabel;
    /**
     * @Description 画笔浓度滑动条
     */
    @FXML
    protected Slider PenOpacitySlider;
    /**
     * @Description 画笔浓度显示标签
     */
    @FXML
    protected Label PenOpacityLabel;
    /**
     * @Description 绘图主控的引用
     */
    protected MainDrawingController mdc = MainDrawingController.getMDC();

    @FXML
    void onRectButtonClicked(ActionEvent event) {
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof ShapeBrush){
                ((ShapeBrush) brush).setActiveShape(ShapeBrush.SHAPE_RECT);
                ControllerSet.muc.sendMessage("[图形笔] 切换为绘制矩形。");
            }
        }
    }

    @FXML
    void onCircleButtonClicked(ActionEvent event) {
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof ShapeBrush){
                ((ShapeBrush) brush).setActiveShape(ShapeBrush.SHAPE_CIRCLE);
                ControllerSet.muc.sendMessage("[图形笔] 切换为绘制圆形。");
            }
        }
    }

    @FXML
    void onOvalButtonClicked(ActionEvent event) {
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof ShapeBrush){
                ((ShapeBrush) brush).setActiveShape(ShapeBrush.SHAPE_OVAL);
                ControllerSet.muc.sendMessage("[图形笔] 切换为绘制椭圆。");
            }
        }
    }

    @FXML
    void onLineButtonClicked(ActionEvent event) {
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof ShapeBrush){
                ((ShapeBrush) brush).setActiveShape(ShapeBrush.SHAPE_LINE);
                ControllerSet.muc.sendMessage("[图形笔] 切换为绘制直线。");
            }
        }
    }

    /**
     * @Description 颜色选择器被操作,画笔的颜色选择器
     */
    @FXML
    protected void OnShapeColorPickerSet(){
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof ShapeBrush){
                ((ShapeBrush) brush).setColor(ShapeColorPicker.getValue());
                ControllerSet.muc.sendMessage("[图形笔] 更换颜色为"+ShapeColorPicker.getValue()+"。");
            }
        }
    }

    /**
     * @Description 切换是否为填充态
     */
    @FXML
    protected void OnFillShapeCheckBoxSet(){
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof ShapeBrush){
                ((ShapeBrush) brush).setFillShape(FillShapeCheckBox.isSelected());
                if(FillShapeCheckBox.isSelected())
                    ControllerSet.muc.sendMessage("[图形笔] 更换绘制策略为填充。");
                else
                    ControllerSet.muc.sendMessage("[图形笔] 更换绘制策略为边框。");
            }
        }
    }

    public void updatePenWidth(){
        double penWidth = PenWidthSlider.getValue();
        PenWidthLabel.setText(Integer.toString((int)penWidth));
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof ShapeBrush){
                ((ShapeBrush) brush).setLineWidth(penWidth);
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

    public void updatePenOpacity(){
        double penWidth = PenOpacitySlider.getValue();
        PenOpacityLabel.setText(String.format("%.1f", penWidth));
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof ShapeBrush){
                ((ShapeBrush) brush).setOpacity(penWidth);
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

    public void updateUIbyShapeBrush(){
        ShapeBrush shapeBrush = ShapeBrush.getShapeBrush();
        if(mdc.isActive()){
            double penWidth = shapeBrush.getLineWidth();
            double opacity = shapeBrush.getOpacity();
            boolean isFillShape = shapeBrush.isFillShape();
            Color color = shapeBrush.getColor();

            PenWidthLabel.setText(Integer.toString((int)penWidth));
            PenWidthSlider.setValue(penWidth);
            PenOpacityLabel.setText(String.format("%.1f", opacity));
            PenOpacitySlider.setValue(opacity);
            FillShapeCheckBox.setSelected(isFillShape);
            ShapeColorPicker.setValue(color);
        }
    }

}
