<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="0px">
      <div class="el-table__header-wrapper dialog-table">
        <h3 class="title"><i class="el-icon-s-grid"></i>基本信息</h3>
        <table cellspacing="0" cellpadding="0" border="0" >
          <colgroup>
            <col width="15%" />
            <col width="35%" />
            <col width="15%" />
            <col width="35%" />
          </colgroup>
          <tr>
            <td><b>业户名称：</b></td>
            <td colspan="3">
              <el-form-item prop="companyName">
                <el-input v-model.trim="form.companyName" placeholder="请输入业户名称" maxlength="50" show-word-limit/>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>地址：</b></td>
            <td colspan="3">
              <el-form-item prop="address">
                <el-input v-model.trim="form.address" placeholder="请输入地址" maxlength="50" show-word-limit/>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>法定代表人身份证号：</b></td>
            <td>
              <el-form-item prop="legalIdcard">
                <el-input v-model.trim="form.legalIdcard" placeholder="请输入法定代表人身份证号" maxlength="20" show-word-limit/>
              </el-form-item>
            </td>
            <td><b>联系电话：</b></td>
            <td>
              <el-form-item prop="mobile">
                <el-input v-model.trim="form.mobile" placeholder="请输入联系电话" maxlength="6" show-word-limit/>
              </el-form-item>
            </td>
          </tr>
        </table>
      </div>

      <div class="el-table__header-wrapper dialog-table">
        <el-button type="primary" style="float:right" @click="addCarInfo"  class="data-btn">增加</el-button>
        <h3 class="title"><i class="el-icon-s-grid"></i>货运车辆基本情况</h3>
        <table cellspacing="0" cellpadding="0" border="0" class="data-table">
          <colgroup>
            <col width="5%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="15%" />
            <col width="10%" />
          </colgroup>
          <tr>
            <th>序号</th>
            <th>运输证号</th>
            <th>车辆牌照</th>
            <th>厂牌型号</th>
            <th>车辆类型</th>
            <th>吨位（吨）</th>
            <th>燃料</th>
            <th>轴数</th>
            <th>技术等级</th>
            <th>操作</th>
          </tr>
          <tbody v-for="(item,index) in form.carList">
          <tr>
            <td>{{index+1}}</td>
            <td>
              <el-form-item prop="transportLicenseNo" >
                <el-input v-model.trim="item.transportLicenseNo" placeholder="请输入运输证号" maxlength="50" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <el-form-item prop="carLicense">
                <el-input v-model.trim="item.carLicense" placeholder="请输入车辆牌照" maxlength="20" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <el-form-item prop="cpxhNum">
                <el-input v-model.trim="item.cpxhNum" placeholder="请输入厂牌型号" maxlength="20" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <el-form-item prop="carType">
                <el-input v-model.trim="item.carType" placeholder="请输入车辆类型" maxlength="50" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <el-form-item prop="weightNum">
                <el-input v-model.trim="item.weightNum" placeholder="请输入吨位（吨）" maxlength="5" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <el-form-item prop="fuel">
                <el-input v-model.trim="item.fuel" placeholder="请输入燃料" maxlength="10" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <el-form-item prop="axesNum">
                <el-input v-model.trim="item.axesNum" placeholder="请输入轴数" maxlength="5" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <el-form-item prop="technicalLevel">
                <el-input v-model.trim="item.technicalLevel" placeholder="请输入技术等级" maxlength="20" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="delCarInfo(index)">删除
              </el-button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <div class="el-table__header-wrapper dialog-table">
        <el-button type="primary" style="float:right" @click="addDirverInfo"  class="data-btn">增加</el-button>
        <h3 class="title"><i class="el-icon-s-grid"></i>驾驶员基本情况</h3>
        <table cellspacing="0" cellpadding="0" border="0" class="data-table">
          <colgroup>
            <col width="5%" />
            <col width="15%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="20%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
          </colgroup>
          <tr>
            <th>序号</th>
            <th>姓名</th>
            <th>性别</th>
            <th>年龄</th>
            <th>文化程度</th>
            <th>取得驾驶证时间</th>
            <th>准驾车型</th>
            <th>从业资格证号</th>
            <th>操作</th>
          </tr>
          <tbody v-for="(item,index) in form.driverList">
          <tr>
            <td>{{index+1}}</td>
            <td>
              <el-form-item prop="driverName" >
                <el-input v-model.trim="item.driverName" placeholder="请输入驾驶员姓名" maxlength="50" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <el-form-item prop="sex">
                <el-input v-model.trim="item.sex" placeholder="请输入性别" maxlength="5" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <el-form-item prop="age">
                <el-input v-model.trim="item.age" placeholder="请输入年龄" maxlength="5" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <el-form-item prop="eduLevel">
                <el-input v-model.trim="item.eduLevel" placeholder="请输入文化程度" maxlength="50" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <el-form-item prop="linceseTime">
                <el-input v-model.trim="item.linceseTime" placeholder="请输入取得驾驶证时间" maxlength="5" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <el-form-item prop="carTypeUse">
                <el-input v-model.trim="item.carTypeUse" placeholder="请输入准驾车型" maxlength="20" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <el-form-item prop="cyzgNum">
                <el-input v-model.trim="item.cyzgNum" placeholder="请输入从业资格证号" maxlength="50" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <el-button style="float:left"
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="delDriverInfo(index)">删除
              </el-button>
            </td>
          </tr>
          </tbody>
        </table>

      </div>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>

  </div>
</template>

<script>
import {saveOrUpdateInfo,getCaseFormInfo} from "@/api/zc/businessManagement/windowAcceptance";
export default {
  components: {},
  name: "dlysxkzForm",
  props: ['caseOid'],
  data() {
    return {
      form:{caseOid:this.caseOid,carList:[],driverList:[]},

    };
  },
  created() {
    if(this.caseOid){
      this.getFormInfo();
    }
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    // 表单重置
    reset() {
      this.form = {
        name: null
      };
      this.resetForm("form");
    },

    //车辆信息
    addCarInfo(){
      let carInfo={transportLicenseNo:"",carLicense:"",cpxhNum:"",carType:"",weightNum:"",fuel:"",axesNum:"",technicalLevel:""}
      this.form.carList.push(carInfo);
    },
    delCarInfo(index){
      this.form.carList.splice(index,1);
    },

    //驾驶员信息
    addDirverInfo(){
      let driver={driverName:"",sex:"",age:"",eduLevel:"",linceseTime:"",carTypeUse:"",cyzgNum:""};
      this.form.driverList.push(driver);
    },

    delDriverInfo(index){
      this.form.driverList.splice(index,1);
    },
    submitForm(){
      saveOrUpdateInfo(this.form).then(response=>{
        let res=response.data;
        if(res.status=='true'){
          this.cancel();
        }else{
          this.$message.error("保存失败！");
        }
      })
    },
    getFormInfo(){
      getCaseFormInfo(this.caseOid).then(response=>{
        let res=response.data;
        if(res){
          this.form.carList=res.carList;
          this.form.driverList=res.driverList;
          this.form.companyName=res.companyName;
          this.form.address=res.address;
          this.form.legalIdcard=res.legalIdcard;
          this.form.mobile=res.mobile;
        }
      })
    },
    cancel(){
      this.$emit("close-bgForm");
    }
  }
}
</script>
<style scoped>
  .el-tree>.el-tree-node{
    min-width:100%;
    display: inline-block;
  }
</style>

