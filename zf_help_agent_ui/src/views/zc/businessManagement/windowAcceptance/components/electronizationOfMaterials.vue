<!-- 材料电子化 -->
<template>
  <div class="electronization-of-materials" v-loading="loading">
    <el-scrollbar class="common-dialog-content" ref="scrollbar">
      <div
        class="common-dialog--header"
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;
        "
      >
        <div class="common-dialog--title" style="margin: unset">
          系统智能生成材料
        </div>
      </div>
      <table class="zf-zc-table--common zf-zc-table--td-center">
        <colgroup>
          <col width="5%" />
          <col width="19%" />
          <col width="8%" />
          <col width="15%" />
          <col width="53%" />
        </colgroup>
        <tr>
          <th>序号</th>
          <th>材料名称</th>
          <th>参考份数</th>
          <th>签名材料</th>
          <th>提交方式</th>
        </tr>
        <template v-if="ElectronicSealList.length > 0">
          <tbody v-for="(item, index) in ElectronicSealList" :key="index">
            <tr v-if="!item.isElecBill">
              <td>{{ index + 1 }}</td>
              <td style="text-align: left !important">
                {{ item.materialName }}
              </td>
              <td>
                <span>原件</span>
                <!-- <template>
                  <span v-if="item.sxMaterials.materialType == '0'">原件</span>
                  <span v-if="item.sxMaterials.materialType == '1'"
                    >复印件</span
                  >
                  <span v-if="item.sxMaterials.materialType == '2'"
                    >原件或者复印件</span
                  >
                  ({{ item.sxMaterials.paperNumber }})
                </template> -->
              </td>
              <td>
                <div class="handle-btn">
                  <el-button
                    type="text"
                    size="small"
                    @click="preview(item, 'print')"
                  >
                    <img
                      src="@/assets/image/intelligent/print.png"
                      alt
                      width="18"
                      height="16"
                    />
                    打印
                  </el-button>
                  <el-button
                    type="text"
                    size="small"
                    @click="preview(item, 'see')"
                  >
                    <img
                      src="@/assets/image/intelligent/lookUp.png"
                      alt
                      width="18"
                      height="16"
                    />
                    查看
                  </el-button>
                </div>
              </td>
              <td>
                <el-radio-group
                  v-model.trim="item.collectionType"
                  @change="
                    () => {
                      handleCollectionTypeChange(item);
                    }
                  "
                >
                  <el-radio :label="'6'">在线签章</el-radio>
                  <el-radio :label="'1'">纸质收取</el-radio>
                  <!-- <el-radio :disabled="item.sxMaterials.materialFormat != '7'" :label="'7'">告知承诺</el-radio>
                  <el-radio :disabled="item.rqbzFlag != '1'" :label="'4'">容缺</el-radio> -->
                  <el-radio :label="'3'">扫描</el-radio>
                  <!-- <el-radio :disabled="!item.elecBillOid || !item.directoryObj" :label="'5'">证照库</el-radio>
                  <el-radio :label="'2'">附件上传</el-radio> -->
                </el-radio-group>
              </td>
            </tr>

            <!-- 在线签章 -->
            <tr v-if="item.collectionType == 6">
              <td colspan="5" style="background-color: #fafbfc">
                <div class="grid-content qdcg-text">
                  <div
                    v-show="item.signaturePdfUrl"
                    class="handle-data-list-item--link"
                    @click="downSignedSealedDoc"
                  >
                    <span>已签章文件 -</span>
                    <a
                      :href="item.signaturePdfUrl"
                      :download="item.materialName"
                      target="_blank"
                      >{{ item.materialName }}</a
                    >
                  </div>
                  <div class="handle-data">
                    <el-button
                      v-show="item.signaturePdfUrl"
                      size="mini"
                      type="primary"
                      @click="viewSignatureMaterials(item, 'see')"
                      >查看</el-button
                    >
                    <el-button
                      v-show="item.signaturePdfUrl"
                      size="mini"
                      style="background: rgba(255, 190, 96, 0.13);
