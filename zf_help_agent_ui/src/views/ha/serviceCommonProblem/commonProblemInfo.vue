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
      <p class="mate-title">常见问题信息</p>
      <div class="material">
        <div :style="{ height: 1000 }">
          <el-input
            type="textarea"
            v-model.trim="form.question"
            placeholder="请输入常见问题"
            maxlength="1000"
            show-word-limit
          />
          <el-input
            type="textarea"
            v-model.trim="form.answer"
            placeholder="请输入解答"
            maxlength="1000"
            show-word-limit
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {initOptionFieldValInfo, saveSxOptionFieldValInfo} from "@/api/ha/serviceCommonProblem/commonProblem.js";
export default {
  //定义获取父类传过来值的格式
  props: ["id", "serviceOid"],
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
      optionNames: "",
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
      // console.log("------------"+this.id)
      initOptionFieldValInfo(this.serviceOid, this.id).then(response => {
        this.situationList = response.data.optionTitleList;
        this.form = response.data.comPro;
        // console.log(response.data.optionTitleList)
        this.situationList.forEach(item => {
          item.valOidList = [];
        });
        this.labelList = response.data.labelList;
        if(this.id != null && this.id != "") {
          let oldOptRel = response.data.comPro;
          // this.form = oldOptRel;
          // this.labelOids = [oldOptRel.fieldTypeOid, oldOptRel.labelOid];
          // this.fieldList = response.data.fieldList;
          // this.fieldOids = oldOptRel.valInfo.split(",");
          this.situationList.forEach(item => {
            item.sxServiceOptionVals.forEach(opt => {
              let flag = false;
              if (oldOptRel.valueOids.indexOf(opt.oid) != -1) {
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
                console.log("item.radioValOid:"+item.radioValOid)
                console.log("item.valOidList:"+item.valOidList)
              }
              if (flag) {
                item.collapseFlag = item.situationName;
              }
            });
          });
          let str = "init";
          if(null != oldOptRel && null != oldOptRel.valOidList) {
            str = "update"
          }
          // this.queryComboFieldValList(this.form.fieldOid, str);
        }

      });
    },
    submitForm: function () {
      this.optionOids = "";
      this.optionNames = "";
      this.situationList.forEach(item => {
        if (item.moreStatus == 1 && item.valOidList.length > 0) {
          console.log(item);
          this.optionOids += item.valOidList.toString() + ",";
          this.optionNames += item.oid.toString() + ",";
        } else if (item.radioValOid != null && item.radioValOid != "") {
          this.optionOids += item.radioValOid + ",";
          this.optionNames += item.oid + ",";
        }
      });
      if (this.optionOids == "") {
        this.msgError("选项名称不能为空！");
        return false;
      }

      this.form.id = this.id;
      this.form.serviceOid = this.serviceOid;
      // this.form.valInfo = this.fieldOids.toString();
      this.form.titleOids = this.optionNames;
      this.form.valueOids = this.optionOids;
      this.form.controlType = this.controlType;
      // console.log(this.form);
      saveSxOptionFieldValInfo(this.form).then(response => {
        if (response.code === 200) {
          this.msgSuccess("保存成功");
          this.$emit("combo-form", 1);
        }
      });
    },
    cancel: function () {
      this.$emit("combo-form");
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
