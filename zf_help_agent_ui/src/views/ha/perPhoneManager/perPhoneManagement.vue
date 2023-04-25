<!-- 请求条件 -->
<template>
  <div class="app-container" style="height:auto">
    <!--用户数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="80px"
    >
      <el-form-item label="查询时间">
        <el-date-picker
          v-model.trim="queryParams.beginTime"
          type="date"
          value-format="yyyy-MM-dd HH:mm:ss"
          :picker-options="pickerOptionsStart"
          placeholder="请选择开始日期"
        >
        </el-date-picker
        >-
        <el-date-picker
          v-model.trim="queryParams.endTime"
          type="date"
          value-format="yyyy-MM-dd HH:mm:ss"
          :picker-options="pickerOptionsEnd"
          placeholder="请选择结束日期"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="来电号码" prop="phone">
        <el-input
          v-model.trim="queryParams.phone"
          placeholder="请输入来电号码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="呼叫结果" prop="callResults">
        <el-col :span="24">
          <el-select
            v-model.trim="queryParams.callResults"
            placeholder="请选择呼叫结果"
            size="small">
            <el-option label="接通" value="接通"></el-option>
            <el-option label="未接" value="未接"></el-option>
          </el-select>
        </el-col>
      </el-form-item>
      <el-form-item label="工号" prop="workNumber">
        <el-input
          v-model.trim="queryParams.workNumber"
          placeholder="请输入"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索
        </el-button
        >
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          class="btn-reset"
        >重置
        </el-button>
      </el-form-item>
    </el-form>
    <!-- 工具条 -->
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">

        <el-upload
          class="upload-demo inline-block"
          action
          :multiple="false"
          :http-request="uploadFiles"
          :before-upload="beforeUpload"
          :on-error="uploadError"
          :file-list="fileList"
          :on-change="handleChange"
          :show-file-list="false"
          accept="accept"
        >
          <el-button size="mini" type="primary" icon="el-icon-upload">导入电话绩效</el-button>
        </el-upload>
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['im:location:save']"
        >新增
        </el-button>
      </el-col>

    </el-row>
    <!-- 表头 -->
    <el-table
      v-loading="loading"
      :data="locationList"
      stripe
      style="width: 100%" @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"/>
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="工号"
        align="center"
        prop="workNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="来电号码"
        align="center"
        prop="phone"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="呼叫时间"
        align="center"
        prop="callTime"
        :show-overflow-tooltip="true"
      />
      <el-table-column
      label="呼叫时长"
      align="center"
      prop="duration"
      :show-overflow-tooltip="true"
      />
      <el-table-column
        label="呼叫结果"
        align="center"
        prop="callResults"
        :show-overflow-tooltip="true"
      />

      <el-table-column
        label="操作"
        align="center"
        width="340"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['im:location:view']"
          >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['im:location:update']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="removeById(scope.row.id)"
            v-hasPermi="['im:location:delete']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNumber"
      :limit.sync="queryParams.pageSize"
      style="padding: 30px 0; text-align: center;"
      layout="total, sizes, prev, pager, next, jumper"
      @pagination="getList"
    />

    <!-- 信息详细 -->
    <!-- 添加或修改应用信息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      width="900px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <el-input v-show="false"/>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%"/>
            <col width="30%"/>
            <col width="20%"/>
            <col width="30%"/>
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>电话工号</b></td>
            <td>
              <el-form-item prop="workNumber">
                <el-input
                  v-model.trim="form.workNumber"
                  placeholder="请输入工号"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>来电号码：</b></td>
            <td >
              <el-form-item prop="phone">
                <el-input
                  v-model.trim="form.phone"
                  placeholder="请输入来电号码"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>来电用户：</b></td>
            <td >
              <el-form-item prop="name">
                <el-input
                  v-model.trim="form.name"
                  placeholder="请输入来电用户"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>呼叫时间：</b></td>
            <td >
              <el-form-item prop="callTime">
                <el-input
                  v-model.trim="form.callTime"
                  placeholder="请输入呼叫时间"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>呼叫时长：</b></td>
            <td >
              <el-form-item prop="duration">
                <el-input
                  v-model.trim="form.duration"
                  placeholder="呼叫时长"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require"></i><b>评价：</b></td>
            <td >
              <el-form-item prop="appraise">
                <el-input
                  v-model.trim="form.appraise"
                  placeholder="请输入评价"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require"></i><b>通话备注：</b></td>
            <td >
              <el-form-item prop="callNotes">
                <el-input
                  v-model.trim="form.callNotes"
                  placeholder="呼叫时长"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require"></i><b>意向结果：</b></td>
            <td >
              <el-form-item prop="intentResults">
                <el-input
                  v-model.trim="form.intentResults"
                  placeholder="请输入评价"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require"></i><b>通话结果：</b></td>
            <td >
              <el-form-item prop="callResults">
                <el-input
                  v-model.trim="form.callResults"
                  placeholder="呼叫时长"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>

        </table>
      </el-form>
      <div slot="footer" align="center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="900px"
      append-to-body
      scrollbar
    >
      <div class="zf-zc-table--title">地点信息</div>
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="zf-zc-table"
      >
        <colgroup>
          <col width="20%"/>
          <col width="30%"/>
          <col width="20%"/>
          <col width="30%"/>
        </colgroup>
        <tr>
          <td><b>电话工号：</b></td>
          <td>
            {{ locationInfo.workNumber }}
          </td>
          <td><b>来电号码：</b></td>
          <td>
            {{ locationInfo.phone }}
          </td>
        </tr>
        <tr>
          <td><b>来电用户：</b></td>
          <td>
            {{ locationInfo.name }}
          </td>
          <td><b>呼叫时间：</b></td>
          <td>
            {{ locationInfo.callTime }}
          </td>
        </tr>

        <tr>
          <td><b>呼叫时长：</b></td>
          <td>
            {{ locationInfo.duration }}
          </td>
          <td><b>评价：</b></td>
          <td>
            {{ locationInfo.appraise }}
          </td>
        </tr>

        <tr>
          <td><b>通话备注：</b></td>
          <td>
            {{ locationInfo.callNotes }}
          </td>
          <td><b>意向结果：</b></td>
          <td>
            {{ locationInfo.intentResults }}
          </td>
        </tr>

        <tr>
          <td><b>通话结果：</b></td>
          <td>
            {{ locationInfo.callResults }}
          </td>
        </tr>

      </table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  deleteById,
  init,
  page,
  saveOrUpdate,
  ImportPhoneExcel
} from "@/api/ha/perPhoneManagement/perPhoneManagement.js";

