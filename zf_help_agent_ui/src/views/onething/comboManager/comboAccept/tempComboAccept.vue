/**
* @Author: wangxl
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
          label-width="108px"
        >
          <el-form-item label="申请项目名称" prop="projectName">
            <el-input
              v-model.trim.trim="queryParams.projectName"
              placeholder="申请项目名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="申请人" prop="applyUserName">
            <el-input
              v-model.trim="queryParams.applyUserName"
              placeholder="请输入申请人"
              clearable
              size="100"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="办件编号" prop="caseNumber">
            <div class="card-item">
              <el-input
                v-model.trim="queryParams.caseNumber"
                placeholder="请输入办件编号"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              >
                <!-- <template slot="append"
                ><el-button
                  class="scan-btn"
                  type="primary"
                  @click="openScan"
                ></el-button
                ></template> -->
              </el-input>
            </div>
          </el-form-item>
          <!--<el-form-item label="登记时间">
            <el-date-picker
              v-model="queryParams.startDate"
              type="date" value-format="yyyy-MM-dd"
              placeholder="请选择开始日期">
            </el-date-picker>-
            <el-date-picker
              v-model="queryParams.endDate"
              type="date" value-format="yyyy-MM-dd"
              placeholder="请选择结束日期">
            </el-date-picker>
          </el-form-item>-->
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

        <el-table v-loading="loading" :data="comboCaseList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="申请项目名称"
            align="center"
            prop="projectName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="办件编号"
            align="center"
            prop="caseNumber"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="申请人"
            align="center"
            prop="applyUserName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="证件号"
            align="center"
            prop="credentialNumber"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="登记日期"
            align="center"
            prop="createDate"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="操作"
            align="center"
            width="250"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <!-- <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-dengji"
                @click="handleAccept(scope.row, 0)"
                v-hasPermi="['sys:comboAccept:update']"
                >修改
              </el-button> -->
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-dengji"
                @click="handleAccept2(scope.row, 0)"
                v-hasPermi="['sys:comboAccept:update']"
              >
                修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-dengji"
                @click="viewAccept(scope.row, 1)"
                v-hasPermi="['sys:comboAccept:update']"
                >查看
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-dengji"
                @click="initCaseAbolish(scope.row)"
                v-hasPermi="['sys:comboAccept:update']"
              >
                作废
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNumber"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>
    <!--一件事办件修改开始-->
    <el-dialog
      v-dialog-drag
      :visible.sync="updateCaseshow"
      title="一件事办件修改"
      v-if="updateCaseshow"
      width="1100px"
      height="700px"
      scrollbar
      :close-on-click-modal="false"
      :modal-append-to-body="false"
      append-to-body
    >
      <update-combo-case
        :comboCaseOid="comboCaseOid"
        :comboDireOid="comboDirectoryOid"
        @closeUpdate="closeUpdate"
      ></update-combo-case>
    </el-dialog>
    <!--一件事办件修改结束-->

    <!-- 一件事办件查看开始 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="caseOpenView"
      v-if="caseOpenView"
      @close="closeOpenView"
      :title="title"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <view-combo-case-new
        :caseOid="indexCaseOid"
        @view-case="closeOpenView"
      ></view-combo-case-new>
      <div slot="footer" class="zf-text-center">
        <el-button @click="closeOpenView">关 闭</el-button>
      </div>
    </el-dialog>
    <!--一件事办件查看结束-->

    <el-dialog
      v-dialog-drag
      :title="title"
      :visible.sync="abolishInit"
      width="900px"
      :close-on-click-modal="false"
      append-to-body
    >
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="140px">
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <el-input v-show="false" v-model="form.caseOid" />
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>是否需要退件：</b></td>
              <td colspan="3">
                <el-radio-group v-model="form.isTjFlag">
                  <el-radio label="1">需要</el-radio>
                  <el-radio label="2">不需要</el-radio>
                </el-radio-group>
              </td>
            </tr>
            <tr>
              <td><b>作废原因：</b></td>
              <td colspan="3">
                <el-form-item prop="acceptOpinionDesc">
                  <el-col :span="24">
                    <el-input
                      type="textarea"
                      v-model.trim="form.acceptOpinionDesc"
                      maxlength="500"
                      show-word-limit
                    ></el-input>
                  </el-col>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="caseAbolish()">确定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <TempCommonDialog
      v-if="commonVisible"
      ref="commonDialog"
      :commmonVisible.sync="commonVisible"
      :pCegisterType="pCegisterType"
      :comboDirectoryOid="comboDirectoryOid"
      :comboDirectoryName="comboDirectoryName"
      :comboCaseOid="comboCaseOid"
      :comboCaseId="comboCaseId"
      :loginUser="loginUser"
      :comboCaseNumber="comboCaseNumber"
      isTempComponent
      @case-close="closeCaseRegisterView"
    />
  </div>
</template>
<script>
import { VueCropper } from "vue-cropper";
import { pageComboCase, saveCaseAbolish, breakComboCase, enterDishonestPerson, dishonestPersonByUser } from "@/api/onething/comboManager/comboAccept/tempComboAccept";
import updateComboCase from "@/views/onething/comboManager/comboAccept/updateComboCase";
import ViewComboCaseNew from "@/views/onething/comboManager/comboAccept/viewComboCaseNew";
import { checkTimeExpire } from "@/api/onething/comboManager/comboAccept/combocaseRqbz";
/** 组件 */
import TempCommonDialog from './dialogs/common-dialog.vue';

