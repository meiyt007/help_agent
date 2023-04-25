 $ (function () {
    'use strict';
    var url = 'form/atta/uploadFile.do';
    var attaInfos = "";
    var materialOids="";
    var materialOid = "";
    var formOid="";
    //var materialOidsStr="#materialOids"
    	  //$ ('#fileupload')
    if($("input[name='fileupload']")){
    	  $("input[name='fileupload']")
    	  .click(function(){
    		  materialOid=$(this).prev().val();
    		  formOid=$(this).next().val();
    		  materialOids="";//全局变量重新设为空
    		  attaInfos="";//全局变量重新设为空
    		  });
    	   $("input[name='fileupload']")
          .fileupload (
                  {
                      url : url,
                      maxFileSize : 99*1024*1024,//99MB
                      maxNumberOfFiles : 3,
                      dataType : 'html',
                      singleFileUploads : true,
                      done : function (e, data) {
	                        var result = eval ('[' + decodeURIComponent (data.result) + "]");
	                        if (result[0].error == "true"){
		                        $.each (result,
		                                        function (index, file) {
			                                        $("#fileName"+materialOid).val(file.atta.originName);//赋值材料名称
			                                        if (($ ("#materialOids"+formOid).val () != ""
		                                                && $ ("#materialOids"+formOid).val () != null && $ ("#materialOids"+formOid)
		                                                .val () != "undefined")){
			                                        	materialOids = $ ("#materialOids"+formOid).val () + ";";
		                                            }
			                                        materialOids = materialOids +materialOid;
			                                        $ ("#materialOids"+formOid).val (materialOids); 
			                                        if (($ ("#attaInfos"+formOid).val () != ""
			                                                && $ ("#attaInfos"+formOid).val () != null && $ ("#attaInfos"+formOid)
			                                                .val () != "undefined"+formOid)){
				                                        attaInfos = $ ("#attaInfos"+formOid).val () + ";";
			                                        }
			                                        attaInfos = attaInfos +file.atta.oid
			                                        $ ("#attaInfos"+formOid).val (attaInfos); 
		                                        }); 
	                        }
	                        else{
		                        alert (result[0].error);
	                        }
                      } }).on ('fileuploadadd', function (e, data) {
	            //开始上传文件之前调用方法
          }).prop ('disabled', !$.support.fileInput).parent ().addClass (
                  $.support.fileInput ? undefined : 'disabled');
    }    
});

function deleteAtta (obj, infos) {
    var deleteRequestUrl = "form/atta/deleteFile.do"
    common.ajaxPost (deleteRequestUrl, { oid : infos }, function (data) {
	    var mes = eval (data);
	    if (mes.success){
		    $ (obj).parents ("tr").remove ();
		    window.location.reload();
	    }
	    else{
		    Dialog.alert (mes.data);
	    }
    });
}
function downLoad(oid){
	location.href="form/atta/downLoadFile.do?oid="+oid;
}