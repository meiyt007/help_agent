<template>
  <div ref="getHeight" class="situation-opt-rel">
    <div class="situation-opt-rel--left">
      <p class="prec-title">选项名称</p>

      <div class="precondition">
        <div>
          <template v-for="(situ, idx) in situationList">
            <el-collapse v-model="situ.collapseFlag">
              <el-collapse-item
                :name="situ.situationName"
                :class="{ precSelected: situ.checkSituation }"
              >
                <template slot="title">
                  <span>{{ idx + 1 }}</span>
                  <p>{{ situ.situationName }}</p>
                </template>
                <el-radio-group
                  v-model="situ.radioValOid"
                  v-if="situ.columnType != 2"
                >
                  <el-radio
                    v-for="(opt, index) in situ.optionValsList"
                    :label="opt.valOid"
                    :key="opt.valOid"
                    >{{ opt.name }}</el-radio
                  >
                </el-radio-group>
                <el-checkbox-group
                  v-model="situ.valOidList"
                  v-if="situ.columnType == 2"
                >
                  <el-checkbox
                    v-for="(opt, index) in situ.optionValsList"
                    :label="opt.valOid"
                    :key="opt.valOid"
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
      <p class="mate-title">字段信息</p>
      <div class="material">
        <div :style="{ height: 1000 }">
          <el-cascader
            v-model="labelOids"
            :options="labelList"
            :props="{ expandTrigger: 'hover' }"
            :show-all-levels="false"
            @change="queryComboFieldList"
            placeholder="所属标签"
            size="small"
            style="width: 300px"
            clearable
            filterable
          >
          </el-cascader>
          <el-checkbox-group v-model="fieldOids">
            <el-checkbox
              v-for="(m, i) in fieldList"
              :label="m.fieldOid"
              :key="m.fieldOid"
              >{{ m.fieldName }}</el-checkbox
            >
          </el-checkbox-group>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  initFieldRel,
  saveOptionFieldRel,
  queryComboFieldList
} from "@/api/onething/comboSituation/optionField.js";
export default {
  //定义获取父类传过来值的格式
  props: ["relOid", "comboDirectoryOid"],
  data() {
    return {
      materialWidth: "",
      form: {},
      situationList: [],
      labelList: [],
      labelOids: [],
      fieldList: [],
      fieldOids: [],
      // 选项ids
      optionOids: ""
    };
  },
  created() {
    this.handleInit();
  },
  mounted() {
    this.materialWidth = this.$refs.getHeight.offsetWidth - 30 + "px";
  },
  methods: {
    /** 查询情形列表 */
    handleInit() {
      initFieldRel(this.comboDirectoryOid, this.relOid).then(response => {
        this.situationList = response.data.situationList;
        this.situationList.forEach(item => {
          item.valOidList = [];
        });
        this.labelList = response.data.labelList;
        if (this.relOid != "") {
          let oldOptRel = response.data.fieldRel;
          this.form = oldOptRel;
          this.labelOids = [oldOptRel.typeOid, oldOptRel.labelOid];
          this.fieldList = response.data.fieldList;
          this.fieldOids = oldOptRel.fieldOids.split(",");
          this.situationList.forEach(item => {
            item.optionValsList.forEach(opt => {
              let flag = false;
              if (oldOptRel.optionOids.indexOf(opt.valOid) != -1) {
                flag = true;
                if (item.columnType == 2) {
                  // 多选
                  item.valOidList.push(opt.valOid);
                } else if (item.columnType != 2) {
                  // 单选
                  item.radioValOid = opt.valOid;
                }
              }
              if (flag) {
                item.collapseFlag = item.situationName;
              }
            });
          });
        }
      });
    },
    submitForm: function() {
      this.optionOids = "";
      this.situationList.forEach(item => {
        if (item.columnType == 2 && item.valOidList.length > 0) {
          this.optionOids += item.valOidList.toString() + ",";
        } else if (
          item.columnType != 2 &&
          item.radioValOid != null &&
          item.radioValOid != ""
        ) {
          this.optionOids += item.radioValOid + ",";
        }
      });
      if (this.optionOids == "") {
        this.msgError("选项名称不能为空！");
        return false;
      }
      if (this.fieldOids.length == 0) {
        this.msgError("字段信息不能为空！");
        return false;
      }
      this.form.oid = this.relOid;
      this.form.comboDirectoryOid = this.comboDirectoryOid;
      this.form.fieldOids = this.fieldOids.toString();
      this.form.optionOids = this.optionOids;
      saveOptionFieldRel(this.form).then(response => {
        if (response.code === 200) {
          this.msgSuccess("保存成功");
          this.$emit("combo-form", 1);
        }
      });
    },
    cancel: function() {
      this.$emit("combo-form");
    },
    queryComboFieldList(val) {
      this.form.typeOid = val[0];
      this.form.labelOid = val[1];
      queryComboFieldList(this.comboDirectoryOid, val[0], val[1]).then(
        response => {
          this.fieldList = response.data;
          this.fieldOids = [];
        }
      );
    }
  }
};
</script>
<style lang="scss" scoped>
// .app-container {
//   padding: 0 20px;
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
  bottom: 70px;
  z-index: 2;
  width: 1264px;
  background-color: #fff;
  margin: 20px 0;
}
</style>
