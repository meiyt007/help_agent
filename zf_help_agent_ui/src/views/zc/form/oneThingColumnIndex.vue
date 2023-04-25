<template>
  <div class="app-container-edit">
    <el-form
      ref="form"
      :model="form"
      :rules="columnList.rules"
      class="formEdit"
      label-width="0px"
    >
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5"> </el-col>
      </el-row>
      <el-table
        v-if="1 == soucrce"
        v-loading="loading"
        :data="form.columnList"
        border
      >
        <el-table-column label="序号" width="65" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="对象属性" align="center">
          <template slot-scope="scope">
            <el-form-item
              :prop="'columnList.' + scope.$index + '.objectProperty'"
              :rules="columnList.rules.objectProperty"
            >
              <el-input
                placeholder="请输入对象属性"
                v-model.trim="scope.row.objectProperty"
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center">
          <template slot-scope="scope">
            <el-form-item prop="demo">
              <el-input
                placeholder="请输入备注"
                v-model.trim="scope.row.demo"
                :disabled="true"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>
          </template>
        </el-table-column>
      </el-table>

      <el-table
        v-if="0 == soucrce"
        v-loading="loading"
        :data="form.columnList"
        border
      >
        <el-table-column label="序号" width="65" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="字段编码" min-width="300" align="center">
          <template slot-scope="scope">
            <el-form-item prop="fieldCode">
              <el-input
                placeholder="请输入字段名"
                v-model.trim="scope.row.fieldCode"
                :disabled="true"
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="字段类型" width="200" align="center">
          <template slot-scope="scope">
            <el-form-item prop="columnType">
              <el-select v-model="scope.row.columnType" :disabled="true">
                <el-option
                  v-for="data in columnTypeMap"
                  :key="data"
                  :label="data"
                  :value="data"
                ></el-option>
              </el-select>
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="最大长度" width="200" align="center">
          <template slot-scope="scope">
            <el-form-item prop="maxLenth">
              <el-input
                placeholder="请输入最大长度"
                :disabled="true"
                v-model.trim="scope.row.maxLenth"
                maxlength="4"
                show-word-limit
              />
            </el-form-item>
          </template>
        </el-table-column>
        <!--<el-table-column label="是否必填" align="center"  width="100" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.notNull"
              :active-value="1"
              :inactive-value="0"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="对象属性" align="center" width="130"  prop="objectProperty" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="数据存储类型"  width="150"   align="center" prop="dataType">
          <template slot-scope="scope">
            <el-form-item prop="dataType">
              <el-select v-model="scope.row.dataType" >
                <el-option
                  v-for="data in dataTypeMap"
                  :key="data.id"
                  :label="data.value"
                  :value="data.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </template>
        </el-table-column>-->
        <el-table-column
          label="字段名称"
          min-width="300"
          align="center"
          prop="demo"
        >
          <template slot-scope="scope">
            <el-form-item prop="fieldName">
              <el-input
                placeholder="请输入备注"
                :disabled="true"
                v-model.trim="scope.row.fieldName"
                maxlength="20"
                show-word-limit
              />
            </el-form-item>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
  </div>
</template>

