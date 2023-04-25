package com.zfsoft.superwindow.util;

import com.ctc.wstx.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarqueeUtil {
    /** 选框分隔符 */
    public static final String MARQUEE_SPLIT_CHAR = "□";

    /**
     * 切割选择后的选框识别结果
     *
     * @param mvList
     *            原始选框值
     * @param selectMarqueeValue
     *            选择后的选框识别结果值
     * @param recognitionType
     *            识别类型，如果是手写体，最后一个加上字，否则只处理选中的值
     */
    public static List<String> cutSelectMarquee(List<String> mvList, String selectMarqueeValue,
                                                String recognitionType) {
        List<String> selectMarqueeList = new ArrayList<String>();
        if (mvList == null || mvList.size() < 1) {
            return selectMarqueeList;
        }
        if (selectMarqueeValue == null || selectMarqueeValue.equals("")) {
            return selectMarqueeList;
        }
        selectMarqueeValue = replaceChar(selectMarqueeValue);
        Map<String, Integer> selectMarqueeMap = new HashMap<String, Integer>();

        // 对于字符串存在包含关系的处理
        Map<String, List<String>> conMap = new HashMap<String, List<String>>();
        for (int i = 0; i < mvList.size(); i++) {
            List<String> conList = new ArrayList<String>();
            for (int j = 0; j < mvList.size(); j++) {
                if (i != j) {
                    if (mvList.get(j).contains(mvList.get(i))) {
                        conList.add(mvList.get(j));
                    }
                }
            }
            if (conList.size() > 0) {
                conMap.put(mvList.get(i), conList);
            }
        }

        for (String mv : mvList) {
            if(StringUtils.isBlank(mv)){
                continue;
            }
          /*  if (StrUtil.isBlank(mv)) {
                continue;
            }*/
            if (conMap.containsKey(mv)) {
                String smv = selectMarqueeValue;
                // 选项存在包含关系的处理
                List<String> conList = conMap.get(mv);
                int pos = 0;
                for (int i = 0; i < conList.size() + 1; i++) {
                    pos = pos + smv.indexOf(mv);
                    boolean flag = true;
                    // 存在包含关系时，验证字符串出现的位置，最终确定是否选中
                    for (String con : conList) {
                        int p = selectMarqueeValue.indexOf(con);
                        if (pos == p) {
                            flag = false;
                            continue;
                        }
                    }
                    if (flag) {
                        // 验证选择项是否在目标字符串中，并确定位置
                        if (pos > 0) {
                            String valid = selectMarqueeValue.substring(pos - 1, pos);
                            if (!valid.equals(MARQUEE_SPLIT_CHAR)) {
                                selectMarqueeMap.put(mv, pos);
                            }
                        } else if (pos == 0) {
                            selectMarqueeMap.put(mv, pos);
                        }
                        break;
                    }
                    smv = smv.substring(++pos);
                }
            } else {
                // 选项不存在包含关系的处理
                int pos = selectMarqueeValue.indexOf(mv);
                // 验证选择项是否在目标字符串中，并确定位置
                if (pos > 0) {
                    String valid = selectMarqueeValue.substring(pos - 1, pos);
                    if (!valid.equals(MARQUEE_SPLIT_CHAR)) {
                        selectMarqueeMap.put(mv, pos);
                    }
                } else if (pos == 0) {
                    selectMarqueeMap.put(mv, pos);
                }
            }
        }
        for (String selectMv : selectMarqueeMap.keySet()) {
            for (int j = 0; j < mvList.size(); j++) {
                String mv = mvList.get(j);
                if (selectMv.equals(mv)) {
                    if (FaStaticParam.RECOGNITION_TYPE_MARQUEE_SX.equals(recognitionType)) {
                        // 当最后一个存在手写时，可以识别，否则无法识别手写
                        int beginPos = selectMarqueeMap.get(selectMv);
                        if (j >= mvList.size() - 1) {
                            selectMarqueeList.add(selectMarqueeValue.substring(beginPos));
                        } else {
                            selectMarqueeList.add(selectMv);
                        }
                    } else {
                        // 印刷体，不识别最后的手写体部分
                        selectMarqueeList.add(selectMv);
                    }
                    break;
                }
            }
        }
        return selectMarqueeList;
    }

    /**
     * 对选框内容进行分割，分割字符串为：口或囗
     *
     * @param marqueeValue
     *            选框识别内容
     */
    public static List<String> cutMarquee(String marqueeValue) {
        List<String> mvList = new ArrayList<String>();
        if (marqueeValue == null || marqueeValue.equals("")) {
            return mvList;
        }
        marqueeValue = replaceChar(marqueeValue);
        String[] mvs = marqueeValue.split("[" + MARQUEE_SPLIT_CHAR + "]");
        for (String mv : mvs) {
            if (mv == null || mv.trim().equals("")) {
                continue;
            }
            int len = mv.length();
            if (mv.lastIndexOf("(") == len - 1) {
                // 如果最后一个为左括号，直接删除
                mv = mv.substring(0, len - 1);
            } else if (mv.lastIndexOf(")") == len - 1) {
                // 如果最后一个为右括号，如果存在左括号，不处理，如果不存在左括号，删除有括号
                if (mv.indexOf("(") < 0) {
                    mv = mv.substring(0, len - 1);
                }
            }
            mvList.add(mv);
        }
        return mvList;
    }

    /**
     * 处理选框值
     *
     * @param marqueeJsonValue
     *            选框原始值
     * @param words
     *            已填写后的值
     * @param recognitionType
     *            识别类型，如果是手写体，最后一个加上字，否则只处理选中的值
     * @return 处理后的值
     */
    @SuppressWarnings("unchecked")
    public static String handleMarqueeValue(String marqueeJsonValue, String words, String recognitionType) {
        List<String> mvList = JsonUtil.jsonToCollection(marqueeJsonValue, String.class);
        List<String> selectMvList = MarqueeUtil.cutSelectMarquee(mvList, words, recognitionType);
        String selectMv = "";
        if (selectMvList != null && selectMvList.size() > 0) {
            for (String str : selectMvList) {
                selectMv += str + ", ";
            }
            selectMv = selectMv.substring(0, selectMv.length() - 2);
        }
        return selectMv;
    }

    /**
     * 替换字符，包含：，（，），囗，口，并去除文字中的空格
     *
     * @param marqueeValue
     *            替换的字符串
     * @return 替换结果
     */
    public static String replaceChar(String marqueeValue) {
        return marqueeValue.replace("，", ",").replace("（", "(").replace("）", ")").replace("囗", MARQUEE_SPLIT_CHAR)
                .replace("口", MARQUEE_SPLIT_CHAR).replace(" ", "");
    }
}
