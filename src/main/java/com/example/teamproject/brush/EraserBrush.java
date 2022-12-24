package com.example.teamproject.brush;

import com.example.teamproject.structure.SelectedRegion;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

/**
 * @Description 橡皮
 * @Author  CR
 * @Date    2022.12.16
 **/
public class EraserBrush extends Brush{


     //每种画笔都是单例模式

    private static final EraserBrush eraser = new EraserBrush();
    public static EraserBrush getEraserBrush() {
        return eraser;
    }
    private EraserBrush(){}

    public static SelectorBrush selectorBrush = SelectorBrush.getSelectorBrush();

    private boolean selected = false;

    private boolean isCircular = true;//橡皮类型 为false的时候是方形擦除 为true的时候是圆形擦除

    /**
     *  设置线宽
     */
    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }


    /**
     *  更新当前选中的图层
     */
    @Override
    public void updateActiveLayer(){
        if (mdc.isActive()){
            activeLayer = mdc.getActiveLayer();
            if(activeLayer != null){
                //更新笔刷的引用
                canvas = activeLayer.getCanvas();
                gc = canvas.getGraphicsContext2D();
                effectCanvas = activeLayer.getEffectCanvas();
                effectGc = effectCanvas.getGraphicsContext2D();

                //笔刷设置为当前选择的状态
                setLineWidth(lineWidth);

                System.out.println("eraser changes activeLayer to "+activeLayer);
                System.out.println("eraser set canvas "+canvas);
            }
        }
    }

    /**
     *  开始绘画
     * @param x 起始点的x
     * @param y 起始点的y
     */
    @Override
    public void drawBegin(double x, double y) {
        //只能在选区内工作 一旦检测到当前笔刷超出了选区 那么不允许继续绘画
        if(!selectorBrush.inSelectedRegion(x, y))
            return;
        eraseCircularRegion(x, y);
        isDrawing = true;
    }

    /**
     *  绘制路径点
     * @param x 路径点x
     * @param y 路径点y
     */
    @Override
    public void drawTo(double x, double y) {
        if(isDrawing){
            //只能在选区内工作 一旦检测到当前笔刷的边缘超出了选区 那么不让绘画
            if(selectorBrush.inSelectedRegion(x, y)){
                eraseCircularRegion(x, y);
            }
        }
    }

    /**
     *  绘画结束
     */
    @Override
    public void drawEnd(double x, double y) {
        if(isDrawing){
            //如果当前有选区 那么将结果反向写回选区
            if(SelectorBrush.getSelectorBrush().hasSelected())
                activeLayer.updateColorRegionByEffectCanvas();
            activeLayer.saveOp();
            System.out.println("[eraser] draw end");
            isDrawing = false;
        }
    }


    /**
     *  更改选择
     */
    public void changeSelected(boolean selected){
        this.selected = selected;
    }

    /**
     *  设置类型
     */
    public void setType(boolean isCircular) {
        this.isCircular = isCircular;
    }

    /**
     *  getCircular
     */
    public boolean getCircular() {
        return isCircular;
    }

    /**
     *  擦除以(x,y)为圆心的圆形区域 如果存在选区 那么只擦除选区
     * @param x x
     * @param y y
     */
    public void eraseCircularRegion(double x, double y){
        int sx = (int)(x - lineWidth), ex = (int)(x + lineWidth);
        int sy = (int)(y - lineWidth), ey = (int)(y + lineWidth);
        int maxX = (int)canvas.getWidth(), maxY = (int)canvas.getHeight();
        PixelWriter pixelWriter;
        //存在选区
        if(selected) {
            pixelWriter = effectGc.getPixelWriter();
            SelectedRegion selectedRegion = SelectorBrush.getSelectorBrush().getSelectedRegion();
            int offsetX = (int)selectedRegion.x;
            int offsetY = (int)selectedRegion.y;
            //在所有可能的区域内 如果超出 那么清除

            //方形擦除
            if(!isCircular){
                for (int i = sx; i < ex; i++){
                    for (int j = sy; j < ey; j++){
                        if(i<0 || j<0 || i>=maxX || j>=maxY)
                            continue;
                        if(selectedRegion.pointInRegionRelative(i-offsetX, j-offsetY)){
                            pixelWriter.setColor(i, j, Color.TRANSPARENT);
                        }
                    }
                }
            }
            //圆形擦除
            else {
                for (int i = sx; i < ex; i++){
                    for (int j = sy; j < ey; j++){
                        if(i<0 || j<0 || i>=maxX || j>=maxY)
                            continue;
                        if(inCircle(x, y, i, j, lineWidth) && selectedRegion.pointInRegionRelative(i-offsetX, j-offsetY)){
                            pixelWriter.setColor(i, j, Color.TRANSPARENT);
                        }
                    }
                }
            }
        }
        else{
            pixelWriter = gc.getPixelWriter();
            //方形擦除
            if(!isCircular){
                for (int i = sx; i < ex; i++){
                    for (int j = sy; j < ey; j++){
                        if(i<0 || j<0 || i>=maxX || j>=maxY)
                            continue;
                        pixelWriter.setColor(i, j, Color.TRANSPARENT);
                    }
                }
            }
            //圆形擦除
            else {
                for (int i = sx; i < ex; i++){
                    for (int j = sy; j < ey; j++){
                        if(i<0 || j<0 || i>=maxX || j>=maxY)
                            continue;
                        if(inCircle(x, y, i, j, lineWidth)){
                            pixelWriter.setColor(i, j, Color.TRANSPARENT);
                        }
                    }
                }
            }
        }
    }

    /**
     *  判断是否在圆内
     */
    private boolean inCircle(double x0, double y0, double x, double y, double r){
        return (x0-x)*(x0-x) + (y0-y)*(y0-y) <= r*r;
    }


}
