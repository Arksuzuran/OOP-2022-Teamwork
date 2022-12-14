package com.example.teamproject.controller;

import com.example.teamproject.brush.Brush;
import com.example.teamproject.brush.BrushType;
import com.example.teamproject.brush.SelectorBrush;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

/**
 * @Description 控制选区笔UI
 * @Author  CZX FGL
 * @Date    2022.12.15
 **/
public class SelectorUIController {

    @FXML
    protected ColorPicker RegionColorPicker;//选区的选色器

    @FXML
    protected CheckBox SelectSaveCheckBox;
    @FXML
    protected CheckBox SelectLineCheckBox;

    //绘图主控的引用
    protected MainDrawingController mdc = MainDrawingController.getMDC();

    /**
     * 选择选区笔的按钮
     */
    @FXML
    protected void onSelectorButtonClick(){
        if(mdc.isActive()){
            mdc.setActiveBrush(BrushType.SELECTOR);
        }
    }
    /**
     * 选区的颜色选择器
     */
    public void fillRegion(){
        Color color = RegionColorPicker.getValue();
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof SelectorBrush){
                ((SelectorBrush) brush).fillSelectedRegion(color);
            }
        }
    }

    /**
     * 为选区填充指定颜色
     */
    @FXML
    protected void OnRegionFillButtonClick(){
        fillRegion();
    }

    /**
     * 确认选区
     */
    @FXML
    protected void OnRegionSelectedButtonClick(){
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof SelectorBrush){
                ((SelectorBrush) brush).endSelecting();
            }
        }
    }

    /**
     * 选区不保留（松手即确定选区）
     */
    @FXML
    protected void OnSelectSaveCheckBoxChanged(){
        boolean save = SelectSaveCheckBox.isSelected();
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof SelectorBrush){
                ((SelectorBrush) brush).changeRegionSave(save);
            }
        }

    }

    /**
     * 选区边界线不跟随
     */
    @FXML
    protected void OnSelectLineCheckBoxChanged(){
        boolean hasLine = SelectLineCheckBox.isSelected();
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof SelectorBrush){
                ((SelectorBrush) brush).changeBoundFollow(hasLine);
            }
        }
    }
}
