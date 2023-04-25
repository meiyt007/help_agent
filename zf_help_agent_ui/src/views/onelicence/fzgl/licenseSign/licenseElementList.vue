/**
* @Author: wangwg
* @Date: 2021-02-27
* @Description: 证照要素信息
*/
<template>
  <div class="app-container">
    <!-- 填充证照要素信息 -->
    <h3 class="title"><i class="el-icon-s-grid"></i>证照要素信息</h3>
    <el-table  :data="elementsList" class="zzyspz">
      <el-table-column label="要素名称"  align="center" prop="elementName" :show-overflow-tooltip="true"/>
      <el-table-column label="要素值" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <template v-if="scope.row.elementType==='imgFile'">
            <el-upload
              class="upload-demo"
              :action="uploadUrl()"
              :file-list="fileList"
              :on-error="uploadError"
              :on-success="function (res,file){return handleImgSuccess(res,file,scope.row.elementOid)}"
              :on-remove="function (res,file){return handleImgRemove(res,file,scope.row.elementOid)}"
              >
              <el-button size="small" type="primary">选择文件<i class="iconfont zfsoft-wenjianshangchuan"></i></el-button>
            </el-upload>
            <el-input v-model="elementValue[scope.row.elementOid]"  readonly  maxlength="100" show-word-limit />
          </template>
          <template v-else>
            <el-input v-model="elementValue[scope.row.elementOid]"  placeholder="请填写数据" maxlength="100" show-word-limit />
          </template>


        </template>
      </el-table-column>
    </el-table>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="pushElemenValue">打印证照</el-button>
      <el-button @click="viewDialog">关 闭</el-button>
    </div>
    <el-dialog v-dialog-drag :visible.sync="elementPrintView" v-if="elementPrintView"  title="证照打印" width="70%" :close-on-click-modal="false"  append-to-body>
      <el-scrollbar>
      <lisence-print @case-close="closeView" :industryCaseOid="industryCaseOid" :resultOid="resultOid" :licenseName="licenseName" :faModelTemplateOid="faModelTemplateOid" :elementValueList="elementValueList" :zcPath="zcPath" :industryPath="industryPath" :metaDataJsonArrayStr="elementsList"></lisence-print>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>
<script>
import {getRequestUrl, queryElementList} from "@/api/onelicence/fzgl/licenseSignIndustry";
import { pageFile, uploadFile } from "@/api/sys/atta";
import lisencePrint from "@/views/onelicence/fzgl/licenseSign/lisencePrint";
export default {
  name: "licenseElement",
  props:["resultOid","industryCaseOid","licenseName"],
  components: {lisencePrint},
  data() {
    return {
      elementPrintView: false,
      elementValue: [],
      elementsList: [],
      elementValueList: [],
      fileList:[],
      attaList:[],
      faModelTemplateOid :'',
      zcPath: '',
      industryPath: '',
    };
  },
  created() {
    this.getList();
    this.queryZcUrl();
    this.queryIndustryUrl();
  },
  methods: {
    /** 查询列表 */
    getList() {
      queryElementList(this.resultOid).then(response => {
        this.elementsList = response.data.metaDataJsonArrayStr;
        console.log(JSON.stringify(this.elementsList))
      })
    },
    pushElemenValue(){
      if (this.elementsList.length > 0) {
        this.elementsList.forEach(data => {
          let elements={};
          let elementOid=data.elementOid;
          elements.elementOid=data.elementOid;
          let elementValuenew="";
          if(this.elementValue[data.elementOid]!=undefined){
            elementValuenew=this.elementValue[data.elementOid];
          }
          elements.elementValue=elementValuenew;
          this.elementValueList.push(elements);
         // console.log("elementValueList="+ JSON.stringify(this.elementValueList) )
        });

      }
      this.openElementPrint();
     // this.lisencePrint(this.resultOid);
    },
   /* lisencePrint(resultOid){
      let elementValues =JSON.stringify(this.elementValueList)
      let faModelTemplateOid = '';
      let viewOrEdit='edit';
      let url=process.env.BASE_URL+'bc/lisencePrint.html?faModelTemplateOid='+faModelTemplateOid+'&materialCatalogOid='+resultOid+'&viewOrEdit='+viewOrEdit+'&elementValues='+elementValues;
      var winObj=window.open(url);
      var loop = setInterval(function() {
        if(winObj.closed) {
          alert(333333333)
        }
        clearInterval(loop);
        parent.location.reload();
      },  1);
    },*/
    openElementPrint(){
      this.elementPrintView = true;
      this.title = "证照打印";
    },
    //获取综窗地址
    queryZcUrl(){
      let _that =this;
      let code="ZC_ROUTE_URL" ;
      getRequestUrl(code).then(response => {
        _that.zcPath = response.data.value;
      })
    },
    //获取一业一证地址
    queryIndustryUrl(){
      let _that =this;
      let code="INDUSTRY_ROUTE_URL" ;
      getRequestUrl(code).then(response => {
        _that.industryPath = response.data.value;
      })
    },
    // 关闭按钮
    closeView() {
      this.elementPrintView= false;
      this.viewDialog();
    },
    cancel() {
      this.openInit = false;
      this.reset();
    },
    viewDialog(){
      this.$emit('case-close');
      this.getList();
    },
    handleImgSuccess(resp,file,elementOid){
      this.fileList=[];
      this.elementValue[elementOid]=resp.data.url;
    },
    handleImgRemove(resp,file,elementOid){
    /*  this.attaList = this.attaList.filter(item => item.elementOid !== elementOid);*/
      // console.log("attaList="+ JSON.stringify(this.attaList) )
    },
    //失败后返回
    uploadError(resp){
      this.msgError("文件上传失败");
    },
    uploadUrl(){
      return uploadFile();
    }
  },
};
</script>
<style lang="scss" scoped>
.el-upload{
  float: left;
}

</style>
