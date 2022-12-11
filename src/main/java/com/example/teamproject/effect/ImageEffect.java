package com.example.teamproject.effect;

import javafx.scene.image.Image;
import org.opencv.core.Mat;

/**
 * @Description 用于直接处理Mat的工具类
 * @Author  CZX +
 * @Date    2022.12.5
 **/
public class ImageEffect {

    /**
     * 获取mat的反色图像（255-x）
     * @param mat 要反色处理的图像
     * @return 反色处理完成的图像
     */
    public static Mat reverseColorMat(Mat mat){
        Mat dst = new Mat(mat.size(),mat.type());

        double[] pixelArr = new double[3];
        for(int i = 0,rlen=mat.rows(); i<rlen; i++){
            for (int j=0,clen=mat.cols(); j<clen; j++){
                pixelArr = mat.get(i, j).clone();

                pixelArr[0] = 255 - pixelArr[0];
                pixelArr[1] = 255 - pixelArr[1];
                pixelArr[2] = 255 - pixelArr[2];

                dst.put(i, j, pixelArr);
            }
        }
        return dst;
    }

}
