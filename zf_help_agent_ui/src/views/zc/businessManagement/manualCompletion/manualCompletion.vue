/** * @Author: dxl * @Date: 2020-11-16 * @Description: 手动办结 */
<template>
  <div class="app-container">
    <!--区划数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
    >
      <el-form-item label="办件编号" prop="caseNumber">
        <div class="card-item">
          <el-input
            v-model.trim="queryParams.caseNumber"
            placeholder="请输入办件编号"
            clearable
            size="100"
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
      <el-form-item label="申请人" prop="applyUserName">
        <el-input
          v-model.trim="queryParams.applyUserName"
          placeholder="请输入申请人"
          clearable
          size="100"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登记日期">
        <el-date-picker
          v-model.trim="queryParams.startDate"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsStart"
          placeholder="请选择开始日期"
        >
        </el-date-picker>
        -
        <el-date-picker
          v-model.trim="queryParams.endDate"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsEnd"
          placeholder="请选择结束日期"
        >
        </el-date-picker>
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

    <el-table
      v-loading="loading"
      :data="caseRegList"
      border
      :fit="true"
      :height="calcHeight"
    >
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
        label="事项类型"
        align="center"
        prop="serviceTypeName"
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
        label="状态"
        align="center"
        prop="caseStatus"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <p v-if="scope.row.caseStatus == 1">待预审</p>
          <p v-if="scope.row.caseStatus == 2">办理中</p>
          <p v-if="scope.row.caseStatus == 3">办结</p>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="150"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:manual:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:manual:init']"
            >办结</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      :total="queryParams.total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog
      v-dialog-drag
      :visible.sync="openView"
      v-if="openView"
      title="查看办件信息"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <view-case-info
        :caseNumber="indexCaseNumber"
        @view-case="viewCaseClose"
      ></view-case-info>
      <div slot="footer" class="zf-text-center">
        <el-button @click="openView = false"> 关闭 </el-button>
      </div>
    </el-dialog>

    <!-- 办结信息 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="addDialogShow"
      :visible.sync="addDialogShow"
      width="1100px"
      height="600px"
      scrollbar
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>办结状态：</b></td>
            <td colspan="3">
              <el-form-item>
                <el-radio-group
                  v-model="form.finalStatus"
                  @change="handleRowChange"
                >
                  <el-radio :label="40">出证办结</el-radio>
                  <el-radio :label="41">不出证办结</el-radio>
                  <el-radio :label="44">不予批准</el-radio>
                </el-radio-group>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>办结意见：</b></td>
            <td colspan="3">
              <el-form-item prop="finalOpinion">
                <el-input
                  v-model.trim="form.finalOpinion"
                  type="textarea"
                  placeholder="请输入办结意见"
                  maxlength="100"
                  show-word-limit
                ></el-input>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>办结意见说明：</b></td>
            <td colspan="3">
              <el-form-item prop="finalOpinionDesc">
                <el-input
                  v-model="form.finalOpinionDesc"
                  type="textarea"
                  placeholder="请输入意见说明"
                  maxlength="200"
                  show-word-limit
                ></el-input>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>办结结果附件：</b></td>
            <td colspan="3">
              <el-form-item prop="iconName">
                <el-upload
                  class="upload-demo"
                  :action="uploadUrl()"
                  :on-error="uploadError"
                  :before-upload="beforeUpload"
                  :file-list="fileList"
                  :on-success="uploadSuccess"
                >
                  <el-button type="primary" size="small"
                    >上传附件<i class="el-icon-upload el-icon--right"></i
                  ></el-button>
                </el-upload>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div class="zf-zc-table--title">附件列表</div>
      <el-table :data="messageFileList" border style="width: 100%">
        <el-table-column align="center" label="附件名称">
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="附件大小" width="180">
          <template slot-scope="scope">
            <span>{{ scope.row.size }}</span>
          </template>
        </el-table-column>
        <el-table-column width="180" label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="success"
              @click="downloadFile(scope.row.oid)"
              >下载</el-button
            >
            <el-button
              size="mini"
              type="danger"
              @click="handleAttaDelete(scope.$index, scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  saveOrUpdate,
  getOrganCurr,
  checkedCase,
  saveOrUpdateCaseLicenseManage
} from "@/api/zc/businessManagement/manualCompletion.js";
import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
import { uploadFile } from "@/api/sys/atta";
import { getQlCaseExt } from "@/api/zc/businessManagement/viewCaseInfo.js";
import { sxServiceOidsListByUserOid } from "@/api/zc/businessManagement/sxServiceRegistrar";
import Resolution from "@/mixins/resolution.js";
import {
  activeScanningGun,
  openScanningGun
} from "@/api/zc/businessManagement/charge";
export default {
  name: "ManualCompletion",
  mixins: [Resolution],
  components: { viewCaseInfo },
  data() {
    return {
      loading: false,
      queryServiceOids: "",
      // 列表数据
      caseRegList: [],
      attaOids: [],
      //附件列表
      messageFileList: [],
      // 弹窗标题
      dialogTitle: "",
      addDialogShow: false,
      caseManagerInfo: {},

      indexCaseNumber: "",
      openView: false,
      fileList: [],
      organOidCurr: "", //当前登录人机构
      // 表单参数
      form: {
        caseOid: "",
        finalStatus: 40,
        finalOpinion: "",
        finalOpinionDesc: "",
        attaOid: ""
      },
      // 表单校验
      rules: {
        finalOpinion: [
          { required: true, message: "办结意见不能为空", trigger: "blur" }
        ]
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseNumber: "",
        caseStatus: 2,
        organOid: "",
        startDate: null,
        endDate: new Date(),
        sourceApp: 1,
        serviceOids: "",
        applyUserName: "",
        total: 0
      },
      pickerOptionsStart: {
        disabledDate: time => {
          const endDateVal = new Date(this.queryParams.endDate).getTime();
          if (endDateVal) {
            return time.getTime() > endDateVal - 0;
          }
        }
      },
      pickerOptionsEnd: {
        disabledDate: time => {
          const beginDateVal = new Date().getTime();
          if (beginDateVal) {
            return time.getTime() > beginDateVal - 0;
          }
        }
      }
    };
  },
  computed: {
    calcHeight() {
      return (this.resolutionHeight === 1080 &&
        this.resolutionWidth === 1280) ||
        (this.resolutionHeight === 1024 && this.resolutionWidth === 1280)
        ? "calc(100% - 165px)"
        : "calc(100% - 112px)";
    }
  },
  methods: {
    initStartTime() {
      let time = new Date(new Date().getTime() - 14 * 24 * 60 * 60 * 1000);
      this.queryParams.startDate = time;
    },
    handleRowChange(data) {},
    viewCaseClose() {
      this.openView = false;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        if (response.data) {
          this.caseRegList = response.data.data;
          this.queryParams.total = response.data.total;
        }
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.addDialogShow = false;
      this.reset();
    },
    /** 重置按钮操作 */
    resetQuery() {
      Object.assign(this.queryParams, this.$options.data().queryParams);
      this.resetForm("queryForm");
      this.queryParams.serviceOids = this.queryServiceOids;
      this.handleQuery();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form);
      this.resetForm("form");
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.indexCaseNumber = row.caseNumber;
      this.openView = true;
      this.dialogTitle = "查看详情";
    },
    /** 办结操作 */
    handleInit(row) {
      this.reset();
      this.messageFileList = [];
      this.caseManagerInfo.regOid = row.caseOid;
      this.caseManagerInfo.caseNumber = row.caseNumber;
      this.caseManagerInfo.applyUserType = row.applyUserType;
      this.caseManagerInfo.applyUserName = row.applyUserName;
      this.caseManagerInfo.idCard = row.credentialNumber;
      this.caseManagerInfo.applyPhone = row.applyUserPhone;
      this.caseManagerInfo.applyAddress = row.applyUserAddress;
      this.caseManagerInfo.serviceType = row.serviceTypeName;
      this.caseManagerInfo.caseRegisterDate = row.createDate;
      this.caseManagerInfo.serviceOid = row.serviceOid;
      if (row.caseOid) {
        this.form.caseOid = row.caseOid;
        this.addDialogShow = true;
      } else {
        this.addDialogShow = true;
      }
      this.dialogTitle = "办结";
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      if (_that.form.finalStatus != 44) {
        //验证是否存在补齐补正和容缺后补材料未补
        checkedCase(_that.form.caseOid).then(response => {
          if (response.code === 200) {
            if (response.data) {
              _that.$message.error(response.data);
              return false;
            } else {
              _that.saveManualInfo();
            }
          }
        });
      } else {
        _that.saveManualInfo();
      }
    },
    saveManualInfo() {
      let _that = this;
      _that.form.attaOid = this.attaOids.join(",");
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdate(_that.form).then(response => {
            if (response.code === 200) {
              //送达方式
              getQlCaseExt(this.caseManagerInfo.regOid).then(response => {
                let deliverWay = response.data.resultDeliveryWay;
                this.caseManagerInfo.deliverWay = deliverWay;
                //保存出证信息
                if (_that.form.finalStatus == 40) {
                  saveOrUpdateCaseLicenseManage(this.caseManagerInfo).then(
                    response => {
                      if (response.data == 200) {
                      }
                    }
                  );
                }
              });

              _that.msgSuccess("办结成功");
              _that.addDialogShow = false;
              setTimeout(() => {
                _that.getList();
              }, 10);
            }
          });
        }
      });
    },
    //删除附件
    handleAttaDelete(index, row) {
      this.messageFileList.splice(index, 1);
      this.attaOids.splice(index, 1);
    },
    //下载附件
    downloadFile(attaOid) {
      this.download(attaOid);
    },
    //成功后返回
    uploadSuccess(resp) {
      this.fileList = [];
      if (200 !== resp.code) {
        return this.msgError(resp.message);
      }
      const filem = {
        oid: resp.data.oid,
        name: resp.data.name,
        size: resp.data.size,
        url: resp.data.url
      };
      this.attaOids.push(resp.data.oid);
      this.messageFileList.push(filem);
    },
    //失败后返回
    uploadError(resp) {
      this.msgError("文件上传失败");
    },
    //上传之前
    beforeUpload(file) {
      if (
        file.name.indexOf("%00") > -1 ||
        file.name.indexOf("./") > -1 ||
        file.name.indexOf(".\\") > -1
      ) {
        this.msgError("上传文件名称非法！");
        return false;
      }
      const fileSize = file.size;
      if (0 == fileSize) {
        this.msgError("上传文件不能为空！");
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 100;
      if (!isLt2M) {
        this.msgError("上传文件大小不能超过 100MB！");
      }
      return isLt2M;
    },
    uploadUrl() {
      return uploadFile();
    },
    //查询所有授权事项
    getRegSxServiceOids() {
      sxServiceOidsListByUserOid().then(respon => {
        if (respon.code === 200) {
          if (respon.data) {
            this.queryParams.serviceOids = respon.data.join(",");
            this.queryServiceOids = respon.data.join(",");
          }
          this.getList();
        }
      });
    },

    //打开扫码枪
    openScan() {
      openScanningGun().then(res => {
        if (res.StateCode == 0) {
          //成功后激活扫码枪
          activeScanningGun()
            .then(response => {
              if (response.StateCode == 0) {
                this.$message.success("扫描成功");
                this.queryParams.caseNumber = response.data;
              } else if (response.StateCode == -1) {
                this.$message.error("扫码枪扫描超时");
              } else if (response.StateCode == -2) {
                this.$message.error("扫码枪没有打开");
              }
            })
            .catch(err => {
              console.log(err);
            });
        } else if (res.StateCode == -1) {
          this.$message.error(res.tips);
        } else if (res.StateCode == -4) {
          //扫码枪已打开
          activeScanningGun()
            .then(response => {
              if (response.StateCode == 0) {
                this.$message.success("扫描成功");
                this.queryParams.caseNumber = response.data;
              } else if (response.StateCode == -1) {
                this.$message.error("扫码枪扫描超时");
              } else if (response.StateCode == -2) {
                this.$message.error("扫码枪没有打开");
              }
            })
            .catch(err => {
              console.log(err);
            });
        } else {
          this.$message.error("加载配置失败");
        }
      });
    }
  },
  created() {
    this.initStartTime();
    this.getRegSxServiceOids();
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
</style>
