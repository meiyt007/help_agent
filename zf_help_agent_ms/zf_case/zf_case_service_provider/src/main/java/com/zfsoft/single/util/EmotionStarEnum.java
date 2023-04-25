package com.zfsoft.single.util;

/**
 * 情绪分析星级枚举
 */
public enum EmotionStarEnum {
    ONE_STAR_1(1,"disgust"),ONE_STAR_2(1,"fear"),ONE_STAR_3(1,"sad"),
    TWO_STAR(2,"angre"), THREE_STAR(3,"neutral"),
    FOUR_STAR(4,"surprise"), FIVE_STAR(5,"happy");

    /**
     * 根据表情返回星值
     * @param emotion
     * @return
     */
    public static int getStar(String emotion){
        int star = 0;
        for (EmotionStarEnum value : EmotionStarEnum.values()) {
            if(value.getEmotion().equals(emotion)){
                star = value.getStarNum();
                break;
            }
        }
        return star;
    }

    EmotionStarEnum(Integer starNum, String emotion) {
        this.starNum = starNum;
        this.emotion = emotion;
    }

    private Integer starNum;
    private String emotion;

    public Integer getStarNum() {
        return starNum;
    }

    public void setStarNum(Integer starNum) {
        this.starNum = starNum;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }
}
