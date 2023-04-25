<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/include/header.inc"%>
<link rel="stylesheet" href="${ctxPath }css/member_data.css">
<link rel="stylesheet" href="${ctxPath }js/plugins/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
end85889093
<link rel="stylesheet" href="${ctxPath }css/addIcon_demo.css">
<script type="text/javascript" src="${ctxPath }js/plugins/ztree/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="${ctxPath }js/plugins/ztree/jquery.ztree.exhide.min.js"></script>
<script src="${ctxPath }js/plugins/suggest/bootstrap-suggest.js"></script>
<script type='text/javascript' src='${ctxPath }js/plugins/plupload/plupload.js'></script>
<script  type="text/javascript" src="${ctxPath }js/plugins/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(function(){
		$("#myTab").on("click", "li", function(){
			tabSwitch(this);
		});
		/*默认打开第一个选项卡*/
		var firstLi = $("#myTab").children(":first");
		firstLi.attr("class", "active");
		firstLi.click();
	});
	function tabSwitch(obj) {
		$("#myTabFrame").children("div").each(function(){
			if($(this).attr("id") == $(obj).attr("role")) {
				$(this).css("display", "block");
				$(this).css("visibility", "visible");
			} else {
				$(this).css("display", "none");
				$(this).css("visibility", "hidden");
			}
		});
	}

	function linkHandle(type) {
		/*前端验证*/
		${form.volidValueJs }
		var oid = '${linkPerson.oid }';
		var nextUrl = "${ctxPath }workflow/task/nextNode.do?linkPersonOid="
			+ oid + "&type=" + type;
		var diag = openDialog('环节选择', 900, 300, nextUrl, function(flag, message, name, value) {
			diag.close();
			if (flag) {
				var url = "${ctxPath }workflow/task/next.do?linkPersonOid=" + oid;
				if (message) {
					url += "&nextStepOids=" + message;
				}
				if (type == "2") {
					url = "${ctxPath }workflow/task/fallback.do?linkPersonOid=" + oid;
					if (message) {
						url += "&fallbackStepOid=" + message;
					}
				} else if (type == "3") {
					url = "${ctxPath }workflow/task/reject.do?linkPersonOid=" + oid;
					if (message) {
						url += "&rejectStepOid=" + message;
					}
				}
				var postData = {};
				/* if(name && value) {
					postData = {"name" : name, "value" : value};
				} 
				postData.formInfo = JSON.stringify(formData);*/
				common.ajaxPost(url, $('#inputForm').serialize(), function(data) {
					var mes = eval(data);
					if (mes.success) {
						Dialog.alert("办理成功！", function() {
							ownerDialog.diagClose(true);
						});
					} else {
						Dialog.alert(mes.data);
					}
				}); 
			}
		});
	}
	function viewSelect(formName, id, oid) {
		var diag = new Dialog(900, 600, "视图数据选择");
		var columnName = formName + "_";
		diag.URL = "${ctxPath }form/view/preview.do?oid=" + oid
				+ "&operatFlag=Y";
		diag.show();
		diag.callBack = function(data) {
			$("#"+id+"").click(
					function() {
						var cName = this.name.substring(columnName.length,
								this.name.length + 1);
						if(cName.length > 32){
							cName = cName.substring(0, cName.length - 33);
						}
						if (typeof (data[cName]) != 'undefined'
								&& data[cName] != '')
							this.value = data[cName];
					});
			$("#"+id+"").trigger("click");
			$("#"+id+"").unbind();
			diag.close();
		};
	}
	$ (function () {
	    common.ajaxPost ('common/districtTree.do', null, function (data) {
	    	if($("#districtTree")){
	    		var districtId=$("input[name='districtName']").attr("id");
		    txtBindTree ('districtTree', eval (data.districtJson), districtId, 'districtOid', 'clearDistrictTree',
		            function (treeNode) {
		    	       if($("#sysOrganTree")){
			            common.clearControlValue('organName', 'organOid');
			            initOrgan (treeNode.id);
		    	       }
		            });
	    	}
	    });
	    if($("#districtTree")){
	    	initOrgan ('${param.districtOid }');
	    } 
	    if ($ ("#clearDistrictTree")){
	    	var districtId=$("input[name='districtName']").attr("id");
		    $ ("#clearDistrictTree").click (function () {
		    	  if($("#sysOrganTree")){
			    	common.clearControlValue (districtId, 'districtOid', 'organName', 'organOid');
			    	initOrgan ('');
		    	  }else{
		    		  common.clearControlValue (districtId, 'districtOid');
		    	  }
		    });
		    treeQueryEvent("districtTreeQuery", "districtTree");
		    treeQueryEvent("sysOrganTreeQuery", "sysOrganTree");
	    }
	});
	function initOrgan (districtOid) {
	    common.ajaxPost ('common/organTree.do', { districtOid : districtOid }, function (data) {
		    txtBindTree ('sysOrganTree', eval (data.sysOrganJson), 'organName', 'organOid', 'clearOrganTree');
	    })
	}
	$(function() {
		/*表单设计与基本平台样式不一致，折衷处理*/
		$('table').each(function() {
			/*为每一个table添加样式*/
			$(this).addClass("table-bordereds");
		});
		$('table tbody tr').each(function() {
			/*为每一个tr下的td添加样式*/
			$(this).children('td:odd').each(function() {
				$(this).addClass("table_text");
			});
			$(this).children('td:even').each(function() {
				$(this).addClass("table_header");
			});
		});
	});
	function addTab(subFormId,subFormVersionId) {
		 htmlobj=$.ajax({url:"${ctxPath}form/getSubFormHtml.do",async:false,data:{'subFormId':subFormId,'subFormVersionId':subFormVersionId}});
		 $("#childTab").append(htmlobj.responseText);
		 $('table').each(function() { /*为每一个table添加样式*/
				$(this).addClass("table-bordereds");
			});
		 $('table tbody tr').each(function() { /*为每一个tr下的td添加样式*/
				$(this).children('td:odd').each(function() {
					$(this).addClass("table_text");
				});
				$(this).children('td:even').each(function() {
					$(this).addClass("table_header");
				});
			});
	}
	function delTab(obj) {
		var len = $(obj).parent().siblings().length;
		if(len){
				$(obj).parent().remove();	
		}
	}
	function deleteAtta (obj, attaOid) { 
		var deleteRequestUrl = '${ctxPath }form/atta/deleteFile.do';
	  	common.ajaxPost (deleteRequestUrl, { attaOid : attaOid },
	  			function (data) {
	 				var mes = eval (data);
	 		 		if (mes.success){  
	 		 			Dialog.alert (mes.data,
	 		 				function(){
	  							$ (obj).parents ('tr').remove ();
								location.reload();
	 							}); 
	 		 		}else { 
	 						Dialog.alert (mes.data); 
	 					} 
	 		 		});
	}
	function downLoad(oid){ 
		location.href='${ctxPath }atta/downloadFile.do?attaOid='+oid; 
	}
	$(function() {
		var ids = new Array();
		${form.unUploadAtta}
		$.each(ids,function(i, n) {
							var self = this.toString();
							var uploader = new plupload.Uploader(
									{
										runtimes : 'html5,flash,silverlight',
										flash_swf_url : '${ctxPath1 }/js/plugins/plupload/Moxie.swf',
										silverlight_xap_url : '${ctxPath1 }/js/plugins/plupload/Moxie.xap',
										url : '${ctxPath }atta/uploadchunkFile.do',
										multi_selection : false,
										max_file_size : '1gb',
										chunk_size : '10mb',
										unique_names : true,
										browse_button : self,
										init : {
											FilesAdded : function(up, files) {
												openMask();
												up.start();
												return false;
											},
											FileUploaded : function(up, file,
													info) {
												var respContent = $
														.parseJSON(info.response);
												var headerOid = respContent.message.oid;
												if (respContent.status) {
													var fileObj = respContent.message;
													var fileName = fileObj.originName;
													var name = self.replace(
															'upload',
															'fileName');
													$('#' + name).val(
															fileObj.originName);
													var attaOid = self
															.replace('upload',
																	'attaOid');
													$('#' + attaOid).val(
															fileObj.oid);
												}
											},
											UploadComplete : function(uploader,
													files) {
												closeMask();
											}
										}
									});
							uploader.init();
						});
	});
	end85889093
