package com.example.teamproject.layers;

import com.example.teamproject.controller.LayerController;
import com.example.teamproject.tools.ImageFormConverter;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @Description 所有layer的父类 待定
 * @Author
 * @Date
 **/
public class Layer{

    //该layer在UI中的控制类
    protected LayerController layerController;


    //当前层的图片
    protected Image image;
    public Image getImage() {
        return image;
    }

    //画布
    final protected ImageView imageView;
    //效果画布 用以显示绘图时的提示效果
    final protected Canvas effectCanvas;
    //图层命名
    protected String layerName;

    public Layer(ImageView imageView, Canvas effectCanvas, LayerController layerController){
        this.imageView = imageView;
        this.effectCanvas = effectCanvas;
        this.layerController = layerController;
        //image = TransTool.getBlankImage();
    }

    /**
     * 将effectCanvas中的内容写入该图层的image
     */
    public void updateImage(){
        image = ImageFormConverter.canvasToImage(effectCanvas);
    }

    //设置图层名称
    public void setLayerName(String s){
        layerName = s;
    }


}
