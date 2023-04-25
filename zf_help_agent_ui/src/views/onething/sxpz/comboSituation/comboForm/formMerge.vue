<template>
  <div>
    <div class="prompt" ref="getHeight">
      <el-alert type="warning" v-for="(merge, index) in mergeList" :key="index">
        <template slot="title">
          <div><img src="~@/assets/image/search.png" alt="" /></div>
          <span
            >{{ merge[0].labelName }}与{{
              merge[1].labelName
            }}相似度较高，推荐合并</span
          >
          <button @click="fastMerge(merge)">立即合并</button>
        </template>
      </el-alert>
    </div>
    <div class="collapse-box">
      <p @click="operateAll" v-html="foldingLabel"></p>
      <el-scrollbar :style="{ height: collapseHeight }">
        <el-collapse
          v-model="activeNames"
          class="collapse"
          @change="handleChange"
        >
          <el-collapse-item
            v-for="(label, index) in labelList"
            :key="label.labelOid"
            :name="label.labelOid"
            :class="{ 'active-line': selectedLine[index] }"
          >
            <template slot="title">
              <span @click.stop>
                <el-checkbox
                  v-model="selectedLine[index]"
                  @change="selectOneLine($event, index)"
                ></el-checkbox>
              </span>
              <p>{{ label.labelName }}</p>
            </template>
            <div>
              <el-checkbox-group v-model="fieldOidList">
                <el-checkbox
                  v-for="field in label.fieldList"
                  :label="field.fieldOid"
                  :key="field.fieldOid"
                >
                  {{ field.fieldName }}
                </el-checkbox>
              </el-checkbox-group>
            </div>
          </el-collapse-item>
        </el-collapse>
      </el-scrollbar>
    </div>
    <div class="operation">
      <button class="blue" type="button" @click="mergeLabel()">
        <i class="merge"></i>合并标签
      </button>
      <button class="blue" type="button" @click="mergeField()">
        <i class="merge"></i>合并字段
      </button>
      <button class="lightblue" type="button" @click="moveField()">
        <i class="move"></i>移动字段
      </button>
      <button class="lightblue" type="button" @click="changeFactor()">
        <i class="modify"></i>编辑
      </button>
      <button class="gray" type="button" @click="resetLabel()">
        <i class="reset"></i>重置
      </button>
    </div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openLabelInit"
      v-if="openLabelInit"
      width="1100px"
      scrollbar
      append-to-body
    >
      <el-form
        ref="labelForm"
        :model="labelForm"
        :rules="labelRules"
        label-width="0px"
      >
        <el-input v-show="false" v-model="labelForm.labelOid" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>一件事名称：</b></td>
            <td>{{ labelForm.thingName }}</td>
            <td><i class="require">*</i><b>所属分类：</b></td>
            <td>{{ labelForm.typeName }}</td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>标签名称：</b></td>
            <td>
              <el-form-item prop="labelName">
                <el-input
                  v-model.trim="labelForm.labelName"
                  placeholder="请输入事项名称"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>标签编码：</b></td>
            <td>
              <el-form-item prop="labelCode">
                <el-input
                  v-model.trim="labelForm.labelCode"
                  placeholder="请输入设定依据"
                  maxlength="100"
                  show-word-limit
                >
                </el-input>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>是否为动态表格：</b></td>
            <td colspan="3">
              <el-form-item prop="isMovingFlag">
                <el-radio-group v-model="labelForm.isMovingFlag">
                  <el-radio :label="0">否</el-radio>
                  <el-radio :label="1">是</el-radio>
                </el-radio-group>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitLabelForm">确 定</el-button>
        <el-button @click="cancelLabel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 移动 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openMove"
      v-if="openMove"
      width="560px"
      class="move-dialog"
      append-to-body
    >
      <div class="el-table__header-wrapper dialog-table">
        <el-form ref="fieldForm" class="demo-ruleForm">
          <el-form-item prop="labelOid" label="标签名称">
            <el-select
              v-model="moveLabelOid"
              placeholder="标签名称"
              size="small"
              style="width: 240px"
              clearable
            >
              <el-option
                v-for="label in this.labelList"
                :key="label.labelOid"
                :label="label.labelName"
                :value="label.labelOid"
              />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitMove">确 定</el-button>
        <el-button @click="openMove = false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 编辑 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openFieldInit"
      v-if="openFieldInit"
      width="1100px"
      scrollbar
      append-to-body
    >
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>事项标题</h3>-->
        <el-form
          ref="fieldForm"
          :model="fieldForm"
          :rules="fieldRules"
          label-width="0px"
          class="demo-ruleForm"
        >
          <el-input v-show="false" v-model="fieldForm.fieldOid" />
          <table
            cellspacing="0"
            cellpadding="0"
            border="0"
            class="el-table__body"
          >
            <colgroup>
              <col width="18%" />
              <col width="32%" />
              <col width="18%" />
              <col width="32%" />
            </colgroup>
            <tr>
              <td><b>一件事名称：</b></td>
              <td>{{ fieldForm.thingName }}</td>
              <td><i class="require">*</i><b>所属标签：</b></td>
              <td>
                {{ fieldForm.labelName }}
                <!--                  <el-form-item prop="labelOid">-->
                <!--                    <el-select v-model="queryParams.labelOid" placeholder="所属标签" size="small" style="width: 240px" clearable>-->
                <!--                      <el-option v-for="label in labelList" :key="label.labelOid"-->
                <!--                                 :label="label.labelName" :value="label.labelOid" />-->
                <!--                    </el-select>-->
                <!--                  </el-form-item>-->
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>字段名称：</b></td>
              <td>
                <el-form-item prop="fieldName">
                  <el-input
                    v-model.trim="fieldForm.fieldName"
                    placeholder="请输入字段名称"
                    maxlength="100"
                    show-word-limit
                  />
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>字段编码：</b></td>
              <td>
                <el-form-item prop="fieldCode">
                  <el-input
                    v-model.trim="fieldForm.fieldCode"
                    placeholder="请输入字段编码"
                    maxlength="100"
                  ></el-input>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFieldForm">确 定</el-button>
        <el-button @click="cancelField">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openMergeField"
      v-if="openMergeField"
      width="1100px"
      scrollbar
      append-to-body
    >
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>事项标题</h3>-->
        <el-form
          ref="mergeFieldForm"
          :model="mergeFieldForm"
          :rules="mergeFieldRules"
          label-width="0px"
          class="demo-ruleForm"
        >
          <el-input v-show="false" v-model="mergeFieldForm.fieldOid" />
          <table
            cellspacing="0"
            cellpadding="0"
            border="0"
            class="el-table__body"
          >
            <colgroup>
              <col width="18%" />
              <col width="32%" />
              <col width="18%" />
              <col width="32%" />
            </colgroup>
            <tr>
              <td><b>合并字段项：</b></td>
              <td colspan="3">{{ mergeFieldForm.fieldMergeName }}</td>
            </tr>
            <tr>
              <td><b>一件事名称：</b></td>
              <td>{{ mergeFieldForm.thingName }}</td>
              <td><i class="require">*</i><b>所属标签：</b></td>
              <td>
                <el-form-item prop="labelOid">
                  <el-select
                    v-model="mergeFieldForm.labelOid"
                    placeholder="所属标签"
                    size="small"
                  >
                    <el-option
                      v-for="label in labelList"
                      :key="label.labelOid"
                      :label="label.labelName"
                      :value="label.labelOid"
                    />
                  </el-select>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>字段名称：</b></td>
              <td>
                <el-form-item prop="fieldName">
                  <el-input
                    v-model.trim="mergeFieldForm.fieldName"
                    placeholder="请输入字段名称"
                    maxlength="100"
                    show-word-limit
                  />
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>字段编码：</b></td>
              <td>
                <el-form-item prop="fieldCode">
                  <el-input
                    v-model.trim="mergeFieldForm.fieldCode"
                    placeholder="请输入字段编码"
                    maxlength="100"
                  ></el-input>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitMergeFieldForm"
          >确 定</el-button
        >
        <el-button @click="cancelMergeField">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  getLabelList,
  queryCouldMergeLabelList,
  mergeLabel,
  resetLabel,
  moveField,
  getMergeFieldInfo,
  mergeField
} from "@/api/onething/comboForm/formMerge.js";

