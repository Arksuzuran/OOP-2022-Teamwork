package com.example.teamproject.effect;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * @Description TODO
 * @Author
 * @Date
 **/
public class GammaCorrectionEffect implements Effect{
    @Override
    public Mat process(Mat mat, double t1, double t2, double t3) {
        return ImageEffect.gammaCorrection(mat, t1);
    }

}
