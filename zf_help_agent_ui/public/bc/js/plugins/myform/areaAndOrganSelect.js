$ (function () {
    common.ajaxPost ('common/districtTree.do', null, function (data) {
    	if($("#districtTree")){
    		var districtId=$("input[name='districtName']").attr("id");
	    txtBindTree ('districtTree', eval (data.districtJson), districtId, 'districtOid', 'clearDistrictTree',
	            function (treeNode) {
	    	       if($("#sysOrganTree")){
	    	    	//var organId=$("input[name='organName']").attr("id");
		            //common.clearControlValue (organId, 'organOid');
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
	    	    //var organId=$("input[name='organName']").attr("id");
		    	common.clearControlValue (districtId, 'districtOid', 'organName', 'organOid');
		    	initOrgan ('');
	    	  }else{
	    		  common.clearControlValue (districtId, 'districtOid');
	    	  }
	    });
    }
});
function initOrgan (districtOid) {
    common.ajaxPost ('common/organTree.do', { districtOid : districtOid }, function (data) {
	    txtBindTree ('sysOrganTree', eval (data.sysOrganJson), 'organName', 'organOid', 'clearOrganTree');
    })
}