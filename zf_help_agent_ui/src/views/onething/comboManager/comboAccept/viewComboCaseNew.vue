<template>
  <el-tabs v-model="activeName" @tab-click="handleClick" style="overflow: hidden">
    <el-tab-pane label="基本信息" name="info">
      <!--事项信息-->
      <div class="zf-zc-table--title">一件事目录相关信息</div>
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td>
            <b>一件事目录名称：</b>
          </td>
          <td>
            {{ form.comboDirectory.comboDirectoryName }}
          </td>
          <td>
            <b>一件事目录编号：</b>
          </td>
          <td>
            {{ form.comboDirectory.comboDirectoryCode }}
          </td>
        </tr>
        <tr>
          <td>
            <b>所属区划：</b>
          </td>
          <td>
            {{ form.comboDirectory.districtName }}
          </td>
          <td>
            <b>所属一件事分类：</b>
          </td>
          <td>
            {{ form.comboDirectory.themeName }}
          </td>
        </tr>
        <tr>
          <td>
            <b>承诺时限(工作日)：</b>
          </td>
          <td>
            {{ form.comboDirectory.promiseLimit }}
          </td>
          <td>
            <b>法定时限(工作日)：</b>
          </td>
          <td>
            {{ form.comboDirectory.legalLimit }}
          </td>
        </tr>
      </table>

      <div class="zf-zc-table--title">申请人/申请单位相关信息</div>
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td><b>办件编号：</b></td>
          <td>
            {{ form.info.caseNumber }}
          </td>
          <td>
            <b v-if="form.info.applyUserType == 0">申请人名称：</b><b v-else>申请单位名称：</b>
          </td>
          <td>{{ form.info.applyUserName }}</td>
        </tr>
        <tr>
          <td><b>证件号码：</b></td>
          <td>{{ form.info.credentialNumber }}</td>
          <td>
            <b v-if="form.info.applyUserType == 0">申请人邮政编码：</b><b v-else>申请单位邮政编码：</b>
          </td>
          <td>{{ form.info.applyPostCode }}</td>
        </tr>
        <tr>
          <td>
            <b v-if="form.info.applyUserType == 0">申请人手机：</b><b v-else>申请单位手机：</b>
          </td>
          <td>{{ form.info.applyUserPhone }}</td>
          <td>
            <b v-if="form.info.applyUserType == 0">申请人电话：</b><b v-else>申请单位电话：</b>
          </td>
          <td>{{ form.info.applyUserTel }}</td>
        </tr>
        <tr>
          <td><b>通讯地址：</b></td>
          <td colspan="5">{{ form.info.applyUserAddress }}</td>
        </tr>
        <tr>
          <td><b>申请项目名称：</b></td>
          <td colspan="5">{{ form.info.projectName }}</td>
        </tr>
        <tr>
          <td><b>所属业务管辖地：</b></td>
          <td>{{ form.info.bussVenueDistrictName }}</td>
          <td><b>受理具体地点：</b></td>
          <td>{{ form.info.specificLocation }}</td>
        </tr>
        <tr>
          <td><b>投资项目名称：</b></td>
          <td>{{ form.info.investProjecName }}</td>
          <td><b>投资项目编号：</b></td>
          <td>{{ form.info.investProjectCode }}</td>
        </tr>
        <tr>
          <td><b>摘要内容：</b></td>
          <td colspan="5">{{ form.info.projectAbstract }}</td>
        </tr>
      </table>

      <!--联系人信息-->
      <template v-if="form.info.proxyFlag === 1">
        <div class="zf-zc-table--title">联系人/代理人相关信息</div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>联系人/代理人名称：</b></td>
            <td>{{ form.info.contactUserName }}</td>
            <td><b>联系人/代理人身份证号码：</b></td>
            <td>{{ form.info.contactCredentialNumber }}</td>
          </tr>
          <tr>
            <td><b>手机号：</b></td>
            <td>{{ form.info.contactUserPhone }}</td>
            <td><b>固定电话：</b></td>
            <td>{{ form.info.contactUserTel }}</td>
          </tr>
          <tr>
            <td><b>电子邮箱：</b></td>
            <td>{{ form.info.contactEmail }}</td>
            <td><b>备注：</b></td>
            <td>{{ form.info.contactRemark }}</td>
          </tr>
        </table>
      </template>

      <!--收件相关信息-->
      <div class="zf-zc-table--title">收件相关信息</div>
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td><b>送达方式：</b></td>
          <td colspan="5">
            <span v-if="form.info.resultDeliveryWay == 1"> 快递送达</span>
            <span v-if="form.info.resultDeliveryWay == 2">自行取件</span>
            <span v-if="form.info.resultDeliveryWay == 3">其他</span>
          </td>
        </tr>
        <tr v-if="form.info.resultDeliveryWay == 1">
          <td><b>收件人姓名：</b></td>
          <td>{{ form.info.addresseeName }}</td>
          <td><b>收件人邮政编码：</b></td>
          <td>{{ form.info.addresseePostCode }}</td>
        </tr>
        <tr v-if="form.info.resultDeliveryWay == 1">
          <td><b>收件人手机：</b></td>
          <td>{{ form.info.addresseePhone }}</td>
          <td><b>收件人电话：</b></td>
          <td>{{ form.info.addresseeTel }}</td>
        </tr>
        <tr v-if="form.info.resultDeliveryWay == 1">
          <td><b>收件人地址：</b></td>
                  <td>{{form.info.addresseeAddress}}</td>
          <td><b>收件人详细地址：</b></td>
          <td colspan="3">{{ form.info.addresseeDetailAddress }}</td>
        </tr>
      </table>
    </el-tab-pane>
    <el-tab-pane label="情形信息" name="situation">
      <!--材料出库信息-->
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td><b>情形名称：</b></td>
          <td colspan="3">
            {{ situationName }}
          </td>
        </tr>
        <tr>
          <td><b>情形选项：</b></td>
          <td colspan="3">
            <div v-if="
                direSituationOptionTitles &&
                  direSituationOptionTitles.length > 0
              ">
              <div v-for="(data, index) in direSituationOptionTitles" :key="index">
                <div class="check-list">
                  <span>{{ data.pacSituationName }}【</span>
                  <span v-if="data.moreStatus != 3">
                    <span v-for="item in data.caseSituatResultList" :key="item.oid">
                      {{ item.optionName }}
                    </span>
                  </span>
                  <span v-else>
                    {{ data.txtContent }}
                  </span>
                  <span>】</span>
                </div>
              </div>
            </div>
            <div v-else>暂无数据</div>
          </td>
        </tr>
        <tr>
          <td><b>所需材料：</b></td>
          <td colspan="3">
            <!--            <div v-if="comboDireMaterials && comboDireMaterials.length > 0">
              <div v-for="(data, index) in comboDireMaterials" :key="index">
                <div class="check-list">
                  {{ index + 1 + "." + data.materialName }};
                </div>
              </div>
            </div>-->
            <div v-if="form.caseMaterials && form.caseMaterials.length > 0">
              <div v-for="(data, index) in form.caseMaterials" :key="index">
                <div class="check-list">
                  {{ index + 1 + "." + data.materialName }}
                </div>
              </div>
            </div>
            <div v-else>暂无数据</div>
          </td>
        </tr>
      </table>
    </el-tab-pane>
    <el-tab-pane label="电子表单" name="comboForm" v-if="serFormList.length > 0">
      <el-table :data="serFormList" border>
        <el-table-column label="序号" align="center" width="50">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="表单名称" align="center" :show-overflow-tooltip="true" prop="formName" />
        <el-table-column label="操作" align="center" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="viewCaseFromInfo(scope.row)">预览
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
    <el-tab-pane label="材料信息" name="caseMaterial">
      <el-table :data="form.caseMaterials" border>
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="材料名称" align="center" prop="materialName" show-overflow-tooltip />
        <el-table-column label="是否收取" align="center" width="100">
          <template slot-scope="scope">
            {{ scope.row.collectionFlag == 1 ? "是" : "否" }}
          </template>
        </el-table-column>
        <el-table-column label="收取方式" align="center" prop="collectionType">
          <template slot-scope="scope">
            <p v-if="scope.row.collectionType == 1">纸质收取</p>
            <p v-if="scope.row.collectionType == 2">附件上传</p>
            <p v-if="scope.row.collectionType == 3">扫描</p>
            <p v-if="scope.row.collectionType == 5">证照库</p>
            <p v-if="scope.row.collectionType == 4">容缺后补</p>
            <p v-if="scope.row.collectionType == 7">告知承诺</p>
          </template>
        </el-table-column>
        <el-table-column label="收取数量" align="center" width="100" prop="collectionNumber" />
        <el-table-column label="收取时间" align="center" prop="collectionDate" />
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" v-if="
                scope.row.collectionType == 5 && scope.row.collectionFlag == 1
              " @click="viewElemsInfo(scope.row.elemLicenseOid)">证照预览</el-button>
            <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" v-else-if="
                (scope.row.collectionType == 2 ||
                  scope.row.collectionType == 3) &&
                  scope.row.collectionFlag == 1
              " @click="handleViewAtta(scope.row)">附件查看</el-button>
            <el-button size="mini" type="text" v-else>无附件</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
    <el-tab-pane label="关联办件信息" name="qlCase">
      <el-table :data="form.qlCases" border>
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="办件编号" align="center" prop="caseNumber" :show-overflow-tooltip="true" />
        <el-table-column label="办件状态" align="center" prop="collectionFlag">
          <template slot-scope="scope">
            <p v-if="scope.row.caseStatus == 1">待预审</p>
            <p v-if="scope.row.caseStatus == 2">办理中</p>
            <p v-if="scope.row.caseStatus == 3">办结</p>
            <p v-if="scope.row.caseStatus == 5">异常办结</p>
            <p v-if="scope.row.caseStatus == -1">已作废</p>
          </template>
        </el-table-column>
        <el-table-column label="受理日期" align="center" prop="acceptanceDate" :show-overflow-tooltip="true" />
        <el-table-column label="应办结时间" align="center" prop="shouldConcludeDate" :show-overflow-tooltip="true" />
        <el-table-column label="办结时间" align="center" prop="concludeDate" :show-overflow-tooltip="true" />
        <el-table-column label="业务项名称" align="center" prop="serviceName" :show-overflow-tooltip="true" />
        <!-- <el-table-column
          label="事项类型"
          align="center"
          prop="serviceTypeName"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="项目名称"
          align="center"
          prop="projectName"
          :show-overflow-tooltip="true"
        /> -->
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleChildCaseInfo(scope.row)"
              v-hasPermi="['sys:done:view']">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
    <el-tab-pane label="材料出库信息" name="outFlag" v-if="outFlag">
      <!--材料出库信息-->
      <div v-for="(outInfo, idx) in outList" :key="idx">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>出库状态：</b></td>
            <td>
              <span v-if="outInfo.outStatus == 0">待出库</span>
              <span v-if="outInfo.outStatus == 1 ||outInfo.outStatus == 2">已出库</span>
              <!--              <span v-if="outInfo.outStatus == 2">已签收</span>-->
              <!--              <span v-if="outInfo.outStatus == 1">已出库</span>-->
            </td>
            <td><b>出库时间：</b></td>
            <td>{{ outInfo.outStockDate }}</td>
          </tr>
          <tr v-if="outInfo.outType">
            <td><b>出库人：</b></td>
            <td>{{ outInfo.outUserName }}</td>
            <td><b>出库方式：</b></td>
            <td>
              <span v-if="outInfo.outType == 1">递送员出库</span>
              <span v-if="outInfo.outType == 2">快递送达</span>
            </td>
          </tr>
          <tr v-if="outInfo.outType == 1">
            <td><b>领件人名称：</b></td>
            <td>{{ outInfo.receiverName }}</td>
            <td><b>领件人手机：</b></td>
            <td>{{ outInfo.receiverPhone }}</td>
          </tr>
          <tr v-if="outInfo.outType == 2">
            <td><b>快递公司：</b></td>
            <td>{{ outInfo.kdCompany }}</td>
            <td><b>快递编号：</b></td>
            <td>{{ outInfo.kdCode }}</td>
          </tr>
          <tr v-if="outInfo.outType == 2">
            <td><b>寄件人名称：</b></td>
            <td>{{ outInfo.senderUserName }}</td>
            <td><b>寄件人手机：</b></td>
            <td>{{ outInfo.senderUserPhone }}</td>
          </tr>
          <tr v-if="outInfo.idCard">
            <td><b>身份证号码：</b></td>
            <td colspan="3">{{ outInfo.idCard }}</td>
          </tr>
          <tr>
            <td><b>出库材料：</b></td>
            <td colspan="3">
              <span v-for="(data, index) in outInfo.comboMaterials" :key="index">
                {{ index + 1 }}、{{ data.materialName }}<br />
              </span>
            </td>
          </tr>
        </table>
        <el-table :data="outInfo.caseMaterialOutOfStockRecordList" border>
          <el-table-column label="序号" align="center" width="50">
            <template slot-scope="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>

          <el-table-column label="材料流转类型" align="center" :show-overflow-tooltip="true" prop="collectionFlag">
            <template slot-scope="scope">
              <p v-if="scope.row.materialsFlowType == '1'">打印材料流转单</p>
              <p v-if="scope.row.materialsFlowType == '2'">材料出库</p>
              <p v-if="scope.row.materialsFlowType == '3'">材料送达签收</p>
            </template>
          </el-table-column>


          <el-table-column label="流转时间" align="center" :show-overflow-tooltip="true" prop="createDate" />
          <el-table-column label="操作人" align="center" :show-overflow-tooltip="true" prop="receiverName" />
          <el-table-column label="操作人手机" align="center" :show-overflow-tooltip="true" prop="receiverPhone" />
        </el-table>


      </div>
    </el-tab-pane>
    <el-tab-pane v-if="resultFlag" label="证照签收信息" name="viewRecord">
      <el-table :data="resultList" border>
        <el-table-column label="序号" width="50" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="事项名称" :show-overflow-tooltip="true" align="center" prop="serviceName" />
        <el-table-column label="证照名称" :show-overflow-tooltip="true" align="center" prop="licenseName" />
        <el-table-column label="证照样本" :show-overflow-tooltip="true" align="center" prop="sampleName" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" v-if="scope.row.caseCombo != null"
              @click="viewRecord(scope.row)" v-hasPermi="['sys:licenseIssued:view']">记录</el-button>
            <el-button size="mini" type="text" v-else>未签收</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
    <!-- 关联办件详细信息 -->
    <el-dialog v-dialog-drag :visible.sync="childCaseOpenView" v-if="childCaseOpenView" @close="closeChildView"
      :title="title" width="1100px" height="700px" scrollbar append-to-body>
      <view-case-info-by-one-case :caseNumber="indexCaseNumber"></view-case-info-by-one-case>
      <div slot="footer" style="text-align: center">
        <el-button type="primary" @click="closeChildView">
          关闭
        </el-button>
      </div>
    </el-dialog>

    <!-- 材料附件详细信息 -->
    <el-dialog v-dialog-drag :visible.sync="attaView" v-if="attaView" @close="closeAttaView" title="附件查看" width="1100px"
      scrollbar height="700px" append-to-body>
      <el-table :data="attaList" border>
        <el-table-column label="序号" align="center" min-width="5%">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="材料名称" align="center" min-width="10%" prop="name" show-overflow-tooltip />
        <el-table-column label="原始名称" align="center" min-width="30%" prop="originName" show-overflow-tooltip />
        <el-table-column label="操作" align="center" min-width="10%">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="viewFile(scope.row.oid)">预览
            </el-button>
            <el-button size="mini" type="text" icon="iconfont zfsoft-xiazai" @click="downloadFile(scope.row.oid)">下载
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!--引入办件文件的预览弹出层-->
    <el-dialog v-dialog-drag title="文件预览" :visible.sync="showFile" v-if="showFile" @close="closeCaseFileView"
      width="1100px" append-to-body>
      <combo-case-file-view :attaOid="fileAttaOid" @father-click="closeCaseFileView"></combo-case-file-view>
    </el-dialog>
    <el-dialog :title="caseFormtitle" :visible.sync="caseFormView" width="1300px" height="700px" scrollbar
      v-if="caseFormView" append-to-body>
      <FormView v-if="reportForm.designOid" :designOid="reportForm.designOid" :reportOid="reportForm.reportOid"
        :disabled="true" :authorizeKey="reportForm.authorizeKey" :formNames="reportForm.formNames" />
    </el-dialog>
    <!-- 记录信息-->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="viewDialogTitle" :visible.sync="openRecordShow"
      append-to-body>
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td><b>送达方式：</b></td>
          <td colspan="3">
            <span v-if="viewform.sendWay == '1'">快递送达</span>
            <span v-if="viewform.sendWay == '2'">人工送达窗口</span>
          </td>
        </tr>
        <tr>
          <td><b>证件类型：</b></td>
          <td colspan="3">
            <span v-if="viewform.carType == '1'">证照</span>
            <span v-if="viewform.carType == '2'">批文</span>
            <span v-if="viewform.carType == '3'">其他</span>
          </td>
        </tr>
        <tr>
          <td><b>快递目标：</b></td>
          <td colspan="3">
            <span v-if="viewform.deliverTarget == '1'">窗口</span>
            <span v-if="viewform.deliverTarget == '2'">申请人</span>
          </td>
        </tr>
        <tr v-if="viewform.sendWay == 1">
          <td><b>快递公司：</b></td>
          <td>
            {{ viewform.deliverCompany }}
          </td>
          <td><b>快递单号：</b></td>
          <td>
            {{ viewform.deliverNumber }}
          </td>
        </tr>
        <tr v-if="viewform.sendWay == 1 || viewform.sendWay == 2">
          <td><b>送件人姓名：</b></td>
          <td>
            {{ viewform.sendPerson }}
          </td>
          <td><b>送件人手机号：</b></td>
          <td>
            {{ viewform.sendPhone }}
          </td>
        </tr>
        <tr v-if="viewform.sendWay == 1 || viewform.sendWay == 2">
          <td><b>送件部门：</b></td>
          <td>
            {{ viewform.organName }}
          </td>
          <td><b>送件时间：</b></td>
          <td>
            {{ viewform.sendTime }}
          </td>
        </tr>
      </table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="openRecordShow = false">关闭</el-button>
      </div>
    </el-dialog>
  </el-tabs>
