package com.zfsoft.single.util.fa;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.rest.pojo.ocr.OcrHandPrintLocationItemResponse;
import com.zfsoft.rest.pojo.ocr.OcrHandWrittenItemResponse;
import com.zfsoft.rest.pojo.ocr.OcrHandWrittenItemWordsResponse;
import com.zfsoft.single.util.MarqueeUtil;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhujiajian
 * @description ocr识别帮助类
 * @date 2018-03-12 15:16:20
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
public class FaOcrUtil {


    /**
     * 普通文字之间的分隔符
     */
    public final static String JOIN_OCR = "我我我我我我";
    /**
     * 选择框之间的分隔符
     */
    public final static String JOIN_MARQUEE_OCR = "开开开开开开";


    /**
     * 从图片切割规则中提取识别结果，返回给调用方
     *
     * @param ruleList 图片切割规则列表
     * @return 识别结果json串
     */
    public static List<Map<String, String>> ocrResultToString(List<PictureCutRuleNew> ruleList) {
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        if (ruleList == null || ruleList.size() == 0) {
            return resultList;
        }
        for (int i = 0; i < ruleList.size(); i++) {
            PictureCutRuleNew rule = ruleList.get(i);
            Map<String, String> resultMap = new HashMap<String, String>();
            resultMap.put("metadataName", rule.getName());
            resultMap.put("metadataCode", rule.getCode());
            resultMap.put("words", rule.getResultText());
            resultMap.put("picPath", rule.getResultPic());
            resultList.add(resultMap);
        }
        return resultList;
    }


    /**
     * 删除Met对象，用于对象存入缓存
     *
     * @param ruleList 图像切割规则列表
     */
    public static void removeMet(List<PictureCutRuleNew> ruleList) {
        for (PictureCutRuleNew rule : ruleList) {
            rule.setResultMat(null);
        }
    }

    /**
     * 加载Met对象
     *
     * @param ruleList 图像切割规则列表
     */
    public static void initMet(List<PictureCutRuleNew> ruleList) {
        for (PictureCutRuleNew rule : ruleList) {
            if (rule != null && StrUtil.isNotBlank(rule.getResultPic())) {
                Mat mat = OpenCVImageHandle.imread(rule.getResultPic());
                rule.setResultMat(mat);
            }
        }
    }

    /**
     * 根据百度的识别结果提取区块内容
     *
     * @param responseList OCR识别结果
     * @param wordsList    OCR识别分割结果
     */
    public static void baiduOcrResultHandle(List<OcrHandPrintLocationItemResponse> responseList, List<String> wordsList) {
        if (CollUtil.isEmpty(responseList)) {
            return;
        }
        String words = "";
        for (int i = 0; i < responseList.size(); i++) {
            OcrHandPrintLocationItemResponse response = responseList.get(i);
            String w = response.getWords();
            w = w == null ? "" : w.trim();
            if (JOIN_OCR.contains(w.toUpperCase())) {
                // 如果值为分隔符，去除值
                wordsList.add(MarqueeUtil.replaceChar(words));
                words = "";
            } else {
                // 如果值不为分隔符，则相加
                words += w;
            }
        }
        // 将最后一个结果添加到集合中
        wordsList.add(MarqueeUtil.replaceChar(words));
    }

    /**
     * 根据腾讯的识别结果提取区块内容
     *
     * @param responseList OCR识别结果
     * @param wordsList    OCR识别分割结果
     * @param wordsPosList 带位置的文字集合
     */
    public static void tencentOcrResultHandle(List<OcrHandWrittenItemResponse> responseList, List<String> wordsList,
                                              List<Map<Integer[], String>> wordsPosList) {
        if (CollUtil.isEmpty(responseList)) {
            return;
        }
        String words = "";
        Map<Integer[], String> wordsPosMap = new HashMap<Integer[], String>();
        for (int i = 1; i < responseList.size(); i++) {
            OcrHandWrittenItemResponse response = responseList.get(i);
            String w = response.getContent();
            w = w == null ? "" : w.trim();
            if (JOIN_OCR.contains(w.toUpperCase())) {
                // 如果值为分隔符，去除值
                wordsList.add(MarqueeUtil.replaceChar(words));
                if (wordsPosList != null) {
                    wordsPosList.add(wordsPosMap);
                    wordsPosMap = new HashMap<Integer[], String>();
                }
                words = "";
            } else {
                // 如果值不为分隔符，则相加
                words += w;
                if (wordsPosList != null) {
                    Integer[] pos = new Integer[]{response.getItemcoord().getX(), response.getItemcoord().getY()};
                    wordsPosMap.put(pos, w);
                }
            }
        }
        // 将最后一个结果添加到集合中
        wordsList.add(MarqueeUtil.replaceChar(words));
        if (wordsPosList != null) {
            wordsPosList.add(wordsPosMap);
        }
    }

