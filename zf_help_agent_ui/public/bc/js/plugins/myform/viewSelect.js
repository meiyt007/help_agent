function viewSelect(oid){
	     alert("gooo2");
		var diag = new Dialog(900, 600, "视图数据选择");
		diag.URL = "${ctxPath }form/view/preview.do?oid="+oid+"&operatFlag=Y";
		diag.show();
		diag.callBack = function(data){			
			$("input:text").each(function(){
				if(typeof(data[this.name])!='undefined'&&data[this.name]!='')
	    		 this.value=data[this.name];
	    	}); 
			diag.close();
		};
	}