package com.example.teamproject.tools;

import com.example.teamproject.layers.Layer;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.transform.Transform;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.*;

public class ImageFormConverter {

    public static Image mergeLayers(ArrayList<Layer> layerList){
        if(layerList.isEmpty()){
            return null;
        }
        Image image = layerList.get(0).getImage();
        Mat mat = imageToMat(image);
        for( int i=1 ; i<layerList.size(); i++){
            image = layerList.get(i).getImage();
            imageToMat(image).copyTo(mat);//other blending methods
        }
        return matToImage(mat);
    }

    public static Image canvasToImage(Canvas canvas){
        int pixelScale = 1;//?
        WritableImage writableImage = new WritableImage((int)Math.rint(pixelScale*canvas.getWidth()), (int)Math.rint(pixelScale*canvas.getHeight()));
        SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(Transform.scale(pixelScale, pixelScale));

        canvas.snapshot(spa, writableImage);
        int height = (int) (canvas.getHeight());
        int width = (int) (canvas.getWidth());
        byte[] pixelBuffer = new byte[width * height * 4];
        PixelReader pixelReader = writableImage.getPixelReader();
        WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
        pixelReader.getPixels(0, 0, width, height, format, pixelBuffer, 0, width * 4);
        return new Image(new ByteArrayInputStream(pixelBuffer));
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
