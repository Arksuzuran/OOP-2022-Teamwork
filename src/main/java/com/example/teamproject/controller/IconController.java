package com.example.teamproject.controller;

import javafx.scene.image.Image;

import java.util.Objects;

public class IconController {

    public static Image n1 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/1.png")).toString());
    public static Image n2 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/2.png")).toString());
    public static Image n3 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/3.png")).toString());
    public static Image n4 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/4.png")).toString());
//    public static Image n5 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/5.png")).toString());
    public static Image n5 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/6.png")).toString());

    public static Image i1 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/11.png")).toString());
    public static Image i2 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/22.png")).toString());
    public static Image i3 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/33.png")).toString());
    public static Image i4 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/44.png")).toString());
//    public static Image i5 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/55.png")).toString());
    public static Image i5 = new Image(Objects.requireNonNull(IconController.class.getResource("icons/66.png")).toString());

    public static void change(int index, MainUIController m){
        m.b1.setImage(i1);
        m.b2.setImage(i2);
        m.b3.setImage(i3);
        m.b4.setImage(i4);
        m.b5.setImage(i5);

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
    }

}