<script>
  import {
    datasourceColumn,
    queryFormColumnListByObjectOid
  } from "@/api/form/manager";
  import { getFieldList } from "@/api/zc/sxService/serviceFormConfig/formFillField";
  import { queryComboFieldList } from "@/api/onething/comboForm/formManage";
  import { validateLegalStrNoNumber } from "@/utils/validate";
  export default {
    name: "Column",
    props: ["params", "moduleOid", "openCreateTable", "type"],
    data() {
      let validateRepeat = (rule, value, callback) => {
        if (value !== "") {
          let columnList = this.form.columnList.filter(
            d => d.objectProperty === value
          );
          if (columnList.length > 1) {
            callback(new Error("当前对象属性已存在，请重新输入"));
          } else {
            callback();
          }
        } else {
          callback();
        }
      };
      return {
        //授权id
        // 遮罩层
        loading: false,
        // 总条数
        total: 0,
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        soucrce: 0,
        // 启用状态
        ableMap: {
          "1": "启用",
          "0": "禁用"
        },
        // 数据存储类型(0 字符串 1数组 2对象 默认字符串，3文件类)
        dataTypeMap: [
          {
            id: 0,
            value: "字符串"
          },
          {
            id: 1,
            value: "数组"
          },
          {
            id: 2,
            value: "对象"
          },
          {
            id: 3,
            value: "文件类"
          }
        ],
        // 字段类型
        columnTypeMap: ["VARCHAR", "INTEGER", "BIGINT", "DOUBLE"],
        // 表单参数
        form: {
          columnList: []
        },
        dataSourceOptions: [],
        openJson: false,
        jsonData: "",
        // 表单校验
        columnList: {
          rules: {
            objectProperty: [
              {
                required: true,
                message: "对象属性不能为空",
                trigger: "blur"
              },
              {
                required: true,
                validator: validateLegalStrNoNumber,
                trigger: "blur"
              },
              {
                required: true,
                validator: validateRepeat,
                trigger: "blur"
              }
            ]
          }
        }
      };
    },
    created() {
      this.form.columnList = [];
      //修改为始终查询数据库
      this.querySxColumnByType();
    },
    methods: {
      // 启用状态
      statusFormat(row) {
        return this.selectMapLabel(this.ableMap, row.isAble);
      },
      //获取表单的内容
      getFormData() {
        return new Promise((resolve, reject) => {
          //你的逻辑代码
          this.$refs["form"].validate(valid => {
            if (valid) {
              resolve(this.form.columnList);
            }
          });
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        let that = this;
        this.$confirm("是否确认删除?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(function() {
            if (row.objectProperty) {
              for (let i = 0; i < that.form.columnList.length; i++) {
                if (
                  row.objectProperty == that.form.columnList[i].objectProperty
                ) {
                  that.form.columnList.splice(i, 1);
                  break;
                }
              }
            } else {
              that.form.columnList.splice(row.index, 1);
            }

            /*let delIndex = that.form.columnList.findIndex(d => d.index == row.index)
            that.form.columnList.splice(delIndex, 1)*/
          })
          .catch(function() {});
      },
      //增加框
      addColumn() {
        let column = {
          index: this.form.columnList.length,
          objectProperty: ""
        };
        if (!this.form.columnList) {
          this.form.columnList = [];
        }
        this.form.columnList.push(column);
      },
      //提交按钮
      submitForm() {
        if ("" == this.jsonData) {
          this.msgError("请输入JSON数据");
          return;
        }
        if (this.isJSON(this.jsonData)) {
          const jsonDataOj = eval("(" + this.jsonData + ")");
          //遍历key和value
          const keys = Object.keys(jsonDataOj);
          for (let i = 0; i < keys.length; i++) {
            const key = keys[i];
            const value = jsonDataOj[key];
            let columnData = {
              objectProperty: key,
              demo: value
            };
            this.form.columnList.push(columnData);
          }
          this.openJson = false;
        } else {
          this.msgError("请输入正确的JSON数据");
          return;
        }
      },
      isJSON(str) {
        if (typeof str == "string") {
          try {
            var obj = JSON.parse(str);
            if (typeof obj == "object" && obj) {
              return true;
            } else {
              return false;
            }
          } catch (e) {
            return false;
          }
        }
      },
      //获取数据库的列表
      getDatasourceColumn() {
        if (undefined == this.params.objectForm || "" == this.params.objectForm) {
          this.msgError("请输入数据库表名");
          return;
        }
        datasourceColumn(this.params).then(response => {
          if (response.code === 200) {
            this.msgSuccess("操作成功！");
            this.form.columnList = response.data;
          }
        });
      },
      //删除子项模块
      deleteRow(index) {
        this.form.columnList.splice(index, 1);
      },
      //获取事项分类下的列表
      querySxColumnByType() {
        if ("0" === this.type) {
          getFieldList(this.params.contentOid, this.params.fieldTypeOid).then(
            response => {
              if (response.code === 200) {
                response.data.forEach((col, idx) => {
                  // 如果字段类型、字段长度为空，默认是varchar(100)
                  if (col.columnType == null || col.columnType.length <= 0) {
                    col.columnType = "varchar";
                  }
                  if (col.maxLenth == null || col.maxLenth.length <= 0) {
                    col.maxLenth = "100";
                  }
                });
                this.$set(this.form, "columnList", response.data);
              }
            }
          );
        } else {
          queryComboFieldList(
            this.params.contentOid,
            this.params.fieldTypeOid
          ).then(response => {
            if (response.code === 200) {
              this.form.columnList = response.data;
              response.data.forEach((col, idx) => {
                // 如果字段类型、字段长度为空，默认是varchar(100)
                if (col.columnType == null || col.columnType.length <= 0) {
                  col.columnType = "varchar";
                }
                if (col.maxLenth == null || col.maxLenth.length <= 0) {
                  col.maxLenth = "100";
                }
              });
              this.$set(this.form, "columnList", response.data);
            }
          });
        }
      }
    }
  };
</script>
<style lang="scss" scoped>
  .app-container-edit {
    padding: 0px;
    padding-top: 10px;
  }

  .formEdit {
    padding: 0;
  }

  .formEdit table tr td:nth-of-type(2n) {
    color: #606266;
    text-align: center !important;
  }

  .json-data-dialog .el-textarea__inner {
    height: 200px !important;
  }

  .el-table {
    .el-form-item {
      margin-top: 8px !important;
      margin-bottom: 8px !important;
    }

    & >>> .el-table__row {
      td {
        border-right: 1px solid #ebeef5 !important;
      }
    }
  }
</style>
