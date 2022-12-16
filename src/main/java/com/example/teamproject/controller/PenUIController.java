package com.example.teamproject.controller;

import com.example.teamproject.brush.Brush;
import com.example.teamproject.brush.PenBrush;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

/**
 * @Description 控制画笔UI
 * @Author  CZX FGL
 * @Date    2022.12.15
 **/
public class PenUIController {


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

    //绘图主控的引用
    protected MainDrawingController mdc = MainDrawingController.getMDC();


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

}
