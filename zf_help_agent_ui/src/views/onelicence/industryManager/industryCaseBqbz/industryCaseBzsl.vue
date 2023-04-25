<template>
  <el-tabs v-model="activeName" @tab-click="handleClick" style="overflow: hidden;">
    <el-tab-pane label="基本信息" name="first">
      <!--事项信息-->
      <div class="el-table__header-wrapper dialog-table">
        <h3><i class="el-icon-document"></i>事项相关信息</h3>
        <el-form  :model="form.info" label-width="0px">
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>业态目录名称：</b></td>
              <td>
                {{form.info.comboDirectoryName}}
              </td>
              <td><b>办件编号：</b></td>
              <td>
                {{form.info.caseNumber}}
              </td>
            </tr>
            <tr>
              <td><b>申请人名称：</b></td>
              <td>{{form.info.applyUserName}}</td>
              <td><b>申请人邮政编码：</b></td>
              <td>{{form.info.applyPostCode}}</td>
            </tr>
            <tr>
              <td><b>证件号码：</b></td>
              <td>{{form.info.credentialNumber}}</td>
              <td><b>申请人地址：</b></td>
              <td>{{form.info.applyUserAddress}}</td>
            </tr>
            <tr>
              <td><b>申请人电话：</b></td>
              <td>{{form.info.applyUserTel}}</td>
              <td><b>申请人手机：</b></td>
              <td>{{form.info.applyUserPhone}}</td>
            </tr>

          </table>

        </el-form>
      </div>
    </el-tab-pane>
    <el-tab-pane label="收取材料" name="third">
      <div class="el-table__header-wrapper dialog-table">
        <h3><i class="el-icon-document"></i>容缺材料信息</h3>
        <el-form
          label-width="0px"
          class="demo-ruleForm"
          :label-position="labelPosition"
        >
          <table cellspacing="0" cellpadding="0" border="0" class="data-table">
            <colgroup>
              <col width="10%"/>
              <col width="20%"/>
              <col width="10%"/>
              <col width="60%"/>
            </colgroup>
            <tr>
              <th>序号</th>
              <th>材料名称</th>
              <th>收取份数</th>
              <th>收取方式</th>
            </tr>
            <tbody v-for="(data,index) in form.material">
            <tr>
              <td style="text-align: center">{{index+1}}</td>
              <td>{{data.comCaseMaterial.materialName}}</td>
              <td style="text-align: center">{{data.comCaseMaterial.collectionNumber}}</td>
              <td>
                <el-radio-group v-model="data.comCaseMaterial.collectionType" @change="handleRowChange(data,index)">
                  <el-radio label="1" >纸质收取</el-radio>
                  <el-radio label="2" >附件上传</el-radio>
                  <el-radio label="3" >扫描</el-radio>
                  <el-radio label="5" >证照库</el-radio>
                  <el-radio label="4" >容缺后补</el-radio>
                </el-radio-group>
              </td>
            </tr>

            <tr v-if="data.comCaseMaterial.collectionType==2 || data.comCaseMaterial.collectionType==3 || data.comCaseMaterial.collectionType==5">
              <td colspan="5" >
                <div class="handle-data">
                  <!--<el-row class="right-btn-group">
                    <el-button type="primary" icon="el-icon-document" size="mini">材料比对</el-button>
                  </el-row>-->
                  <el-row class="right-btn-group-two">
                    <el-form ref="formForm"  size="medium" label-width="100px">
                      <el-form-item>
                        <el-upload
                          class="upload-demo"
                          action=""
                          :http-request="uploadFiles"
                          :before-upload="beforeUpload"
                          :on-error="uploadError"

                          :show-file-list="showFileList"
                          accept="accept">
                          <el-button v-if="data.comCaseMaterial.collectionType == 2" size="mini" type="primary" icon="el-icon-upload" @click="pushMaterialOid(data.comCaseMaterial.caseMaterialOid,index)">点击上传</el-button>

                        </el-upload>
                      </el-form-item>
                      <el-button v-if="data.comCaseMaterial.collectionType == 5" size="mini" type="primary" icon="el-icon-upload" @click="getElecLicenInfo(data.comCaseMaterial.materialOid,data.comCaseMaterial.caseMaterialOid,data.comCaseMaterial.sourceType,index)">获取证照</el-button>
                      <el-button v-if="data.comCaseMaterial.collectionType == 3" size="mini" type="primary" icon="el-icon-upload" @click="scanCard(data.comCaseMaterial.caseMaterialOid,index)">点击扫描</el-button>
                    </el-form>
                  </el-row>
                  <ul v-for="dataAtta in attaList[index]">
                    <li v-if="dataAtta!=null">
                      <el-row :gutter="24">
                        <el-col :span="14">
                          <div class="grid-content qdcg-text">
                            <p>{{dataAtta.originName}}</p>
                          </div>
                        </el-col>
                        <el-col :span="5">
                          <div class="grid-content qdcg-btn">
                            <el-button type="text" icon="el-icon-view"v-if="data.comCaseMaterial.collectionType == 2 || data.comCaseMaterial.collectionType == 3" @click="viewFile(dataAtta.oid)"></el-button>
                            <el-button type="text" v-if="data.comCaseMaterial.collectionType == 2 || data.comCaseMaterial.collectionType == 3"
                                       icon="el-icon-delete" @click="deleteMaterialAtta(data.comCaseMaterial.caseMaterialOid,dataAtta.attaOid,index)"
                            ></el-button>
                            <el-button type="text" icon="el-icon-view" v-if="data.comCaseMaterial.collectionType == 5" @click="viewElemsInfo(dataAtta.attaOid)"></el-button>
                          </div>
                        </el-col>
                      </el-row>
                    </li>
                  </ul>
                </div>
              </td>
            </tr>
            </tbody>
          </table>
        </el-form>
      </div>
      <div class="el-table__header-wrapper dialog-table">
        <h3><i class="el-icon-document"></i>补正申请信息</h3>
        <el-form  :model="formParam" label-width="0px">
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="10%" />
              <col width="80%" />
            </colgroup>
            <tr>
              <td><b>办理意见：</b></td>
              <td> <el-form-item prop="patchOpinion">
              <el-input v-model.trim="formParam.patchOpinion" type="textarea" placeholder="请输入办理意见" maxlength="2000" show-word-limit></el-input>
            </el-form-item></td>
            </tr>
            <tr>
              <td><b>备注：</b></td>
              <td>
                <el-form-item prop="memo">
                  <el-input v-model.trim="formParam.memo" type="textarea" placeholder="请输入备注" maxlength="2000" show-word-limit></el-input>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
        <div slot="footer" class="dialog-footer" style="text-align: center;margin-top: 20px">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="closeDialog()">取 消</el-button>
        </div>
      </div>
      <!--引入文件的预览弹出层-->
      <el-dialog v-dialog-drag title="文件预览" :visible.sync="viewFileShow" v-if="viewFileShow"  width="60%"  append-to-body>
        <industry-case-file-view :attaOid="fileAttaOid"  @father-click="closeFileView"></industry-case-file-view>
      </el-dialog>

    </el-tab-pane>
  </el-tabs>
