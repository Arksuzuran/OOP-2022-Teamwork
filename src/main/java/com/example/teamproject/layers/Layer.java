package com.example.teamproject.layers;

import com.example.teamproject.controller.LayerController;
import com.example.teamproject.controller.MainDrawingController;
import com.example.teamproject.tools.ImageFormConverter;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @Description layer类 是单次图像处理或者绘画操作的最小单位
 * @Author  CZX
 * @Date 2022.12.5
 **/
public class Layer{

    private MainDrawingController mdc = MainDrawingController.getMDC();

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
        image = ImageFormConverter.canvasToImage(new Canvas(effectCanvas.getWidth(), effectCanvas.getHeight()));
    }

    /**
     * 将effectCanvas中的内容写入该图层的image
     * 再将image的更改汇总到imageView里
     */
    public void updateImage(){
        Image newImagePart = ImageFormConverter.canvasToImage(effectCanvas);
        setImage(ImageFormConverter.mergeImages(image, newImagePart));
        mdc.updateImageView();
    }

    /**
     * 直接更换当前图层的图片为image
     * 再将image的更改汇总到imageView里
     * @param image 要更换的图片
     */
    public void setImage(Image image){
        this.image = image;
        mdc.updateImageView();
    }

    //设置图层名称
    public void setLayerName(String s){
        layerName = s;
    }


}
