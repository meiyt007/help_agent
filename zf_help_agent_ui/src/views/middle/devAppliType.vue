<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
      @submit.native.prevent
    >
      <el-form-item label="类别名称" prop="typeName">
        <el-input
          v-model.trim="queryParams.typeName"
          placeholder="请输入类别名称"
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
          >搜索</el-button
        >
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          class="ml5"
        >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['im:devAppliType:update']"
        >编辑</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-delete"
          size="mini"
          @click="submitBatchForm"
        >删除</el-button
        >
      </el-col>
    </el-row>
    <el-table
      ref="multipleTable"
      :data="fieldTypeList"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="60" align="center"> </el-table-column>

      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="应用分类名称"
        align="center"
        prop="typeName"
        show-overflow-tooltip
      />
      <el-table-column
        label="排序号"
        align="center"
        prop="sort"
        show-overflow-tooltip
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createDate"
        show-overflow-tooltip
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
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sx:fieldType:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['sx:fieldType:update']"
            >编辑下级</el-button
          >
        </template>
      </el-table-column>
      <el-table-column type="expand">
        <template slot-scope="props">
          <template v-for="(item, index) in props.row.devAppliTypeDtoList">
            <el-form
              label-position="left"
              inline
              class="demo-table-expand"
            >
              <el-form-item style="width: 3%" align="center">
                <span>{{ index + 1 }}</span>
              </el-form-item>
              <el-form-item style="width: 15%" align="center">
                <span>{{ item.typeName }}</span>
              </el-form-item>
              <el-form-item style="width: 14%" align="center">
                <span>{{ item.createDate }}</span>
              </el-form-item>
              <el-form-item style="width: 5%" align="center">
                <span>
                  <el-button
                    icon="el-icon-sort-up"
                    class="handle-btn"
                    style="border: none"
                    @click="devAppliTypeMoveUp(item.oid)"
                    v-hasPermi="['im:devAppliType:up']"
                  >上移</el-button>
                </span>
              </el-form-item>
              <el-form-item style="width: 5%" align="center">
                 <span>
                  <el-button
                    icon="el-icon-sort-down"
                    class="handle-btn"
                    style="border: none"
                    @click="devAppliTypeMoveDown(item.oid)"
                    v-hasPermi="['im:devAppliType:down']"
                  >下移</el-button>
                </span>
              </el-form-item>
              <el-form-item style="width: 5%" align="center">
                 <span>
                  <el-button
                    icon="el-icon-delete"
                    class="handle-btn"
                    style="border: none"
                    @click="submitDelForm(item.oid)"
                    v-hasPermi="['im:devAppliType:delete']"
                  >删除</el-button>
                </span>
              </el-form-item>
            </el-form>
          </template>
        </template>
      </el-table-column>

    </el-table>

    <!-- 添加或修改 -->
    <el-dialog
      v-dialog-drag
      height="600px"
      scrollbar
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      v-if="openInit"
      width="1100px"
      append-to-body
    >
      <div id="printTest">
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <el-button type="primary" @click="addHtml" class="add-btn"             v-hasPermi="['im:devAppliType:save']"
          >增加</el-button
          >
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table--common zf-zc-table--td-center">
            <colgroup>
              <col width="50" />
              <col width="220" />
              <col width="160" />
              <col width="70" />
            </colgroup>
            <tr>
              <th>序号</th>
              <th>类别名称</th>
              <th>排序</th>
              <th>操作</th>
            </tr>
            <template v-for="(ruleForm, index) in form.devAppliTypeDtoList">
              <template>
                <tr>
                  <td>
                    <el-form-item prop="xuhao">
                      {{ index + 1 }}
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <el-input
                        maxlength="32"
                        v-model="ruleForm.typeName"></el-input>
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <el-input
                        maxlength="3"
                        v-model.trim="ruleForm.sort"
                      ></el-input>
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <el-button
                        style="border: 0px"
                        icon="el-icon-delete"
                        @click="delHtml(index)"
                        v-if="isShowdel(ruleForm)"
                        class="handle-btn"
                      ></el-button>
                    </el-form-item>
                  </td>
                </tr>
              </template>
            </template>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看应用信息详细 -->
    <el-dialog
      title="应用分类详细信息"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="70%"
      append-to-body
    >
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>事项标题</h3>-->
        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          class="zf-zc-table"
        >
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>分类名称：</b></td>
            <td>
              {{ devAppliType.typeName }}
            </td>
            <td><b>排序：</b></td>
            <td>
              {{ devAppliType.sort }}
            </td>
          </tr>
        </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { page, init, getOne, save, up, down, batchDelete, del } from "@/api/middle/devAppliType";
