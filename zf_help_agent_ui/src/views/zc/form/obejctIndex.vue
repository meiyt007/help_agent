<template>
  <div class="object-container">
    <!-- 添加或修改存储对象信息对话框 -->
    <!--<h3>标题</h3>-->
    <el-form
      ref="dataForm"
      :model="dataForm"
      :rules="rules"
      label-width="0px"
      class="demo-ruleForm"
    >
      <el-input v-show="false" v-model="dataForm.id" />
      <el-input v-show="false" v-model="dataForm.isAble" />
      <el-input v-show="false" v-model="dataForm.isDelete" />
      <el-input v-show="false" v-model="dataForm.createDate" />
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td><i class="require">*</i><b>存储对象名称：</b></td>
          <td colspan="3">
            <el-form-item prop="objectName">
              <el-input
                v-model.trim="dataForm.objectName"
                placeholder="请输入存储对象名称"
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><i class="require">*</i><b>所属模块：</b></td>
          <td>
            <el-form-item prop="moduleOid">
              <el-select
                v-model="dataForm.moduleOid"
                @change="moduleOidChange"
                filterable
                clearable
                placeholder="请选择"
              >
                <el-option
                  v-for="ty in moduleOptions"
                  :key="ty.moduleOid"
                  :label="ty.moduleName"
                  :value="ty.moduleOid"
                ></el-option>
              </el-select>
            </el-form-item>
          </td>
          <td><b>数据源：</b></td>
          <td>
            <el-form-item prop="datasourceOid">
              <el-select
                v-model="dataForm.datasourceOid"
                filterable
                clearable
                @change="dataSourceChange"
                placeholder="请选择"
              >
                <el-option
                  v-for="ty in dataSourceOptions"
                  :key="ty.datasourceOid"
                  :label="ty.connectionName"
                  :value="ty.datasourceOid"
                ></el-option>
              </el-select>
              <el-button type="primary" size="small" @click="editDatasource"
                >编 辑</el-button
              >
            </el-form-item>
          </td>
        </tr>
        <tr
          v-if="null != dataForm.datasourceOid && '' != dataForm.datasourceOid"
        >
          <td><i class="require">*</i><b>数据库表名：</b></td>
          <td colspan="3">
            <el-form-item prop="objectForm">
              <el-input
                v-model.trim="dataForm.objectForm"
                placeholder="请输入数据库表名"
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
      </table>

      <column
        ref="columnChild"
        :key="dataForm.datasourceOid"
        :params="params"
        :type="type"
      ></column>
    </el-form>
    <div slot="footer" align="center">
      <el-button type="primary" @click="submitForm" style="margin-top: 10px">
        确 定
      </el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>

    <el-dialog
      v-dialog-drag
      :visible.sync="view.show"
      v-for="view in datasourceViewOptions"
      title="编辑数据源"
      @close="closeView"
      width="1100px"
      append-to-body
    >
      <datasource-index
        :authorizeKey="authorizeKey"
        :moduleOid="view.moduleOid"
      ></datasource-index>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeView">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  saveObject,
  getFormObjectByObjectOid,
  dataSourcelist,
  modulelist,
  getFormMainByFormCode,
  datasourceColumn,
  createTableByFormTableDto
} from "@/api/form/manager";
import { batchUpdateSxFillField } from "@/api/zc/sxService/serviceFormConfig/formFillField.js";
import Column from "./columnIndex";
import DatasourceIndex from "./datasourceIndex";
export default {
  components: {
    Column,
    DatasourceIndex
  },
  props: [
    "authorizeKey",
    "formCode",
    "formMainOid",
    "fieldTypeOid",
    "contentOid",
    "type",
    "objectName",
    "serviceOid"
  ],
  name: "ObjectIndex",
  data() {
    const validateObjectForm = (rule, value, callback) => {
      if (value == undefined) {
        callback("数据库表名不能为空");
      } else {
        if (value.length <= 0) {
          callback("数据库表名不能为空");
        }
        if (
          value.length < 7 ||
          (value.length >= 7 && value.substring(0, 7) !== "t_form_")
        ) {
          callback("数据库表名前缀需是t_form_");
        }

        callback();
      }
    };
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,

      //参数
      params: {},
      // 弹出层标题
      title: "",

      datasourceViewOptions: [],

      moduleOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: ""
      },
      // 表单参数
      dataForm: {
        moduleOid: "",
        datasourceOid: "",
        objectName: "",
        // 数据库表名设置默认值
        objectForm: "t_form_"
      },
      dataSourceOptions: [],

      // 表单校验
      rules: {
        objectName: [
          {
            required: true,
            message: "存储对象名称不能为空",
            trigger: "blur"
          }
        ],
        objectCode: [
          {
            required: true,
            message: "存储对象编码不能为空",
            trigger: "blur"
          }
        ],
        objectForm: [
          {
            required: true,
            validator: validateObjectForm,
            trigger: "blur"
          }
        ],
        moduleOid: [
          {
            required: true,
            message: "请选择所属模块",
            trigger: "blur"
          }
        ]
      }
    };
  },
  created() {
    this.initObjectIndex();
  },
  watch: {
    // 数据库表名的填写
    "dataForm.objectForm": {
      handler(val) {
        this.params.objectForm = val;
      }
    },
    /* openCreateTable: {
      deep: true,*/
    handler(val) {
      if (val) {
        this.initObjectIndex();
      }
    }
    // }
  },
  methods: {
    //初始化数据
    initObjectIndex() {
      if (!this.authorizeKey || "" === this.authorizeKey) {
        this.msgError("参数错误：authorizeKey is null");
        return false;
      }
      this.getForm();
      this.queryMoudleList();
      this.params.contentOid = this.contentOid;
      this.params.fieldTypeOid = this.fieldTypeOid;
    },

    getForm() {
      this.reset();
      this.params = {};
      if (this.formCode && "" !== this.formCode) {
        getFormMainByFormCode(this.formCode).then(response => {
          let oid = response.data.objectOid;
          if (oid !== undefined) {
            getFormObjectByObjectOid(oid).then(res => {
              this.dataForm = res.data;
              this.getDataSourcelist(res.data.moduleOid);
              this.params.objectOid = res.data.objectOid;
              this.params.datasourceOid = res.data.datasourceOid;
              this.params.moduleOid = res.data.moduleOid;
            });
          }
        });
      } else {
        this.dataForm.moduleOid = "";
        this.params.objectOid = "";
        this.params.datasourceOid = "";
        this.params.moduleOid = "";
      }
    },

    editDatasource() {
      if ("" === this.dataForm.moduleOid) {
        this.msgError("所属模块不能为空");
        return false;
      }
      let view = {
        show: true,
        moduleOid: this.dataForm.moduleOid
      };
      this.datasourceViewOptions.push(view);
    },
    getDataSourcelist(moduleOid) {
      let params = {
        authorizeKey: this.authorizeKey,
        moduleOid: moduleOid
      };
      dataSourcelist(params)
        .then(response => {
          this.dataSourceOptions = response.data;
        })
        .catch(e => {
          console.log(e, "获取数据源出错");
        });
    },
    queryMoudleList() {
      let par = {
        authorizeKey: this.authorizeKey
      };
      modulelist(par).then(response => {
        this.moduleOptions = response.data;
      });
    },
    //模块的切换
    moduleOidChange(moduleOid) {
      this.dataForm.moduleOid = moduleOid;
      this.params.datasourceOid = "";
      this.dataSourceOptions = [];
      if ("" !== moduleOid) {
        this.getDataSourcelist(moduleOid);
      }
    },
    // 取消按钮
    cancel() {
      this.reset();
      this.$emit("cancle-close");
    },
    // 表单重置
    reset() {
      this.resetForm("dataForm");
    },
    closeView() {
      this.datasourceViewOptions.pop();
      if (this.dataForm.moduleOid) {
        this.getDataSourcelist(this.dataForm.moduleOid);
      }
      this.$emit("design-close");
    },

    /** 提交按钮 */
    submitForm() {
      if (!this.authorizeKey) return this.$message.warning("保存失败");
      this.dataForm.authorizeKey = this.authorizeKey;
      this.dataForm.formCode = this.formCode;
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          //调用子类的方法获取表单内容
          this.$refs.columnChild.getFormData().then(columnList => {
            this.dataForm.columnList = columnList;
            if (null == columnList || columnList.length === 0) {
              this.msgError("存储对象表结构不能为空");
              return false;
            } else {
              let flag = true;
              let columnList = this.dataForm.columnList;
              for (let item in columnList) {
                if (
                  null == columnList[item].fieldCode ||
                  "" === columnList[item].fieldCode
                ) {
                  this.msgWarning("字段名不能为空！");
                  flag = false;
                  return false;
                }
                if (
                  null == columnList[item].columnType ||
                  "" === columnList[item].columnType
                ) {
                  this.msgWarning("字段类型不能为空！");
                  flag = false;
                  return false;
                } else if ("VARCHAR" === columnList[item].columnType) {
                  let numberReg = /^[1-9]\d*$/;
                  if (
                    null == columnList[item].columnLenght ||
                    "" === columnList[item].columnLenght
                  ) {
                    this.msgWarning("最大长度不能为空！");
                    flag = false;
                    return false;
                  }
                  if (!numberReg.test(columnList[item].columnLenght)) {
                    this.msgWarning("最大长度只能是正整数！");
                    flag = false;
                    return false;
                  }
                }
              }
              if (
                null != this.dataForm.datasourceOid &&
                "" !== this.dataForm.datasourceOid
              ) {
                this.dataForm.saveType = 0;
              }
              let colArr = [];
              // 需要更新的集合
              let colArrUpdate = [];
              this.dataForm.columnList.forEach(col => {
                let colObj = {
                  tableName: this.dataForm.objectForm,
                  columnName: col.fieldCode,
                  columnType: col.columnType,
                  maxLenth: col.columnLenght,
                  demo: col.fieldName
                };
                let colObjUpdate = {
                  id: col.id,
                  fieldCode: col.fieldCode,
                  columnType: col.columnType,
                  columnLenght: col.columnLenght
                };
                colArrUpdate.push(colObjUpdate);
                colArr.push(colObj);
              });
              let jsonstr = JSON.stringify(colArr);
              if (flag) {
                // 创建表
                createTableByFormTableDto(
                  jsonstr,
                  this.dataForm.datasourceOid,
                  this.dataForm.objectForm,
                  this.formCode
                )
                  .then(res1 => {
                    if (res1.code === 200) {
                      datasourceColumn(this.params).then(res2 => {
                        if (res2.code === 200) {
                          this.dataForm.columnList = res2.data;
                          this.dataForm.isSave = 1;
                          saveObject(this.dataForm).then(response => {
                            if (response.code === 200) {
                              //   this.msgSuccess("操作成功！");
                              this.$emit(
                                "design-close",
                                response.data,

                                this.dataForm.objectName
                              );
                            }
                          });
                        }
                      });
                    }
                  })
                  .catch(error => {
                    console.log(error);
                  });
                // 更新字段类型和长度
                this.updateFillFieldInfoList(colArrUpdate);
              }
            }
          });
        }
      });
    },
    //数据源切换
    dataSourceChange(data) {
      if ("" === data) {
        this.$message.error("数据库配置异常，请检查或重新配置");
      } else {
        this.dataForm.datasourceOid = data;
        this.params.datasourceOid = data;
      }
      this.$refs["dataForm"].clearValidate();
    },

    // 更新字段信息集合
    updateFillFieldInfoList(colArrUpdate) {
      // 事项
      batchUpdateSxFillField(colArrUpdate).then(response => {
        if (response.code === 200) {
          console.log("更新事项字段信息成功：");
          console.log(colArrUpdate);
        }
      });
    }
  }
};
</script>
<style>
.object-container {
  padding-top: 0px !important;
}
</style>
