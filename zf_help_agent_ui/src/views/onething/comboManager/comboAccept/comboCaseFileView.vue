<template>
  <div>
    <div v-if="showImage == true" class="dialog-body-content-base-style">
      <img width="300px" height="550px"  style="width: 100%" :src="fileUrl">
    </div>
    <div v-if="showDoc == true" class="dialog-body-content-base-style">
      <iframe frameborder="0"
              :src="'https://view.officeapps.live.com/op/view.aspx?src=' + this.fileUrl"
              width='100%'>
      </iframe>
    </div>
    <div v-if="showTxt == true" class="dialog-body-content-base-style">
      <embed :src="fileUrl"  width="100%" height="550px"/>
    </div>
    <div v-else-if="showPdf == true" class="dialog-body-content-base-style" style="justify-content: center; align-items: center;height: 100%">
      <div v-if="fastFlag == '0'" class="el-button-center">
        {{currentPage}} / {{pageCount}}
        <el-button-group>
          <el-button type="primary" @click="changePre" icon="el-icon-arrow-left">上一页</el-button>
          <el-button type="primary" @click="changeNext" >下一页<i class="el-icon-arrow-right el-icon--right"></i></el-button>
        </el-button-group>
      </div>
      <div  v-if="fastFlag == '0'">
      <pdf :src="pdfSrc"
             :page="currentPage"
             @num-pages="pageCount = $event"
             @page-loaded="currentPage = $event"
             class="pdf-set"
        ></pdf>
      </div>
      <embed v-if="fastFlag == '1'" :src="pdfSrc"  width="100%" height="550px"/>
    </div>

  </div>
</template>
<script>
  import { getComboCaseAttaByAttaOid,getPdf } from "@/api/onething/comboManager/comboAccept/comboCaseAtta";
  import pdf from 'vue-pdf'
  export default {
    components: {
      pdf
    },
    name: "ComboCaseFileView",
    props:["attaOid"],
    data() {
      return {
        flag:true,
        showDoc: false,
        showPdf: false,
        showImage: false,
        showTxt:false,
        fileUrl: "",
        currentPage: 1, // pdf文件页码
        pageCount: 1, // pdf文件总页数
        scale: 1.0,
        pdfSrc:"",
        fastFlag:'1'
      }
    },
    created() {
      this.getAttaNginxPath();
    },
    methods: {
      changePre(){
        if(this.currentPage>1){
          this.currentPage--
        }
      },
      changeNext(){
        if(this.currentPage < this.pageCount){
          this.currentPage++
        }
      },
      getAttaNginxPath(){
        if(!this.attaNginxPath){
          if(this.attaOid){
            getComboCaseAttaByAttaOid(this.attaOid).then(response => {
              let attaNginxPath = response.data.fastdfsNginxUrl;
              if(null!=attaNginxPath){
                this.preview(1,attaNginxPath,attaNginxPath);
              }else {
                let name = response.data.name;
                let attaOid= response.data.attaOid;
                this.preview(0,name,attaOid);
              }

            });
          }
        }else {
          this.preview(1,this.attaNginxPath,this.attaNginxPath);
        }
      },
      preview(ty,name,fileUrl) {
        let type = this.iconByType(name);
        if(1 === ty){
          this.fileUrl = fileUrl;
          this.fastFlag ='1';
        }else {
          this.fastFlag ='0';
          this.fileUrl = process.env.VUE_APP_ZC_ROUTE_PATH + "/comboCase/atta/downloadFile/"+ fileUrl;
        }
        if (type.indexOf("doc") != -1 || type.indexOf("docx") != -1 || type.indexOf("xsl") != -1 || type.indexOf("xslx") != -1) {
          this.$emit('father-click');
          this.msgWarning("当前文件暂不支持预览");
          //this.showDoc = true
        } else if (type.indexOf("pdf") != -1) {
         if(1 === ty){
            this.pdfSrc = fileUrl;
          }else {
            this.getPdfCode(fileUrl);
          }
          this.showPdf = true;
          //this.$emit('father-click');
          //this.msgWarning("当前文件暂不支持预览");
        } else if (type.indexOf("jpg") != -1 || type.indexOf("png") != -1 || type.indexOf("jpeg") != -1) {
          this.showImage = true;
        } else if (type.indexOf("txt") != -1 || type.indexOf("TXT") != -1) {
          this.showTxt = true;
        }  else {
          this.$emit('father-click');
          this.msgWarning("当前文件暂不支持预览");
        }

      },
      /**
       * 通过文件后缀返回文件的图标
       */
      iconByType(path) {
        return path.substring(path.lastIndexOf(".") + 1, path.length);
      },
      closePreviewClick() {
        if (this.showDoc == true) {
          this.showDoc = false
        } else if (this.showPdf == true) {
          this.showPdf = false
        } else if (this.showVideo == true) {
          this.showVideo = false
        }
      },
      // 初始化获取pdf文件
      getPdfCode (fileOid) {
        let that = this;
        getPdf(fileOid).then(response => {
          that.pdfSrc  = that.getObjectURL(response);
        }).catch(function (error) {
          console.log(error);
        });
      },

      // 将返回的流数据转换为url
      getObjectURL(file) {
        let url = null;
        if (window.createObjectURL != undefined) { // basic
          url = window.createObjectURL(file);
        } else if (window.webkitURL != undefined) { // webkit or chrome
          try {
            url = window.webkitURL.createObjectURL(file);
          } catch (error) {

          }
        } else if (window.URL != undefined) { // mozilla(firefox)
          try {
            url = window.URL.createObjectURL(file);
          } catch (error) {

          }
        }
        return url;
      },
    }
  }
</script>
<style scoped>
  .pdf-set{
    display: inline-block;
    height:100%;
    width:100%;
  }
  .el-button-center{
    margin-left: 35%;
  }
</style>
