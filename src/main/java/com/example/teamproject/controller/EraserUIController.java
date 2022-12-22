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
import javafx.scene.image.ImageView;

/**
 * @Description 控制橡皮的UI
 * @Author  CZX FGL
 * @Date    2022.12.15
 **/
public class EraserUIController {

    public ImageView b1;
    public ImageView b2;
    public ImageView b3;
    public ImageView b4;
    public ImageView b5;
    public ImageView b6;
    public ImageView b7;
    public ImageView b8;
    public ImageView b9;

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
        updatePenWidth();
    }
    @FXML
    protected void OnPenWidthIncreaseButtonClick(){
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

    @FXML
    void B1Clicked(ActionEvent event) {
        init(1);
    }

    @FXML
    void B2Clicked(ActionEvent event) {
        init(2);
    }

    @FXML
    void B3Clicked(ActionEvent event) {
        init(3);
    }

    @FXML
    void B4Clicked(ActionEvent event) {
        init(4);
    }

    @FXML
    void B5Clicked(ActionEvent event) {
        init(5);
    }

    @FXML
    void B6Clicked(ActionEvent event) {
        init(6);
    }

    @FXML
    void B7Clicked(ActionEvent event) {
        init(7);
    }

    @FXML
    void B8Clicked(ActionEvent event) {
        init(8);
    }

    @FXML
    void B9Clicked(ActionEvent event) {
        init(9);
    }
}
