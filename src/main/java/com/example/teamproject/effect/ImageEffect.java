package com.example.teamproject.effect;

import com.example.teamproject.brush.SelectorBrush;
import com.example.teamproject.structure.SelectedRegion;
import javafx.scene.paint.Color;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;

/**
 * @Description 用于直接处理Mat的工具类
     * @Author  ZDW
 * @Date    2022.12.14
 **/
public class ImageEffect {
    /**
     * 获取mat的反色图像（255-x）
     * @param mat 要反色处理的图像
     * @return 反色处理完成的图像
     */
    static SelectedRegion selectedRegion = SelectorBrush.getSelectorBrush().getSelectedRegion();
    public static Mat originalCopy = null;
    public static Mat reverseColorMat(Mat mat){
        Mat dst = new Mat(mat.size(), mat.type());

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
    /*
        contrast: 1.0 - 3.0
        brightness: 0 - 100
    */
    private static byte saturate(double val) {
        int iVal = (int) Math.round(val);
        iVal = Math.min(Math.max(iVal, 0), 255);
        return (byte) iVal;
    }
    public static Mat contrastControl(Mat mat, double contrast, int brightness){
        Mat dst = new Mat(mat.size(), mat.type());
        mat.convertTo(dst, -1, contrast, brightness);
        return dst;
    }
    public static Mat hueControl(Mat mat, double red, double green, double blue){
        Mat dst = new Mat(mat.size(), mat.type());
        byte[] matData = new byte[(int) (mat.total() * mat.channels())];
        mat.get(0, 0, matData);
        byte[] dstData = new byte[(int) (dst.total() * dst.channels())];
        for (int y = 0; y < mat.rows(); y++) {
            for (int x = 0; x < mat.cols(); x++) {
                for (int c = 0; c < mat.channels(); c++) {
                    double pixelValue = matData[(y * mat.cols() + x) * mat.channels() + c];
                    pixelValue = pixelValue < 0 ? pixelValue + 256 : pixelValue;
                    if(c == 0){
                        pixelValue *= blue;
                    }
                    if(c == 1){
                        pixelValue *= green;
                    }
                    if(c == 2){
                        pixelValue *= red;
                    }
                    dstData[(y * mat.cols() + x) * mat.channels() + c] = (byte) pixelValue;
                }
            }
        }
        dst.put(0, 0, dstData);
        return dst;
    }

    /*
    非线性地处理亮度，让图片不会过暗或者过曝
    gamma > 0
     */
    public static Mat gammaCorrection(Mat mat, double gamma){
        Mat lookUpTable = new Mat(1, 256, CvType.CV_8U);
        byte[] lookUpTableData = new byte[(int) (lookUpTable.total()*lookUpTable.channels())];
        for (int i = 0; i < lookUpTable.cols(); i++) {
            lookUpTableData[i] = saturate(Math.pow(i / 255.0, gamma) * 255.0);
        }
        lookUpTable.put(0, 0, lookUpTableData);
        Mat dst = new Mat();
        Core.LUT(mat, lookUpTable, dst);
        return dst;
    }

}
/*
输入任意（有范围）的各种参数(ui的滑动条输入)：明度-亮度？对比度，饱和度，色调（怎么变换？），二值化的阈值，
每一个滑动条滑动时，将canvas转化成image转化成mat（onMouseDown),改变相应参数，调用这里的方法对mat进行变换（onDrag），将mat转化为image，更新到canvas输出调整后的图片(onMouseUp)
snapshot的精度损失
只对选区生效

缩放效果？

自动绘制多边形轮廓，形成选区

其他特效滤镜

IO

*/