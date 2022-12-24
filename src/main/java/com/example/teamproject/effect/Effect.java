package com.example.teamproject.effect;

import org.opencv.core.Mat;
/**
 * @Description Effect接口
 * @Author CZX
 * @Date 2022.12.12
 **/
public interface Effect {
    Mat process(Mat mat, double t1, double t2, double t3);
}
