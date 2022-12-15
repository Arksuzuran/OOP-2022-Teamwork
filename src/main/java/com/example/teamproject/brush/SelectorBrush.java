package com.example.teamproject.brush;

import com.example.teamproject.controller.ControllerSet;
import com.example.teamproject.effect.FillColorEffect;
import com.example.teamproject.io.Open;
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
import java.io.File;
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
    private ArrayList<Point2D.Double> pointList = new ArrayList<>();//点集
    private GeneralPath polygon = null;//边界点集组成的多边形
    private Rectangle bounds = null;//多边形的矩形边界

    private SelectedRegion selectedRegion = null;//被选中的区域
    private WritableImage selectedImage = null;//裁剪下来的图形
    private WritableImage boundImage = null;//边缘辅助线线图形
    private double posX = 0, posY = 0;//裁剪处的x与y值
    private double deltaX = 0, deltaY = 0;//拖动过程中的总偏移量
    private boolean hasSelected = false;//当前是否已经完成选区绘制

    private boolean boundFollowing = true;//选区边界是否跟随移动
    private boolean regionSave = true;//选区是否在鼠标松开后仍然保留


    //上一次的坐标
    private PathPoint lastPoint;

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
            selectedRegion.setPosition(posX+deltaX, posY+deltaY);
            //更新effectCanvas
            activeLayer.updateEffectCanvas(selectedRegion, false);
            //选择线跟随
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
            createLineSelectedRegion();

            //其他笔刷此时切换到effectCanvas
            PenBrush.getPenBrush().changeSelectedGc(true);
            EraserBrush.getEraserBrush().changeSelected(true);
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

            activeLayer.updateEffectCanvas(selectedRegion, true);
            //activeLayer.updateEffectCanvas(posX+deltaX, posY+deltaY, selectedImage, true);
            activeLayer.clearMainEffectCanvas();
        }
        //无论如何 都停止当前绘画
        if(isDrawing || hasSelected){
            hasSelected = false;
            isDrawing = false;
            init();

            PenBrush.getPenBrush().changeSelectedGc(false);
            EraserBrush.getEraserBrush().changeSelected(false);
        }
    }

    /**
     * 完成选区的绘制 通过选区边界生成选区：
     * 清空原有图层中被选中的区域
     * 将原有图层中被选中的区域转移到效果图层上面
     */
    private void createLineSelectedRegion(){
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
        //颜色数组
        Color[][] colorRegion = new Color[bounds.width+2][bounds.height+2];
//        for(int i=0; i<=bounds.width; i++)
//            for (int j=0; j<=bounds.height; j++)
//                colorRegion[i][j] = Color.TRANSPARENT;

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
                    colorRegion[x-minX][y-minY] = color;

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
//        effectGc.drawImage(selectedImage, posX, posY);
        //生成选区对象
        selectedRegion = new SelectedRegion(inRegion, colorRegion, minX, minY, bounds.width, bounds.height, polygon);

        //及时更新
        activeLayer.updateEffectCanvas(selectedRegion, false);

        System.out.println("select process success. [width]"+bounds.width+" [height]"+bounds.height);
    }

    /**
     * 导入图片并以此新建选区
     * @param file  要导入的图片
     */
    public void createImageSelectedRegion(File file){
        if(hasSelected)
            return;
        //生成选区
        selectedRegion = Open.importImageInSelectedRegion(file);
        if(selectedRegion == null)
            return;

        //更新画面
        activeLayer.updateEffectCanvas(selectedRegion, false);
        System.out.println("import image as selectedRegion success. [width]"+selectedRegion.sizeX+" [height]"+selectedRegion.sizeY);
        hasSelected = true;
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
     * 将选区填充为指定颜色 并及时更新该颜色
     * @param color 所要填充的颜色
     */
    public void fillSelectedRegion(Color color){
        if(hasSelected){
            FillColorEffect.FillSelectedRegion(selectedRegion, color);
            activeLayer.updateEffectCanvas(selectedRegion, false);
            ControllerSet.muc.sendMessage("成功为选区填充颜色："+color+"。");
//            WritableImage image = FillColorEffect.FillSelectedRegion(selectedRegion, color);
//            updateImage(image);
        }
    }
    /**
     * 将选区外的区域填充为指定颜色 并及时更新该颜色
     * @param color 所要填充的颜色
     */
    public void fillUnselectedRegion(Color color){
        if(hasSelected){
            FillColorEffect.FillUnselectedRegion(selectedRegion, color, canvas);
        }
    }

    /**
     * 从前端UI直接接受信息
     */
    public void setBoundFollow(boolean follow){
        boundFollowing = follow;
    }
    public void setRegionSave(boolean save){
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
     * 查询点(x,y)是否在选区内部 如果当前没有选区 那么一定返回true
     * @param x x
     * @param y y
     * @return  在选区内部
     */
    public boolean inSelectedRegion(double x, double y){
        if(!hasSelected)
            return true;
        //将x y反向变换回选区移动前的位置
        x -= deltaX;
        y -= deltaY;
        return Polygon.polygonContains(new Point2D.Double(x, y), polygon);
    }
//    /**
//     * 更新当前的选区为新图片
//     * @param image 新图片
//     */
//    public void updateImage(WritableImage image) {
//        if(hasSelected){
//            selectedImage = image;
//            selectedRegion.selectedImage = image;
//            activeLayer.updateEffectCanvas(posX+deltaX, posY+deltaY, selectedImage, false);
//        }
//    }
    /**
     * 获取当前选区
     */
    public SelectedRegion getSelectedRegion(){
        return selectedRegion;
    }
}