</template>
<script>
  import IframeUrlView from "@/views/iframe/formIndexView";
  import {
    getCaseSituationList,
    getSituationResult,
    getComboFormInfo,
    getComboCaseValRel
  } from "@/api/onething/comboManager/comboAccept/updateComboCase";
  import {
    CodeToText
  } from "element-china-area-data";
  import ViewCaseInfoByOneCase from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfoByOneCase";
  import {
    getElecLicenUrl
  } from "@/api/zc/businessManagement/elemLice";
  import ComboCaseFileView from "@/views/onething/comboManager/comboAccept/comboCaseFileView";
  import {
    fileDownLoad
  } from "@/api/onething/comboManager/comboAccept/comboCaseAtta";
  import {
    queryComboDirectoryResult,
    queryComboSignByMaps
  } from "@/api/onething/fzgl/licenseSignCombo";
  import {
    batchOutList
  } from "@/api/onething/comboManager/comboAccept/materialOut";
  export default {
    name: "viewComboCaseNew",
    components: {
      IframeUrlView,
      ComboCaseFileView,
      ViewCaseInfoByOneCase
    },
    props: ["caseOid"],
    data() {
      return {
        activeName: "info",
        caseOidIndex: this.caseOid,
        childCaseOpenView: false,
        //情形名称
        situationName: "默认自定情形",
        //情形选项
        direSituationOptionTitles: [],
        //情形材料
        comboDireMaterials: [],
        // 表单参数
        form: {
          info: {},
          comboDirectory: {},
          qlCases: [],
          caseMaterials: []
        },
        attaView: false,
        showFile: false,
        fileAttaOid: "",
        //证照签收信息
        resultList: [],
        resultFlag: false,
        viewform: {},
        openRecordShow: false,
        viewDialogTitle: "",
        //办件材料出库信息
        outList: [],
        outFlag: false,
        serFormList: [],
        reportForm: {},
        caseFormtitle: "",
        caseFormView: false,
        formNames: ""
      };
    },

    created() {},
    //获取父页面的值
    mounted() {
      this.getOneCase();
    },
    methods: {
      handleClick(tab, event) {},
      //查询办件信息
      getOneCase() {
        getCaseSituationList(this.caseOidIndex).then(response => {
          /*this.situationName = response.data.comboSituations
            ? response.data.comboSituations
            : this.situationName;
          //情形标题选项
          let optionTitles = response.data.comboOptionTitleList;
          this.getSituationOpinionTitle(optionTitles);*/
          //获取情形和选项
          this.getSituationResult();
          this.getCaseForm(this.caseOidIndex);
          //获取一件事材料
          this.comboDireMaterials = response.data.directoryMaterials;
          //获取一件事目录信息
          this.form.comboDirectory = response.data.comboDirectory;
          //办件基本信息
          this.form.info = response.data.comboCase;
          //一件事办件子办件
          this.form.qlCases = response.data.comboCase.qlCases;
          //办件材料信息
          this.form.caseMaterials = response.data.comboCase.comboCaseMaterials;
          // this.getcaseFormInfo();
          //编辑格式化地址
          const selfArr = this.form.info.addresseeAddress.replace(/\[/g,'').replace(/\]/g,'').replace(/\"/g,'').split(',');
          this.changeAddress(selfArr);
          //材料出库数据、
          this.batchOutList(this.caseOidIndex);
          //获取证照签收数据
          this.queryComboDirectoryResult(this.caseOidIndex);
        });
      },
      /** 填充标题和选项值 */
      getSituationOpinionTitle(optionTitles) {
        if (optionTitles) {
          optionTitles.forEach(optionTitle => {
            let titleValues = {};
            titleValues.titleName = optionTitle.name;
            titleValues.titleOid = optionTitle.oid;
            titleValues.comboOptionVals = optionTitle.comboOptionVals;
            this.direSituationOptionTitles.push(titleValues);
          });
        }
      },
      getSituationResult() {
        getSituationResult(this.caseOidIndex).then(res => {
          this.direSituationOptionTitles = res.data;
        });
      },
      //编辑格式化地址、
      changeAddress(selfArr) {
        if (null != selfArr) {
          var provinceCode = "";
          var zhixia = "";
          var city = "";
          if (null != selfArr[0]) {
            provinceCode = CodeToText[selfArr[0]];
          }
          if (null != selfArr[1]) {
            zhixia = "/" + CodeToText[selfArr[1]];
          }
          if (null != selfArr[2]) {
            city = "/" + CodeToText[selfArr[2]];
          }
          this.form.info.addresseeAddress = provinceCode + zhixia + city;
        }
      },
      //查询办件表单
      getCaseForm(caseOid) {
        getComboFormInfo(caseOid).then(response => {
          console.log(response,'response');
          this.serFormList = response ?.data ?? [];
        });
      },
      //表单预览
      async viewCaseFromInfo(row) {
        this.formNames = "";
        await getComboCaseValRel(this.caseOid, row.designOid).then(response => {
          if (response.data) {
            this.formNames = response.data.formNames;
          }
        });
        this.reportForm = {
          designOid: row.designOid,
          authorizeKey: row.authorizeKey,
          reportOid: this.caseOid,
          formNames: this.formNames
        };
        this.caseFormtitle = "表单预览";
        this.caseFormView = true;
      },
      //材料出库数据、
      batchOutList(caseOidIndex) {
        batchOutList(caseOidIndex).then(response => {
          if (response.data && response.data.length > 0) {
            this.outList = response.data;
            this.outFlag = true;
          } else {
            this.outFlag = false;
          }
        });
      },
      //获取证照签收数据
      queryComboDirectoryResult(caseOidIndex) {
        queryComboDirectoryResult(caseOidIndex).then(response => {
          if (response.data.data && response.data.data.length > 0) {
            this.resultList = response.data.data;
            this.resultFlag = true;
          } else {
            this.resultFlag = false;
          }
        });
      },
      /** 关联办件查看按钮操作 */
      handleChildCaseInfo(row) {
        this.indexCaseNumber = row.caseNumber;
        this.childCaseOpenView = true;
        this.title = "单办办件基本信息";
      },
      /** 关联办件查看关闭按钮操作 */
      closeChildView() {
        this.childCaseOpenView = false;
      },
      //预览附件
      viewFile(attaOid) {
        this.fileAttaOid = attaOid;
        this.showFile = true;
      },
      handleViewAtta(row) {
        this.attaList = row.attaList;
        this.attaView = true;
      },
      closeAttaView() {
        this.attaList = [];
        this.attaView = false;
      },
      //关闭预览附件
      closeCaseFileView() {
        this.showFile = false;
      },
      //下载附件
      downloadFile(attaOid) {
        this.loading = true;
        fileDownLoad(attaOid);
        this.loading = false;
      },
      //电子证照预览
      viewElemsInfo(eleLincenseOid) {
        getElecLicenUrl(eleLincenseOid).then(response => {
          let urlElem = [];
          if (response.data) {
            urlElem = response.data;
            if (urlElem[0].viewOfdUrl) {
              window.open(urlElem[0].viewOfdUrl, "width=1200px;height=800px;");
            } else {
              this.$message.error("暂时无法查看证照信息！");
            }
          }
        });
      },
      //证照签收信息查看
      viewRecord(row) {
        queryComboSignByMaps(row).then(response => {
          this.viewform = response.data;
        });
        this.openRecordShow = true;
        this.viewDialogTitle = "查看记录";
      },
      //下载附件
      downloadFormFile(attaOid) {
        this.download(attaOid);
      }
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