export default {
  components: {},
  name: "SxFieldType",
  data() {
    return {
      // 遮罩层
      loading: true,
      isAbleMap: { 0: "禁用", 1: "启用" },
      // 总条数
      title: "",
      // 数据表格
      fieldTypeList: [],
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 启用状态
      ableMap: { 1: "启用", 0: "禁用" },
      // 查询参数
      queryParams: {
        typeName: "",
      },
      devAppliType: {},
      form: {
        devAppliTypeDtoList: [],
        parentTypeOid: ""
      },
      rules: {
        typeName: [
          { required: true, message: "类别名称不能为空", trigger: "blur" },
        ],
        sort: [{ required: true, message: "排序号不能为空", trigger: "blur" }],
      },
    };
  },
  watch: {},
  created() {
    this.getList();
  },
  methods: {
    /** 查询业务层级列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then((response) => {
        this.fieldTypeList = response.data;
        this.loading = false;
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.reset();
      const oid = row.oid;
      getOne(oid).then((response) => {
        this.devAppliType = response.data;
        this.openView = true;
        this.title = "应用分类详细信息";
      });
    },
    /** 上移按钮操作 */
    devAppliTypeMoveUp(oid) {
      this.reset();
      up(oid).then((response) => {
        if(response.code == 200){
          this.msgSuccess(response.data);
          setTimeout(() => {
            this.getList();
          }, 10);
          this.reset();
        }else {
          alert(response.message)
        }
      });
    },
    /** 下移按钮操作 */
    devAppliTypeMoveDown(oid) {
      this.reset();
      down(oid).then((response) => {
        if(response.code  == 200){
          this.msgSuccess(response.data);
          setTimeout(() => {
            this.getList();
          }, 10);
          this.reset();
        }else {
          this.msgError(response.data)
        }
      });
    },
    handleSelectionChange(val){
      this.multipleSelection = val;
    },
    /** 批量删除操作 */
    submitBatchForm: function() {
      let _that = this;
      var oid = [];
      if (_that.multipleSelection.length <= 0) {
        _that.$message.error("请选择删除!");
        return false;
      }
      for (let ss = 0; ss < _that.multipleSelection.length; ss++) {
        oid.push(_that.multipleSelection[ss].oid);
      }
      var oids = oid.join(",");
      batchDelete(oids).then(response => {
        if (response.code === 200) {
          _that.msgSuccess("删除成功");
          this.getList();
        }
      });
    },

    /** 单个删除操作 */
    submitDelForm: function(oid) {
      this.$confirm(
        "删除服务商可能会导致对应标签数据出错，是否确认删除?",
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function () {
          return del(oid);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () { });
    },

    isShowdel (row) {
      return !row.oid && row.oid== undefined;
    },
    // 初始化编辑下级页面
    handleInit (row) {
      const oid = row.oid;
      if (oid === undefined) {
        init(oid).then((response) => {
          this.form.devAppliTypeDtoList = response.data;
          this.openInit = true;
          this.title = "修改";
        });
      } else {
        init(oid).then((response) => {
          this.form.devAppliTypeDtoList = response.data;
          this.form.parentTypeOid = oid;
          this.openInit = true;
          this.title = "修改";
        });
      }
    },

    //添加子项模块
    addHtml: function () {
      this.form.devAppliTypeDtoList.push({

      })
    },
    //删除子项模块
    delHtml: function (index) {
      this.devAppliType = {};
      this.devAppliType = this.form.devAppliTypeDtoList[index];
      this.form.devAppliTypeDtoList.splice(index, 1);
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.devAppliTypeDtoList == null && this.form.devAppliTypeDtoList.length == 0) {
            this.msgWarning("请至少录入一个类别！");
            return false;
          }else {
            let chlidName = [];
            let chlidSort = [];
            for (let devAppli of this.form.devAppliTypeDtoList) {
              if(devAppli.typeName==null||devAppli.typeName==''){
                this.msgWarning("类别名称不能为空！");
                return false;
              }
              if(devAppli.sort==null||devAppli.sort==''){
                this.msgWarning("排序号不能为空！");
                return false;
              }
              const re = /^[1-9]{1,}[\d]*$/;
              if(!re.test(devAppli.sort)||parseInt(devAppli.sort) <= 0){
                this.msgWarning("排序号必须为正整数！");
                return false;
              }
              chlidName.push(devAppli.typeName);
              chlidSort.push(devAppli.sort);
            }
            let repeat = chlidName.join(",") + ",";
            let repeatSort = chlidSort.join(",") + ",";

            for (let i = 0; i < chlidName.length; i++) {

              for(let j =i+1;j<chlidName.length;j++){
                 if(chlidName[i] == chlidName[j]){
                   this.msgWarning("子级名称:" + chlidName[i] + " 已存在，不能重复添加！");
                   return false;
                 }
                if(chlidSort[i] == chlidSort[j]){
                  this.msgWarning("排序号:" + chlidSort[i] + " 已存在，不能重复添加！");
                  return false;
                }
              }

            /*  if (repeat.replace(chlidName[i] + ",", "").indexOf(chlidName[i] + ",") > -1) {
                this.msgWarning("子级名称:" + repeat[i] + " 已存在，不能重复添加！");
                return false;
              }
             debugger;
              if (repeatSort.replace(chlidSort[i] + ",", "").indexOf(chlidSort[i] + ",") > -1) {
                this.msgWarning("不允许有重复排序号！");
                return false;
              }*/

            }
          }
          console.log(this.form);
          save(this.form).then((response) => {
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
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams = {};
      this.handleQuery();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form)
      this.resetForm("form");
    },
    // 取消按钮
    cancel() {
      this.openInit = false;
      this.openView = false;
      this.reset();
    },
  },
};
</script>
<style></style>
