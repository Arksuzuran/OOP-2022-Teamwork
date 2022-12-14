package com.example.teamproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @Description 控制创建新作品的弹窗  补充：该窗口存在时应当不能关闭主窗口，后续需要修正该bug
 * @Author  FGL
 **/
public class NewWorkUIController {
    @FXML
    private TextArea NewHeight;
    @FXML
    private TextArea NewName;
    @FXML
    private TextArea NewWidth;

    Stage stage;

    public void init(Stage stage){
        this.stage = stage;
        NewName.setText("untitled");
        NewHeight.setText("500");
        NewWidth.setText("700");
    }

    @FXML
    protected void ConfirmNew(MouseEvent event) {
        String name = NewName.getText();
        int width = Integer.parseInt(NewWidth.getText());
        int height = Integer.parseInt(NewHeight.getText());

        ControllerSet.muc.createNewWork(width, height, name);

        stage.close();
    }

    @FXML
    void CancelNew(MouseEvent event) {
        stage.close();
    }
}
