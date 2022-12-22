package com.example.teamproject.brush;

import com.example.teamproject.effect.ImageEffect;
import com.example.teamproject.structure.SelectedRegion;
import com.example.teamproject.tools.ImageFormConverter;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.*;
import javafx.scene.shape.StrokeLineCap;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @Description 钢笔笔刷（即默认的直线）。
 * 对于钢笔来说，其所作的绘画均在canvas上直接显示。
 * @Author  CZX
 * @Date    2022.12.10
 **/

public class PenBrush extends Brush{

    /**
     * 每种画笔都是单例模式
     */
    private static final PenBrush Pen = new PenBrush();
    public static PenBrush getPenBrush() {
        return Pen;
    }
    private PenBrush(){}

    public static SelectorBrush selectorBrush = SelectorBrush.getSelectorBrush();
    //画笔颜色 默认为黑
    private Color color = Color.BLACK;
    private double opacity = 0;
    private Boolean isSoft = false;
    private Image brushMaterial = null;
    private Paint paint = null;
    //当前正在操作的画笔
    private GraphicsContext nowGc = null;

    private boolean selected = false;

    public void setColor(Color color) {
        this.color = color;
        nowGc.setStroke(color);
    }
    //设置新的滑动条，范围0-1
    public void setOpacity(double opacity){
        this.opacity = opacity;
        this.color = new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), opacity);
        nowGc.setStroke(color);
    }
    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
        nowGc.setLineWidth(lineWidth);
    }

    /**
     * 多种笔刷的实现
     */
    private static final String SOFT = "soft";
    private static final String GRID_1 = "brushtex/Canvas.bmp";
    private static final String CARPET_1 = "brushtex/Carpet 01.bmp";
    private static final String CLOUD_1 = "brushtex/Cloud 01.bmp";
    private static final String DIRT_1 = "brushtex/Dirt 01.bmp";
    private static final String DIRT_2 = "brushtex/Dirt 06.bmp";
    private static final String DIRT_3 = "brushtex/Dirt 09.bmp";
    private static final String FABRIC_1 = "brushtex/Fabric 02.bmp";
    private static final String FUZY_1 = "brushtex/Fuzystatic.bmp";
    private static final String GLASS_1 = "brushtex/Glass 02.bmp";
    private static final String LEAVES_1 = "brushtex/Leaves 04.bmp";
    private static final String METAL_1 = "brushtex/Metal 01.bmp";
    private static final String NOISE_1 = "brushtex/Noise 01.bmp";
    private static final String PAPER_1 = "brushtex/Paper 01.bmp";
    private static final String ROCK_1 = "brushtex/Rock 01.bmp";
    private static final String SPOT = "brushtex/Spot 02.BMP";
    private static final String WAVE_1 = "brushtex/Wave 01.bmp";
    private static final String WAVE_2 = "brushtex/Wave 08.bmp";
    private static final String WOOD_1 = "brushtex/Wood 03.bmp";

    //达不到预期的调节笔刷硬度的效果，只能大致的实现软笔刷，只分成启用或者不启用
    //启用时需要在setColor,setOpacity之后调用：ui上搞一个勾选框，勾上则把isSoft设置为true
    /**
     * 设置画笔的笔刷效果
     * @param url   笔刷常量
     */
    public void setBrushMaterial(String url){
        if(url.equals("soft")){
            nowGc.setStroke(createSoftBrushGradient(color, lineWidth));//直径还是半径？
        }
        else {
            Image image = new Image(url);

            /*

                图像处理

             */

            nowGc.setStroke(new ImagePattern(image));
        }



        /*
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("D:\\Personal\\computing\\java\\OOP\\OOP-2022-Teamwork\\brushtex\\Carpet 02.bmp"));
        }
        catch (IOException e) {
            System.out.println(e);
        }
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                int p = img.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (int)(color.getRed() * 256);
                int g = (int)(color.getGreen() * 256);
                int b = (int)(color.getBlue() * 256);
                // set new RGB keeping the r
                // value same as in original image
                // and setting g and b as 0.
                p = (a << 24) | (r << 16) | (g << 8) | b;

                img.setRGB(x, y, p);
            }
        }
        ImagePattern imagePattern = new ImagePattern(SwingFXUtils.toFXImage(img, null));
         */
    }
    //只有圆形，正方形，三角形
    public void setCap(StrokeLineCap strokeLineCap){
        nowGc.setLineCap(strokeLineCap);
    }

    //更新当前选中的图层
    @Override
    public void updateActiveLayer(){
        if (mdc.isActive()){
            activeLayer = mdc.getActiveLayer();
            if(activeLayer != null){
                //更新笔刷的引用
                canvas = activeLayer.getCanvas();
                gc = canvas.getGraphicsContext2D();
                effectCanvas = activeLayer.getEffectCanvas();
                effectGc = effectCanvas.getGraphicsContext2D();

                if(selected)
                    nowGc = effectGc;
                else
                    nowGc = gc;//默认画笔

                //笔刷设置为当前选择的状态
                setColor(color);
                setLineWidth(lineWidth);

                System.out.println("pen changes activeLayer to "+activeLayer);
                System.out.println("pen set canvas "+canvas);
            }
        }
    }

    /**
     * 实现抖动修正的数据结构
     */
    //存储路径点列表
    LinkedList<PathPoint> pointQueue = new LinkedList<>();
    //用于二阶抖动修正
    PathPoint startPoint, controlPoint, endPoint;
    //抖动修正等级
    private int smoothLevel = 1;
    public void setSmoothLevel(int level){
        smoothLevel = level;
    }

    private static final double GAP_TWO = 0.5;
    private static final double GAP_THREE = 1;
    /**
     * 开始绘画
     * @param x 起始点的x
     * @param y 起始点的y
     */

    @Override
    public void drawBegin(double x, double y) {
        initSmoothing();
        //只能在选区内工作 一旦检测到当前笔刷超出了选区 那么不允许继续绘画
        if(!selectorBrush.inSelectedRegion(x, y))
            return;

        isDrawing = true;
        setCap(StrokeLineCap.ROUND);
        /*
        if(isSoft){
            setSoft();
        }
        else {
            setColor(color);
        }
         */
        System.out.println("[pen] draw start. [color]"+color+" [smoothLevel]"+smoothLevel);
        if(smoothLevel <= 1){
            startPoint = new PathPoint(x, y);
//            nowGc.beginPath();
//            nowGc.moveTo(x, y);
//            nowGc.stroke();
        }
        //等级2
        else if(smoothLevel == 2){
            startPoint = new PathPoint(x, y);
        }
        //等级3 4
        else{
            pointQueue.addLast(new PathPoint(x, y));
        }
    }

    /**
     * 绘制路径点
     * @param x 路径点x
     * @param y 路径点y
     */
    @Override
    public void drawTo(double x, double y) {
        if(isDrawing){
            //只能在选区内工作 一旦检测到当前笔刷的边缘超出了选区 那么不让绘画
            if(!selectorBrush.inSelectedRegion(x, y)){
                drawClose();
                return;
            }
            PathPoint inputPoint = new PathPoint(x, y);

            if(smoothLevel <= 1){
                drawLine(startPoint, inputPoint);

                startPoint = inputPoint;
            }
            //等级2
            else if(smoothLevel == 2){
                //上一步刚刚推入起点p1 那么本步不需要画曲线 而是取p2作为控制点
                if (controlPoint != null) {
                    endPoint = getMidPoint(controlPoint, inputPoint);
                    //要求间隔大于GAP_TWO时才允许画线
                    if(calDistance2(startPoint, endPoint)>GAP_TWO)
                        drawQuadraticCurve(startPoint, controlPoint, endPoint);
                    startPoint = endPoint;
                }
                controlPoint = inputPoint;
            }
            //等级3
            else if(smoothLevel == 3){
                //要求间隔大于GAP_THREE时才允许添加
                if(calDistance2(pointQueue.getLast(), inputPoint)>GAP_THREE)
                    pointQueue.addLast(inputPoint);
                //凑够三个路径点 就进行一次绘制
                if(pointQueue.size()==3){
                    drawQuadraticCurve(pointQueue.get(0), pointQueue.get(1), pointQueue.get(2));
                    pointQueue.removeFirst();   pointQueue.removeFirst();
                }
            }
            //等级4及以上
            else{
                //要求间隔大于smoothLevel*0.5时才允许添加
                if(calDistance2(pointQueue.getLast(), inputPoint)>smoothLevel)
                    pointQueue.addLast(inputPoint);
                //凑够四个路径点 就进行一次绘制
                if(pointQueue.size()==4){
                    drawTripleCurve(pointQueue.get(0), pointQueue.get(1), pointQueue.get(2), pointQueue.get(3));
                    pointQueue.removeFirst();   pointQueue.removeFirst();   pointQueue.removeFirst();
                }
            }
        }
    }

    @Override
    public void drawEnd(double x, double y) {
        if(isDrawing){
            //如果当前有选区 那么将结果反向写回选区
            if(SelectorBrush.getSelectorBrush().hasSelected())
                activeLayer.updateColorRegionByEffectCanvas();
            activeLayer.saveOp();
            drawClose();
            System.out.println("[pen] draw end");
            isDrawing = false;
        }
    }

    /**
     * 进行当前绘画的结算 但保留isDrawing
     */
    private void drawClose(){
        if(smoothLevel == 2){
            //此时没有了inputPoint 那么直接将剩下的startPoint和controlPoint相连即可
            drawLine(startPoint, controlPoint);
        }
        //三阶
        else if(smoothLevel>=3) {
            //根据余下路径点的数量进行绘制
            if(pointQueue.size()==3){
                drawQuadraticCurve(pointQueue.get(0), pointQueue.get(1), pointQueue.get(2));
                pointQueue.clear();
            }
            else if(pointQueue.size()==2){
                drawLine(pointQueue.get(0), pointQueue.get(1));
                pointQueue.clear();
            }
        }
        initSmoothing();
    }

    /**
     * 初始化抖动修正所需的数据结构
     */
    private void initSmoothing(){
        startPoint = null;
        controlPoint = null;
        endPoint = null;
        pointQueue.clear();
    }
    private PathPoint getMidPoint(PathPoint p1, PathPoint p2){
        return new PathPoint((p1.x+ p2.x)/2, (p1.y+ p2.y)/2);
    }
    private double calDistance2(PathPoint p1, PathPoint p2){
        return (p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y);
    }
    private void drawLine(PathPoint sp, PathPoint ep){
        if(sp==null || ep==null)
            return;
        nowGc.beginPath();
        nowGc.moveTo(sp.x, sp.y);
        nowGc.lineTo(ep.x, ep.y);
        nowGc.stroke();
        nowGc.closePath();
        eraseOutRegion(ep.x, ep.y);//删除超出选区的部分
    }
    /**
     * 绘制二阶贝赛尔曲线
     * @param sp 起始点
     * @param cp 控制点
     * @param ep 结束点
     */
    private void drawQuadraticCurve(PathPoint sp, PathPoint cp, PathPoint ep){
        if(sp==null || ep==null || cp==null)
            return;
        nowGc.beginPath();
        nowGc.moveTo(sp.x, sp.y);
        nowGc.quadraticCurveTo(cp.x, cp.y, ep.x, ep.y);
        nowGc.stroke();
        nowGc.closePath();
        eraseOutRegion(ep.x, ep.y);//删除超出选区的部分
    }
    /**
     * 绘制三阶贝塞尔曲线
     * @param sp    起始点
     * @param cp1   控制点1
     * @param cp2   控制点2
     * @param ep    结束点
     */
    private void drawTripleCurve(PathPoint sp, PathPoint cp1, PathPoint cp2, PathPoint ep){
        if(sp==null || ep==null || cp1==null || cp2==null)
            return;
        nowGc.beginPath();
        nowGc.moveTo(sp.x, sp.y);
        nowGc.bezierCurveTo(cp1.x, cp1.y, cp2.x, cp2.y, ep.x, ep.y);
        nowGc.stroke();
        nowGc.closePath();
        eraseOutRegion(ep.x, ep.y);//删除超出选区的部分
    }

    /**
     * 根据当前是否有选区来调整所使用的gc
     * @param selected  有选区
     */
    public void changeSelectedGc(boolean selected){
        this.selected = selected;
        if(selected)
            nowGc = effectGc;
        else
            nowGc = gc;
    }

    /**
     * 删除当前画布中 超出选区的部分
     */
    public void eraseOutRegion(double x, double y){
        if(!selected)
            return;
        PixelWriter pixelWriter = nowGc.getPixelWriter();
        SelectedRegion selectedRegion = SelectorBrush.getSelectorBrush().getSelectedRegion();
        int sx = (int)(x - lineWidth), ex = (int)(x + lineWidth), offsetX = (int)selectedRegion.x;
        int sy = (int)(y - lineWidth), ey = (int)(y + lineWidth), offsetY = (int)selectedRegion.y;
        //在所有可能的区域内 如果超出 那么清除
        for(int i=sx; i<ex; i++){
            for (int j=sy; j<ey; j++){
                if(i<0 || j<0)
                    continue;
                if(!selectedRegion.pointInRegionRelative(i-offsetX, j-offsetY)){
                    pixelWriter.setColor(i, j, Color.TRANSPARENT);
                }
            }
        }
    }

    private RadialGradient createSoftBrushGradient(Color primaryColor, double radius) {
        return new RadialGradient(
                0, 0,
                0, 0,
                radius,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, primaryColor),
                new Stop(1, Color.TRANSPARENT)
        );
    }
}