import { downloadPrintFile, getFileDownPath, getloginUser, } from "@/api/zc/businessManagement/windowAcceptance";
import { downGzcnsWord } from "@/api/onething/comboManager/comboAccept/comboAccept";
export default {
  name: "TempComboAccept",
  components: { updateComboCase, ViewComboCaseNew, VueCropper, TempCommonDialog},
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      comboCaseList: [],
      comboCaseOid: '',
      comboCaseId: '',
      comboCaseNumber: '',
      comboDirectoryOid: '',
      pCegisterType: 0,
      comboDirectoryName: '',
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      abolishInit: false,
      updateCaseshow: false,
      // 查看显示弹出层
      openView: false,
      // 办件查看
      caseOpenView: false,
      indexCaseOid: '',
      caseDialogOptions: [],
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        projectName: "",
        applyUserName: "",
        caseNumber: ""
      },
      // 表单参数
      form: {
        isTjFlag: "2",
        acceptOpinionDesc: "",
      },
      // 表单校验
      rules: {
        acceptOpinionDesc: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
      },

      commonVisible: false,

      loginUser: {}

    };
  },
  created () {
    this.getList();
    this.queryLoginInfo();
  },
  methods: {
    /** 登录信息 */
    queryLoginInfo () {
      getloginUser().then(response => {
        this.loginUser = response.data;
      });
    },
    /** 查询列表 */
    getList () {
      this.loading = true;
      pageComboCase(this.queryParams).then(response => {
        this.comboCaseList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    /**一件事办件修改**/
    handleAccept (row, type) {
      let _that = this;
      checkTimeExpire(row.caseOid).then(response => {
        if (response.data.message == true) {
          this.$confirm('此办件容缺补正时间已超期，办件中止！', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function () {
            breakComboCase(row.caseOid).then(res => {
              if (res.data != "") {
                _that.msgSuccess("办件中止成功！");
                _that.getList();
              }
            })
          })
        } else {
          this.comboCaseOid = row.caseOid;
          this.comboDirectoryOid = row.comboDireOid;
          this.comboDirectoryName = row.projectName;
          this.pCegisterType = row.applyUserType;
          this.updateCaseshow = true;
        }
      });
    },
    handleAccept2 (row, type) {
      let _that = this;
      checkTimeExpire(row.caseOid).then(response => {
        if (response.data.message == true) {
          this.$confirm('此办件容缺补正时间已超期，办件中止！', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            breakComboCase(row.caseOid).then(res => {
              if (res.data != "") {
                _that.msgSuccess("办件中止成功！");
                _that.getList();
              }
            })
          })
        } else {
          this.comboCaseOid = row.caseOid;
          this.comboCaseId = row.id;
          this.comboDirectoryOid = row.comboDireOid;
          this.comboDirectoryName = row.projectName;
          this.pCegisterType = row.applyUserType;
          this.comboCaseNumber = row.caseNumber;
          this.commonVisible = true;
        }
      });
    },
    /** 办件作废 */
    initCaseAbolish (row) {
      //const oid = row.caseOid;
      //getOne(oid).then(response => {
      let _that = this;
      _that.form.caseOid = row.caseOid;
      _that.form.acceptOpinionDesc = null;
      _that.abolishInit = true;
      _that.title = "作废一件事办件";
      //});
    },
    /** 办件作废 */
    caseAbolish () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.isTjFlag == "1") {
            this.form.caseStatus = 5;
          } else {
            this.form.caseStatus = -1;
          }
          saveCaseAbolish(this.form).then(response => {
            if (response.data != "") {
              checkTimeExpire(this.form.caseOid).then(response => {
                if (response.data.message == true) {
                  this.dishonestPersonList(this.form.caseOid);
                }
              })
              this.abolishInit = false;
              this.form.acceptOpinionDesc = null;
              this.msgSuccess("办件作废成功！");
              this.getList();
            }
          });
        }
      });
    },
    /** 失信人名单 */
    dishonestPersonList (caseOid) {
      enterDishonestPerson(caseOid).then(response => {
        if (response.data != "") {
          this.msgSuccess("该申请人被纳入失信人名单！");
        }
      })
    },
    closeUpdate () {
      this.updateCaseshow = false;
      this.getList();
    },
    /** 取消按钮 */
    cancel () {
      this.abolishInit = false;
      this.form.acceptOpinionDesc = null;
    },
    /** 办件查看按钮操作 */
    viewAccept (row) {
      this.indexCaseOid = row.caseOid;
      this.caseOpenView = true;
      this.title = "一件事暂存办件信息";
    },
    /** 办件查看关闭按钮操作 */
    closeOpenView () {
      this.caseOpenView = false;
    },

    /*closeCaseRegisterView () {
      this.commonVisible = false;
    }*/
    // 办件修改关闭按钮
    closeCaseRegisterView (obj) {
      // this.closeCamera();
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
        });
      this.commonVisible = false;
      this.getList();
    },
    //调用c++服务打印
    handlePrintAll (obj) {
      getFileDownPath(obj).then(response => {
        if (response.data) {
          let url = response.data;
          downloadPrintFile(url).then(res => {
            // 跳转到材料出库
            // obj.isAllowMaterialOut && this.$router.push("materialOut");
          });
          //window.location.href ='http://172.168.253.56:10001/Download?url='+response.data;
        } else {
          // 跳转到材料出库
          // obj.isAllowMaterialOut && this.$router.push("materialOut");
        }
      });
    },
    downGzWord (obj) {
      downGzcnsWord(obj).then(response => {
        let url = response.data;
        //调用C++的打印接口
        /*downloadPrintFile(url).then(res => {
          console.log(res.StateCode)
          if (res.StateCode != 0) {
            if (res.StateCode == -3) {
              this.$message.error("打开文档失败!");
            } else if (res.StateCode == -5) {
              this.$message.error("下载失败!");
            }
          }
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
.card-item {
  .el-form-item {
    position: relative;
  }
  .el-input {
    margin-bottom: 0px;
    border-radius: 3px;
    &:last-child {
      margin-bottom: 0;
    }
  }
}
.scan-btn {
  position: absolute;
  height: 30px;
  background: #c1d0d9 url(~@/assets/image/scan-icon.png) no-repeat center center !important;
  border: none;
  box-shadow: none;
  top: 10px;
  border-radius: 1px;
}
::v-deep .el-form-item__content {
  margin-left: 0 !important;
}
</style>
