package com.zfsoft.superwindow.util.fa;

import com.zfsoft.superwindow.util.CvUtil;
import org.apache.commons.lang.ArrayUtils;
import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;

/**
 * @description:  图片处理，抽象类
 * @author: liangss
 * @date: 2020-12-05 16:45:29
 */
public  class OpenCVImageHandle {


    /** 红色 */
    public static final Scalar RED_COLOR = new Scalar(0, 0, 255);
    /** 绿色 */
    public static final Scalar GREEN_COLOR = new Scalar(0, 255, 0);
    /** 蓝色 */
    public static final Scalar BLUE_COLOR = new Scalar(255, 0, 0);
    /** 白色 */
    public static final Scalar BLACK_COLOR = new Scalar(0, 0, 0);
    /** 黑色 */
    public static final Scalar WHITE_COLOR = new Scalar(255, 255, 255);

    /**
     * 读取文件到Mat对象中
     *
     * @param fileName
     *            文件全路径
     */
    public static Mat imread(String fileName) {
        return Imgcodecs.imread(fileName);
    }

    /**
     * 将Mat保存到文件中
     *
     * @param fileName
     *            文件全路径
     * @param img
     *            Mat对象
     */
    public static void imwrite(String fileName, Mat img) {
        Imgcodecs.imwrite(fileName, img);
    }

    /**
     * 均值滤波<br/>
     * 典型的线性滤波算法，它是指在图像上对目标像素给一个模板，该模板包括了其周围的临近像素（以目标像素为中心的周围8个像素，构成一个滤波模板，
     * 即去掉目标像素本身），再用模板中的全体像素的平均值来代替原来像素值。<br/>
     * 均值滤波本身存在着固有的缺陷，即它不能很好地保护图像细节，在图像去噪的同时也破坏了图像的细节部分，从而使图像变得模糊，不能很好地去除噪声点。
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param ksize
     *            均值滤波器模板大小
     * @param anchor
     *            锚点，如果为Point(-1,-1)，则锚点是滤波器的中心点
     */
    public static void blur(Mat srcMat, Mat dstMat, Size ksize, Point anchor) {
        Imgproc.blur(srcMat, dstMat, ksize, anchor);
    }

    /**
     * 均值滤波<br/>
     * 典型的线性滤波算法，它是指在图像上对目标像素给一个模板，该模板包括了其周围的临近像素（以目标像素为中心的周围8个像素，构成一个滤波模板，
     * 即去掉目标像素本身），再用模板中的全体像素的平均值来代替原来像素值。<br/>
     * 均值滤波本身存在着固有的缺陷，即它不能很好地保护图像细节，在图像去噪的同时也破坏了图像的细节部分，从而使图像变得模糊，不能很好地去除噪声点。
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param ksize
     *            均值滤波器模板大小
     */
    public static void blur(Mat srcMat, Mat dstMat, Size ksize) {
        Imgproc.blur(srcMat, dstMat, ksize);
    }

    /**
     * 均值滤波<br/>
     * 典型的线性滤波算法，它是指在图像上对目标像素给一个模板，该模板包括了其周围的临近像素（以目标像素为中心的周围8个像素，构成一个滤波模板，
     * 即去掉目标像素本身），再用模板中的全体像素的平均值来代替原来像素值。<br/>
     * 均值滤波本身存在着固有的缺陷，即它不能很好地保护图像细节，在图像去噪的同时也破坏了图像的细节部分，从而使图像变得模糊，不能很好地去除噪声点。
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param ksize
     *            均值滤波器模板大小
     * @param anchor
     *            锚点，如果为Point(-1,-1)，则锚点是滤波器的中心点
     * @param borderType
     *            边缘点插值类型
     */
    public static void blur(Mat srcMat, Mat dstMat, Size ksize, Point anchor, int borderType) {
        Imgproc.blur(srcMat, dstMat, ksize, anchor, borderType);
    }

    /**
     * 高斯滤波<br/>
     * 一种线性平滑滤波，适用于消除高斯噪声，广泛应用于图像处理的减噪过程。通俗的讲，高斯滤波就是对整幅图像进行加权平均的过程，每一个像素点的值，
     * 都由其本身和邻域内的其他像素值经过加权平均后得到。高斯滤波的具体操作是：用一个模板（或称卷积、掩模）扫描图像中的每一个像素，
     * 用模板确定的邻域内像素的加权平均灰度值去替代模板中心像素点的值。
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param ksize
     *            高斯滤波器模板大小
     * @param sigmaX
     *            高斯滤波在横线的滤波系数
     */
    public static void gaussianBlur(Mat srcMat, Mat dstMat, Size ksize, double sigmaX) {
        Imgproc.GaussianBlur(srcMat, dstMat, ksize, sigmaX);
    }

    /**
     * 高斯滤波<br/>
     * 一种线性平滑滤波，适用于消除高斯噪声，广泛应用于图像处理的减噪过程。通俗的讲，高斯滤波就是对整幅图像进行加权平均的过程，每一个像素点的值，
     * 都由其本身和邻域内的其他像素值经过加权平均后得到。高斯滤波的具体操作是：用一个模板（或称卷积、掩模）扫描图像中的每一个像素，
     * 用模板确定的邻域内像素的加权平均灰度值去替代模板中心像素点的值。
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param ksize
     *            高斯滤波器模板大小
     * @param sigmaX
     *            高斯滤波在横线的滤波系数
     * @param sigmaY
     *            高斯滤波在竖向的滤波系数
     */
    public static void gaussianBlur(Mat srcMat, Mat dstMat, Size ksize, double sigmaX, double sigmaY) {
        Imgproc.GaussianBlur(srcMat, dstMat, ksize, sigmaX, sigmaY);
    }

    /**
     * 高斯滤波<br/>
     * 一种线性平滑滤波，适用于消除高斯噪声，广泛应用于图像处理的减噪过程。通俗的讲，高斯滤波就是对整幅图像进行加权平均的过程，每一个像素点的值，
     * 都由其本身和邻域内的其他像素值经过加权平均后得到。高斯滤波的具体操作是：用一个模板（或称卷积、掩模）扫描图像中的每一个像素，
     * 用模板确定的邻域内像素的加权平均灰度值去替代模板中心像素点的值。
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param ksize
     *            高斯滤波器模板大小
     * @param sigmaX
     *            高斯滤波在横线的滤波系数
     * @param sigmaY
     *            高斯滤波在竖向的滤波系数
     * @param borderType
     *            边缘点插值类型
     */
    public static void gaussianBlur(Mat srcMat, Mat dstMat, Size ksize, double sigmaX, double sigmaY, int borderType) {
        Imgproc.GaussianBlur(srcMat, dstMat, ksize, sigmaX, sigmaY, borderType);
    }

    /**
     * 中值滤波<br/>
     * 一种非线性平滑技术，它将每一像素点的灰度值设置为该点某邻域窗口内的所有像素点灰度值的中值。<br/>
     * 中值滤波法对消除椒盐噪声非常有效，在光学测量条纹图象的相位分析处理方法中有特殊作用，但在条纹中心分析方法中作用不大。<br/>
     * 中值滤波在图像处理中, 常用于保护边缘信息,是经典的平滑噪声的方法。
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param ksize
     *            均值滤波器模板大小
     */
    public static void medianBlur(Mat srcMat, Mat dstMat, int ksize) {
        Imgproc.medianBlur(srcMat, dstMat, ksize);
    }

    /**
     * 双边滤波<br/>
     * 一种非线性的滤波方法，是结合图像的空间邻近度和像素值相似度的一种折衷处理，同时考虑空间与信息和灰度相似性，达到保边去噪的目的，具有简单、非迭代、
     * 局部处理的特点。
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param d
     *            在过滤过程中每个像素邻域的直径范围。如果这个值是非正数，则函数会从第五个参数sigmaSpace计算该值
     * @param sigmaColor
     *            颜色空间过滤器的sigma值，这个参数的值越大，表明该像素邻域内有越宽广的颜色会被混合到一起，产生较大的半相等颜色区域。
     * @param sigmaSpace
     *            坐标空间中滤波器的sigma值，如果该值较大，则意味着颜色相近的较远的像素将相互影响，
     *            从而使更大的区域中足够相似的颜色获取相同的颜色。当d>0时，d指定了邻域大小且与sigmaSpace五官，
     *            否则d正比于sigmaSpace
     */
    public static void bilateralFilter(Mat srcMat, Mat dstMat, int d, double sigmaColor, double sigmaSpace) {
        Imgproc.bilateralFilter(srcMat, dstMat, d, sigmaColor, sigmaSpace);
    }

