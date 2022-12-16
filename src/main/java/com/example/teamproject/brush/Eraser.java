package com.example.teamproject.brush;

public class Eraser extends Brush{
    @Override
    public void drawBegin(double x, double y) {
        isDrawing = true;
    }

    @Override
    public void drawTo(double x, double y) {

    }

    @Override
    public void drawEnd() {
        if(isDrawing){
            //如果当前有选区 那么将结果反向写回选区
            if(SelectorBrush.getSelectorBrush().hasSelected())
                activeLayer.updateColorRegionByEffectCanvas();
            //drawClose();
            System.out.println("[eraser] draw end");
            isDrawing = false;
        }
    }
}
