package com.example.teamproject.tools;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * @Description 多边形区域的工具类
 * @Author  CZX
 * @Date    2022.12.11
 **/
public class Polygon {
    /**
     * 根据给出的点集创建一个多边形
     * @param pointList 点集
     * @return  多边形
     */
    public static GeneralPath createPolygon(ArrayList<Point2D.Double> pointList){
        GeneralPath p = new GeneralPath();
        //第一个点
        Point2D.Double firstPoint = pointList.get(0);
        p.moveTo(firstPoint.x, firstPoint.y);
        pointList.remove(0);
        //连接生成区域
        for (Point2D.Double d : pointList){
            p.lineTo(d.x, d.y);
        }
        //使区域闭合
        p.lineTo(firstPoint.x, firstPoint.y);
        //结束路径绘制
        p.closePath();

        return p;
    }

    /**
     * 判断给定的点在不在指定多边形里
     * @param point 点
     * @param p 多边形
     * @return  在则返回true
     */
    public static boolean polygonContains(Point2D.Double point, GeneralPath p){
        return p.contains(point);
    }
}
