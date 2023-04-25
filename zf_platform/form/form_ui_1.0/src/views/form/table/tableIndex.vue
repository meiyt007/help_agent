<template>
  <div class="app-container object-container">
    <!-- 添加或修改存储对象信息对话框 123 -->
    <div>
      <!--<h3>标题</h3>-->
      <el-form ref="form" :model="form" :rules='formTableList.rules'  label-width="0px" class="demo-ruleForm">
        <div class="el-table__header-wrapper dialog-table table-add">
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="18%" />
              <col width="32%" />
              <col width="18%" />
              <col width="32%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>数据库表名：</b></td>
              <td>
                {{tableName}}
              </td>
              <td><b>数据源：</b></td>
              <td>
                <el-form-item>
                  {{dataSource.connectionName}}
                </el-form-item>
              </td>
            </tr>
          </table>
        </div>
        <el-row :gutter="10" class="mb8 el-row-add" >
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="addColumn"
            >新增字段
            </el-button>
          </el-col>
          <el-tooltip class="el-tooltip-content-div" placement="top-start" effect="dark">
            <div slot="content">
              1.数据库字段类型只支持常用的字段类型。
              <br/>
              2.创建索引后，如取消索引，可能出现无法删除的情况，则需要手动到数据库进行删除操作。
              <br/>
              3.如出现最大长度，无法修改的情况，则长度为数据库固定长度。
            </div>
            <i class="el-icon-question"></i>
          </el-tooltip>
        </el-row>
        <el-table :data="form.formTableList" border>
          <!--<el-table-column type="selection" width="55" align="center"  />-->
          <el-table-column label="序号" width="65" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="字段名" min-width="300" align="center">
            <template slot-scope="scope">
              <el-form-item :prop="'formTableList.' + scope.$index + '.columnName'"
                            :rules="formTableList.rules.columnName">
                <el-input placeholder="请输入字段名" v-model.trim="scope.row.columnName" maxlength="50"
                          show-word-limit />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column v-if="'mongoDB' != dataSource.type" label="字段类型" width="160" align="center">
            <template slot-scope="scope">
              <el-form-item :prop="'formTableList.' + scope.$index + '.columnType'"
                            :rules="formTableList.rules.columnType">
                <el-select v-model="scope.row.columnType">
                  <el-option v-for="data in dataSourceTypeList" :key="data" :label="data" :value="data"></el-option>
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column v-if="'mongoDB' != dataSource.type" label="最大长度" width="200" align="center">
            <template slot-scope="scope">
              <el-form-item :prop="'formTableList.' + scope.$index + '.maxLength'"
                            :rules="formTableList.rules.maxLength">
                <el-input placeholder="请输入最大长度"
                  v-model.trim="scope.row.maxLength"
                  :disabled="dataSource.type == 'oracle' && (scope.row.columnType == 'LONG' || scope.row.columnType == 'TIMESTAMP')"
                  maxlength="10"
                  show-word-limit
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="是否必填" align="center"  width="80" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.isNotNull"
                :active-value="1"
                :inactive-value="0"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column v-if="'mongoDB' != dataSource.type" label='是否添加索引' align="center"  width="100" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.indexFlag"
                :active-value="1"
                :inactive-value="0"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="备注" min-width="200" align="center" prop="demo">
            <template slot-scope="scope">
              <el-form-item prop="demo">
                <el-input placeholder="请输入备注" v-model.trim="scope.row.demo" maxlength="40"
                          show-word-limit />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center"  width="100">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="deleteRow(scope.$index)">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer" align="right">
        <el-button type="primary" @click="submitForm">确 认</el-button>
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </div>
  </div>
</template>