border: 1px solid #FFBE60;
color: #ED8000;"
                      @click="ElectronicSeal(item, index)"
                      >重新签章</el-button
                    >
                    <el-button
                      v-show="!item.signaturePdfUrl"
                      size="mini"
                      type="primary"
                      @click="ElectronicSeal(item, index)"
                      >立即签章</el-button
                    >
                  </div>
                </div>
              </td>
            </tr>
          </tbody>
        </template>
        <tr v-else>
          <td colspan="5">暂无数据</td>
        </tr>
      </table>

      <div
        class="common-dialog--header"
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;
        "
      >
        <div class="common-dialog--title" style="margin: unset">
          申请材料信息
        </div>
        <el-button
          type="primary"
          size="small"
          plain
          :disabled="isMatchAllElec"
          @click="hanldeMatchAllElec"
          >智能匹配证照库</el-button
        >
      </div>
      <table class="zf-zc-table--common zf-zc-table--td-center">
        <colgroup>
          <col width="5%" />
          <col width="19%" />
          <col width="8%" />
          <col width="15%" />
          <col width="53%" />
        </colgroup>
        <tr>
          <th>序号</th>
          <th>材料名称</th>
          <th>参考份数</th>
          <th>材料样本</th>
          <th>提交方式</th>
        </tr>
        <template v-if="sxServiceMaterialAttaList.length > 0">
          <tbody
            v-for="(item, index) in sxServiceMaterialAttaList"
            :key="index"
          >
            <tr v-if="!item.isElecBill">
              <td>{{ item.index }}</td>
              <td style="text-align: left !important">
                {{ item.materialName }}
              </td>
              <td>
                <template>
                  <span v-if="item.sxMaterials.materialType == '0'">原件</span>
                  <span v-if="item.sxMaterials.materialType == '1'"
                    >复印件</span
                  >
                  <span v-if="item.sxMaterials.materialType == '2'"
                    >原件或者复印件</span
                  >
                  ({{ item.sxMaterials.paperNumber }})
                </template>
              </td>
              <td>
                <div class="handle-btn">
                  <el-button
                    type="text"
                    size="small"
                    @click="downLoadMaterialAddr(item)"
                  >
                    <img
                      src="@/assets/image/intelligent/print.png"
                      alt
                      width="18"
                      height="16"
                    />
                    打印
                  </el-button>
                  <el-button
                    type="text"
                    size="small"
                    @click="
                      viewMaterialAddr(
                        item.materialSampleAddr,
                        item.materialSampleAddrYl
                      )
                    "
                  >
                    <img
                      src="@/assets/image/intelligent/lookUp.png"
                      alt
                      width="18"
                      height="16"
                    />
                    查看
                  </el-button>
                </div>
              </td>
              <td>
                <el-radio-group
                  v-model.trim="item.collectionType"
                  @change="
                    () => {
                      handleCollectionTypeChange(item);
                    }
                  "
                >
                  <el-radio :label="'1'">纸质收取</el-radio>
                  <el-radio
                    :disabled="item.sxMaterials.materialFormat != '7'"
                    :label="'7'"
                    >告知承诺</el-radio
                  >
                  <el-radio :disabled="item.rqbzFlag != '1'" :label="'4'"
                    >容缺</el-radio
                  >
                  <el-radio :label="'3'">扫描</el-radio>
                  <el-radio
                    :disabled="!item.elecBillOid || !item.directoryObj"
                    :label="'5'"
                    >证照库</el-radio
                  >
                  <el-radio :label="'2'">附件上传</el-radio>
                </el-radio-group>
              </td>
            </tr>
            <tr v-else>
              <td colspan="5" class="is-elec-bill">
                <div class="is-elec-bill--content">
                  <div v-if="!item.elecLicenName"></div>
                  <div
                    v-if="item.elecLicenName && !item.elemLicenseOid"
                    style="color: #c56412"
                  >
                    未匹配到相应证照
                  </div>
                  <div
                    v-if="item.elecLicenName && item.elemLicenseOid"
                    class="is-elec-bill-content-link"
                    @click="viewElemsInfo(item.elemLicenseOid)"
                  >
                    <span
                      >{{ item.elecLicenName }}-{{ item.elecLicenNumber }}</span
                    >
                  </div>
                  <div class="is-elec-bill--content-btn">
                    <el-button
                      type="primary"
                      size="mini"
                      plain
                      @click="viewElemsInfo(item.elemLicenseOid)"
                      >预览</el-button
                    >

                    <el-button
                      type="primary"
                      size="mini"
                      plain
                      @click="getElecLicen(item)"
                      >{{
                        item.elemLicenseOid ? "重新获取" : "获取证照"
                      }}</el-button
                    >
                  </div>
                </div>
              </td>
            </tr>
            <!-- 附件上传 -->
            <tr v-if="item.collectionType == 2">
              <td colspan="5" style="background-color: #fafbfc">
                <div class="handle-data">
                  <div class="handle-data-list">
                    <div
                      class="handle-data-list-item"
                      v-for="(dataAtta, idx) in item.attaList"
                      :key="idx"
                    >
                      <div class="grid-content qdcg-text">
                        <div
                          @click="viewFile(dataAtta.attaOid)"
                          class="handle-data-list-item--link"
                        >
                          材料名称 - {{ dataAtta.originName }}
                        </div>
                      </div>
                      <div class="btnArea">
                        <el-button
                          type="text"
                          icon="el-icon-view"
                          @click="viewFile(dataAtta.attaOid)"
                          >查看</el-button
                        >
                        <el-button
                          type="text"
                          icon="el-icon-delete"
                          @click="deleteMaterialAtta(idx, index)"
                          >删除</el-button
                        >
                      </div>
                    </div>
                  </div>
                  <el-upload
                    class="upload-demo"
                    action
                    :http-request="uploadFiles"
                    :before-upload="beforeUpload"
                    :on-error="uploadError"
                    :file-list="fileList"
                    :show-file-list="showFileList"
                    accept=".pdf, .PDF"
                  >
                    <el-button
                      v-if="item.collectionType == 2"
                      size="mini"
                      type="primary"
                      icon="el-icon-upload"
                      @click="pushMaterialOid(item.caseMaterialOid, index)"
                      >点击上传</el-button
                    >
                  </el-upload>
                </div>
              </td>
            </tr>
          </tbody>
        </template>
        <tr v-else>
          <td colspan="5">暂无数据</td>
        </tr>
      </table>

      <!-- 容缺补正 -->
      <table
        v-if="isExitRqbzTime"
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="zf-zc-table"
      >
        <colgroup>
          <col width="15%" />
          <col width="35%" />
          <col width="15%" />
          <col width="35%" />
        </colgroup>
        <tr>
          <td>
            <i class="require">*</i>
            <b>容缺补正时间：</b>
          </td>
          <td colspan="3">
            <el-date-picker
              v-model.trim="rqbzDueDate"
              type="date"
              value-format="yyyy-MM-dd"
              :picker-options="optionDate"
              placeholder="选择容缺补正时间"
              @change="handleDueDateChange"
            />
          </td>
        </tr>
      </table>
    </el-scrollbar>
    <div class="common-dialog-footer">
      <el-button type="primary" @click="$emit('lastStep', 3)">上一步</el-button>
      <el-button type="primary" :disabled="loading" @click="nextStep"
        >下一步</el-button
      >
    </div>
    <GpyUplaodDialog
      v-if="dialogVisible"
      v-bind="$attrs"
      :caseOid="caseOid"
      :serviceOid="serviceOid"
      :serviceName="serviceName"
      :dialogVisible.sync="dialogVisible"
      :materialAttaList="materialAttaList"
      @nextStep="
        id => {
          $emit('nextStep', 5);
        }
      "
    />

    <el-dialog
      v-dialog-drag
      title="文件预览"
      :visible.sync="view.show"
      v-for="view in newViewDialogOptions"
      :key="view.attaOid"
      @close="closeFileViewNew"
      custom-class="preview-dialog"
      width="1158px"
      height="800px"
      :scrollbar="view.ext !== 'pdf'"
      append-to-body
    >
      <sx-file-view
        id="print"
        :attaOid="view.attaOid"
        @father-click="closeFileViewNew"
      />
      <div style="text-align: center" slot="footer">
        <!-- <el-button
          type="primary"
          id="trueBtn"
          v-show="printView"
          v-print="printObj"
        >
          打印
        </el-button> -->
        <el-button @click="closeFileViewNew">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 个人电子证照 -->
    <el-dialog
      v-dialog-drag
      title="引用电子证照库"
      :visible.sync="elecVisible"
      width="500px"
      append-to-body
      center
      @close="handleElecClose"
    >
      <el-form
        ref="elecForm"
        label-width="100px"
        :model="elecForm"
        :rules="rules"
      >
        <el-divider v-if="clickHandleMatchAllElec && isPersonElecShow"
          >个人证照信息录入</el-divider
        >
        <template v-if="isPersonElecShow">
          <el-form-item label="姓名：" prop="userName">
            <el-input
              v-model.trim="elecForm.userName"
              placeholder="请输入姓名"
            ></el-input>
          </el-form-item>
          <el-form-item label="证件号：" prop="idCard">
            <el-input
              v-model.trim="elecForm.idCard"
              placeholder="请输入身份证号码"
            ></el-input>
          </el-form-item>
        </template>
        <el-divider v-if="clickHandleMatchAllElec && isLegalElecShow"
          >法人证照信息录入</el-divider
        >
        <template v-if="isLegalElecShow">
          <el-form-item label="单位名称：" prop="legalUserName">
            <el-input
              v-model.trim="elecForm.legalUserName"
              placeholder="请输入持证单位名称"
            ></el-input>
          </el-form-item>
          <el-form-item label="证件号：" prop="legalIdCard">
            <el-input
              v-model.trim="elecForm.legalIdCard"
              placeholder="请输入企业信用代码或者组织机构代码"
            ></el-input>
          </el-form-item>
        </template>
      </el-form>
      <div slot="footer" style="text-align: center">
        <el-button v-if="isPersonElecShow" type="primary" @click="scanCard"
          >扫描身份证</el-button
        >
        <el-button type="primary" @click="handelElecFormSubmit">提交</el-button>
      </div>
    </el-dialog>

    <!--引入文件的预览弹出层-->
    <el-dialog
      v-dialog-drag
      title="文件预览"
      :visible.sync="showFile"
      v-if="showFile"
      @close="closeFileView"
      custom-class="preview-dialog"
      width="1100px"
      scrollbar
      height="700px"
      append-to-body
    >
      <case-file-view
        id="print"
        :attaOid="fileAttaOid"
        @father-click="closeFileView"
      ></case-file-view>
      <div slot="footer" class="zf-text-center">
        <!-- <el-button type="primary" id="trueBtn" v-print="printObj">
          打印
        </el-button> -->
        <el-button @click="showFile = false">关闭</el-button>
      </div>
    </el-dialog>
    <!-- 电子签章文件预览弹窗 -->
    <el-dialog
      v-dialog-drag
      title="文件预览"
      :visible.sync="viewDialogOptions.show"
      @close="closeFileViewNew"
      custom-class="preview-dialog"
      width="1158px"
      height="800px"
      :scrollbar="viewDialogOptions.ext !== 'pdf'"
      append-to-body
    >
      <iframe
        id="print"
        :src="viewDialogOptions.attaUrl"
        scrolling="no"
        frameborder="0"
        style=" width:100%; height: 800px"
      ></iframe>
      <!-- <file-view :attaOid="view.attaOid" @father-click="closeFileViewNew" ></file-view> -->
      <div style="text-align: center" slot="footer" v-show="printDoc">
        <!-- <el-button type="primary" id="trueBtn" v-print="printObj">
          打印
        </el-button> -->
        <el-button @click="closeFileViewNew">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 材料签章二维码弹窗 -->
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
      <div style="width:214px; margin-left:193px;margin-bottom:20px">
        <img :src="imgUrl" alt="二维码" />
      </div>
      <div style="text-align:center">
        <strong
          >请用手机浏览器、蒙速办app、支付宝、微信、QQ扫描上方二维码进行签章。</strong
        >
      </div>
    </el-dialog>

    <!-- 多人电子签章选择 -->
    <el-dialog
      v-dialog-drag
      title="电子签章选择"
      :visible.sync="selSignatureVisible"
      :width="isSetting ? '835px' : '1100px'"
      height="700px"
      class="subDialoag"
      append-to-body
      :lock-scroll="false"
      center
    >
      <!-- 配置页 -->
      <div v-show="!isSetting" v-loading="isDialogLoading" style="width:100%;height:100%">
        <div class="setting-page">
          <div class="headerTitle">
          <div class="title">
            《{{
              editRowData.materialName
            }}》需要以下类型人员和企业完成电子签章。
          </div>
          <div class="notice" v-if="editRowData.message">
            {{ editRowData.message }}
          </div>
          </div>
          <div class="role-block" v-for="(item, index) in manySignList">
            <div
        class="common-dialog--header"
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;
          margin-top:21px
        "
      >
              <div class="sub-title common-dialog--title">{{ item.roleName }}</div>
              <div class="add-btn" @click="handleAdd(item.list)">
                <i class="icon el-icon-user-solid" v-if="item.type == 0"></i>
                <i class="icon el-icon-office-building " v-else></i>
                <p> {{ item.type == 0 ? "添加签章人" : "添加签署企业" }}</p>

              </div>
      </div>

            <el-table
              :data="item.list"
              border
              class="table roleTable"
            >
              <el-table-column
                label="序号"
                width="55"
                type="index"
                align="center"
              >
                <template slot-scope="scope">
                  <span>{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="企业名称"
                align="center"
                prop="companyName"
                v-if="index === 2"
              >
                <template slot-scope="scope">
                  <el-input
                    placeholder="必填"
                    v-model="scope.row.companyName"
                    @blur="handleNoEmptyBlur(scope.row, scope.row.companyName)"
                  ></el-input>
                </template>
              </el-table-column>
              <el-table-column
                label="统一社会信用代码"
                align="center"
                prop="socialCode"
                v-if="index === 2"
              >
                <template slot-scope="scope">
                  <el-input
                    placeholder="必填"
                    v-model="scope.row.socialCode"
                    @blur="handleNoEmptyBlur(scope.row, scope.row.socialCode)"
                  >
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column label="签署人姓名" align="center" prop="name">
                <template slot-scope="scope">
                  <el-input
                    placeholder="必填"
                    v-model="scope.row.name"
                    @blur="handleNoEmptyBlur(scope.row, scope.row.name)"
                  >
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column label="签署人手机号" align="center" prop="phone">
                <template slot-scope="scope">
                  <el-input
                    placeholder="必填"
                    v-model="scope.row.phone"
                    @blur="handlePhoneBlur(scope.row, scope.row.phone)"
                  >
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="350">
                <template slot-scope="scope">
                  <span
                    class="delete-btn click-element"
                    @click="handleDelete(item.list, scope.row.$index)"
                  >
                    <i class="el-icon-delete"></i>
                    删除
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
        <div
          class="setting-footer"
        >
        <el-button style="background: #E7EDF3;" @click="selSignatureVisible = false">取消</el-button>
        <el-button type="primary"  @click="saveManySign" style="margin-left:15px">保存并去签署</el-button>
        </div>
      </div>
      <!-- 通知页 -->
      <div class="notice-page" v-show="isSetting" v-loading="isDialogLoading">
        <div class="headerTitle">
                  <p class="notice">
          提示：《{{
            editRowData.materialName
          }}》需要以下人员完成电子签章，请点击【立即签署】按钮进行签署或者点击【通知签署】按钮，系统会给签署人或企业签署人手机号发送签署短信进行签署。
        </p>
        </div>
        <div v-for="(item, index) in manySignList" :key="index">
                  <div
        class="common-dialog--header"
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;
          margin-top:21px
        "
      >
       <div class="sub-title">{{ item.roleName }}</div>
      </div>
          <div class="dialog-list">
            <div
              class="dialog-item"
              v-for="(subItem, subIndex) in item.list"
              :key="subIndex"
            >
              <div class="name">{{ subItem.name }}</div>
              <div class="phone">{{ subItem.phone }}</div>
                <div class="btn signal-btn" @click="handleManySign(subItem)">
                  扫码签署
                </div>
                <div
                  class="btn notice-btn"
                  :class="{ finish: subItem.mailStatus !== 'N' }"
                  @click="sendMessage(item, subItem)"
                >
                  {{ subItem.mailStatus == "N" ? "通知签署" : "已通知" }}
                </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import GpyUplaodDialog from "../dialogs/gpy-upload-dialog.vue";
import materialFileView from "@/views/zc/businessManagement/windowAcceptance/materialFileView";
import sxFileView from "@/views/zc/qdgl/sxFileView";
import fileView from "@/views/common/fileView";
import {
  updateQlCaseMaterialList,
  getAllQlCaseMaterialListByAttaOid,
  saveOrUpdateCaseMaterialAttaList
} from "@/api/materialCategory.js";
import {
  getElecLicenUrl,
  queryElecLicenseByDirCode,
  queryElecLicenseType
} from "@/api/zc/businessManagement/elemLice";
import { getIdCardInfo, openIdCard, findIdCard } from "@/api/sys/hardwareScan";
import { downloadMaterial } from "@/api/zc/qdgl/materialDetails.js";
import { querySaveCallRecordByOid } from "@/api/zc/qhjh/qhjh";

import { uploadCaseMaterialFile } from "@/api/zc/businessManagement/caseMaterialAtta";
import caseFileView from "@/views/zc/businessManagement/caseBqbz/caseFileView";
import {
  getElectronicSealInfo2,
  getElectronicSealInfo4,
  queryQlCaseMaterialListByCaseOidForZC,
  getHtmlType,
  getSignRole,
  getSignaturePerson,
  getTemplateList,
  saveSignaturePerson,
  sendMessage
} from "@/api/zc/businessManagement/windowAcceptance";
import { mapGetters } from "vuex";
import moment from "moment";

import { awaitPromise } from "@/utils/utils";
import GPYDrive from "@/api/handwareDrive.js";
import DEVEICETYPE, {
  ID_CARD_V1,
  ID_CARD_V2,
  ID_CARD_V3
} from "@/components/HiSpeedCamera/config.js";
import {
  openIdcardv3,
  closeIdcardv3,
  getdataIdcardv3
} from "@/api/handwarev3.js";
// 环境
import QRCode from "qrcode";
const ENV = process.env.NODE_ENV === "production";

// rules
const personRules = {
  userName: [{ required: true, message: "姓名不能为空", trigger: "change" }],
  idCard: [{ required: true, message: "证件号不能为空", trigger: "change" }]
};

const legalRules = {
  legalUserName: [
    { required: true, message: "单位名称不能为空", trigger: "change" }
  ],
  legalIdCard: [
    { required: true, message: "证件号不能为空", trigger: "change" }
  ]
};
export default {
  inheritAttrs: false,
  name: "ElectronizationOfMaterials",
  props: [
    "caseOid",
    "serviceOid",
    "serviceName",
    "isQlCaseChanged",
    "onlyMounted",
    "ruleForm"
  ],
  components: {
    GpyUplaodDialog,
    materialFileView,
    fileView,
    sxFileView,
    caseFileView
  },
  data() {
    return {
      dialogVisible: false,
      sxServiceMaterialAttaList: [],
      fileSxAttaOid: "",
      viewDialogOptions: {},
      newViewDialogOptions: [],
      loading: false,

      elecForm: {
        userName: "",
        idCard: "",
        legalUserName: "",
        legalIdCard: ""
      },

      elecVisible: false,

      currentElecForm: {},

      clickHandleMatchAllElec: false,

      rqbzDueDate: "",

      optionDate: {
        disabledDate(time) {
          return time.getTime() < Date.now(); // 选当前时间之后的时间
        }
      },

      isReadedRqbzNotice: false, // 是否阅读了容缺补正通知书

      isReadedInformNotice: false, // 是否阅读了告知承诺通知书
      showFileList: false,
      fileList: [],
      materialOid: null,
      attaIndex: null, // 当前选中的附加上传材料索引
      fileAttaOid: "",
      showFile: false,
      materialAttaList: [], // 附件保存数据
      printObj: {
        id: "print",
        popTitle: "",
        extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>'
      },
      printView: false,
      printSrc: "",
      eleShow: false,
      ElectronicSealList: [],
      imgUrl: "", //生成的二维码
      previewImageUrl: "/case-api/pic/previewImage?fastdfsNginxUrl=",
      startTime: 0,
      timer: null,
      signedSealedDoc: "",
      printDoc: false,
      isDialogLoading: false,
      roleList: [],
      manySignList: [],
      isSetting: 0,
      selSignatureVisible: false,
      roleIdList: [],
      editRowData:{},
      qzIndex:0
    };
  },

  computed: {
    saveComponentIndex() {
      return this.$store.state.config.saveComponentIndex;
    },
    ...mapGetters(["saveCallRecordOid"]),
    /** 是否可以点击批量匹配电子证照 */
    isMatchAllElec({ sxServiceMaterialAttaList }) {
      return (
        sxServiceMaterialAttaList.filter(
          item => !!item.elecBillOid && !!item.directoryObj
        ).length === 0
      );
    },
    /** 是否存在容缺补正 */
    isExitRqbzTime({ sxServiceMaterialAttaList }) {
      return (
        sxServiceMaterialAttaList.filter(item => item.collectionType === "4")
          .length > 0
      );
    },

    /** 是否存在告知承诺 */
    isExitInformPromise({ sxServiceMaterialAttaList }) {
      return (
        sxServiceMaterialAttaList.filter(item => item.collectionType === "7")
          .length > 0
      );
    },

    /** 判断是否有个人证照 */
    personElecList({ sxServiceMaterialAttaList }) {
      return sxServiceMaterialAttaList.filter(
        item =>
          !item.isElecBill &&
          item.elecBillOid &&
          item.directoryObj &&
          item.directoryObj !== "1"
      );
    },

    /** 判断是否有法人的 */
    legalELecList({ sxServiceMaterialAttaList }) {
      return sxServiceMaterialAttaList.filter(
        item =>
          !item.isElecBill &&
          item.elecBillOid &&
          item.directoryObj &&
          item.directoryObj === "1"
      );
    },

    /** 判断个人证照是否显示 */
    isPersonElecShow({
      currentElecForm,
      clickHandleMatchAllElec,
      personElecList
    }) {
      return (
        (currentElecForm.directoryObj &&
          currentElecForm.directoryObj !== "1") ||
        (clickHandleMatchAllElec && personElecList.length > 0)
      );
    },

    /** 判断法人证照是否显示 */
    isLegalElecShow({
      currentElecForm,
      clickHandleMatchAllElec,
      legalELecList
    }) {
      return (
        (currentElecForm.directoryObj &&
          currentElecForm.directoryObj === "1") ||
        (clickHandleMatchAllElec && legalELecList.length > 0)
      );
    },

    rules({
      isPersonElecShow,
      isLegalElecShow,
      clickHandleMatchAllElec,
      personElecList,
      legalELecList
    }) {
      let rules = {};
      if (clickHandleMatchAllElec) {
        if (legalELecList.length > 0) {
          rules = { ...rules, ...legalRules };
        }

        if (personElecList.length > 0) {
          rules = { ...rules, ...personRules };
        }
      } else {
        if (isPersonElecShow) {
          rules = { ...personRules };
        }

        if (isLegalElecShow) {
          rules = { ...legalRules };
        }
      }

      return rules;
    }
  },

  watch: {
    saveComponentIndex: {
      handler(val) {
        if (val === 4) {
          // this.initCase();
          this.saveMaterialStep();
        }
      }
    },
    isExitRqbzTime: {
      immediate: true,
      handler(val) {
        if (!val) {
          this.rqbzDueDate = "";
          this.$emit("setRqbzTime", "");
          if (this.onlyMounted) {
            this.ruleForm.rqbzDueDate = "";
          }
        } else {
          if (this.onlyMounted) {
            this.rqbzDueDate = this.ruleForm._rqbzDueDate;
            this.ruleForm.rqbzDueDate = this.ruleForm._rqbzDueDate;
          }
        }
      }
    }
  },


  mounted() {
    // this.ElectronicSealInfo();
    // 如果是暂存受理页面
    if (this.onlyMounted) {
      this.loading = true;
      this.handleIdCardInfo();
      this.getAttaList();
    }
    // 点击下一步绑定办件和附件数据信息
  },
  async activated() {
     this.handleIdCardInfo();
      this.getAttaList();
    // 如果非暂存受理页面
    if (!this.onlyMounted) {
      // 如果存在叫号id
      if (this.saveCallRecordOid) {
        try {
          const { code, data } = await querySaveCallRecordByOid({
            oid: this.saveCallRecordOid
          });
          if (code === 200) {
            this.elecForm.userName = data.caseUserName;
            this.elecForm.idCard = data.cardNo;
          }
          this.handleIdCardInfo();
          this.getInit();
        } catch (error) {
          this.handleIdCardInfo();
          this.getInit();
        }
      } else {
        this.handleIdCardInfo();
        this.getInit();
      }
    } else {
      this.handleIdCardInfo();
    }
  },
  deactivated() {
    const { userName, idCard, legalUserName, legalIdCard } = this.elecForm;

    if (
      this.onlyMounted &&
      (!userName || !idCard || !legalIdCard || !legalUserName)
    )
      return;

    if (!userName && !idCard && !legalUserName && !legalIdCard) return;

    // 判断是否缓存过身份信息
    const idCardInfo = localStorage?.idCardInfo
      ? JSON.parse(localStorage.idCardInfo)
      : false;

    if (idCardInfo) {
      localStorage.setItem(
        "idCardInfo",
        JSON.stringify({
          ...idCardInfo,
          CNName: this.elecForm.userName,
          carID: this.elecForm.idCard,
          legalUserName: this.elecForm.legalUserName,
          legalIdCard: this.elecForm.legalIdCard
        })
      );
    } else {
      localStorage.setItem(
        "idCardInfo",
        JSON.stringify({
          CNName: this.elecForm.userName,
          carID: this.elecForm.idCard,
          legalUserName: this.elecForm.legalUserName,
          legalIdCard: this.elecForm.legalIdCard
        })
      );
    }
  },
  methods: {
    handleIdCardInfo() {
      // 判断是否缓存过身份信息
      const idCardInfo = localStorage?.idCardInfo
        ? JSON.parse(localStorage.idCardInfo)
        : false;
      if (idCardInfo) {
        this.elecForm.userName = idCardInfo.CNName || this.elecForm.userName;
        this.elecForm.idCard = idCardInfo.carID || this.elecForm.idCard;
        this.elecForm.legalUserName =
          idCardInfo.legalUserName || this.elecForm.legalUserName;
        this.elecForm.legalIdCard =
          idCardInfo.legalIdCard || this.elecForm.legalIdCard;
      }
    },

    async nextStep() {
      clearTimeout(this.timer);
      // alert('正在运行')
      // FIX: 饿了么button会自动聚焦
      if (this.dialogVisible) return;
      // 容缺补正和告知承诺只能选择其中一种
      if (this.isExitInformPromise && this.isExitRqbzTime) {
        return this.$message.warning(
          "【容缺补正】和【告知承诺】提交方式只能选择一种"
        );
      }

      // 容缺补正时间必填
      if (this.isExitRqbzTime && !this.rqbzDueDate) {
        // 自动滚动到底部
        this.$nextTick(() => {
          this.$refs.scrollbar.$refs.wrap.scrollTop = this.$refs.scrollbar.$refs.wrap.scrollHeight;
        });
        return this.$message.warning("请输入容缺补正时间!");
      } else if (
        this.isExitRqbzTime &&
        this.rqbzDueDate &&
        !this.isReadedRqbzNotice
      ) {
        const [_, action] = await awaitPromise(
          this.$alert(
            `告知申请人在<strong style="color:#fb783f">${moment(
              this.rqbzDueDate
            ).format(
              "YYYY[年]MM[月]DD[日]"
            )}</strong>补齐缺少的材料，否则材料将退回并结束该办件`,
            "容缺补正须知",
            {
              confirmButtonText: "已告知",
              dangerouslyUseHTMLString: true
            }
          )
        );
        if (action !== "confirm") return;
        this.isReadedRqbzNotice = true;
        this.$emit("setRqbzTime", this.rqbzDueDate);
      }

      // 如果选择了承诺告知
      if (this.isExitInformPromise && !this.isReadedInformNotice) {
        const materialNames = this.sxServiceMaterialAttaList
          .filter(item => item.collectionType === "7")
          .map(item => item.materialName);
        const content = (
          <div>
            <p>请告知申请人，以下材料需申请人以书面形式承诺符合办理条件：</p>
            {materialNames.map((ele, idx) => (
              <p>
                <span>{idx + 1}、</span>
                <span>{ele}</span>
              </p>
            ))}
          </div>
        );
        const [_, action] = await awaitPromise(
          this.$msgbox({
            title: "告知承诺须知",
            message: content,
            showCancelButton: false,
            confirmButtonText: "已告知",
            customClass: "inform-promise--msgbox"
          })
        );
        if (action !== "confirm") return;
        this.isReadedInformNotice = true;
      }

      // 判断是否选择了电子证照的却没有设置电子证照信息的
      const hasELec = this.sxServiceMaterialAttaList
        ?.filter(item => item.collectionType === "5" && !item.isElecBill)
        ?.find(item => !item.elecLicenNumber);
      if (hasELec)
        return this.$message.warning(
          `请获取【${hasELec.materialName}】的电子证照信息`
        );

      // 是否有扫描的方式
      const hasScanning = this.sxServiceMaterialAttaList.some(
        item => item.collectionType === "3"
      );

      //判断是否选择了附件上传方式
      const fileUpLoadList = this.sxServiceMaterialAttaList?.filter(
        item => item.collectionType === "2"
      );
      if (fileUpLoadList.length !== 0) {
        // 判端是否都有文件上传
        const isAllUpload = fileUpLoadList.find(
          item => item.attaList.length === 0
        );
        if (isAllUpload) {
          return this.$message.warning(
            `请上传【${isAllUpload.materialName}】的附件信息`
          );
        }
        // 附件保存数据
        this.materialAttaList = fileUpLoadList.map(item => {
          return {
            caseMaterialOid: item.caseMaterialOid,
            collectionType: item.collectionType,
            qlCaseMaterialAttaList: item.attaList.map(i => ({
              attaOid: i.attaOid,
              caseMaterialOid: item.caseMaterialOid,
              src: i.fastdfsNginxUrl
            }))
          };
        });
        // 如果没有选择扫描上传方式 上传附件保存附件的接口
        if (!hasScanning) {
          saveOrUpdateCaseMaterialAttaList(this.materialAttaList).then(res => {
            if (res.code !== 200 || !res.data.success) {
              return this.$message.warning("上传附件保存材料失败");
            }
          });
        }
      } else {
        // 删除所有附件
        this.sxServiceMaterialAttaList.forEach(
          item => (item.attaList.length = 0)
        );
      }

      // 选择上传方式
      const loading = this.$loading({
        lock: true,
        text: "正在加载中...",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      const { code, messge } = await updateQlCaseMaterialList(
        this.sxServiceMaterialAttaList.filter(item => !item.isElecBill)
      );
      if (code !== 200) {
        loading.close();
        return this.$message.warning(messge || "保存材料附件失败");
      }
      loading.close();
      // 缓存所有的caseMaterialOid
      const caseMaterialOids = this.sxServiceMaterialAttaList?.reduce(
        (prev, cur) => {
          if (prev[cur.collectionType]) {
            prev[cur.collectionType] = `${prev[cur.collectionType]};${
              cur.caseMaterialOid
            }`;
          } else {
            prev[cur.collectionType] = cur.caseMaterialOid;
          }
          return prev;
        },
        {}
      );
      this.$emit("caseMaterialOids", caseMaterialOids);

      // 获取容缺补正材料名称
      const rqbzMaterialNames = this.sxServiceMaterialAttaList
        .filter(item => item.collectionType == "4")
        .map(item => item.materialName)
        .join(",");

      this.$emit("setRqbzMaterialNames", rqbzMaterialNames);

      if (hasScanning) {
        this.dialogVisible = true;
        return;
      }

      this.$emit("nextStep", 5);
    },

    handleDueDateChange() {
      this.isReadedRqbzNotice = false;
    },

    getInit() {
      if (this.isQlCaseChanged) {
        this.loading = true;
        this.getAttaList();
        this.$emit("setIsQlCaseChanged", false);
      }
    },

    //材料附件
    getAttaList() {
      this.sxServiceMaterialAttaList = [];
      queryQlCaseMaterialListByCaseOidForZC(this.caseOid)
        .then(response => {
          this.loading = false;
          if (response.code !== 200 || !response.data)
            return this.$message.warning("查询材料附件失败");
          this.ElectronicSealList = [];
          if (response.data.autoProduceMaterialList.length) {
            response.data.autoProduceMaterialList.forEach((item, index) => {
              if (item.qlCaseMaterialAttaList.length) {
                item.qlCaseMaterialAttaList.forEach(ite => {
                  ite.materialName = item.materialName;
                  ite.materialOid = item.materialOid;
                  ite.collectionType = "6";
                  ite.roleType = item.roleType
                  ite.message = item.memo
                  this.ElectronicSealList.push(ite);
                });
              }
            });
          }
          this.sxServiceMaterialAttaList = response.data.needUploadMaterialList.reduce(
            (prev, item, idx) => {
              let collectionType = item.collectionType;

              if (!this.onlyMounted) {
                collectionType =
                  !!item.directoryObj &&
                  !!item.elecBillOid &&
                  !!item.elemNumber &&
                  !!item.elemLicenseOid
                    ? "5"
                    : item.collectionType || "1";
              }

              prev.push({
                ...item,
                collectionType,
                index: idx + 1,
                sxMaterials: {}
              });

              if (
                collectionType === "5" &&
                !!item.elecBillOid &&
                !!item.elemNumber &&
                !!item.elemLicenseOid
              ) {
                // 新增一行
                const row = {
                  isElecBill: true,
                  ...item,
                  originElecInfo: {
                    originName: item.elemNumber,
                    attaOid: item.elecLicenOid,
                    elemLicenseOid: item.elecLicenOid,
                    elecLicenName: item.elecLicenName,
                    elecLicenNumber: item.elecLicenNumber
                  }
                };

                prev = [...prev, row];
              }

              return prev;
            },
            []
          );
          this.$attrs.sxServiceMaterialList.forEach(item => {
            this.sxServiceMaterialAttaList.forEach(ite => {
              if (ite.isElecBill) return;
              if (item.materialOid == ite.materialOid) {
                ite.sxMaterials = item;
                ite.collectionNumber = item.paperNumber;
                if (this.onlyMounted || ite.collectionType == "5") return;
                ite.collectionType = item.materialFormat
                  ? (item.materialFormat == "1"
                      ? item.materialFormat
                      : item.materialFormat == "2"
                      ? "3"
                      : ite.collectionType
                    ).toString()
                  : ite.collectionType;
              }
            });
          });

          if (this.onlyMounted) {
            if (this.isExitRqbzTime) {
              this.isReadedRqbzNotice = true;
            }

            if (this.isExitInformPromise) {
              this.isReadedInformNotice = true;
            }

            this.rqbzDueDate = this.ruleForm.rqbzDueDate;
          }
        })
        .catch(() => {
          this.loading = false;
        });
    },

    downLoadMaterialAddr(data) {
      if (data.materialSampleAddr) {
        let item = {
          show: true,
          attaOid: data.materialSampleAddr,
          ext: data.materialSampleAddrYl?.split?.(".")?.splice?.(-1)?.[0]
        };
        this.newViewDialogOptions.push(item);
      } else {
        this.$message.warning("暂无材料打印！");
      }
      this.printObj.popTitle = data.materialName;
      this.printView = true;
    },

    //材料查看
    viewMaterialAddr(attaOid, addr) {
      if (attaOid) {
        let item = {
          show: true,
          attaOid: attaOid,
          ext: addr?.split?.(".")?.splice?.(-1)?.[0]
        };
        console.log(
          "888888888888888888",
          addr?.split?.(".")?.splice?.(-1)?.[0]
        );
        this.newViewDialogOptions.push(item);
      } else {
        this.$message.warning("暂无材料查看！");
      }
    },
    //关闭预览附件
    closeFileViewNew() {
      this.newViewDialogOptions.pop();
      this.viewDialogOptions.show = false;
    },

    handleCollectionTypeChange(value) {
      // 如果选中了告知承诺
      if (value.collectionType === "7") {
        this.isReadedInformNotice = false;
      }

      // 如果选中了容缺补正
      if (value.collectionType === "4") {
        this.isReadedRqbzNotice = false;
      }

      const index = this.sxServiceMaterialAttaList.findIndex(
        item => item.id === value.id
      );

      // 如果是证照上传
      if (value.collectionType === "5") {
        if (index >= 0) {
          this.sxServiceMaterialAttaList.splice(index + 1, 0, {
            ...this.sxServiceMaterialAttaList[index],
            ...(this.sxServiceMaterialAttaList[index]?.originElecInfo ?? {}),
            isElecBill: true
          });
          // 设置回缓存的值
          if (
            this.sxServiceMaterialAttaList[index]?.originElecInfo
              ?.elemLicenseOid
          ) {
            this.$set(this.sxServiceMaterialAttaList, index, {
              ...this.sxServiceMaterialAttaList[index],
              ...this.sxServiceMaterialAttaList[index].originElecInfo
            });

            return;
          }

          this.getElecLicen(this.sxServiceMaterialAttaList[index + 1]);
        }
      } else {
        if (this.sxServiceMaterialAttaList[index + 1]?.isElecBill) {
          this.sxServiceMaterialAttaList.splice(index + 1, 1);
        }
        this.$set(this.sxServiceMaterialAttaList, index, {
          ...this.sxServiceMaterialAttaList[index],
          originName: undefined,
          attaOid: undefined,
          elemLicenseOid: undefined,
          elecLicenName: undefined,
          elecLicenNumber: undefined
        });
      }
    },

    // 打开证照弹框 获取证照信息
    getElecLicen(elecForm) {
      if (elecForm.directoryObj) {
        this.currentElecForm = { ...elecForm };
        this.elecVisible = true;
      } else {
        queryElecLicenseType(elecForm.materialOid, elecForm.elecBillOid).then(
          ({ code, data }) => {
            if (code !== 200 || !data) return;
            const { directoryObj } = data;
            elecForm.directoryObj = directoryObj;
            this.currentElecForm = { ...elecForm };
            this.elecVisible = true;
          }
        );
      }
    },

    handelElecFormSubmit() {
      this.$refs.elecForm.validate(async valid => {
        if (valid) {
          if (this.clickHandleMatchAllElec) {
            this.elecVisible = false;
            if (this.personElecList.length > 0) {
              this.queryAllEelc(
                this.caseOid,
                this.personElecList,
                this.elecForm.userName,
                this.elecForm.idCard
              );
            }
            if (this.legalELecList.length > 0) {
              this.queryAllEelc(
                this.caseOid,
                this.legalELecList,
                this.elecForm.legalUserName,
                this.elecForm.legalIdCard
              );
            }
            return;
          }
          const { materialOid, elecBillOid } = this.currentElecForm;
          const {
            userName,
            idCard,
            legalUserName,
            legalIdCard
          } = this.elecForm;
          const index = this.sxServiceMaterialAttaList.findIndex(
            item => item.materialOid === materialOid
          );

          this.elecVisible = false;
          if (this.isPersonElecShow) {
            this.getElecLicenInfo(
              this.caseOid,
              materialOid,
              userName,
              idCard,
              elecBillOid,
              index
            );
          } else {
            this.getElecLicenInfo(
              this.caseOid,
              materialOid,
              legalUserName,
              legalIdCard,
              elecBillOid,
              index
            );
          }
        }
      });
    },

    getElecLicenInfo(
      caseOid,
      materialOid,
      userName,
      idCard,
      elecBillOid,
      index
    ) {
      return queryElecLicenseByDirCode(
        materialOid,
        userName,
        idCard,
        elecBillOid,
        caseOid
      ).then(({ data, code, message }) => {
        if (code !== 200) {
          this.currentElecForm = {};
          return this.$message.warning(
            message || "暂无证照,请检查证照相关配置！"
          );
        }
        const { licenseNumber, elecLicenOid, elecLicenNumber, elecLicenName } =
          data || {};
        this.$set(this.sxServiceMaterialAttaList, index, {
          ...this.sxServiceMaterialAttaList[index],
          originName: licenseNumber,
          attaOid: elecLicenOid,
          elemLicenseOid: elecLicenOid,
          elemNumber: licenseNumber,
          elecLicenName: elecLicenName || "未匹配到相应证照",
          elecLicenNumber,
          originElecInfo: {
            // 设置缓存
            originName: licenseNumber,
            attaOid: elecLicenOid,
            elemLicenseOid: elecLicenOid,
            elecLicenName: elecLicenName || "未匹配到相应证照",
            elecLicenNumber
          }
        });

        this.$set(this.sxServiceMaterialAttaList, index + 1, {
          ...this.currentElecForm,
          ...(data || {}),
          elemLicenseOid: elecLicenOid,
          elecLicenName: elecLicenName || "未匹配到相应证照"
        });

        this.currentElecForm = {};

        // this.$message.success("电子证照引用成功");
      });
    },
    //电子证照预览
    viewElemsInfo(eleLincenseOid) {
      if (!eleLincenseOid) return this.$message.warning("请先获取证照信息");

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

    // 批量匹配电子证照
    hanldeMatchAllElec() {
      this.clickHandleMatchAllElec = true;
      this.sxServiceMaterialAttaList = this.sxServiceMaterialAttaList.reduce(
        (prev, cur) => {
          const arr = [];
          if (cur.elecBillOid && !cur.isElecBill && !!cur.directoryObj) {
            cur.collectionType = "5";
            arr.push({
              ...cur,
              isElecBill: true
            });
          }
          if (cur.isElecBill) {
            return prev;
          }
          prev.push(cur);
          prev = [...prev, ...arr];
          return prev;
        },
        []
      );

      this.elecVisible = true;
    },

    queryAllEelc(caseOid = "", list = [], userName = "", idCard = "") {
      const promiseAllList = list.map(item =>
        queryElecLicenseByDirCode(
          item.materialOid,
          userName,
          idCard,
          item.elecBillOid,
          caseOid
        )
      );
      this.clickHandleMatchAllElec = false;
      return Promise.all(promiseAllList).then(ret => {
        ret.forEach(({ code, data }, idx) => {
          if (code !== 200)
            return console.warn(`${list[idx].materialName}获取证照信息失败`);
          const index = this.sxServiceMaterialAttaList.findIndex(
            item => item.id === list[idx].id && !item.isElecBill
          );
          if (index >= 0) {
            const {
              licenseNumber,
              elecLicenOid,
              elecLicenName,
              elecLicenNumber
            } = data || {};

            this.$set(this.sxServiceMaterialAttaList, index, {
              ...this.sxServiceMaterialAttaList[index],
              originName: licenseNumber,
              attaOid: elecLicenOid,
              elemLicenseOid: elecLicenOid,
              elemNumber: licenseNumber,
              elecLicenName: elecLicenName || "未匹配到相应证照",
              elecLicenNumber,
              collectionType: "5",
              originElecInfo: {
                // 设置缓存
                originName: licenseNumber,
                attaOid: elecLicenOid,
                elemLicenseOid: elecLicenOid,
                elecLicenName: elecLicenName || "未匹配到相应证照",
                elecLicenNumber
              }
            });

            if (this.sxServiceMaterialAttaList[index + 1]?.isElecBill) {
              this.$set(this.sxServiceMaterialAttaList, index + 1, {
                ...this.sxServiceMaterialAttaList[index + 1],
                ...(data || {}),
                elemLicenseOid: elecLicenOid,
                elecLicenName: elecLicenName || "未匹配到相应证照"
              });
            } else {
              this.sxServiceMaterialAttaList.splice(index + 1, 0, {
                ...this.sxServiceMaterialAttaList[index],
                ...(this.sxServiceMaterialAttaList[index]?.originElecInfo ??
                  {}),
                isElecBill: true
              });
            }
          } else {
            return console.warn(`${list[idx].materialName}匹配证照信息失败`);
          }
        });
      });
    },

    handleElecClose() {
      this.$refs?.elecForm?.clearValidate?.();
      // 如果点击了智能匹配
      this.clickHandleMatchAllElec = false;
      this.elecVisible = false;
    },

    scanCard() {
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V1) {
        this.getIdcardDatav1();
      }
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V2) {
        this.getIdcardDatav2();
      }
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V3) {
        this.getIdcardDatav3();
      }
    },
    async getIdcardDatav1() {
      openIdCard().then(response => {
        //打开设备
        if (response.state == "sucess") {
          this.getIdcardDataByv1(); //重新获取身份证信息
        } else {
          //查找设备
          findIdCard().then(resFind => {
            if ((resFind.state = "sucess" && resFind.StateCode == 0)) {
              this.getIdcardDataByv1();
            } else {
              this.$message.error("无法找到设备");
              return false;
            }
          });
        }
      });
    },
    async getIdcardDataByv1() {
      getIdCardInfo().then(response => {
        let res = response;
        if (!res) {
          this.$message.error("请检查设备或连接是否正常!");
          return false;
        }
        if (res.state == "sucess") {
          this.elecForm.userName = res.CNName.trim();
          this.elecForm.idCard = res.carID;
          // 缓存身份证信息 方便后面 人证对比 使用
          localStorage.setItem("idCardInfo", JSON.stringify(res));
        } else if (res.StateCode == -1) {
          //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
          //判断有没有设备
          //打开身份证
          openIdCard().then(response => {
            if ((response.state = "sucess")) {
              this.getIdcardDataByv1(); //重新获取身份证信息
            }
          });
        } else {
          this.$message.error(res.tips);
          return false;
        }
      });
    },
    async getIdcardDatav3() {
      openIdcardv3().then(res => {
        if (res.StateCode == 0 || res.StateCode == -1) {
          return this.getIdcardDataByv3(); //获取身份证信息
        } else {
          return this.$message.warning("请确认设备连接是否正常");
        }
      });
    },
    async getIdcardDataByv3() {
      getdataIdcardv3().then(resData => {
        if (resData.state == "sucess" && resData.StateCode == 0) {
          let resInfo = JSON.parse(resData.data);
          let res = {
            CNName: resInfo.name,
            sex: resInfo.sex,
            carID: resInfo.number,
            nation: resInfo.nation,
            address: resInfo.department,
            bron: resInfo.birthday,
            imgData: "data:image/jpeg;base64," + resInfo.Image
          };
          this.elecForm.userName = res.CNName.trim();
          this.elecForm.idCard = res.carID;
        } else if (resData.StateCode == -1) {
          //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
          //判断有没有设备
          //打开身份证
          openIdcardv3().then(res => {
            if (res.StateCode == 0 || res.StateCode == -1) {
              this.getIdcardDataByv3(); //重新获取身份证信息
            }
          });
        } else {
          this.$message.error(resData.tips);
          return false;
        }
      });
    },
    async getIdcardDatav2() {
      const resData = await GPYDrive.readCardInfo();
      if (!resData || resData.code !== 200) {
        this.$message.error("读取身份证信息失败");
        return false;
      }
      let res = {
        CNName: resData.data.name,
        sex: resData.data.sex,
        carID: resData.data.number,
        nation: resData.data.nation,
        address: resData.data.address,
        bron: resData.data.birthday,
        imgData: "data:image/jpeg;base64," + resData.data.image
      };
      this.elecForm.userName = res.CNName.trim();
      this.elecForm.idCard = res.carID;
      // 缓存身份证信息 方便后面 人证对比 使用
      localStorage.setItem("idCardInfo", JSON.stringify(res));
    },
    pushMaterialOid(materialOid, index) {
      this.materialOid = materialOid;
      this.attaIndex = index; //标识材料索引
    },
    //上传之前
    beforeUpload(file) {
      if (
        file.name.indexOf("%00") > -1 ||
        file.name.indexOf("./") > -1 ||
        file.name.indexOf(".\\") > -1
      ) {
        this.msgError("上传文件名称非法！");
        return false;
      }
      const fileSize = file.size;
      if (0 == fileSize) {
        this.msgError("上传文件不能为空！");
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 500;
      if (!isLt2M) {
        this.msgError("上传文件大小不能超过 500MB！");
      }
      return isLt2M;
    },
    /** 上传附件 */
    uploadFiles(file) {
      let formData = new FormData();
      formData.append("files", file.file);
      this.$getResponse(uploadCaseMaterialFile(formData), (error, response) => {
        if (error || !response) return this.uploadError();
        const { code, data } = response;
        if (code !== 200) return this.uploadError();
        const target = this.sxServiceMaterialAttaList[this.attaIndex];
        target.attaList = target.attaList.concat(data);
      });
    },
    //失败后返回
    uploadError(resp) {
      this.msgError("文件上传失败");
    },
    //预览附件
    viewFile(attaOid) {
      this.fileAttaOid = attaOid;
      this.showFile = true;
    },
    // //关闭预览附件
    closeFileView() {
      this.showFile = false;
    },
    //删除附件信息
    deleteMaterialAtta(idx, index) {
      this.sxServiceMaterialAttaList[index].attaList.splice(idx, 1);
    },

    //获取电子签章弹窗信息
    // async ElectronicSealInfo() {
    //   const { data, code, message } = await queryQlCaseMaterialListByCaseOidForZC(
    //     this.caseOid,
    //   );
    //   console.log("初始化data", data);
    //   // this.serviceOid
    //   if (code !== 200) {
    //     // this.getFormState = false;
    //     return message && this.$message.warning(message);
    //   }
    //   this.ElectronicSealList = [];
    //   data?.map?.((item, idx) => {
    //     this.ElectronicSealList.push(Object.assign(item, { isSeal: "未签章" }));
    //   }) ?? [];
    //   console.log("yyyyyyyyyyy", this.ElectronicSealList);
    //   // this.getFormState = false;
    // },

    //电子签章弹窗二维码
    async ElectronicSeal(row, index) {
      this.editRowData = row
      this.qzIndex = index
      const { id } = row;
      this.getTemplateList()
      try {
        if (!row.roleType) {
          await this.getFlow(row, index);
        } else {
          this.manySignList = [];
          this.getHtmlType(row, index);
        }
      } catch (error) {
        console.log(error);
      }
    },

    async getFlow(row, index) {
    try {
        const {
          applyUserName,
          applyUserPhone,
          credentialNumber,
          legalPersonName
        } = this.ruleForm;
        const templateName =
          this.ElectronicSealList[index].materialName + "pdf";
        const { templatePdfUrl } = this.ElectronicSealList[index];
        const cegisterType = this.$attrs.cegisterType;
        let Edata = {};
        if (cegisterType === "1") {
          Edata = {
            signerDTOList: [
              {
                name: applyUserName,
                mobile: applyUserPhone,
                uscc: "",
                orgName: ""
              }
            ],
            fileList: [{ fileName: templateName, fileUrl: templatePdfUrl }],
            caseOid: this.caseOid,
            materialOid: row.materialOid
          };
        } else {
          Edata = {
            signerDTOList: [
              {
                name: legalPersonName,
                mobile: applyUserPhone,
                uscc: credentialNumber,
                orgName: applyUserName
              }
            ],
            fileList: [{ fileName: templateName, fileUrl: templatePdfUrl }],
            caseOid: this.caseOid,
            materialOid: row.materialOid
          };
        }
        const { data, code, message } = await getElectronicSealInfo2(Edata);

        const { signUrl } = JSON.parse(data).data.signTasks[index];
        this.flowId = JSON.parse(data).data.flowId;
        this.imgUrl = await QRCode.toDataURL(signUrl);
        const Edata3 = {
          caseOid: this.caseOid,
          materialOid: row.materialOid,
          flowId: this.flowId,
          status: 2
        };
        this.eleShow = true;
        if (code === 200) {
          this.startTime = new Date().getTime();
          this.getElectronicSealStatu(Edata3.materialOid);
        }
      } catch (error) {
        console.log(error);
      }
    },

    // 多人签章 ———— 1.判断该办件事项是否配置，跳到配置页还是通知页
    getHtmlType() {
      getHtmlType({
        materialOid: this.editRowData.materialOid,
        caseOid: this.caseOid
      }).then(res=>{
     if (res.code === 200) {
        this.isSetting = res.data; //0就是进入配置页面，1就是进入签章通知页
        this.selSignatureVisible = true;
        this.isDialogLoading = true;
        this.getSignRole();
      }
      }).catch(err=>{

      })

    },

    //多人签章获取签章角色列表
    getSignRole() {
    getSignRole({
        materialOid: this.editRowData.materialOid
      }).then(res=>{
   if (res.code === 200) {
        this.roleList = res.data;
        this.roleList.forEach(item => {
          this.manySignList.push({
            ...item,
            list: []
          });
        });
        if (this.isSetting) {
          // 已配置  获取配置签章人员列表
          this.getSignaturePerson();
        } else {
          this.isDialogLoading = false;
        }
      }
      }).catch(err=>{

      })

    },

    //  多人签章列表
    async getSignaturePerson() {
   getSignaturePerson({
        materialOid: this.editRowData.materialOid,
        caseOid: this.caseOid
      }).then(res=>{
    this.isDialogLoading = false;
      if (res.code === 200) {
        let roleIdList = this.roleList.map(item => item.id);
        this.manySignList.forEach(item => {
          item.list = [];
        });
        res.data.forEach(item => {
          let index = roleIdList.indexOf(item.roleId);
          this.manySignList[index].list.push(item);
        });
      }
      }).catch(err=>{

      })
    },

    // 添加签章人或者 签署企业
    handleAdd(list) {
      list.push({
        name: "",
        phone: "",
        companyName: "",
        socialCode: ""
      });
    },

getTemplateList() {
            this.fileListParams = [{
            "attaOid": "45e0509a217244df9e5672f6a7b7fc05",
"attaUrl": "http://192.168.212.199:8899/group1/M00/00/5C/wKjUx2K8KwGAH74oAAEm7XEe-GA937.pdf",
"materialName": "《增、减、补、换发证照申请书》",
"materialOid": "ff8080817d1390a7017d3108e2d01820",
"sealStatus": 0,
"templateName": "营业执照遗失补领、换发申请表"
          }]
		// 		getTemplateList({
		// 			reportOid: this.caseOid,
		// 			serviceOid: this.serviceOid
		// 		}).then(res=>{
		// if (res.code === 200) {
		// 			this.fileListParams = res.data;
		// 		}
    //     }).catch(err=>{

    //     })

			},

    // 新增多人签章
    async saveManySign() {
      let signList = [];
      this.manySignList.forEach((item, index) => {
        item.list.forEach(subItem => {
          // 公司签署
          if (index === 2) {
            if (!subItem.companyName || !subItem.socialCode) {
              this.$message.error("必填项不能为空");
              return;
            }
          }
          if (!subItem.name || !subItem.phone) {
            this.$message.error("必填项不能为空");
            return;
          }

          signList.push({
            roleId: item.id,
            roleName: item.roleName,
            materialOid: this.editRowData.materialOid,
            caseOid: this.caseOid,
            name: subItem.name,
            phone: subItem.phone,
            companyName: subItem.companyName,
            socialCode: subItem.socialCode
          });
        });
      });
      let fileList = [];
      this.fileListParams.forEach(item => {
        if (item.materialOid === this.editRowData.materialOid) {
          fileList = [
            {
              fileName: item.materialName,
              fileUrl: item.attaUrl
            }
          ];
        }
      });
      saveSignaturePerson({
        signList,
        fileList: this.getFileList()
      }).then(res=>{
    if (res.code === 200) {
        this.getSignaturePerson();
        this.$message({
          message: "保存成功",
          type: "success"
        });
        this.isSetting = 1;
      }
      }).catch(err=>{

      })

    },

    // 手机号输入框失焦事件
    handlePhoneBlur(item, value) {
      const reg = /^((0\d{2,3}-\d{7,8})|(1[345789]\d{9}))$/;
      if (value && !reg.test(value)) {
        this.$message.error("请填写正确格式的手机号");
      } else if (!value) {
        this.$message.error("手机号不能为空");
      }
    },
    handleNoEmptyBlur(item, value) {
      if (!value) {
        this.$message.error("必填项不能为空");
      }
    },
    // 删除
    handleDelete(list, index) {
      list.splice(index, 1);
    },
    // 通知签章发送短信
    async sendMessage(item, subItem) {
      console.log("通知签章", subItem);
      if (subItem.mailStatus !== "N") return;
      const formData = new FormData()
      formData.append('id',subItem.id)
      formData.append('phone',subItem.phone)
      formData.append('roleId',subItem.roleId)
      formData.append('signUrl', subItem.signatureUrl)
     sendMessage(formData).then(res=>{
     if (res.code === 200) {
        subItem.mailStatus = "Y";
        this.$message({
          message: "短信发送成功",
          type: "success"
        });
        this.startTime = new Date().getTime();
 this.getElectronicSealStatu(item.materialOid);
      }
      }).catch(res=>{

      })

    },
    // 点击多人签章弹窗中的立即签章
   async handleManySign(item) {
this.imgUrl = await QRCode.toDataURL(item.signatureUrl)
this.eleShow = true;
this.startTime = new Date().getTime();
 this.getElectronicSealStatu(item.materialOid);
    },
    getFileList() {
      let fileList = [];
      this.fileListParams.forEach(item => {
        if (item.materialOid === this.editRowData.materialOid) {
          fileList = [
            {
              fileName: item.materialName,
              fileUrl: item.attaUrl
            }
          ];
        }
      });
      return fileList;
    },

    //获取电子签章状态
    getElectronicSealStatu(materialOid) {
      const reload = () => {
        clearTimeout(this.timer); // 清除定时器
        // 超过10分钟则停止轮询
        if (new Date().getTime() - this.startTime > 10 * 60 * 1000) {
          clearTimeout(this.timer);
          return;
        }
        // 1s一次, 轮询中
        this.timer = setTimeout(() => {
          this.getElectronicSealStatu(materialOid); // 调用轮询
        }, 1000);
      };
      getElectronicSealInfo4(this.caseOid, materialOid)
        .then(res => {
          if (res && res.code === 200 && res.data[0].downloadUrl) {
            if (
              this.signedSealedDoc &&
              this.signedSealedDoc === res.data[0].downloadUrl
            ) {
              reload();
            } else {
              clearTimeout(this.timer);
              this.eleShow = false;
              this.selSignatureVisible = false
              this.signedSealedDoc = res.data[0].downloadUrl;
              this.getAttaList()
            }
          } else {
            // 没成功,调用轮询
            reload();
          }
        })
        .catch(err => {
          // 请求错误,也继续轮询
          reload();
        });
    },

    //电子签章打印预览
    preview(row, type) {
      if (type === "print") {
        this.printDoc = true;
      }
      if (row.templatePdfUrl) {
        this.viewDialogOptions = {
          show: true,
          attaOid: row.attaOid,
          attaUrl: row.templatePdfUrl
        };
        this.printObj.popTitle = row.materialName;
      } else {
        this.$message.warning("暂无材料！");
      }
    },

    //查看签完章材料
    viewSignatureMaterials(row) {
      this.viewDialogOptions = {
        show: true,
        attaOid: row.attaOid,
        attaUrl: row.signaturePdfUrl
      };
      console.log("1111111", this.viewDialogOptions)
      this.printObj.popTitle = row.materialName;
    },

    downSignedSealedDoc() {
      downloadMaterial(attaOid);
    },
    saveMaterialStep() {
      const loading = this.$loading({
        lock: true,
        text: "正在暂存信息",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      saveOrUpdateCaseMaterialAttaList(this.materialAttaList)
        .then(res => {
          loading.close();
          if (res.code === 200) {
            this.$store.commit("setSaveComponentIndex", 0);
            this.$emit("close");
          } else {
            this.$store.commit("setSaveComponentIndex", 0);
            return this.$message.warning("暂存失败");
          }
        })
        .catch(err => {
          loading.close();
          this.$store.commit("setSaveComponentIndex", 0);
        });
    }
  }
};
</script>

<style lang="scss">
.el-message-box.inform-promise--msgbox {
  width: 450px;
}

.handle-btn {
  .el-button {
    span {
      display: flex;
      align-items: center;
      img {
        margin-right: 4px;
      }
    }
  }
}

.is-elec-bill {
  background-color: #fafbfc !important;
  height: 40px;
  padding: unset !important;

  .is-elec-bill--content {
    display: flex;
    justify-content: space-between;
    padding: 0 80px;
    align-items: center;

    .is-elec-bill-content-link {
      text-decoration: underline;
      cursor: pointer;

      &:hover {
        color: #409eff;
      }
    }
  }
}
</style>
<style lang="scss" scoped>
.grid-content {
  width: 100%;
  height: 100%;
  display: flex !important;
  align-items: center;
  justify-content: flex-end;
  .handle-data-list-item--link {
    width: 60%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    padding-left: 30px;
  }
  .handle-data {
    width: 30%;
    height: 100%;
    display: flex;
    justify-content: flex-end;
    padding: 25px 40px;

    &-list {
      flex: 1;
      display: flex;
      flex-direction: column;

      &-item {
        flex: 1;
        display: flex;
        padding: 0 20px;
        justify-content: space-between;
        align-items: center;

        &--link {
          text-decoration: underline;
          cursor: pointer;

          &:hover {
            color: #409eff;
          }
        }
      }
    }
  }
}
.handle-data {
  width: 100%;
  height: 100%;
  display: flex !important;
  align-items: center;
  justify-content: flex-end;
  background-color: #fff !important;
  .handle-data-list {
    width: 80%;
    height: 100%;
    .handle-data-list-item {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .grid-content {
        width: 50%;
        height: 100%;
        .handle-data-list-item--link {
          width: 100%;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: flex-start;
        }
      }
      .btnArea {
        width: 30%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: flex-end;
      }
    }
  }
  .upload-demo {
    width: 20%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding: 0 20px;
  }
}
::v-deep .el-radio {
  margin-right: 15px;
}

::v-deep.el-input--medium .el-input__icon {
  line-height: 32px;
}
.setting-page{
  width: 100%;
  height: calc(100% - 40px);
  overflow-y: auto;
}
.setting-footer{
  width: 100%;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.headerTitle{
  width: 100%;
  height: auto;
  padding: 15px;
background: rgba(151, 91, 14, 0.07);
border-radius: 5px;
.title{
  font-size: 16px;
font-family: Microsoft YaHei;
font-weight: bold;
color: #975B0E;
}
.notice{
  margin-top:15px;
  font-size: 14px;
font-family: Microsoft YaHei;
font-weight: 400;
color: #975B0E;
line-height: 25px;
}
}
.common-dialog--header{
  .sub-title{
    position: relative;
    padding-left: 10px;
    font-size: 16px;
font-family: Microsoft YaHei;
font-weight: bold;
color: #2A344C;
    &:before{
          content: "";
    position: absolute;
    left: 0;
    top: 50%;
    transform: translateY(-50%);
    width: 3.5px;
    height: 16px;
    background: #2e7dff;
    border-radius: 2px;
    }
  }
      .add-btn{
      display: flex;
      align-items: center;
      justify-content: flex-end;
      width: auto;
      height: 100%;
      cursor: pointer;
      background: rgba(46, 125, 255, 0.13);
border: 1px solid #2E7DFF;
border-radius: 5px;
padding: 8px 15px;
      p{
        padding: 0;
        margin: 0;
        margin-left: 6px;
      }
    }
}
.dialog-list{
  width: 100%;
  min-height: 166px;
  display: flex;
  flex-wrap:wrap;
    align-items: center;
  justify-content: flex-start;
  .dialog-item{
    width: 151px;
height: 166px;
background: #FFFFFF;
border: 1px solid #D9E2EA;
box-shadow: 0px 3px 0px 0px rgba(171, 167, 167, 0.18);
border-radius: 8px;
margin-bottom: 10px;
margin-right: 8px;
display: flex;
flex-direction: column;
align-items: center;
justify-content: space-around;
.name{
  font-size: 18px;
font-family: Microsoft YaHei;
font-weight: bold;
color: #2A344C;
}
.phone{
  font-size: 14px;
font-family: Microsoft YaHei;
font-weight: bold;
color: #2A344C;
}
.btn{
  width: 108px;
height: 35px;
border: 1px solid;
border-radius: 4px;
font-size: 16px;
font-family: Microsoft YaHei;
font-weight: bold;
display: flex;
align-items: center;
justify-content: center;
cursor: pointer;
}
.signal-btn{
  color: #FFFFFF;
// border-image: linear-gradient(180deg, #519BFF, #3D94FF) 10 10;
background: linear-gradient(180deg, #519BFF 0%, #3D94FF 100%);
}
.notice-btn{
  // border-image: linear-gradient(180deg, #7BC28E, #218B60) 10 10;
background: linear-gradient(180deg, #7BC28E 0%, #218B60 100%);
opacity: 0.65;
color: #006E3B;
}
  }
}
</style>
