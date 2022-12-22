package com.example.teamproject.controller;

import com.example.teamproject.HelloApplication;
import com.example.teamproject.brush.Brush;
import com.example.teamproject.brush.BrushType;
import com.example.teamproject.brush.PenBrush;
import com.example.teamproject.brush.SelectorBrush;
import com.example.teamproject.io.Open;
import com.example.teamproject.io.Save;
import com.example.teamproject.structure.Layer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * @Description 目前主界面上常驻的UI组件的Controller
 * @Author  CZX FGL
 * @Date    2022.11.30
 **/
public class MainUIController {

    //按钮图标
    public ImageView b1;
    public ImageView b2;
    public ImageView b3;
    public ImageView b4;
    public ImageView b5;
    public ImageView b7;

    @FXML
    protected Label welcomeText;
    @FXML
    protected Text OutputText;
    @FXML
    protected Text PositionText;
    @FXML
    protected VBox LayerBox;
    @FXML
    protected VBox BrushBox;
    //按钮
    @FXML
    protected Button NewWorkButton;
    @FXML
    protected Button NewLayerButton;

    //调色板
    @FXML
    protected ColorPicker PenColorPicker;//画笔的选色器
    @FXML
    protected ColorPicker RegionColorPicker;//选区的选色器
    /**
     * 滑动条部分
     */
    //画笔粗细滑动条
    @FXML
    protected Slider PenWidthSlider;
    //画笔粗细宽度显示标签
    @FXML
    protected Label PenWidthLabel;

    @FXML
    //抖动修正滑动条
    protected Slider SmoothLevelSlider;
    //抖动修正值显示标签
    @FXML
    protected Label SmoothLevelLabel;

    /**
     * checkbox
     */
    @FXML
    protected CheckBox SelectSaveCheckBox;
    @FXML
    protected CheckBox SelectLineCheckBox;


    public ScrollPane getDrawingScrollPane() {
        return DrawingScrollPane;
    }

    /**
     * 画图层各部分的引用 在创建新画布后必须对此进行更新！否则后端无法工作！
     */
    //绘图区
    @FXML
    protected ScrollPane DrawingScrollPane;

    protected Canvas mainEffectCanvas = null;
    protected Pane mainDrawingPane = null;
    protected CanvasController canvasController = null;

    //画布尺寸
    protected double sizeX = 980;
    protected double sizeY = 900;

    protected String name = null;
    protected boolean hasActiveWork = false;
    //绘图主控的引用
    protected MainDrawingController mdc = MainDrawingController.getMDC();

    {
        ControllerSet.muc = this;
    }

