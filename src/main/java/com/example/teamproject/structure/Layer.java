package com.example.teamproject.structure;

import com.example.teamproject.brush.SelectorBrush;
import com.example.teamproject.controller.LayerUIController;
import com.example.teamproject.controller.MainDrawingController;
import com.example.teamproject.io.Open;
import com.example.teamproject.tools.ImageFormConverter;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;

/**
 * @Description layer类
 * @Author  CZX
 * @Date 2022.12.11
 **/
public class Layer{

    private MainDrawingController mdc = MainDrawingController.getMDC();

    //该layer在UI中的控制类
    protected LayerUIController layerUIController;
    //最顶层的效果画布
    final protected Canvas mainEffectCanvas;
    final protected GraphicsContext mainEffectGc;
    //效果画布 用以显示绘图时的提示效果
    final protected Canvas effectCanvas;
    final protected GraphicsContext effectGc;
    //画布
    final protected Canvas canvas;
    final protected GraphicsContext gc;
    //图层命名
    protected String layerName;

    //存储对图层的操作
    final private ArrayList<Image> opList = new ArrayList<>();
    //分别存储当前操作的位置 和操作数组的上限
    private int pos;

    //当前图层是否可见
    private boolean isVisible = true;

    public Layer(Canvas canvas, Canvas effectCanvas, Canvas mainEffectCanvas, LayerUIController layerUIController){
        this.canvas = canvas;
        this.effectCanvas = effectCanvas;
        this.layerUIController = layerUIController;
        this.mainEffectCanvas = mainEffectCanvas;
        gc = canvas.getGraphicsContext2D();
        effectGc = effectCanvas.getGraphicsContext2D();
        mainEffectGc = mainEffectCanvas.getGraphicsContext2D();
        layerName = mdc.getNewLayerName();
        layerUIController.setLayerNameLabel(layerName);

        pos = -1;
    }

    public void importImageToCanvas(File file){
        Image image = Open.importImage(file);
        gc.drawImage(image, 0, 0);
    }
    public void importImageToCanvas(Image image){
        gc.drawImage(image, 0, 0);
    }
    public Canvas getMainEffectCanvas() {
        return mainEffectCanvas;
    }

    public Canvas getEffectCanvas() {
        return effectCanvas;
    }

    public Canvas getCanvas() {
        return canvas;
    }
    public boolean isVisible() {
        return isVisible;
    }
    public void setVisible(boolean visible) {
        isVisible = visible;
        canvas.setVisible(visible);
        effectCanvas.setVisible(visible);
    }



    /**
     * 利用image重新绘制effectCanvas
     * @param x 新图案左上角的坐标x
     * @param y 新图案左上角的坐标y
     * @param image 要绘制的新图案
     */
    public void updateEffectCanvas(double x, double y, WritableImage image, boolean mergeToCanvas){
        clearEffectCanvas();
        if(!mergeToCanvas)
            effectGc.drawImage(image, x, y);
        else
            gc.drawImage(image, x, y);
    }

    /**
     * 利用selectedRegion重新绘制effectCanvas
     * @param selectedRegion    选区
     * @param mergeToCanvas     是否复制给主画布
     */
    public void updateEffectCanvas(SelectedRegion selectedRegion, boolean mergeToCanvas){
        clearEffectCanvas();
        PixelWriter pixelWriter;
        if(!mergeToCanvas){
            pixelWriter = effectGc.getPixelWriter();
        }
        else{
            pixelWriter = gc.getPixelWriter();
        }
        Color[][] colors = selectedRegion.getColorRegion();
        int offsetX = (int)selectedRegion.x, offsetY = (int)selectedRegion.y;
        for(int i=0; i<selectedRegion.sizeX; i++){
            for (int j=0; j<selectedRegion.sizeY; j++){
                //在选区内 且不透明 那么复制
                if(selectedRegion.pointInRegionRelative(i, j)){
                    Color color = colors[i][j];
                    if(color.getOpacity()!=0){
                        pixelWriter.setColor(i+offsetX, j+offsetY, color);
                    }
                }
            }
        }
    }

    /**
     * 画笔在对选区操作完成后调用
     * 利用当前effectCanvas里的内容 反向写回selectedRegion的colorRegion数组
     */
    public void updateColorRegionByEffectCanvas(){
        SelectedRegion selectedRegion = SelectorBrush.getSelectorBrush().getSelectedRegion();
        Color[][] colors = selectedRegion.getColorRegion();
        int offsetX = (int)selectedRegion.x, offsetY = (int)selectedRegion.y;

        WritableImage oriImage = ImageFormConverter.canvasToImage(effectCanvas);
        PixelReader pixelReader = oriImage.getPixelReader();

        for(int i=0; i<selectedRegion.sizeX; i++){
            for (int j=0; j<selectedRegion.sizeY; j++){
                //将选区内effectCanvas的内容再复制到color数组里面
                if(i+offsetX>=0 && j+offsetY>=0){
                    Color color = pixelReader.getColor(i+offsetX, j+offsetY);
                    colors[i][j] = color;
                }
            }
        }
    }

    public void updateMainEffectCanvas(double x, double y, WritableImage image){
        clearMainEffectCanvas();
        mainEffectGc.drawImage(image, x, y);
    }
    public void clearMainEffectCanvas(){
        mainEffectGc.clearRect(0,0,mainEffectCanvas.getWidth(),mainEffectCanvas.getHeight());
    }
    public void clearEffectCanvas(){
        effectGc.clearRect(0,0,effectCanvas.getWidth(), effectCanvas.getHeight());
    }
//    /**
//     * 将effectCanvas中的内容写入该图层的image
//     * 再将image的更改汇总到imageView里
//     */
//    public void updateImage(){
//        Image newImagePart = ImageFormConverter.canvasToImage(effectCanvas);
//        setImage(ImageFormConverter.mergeImages(image, newImagePart));
//        mdc.updateImageView();
//    }

//    /**
//     * 直接更换当前图层的图片为image
//     * 再将image的更改汇总到imageView里
//     * @param image 要更换的图片
//     */
//    public void setImage(Image image){
//        this.image = image;
//        mdc.updateImageView();
//    }

    //设置图层名称
    public void setLayerName(String s){
        layerName = s;
    }

    /**
     * 在图层被更改后调用
     * 以image形式保存当前状态，以便于回退
     */
    public void saveOp(){
        Image image = ImageFormConverter.canvasToImage(canvas);
        if (opList.size() > pos + 1) {
            opList.subList(pos + 1, opList.size()).clear();
        }
        pos++;
        opList.add(image);
        System.out.println("save now:"+pos);
    }

    /**
     * 撤回该图层的修改
     */
    public boolean undoOp(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        //撤回最后一次操作 清空即可
        if(pos <= 0){
            pos = -1;
            return false;
        }
        else{
            --pos;
            Image image = opList.get(pos);
            System.out.println("undo now:"+pos);
            gc.drawImage(image, 0, 0);
            return true;
        }
    }

    /**
     * 撤回 撤回对图层的修改
     * 前提是
     */
    public boolean RedoOp(){

        if(pos < opList.size()-1){
            Image image = opList.get(++pos);
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.drawImage(image, 0, 0);
            return true;
        }
        else {
            return false;
        }
    }

}
