<template>
<div>
  <el-tabs v-model="activeName" style="overflow: hidden">
    <el-tab-pane label="基本信息" name="first">
      <!--事项信息-->
      <div class="zf-zc-table--title" style="margin-top: 0">事项相关信息</div>

      <table v-if="form.info.sxService && form.info.sxServiceExtend" cellspacing="0" cellpadding="0" border="0"
        class="zf-zc-table">
        <colgroup>
          <col width="14%" />
          <col width="19%" />
          <col width="14%" />
          <col width="19%" />
          <col width="14%" />
          <col width="20%" />
        </colgroup>
        <tr>
          <td><b>事项名称：</b></td>
          <td colspan="3">{{ form.info.sxService.serviceName }}</td>
          <td><b>事项类型：</b></td>
          <td>{{ form.info.sxService.serviceTypeName }}</td>
        </tr>
        <tr>
          <!--          <td><b>基本编码：</b></td>
          <td>{{ form.info.sxService.basicCode }}</td>-->
          <td><b>实施编码：</b></td>
          <td colspan="3">{{ form.info.sxService.implementCode }}</td>
          <td><b>事项状态：</b></td>
          <td>{{ form.info.sxService.serviceStatusName }}</td>
        </tr>
        <tr>
          <td><b>实施机构：</b></td>
          <td>{{ form.info.sxService.organName }}</td>
          <td><b>承诺时限：</b></td>
          <td>
            {{ form.info.sxServiceExtend.promiseLimit }}
            <span v-if="form.info.sxServiceExtend.promiseLimitType == 'W'">
              (工作日)
            </span>
            <span v-else> (自然日) </span>
          </td>
          <td><b>法定时限：</b></td>
          <td>
            {{ form.info.sxServiceExtend.legalLimit }}
            <span v-if="form.info.sxServiceExtend.legalLimitType == 'W'">
              (工作日)
            </span>
            <span v-else> (自然日) </span>
          </td>
        </tr>
        <tr>
          <td><b>行使层级：</b></td>
          <td>{{ form.info.sxService.levelName }}</td>
          <td><b>服务对象：</b></td>
          <td>{{ form.info.sxService.serviceObjectName }}</td>
          <td><b>办理形式：</b></td>
          <td>{{ form.info.sxService.handleFormName }}</td>
        </tr>
        <tr>
          <td><b>办理类型：</b></td>
          <td colspan="5">{{ form.info.sxService.handleTypeName }}</td>
        </tr>
        <tr>
          <td><b>实施主体性质：</b></td>
          <td colspan="3">{{ form.info.sxService.implementOrganPropertyName }}</td>
          <td><b>办件类型：</b></td>
          <td>{{ form.info.sxService.caseTypeName }}</td>
        </tr>
        <tr>
          <td><b>是否收费：</b></td>
          <td>{{ form.info.sxService.chargeFlagName }}</td>
          <td><b>收费环节：</b></td>
          <td>{{ form.info.sxService.chargeLinkName }}</td>
          <td><b>到现场次数：</b></td>
          <td>{{ form.info.sxService.countToScence }}</td>
        </tr>
        <tr v-if="form.info.sxService.chargeFlag == 1">
          <td><b>收费标准：</b></td>
          <td colspan="5">{{ form.info.sxServiceExtend.chargeStandard }}</td>
        </tr>
        <tr v-if="form.info.sxService.chargeFlag == 1">
          <td><b>收费依据：</b></td>
          <td colspan="5">{{ form.info.sxServiceExtend.chargeAccord }}</td>
        </tr>
        <tr>
          <td><b>是否支持网上支付：</b></td>
          <td>{{ form.info.sxService.onlinePayFlagName }}</td>
          <td><b>是否支持在线预约：</b></td>
          <td>{{ form.info.sxService.appointmentFlagName }}</td>
          <td><b>是否支持物流快递：</b></td>
          <td>{{ form.info.sxService.expressFlagName }}</td>
        </tr>
        <tr>
          <td><b>是否联合办理：</b></td>
          <td>{{ form.info.sxService.unionOrganFlagName }}</td>
          <td><b>联合办理机构：</b></td>
          <td>{{ form.info.sxService.unionOrgan }}</td>
          <td><b>是否支持人证核身标识：</b></td>
          <td>{{ form.info.sxService.showRzhsName }}</td>
        </tr>
        <tr>
          <td><b>行政管辖地：</b></td>
          <td>{{ form.info.sxService.adminJurisdictionName }}</td>
          <td><b>主题分类：</b></td>
          <td colspan="3">
            {{ form.info.sxService.subjectClassificationName }}
          </td>
        </tr>
        <tr>
          <td><b>事项性质：</b></td>
          <td>{{ form.info.sxService.serviceCharacterName }}</td>
          <td><b>生命周期分类：</b></td>
          <td>{{ form.info.sxService.lifeCycleClassificationName }}</td>
          <td><b>办事群体分类：</b></td>
          <td>{{ form.info.sxService.serviceGroupClassificationName }}</td>
        </tr>
        <tr>
          <td><b>公开方式：</b></td>
          <td>{{ form.info.sxService.openWayName }}</td>
          <td><b>公开渠道：</b></td>
          <td>{{ form.info.sxService.openChannelName }}</td>
          <td><b>办理结果送达方式：</b></td>
          <td>{{ form.info.sxService.resultDeliveryWayName }}</td>
        </tr>
        <tr>
          <td><b>公开内容：</b></td>
          <td colspan="5">{{ form.info.sxService.openContent }}</td>
        </tr>
        <tr>
          <td><b>公开文书内容：</b></td>
          <td colspan="5">{{ form.info.sxService.openDocumentContent }}</td>
        </tr>
        <tr>
          <!--          <td><b>所属目录：</b></td>
          <td>{{ form.info.sxService.directoryName }}</td>-->
          <td><b>是否支持终端办理：</b></td>
          <td>{{ form.info.sxService.terminalProcessingName }}</td>
          <td><b>事项来源：</b></td>
          <td>{{ form.info.sxService.originName }}</td>
          <td><b>所属是否有中介服务事项：</b></td>
          <td>{{ form.info.sxService.isZjfwName }}</td>
        </tr>
        <tr>
          <td><b>必须到现场办理原因说明：</b></td>
          <td colspan="5">{{ form.info.sxService.reasonToScence }}</td>
        </tr>
        <tr>
          <td><b>移动端是否对接单点登录：</b></td>
          <td>{{ form.info.sxService.appIssingleLoginName }}</td>
          <td><b>移动端办理地址：</b></td>
          <td colspan="3">{{ form.info.sxService.mobileTerminalUrl }}</td>
        </tr>
        <tr>
          <td><b>计算机端是否对接单点登录：</b></td>
          <td>{{ form.info.sxService.isSingleLoginName }}</td>
          <td><b>计算机端在线办理跳转地址：</b></td>
          <td>{{ form.info.sxService.linkAddr }}</td>
          <td><b>是否存在子项：</b></td>
          <td>{{ form.info.sxService.existChildItemName }}</td>
        </tr>
        <tr>
          <td><b>网站或系统名称：</b></td>
          <td colspan="3">{{ form.info.sxService.webName }}</td>
          <td><b>网上申请链接：</b></td>
          <td>{{ form.info.sxService.onlineApplyLink }}</td>
        </tr>
        <tr>
          <td><b>设定依据：</b></td>
          <td colspan="5">{{ form.info.sxServiceExtend.setAccord }}</td>
        </tr>
        <tr>
          <td><b>行使内容：</b></td>
          <td colspan="5">
            {{ form.info.sxServiceExtend.exerciseContent }}
          </td>
        </tr>
        <tr>
          <td><b>承诺期限说明：</b></td>
          <td colspan="5">{{ form.info.sxService.promiseTimeDesc }}</td>
        </tr>
        <tr>
          <td><b>权限划分标准：</b></td>
          <td colspan="5">{{ form.info.sxServiceExtend.dividStandard }}</td>
        </tr>
        <tr>
          <td><b>数量限制说明：</b></td>
          <td colspan="5">
            {{ form.info.sxServiceExtend.numlimitContent }}
          </td>
        </tr>
        <tr>
          <td><b>数量限制依据：</b></td>
          <td colspan="5">{{ form.info.sxServiceExtend.numlimitDesc }}</td>
        </tr>
        <tr>
          <td><b>年审年检：</b></td>
          <td colspan="5">{{ form.info.sxServiceExtend.auditChannel }}</td>
        </tr>
      </table>
    </el-tab-pane>
    <!-- <el-tab-pane label="收费信息" name="second">
      <el-table :data="form.chargeList" border>
        <el-table-column label="序号" align="center" min-width="6%">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column
          label="收费项名称"
          align="center"
          min-width="20%"
          prop="chargeName"
          show-overflow-tooltip
        />
        <el-table-column
          label="收费标准"
          align="center"
          min-width="50%"
          prop="chargeStandard"
          show-overflow-tooltip
        />
        <el-table-column
          label="是否允许减免"
          align="center"
          min-width="10%"
          prop="allowableRelief"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span v-if="scope.row.allowableRelief == 0">否</span>
            <span v-if="scope.row.allowableRelief == 1">是</span>
          </template>
        </el-table-column>
        <el-table-column
          label="是否必须"
          align="center"
          min-width="10%"
          prop="mustChargeFlag"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span v-if="scope.row.mustChargeFlag == 0">否</span>
            <span v-if="scope.row.mustChargeFlag == 1">是</span>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane> -->
    <el-tab-pane label="收取材料" name="third">
      <el-table :data="form.material" border>
        <el-table-column label="序号" align="center" min-width="6%">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="材料名称" align="center" min-width="30%" prop="materialName" show-overflow-tooltip />
        <el-table-column label="材料类型" align="center" min-width="10%" prop="materialType">
          <template slot-scope="scope">
            <p v-if="scope.row.materialType === 0">原件</p>
            <p v-if="scope.row.materialType === 1">复印件</p>
            <p v-if="scope.row.materialType === 2">原件和复印件</p>
          </template>
        </el-table-column>
        <el-table-column label="材料形式" align="center" min-width="20%" prop="materialFormat">
          <template slot-scope="scope">
            <p v-if="scope.row.materialFormat === 1">纸质</p>
            <p v-if="scope.row.materialFormat === 2">电子版</p>
            <p v-if="scope.row.materialFormat == 3">证照</p>
            <p v-if="scope.row.materialFormat == 4">容缺补正</p>
            <p v-if="scope.row.materialFormat == 7">告知承诺</p>
          </template>
        </el-table-column>
        <el-table-column label="审查要点" align="center" min-width="20%" prop="examinePoint" show-overflow-tooltip />
        <el-table-column label="收取数量" align="center" min-width="15%" prop="paperNumber" />
        <!--<el-table-column label="收取方式" align="center" min-width="15%" prop="collectionType">
            <template slot-scope="scope">
              <p  v-if="scope.row.collectionType===1">纸质收取</p>
              <p v-if="scope.row.collectionType===2">附件上传</p>
              <p  v-if="scope.row.collectionType===3">扫描</p>
              <p v-if="scope.row.collectionType===4">容缺后补</p>
            </template>
          </el-table-column>-->
      </el-table>
    </el-tab-pane>
    <el-tab-pane label="前置核验" name="fourth">
      <el-table :data="tableData">
        <el-table-column label="核验名称(必填)" align="center" prop="precheckName" show-overflow-tooltip></el-table-column>
        <el-table-column label="核验地址(必填)" align="center" prop="precheckAddress" show-overflow-tooltip></el-table-column>
        <el-table-column label="核验排序" width="100" align="center" prop="sort" show-overflow-tooltip></el-table-column>
        <el-table-column label="核验参考附件" align="center" prop="attaName">
          <template slot-scope="scope">
            <template v-if="scope.row.attaOid" >
                <el-link type="primary" @click="viewFile(scope.row)">{{ scope.row.attaName }}</el-link>
            </template>
              <template v-else>无</template>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
  </el-tabs>
  <el-dialog title="预览附件" :visible.sync="openInit" :close-on-click-modal="false" append-to-body>
      <el-scrollbar style="height:calc(100vh - 330px)">
        <iframe :src="fileUrl" frameborder="0" style="height:calc(100vh - 330px);width:100%"></iframe>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>
