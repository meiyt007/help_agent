/**
* @Author: wangns
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--事项数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="120px">
          <el-form-item label="业态目录名称" prop="comboDirectoryName">
            <el-input v-model.trim="queryParams.comboDirectoryName" placeholder="请输入业态目录名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="业态分类名称" prop="themeName">
            <el-input v-model.trim="queryParams.themeName" placeholder="请输入业态分类名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="服务对象" prop="comboServiceObject">
            <el-select clearable filterable  v-model="queryParams.comboServiceObject" placeholder="请选择">
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>

        <el-table v-loading="loading" :data="districtList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="业态目录名称"  align="center" prop="comboDirectoryName" :show-overflow-tooltip="true"/>
          <el-table-column label="业态分类名称"  align="center" prop="themeName" :show-overflow-tooltip="true"/>
          <el-table-column label="业态编码"  align="center" prop="comboDirectoryCode" :show-overflow-tooltip="true"/>
          <el-table-column label="服务对象"  align="center" prop="comboServiceObjectName" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" v-if="scope.row.comboServiceObject === '1'" type="text" icon="iconfont zfsoft-xiugai"  @click="registerCase(scope.row,'0')" v-hasPermi="['sys:comboAccept:update']">个人登记</el-button>
              <el-button size="mini" v-else type="text" icon="iconfont zfsoft-xiugai"  @click="registerCase(scope.row,'1')" v-hasPermi="['sys:comboAccept:update']">法人登记</el-button>
              <!-- <el-button size="mini" type="text" icon="el-icon-s-order" @click="onceNoticeForm(scope.row)" v-hasPermi="['sys:comboAccept:update']" >一次性告知单</el-button> -->
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNumber" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>

    <!--一业一证一次性告知单开始-->
    <el-dialog v-dialog-drag :visible.sync="data.show" v-for="(data,index) in onceNoticeOptions" :key='index' :title="data.title" width="1100px" height='700px' scrollbar :close-on-click-modal="false" append-to-body>
        <industry-case-notice :comboDireOid="data.comboDireOid" :title="data.title"  @father-click="closeChild" ></industry-case-notice>
    </el-dialog>
    <!--一业一证一次性告知单结束-->
    <!--一业一证登记受理开始-->
    <el-dialog v-dialog-drag :visible.sync="initShow"  title="一业一证添加" v-if="initShow" :close-on-click-modal="false"  width="1100px" height='700px' scrollbar append-to-body>
        <init-industry-case :comboDireOid="comboDirectoryOid" :pCegisterType="pCegisterType" @view-case="closeCaseView"></init-industry-case>
    </el-dialog>
    <!--一业一证登记受理结束-->
      <CommonDialog
      v-if="commonVisible"
      ref="commonDialog"
      :commmonVisible.sync="commonVisible"
      :loginUser="loginUser"
      :pCegisterType.sync="pCegisterType"
      :comboDirectoryOid="comboDirectoryOid"
      :comboDirectoryName="comboDirectoryName"
      :serviceRoot="serviceRoot"
      :comeFormArtific="comeFormArtific"
      @case-close="closeCaseRegisterView"
    />
  </div>
</template>

<script>
// import the styles
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import Treeselect from '@riophae/vue-treeselect';
import { downGzcnsWord } from "@/api/onelicence/industryManager/industryCaseAccept/industryCaseAccept";
import {pageIndustryOfUser} from "@/api/onelicence/industryManager/industryCaseAccept/industryCaseAccept";
import industryCaseNotice from '@/views/onelicence/industryManager/industryCaseAccept/industryCaseNotice';
import initIndustryCase from "@/views/onelicence/industryManager/industryCaseAccept/initIndustryCase";
import CommonDialog from "./dialogs/common-dialog.vue";
import {
  getloginUser,
  getFileDownPath,
  downloadPrintFile,
} from "@/api/zc/businessManagement/windowAcceptance";
export default {
  name: "IndustryCaseAccept",
  components: {Treeselect,industryCaseNotice,initIndustryCase,CommonDialog},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      districtList: [],

      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      initShow:false,
      // 查看显示弹出层
      openView: false,
      caseDialogOptions:[],
      onceNoticeOptions:[],
      comboDirectoryOid:'',
      comboDirectoryName: '',
      pCegisterType:'',
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        comboDirectoryName: "",
        themeName:"",
        comboServiceObject:"",
        status:"1"

      },
      // 选项
      options: [{
        value: '1',
        label: '自然人'
      }, {
        value: '2',
        label: '企业法人'
      }, {
        value: '3',
        label: '事业法人'
      }, {
        value: '4',
        label: '社会组织法人'
      }, {
        value: '5',
        label: '非法人企业'
      }, {
        value: '6',
        label: '行政机关'
      }, {
        value: '9',
        label: '其他组织'
      }],
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
       // 弹框是否打开
      commonVisible: false,
      loginUser: {},

      serviceRoot: [false, false], // 服务对象权限
      comeFormArtific: false

    };
  },
  created() {
    this.getList();
    this.queryLoginInfo();
  },
  methods: {

    /** 查询列表 */
    getList() {
      this.loading = true;
      pageIndustryOfUser(this.queryParams).then(response => {
        this.districtList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form)
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNumber = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 一次性告知 */
    onceNoticeForm(row) {
      let _that = this;
      //_that.onceNoticeOptions = [];
      let onceNotice = {comboDireOid:row.comboDirectoryOid,show:true,title:'一次性告知单'};
      _that.onceNoticeOptions.push(onceNotice);
    },
    closeChild() {//关闭验证规则页面
      this.onceNoticeOptions=[];
      let item = {comboDireOid:comboDireOid,show:false,title:'一次性告知单'};
      this.onceNoticeOptions.push(item);
    },
    /**一业一证添加**/
    handleAccept(row,pCegisterType){
      this.comboDirectoryOid=row.comboDirectoryOid;
      this.pCegisterType=pCegisterType;
      this.initShow=true;
    },
    // 关闭按钮
    closeCaseView() {
      this.comboDirectoryOid='';
      this.pCegisterType='';
      this.initShow=false;
      this.getList();
    },
    // 是否展示个人登记按钮
    isShowPersonalRegisterCase (row) {
      return row.comboServiceObject && row.comboServiceObject.indexOf("1") != -1;
    },
    // 是否展示法人登记按钮
    isShowBusinessRegisterCase (row) {
      return (
        row.comboServiceObject &&
        (row.comboServiceObject.indexOf("2") != -1 ||
          row.comboServiceObject.indexOf("3") != -1 ||
          row.comboServiceObject.indexOf("4") != -1 ||
          row.comboServiceObject.indexOf("5") != -1 ||
          row.comboServiceObject.indexOf("6") != -1 ||
          row.comboServiceObject.indexOf("9") != -1)
      );
    },
    registerCase(row,pCegisterType){
      this.comboDirectoryOid = row.comboDirectoryOid;
      this.comboDirectoryName = row.comboDirectoryName;
      this.commonVisible = true;
      this.serviceRoot = [this.isShowPersonalRegisterCase(row), this.isShowBusinessRegisterCase(row)];
      this.pCegisterType = pCegisterType;
      this.comeFormArtific = false;
    },
    // 办件修改关闭按钮
    closeCaseRegisterView (obj) {
      //sky
      //this.$nextTick(this.closeCamera);
      let mesTitle = "";
      //当obj不为空时打印
      if (obj && obj.isRqslFlag) {
        mesTitle = "是否打印容缺补正受理通知书?";
      } else if (obj && obj.isSl == "false" && obj.caseNumber) {
        mesTitle = "是否打印不予受理通知单?";
      } else if (obj && obj.isSl == "true") {
        mesTitle = "是否打印受理通知单?";
      } else if (obj && obj.isGzSl == "true") {
        mesTitle = "是否打印告知承诺单?";
      }
      if (!mesTitle) {
        this.getList();
        return;
        //return this.$router.push("materialOut");
      }
      this.$confirm(mesTitle, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        type: "warning"
      })
        .then(() => {
            this.downGzWord(obj);
        })
        .catch(action => {
          if (action === "cancel") {
            console.log("打印取消...");
          }
          //this.$router.push("industryCasematerialOut");
        });
      this.getList();
    },
    downGzWord (obj) {
      downGzcnsWord(obj).then(response => {
        let url = response.data;
        /*downloadPrintFile(url).then(res => {
          console.log(res.StateCode)
          if(res.StateCode!=0){
            if(res.StateCode==-3){
              this.$message.error("打开文档失败!");
            }else if(res.StateCode==-5){
              this.$message.error("下载失败!");
            }
          }
          // 跳转到材料出库
          this.$router.push("materialOut");
        });*/
        //调用pageoffice的打印接口
        POBrowser.openWindowModeless(
          process.env.VUE_APP_BASE_API_PAGE +
          "/manage/zhuozheng/pageOfficePrint?fileUrl="+url, "width=1200px;height=800px;"
        );
      })
    },
        /** 登录信息 */
    queryLoginInfo () {
      getloginUser().then(response => {
        this.loginUser = response.data;
      });
    },

  }
};
</script>
<style lang="scss" scoped>
.dialog-table {
  padding: 20px;
  box-sizing: border-box;
}
.dialog-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}
.dialog-table table {
  width: 100%;
}
.dialog-table table {
  border: 1px solid #dfe6ec;
  border-collapse: collapse;
}

.dialog-table table tr td {
  border: 1px solid #dfe6ec;
  padding: 17px 8px;
  font-size: 12px;
  color: #515a6e;
  text-align: right;
}
.dialog-table table tr td:nth-of-type(2n) {
  color: #606266;
  text-align: left;
}
.dialog-table .el-form-item {
  margin-bottom: 0;
}
.el-form-item--small .el-form-item_content {
  margin-left: 0px !important;
}
.line {
  text-align: center;
}
</style>