</script>
<script type="text/javascript">
<c:forEach items="${form.htmlList}" var="htmlVo" varStatus="sta">
<c:out value='${htmlVo.scripts }' escapeXml="false"/>
</c:forEach>
</script>
<body>
	<c:out value='${form.objects}' escapeXml="false" />
	<div id="man-open1">
		<div id="manage">
<!-- 拼接form -->
end85889093
	<input type="hidden" name="token" value="${token }" />
	<input type="hidden" name="workflowInstanceOid" value="${linkPerson.workflowHandleLink.handleOid }" />
	<input type="hidden" name="bussOid" value="${bussInfo.oid }" />
	<c:forEach items="${form.htmlList}" var="htmlVo" varStatus="sta">
		<c:out value='${htmlVo.formHidden }' escapeXml="false"/>
	</c:forEach>
			<div class="pull-wrap">
				<table class="table table-bordereds">
					<tr>
						<td class="table_header">名称：</td>
						<td class="table_text"><c:out value='${bussInfo.name }' /></td>
						<td class="table_header">办理时限：</td>
						<td class="table_text"><c:out value='${bussInfo.timeLimit }' /></td>
					</tr>
					<tr>
						<td class="table_header">所属流程类型：</td>
						<td class="table_text"><c:out value='${bussInfo.workflowType.name }' /></td>
						<td class="table_header">新增时间：</td>
						<td class="table_text"><fmt:formatDate value="${bussInfo.createDate }" pattern="yyyy-MM-dd" /></td>
					</tr>
				</table>
			</div>
			<div class="memberData">
				<ul class="nav nav-tabs" role="tablist" id="myTab">
					<c:forEach items="${form.htmlList}" var="htmlVo" varStatus ="sta" >
						<li role="<c:out value='${htmlVo.tableOid }'/>">
								<a role="tab" data-toggle="tab"><c:out value='${htmlVo.disPlayName }'/></a>
						</li>
				   </c:forEach>
				</ul>
				<div class="tab-content" id="myTabFrame">
				<c:forEach items="${form.htmlList}" var="htmlVo" varStatus ="sta" >
						<div role="tabpanel" class="tab-pane" id="<c:out value='${htmlVo.tableOid }'/>">
						<c:out value='${htmlVo.tableContent }' escapeXml="false"/>
						<c:out value='${htmlVo.subTableContents }' escapeXml="false"/>
						</div>
					</c:forEach>
				</div>
			</div>
			<div class="work-list"></div>
			<div class="layui-layer-btn">
				end85889093
				<c:if test="${empty buttonShow}">
				<a onclick="linkHandle('1')" class="layui-layer-btn2">下一步</a>
				<a onclick="linkHandle('2')" class="layui-layer-btn2">退回</a>
				<a onclick="linkHandle('3')" class="layui-layer-btn2">驳回</a>
				<a onclick="ownerDialog.diagClose()" class="layui-layer-btn1">关闭</a>
				</c:if>
			</div>
			</form>
		</div>
	</div>
</body>