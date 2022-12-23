package com.example.teamproject.brush;

import com.example.teamproject.controller.ControllerSet;
import javafx.scene.control.ScrollPane;
import javafx.scene.transform.Scale;
import org.opencv.core.Point;

public class MoveBrush extends Brush{

    Point prev = new Point();
    private static final MoveBrush moveBrush = new MoveBrush();
    public static MoveBrush getMoveBrush() {
        return moveBrush;
    }
    private MoveBrush(){}
    Scale newScale = null;
    @Override
    public void drawBegin(double x, double y) {
        Scale newScale = new Scale();
        newScale.setPivotX(x);
        newScale.setPivotY(y);
        prev = new Point(x, y);
    }

    //get到鼠标实时的x y坐标
    //选择全部
    @Override
    public void drawTo(double x, double y) {
        ScrollPane scrollPane = ControllerSet.muc.getDrawingScrollPane();
        scrollPane.setHvalue(scrollPane.getHvalue() - (x - prev.x) / scrollPane.getWidth());
        scrollPane.setVvalue(scrollPane.getVvalue() - (y - prev.y) / scrollPane.getHeight());

    }

    @Override
    public void drawEnd(double x, double y) {
        prev = new Point(0,0);
    }
}
