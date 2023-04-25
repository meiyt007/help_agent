<template>
  <div
    class="situation-item"
    :class="{
      'situation-item--first': situation.ifFrist && situation.columnType == 3,
    }"
  >
    <!-- 没有选中链接的方式 -->
    <template v-if="null == situation.linkStatus || situation.linkStatus === 0">
      <!-- 单选 -->
      <template v-if="situation.columnType == 1">
        <el-radio-group
          class="next-options"
          v-loading="loading"
          @change="
            selectSitu($event, situation.optionValsList, situation.columnType)
          "
          v-model="situationRadioVal"
        >
          <SituationTitle
            v-if="!situation.ifFrist && situation.titleShowStyle === 1"
            :situation="situation"
          />
          <div
            :class="[item.remark ? 'situation-item-info' : '']"
            v-for="(item, idx) in situation.optionValsList"
            :key="idx"
          >
            <el-radio :label="item.valOid">{{ item.name }}</el-radio>
            <SituationRemark
              v-if="item.remark && !situation.ifFrist"
              :content="item.remark"
            />
            <template v-if="situationRadioVal === item.valOid">
              <div v-for="(childd, index) in childItems" :key="index">
                <template v-if="childd.valOid == item.valOid">
                  <SituationItem
                    ref="childSituationItem"
                    :class="`situation-item-box situation-item-box--${childIdx}`"
                    v-for="child in childd.situationList"
                    :key="child.id"
                    :situation="child"
                    :comboDireOid="comboDireOid"
                    :idx="idx"
                    :childIdx="index"
                  />
                  <!-- :situationOids="initSituationOids" -->
                </template>
              </div>
            </template>
          </div>
        </el-radio-group>
      </template>
      <!-- 多选 -->
      <template v-if="situation.columnType == 2">
        <SituationTitle v-if="!situation.ifFrist" :situation="situation">
          <!--此处不能使用el-checkbox，会被上一层el-checkbox-group影响-->
          <CheckBox
            id="idx"
            :checked="allCheckbox"
            @change="allCheckboxSelect"
            style="margin-left: 5px"
          />
        </SituationTitle>
        <el-checkbox-group
          class="next-options"
          v-loading="loading"
          v-model="situationCheckboxVal"
          :key="situation.situationOid"
          @change="
            selectSitu($event, situation.optionValsList, situation.columnType)
          "
        >
          <div
            :class="[item.remark ? 'situation-item-info' : '']"
            v-for="(item, idx) in situation.optionValsList"
            :key="idx"
          >
            <el-checkbox :label="item.valOid">{{ item.name }}</el-checkbox>
            <SituationRemark
              v-if="item.remark && !item.ifFrist"
              :content="item.remark"
            />
            <template v-if="situationCheckboxVal.indexOf(item.valOid) > -1">
              <div v-for="(childd, index) in childItems" :key="index">
                <template v-if="childd.valOid == item.valOid">
                  <SituationItem
                    ref="childSituationItem"
                    :class="`situation-item-box situation-item-box--${childIdx}`"
                    v-for="child in childd.situationList"
                    :key="child.id + 'checkbox'"
                    :situation="child"
                    :comboDireOid="comboDireOid"
                    :idx="idx"
                    :childIdx="index"
                  />
                  <!-- :situationOids="initSituationOids" -->
                </template>
              </div>
            </template>
          </div>
        </el-checkbox-group>
      </template>
      <!-- 输入框 -->
      <template v-if="situation.columnType == 3">
        <!-- v-if="!situation.ifFrist" -->
        <SituationText
          v-model="inputTextVal"
          :situation="situation"
          :inputVal="inputVal"
          @setInputVal="
            (val) => {
              inputVal = val
              selectSitu(val.valOid, situation.optionValsList)
            }
          "
        />
        <template v-if="!!inputVal">
          <div
            v-for="(childd, index) in childItems"
            :key="index"
            :class="{ 'situation-item--text-first': situation.ifFrist }"
          >
            <template v-if="childd.valOid == inputVal.valOid">
              <SituationItem
                ref="childSituationItem"
                :class="`situation-item-box situation-item-box--${childIdx}`"
                v-for="child in childd.situationList"
                :key="child.id + 'checkbox'"
                :situation="child"
                :comboDireOid="comboDireOid"
                :idx="idx"
                :childIdx="index"
              />
              <!-- :situationOids="initSituationOids" -->
            </template>
          </div>
        </template>
      </template>
    </template>
    <!-- 选择链接的方式 -->
    <template v-if="null != situation.linkStatus && situation.linkStatus === 1">
      <div class="already-know">
        <SituationTitle
          v-if="!situation.ifFrist"
          :situation="situation"
          @openWindowlink="openWindowlink"
        />
        <!-- <el-checkbox-group
          v-model="situationCheckboxVal"
          v-for="(item, index) in situation.optionValsList"
          :key="index"
        >
          <el-checkbox :label="item.valOid">{{ item.name }}</el-checkbox>
          <SituationRemark v-if="item.remark" :content="item.remark" />
        </el-checkbox-group> -->
      </div>
    </template>
  </div>
