/**
 * 控件类型枚举
 */
let ELEMENTTYPE;
(function(ELEMENTTYPE) {
  /**文本框 */
  ELEMENTTYPE[ELEMENTTYPE["INPUT"] = 1] = "INPUT";
  /** 文本域 */
  ELEMENTTYPE[ELEMENTTYPE["TEXTAREA"] = 2] = "TEXTAREA";
  /** 下拉菜单  */
  ELEMENTTYPE[ELEMENTTYPE["SELECT"] = 3] = "SELECT";
  /** 单选框  */
  ELEMENTTYPE[ELEMENTTYPE["RADIO"] = 4] = "RADIO";
  /** 复选框  */
  ELEMENTTYPE[ELEMENTTYPE["CHECKBOX"] = 5] = "CHECKBOX";
  /** 时间控件  */
  ELEMENTTYPE[ELEMENTTYPE["DATEPICKER"] = 6] = "DATEPICKER";
  /** 附件控件  */
  ELEMENTTYPE[ELEMENTTYPE["ENCLOSURE"] = 7] = "ENCLOSURE";
  /** 图片控件  */
  ELEMENTTYPE[ELEMENTTYPE["IMAGEUPLOADER"] = 8] = "IMAGEUPLOADER";
  /** 隐藏控件  */
  ELEMENTTYPE[ELEMENTTYPE["HIDDEN"] = 9] = "HIDDEN";
  /** 下拉多选控件  */
  ELEMENTTYPE[ELEMENTTYPE["MORESELECT"] = 10] = "MORESELECT";
})(ELEMENTTYPE || (ELEMENTTYPE = {}));

/**特殊类型验证枚举 */
let REGTYPE;
(function(REGTYPE) {
  /**身份证验证 */
  REGTYPE[REGTYPE["IDCARD"] = '1'] = "IDCARD";
  /**手机号验证 */
  REGTYPE[REGTYPE["PHONE"] = '2'] = "PHONE";
  /**邮箱验证 */
  REGTYPE[REGTYPE["EMAIL"] = '3'] = "EMAIL";
  /**座机号验证 */
  REGTYPE[REGTYPE["LANDLINE"] = '4'] = "LANDLINE";
  /**电话号码验证  手机号或座机号 */
  REGTYPE[REGTYPE["TELPHONE"] = '5'] = "TELPHONE";
  /**邮政编码验证 */
  REGTYPE[REGTYPE["POSTCODE"] = '6'] = "POSTCODE";
})(REGTYPE || (REGTYPE = {}));

module.exports = {ELEMENTTYPE:ELEMENTTYPE,REGTYPE:REGTYPE}
