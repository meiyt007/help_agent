<template>
  <div class="object-container">
    <!-- 添加或修改存储对象信息对话框 -->
    <div>
      <!--<h3>标题</h3>-->
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <el-input v-show="false" v-model="form.id" />
        <el-input v-show="false" v-model="form.isAble" />
        <el-input v-show="false" v-model="form.isDelete" />
        <el-input v-show="false" v-model="form.createDate" />
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
                  v-model.trim="form.objectName"
                  placeholder="请输入存储对象名称"
                  maxlength="50"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <!--<td><i class="require">*</i><b>存储对象编码：</b></td>
            <td>
              <el-form-item prop="objectCode">
                <el-input v-model.trim="form.objectCode" placeholder="请输入存储对象编码" maxlength="50" show-word-limit/>
              </el-form-item>
            </td>-->
          </tr>
          <tr>
            <td><i class="require">*</i><b>所属模块：</b></td>
            <td>
              <el-form-item prop="moduleOid">
                <el-select
                  v-model="form.moduleOid"
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
            <td><i class="require">*</i><b>数据源：</b></td>
            <td>
              <el-form-item prop="datasourceOid">
                <el-select
                  v-model="form.datasourceOid"
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
          <tr v-if="null != form.datasourceOid && '' != form.datasourceOid">
            <td><i class="require">*</i><b>数据库表名：</b></td>
            <td colspan="3">
              <el-form-item prop="objectForm">
                <el-input
                  v-model.trim="form.objectForm"
                  placeholder="请输入数据库表名"
                  maxlength="30"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
        </table>

        <column
          ref="columnChild"
          :openCreateTable="openCreateTable"
          :type="type"
          :key="form.datasourceOid"
          :params="params"
        ></column>
      </el-form>
      <div slot="footer" class="dialog-footer" align="right">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </div>

    <el-dialog
      v-dialog-drag
      :visible.sync="view.show"
      v-for="view in datasourceViewOptions"
      title="编辑数据源"
      @close="closeView"
      width="90%"
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
  getFormConfigJson,
  getFormCodes
} from "@/api/onething/comboForm/formManage";
import {
  save,
  saveObject,
  mergeFormByFormCodes,
  getFormObjectByObjectOid,
  dataSourcelist,
  modulelist,
  getFormMainByFormCode,
  datasourceColumn,
  createTableByFormTableDto
} from "@/api/form/manager";
import { batchUpdateSxFillField } from "@/api/zc/sxService/serviceFormConfig/formFillField.js";
import { batchUpdateComboFillField } from "@/api/onething/comboForm/fieldManage.js";
import Column from "@/views/zc/form/oneThingColumnIndex.vue";
import DatasourceIndex from "@/views/zc/form/datasourceIndex";
export default {
  name: "ComboObjectIndex",
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
    "openCreateTable",
    "type",
    "objectName"
  ],
  data () {
    const validateObjectForm = (rule, value, callback) => {
      if (value.length <= 0) {
        callback("数据库表名不能为空");
      }
      if (
        value.length < 7 ||
        (value.length >= 7 && value.substring(0, 7) !== "t_form_")
      ) {
        callback("数据库表名前缀需是t_form_");
      } else {
        callback("");
      }
    };
    return {
      //授权id
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 存储对象表格数据
      objectList: [],
      objectOptions: {
        "0": "存储对象",
        "1": "逻辑对象"
      },
      //参数
      params: {},
      // 弹出层标题
      title: "",
      // 查看显示弹出层
      openView: false,
      datasourceViewOptions: [],
      formConfigJson: "",
      formCodes: "",
      // 启用状态
      ableMap: {
        "1": "启用",
        "0": "禁用"
      },
      moduleOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: ""
      },
      // 表单参数
      form: {
        // 数据库表名设置默认值
        objectForm: "t_form_"
      },
      dataSourceOptions: [],
      columnDialogOptions: [],
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
        ],
        datasourceOid: [
          {
            required: true,
            message: "请选择数据源",
            trigger: "blur"
          }
        ]
      }
    };
  },
  created () {
    console.log(
      this.formCode + "--" + this.authorizeKey + "---" + this.contentOid
    );
    this.form.objectName = this.objectName;
    this.initObjectIndex();
  },
  watch: {
    // 数据库表名的填写
    "form.objectForm": {
      handler (val) {
        this.params.objectForm = val;
      }
    },
    openCreateTable: {
      deep: true,
      handler (val) {
        if (val) {
          this.initObjectIndex();
        }
      }
    }
  },
  methods: {
    //初始化数据
    initObjectIndex () {
      if (!this.authorizeKey || "" === this.authorizeKey) {
        this.msgError("参数错误：authorizeKey is null");
        return false;
      }
      this.getForm();
      this.queryMoudleList();
      this.params.contentOid = this.contentOid;
      this.params.fieldTypeOid = this.fieldTypeOid;
      if ("1" === this.type) {
        this.getFormCodes();
        this.getFormConfigJson();
      }
    },

    getForm () {
      this.reset();
      this.params = {};
      if (this.formCode && "" !== this.formCode) {
        getFormMainByFormCode(this.formCode).then(response => {
          let oid = response.data.objectOid;
          if (oid !== undefined) {
            getFormObjectByObjectOid(oid).then(response => {
              this.form = response.data;
              this.getDataSourcelist(this.form.moduleOid);
              this.params.objectOid = this.form.objectOid;
              this.params.datasourceOid = this.form.datasourceOid;
              this.params.moduleOid = this.form.moduleOid;
            });
          }
        });
      }
    },

    editDatasource () {
      if ("" === this.form.moduleOid) {
        this.msgError("所属模块不能为空");
        return false;
      }
      let view = {
        show: true,
        moduleOid: this.form.moduleOid
      };
      this.datasourceViewOptions.push(view);
    },
    getDataSourcelist (moduleOid) {
      let params = {
        authorizeKey: this.authorizeKey,
        moduleOid: moduleOid
      };
      dataSourcelist(params)
        .then(response => {
          this.dataSourceOptions = response.data;
        })
        .catch(function (e) {
          console.log(e, "获取数据源出错");
        });
    },
    queryMoudleList () {
      let par = {
        authorizeKey: this.authorizeKey
      };
      modulelist(par).then(response => {
        this.moduleOptions = response.data;
      });
    },
    //模块的切换
    moduleOidChange (moduleOid) {
      this.$set(this.form, "datasourceOid", null);
      this.params.datasourceOid = "";
      this.dataSourceOptions = [];
      if ("" !== moduleOid) {
        this.getDataSourcelist(moduleOid);
      }
    },
    // 取消按钮
    cancel () {
      this.reset();
      this.$emit("design-close");
    },
    // 表单重置
    reset () {
      this.form = {
        name: null
      };
      this.resetForm("form");
    },
    closeView () {
      this.datasourceViewOptions.pop();
      if (this.form.moduleOid) {
        this.getDataSourcelist(this.form.moduleOid);
      }
    },

    /** 提交按钮 */
    submitForm () {
      if (null == this.form.authorizeKey)
        this.form.authorizeKey = this.authorizeKey;
      this.form.formCode = this.formCode;
      this.$refs["form"].validate(valid => {
        if (valid) {
          //调用子类的方法获取表单内容
          this.$refs.columnChild.getFormData().then(columnList => {
            this.form.columnList = columnList;
            if (null == columnList || columnList.length === 0) {
              this.msgError("存储对象表结构不能为空");
              return false;
            } else {
              //formObject.getSaveType()
              let flag = true;
              let columnList = this.form.columnList;
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
                    null == columnList[item].maxLenth ||
                    "" === columnList[item].maxLenth
                  ) {
                    this.msgWarning("最大长度不能为空！");
                    flag = false;
                    return false;
                  }
                  if (!numberReg.test(columnList[item].maxLenth)) {
                    this.msgWarning("最大长度只能是正整数！");
                    flag = false;
                    return false;
                  }
                }
              }
              if (
                null != this.form.datasourceOid &&
                "" !== this.form.datasourceOid
              ) {
                this.form.saveType = 0;
              }
              let colArr = [];
              // 需要更新的集合
              let colArrUpdate = [];
              this.form.columnList.forEach(col => {
                let colObj = {
                  tableName: this.form.objectForm,
                  columnName: col.fieldCode,
                  columnType: col.columnType,
                  maxLenth: col.maxLenth,
                  demo: col.fieldName
                };
                let colObjUpdate = {
                  id: col.id,
                  fieldCode: col.fieldCode,
                  columnType: col.columnType,
                  maxLenth: col.maxLenth
                };
                colArrUpdate.push(colObjUpdate);
                colArr.push(colObj);
              });
              let jsonstr = JSON.stringify(colArr);
              if (flag) {
                // 创建表
                if ("1" === this.type) {
                  if (this.formCodes == null || this.formCodes === "") {
                    this.msgWarning("事项表单未设计完成！");
                    return false;
                  }
                  if (
                    this.formConfigJson === null ||
                    this.formConfigJson === ""
                  ) {
                    this.msgWarning("找不到一件事标签和字段信息！");
                    return false;
                  }
                }
                createTableByFormTableDto(
                  jsonstr,
                  this.form.datasourceOid,
                  this.form.objectForm,
                  this.formCode
                ).then(res1 => {
                  if (res1.code === 200) {
                    datasourceColumn(this.params).then(res2 => {
                      if (res2.code === 200) {
                        this.form.columnList = res2.data;
                        this.form.isSave = 1;
                        saveObject(this.form).then(response => {
                          if (response.code === 200) {
                            this.msgSuccess("操作成功！");
                            this.$emit("select-datasource", response.data);
                          }
                        });
                        // if ("0" === this.type) {
                        //   saveObject(this.form).then(response => {
                        //     if (response.code === 200) {
                        //       this.msgSuccess("操作成功！");
                        //       this.$emit(
                        //         "select-datasource",
                        //         response.data.formCode
                        //       );
                        //     }
                        //   });
                        // } else if ("1" === this.type) {
                        //   //一件事
                        //   save(this.form).then(response => {
                        //     if (response.code === 200) {
                        //       let objectOid = response.data.objectOid;
                        //       let saveDataType = response.data.saveType;
                        //       let params = {
                        //         formCodes: this.formCodes,
                        //         objectOid: objectOid,
                        //         saveDataType: saveDataType,
                        //         formConfigJson: this.formConfigJson,
                        //         formCode: this.formCode
                        //       };
                        //       console.log(params);
                        //       mergeFormByFormCodes(params).then(res => {
                        //         if (res.code === 200) {
                        //           console.log(res.data, "保存返回信息");
                        //           this.msgSuccess("操作成功！");
                        //           this.$emit("select-datasource", res.data);
                        //         }
                        //       });
                        //     }
                        //   });
                        // } else {
                        //   this.msgWarning("参数错误！");
                        //   return false;
                        // }
                      }
                    });
                  }
                });
                // 更新字段类型和长度
                this.updateFillFieldInfoList(this.type, colArrUpdate);
              }
            }
          });
        } else {
          return false;
        }
      });
    },
    getFormCodes () {
      this.formCodes = "";
      console.log(this.contentOid);
      getFormCodes(this.contentOid, this.fieldTypeOid).then(response => {
        this.formCodes = response.data;
      });
    },
    getFormConfigJson () {
      this.formConfigJson = "";
      getFormConfigJson(this.contentOid, this.fieldTypeOid).then(response => {
        this.formConfigJson = response.data;
        console.log(this.formConfigJson, "formConfigJson");
      });
    },
    //数据源切换
    dataSourceChange (data) {
      if ("" === data) {
        this.$set(this.form, "datasourceOid", "");
        this.$set(this.form, "objectForm", "t_form_");
      }
      this.$set(this.form, "objectForm", "t_form_");
      this.params.datasourceOid = data;
      this.$refs["form"].clearValidate();
    },
    //打开表结构弹框
    openColumnView (row) {
      let item = {
        show: true,
        columnOid: row.columnOid
      };
      this.columnDialogOptions.push(item);
    },
    //关闭表结构弹框
    closeColumnView () {
      this.columnDialogOptions.pop();
    },
    // 更新字段信息集合
    updateFillFieldInfoList (type, colArrUpdate) {
      if (type === "0") {
        // 事项
        batchUpdateSxFillField(colArrUpdate).then(response => {
          if (response.code === 200) {
            console.log("更新事项字段信息成功：");
            console.log(colArrUpdate);
          }
        });
      } else if (type === "1") {
        // 一件事
        batchUpdateComboFillField(colArrUpdate).then(response => {
          if (response.code === 200) {
            console.log("更新一件事字段信息成功：");
            console.log(colArrUpdate);
          }
        });
      }
    }
  }
};
</script>
<style>
.object-container {
  padding-top: 0px !important;
}
</style>
