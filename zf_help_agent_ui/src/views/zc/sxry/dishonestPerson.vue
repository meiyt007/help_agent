<!--author:liyanqing-->
<template>
  <div class="app-container">
    <!--提交表单-->
    <el-form
      ref="queryForm"
      :model="queryParams"
      :inline="true"
      label-width="108px"
      @submit.native.prevent
    >
      <el-form-item label="失信人员" prop="name">
        <el-input
          v-model.trim="queryParams.name"
          placeholder="请输入失信人员名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="失信状态">
        <el-radio-group v-model="queryParams.status">
          <el-radio
            v-for="(status, key) in statusOptions"
            :key="key"
            :label="key"
            >{{ status }}</el-radio
          >
        </el-radio-group>
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

    <!--   新增    -->
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['sys:dishonest:init']"
          >新增</el-button
        >
      </el-col>
    </el-row>

    <!--列表-->
    <el-table
      v-loading="loading"
      :data="dishonestPersonList"
      border
      :fit="true"
      height="calc(100% - 160px)"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="失信人员名称"
        :show-overflow-tooltip="true"
        align="center"
        prop="name"
      />
      <el-table-column
        label="证件号码"
        :show-overflow-tooltip="true"
        align="center"
        prop="cardNumber"
      />
      <el-table-column
        label="联系方式"
        :show-overflow-tooltip="true"
        align="center"
        prop="phone"
      />
      <el-table-column
        label="创建时间"
        :show-overflow-tooltip="true"
        align="center"
        prop="createDate"
      />

      <el-table-column
        label="操作"
        align="center"
        width="380"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-biangengjilu"
            @click="queryRecordList(scope.row)"
            v-if="scope.row.status == 0"
            v-hasPermi="['sys:dishonest:record']"
            >失信记录</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:dishonest:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-if="scope.row.status == 0"
            v-hasPermi="['sys:dishonest:init']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-if="scope.row.status == 0"
            v-hasPermi="['sys:dishonest:delete']"
            >删除</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-quxiaozanting"
            @click="handleCancel(scope.row)"
            v-if="scope.row.status == 0"
            v-hasPermi="['sys:dishonest:revoke']"
            >失信撤销</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      v-if="addDialogShow"
      :visible.sync="addDialogShow"
      width="900px"
      append-to-body
      v-dialog-drag
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="80%" />
          </colgroup>
          <tr>
            <td>
              <i class="require">*</i>
              <b>姓名：</b>
            </td>
            <td>
              <el-form-item prop="name">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.name"
                    placeholder="请输入失信人名称"
                    maxlength="20"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <i class="require">*</i>
              <b>手机号码：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="phone">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.phone"
                    placeholder="请输入手机号码"
                    maxlength="11"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <i class="require">*</i>
              <b>证件类型：</b>
            </td>
            <td>
              <el-form-item prop="cardType">
                <el-select
                  v-model="form.cardType"
                  placeholder="请选择"
                  @change="changeType"
                >
                  <el-option
                    v-for="item in typeList"
                    :key="item.dictOid"
                    :label="item.name"
                    :value="item.dictOid"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <i class="require">*</i>
              <b>证件号码：</b>
            </td>
            <td>
              <el-form-item prop="cardNumber">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.cardNumber"
                    placeholder="请输入证件号码"
                    maxlength="18"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" style="text-align:center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button
          @click="
            () => {
              addDialogShow = false;
              reset();
            }
          "
          >取 消</el-button
        >
      </div>
    </el-dialog>

    <!-- 详细 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="detailDialogShow"
      v-if="detailDialogShow"
      width="900px"
      append-to-body
      v-dialog-drag
    >
      <div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>姓名：</b>
            </td>
            <td colspan="3">
              {{ form.name }}
            </td>
          </tr>
          <tr>
            <td>
              <b>证件类型：</b>
            </td>
            <td colspan="3">
              {{ form.cardType }}
            </td>
          </tr>
          <tr>
            <td>
              <b>证件号码：</b>
            </td>
            <td colspan="3">
              {{ form.cardNumber }}
            </td>
          </tr>
          <tr>
            <td>
              <b>联系方式：</b>
            </td>
            <td colspan="3">
              {{ form.phone }}
            </td>
          </tr>
          <!--          <tr>
              <td>
                <b>失信原由：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="dishonestReason">
                  <el-col :span="24">
                    {{form.dishonestReason}}
                  </el-col>
                </el-form-item>
              </td>
            </tr>-->
          <tr v-if="form.status == 1">
            <td>
              <b>撤销批准人：</b>
            </td>
            <td colspan="3">
              {{ form.revokeUserName }}
            </td>
          </tr>
          <tr v-if="form.status == 1">
            <td>
              <b>撤销原因：</b>
            </td>
            <td colspan="3">
              {{ form.revokeReason }}
            </td>
          </tr>
        </table>

        <div slot="footer" style="text-align:center">
          <el-button @click="detailDialogShow = false">关 闭</el-button>
        </div>
      </div>
    </el-dialog>

    <!-- 撤销操作 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      v-if="cancelDialogShow"
      :visible.sync="cancelDialogShow"
      width="900px"
      append-to-body
      v-dialog-drag
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="13%" />
            <col width="37%" />
            <col width="13%" />
            <col width="37%" />
          </colgroup>
          <tr>
            <td>
              <i class="require">*</i>
              <b>撤销批准人：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="revokeUserName">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.revokeUserName"
                    placeholder="请输入撤销批准人"
                    maxlength="20"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <i class="require">*</i>
              <b>撤销原因：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="revokeReason">
                <el-col :span="24">
                  <el-input
                    type="textarea"
                    v-model.trim="form.revokeReason"
                    placeholder="请输入撤销原因"
                    maxlength="500"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" style="text-align:center">
        <el-button type="primary" @click="dishonestRevoke">确 定</el-button>
        <el-button
          @click="
            () => {
              cancelDialogShow = false;
              reset();
            }
          "
          >取 消</el-button
        >
      </div>
    </el-dialog>

    <!--失信记录-->
    <el-dialog
      v-dialog-drag
      :visible.sync="result.show"
      title="失信记录"
      v-for="(result, index) in recordeOptions"
      :key="index"
      @close="closeResultView"
      :close-on-click-modal="false"
      width="900px"
      append-to-body
      scrollbar
      height="600px"
    >
      <dishonest-record
        :dishonestOid="result.dishonestOid"
        :status="result.status"
      />
      <div slot="footer" style="text-align:center">
        <el-button @click="result.show = false">
          关闭
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  get,
  saveOrUpdate,
  deletes,
  dishonestCancel,
  getCertificateType,
  getOneDict
} from "@/api/zc/sxry/dishonestPerson";
import { deepClone } from "@/utils";
import dishonestRecord from "@/views/zc/sxry/dishonestRecord";
import {
  validatePhone,
  validIDCard,
  validUnifiedCredit
} from "@/utils/validate";
import { changeCredentialType } from "@/api/zc/businessManagement/windowAcceptance";
export default {
  name: "DishonestPerson",
  components: { dishonestRecord },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      dishonestPersonList: [],
      //查看信息弹框标识
      detailDialogShow: false,
      addDialogShow: false,
      cancelDialogShow: false,
      // 弹出层标题
      title: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: "0"
      },
      // 失信状态
      statusOptions: { "0": "执行", "1": "撤销" },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "请输入失信人名称", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "请输入手机号码", trigger: "blur" },
          {
            validator: validatePhone,
            message: "请输入正确的失信人手机号",
            trigger: "blur"
          }
        ],
        cardType: [
          { required: true, message: "请选择证件类型", trigger: "blur" }
        ],
        cardNumber: [
          { required: true, message: "请输入证件号码", trigger: "blur" }
        ],
        revokeUserName: [
          { required: true, message: "请输入撤销批准人", trigger: "blur" }
        ],
        revokeReason: [
          { required: true, message: "请输入撤销原由", trigger: "blur" }
        ]
      },
      //失信记录页面
      recordeOptions: [],
      //证件类型
      typeList: []
    };
  },
  created() {
    this.getList();
    //获取证件类型
    this.getTypeList();
  },
  methods: {
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.dishonestPersonList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.status = "0"; //重置radio无效
      this.handleQuery();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form);
      this.resetForm("form");
      this.rules.cardNumber = [
        { required: true, message: "请输入证件号码", trigger: "blur" }
      ];
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      _that.reset();
      if (row.id) {
        get(row.id).then(response => {
          _that.addDialogShow = true;
          _that.form = deepClone(response.data);
        });
      } else {
        _that.form = {};
        _that.addDialogShow = true;
      }
      _that.dialogTitle = row.id ? "修改" : "新增";
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let id = row.id;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return deletes(id);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    },
    /** 撤销按钮操作 */
    handleCancel(row) {
      let id = row.id;
      let _that = this;
      _that.reset();
      if (row.id) {
        get(id).then(response => {
          _that.cancelDialogShow = true;
          _that.form = deepClone(response.data);
        });
      }
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdate(_that.form).then(response => {
            _that.msgSuccess("保存成功");
            _that.addDialogShow = false;
            setTimeout(() => {
              _that.getList();
            }, 10);
            _that.reset();
          });
        }
      });
    },
    /** 撤销信息保存 */
    dishonestRevoke: function() {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          dishonestCancel(_that.form).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("已撤销");
              _that.cancelDialogShow = false;
              setTimeout(() => {
                _that.getList();
              }, 10);
              _that.reset();
            }
          });
        }
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      let _that = this;
      _that.reset();
      const id = row.id;
      get(id).then(response => {
        _that.form = deepClone(response.data);
        _that.detailDialogShow = true;
        _that.dialogTitle = "查看详情";
        this.getType(response.data.cardType);
      });
    },
    //失信记录
    queryRecordList(row) {
      let item = {
        show: true,
        dishonestOid: row.dishonestOid,
        status: row.status
      };
      this.recordeOptions.push(item);
    },
    // 关闭按钮
    closeResultView() {
      this.recordeOptions.pop();
    },
    getTypeList() {
      let _that = this;
      getCertificateType(0).then(response => {
        _that.typeList = _that.typeList.concat(response.data);
      });
      getCertificateType(1).then(response => {
        _that.typeList = _that.typeList.concat(response.data);
      });
    },

    /** 改变证件类型 */
    changeType(item) {
      let _that = this;
      getOneDict(item).then(response => {
        let type = {};
        _that.rules.cardNumber.forEach((item, index) => {
          if (response.data.code == "SFZ") {
            type.validator = validIDCard;
            type.trigger = "blur";
            _that.rules.cardNumber.push(type);
          } else if (response.data.code == "XYDMZ") {
            type.validator = validUnifiedCredit;
            type.trigger = "blur";
            _that.rules.cardNumber.push(type);
          } else {
            if (index == 1) {
              _that.rules.cardNumber.splice(
                _that.rules.cardNumber.indexOf(item),
                1
              );
            }
          }
        });
      });
    },
    //获取证件类型名称
    getType(oid) {
      getOneDict(oid).then(response => {
        this.form.cardType = response.data.name;
      });
    }
  }
};
</script>
