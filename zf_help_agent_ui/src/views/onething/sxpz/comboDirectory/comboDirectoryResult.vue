/** * @Author: wangxl */
<template>
  <div>
    <el-tabs>
      <div class="zf-zc-table--title">统一证照</div>

      <el-row :gutter="10" class="mb8 fl">
        <el-col :span="1.5">
          <el-button
            type="default"
            icon="el-icon-plus"
            size="mini"
            @click="handleInit"
            v-hasPermi="['combo:directory:init']"
            >新增</el-button
          >
          <el-button
            type="default"
            icon="el-icon-plus"
            size="mini"
            @click="setResultStatus"
            v-hasPermi="['combo:directory:init']"
            >确认证照</el-button
          >
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="directoryResultList" border>
        <el-table-column label="序号" width="80" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="resultName"
          label="证照名称"
          align="center"
          width="300"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          prop="resultSampleName"
          label="证照样本"
          align="center"
          width="250"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          prop="status"
          label="状态"
          :formatter="getStatus"
          align="center"
          width="100"
        />
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-xiugai"
              @click="handleInit(scope.row)"
              v-hasPermi="['combo:directory:update']"
              >修改</el-button
            >

            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-xiugai"
              @click="handleInitMater(scope.row)"
              v-hasPermi="['combo:directory:update']"
              >证照目录配置</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-shanchu"
              @click="handleDelete(scope.row)"
              v-hasPermi="['combo:directory:delete']"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div class="zf-zc-table--title">事项证照信息</div>

      <!-- <el-row :gutter="20">
        <el-col :span="24" :xs="24"> -->
      <el-row :gutter="10" class="mb8 fl">
        <el-col :span="1.5">
          <el-button
            type="default"
            icon="el-icon-plus"
            size="mini"
            @click="handleResultInit"
            v-hasPermi="['combo:directory:init']"
            >新增</el-button
          ><label style="color: #bd2c00;"
            >（如果您还需要展示涉及事项证照，请在这里进行操作！）</label
          >
        </el-col>
      </el-row>
      <el-table
        ref="multipleTable"
        :data="comboSxResultList"
        style="width: 100%"
        border
      >
        <el-table-column label="序号" width="80" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="serviceName"
          label="事项名称"
          align="center"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          prop="resultName"
          label="证照名称"
          align="center"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          prop="resultSampleName"
          label="证照样本"
          align="center"
          :show-overflow-tooltip="true"
        >
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="200"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <!--<el-button
                  size="mini"
                  type="text"
                  icon="iconfont zfsoft-chakan"
                  @click="viewFile(scope.row)"
                  v-if="scope.row.resultSampleAddr != ''"
                  v-hasPermi="['combo:directory:view']"
                >预览</el-button>-->

            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-xiugai"
              @click="handleInitMaterSx(scope.row)"
              v-hasPermi="['combo:directory:update']"
              >证照目录配置</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-shanchu"
              @click="handleSxDelete(scope.row)"
              v-hasPermi="['combo:directory:delete']"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <!-- </el-col>
      </el-row> -->

      <!-- 添加或修改信息对话框 -->
      <el-dialog
        v-dialog-drag
        :title="title"
        :visible.sync="openInitMater"
        v-if="openInitMater"
        width="1100px"
        append-to-body
        @close="cancelMater"
      >
        <el-form
          ref="form"
          :model="form"
          :rules="rules"
          label-width="0px"
          class="dialog-table"
        >
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.resultOid" />
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td>
                <b>选择证照目录：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="billOid">
                  <el-select
                    v-model="form.billOid"
                    filterable
                    @change="billListSelect"
                  >
                    <el-option
                      v-for="dict in billList"
                      :key="dict.billOid"
                      :label="dict.directoryName"
                      :value="dict.billOid"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </td>
            </tr>
            <tr></tr>
            <tr>
              <td><b>证照目录编码：</b></td>

              <td>{{ form.directoryCode }}</td>

              <td>
                <b>
                  证照目录类型：
                </b>
              </td>
              <td>
                <b v-if="form.directoryType == '0'">证照</b>
                <b v-if="form.directoryType == '1'">批文</b>
              </td>
            </tr>
          </table>
        </el-form>
        <div slot="footer" class="zf-text-center">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancelMater">取 消</el-button>
        </div>
      </el-dialog>
      <el-dialog
        v-dialog-drag
        :visible.sync="item.show"
        v-for="item in dialogOptions"
        title="整合统一证照"
        :key="item.resultOid"
        :close-on-click-modal="false"
        width="1100px"
        append-to-body
      >
        <service-result
          :comboDireOid="item.comboDirectoryOid"
          :resultOid="item.resultOid"
          @dialog-close="closeDireResult"
        ></service-result>
      </el-dialog>

      <!--新增关联事项-->
      <el-dialog
        v-dialog-drag
        :visible.sync="chooseResultView"
        title="事项证照"
        :close-on-click-modal="false"
        width="1100px"
        scrollbar
        append-to-body
      >
        <choose-result
          :direOid="comboDirectoryOid"
          @sx-dialog-close="closeServiceResult"
        ></choose-result>
        <div slot="footer" class="zf-text-center">
          <el-button @click="closeServiceResult">关 闭</el-button>
        </div>
      </el-dialog>

      <!--引入文件的预览弹出层-->
      <el-dialog
        v-dialog-drag
        title="文件预览"
        :visible.sync="view.show"
        v-for="view in viewDialogOptions"
        :key="view.attaOid"
        :close-on-click-modal="false"
        @close="closeFileView"
        width="1100px"
        height="700px"
        scrollbar
        append-to-body
      >
        <service-result-file-view
          :attaOid="view.attaOid"
        ></service-result-file-view>
      </el-dialog>
    </el-tabs>
  </div>
