package com.example.teamproject.tools;

import com.example.teamproject.layers.Layer;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.transform.Transform;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * @Description 实现Image、Mat、Canvas的互相转化，Canvas是前端对象，Mat是后端对象用于具体的图像处理
 * @Author  Melchoirr
 * @Date    2022.12.4
 **/

public class ImageFormConverter {

    /**
     * 将图层数组进行合并，生成混合后的Image
     * 用于每次画图or图像处理操作后，使得UI界面能够显示最终处理效果
     * @param layerList 要合并的图层数组
     * @return  合成的结果
     */
    public static Image mergeLayers(ArrayList<Layer> layerList){
        if(layerList.isEmpty()){
            return null;
        }
        Image image = layerList.get(0).getImage();
        Mat mat = imageToMat(image);
        for( int i = 1 ; i < layerList.size(); i ++){
            image = layerList.get(i).getImage();
            Mat tmp = imageToMat(image);//other blending methods
            mat = mergeMat(mat, tmp);
            //Core.addWeighted(mat, 0.5, tmp, 0.5, 0, mat);
        }
        return matToImage(mat);
    }

    /**
     * 创建指定高和宽的黑底空图片
     * @param width 宽
     * @param height    高
     * @return
     */
    public static Image newBlankImage(int width, int height){
        Mat mat = new Mat(height, width, CvType.CV_8UC4);
        return matToImage(mat);
    }

    /**
     * 将两个image对象合并 返回新的image
     * @param image1 要合并的image
     * @param image2 要合并的image（二者不分先后）
     * @return
     */
    public static Image mergeImages(Image image1, Image image2){
        if(image1!=null && image2!=null){
            Mat mat = new Mat((int)image1.getHeight(), (int)image1.getWidth(), CvType.CV_8UC4);
            //Core.addWeighted(imageToMat(image1), 0.5, imageToMat(image2), 0.5, 0, mat);

            Mat mat1 = ImageEffect.reverseColorMat(imageToMat(image1));
            Mat mat2 = ImageEffect.reverseColorMat(imageToMat(image2));
            Core.add(mat1, mat2, mat);

            //return matToImage(mat);
            return matToImage(ImageEffect.reverseColorMat(mat));
        }
        return null;
    }
    /**
     * 将两个image对象合并 返回新的mat
     * @param mat1 要合并的mat
     * @param mat2 要合并的mat（二者不分先后）
     * @return
     */
    public static Mat mergeMat(Mat mat1, Mat mat2){
        if(mat1!=null && mat2!=null){
            mat1 = ImageEffect.reverseColorMat(mat1);
            mat2 = ImageEffect.reverseColorMat(mat2);
            Core.add(mat1, mat2, mat1);
            return ImageEffect.reverseColorMat(mat1);
        }
        return null;
    }


    /**
     * 将Canvas对象转换为Image对象
     * @param canvas    要转的canvas
     * @return  转换好的Image
     */
    public static Image canvasToImage(Canvas canvas){
        double pixelScale = 1.0;//?
        WritableImage writableImage = new WritableImage((int)(pixelScale*canvas.getWidth()), (int)Math.rint(pixelScale*canvas.getHeight()));
        SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(Transform.scale(pixelScale, pixelScale));
        writableImage = canvas.snapshot(spa, writableImage);
        return writableImage;
    }

    public static Mat imageToMat(Image image){
        try {
            int height = (int) (image.getHeight());
            int width = (int) (image.getWidth());
            byte[] pixelBuffer = new byte[width * height * 4];
            PixelReader pixelReader = image.getPixelReader();
            WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
            pixelReader.getPixels(0, 0, width, height, format, pixelBuffer, 0, width * 4);
            Mat mat = new Mat(height, width, CvType.CV_8UC4);

            mat.put(0, 0, pixelBuffer);
            return mat;
        }catch (Exception e) {
            System.out.println("imageToMat error");
            return null;
        }

    }

    public static Image matToImage(Mat mat){
        try {
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".bmp", mat, matOfByte);
            return new Image(new ByteArrayInputStream(matOfByte.toArray()));
        } catch (Exception e) {
            System.out.println("matToImage error");
            return null;
        }
    }
    public static Image fileToImage(File file){
        return new Image(file.getAbsolutePath());
    }
}
