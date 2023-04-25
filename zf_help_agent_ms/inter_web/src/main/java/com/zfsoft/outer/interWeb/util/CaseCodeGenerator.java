package com.zfsoft.outer.interWeb.util;

import java.util.Random;

/**
 * CaseCode生成器
 */
public final class CaseCodeGenerator {

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static String randomOID(int numberDigit, int encryptedDigit) {
        if (numberDigit < 2) {
            throw new IllegalArgumentException("生成编号总位数过小。");
        }
        if (encryptedDigit < 1) {
            throw new IllegalArgumentException("转换位数过小。");
        }
        if (numberDigit / 2 - 1 < encryptedDigit) {
            throw new IllegalArgumentException("加密位数过大。");
        }

        String headMaxValue = getMaxValueWithDigit(encryptedDigit);
        int headRandomInt = RANDOM.nextInt(Integer.valueOf(headMaxValue));
        String headRandomStr = polish(headRandomInt, encryptedDigit);
        String headBinaryString = Integer.toBinaryString(Integer.valueOf(headRandomStr));
        char[] tailChars = new char[headBinaryString.length()];
        for (int i = 0; i < headBinaryString.length(); i++) {
            char charAt = headBinaryString.charAt(i);
            tailChars[i] = charAt == '0' ? '1' : '0';
        }
        String tailbinaryString = new String(tailChars);
        int tailbinaryInt = Integer.parseInt(tailbinaryString, 2);
        String tailRandomStr = String.valueOf(tailbinaryInt);
        if (tailRandomStr.length() < 2)
            tailRandomStr = polish(tailbinaryInt, 2);
        // 剩余位数
        int innerRandomDigit = numberDigit - headRandomStr.length() - tailRandomStr.length();
        String innerRandomStr = "";
        if (innerRandomDigit > 0) {
            String innerRandomMaxValue = getMaxValueWithDigit(innerRandomDigit);
            int innerRandom = RANDOM.nextInt(Integer.valueOf(innerRandomMaxValue));
            innerRandomStr = polish(innerRandom, innerRandomDigit);
        }
        String serviceRecordOid = headRandomStr + innerRandomStr + tailRandomStr;
        return serviceRecordOid;
    }

    private static String getMaxValueWithDigit(int digit) {
        String maxValue = "";
        for (int i = 0; i < digit; i++) {
            maxValue += "9";
        }
        return maxValue;
    }

    /**
     * <b>Description:</b>补位，0填充<br>
     *
     * @param randomNumber 需补位数字
     * @param digit        位数
     * @return
     * @Note <b>Author:</b>WuQiang <br>
     * <b>Date:</b> 2019年4月15日 下午4:39:23
     */
    private static String polish(int randomNumber, int digit) {
        String numberStr = String.valueOf(randomNumber);
        if (numberStr.length() < digit) {
            int count = digit - numberStr.length();
            for (int i = 0; i < count; i++) {
                numberStr = "0" + numberStr;
            }
        }
        return numberStr;
    }

    /**
     * <b>Description:</b>办件编号规则验证<br>
     *
     * @param caseCode        办件编号
     * @param encryptedDigits 加密位数
     * @return true/false
     * @throws NullPointerException
     * @throws IllegalArgumentException
     * @Note <p>
     * 将办件编号({@link caseCode})前几位({@link encryptedDigits}
     * )位转为二进制，将得到的二进制进行0/1互换，转为十进制后判断办件编号({@link caseCode}
     * )最后位是否包含
     * <p>
     * <b>示例：
     * <p>
     * 办件编号 caseCode : 376665127891329
     * <p>
     * 加密位数 encryptedDigits : 4
     * <p>
     * 得到HEAD : 3766
     * <p>
     * 转为二进制 : 111010110110
     * <p>
     * 进行0/1互换 : 000101001001
     * <p>
     * 转为二进制 : 329
     * <p>
     * caseCode : 376665127891329 包含 329，符合验证规则
     * <p>
     * <b>Author:</b> WuQiang <br>
     * <b>Date:</b> 2019年4月15日 下午5:42:51
     */
    public static boolean validate(String caseCode, int encryptedDigits)
            throws NullPointerException, IllegalArgumentException {
        if (caseCode == null || "".equals(caseCode)) {
            throw new NullPointerException("service record oid is empty.");
        }
        if (caseCode.length() != 15) {
            throw new IllegalArgumentException("service record oid must be 15 digit.");
        }
        if (encryptedDigits < 1) {
            throw new IllegalArgumentException("encrypted digits must greater than 1.");
        }
        if (caseCode.length() / 2 - 1 < encryptedDigits) {
            throw new IllegalArgumentException("encrypted digits greater than half of service record oid length.");
        }
        String headStr = caseCode.substring(0, encryptedDigits);
        String headBinaryString = Integer.toBinaryString(Integer.valueOf(headStr));
        char[] tailChars = new char[headBinaryString.length()];
        for (int i = 0; i < headBinaryString.length(); i++) {
            char charAt = headBinaryString.charAt(i);
            tailChars[i] = charAt == '0' ? '1' : '0';
        }
        String tailBinaryString = new String(tailChars);
        int tailBinaryInt = Integer.parseInt(tailBinaryString, 2);
        return caseCode.endsWith(String.valueOf(tailBinaryInt));
    }
}

