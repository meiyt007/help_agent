var common = {
	/* VUE_APP_BASE_API : '/dev-api',
	 VUE_APP_ZC_ROUTE_PATH :'/dzcpt-lss/zc',*/
	  /**
	   * 不定长参数，每三个参数为一个验证组<br/> 第一个参数：需要验证的控件ID<br/> 第二个参数：验证规则<br/> em：不能为空<br/>
	   * en：只能为英文字母<br/> ch：只能为汉字<br/> int：只能为int<br/> num：只能为数字，包括整数和浮点数<br/>
	   * pho：只能为电话<br/> mob：只能为手机<br/> pm：只能为电话或手机<br/> email：只能为email<br/>
	   * url：只能为url<br/> ic：只能为身份证号<br/> pwd：密码验证，以字母开头，长度在6-12之间，只能包含字符、数字和下划线<br/>
	   * rs：合法的字符串(a-zA-Z0-9-_)<br/> len：验证字符串长度，格式：（len:2-6），字符串长度必须在2-6之间<br/>
	   * csc:
	   * 第三个参数：验证不通过时，提示的信息
	   */
	  validControlParam : function() {
		  if (!arguments) {
			  return;
		  }
		  var errorMessage = new Array();
		  var emPos = 0;
		  for (var i = 0; i < arguments.length; i = i + 3) {
			  if (i + 3 > arguments) {
				  break;
			  }
			  // 获取验证组的值，控件ID、验证规则、提示信息
			  var controlId = arguments[i];
			  var validType = arguments[i + 1];
			  var tipMessage = arguments[i + 2];
  
			  var docCon = document.getElementById(controlId);
			  if (docCon == null) { // 如果控件ID不存在，不验证
				  continue;
			  }
			  var conValue = docCon.value;
			  conValue = conValue.trim();
  
			  if (this.validParam(conValue, validType)) {
				  errorMessage[emPos++] = tipMessage;
			  }
  
		  }
		  return errorMessage;
	  },
  
	  /**
	   * 不定长参数，每三个参数为一个验证组<br/> 第一个参数：需要验证的值<br/> 第二个参数：验证规则<br/> em：不能为空<br/>
	   * en：只能为英文字母<br/> ch：只能为汉字<br/> int：只能为int<br/> num：只能为数字，包括整数和浮点数<br/>
	   * pho：只能为电话<br/> mob：只能为手机<br/> pm：只能为电话或手机<br/> email：只能为email<br/>
	   * url：只能为url<br/> ic：只能为身份证号<br/> pwd：密码验证，以字母开头，长度在6-12之间，只能包含字符、数字和下划线<br/>
	   * rs：合法的字符串(a-zA-Z0-9-_)<br/> len：验证字符串长度，格式：（len:2-6），字符串长度必须在2-6之间<br/>
	   * mvn: 多个值不能同时为空<br/>
	   * sck:判断是否包含中英文特殊字符，除英文"-_"字符外<br/>
	   * zipcode:邮政编码<br/>
	   * 第三个参数：验证不通过时，提示的信息
	   */
	  validValueParam : function() {
		  if (!arguments) {
			  return;
		  }
		  var errorMessage = new Array();
		  var emPos = 0;
		  for (var i = 0; i < arguments.length; i = i + 3) {
			  if (i + 3 > arguments) {
				  break;
			  }
			  // 获取验证组的值，控件ID、验证规则、提示信息
			  var controlValue = arguments[i];
			  var validType = arguments[i + 1];
			  var tipMessage = arguments[i + 2];
  
			  var conValue = controlValue;
			  if (!controlValue) {
				  conValue = '';
			  }
			  if(validType != 'mvn') {
				  try {
					  conValue = conValue.trim(); // 去除空格
				  } catch(ex) {
					  conValue = '';
				  }
			  }
  
			  if (this.validParam(conValue, validType)) {
				  errorMessage[emPos++] = tipMessage;
			  }
		  }
		  return errorMessage;
	  },
	  /**
	   * 生成二维码共用方法
	   * 依赖jquery.js、qrcode.js
	   * https://github.com/davidshimjs/qrcodejs
	   */
	  qrcode : function(jqsel,options_){
		  var default_options = {
			  text : "",
			  width : 150,
			  height : 150,
			  colorDark : "#000000",
			  colorLight : "#ffffff",
			  correctLevel : QRCode.CorrectLevel.H
		  };
		  var options = $.extend(true, default_options, options_);
		  var qrcode = new QRCode($(jqsel)[0],options);
		  return qrcode;
	  },
	  /**
	   * 验证值是否符合规则
	   *
	   * @param conValue
	   *            需要验证的值
	   * @param validType
	   *            em：不能为空<br/> en：只能为英文字母<br/> ch：只能为汉字<br/>
	   *            int：只能为int<br/> int1：只能为int，必须大于0<br/>
	   *            num：只能为数字，包括整数和浮点数<br/> num1：只能为数字，包括整数和浮点数，必须大于0<br/>
	   *            pho：只能为电话<br/> mob：只能为手机<br/> mobOrNull phoOrNull
	   *            pm：只能为电话或手机<br/> email：只能为email<br/> url：只能为url<br/>
	   *            ic：只能为身份证号<br/> pwd：密码验证，以字母开头，长度在6-12之间，只能包含字符、数字和下划线<br/>
	   *            rs：合法的字符串(a-zA-Z0-9-_)<br/>
	   *            len：验证字符串长度，格式：（len:2-6），字符串长度必须在2-6之间<br/>
	   *            zipcode：验证邮政编码(/^[0-9]{6}$/)<br/>
	   *
	   */
	  validParam : function(conValue, validType) {
		  var vts = validType.split(":");
		  if (vts.length > 1) {
			  var vtType = vts[0];
			  var vtParam = vts[1];
			  switch (vtType) {
			  case 'len':
				  if (this.isEmpty(conValue)) {
					  return false;
				  }
				  var vtps = vtParam.split("-");
				  if (vtps.length == 1) {
					  if (!this.validStrLength(conValue, vtps[0])) {
						  return true;
					  }
				  } else if (vtps.length == 2) {
					  if (!this.validStrLength(conValue, vtps[1], vtps[0])) {
						  return true;
					  }
				  }
				  break;
			  }
		  } else {
			  switch (validType) {
			  case 'em':
				  if (this.isEmpty(conValue)) {
					  return true;
				  }
				  break;
			  case 'en':
				  if (!this.isEnglish(conValue)) {
					  return true;
				  }
				  break;
			  case 'ch':
				  if (!this.isChinese(conValue)) {
					  return true;
				  }
				  break;
			  case 'int':
				  if (!this.isInteger(conValue)) {
					  return true;
				  }
				  break;
			  case 'int1':
				  if (!this.isInteger(conValue)) {
					  return true;
				  }
				  if(parseInt(conValue) <= 0) {
					  return true;
				  }
				  break;
			  case 'int2':
				  if (!this.isInteger(conValue)) {
					  return true;
				  }
				  if(parseInt(conValue) < 0) {
					  return true;
				  }
				  break;
			  case 'num':
				  if (!this.isNumber(conValue)) {
					  return true;
				  }
				  break;
			  case 'num1':
				  if (!this.isNumber(conValue)) {
					  return true;
				  }
				  if(parseFloat(conValue) <= 0) {
					  return true;
				  }
				  break;
			  case 'pho':
				  if (!this.isPhone(conValue)) {
					  return true;
				  }
				  break;
			  case 'phoOrNull'://add by wangsh 20170608 号码为空时返回 true
  
				  if (!this.isPhone(conValue,true)) {
					  return true;
				  }
				  break;
			  case 'mob':
				  if (!this.isMobile(conValue)) {
					  return true;
				  }
				  break;
			  case 'mobOrNull'://add by wangsh 20170608 号码为空时返回 true
				  if (!this.isMobile(conValue,true)) {
					  return true;
				  }
				  break;
			  case 'pm':
				  if (!this.isMobile(conValue) && !this.isPhone(conValue)) {
					  return true;
				  }
				  break;
				  //邮政编码
			  case 'zipcode':
				  if (!this.isZipCode(conValue)) {
					  return true;
				  }
				  break;
			  case 'email':
				  if (!this.isEmail(conValue)) {
					  return true;
				  }
				  break;
			  case 'url':
				  if (!this.isUrl(conValue)) {
					  return true;
				  }
				  break;
			  case 'ic':
				  if (!this.isIdCardNo(conValue)) {
					  return true;
				  }
				  break;
			  case 'pwd':
				  if (!this.isPwd(conValue)) {
					  return true;
				  }
				  break;
			  case 'rs':
				  if (!this.isRightfulString(conValue)) {
					  return true;
				  }
				  break;
			  case 'unite':
				  if(!this.ifUniteCode(conValue)){
					  return true;
				  }
				  break;
			  case 'sck':
				  return this.isContainsSpecialChar(conValue);
				  /*if (!this.isContainsSpecialChar(conValue)) {
					  return true;
				  }*/
				  break;
			  case 'sten':
				  if (!this.isStartWithEnString(conValue)) {
					  return true;
				  }
				  break;
			  case 'mvn':
				  for(var i = 0; i < conValue.length; i++) {
					  if (!this.isEmpty(conValue[i])) {
						  return false;
					  }
				  }
				  return true;
			  }
		  }
		  return false;
	  },
  
	  /**
	   * 判断字符串str是否为空
	   */
	  isEmpty : function(str) {
		  return str == '' || str == null || str.trim() == '';
	  },
  
	  /**
	   * 验证字符串的长度是否在一定范围内
	   */
	  validStrLength : function(str, maxLength, minLength) {
		  if (str == null) {
			  return false;
		  }
		  if (maxLength && !this.isIntGtZero(maxLength)) {
			  return false;
		  }
		  if (minLength && !this.isIntGtZero(minLength)) {
			  return false;
		  }
		  str = str.trim();
		  var len = str.length;
		  // 如果maxLength有效，判断字符串长度是否超出范围
		  if (maxLength) {
			  if (len <= maxLength) { // 字符串长度未超过maxLength
				  if (minLength) { // 验证minLength是否有效
					  if (len >= minLength) { // 字符串长度大于minLength，验证通过
						  return true;
					  } else { // 字符串长度小于minLength，验证不通过
						  return false;
					  }
				  } else { // minLength无效，不验证
					  return true;
				  }
			  } else { // 字符串长度超过maxLength，验证不通过
				  return false;
			  }
		  } else { // maxLength无效，验证通过
			  return true;
		  }
	  },
  
	  /**
	   * 判断整数num是否等于0
	   */
	  isIntEqZero : function(num) {
		  return num == 0;
	  },
  
	  /**
	   * 判断整数num是否大于0
	   */
	  isIntGtZero : function(num) {
		  return num > 0;
	  },
  
	  /**
	   * 判断整数num是否大于或等于0
	   */
	  isIntGteZero : function(num) {
		  return num >= 0;
	  },
  
	  /**
	   * 判断浮点数num是否等于0
	   */
	  isFloatEqZero : function(num) {
		  return num == 0;
	  },
  
	  /**
	   * 判断浮点数num是否大于0
	   */
	  isFloatGtZero : function(num) {
		  return num > 0;
	  },
  
	  /**
	   * 判断浮点数num是否大于或等于0
	   */
	  isFloatGteZero : function(num) {
		  return num >= 0;
	  },
  
	  /**
	   * 匹配Email地址
	   */
	  isEmail : function(str) {
		  if (str == null || str == "")
			  return false;
		  var result = str.match(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/);
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  /**
	   * 判断数值类型，包括整数和浮点数
	   */
	  isNumber : function(str) {
		  if (this.isDouble(str) || this.isInteger(str))
			  return true;
		  return false;
	  },
  
	  /**
	   * 只能输入数字[0-9]
	   */
	  isDigits : function(str) {
		  if (str == null || str == "")
			  return false;
		  var result = str.match(/^\d+$/);
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  /**
	   * 匹配money
	   */
	  isMoney : function(str) {
		  if (str == null || str == "")
			  return false;
		  var result = str
				  .match(/^(([1-9]\d*)|(([0-9]{1}|[1-9]+)\.[0-9]{1,2}))$/);
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  /**
	   * 匹配phone
	   */
	  isPhone : function(str,canBeNull) {
		  //modified by wangsh 排除控制验证
  
		  if (str == null || str == ""){
			  if(canBeNull)return true;
			  else return false
		  }
		  ///^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{4,7}(\-\d{1,4})?$/
		  var result = str.match(/^(\(\d{3,4}\)|\d{3,4}-)?\d{7,14}$/);
  
		  if (result == null){
			  return false;
		  }
		  return true;
	  },
  
	  /**
	   * 匹配mobile
	   */
	  isMobile : function(str,canBeNull) {
		  //modified by wangsh 排除控制验证
		  if (str == null || str == ""){
			  if(canBeNull)return true;
			  else return false
		  }
		  //var result = str.match(/^((\(\d{2,3}\))|(\d{3}\-))?((13\d{9})|(15\d{9})|(18\d{9}))$/);
		  var result = str.match(/^1\d{10}$/);
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  /**
	   * 联系电话(手机/电话皆可)验证
	   */
	  isTel : function(tel) {
		  if (this.isMobile(tel) || this.isPhone(tel))
			  return true;
		  return false;
	  },
  
	  /**
	   * 匹配qq
	   */
	  isQq : function(str) {
		  if (str == null || str == "")
			  return false;
		  var result = str.match(/^[1-9]\d{4,12}$/);
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  /**
	   * 匹配english
	   */
	  isEnglish : function(str) {
		  if (str == null || str == "")
			  return false;
		  var result = str.match(/^[A-Za-z]+$/);
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  /**
	   * 匹配integer
	   */
	  isInteger : function(str) {
		  if (str == null || str == "")
			  return false;
		  var result = str.match(/^[-\+]?\d+$/);
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  /**
	   * 匹配double或float
	   */
	  isDouble : function(str) {
		  if (str == null || str == "")
			  return false;
		  var result = str.match(/^[-\+]?\d+(\.\d+)?$/);
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  /**
	   * 匹配邮政编码
	   */
	  isZipCode : function(str) {
		  if (str == null || str == "")
			  return false;
		  var result = str.match(/^[0-9]{6}$/);
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  /**
	   * 匹配URL
	   */
	  isUrl : function(str) {
		  if (str == null || str == "")
			  return false;
		  var result = str.match(/^[a-zA-z]+:\/\/[^\s]*$/);
				  //.match(/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\’:+!]*([^<>\"])*$/);
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  /**
	   * 匹配密码，以字母开头，长度在6-12之间，只能包含字符、数字和下划线。
	   */
	  isPwd : function(str) {
		  if (str == null || str == "")
			  return false;
		  //var result = str.match(/^[a-zA-Z]\w{5,11}$/);
		  //匹配密码 数字，字母，特殊字符 8-20位
		  var result = str.match(/^(?=.*[A-Za-z])(?=.*\d)(?=.*[\s\S])[A-Za-z\d(\s\S)]{8,20}$/)
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  /**
	   * 判断是否为合法字符(a-zA-Z0-9-_)
	   */
	  isRightfulString : function(str) {
		  if (str == null || str == "")
			  return false;
		  var result = str.match(/^[A-Za-z0-9_-]+$/);
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  ifUniteCode:function(str){
		  if(str == null || str == ""){
			  return false;
		  }
		  var result = str.match(/^[^_IOZSVa-z\W]{2}\d{6}[^_IOZSVa-z\W]{10}$/);
		  if (result == null)
			  return false;
		  return true;
	  },
	  /**
	   * 判断是否为英文字母开头的合法字符(^[a-zA-Z][a-zA-Z0-9_]*$)
	   */
	  isStartWithEnString : function(str) {
		  if (str == null || str == "")
			  return false;
		  var result = str.match(/^[a-zA-Z][a-zA-Z0-9_]*$/);
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  /**
	   * 匹配身份证号码
	   */
	  isIdCardNo : function(num) {
		  var len = num.length, reg;
		  if (len == 15) {
			  reg = new RegExp(/^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$/);
		  } else if (len == 18) {
			  reg = new RegExp(/^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/);
		  } else {
			  // alert("输入的数字位数不对。");
			  return false;
		  }
		  var r = num.match(reg);
		  if( r == null){
			  return false;
		  }
		  return true;
	  },
  
  
	  /**
	   * 匹配汉字
	   */
	  isChinese : function(str) {
		  if (str == null || str == "")
			  return false;
		  var result = str.match(/^[\u4e00-\u9fa5]+$/);
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  /**
	   * 匹配中文(包括汉字和字符)
	   */
	  isChineseChar : function(str) {
		  if (str == null || str == "")
			  return false;
		  var result = str.match(/^[\u0391-\uFFE5]+$/);
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  /**
	   * 字符验证，只能包含中文、英文、数字、下划线等字符。
	   */
	  stringCheck : function(str) {
		  if (str == null || str == "")
			  return false;
		  var result = str.match(/^[a-zA-Z0-9\u4e00-\u9fa5-_]+$/);
		  if (result == null)
			  return false;
		  return true;
	  },
  
	  /**
	   * 过滤中英文特殊字符，除英文"-_"字符外
	   */
	  stringFilter : function(str) {
		  var pattern = new RegExp(
				  "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");
		  var rs = "";
		  for (var i = 0; i < str.length; i++) {
			  rs = rs + str.substr(i, 1).replace(pattern, '');
		  }
		  return rs;
	  },
	  confirm : function(mess, href, closed){//确认对话框
		  layer.open({
				content: mess
				,title: '系统提示'
				,btn: ['确定', '取消']
				,yes: function(index, layero){
				  layer.close(index);
					if (typeof href == 'function') {
					  href();
				  }else{
					  location = href;
				  }
				},btn2: function(index, layero){
					layer.close(index);
				},cancel: function(){
				  //右上角关闭回调
				  if (typeof closed == 'function') {
					  closed();
				  }
				}
			  });
			  return false;
	  },
  
	  /**
	   * 判断是否包含中英文特殊字符，除英文"-_"字符外
	   */
	  isContainsSpecialChar : function(str) {
		  if (str == null || str == "")
			  return false;
		  var reg = RegExp(/[(\ )(\`)(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\*)(\()(\))(\+)(\=)(\|)(\{)(\})(\')(\:)(\;)(\')(',)(\[)(\])(\.)(\<)(\>)(\/)(\?)(\~)(\！)(\@)(\#)(\￥)(\%)(\…)(\&)(\*)(\（)(\）)(\—)(\+)(\|)(\{)(\})(\【)(\】)(\‘)(\；)(\：)(\”)(\“)(\’)(\。)(\，)(\、)(\？)]+/);
		  return reg.test(str);
	  },
	  /**
	   * 使用get方法进行Ajax请求
	   * @param url 请求地址
	   * @param params 请求参数
	   * @param sucFun 成功执行方法
	   * @param failFun 失败执行方法
	   * @param dataType 预期服务器返回的数据类型
	   * 	"xml": 返回 XML 文档，可用 jQuery 处理
	   * 	"html": 返回纯文本 HTML 信息；包含的 script 标签会在插入 dom 时执行
	   * 	"script": 返回纯文本 JavaScript 代码。不会自动缓存结果。除非设置了 "cache" 参数。注意：在远程请求时(不在同一个域下)，所有 POST 请求都将转为 GET 请求。（因为将使用 DOM 的 script标签来加载）
	   * 	"json": 返回 JSON 数据
	   * 	"text": 返回纯文本字符串
	   */
	  ajaxGet: function(url, params, sucFun, failFun, dataType, completeFun) {
		  this.ajax(url, 'get', params, sucFun, failFun, dataType, completeFun);
	  },
	  /**
	   * 使用post方法进行Ajax请求
	   * @param url 请求地址
	   * @param params 请求参数
	   * @param sucFun 成功执行方法
	   * @param failFun 失败执行方法
	   * @param dataType 预期服务器返回的数据类型
	   * 	"xml": 返回 XML 文档，可用 jQuery 处理
	   * 	"html": 返回纯文本 HTML 信息；包含的 script 标签会在插入 dom 时执行
	   * 	"script": 返回纯文本 JavaScript 代码。不会自动缓存结果。除非设置了 "cache" 参数。注意：在远程请求时(不在同一个域下)，所有 POST 请求都将转为 GET 请求。（因为将使用 DOM 的 script标签来加载）
	   * 	"json": 返回 JSON 数据
	   * 	"text": 返回纯文本字符串
	   */
	  ajaxPost: function(url, params, sucFun, failFun, dataType, completeFun,async) {
		  this.ajax(url, 'post', params, sucFun, failFun, dataType, completeFun,async);
	  },
	  /**
	   * 自定义方法（get、post）进行Ajax请求
	   * @param url 请求地址
	   * @param method 请求地址
	   * @param params 请求参数
	   * @param sucFun 成功执行方法
	   * @param failFun 失败执行方法
	   * @param dataType 预期服务器返回的数据类型
	   */
	  ajax: function(url, method, params, sucFun, failFun, dataType, completeFun,async) {
		  openMask();
		  if (typeof(async) == "undefined") {
			  async = true;
		  }
		  if (!failFun) {
			  failFun = function(data) {
				  try {
					  if(data.status == 530 ){
						  //Dialog.alert('请重新登录',function(){
							  window.location.reload();
						  //});
					  }else{
						  var mes = data.responseText;
						  mes = mes.replace("\r\n", "");
						  var mes = eval(mes);
						  Dialog.typeAlert(2,"未知错误:" + mes.message);
					  }
				  } catch(ex) {
					  var noPermission = $(data.responseText).find("#noPermission");
					  if(noPermission) {
						  mes = noPermission.text();
						  if(mes){
							  Dialog.typeAlert(2,mes);
						  }
					  } else {
						  mes = data.responseText;
						  Dialog.typeAlert(2,"未知错误:" + mes);
					  }
				  }
			  }
		  }
		  var innerCompleteFun = function(data){
			  closeMask();
			  if(completeFun) {
				  completeFun(data);
			  }
		  };	//内部完成方法
		  if(!dataType) {
			  dataType = 'json';
		  }
		  if(!params) {
			  params = {};
		  }
		  $.ajax({
			  url : url,
			  data : params,
			  type : method,
			  async:async,
			  dataType : dataType,
			  success : sucFun,
			  error :failFun,
			  complete : innerCompleteFun
		  });
	  },
	  alert : function(mess, beSure,beCancel){//成功操作提示
		  if(mess == null){
			  mess = "null";
		  }
		  layer.open({
				content: mess,
				title: '系统提示',
				btn: ['确定'],
				yes: function(index, layero){
					layer.close(index);
					if (typeof beSure == 'function') {
					  beSure();
					}
				},
				cancel:function(index, layero) {
					layer.close(index);
						if (typeof beCancel == 'function') {
						beCancel();
					}
				}
			  });
	  },
	  /**
	   * 清除控件的值，参数为控件的ID值，可传入多个控件ID
	   */
	  clearControlValue : function() {
		  if (!arguments) {
			  return;
		  }
		  for (var i = 0; i < arguments.length; i++) {
			  var argObj = $("#" + arguments[i]);
			  if(argObj) {
				  argObj.val('');
			  }
		  }
	  }
  
  }
  // 为字符串添加trim方法，去除左右空格
  String.prototype.trim = function() {
	  return this.replace(/(^\s*)|(\s*$)/g, "");
  }
  // 为字符串添加ltrim方法，去除左空格
  String.prototype.ltrim = function() {
	  return this.replace(/(^\s*)/g, "");
  }
  // 为字符串添加rtrim方法，去除右空格
  String.prototype.rtrim = function() {
	  return this.replace(/(\s*$)/g, "");
  }
  //数组去重
  Array.prototype.unique = function() {
	  var res = [];
	  var json = {};
	  for (var i = 0; i < this.length; i++) {
		  if (!json[this[i]]) {
			  res.push(this[i]);
			  json[this[i]] = 1;
		  }
	  }
	  return res;
  }
  Date.prototype.format = function(format) {
	  var o = {
		  "M+": this.getMonth() + 1,
		  "d+": this.getDate(),
		  "h+": this.getHours(),
		  "m+": this.getMinutes(),
		  "s+": this.getSeconds(),
		  "q+": Math.floor((this.getMonth() + 3) / 3),
		  "S": this.getMilliseconds()
	  }
	  if (/(y+)/.test(format)) {
		  format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	  }
	  for (var k in o) {
		  if (new RegExp("(" + k + ")").test(format)) {
			  format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		  }
	  }
	  return format;
  }
  /**
  * js中更改日期
  * part y年， m月， d日， h小时， n分钟，s秒
  * value 数值
  */
  Date.prototype.add = function(part, value) {
	  value *= 1;
	  if (isNaN(value)){
		  value = 0;
	  }
	  switch (part){
		  case "y":
			  this.setFullYear(this.getFullYear() + value);
			  break;
		  case "m":
			  this.setMonth(this.getMonth() + value);
			  break;
		  case "d":
			  this.setDate(this.getDate() + value);
			  break;
		  case "h":
			  this.setHours(this.getHours() + value);
			  break;
		  case "n":
			  this.setMinutes(this.getMinutes() + value);
			  break;
		  case "s":
			  this.setSeconds(this.getSeconds() + value);
			  break;
		  default:
	  }
  }
  
  Date.prototype.getDayOfWeek=function(){
	  var weekday=new Array(7);
	  weekday[0]="星期天";
	  weekday[1]="星期一";
	  weekday[2]="星期二";
	  weekday[3]="星期三";
	  weekday[4]="星期四";
	  weekday[5]="星期五";
	  weekday[6]="星期六";
	  return weekday[this.getDay()];
  }
  
  function openDialog(title, width, height, url, closeFunction,openMaxFlag) {
	  var diag = new Dialog();
	  if(!width || width == '' || width == 900) {
		  width = $($topWindow).width()*0.8;
	  }
	  diag.Width = width;
  
	  if(!height || height == '' || height == 400) {
		  height = $($topWindow).height()*0.8;
	  }
	  diag.Height = height;
	  if(openMaxFlag){
		  diag.DefaultMaxWindow = true;
	  }
	  diag.Title = title;
	  diag.URL = url;
	  diag.diagClose = closeFunction;
	  diag.show();
	  return diag;
  }
  var openNum = 0;	//遮罩打开次数
  var closeNum = 0;	//遮罩关闭次数
  function openMask() {
	  var topWin = $topWindow();
  
	  if(document.URL.indexOf('index.do') > -1)
		  return;
	  //1秒内不要连续弹出遮罩层 start
	  if(((new Date()).getTime() - topWin.nowDate) < 1000) {
		  topWin.nowDate = (new Date()).getTime();
		  return;
	  }
	  topWin.nowDate = (new Date()).getTime();
	  //1秒内不要连续弹出遮罩层 end
	  openNum++;
  
	  var bgMask = topWin.document.getElementById("fullMaskDiv");
	  if (bgMask) {
		  bgMask.style.display = "block";
	  }
  }
  //当flag为true时，不检查打开次数，否则检查打开次数
  function closeMask(flag) {
	  closeNum++;
	  var topWin = $topWindow();
	  var bgMask = topWin.document.getElementById("fullMaskDiv");
	  //当打开和关闭次数相同时，关闭遮罩
	  if (bgMask && (flag || closeNum >= openNum)) {
		  openNum = 0;
		  closeNum = 0;
		  bgMask.style.display = "none";
	  }
  }
  var $topWindow = function() {
	  var parentWin = window;
	  while (parentWin != parentWin.parent) {
		  if (parentWin.parent.document.getElementsByTagName("FRAMESET").length > 0) break;
		  parentWin = parentWin.parent;
	  }
	  return parentWin;
  };
  
  function getRootPath(){
	  //获取当前网址，如： http://localhost:8088/test/test.jsp
	  var curPath=window.document.location.href;
	  //获取主机地址之后的目录，如： test/test.jsp
	  var pathName=window.document.location.pathname;
	  var pos=curPath.indexOf(pathName);
	  //获取主机地址，如： http://localhost:8088
	  var localhostPaht=curPath.substring(0,pos);
	  //获取带"/"的项目名，如：/test
	  var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	  return(localhostPaht+projectName+'/');
  }
  function treeQueryEvent(txtId, treeId, fun){
	  if(txtId == '' || treeId == '') {
		  return;
	  }
	  if ( $("#" + txtId).length < 1 || $("#" + treeId).length < 1) {
		  return;
	  }
	  if (!fun) {
		  fun = function(treeId, txtId) {
			  var zTree = $.fn.zTree.getZTreeObj(treeId);
			  if($("#" + txtId).val()){
				  var nodeList = zTree.getNodesByParamFuzzy('name', $("#" + txtId).val());
				  var selectNodeList = new Array();
				  for (var i = 0; i < nodeList.length; i++) {
					  var node = nodeList[i];
					  selectNodeList.push(node);
					  while(node.getParentNode() != null) {
						  selectNodeList.push(node.getParentNode());
						  node = node.getParentNode();
					  }
				  }
				  var allNode = zTree.transformToArray(zTree.getNodes());
				  zTree.hideNodes(allNode);
				  zTree.showNodes(selectNodeList);
			  }else{
				  var allNode = zTree.transformToArray(zTree.getNodes());
				  zTree.showNodes(allNode);
			  }
		  };
	  }
	  var txtObject = $("#" + txtId);
	  //输入框的修改事件，在IE8、9下有BUG，无法监听到删除，使用定时器替代
	  if(txtObject) {
		  var objectInterval;
		  txtObject.on("focus", function(){
			  objectInterval = setInterval(function(){
				  fun(treeId, txtId);
			  }, 500);
		  });
		  txtObject.on("blur", function(){
			  clearInterval(objectInterval);
		  });
	  }
  }
  