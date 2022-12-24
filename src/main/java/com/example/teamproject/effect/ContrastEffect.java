package com.example.teamproject.effect;

import org.opencv.core.Mat;

/**
 * @Description TODO
 * @Author CZX
 * @Date 2022.12.12
 **/
public class ContrastEffect implements Effect{

    /*
        contrast: 0 - 3.0 middle: 1
        brightness: -100 - 100
    */
    @Override
    public Mat process(Mat mat, double contrast, double brightness, double t3) {
        return ImageEffect.contrastControl(mat, contrast, (int) brightness);
    }

}
