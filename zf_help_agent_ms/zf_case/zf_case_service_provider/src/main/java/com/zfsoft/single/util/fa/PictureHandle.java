package com.zfsoft.single.util.fa;

import cn.hutool.core.date.DateUtil;
import com.zfsoft.single.util.FaStaticParam;
import com.zfsoft.single.util.StringUtil;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 材料目录图片识别结果接口
 * @author: liangss
 * @date: 2020-11-03 16:45:29
 */
public abstract class PictureHandle {

    /**
     * 拼接图片的分割图片名称
     */
    private static final String JOIN_SEQ_PIC_NAME = "pic_cut_join.jpg";
    /**
     * 拼接图片的分割图片名称
     */
    private static final String JOIN_MARQUEE_SEQ_PIC_NAME = "pic_cut_join_marquee.jpg";
    /**
     * 内部矩形最小面积
     */
    private static final double INNER_RECT_MIN_AREA = 0.4;
    /**
     * 内部矩形最大面积
     */
    private static final double INNER_RECT_MAX_AREA = 0.85;
    /**
     * 图片路径
     */
    protected String picPath;
    /**
     * 图片名称
     */
    protected String picName;
    /**
     * 文件扩展名
     */
    protected String picExt;
    /**
     * 处理后的图片尺寸-宽
     */
    protected int width;
    /**
     * 处理后的图片尺寸-高
     */
    protected int height;
    /**
     * 源图像
     */
    protected Mat srcMat;
    /**
     * 图像结果
     */
    protected Mat resultMat = null;
    /**
     * 图片角的类型，1-直角，2-圆角
     */
    protected String angleType;
    /**
     * 百度处理最大尺寸
     */
    protected Integer maxBaiduSize = 4000;
    /**
     * 腾讯处理最大尺寸
     */
    protected Integer maxTencentSize = 3000;
    /**
     * 当前项目所在文件路径
     */
    private static String appPath = "";

    public PictureHandle(String picPath, String picName) throws Exception {
        this.picPath = picPath;
        this.picName = picName;

        init();
    }

    /**
     * @param fullPicPath 文件绝对路径包含文件名
     */
    public PictureHandle(String fullPicPath) throws Exception {
        File picFile = new File(fullPicPath);
        this.picPath = picFile.getParent() + File.separator;
        this.picName = picFile.getName();

        init();
    }

    /**
     * 初始化数据
     */
    protected void init() throws Exception {
        int lastDotPos = picName.lastIndexOf(".");
        picExt = lastDotPos > -1 ? picName.substring(lastDotPos + 1).toUpperCase() : "";
        if (!FaStaticParam.PIC_EXT_SET.contains(picExt)) {
            throw new Exception("不支持的图片格式");
        }
        srcMat = OpenCVImageHandle.imread(picPath + picName);
        if (srcMat.empty()) {
            throw new Exception("图片不存在");
        }
    }

