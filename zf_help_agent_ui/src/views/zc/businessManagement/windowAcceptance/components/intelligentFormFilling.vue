<!-- 智能填表 -->
<template>
  <div class="intelligent-form-filling" v-loading="getFormState">
    <el-scrollbar class="common-dialog-content">
      <el-tabs v-model.trim="activeName" @tab-click="handleTabClick">
        <el-tab-pane label="办件申请基础信息" name="1">
          <BaseForm
            :cegisterType="cegisterType"
            :fillUserInfo="fillUserInfo"
            ref="ruleForm"
            :caseOid="caseOid"
            :serviceOid="serviceOid"
            :rqbzDueDate="rqbzDueDate"
            :lastComponentId="$attrs.lastComponentId"
            @setFormField="handleFormField"
          />
        </el-tab-pane>
        <el-tab-pane
          v-for="item in reportFormList"
          :key="item.id"
          :label="item.formName"
          :name="item.name"
        >
          <div class="el-table__header-wrapper">
            <!-- <div class="common-dialog--title" style="margin-bottom: 10px">
              {{ item.formName }}
            </div> -->
            <FormReport
              v-if="item.reportForm.designOid"
              :ref="`reportForm_${item.designOid}`"
              :key="item.designOid"
              :reportForm="item.reportForm"
              :authorizeKey="item.authorizeKey"
              :formNames="item.formNames"
              :isZc="isZc"
              :isOneLevelBindFormData="true"
              @sendRes="item.sendRes"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-scrollbar>
    <div class="common-dialog-footer">
      <el-button type="primary" @click="$emit('lastStep', 2)">上一步</el-button>
      <el-button
        style="background: rgb(207 147 12); color: #fff"
        @click="saveApplyCaseForm(true)"
      >
        暂存
      </el-button>
      <el-button type="primary" @click="nextStep">下一步</el-button>
    </div>
    <ZfLoading v-if="zfloading" :text="zftext" />
  </div>
</template>

<script>
import IntelligentFormFilling from "@/mixins/intelligentFormFilling";
import fileView from '@/views/common/fileView';
import { queryDesignFormList } from "@/api/zc/sxService/serviceFormConfig/formDesign";
//import { selectBySxSerFormByServiceOid } from "@/api/zc/businessManagement/windowAcceptance";
import { getElectronicSealInfo,getElectronicSealInfo2} from "@/api/zc/businessManagement/windowAcceptance"
// 表单组件
import BaseForm from "./form/base-form.vue";
export default {
  mixins: [IntelligentFormFilling],
  components: { BaseForm,fileView },
  props:{
    fillUserInfo:{
      type:Object,
      default:()=>({})
    },
    //  ["loginUser"]
  },
    data(){
    return{
        printObj: {
        id: 'print',
        popTitle: '电子材料印章',
        extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>'
      },
      previewImageUrl: '/case-api/pic/previewImage?fastdfsNginxUrl=',
      //
      valOids: 'ed167cbb3ab84ba4bdb950ce57a20bc0,4bcd1f74caf1401d9dc41b30bdf17d94,3a46f642616a42948ccbd282950dc1eb,dab61ea64c384585bf4764138c4b72db,06b72dceebfa42b29a4aa1382de5a8e4',
    }
  },

  activated() {
    // this.ElectronicSealInfo()
    this.selectBySxSerFormByServiceOid();
  },
  methods: {
    // 初始获取表单信息
    async selectBySxSerFormByServiceOid() {
      this.formLoading = true
      this.reportFormList = []
      this.getFormState = true;
      try {
        const { data, code, message } = await queryDesignFormList(
          this.serviceOid,
          this.caseOid,
          this.valOids
        );
        this.DesingnFromList = data
        // console.log('this.DesingnFromList',this.DesingnFromList)
        if (code !== 200) {
          this.getFormState = false;reportForm
          return message && this.$message.warning(message);
        }
        this.reportFormList =
          data?.map?.((item, idx) => {
            return {
              ...item,
              childFormName: (item?.childFormName?.split(",") ?? []).filter(
                (item) => item
              ),
              name: String(idx + 2),
              elecFormTimer: null,
              reportForm: {
                designOid: item.designOid,
                authorizeKey: item.authorizeKey,
                formCode: item.formCode,
                reportOid: this.caseOid,
                formData: JSON.stringify(item.formVals)
              },
              // 保存表单后返回的接口数据
              sendRes: (ret) => {
                return JSON.parse(ret);
              },
            };
          }) ?? [];
        this.getFormState = false;
      } catch (error) {
        this.getFormState = false;
      }
      console.log("11111111111",this.reportFormList)
    },

    // handleClose(view){
    //   Vue.set(view,show,false)
    // }
  },
};
</script>

<style scoped lang="scss">
@import "./style/elec-form.scss";
// .dialog-table {
//   padding: unset;
// }

.custom-table td {
  padding: 0 10px !important;
}

>>> .el-dialog.hardware-scan {
  height: 714px;
  .el-dialog__body {
    height: calc(100% - 56px);
    padding: 26px 24px 0px 24px;
  }
}

>>> .el-form-item {
  padding: 20px 0;
}

>>> .el-select {
  width: 100%;
}

// 自定义表单
>>> .el-form{
  .el-row{
    .el-col{
      padding: 10px 0 !important;
    }
  }
}
>>> .el-table {
  &__header {
    thead {
      tr th {
        width: 100%;
        padding: 0;
        background: #fff;
        color: #121b2f;
        font-size: 14px;
        font-weight: 700;
        &:not(:last-child) {
          border-right: 1px solid #e0e6f0;
        }
      }
    }
  }

  &__row {
    td {
      width: 100%;
      background-color: #fff !important;
      text-align: center !important;

      .el-form-item {
        padding: unset;
      }

      &:first-child {
        border-left: unset;
      }
    }
  }
}

</style>