    /**
     * 根据百度的识别结果提取区块内容，带选框的处理
     *
     * @param responseList OCR识别结果
     * @param wordsList    OCR识别分割结果
     */
    public static void baiduOcrResultMarqueeHandle(List<OcrHandPrintLocationItemResponse> responseList, List<String> wordsList) {
        if (CollUtil.isEmpty(responseList)) {
            return;
        }
        String words = "";
        boolean joinOcrFlag = false, joinMarqueeOcrFlag = true;
        for (int i = 1; i < responseList.size(); i++) {
            OcrHandPrintLocationItemResponse response = responseList.get(i);
            String w = response.getWords();
            w = w == null ? "" : w.trim();
            if (JOIN_MARQUEE_OCR.contains(w.toUpperCase())) {
                if (joinOcrFlag) { // 如果两种分隔符之间未识别到字符，默认为空
                    wordsList.add("");
                } else {
                    wordsList.add(MarqueeUtil.replaceChar(words));
                }
                joinMarqueeOcrFlag = false;
                joinOcrFlag = false;
                continue;
            }
            if (JOIN_OCR.equals(w.toUpperCase())) {
                // 如果值为分隔符，去除值
                if (joinMarqueeOcrFlag) {
                    wordsList.add(MarqueeUtil.replaceChar(words));
                }
                words = "";
                joinOcrFlag = true;
                joinMarqueeOcrFlag = true;
            } else {
                // 如果值不为分隔符，则相加
                words += w;
            }
        }
        // 将最后一个结果添加到集合中
        wordsList.add(MarqueeUtil.replaceChar(words));
    }

    /**
     * 根据腾讯的识别结果提取区块内容，带选框的处理，如果置信度小于80%，不识别为分隔符
     *
     * @param responseList OCR识别结果
     * @param wordsList    OCR识别分割结果
     * @param wordsPosList 带位置的文字集合
     * @param mFlag        是否已经处理过选择框的分隔符
     */
    public static boolean tencentOcrResultMarqueeHandle(List<OcrHandWrittenItemResponse> responseList, List<String> wordsList,
                                                        List<Map<Integer[], String>> wordsPosList, boolean mFlag) {
        if (CollUtil.isEmpty(responseList)) {
            return mFlag;
        }
        String words = "";
        Map<Integer[], String> wordsPosMap = new HashMap<Integer[], String>();
        boolean marqueeFlag = mFlag; // 分隔符连续出现的次数
        boolean joinOcrFlag = false, joinMarqueeOcrFlag = true;
        for (int i = 1; i < responseList.size(); i++) {
            OcrHandWrittenItemResponse response = responseList.get(i);
            List<OcrHandWrittenItemWordsResponse> wordsResponseList = response.getWords();
            String w = "";
            if (wordsResponseList != null && wordsResponseList.size() > 0) {
                for (int j = 0; j < wordsResponseList.size(); j++) {
                    OcrHandWrittenItemWordsResponse wordsResponse = wordsResponseList.get(i);
                    if (marqueeFlag) {// 如果分隔符连续出现的次数大于等于2次，对选择框的值进行处理
                        String ch = MarqueeUtil.replaceChar(wordsResponse.getCharacter());
                        if (MarqueeUtil.MARQUEE_SPLIT_CHAR.equals(ch)) {
                            double confidence = wordsResponse.getConfidence();
                            if (confidence < 0.85) {
                                ch = "日";
                            }
                        }
                        w += ch;
                    } else {
                        w = response.getContent();
                    }
                }
            }
            // String w = String.valueOf(itemJson.get("itemstring"));
            w = w == null ? "" : w.trim();
            if (JOIN_MARQUEE_OCR.contains(w.toUpperCase())) {
                marqueeFlag = true;
                if (joinOcrFlag) { // 如果两种分隔符之间未识别到字符，默认为空
                    wordsList.add("");
                    if (wordsPosList != null) {
                        wordsPosList.add(wordsPosMap);
                    }
                } else {
                    wordsList.add(MarqueeUtil.replaceChar(words));
                    if (wordsPosList != null) {
                        wordsPosList.add(wordsPosMap);
                    }
                }
                joinMarqueeOcrFlag = false;
                joinOcrFlag = false;
                continue;
            }
            if (JOIN_OCR.contains(w.toUpperCase())) {
                // 如果值为分隔符，去除值
                if (joinMarqueeOcrFlag) {
                    wordsList.add(MarqueeUtil.replaceChar(words));
                    if (wordsPosList != null) {
                        wordsPosList.add(wordsPosMap);
                    }
                }
                words = "";
                if (wordsPosList != null) {
                    wordsPosMap = new HashMap<Integer[], String>();
                }
                joinOcrFlag = true;
                joinMarqueeOcrFlag = true;
            } else {
                // 如果值不为分隔符，则相加
                words += w;

                if (wordsPosList != null) {
                    Integer[] pos = new Integer[]{response.getItemcoord().getX(), response.getItemcoord().getY()};
                    wordsPosMap.put(pos, w);
                }

                joinOcrFlag = false;
            }
        }
        // 将最后一个结果添加到集合中
        wordsList.add(MarqueeUtil.replaceChar(words));
        if (wordsPosList != null) {
            wordsPosList.add(wordsPosMap);
        }
        return marqueeFlag;
    }

}
