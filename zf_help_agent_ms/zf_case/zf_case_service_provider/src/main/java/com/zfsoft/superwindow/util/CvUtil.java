package com.zfsoft.superwindow.util;

/**
 * 计算机视觉帮助方法
 */
public class CvUtil {

    /**
     * 获取两点之间的距离
     *
     * @param p1
     *            第一个点 （x1, y1）
     * @param p2
     *            第二个点（x2, y2）
     */
    public static double getDistance(double[] p1, double[] p2) {
        return Math.sqrt(Math.abs((p1[0] - p2[0]) * (p1[0] - p2[0]) + (p1[1] - p2[1]) * (p1[1] - p2[1])));
    }

    /**
     * 计算两条直线的交点。直线由整数向量形式提供。
     *
     * @param line1
     *            第一条线的点集合（x1, y1, x2, y2）
     * @param line2
     *            第二条线的点集合（x3, y3, x4, y4）
     * @return 返回交点
     */
    public static double[] getCrossPoint(double[] line1, double[] line2) {
        double[] pt = new double[2];
        double k1, k2, b1, b2;
        // 如果第一条直线斜率不存在
        if (line1[0] == line1[2]) {
            pt[0] = line1[0];
            pt[1] = line2[1] == line2[3] ? line2[1]
                    : (line2[1] - line2[3]) * (pt[0] - line2[0]) / (line2[0] - line2[2]) + line2[1];
        } else if (line2[0] == line2[2]) {
            // 如果第二条直线斜率不存在
            pt[0] = line2[0];
            pt[1] = line1[1] == line1[3] ? line1[1]
                    : (line1[1] - line1[3]) * (pt[0] - line1[0]) / (line1[0] - line1[2]) + line1[1];
        } else {
            // 求出斜截式方程。然后让k1x + b1 = k2x + b2，解出x，再算出y即可
            k1 = (line1[3] - line1[1]) / (line1[2] - line1[0]);
            b1 = (line1[1] - k1 * line1[0]);
            k2 = (line2[3] - line2[1]) / (line2[2] - line2[0]);
            b2 = (line2[1] - k2 * line2[0]);
            pt[0] = (b2 - b1) / (k1 - k2); // 算出x
            pt[1] = k1 * pt[0] + b1; // 算出y
        }
        return pt;
    }

    /**
     * 获取三个点之间的夹角，夹角为按三个点的顺序画两条直线得到的角度<br/>
     * 根据三角形的余弦定理计算夹角cosA = (b * b + c * c - a * a) / (2 * b * c)
     *
     * @param p1
     *            第一个点（x1, y1）
     * @param p2
     *            第二个点（x2, y2）
     * @param p3
     *            第三个点（x3, y3）
     * @return 夹角
     */
    public static double getLineAngle(double[] p1, double[] p2, double[] p3) {
        double l1 = getDistance(p1, p2);
        double l2 = getDistance(p3, p2);
        double l3 = getDistance(p1, p3);
        double angle = Math.toDegrees(Math.acos((l1 * l1 + l2 * l2 - l3 * l3) / (2 * l1 * l2)));
        return angle;
    }

    /**
     * 获取两条之间之间的夹角（直角或锐角）
     *
     * @param p1
     *            第一条线的第一个点（x1, y1）
     * @param p2
     *            第一条线的第二个点（x2, y2）
     * @param p3
     *            第二条线的第一个点（x3, y3）
     * @param p4
     *            第二条线的第二个点（x4, y4）
     * @return 夹角（直角或锐角）
     */
    public static double getLineAngle(double[] p1, double[] p2, double[] p3, double[] p4) {
        return getLineAngle(new double[] { p1[0], p1[1], p2[0], p2[1] }, new double[] { p3[0], p3[1], p4[0], p4[1] });
    }

    /**
     * 获取两条之间之间的夹角（直角或锐角）
     *
     * @param p1
     *            第一条线的四个点 （x1, y1, x2, y2）
     * @param p2
     *            第一条线的四个点 （x3, y3, x4, y4）
     * @return 夹角（直角或锐角）
     */
    public static double getLineAngle(double[] p1, double[] p2) {
        // 获取直线交点
        double[] crossPoint = getCrossPoint(p1, p2);
        // 根据交点以及直线上的点，获取夹角
        return getLineAngle(new double[] { p1[0], p1[1] }, crossPoint, new double[] { p2[0], p2[1] });
    }

    /**
     * 点到直线的最短距离的判断 点p0 到由两点组成的线段p1, p2
     *
     * @param p0
     *            点（x1, y1）
     * @param p1
     *            线段一点（x2, y2）
     * @param p2
     *            线段另一点（x3, y3）
     * @return 距离
     */
    public static double pointToLine(double[] p0, double[] p1, double[] p2) {
        double a, b, c;
        a = getDistance(p1, p2); // 线段的长度
        b = getDistance(p1, p0); // (x1,y1)到点的距离
        c = getDistance(p2, p0); // (x2,y2)到点的距离
        if (c <= 0.000001 || b <= 0.000001) {
            return 0;
        }
        if (a <= 0.000001 || c * c >= a * a + b * b) {
            return b;
        }
        if (b * b >= a * a + c * c) {
            return c;
        }
        double space = 0;
        double p = (a + b + c) / 2;// 半周长
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
        space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
        return space;
    }
}
