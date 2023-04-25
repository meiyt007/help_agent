<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/header.inc"%>end85889093
<link rel="stylesheet" href="${ctxPath }css/member_data.css">
<link rel="stylesheet"
	href="${ctxPath }js/plugins/ztree/zTreeStyle/zTreeStyle.css"
	type="text/css">
end85889093
<script type="text/javascript"
	src="${ctxPath }js/plugins/ztree/jquery.ztree.all.min.js"></script>
<script src="${ctxPath }js/plugins/suggest/bootstrap-suggest.js"></script>
<script type="text/javascript" src="${ctxPath }js/plugins/ztree/jquery.ztree.exhide.min.js"></script>
<script type="text/javascript" src="${ctxPath }js/plugins/plupload/plupload.js"></script>
<script type="text/javascript"
	src="${ctxPath }js/plugins/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(function() {
		$("#myTab").on("click", "li", function() {
			tabSwitch(this);
		});
		/**默认打开第一个选项卡**/
		var firstLi = $("#myTab").children(":first");
		firstLi.attr("class", "active");
		firstLi.click();
	});
	function tabSwitch(obj) {
		$("#myTabFrame").children("div").each(function() {
			if ($(this).attr("id") == $(obj).attr("role")) {
				$(this).css("display", "block");
				$(this).css("visibility", "visible");
			} else {
				$(this).css("display", "none");
				$(this).css("visibility", "hidden");
			}
		});
	}
	function startFlow() {
		var mes = null;
		volidValueJs
		$.ajax({
			url : $('#inputForm').attr("action"),
			type : 'post',
			async : true,
			data : $('#inputForm').serialize(),
			success : function(data) {
				mes = eval(data);
				if (mes.success) {
					Dialog.alert("启动成功！", function() {
						ownerDialog.diagClose();
					});
				} else {
					Dialog.alert(mes.data);
				}
			},
			fail : function(data) {
				mes = eval(data);
			}
		});
		return mes;
	}
	function viewSelect(formName, oid) {
		var diag = new Dialog(900, 600, "视图数据选择");
		var columnName = formName + "_";
		diag.URL = "${ctxPath }form/view/preview.do?oid=" + oid
				+ "&operatFlag=Y";
		diag.show();
		diag.callBack = function(data) {
			$("input:text").each(
					function() {
						var cName = this.name.substring(columnName.length,
								this.name.length + 1);
						if (typeof (data[cName]) != 'undefined'
								&& data[cName] != '')
							this.value = data[cName];
					});
			diag.close();
		};
	}
	$(function() {
		common.ajaxPost('common/districtTree.do', null, function(data) {
			if ($("#districtTree")) {
				var districtId = $("input[name='districtName']").attr("id");
				txtBindTree('districtTree', eval(data.districtJson),
						districtId, 'districtOid', 'clearDistrictTree',
						function(treeNode) {
							if ($("#sysOrganTree")) {
								common.clearControlValue('organName',
										'organOid');
								initOrgan(treeNode.id);
							}
						});
			}
		});
		if ($("#districtTree")) {
			initOrgan('${param.districtOid }');
		}
		if ($("#clearDistrictTree")) {
			var districtId = $("input[name='districtName']").attr("id");
			$("#clearDistrictTree")
					.click(
							function() {
								if ($("#sysOrganTree")) {
									common.clearControlValue(districtId,
											'districtOid', 'organName',
											'organOid');
									initOrgan('');
								} else {
									common.clearControlValue(districtId,
											'districtOid');
								}
							});
			treeQueryEvent("districtTreeQuery", "districtTree");
		    treeQueryEvent("sysOrganTreeQuery", "sysOrganTree");
		}	    
	});
	function initOrgan(districtOid) {
		common.ajaxPost('common/organTree.do', {
			districtOid : districtOid
		}, function(data) {
			txtBindTree('sysOrganTree', eval(data.sysOrganJson), 'organName',
					'organOid', 'clearOrganTree');
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
	// upload
</script>
end85889093
<body>
	<div id="man-open1">
		<div id="manage">
			end85889093
			<!-- 拼接form -->

			<div class="pull-wrap">
				<table class="table table-bordereds">
					<tr>
						<td class="table_header">名称：</td>
						<td class="table_text"><c:out value='${bussInfo.name }' /></td>
						<td class="table_header">办理时限：</td>
						<td class="table_text"><c:out value='${bussInfo.timeLimit }' />
						</td>
					</tr>
					<tr>
						<td class="table_header">所属流程类型：</td>
						<td class="table_text"><c:out
								value='${bussInfo.workflowType.name }' /></td>
						<td class="table_header">新增时间：</td>
						<td class="table_text"><fmt:formatDate
								value="${bussInfo.createDate }" pattern="yyyy-MM-dd" /></td>
					</tr>
				</table>
			</div>
			end85889093
			<div class="memberData">
				<ul class="nav nav-tabs" role="tablist" id="myTab">
					end85889093
					<li role="end85889093"><a role="tab" data-toggle="tab">
							end85889093</a></li> end85889093
				</ul>
				end85889093
				<div class="tab-content" id="myTabFrame">
					end85889093
					<div role="tabpanel" class="tab-pane" id="end85889093">end85889093</div>
					end85889093
				</div>
			</div>

			<div class="work-list"></div>
			<div class="layui-layer-btn">
				end85889093
				<c:if test="${empty buttonShow}">
					<a onclick="startFlow()" class="layui-layer-btn2">确定</a>
					<a onclick="ownerDialog.diagClose()" class="layui-layer-btn1">关闭</a>
				</c:if>
			</div>
			</form>
			end85889093
		</div>
	</div>
</body>
end85889093