    /**
     * 双边滤波<br/>
     * 一种非线性的滤波方法，是结合图像的空间邻近度和像素值相似度的一种折衷处理，同时考虑空间与信息和灰度相似性，达到保边去噪的目的，具有简单、非迭代、
     * 局部处理的特点。
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param d
     *            在过滤过程中每个像素邻域的直径范围。如果这个值是非正数，则函数会从第五个参数sigmaSpace计算该值
     * @param sigmaColor
     *            颜色空间过滤器的sigma值，这个参数的值越大，表明该像素邻域内有越宽广的颜色会被混合到一起，产生较大的半相等颜色区域。
     * @param sigmaSpace
     *            坐标空间中滤波器的sigma值，如果该值较大，则意味着颜色相近的较远的像素将相互影响，
     *            从而使更大的区域中足够相似的颜色获取相同的颜色。当d>0时，d指定了邻域大小且与sigmaSpace五官，
     *            否则d正比于sigmaSpace
     * @param borderType
     *            边缘点插值类型
     */
    public static void bilateralFilter(Mat srcMat, Mat dstMat, int d, double sigmaColor, double sigmaSpace,
                                       int borderType) {
        Imgproc.bilateralFilter(srcMat, dstMat, d, sigmaColor, sigmaSpace, borderType);
    }

    /**
     * 灰度化<br/>
     * 在RGB模型中，如果R=G=B时，则彩色表示一种灰度颜色，其中R=G=B的值叫灰度值，因此，灰度图像每个像素只需一个字节存放灰度值（又称强度值、
     * 亮度值），灰度范围为0-255。
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     */
    public static void cvtColor(Mat srcMat, Mat dstMat) {
        Imgproc.cvtColor(srcMat, dstMat, Imgproc.COLOR_RGB2GRAY);
    }