</template>

<script>
import { queryComboDirectorySituationByValOid, } from "@/api/onething/comboManager/comboAccept/situation";

// components
import SituationRemark from './situationRemark';
import SituationText from './situationText';
import SituationTitle from './situationTitle';

import { getCurrentComponentsIndex } from './util.js';

import CheckBox from './checkbox';
export default {
  name: "SituationItem",
  inject: ['parentInstance'],
  components: {
    SituationRemark,
    SituationText,
    SituationTitle,
    CheckBox
  },
  props: {
    situation: {
      type: Object,
      default: () => {
        return {};
      }
    },
    comboDireOid: {
      type: String,
      default: ""
    },
    situationOids: {
      type: String,
      default: ""
    },
    idx: {
      type: Number
    },
    //父组件中多选框是否权限
    hasAllSelect: {
      type: Boolean,
      default: false
    },
    hasClickAllBut: {
      type: Boolean,
      default: false
    },
  },
  data () {
    return {
      //单选
      situationRadioVal: "",
      //复选
      situationCheckboxVal: [],
      childItems: [],
      childItemsMap: [],
      //情形选项文本框值
      inputTextVal: "",
      inputVal: null,
      indd: 0,
      // initSituationOids: this.situationOids,
      openInit: false,
      loading: false,
      allCheckbox: false,
      childIdx: 0,
    };
  },
  created () {
    this.indd = this.idx;
    // 获取当前组件索引
    this.childIdx = getCurrentComponentsIndex(this);
  },
  mounted () {
    this.$nextTick(async () => {
      await this.handleDefaultFlag();

      this.$nextTick(() => {
        this.handleSituationAnswerList();
      })
    })
  },
  watch: {
    hasAllSelect (newValue, oldValue) {
      if (!newValue) {
        if (this.hasClickAllBut) {
          this.situationCheckboxVal = [];
        }
      } else {
        this.situation.optionValsList.forEach(st => {
          this.situationCheckboxVal.push(st.valOid);
        });
        this.selectSitu(this.situationCheckboxVal, this.situation.optionValsList, this.situation.columnType);
      }
    },
    //监听多选框的值，操作父级元素中的全选checkbox状态
    situationCheckboxVal (newVal, oldVal) {
      // 部分选中
      if (newVal.length < this.situation.optionValsList.length) {
        this.$emit("cancelSelect");
      }
      // 全部选中
      if (newVal.length === this.situation.optionValsList.length) {
        this.$emit("allSelect");
      }
    }
  },
  methods: {
    /** 判断是否含有defaultFlag = 1 默认选中的  */
    async handleDefaultFlag () {
      const { columnType, optionValsList, linkStatus } = this.situation;
      if (linkStatus !== 1) {
        if (columnType === 1) {
          const defaultValue = optionValsList.find(item => item.defaultFlag === 1);

          if (!defaultValue) return;

          // 判断智能问答携带的情形中是否存在 如果存在删除
          if (this.parentInstance.situationAnswerList.includes(defaultValue.valOid)) {
            this.parentInstance.situationAnswerList = this.parentInstance.situationAnswerList.filter(d => d !== defaultValue.valOid);
          }

          this.situationRadioVal = defaultValue.valOid;
          return await this.selectSitu(defaultValue.valOid, optionValsList, columnType);
        } else if (columnType === 2) {
          const defaultValue = optionValsList.filter(item => item.defaultFlag === 1).map(item => item.valOid);

          if (defaultValue.length === 0) return;

          // 判断智能问答携带的情形中是否存在 如果存在删除
          defaultValue.forEach(item => {
            if (this.parentInstance.situationAnswerList.includes(item)) {
              this.parentInstance.situationAnswerList = this.parentInstance.situationAnswerList.filter(d => d !== item);
            }
          })

          this.situationCheckboxVal = defaultValue;
          return await this.selectSitu(defaultValue, optionValsList, columnType);
        }
      }
    },

    /** 处理智能问答带出的情形oid */
    async handleSituationAnswerList () {
      const value = this.parentInstance.situationAnswerList;

      if (value.length === 0) return;

      const { columnType, optionValsList, linkStatus } = this.situation;

      if (linkStatus !== 1) {
        if (columnType === 1) {
          const defaultValue = optionValsList.find(item => value.includes(item.valOid));

          if (!defaultValue) return;

          // 删除已经存在的oid
          this.parentInstance.situationAnswerList = value.filter(d => d !== defaultValue.valOid);

          this.situationRadioVal = defaultValue.valOid;
          return await this.selectSitu(defaultValue.valOid, optionValsList, columnType);
        } else if (columnType === 2) {
          const defaultValue = optionValsList.filter(item => value.includes(item.valOid)).map(item => item.valOid);

          if (defaultValue.length === 0) return;

          // 删除已经存在的oid
          defaultValue.forEach(item => {
            if (value.includes(item)) {
              this.parentInstance.situationAnswerList = value.filter(d => d !== item);
            }
          });

          this.situationCheckboxVal = defaultValue;
          return await this.selectSitu(defaultValue, optionValsList, columnType);
        }
      }
    },

    async selectSitu (selectVal = "", optionValsList, columnType) {
      if (typeof selectVal !== 'string') {
        if (Object.prototype.toString.call(selectVal) === '[object Array]') {
          this.situationCheckboxVal = [...new Set([...selectVal])];
        }
        // 部分全选时清除全选框
        this.allCheckbox = selectVal.length == optionValsList.length;
      }
      let checkOids = this.getCurrentSelectItemValues()?.join?.(',');
      this.loading = true;
      let { code, data } = await queryComboDirectorySituationByValOid({
        comboDirectoryOid: this.comboDireOid,
        situationOids: this.parentInstance.getAllSituationOids(),
        valOid: this.parentInstance.getAllSelectValues(),
        checkOids
      });
      this.loading = false;
      if (data && code === 200) {
        const arr = this.handleSort(data);

        console.log(arr);

        arr.forEach(childSituation => {
          this.parentInstance.handleSituationItem(childSituation);
        });

        // data.forEach(childSituation => {
        //   this.parentInstance.handleSituationItem(childSituation);
        // });
      }
      //添加第一层选项值备注
      if (selectVal.indexOf(",") < 0 && columnType == 1) {
        if (optionValsList) {
          optionValsList.forEach(va => {
            if (va.valOid == selectVal) {
              this.$emit('getRe', va.remark);
            }
          });
        }
      }
    },

    getIndex (list, cur) {
      const arr = list
        .filter(item => item.optionOid !== cur.optionOid);
      const index = arr.findIndex(item => item.directorySituationList.some(i => i.optionValsList.some(c => c.valOid === cur.optionOid)));
      return [index, arr[index]];
    },

    handleSort (data) {
      let idxs = [];
      data.forEach((item) => {
        let [index, relation] = this.getIndex(data, item);
        idxs.push({
          index,
          value: item,
          relation
        });
      })

      let arr = [];

      let first = idxs.filter(item => item.index === -1 && !item.relation);

      first.forEach((i, idx) => {
        idxs.splice(idx, 1);
        arr.push(i.value);
        handleRelation(i.value);
      })

      function handleRelation (item) {
        let cur = idxs.filter(i => i.relation).filter(i => i.relation.optionOid === item.optionOid);

        cur.forEach(c => {
          let i = idxs.findIndex(j => j.value.optionOid === c.value.optionOid);
          idxs.splice(i, 1);
        })

        arr = arr.concat(cur.map(i => i.value));

        if (cur.length > 0) {
          cur.forEach(i => {
            handleRelation(i.value);
          })
        }
      }

      return arr;
    },

    //获取选择的值
    getSelectItemValues () {
      let arr = [];
      arr = arr.concat(this.getCurrentSelectItemValues());
      let childSituationItemRefs = this.$refs.childSituationItem;
      if (childSituationItemRefs && childSituationItemRefs.length > 0) {
        childSituationItemRefs.forEach(csr => {
          let csrArr = csr.getSelectItemValues();
          arr.push(...csrArr);
        });
      }
      //去重数组
      return [...new Set(arr)];
    },

    getCurrentSelectItemValues () {
      let arr = [];
      if (this.situationRadioVal) {
        arr.push(this.situationRadioVal);
      }
      if (this.situationCheckboxVal && this.situationCheckboxVal.length > 0) {
        arr.push(...this.situationCheckboxVal);
      }
      if (this.inputVal) {
        arr.push(this.inputVal.valOid);
      }
      return [...new Set(arr)];
    },

    openWindowlink () {
      window.open(this.situation.linkUrl);
    },

    /** 全选 */
    allCheckboxSelect (checked) {
      if (checked) {
        this.situation.optionValsList.forEach(item => {
          this.situationCheckboxVal.push(item.valOid);
        });
        this.selectSitu(this.situationCheckboxVal, this.situation.optionValsList);
      } else {
        this.situationCheckboxVal = [];
        this.selectSitu('', this.situation.optionValsList);
      }
      this.allCheckbox = checked;
    },
  },
}

