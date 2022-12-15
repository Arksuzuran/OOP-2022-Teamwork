package com.example.teamproject.controller;

import com.example.teamproject.brush.Brush;
import com.example.teamproject.brush.BrushType;
import com.example.teamproject.brush.SelectorBrush;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.stage.FileChooser;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;

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
     * 选区的颜色填充
     */
    public void fillRegion(){
        Color color = RegionColorPicker.getValue();
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof SelectorBrush){
                ((SelectorBrush) brush).fillSelectedRegion(color);
                ControllerSet.muc.sendMessage("已为选区填充颜色："+color);
            }
        }
    }
    public void fillUnselectedRegion(){
        Color color = RegionColorPicker.getValue();
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof SelectorBrush){
                ((SelectorBrush) brush).fillUnselectedRegion(color);
                ControllerSet.muc.sendMessage("已为选区外填充颜色："+color);
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
    @FXML
    protected void OnUnselectedRegionFillButtonClick(){
        fillUnselectedRegion();
    }

    /**
     * 导入图片并生成选区
     */
    @FXML
    protected void onImportImageButtonClock(){
        importImageAsRegion();
    }

    protected void importImageAsRegion(){

        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof SelectorBrush){
                SelectorBrush selectorBrush = (SelectorBrush) brush;
                if(((SelectorBrush) brush).hasSelected())
                    ControllerSet.muc.sendMessage("不能在已有选区的情况下导入图片作为选区。");

                else{
                    Stage stage = new Stage();
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Open");
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("All Images", "*.*"),
                            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                            new FileChooser.ExtensionFilter("PNG", "*.png"));
                    File file = fileChooser.showOpenDialog(stage);

                    ((SelectorBrush) brush).createImageSelectedRegion(file);
                    ControllerSet.muc.sendMessage("成功导入图片作为选区。");
                }
            }
        }
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
                ControllerSet.muc.sendMessage("选区已确认更改。");
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
                ((SelectorBrush) brush).setRegionSave(save);
                ControllerSet.muc.sendMessage("选区不保留。现在，拖动并松手后将自动确认选区。");
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
                ((SelectorBrush) brush).setBoundFollow(hasLine);
                ControllerSet.muc.sendMessage("选区边界不跟随。现在，选区边界虚线将不再显示");
            }
        }
    }
}