import {
  init as getLabel,
  save as saveLabel
} from "@/api/onething/comboForm/labelManage";
import {
  get as getField,
  save as saveField
} from "@/api/onething/comboForm/fieldManage";
export default {
  props: ["thingOid", "fieldTypeOid","comboFormOid"],
  name: "formMerge",
  data () {
    return {
      //初始化折叠面板展开项
      activeNames: [],
      //选中一行样式
      selectedLine: [],
      collapseHeight: "",
      labelList: [],
      labelOidList: [],
      fieldOidList: [],
      labelName: "",
      mergeOids: "",
      mergeFieldOids: "",
      openMergeField: false,
      openMove: false,
      moveLabelOid: "",
      openLabelInit: false,
      openFieldInit: false,
      title: "",
      labelForm: {},
      fieldForm: {},
      mergeFieldForm: {},
      labelRules: {
        labelName: [
          {
            required: true,
            message: "标签名称不能为空",
            trigger: "blur"
          }
        ],
        labelCode: [
          {
            required: true,
            message: "标签编码不能为空",
            trigger: "blur"
          }
        ],
        isMovingFlag: [
          { required: true, message: "请选择是否为动态表格", trigger: "blur" }
        ]
      },
      fieldRules: {
        fieldName: [
          {
            required: true,
            message: "字段名称不能为空",
            trigger: "blur"
          }
        ],
        fieldCode: [
          {
            required: true,
            message: "字段编码不能为空",
            trigger: "blur"
          }
        ]
      },
      mergeFieldRules: {
        fieldName: [
          {
            required: true,
            message: "字段名称不能为空",
            trigger: "blur"
          }
        ],
        fieldCode: [
          {
            required: true,
            message: "字段编码不能为空",
            trigger: "blur"
          }
        ],
        labelOid: [
          {
            required: true,
            message: "所属标签不能为空",
            trigger: "blur"
          }
        ]
      },
      mergeList: [],
      labelListAll: [],
      foldingLabel: "展开全部"
    };
  },
  mounted () {
    this.collapseHeight = 400 - this.$refs.getHeight.offsetHeight + "px";
  },
  created () {
    this.getList();
  },
  watch: {
    /*openMerge: {
        deep: true,*/
    handler: function (newVal) {
      if (newVal) {
        this.getList();
        this.reset();
      }
    }
    // },
  },
  methods: {
    handleChange (val) {
      if (val.length === 0) {
        this.foldingLabel = "展开全部";
      } else if (this.labelList.length === val.length) {
        this.foldingLabel = "收起全部";
      }
    },
    /** 选中一行 */
    selectOneLine (state, index) {
      this.selectedLine[index] = !!state;
      this.$forceUpdate();
      this.checkLabelOid(state, index);
    },
    checkLabelOid (state, index) {
      let label = this.labelList[index];
      let index1 = this.labelOidList.indexOf(label.labelOid);
      if (!!state && index1 === -1) {
        this.labelOidList.push(label.labelOid);
      } else if (!state) {
        this.labelOidList.splice(index1, 1);
      }
    },
    /** 操作全部 */
    operateAll () {
      if (this.foldingLabel === "展开全部") {
        this.activeNames = this.labelListAll;
        this.foldingLabel = "收起全部";
      } else {
        this.activeNames = [];
        this.foldingLabel = "展开全部";
      }
    },
    getList () {
      getLabelList(this.thingOid, this.fieldTypeOid,this.comboFormOid).then(response => {
        response.data.forEach(item => {
          this.labelListAll.push(item.labelOid);
          this.labelListAll = [...new Set([...this.labelListAll])];
        });
        this.labelList = response.data;
      });
      this.getMergeList();
    },
    getMergeList () {
      queryCouldMergeLabelList(this.thingOid, this.fieldTypeOid,this.comboFormOid).then(
        response => {
          this.mergeList = response.data;
        }
      );
    },
    /**推荐快速合并**/
    fastMerge (merge) {
      this.mergeOids = merge[0].labelOid + "," + merge[1].labelOid;
      this.labelName = merge[0].labelName;
      this.mergeCommit();
    },
    /**合并标签**/
    mergeLabel () {
      this.mergeOids = "";
      if (this.labelOidList.length < 2) {
        this.$message({
          type: "error",
          message: "请选中至少两项进行合并"
        });
        return false;
      }
      this.selectedLine.forEach((ele, idx) => {
        if (ele) {
          this.labelName = this.labelList[idx].labelName;
        }
      });
      this.labelOidList.forEach(value => {
        if (this.mergeOids === "") {
          this.mergeOids = value;
        } else {
          this.mergeOids = this.mergeOids + "," + value;
        }
      });
      this.mergeCommit();
    },
    /**合并提交**/
    mergeCommit () {
      // console.log(this.mergeOids, 'mergeOids');
      let that = this;
      this.$prompt("请输入合并后标签名称", "合并标签", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputValue: this.labelName,
        inputPattern: /\S/,
        inputErrorMessage: "标签名称不能为空"
      })
        .then(({ value }) => {
          mergeLabel(that.mergeOids, value, that.thingOid, that.fieldTypeOid,that.comboFormOid).then(response => {
            if (response.code === 200 && !response.data) {
              this.$message({
                type: "success",
                message: "合并标签成功"
              });
              this.selectedLine.forEach((ele, idx) => {
                if (ele) {
                  this.selectedLine[idx] = false;
                }
              });
              //防止合并过的标签和字段被再次编辑的时候统计到
              this.reset();
              this.getList();
            } else {
              this.$message.error(response.data);
            }
          });
        })
        .catch(() => {
          // this.$message({
          //   type: 'info',
          //   message: '取消操作'
          // });
        });
    },

    mergeField () {
      if (this.fieldOidList.length === 0) {
        this.$message({
          type: "error",
          message: "请选中至少两个字段进行合并"
        });
        return false;
      }
      this.mergeFieldOids = "";
      this.fieldOidList.forEach(value => {
        if (this.mergeFieldOids === "") {
          this.mergeFieldOids = value;
        } else {
          this.mergeFieldOids = this.mergeFieldOids + "," + value;
        }
      });
      getMergeFieldInfo(this.mergeFieldOids).then(response => {
        this.mergeFieldForm = response.data;
        this.mergeFieldForm.fieldOid = this.mergeFieldOids;
        this.mergeFieldForm.comboFormOid=this.comboFormOid;
        this.openMergeField = true;
        this.title = "字段合并";
      });
    },
    submitMergeFieldForm () {
      this.$refs["mergeFieldForm"].validate(valid => {
        if (valid) {
          mergeField(this.mergeFieldForm).then(response => {
            if (response.code === 200 && !response.data) {
              this.msgSuccess("保存成功");
              this.openMergeField = false;
              this.reset();
              this.getList();
            } else if (response.data) {
              this.$message.error(response.data);
            }
          });
        } else {
          return false;
        }
      });
    },

    resetLabel () {
      let thingOid = this.thingOid;
      let fieldTypeOid = this.fieldTypeOid;
      let comboFormOid=this.comboFormOid;
      this.$confirm("是否确认重置?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return resetLabel(thingOid, fieldTypeOid,comboFormOid);
        })
        .then(() => {
          this.msgSuccess("重置成功");
          this.reset();
          this.getList();
          this.selectedLine.forEach((ele, idx) => {
            if (ele) {
              this.selectedLine[idx] = false;
            }
          });
        })
        .catch(function () { });
    },

    changeFactor () {
      console.log("lable:" + JSON.stringify(this.labelOidList))
      console.log("fieldOidList:" + JSON.stringify(this.fieldOidList))
      let num = this.labelOidList.length + this.fieldOidList.length;
      if (num === 0) {
        this.$message({
          type: "error",
          message: "请选中要修改的要素"
        });
        return false;
      }
      if (num > 1) {
        this.$message({
          type: "error",
          message: "一次只能编辑一个要素"
        });
        return false;
      }
      if (this.labelOidList.length > 0) {
        this.labelForm = {};
        const oid = this.labelOidList[0];
        getLabel(oid).then(response => {
          this.labelForm = response.data;
          this.openLabelInit = true;
          this.title = "标签信息修改";
        });
      } else {
        this.fieldForm = {};
        const oid = this.fieldOidList[0];
        getField(oid).then(response => {
          this.fieldForm = response.data;
          this.openFieldInit = true;
          this.title = "字段信息修改";
        });
      }
    },

    submitLabelForm () {
      this.$refs["labelForm"].validate(valid => {
        if (valid) {
          saveLabel(this.labelForm).then(response => {
            if (response.code === 200 && !response.data) {
              this.msgSuccess("保存成功");
              this.openLabelInit = false;
              this.reset();
              this.getList();
            } else if (response.code === 200 && response.data) {
              this.$message.error(response.data);
            }
          });
        } else {
          return false;
        }
      });
    },
    submitFieldForm () {
      this.$refs["fieldForm"].validate(valid => {
        if (valid) {
          saveField(this.fieldForm).then(response => {
            if (response.code === 200 && !response.data) {
              this.msgSuccess("保存成功");
              this.openFieldInit = false;
              this.reset();
              this.getList();
            } else if (response.code === 200 && response.data) {
              this.$message.error(response.data);
            }
          });
        } else {
          return false;
        }
      });
    },
    cancelLabel () {
      this.openLabelInit = false;
    },
    cancelField () {
      this.openFieldInit = false;
    },
    cancelMergeField () {
      this.openMergeField = false;
    },
    moveField () {
      if (this.fieldOidList.length === 0) {
        this.$message({
          type: "error",
          message: "请选中要移动的字段"
        });
        return false;
      }
      this.openMove = true;
    },

    submitMove () {
      if (this.moveLabelOid === "") {
        this.$message({
          type: "error",
          message: "请选中标签名称"
        });
        return false;
      }
      let oids = "";
      this.fieldOidList.forEach(value => {
        if (oids === "") {
          oids = value;
        }
        oids = oids + "," + value;
      });
      moveField(this.moveLabelOid, oids).then(response => {
        if (response.code === 200) {
          this.$message({
            type: "success",
            message: "移动字段成功"
          });
          this.reset();
          this.openMove = false;
          this.getList();
        }
      });
    },

    reset () {
      this.moveLabelOid = "";
      this.fieldOidList = [];
      this.labelOidList = [];
      this.selectedLine = this.labelList.map(item => false);
      this.activeNames = [];
      this.foldingLabel = "展开全部";
    }
  }
};
</script>
<style lang="scss" scoped>
.prompt {
  .el-alert {
    height: 37px;
    line-height: 37px;
    color: #b07a03;
    font-size: 14px;
    padding: 0;
    background-color: #f2ebd5;
    border: 1px dashed #f4cca6;
    margin-bottom: 12px;

    & >>> .el-alert__content {
      padding: 0;

      .el-icon-close {
        width: 23px;
        height: 23px;
        margin-top: -6px;
        background: url(~@/assets/image/close-alert.png) no-repeat;
        background-size: 100% 100%;

        &::before {
          display: none;
        }
      }
    }

    div {
      width: 44px;
      display: inline-block;
      height: 37px;
      background-color: #f3e3ba;
      vertical-align: middle;

      img {
        margin-top: 8px;
        margin-left: 12px;
      }
    }

    button {
      border: 0;
      width: 75px;
      height: 24px;
      border-radius: 3px;
      background-color: #f79c43;
      color: #fff;
      margin-left: 10px;
      cursor: pointer;
    }
  }
}

