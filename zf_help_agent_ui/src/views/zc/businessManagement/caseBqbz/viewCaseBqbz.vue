<template>
  <el-tabs v-model="activeName" @tab-click="handleClick" style="overflow: hidden;">
    <el-tab-pane label="基本信息" name="first">
      <!--事项信息-->
      <div class="el-table__header-wrapper dialog-table">
        <h3><i class="el-icon-document"></i>事项相关信息</h3>
        <el-form  :model="form.info" label-width="0px">
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="12%" />
              <col width="21%" />
              <col width="12%" />
              <col width="21%" />
              <col width="12%" />
              <col width="22%" />
            </colgroup>
            <tr>
              <td><b>事项名称：</b></td><td colspan="3">{{form.info.serviceName}}</td>
              <td><b>事项类型：</b></td><td>{{form.info.serviceTypeName}}</td>
            </tr>
            <tr>
              <td><b>实施机构：</b></td><td>{{form.serviceInfo.sxService.organName}}</td>
              <td><b>承诺时限：</b></td><td>{{form.serviceInfo.sxServiceExtend.promiseLimit}}<span v-if="form.serviceInfo.sxServiceExtend.promiseLimitType=='W'">工作日</span><span v-if="form.serviceInfo.sxServiceExtend.promiseLimitType=='N'">自然日</span><span v-if="form.serviceInfo.sxServiceExtend.promiseLimitType=='H'">小时</span></td>
              <td><b>法定时限：</b></td><td>{{form.serviceInfo.sxServiceExtend.legalLimit}}<span v-if="form.serviceInfo.sxServiceExtend.legalLimitType=='W'">工作日</span><span v-if="form.serviceInfo.sxServiceExtend.legalLimitType=='N'">自然日</span><span v-if="form.serviceInfo.sxServiceExtend.legalLimitType=='H'">小时</span></td>
            </tr>
          </table>
        </el-form>
      </div>
      <!--申请人信息-->
      <div class="el-table__header-wrapper dialog-table">
        <h3><i class="el-icon-document"></i>申请人相关信息</h3>
        <el-form  :model="form.applyPerson" label-width="0px">
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="12%" />
              <col width="21%" />
              <col width="12%" />
              <col width="21%" />
              <col width="12%" />
              <col width="22%" />
            </colgroup>
            <tr>
              <td><b>申请项目名称：</b></td><td colspan="5">{{form.info.projectName}}</td>
            </tr>
            <tr>
              <td><b>业务管辖地：</b></td><td colspan="3">{{form.applyPerson.bussVenueDistrictOid}}</td>
              <td><b>申请数量：</b></td><td>{{form.applyPerson.applyNumber}}</td>
            </tr>
            <tr>
              <td><b>受理具体地点：</b></td><td colspan="3">{{form.applyPerson.specificLocation}}</td>
              <td v-if="form.applyPerson.applyUserType=='0'"><b>申请人姓名：</b></td>
              <td v-else><b>申请单位名称：</b></td>
              <td>{{form.applyPerson.applyUserName}}</td>
            </tr>
            <tr>
              <td><b>证件类型：</b></td><td>{{form.applyPerson.credentialType}}</td>
              <td><b>证件号：</b></td><td>{{form.applyPerson.credentialNumber}}</td>
              <td v-if="form.applyPerson.applyUserType=='0'"><b>联系人手机：</b></td>
              <td v-else><b>申请单位手机：</b></td>
              <td>{{form.applyPerson.applyUserPhone}}</td>
            </tr>
            <!--<tr>
              <td><b>申请人住址：</b></td><td colspan="5">{{form.applyPerson.addresseeAddress}}</td>
            </tr>-->
            <tr>
              <td v-if="form.applyPerson.applyUserType=='0'"><b>联系人固话：</b></td>
              <td v-else><b>申请单位固话：</b></td>
              <td>{{form.applyPerson.addresseePhone}}</td>
              <td><b>通讯地址：</b></td><td colspan="3">{{form.applyPerson.addresseeAddress}}</td>
            </tr>
           <!-- <tr>
              <td><b>投资项目名称：</b></td><td>{{form.applyPerson.applyNumber}}</td>
              <td><b>投资项目编号：</b></td><td colspan="3">{{form.applyPerson.applyNumber}}</td>
            </tr>
            <tr>
              <td><b>办件摘要：</b></td><td colspan="5">{{form.applyPerson.applyNumber}}</td>
            </tr>-->
          </table>
        </el-form>
      </div>
      <!--收件相关信息-->
      <div class="el-table__header-wrapper dialog-table">
        <h3><i class="el-icon-document"></i>收件相关信息</h3>
        <el-form  :model="form.applyPerson" label-width="0px">
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="12%" />
              <col width="21%" />
              <col width="12%" />
              <col width="21%" />
              <col width="12%" />
              <col width="22%" />
            </colgroup>
            <tr>
              <td><b>送达方式：</b></td><td colspan="5">
              <span v-if="form.caseExt.resultDeliveryWay==1"> 快递送达</span>
              <span v-if="form.caseExt.resultDeliveryWay==2">自行取件</span>
              <span v-if="form.caseExt.resultDeliveryWay==3">其他</span>
            </td>
            </tr>
            <tr v-if="form.caseExt.resultDeliveryWay==1">
              <td><b>收件人姓名：</b></td><td>{{form.applyPerson.addresseeName}}</td>
              <td><b>收件人邮编：</b></td><td>{{form.applyPerson.addresseePostCode}}</td>
              <td><b>收件人手机：</b></td><td>{{form.applyPerson.addresseePhone}}</td>
            </tr>
            <tr v-if="form.caseExt.resultDeliveryWay==1">
              <td><b>收件人电话：</b></td><td>{{form.applyPerson.addresseeTel}}</td>
              <td><b>收件人地址：</b></td><td colspan="3">{{form.applyPerson.addresseeAddress}}</td>
            </tr>
            <tr v-if="form.caseExt.resultDeliveryWay==1">
              <td><b>收件人详细地址：</b></td><td colspan="5">{{form.applyPerson.addresseeDetailAddress}}</td>
            </tr>
          </table>
        </el-form>
      </div>
    </el-tab-pane>
    <el-tab-pane label="收取材料" name="third">
        <el-table :data="material" border="1">
          <el-table-column label="序号" align="center" min-width="5%">
            <template slot-scope="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="材料名称" align="center" min-width="40%" prop="materialName"/>
          <el-table-column label="是否收取" align="center" min-width="10%" prop="collectionFlag">
          <template slot-scope="scope">
            <p  v-if="scope.row.collectionFlag==1">已收取</p>
            <p v-if="scope.row.collectionFlag==0">未收取</p>
          </template>
          </el-table-column>