    //============================================创建新作品==============================================//
    /**
     * 根据当前选择的尺寸 创建新的画布 并将其加入mainDrawingPane的底部
     * @return 新画布的引用
     */
    public Canvas createNewCanvasAddingToPane(){
        Canvas canvas = new Canvas(sizeX, sizeY);
        canvas.setOpacity(1);
        canvas.setStyle("-fx-background-color: WHITE");
        canvasController.addNewCanvasAtBack(canvas);
        return canvas;
    }
    /**
     * @Description 按下“新空作品”按钮时，生成新的空作品.
     * @Author  CZX FGL
     * @Date    2022.12.4
     */
    @FXML
    protected void OnNewWorkButtonClick(){
        System.out.println("NewWorkButtonClick");
        if(!hasActiveWork) {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("new_work-view.fxml"));
            try {
                Scene scene = new Scene(loader.load(), 350, 190);
                Stage stage = new Stage();
                stage.setTitle("New");

                NewWorkUIController controller = loader.getController();
                controller.init(stage);

                stage.setScene(scene);
                stage.show();
            }catch (IOException e){
                sendMessage("创建作品失败, 文件IO异常。");
            }
        }
        else{

            /**
             *
             * 此处填充IO代码
             * 在已存在作品的情况下新建作品 需要询问覆盖情况
             *
             */
        }
    }

    @FXML
    protected void OnImportWorkButtonClick(){
        File file = Open.getInputFile();
        if(file != null){
            Image image = Open.importImage(file);
            sizeX = image.getWidth();
            sizeY = image.getHeight();
            Layer layer = createNewWork(sizeX, sizeY, name);
            layer.importImageToCanvas(image);
        }
        else{
            sendMessage("[打开文件] 文件未成功打开。可能是您手动取消了导入，或者文件格式不支持。");
        }
    }
    public void setName(String name) {
        this.name = name;
    }


    /**
     * 创建新作品
     * 该方法会重置主控类
     * 创建新作品需要4步：
     * 1.加载画布Pane和mainEffectPane
     * 2.创建第一个图层layer1
     * 3.用canvas、layer1、this初始化主控类
     * 后续优化：当前没有作品时会在画图板的位置显示其他UI
     * @param sizeX 新作品的宽度
     * @param sizeY 新作品的高度
     * @return 首个图层的引用
     */
    protected Layer createNewWork(double sizeX, double sizeY, String name){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        createNewCanvasField();
        hasActiveWork = true;
        Layer layer1 = createNewLayer();
        mdc.initialize(mainEffectCanvas, layer1, this, sizeX, sizeY, name);
        sendMessage(String.format("[新建作品] 成功新建作品, 高度%d, 宽度%d，名称 %s", (int)sizeX, (int)sizeY ,name));
        return layer1;
        //layer1.setImage(new Image("E:\\JavaTeamwork\\OOP-2022-Teamwork\\8.png"));
    }

    /**
     * 生成新的画图板区域 该方法会直接重置画图区
     * 该方法需要前端根据自己所写的fxml更改
     */
    public void createNewCanvasField(){
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("canvas-view.fxml"));
        try {
            //加载画布区域
            Pane pane = loader.load();

            DrawingScrollPane.setContent(pane);//注意：ScrollPane内只允许有一个Node

            //获取画布区域的控制类(注意！该方法必须在load之后使用！)
            canvasController = loader.getController();
            ControllerSet.canvasController = canvasController;

            //获取自带的效果画布 以及画图区域pane的引用
            mainEffectCanvas = canvasController.getMainEffectCanvas();
            mainDrawingPane = canvasController.getDrawingPane();

            //修改大小至预定值
            mainDrawingPane.setPrefWidth(sizeX);
            mainDrawingPane.setPrefHeight(sizeY);

            mainEffectCanvas.setWidth(sizeX);
            mainEffectCanvas.setHeight(sizeY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void OnAutoSaveWorkButtonClick(){
        if(mdc.isActive()){
            mdc.save(null, true);

        }
        else{
            sendMessage("[保存作品] 要进行保存，您首先需要新建一个作品。");
        }
    }
    /**
     * 按下另存按钮
     */
    @FXML
    protected void OnSaveWorkButtonClick(){
        if(mdc.isActive()){
            File file = Save.getOutputFile();
            if(file != null){
                mdc.save(file, false);

            }
            else{
                sendMessage("[另存作品] 文件保存失败。可能是您选择的不支持的类型。");
            }
        }
        else{
            sendMessage("[另存作品] 要进行保存，您首先需要新建一个作品。");
        }
    }

    /**
     * 按下修改自动保存路径的按钮
     */
    @FXML
    protected void OnAutoSaveChangeButtonClick(){
        if(mdc.isActive()){
            File file = Save.getOutputFile();
            if(file != null){
                mdc.setAutoSaveFile(file);
                sendMessage("[自动保存] 已成功为您修改自动保存路径。");
            }
            else{
                sendMessage("[自动保存] 文件打开失败。请确保您打开的是支持的文件类型。");
            }
        }
        else{
            sendMessage("[自动保存] 要修改自动保存路径，您首先需要新建一个作品。");
        }
    }


//==================================================图层===========================================//
    /**
     * 点击“新建图层按钮“
     * 生成新图层 并添加至总控类中
     */
    @FXML
    protected void onNewLayerButtonClick() {
        //只有主控激活的时候才能创建新图层
        if(mdc.isActive()){
            Layer layer = createNewLayer();
            mdc.addNewLayer(layer);
            sendMessage("[新建图层] 成功新建图层。");
        }
        else{
            sendMessage("[新建图层] 要新建图层，请先新建作品。");
        }
    }

    /**
     * 新建一个图层对象 及其对应的边栏UI 并初始化该对象
     * 该方法需要前端同学根据自己所写的fxml更改这句话：FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("layer-view.fxml"));
     * @return  返回该图层的引用
     */
    //生成新图层
    public Layer createNewLayer(){
        //如果当前没有作品 那么是不可能创建图层的
        if(!hasActiveWork){
            System.out.println("No active work. Cannot create new layer");
            return null;
        }
        //载入图层UI的fxml
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("layer-view.fxml"));

        try {
            //生成左侧边栏的图层UI
            VBox tmp;
            tmp = loader.load();
            LayerBox.getChildren().add(tmp);

            //添加两个Canvas

            //效果画布
            Canvas effectCanvas = createNewCanvasAddingToPane();
            //主画布
            Canvas canvas = createNewCanvasAddingToPane();

            //获取画布UI的控制类
            LayerUIController layerUIController = loader.getController();
            //UI控制类和layer互相持有对方的引用
            Layer layer = new Layer(canvas, effectCanvas, mainEffectCanvas, layerUIController);
            layerUIController.setLayer(layer);
            layerUIController.setMuc(this);
            //生成Layer类
            return layer;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 删除某个图层
     * @param layerUIController 要删除的图层的controller
     */
    public void DeleteLayer(LayerUIController layerUIController){
        ObservableList<Node> list = LayerBox.getChildren();
        Layer layer = layerUIController.getLayer();
        //最后一个图层不允许删除
        if(list.size()<=1){
            System.out.println("delete layer failed: last layer");
            sendMessage("[删除图层] 您不能删除最后一个图层");
            return;
        }
        //先后在总控类 画图区 删除该图层
        mdc.delLayer(layer);
        canvasController.deleteLayer(layer);
        //给总控类设置新的被选中的图层
        layer = canvasController.getTopLayer();
        mdc.setActiveLayer(layer);
        //在UI界面删除该图层的UI
        VBox layerBox = layerUIController.getLayerPane();
        for (Node node : list){
            if(node == layerBox){
                LayerBox.getChildren().remove(node);
                break;
            }
        }
        System.out.println("delete layer success");
        sendMessage("[删除图层] 图层已被删除");
    }

    /**
     * 按下撤回按钮
     */
    @FXML
    protected void OnUndoButtonClick(){
        if(mdc.isActive()){
            if(mdc.getActiveLayer().undoOp()){
                sendMessage("[撤回] 已成功撤回操作。");
            }
            else{
                sendMessage("[撤回] 图层已被清空。");
            }
        }
    }

    /**
     * 按下重做按钮
     */
    @FXML
    protected void OnReDoButtonClick(){
        if(mdc.isActive()){
            if(mdc.getActiveLayer().RedoOp()){
                sendMessage("[重做] 已成功取回被撤销的操作。");
            }
            else{
                sendMessage("[重做] 当前没有可取回的操作");
            }
        }
    }

//=============================================画笔=====================================================//
    /**
     * 选择画笔的按钮
     * 1.更换上新的UI
     * 2.如果主控激活 那么选择画笔作为笔刷
     */
    //选中铅笔
    @FXML
    protected void onPenBrushButtonClick(){
        IconController.change(1, this);
        //更新UI
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("pen-view.fxml"));
        VBox tmp = null;
        try {
            tmp = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BrushBox.getChildren().clear();
        BrushBox.getChildren().add(tmp);
        ControllerSet.penUIController = loader.getController();

        ControllerSet.penUIController.init(0);

        //只有主控激活时才能选择笔刷
        if(mdc.isActive()){
            mdc.setActiveBrush(BrushType.PEN);
            //根据当前UIController里 选中的笔刷信息 来设置笔刷对象的属性
//            updatePenColor();
//            updatePenWidth();
            sendMessage("[画笔] 成功选中画笔");
        }
    }
    /**
     * 颜色选择器被操作
     * 画笔的颜色选择器
     */
    public void updatePenColor(){

        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof PenBrush){
                ((PenBrush) brush).setColor(PenColorPicker.getValue());
            }
            sendMessage("[画笔] 成功选择颜色");
        }
    }
    @FXML
    protected void OnPenColorPickerSet(){
        updatePenColor();
    }

    /**
     * 如果当前有作品且选中了钢笔笔刷 那么调节其粗细
     * 更新对应UI
     */
    @FXML
    protected void OnPenWidthSliderSet(){
        updatePenWidth();
    }
    public void updatePenWidth(){
        double penWidth = PenWidthSlider.getValue();
        PenWidthLabel.setText(Integer.toString((int)penWidth));

        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof PenBrush){
                ((PenBrush) brush).setLineWidth(penWidth);
            }
            sendMessage("[画笔] 成功调整大小");
        }
    }
    @FXML
    protected void OnSmoothLevelSliderSet(){
        updateSmoothLevel();
    }

    /**
     * 如果当前有作品且选中了钢笔笔刷 那么调节其抖动修正级别
     * 更新对应UI
     */
    public void updateSmoothLevel(){
        int smoothLevel = (int)SmoothLevelSlider.getValue();
        SmoothLevelLabel.setText(Integer.toString(smoothLevel));

        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof PenBrush){
                ((PenBrush) brush).setSmoothLevel(smoothLevel);
            }
            sendMessage("[画笔] 成功调整抖动修正等级");
        }
    }

    /**
     * 选择橡皮的按钮
     */
    @FXML
    protected void onEraserBrushButtonClick(){
        IconController.change(3, this);
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("eraser-view.fxml"));
        VBox tmp = null;
        try {
            tmp = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BrushBox.getChildren().clear();
        BrushBox.getChildren().add(tmp);

        if(mdc.isActive()){
            mdc.setActiveBrush(BrushType.ERASER);
            sendMessage("[橡皮] 成功选中橡皮");
        }
    }
    @FXML
    protected void onMoverButtonClick(){
        IconController.change(7, this);
        BrushBox.getChildren().clear();

        if(mdc.isActive()){
            mdc.setActiveBrush(BrushType.MOVEBRUSH);
            sendMessage("[移动] 成功选中移动");
        }
    }