.collapse-box {
  background-color: #ffffff;
  border-radius: 3px;
  overflow: hidden;

  & > p {
    color: #0072ff;
    font-size: 14px;
    height: 40px;
    line-height: 40px;
    padding-right: 65px;
    margin: 0;
    text-align: right;
    cursor: pointer;
    background-color: #e6e8ee;
  }

  .collapse {
    border: 0;
    margin: 27px 34px;

    .el-collapse-item {
      &:not(:last-child) {
        margin-bottom: 25px;
      }

      &.active-line {
        & >>> .el-collapse-item__header {
          background-color: #0072ff;
          border-color: #0072ff;

          & > span {
            background-color: #0072ff;
          }

          p {
            color: #fff;
          }

          .el-collapse-item__arrow {
            background: url(~@/assets/image/fold-active-text.png) no-repeat;
            background-size: 100% 100%;
          }
        }

        & >>> .el-collapse-item__wrap {
          background-color: #dbebff;
        }

        & >>> .el-collapse-item__arrow.is-active {
          background: url(~@/assets/image/unfold-active-text.png) no-repeat;
          background-size: 100% 100%;
        }
      }
    }

    & >>> .el-collapse-item__header {
      height: 37px;
      line-height: 37px;
      border: 1px solid #c8cdd3;
      box-sizing: content-box;
      border-radius: 5px;
      overflow: hidden;
      padding-right: 20px;
      background-color: #f5f7f9;

      & > span {
        width: 44px;
        height: 37px;
        text-align: center;
        background-color: #eaeef2;
      }

      p {
        color: #2a344c;
        font-size: 14px;
        margin-left: 10px;
      }

      .el-checkbox {
        &.is-checked {
          .el-checkbox__inner {
            background: url(~@/assets/image/checkbox-icon-active.png) no-repeat;
            background-size: 100% 100%;
          }
        }

        .el-checkbox__inner {
          width: 16px;
          height: 16px;
          border-radius: 50%;
          background: url(~@/assets/image/checkbox-icon.png) no-repeat;
          background-size: 100% 100%;
          border: 0;

          &::after {
            display: none;
          }
        }
      }

      .el-collapse-item__arrow {
        width: 29px;
        height: 14px;
        transform: none;
        background: url(~@/assets/image/fold-text.png) no-repeat;
        background-size: 100% 100%;

        &::before {
          display: none;
        }

        &.is-active {
          background: url(~@/assets/image/unfold-text.png) no-repeat;
          background-size: 100% 100%;
        }
      }
    }

    & >>> .el-collapse-item__wrap {
      border-bottom: 0;
      margin-left: 44px;
      background-color: #f5f7f9;

      .el-collapse-item__content {
        padding: 15px 20px;

        .el-checkbox {
          color: #2a344c;
          font-size: 14px;

          .el-checkbox__inner {
            width: 16px;
            height: 16px;
            border-radius: 3px;
            background-color: #edf0f4;

            &::after {
              border-color: #0072ff;
              width: 4px;
              height: 8px;
            }
          }

          &.is-checked {
            .el-checkbox__inner {
              border-color: #0072ff;
            }

            .el-checkbox__label {
              color: #0072ff;
            }
          }

          @media screen and (max-width: 1920px) {
            .el-checkbox__label {
              max-width: 1083px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              vertical-align: middle;
            }
          }

          @media screen and (max-width: 1600px) {
            .el-checkbox__label {
              max-width: 857px;
            }
          }

          @media screen and (max-width: 1500px) {
            .el-checkbox__label {
              max-width: 749px;
            }
          }

          @media screen and (max-width: 1400px) {
            .el-checkbox__label {
              max-width: 691px;
            }
          }
        }
      }
    }
  }
}

