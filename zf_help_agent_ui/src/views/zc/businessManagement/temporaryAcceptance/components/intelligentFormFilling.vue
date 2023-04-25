<!-- 智能填表 -->
<template>
  <div class="intelligent-form-filling">
    <el-scrollbar class="common-dialog-content">
      <el-tabs v-model.trim="activeName" @tab-click="handleTabClick">
        <el-tab-pane label="办件申请基础信息" name="1">
          <BaseForm
            ref="ruleForm"
            :cegisterType="cegisterType"
            :caseOid="caseOid"
            :serviceOid="serviceOid"
            :rqbzDueDate="rqbzDueDate"
            onlyMounted
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
            <FormReport
              v-if="item.reportForm.designOid"
              :ref="`reportForm_${item.designOid}`"
              :key="item.designOid"
              :reportForm="item.reportForm"
              :authorizeKey="item.authorizeKey"
              :isShowDefaultVal="false"
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
    <el-dialog
      v-dialog-drag
      :title="title"
      :visible.sync="openView"
      :close-on-click-modal="false"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <el-form :model="form">
        <!--            <el-row>
              <el-form-item label="扫码签收" prop="caseNumber">
                <el-input v-model.trim="queryParams.caseNumber" placeholder="请输入办件编号" clearable size="20" @keyup.enter.native="handleQuery" />
              </el-form-item>

              <el-form-item class="fr mr0">
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQueryCase">搜索</el-button>
              </el-form-item>
            </el-row>-->
        <el-table v-loading="loading" :data="ElectronicSealList" border>
          <el-table-column label="序号" width="70" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="材料名称"
            align="center"
            prop="templateName"
            show-overflow-tooltip
          />
          <el-table-column
            label="状态"
            align="center"
            prop="isSeal"
            show-overflow-tooltip
            width="180"
          />
          <!-- <el-table-column label="证照样本" align="center" prop="sampleName" show-overflow-tooltip/> -->
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
            width="280"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                @click="preview(scope.row, scope.$index)"
                >预览/打印</el-button
              >
              <el-button
                size="mini"
                type="primary"
                round
                style="color: white; width: 80px"
                @click="ElectronicSeal(scope.row, scope.$index)"
                >电子签章</el-button
              >
              <el-button
                size="mini"
                type="primary"
                round
                style="color: white; width: 80px"
                @click="getElectronicSealStatu(scope.row, scope.$index)"
                >查看状态</el-button
              >
              <!-- <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" v-if="scope.row.signRecord !=null"  @click="viewRecord(scope.row)" v-hasPermi="['sys:licenseIssued:view']">记录</el-button> -->
            </template>
          </el-table-column>
        </el-table>
      </el-form>

      <div slot="footer" class="zf-text-center">
        <el-button @click="openView = false">取消</el-button>
        <el-button type="primary" @click="IFsaveApplyCaseForm()"
          >确认</el-button
        >
      </div>
    </el-dialog>
    <!-- 电子签章文件预览弹窗 -->
    <el-dialog
      id="print"
      v-dialog-drag
      title="文件预览"
      :visible.sync="view.show"
      v-for="view in viewDialogOptions"
      :key="view.attaOid"
      @close="closeFileViewNew"
      custom-class="preview-dialog"
      width="1158px"
      height="800px"
      :scrollbar="view.ext !== 'pdf'"
      append-to-body
    >
      <iframe
        id="print"
        :src="ElectronicSealList[view.idx].attaUrl"
        scrolling="no"
        frameborder="0"
        style="width: 100%; height: 800px"
      ></iframe>
      <!-- <sx-file-view :attaOid="view.attaOid" @father-click="closeFileViewNew" /> -->
      <div style="text-align: center" slot="footer">
        <el-button type="primary" id="trueBtn" v-print="printObj">
          打印
        </el-button>
        <el-button @click="handleClose">关闭</el-button>
      </div>
    </el-dialog>
    <el-dialog
      v-dialog-drag
      title="电子材料签章"
      :visible.sync="eleShow"
      @close="closeFileViewNew"
      custom-class="preview-dialog"
      width="600px"
      height="400px"
      append-to-body
    >
      <div style="width: 214px; margin-left: 193px; margin-bottom: 20px">
        <img :src="imgUrl" alt="二维码" />
      </div>
      <div style="text-align: center">
        <strong
          >请用手机浏览器、蒙速办app、支付宝、微信、QQ扫描上方二维码进行签章。</strong
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import IntelligentFormFilling from "@/mixins/intelligentFormFilling";
// 表单组件
import BaseForm from "./form/base-form.vue";
import { queryDesignFormList } from "@/api/zc/sxService/serviceFormConfig/formDesign";
//import { selectBySxSerFormByServiceOid } from "@/api/zc/businessManagement/windowAcceptance";
import {
  getSxService,
  getSituationMaterialListByOids,
  getElectronicSealInfo,
} from "@/api/zc/businessManagement/windowAcceptance";
import { getCaseTitleValueList } from "@/api/zc/businessManagement/temporaryAcceptance";
import { getAllQlCaseMaterialListByAttaOid } from "@/api/materialCategory.js";

