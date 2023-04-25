/**
* @Author: liangss
* @Date: 2020-11-03 14:04:48
* @Last Modified by: liangss
* @Description: 材料类别管理
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="70px">
          <el-form-item label="元素编码" prop="elementCode">
            <el-input v-model.trim="queryParams.elementCode" placeholder="请输入元素编码" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="元素名称" prop="elementName">
            <el-input v-model.trim="queryParams.elementName" placeholder="请输入元素名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>


       <el-row :gutter="10" class="mb8">
       <!--   <el-col :span="1.5">
            <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit" v-hasPermi="['sys:AreaSite:init']">新增</el-button>
          </el-col>-->

         <el-col :span="1.5">
           <el-button
             type="default"
             icon="el-icon-plus"
             size="mini"
             @click="handleInit"
           >新增</el-button>
         </el-col>
        </el-row>



        <el-table v-loading="loading" :data="materialCategoryList" border>
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="元素编码" align="center" prop="elementCode" :show-overflow-tooltip="true"/>
          <el-table-column label="元素名称" align="center" prop="elementName" :show-overflow-tooltip="true"/>
          <el-table-column label="创建时间" align="center" prop="createDate" :show-overflow-tooltip="true"/>
          <el-table-column label="修改时间" align="center" prop="modifyDate" :show-overflow-tooltip="true"/>
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
<!--    <div slot="footer" class="dialog-footer">-->
<!--      <el-button @click="cancel">取 消</el-button>-->
<!--    </div>-->
    <!-- 添加或修改材料类别 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openInit" v-if="openInit" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
       <!--   elementName  elementCode-->
          <tr v-if="statusType=='edit'">
            <td>
              <b><i class="require">*</i>元素名称：</b>
            </td>
            <td >
              <el-form-item prop="elementName">
                <el-col :span="24">
                  <el-input v-model.trim="form.elementName" placeholder="请输入元素名称" maxlength="50" show-word-limit />
                </el-col>
              </el-form-item>
            </td>

            <td >
              <b>元素编码：</b>
            </td>
            <td >
              <el-form-item prop="elementCode">
              <!--  <el-col :span="24">-->
                  {{form.elementCode}}
                 <!-- <el-input readonly v-model.trim="form.elementCode" placeholder="请输入元素编码" maxlength="50" show-word-limit />-->
              <!--  </el-col>-->
              </el-form-item>
            </td>

          </tr>

          <tr v-if="statusType=='add'">
            <td>
              <b><i class="require">*</i>元素名称：</b>
            </td>
            <td  colspan="3">
              <el-form-item prop="elementName">
                <el-col :span="24">
                  <el-input v-model.trim="form.elementName" placeholder="请输入元素名称" maxlength="50" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
<!--          @change="selectModel"-->
          <tr >
            <td>
              <b>卡证目录元素名称：</b>
            </td>
            <td  colspan="3">
              <el-form-item prop="cardCatalogueElementOid">
                <el-select
                  v-model="form.cardCatalogueElementOid"
                  placeholder="请选择卡证目录元素"
                  :clearable="true"
                >
                  <el-option
                    v-for="mco in cardCatalogueElementListOptions"
                    :key="mco.oid"
                    :label="mco.cardCatalogueElementName"
                    :value="mco.oid"
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

    <!-- 咨询登记信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" width="800px" v-if="openView" append-to-body>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>元素名称：</b>
            </td>
            <td >

                {{form.elementName}}
            </td>
            <td>
              <b>元素编码：</b>
            </td>
            <td >
                {{form.elementCode}}
            </td>
          </tr>
          <tr>

            <td>
              <b>卡证目录元素名称：</b>
            </td>
            <td colspan="3" >
              {{form.cardCatalogueElementName}}
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
import {page,getOne,saveOrUpdate,del,checkIsRelation,checkMaterialCatalogChlidRepeat,
  queryFaModelRuleValidationList,queryComboFaModelRuleValidationList,getCardCatalogueElementList} from "@/api/zc/clzs/directoryManagement/materialCatalogElement.js";
import Treeselect from '@riophae/vue-treeselect';
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {checkisExist} from "@/api/zc/clzs/directoryManagement/materialCategory";
export default {
  name: "advisoryRegistration",
  props: ['materialCatalogOid','materialParentOid','cardCatalogueOid'],
  components: {Treeselect},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      materialCategoryList: [],
      //卡证目录元素
      cardCatalogueElementListOptions:[],
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      statusType:'',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        elementCode: null,
        elementName:null,
        materialCatalogOid:this.materialCatalogOid,
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {
        id: '', //逻辑主键
        elementName: '',
        elementCode: '',
        cardCatalogueElementOid:'',

      },
      // 表单校验
      rules: {
        elementName: [
          { required: true, message: "元素名称不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    //获取卡证目录元素
    getCardCatalogueElementList(this.cardCatalogueOid).then(response => {
      this.cardCatalogueElementListOptions = response.data;
    });

  },
  methods: {
    //获取选择材料类别的名称
    selectModel (val) {
      this.form.cardCatalogueElementCode = val ? this.cardCatalogueElementListOptions.find(ele => ele.oid === val).cardCatalogueElementCode : '';
      this.form.cardCatalogueElementName = val ? this.cardCatalogueElementListOptions.find(ele => ele.oid === val).cardCatalogueElementName : '';
    },
    /** 查询材料目录元素列表 */
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
        _that.title = "查看材料目录元素信息";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      const mcoid=row.materialCatalogElementOid;
      _that.reset();
      if(row.id) {
        _that.statusType='edit';
        //checkIsRelation(mcoid).then(response => {
        //判断是否存在已配置的事项目录规则
      /*  queryFaModelRuleValidationList(mcoid).then(response => {
          let list = [];
          list = response.data;
          if (list && list.length > 0) {
             _that.msgSuccess("该元素存在已配置的验证规则该元素不可修改");
          }else {
            queryComboFaModelRuleValidationList(mcoid).then(response => {
              let comboList = [];
              comboList = response.data;
              if (comboList && comboList.length > 0) {
                _that.msgSuccess("该元素存在已配置的验证规则该元素不可修改");
              }else {
                getOne(row.id).then(response => {
                  _that.openInit = true;
                  _that.form = response.data;
                  _that.subList=response.data.subList;
                });
              }
          })
          }
        });*/

        getOne(row.id).then(response => {
          _that.openInit = true;
          _that.form = response.data;
          _that.subList=response.data.subList;
        });
      } else {
        _that.form.cardCatalogueElementOid="";
        _that.statusType='add';
        _that.openInit = true;
      }
      _that.title = row.id ? "修改材料目录元素信息" : "新增材料目录元素信息";
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      _that.form.materialCatalogOid=this.materialCatalogOid;
      _that.form.materialParentOid=this.materialParentOid;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          //判断子级目录名称是否重复
          checkMaterialCatalogChlidRepeat(_that.form.materialCatalogOid,_that.form.materialCatalogElementOid,_that.form.elementName).then(responseGet => {
            if(responseGet.data=='0'){
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
              _that.msgWarning("该目录下已存在相同元素名称，不能重复添加！");
            }
          });

        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let _that = this;
      const oid = row.id;
      const mcoid=row.materialCatalogElementOid;
      //判断是否存在已配置的事项目录规则
      queryFaModelRuleValidationList(mcoid).then(response => {
        let list = [];
        list = response.data;
        if (list && list.length > 0) {
          _that.msgSuccess("该元素存在已配置的验证规则该元素不可删除");
        }else {
          queryComboFaModelRuleValidationList(mcoid).then(response => {
            let comboList = [];
            comboList = response.data;
            if (comboList && comboList.length > 0) {
              _that.msgSuccess("该元素存在已配置的验证规则该元素不可删除");
            }else {
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


 /*     checkIsRelation(mcoid).then(response => {
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
          _that.msgSuccess("存在已启用的识别模板是否选用该元素不可删除");
        }
      });*/

    },
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
