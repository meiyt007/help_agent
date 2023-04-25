<template>
  <div class="dialogContrast">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <template v-if="attaList.length<=0">
            暂时没有相关附件
          </template>
          <template  v-for="(atta,index) in attaList">
              <el-tab-pane :label="atta.name" :name="atta.activeName">
                <div v-show="atta.type=='pdf'">
                  <embed :src="atta.url"   width="100%" height="550px"  type="application/pdf" />
                </div>
                <div v-show="atta.type=='picture'">
                  <img    :src="atta.url"  width="100%" height="99%" >
                </div>
                <div v-show="atta.type=='txt'" >
              <!--    <iframe frameborder="0"
                          :src="'https://view.officeapps.live.com/op/view.aspx?src=' + atta.url"
                          width='100%'>
                  </iframe>-->
                <!--  <embed :src="atta.url"  width="99%" height="99%"/>-->
                </div>
              </el-tab-pane>
            </template>
        </el-tabs>
      </el-col>

     <el-col :span="12">
        <h3><i class="el-icon-document"></i>材料模板信息</h3>
        <iframe :src=fileViewurl  rameborder="0" width="100%" height="400px"  ></iframe>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {getCaseAttaByAttaOid} from "@/api/zc/businessManagement/caseAtta";
import pdf from "vue-pdf";

export default {
  name: "Home",
  components: {pdf},
  props: ['sampleInfoOid','comboDirectoryOid','materiaOid','fileViewurl','caseOid','attaOids'],
  data() {
    return {
      labelPosition: "top",
      activeName: "first",
      pageNum: 1,
      pageRotate: 0,
      csUrl:'',
      // 加载进度
      loadedRatio: 0,
      curPageNum: 0,
      attaList:[],
      sxAttaList:[],
    };
  },
  created() {

   this.getSysAttaList();

   /* sxAttaList*/


    },
  methods: {
    getSysAttaList() {
     /* http://172.168.252.42:9090/sysAttaService/download?attaOid=9ff665d6608d4564a9fd94e728287a73*/
      /*let materialOid="2c287c0675f46b170175f487488f0040";
      let caseOid="bbfc63596a9a4e03a18324f113b4cdce";*/
      let materialOid=this.materiaOid;
      let caseOid=this.caseOid;
      let attaOid=this.attaOids.split(";");
      this.loading = true;
      let atta={};
      if(attaOid && attaOid.length>0){

        attaOid.forEach((ite,j)=>{
          if(ite){
            getCaseAttaByAttaOid(ite).then(response => {
              let sysAtta=response.data;
              atta={};
              let type=this.iconByType(sysAtta.originName);
              if(type=='jpg'||type=='JPG'||type=='jpeg'||type=='JPEG'||type=='png'||type=='PNG'){
                atta.type="picture";
              }else if(type=='PDF'||type=='pdf'){
                atta.type="pdf";
              }else if(type=='txt'||type=='TXT'){
                atta.type="txt";
              }
              if(type=='jpg'||type=='JPG'||type=='jpeg'||type=='JPEG'||type=='png'||type=='PNG'||type=='PDF'||type=='pdf'){
                if(j<=0){
                  atta.activeName="first";
                }else{
                  atta.activeName="";
                }
                j=j+1;
                atta.pageNum=1;
                atta.pageRotate=0;
                atta.pageTotalNum=1;
                atta.name=sysAtta.originName;
                let  filePath=sysAtta.filePath;
                let  name=sysAtta.name;
                atta.url=sysAtta.fastdfsNginxUrl;//previewUrl+"?attaOid="+dataList[i].attaOid;
                this.attaList.push(atta);
              }
            });
          }
        })
      }

    },
    iconByType(path) {//截取类型
      return path.substring(path.lastIndexOf(".") + 1, path.length);
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    // 上一页函数，
    prePage(index) {
      var page = this.attaList[index].pageNum;
      page = page > 1 ? page - 1 : this.attaList[index].pageTotalNum;
      this.attaList[index].pageNum = page;
    },
    // 下一页函数
    nextPage(index) {
      var page = this.attaList[index].pageNum;
      page = page < this.attaList[index].pageTotalNum ? page + 1 : 1;
      this.attaList[index].pageNum = page;
    },
    // 页面顺时针翻转90度。
    clock(index) {
      this.attaList[index].pageRotate += 90;
    },
    // 页面逆时针翻转90度。
    counterClock(index) {
      this.attaList[index].pageRotate -= 90;
    },
    pageresilt(e,index){//获取总页数
      if(e!=undefined){
        this.attaList[index].pageTotalNum=e;
      }

    },
    // 页面加载回调函数，其中e为当前页数
    pageLoaded(e) {
      this.curPageNum = e;
    },
    // 其他的一些回调函数。
    pdfError(error) {
      console.error(error);
    },
  },
};
</script>

<style lang="scss" scoped>
.dialogContrast {
  font-size: 12px;
  padding: 20px;
  box-sizing: border-box;
  color: #333;
}
.dialogContrast h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}
.contract iframe {
  width: 100%;
  height: 100vh;
  border: none;
}
.page{
  display: inline-block;
  vertical-align: middle;
  margin: 0px 10px;
}

.el-container {
  flex-direction: column;
}
.panel-body {
  margin-top: 10px;
}

input {
  display: none;
}
.demo {
  min-height: 500px;
}
.demo div.canvas-container {
  border: 1px solid red;
}
canvas {
  border: 1px dashed black;
}
.draw-btn-group {
  // width: 1270px;
  margin-bottom: 10px;
  & > div {
    display: inline-block;
    vertical-align: middle;
    background: #fafafa;
    width: auto;
    text-align: center;
    padding: 5px 10px;
    cursor: pointer;
    &:hover {
      background: #eee;
    }
    i {
      display: block;
      margin: auto;
      background-repeat: no-repeat;
      background-size: 80%;
      background-position: 50% 50%;
      height: 25px;
      width: 25px;
    }

  }
  .active {
    background: #eee;
  }
}
.maintenancePlanAdd,.marker-right{
  display: inline-block;
  vertical-align: top;
}
.marker-right{
  font-size: 16px;
  color: #333;
  width: 200px;
  margin-top: 60px;
}
.marker-right h3{
  font-weight: bold;
  font-size: 14px;
  margin-bottom: 10px;
}
.marker-right h4{
  font-weight: normal;
  margin: 0;
  font-size: 12px;
  margin-bottom: 5px;
}
.marker-right textarea{
  display: block;
  border: 1px solid #ddd;
  font-size: 14px;
  padding: 5px;
  box-sizing: border-box;
  outline: none;
  width: 100%;
  margin-bottom: 15px;
  border-radius: 3px;
  min-height: 70px!important;
}

</style>