</template>

<script>
import {
  handleInitResultMater,
  queryMaterBill,
  saveOrUpdateResult,
  getSxMaterialBillOne
} from "@/api/onething/sxpz/sxSerMaterBill";
import {
  queryDirectoryResultList,
  querySxServiceResultList,
  del,
  delSxResult,
  setRtStatus
} from "@/api/onething/sxpz/comboDirectoryResult";
import serviceResult from "@/views/onething/sxpz/comboDirectory/serviceResult";
import chooseResult from "@/views/onething/sxpz/comboDirectory/chooseResult";
import serviceResultFileView from "@/views/onething/sxpz/comboDirectory/serviceResultFileView";
export default {
  components: {
    serviceResult,
    chooseResult,
    serviceResultFileView
  },
  name: "ComboDirectoryResult",
  //定义获取父类传过来值的格式
  props: ["comboDireOid"],
  data() {
    return {
      // 遮罩层
      loading: true,
      total: 0,
      comboDirectoryOid: "",
      directoryResultList: [],
      comboSxResultList: [],
      chooseResultView: false,
      direResultShow: false,
      viewDialogOptions: [],
      openInitMater: false,
      billList: [],
      // 查询参数
      queryParams: {
        comboDirectoryOid: this.comboDireOid,
        pageNum: 1,
        pageSize: 10
      },
      dialogOptions: [],
      pubResultOid: "",
      resultOid: "",
      // 表单参数
      form: {},
      formBill: {},
      rules: {}
    };
  },
  created() {
    this.comboDirectoryOid = this.comboDireOid;
    //统一证照List
    this.getDirectoryResultlList();
    //被选择事项证照List
    this.getSxResultList();
  },
  methods: {
    // 表单重置
    reset() {
      this.resetForm("form");
    },
    getDirectoryResultlList() {
      this.loading = true;
      queryDirectoryResultList(this.comboDireOid).then(response => {
        this.directoryResultList = response.data;
        this.loading = false;
      });
    },
    getSxResultList() {
      this.loading = true;
      querySxServiceResultList(this.comboDireOid).then(response => {
        this.comboSxResultList = response.data;
        this.loading = false;
      });
    },
    cancelMater() {
      this.openInitMater = false;
      this.getSxResultList();
      this.title = "材料列表";
    },
    getBillList() {
      let _that = this;
      // 查询文书模板数据
      queryMaterBill().then(response => {
        _that.billList = response.data;
      });
    },
    //切换文书数据
    billListSelect(val) {
      let _that = this;
      getSxMaterialBillOne(val).then(response => {
        _that.formBill = response.data;
        _that.form.directoryType = _that.formBill.directoryType;
        _that.form.directoryCode = _that.formBill.directoryCode;
      });
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      this.reset();
      let oid = row.resultOid;
      let item = {
        show: true,
        comboDirectoryOid: this.comboDirectoryOid,
        resultOid: oid
      };
      this.dialogOptions.push(item);
    },
    //预览附件
    viewFile(row) {
      let item = {
        show: true,
        attaOid: row.resultSampleAddr
      };
      this.viewDialogOptions.push(item);
    },
    //关闭预览附件
    closeFileView() {
      this.viewDialogOptions.pop();
    },
    handleResultInit() {
      this.chooseResultView = true;
      this.comboDirectoryOid = this.comboDirectoryOid;
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const oid = row.id;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return del(oid);
        })
        .then(() => {
          this.msgSuccess("删除成功");
          //统一证照List
          this.getDirectoryResultlList();
        })
        .catch(function() {});
    },
    closeDireResult() {
      this.dialogOptions.pop();
      let item = {
        show: false
      };
      //统一证照List
      this.getDirectoryResultlList();
    },
    closeServiceResult() {
      this.chooseResultView = false;
      //事项证照List
      this.getSxResultList();
    },
    getStatus(val) {
      if (val.status == 0) {
        return "未确认";
      } else if (val.status == 1) {
        return "已确认";
      }
    },
    /** 新增和修改按钮操作 */
    handleInitMater(row) {
      let _that = this;
      _that.loading = true;
      if (row.resultOid) {
        handleInitResultMater(row.resultOid).then(response => {
          _that.openInitMater = true;
          _that.form = response.data;
        });
        _that.getBillList();
        _that.title = "证照目录关联";
      }
    },
    handleInitMaterSx(row) {
      let _that = this;
      _that.loading = true;
      if (row.sxResultOid) {
        handleInitResultMater(row.sxResultOid).then(response => {
          _that.openInitMater = true;
          _that.form = response.data;
        });
        _that.getBillList();
        _that.title = "证照目录关联";
      }
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdateResult(_that.form).then(response => {
            if (response.code == 200) {
              _that.msgSuccess("保存成功");
              _that.openInitMater = false;
              this.getSxResultList();
              _that.title = "材料列表";
            }
          });
        }
      });
    },
    //确认目录统一证照
    setResultStatus() {
      const comboDireOid = this.comboDireOid;
      this.$confirm("确认目录证照吗?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return setRtStatus(comboDireOid);
        })
        .then(() => {
          this.msgSuccess("已确认");
          //事项证照List
          this.getDirectoryResultlList();
        })
        .catch(function() {});
    },
    //删除事项证照
    handleSxDelete(row) {
      const oid = row.id;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return delSxResult(oid);
        })
        .then(() => {
          this.msgSuccess("删除成功");
          //事项证照List
          this.getSxResultList();
        })
        .catch(function() {});
    }
  }
};
</script>
