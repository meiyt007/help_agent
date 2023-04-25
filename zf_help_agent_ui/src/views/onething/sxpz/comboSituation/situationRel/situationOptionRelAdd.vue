<template>
  <div class="precondition">
    <p :style="{ width: materialWidth }">
      <span>前置选项</span>
      <span>情形展示</span>
    </p>
    <div ref="getHeight" class="collapse">
      <template v-for="(situ, idx) in situationList">
        <el-collapse v-model="situ.collapseFlag">
          <el-collapse-item
            :name="situ.situationName"
            :class="{ precSelected: situ.checkSituation }"
          >
            <template slot="title">
              <span>{{ idx + 1 }}</span>
              <p :title="situ.situationName">{{ situ.situationName }}</p>
              <el-checkbox
                @change="preconditionChange($event, situ)"
                v-model="situ.checkSituation"
              ></el-checkbox>
            </template>
            <el-radio-group
              @change="radioChange($event, situ)"
              v-model="situ.radioValOid"
              v-if="situ.columnType !=2 "
            >
              <el-radio
                :title="opt.name"
                v-for="(opt, index) in situ.optionValsList"
                :label="opt.valOid"
              >
                {{ opt.name }}</el-radio
              >
            </el-radio-group>
            <el-checkbox-group
              @change="checkboxChange($event, situ)"
              v-model="situ.valOidList"
              v-if="situ.columnType == 2"
            >
              <el-checkbox
                :title="opt.name"
                v-for="(opt, index) in situ.optionValsList"
                :label="opt.valOid"
              >
                {{ opt.name }}
              </el-checkbox>
            </el-checkbox-group>
          </el-collapse-item>
        </el-collapse>
      </template>
    </div>
  </div>
</template>

<script>
import {
  initComboOptionRel,
  saveOptionRel
} from "@/api/onething/comboSituation/comboOptionRel.js";
export default {
  //定义获取父类传过来值的格式
  props: ["relOid", "comboDirectoryOid"],
  data () {
    return {
      materialWidth: '',
      situationList: [],
      situationOids: '',
      optionOids: ''
    };
  },
  created () {
    this.handleInit();
  },
  mounted () {
    this.materialWidth = this.$refs.getHeight.offsetWidth + 'px';
  },
  methods: {
    /** 查询情形列表 */
    handleInit () {
      initComboOptionRel(this.comboDirectoryOid, this.relOid).then(response => {
        this.situationList = response.data.situationList;
        this.situationList.forEach(item => {
          item.valOidList = [];
        });
        if (this.relOid != '') {
          let oldOptRel = response.data.optionRel;
          this.situationList.forEach(item => {
            if (oldOptRel.titleOids.indexOf(item.situationOid) != -1) {
              item.checkSituation = true;
              this.situationOids += item.situationOid + ',';
            }
            item.optionValsList.forEach(opt => {
              let flag = false;
              if (oldOptRel.valueOids.indexOf(opt.valOid) != -1) {
                flag = true;
                if (item.columnType == 2) { // 多选
                  item.valOidList.push(opt.valOid);
                } else if (item.columnType == 1 || item.columnType == 3) { // 单选或者文本
                  item.radioValOid = opt.valOid;
                }
              }
              if (flag) {
                item.collapseFlag = item.situationName;
              }
            })
          })
        }
      });
    },
    submitForm: function () {
      this.optionOids = '';
      this.situationList.forEach(item => {
        if (item.columnType == 2 && item.valOidList.length > 0) {
          this.optionOids += item.valOidList.toString() + ',';
        } else if (item.columnType !=2 && item.radioValOid != null && item.radioValOid != '') {
          this.optionOids += item.radioValOid + ',';
        }
      });
      if (this.optionOids == '') {
        this.msgError('前置选项不能为空！');
        return false;
      }
      if (this.situationOids == '') {
        this.msgError('情形展示不能为空！');
        return false;
      }
      saveOptionRel(this.comboDirectoryOid, this.situationOids, this.optionOids, this.relOid).then(response => {
        if (response.code === 200) {
          this.msgSuccess("保存成功");
          this.$emit('combo-form', 1);
        }
      });
    },
    cancel: function () {
      this.$emit('combo-form');
    },
    // 选中或取消情形
    preconditionChange (checkFlag, item) {
      if (checkFlag) {
        item.checkSituation = true;
        item.collapseFlag = '';
        this.situationOids += item.situationOid + ',';
        item.valOidList = [];
        item.radioValOid = '';
      } else {
        item.checkSituation = false;
        this.situationOids = this.situationOids.replace(item.situationOid + ',', '');
      }
    },
    // 选中单选选项
    radioChange (valOid, pItem) {
      pItem.checkSituation = false;
      this.situationOids = this.situationOids.replace(pItem.situationOid + ',', '');
    },
    // 选中或取消多选选项
    checkboxChange (checkFlag, pItem) {
      if (checkFlag) {
        pItem.checkSituation = false;
        this.situationOids = this.situationOids.replace(pItem.situationOid + ',', '');
      }
    },
  }
}

</script>
<style lang="scss" scoped>
.precondition {
  overflow: hidden;

  & > p {
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

    span {
      &:nth-child(2) {
        float: right;
      }
    }
  }

  .collapse {
    margin: 50px 0;
  }

  .el-collapse {
    border-top-color: transparent;
    border-bottom-color: transparent;

    .el-collapse-item {
      box-shadow: 0 0 5px #ededed;
      margin: 0 2px 10px;

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
              max-width: 1150px;
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
              max-width: 940px;
            }
          }

          @media screen and (max-width: 1500px) {
            .el-radio__label,
            .el-checkbox__label {
              max-width: 780px;
            }
          }
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
  background-color: #fff;
  padding: 20px 0;
}
</style>
