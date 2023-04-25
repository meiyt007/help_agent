/**
* @Author: wangns
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--事项数据-->
      <el-col :span="24" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="120px"
        >
          <el-form-item label="一件事目录名称" prop="comboDirectoryName">
            <el-input
              v-model.trim="queryParams.comboDirectoryName"
              placeholder="请输入一件事目录名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="一件事分类名称" prop="themeName">
            <el-input
              v-model.trim="queryParams.themeName"
              placeholder="请输入一件事分类名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="服务对象" prop="comboServiceObject">
            <el-select
              clearable
              filterable
              v-model="queryParams.comboServiceObject"
              placeholder="请选择"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
              >搜索</el-button
            >
            <el-button
              type="warning"
              icon="el-icon-refresh"
              size="mini"
              @click="resetQuery"
              class="btn-reset"
              >重置</el-button
            >
          </el-form-item>
        </el-form>
        <div class="container">
          <div
            class="table-container"
            :class="{ tableInfo: true }"
          >
            <el-table
              v-loading="loading"
              :data="serviceList"
              border
              :fit="true"
            >
              <el-table-column
                label="序号"
                width="55"
                type="index"
                align="center"
              >
                <template slot-scope="scope">
                  <span>{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="一件事目录名称"
                align="center"
                prop="comboDirectoryName"
                :show-overflow-tooltip="true"
              />
              <el-table-column
                label="一件事分类名称"
                align="center"
                prop="themeName"
                :show-overflow-tooltip="true"
              />
              <el-table-column
                label="一件事编码"
                align="center"
                prop="comboDirectoryCode"
                :show-overflow-tooltip="true"
              />
              <el-table-column
                label="服务对象"
                align="center"
                prop="comboServiceObjectName"
                :show-overflow-tooltip="true"
              />
              <el-table-column
                label="操作"
                align="center"
                class-name="small-padding fixed-width"
                min-width="100"
              >
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    v-if="isShowPersonalRegisterCase(scope.row)"
                    type="text"
                    icon="iconfont zfsoft-xiugai"
                    @click="registerCase(scope.row, '0')"
                    v-hasPermi="['sys:comboAccept:update']"
                  >
                    个人登记
                  </el-button>
                  <el-button
                    size="mini"
                    v-if="isShowBusinessRegisterCase(scope.row)"
                    type="text"
                    icon="iconfont zfsoft-xiugai"
                    @click="registerCase(scope.row, '1')"
                    v-hasPermi="['sys:comboAccept:update']"
                  >
                    法人登记
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <pagination
              v-show="total > 0"
              :total="total"
              :page.sync="queryParams.pageNumber"
              :limit.sync="queryParams.pageSize"
              @pagination="getList(null)"
            />
          </div>
        </div>
      </el-col>
    </el-row>

    <!--一件事办件一次性告知单开始-->
    <el-dialog
      v-dialog-drag
      :visible.sync="data.show"
      v-for="data in onceNoticeOptions"
      :key="data.comboDireOid"
      :title="data.title"
      width="1100px"
      height="700px"
      scrollbar
      :close-on-click-modal="false"
      append-to-body
    >
      <combo-once-notice
        :comboDireOid="data.comboDireOid"
        :title="data.title"
        @father-click="closeChild"
      ></combo-once-notice>
    </el-dialog>
    <!--一件事办件一次性告知单结束-->
    <!--一件事办件登记受理开始-->
    <el-dialog
      v-dialog-drag
      :visible.sync="initShow"
      title="一件事办件添加"
      v-if="initShow"
      :close-on-click-modal="false"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <init-combo-case
        :comboDireOid="comboDirectoryOid"
        :pCegisterType="pCegisterType"
        @closeUpdate="closeUpdate"
      ></init-combo-case> 
    </el-dialog>
    <!--一件事办件登记受理结束-->

    <!-- 智能问答 -->
<!--    <div
      v-if="!isWindowAcceptance && znzxFlag"
      v-show="artificialService"
      :class="[
        'intelligent-container',
        commonVisible ? 'intelligent-container&#45;&#45;visible' : '',
        isNotFullScreen ? 'intelligeng-container&#45;&#45;not-fullscreen' : '',
      ]"
    >
      <div class="artificial-service clearfix">
        <div class="as-hd">
          <div class="as-hd-lt fl">
            <img src="@/assets/image/service.png" />
            <span>智能问答</span>
          </div>
          <div class="as-hd-rt fr">
            <el-switch
              v-model="microphoneSwitch"
              active-text="麦克风已开启"
              inactive-text="麦克风已关闭"
              @change="onMicrophoneSwitch"
            ></el-switch>
            <img
              @click="foldingArtificialService"
              width="24"
              src="@/assets/image/folding.png"
            />
          </div>
        </div>
        <div class="as-main" ref="asMain">
          <div
            class="talk-item"
            v-for="(item, index) in intelligentMsgList"
            :key="index"
          >
            <LeftItem
              :id="item.id"
              :type="item.type"
              :content="item.content"
              v-if="item.me"
            />
            <RightItem
              :id="item.id"
              :type="item.type"
              :content="item.content"
              :message="item.message"
              v-else
              @radioSelect="radioListfunction"
              @answerClick="handleAnswerClick"
            />
          </div>
        </div>
      </div>
    </div>-->

    <!-- 智能问答 button -->
<!--    <div
      v-if="!isWindowAcceptance && znzxFlag"
      class="intelligent"
      v-show="!artificialService"
      @click="handleArtificialService"
    >
      <img width="43" height="130" src="@/assets/image/IntelligentQA.png" />
    </div>-->

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
import { pageComboOfUser, downGzcnsWord } from "@/api/onething/comboManager/comboAccept/comboAccept";
import comboOnceNotice from '@/views/onething/comboManager/comboAccept/comboOnceNotice';
import initComboCase from "@/views/onething/comboManager/comboAccept/initComboCase";
import CommonDialog from "./dialogs/common-dialog.vue";
import LeftItem from "./components/intelligentTalk/LeftItem";
//import RightItem from "./components/intelligentTalk/RightItem";
//import IntelligentConsultation from '@/mixins/intelligentConsultation.js';
import {
  getloginUser,
  getFileDownPath,
  downloadPrintFile,
} from "@/api/zc/businessManagement/windowAcceptance";
import Utils from "@/assets/js/util.js";
import { mapGetters } from "vuex";
export default {
  name: "ComboAccept",
  //mixins: [IntelligentConsultation],
  components: { Treeselect, comboOnceNotice, initComboCase, CommonDialog, LeftItem },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      serviceList: [],

      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      initShow: false,
      // 查看显示弹出层
      openView: false,
      caseDialogOptions: [],
      onceNoticeOptions: [],
      comboDirectoryOid: '',
      comboDirectoryName: '',
      pCegisterType: '',
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        comboDirectoryName: "",
        themeName: "",
        comboServiceObject: "",
        status: "1",
        comboDirectoryOids: []
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
  computed: {
    ...mapGetters([
      "wgpjFlag"
    ]),
  },
  created () {
    this.getList();
    this.queryLoginInfo();
  },
  methods: {
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

    // 是否展示个人登记按钮
    isShowPersonalRegisterCase (row) {
      return row.comboServiceObject && row.comboServiceObject.indexOf("1") != -1;
    },
    /** 登录信息 */
    queryLoginInfo () {
      getloginUser().then(response => {
        this.loginUser = response.data;
      });
    },

    /** 查询列表 */
    getList (oids) {
      this.loading = true;
      return pageComboOfUser({ ...this.queryParams, comboDirectoryOids: oids || this.queryParams.comboDirectoryOids }).then(response => {
        this.serviceList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
        return Promise.resolve(response);
      });
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset () {
      Object.assign(this.form, this.$options.data().form)
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNumber = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 一次性告知 */
    onceNoticeForm (row) {
      let _that = this;
      //_that.onceNoticeOptions = [];
      let onceNotice = { comboDireOid: row.comboDirectoryOid, show: true, title: '一次性告知单' };
      _that.onceNoticeOptions.push(onceNotice);
    },
    closeChild () {//关闭验证规则页面
      this.onceNoticeOptions = [];
      let item = { comboDireOid: comboDireOid, show: false, title: '一次性告知单' };
      this.onceNoticeOptions.push(item);
    },
    /**一件事办件添加**/
    handleAccept (row, pCegisterType) {
      this.comboDirectoryOid = row.comboDirectoryOid;
      this.pCegisterType = pCegisterType;
      this.initShow = true;
    },
    // 关闭按钮
    closeUpdate () {
      this.comboDirectoryOid = '';
      this.pCegisterType = '';
      this.initShow = false;
      this.getList();
    },

    // 点击弹框
    registerCase (row, pCegisterType, artificialService) {
      this.comboDirectoryOid = row.comboDirectoryOid;
      this.comboDirectoryName = row.comboDirectoryName;
      this.commonVisible = true;

      this.serviceRoot = [this.isShowPersonalRegisterCase(row), this.isShowBusinessRegisterCase(row)];
        this.pCegisterType = pCegisterType;
        this.comeFormArtific = false;
     // }
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
        return this.$router.push("materialOut");
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
          this.$router.push("materialOut");
        });
    },
    //调用c++服务打印
    handlePrintAll (obj) {
      getFileDownPath(obj).then(response => {
        if (response.data) {
          let url = response.data;
          downloadPrintFile(url).then(res => {
            // 跳转到材料出库
            this.$router.push("materialOut");
          });
          //window.location.href ='http://172.168.253.56:10001/Download?url='+response.data;
        } else {
          // 跳转到材料出库
          this.$router.push("materialOut");
        }
      });
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

  }
};
</script>
<style lang="scss" scoped>
@import '@/views/zc/businessManagement/windowAcceptance/smartRegistration.scss';
.el-tree > .el-tree-node {
  min-width: 100%;
  display: inline-block;
}
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
@page {
  margin-bottom: 1mm;
}
</style>
