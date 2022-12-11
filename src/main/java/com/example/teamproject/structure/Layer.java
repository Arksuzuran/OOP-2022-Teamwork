package com.example.teamproject.structure;

import com.example.teamproject.controller.LayerController;
import com.example.teamproject.controller.MainDrawingController;
import com.example.teamproject.tools.ImageFormConverter;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

/**
 * @Description layer类
 * @Author  CZX
 * @Date 2022.12.11
 **/
public class Layer{

    private MainDrawingController mdc = MainDrawingController.getMDC();

    //该layer在UI中的控制类
    protected LayerController layerController;
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

    public Layer(Canvas canvas, Canvas effectCanvas, Canvas mainEffectCanvas, LayerController layerController){
        this.canvas = canvas;
        this.effectCanvas = effectCanvas;
        this.layerController = layerController;
        this.mainEffectCanvas = mainEffectCanvas;
        gc = canvas.getGraphicsContext2D();
        effectGc = effectCanvas.getGraphicsContext2D();
        mainEffectGc = mainEffectCanvas.getGraphicsContext2D();
        layerName = mdc.getNewLayerName();
        layerController.setLayerNameLabel(layerName);
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

    /**
     * 重新绘制effectCanvas
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


}
