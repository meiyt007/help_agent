<!-- 智能问答 -->
<template>
  <div class="onething-intelligent-qa" v-loading="loading">
    <div class="intelligent-qa-service" @click="openServiceObject">
      【 服务对象：{{
        pCegisterType ? (pCegisterType == '1' ? '法人' : '个人') : '-'
      }}
      】
    </div>
    <el-scrollbar class="common-dialog-content">
      <template v-if="comboSituationList.length > 0">
        <div
          class="situation-box situation-box--bottom-border"
          :class="{
            'no-padding-bottom':
              situation.ifFrist && situation.linkStatus === 1,
          }"
          v-for="(situation, index) in comboSituationList"
          :key="index"
        >
          <situation
            ref="situation"
            :situation="situation"
            :comboDireOid="comboDirectoryOid"
            :situationOids="situationOids"
            :index="index"
          />
        </div>
      </template>
      <el-empty description="暂无情形信息" v-else></el-empty>
    </el-scrollbar>
    <div class="common-dialog-footer">
      <el-button type="primary" @click="beforeNextStep">下一步</el-button>
    </div>

    <!-- 服务对象弹框组件 -->
    <ServiceObjectDialog
      v-if="visibleDialog"
      :visibleDialog.sync="visibleDialog"
      :serviceObject="pCegisterType"
      @setServiceObject="
        (data) => {
          $emit('setServiceObject', data)
        }
      "
    />

    <ConsultationTelephone />

    <el-dialog v-dialog-drag title="前置核验" :visible.sync="visible" append-to-body footerCenter height="600px" scrollbar>
      <Verfication ref="verfication" :comboDirectoryOid="comboDirectoryOid" />
      <span slot="footer">
        <el-button type="primary" @click="nextStep">下一步</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { queryNoRelSituationListByDireOid, judgeStopCondition } from '@/api/onething/comboManager/comboAccept/situation';
import situation from './situation/situation';
import { handleArr2String, } from './util';

import ServiceObjectDialog from '@/views/zc/businessManagement/windowAcceptance/components/serviceObjectDialog.vue';
import ConsultationTelephone from '@/views/zc/businessManagement/windowAcceptance/components/consultationTelephone.vue';
import Verfication from './verfication.vue';
import { queryComboDirectoryPrecheckList } from "@/api/onething/sxpz/comboDirectory";

