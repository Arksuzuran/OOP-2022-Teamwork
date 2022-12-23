package com.example.teamproject.effect;

import org.opencv.core.Mat;

/**
 * @Description TODO
 * @Author
 * @Date
 **/
public class HueEffect implements Effect{

    @Override
    public Mat process(Mat mat, double t1, double t2, double t3) {
        return ImageEffect.hueControl(mat, t1, t2, t3);
    }

}
