<template>
  <div class="list-item">
    <SituationTitle
      v-if="isShowFirstTitle"
      :situation="situation"
      @openWindowlink="openWindowlink"
      style="display: flex; align-items: center"
    >
      <!-- 多选 -->
      <el-checkbox
        style="margin-left: 5px"
        v-if="situation.columnType == 2 && situation.linkStatus !== 1"
        v-model="chooseAllCheckbox"
        @change="chooseAll"
      />
      <!-- 文本输入框 -->
      <!-- <template
        v-if="null == situation.linkStatus || situation.linkStatus === 0"
      >
        <el-input
          v-if="situation.columnType == 3"
          v-model="inputTextVal"
          ref="inputText"
          :placeholder="'请输入' + situation.situationName"
          style="width: 360px; margin-left: 20px"
          @change="checkContent"
        />
      </template> -->
    </SituationTitle>
    <!-- 文本输入框 -->
    <!-- <span v-if="null != inputVal">
      <el-radio-group
        v-model="inputVal.optionCode"
        class="el-radio-group--text"
      >
        <el-radio :label="inputVal.optionCode">
          {{ inputVal.name }}
        </el-radio>
        <SituationRemark v-if="inputVal.remark" :content="inputVal.remark" />
      </el-radio-group>
    </span> -->
    <!-- v-if="isShowFirstTitle" -->
    <SituationItem
      ref="situationItem"
      :situation="situation"
      :comboDireOid="comboDireOid"
      :hasAllSelect="hasAllSelect"
      :hasClickAllBut="hasClickAllBut"
      @cancelSelect="cancelSelect"
      @allSelect="allSelect"
      :situationOids="situationOids"
      :idx="idx"
      @getRe="getRemark"
    />
    <div
      v-if="showQualiRemark[idx] != '' && showQualiRemark[idx] != null"
      class="hint"
      v-html="showQualiRemark[idx]"
    />
  </div>
</template>

<script>
import SituationItem from "./situationItem";
import SituationRemark from './situationRemark';
import SituationTitle from './situationTitle';

import { getOptionValBySituOid } from "@/api/onething/comboManager/comboAccept/situation";

import { isObject } from './util';