export default {
  mixins: [IntelligentFormFilling],
  components: { BaseForm },
  data() {
    return {
      printObj: {
        id: "print",
        popTitle: "电子材料印章",
        extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>',
      },
      DesingnFromList: [],
    };
  },
  created() {
    this.getServiceDetail();

    this.selectBySxSerFormByServiceOid();

    this.getQlCaseSituationList();

    this.getAttaList();
    this.ElectronicSealInfo();
  },
  methods: {
    //获取电子签章弹窗信息
    async ElectronicSealInfo() {
      const { data, code, message } = await getElectronicSealInfo(
        this.serviceOid,
        this.caseOid
      );
      console.log("初始化data", data);
      // this.serviceOid
      if (code !== 200) {
        // this.getFormState = false;
        return message && this.$message.warning(message);
      }
      this.ElectronicSealList = [];
      data?.map?.((item, idx) => {
        this.ElectronicSealList.push(Object.assign(item, { isSeal: "未签章" }));
      }) ?? [];
      // this.getFormState = false;
    },
    // 初始获取表单信息
    async selectBySxSerFormByServiceOid() {
      this.getFormState = true;
      try {
        const { data, code, message } = await queryDesignFormList(
          this.serviceOid
        );
        this.DesingnFromList = data;
        console.log("this.DesingnFromList", this.DesingnFromList);
        if (code !== 200) {
          this.getFormState = false;
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
                reportOid: this.caseOid,
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
    },
    //电子签章弹窗预览按钮功能
    preview(row, index) {
      const { attaOid, addr } = row;
      if (true) {
        let item = {
          show: true,
          attaOid: attaOid,
          idx: index,
          // ext: addr?.split?.(".")?.splice?.(-1)?.[0],
        };
        // alert(item.attaOid)
        this.viewDialogOptions.push(item);
      } else {
        this.$message.warning("暂无材料查看！");
      }
    },
    /** -------------------------------------暂存受理提前请求接口-------------------------------------- */

    /** 获取事项详细信息 */
    getServiceDetail() {
      getSxService(this.serviceOid).then(({ code, data, message }) => {
        if (code !== 200)
          return this.$message.warning(message || "接口请求失败");
        var reg = new RegExp("null ", "g"); //创建正则RegExp对象
        const serviceForm = {
          serviceName: data.sxService.serviceName,
          serviceTypeName: data.sxService.serviceTypeName,
          organName: data.sxService.organName,
          sxServiceLocations: data.sxServiceLocations,
          zxDhText: data.sxService.zxDhText,
          tsDhText: data.sxService.tsDhText,
          sxServiceExtend: data?.sxServiceExtend ?? {},
          sxAcceptConditions: data.sxAcceptConditions,
          sxServiceLinks: data.sxServiceLinks,
          sxServiceQuestions: data.sxServiceQuestions,
          showRzhs: data.sxService.showRzhs,
          resultDeliveryWayName: data.sxService.resultDeliveryWayName.replace(
            reg,
            ""
          ),
        };
        this.$emit("setServiceFormData", {
          serviceName: serviceForm.serviceName,
          zxDhText: serviceForm.zxDhText,
          serviceTypeName: serviceForm.serviceTypeName,
          showRzhs: serviceForm.showRzhs,
          organName: serviceForm.organName,
        });
      });
    },

    /** 办件情形获取 */
    getQlCaseSituationList() {
      //查询办件情形情形
      getCaseTitleValueList(this.caseOid).then((response) => {
        //填充标题选项
        this.$emit("setSituationCheckList", response?.data || []);

        this.getMaterialList(response?.data || []);
      });
    },

    /** 点击选项值 选项有精细化材料展示精细化材料 */
    async getMaterialList(situationCheckList) {
      //根据被选中的选项获取关联材料
      const { data, code, message } = await getSituationMaterialListByOids(
        this.serviceOid,
        situationCheckList.map((item) => item.valueOid)
      );
      if (code !== 200) return message && this.$message.warning(message);
      this.$emit("setSxServiceMaterialList", data || []);
    },

    //材料附件
    getAttaList() {
      getAllQlCaseMaterialListByAttaOid(this.caseOid).then(({ code, data }) => {
        if (code !== 200 || !data)
          return this.$message.warning("查询材料附件失败");
        // 缓存所有的caseMaterialOid
        const caseMaterialOids = data.reduce((prev, cur) => {
          if (prev[cur.collectionType]) {
            prev[cur.collectionType] = `${prev[cur.collectionType]};${
              cur.caseMaterialOid
            }`;
          } else {
            prev[cur.collectionType] = cur.caseMaterialOid;
          }
          return prev;
        }, {});
        this.$emit("caseMaterialOids", caseMaterialOids);

        // 获取容缺补正材料名称
        const rqbzMaterialNames = data
          .filter((item) => item.collectionType == "4")
          .map((item) => item.materialName)
          .join(",");

        this.$emit("setRqbzMaterialNames", rqbzMaterialNames);
      });
    },
  },
};
</script>

<style scoped lang="scss">
@import "@/views/zc/businessManagement/windowAcceptance/components/style/elec-form.scss";
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
>>> .el-table {
  &__header {
    thead {
      tr th {
        padding: 0;
        background: #edf0f5;
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
