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
              <td><b>一件事目录名称：</b></td>
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
        <el-table :data="material" border>
          <el-table-column label="序号" align="center" min-width="5%">
            <template slot-scope="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="材料名称" align="center" min-width="30%" prop="materialName"/>
          <el-table-column label="是否收取" align="center" min-width="10%" prop="collectionFlag">
          <template slot-scope="scope">
            <p  v-if="scope.row.collectionFlag==1">已收取</p>
            <p v-if="scope.row.collectionFlag==0">未收取</p>
          </template>
          </el-table-column>
          <el-table-column label="审查要点" align="center" min-width="20%" prop="date"/>
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
  </el-tabs>
</template>
<script>
import {getOneCase,getOneApplyPerson,getOneMaterialInfo,getQlCaseExt} from "@/api/zc/businessManagement/viewCaseInfo.js";
import {getOneByid,getComboCaseByOid} from "@/api/onething/comboManager/comboAccept/caseBqbz";
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
      form: { info: {},caseExt:{},applyPerson:{}},
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
    this.caseOid = this.msgVal.caseOid;
    this.getOneCase();
  },
  methods: {
    handleClick(tab, event) {
    },
    //查询办件信息
    getOneCase() {
      getComboCaseByOid(this.caseOid).then(response => {
          this.form.info = response.data;
          this.caseOid=response.data.caseOid;
        this.material = response.data.comboCaseMaterials;

        console.log("---------caseinfo------"+ this.material)
        //this.getApplyInfo();
        //this.getMaterialInfo();
        //this.getSdfs();
        this.getOneByid();
        })
    },
    getApplyInfo(){
      //查询申请人信息
      getOneApplyPerson(this.caseOid).then(response => {
        this.form.applyPerson = response.data;
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

