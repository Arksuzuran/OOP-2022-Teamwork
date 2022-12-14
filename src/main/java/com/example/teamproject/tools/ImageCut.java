package com.example.teamproject.tools;

import javafx.scene.image.Image;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @Description 将指定多边形内的图片裁剪下来 返回
 * @Author  CZX
 * @Date    2022.12.11
 **/
public class ImageCut {

    /**
     * 将指定image中，被边界围起来的部分切下来
     * @param inputImage 输入image
     * @return  被围起来的部分
     */
    public static Image imagePolygonCut(Image inputImage, GeneralPath clip){
        try {
            //image转bf
            BufferedImage bufferedImage = ImageFormConverter.ImageToBufferedImage(inputImage);

            Rectangle bounds = clip.getBounds();
            //BufferedImage img = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_BGR);
            BufferedImage img = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_BGR);
            Graphics2D g2d = img.createGraphics();
            clip.transform(AffineTransform.getTranslateInstance(0, 0));
            g2d.setClip(clip);
            //g2d.translate(0, 0);
            g2d.drawImage(bufferedImage, 0, 0, null);
            g2d.dispose();

            //bf转image
            Image outputImage = ImageFormConverter.BufferedImageToImage(bufferedImage);
            System.out.println("image polygonCut success");
            return outputImage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