//=================================================选区======================================================//
    /**
     * 选择选区笔的按钮
     */
    @FXML
    protected void onSelectorButtonClick(){
        IconController.change(2, this);
        //更新UI
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("selector-view.fxml"));
        VBox tmp = null;
        try {
            tmp = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BrushBox.getChildren().clear();
        BrushBox.getChildren().add(tmp);
        ControllerSet.selectorUIController = loader.getController();

        //只有主控激活的时候才能选择笔刷
        if(mdc.isActive()){
            mdc.setActiveBrush(BrushType.SELECTOR);
            sendMessage("当前工具切换为选区笔。");
            sendMessage("[橡皮] 成功选中选区笔");
        }
    }

    /**
     * 选区的颜色填充
     */
    public void fillRegion(){
        Color color = RegionColorPicker.getValue();
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof SelectorBrush){
                ((SelectorBrush) brush).fillSelectedRegion(color);

            }
        }
    }

    /**
     * 为选区填充指定颜色
     */
    @FXML
    protected void OnRegionFillButtonClick(){
        fillRegion();
    }


    /**
     * 确认选区
     */
    @FXML
    protected void OnRegionSelectedButtonClick(){
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof SelectorBrush){
                ((SelectorBrush) brush).endSelecting();
            }
        }
    }

    /**
     * 选区不保留（松手即确定选区）
     */
    @FXML
    protected void OnSelectSaveCheckBoxChanged(){
        boolean save = SelectSaveCheckBox.isSelected();
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof SelectorBrush){
                ((SelectorBrush) brush).setRegionSave(save);
            }
        }
    }

    /**
     * 选区边界线不跟随
     */
    @FXML
    protected void OnSelectLineCheckBoxChanged(){
        boolean hasLine = SelectLineCheckBox.isSelected();
        if(mdc.isActive()){
            Brush brush = mdc.getActiveBrush();
            if(brush instanceof SelectorBrush){
                ((SelectorBrush) brush).setBoundFollow(hasLine);
            }
        }
    }

    //==========================图形=========================
    @FXML
    protected void onShapeBrushButtonClick(){
        IconController.change(4, this);
        fillRegion();

        //更新UI
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("shape-view.fxml"));
        VBox tmp = null;
        try {
            tmp = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BrushBox.getChildren().clear();
        BrushBox.getChildren().add(tmp);
        ControllerSet.shapeController = loader.getController();
    }

    @FXML
    protected void onImageProcessButtonClick(){
        IconController.change(5, this);
        fillRegion();

        //更新UI
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("process-view.fxml"));
        VBox tmp = null;
        try {
            tmp = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BrushBox.getChildren().clear();
        BrushBox.getChildren().add(tmp);
        ControllerSet.processController = loader.getController();
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    /**
     * 设置底部提示文本
     * @param s 要设置的文本
     */
    public void sendMessage(String s){
        OutputText.setText(s);
    }
    public void updatePositionText(String s){
        PositionText.setText(s);
    }
}