</template>
<script>
import {getOneCase,getOneApplyPerson,getQlCaseExt} from "@/api/zc/businessManagement/viewCaseInfo.js";
import {saveOrUpdate,getCorrectMaterialInfo,getIndustryCaseByOid} from "@/api/onelicence/industryManager/industryCaseAccept/industryCaseBqbz";
import {uploadCaseMaterialFile} from "@/api/onelicence/industryManager/industryCaseAccept/initIndustryCase";
import {getElecLicenUrl,queryElecLicenseByDirCode} from "@/api/zc/businessManagement/elemLice";
import industryCaseFileView from "@/views/onelicence/industryManager/industryCaseAccept/industryCaseFileView";
import {getPubMaterialInfo} from "@/api/onelicence/industryManager/industryCaseAccept/industryCaseRqbz";
export default {
  name: 'caseBzsl',
  components: {industryCaseFileView},

  data() {
    return {
      // 遮罩层
      loading: true,
      activeName: 'first',
      caseNumber:"",
      caseOid:"",
      fileAttaOid:'',
      viewFileShow:false,
      id:"",
      correctionOid: "",
      labelPosition: "top",
      showFileList:false,
      materialOid: null,
      caseMaterials:[],
      //材料与上传成功的附件集合
      materialAttaList: [],
      materialCollectionType:[],
      elemLicenseList:[],

      //附件集合
      attaList: [],
      open:true,
      // 表单参数
      form: { info: {},caseExt:{},applyPerson:{},material:[]},
      formParam:{caseOid:"",patchOpinion:"",memo:"",materialAtta:[],materialCollect:[]},
    };
  },
  created() {
  },
  //定义获取父页面传过来的值的格式
  props: {
    msgVal: {}
  },
  //获取父页面的值
  mounted() {
    this.caseNumber=this.msgVal.caseNumber;
    this.id=this.msgVal.id;//补齐补正主键
    this.correctionOid=this.msgVal.correctionOid;
    this.caseOid = this.msgVal.caseOid;
    this.getOneCase();
  },
  methods: {
    handleRowChange(data,index) {
      this.materialCollectionType.forEach((item,i) => {
        //判断材料主键是否已经存在，存在移除
        if(item.materialOid==data.comCaseMaterial.caseMaterialOid){
          this.materialCollectionType.splice(i,1);
        }
      })
      let list={};
      list.materialOid=data.comCaseMaterial.caseMaterialOid;
      list.collectionType=data.comCaseMaterial.collectionType;
      this.materialCollectionType.push(list);
      //清空附件信息
      this.materialAttaList.forEach((att,j)=>{
        if(att.materialOid==data.caseMaterialOid){
          this.materialAttaList.splice(j,1);
        }
      })
      this.$set(this.attaList, index, []);
      //清除电子证照信息
      this.elemLicenseList.forEach((ite,i)=>{
        if(ite.materialOid==data.caseMaterialOid){
          //移除原来的重新赋值
          this.elemLicenseList.splice(i,1);
        }
      })
    },
    handleClick(tab, event) {
    },
    //预览附件
    viewFile(attaOid){
alert(attaOid);
      this.fileAttaOid=attaOid;
      this.viewFileShow=true;
    },
    //关闭预览附件
    closeFileView(){
      this.viewFileShow=false;
    },
    pushMaterialOid(materialOid,index){
      this.materialOid = materialOid;
      this.attaIndex=index;//标识材料索引
    },
    //查询办件信息
    getOneCase() {
      getIndustryCaseByOid(this.caseOid).then(response => {
        this.form.info = response.data;
        this.caseOid=response.data.caseOid;
        //this.getApplyInfo();
        this.getOneMaterialInfo();

        //this.getSdfs();
        })
    },
    /*getApplyInfo(){
      //查询申请人信息
      getOneApplyPerson(this.caseOid).then(response => {
        this.form.applyPerson = response.data;
      })
    },*/
    getOneMaterialInfo(){
      //查询材料信息this.caseOid''
      getCorrectMaterialInfo(this.correctionOid,this.caseOid).then(response => {
        //this.caseOid="1853f7f1a75444dd8611ba44d4d88405";
        this.form.material = response.data;
        this.form.material.forEach((item,i)=>{
          this.attaList[i]=item.materialAtta;
          //循环材料附件，保存主键和采集类型，附件主键和材料主键关系
          if(item.materialAtta){
            item.materialAtta.forEach((childItem,i)=>{
              let list={};
              list.materialOid=item.comCaseMaterial.caseMaterialOid;
              list.attaOid=childItem.attaOid;
              this.materialAttaList.push(list);
            })
          }
          //先保存材料采集类型和材料主键关系
          this.materialCollectionType.forEach((itemType,i) => {
            //判断材料主键是否已经存在，存在移除
            if(itemType.materialOid==item.comCaseMaterial.caseMaterialOid){
              this.materialCollectionType.splice(i,1);
            }
          })
          let listType={};
          listType.materialOid=item.comCaseMaterial.caseMaterialOid;
          listType.collectionType=item.comCaseMaterial.collectionType;
          this.materialCollectionType.push(listType);
        })

      })
    },
    getSdfs(){
      //送达方式
      getQlCaseExt(this.caseOid).then(response => {
        this.form.caseExt = response.data;
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      _that.formParam.caseOid=_that.caseOid;
      _that.formParam.id=_that.id;
      _that.formParam.materialAtta=this.materialAttaList;
      _that.formParam.materialCollect=this.materialCollectionType;
      _that.formParam.elemLicense=this.elemLicenseList;
      //console.log("-----"+JSON.stringify(_that.formParam))
      saveOrUpdate(_that.formParam).then(response => {
        if (response.code === 200) {
          _that.msgSuccess("保存成功");
          //调取父页面方法，关闭页面
          _that.closeDialog();
        }
      });
    },
    closeDialog() {//调取父页面方法
      this.$emit('hideDialog');
    },
    //失败后返回
    uploadError(resp){
      this.msgError("文件上传失败");
    },
    /** 上传附件 */
    uploadFiles(file){
      let formData = new FormData();
      formData.append("files",file.file);
      uploadCaseMaterialFile(formData).then(response => {
        if(response.data != ""){
          let resultAtta=[];
          if(this.attaList.length>0){
            if(this.attaList[this.attaIndex]){
              resultAtta= [].concat(this.attaList[this.attaIndex]);
            }else{
            }
          }
          response.data.forEach((data,index) => {
            resultAtta.push(data);//存放每一个上传的附件
            //放置材料与附件的关系,生成多条记录
            let list={};
            list.materialOid=this.materialOid;
            list.attaOid=data.oid;
            this.materialAttaList.push(list);
          });
          this.attaList[this.attaIndex]= [].concat(resultAtta);
          this.$forceUpdate();//强制视图刷新
        }
      })
    },
    //上传之前
    beforeUpload(file) {
      if(file.name.indexOf("%00") > -1 || file.name.indexOf("./") > -1 || file.name.indexOf(".\\") > -1 ) {
        this.msgError('上传文件名称非法！');
        return false;
      }
      const fileSize = file.size;
      if (0 == fileSize) {
        this.msgError('上传文件不能为空！');
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 100;
      if (!isLt2M) {
        this.msgError('上传文件大小不能超过 100MB！');
      }
      return isLt2M;
    },
    //删除附件信息
    deleteMaterialAtta(caseMaterialOid,attaOid,index){
      this.materialAttaList.forEach((item,i) => {
        //判断材料主键是否已经存在，存在移除
        if(item.materialOid==caseMaterialOid&&item.attaOid==attaOid){
          this.materialAttaList.splice(i,1);
        }
        //剔除附件list中的附件
        if(this.attaList.length>0){
          if(this.attaList[index]){
            //剔除附件信息重新加载页面
            this.attaList[index].forEach((ind,i)=>{
              if(ind.attaOid==attaOid){
                this.attaList[index].splice(i,1);
              }
            })
          }
        }

      })
      this.$forceUpdate();//强制更新视图
    },
    getElecLicenInfo(materialOid,caseMaterialOid,sourceType,index){
      let _that=this;
      let userName="";
      let idCard="";
      let userType=this.form.info.applyUserType;
      idCard=_that.form.info.credentialNumber;
      userName= _that.form.info.applyUserName ;
     /* if (userType == "0"){
        idCard=_that.form.info.credentialNumber;
        userName= _that.form.info.applyUserName ;
      }
      if (userType == "1"){
        idCard=_that.form.info.contactCredentialNumber
        userName=_that.form.info.contactUserName
      }*/
      if(userName&&idCard){
        if(sourceType==0){//公共材料
          //查询公共材料信息获取证照目录
          getPubMaterialInfo(materialOid).then(response=>{
            let pubMaterial=response.data;
            if(pubMaterial.elecBillOid){
              queryElecLicenseByDirCode(materialOid, userName, idCard,pubMaterial.elecBillOid).then(response => {
                if (response.data) {
                  let res = {};
                  let eleArr = [];
                  res.originName = response.data.licenseNumber;
                  res.attaOid = response.data.elecLicenOid;
                  res.materialOid = caseMaterialOid;
                  eleArr[0] = res;
                  _that.$set(_that.attaList,index,eleArr);
                  //判断elemLicenseList是否已经存在办件的证照信息
                  if (_that.elemLicenseList) {
                    _that.elemLicenseList.forEach((ite, i) => {
                      if (ite.materialOid == caseMaterialOid) {
                        //移除原来的重新赋值
                        _that.elemLicenseList.splice(i, 1);
                      }
                    })
                  }
                  _that.elemLicenseList.push(res);

                } else {
                  _that.$message.error("请检查证照相关配置！");
                  return;
                }
              });
            }else{
              _that.$message.error("请检查证照相关配置！");
              return;
            }
          })
        }else{//不是公共材料
          queryElecLicenseByDirCode(materialOid, userName, idCard,'').then(response => {
            if (response.data) {
              let res = {};
              let eleArr = [];
              res.originName = response.data.licenseNumber;
              res.attaOid = response.data.elecLicenOid;
              res.materialOid = caseMaterialOid;
              eleArr[0] = res;
              _that.$set(_that.attaList,index,eleArr);
              //判断elemLicenseList是否已经存在办件的证照信息
              if (_that.elemLicenseList) {
                _that.elemLicenseList.forEach((ite, i) => {
                  if (ite.materialOid == caseMaterialOid) {
                    //移除原来的重新赋值
                    _that.elemLicenseList.splice(i, 1);
                  }
                })
              }
              _that.elemLicenseList.push(res);

            } else {
              _that.$message.error("请检查证照相关配置！");
              return;
            }
          });
        }
      }else{
        _that.$message.error("申请人/申请单位和证件号不能为空！");
        return;
      }
    },
    //电子证照预览
    viewElemsInfo(eleLincenseOid){
      getElecLicenUrl(eleLincenseOid).then(response => {
        let urlElem=[];
        if(response.data){
          urlElem=response.data;
          if(urlElem[0].viewOfdUrl){
            window.open(urlElem[0].viewOfdUrl,'width=1200px;height=800px;');
          }else{
            this.$message.error("暂时无法查看证照信息！");
          }
        }
      });
    },
    base64ToFile(urlData, fileName) {
      let arr = urlData.split(',');
      // let mime = arr[0].match(/:(.*?);/)[1];
      let bytes = atob(arr[0]); // 解码base64
      let n = bytes.length
      let ia = new Uint8Array(n);
      while (n--) {
        ia[n] = bytes.charCodeAt(n);
      }
      return new File([ia], fileName, { type: 'image/jpeg' });
    },
    // 接收socket回调函数返回数据的方法
    getConfigResult(data) {
      let _that = this;
      if (data.status == 0) {
        //高拍仪
        if (data.type) {
          let types = data.type.split(",");
          //所属材料主键
          let materialOid = types[0];
          //扫描所属行索引
          let attaIndex = types[1];
          if (data.device == "HighCamera"){
            if (data.content.Cameras64){
              let base64s = data.content.Cameras64.split(",");
              for (let i = 0; i < base64s.length; i++) {
                if (!base64s[i]) {
                  continue;
                }
                let file = _that.base64ToFile(base64s[i], "scanPicture" + i+".jpg");
                let formD = new FormData();
                formD.append("files", file);
                uploadCaseMaterialFile(formD).then(response => {
                  if(response.data != ""){
                    let resultAtta=[];
                    if(_that.attaList.length>0){
                      if(_that.attaList[attaIndex]){
                        resultAtta= [].concat(_that.attaList[attaIndex]);
                      }else{
                      }
                    }
                    response.data.forEach((data,index) => {
                      resultAtta.push(data);//存放每一个上传的附件
                      //放置材料与附件的关系,生成多条记录
                      let list={};
                      list.materialOid = materialOid;
                      list.attaOid=data.attaOid;
                      _that.materialAttaList.push(list);
                    });
                    _that.attaList[attaIndex]= [].concat(resultAtta);
                    _that.$forceUpdate();//强制视图刷新
                  }
                })
              }
            }
          }
        }
      }
      if (data.status == 1) {
        _that.$message.error(data.message)
      }
    },
    //初始化socket发生错误回调
    socketError() {
      this.$message.error("请检查设备或连接是否正常")
    },
    scanCard(caseMaterialOid,index) {
      //caseMaterialOid--所属材料主键
      //index--所属操作行索引
      //Device：设备类型、
      let info  = '{"device":"HighCamera", "type":"'+caseMaterialOid+','+index+'"}';
      //建立socket连接
      this.socketApi.initWebSocket(this.socketError);
      this.socketApi.sendSock(info, this.getConfigResult);
    },

  },
  destroyed: function () {
    //在离开此页面的时候主动关闭socket
    this.socketApi.webSocketClose();
  }
};
</script>
<style lang="scss" scoped>
.el-scrollbar__wrap{overflow: hidden}
.dialog-table{padding: 5px;}
</style>

