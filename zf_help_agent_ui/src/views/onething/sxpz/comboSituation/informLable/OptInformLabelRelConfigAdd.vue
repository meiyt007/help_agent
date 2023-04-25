<template>
  <div class="situation-opt-rel">
    <div class="situation-opt-rel--left">
      <p class="prec-title">前置选项</p>
      <div class="precondition">
        <template v-for="(situ, idx) in situationList">
          <el-collapse v-model="situ.collapseFlag">
            <el-collapse-item
              :name="situ.situationName"
              :class="{ precSelected: situ.checkSituation }"
            >
              <template slot="title">
                <span>{{ idx + 1 }}</span>
                <p :title="situ.situationName">
                  {{ situ.situationName }}
                </p>
              </template>
              <el-radio-group
                v-model="situ.radioValOid"
                v-if="situ.columnType != 2"
              >
                <el-radio
                  :title="opt.name"
                  v-for="(opt, index) in situ.optionValsList"
                  :label="opt.valOid"
                  :key="opt.valOid"
                >
                  {{ opt.name }}
                </el-radio>
              </el-radio-group>
              <el-checkbox-group
                v-model="situ.valOidList"
                v-if="situ.columnType == 2"
              >
                <el-checkbox
                  :title="opt.name"
                  v-for="(opt, index) in situ.optionValsList"
                  :label="opt.valOid"
                  :key="opt.valOid"
                >
                  {{ opt.name }}
                </el-checkbox>
              </el-checkbox-group>
            </el-collapse-item>
          </el-collapse>
        </template>
      </div>
    </div>

    <div class="situation-opt-rel--right">
      <p class="mate-title">标签</p>
      <div class="material">
        <el-checkbox-group v-model="labelOidArr">
          <el-checkbox
            v-for="(m, i) in informLabelList"
            :label="m.oid"
            :title="m.labelCodeNames"
            :key="m.oid"
          >
            {{ m.labelCodeNames }}
          </el-checkbox>
        </el-checkbox-group>
      </div>
    </div>
  </div>
</template>

<script>
import {
  init,
  save
} from "@/api/onething/sxpz/inform/comboOptInformLabelRelConfig.js";
import {
  listComboInformLabelByComboDirectoryOid
} from "@/api/onething/sxpz/inform/comboInformLabel.js";

export default {
  //定义获取父类传过来值的格式
  props: ["optInformLabelRelOid", "comboDirectoryOid", "optInformLabelRelId"],
  name: "OptInformLabelRelConfigAdd",
  data () {
    return {
      form: {},
      situationList: [],
      // 事项ids
      informLabelList: [],
      labelOidArr: [],
      // 选项ids
      optionOids: ''
    };
  },
  created () {
    this.handleInit();
  },
  methods: {
    /** 查询情形列表 */
    handleInit () {
      init(this.comboDirectoryOid, this.optInformLabelRelOid).then(response => {
        this.situationList = response.data.situationList;
        this.labelOidArr = response.data.labelOidArr;
        if (this.optInformLabelRelOid != undefined) {
          this.situationList.forEach(item => {
            // 多选
            if (item.columnType == 2 && item.valOidList != null && item.valOidList.length > 0) {
              item.collapseFlag = item.situationName;
              // 单选
            } else if (item.columnType != 2 && item.radioValOid != null && item.radioValOid.length > 0) { // 单选
              item.collapseFlag = item.situationName;
            }
          })
        } else {
          this.situationList.forEach(item => {
            item.valOidList = [];
            item.valOids = "";
          });
        }
        console.log(this.situationList);
      });
      listComboInformLabelByComboDirectoryOid(this.comboDirectoryOid).then(response => {
        this.informLabelList = response.data;
      });
    },
    submitForm: function () {
      this.optionOids = '';
      this.situationList.forEach(item => {
        if (item.columnType == 2 && item.valOidList.length > 0) {
          this.optionOids += item.valOidList.toString() + ',';
        } else if (item.columnType != 2 && item.radioValOid != null && item.radioValOid != '') {
          this.optionOids += item.radioValOid + ',';
        }
      });

      this.labelOids = '';
      this.labelOidArr.forEach(labelOid => {
        this.labelOids += labelOid + ",";
      });
      if (this.optionOids == '') {
        this.msgError('前置选项不能为空！');
        return false;
      }
      if (this.labelOids.length <= 0) {
        this.msgError('标签不能为空！');
        return false;
      }
      this.form.comboDirectoryOid = this.comboDirectoryOid;
      this.form.valOids = this.optionOids;
      this.form.labelOids = this.labelOids;
      this.form.oid = this.optInformLabelRelOid;
      this.form.id = this.optInformLabelRelId;

      save(this.form).then(response => {
        if (response.code === 200) {
          this.msgSuccess("保存成功");
          this.$emit('close-label-rel-config-add');
        }
      });
    },
  }
}

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
  position: fixed;
  z-index: 2;
}

.prec-title {
  width: 37%;
}

.mate-title {
  width: 18%;
}

.precondition {
  margin-top: 50px;
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

          @media screen and (max-width: 1920px) {
            .el-radio__label,
            .el-checkbox__label {
              max-width: 710px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              display: inline-block;
              vertical-align: middle;
            }
          }

          @media screen and (max-width: 1700px) {
            .el-radio__label,
            .el-checkbox__label {
              max-width: 580px;
            }
          }

          @media screen and (max-width: 1500px) {
            .el-radio__label,
            .el-checkbox__label {
              max-width: 420px;
            }
          }

          @media screen and (max-width: 1400px) {
            .el-radio__label,
            .el-checkbox__label {
              max-width: 470px;
            }
          }
        }
      }
    }
  }
}

.material {
  line-height: 40px;

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
    margin-top: 50px;

    .el-checkbox {
      // display: block;
      font-size: 14px;
      color: #353535;
      display: flex;
      margin-bottom: 10px;

      & >>> .el-checkbox__label {
        width: 100%;
        // overflow: hidden;
        // vertical-align: middle;
        // text-overflow: ellipsis;
        white-space: normal;
      }
    }
  }
}

@media screen and (max-width: 1280px) {
  .prec-title {
    width: 55.5%;
  }

  .mate-title {
    width: 27%;
  }
}
</style>