<script>
  import {
    saveFormTableVo,
    getDataSourceTypeList,
    queryFormTableList,
    createTableByFormTableDto
  } from "@/api/form/table";
  import {
    getFormDataSourceByDataSourceOid
  } from "@/api/form/dataSource";

  import {
    validateLegalStrNoNumber,
    validIntInCludeZero
  } from '@/utils/validate'
  export default {
    name: "TableIndex",
    components: {

    },
    props: ['datasourceOid', 'tableName'],
    data() {
      let validataVariableLength = (rule, value, callback) => {
        if(''!=value && value.indexOf(',')==-1){
          const re = /^[0-9]{1,}[\d]*$/;
          const rsCheck = re.test(value);
          if (!rsCheck) {
            callback(new Error('请输入非负整数'));
            return;
          }
        }

        let index = rule.field.substring(rule.field.indexOf('.')+1,rule.field.lastIndexOf("."));
        let columnType = this.form.formTableList[index].columnType;
        let noLengthListMysql = ['BLOB','TEXT','DATE','TIME','TIMESTAMP','DATETIME'];
        let noLengthListPg = ['INTEGER','FLOAT8'];
        let noLengthList = [];
        noLengthList.push(...noLengthListMysql);
        noLengthList.push(...noLengthListPg);
        let type = this.dataSource.type;
        if(columnType){
          if('' == value || !value){
            callback(new Error('最大长度不能为空！'));
            return;
          }
          if('oracle' == type){
            if('LONG' == columnType  || 'TIMESTAMP' == columnType){
              this.form.formTableList[index].maxLength = '0';
            }
            if('DATE' == columnType){
              this.form.formTableList[index].maxLength = '7';
            }
          }
          if('mysql' == type){
            if('DOUBLE' == columnType){
              if(0==parseInt(value)){
                callback(new Error('当前字段类型最大长度不能为0'));
                return;
              }else if((''+value).indexOf(',')==-1){
                this.form.formTableList[index].maxLength = value + ',0';
              }
            }
          }
          if('postgreSQL' == type){
            if('CHARACTER' == columnType || 'VARCHAR'  == columnType || 'NUMERIC' == columnType){
              if(0==parseInt(value)){
                callback(new Error('当前字段类型最大长度不能为0'));
                return;
              }
            }
          }
          if('sqlserver' == type){
            if('CHARACTER' == columnType || 'VARCHAR'  == columnType || 'NUMERIC' == columnType){
              if(0==parseInt(value)){
                callback(new Error('当前字段类型最大长度不能为0'));
                return;
              }
            }
          }
          callback();
        }else{
          callback();
          //callback(new Error('请先选择字段类型'));
        }
      };
      let validataVariableName = (rule, value, callback) => {
        if (value != '') {
          //let index = rule.field.substring(rule.field.indexOf('.')+1,rule.field.lastIndexOf("."));
          let columnList = this.form.formTableList.filter(d => d.columnName == value);
          if (columnList.length > 1) {
            callback(new Error('当前字段名已存在，请重新输入'));
          } else {
            callback();
          }
        } else {
          callback();
        }
      };
      return {

        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 存储对象表格数据
        tableList: [],
        //参数
        params: {},
        // 弹出层标题
        title: "",
        // 查看显示弹出层
        openView: false,
        datasourceViewOptions: [],
        dataSource:{},
        // 启用状态
        ableMap: {
          '1': '启用',
          '0': '禁用'
        },
        dataSourceTypeList: [],
        // 表单参数
        form: {
          formTableList:[]
        },
        dataSourceOptions: [],
        // 表单校验
        formTableList: {
          rules: {
            columnName: [{
              required: true,
              message: '字段名不能为空',
              trigger: 'blur'
            },
              {
                required: true,
                validator: validateLegalStrNoNumber,
                trigger: 'blur'
              },
              {required: true, validator: validataVariableName, trigger: 'blur'}
            ],
            columnType:[ {required: true, message: '字段类型不能为空', trigger: 'change'}],
            maxLength:[ {required: true,validator: validataVariableLength,trigger: 'blur'},
             /* {required: true,validator: validIntInCludeZero,trigger: 'blur'}*/
              ],
          },
        }
      };
    },
    created() {
      if(!this.tableName){
        this.msgError("数据库表名不能为空");
        return false;
      }
      if(!this.datasourceOid){
        this.msgError("数据源不能为空");
        return false;
      }
      this.form.tableName = this.tableName;
      this.form.datasourceOid = this.datasourceOid;
      this.getFormList();
      this.getDataSourceType();
      this.getFormDataSource();
    },
    watch: {

    },
    methods: {
      //增加框
      addColumn() {
        let column = {index: this.form.formTableList.length,maxLength:'0'}
        if (!this.form.formTableList) {
          this.form.formTableList = []
        }
        this.$refs["form"].clearValidate();
        this.form.formTableList.push(column)
      },
      getDataSourceType(){
        getDataSourceTypeList(this.datasourceOid).then(response => {
          this.dataSourceTypeList = response.data;
        })
      },
      getFormDataSource(){
        getFormDataSourceByDataSourceOid(this.datasourceOid).then(response => {
          this.dataSource = response.data;
        })
      },
      getFormList() {
        let params = {
          tableName:this.tableName,
          datasourceOid:this.datasourceOid
        };
        queryFormTableList(params).then(response => {
           this.form.formTableList = response.data;
           if(this.form.formTableList.length > 0){
             this.form.isCreate = 1;
           }else{
             this.form.isCreate = 0;
           }
        })
      },
      // 取消按钮
      cancel() {
        this.$emit("closeView")
      },
      // 表单重置
      reset() {
        this.form = {
          name: null
        };
        this.resetForm("form");
      },
      closeView() {

      },
      deleteRow(index) {
        let that = this;
        this.$confirm('是否确认删除吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          that.form.formTableList.splice(index, 1);
          that.$refs["form"].clearValidate();
        }).then(() => {

        }).catch(function() {});
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            let that = this;
            that.$confirm('是否确认生成对应数据库表结构吗?', "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }).then(function() {
              saveFormTableVo(that.form).then(response => {
                if(that.form.isCreate == 1){
                  that.msgSuccess("更新成功");
                }else {
                  that.msgSuccess("创建成功");
                }
                that.form.isCreate = 1;
              })
            }).then(() => {

            }).catch(function() {});
          } else {
            return false;
          }
        });
      },
    }
  };

</script>
<style scoped>
  .object-container {
    padding-top: 0px !important;
  }
  .table-add{
    margin-bottom: 5px !important;
  }
  .el-tooltip-content-div{
    font-size: 25px !important;
  }
</style>
