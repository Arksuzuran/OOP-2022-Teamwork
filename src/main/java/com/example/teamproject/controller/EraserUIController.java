package com.example.teamproject.controller;

import com.example.teamproject.brush.Brush;
import com.example.teamproject.brush.PenBrush;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

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

    @FXML
    //抖动修正滑动条
    protected Slider SmoothLevelSlider;
    //抖动修正值显示标签
    @FXML
    protected Label SmoothLevelLabel;

    //绘图主控的引用
    protected MainDrawingController mdc = MainDrawingController.getMDC();



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
        updatePenWidth();
    }
    @FXML
    protected void OnPenWidthIncreaseButtonClick(){
        updatePenWidth();
    }


}
