<template>
  <div>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button type="default" icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['qdgl:condition:addUpdate']">新增
        </el-button>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="placeList" border>
      <el-table-column label="序号" align="center" type="index" width="55" />
      <el-table-column label="受理条件内容" align="center" show-overflow-tooltip prop="conditionText" />
      <el-table-column label="操作" align="center" width="260" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"
            v-hasPermi="['qdgl:condition:view']">查看</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleUpdate(scope.row)"
            v-hasPermi="['qdgl:condition:addUpdate']">修改</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)"
            v-hasPermi="['qdgl:condition:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 新增修改 -->
    <el-dialog v-dialog-drag :visible.sync="addUpdateFlag" v-if="addUpdateFlag" :title="addUpdateTitle" width="900px"
      append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <tr>
            <td width="15%"><i class="require">*</i><b>受理条件内容：</b></td>
            <td width="85%">
              <el-form-item prop="conditionText">
                <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 20 }" v-model.trim="form.conditionText"
                  placeholder="请输入受理条件内容" />
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" style="text-align: center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 查看用户配置对话框 -->
    <el-dialog v-dialog-drag title="查看受理条件信息" :visible.sync="conditionView" v-if="conditionView"
      :close-on-click-modal="false" width="900px" height="700px" scrollbar  append-to-body>

      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td width="15%"><b>受理条件内容：</b></td>
          <td width="85%">
            {{ form.conditionText }}
          </td>
        </tr>
      </table>

      <div slot="footer" style="text-align: center">
        <el-button @click="viewCancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {
    page,
    save,
    detail,
    del
  } from "@/api/zc/qdgl/acceptanceCondition";

  export default {
    name: "acceptanceCondition",
    components: {},
    props: ["organOid", "createUser", "serviceOid"],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 应用表格数据
        placeList: [],
        addUpdateTitle: "",
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          serviceOid: this.serviceOid
        },
        addUpdateFlag: false,
        conditionView: false,
        form: {
          id: "",
          conditionOid: "",
          conditionText: "",
          organOid: this.organOid,
          createUser: this.createUser,
          serviceOid: this.serviceOid
        },
        rules: {
          conditionText: [{
            required: true,
            message: "受理条件内容不能为空",
            trigger: "blur"
          }]
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询办理环节列表 */
      getList() {
        this.loading = true;
        let that = this;
        page(this.queryParams)
          .then(response => {
            this.placeList = response.data.data;
            this.total = response.data.total;
            this.loading = false;
          })
          .catch(function () {
            that.loading = false;
          });
      },
      handleAdd() {
        this.addUpdateTitle = "新增受理条件";
        this.addUpdateFlag = true;
        this.form.id = "";
        this.form.conditionOid = "";
        this.form.conditionText = "";
      },
      handleUpdate(row) {
        this.addUpdateTitle = "修改受理条件";
        this.addUpdateFlag = true;
        detail(row.id).then(res => {
          if (res.code == 200) {
            this.form.id = res.data.id;
            this.form.questionOid = res.data.questionOid;
            this.form.conditionText = res.data.conditionText;
          }
        });
      },
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            save(this.form)
              .then(response => {
                if (response.code == 200) {
                  this.$message.success("保存成功");
                  this.addUpdateFlag = false;
                  this.getList();
                } else {
                  this.$message.error("保存失败！");
                }
              })
              .catch(function () {
                this.addUpdateFlag = false;
              });
          }
        });
      },
      cancel() {
        this.addUpdateFlag = false;
        this.form.id = "";
        this.form.conditionOid = "";
        this.form.conditionText = "";
      },
      handleView(row) {
        this.conditionView = true;
        detail(row.id).then(res => {
          this.form.conditionText = res.data.conditionText;
        });
      },
      viewCancel() {
        this.conditionView = false;
        this.form.conditionText = "";
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        this.$confirm("是否确认删除此受理条件的内容?", "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
          .then(function () {
            return del(row.id);
          })
          .then(() => {
            this.getList();
            this.msgSuccess("删除成功");
          })
          .catch(function () {});
      }
    }
  };

</script>