export default {
  name: 'Situation',
  components: {
    SituationItem,
    SituationRemark,
    SituationTitle
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
    index: {
      type: Number
    }
  },

  data () {
    return {
      showQualiRemark: [],
      idx: 0,
      chooseAllCheckbox: false,
      //是否全选的标识
      hasAllSelect: false,
      checkboxAllFlag: false,
      hasClickAllBut: false,
      inputTextVal: '',
      inputVal: null,
    };
  },
  computed: {
    // 是否在第一级展示`component: situationItem`组件
    isShowFirstTitle ({ situation: { columnType, linkStatus }, inputVal }) {
      // 不是文本类型的或者是连接类型的就展示第一级
      return !((null == linkStatus || linkStatus === 0) && columnType === 3 && !inputVal);
    }
  },
  created () {
    this.idx = this.index;
  },
  methods: {
    //验证一级文本框内容
    checkContent () {
      const FloatRegex = /^[0-9]+\.?[0-9]*$/;
      if (!FloatRegex.test(this.inputTextVal)) {
        this.msgWarning("输入框内容必须为数值");
        return false;
      }
      if (parseFloat(this.inputTextVal) >= 100000000) {
        this.msgWarning("输入框内容数值过大");
        return false;
      }
      getOptionValBySituOid({
        situationOid: this.situation.situationOid,
        textVal: this.inputTextVal
      }).then(response => {
        if (response.data) {
          let obj = {
            valOid: response.data.valOid,
            name: response.data.name,
            remark: response.data.remark,
            optionCode: response.data.optionCode
          };
          this.inputVal = obj;
        }
      });
    },
    //子页面传回选项值备注
    getRemark (data) {
      this.showQualiRemark[this.index] = data;
      this.$forceUpdate();
    },

    /** 获取选项值values */
    getAllSelectValues () {
      try {
        let children = this.$refs.situationItem;
        let selectValOids = [], inputArr = [];
        while (children) {
          if (Array.isArray(children) && children?.length > 0) {
            children.forEach(item => {
              this.handleSelectValues(selectValOids, item);
              this.handleInputValues(inputArr, item);
              children = item?.$refs?.childSituationItem;
            })
          } else if (isObject(children)) {
            this.handleInputValues(inputArr, children);
            this.handleSelectValues(selectValOids, children);
            children = children?.$refs?.childSituationItem;
          } else {
            children = null;
          }
        }
        // this.handleInputValues(inputArr, this);
        if (inputArr.length > 0) {
          selectValOids = selectValOids.concat(inputArr.map(item => item.valOid));
        }
        return selectValOids;
      } catch (error) {
        console.log('%c [error]:', 'color:red;font-weight:700;', error);
      }
    },

    /** 获取选项值situaionOids */
    getAllSituaionOids () {
      try {
        let children = this.$refs.situationItem;
        let situationArr = [];
        while (children) {
          if (Array.isArray(children) && children?.length > 0) {
            children.forEach(item => {
              this.handleSituationValues(situationArr, item);
              children = item?.$refs?.childSituationItem;
            })
          } else if (isObject(children)) {
            this.handleSituationValues(situationArr, children);
            children = children?.$refs?.childSituationItem;
          } else {
            children = null;
          }
        }
        this.handleSituationValues(situationArr, this);
        return situationArr;
      } catch (error) {
        console.log('%c [error]:', 'color:red;font-weight:700;', error);
      }
    },

    getValues () {
      try {
        let children = this.$refs.situationItem;
        let inputArr = [], situationArr = [], selectValOids = [], mustStatusNoCheckArr = [];
        while (children) {
          if (Array.isArray(children) && children?.length > 0) {
            children.forEach(item => {
              this.handleInputValues(inputArr, item);

              this.handleSituationValues(situationArr, item);

              this.handleSelectValues(selectValOids, item);

              this.handleMustStatus(mustStatusNoCheckArr, item);

              children = item?.$refs?.childSituationItem;
            })
          } else if (isObject(children)) {
            this.handleInputValues(inputArr, children);

            this.handleSituationValues(situationArr, children);

            this.handleSelectValues(selectValOids, children);

            this.handleMustStatus(mustStatusNoCheckArr, children);

            children = children?.$refs?.childSituationItem;
          } else {
            children = null;
          }
        }

        // this.handleInputValues(inputArr, this);

        this.handleSituationValues(situationArr, this);

        // 如果存在一级标题是文本类型 判断当前文本是否必填
        // if (this.situation.mustStatus === 1 && this.situation.columnType === 3 && !this.inputTextVal) {
        //   mustStatusNoCheckArr.unshift({
        //     situationOid: this.situation.situationOid,
        //     situationName: this.situation.situationName,
        //   });
        // }

        if (inputArr.length > 0) {
          selectValOids = selectValOids.concat(inputArr.map(item => item.valOid));
        }

        return [inputArr, situationArr, selectValOids, mustStatusNoCheckArr];
      } catch (error) {
        console.log('%c [error]:', 'color:red;font-weight:700;', error);
      }
    },

    /** 处理`component:situationItem`情形选项 */
    handleSituationItem (childSituation) {
      try {
        let children = this.$refs.situationItem;
        while (children) {
          if (Array.isArray(children) && children?.length > 0) {
            children.forEach(item => {
              this._handleSituationItem(childSituation, item)
              children = item?.$refs?.childSituationItem;
            })
          } else if (isObject(children)) {
            this._handleSituationItem(childSituation, children)
            children = children?.$refs?.childSituationItem;
          } else {
            children = null;
          }
        }
      } catch (error) {
        console.log('%c [error]:', 'color:red;font-weight:700;', error);
      }
    },

    _handleSituationItem (childSituation, instance) {
      const arr = instance.getCurrentSelectItemValues();
      if (arr.includes(childSituation.optionOid)) {
        // 如果是单选
        if (instance.situation.columnType === 1 && !instance.situation.linkStatus) {
          instance.childItemsMap = [];
          instance.childItems = [];
        }

        if (childSituation.directorySituationList.length === 0) {
          // instance.childItemsMap = instance.childItemsMap.filter(item => item.optionOid !== childSituation.optionOid);
          // instance.childItems = instance.childItems.filter(item => item.valOid !== childSituation.optionOid);
          // instance.initSituationOids = instance.situationOids;

          // 如果是多选
          if (instance.situation.columnType === 2) {
            instance.childItemsMap = instance.childItemsMap.filter(item => item.optionOid !== childSituation.optionOid);
            instance.childItems = instance.childItems.filter(item => item.valOid !== childSituation.optionOid);
          }
        } else {
          const nextSituation = instance.situation.optionValsList.find(item => item.valOid === childSituation.optionOid);
          if (nextSituation) {
            let situationList = [];
            if (childSituation.directorySituationList) {
              childSituation.directorySituationList.forEach(child => {
                let index = instance.childItemsMap.map(item => item.situationOid).findIndex(cl => cl === child.situationOid);
                if (index < 0) {
                  instance.childItemsMap.push({
                    situationOid: child.situationOid,
                    optionOid: childSituation.optionOid
                  });
                  situationList.push(child);
                }
                // if (child.situationOid) {
                //   if (instance.initSituationOids.indexOf(child.situationOid) == -1) {
                //     instance.initSituationOids += ',' + child.situationOid + ',';
                //   }
                // }
              });
              if (situationList.length > 0) {
                let obj = {
                  "valOid": childSituation.optionOid,
                  "situationList": situationList
                };
                instance.childItems.push(obj);
              }
            } else {
              // if (instance.initSituationOids.indexOf(instance.situation.situationOid) == -1) {
              //   instance.initSituationOids += ',' + instance.situation.situationOid + ',';
              // }
            }
          }
        }
      }
    },

    handleInputValues (arr, instance) {
      if (instance.inputTextVal && instance.inputVal) {
        arr.push({
          pacSituationOid: instance.situation.situationOid,
          txtContent: instance.inputTextVal,
          valOid: instance.inputVal.valOid
        })
      }
    },

    handleSelectValues (arr, instance) {
      if (instance.situationRadioVal) {
        arr.push(instance.situationRadioVal);
      }

      if (instance.situationCheckboxVal && instance.situationCheckboxVal.length > 0) {
        arr.push(...instance.situationCheckboxVal);
      }
    },

    handleSituationValues (arr, instance) {
      if (instance.situation.situationOid) {
        arr.push(instance.situation.situationOid);
      }
    },

    handleMustStatus (arr, instance) {
      if (instance.situation.mustStatus === 1 && instance.situation.linkStatus !== 1) {
        if (
          (instance.situation.linkStatus === 0 && instance.situation.columnType === 1 && !instance.situationRadioVal)
          ||
          (instance.situation.linkStatus === 0 && instance.situation.columnType === 2 && instance.situationCheckboxVal.length === 0)
          ||
          (instance.situation.linkStatus === 0 && instance.situation.columnType === 3 && !instance.inputTextVal)
          ||
          (instance.situation.linkStatus === 1 && instance.situationCheckboxVal.length === 0)
        ) {
          arr.push({
            situationOid: instance.situation.situationOid,
            situationName: instance.situation.situationName,
          })
        }
      }
    },

    /** 全选 */
    chooseAll (checked) {
      this.hasClickAllBut = true;
      this.checkboxAllFlag = checked;
      this.hasAllSelect = checked;
    },
    /** 取消全选 */
    cancelSelect () {
      this.hasClickAllBut = false;
      this.hasAllSelect = false;
      this.chooseAllCheckbox = false;

    },
    allSelect () {
      this.hasClickAllBut = false;
      this.hasAllSelect = true;
      this.chooseAllCheckbox = true;
    },

    openWindowlink () {
      if (this.situation.linkUrl && this.situation.linkStatus === 1) {
        window.open(this.situation.linkUrl);
      }
    }
  }
}