.operation {
  width: 577px;
  margin: 15px auto 0;

  button {
    padding: 8px 10px;
    border: 0;
    color: #fff;
    font-size: 16px;
    cursor: pointer;
    border-radius: 5px;
    margin: 0 8px;

    i {
      width: 17px;
      height: 17px;
      display: inline-block;
      vertical-align: middle;
      margin-right: 8px;

      &.merge {
        background: url(~@/assets/image/merge-icon.png) no-repeat;
        background-size: 100% 100%;
      }

      &.move {
        background: url(~@/assets/image/zoom-icon.png) no-repeat;
        background-size: 100% 100%;
      }

      &.add {
        background: url(~@/assets/image/add-icon.png) no-repeat;
        background-size: 100% 100%;
      }

      &.modify {
        background: url(~@/assets/image/modify-icon.png) no-repeat;
        background-size: 100% 100%;
      }

      &.delete {
        background: url(~@/assets/image/delete-icon.png) no-repeat;
        background-size: 100% 100%;
      }

      &.reset {
        background: url(~@/assets/image/reset-icon.png) no-repeat;
        background-size: 100% 100%;
      }
    }

    &.blue {
      background-color: #255cfc;
    }

    &.lightblue {
      background-color: #2e7dff;
    }

    &.orange {
      background-color: #ff5a2e;
    }

    &.gray {
      background-color: #798998;
    }
  }
}

.move-dialog .el-select {
  width: 450px !important;
}

.move-dialog {
  margin: 0 !important;
  position: absolute;
  top: 30%;
  margin-top: -100px;
}
</style>