<script>
  import {
    getSxServiceOne
  } from "@/api/zc/businessManagement/sxServiceRegistrar";
  import {
    querySxServicePrecheckList
  } from "@/api/zc/qdgl/sxService";
  export default {
    name: 'viewSxServiceInfo',
    //定义获取父页面传过来的值的格式
    props: ["sxServiceOid"],
    data() {
      return {
        // 遮罩层
        loading: true,
        activeName: 'first',
        caseNumber: "",
        caseOid: "",
        // 表单参数
        form: {
          info: {},
          material: [],
          chargeList: []
        },
        serviceDialogOptions: [],
        serviceName: "",
        tableData: [],
        openInit:false,
        fileUrl:''
      };
    },
    //获取父页面的值
    mounted() {
      this.serviceOid = this.sxServiceOid;
      this.getSxServiceOne();
      this.querySxServicePrecheckList();
    },

    methods: {
      //查询事项信息
      getSxServiceOne() {
        getSxServiceOne(this.serviceOid).then(response => {
          this.form.info = {
            ...response.data,
            sxServiceExtend: response.data ?.sxServiceExtend ?? {}
          }
          this.form.material = response.data ?.sxServiceMaterials || [];
          this.form.chargeList = response.data ?.sxServiceCharges ?? [];
        })
      },
      querySxServicePrecheckList() {
        this.$getResponse(querySxServicePrecheckList({
          serviceOid: this.serviceOid,
        }), (error, res) => {
          if (error || res.code !== 200) return;
          this.tableData = res.data || [];
        })
      },
      viewFile(item) {
        this.fileUrl = item.attaUrl;
        this.openInit = true;
      },
    }
  };

</script>
<style lang="scss" scoped>
  .el-scrollbar__wrap {
    overflow: hidden;
  }

  .dialog-table {
    padding: 5px;
  }

</style>
