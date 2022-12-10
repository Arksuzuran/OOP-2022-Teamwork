package com.example.teamproject.layers;

import com.example.teamproject.controller.LayerController;
import com.example.teamproject.controller.MainDrawingController;
import com.example.teamproject.tools.ImageFormConverter;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    //效果画布 用以显示绘图时的提示效果
    final protected Canvas effectCanvas;

    //画布
    final protected Canvas canvas;
    //图层命名
    protected String layerName;

    public Layer(Canvas canvas, Canvas effectCanvas, Canvas mainEffectCanvas, LayerController layerController){
        this.canvas = canvas;
        this.effectCanvas = effectCanvas;
        this.layerController = layerController;
        this.mainEffectCanvas = mainEffectCanvas;
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
