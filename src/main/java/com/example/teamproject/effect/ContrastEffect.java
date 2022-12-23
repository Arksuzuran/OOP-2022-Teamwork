package com.example.teamproject.effect;

import org.opencv.core.Mat;

/**
 * @Description TODO
 * @Author
 * @Date
 **/
public class ContrastEffect implements Effect{

    /*
        contrast: 1.0 - 3.0
        brightness: 0 - 100
    */
    @Override
    public Mat process(Mat mat, double contrast, double brightness, double t3) {
        return ImageEffect.contrastControl(mat, contrast, (int) brightness);
    }

}
