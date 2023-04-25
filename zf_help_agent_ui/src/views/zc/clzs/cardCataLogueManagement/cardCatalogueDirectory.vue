
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--咨询数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="70px" @submit.native.prevent>
          <el-form-item label="目录名称" prop="cardCatalogueName">
            <el-input v-model.trim="queryParams.cardCatalogueName" placeholder="请输入目录名称" clearable size="small" @keyup.enter.native="handleQuery" />
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
             style="margin-bottom: 10px"
             @click="handleInit"
           >新增</el-button>
         </el-col>
        </el-row>

        <el-table v-loading="loading" :data="cardCatalogueList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="目录编码"  align="center" prop="cardCatalogueCode" :show-overflow-tooltip="true"/>
          <el-table-column label="目录名称"  align="center" prop="cardCatalogueName" :show-overflow-tooltip="true"/>
          <el-table-column label="创建时间" align="center" prop="createDate" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button  size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"  >查看</el-button>
              <el-button  size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInit(scope.row)" >修改</el-button>
              <el-button  size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)" >删除</el-button>
<!--              <el-button  size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleElement(scope.row)" >目录元素</el-button>-->
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>
    <!-- 添加或修改卡证目录 -->
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
              <b><i class="require">*</i>卡证目录编码：</b>
            </td>
            <td >
              <el-form-item prop="cardCatalogueCode">
                <el-col :span="24">
                  <el-input v-model.trim="form.cardCatalogueCode" placeholder="请输入卡证目录编码" maxlength="100" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b><i class="require">*</i>卡证目录名称：</b>
            </td>
            <td >
              <el-form-item prop="cardCatalogueName">
                <el-col :span="24">
                  <el-input v-model.trim="form.cardCatalogueName" placeholder="请输入卡证目录名称" maxlength="100" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>ocr类型：</b></td>
            <td colspan="3">
              <el-form-item prop="type">
                <el-form-item>
<!--                  <el-radio v-model="form.type" label="0">百度Ocr</el-radio>-->
                  <el-radio  v-model="form.type" label="1">textInOcr</el-radio>
                </el-form-item>
              </el-form-item>
            </td>
          </tr>

        </table>


        <el-button type="primary" @click="addHtml" class="add-btn">增加</el-button
        >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table--common zf-zc-table--td-center">
          <colgroup>
            <col width="220" />
            <col width="220" />
            <col width="160" />
          </colgroup>
          <tr>
            <th>目录元素编码</th>
            <th>目录元素名称</th>
            <th>操作</th>
          </tr>
          <template v-for="(ruleForm, index) in form.subList">
            <template v-if="ruleForm.deleteFlag === 0">
              <tr>
                <td>
                  <el-form-item
                    :prop="'subList.' + index + '.cardCatalogueElementCode'"
                    :rules="rules.cardCatalogueElementCode"
                  >
                    <el-input v-model.trim="ruleForm.cardCatalogueElementCode"></el-input>
                  </el-form-item>
                </td>

                <td>
                  <el-form-item
                    :prop="'subList.' + index + '.cardCatalogueElementName'"
                    :rules="rules.cardCatalogueElementName"
                  >
                    <el-input
                      v-model.trim="ruleForm.cardCatalogueElementName"
                    ></el-input>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item>
                    <el-button
                      style="border: 0px"
                      icon="el-icon-delete"
                      @click="delHtml(index)"
                      class="handle-btn"
                    ></el-button>
                  </el-form-item>
                </td>
              </tr>
            </template>
          </template>
        </table>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 信息详细 -->
<!--    <el-dialog v-dialog-drag :title="title" :visible.sync="openView" width="800px" v-if="openView" append-to-body>-->

    <el-dialog
      v-dialog-drag
      height="600px"
      scrollbar
      :title="title"
      :visible.sync="openView"
      v-if="openView"
      width="openView"
      append-to-body
    >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>卡证目录编码：</b>
            </td>
            <td colspan="3">
              {{form.cardCatalogueCode}}
            </td>
          </tr>
          <tr>
            <td>
              <b>卡证目录名称：</b>
            </td>
            <td colspan="3">
              {{form.cardCatalogueName}}
            </td>
          </tr>
        </table>


      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table--common zf-zc-table--td-center">
        <colgroup>
          <col width="220" />
          <col width="220" />
        </colgroup>
        <tr>
          <th>目录元素编码</th>
          <th>目录元素名称</th>
        </tr>
        <template v-for="(ruleForm, index) in form.subList">
          <template v-if="ruleForm.deleteFlag === 0">
            <tr>
              <td>
               {{ruleForm.cardCatalogueElementCode}}
              </td>
              <td>
                {{ruleForm.cardCatalogueElementName}}
              </td>
            </tr>
          </template>
        </template>
      </table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {page,getOne,saveOrUpdate,del} from "@/api/zc/clzs/cardCatalogueManagement/materialCatalog";
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
      cardCatalogueList: [],
      cardCatalogueOid:"",
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
        cardCatalogueName: null,
        cardCatalogueCode: null,
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      cardCatalogueElement:{},
      // 表单参数
      form: {
        oid: '', //逻辑主键
        cardCatalogueName: '', // 卡证目录名称
        cardCatalogueCode: '', // 卡证目录编码
        type:'1',
        subList: [],
      },
      // 表单校验
      rules: {
        cardCatalogueCode: [
          { required: true, message: "卡证目录编码不能为空", trigger: "blur" }
        ],
        cardCatalogueName: [
          { required: true, message: "卡证目录名称不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "ocr类型不能为空", trigger: "blur" }
        ],
        cardCatalogueElementCode: [{
          required: true,
          message: "卡证目录元素编码不能为空",
          trigger: "blur"
        }],
        cardCatalogueElementName: [{
          required: true,
          message: "卡证目录元素名称不能为空",
          trigger: "blur"
        }],


      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    //添加子项模块
    addHtml: function () {
      this.form.subList.push({
        deleteFlag: 0,
      })
    },
    //删除子项模块
    delHtml: function (index) {
      if(this.form.subList[index].id) {
        this.form.subList[index].deleteFlag = 1;
      }else{
        this.form.subList.splice(index, 1);
      }
      /*this.cardCatalogueElement = {};
      this.cardCatalogueElement = this.form.subList[index];
      this.cardCatalogueElement.deleteFlag = 1;
      this.form.subList.splice(index, 1);
      if (undefined != this.form.oid && null != this.form.oid) {
        if (this.cardCatalogueElement.cardCatalogueOid != '') {
          this.form.subList.push(this.cardCatalogueElement);
        }
      }*/
    },
    /** 查询卡证目录列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.cardCatalogueList = response.data.data;
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
      const oid = row.oid;
      getOne(oid).then(response => {
        _that.form = response.data;
        _that.openView = true;
        _that.title = "查看卡证目录信息";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      _that.reset();
      if(row.oid) {
        getOne(row.oid).then(response => {
          _that.openInit = true;
          _that.form = response.data;
         /* alert(JSON.stringify(_that.form ))*/
        });
      } else {
        this.materialCategoryOid="";
        _that.openInit = true;
        _that.form ={
          oid: '', //逻辑主键
          type:'1',
          cardCatalogueName: '', // 卡证目录名称
          cardCatalogueCode: '', // 卡证目录编码
          subList: [],
        };
      }
      _that.title = row.oid ? "修改卡证目录信息" : "新增卡证目录信息";
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
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
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let _that = this;
      const oid = row.oid;
      _that.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
          return del(oid);
        }).then(() => {
          _that.getList();
          _that.msgSuccess("删除成功");
        });
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
