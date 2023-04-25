package com.zfsoft.superwindow.util;

/**
 * @author   wangwg
 * @date 2021-01-22
 */
public enum DocuTemplatEnum {
    /**
     * caseAccept受理通知书   notAccepted不予受理通知书  rqbzAccept容缺受理通知书
     * SSMBQD应用类型-实施模板清单  YJSBB应用类型-件事模板
     * SLTXS通知书类型-受理通知书   BYSLTZS通知书类型-不予受理通知书  RQSLTZS通知书类型-容缺受理通知书
     */

    CASE_ACCEPT("SSMBQD", "SLTXS", "caseAccept"),
    NOT_ACCEPT("SSMBQD", "BYSLTZS", "notAccepted"),
    CASE_RQSL("SSMBQD","RQSLTZS","rqbzAccept"),
    CASE_GZCNS("SSMBQD","GZCNS","gzcnsAccept"),
    ONTHING_RQBZ_ACCEPT("YJSBB", "RQSLTZS", "onethingRqbzAccept"),
    ONTHING_NOT_ACCEPT("YJSBB", "BYSLTZS", "onethingNotAccept");

    /**
     * 获取文书模板的名称
     *
     * @param appCode
     * @param docTypeCode
     * @return
     */
    public static  String getTemplateFileName(String appCode,String docTypeCode){
        String filename=null;
        for (DocuTemplatEnum value : DocuTemplatEnum.values()) {
           if(value.getAppCode().equals(appCode) &&value.getDocTypeCode().equals(docTypeCode)){
                 filename = value.getTemplatName();
                 break;
           }
        }
        return filename;
    }


    DocuTemplatEnum(String appCode, String docTypeCode, String templatName) {
        this.appCode = appCode;
        this.docTypeCode = docTypeCode;
        this.templatName = templatName;
    }

    private String appCode;
    private String docTypeCode;
    private String templatName;


    public String getAppCode() {
        return appCode;
    }

    public String getDocTypeCode() {
        return docTypeCode;
    }

    public String getTemplatName() {
        return templatName;
    }
}