</script>

<style lang="scss">
.situation-item {
  animation: show 1s;

  &.situation-item--first {
    margin-left: 0;
  }
  .next-options {
    width: 100%;

    p {
      // font-size: 16px;
      // margin: 0;
      margin-top: unset;
      display: flex;
      align-items: center;
    }

    .next-options {
      // padding: 15px 25px;
      // margin: 10px 0;

      &.options-border {
        border: 1px dashed #ccc;
      }
    }

    & > div:not(:first-child) {
      margin-top: 20px;
    }

    .el-checkbox {
      margin-right: 5px;

      & >>> .el-checkbox__input {
        &.is-checked {
          .el-checkbox__inner {
            background-color: #237ee9;
            border-color: #237ee9;
            border-radius: 4px;
          }
        }

        .el-checkbox__inner {
          width: 20px;
          height: 20px;

          &:hover {
            border-color: #237ee9;
          }

          &::after {
            height: 11px;
            left: 6px;
            width: 5px;
            border: 2px solid #fff;
            border-left: 0;
            border-top: 0;
          }
        }
      }

      &.is-checked {
        & >>> .el-checkbox__label {
          color: #237ee9;
        }
      }

      & >>> .el-checkbox__label {
        font-size: 16px;
        color: #353535;
        vertical-align: middle;
      }
    }

    .el-radio {
      margin-right: 5px;

      &.is-checked {
        & >>> .el-radio__inner {
          background-color: #237ee9;
          border-color: #237ee9;
        }

        & >>> .el-radio__label {
          color: #237ee9;
        }
      }

      & >>> .el-radio__inner {
        width: 20px;
        height: 20px;

        &::after {
          width: 6px;
          height: 6px;
        }
      }

      & >>> .el-radio__label {
        font-size: 16px;
        color: #353535;
        vertical-align: middle;
      }
    }

    .run-area {
      margin-top: 25px;
      margin-left: 25px;

      & >>> .el-form-item__content {
        line-height: initial;
      }
    }

    .already-know {
      // margin: 35px 0 35px 25px;

      // span {
      //   font-size: 16px;
      //   color: #747b83;
      //   margin-right: 10px;
      //   cursor: pointer;
      //   border-bottom: 1px solid #747b83;
      // }

      .el-checkbox-group {
        display: inline-block;
        margin-top: 0;
      }
    }
  }

  .hint {
    margin-top: 15px;
    font-size: 15px;
    color: #999999;
  }

  // .situation-item-title {
  //   font-weight: bold;
  //   color: #2a344c;
  //   font-size: 14px;
  //   margin-right: 4px;

  //   &--link .situation-item-title--link-span {
  //     text-decoration: underline;
  //     cursor: pointer;
  //   }
  // }

  .situation-item-box {
    border: 1px dashed #cccccc;
    padding: 10px 25px;
    margin: 10px 0;

    &--1 {
      background: #fafcfd;
      border-color: #bbc5cd;
    }

    &--2 {
      background: #f3f8fc;
      border-color: #aeb8c6;
    }
  }

  .situation-item--text-first {
    margin-left: 30px;
  }
}
</style>