    /**
     * 灰度化
     *
     * @param srcMat
     *            输入图像
     * @param
     *
     * @param type
     *            1-最大值法，2-平均值法，3-加权平均值法
     */
    public static Mat cvtColor(Mat srcMat, int type) {
        if (srcMat.empty()) {
            return null;
        }
        int rows = srcMat.rows();
        int cols = srcMat.cols();
        Mat dstMat = new Mat(rows, cols, srcMat.channels());
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double[] colors = srcMat.get(row, col);
                double color = 0;
                if (type == 1) {
                    color = Collections.min((Collection<? extends Double>) Arrays.asList(ArrayUtils.toObject(colors)));
                 /*   Collections.min()*/
                } else if (type == 3) {
                    color = colors[0] * 0.114 + colors[1] * 0.587 + colors[2] * 0.299;
                } else {
                    color = (colors[0] + colors[1] + colors[2]) / 3;
                }
                dstMat.put(row, col, color);
            }
        }
        return dstMat;
    }

    /**
     * Laplacian算子
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param ddepth
     *            目标图像的深度
     */
    public static void laplacian(Mat srcMat, Mat dstMat, int ddepth) {
        Mat lapMat = new Mat();
        Imgproc.Laplacian(srcMat, lapMat, ddepth, 3, 1, 0, Core.BORDER_DEFAULT);

        Core.convertScaleAbs(lapMat, dstMat);
    }

    /**
     * Laplacian算子
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param ddepth
     *            目标图像的深度
     * @param ksize
     *            用于计算二阶导数的滤波器的孔径尺寸，大小必须为正奇数，且有默认值1
     * @param scale
     *            计算拉普拉斯值的时候可选的比例因子，有默认值1
     * @param delta
     *            表示在结果存入目标图（第二个参数dst）之前可选的delta值，有默认值0
     */
    public static void laplacian(Mat srcMat, Mat dstMat, int ddepth, int ksize, double scale, double delta) {
        Mat lapMat = new Mat();
        Imgproc.Laplacian(srcMat, lapMat, ddepth, ksize, scale, delta, Core.BORDER_DEFAULT);

        Core.convertScaleAbs(lapMat, dstMat);
    }

    /**
     * Laplacian算子
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param ddepth
     *            目标图像的深度
     * @param ksize
     *            用于计算二阶导数的滤波器的孔径尺寸，大小必须为正奇数，且有默认值1
     * @param scale
     *            计算拉普拉斯值的时候可选的比例因子，有默认值1
     * @param delta
     *            表示在结果存入目标图（第二个参数dst）之前可选的delta值，有默认值0
     * @param borderType
     *            边界模式，默认值为BORDER_DEFAULT
     */
    public static void laplacian(Mat srcMat, Mat dstMat, int ddepth, int ksize, double scale, double delta,
                                 int borderType) {
        Mat lapMat = new Mat();
        Imgproc.Laplacian(srcMat, lapMat, ddepth, ksize, scale, delta, borderType);

        Core.convertScaleAbs(lapMat, dstMat);
    }

    /**
     * canny算子
     *
     * @param srcMat
     *            输入图像，即源图像，填Mat类的对象即可，且需为单通道8位图像
     * @param dstMat
     *            输出图像，需要和源图片有一样的尺寸和类型
     * @param threshold1
     *            第一个滞后性阈值
     * @param threshold2
     *            第二个滞后性阈值
     *            函数阈值1和阈值2两者的小者用于边缘连接，而大者用来控制强边缘的初始段，推荐的高低阈值比在2:1到3:1之间
     */
    public static void canny(Mat srcMat, Mat dstMat, double threshold1, double threshold2) {
        Imgproc.Canny(srcMat, dstMat, threshold1, threshold2);
    }

    /**
     * canny算子
     *
     * @param srcMat
     *            输入图像，即源图像，填Mat类的对象即可，且需为单通道8位图像
     * @param dstMat
     *            输出图像，需要和源图片有一样的尺寸和类型
     * @param threshold1
     *            第一个滞后性阈值
     * @param threshold2
     *            第二个滞后性阈值<br/>
     *            阈值1和阈值2两者的小者用于边缘连接，而大者用来控制强边缘的初始段，推荐的高低阈值比在2:1到3:1之间
     * @param apertureSize
     *            应用Sobel算子的孔径大小，其有默认值3
     * @param l2gradient
     *            一个计算图像梯度幅值的标识，有默认值false
     */
    public static void canny(Mat srcMat, Mat dstMat, double threshold1, double threshold2, int apertureSize,
                             boolean l2gradient) {
        Imgproc.Canny(srcMat, dstMat, threshold1, threshold2, apertureSize, l2gradient);
    }

    /**
     * sobel算子
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param ddepth
     *            输出图像的深度
     * @param ksize
     *            核的大小；必须取1，3，5或7，默认值3
     * @param scale
     *            计算导数值时可选的缩放因子，默认值是1，是没有应用缩放的
     * @param delta
     *            表示在结果存入目标图（第二个参数dst）之前可选的delta值，有默认值0
     */
    public static void sobel(Mat srcMat, Mat dstMat, int ddepth, int ksize, double scale, double delta) {
        Mat gradX = new Mat();
        Mat gradY = new Mat();
        Mat absGradX = new Mat();
        Mat absGradY = new Mat();

        // X 方向上的差分阶数
        Imgproc.Sobel(srcMat, gradX, ddepth, 1, 0, ksize, scale, delta, Core.BORDER_DEFAULT);
        Core.convertScaleAbs(gradX, absGradX);

        // Y 方向上的差分阶数
        Imgproc.Sobel(srcMat, gradY, ddepth, 0, 1, ksize, scale, delta, Core.BORDER_DEFAULT);
        Core.convertScaleAbs(gradY, absGradY);

        // 实现图片合成
        Core.addWeighted(absGradX, 0.5, absGradY, 0.5, 0.0, dstMat);
    }

    /**
     * 二值化
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param thresh
     *            阈值的具体值
     * @param maxval
     *            当第五个参数阈值类型type取THRESH_BINARY或THRESH_BINARY_INV阈值类型时的最大值
     * @param type
     *            阈值类型。 <br/>
     *            其它参数很好理解，我们来看看第五个参数，第五参数有以下几种类型 <br/>
     *            0: Imgproc.THRESH_BINARY
     *            当前点值大于阈值时，取Maxval,也就是第四个参数，下面再不说明，否则设置为0<br/>
     *            1: Imgproc.THRESH_BINARY_INV 当前点值大于阈值时，设置为0，否则设置为Maxval <br/>
     *            2: Imgproc.THRESH_TRUNC 当前点值大于阈值时，设置为阈值，否则不改变 <br/>
     *            3: Imgproc.THRESH_TOZERO 当前点值大于阈值时，不改变，否则设置为0 <br/>
     *            4: Imgproc.THRESH_TOZERO_INV 当前点值大于阈值时，设置为0，否则不改变
     */
    public static void threshold(Mat srcMat, Mat dstMat, double thresh, double maxval, int type) {
        Imgproc.threshold(srcMat, dstMat, thresh, maxval, type);
    }

    /**
     * 调整图像的亮度和对比度
     *
     * @param srcMat
     *            输入图像
     * @param contrastValue
     *            对比度
     * @param brightValue
     *            亮度
     */
    public static Mat contrastOrBrightAdjust(Mat srcMat, int contrastValue, int brightValue) {
        if(srcMat == null || srcMat.empty()) {
            return null;
        }
        int rows = srcMat.rows(), cols = srcMat.cols();
        Mat dstMat = new Mat(rows, cols, srcMat.type());
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double[] colors = srcMat.get(row, col);
                double[] cs = new double[3];
                for (int c = 0; c < 3; c++) {
                    cs[c] = (contrastValue * 0.01) * (colors[c] + brightValue);
                    cs[c] = cs[c] < 0 ? 0 : cs[c];
                    cs[c] = cs[c] > 255 ? 255 : cs[c];
                }
                dstMat.put(row, col, cs);
            }
        }
        return dstMat;
    }

    /**
     * 返回指定形状和尺寸的结构元素
     *
     * @param shape
     *            内核的形状，有三种形状可以选择<br/>
     *            矩形：MORPH_RECT;<br/>
     *            交叉形：MORPH_CORSS;<br/>
     *            椭圆形：MORPH_ELLIPSE;
     * @param ksize
     *            内核的尺寸
     * @return 结构元素
     */
    public static Mat getStructuringElement(int shape, Size ksize) {
        return Imgproc.getStructuringElement(shape, ksize);
    }

    /**
     * 返回指定形状和尺寸的结构元素
     *
     * @param shape
     *            内核的形状，有三种形状可以选择<br/>
     *            矩形：MORPH_RECT;<br/>
     *            交叉形：MORPH_CORSS;<br/>
     *            椭圆形：MORPH_ELLIPSE;
     * @param ksize 内核的尺寸
     * @paramanchor锚点的位置
     * @return 结构元素
     */
    public static Mat getStructuringElement(int shape, Size ksize, Point anchor) {
        return Imgproc.getStructuringElement(shape, ksize, anchor);
    }

    /**
     * 形态学操作，开运算、闭运算、形态梯形、顶帽、黑帽
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param op
     *            形态学运算的类型，可以是如下之一的标识符： <br/>
     *            Imgproc.MORPH_OPEN – 开运算（Opening operation）<br/>
     *            Imgproc.MORPH_CLOSE – 闭运算（Closing operation） <br/>
     *            Imgproc.MORPH_GRADIENT -形态学梯度（Morphological gradient） <br/>
     *            Imgproc.MORPH_TOPHAT - “顶帽”（“Top hat”）<br/>
     *            Imgproc.MORPH_BLACKHAT - “黑帽”（“Black hat“）
     * @param kernel
     *            形态学运算的内核。若为NULL时，表示的是使用参考点位于中心3x3的核。我们一般使用函数
     *            Imgproc.getStructuringElement配合这个参数的使用。
     *            Imgproc.getStructuringElement函数会返回指定形状和尺寸的结构元素（内核矩阵）。
     */
    public static void morphologyEx(Mat srcMat, Mat dstMat, int op, Mat kernel) {
        Imgproc.morphologyEx(srcMat, dstMat, op, kernel);
    }

    /**
     * 形态学操作，开运算、闭运算、形态梯形、顶帽、黑帽
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param op
     *            形态学运算的类型，可以是如下之一的标识符： <br/>
     *            Imgproc.MORPH_OPEN – 开运算（Opening operation）<br/>
     *            Imgproc.MORPH_CLOSE – 闭运算（Closing operation） <br/>
     *            Imgproc.MORPH_GRADIENT -形态学梯度（Morphological gradient） <br/>
     *            Imgproc.MORPH_TOPHAT - “顶帽”（“Top hat”）<br/>
     *            Imgproc.MORPH_BLACKHAT - “黑帽”（“Black hat“）
     * @param kernel
     *            形态学运算的内核。若为NULL时，表示的是使用参考点位于中心3x3的核。我们一般使用函数
     *            Imgproc.getStructuringElement配合这个参数的使用。
     *            Imgproc.getStructuringElement函数会返回指定形状和尺寸的结构元素（内核矩阵）。
     * @param anchor
     *            锚的位置，其有默认值（-1，-1），表示锚位于中心
     * @param iterations
     *            迭代使用函数的次数，默认值为1
     */
    public static void morphologyEx(Mat srcMat, Mat dstMat, int op, Mat kernel, Point anchor, int iterations) {
        Imgproc.morphologyEx(srcMat, dstMat, op, kernel, anchor, iterations);
    }

    /**
     * 检测轮廓
     *
     * @paramimage
     *            单通道图像矩阵<br/>
     *            可以是灰度图，但更常用的是二值图像，一般是经过Canny、拉普拉斯等边缘检测算子处理过的二值图像
     * @param contours
     *            轮廓集合<br/>
     *            集合内每个元素保存了一组由连续的Point点构成的点的集合，每一组Point点集就是一个轮廓
     * @param hierarchy
     *            轮廓关系集合<br/>
     *            集合hiararchy内的元素和轮廓集合contours内的元素是一一对应的，集合的容量相同。
     *            hierarchy向量内每一个元素的4个int型变量——hierarchy[i][0]~hierarchy[i][3]，
     *            <br/>
     *            分别表示第i个轮廓的后一个轮廓、前一个轮廓、父轮廓、内嵌轮廓的索引编号。<br/>
     *            如果当前轮廓没有对应的后一个轮廓、前一个轮廓、父轮廓或内嵌轮廓的话，则hierarchy[i][0]
     *            ~hierarchy[i][3]的相应位被设置为默认值-1
     * @param mode
     *            轮廓的检索模式<br/>
     *            Imgproc.RETR_EXTERNAL：只检测最外围轮廓，包含在外围轮廓内的内围轮廓被忽略<br/>
     *            Imgproc.RETR_LIST：检测所有的轮廓，包括内围、外围轮廓，但是检测到的轮廓不建立等级关系，彼此之间独立，
     *            没有等级关系， 这就意味着这个检索模式下不存在父轮廓或内嵌轮廓，
     *            所以hierarchy向量内所有元素的第3、第4个分量都会被置为-1 <br/>
     *            Imgproc.RETR_CCOMP：检测所有的轮廓，但所有轮廓只建立两个等级关系，外围为顶层，
     *            若外围内的内围轮廓还包含了其他的轮廓信息， 则内围内的所有轮廓均归属于顶层<br/>
     *            Imgproc.RETR_TREE：检测所有轮廓，所有轮廓建立一个等级树结构。外层轮廓包含内层轮廓，
     *            内层轮廓还可以继续包含内嵌轮廓。
     * @param method
     *            轮廓的近似方法<br/>
     *            Imgproc.CHAIN_APPROX_NONE：保存物体边界上所有连续的轮廓点到contours向量内<br/>
     *            Imgproc.CHAIN_APPROX_SIMPLE：
     *            仅保存轮廓的拐点信息，把所有轮廓拐点处的点保存入contours向量内，拐点与拐点之间直线段上的信息点不予保留
     */
    public static void findContours(Mat srcMat, List<MatOfPoint> contours, Mat hierarchy, int mode, int method) {
        Imgproc.findContours(srcMat, contours, hierarchy, mode, method);
    }

    /**
     * 检测轮廓
     *
     * @paramimage
     *            单通道图像矩阵<br/>
     *            可以是灰度图，但更常用的是二值图像，一般是经过Canny、拉普拉斯等边缘检测算子处理过的二值图像
     * @param contours
     *            轮廓集合<br/>
     *            集合内每个元素保存了一组由连续的Point点构成的点的集合，每一组Point点集就是一个轮廓
     * @param hierarchy
     *            轮廓关系集合<br/>
     *            集合hiararchy内的元素和轮廓集合contours内的元素是一一对应的，集合的容量相同。
     *            hierarchy向量内每一个元素的4个int型变量——hierarchy[i][0]~hierarchy[i][3]，
     *            <br/>
     *            分别表示第i个轮廓的后一个轮廓、前一个轮廓、父轮廓、内嵌轮廓的索引编号。<br/>
     *            如果当前轮廓没有对应的后一个轮廓、前一个轮廓、父轮廓或内嵌轮廓的话，则hierarchy[i][0]
     *            ~hierarchy[i][3]的相应位被设置为默认值-1
     * @param mode
     *            轮廓的检索模式<br/>
     *            Imgproc.RETR_EXTERNAL：只检测最外围轮廓，包含在外围轮廓内的内围轮廓被忽略<br/>
     *            Imgproc.RETR_LIST：检测所有的轮廓，包括内围、外围轮廓，但是检测到的轮廓不建立等级关系，彼此之间独立，
     *            没有等级关系， 这就意味着这个检索模式下不存在父轮廓或内嵌轮廓，
     *            所以hierarchy向量内所有元素的第3、第4个分量都会被置为-1 <br/>
     *            Imgproc.RETR_CCOMP：检测所有的轮廓，但所有轮廓只建立两个等级关系，外围为顶层，
     *            若外围内的内围轮廓还包含了其他的轮廓信息， 则内围内的所有轮廓均归属于顶层<br/>
     *            Imgproc.RETR_TREE：检测所有轮廓，所有轮廓建立一个等级树结构。外层轮廓包含内层轮廓，
     *            内层轮廓还可以继续包含内嵌轮廓。
     * @param method
     *            轮廓的近似方法<br/>
     *            Imgproc.CHAIN_APPROX_NONE：保存物体边界上所有连续的轮廓点到contours向量内<br/>
     *            Imgproc.CHAIN_APPROX_SIMPLE：
     *            仅保存轮廓的拐点信息，把所有轮廓拐点处的点保存入contours向量内，拐点与拐点之间直线段上的信息点不予保留
     * @param offset
     *            偏移量<br/>
     *            所有的轮廓信息相对于原始图像对应点的偏移量，相当于在每一个检测出的轮廓点上加上该偏移量，并且Point还可以是负值
     */
    public static void findContours(Mat srcMat, List<MatOfPoint> contours, Mat hierarchy, int mode, int method,
                                    Point offset) {
        Imgproc.findContours(srcMat, contours, hierarchy, mode, method, offset);
    }

    /**
     * 将检测到的轮廓画到图像中
     *
     * @param image
     *            要绘制轮廓的图像
     * @param contours
     *            所有输入的轮廓，每个轮廓被保存成一个point向量
     * @param contourIdx
     *            指定要绘制轮廓的编号，如果是负数，则绘制所有的轮廓
     * @param color
     *            绘制轮廓所用的颜色
     */
    public static void drawContours(Mat image, List<MatOfPoint> contours, int contourIdx, Scalar color) {
        Imgproc.drawContours(image, contours, contourIdx, color);
    }

    /**
     * 将检测到的轮廓画到图像中
     *
     * @param image
     *            要绘制轮廓的图像
     * @param contours
     *            所有输入的轮廓，每个轮廓被保存成一个point向量
     * @param contourIdx
     *            指定要绘制轮廓的编号，如果是负数，则绘制所有的轮廓
     * @param color
     *            绘制轮廓所用的颜色
     * @param thickness
     *            绘制轮廓的线的粗细，如果是负数，则轮廓内部被填充
     */
    public static void drawContours(Mat image, List<MatOfPoint> contours, int contourIdx, Scalar color, int thickness) {
        Imgproc.drawContours(image, contours, contourIdx, color, thickness);
    }

    /**
     * 将检测到的轮廓画到图像中
     *
     * @param image
     *            要绘制轮廓的图像
     * @param contours
     *            所有输入的轮廓，每个轮廓被保存成一个point向量
     * @param contourIdx
     *            指定要绘制轮廓的编号，如果是负数，则绘制所有的轮廓
     * @param color
     *            绘制轮廓所用的颜色
     * @param thickness
     *            绘制轮廓的线的粗细，如果是负数，则轮廓内部被填充
     * @param lineType
     *            绘制轮廓的线的连通性
     * @param hierarchy
     *            关于层级的可选参数，只有绘制部分轮廓时才会用到
     * @param maxLevel
     *            绘制轮廓的最高级别，这个参数只有hierarchy有效的时候才有效<br/>
     *            maxLevel=0，绘制与输入轮廓属于同一等级的所有轮廓即输入轮廓和与其相邻的轮廓<br/>
     *            maxLevel=1,绘制与输入轮廓同一等级的所有轮廓与其子节点<br/>
     *            maxLevel=2，绘制与输入轮廓同一等级的所有轮廓与其子节点以及子节点的子节点
     * @param offset
     *            按给定值移动所有点的坐标
     */
    public static void drawContours(Mat image, List<MatOfPoint> contours, int contourIdx, Scalar color, int thickness,
                                    int lineType, Mat hierarchy, int maxLevel, Point offset) {
        Imgproc.drawContours(image, contours, contourIdx, color, thickness, lineType, hierarchy, maxLevel, offset);
    }

    /**
     * 包含点集最小面积的矩形，这个矩形是可以有偏转角度的，可以与图像的边界不平行
     *
     * @param point2f
     *            输入的点集
     * @return 矩形
     */
    public static RotatedRect minAreaRect(MatOfPoint2f point2f) {
        return Imgproc.minAreaRect(point2f);
    }

    /**
     * 计算轮廓的垂直边界最小矩形，矩形是与图像上下边界平行的
     *
     * @param point
     *            输入的点集
     * @return 矩形
     */
    public static Rect boundingRect(MatOfPoint point) {
        return Imgproc.boundingRect(point);
    }

    /**
     * 计算图像轮廓的周长
     *
     * @param curve
     *            图像的轮廓
     * @param closed
     *            轮廓是否封闭的
     * @return 周长
     */
    public static double arcLength(MatOfPoint2f curve, boolean closed) {
        return Imgproc.arcLength(curve, closed);
    }

    /**
     * 获取轮廓的面积
     *
     * @param con
     *            轮廓
     */
    public static double contourArea(MatOfPoint con) {
        return Imgproc.contourArea(con);
    }

    /**
     * 获得图像绕着某一点的旋转矩阵
     *
     * @param center
     *            旋转的中心点
     * @param angle
     *            旋转的角度
     * @param scale
     *            图像缩放因子
     * @return 旋转矩阵
     */
    public static Mat getRotationMatrix2D(Point center, double angle, double scale) {
        return Imgproc.getRotationMatrix2D(center, angle, 1);
    }

    /**
     * 对图像进行仿射变换，变换的图像边界是平行的
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param rotationMat
     *            透视变换的矩阵，通过getRotationMatrix2D方法获取的对象
     * @param dsize
     *            输出图像的大小
     */
    public static void warpAffine(Mat srcMat, Mat dstMat, Mat rotationMat, Size dsize) {
        Imgproc.warpAffine(srcMat, dstMat, rotationMat, dsize);
    }

    /**
     * 利用概率霍夫变换来检测直线
     *
     * @param image
     *            输入图像，要求是单通道，8位图像
     * @param lines
     *            输出参数，4个元素表示，即直线的起始和终止端点的4个坐标（x1,y1）,(x2,y2)
     * @param rho
     *            距离分辨率，一般为1
     * @param theta
     *            角度的分辨率，一般CV_PI/180
     * @param threshold
     *            阈值，hough变换图像空间值最大点，大于则执行
     * @param minLineLength
     *            最小直线长度（像素），即如果小于该值，则不被认为是一条直线
     * @param maxLineGap
     *            直线间隙最大值，如果两条直线间隙大于该值，则被认为是两条线段，否则是一条
     */
    public static void houghLinesP(Mat image, Mat lines, double rho, double theta, int threshold, double minLineLength,
                                   double maxLineGap) {
        Imgproc.HoughLinesP(image, lines, rho, theta, threshold, minLineLength, maxLineGap);
    }

    /**
     * 对图像轮廓点进行多边形拟合
     *
     * @param curve
     *            图像的轮廓点组成的点集
     * @param approxCurve
     *            输出的多边形点集
     * @param epsilon
     *            输出的精度，就是两个轮廓点之间最大距离数，5,6,7,8,...
     * @param closed
     *            输出的多边形是否封闭
     */
    public static void approxPolyDP(MatOfPoint2f curve, MatOfPoint2f approxCurve, double epsilon, boolean closed) {
        Imgproc.approxPolyDP(curve, approxCurve, epsilon, closed);
    }

    /**
     * 从原图像中提取提取一个感兴趣的矩形区域图像
     *
     * @param image
     *            输入图像
     * @param patchSize
     *            获取矩形的大小
     * @param center
     *            获取的矩形在原图像中的位置
     * @param patch
     *            输出的图像
     */
    public static void getRectSubPix(Mat image, Size patchSize, Point center, Mat patch) {
        Imgproc.getRectSubPix(image, patchSize, center, patch);
    }

    /**
     * 根据输入和输出点获得图像透视变换的矩阵
     *
     * @param srcMat
     *            透视变换输入点
     * @param dstMat
     *            透视变换输出点
     */
    public static Mat getPerspectiveTransform(Mat srcMat, Mat dstMat) {
        return Imgproc.getPerspectiveTransform(srcMat, dstMat);
    }

    /**
     * 对图像进行透视变换，就是变形
     *
     * @param srcMat
     *            输入图像
     * @param dstMat
     *            输出图像
     * @param rotationMat
     *            透视变换的矩阵，通过getPerspectiveTransform方法获取的对象
     * @param dsize
     *            输出图像的大小
     */
    public static void warpPerspective(Mat srcMat, Mat dstMat, Mat rotationMat, Size dsize) {
        Imgproc.warpPerspective(srcMat, dstMat, rotationMat, dsize);
    }

    /**
     * 对图像执行缩放
     *
     * @param src
     *            输入图像
     * @param dst
     *            输出图像
     * @param dsize
     *            输出图像的大小
     * @param fx
     *            横轴上的比例因子
     * @param fy
     *            垂直轴上的比例因子
     * @param interpolation
     *            插值方法的整数变量<br/>
     *            Imgproc.INTER_NEAREST最邻近<br/>
     *            Imgproc.INTER_LINEAR双线性<br/>
     *            Imgproc.INTER_AREA基于像素区域，当图像缩小时候，该方法可以避免波纹出现。当图像放大时，
     *            类似于INTER_NEAREST方法<br/>
     *            Imgproc.INTER_CUBIC立方插值
     */
    public static void resize(Mat src, Mat dst, Size dsize, double fx, double fy, int interpolation) {
        Imgproc.resize(src, dst, dsize, fx, fy, interpolation);
    }

    /**
     * 图片翻转
     *
     * @param src
     *            输入图像
     * @param dst
     *            输出图像
     * @param flipCode
     *            翻转类型 1：水平方向旋转180度 0：垂直方向旋转180度 -1：垂直和水平方向同时旋转
     */
    public static void flip(Mat src, Mat dst, Integer flipCode) {
        Core.flip(src, dst, flipCode);
    }

    /**
     * 模板匹配
     *
     * @param image
     *            源图像
     * @param templ
     *            模板图像
     * @paramresult
     *            结果
     * @param method
     *            匹配方法<br/>
     *            Imgproc.TM_SQDIFF：平方差匹配<br/>
     *            Imgproc.TM_SQDIFF_NORMED：标准平方差匹配<br/>
     *            利用平方差来进行匹配,最好匹配为0.匹配越差,匹配值越大<br/>
     *            Imgproc.TM_CCORR：相关匹配<br/>
     *            Imgproc.TM_CCORR_NORMED：标准相关匹配<br/>
     *            采用模板和图像间的乘法操作,所以较大的数表示匹配程度较高,0标识最坏的匹配效果<br/>
     *            Imgproc.TM_CCOEFF：相关性系数匹配<br/>
     *            Imgproc.TM_CCOEFF_NORMED：标准相关性系数匹配<br/>
     *            将模版对其均值的相对值与图像对其均值的相关值进行匹配,1表示完美匹配,-1表示糟糕的匹配,0表示没有任何相关性(随机序列).
     *            <br/>
     */
    public static Core.MinMaxLocResult matchTemplate(Mat image, Mat templ, int method) {
        Mat result = new Mat();
        Imgproc.matchTemplate(image, templ, result, method);

        Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(result);
        return minMaxLocResult;
    }

    /**
     * 图片顺时针旋转，只能旋转90、180、270
     *
     * @param srcMat
     *            旋转源图像
     * @param rotateType
     *            旋转类型，1-90度，2-180度，3-270，其他值为90度
     * @return 结果图像
     */
    public static Mat picRotate(Mat srcMat, int rotateType, int width, int height) {
        int srcWidth = srcMat.width();
        int srcHeight = srcMat.height();

        MatOfPoint2f srcP = new MatOfPoint2f(new Point(0, srcHeight), new Point(0, 0), new Point(srcWidth, srcHeight),
                new Point(srcWidth, 0));
        if (rotateType == 2) {
            srcP = new MatOfPoint2f(new Point(srcWidth, srcHeight), new Point(0, srcHeight), new Point(srcWidth, 0),
                    new Point(0, 0));
        } else if (rotateType == 3) {
            srcP = new MatOfPoint2f(new Point(srcWidth, 0), new Point(srcWidth, srcHeight), new Point(0, 0),
                    new Point(0, srcHeight));
        }

        MatOfPoint2f dstP = new MatOfPoint2f(new Point(0, 0), new Point(width, 0), new Point(0, height),
                new Point(width, height));

        Mat transform = OpenCVImageHandle.getPerspectiveTransform(srcP, dstP);

        Mat resultMat = new Mat();
        resultMat.create(height, width, 3);
        OpenCVImageHandle.warpPerspective(srcMat, resultMat, transform, resultMat.size());
        return resultMat;
    }

    /**
     * 图片相似度计算
     *
     * @param srcPic
     *            源图，如果图片为空，直接返回0
     * @param similarPic
     *            相似度图片，如果图片为空，直接返回0
     * @return 最好的相似度
     */
    public static double similarCalc(String srcPic, String similarPic) {
        Mat srcMat = imread(srcPic);
        if (srcMat.empty()) {
            return 0;
        }
        Mat similarMat = imread(similarPic);
        if (similarMat.empty()) {
            return 0;
        }
        Mat graySrcMat = srcMat.clone();
        Mat graySimilarMat = similarMat.clone();
        // 处理成灰度图，减少颜色的影响
        // OpenCVImageHandle.cvtColor(srcMat, graySrcMat);
        // OpenCVImageHandle.cvtColor(similarMat, graySimilarMat);

        int tmType = Imgproc.TM_CCORR_NORMED;
        Core.MinMaxLocResult locResult = OpenCVImageHandle.matchTemplate(graySrcMat, graySimilarMat, tmType);
        double resultVal = locResult.maxVal;
        // 如果匹配方法为平方差匹配，minVal为最好的匹配结果
        if (tmType == Imgproc.TM_SQDIFF || tmType == Imgproc.TM_SQDIFF_NORMED) {
            resultVal = 1 - locResult.minVal;
        }

        return resultVal;
    }

    /**
     * 计算图片轮廓的周长
     * @param picPath 源图片
     * @return 周长
     */
    public static double calcPicOutlineLength(String picPath) {
        Mat srcMat = Imgcodecs.imread(picPath);
        if (srcMat.empty()) {
            return 0;
        }
        // 获取源图像的长度
        Double srcWidth = new Double(srcMat.width());
        Double srcHeight = new Double(srcMat.height());
        Double picGirth = (srcWidth + srcHeight) * 2; // 图片周长
        Double picArea = (srcWidth + 10) * (srcHeight + 10); // 图片面积

        // 灰度化
        Mat grayMat = new Mat();
        Imgproc.cvtColor(srcMat, grayMat, Imgproc.COLOR_RGB2GRAY);
        srcMat.release();

        // 二值化
        Mat thresholdMat = new Mat();
        Imgproc.threshold(grayMat, thresholdMat, 0, 255, Imgproc.THRESH_OTSU);
        grayMat.release();

        Mat blankMat = new Mat(srcHeight.intValue() + 10, srcWidth.intValue() + 10, thresholdMat.type(),
                new Scalar(255));
        Mat tMat = new Mat(blankMat, new Rect(5, 5, srcWidth.intValue(), srcHeight.intValue()));
        thresholdMat.copyTo(tMat);
        thresholdMat.release();

        // 查找轮廓
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(blankMat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
        blankMat.release();

        double outlineLength = 0; // 周长

        // 计算轮廓周长的阈值，当轮廓的周长大于阈值时，不算轮廓的周长
        double picGirthThreshold = picGirth * 0.9;
        // 计算面积的阈值，当轮廓的面积大于阈值时，不算轮廓的周长
        double picAreaThreshold = picArea * 0.9;

        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            MatOfPoint matOfPoint = contours.get(contourIdx);
            MatOfPoint2f point2f = new MatOfPoint2f(matOfPoint.toArray());
            double arcLength = Imgproc.arcLength(point2f, false);

            // 轮廓的周长必须在图片周长的90%和周长以内
            if (arcLength > picGirthThreshold && arcLength < picGirth) {
                Double outlineArea = Imgproc.contourArea(matOfPoint);
                // 轮廓的面积大于阈值时，不算总轮廓的周长
                if (outlineArea > picAreaThreshold) {
                    continue;
                }
            }

            matOfPoint.release();
            point2f.release();
            outlineLength += arcLength;
        }

        return outlineLength;
    }

    /**
     * 图片相似度计算
     *
     * @param srcPic
     *            源图，如果图片为空，直接返回0
     * @param similarPic
     *            相似度图片，如果图片为空，直接返回0
     * @param tmType
     *            匹配方法<br/>
     *            Imgproc.TM_SQDIFF：平方差匹配<br/>
     *            Imgproc.TM_SQDIFF_NORMED：标准平方差匹配<br/>
     *            利用平方差来进行匹配,最好匹配为0.匹配越差,匹配值越大<br/>
     *            Imgproc.TM_CCORR：相关匹配<br/>
     *            Imgproc.TM_CCORR_NORMED：标准相关匹配<br/>
     *            采用模板和图像间的乘法操作,所以较大的数表示匹配程度较高,0标识最坏的匹配效果<br/>
     *            Imgproc.TM_CCOEFF：相关性系数匹配<br/>
     *            Imgproc.TM_CCOEFF_NORMED：标准相关性系数匹配<br/>
     *            将模版对其均值的相对值与图像对其均值的相关值进行匹配,1表示完美匹配,-1表示糟糕的匹配,0表示没有任何相关性(随机序列).
     * @return 最好的相似度
     */
    public static double similarCalc(String srcPic, String similarPic, int tmType) {
        Mat srcMat = imread(srcPic);
        if (srcMat.empty()) {
            return 0;
        }
        Mat similarMat = imread(similarPic);
        if (similarMat.empty()) {
            return 0;
        }
        Mat graySrcMat = srcMat.clone();
        Mat graySimilarMat = similarMat.clone();
        // 处理成灰度图，减少颜色的影响
        // OpenCVImageHandle.cvtColor(srcMat, graySrcMat);
        // OpenCVImageHandle.cvtColor(similarMat, graySimilarMat);

        Core.MinMaxLocResult locResult = OpenCVImageHandle.matchTemplate(graySrcMat, graySimilarMat, tmType);
        double resultVal = locResult.maxVal;
        // 如果匹配方法为平方差匹配，minVal为最好的匹配结果
        if (tmType == Imgproc.TM_SQDIFF || tmType == Imgproc.TM_SQDIFF_NORMED) {
            resultVal = 1 - locResult.minVal;
        }

        return resultVal;
    }

    /**
     * 两张相同尺寸的图片计算相似度
     *
     * @param srcPic1
     *            图片1
     * @param srcPic2
     *            图片2
     */
    public static double calcSimilarSameSize(String srcPic1, String srcPic2) {
        return calcSimilarSameSize(imread(srcPic1), imread(srcPic2));
    }

    /**
     * 两张相同尺寸的图片计算相似度
     *
     * @param srcMat1
     *            图片1
     * @param srcMat2
     *            图片2
     */
    public static double calcSimilarSameSize(Mat srcMat1, Mat srcMat2) {
        List<Mat> images1 = new ArrayList<>();
        images1.add(srcMat1);
        MatOfInt channels1 = new MatOfInt(0); // 图像通道数，0表示只有一个通道
        MatOfInt histSize1 = new MatOfInt(256); // CV_8U类型的图片范围是0~255，共有256个灰度级
        Mat histogramOfGray1 = new Mat(); // 输出直方图结果，共有256行，行数的相当于对应灰度值，每一行的值相当于该灰度值所占比例
        MatOfFloat histRange1 = new MatOfFloat(0, 255);
        Imgproc.calcHist(images1, channels1, new Mat(), histogramOfGray1, histSize1, histRange1, false); // 计算直方图
        // 按行归一化
        Core.normalize(histogramOfGray1, histogramOfGray1, 0, histogramOfGray1.rows(), Core.NORM_MINMAX, -1, new Mat());

        List<Mat> images2 = new ArrayList<>();
        images2.add(srcMat2);
        MatOfInt channels2 = new MatOfInt(0); // 图像通道数，0表示只有一个通道
        MatOfInt histSize2 = new MatOfInt(256); // CV_8U类型的图片范围是0~255，共有256个灰度级
        Mat histogramOfGray2 = new Mat(); // 输出直方图结果，共有256行，行数的相当于对应灰度值，每一行的值相当于该灰度值所占比例
        MatOfFloat histRange2 = new MatOfFloat(0, 255);
        Imgproc.calcHist(images2, channels2, new Mat(), histogramOfGray2, histSize2, histRange2, false); // 计算直方图
        // 按行归一化
        Core.normalize(histogramOfGray2, histogramOfGray2, 0, histogramOfGray2.rows(), Core.NORM_MINMAX, -1, new Mat());

        double similarity = Imgproc.compareHist(histogramOfGray1, histogramOfGray2, Imgproc.CV_COMP_CORREL);
        return similarity;
    }

    /**
     * 相似度计算：转化为二值化，并去除噪点
     *
     * @param srcPath
     *            图片1
     * @param desPath
     *            图片2
     * @return 相似度
     */
    public static double thresholdMatSimilar(String srcPath, String desPath) {
        Mat srcMat = OpenCVImageHandle.imread(srcPath);
        // 如果模板文件不存在，直接返回false
        if (srcMat == null || srcMat.empty()) {
            return 0;
        }
        Mat desMat = OpenCVImageHandle.imread(desPath);
        if (desMat == null || desMat.empty()) {
            return 0;
        }

        double picSimilar = 0;

        OpenCVImageHandle.cvtColor(srcMat, srcMat);
        OpenCVImageHandle.threshold(srcMat, srcMat, 0, 255, Imgproc.THRESH_OTSU);

        Mat sMat = OpenCVImageHandle.getMinBlackAreaByThread(srcMat);
        srcMat.release();

        OpenCVImageHandle.cvtColor(desMat, desMat);
        OpenCVImageHandle.threshold(desMat, desMat, 0, 255, Imgproc.THRESH_OTSU);

        Mat dMat = OpenCVImageHandle.getMinBlackAreaByThread(desMat);
        desMat.release();
        Size sSize = sMat.size();
        Size dSize = dMat.size();
        Mat sMat1 = sMat.clone();
        Mat dMat1 = dMat.clone();
        Size maxSize = null, minSize = null;
        if (sSize.width * sSize.height > dSize.width * dSize.height) {
            maxSize = sSize;
            minSize = dSize;
        } else {
            maxSize = dSize;
            minSize = sSize;
        }
        OpenCVImageHandle.resize(dMat, dMat, maxSize, 1, 1, Imgproc.INTER_AREA);
        OpenCVImageHandle.resize(sMat, sMat, maxSize, 1, 1, Imgproc.INTER_AREA);
        double picSimilar1 = OpenCVImageHandle.threadPicCompare(sMat, dMat);

        OpenCVImageHandle.resize(sMat1, sMat1, minSize, 1, 1, Imgproc.INTER_AREA);
        OpenCVImageHandle.resize(dMat1, dMat1, minSize, 1, 1, Imgproc.INTER_AREA);
        double picSimilar2 = OpenCVImageHandle.threadPicCompare(sMat1, dMat1);

        picSimilar = (picSimilar1 + picSimilar2) / 2;

        sMat1.release();
        dMat1.release();
        sMat.release();
        dMat.release();

        return picSimilar;
    }

    /**
     * 将Mat类型转化成BufferedImage类型
     *
     * @paramamatrix
     *            Mat对象
     * @paramfileExtension
     *            文件扩展名
     */
    public static BufferedImage mat2Img(Mat mat) {
        byte[] data1 = new byte[mat.rows() * mat.cols() * (int) (mat.elemSize())];
        mat.get(0, 0, data1);
        BufferedImage image1 = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_BYTE_GRAY);
        image1.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data1);
        return image1;
    }

    /**
     * 二值化图像，获取最小的黑色区域
     *
     * @param threadMat
     *            二值化图像
     * @return 最小的黑色区域
     */
    public static Mat getMinBlackAreaByThread(Mat threadMat) {
        if (threadMat.empty()) {
            return null;
        }
        int rows = threadMat.rows();
        int cols = threadMat.cols();

        // 清除噪点，检测周围5个像素的颜色是否全部是白色，如果全部是白色，清除该点
        Mat resultMat = new Mat(rows, cols, threadMat.type());
        int aroundNum = 3;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double d = threadMat.get(row, col)[0];
                if (d == 0) { // 只检测黑点
                    int blackDotNum = 0;
                    for (int i = 1; i < aroundNum; i++) {
                        int minRow = row - i;
                        int maxRow = row + i;
                        minRow = minRow < 0 ? 0 : minRow;
                        maxRow = maxRow >= rows ? rows - 1 : maxRow;
                        for (int j = 1; j < aroundNum - i; j++) {
                            int minCol = col - j;
                            int maxCol = col + j;
                            minCol = minCol < 0 ? 0 : minCol;
                            maxCol = maxCol >= cols ? cols - 1 : maxCol;
                            double minD = threadMat.get(minRow, minCol)[0];
                            double maxD = threadMat.get(maxRow, maxCol)[0];
                            if (minD == 0) {
                                blackDotNum++;
                            }
                            if (maxD == 0) {
                                blackDotNum++;
                            }
                        }
                    }
                    // 周围有黑点，认为不是噪点
                    if (blackDotNum > 0) {
                        resultMat.put(row, col, d);
                    } else {
                        resultMat.put(row, col, 255);
                    }
                } else {
                    resultMat.put(row, col, 255);
                }
            }
        }

        // 定义最大最小的行和列
        int minRow = 0, maxRow = 0, minCol = 0, maxCol = 0;

        // 计算最小的行
        outer: for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double d = resultMat.get(row, col)[0];
                if (d == 0) {
                    minRow = row;
                    break outer;
                }
            }
        }
        // 计算最小的列
        outer: for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                double d = resultMat.get(row, col)[0];
                if (d == 0) {
                    minCol = col;
                    break outer;
                }
            }
        }
        // 计算最大的行
        outer: for (int row = rows - 1; row >= 0; row--) {
            for (int col = 0; col < cols; col++) {
                double d = resultMat.get(row, col)[0];
                if (d == 0) {
                    maxRow = row;
                    break outer;
                }
            }
        }
        // 计算最大的列
        outer: for (int col = cols - 1; col >= 0; col--) {
            for (int row = 0; row < rows; row++) {
                double d = resultMat.get(row, col)[0];
                if (d == 0) {
                    maxCol = col;
                    break outer;
                }
            }
        }

        Mat blackAreaMat = new Mat(maxRow - minRow, maxCol - minCol, threadMat.type());
        for (int i = 0, row = minRow; row < maxRow + 1; i++, row++) {
            for (int j = 0, col = minCol; col < maxCol + 1; j++, col++) {
                blackAreaMat.put(i, j, resultMat.get(row, col));
            }
        }

        return blackAreaMat;
    }

    /**
     * 相同尺寸的图像二值化对象比对，比较黑点的数量
     *
     * @param m1
     *            图像1
     * @param m2
     *            图像2
     * @return 相似度
     */
    public static double threadPicCompare(Mat m1, Mat m2) {
        if (m1.empty() || m2.empty()) {
            return 0;
        }
        int rows = m1.rows();
        int cols = m1.cols();

        // 误差控制为2%
        int errorRow = rows * 2 / 100;
        // 如果误差行数小于1，默认设置为1
        errorRow = errorRow < 1 ? 1 : errorRow;
        int errorCol = cols * 2 / 100;
        // 如果误差列表小于1，默认设置为1
        errorCol = errorCol < 1 ? 1 : errorCol;

        int blackCompare1 = 0;
        int blackNum1 = 0; // 图片的黑点数量
        for (int row = 0; row < rows; row++) {

            int beginRow = row - errorRow < 0 ? 0 : row - errorRow;
            int endRow = row + errorRow > rows - 1 ? rows - 1 : row + errorRow;

            for (int col = 0; col < cols; col++) {
                double d = m1.get(row, col)[0];
                if (d == 0) {
                    blackNum1++;
                    int beginCol = col - errorCol < 0 ? 0 : col - errorCol;
                    int endCol = col + errorCol > cols - 1 ? cols - 1 : col + errorCol;
                    outer: for (int row2 = beginRow; row2 < endRow; row2++) {
                        for (int col2 = beginCol; col2 < endCol; col2++) {
                            if (m2.get(row2, col2)[0] == d) {
                                blackCompare1++;
                                break outer;
                            }
                        }
                    }
                }
            }
        }

        int blackCompare2 = 0;
        int blackNum2 = 0; // 图片的黑点数量
        for (int row = 0; row < rows; row++) {

            int beginRow = row - errorRow < 0 ? 0 : row - errorRow;
            int endRow = row + errorRow > rows - 1 ? rows - 1 : row + errorRow;

            for (int col = 0; col < cols; col++) {
                double d = m2.get(row, col)[0];
                if (d == 0) {
                    blackNum2++;
                    int beginCol = col - errorCol < 0 ? 0 : col - errorCol;
                    int endCol = col + errorCol > cols - 1 ? cols - 1 : col + errorCol;
                    outer: for (int row2 = beginRow; row2 < endRow; row2++) {
                        for (int col2 = beginCol; col2 < endCol; col2++) {
                            if (m1.get(row2, col2)[0] == d) {
                                blackCompare2++;
                                break outer;
                            }
                        }
                    }
                }
            }
        }

        if (blackCompare1 == 0 || blackCompare2 == 0) {
            return 0;
        }

        double blackCompareRatio = blackCompare1 * 1.0 / blackNum1 + blackCompare2 * 1.0 / blackNum2;
        double compareRation = 0;
        // 相似度算法，两张图片黑点数对比，差别率
        if (blackCompare1 > blackCompare2) {
            compareRation = blackCompare2 * 1.0 / blackCompare1;
        } else {
            compareRation = blackCompare1 * 1.0 / blackCompare2;
        }
        // 计算黑点的比例
        double blackRatio = 0;
        if (blackNum1 > blackNum2) {
            blackRatio = blackNum2 * 1.0 / blackNum1;
        } else {
            blackRatio = blackNum1 * 1.0 / blackNum2;
        }
        return (compareRation + blackCompareRatio + blackRatio) / 4;

        //return (blackCompare2 * 1.0 / blackNum2 + blackCompare1 * 1.0 / blackNum1) / 2;
    }

    /**
     * 峰值信噪比，用于相似度对比
     *
     * @param m1
     *            图像1
     * @param m2
     *            图像2
     * @return 相似度值
     */
    public static double getPSNR(Mat m1, Mat m2) {
        Mat s1 = new Mat();
        Core.absdiff(m1, m2, s1); // |I1 - I2|AbsDiff函数是 OpenCV 中计算两个数组差的绝对值的函数
        s1.convertTo(s1, CvType.CV_32F); // 这里我们使用的CV_32F来计算，因为8位无符号char是不能进行平方计算
        s1 = s1.mul(s1); // |I1 - I2|^2

        Scalar s = Core.sumElems(s1); // 对每一个通道进行加和

        double sse = s.val[0] + s.val[1] + s.val[2]; // sum channels

        if (sse <= 1e-10){
            return 0;
        } // 对于非常小的值我们将约等于0

        else {
            double mse = sse / (double) (m1.channels() * m1.total());// 计算MSE
            double psnr = 10.0 * Math.log10((255 * 255) / mse);
            return psnr;// 返回PSNR
        }
    }

    /**
     * 结构相似性，是一种衡量两幅图像相似度的指标
     *
     * @param m1
     *            图像1
     * @param m2
     *            图像2
     * @return 相似度值
     */
    public static double ssim(Mat m1, Mat m2) {
        double C1 = 6.5025, C2 = 58.5225;
        int d = CvType.CV_32F;
        Mat i1 = new Mat(), i2 = new Mat();
        m1.convertTo(i1, d);
        m2.convertTo(i2, d);
        Mat I1_2 = i1.mul(i1);
        Mat I2_2 = i2.mul(i2);
        Mat I1_I2 = i1.mul(i2);
        Mat mu1 = new Mat(), mu2 = new Mat();
        gaussianBlur(i1, mu1, new Size(11, 11), 1.5);
        gaussianBlur(i2, mu2, new Size(11, 11), 1.5);
        Mat mu1_2 = mu1.mul(mu1);
        Mat mu2_2 = mu2.mul(mu2);
        Mat mu1_mu2 = mu1.mul(mu2);
        Mat sigma1_2 = new Mat(), sigam2_2 = new Mat(), sigam12 = new Mat();
        gaussianBlur(I1_2, sigma1_2, new Size(11, 11), 1.5);
        Core.subtract(sigma1_2, mu1_2, sigma1_2);
        // sigma1_2 -= mu1_2;

        gaussianBlur(I2_2, sigam2_2, new Size(11, 11), 1.5);
        Core.subtract(sigam2_2, mu2_2, sigam2_2);
        // sigam2_2 -= mu2_2;

        gaussianBlur(I1_I2, sigam12, new Size(11, 11), 1.5);
        Core.subtract(sigam12, mu1_mu2, sigam12);
        // sigam12 -= mu1_mu2;
        Mat t1 = new Mat(), t2 = new Mat(), t3 = new Mat();
        Core.add(mu1_mu2, mu1_mu2, mu1_mu2);
        Core.add(mu1_mu2, new MatOfDouble(C1), t1);
        // t1 = 2 * mu1_mu2 + C1;

        Core.add(sigam12, sigam12, sigam12);
        Core.add(sigam12, new MatOfDouble(C2), t2);
        // t2 = 2 * sigam12 + C2;
        t3 = t1.mul(t2);

        Core.add(mu1_2, mu1_2, mu2_2);
        Core.add(mu2_2, new MatOfDouble(C1), t1);
        // t1 = mu1_2 + mu2_2 + C1;

        Core.add(sigma1_2, sigam2_2, sigma1_2);
        Core.add(sigma1_2, new MatOfDouble(C2), t2);
        // t2 = sigma1_2 + sigam2_2 + C2;
        t1 = t1.mul(t2);

        Mat ssim_map = new Mat();
        Core.divide(t3, t1, ssim_map);
        Scalar mssim = Core.mean(ssim_map);

        double ssim = (mssim.val[0] + mssim.val[1] + mssim.val[2]) / 3;
        return ssim;
    }

    /**
     * 根据BufferedImage获取图片的直方图
     *
     * @paramimg
     *            文件流
     */
    public static int[] getData(Mat mat) throws Exception {
        return getData(mat2Img(mat));
    }

    /**
     * 根据BufferedImage获取图片的直方图
     *
     * @param img
     *            文件流
     */
    public static int[] getData(BufferedImage img) throws Exception {
        BufferedImage slt = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        slt.getGraphics().drawImage(img, 0, 0, 100, 100, null);
        int[] data = new int[256];
        for (int x = 0; x < slt.getWidth(); x++) {
            for (int y = 0; y < slt.getHeight(); y++) {
                int rgb = slt.getRGB(x, y);
                Color myColor = new Color(rgb);
                int r = myColor.getRed();
                int g = myColor.getGreen();
                int b = myColor.getBlue();
                data[(r + g + b) / 3]++;
            }
        }
        // data 就是所谓图形学当中的直方图的概念
        return data;
    }

    /**
     * 根据图片全路径获取图片的直方图
     *
     * @param name
     *            图片全路径
     */
    public static int[] getData(String name) throws Exception {
        return getData(ImageIO.read(new File(name)));
    }

    /**
     * 比较两张图片，获取相似度结果
     *
     * @param srcMat1
     *            图片1
     * @param srcMat2
     *            图片2
     */
    public static float compare(Mat srcMat1, Mat srcMat2) throws Exception {
        int[] s1 = getData(srcMat1);
        int[] s2 = getData(srcMat2);
        return compare(s1, s2);
    }

    /**
     * 比较两张图片的直方图，获取相似度结果
     *
     * @param s
     *            图片1的直方图
     * @param t
     *            图片2的直方图
     */
    public static float compare(int[] s, int[] t) {
        float result = 0F;
        for (int i = 0; i < 256; i++) {
            int abs = Math.abs(s[i] - t[i]);
            int max = Math.max(s[i], t[i]);
            result += (1 - ((float) abs / (max == 0 ? 1 : max)));
        }
        return (result / 256);
    }

    /**
     * 对4个坐标点排序，顺时针排序
     *
     * @param points
     *            坐标点集合
     * @return 排序结果
     */
    public static Point[] pointSort(Point[] points) {
        if (points == null || points.length == 0) {
            return points;
        }
        double xs = 0, ys = 0;
        for(Point p : points) {
            xs += p.x;
            ys += p.y;
        }
        xs = xs / points.length;
        ys = ys / points.length;
        Point originPoint = new Point(0, ys);
        Map<Point, Double> pMap = new HashMap<Point, Double>();
        for (Point p : points) {
            if (p.y > ys) { // 中心点下的点
                pMap.put(p, 360 - CvUtil.getLineAngle(new double[] { originPoint.x, originPoint.y },
                        new double[] { xs, ys }, new double[] { p.x, p.y }));
            } else if (p.y < ys) { // 中心点上的点
                pMap.put(p, CvUtil.getLineAngle(new double[] { originPoint.x, originPoint.y }, new double[] { xs, ys },
                        new double[] { p.x, p.y }));
            } else { // 与中心点一行的点
                if (p.x > xs) {
                    pMap.put(p, 180D);
                } else {
                    pMap.put(p, 0D);
                }
            }
        }
        // 对map进行值排序
        pMap = sortMapByValue(pMap);

        Point[] ps = new Point[pMap.size()];
        int index = 0;
        for (Point p : pMap.keySet()) {
            ps[index++] = p;
        }

        return ps;
    }

    /**
     * 使用 Map按value进行排序
     *
     * @parammap
     * @return
     */
    public static Map<Point, Double> sortMapByValue(Map<Point, Double> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<Point, Double> sortedMap = new LinkedHashMap<Point, Double>();
        List<Entry<Point, Double>> entryList = new ArrayList<Entry<Point, Double>>(oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());

        Iterator<Entry<Point, Double>> iter = entryList.iterator();
        Entry<Point, Double> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

    /**
     * 清理多余的点（至少3个点），根据点之间的距离计算，清理距离最短的点，并根据两点计算新的点
     *
     * @param points
     *            点集
     * @param clearNum
     *            清理数量
     * @return 清理后的集合
     */
    public static Point[] clearMorePoint(Point[] points, int clearNum) {
        if (points.length < 3) {
            return points;
        }
        Point[] ps = points;
        for (int cn = 0; cn < clearNum; cn++) {
            double minDis = 100000;
            int index1 = 0, index2 = 0;
            for (int i = 0; i < ps.length; i++) {
                double[] p1 = new double[] { ps[i].x, ps[i].y };
                for (int j = i + 1; j < ps.length; j++) {
                    double distance = CvUtil.getDistance(p1, new double[] { ps[j].x, ps[j].y });
                    if (distance < minDis) {
                        minDis = distance;
                        index1 = i;
                        index2 = j;
                    }
                }
            }
            Point avgP = new Point((ps[index1].x + ps[index2].x) / 2, (ps[index1].y + ps[index2].y) / 2);
            Point[] pps = new Point[ps.length - 1];
            for (int i = 0, j = 0; i < ps.length; i++) {
                if (i != index1 && i != index2) {
                    pps[j++] = ps[i];
                }
            }
            pps[pps.length - 1] = avgP;
            ps = pps;
        }

        return ps;
    }

}

class MapValueComparator implements Comparator<Entry<Point, Double>> {

    @Override
    public int compare(Entry<Point, Double> o1, Entry<Point, Double> o2) {
        return o1.getValue() < o2.getValue() ? 1 : -1;
    }

}
