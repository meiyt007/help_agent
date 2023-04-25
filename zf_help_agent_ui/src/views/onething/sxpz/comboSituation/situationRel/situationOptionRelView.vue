<template>
  <div class="precondition">
    <p :style="{ width: materialWidth }">
      <span>前置选项</span>
      <span>情形展示</span>
    </p>
    <div ref="getHeight">
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
                disabled="true"
                v-model="situ.checkSituation"
              ></el-checkbox>
            </template>
            <el-radio-group
              disabled="true"
              v-model="situ.radioValOid"
              v-if="situ.columnType !=2"
            >
              <el-radio
                :title="opt.name"
                :label="opt.valOid"
                v-for="(opt, index) in situ.optionValsList"
              >
                {{ opt.name }}</el-radio
              >
            </el-radio-group>
            <el-checkbox-group
              disabled="true"
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
  initComboOptionRel
} from "@/api/onething/comboSituation/comboOptionRel.js";
export default {
  //定义获取父类传过来值的格式
  props: ["relOid", "comboDirectoryOid"],
  data () {
    return {
      activeNames: ['1'],
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
        console.log(this.situationList)
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
                } else if (item.columnType == 1) { // 单选
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
</style>