export default {
  name: 'IntelligentQA',
  inheritAttrs: false,
  provide () {
    return {
      parentInstance: this
    }
  },
  props: {
    comboDirectoryOid: {
      type: String,
      default: ''
    },
    // 智能问答 通过语音 带过来的 oid
    situationAnswerList: {
      type: Array,
      default: () => []
    },
    situation: {
      type: Object,
    },
    pCegisterType: [String, Number],
    serviceRoot: Array,
    comeFormArtific: Boolean
  },
  components: { situation, ServiceObjectDialog, Verfication },
  data () {
    return {
      socialCreditCode: '', // 请输入统一社会信用代码
      //情形列表
      comboSituationList: [],
      situationOids: '',
      loading: true,

      visibleDialog: false,
      visible: false,
      tableData:[]
    }
  },
  mounted () {
    this.queryNoRelSituationListByDireOid();

    this.$getResponse(queryComboDirectoryPrecheckList({ comboDirectoryOid: this.comboDirectoryOid, }), (error, res) => {
          if (error || res.code !== 200) return;
          this.tableData = res.data.sort((a, b) => a.sort - b.sort).map(item => ({ ...item, switch: false }));
      });

    if (this.comeFormArtific) {
      this.openServiceObject(false);
    }
  },
  methods: {
    queryNoRelSituationListByDireOid () {
      queryNoRelSituationListByDireOid({ comboDirectoryOid: this.comboDirectoryOid })
        .then(({ code, data }) => {
          this.loading = false;
          if (code !== 200) return this.$message.warning('查询情形信息失败');
          // 过滤掉标题不显示的
          this.comboSituationList = (data || []).filter(item => item.titleShowStyle === 1);
          // 初始情形oids
          this.situationOids = this.comboSituationList
            .map(item => item.situationOid)
            .filter(item => item)
            .join(',');
        })
        .catch(() => {
          this.loading = false;
        });
    },

    /** 获取当前所有的选项值 提供给`component:situationItem`使用 */
    getAllSelectValues () {
      try {
        let selectValOids = [];
        this.$refs.situation.forEach(child => {
          const _selectValOids = child.getAllSelectValues();
          selectValOids = selectValOids.concat(_selectValOids);
        });
        // 转字符串
        selectValOids = handleArr2String([...new Set([...selectValOids])]);

        // console.log('%c [selectValOids]:', 'color:red;font-weight:700;', selectValOids);

        return selectValOids;
      } catch (error) {
        console.log(error);
        return '';
      }
    },

    /** 获取当前所有的情形oid 提供给`component:situationItem`使用 */
    getAllSituationOids () {
      try {
        let situationOids = [];
        this.$refs.situation.forEach(child => {
          const _situationOids = child.getAllSituaionOids();
          situationOids = situationOids.concat(_situationOids);
        });
        // 转字符串
        situationOids = handleArr2String([...new Set([...situationOids])]);

        // console.log('%c [selectValOids]:', 'color:red;font-weight:700;', selectValOids);

        return situationOids;
      } catch (error) {
        console.log(error);
        return '';
      }
    },

    getAllSituations () {
      try {
        // 获取文本内容, 情形oids, 选项值, 是否必选集合
        let inputArr = [], situationArr = [], selectValOids = [], mustStatusNoCheckArr = [];
        this.$refs.situation?.forEach(child => {
          const [_inputArr, _situationArr, _selectValOids, _mustStatusNoCheckArr] = child.getValues();
          inputArr = inputArr.concat(_inputArr);
          situationArr = situationArr.concat(_situationArr);
          selectValOids = selectValOids.concat(_selectValOids);
          mustStatusNoCheckArr = mustStatusNoCheckArr.concat(_mustStatusNoCheckArr);
        });
        // 转字符串
        situationArr = handleArr2String([...new Set([...situationArr])]);
        selectValOids = handleArr2String([...new Set([...selectValOids])]);

        // console.log('%c [inputArr]:', 'color:red;font-weight:700;', inputArr);
        // console.log('%c [situationArr]:', 'color:red;font-weight:700;', situationArr);
        // console.log('%c [selectValOids]:', 'color:red;font-weight:700;', selectValOids);
        // console.log('%c [mustStatusNoCheckArr]:', 'color:red;font-weight:700;', mustStatusNoCheckArr);

        return [inputArr, situationArr, selectValOids, mustStatusNoCheckArr];
      } catch (error) {
        console.log(error);
        return [];
      }
    },

    /** 处理`component:situationItem`情形选项 */
    handleSituationItem (childSituation) {
      try {
        this.$refs.situation.forEach(child => {
          child.handleSituationItem(childSituation);
        });
      } catch (error) {
        console.log(error);
      }
    },

    async beforeNextStep () {
      try {

        // if (this.comboSituationList.length === 0) {
        //   return this.visible = true;
        // }

        const [inputArr, situationArr, selectValOids, mustStatusNoCheckArr] = this.getAllSituations();

        // 判断必选是否全部选中
        if (mustStatusNoCheckArr?.length > 0) {
          return this.$message.warning(`【${mustStatusNoCheckArr[0].situationName}】为必选或必填情形`);
        }

        // 判断是否修改过情形选项
        if (
          JSON.stringify(this.situation?.textVals) === JSON.stringify(inputArr)
          &&
          JSON.stringify(this.situation?.valOids) === JSON.stringify(selectValOids)
        ) {
          console.log('没有修改过情形选项');
          this.$emit('setIsQlCaseChanged', false);

          if(this.tableData.length>0){
            return this.visible = true;
          }else{
            return this.$emit("nextStep", 2);
          }

        }

        // 判断是否存在阻塞情形
        if (await this.handleBlockSituation(selectValOids)) {
          // 缓存情形信息
          this.$emit('setSituation', {
            textVals: inputArr,
            situationOids: situationArr,
            valOids: selectValOids,
          });

          this.$emit('setIsQlCaseChanged', true);
          if(this.tableData.length>0){
            return this.visible = true;
          }else{
            return this.$emit("nextStep", 2);
          }
        }
      } catch (error) {
        console.log(error);
      }
    },

    nextStep () {
      let valid = this.$refs.verfication.validate();
      if (valid) {
        return this.$message.warning(`核验名称【${valid.precheckName}】未通过核验, 请点击核验!`);
      }
      this.visible = false;
      return this.$emit("nextStep", 2);
    },

    /* 判断是否有阻塞情形 */
    async handleBlockSituation (selectValOids = '') {
      this.loading = true;
      try {
        const { code, data } = await judgeStopCondition({ comboDirectoryOid: this.comboDirectoryOid, selectValOids });
        this.loading = false;
        if (code !== 200) {
          this.$message.warning('查询阻塞情形失败');
          return false;
        }

        if (data) {
          this.$message.warning(data);
          return false;
        }

        return true;
      } catch (error) {
        this.loading = false;
        this.$message.warning('查询阻塞情形失败');
        return false;
      }
    },

    openServiceObject (isTip = true) {
      const [isPerson, isLegal] = this.serviceRoot;
      if (isPerson && isLegal) {
        this.visibleDialog = true;
      } else {
        isTip && this.$message.warning(`该事项仅支持${isPerson ? '个人' : '法人'}办理`);
      }
    },
  }
}
</script>

<style lang="scss">
.onething-intelligent-qa {
  position: relative;
  .intelligent-qa-service {
    position: absolute;
    right: 0;
    top: 0;
    width: 210px;
    height: 48px;
    background: #e1ebff;
    border-radius: 3px 3px 3px 24px;
    font-size: 14px;
    font-family: PingFang SC;
    font-weight: 500;
    color: #35528a;
    text-align: center;
    line-height: 48px;
    cursor: pointer;
    z-index: 1;
  }
  .situation-box--bottom-border {
    border-bottom: 1px dashed #cccccc;
  }

  .situation-social-code {
    display: flex;
    align-items: center;
    padding: 0 0 20px 20px;

    .el-input {
      flex: 1;
      margin-left: 20px;

      &__inner {
        width: 360px;
      }
    }
  }

  .situation-box {
    padding: 30px 0;

    &:first-child {
      padding-top: unset;
    }
  }

  .no-padding-bottom {
    padding-bottom: unset;
  }
}
</style>
