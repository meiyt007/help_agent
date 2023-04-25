/**
* @Author: liangss
* @Date: 2020-11-03 14:04:48
* @Last Modified by: liangss
* @Description: 材料类别管理
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--咨询数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="70px">
          <el-form-item label="类别名称" prop="categoryName">
            <el-input v-model.trim="queryParams.categoryName" placeholder="请输入类别名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="类别编码" prop="categoryCode">
            <el-input v-model.trim="queryParams.categoryCode" placeholder="请输入类别编码" clearable size="small" @keyup.enter.native="handleQuery" />
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

        <el-table v-loading="loading" :data="materialCategoryList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="类别名称"  align="center" prop="categoryName" :show-overflow-tooltip="true"/>
          <el-table-column label="类别编码"  align="center" prop="categoryCode" :show-overflow-tooltip="true"/>
          <el-table-column label="创建时间"align="center" prop="createDate" :show-overflow-tooltip="true"/>
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
    <!-- 添加或修改材料类别 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openInit" v-if="openInit" width="900px" append-to-body>
    <div class="dia-content" style="width:100%; min-height:300px; max-height:700px; overflow-y:scroll; ">
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
              <b><i class="require">*</i>类别名称：</b>
            </td>
            <td >
              <el-form-item prop="categoryName">
                <el-col :span="24">
                  <el-input v-model.trim="form.categoryName" placeholder="请输入类别名称" maxlength="20" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b><i class="require">*</i>类别编码：</b>
            </td>
            <td >
              <el-form-item prop="categoryCode">
                <el-col :span="24">
                  <el-input v-model.trim="form.categoryCode" placeholder="请输入类别编码" maxlength="50" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>


          <tr>
            <td>
              <b>备注：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="note">
                <el-col :span="24">
                  <el-input type="textarea" placeholder="请输入备注" v-model.trim="form.note" maxlength="200" show-word-limit></el-input>
                </el-col>
              </el-form-item>
            </td>
          </tr>

<!--          <tr>
            <td><i class="require">*</i><b>排序号：</b></td>
            <td colspan="3">
              <el-form-item prop="sort">
                <el-input-number placeholder="请输入排序号" v-model="form.sort" :min="1" :max="9999"/>
              </el-form-item>
            </td>
          </tr>-->
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
      </div>
    </el-dialog>

    <!-- 咨询登记信息详细 -->
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
              <b>类别名称：</b>
            </td>
            <td >
              {{form.categoryName}}
            </td>
            <td>
              <b>类别编码：</b>
            </td>
            <td >
               {{form.categoryCode}}
            </td>
          </tr>


          <tr>
            <td>
              <b>备注：</b>
            </td>
            <td colspan="3">
              {{form.note}}
            </td>
          </tr>

<!--          <tr>
            <td><b>排序号：</b></td>
            <td colspan="3">
              <el-form-item prop="sort">
                {{form.sort}}
              </el-form-item>
            </td>
          </tr>-->


        </table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {page,getOne,saveOrUpdate,del,checkisExist,checkHasRepeat} from "@/api/zc/clzs/directoryManagement/materialCategory.js";
import Treeselect from '@riophae/vue-treeselect';
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "MaterialCategory",
  components: {Treeselect},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      materialCategoryList: [],
      materialCategoryOid:"",
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
        categoryName: null,
        categoryCode:null
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {
        id: '', //逻辑主键
        materialCategoryOid:'',
        categoryName: '', // 类别名称
        categoryCode: '', // 类别编码
        note: '', // 备注
        sort: 0,//排序
      },
      // 表单校验
      rules: {
        categoryName: [
          { required: true, message: "类别名称不能为空", trigger: "blur" }
        ],
        categoryCode: [
          { required: true, message: "类别编码不能为空", trigger: "blur" }
        ]
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
        this.materialCategoryList = response.data.data;
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
        _that.openView = true;
        _that.title = "查看材料类别信息";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      _that.reset();
      if(row.id) {
        checkisExist(row.materialCategoryOid).then(response => {
          if(response.data=='N'){
            getOne(row.id).then(response => {
              _that.openInit = true;
              _that.form = response.data;
            });
          }else {
            _that.msgSuccess("存在相关联的材料目录不可修改");
          }
        });
        this.materialCategoryOid=row.materialCategoryOid;
      } else {
        this.materialCategoryOid="";
        _that.openInit = true;
      }
      _that.title = row.id ? "修改材料类别信息" : "新增材料类别信息";
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          checkHasRepeat(_that.materialCategoryOid,_that.form.categoryName,_that.form.categoryCode).then(responseGet => {
            if(responseGet.data=='0'){
              saveOrUpdate(_that.form).then(response => {
                if (response.code === 200) {
                  _that.msgSuccess("保存成功");
                  _that.openInit = false;
                  _that.materialCategoryOid="";
                  setTimeout(() => {
                    _that.getList();
                  }, 10);

                }
              });
            }else {
              _that.msgWarning("类别名称或类别编码已存在，不能重复添加！");
            }
          });

        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let _that = this;
      const oid = row.id;
      const mcoid = row.materialCategoryOid;
      checkisExist(mcoid).then(response => {
        if(response.data=='N'){
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
        }else {
          _that.msgSuccess("存在相关联的材料目录不可删除");
        }
      });

      /*_that.$confirm("是否确认删除?", "警告", {
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
        .catch(function() {});*/
    }
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