export default {
  name: "perPhoneManagement",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      locationList: [],
      locationInfo: {},
      locationIdOptions: [],
      multipleSelection: [],  //批量选择中选择的记录列表
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      // 查看显示弹出层
      openView: false,
      openInit: false,
      // 查看显示弹出层
      // openView: false,
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        phone: "",
        callResults: "",
        workNumber: "",
        beginTime: null,
        endTime: null,
      },
      fileList: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          {required: true, message: "必填项", trigger: "blur"}
        ],

      },
      pickerOptionsStart: {
        disabledDate: time => {
          const endTimeVal = new Date(this.queryParams.endTime).getTime();
          if (endTimeVal) {
            return time.getTime() > endTimeVal - 0;
          }
        }
      },
      pickerOptionsEnd: {
        disabledDate: time => {
          const beginTimeVal = new Date((new Date(this.queryParams.beginTime) - 24 * 60 * 60 * 1000)).getTime();
          if (beginTimeVal) {
            return time.getTime() < beginTimeVal - 0;
          }
        }
      },

    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdate(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.openInit = false;
              this.getList();
            }
          });
        } else {
          return false;
        }
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      //this.reset();
      this.locationInfo = row;
      this.openView = true;
      this.title = "查看应用信息";

      /*this.reset();
      const id = row.id;
      initGroup(id).then(response => {
        console.log(response.data)
        this.placeList = response.data;
        this.openView = true;
        this.title = "查看应用信息";
      });*/
    },
    //删除
    removeById(id) {
      this.$confirm('此操作将该用户数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {//确定执行then方法
        //调用接口方法
        deleteById(id)
          .then(response => {
            //提示信息
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            //刷新页面
            this.getList()
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    /** 初始化新增或修改 */
    handleInit(row) {
      const id = row.id;
      if (id === undefined) {
        this.form = {};
        this.openInit = true;
        this.title = "新增";
      } else {
        init(id).then(response => {
          this.form = response.data;
          this.openInit = true;
          this.title = "修改";
        });
      }
    },
    /** 查询列表 */
    getList() {

      this.loading = true;
      page(this.queryParams).then(response => {
        this.locationList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },

    /** 上传附件请求操作 */
    beforeUpload (file) {
      let _that = this;
      let isRightSize = file.size / 1024 / 1024 < 100;
      if (!isRightSize) {
        _that.$message.error("文件大小超过 100MB");
      }
      return isRightSize;
    },
    /** 失败后返回 */
    uploadError (resp) {
      let _that = this;
      _that.msgError("文件上传失败");
    },
    //文件改变时
    handleChange (file, fileList) {
      this.fileList = fileList.slice(-1);
    },
    /** 上传附件 */
    uploadFiles (file) {
      let _that = this;
      let formData = new FormData();
      formData.append("files", file.file);
      this.loading = true;
      ImportPhoneExcel(formData).then(respon => {
        if (respon.code === 200) {
          this.loading = false;
          var insertCount = respon.data.insertCount;
          var notDoCount = respon.data.notDoCount;
          var totalCount = respon.data.totalCount;

          _that.$message.info("导入excel文件成功，共计"+totalCount+"条电话记录，新增"+insertCount+"条电话记录，已存在"+notDoCount+"条电话记录")
          _that.handleQuery();
        } else {
          this.loading = false;
          _that.$message.error("上传文件失败！");
        }
      });
    },
    // 取消按钮
    cancel() {
      this.openInit = false;
      // this.reset();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form);
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

    //获取选择复选框的id值
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    }
  },
};
</script>
<style lang="scss" scoped>
//头像样式start
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

// end
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

.inline-block {
  display: inline-block;
}
</style>
