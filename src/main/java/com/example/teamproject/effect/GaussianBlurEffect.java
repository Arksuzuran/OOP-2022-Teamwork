package com.example.teamproject.effect;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * @Description 整张图片高斯模糊 实现的方式待定
 * @Author
 * @Date
 **/
public class GaussianBlurEffect extends Effect{
    //level = 1 - 15
    public static Mat gaussianBlur(Mat mat, int level){
        Mat dst = mat.clone();
        Imgproc.GaussianBlur(mat, dst, new Size(2 * level - 1, 2 * level - 1), 0, 0);

        return dst;
    }
}
