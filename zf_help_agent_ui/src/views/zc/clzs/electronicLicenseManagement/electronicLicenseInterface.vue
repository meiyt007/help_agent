
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--咨询数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="70px" @submit.native.prevent>
          <el-form-item label="证照名称" prop="electronicLicenseName">
            <el-input v-model.trim="queryParams.electronicLicenseName" placeholder="请输入证照名称" clearable size="small" @keyup.enter.native="handleQuery" />
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

        <el-table v-loading="loading" :data="electronicLicenseList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="证照名称"  align="center" prop="electronicLicenseName" :show-overflow-tooltip="true"/>
          <el-table-column label="证照编码"  align="center" prop="electronicLicenseCode" :show-overflow-tooltip="true"/>
          <el-table-column label="创建时间"align="center" prop="createDate" :show-overflow-tooltip="true"/>
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
              <b><i class="require">*</i>证照名称：</b>
            </td>

            <td>
<!--              {{form.electronicLicenseName}}-->
              <el-form-item prop="electronicLicenseName">
                <el-select
                  v-model="form.electronicLicenseOid"
                  filterable
                  clearable
                  style="width: 50%"
                  @change="getBillOid($event)"
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
<!--            <td >
              <el-form-item prop="electronicLicenseName">
                <el-col :span="24">
                  <el-input v-model.trim="form.electronicLicenseName" placeholder="请输入证照名称" maxlength="100" show-word-limit />
                </el-col>
              </el-form-item>
            </td>-->
            <td>
              <b><i class="require">*</i>证照编码：</b>
            </td>
            <td >
              <el-form-item prop="electronicLicenseCode">
                <el-col :span="24">
                  <el-input v-model.trim="form.electronicLicenseCode" placeholder="请输入证照编码" maxlength="100" show-word-limit />
                </el-col>
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
            <th>证照元素名称</th>
            <th>证照元素编码</th>
            <th>操作</th>
          </tr>
          <template v-for="(ruleForm, index) in form.subList">
            <template v-if="ruleForm.deleteFlag === 0">
              <tr>

                <td>
                  <el-form-item
                    :prop="'subList.' + index + '.electronicLicenseElementName'"
                    :rules="rules.electronicLicenseElementName"
                  >
                    <el-input
                      v-model.trim="ruleForm.electronicLicenseElementName"
                    ></el-input>
                  </el-form-item>
                </td>

                <td>
                  <el-form-item
                    :prop="'subList.' + index + '.electronicLicenseElementCode'"
                    :rules="rules.electronicLicenseElementCode"
                  >
                    <el-input v-model.trim="ruleForm.electronicLicenseElementCode"></el-input>
                  </el-form-item>
                </td>

                <td>
                  <el-form-item>
                    <el-button
                      style="border: 0px"
                      icon="el-icon-delete"
                      @click="delHtml(ruleForm,index)"
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
              <b>证照编码：</b>
            </td>
            <td colspan="3">
              {{form.electronicLicenseCode}}
            </td>
          </tr>
          <tr>
            <td>
              <b>证照名称：</b>
            </td>
            <td colspan="3">
              {{form.electronicLicenseName}}
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
               {{ruleForm.electronicLicenseElementCode}}
              </td>
              <td>
                {{ruleForm.electronicLicenseElementName}}
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
import {page,getOne,saveOrUpdate,del,checkHasRepeat} from "@/api/zc/clzs/electronicLicenseManagement/electronicLicenseInterface";
import Treeselect from '@riophae/vue-treeselect';
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { queryMaterBill } from '@/api/zc/businessManagement/sxSerMaterBill'
export default {
  name: "ElectronicLicenseInterface",
  components: {Treeselect},
  data() {
    return {
      // 遮罩层
      billList: [],
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      electronicLicenseList: [],
      electronicLicenseInterfaceOid:"",
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
        electronicLicenseName: null,
        electronicLicenseCode: null,
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      electronicLicenseElement:{},
      // 表单参数
      form: {
        oid: '', //逻辑主键
        electronicLicenseName: '', // 证照目录名称
        electronicLicenseCode: '', // 证照目录编码
        electronicLicenseOid: '', //证照目录oid
        type:'',
        subList: [],
      },
      // 表单校验
      rules: {
        electronicLicenseCode: [
          { required: true, message: "证照目录编码不能为空", trigger: "blur" }
        ],
        electronicLicenseName: [
          { required: true, message: "证照目录名称不能为空", trigger: "blur" }
        ],
        electronicLicenseElementCode: [{
          required: true,
          message: "证照目录元素编码不能为空",
          trigger: "blur"
        }],
        electronicLicenseElementName: [{
          required: true,
          message: "证照目录元素名称不能为空",
          trigger: "blur"
        }],


      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getBillOid(obj) {
      this.billList.forEach(item => {
        if(item.billOid==obj){
          this.form.electronicLicenseName = item.directoryName;
          this.form.electronicLicenseCode = item.directoryCode;
          this.form.electronicLicenseOid = item.billOid;
        }
      })
    //  console.log(JSON.stringify(obj));
    },
    //添加子项模块
    addHtml: function () {
      this.form.subList.push({
        deleteFlag: 0,
      })
    },
    //删除子项模块
    delHtml: function (form, index) {
      if(this.form.subList[index].id) {
        this.form.subList[index].deleteFlag = 1;
      }else{
        this.form.subList.splice(index, 1);
      }
    },
    /** 查询卡证目录列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.electronicLicenseList = response.data.data;
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
        _that.title = "查看证照目录信息";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      _that.reset();
      _that.getBillList();
      if(row.oid) {

        this.electronicLicenseInterfaceOid=row.oid;
        getOne(row.oid).then(response => {
          _that.openInit = true;
          _that.form = response.data;
        });
      } else {
        this.electronicLicenseInterfaceOid="";
        _that.openInit = true;
        _that.form ={
          oid: '', //逻辑主键
          electronicLicenseName: '', // 电子证照目录名称
          electronicLicenseCode: '', // 电子证照目录编码
          electronicLicenseOid: '', // 电子证照目录oid
          subList: [],
        };
      }
      _that.title = row.oid ? "修改证照目录信息" : "新增证照目录信息";
      },
    getBillList () {
      let _that = this;
      // 查询文书模板数据
      queryMaterBill().then(response => {
        if (response.data) {
          _that.billList = response.data;
          _that.billList.forEach(item => {
            if (item.directoryObj == 1) {
              item.directoryName = item.directoryName + "[法人]"
            } else if (item.directoryObj == 2) {
              item.directoryName = item.directoryName + "[混合]"
            } else if (item.directoryObj == 3) {
              item.directoryName = item.directoryName + "[其他]"
            } else if (item.directoryObj == 0) {
              item.directoryName = item.directoryName + "[自然人]"
            }
          })
        }

      });
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          checkHasRepeat(this.electronicLicenseInterfaceOid ,_that.form.electronicLicenseCode).then(responseGet => {
            if(responseGet.data=== 'false'){
              saveOrUpdate(_that.form).then(response => {
                if (response.code === 200) {
                  _that.msgSuccess("保存成功");
                  _that.openInit = false;
                  setTimeout(() => {
                    _that.getList();
                  }, 10);
                }
              });
            }else {
              _that.msgWarning("该证照已添加，不能重复添加！");
            }
            this.electronicLicenseInterfaceOid="";
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
