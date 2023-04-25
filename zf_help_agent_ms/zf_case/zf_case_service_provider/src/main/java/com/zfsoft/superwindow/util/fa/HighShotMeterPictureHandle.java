package com.zfsoft.superwindow.util.fa;

import com.zfsoft.superwindow.util.FaStaticParam;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 图片处理
 * @author: liangss
 * @date: 2020-11-03 16:45:29
 */
public class HighShotMeterPictureHandle  extends PictureHandle{

    /** 最小的轮廓比例 */
    protected static final double MIN_AREA_SCALE = 0.02;
    /** 最大的轮廓比例 */
    protected static final double MAX_AREA_SCALE = 0.95;
    /** 旋转的最小角度 */
    protected static final double ROTATED_ANGLE = 5;
    /** 原图中最小矩形的顶点 */
    private Point[] rectPoints = null;

    public HighShotMeterPictureHandle(String picPath, String picName) throws Exception {
        super(picPath, picName);
    }

    public HighShotMeterPictureHandle(String absPicPath) throws Exception {
        super(absPicPath);
    }

    @Override
    public boolean pretreatment(int width, int height) {
        this.width = width;
        this.height = height;
        // 获取源图像的长度
        double srcWidth = srcMat.width();
        double srcHeight = srcMat.height();
        // 计算源图像的面积
        double srcArea = srcWidth * srcHeight;
        // 最小的轮廓面积
        double minConArea = srcArea * MIN_AREA_SCALE;
        // 最大的轮廓面积
        double maxConArea = srcArea * MAX_AREA_SCALE;

        // 灰度化
        Mat grayMat = new Mat();
        OpenCVImageHandle.cvtColor(srcMat, grayMat);

        // 二值化
        Mat thresholdMat = new Mat();
        OpenCVImageHandle.threshold(grayMat, thresholdMat, 0, 255, Imgproc.THRESH_OTSU);
        grayMat.release();

        // 闭操作
        Mat element = OpenCVImageHandle.getStructuringElement(Imgproc.MORPH_RECT, new Size(17, 3));
        OpenCVImageHandle.morphologyEx(thresholdMat, thresholdMat, Imgproc.MORPH_CLOSE, element);
        element.release();

        // 查找轮廓
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        OpenCVImageHandle.findContours(thresholdMat, contours, hierarchy, Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_SIMPLE);
        thresholdMat.release();
        hierarchy.release();

        boolean flag = false;
        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            MatOfPoint matOfPoint = contours.get(contourIdx);
            MatOfPoint2f point2f = new MatOfPoint2f(matOfPoint.toArray());
            // 获取轮廓的最小矩形
            RotatedRect minRect = OpenCVImageHandle.minAreaRect(point2f);
            // 获取最小矩形的长宽
            double rectWidth = minRect.size.width;
            double rectHeight = minRect.size.height;
            // 计算轮廓矩形面积
            double contourRectArea = rectWidth * rectHeight;

            // 获取面积在一定范围内的轮廓
            if (contourRectArea >= minConArea && contourRectArea <= maxConArea) {

                extractOutline(matOfPoint, point2f, minRect, width, height);

                flag = true;
                break;
            } else {
                point2f.release();
            }
        }
        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            contours.get(contourIdx).release();
        }

        return flag;
    }

    @Override
    public boolean pretreatment() {
        // 获取源图像的长度
        double srcWidth = srcMat.width();
        double srcHeight = srcMat.height();
        // 计算源图像的面积
        double srcArea = srcWidth * srcHeight;
        // 最小的轮廓面积
        double minConArea = srcArea * MIN_AREA_SCALE;
        // 最大的轮廓面积
        double maxConArea = srcArea * MAX_AREA_SCALE;

        // 灰度化
        Mat grayMat = new Mat();
        OpenCVImageHandle.cvtColor(srcMat, grayMat);

        // 二值化
        Mat thresholdMat = new Mat();
        OpenCVImageHandle.threshold(grayMat, thresholdMat, 0, 255, Imgproc.THRESH_OTSU);
        grayMat.release();

        // 闭操作
        Mat element = OpenCVImageHandle.getStructuringElement(Imgproc.MORPH_RECT, new Size(17, 3));
        OpenCVImageHandle.morphologyEx(thresholdMat, thresholdMat, Imgproc.MORPH_CLOSE, element);
        element.release();

        // 查找轮廓
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        OpenCVImageHandle.findContours(thresholdMat, contours, hierarchy, Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_SIMPLE);
        thresholdMat.release();
        hierarchy.release();

        boolean flag = false;
        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            MatOfPoint matOfPoint = contours.get(contourIdx);
            MatOfPoint2f point2f = new MatOfPoint2f(matOfPoint.toArray());
            // 获取轮廓的最小矩形
            RotatedRect minRect = OpenCVImageHandle.minAreaRect(point2f);
            // 获取最小矩形的长宽
            int width = new Double(minRect.size.width).intValue();
            int height = new Double(minRect.size.height).intValue();
            // 计算轮廓矩形面积
            double contourRectArea = width * height;

            // 获取面积在一定范围内的轮廓
            if (contourRectArea >= minConArea && contourRectArea <= maxConArea) {

                extractOutline(matOfPoint, point2f, minRect, width, height);
                point2f.release();
                flag = true;
                break;
            }
        }
        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            contours.get(contourIdx).release();;
        }
        return flag;
    }

    /**
     * 提取轮廓
     *
     * @param matOfPoint
     *            轮廓点集
     * @param minRect
     *            轮廓的最小矩形
     * @param width
     *            处理后的轮廓宽度
     * @param height
     *            处理后的轮廓高度
     */
    private void extractOutline(MatOfPoint matOfPoint, MatOfPoint2f contour2f, RotatedRect minRect, int width, int height) {
        // 如果角度为直角，采用多边形拟合查找顶点
        if (FaStaticParam.ANGLE_TYPE_RIGHT.equals(angleType)) {
            MatOfPoint2f approxCurve = new MatOfPoint2f();

            double approxDistance = OpenCVImageHandle.arcLength(contour2f, true) * 0.03;
            OpenCVImageHandle.approxPolyDP(contour2f, approxCurve, approxDistance, true);
            Point[] ps = approxCurve.toArray();
            approxCurve.release();
            if (ps.length != 4) {
                /* if (ps.length == 5 || ps.length == 6 || ps.length == 7 || ps.length == 8) {
                    rectPoints = OpenCVImageHandle.clearMorePoint(ps, ps.length - 4);
                } else {
                    // 如果多边形拟合得到的顶点不是4个，继续使用最小矩形查找顶点
                    rectPoints = new Point[4];
                    // 获取矩形的四个顶点
                    minRect.points(rectPoints);
                } */
                // 如果多边形拟合得到的顶点不是4个，继续使用最小矩形查找顶点
                rectPoints = new Point[4];
                // 获取矩形的四个顶点
                minRect.points(rectPoints);
            } else {
                // 矩形顶点顺序为顺时针，多边形拟合顶点为逆时针
                rectPoints = ps; // new Point[4];
                // rectPoints[0] = ps[3];
                // rectPoints[1] = ps[2];
                // rectPoints[2] = ps[1];
                // rectPoints[3] = ps[0];
            }
        } else {
            // 如果角度为圆角，采用最新矩形查找顶点
            rectPoints = new Point[4];
            // 获取矩形的四个顶点
            minRect.points(rectPoints);
        }
        // 对顶点进行排序，排序结果为逆时针
        rectPoints = OpenCVImageHandle.pointSort(rectPoints);
        /*Point[] pp = OpenCVImageHandle.pointSort(rectPoints);
        rectPoints[0] = pp[3];
        rectPoints[1] = pp[0];
        rectPoints[2] = pp[1];
        rectPoints[3] = pp[2];*/

        double minXY = rectPoints[0].x + rectPoints[0].y;
        int leftTopPos = 0;
        // 找到左上角的点
        for (int pos = 0; pos < 4; pos++) {
            double xy = rectPoints[pos].x + rectPoints[pos].y;
            if (xy < minXY) {
                minXY = xy;
                leftTopPos = pos;
            }
        }

        // 根据左上角的点，设置仿射变换的四个顶点
        MatOfPoint2f srcP = null;
        // new MatOfPoint2f(rectPoints[1], rectPoints[2], rectPoints[0],
        // rectPoints[3]);
        switch (leftTopPos) {
            case 1:
                srcP = new MatOfPoint2f(rectPoints[1], rectPoints[0], rectPoints[2], rectPoints[3]);
                break;
            case 2:
                srcP = new MatOfPoint2f(rectPoints[2], rectPoints[1], rectPoints[3], rectPoints[0]);
                break;
            case 3:
                srcP = new MatOfPoint2f(rectPoints[3], rectPoints[2], rectPoints[0], rectPoints[1]);
                break;
            default:
                srcP = new MatOfPoint2f(rectPoints[0], rectPoints[3], rectPoints[1], rectPoints[2]);
                break;
        }

        MatOfPoint2f dstP = new MatOfPoint2f(new Point(0, 0), new Point(width, 0), new Point(0, height),
                new Point(width, height));

        //srcP = new MatOfPoint2f(new Point(3832, 1036), new Point(3655, 2586), new Point(1469, 776), new Point(2235, 2531));
        //dstP = new MatOfPoint2f(new Point(278, 206), new Point(1215, 323), new Point(132, 1662), new Point(1194, 1180));

        //根据输入和输出点获得图像透视变换的矩阵
        Mat transform = OpenCVImageHandle.getPerspectiveTransform(srcP, dstP);
        srcP.release();
        dstP.release();

        resultMat = new Mat(height, width, 3);
        // 透视变换
        OpenCVImageHandle.warpPerspective(srcMat, resultMat, transform, resultMat.size());
        transform.release();

    }

    /**
     * 数组求和
     *
     * @param ds
     *            数组
     * @return 和
     */
    public double arraySum(double[] ds) {
        if (ds == null || ds.length < 1) {
            return 0;
        }
        double sum = 0;
        for (double d : ds) {
            sum += d;
        }
        return sum;
    }
}
