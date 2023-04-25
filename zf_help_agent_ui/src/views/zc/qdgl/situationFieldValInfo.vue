<template>
  <div ref="getHeight" class="situation-opt-rel">
    <div class="situation-opt-rel--left">
      <p class="prec-title">选项名称</p>
      <div class="precondition">
        <div>
          <template v-for="(situ, idx) in situationList">
            <el-collapse :key="idx" v-model="situ.isSelected">
              <el-collapse-item
                :name="situ.name"
                :class="{ precSelected: situ.isSelected }"
              >
                <template slot="title">
                  <span>{{ idx + 1 }}</span>
                  <p>{{ situ.name }}</p>
                </template>
                <!-- 单选 -->
                <el-radio-group
                  v-model="situ.radioValOid"
                  v-if="situ.moreStatus != 1"
                >
                  <el-radio
                    v-for="(opt, index) in situ.sxServiceOptionVals"
                    :label="opt.oid"
                    :key="index"
                    >{{ opt.name }}</el-radio
                  >
                </el-radio-group>
                <!-- 多选 -->
                <el-checkbox-group
                  v-model="situ.valOidList"
                  v-if="situ.moreStatus == 1"
                >
                  <el-checkbox
                    v-for="(opt, index) in situ.sxServiceOptionVals"
                    :label="opt.oid"
                    :key="index"
                    >{{ opt.name }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-collapse-item>
            </el-collapse>
          </template>
        </div>
      </div>
    </div>
    <div class="situation-opt-rel--right">
      <p class="mate-title">字段值信息</p>
      <div class="material">
        <div :style="{ height: 1000 }">
          <el-cascader
            v-model="labelOids"
            :options="labelList"
            :props="{ expandTrigger: 'hover' }"
            :show-all-levels="false"
            @change="querySxFieldList"
            placeholder="所属标签"
            size="small"
            style="width: 100%"
            filterable
          >
          </el-cascader>
          <el-select
            v-model="form.fieldOid"
            placeholder="请选择字段"
            style="width: 100%"
            @change="queryComboFieldValList"
          >
            <el-option
              v-for="m in fieldList"
              :key="m.fieldOid"
              :label="m.fieldName"
              :value="m.fieldOid"
            >
            </el-option>
          </el-select>
          <el-checkbox-group v-model="fieldOids">
            <el-checkbox
              v-for="(m, i) in valList"
              :label="m.text + ':' + m.value"
              :key="i"
              >{{ m.text }}</el-checkbox
            >
          </el-checkbox-group>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {initOptionFieldValInfo, querySxFieldList, querySxFieldValList, getSxFormInfo, saveSxOptionFieldValInfo} from "@/api/zc/qdgl/sxOptionField.js";
export default {
  //定义获取父类传过来值的格式
  props: ["oid", "serviceOid"],
  data () {
    return {
      materialWidth: "",
      form: {},
      situationList: [],
      labelList: [],
      labelOids: [],
      fieldList: [],
      valList: [],
      controlType: "",
      // 选项ids
      optionOids: "",
      // 表单编码
      formCode: "",
      // 字段值ids
      fieldOids: [],
      selectOneFlag: false,
      small: ""
    };
  },
  created () {
    this.handleInit();
  },
  mounted () {
    this.materialWidth = this.$refs.getHeight.offsetWidth - 30 + "px";
  },
  methods: {
    /** 查询情形列表 */
    handleInit () {
      initOptionFieldValInfo(this.serviceOid, this.oid).then(response => {
        this.situationList = response.data.optionTitleList;
        this.situationList.forEach(item => {
          item.valOidList = [];
        });
        this.labelList = response.data.labelList;
        if(this.oid != null && this.oid != "") {
          let oldOptRel = response.data.fieldValRel;
          this.form = oldOptRel;
          this.labelOids = [oldOptRel.fieldTypeOid, oldOptRel.labelOid];
          this.fieldList = response.data.fieldList;
          this.fieldOids = oldOptRel.valInfo.split(",");
          this.situationList.forEach(item => {
            item.sxServiceOptionVals.forEach(opt => {
              let flag = false;
              if (oldOptRel.valOids.indexOf(opt.oid) != -1) {
                flag = true;
                if (item.columnType == 2) {
                  // 多选
                  item.valOidList.push(opt.oid);
                } else if (item.columnType == 1) {
                  // 单选
                  item.radioValOid = opt.oid;
                } else {
                  // 文本
                  item.radioValOid = opt.oid;
                }
              }
              if (flag) {
                item.collapseFlag = item.situationName;
              }
            });
          });
          let str = "init";
          if(null != oldOptRel && null != oldOptRel.valOids) {
            str = "update"
          }
          this.queryComboFieldValList(this.form.fieldOid, str);
        }
      });
    },
    submitForm: function () {
      this.optionOids = "";
      this.situationList.forEach(item => {
        if (item.moreStatus == 1 && item.valOidList.length > 0) {
          this.optionOids += item.valOidList.toString() + ",";
        } else if (item.radioValOid != null && item.radioValOid != "") {
          this.optionOids += item.radioValOid + ",";
        }
      });
      if (this.optionOids == "") {
        this.msgError("选项名称不能为空！");
        return false;
      }
      console.log(this.selectOneFlag);
      console.log(this.fieldOids);
      if (this.fieldOids.length <= 0) {
        this.msgError("字段值信息不能为空！");
        return false;
      } else if (this.selectOneFlag && this.fieldOids.length > 1) {
        this.msgError("该字段值信息只能选择一个！");
        return false;
      }
      this.form.oid = this.oid;
      this.form.serviceOid = this.serviceOid;
      this.form.valInfo = this.fieldOids.toString();
      this.form.valOids = this.optionOids;
      this.form.controlType = this.controlType;
      saveSxOptionFieldValInfo(this.form).then(response => {
        if (response.code === 200) {
          this.msgSuccess("保存成功");
          this.$emit("combo-form", 1);
        }
      });
    },
    cancel: function () {
      this.$emit("combo-form");
    },
    // 查询标签下字段
    querySxFieldList (val) {
      this.form.fieldTypeOid = val[0];
      this.form.labelOid = val[1];
      querySxFieldList(this.serviceOid, val[0], val[1]).then(
        response => {
          this.fieldList = response.data;
          this.valList = [];
          this.$set(this.form, "fieldOid", null);
          this.$set(this.form, "valOids", null);
          this.fieldOids = [];
        }
      );
      this.getFormInfo(this.serviceOid, val[0]);
    },
    // 查询表单编码
    getFormInfo (dirOid, typeOid) {
      getSxFormInfo(dirOid, typeOid).then(response => {
        if(null != response.data) {
          this.formCode = response.data[0].formCode;
        }
      });
    },
    // 查询字段下选项值
    queryComboFieldValList (fieldOid, type) {
      if (type !== 'init') {
        //this.fieldOids = [];
      }
      this.fieldList.forEach(field => {
        if (field.fieldOid == fieldOid) {
          this.toHump(field.fieldCode);
        }
      });
      if (this.formCode == "") {
        getSxFormInfo(this.serviceOid, this.form.typeOid).then(
          response => {
            if(null != response.data) {
              const formInfo = response.data[0];
              this.queryValList(this.small, formInfo.formCode);
            }
          }
        );
      } else {
        this.queryValList(this.small, this.formCode);
      }
    },
    // 查询选项值
    queryValList (fieldCode, formCode) {
      querySxFieldValList(fieldCode, formCode).then(response => {
        if (response.data != null && response.data.options != undefined && response.data.options != null) {
          this.controlType = response.data.type;
          if (this.controlType === 'checkbox-button') {
            this.controlType = 'checkbox';
          }
          this.valList = response.data.options;
          console.log(JSON.stringify(this.valList));
          this.valList.forEach(values => {
            values.value = values.value + ":" + typeof values.value;
            //console.log(typeof values.value);
          })
          if (response.data.type != "checkbox" && response.data.type != "checkbox-button") {
            this.selectOneFlag = true;
          }
        } else {
          this.valList = [];
          this.msgError("该表单字段未设置选项值信息！");
        }
      });
    },
    toHump (name) {
      this.small = "";
      const blank = /\s+/g;
      let str = name.replace(blank, " "); //格式化字符串
      const arr = str.split(" ");
      for (let a = 0; a < arr.length; a++) {
        if (a != 0) {
          this.small += "\n";
        }
        let string = arr[a];
        const strArr = string.split("_");
        for (let b = 0; b < strArr.length; b++) {
          let string1 = strArr[b];
          if (b == 0) {
            this.small += string1.toLowerCase();
          } else {
            string1 =
              string1.substring(0, 1).toUpperCase() +
              string1.substring(1).toLowerCase();
            this.small += string1;
          }
        }
      }
    }
  }
};
</script>
<style lang="scss" scoped>
.situation-opt-rel {
  display: flex;
  .situation-opt-rel--left {
    flex: 2;
    margin-right: 20px;
  }

  .situation-opt-rel--right {
    flex: 1;
  }
}
.prec-title,
.mate-title {
  font-size: 16px;
  color: #3052e8;
  font-weight: bold;
  margin: 0 0 10px;
  padding: 0 35px;
  height: 40px;
  line-height: 40px;
  background-color: #d7dbe4;
}

.precondition {
  .el-collapse {
    border-top-color: transparent;
    border-bottom-color: transparent;

    .el-collapse-item {
      box-shadow: 0 0 5px #ededed;
      margin-bottom: 10px;
      margin-left: 2px;

      &.precSelected {
        & >>> .el-collapse-item__header {
          background-color: #d8f2dd !important;
        }
      }

      & >>> .el-collapse-item__header {
        padding: 0 20px 0 25px;
        border-bottom-color: transparent;

        &.is-active {
          background-color: #f3f6f9;
        }

        span {
          font-size: 18px;
          color: #3052e8;
        }

        p {
          font-size: 14px;
          color: #353535;
          margin-left: 10px;
          width: 89%;
          white-space: nowrap;
          text-overflow: ellipsis;
          overflow: hidden;
        }

        .el-checkbox {
          float: right;
        }

        i {
          color: #3052e8;
          font-size: 18px;
          font-weight: bold;
          transform: rotate(90deg);

          &.is-active {
            transform: rotate(-90deg);
          }
        }
      }

      & >>> .el-collapse-item__wrap {
        border-bottom-color: transparent;
        padding-left: 40px;

        .el-collapse-item__content {
          margin-top: 15px;
        }
      }
    }
  }
}

.material {
  height: 40px;
  line-height: 40px;
  background-color: #d7dbe4;

  & > p {
    font-size: 16px;
    color: #3052e8;
    font-weight: bold;
    margin: 0 0 10px;
    padding: 0 35px;
  }

  & > div {
    background-color: #f3f6f9;
    padding: 20px 30px;
    min-height: 200px;

    .el-checkbox {
      display: block;
      font-size: 14px;
      color: #353535;
    }
  }
}

.dialog-footer {
  position: fixed;
  bottom: 120px;
  z-index: 2;
  width: 1264px;
  background-color: #fff;
  margin: 20px 0;
}
</style>