<!--          <el-table-column label="审查要点" align="center" min-width="20%" prop="date"/>-->
          <el-table-column label="收取数量" align="center" min-width="15%" prop="collectionNumber"/>
          <el-table-column label="收取方式" align="center" min-width="15%" prop="collectionType">
            <template slot-scope="scope">
              <p  v-if="scope.row.collectionType==1">纸质收取</p>
              <p v-if="scope.row.collectionType==2">附件上传</p>
              <p  v-if="scope.row.collectionType==3">扫描</p>
              <p v-if="scope.row.collectionType==4">容缺后补</p>
            </template>
          </el-table-column>
        </el-table>

      <div class="el-table__header-wrapper dialog-table">
        <h3><i class="el-icon-document"></i>补正申请信息</h3>
        <el-form  :model="caseCorrection" label-width="0px">
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="20%" />
              <col width="80%" />
            </colgroup>
            <tr>
              <td><b>补正到期时间：</b></td><td>{{caseCorrection.dueDate}}</td>
            </tr>
            <tr>
              <td><b>补正原因：</b></td><td>{{caseCorrection.correctionReason}}</td>
            </tr>
            <tr>
              <td><b>备注：</b></td><td>{{caseCorrection.applRemark}}</td>
            </tr>
          </table>
        </el-form>
      </div>

    </el-tab-pane>
    <div class="btn-wrap">
      <div class="btn-list mt10">
        <el-button style="margin-left: 90%;" @click="viewDialog()">关闭</el-button>
      </div>
    </div>
  </el-tabs>
</template>
<script>
import {
  getOneCase,
  getOneApplyPerson,
  getOneMaterialInfo,
  getQlCaseExt,
  getServiceInfo, getOneDict
} from "@/api/zc/businessManagement/viewCaseInfo.js";
import {getOneByid} from "@/api/zc/businessManagement/caseBqbz.js";
export default {
  name: 'viewCaseBqbz',
  data() {
    return {
      // 遮罩层
      loading: true,
      activeName: 'first',
      caseNumber:"",
      caseOid:"",
      id:"",
      // 表单参数
      form: { info: {},caseExt:{},serviceInfo:[],applyPerson:{}},
      material:[],
      caseCorrection:{}

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
    this.getOneCase();
  },
  methods: {
    handleClick(tab, event) {
    },
    viewDialog(){
      this.$emit('view-case-bqbz');
    },
    //查询办件信息
    getOneCase() {
      this.form.info={};
      this.form.caseExt={};
      this.form.serviceInfo=[];
      this.form.applyPerson={};
      getOneCase(this.caseNumber).then(response => {
          this.form.info = response.data;
          this.caseOid=response.data.caseOid;
        //console.log("---------caseinfo------"+this.caseOid)
        this.getApplyInfo();
        this.getMaterialInfo();
        this.getSdfs();
        this.getOneByid();
        this.viewServiceInfo(response.data.serviceOid);
        })
    },
    //获取事项
    viewServiceInfo(serviceOid) {
      getServiceInfo(serviceOid).then(response => {
        this.form.serviceInfo = response.data;
      })
    },
    getApplyInfo(){
      //查询申请人信息
      getOneApplyPerson(this.caseOid).then(response => {
        this.form.applyPerson = response.data;
        this.getCredential(response.data.credentialType)
      })
    },
    //获取证件类型
    getCredential(Type){
      getOneDict(Type).then(response => {
        this.form.applyPerson.credentialType = response.data.name;
      })
    },
    getMaterialInfo(){
      //查询材料信息
      getOneMaterialInfo(this.caseOid).then(response => {
        console.log("---------"+JSON.stringify(response.data))
        this.material = response.data;
      })
    },
    getSdfs(){
      //送达方式
      getQlCaseExt(this.caseOid).then(response => {
        this.form.caseExt = response.data;
        console.log("---------caseExt------"+JSON.stringify(this.form.caseExt))
      })
    },
    getOneByid(){
      //补正信息
      getOneByid(this.id).then(response => {
        this.caseCorrection = response.data;
      })
    }

  }
};
</script>
<style lang="scss" scoped>
.el-scrollbar__wrap{overflow: hidden}
.dialog-table{padding: 5px;}
</style>
