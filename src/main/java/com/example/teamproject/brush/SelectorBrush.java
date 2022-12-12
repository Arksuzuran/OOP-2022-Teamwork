package com.example.teamproject.brush;

import com.example.teamproject.effect.FillColorEffect;
import com.example.teamproject.structure.SelectedRegion;
import com.example.teamproject.tools.ImageFormConverter;
import com.example.teamproject.tools.Polygon;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * @Description 选区笔
 * @Author  CZX
 * @Date    2022.12.12
 **/
public class SelectorBrush extends Brush{

    /**
     * 每种画笔都是单例模式
     */
    private static final SelectorBrush selectorBrush = new SelectorBrush();
    public static SelectorBrush getSelectorBrush() {
        return selectorBrush;
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
                mainEffectCanvas = mdc.getMainEffectCanvas();
                mainEffectGc = mainEffectCanvas.getGraphicsContext2D();

                System.out.println("selector changes activeLayer to "+activeLayer);
                System.out.println("selector set canvas "+canvas);
            }
        }
    }
    /**
     * 实现不规则选区的数据结构
     */
    ArrayList<Point2D.Double> pointList = new ArrayList<>();//点集
    GeneralPath polygon = null;//边界点集组成的多边形
    Rectangle bounds = null;//多边形的矩形边界

    SelectedRegion selectedRegion = null;//被选中的区域
    WritableImage selectedImage = null;//裁剪下来的图形
    WritableImage boundImage = null;//边缘辅助线线图形
    double posX = 0, posY = 0;//裁剪处的x与y值
    double deltaX = 0, deltaY = 0;//拖动过程中的总偏移量
    boolean hasSelected = false;//当前是否已经完成选区绘制

    boolean boundFollowing = true;//选区边界是否跟随移动
    boolean regionSave = true;//选区是否在鼠标松开后仍然保留
    //上一次的坐标
    PathPoint lastPoint;

    /**
     * 开始绘制选区
     * 当isSelecting为false时，表示正在选择选区
     * 当isSelecting为true时，表示正在拖拽选区
     * @param x 起始点的x
     * @param y 起始点的y
     */
    @Override
    public void drawBegin(double x, double y) {
        //尚未选取
        if(!isDrawing && !hasSelected){
            init();

            isDrawing = true;
            mainEffectGc.beginPath();
            mainEffectGc.lineTo(x, y);
            mainEffectGc.stroke();
            addPointToList(x, y);
            System.out.println("[selector] draw start.");
        }
        //已经选取完成
        else if(!isDrawing){
            lastPoint = new PathPoint(x, y);
            isDrawing = true;
        }
    }

    /**
     * 绘制或拖动选区
     * 当isSelecting为false时，表示正在选择选区
     * 当isSelecting为true时，表示正在拖拽选区
     * @param x 路径点x
     * @param y 路径点y
     */
    @Override
    public void drawTo(double x, double y) {
        PathPoint newPoint = new PathPoint(x, y);

        if(isDrawing && !hasSelected){
            mainEffectGc.lineTo(x, y);
            mainEffectGc.stroke();
            addPointToList(x, y);
        }
        else if(isDrawing){
            deltaX += newPoint.x - lastPoint.x;
            deltaY += newPoint.y - lastPoint.y;

            activeLayer.updateEffectCanvas(posX+deltaX, posY+deltaY, selectedImage, false);
            if(boundFollowing)
                activeLayer.updateMainEffectCanvas(deltaX, deltaY ,boundImage);
            else
                activeLayer.clearMainEffectCanvas();
        }
        lastPoint = newPoint;
    }

    /**
     * 结束绘制 生成多边形
     */
    @Override
    public void drawEnd() {
        if(isDrawing && !hasSelected){
            isDrawing = false;
            hasSelected = true;
            processSelectedRegion();
        }
        else if(isDrawing){
            if(regionSave)
                isDrawing = false;
            else
                endSelecting();
        }
    }

    /**
     * 立即结束本次选区
     * 停止正在进行的选区或选区移动操作
     * 并把效果层的内容移动到本层去
     */
    public void endSelecting(){
        //如果已经完成了选区的绘制 正在移动选区 那么把选区内容合并到canvas里面
        if(hasSelected){

            activeLayer.updateEffectCanvas(posX+deltaX, posY+deltaY, selectedImage, true);
            activeLayer.clearMainEffectCanvas();
        }
        //无论如何 都停止当前绘画
        if(isDrawing || hasSelected){
            hasSelected = false;
            isDrawing = false;
            init();
        }
    }
    /**
     * 完成选区的绘制 生成选区：
     * 清空原有图层中被选中的区域
     * 将原有图层中被选中的区域转移到效果图层上面
     */
    private void processSelectedRegion(){
        //获得多边形和矩形框
        polygon = Polygon.createPolygon(pointList);
        bounds = polygon.getBounds();

        if (bounds.height<=0 || bounds.width <=0)
            return;

        //原图层的图片
        WritableImage oriImage = ImageFormConverter.canvasToImage(canvas);

        PixelReader pixelReader1 = oriImage.getPixelReader();

        PixelWriter pixelWriter1 = gc.getPixelWriter();


        //矩形区域的属性
        int maxX = (int)bounds.getMaxX(), minX = (int)bounds.getMinX(), maxY = (int)bounds.getMaxY(), minY = (int)bounds.getMinY();

        posX = minX;
        posY = minY;

        //创建扣下来的图片
        selectedImage = new WritableImage(bounds.width, bounds.height);
        PixelWriter pixelWriter2 = selectedImage.getPixelWriter();
        //创建边界线图片
        boundImage = ImageFormConverter.canvasToImage(mainEffectCanvas);
        //矩形区的像素点是否在选区内
        boolean[][] inRegion = new boolean[bounds.width+2][bounds.height+2];

        //遍历所有可能的像素点
        for(int x = minX; x <= maxX; x++){
            for(int y = minY; y <= maxY; y++){

                Point2D.Double p = new Point2D.Double(x, y);

                //在多边形中 那么加入扣下来的图片 并从原图中移除
                if(Polygon.polygonContains(p, polygon)){
                    //取得颜色
                    Color color = pixelReader1.getColor(x, y);

                    //抠出来的图上复制颜色 注意新图的大小变小了 所以要进行坐标变换
                    pixelWriter2.setColor(x-minX, y-minY, color);

                    //原图上该像素点直接设置为透明
                    color = Color.TRANSPARENT;
                    pixelWriter1.setColor(x, y, color);
                    //System.out.println("in: "+x+" "+y);
                    inRegion[x-minX][y-minY] = true;
                }
            }
        }

        //将图片再写入canvas 和effectCanvas
//        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
//        gc.drawImage(oriImage, 0, 0);
        effectGc.drawImage(selectedImage, posX, posY);

        //生成选区对象
        selectedRegion = new SelectedRegion(selectedImage, inRegion);

        System.out.println("select process success. [width]"+bounds.width+" [height]"+bounds.height);
    }

    public void addPointToList(double x, double y){
        pointList.add(new Point2D.Double(x, y));
    }

    /**
     * 初始化选区笔
     */
    private void init(){
        isDrawing = false;
        hasSelected = false;
        pointList.clear();
        polygon = null;
        bounds = null;
        selectedImage = null;
        boundImage = null;
        selectedRegion = null;

        deltaX = 0;
        deltaY = 0;

        mainEffectGc.setLineDashes(5,10);
        mainEffectGc.setLineWidth(1);
    }

    /**
     * 将选区填充为指定颜色
     * @param color 所要填充的颜色
     */
    public void fillSelectedRegion(Color color){
        if(hasSelected){
            WritableImage image = FillColorEffect.FillSelectedRegion(selectedRegion, color);
            updateImage(image);
        }
    }

    /**
     * 从前端UI直接接受信息
     */
    public void changeBoundFollow(boolean follow){
        boundFollowing = follow;
    }
    public void changeRegionSave(boolean save){
        regionSave = save;
        endSelecting();
    }

    /**
     * 返回该笔刷当前是否已选了一个图层 传递给前端UI 在工作时不允许切换图层
     * @return  已经选择了图层
     */
    public boolean hasSelected(){
        return hasSelected;
    }

    /**
     * 更新当前的选区为新图片
     * @param image 新图片
     */
    public void updateImage(WritableImage image) {
        if(hasSelected){
            selectedImage = image;
            selectedRegion.selectedImage = image;
            activeLayer.updateEffectCanvas(posX+deltaX, posY+deltaY, selectedImage, false);
        }
    }
    /**
     * 获取当前选区
     */
    public SelectedRegion getSelectedRegion(){
        return selectedRegion;
    }
}
