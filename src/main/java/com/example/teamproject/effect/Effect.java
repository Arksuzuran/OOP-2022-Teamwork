package com.example.teamproject.effect;

import org.opencv.core.Mat;

public interface Effect {
    Mat process(Mat mat, double t1, double t2, double t3);
}
