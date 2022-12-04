package com.example.teamproject.tools;

import com.example.teamproject.controller.MainDrawingController;
import com.example.teamproject.layers.Layer;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.transform.Transform;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.*;
import javax.imageio.ImageIO;

public class ImageFormConverter {

    public static Image mergeLayers(ArrayList<Layer> layerList){
        if(layerList.isEmpty()){
            return null;
        }
        Image image = layerList.get(0).getImage();
        Mat mat = imageToMat(image);
        for( int i = 1 ; i < layerList.size(); i ++){
            image = layerList.get(i).getImage();
            Mat tmp = imageToMat(image);//other blending methods
            Core.addWeighted(mat, 0.5, tmp, 0.5, 0, mat);
        }
        return matToImage(mat);
    }
    public static Image newBlankImage(int width, int height){
        Mat mat = new Mat(height, width, CvType.CV_8UC4);
        return matToImage(mat);
    }
    public static Image mergeImages(Image image1, Image image2){
        Mat mat = new Mat((int)image1.getHeight(), (int)image1.getWidth(), CvType.CV_8UC4);
        Core.addWeighted(imageToMat(image1), 0.5, imageToMat(image2), 0.5, 0, mat);
        return matToImage(mat);
    }

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