</script>

<style lang="scss">
.list-item {
  margin-left: 0 !important;

  .situation-item-must {
    font-weight: 400;
    color: #ff0000;
    font-size: 14px;
    margin-left: 5px;
    margin-bottom: 4px;
  }

  .el-radio-group--text {
    display: flex;
    align-items: center;
    margin-top: 15px;
    .el-radio {
      margin-right: 10px;
    }
  }

  p {
    color: #000000;
    font-size: 16px;
    margin: 0 0 20px;

    .el-checkbox {
      & .el-checkbox__input {
        &.is-checked {
          .el-checkbox__inner {
            background-color: #1890ff;
            border-color: #1890ff;
            border-radius: 4px;
          }
        }

        .el-checkbox__inner {
          width: 18px;
          height: 18px;

          &:hover {
            border-color: #1890ff;
          }

          &::after {
            height: 10px;
            left: 5px;
            width: 5px;
            border: 2px solid #fff;
            border-left: 0;
            border-top: 0;
          }
        }
      }
    }
  }

  & > div {
    margin-left: 30px;

    & > span {
      color: #353535;
      font-size: 20px;
    }
  }

  .hint {
    margin-top: 15px;
    font-size: 14px;
    color: #999999;
  }
}
</style>
<style lang="scss">
.hint {
  a,
  a:focus,
  a:hover {
    color: #237ee9;
  }
}
</style>
