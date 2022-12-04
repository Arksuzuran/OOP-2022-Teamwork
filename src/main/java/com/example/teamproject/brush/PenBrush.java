package com.example.teamproject.brush;

/**
 * @Description 钢笔笔刷（即默认的直线）。
 * 对于钢笔来说，其所作的绘画均在effectCanvas上显示。
 * 当落笔时，effectCanvas上的内容会被写入其操作的Layer的image
 * @Author  CZX
 * @Date    2022.11.30
 **/

public class PenBrush extends Brush{

    public PenBrush(){
        super();
    }
    //开始画
    @Override
    public void drawBegin(double x, double y) {
        System.out.println("pen: draw start");
        isDrawing = true;
        effectGc.beginPath();
        effectGc.moveTo(x, y);
        effectGc.stroke();
    }

    //画笔移动到指定位置
    @Override
    public void drawTo(double x, double y) {
        if(isDrawing){
            effectGc.lineTo(x, y);
            effectGc.stroke();
        }
    }

    /**
     * 对于铅笔来说
     */
    @Override
    public void drawEnd() {
        if(isDrawing){
            System.out.println("pen :draw end");
            isDrawing = false;
            //写入layer的image
            activeLayer.updateImage();
            //清空效果层
            effectGc.clearRect(0, 0, effectCanvas.getWidth(), effectCanvas.getHeight());


            System.out.println("drawing result :stored");
        }
    }


}
