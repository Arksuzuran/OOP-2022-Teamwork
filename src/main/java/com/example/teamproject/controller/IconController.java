package com.example.teamproject.controller;

import javafx.scene.image.Image;

import java.io.File;
import java.util.Objects;

/**
 * @Description 所有图标的数据类，提供了一个更新图标的方法.
 * @author FGL
 * @Date 2022.12.23
 */

public class IconController {
    public static Image n1 = new Image(new File("icons/1.png").getAbsolutePath());

    //public static Image n1 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/1.png")).toString());
    public static Image n2 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/2.png")).toString());
    public static Image n3 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/3.png")).toString());
    public static Image n4 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/4.png")).toString());
    public static Image n5 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/6.png")).toString());
    public static Image n7 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/7.png")).toString());

    public static Image i1 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/11.png")).toString());
    public static Image i2 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/22.png")).toString());
    public static Image i3 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/33.png")).toString());
    public static Image i4 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/44.png")).toString());
    public static Image i5 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/66.png")).toString());
    public static Image i7 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/77.png")).toString());

    public static Image b1 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b1.png")).toString());
    public static Image b2 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b2.png")).toString());
    public static Image b3 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b3.png")).toString());
    public static Image b4 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b4.png")).toString());
    public static Image b5 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b5.png")).toString());
    public static Image b6 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b6.png")).toString());
    public static Image b7 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b7.png")).toString());
    public static Image b8 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b8.png")).toString());
    public static Image b9 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b9.png")).toString());

    public static Image b11 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b11.png")).toString());
    public static Image b22 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b22.png")).toString());
    public static Image b33 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b33.png")).toString());
    public static Image b44 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b44.png")).toString());
    public static Image b55 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b55.png")).toString());
    public static Image b66 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b66.png")).toString());
    public static Image b77 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b77.png")).toString());
    public static Image b88 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b88.png")).toString());
    public static Image b99 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/b99.png")).toString());

    public static Image title = new Image(Objects.requireNonNull(IconController.class.getResource("icons/title.png")).toString());


    /**
     *  动态更新图标.
     * @author FGL
     * @Date 2022.12.23
     */
    public static void change(int index, MainUIController m){
        m.b1.setImage(i1);
        m.b2.setImage(i2);
        m.b3.setImage(i3);
        m.b4.setImage(i4);
        m.b5.setImage(i5);
        m.b7.setImage(i7);

        if(index == 1){
            m.b1.setImage(n1);
        }
        if(index == 2){
            m.b2.setImage(n2);
        }
        if(index == 3){
            m.b3.setImage(n3);
        }
        if(index == 4){
            m.b4.setImage(n4);
        }
        if(index == 5){
            m.b5.setImage(n5);
        }
        if(index == 7){
            m.b7.setImage(n7);
        }
    }



}
