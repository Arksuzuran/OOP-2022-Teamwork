package com.example.teamproject.controller;

import com.example.teamproject.brush.Brush;
import com.example.teamproject.brush.EraserBrush;
import com.example.teamproject.brush.PenBrush;
import com.example.teamproject.brush.SelectorBrush;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

/**
 * @Description 控制橡皮的UI
 * @Author  CZX FGL
 * @Date    2022.12.15
 **/
public class EraserUIController {

    //画笔粗细滑动条
    @FXML
    protected Slider PenWidthSlider;
    //画笔粗细宽度显示标签
    @FXML
    protected Label PenWidthLabel;
    //是否圆形擦除
    @FXML
    protected CheckBox CircularCheckBox;

    //绘图主控的引用
    protected MainDrawingController mdc = MainDrawingController.getMDC();


    /**
     * 如果当前有作品且选中了橡皮笔刷 那么调节其粗细
     * 更新对应UI
     */
    public void updatePenWidth(){
        double penWidth = PenWidthSlider.getValue();
        PenWidthLabel.setText(Integer.toString((int)penWidth));

        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof EraserBrush){
                ((EraserBrush) brush).setLineWidth(penWidth);
            }
        }
    }
    @FXML
    protected void OnPenWidthSliderSet(){
        updatePenWidth();
    }
    @FXML
    protected void OnPenWidthDecreaseButtonClick(){
        PenWidthSlider.setValue(PenWidthSlider.getValue()-1);
        updatePenWidth();
    }
    @FXML
    protected void OnPenWidthIncreaseButtonClick(){
        PenWidthSlider.setValue(PenWidthSlider.getValue()+1);
        updatePenWidth();
    }

    /**
     * 改变笔头形状
     */
    @FXML
    protected void OnCircularCheckBoxChanged(){
        boolean isCircular = CircularCheckBox.isSelected();
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof EraserBrush){
                ((EraserBrush) brush).setType(isCircular);
                if(isCircular)
                    ControllerSet.muc.sendMessage("[橡皮] 橡皮切换为圆形擦除");
                else
                    ControllerSet.muc.sendMessage("[橡皮] 橡皮切换为方形擦除");
            }
        }
    }

    public void updateUIbyEraserBrush(){
        EraserBrush eraserBrush = EraserBrush.getEraserBrush();
        if(mdc.isActive()){
            double penWidth = eraserBrush.getLineWidth();
            boolean isCircular = eraserBrush.getCircular();

            PenWidthLabel.setText(Integer.toString((int)penWidth));
            PenWidthSlider.setValue(penWidth);
            CircularCheckBox.setSelected(isCircular);
        }
    }
}