    /**
     * 使用二值化方式，验证图片是否空白
     *
     * @return true=空白，false=不空白
     */
    public boolean isPicBlank() {
        // 灰度化
        Mat grayMat = new Mat();
        OpenCVImageHandle.cvtColor(srcMat, grayMat);

        // 二值化
        Mat thresholdMat = new Mat();
        OpenCVImageHandle.threshold(grayMat, thresholdMat, 0, 255, Imgproc.THRESH_OTSU);
        grayMat.release();

        int cols = srcMat.cols();
        int rows = srcMat.rows();
        int blackDotNum = 0; // 黑点数量
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                if (thresholdMat.get(row, col)[0] == 0) {
                    blackDotNum++;
                }
            }
        }
        thresholdMat.release();
        // 如果黑点数量大于5%，认为有内容，否则认为无内容
        if (blackDotNum > cols * rows * 0.01) {
            return false;
        }

        return true;
    }

    /**
     * 对读取出的原图进行尺寸调整，不影响源图片
     *
     * @param width  调整后的宽度
     * @param height 调整后的高度
     */
    public void sourcePicResize(int width, int height) {
        this.width = width;
        this.height = height;
        OpenCVImageHandle.resize(srcMat, srcMat, new Size(width, height), 0, 0, Imgproc.INTER_NEAREST);
    }

    /**
     * 将源图不经过处理直接转化为结果图
     */
    public void srcToResult() {
        resultMat = srcMat;
    }

    /**
     * 图片预处理
     *
     * @param width  预处理图片后的宽度
     * @param height 预处理图片后的高度
     * @return 存在=true，不存在=false
     */
    public abstract boolean pretreatment(int width, int height);

    /**
     * 图片预处理，使用原图尺寸，不改变大小
     *
     * @return 存在=true，不存在=false
     */
    public abstract boolean pretreatment();

    /**
     * 根据起点坐标以及长宽，截取图片中的区域，并保存到磁盘目录中
     *
     * @param x              起点坐标X轴
     * @param y              起点坐标Y轴
     * @param width          截取区域宽
     * @param height         截取区域高
     * @param cutPicSavePath 磁盘目录
     */
    public void cutPic(int x, int y, int width, int height, String cutPicSavePath) {
        Mat cutPicMat = cutRectArea(srcMat, x, y, width, height);

        OpenCVImageHandle.imwrite(cutPicSavePath, cutPicMat);
        cutPicMat.release();
    }

    /**
     * 将预处理的结果保存到磁盘中
     */
    public void saveResultPic(String filePath) {
        if (resultMat != null && !resultMat.empty()) {
            // 模板图片中定位顶点
            OpenCVImageHandle.imwrite(filePath, resultMat);
        }
    }

    /**
     * 获取内部矩形顶点
     *
     * @return 矩形顶点集合（坐上X，坐上Y，右上X，右上Y，左下X，左下Y，右下X，右下Y）
     */
    public Double[] getInnerRectPoint() {
        return getInnerRectPoint(srcMat, height, width, false);
    }

    /**
     * 根据顶点坐标重新定位结果图片的位置
     *
     * @param topLeftX     左上角X坐标
     * @param topLeftY     左上角Y坐标
     * @param topRightX    右上角X坐标
     * @param topRightY    右上角Y坐标
     * @param bottomLeftX  左下角X坐标
     * @param bottomLeftY  左下角Y坐标
     * @param bottomRightX 右下角X坐标
     * @param bottomRightY 右下角Y坐标
     */
    public void resultHandle(double topLeftX, double topLeftY, double topRightX, double topRightY, double bottomLeftX,
                             double bottomLeftY, double bottomRightX, double bottomRightY) {
        Double[] innerRectPoints = this.getInnerRectPoint(resultMat, height, width, true);
        if (innerRectPoints != null) {
            MatOfPoint2f srcP = new MatOfPoint2f(new Point(innerRectPoints[0], innerRectPoints[1]),
                    new Point(innerRectPoints[2], innerRectPoints[3]),
                    new Point(innerRectPoints[4], innerRectPoints[5]),
                    new Point(innerRectPoints[6], innerRectPoints[7]));

            MatOfPoint2f dstP = new MatOfPoint2f(new Point(topLeftX, topLeftY), new Point(topRightX, topRightY),
                    new Point(bottomLeftX, bottomLeftY), new Point(bottomRightX, bottomRightY));

            Mat resultMat1 = new Mat(height, width, resultMat.type(), new Scalar(0, 0, 0));
            Mat transform = OpenCVImageHandle.getPerspectiveTransform(srcP, dstP);
            srcP.release();
            dstP.release();

            OpenCVImageHandle.warpPerspective(resultMat, resultMat1, transform, resultMat1.size());
            transform.release();
            if (resultMat != null && !resultMat.empty()) {
                resultMat.release();
            }
            resultMat = resultMat1.clone();
            resultMat1.release();
        }
    }

    /**
     * 验证源图是否包含特征图片<br/>
     * 特征图片与源图中包含的部分，尺寸要尽量统一，尺寸不统一会非常影响相识度
     *
     * @param traitCutRule 特征块区域
     * @param validFile    特征图片
     * @param similar      相识度，必须>=相识度，才会验证通过，返回true
     * @return 存在=true，不存在=false
     * @throws Exception
     */
    public boolean validPic(PictureCutRuleNew traitCutRule, String validFile, double similar){
        Mat tempMat = OpenCVImageHandle.imread(validFile);
        // 如果模板文件不存在，直接返回false
        if (tempMat == null || tempMat.empty()) {
            return false;
        }

        // 如果结果图片为空，直接返回false
        if (resultMat == null || resultMat.empty()) {
            return false;
        }

        Mat handleMat = resultMat.clone();
        double picSimilar = 0;
        Mat picSimilarMat = new Mat();

        OpenCVImageHandle.cvtColor(tempMat, tempMat);
        OpenCVImageHandle.threshold(tempMat, tempMat, 0, 255, Imgproc.THRESH_OTSU);

        // OpenCVImageHandle.imwrite("d:\\temp\\a_1.jpg", tempMat);
        Mat tMat = OpenCVImageHandle.getMinBlackAreaByThread(tempMat);
        tempMat.release();

        // ImagePHash ipHash = new ImagePHash();
        // String image1 = ipHash.getHash(OpenCVImageHandle.mat2Img(tempMat));
        // OpenCVImageHandle.imwrite("d:\\temp\\a_2.jpg", resultMat);
        for (int i = 0; i < 4; i++) {
            if (i > 0) {
                // 旋转图片，验证
                handleMat = OpenCVImageHandle.picRotate(resultMat, i, width, height);
            }
            // OpenCVImageHandle.imwrite("d:\\temp\\bdc\\bbb_" + i + "_b.jpg", handleMat);
            Mat areaMat = cutRectArea(handleMat, traitCutRule.getStartPosX(), traitCutRule.getStartPosY(),
                    traitCutRule.getWidth(), traitCutRule.getHeight());
            OpenCVImageHandle.cvtColor(areaMat, areaMat);
            OpenCVImageHandle.threshold(areaMat, areaMat, 0, 255, Imgproc.THRESH_OTSU);
            // String image2 = ipHash.getHash(OpenCVImageHandle.mat2Img(areaMat));

            // OpenCVImageHandle.imwrite("d:\\temp\\b_" + i + ".jpg", areaMat);
            Mat aMat = OpenCVImageHandle.getMinBlackAreaByThread(areaMat);
            areaMat.release();

            if (aMat.empty()) {
                continue;
            }
            OpenCVImageHandle.resize(aMat, aMat, tMat.size(), 1, 1, Imgproc.INTER_AREA);
            //OpenCVImageHandle.imwrite("d:\\temp\\b_" + i + "_b.jpg", aMat);

            // double resultVal = ipHash.distance(image1, image2) * 1.0 / image1.length();
            double resultVal = OpenCVImageHandle.threadPicCompare(tMat, aMat);
            aMat.release();
            // 找出最大的相似度
            if (picSimilar == 0 || resultVal > picSimilar) {
                picSimilar = resultVal;
                if (picSimilarMat != null && !picSimilarMat.empty()) {
                    picSimilarMat.release();
                }
                picSimilarMat = handleMat.clone();
            }
            handleMat.release();
        }
        tMat.release();
        if (picSimilar >= similar / 100) {
            resultMat = picSimilarMat.clone();
            picSimilarMat.release();
            return true;
        } else {
            picSimilarMat.release();
        }

        return false;
    }

    /**
     * 根据制定的规则切割图片
     *
     * @param cutRuleList 切割规则
     */
    public void cutPic(List<PictureCutRuleNew> cutRuleList) {
        if (resultMat == null || resultMat.empty()) {
            return;
        }
        if (cutRuleList == null || cutRuleList.size() < 1) {
            return;
        }
        for (PictureCutRuleNew cutRule : cutRuleList) {
            cutRule.setResultMat(cutRectArea(resultMat, cutRule.getStartPosX(), cutRule.getStartPosY(),
                    cutRule.getWidth(), cutRule.getHeight()));
        }
    }

    /**
     * 释放规则中提取的Mat对象
     *
     * @param cutRuleList 切割规则
     */
    public void releaseRuleMat(List<PictureCutRuleNew> cutRuleList) {
        if (cutRuleList == null || cutRuleList.size() < 1) {
            return;
        }
        for (PictureCutRuleNew cutRule : cutRuleList) {
            if (cutRule.getResultMat() != null && !cutRule.getResultMat().empty()) {
                cutRule.getResultMat().release();
            }
        }
    }

    /**
     * 释放Mat对象
     */
    public void releaseMat() {
        if (srcMat != null && !srcMat.empty()) {
            srcMat.release();
        }
        if (resultMat != null && !resultMat.empty()) {
            resultMat.release();
        }
    }

    /**
     * 拼接图片<br/>
     * 拼接之前先按照规则切割图片
     *
     * @param cutRuleList  需要拼接的图片列表
     * @param identifyType 识别类型，1-百度，2-腾讯，默认为百度
     * @return 拼接图片的路径
     */
    public List<String> joinCutPic(List<PictureCutRuleNew> cutRuleList, int identifyType) {
        if (cutRuleList == null || cutRuleList.size() < 1) {
            return null;
        }
        int maxWidth = 0;

        String joinSeqPic = FaStaticParam.PROJECT_PATH+"\\modelTemples\\model" + File.separator + "pic" + File.separator + JOIN_SEQ_PIC_NAME;
        Mat joinSeq = OpenCVImageHandle.imread(joinSeqPic);

        String joinMarqueeSeqPic = FaStaticParam.PROJECT_PATH+"\\modelTemples\\model" + File.separator + "pic" + File.separator + JOIN_MARQUEE_SEQ_PIC_NAME;
        Mat joinMarqueeSeq = OpenCVImageHandle.imread(joinMarqueeSeqPic);

        int joinSeqHeight = joinSeq.height();

        int maxHeight = identifyType == 2 ? maxTencentSize : maxBaiduSize;
        // 图片拼接的最大高度
        List<Integer> picMaxHeightList = new ArrayList<Integer>();
        // 最大高度分割时的索引
        List<Integer> picMaxHeightSizeList = new ArrayList<Integer>();
        // 设置最大的宽度
        int joinHeight = 0;
        boolean joinFlag = true;
        for (int i = 0; i < cutRuleList.size(); i++) {
            PictureCutRuleNew cutRule = cutRuleList.get(i);
            // 如果识别类型为图像，不需要拼接到识别图片中
            if (FaStaticParam.RECOGNITION_TYPE_TX.equals(cutRule.getType())) {
                if (joinHeight + joinSeqHeight > maxHeight) {
                    picMaxHeightList.add(joinHeight);
                    picMaxHeightSizeList.add(i);
                    joinHeight = 0;
                    joinFlag = false;
                } else {
                    joinFlag = true;
                }
                joinHeight += joinSeqHeight;
            } else {
                if (joinHeight + joinSeqHeight + cutRule.getHeight() > maxHeight) {
                    picMaxHeightList.add(joinHeight);
                    picMaxHeightSizeList.add(i);
                    joinHeight = 0;
                    joinFlag = false;
                } else {
                    joinFlag = true;
                }
                joinHeight += cutRule.getHeight() + joinSeqHeight;
                if (cutRule.getWidth() > maxWidth) {
                    maxWidth = cutRule.getWidth();
                }
            }
        }
        if (joinFlag) {
            picMaxHeightList.add(joinHeight);
            picMaxHeightSizeList.add(cutRuleList.size());
        }

        maxWidth = maxWidth < joinSeq.width() ? joinSeq.width() : maxWidth;

        List<Mat> desMatList = new ArrayList<Mat>();
        int index = 0;
        for (int s = 0; s < picMaxHeightList.size(); s++) {
            int picMaxHeight = picMaxHeightList.get(s);
            int maxIndex = picMaxHeightSizeList.get(s);
            Mat desMat = new Mat(picMaxHeight, maxWidth, srcMat.type(), OpenCVImageHandle.WHITE_COLOR);

            int y = 0;
            for (int i = index; i < maxIndex; i++) {
                PictureCutRuleNew cutRule = cutRuleList.get(i);
                // 如果识别类型为图像，不需要拼接到识别图片中
                if (!FaStaticParam.RECOGNITION_TYPE_TX.equals(cutRule.getType())) {
                    Mat tMat = new Mat(desMat, new Rect(0, y, joinSeq.width(), joinSeq.height()));
                    joinSeq.copyTo(tMat);
                    tMat.release();
                    y += joinSeq.height();

                    Mat ruleMat = new Mat(desMat, new Rect(0, y, cutRule.getWidth(), cutRule.getHeight()));
                    cutRule.getResultMat().copyTo(ruleMat);
                    ruleMat.release();

                    y += cutRule.getHeight();
                } else {
                    Mat tMat = new Mat(desMat, new Rect(0, y, joinMarqueeSeq.width(), joinMarqueeSeq.height()));
                    joinMarqueeSeq.copyTo(tMat);
                    tMat.release();

                    y += joinSeq.height();
                }
                index = i + 1;
            }
            desMatList.add(desMat);
        }

        joinSeq.release();
        joinMarqueeSeq.release();

        List<String> picPathList = new ArrayList<String>();
        String pn = picName.substring(0, picExt.length()) + "_";
        for (Mat desMat : desMatList) {

            String fileName = pn + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + "_" + StringUtil.random(6) + "." + picExt;
            String perPath = FaStaticParam.PROJECT_PATH+"\\modelTemples\\model" + File.separator;
            String tempPath = "pic" + File.separator + "temp" + File.separator + "cut" + File.separator;
            File tempFile = new File(perPath + tempPath);
            // 如果文件夹不存在，自动创建
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            OpenCVImageHandle.cvtColor(desMat, desMat);

            // 将切割图片保存到文件目录中
            OpenCVImageHandle.imwrite(perPath + tempPath + fileName, desMat);
            desMat.release();
            picPathList.add(tempPath + fileName);
        }
        return picPathList;
    }

    /**
     * 根据起点坐标（x,y）以及宽度和高度截取区域
     *
     * @param resultMat 源图像
     * @param startPosX 起点坐标X轴
     * @param startPosY 起点坐标Y轴
     * @param width     宽度
     * @param height    高度
     * @return 截取区域对象
     */
    protected Mat cutRectArea(Mat resultMat, int startPosX, int startPosY, int width, int height) {
        Rect cutRect = new Rect(startPosX, startPosY, width, height);
        return new Mat(resultMat, cutRect);
    }

    /**
     * 获取内部矩形顶点
     *
     * @param sMat         源Mat对象
     * @param sHeight      源图片的高度
     * @param sWidth       源图片的宽度
     * @param validConArea 是否验证轮廓面积，true=验证，false=不验证
     * @return 矩形顶点集合（坐上X，坐上Y，右上X，右上Y，左下X，左下Y，右下X，右下Y）
     */
    private Double[] getInnerRectPoint(Mat sMat, int sHeight, int sWidth, boolean validConArea) {
        double srcArea = height * width;
        // 最小的轮廓面积
        double minConArea = srcArea * INNER_RECT_MIN_AREA;
        double maxConArea = srcArea * INNER_RECT_MAX_AREA;

        // 灰度化
        Mat grayMat = new Mat();
        OpenCVImageHandle.cvtColor(sMat, grayMat);

        // 二值化
        Mat thresholdMat = new Mat();
        OpenCVImageHandle.threshold(grayMat, thresholdMat, 0, 255, Imgproc.THRESH_OTSU);
        grayMat.release();

        // 查找轮廓
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        MatOfInt4 hierarchy = new MatOfInt4();
        OpenCVImageHandle.findContours(thresholdMat, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

        int[] hierarchys = hierarchy.toArray();
        hierarchy.release();
        thresholdMat.release();

        // 查找最大的轮廓，面积大于图片面积的95%
        int cid = -1;
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
            point2f.release();
            if (contourRectArea > maxConArea) {
                cid = contourIdx;
                break;
            }
        }

        Double[] innerRectPoints = null;
        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            int beginPos = contourIdx * 4;
            if (hierarchys[beginPos + 3] == cid) {

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
                    if (validConArea) {
                        // 轮廓面积必须大于最小矩形的90%，才认为有效
                        double contourArea = OpenCVImageHandle.contourArea(matOfPoint);
                        if (contourArea < contourRectArea * 0.9) {
                            continue;
                        }
                    }

                    // 如果角度为直角，采用多边形拟合查找顶点
                    MatOfPoint2f approxCurve = new MatOfPoint2f();

                    double approxDistance = OpenCVImageHandle.arcLength(point2f, true) * 0.03;
                    OpenCVImageHandle.approxPolyDP(point2f, approxCurve, approxDistance, true);
                    Point[] ps = approxCurve.toArray();
                    approxCurve.release();
                    point2f.release();
                    matOfPoint.release();

                    Point[] rectPoints = null;
                    if (ps.length != 4) {
                        // 如果多边形拟合得到的顶点不是4个，继续使用最小矩形查找顶点
                        ps = new Point[4];
                        // 获取矩形的四个顶点
                        minRect.points(ps);
                    }
                    //验证获取到的点必须在图像中
                    for (int i = 0; i < ps.length; i++) {
                        Point pi = ps[i];
                        if (pi.x <= 0 || pi.x >= width || pi.y <= 0 || pi.y >= height) {
                            return innerRectPoints;
                        }
                    }

                    rectPoints = OpenCVImageHandle.pointSort(ps);

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

                    innerRectPoints = new Double[8];
                    // 根据左上角的点，设置仿射变换的四个顶点
                    switch (leftTopPos) {
                        case 1:
                            innerRectPoints = new Double[]{rectPoints[1].x, rectPoints[1].y, rectPoints[0].x,
                                    rectPoints[0].y, rectPoints[2].x, rectPoints[2].y, rectPoints[3].x, rectPoints[3].y};
                            break;
                        case 2:
                            innerRectPoints = new Double[]{rectPoints[2].x, rectPoints[2].y, rectPoints[1].x,
                                    rectPoints[1].y, rectPoints[3].x, rectPoints[3].y, rectPoints[0].x, rectPoints[0].y};
                            break;
                        case 3:
                            innerRectPoints = new Double[]{rectPoints[3].x, rectPoints[3].y, rectPoints[2].x,
                                    rectPoints[2].y, rectPoints[0].x, rectPoints[0].y, rectPoints[1].x, rectPoints[1].y};
                            break;
                        default:
                            innerRectPoints = new Double[]{rectPoints[0].x, rectPoints[0].y, rectPoints[3].x,
                                    rectPoints[3].y, rectPoints[1].x, rectPoints[1].y, rectPoints[2].x, rectPoints[2].y};
                            break;
                    }
                    break;
                } else {
                    point2f.release();
                    matOfPoint.release();
                }
            }
        }
        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            contours.get(contourIdx).release();
        }

        return innerRectPoints;
    }


    /**
     * @return 图片路径
     */
    public String getPicPath() {
        return picPath;
    }

    /**
     * @param picPath 图片路径
     */
    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    /**
     * @return 图片名称
     */
    public String getPicName() {
        return picName;
    }

    /**
     * @param picName 图片名称
     */
    public void setPicName(String picName) {
        this.picName = picName;
    }

    /**
     * @return 图片角的类型，1-直角，2-圆角
     */
    public String getAngleType() {
        return angleType;
    }

    /**
     * @param angleType 图片角的类型，1-直角，2-圆角
     */
    public void setAngleType(String angleType) {
        this.angleType = angleType;
    }

}
