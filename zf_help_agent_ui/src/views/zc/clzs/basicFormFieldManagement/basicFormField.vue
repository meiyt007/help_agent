/**
* @Description: 基础表单字段管理
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--咨询数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="70px">
          <el-form-item label="字段名称" prop="fieldName">
            <el-input v-model.trim="queryParams.fieldName" placeholder="请输入字段名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="字段编码" prop="fieldKey">
            <el-input v-model.trim="queryParams.fieldKey" placeholder="请输入字段编码" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>

          <el-form-item label="字段分类" prop="fieldType">
            <el-select
              v-model="queryParams.fieldType"
              placeholder="请选择字段分类"
            >
              <el-option
                v-for="dict in fieldTypeList"
                :key="dict.id"
                :label="dict.name"
                :value="dict.id"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>


       <el-row :gutter="10" class="mb8">

         <el-col :span="1.5">
           <el-button
             type="default"
             icon="el-icon-plus"
             size="mini"
             @click="handleInit"
           >新增</el-button>
         </el-col>
        </el-row>

        <el-table v-loading="loading" :data="basicFormFieldList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="字段名称"  align="center" prop="fieldName" :show-overflow-tooltip="true"/>
          <el-table-column label="字段编码"  align="center" prop="fieldKey" :show-overflow-tooltip="true"/>
          <el-table-column label="所属分类"  align="center" prop="fieldType" :show-overflow-tooltip="true"/>
          <el-table-column label="创建时间" align="center" prop="createDate" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button  size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"  >查看</el-button>
              <el-button  size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInit(scope.row)" >修改</el-button>
              <el-button  size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)" >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>
    <!-- 添加或修改基础表单管理 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openInit" v-if="openInit" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px" >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>

          <tr>
            <td>
              <b><i class="require">*</i>字段名称：</b>
            </td>
            <td >
              <el-form-item prop="fieldName">
                <el-col :span="24">
                  <el-input v-model.trim="form.fieldName" placeholder="请输入字段名称" maxlength="100" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b><i class="require">*</i>字段编码：</b>
            </td>
            <td >
              <el-form-item prop="fieldKey">
                <el-col :span="24">
                  <el-input v-model.trim="form.fieldKey" placeholder="请输入字段编码" maxlength="100" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td>
              <b><i class="require">*</i>所属分类：</b>
            </td>
            <td>
              <el-form-item prop="fieldType">
                <el-select
                  placeholder="请选择所属分类"
                  v-model="form.fieldType"
                  :clearable="true"
                >
                  <el-option
                    v-for="fieldType in fieldTypeList"
                    :key="fieldType.id"
                    :label="fieldType.name"
                    :value="fieldType.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 基础表单详细 -->
    <el-dialog v-dialog-drag :title="title" :visible.sync="openView" width="800px" v-if="openView" append-to-body>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>字段名称：</b>
            </td>
            <td >
              {{form.fieldName}}
            </td>
            <td>
              <b>字段编码：</b>
            </td>
            <td >
               {{form.fieldKey}}
            </td>
          </tr>


          <tr>
            <td>
              <b>所属分类：</b>
            </td>
            <td v-if="this.viewType === '1'">
              单办
            </td>
            <td v-if="this.viewType === '2'">
              一件事
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
import {page,getOne,saveOrUpdate,del,getFaModelRuleValidationList,getComboFaModelRuleValidationListByYZ, checkHasRepeat, checkIsHasRel} from "@/api/zc/clzs/basicFormFieldManagement/basicFormField";
import Treeselect from '@riophae/vue-treeselect';
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "BasicFormField",
  components: {Treeselect},
  data() {
    return {
      // 遮罩层
      viewType: '',
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      basicFormFieldList: [],
      oid:"",
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fieldKey: null,
        fieldName:null
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {
        id: '', //逻辑主键
        oid:'',
        fieldKey: '', // 类别名称
        fieldName: '', // 类别编码
        fieldType: '', // 备注
        sort: 0,//排序
      },
      //人工审核
      fieldTypeList: [
        {
          id: '1',
          name: '单办',
        },
        {
          id: '2',
          name: '一件事',
        },
      ],
      // 表单校验
      rules: {
        fieldKey: [
          { required: true, message: "类别编码不能为空", trigger: "blur" }
        ],
        fieldName: [
          { required: true, message: "类别字段不能为空", trigger: "blur" }
        ],
        fieldType: [
          { required: true, message: "所属类型不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询材料类别列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.basicFormFieldList = response.data.data;
        if (this.basicFormFieldList) {
          for (var i=0; i<this.basicFormFieldList.length; i++) {
            if (this.basicFormFieldList[i].fieldType === '1') {
              this.basicFormFieldList[i].fieldType = "单办";
            } else if(this.basicFormFieldList[i].fieldType === '2') {
              this.basicFormFieldList[i].fieldType = "一件事";
            }
          }
        }
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
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView(row) {
      let _that = this;
      _that.reset();
      const oid = row.id;
      getOne(oid).then(response => {
        _that.form = response.data;
        _that.viewType = response.data.fieldType;
        _that.openView = true;
        _that.title = "查看基础表单字段信息";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      _that.reset();
      if(row.id) {
        getOne(row.id).then(response => {
          _that.openInit = true;
          _that.form = response.data;
        });
        this.oid=row.oid;
      } else {
        this.oid="";
        _that.openInit = true;
      }
      _that.title = row.id ? "修改基础表单字段信息" : "新增基础表单字段信息";
    },

    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          checkHasRepeat(this.form.fieldType, this.form.fieldKey, this.form.oid).then(response => {
            if (response.data === 'false') {
              saveOrUpdate(_that.form).then(response => {
                if (response.code === 200) {
                  _that.msgSuccess("保存成功");
                  _that.openInit = false;
                  _that.oid="";
                  setTimeout(() => {
                    _that.getList();
                  }, 10);
                }
              });
            } else {
              this.msgWarning("基础表单字段该分类已经被填充，请勿重复填充");
            }
          });
        }
      });
    },
    /** 删除按钮操作 */
    /** 删除按钮操作 */
    handleDelete(row) {
      let _that = this;
      const oid = row.id;
      //查询基础字段关联ocr调用规则参数
      let query={};
      query.ruleType='JCBD';
      query.thanTemplateMetadataOid=row.oid;
      query.thanTemplateMetadataCode=row.fieldKey;
      query.fieldType = row.fieldType;  //1 单办  2 一件事
      //验证是否被关联
      checkIsHasRel(query).then(response => {
        let successflag=response.data.success;
        if (successflag ) {
          _that.msgSuccess("该字段已存在填充关联关系，不可删除");
        } else {
          //验证事项ocr规则配置
          getFaModelRuleValidationList(query).then(response => {
            let successflag=response.data.success;
            if (!successflag ) {
              _that.msgSuccess("该字段存在已配置的事项验证规则该字段不可删除");
            }else {
              //验证一件事ocr规则配置
              getComboFaModelRuleValidationListByYZ(query).then(response => {
                let yjssuccessflag=response.data.success;
                if (!yjssuccessflag ) {
                  _that.msgSuccess("该字段存在已配置的一件事验证规则该字段不可删除");
                }else {
                  //删除
                  _that.$confirm("是否确认删除?", "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                  })
                    .then(function() {
                      return del(oid);
                    })
                    .then(() => {
                      _that.getList();
                      _that.msgSuccess("删除成功");
                    })
                    .catch(function() {});

                }
              })
            }
          });
        }
      });
    }
   /* handleDelete(row) {
      let _that = this;
      const oid = row.id;
      _that.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return del(oid);
        })
        .then(() => {
          _that.getList();
          _that.msgSuccess("删除成功");
        })
        .catch(function() {});
    }*/
  },
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
