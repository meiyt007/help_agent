/**
* 一件事办件查看
* @author: wangxl
* @date: 2020-12-1
*/
<template>
  <div class="app-container">
    <!-- 步骤 -->
  <!--  <el-dialog :close-on-click-modal="false"  :title="title" :visible.sync="openInit" v-if="openInit" width="1300px"
               append-to-body>-->
      <div class="process-box" id="print">
        <div class="step-title">
            <span
              v-for="(item, index) in stepData"
              :key="item.index"
              :class="{ active: index == i }">
              {{ item.title }}
            </span>
        </div>
        <!-- 第一步 -->
        <div class="step-content step-first" v-if="show_0">
          <div class="situation-box">
            <h3 class="title"><i class="el-icon-s-grid"></i>情形选择</h3>
            <div class="select-list">
          <span
            v-for="(item, index) in selectData"
            :key="item.index"
            :class="{ current: index == num }"
          >{{ item.title }}</span
          >
            </div>
          </div>
          <div class="option-box">
            <div class="option-title">
              <div class="option-item"><i class="el-icon-s-grid"></i>选项信息</div>
              <div class="option-item">
                <i class="el-icon-s-grid"></i>
                <div class="chose-item">[<span>{{ situationName }}</span>]</div>
                情景选项信息
              </div>
            </div>
          </div>
          <el-form
            :model="optionForm"
            :rules="rules"
            ref="optionForm"
            label-width="0px"
            class="demo-optionForm"
            :label-position="labelPosition"
          >
            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="el-table__body mt20"
            >
              <colgroup>
                <col width="15%"/>
              </colgroup>
              <tr>
                <td><b>选项名称：</b></td>
                <td colspan="3">
                  <div v-if="direSituationOptionTitles.length > 0">
                    <div v-for="(data,index) in direSituationOptionTitles">
                      <div class="check-list">
                        <h3>{{ data.titleName }}</h3>
                        <div v-for="item in data.comboOptionVals">
                          {{ item.name }}
                        </div>
                      </div>
                    </div>
                  </div>
                  <div v-else>暂无数据</div>
                </td>
              </tr>
            </table>
          </el-form>
          <el-button type="primary" icon="el-icon-circle-check" class="next-btn" @click="next(1,1)">下一步</el-button>
        </div>
        <!-- 第二步 -->
        <div class="step-content step-second" v-if="show_1">
          <el-form
            :model="comboDireForm"
            ref="comboDireForm"
            label-width="0px"
            class="demo-comboDireForm"
            :label-position="labelPosition">
            <h3 class="title"><i class="el-icon-s-grid"></i>材料核验列表</h3>
            <div class="data-box">
              <h4>事项基本信息</h4>
              <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
                <colgroup>
                  <col width="15%"/>
                  <col width="35%"/>
                  <col width="15%"/>
                  <col width="35%"/>
                </colgroup>
                <tr>
                  <td><b>一件事目录名称：</b></td><td colspan="3">{{comboDireForm.comboDirectoryName}}</td>
                </tr>
                <tr>
                  <td><b>所属一件事分类：</b></td><td>{{comboDireForm.themeName}}</td>
                  <td><b>所属区划：</b></td><td>{{comboDireForm.districtName}}</td>
                </tr>
                <tr>
                  <td><b>服务对象：</b></td><td>{{comboDireForm.comboServiceObjectName}}</td>
                  <td><b>主办部门：</b></td><td>{{comboDireForm.mainOrganName}}</td>
                </tr>
                <tr>
                  <td><b>协办部门：</b></td><td colspan="3">{{comboDireForm.assistOrganName}}</td>
                </tr>
                <tr>
                  <td><b>线下跑动次数：</b></td><td>{{comboDireForm.countToScence}}</td>
                  <td><b>是否收费：</b></td><td>{{reversedIfCharge}}</td>
                </tr>
                <tr>
                  <td><b>承诺时限(工作日)：</b></td><td>{{comboDireForm.promiseLimit}}</td>
                  <td><b>法定时限(工作日)：</b></td><td>{{comboDireForm.legalLimit}}</td>
                </tr>
              </table>
            </div>
            <div class="data-box">
              <table cellspacing="0" cellpadding="0" border="0" class="data-table">
                <colgroup>
                  <col width="10%"/>
                  <col width="30%"/>
                  <col width="15%"/>
                  <col width="15%"/>
                  <col width="15%"/>
                </colgroup>
                <tr>
                  <th>序号</th>
                  <th>材料名称</th>
                  <th>材料类型</th>
                  <th>材料形式</th>
                  <th>份数</th>
                  <th>必要性</th>
                </tr>
                <tbody v-if="comboDireMaterials.length>0">
                <tr v-for="(data,index) in comboDireMaterials">
                  <td>{{ index + 1 }}</td>
                  <td>{{ data.materialName }}</td>
                  <td>
                    <template>
                      <span v-if="data.materialType==0">原件</span>
                      <span v-if="data.materialType==1">复印件</span>
                      <span v-if="data.materialType==2">原件或者复印件</span>
                    </template>
                  </td>
                  <td>
                    <template>
                      <span v-if="data.materialFormat==1">纸质</span>
                      <span v-if="data.materialFormat==2">电子版</span>
                    </template>
                  </td>
                  <td>{{ data.paperNumber }}</td>
                  <td>
                    <template>
                      <span v-if="data.mustFlag==0">非必须</span>
                      <span v-if="data.mustFlag==1"><span class="bage-necessery">必须</span></span>
                      <span v-if="data.mustFlag==2">容缺后补</span>
                    </template>
                  </td>
                </tr>
                </tbody>
                <tbody v-else>
                <tr>
                  <td colspan="6">暂无数据</td>
                </tr>
                </tbody>
              </table>
            </div>
            <!--</div>-->
            <div class="btn-wrap">
             <!-- <el-checkbox v-model="radio" label="1" @change="checkedBox">我已核验上述材料并确定齐全</el-checkbox>-->
              <div class="btn-list mt10">
                <el-button type="primary" icon="el-icon-back" @click="next(0,-1)">上一步</el-button>
                <el-button type="primary" icon="el-icon-circle-check" @click="next(2,1)">下一步</el-button>
              </div>
            </div>
          </el-form>
        </div>
        <!-- 第三步 -->
        <div class="step-content step-third" v-if="show_2">
          <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="0px"  class="demo-ruleForm"
                   :label-position="labelPosition">
            <div class="step-third-box">
              <h3 class="title"><i class="el-icon-s-grid"></i>申请人信息</h3>
              <table cellspacing="0" cellpadding="0"  border="0"  class="el-table__body">
                <colgroup>
                  <col width="15%"/>
                  <col width="35%"/>
                  <col width="15%"/>
                  <col width="35%"/>
                </colgroup>
                <tr>
                  <td>
                    <span v-if="cegisterType==0">
                       <b>申请人姓名：</b>
                      </span>
                    <span v-else>
                        <b>申请单位名称：</b>
                      </span>
                  </td>
                  <td colspan="3">
                    {{ruleForm.applyUserName}}
                  </td>
                </tr>
                <tr>
                  <td><b>证件类型：</b></td>
                  <td>
                    <el-form-item prop="credentialType">
                      <el-col :span="24">
                        <span v-for="certificateType in certificateTypeList" >
                          <span v-if="certificateType.dictOid == ruleForm.credentialType" >
                            {{certificateType.name}}
                          </span>
                        </span>
                        <!--<el-select v-model="ruleForm.credentialType" placeholder="请选择证件类型">
                          <el-option v-for="certificateType in certificateTypeList" disabled
                                     :key="certificateType.dictOid"
                                     :label="certificateType.name"
                                     :value="certificateType.dictOid">
                          </el-option>
                        </el-select>-->
                      </el-col>
                    </el-form-item>
                  </td>
                  <td><b>证件号码：</b></td>
                  <td>
                    {{ruleForm.credentialNumber}}
                  </td>
                </tr>
                <tr>
                  <td>
                    <span v-if="cegisterType==0">
                       <b>申请人手机：</b>
                      </span>
                    <span v-else>
                        <b>申请单位名称手机：</b>
                      </span>
                  </td>
                  <td>
                    {{ruleForm.applyUserPhone}}
                  </td>
                  <td>
                    <span v-if="cegisterType==0">
                       <b>申请人电话：</b>
                      </span>
                    <span v-else>
                        <b>申请单位电话：</b>
                      </span>
                    <br>(格式：xxxx-xxxxxxxx)
                  </td>
                  <td>
                    {{ruleForm.applyUserTel}}
                  </td>
                </tr>
                <tr v-if="cegisterType==0">
                  <td><b>通讯地址：</b></td>
                  <td colspan="3">
                    {{ruleForm.applyUserAddress}}
                  </td>
                </tr>
                <tr v-else>
                  <td><b>法定代表人：</b></td>
                  <td colspan="3">
                    {{ruleForm.legalPersonName}}
                  </td>
                </tr>
              </table>
            </div>
            <div class="step-third-box">
              <h3 class="title"><i class="el-icon-s-grid"></i>办件信息</h3>
              <table
                cellspacing="0"
                cellpadding="0"
                border="0"
                class="el-table__body"
              >
                <colgroup>
                  <col width="15%"/>
                  <col width="35%"/>
                  <col width="15%"/>
                  <col width="35%"/>
                </colgroup>
                <tr>
                  <td><b>申请项目名称：</b></td>
                  <td colspan="3">
                   {{ruleForm.projectName}}
                  </td>
                </tr>
                <tr>
                  <td><b>业务管辖地：</b></td>
                  <td>
                    <!--<el-form-item prop="bussVenueDistrictOid">
                      <el-col :span="24">
                        <treeselect
                          :multiple="true"
                          :options="districtOptions"
                          :flat="true"
                          :default-expand-level="1"
                          placeholder="请选择业务管辖地"
                          v-model="ruleForm.bussVenueDistrictOid"
                        />
                      </el-col>
                    </el-form-item>-->
                    {{ruleForm.bussVenueDistrictName}}
                  </td>
                  <td><b>受理具体地点：</b></td>
                  <td>
                    {{ruleForm.specificLocation}}
                  </td>
                </tr>
                <tr>
                  <td><b>投资项目名称：</b></td>
                  <td>
                    {{ruleForm.investProjecName}}
                  </td>
                  <td><b>投资项目编号：</b></td>
                  <td>
                    {{ruleForm.investProjectCode}}
                  </td>
                </tr>
                <tr>
                  <td><b>摘要内容：</b></td>
                  <td colspan="3">
                    {{ruleForm.projectAbstract}}
                  </td>
                </tr>
                <tr>
                  <td><b>邮政编码：</b></td>
                  <td>
                    {{ruleForm.applyPostCode}}
                  </td>
                  <td><b>是否为代理人：</b></td>
                  <td>
                    <span v-if="ruleForm.proxyFlag=='1'">是</span>
                    <span v-if="ruleForm.proxyFlag=='0'">否</span>
                  </td>
                </tr>
              </table>
            </div>

            <!--&lt;!&ndash; 代理人信息 &ndash;&gt;-->
            <div class="step-third-box" v-if="proxy_show">
              <h3 class="title"><i class="el-icon-s-grid"></i>联系人/代理人相关信息</h3>
              <table
                cellspacing="0"
                cellpadding="0"
                border="0"
                class="el-table__body"
              >
                <colgroup>
                  <col width="15%"/>
                  <col width="35%"/>
                  <col width="15%"/>
                  <col width="35%"/>
                </colgroup>
                <tr>
                  <td><b>联系人/代理人姓名：</b></td>
                  <td colspan="3">
                    {{ruleForm.contactUserName}}
                  </td>
                </tr>
                <tr>
                  <td><b>联系人/代理人身份证号码：</b></td>
                  <td>
                    {{ruleForm.contactCredentialNumber}}
                  </td>
                  <td><b>电子邮箱：</b></td>
                  <td>
                    {{ruleForm.contactEmail}}
                  </td>
                </tr>
                <tr>
                  <td><b>手机号：</b></td>
                  <td>
                    {{ruleForm.contactUserPhone}}
                  </td>
                  <td><b>固定电话：</b></td>
                  <td>
                    {{ruleForm.contactUserTel}}
                  </td>
                </tr>
                <tr>
                  <td><b>备注：</b></td>
                  <td colspan="3">
                    {{ruleForm.contactRemark}}
                  </td>
                </tr>
              </table>
            </div>
            <!-- &lt;!&ndash; 送达信息 &ndash;&gt;-->
            <div class="step-third-box">
              <h3 class="title"><i class="el-icon-s-grid"></i>送达信息</h3>
              <table
                cellspacing="0"
                cellpadding="0"
                border="0"
                class="el-table__body"
              >
                <colgroup>
                  <col width="15%"/>
                  <col width="35%"/>
                  <col width="15%"/>
                  <col width="35%"/>
                </colgroup>
                <tr>
                  <td><b>送达方式：</b></td>
                  <td colspan="3">
                    <span v-if="ruleForm.resultDeliveryWay=='1'">快递送达</span>
                    <span v-if="ruleForm.resultDeliveryWay=='2'">自行取件</span>
                    <span v-if="ruleForm.resultDeliveryWay=='3'">其他</span>
                  </td>
                </tr>
                <tr v-if="address_show">
                  <td><b>收件人名称：</b></td>
                  <td>
                    {{ruleForm.addresseeName}}
                  </td>
                  <td><b>收件人邮政编码：</b></td>
                  <td>
                    {{ruleForm.addresseePostCode}}
                  </td>
                </tr>
                <tr v-if="address_show">
                  <td><b>收件人手机：</b></td>
                  <td>
                    {{ruleForm.addresseePhone}}
                  </td>
                  <td><b>收件人电话：</b></td>
                  <td>
                    {{ruleForm.addresseeTel}}
                  </td>
                </tr>
                <tr v-if="address_show">
                  <td><b>收件人地址：</b></td>
                  <td colspan="3">
                    {{ruleForm.addresseeAddress}}
                  </td>
                </tr>
                <tr v-if="address_show">
                  <td><b>收件人详细地址：</b></td>
                  <td colspan="3">
                    {{ruleForm.addresseeDetailAddress}}
                  </td>
                </tr>
              </table>
            </div>
            <div class="step-third-box" v-if="formConfig_show">
              <h3 class="title"><i class="el-icon-s-grid"></i>表单信息</h3>
              <table
                cellspacing="0"
                cellpadding="0"
                border="0"
                class="el-table__body"
              >
                <colgroup>
                  <col width="15%"/>
                </colgroup>
                <tr>
                  <td><b>填写表单：</b></td>
                  <td colspan="3">
                    <el-row :gutter="10">
                      <el-col :span="12" v-for="(dataForm,i) in sxSerFormList">
                        <div class="form-box-inline">
                          <h4>1、{{dataForm.formName}}</h4>
                          <div class="form-btn-group">
                            <el-button icon="el-icon-view" class="btn" @click="viewFormFilling(caseForm[i],i)">表单预览</el-button>
                          </div>
                          <ul>
                            <li>
                              <div class="icon bdbm-icon">
                                <i class="el-icon-document"></i>
                              </div>
                              <div class="input-text">
                                <h4>表单编码：</h4>
                                <p v-if="dataForm.formCode">{{dataForm.formCode}}</p>
                                <p v-if="!dataForm.formCode">暂无</p>
                              </div>
                            </li>
                            <li>
                              <div class="icon bdsm-icon">
                                <i class="el-icon-warning-outline"></i>
                              </div>
                              <div class="input-text">
                                <h4>表单说明：</h4>
                                <p v-if="dataForm.formText">{{dataForm.formText}}</p>
                                <p v-if="!dataForm.formText">暂无</p>
                              </div>
                            </li>
                          </ul>
                        </div>
                      </el-col>
                    </el-row>
                  </td>
                </tr>
              </table>
            </div>
            <div class="btn-wrap">
              <div class="btn-list mt10">
                <el-button type="primary" icon="el-icon-back" @click="next(1,-1)">上一步</el-button>
                <el-button type="primary" icon="el-icon-circle-check" @click="next(3,1)">下一步</el-button>
              </div>
            </div>
          </el-form>
        </div>
        <!-- 第四步 -->
        <div class="step-content step-fourth" v-if="show_3">
          <h3 class="title"><i class="el-icon-s-grid"></i>申请者相关信息</h3>
          <el-form
            :model="materialForm"
            :rules="rules"
            ref="materialForm"
            label-width="0px"
            class="demo-materialForm"
            :label-position="labelPosition">
            <table cellspacing="0" cellpadding="0" border="0" class="data-table">
              <colgroup>
                <col width="5%"/>
                <col width="15%"/>
                <col width="10%"/>
                <col width="6%"/>
                <col width="20%"/>
              </colgroup>
              <tr>
                <th>序号</th>
                <th>材料名称</th>
                <th>材料类型</th>
                <th>份数</th>
                <th>收取方式</th>
              </tr>
              <tbody v-for="(item,index) in industryCaseMaterials">
              <tr>
                <td>{{ index + 1 }}</td>
                <td>{{ item.materialName }}</td>
                <td>
                  <template>
                    <span v-if="item.materialType=='0'">原件</span>
                    <span v-if="item.materialType=='1'">复印件</span>
                    <span v-if="item.materialType=='2'">原件或者复印件</span>
                  </template>
                </td>
                <td>{{ item.paperNumber }}</td>
                <td>
                  <span v-if="item.collectionType=='1'">纸质收取</span>
                  <span v-if="item.collectionType=='2'">附件上传</span>
                  <span v-if="item.collectionType=='3'">扫描</span>
                  <span v-if="item.collectionType=='4'">容缺后补</span>
                </td>
              </tr>
              </tbody>
            </table>
            <div class="btn-wrap">
              <div class="btn-list mt10">
                <el-button type="primary" icon="el-icon-back" @click="next(2,-1)">上一步</el-button>
                <el-button type="primary" icon="el-icon-close" @click="viewDialog()">关闭</el-button>
                <!--<el-button type="primary" icon="el-icon-right" @click="next(4,1)">下一步</el-button>-->
              </div>
            </div>
          </el-form>
        </div>
        <!-- 第五步 -->
        <!--<div class="step-content step-last" v-if="show_4">
          <h3 class="title"><i class="el-icon-s-grid"></i>申请者相关信息</h3>
          <el-form
            :model="acceptForm"
            :rules="rules"
            ref="acceptForm"
            label-width="0px"
            class="demo-acceptForm"
            :label-position="labelPosition"
          >
            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="el-table__body"
            >
              <colgroup>
                <col width="15%"/>
                <col width="35%"/>
                <col width="15%"/>
                <col width="35%"/>
              </colgroup>
              <tr>
                <td><b>是否受理：</b></td>
                <td colspan="3">
                  <el-form-item prop="ifAccept">
                    <el-radio-group v-model="acceptForm.ifAccept">
                      <el-radio  :label="1">受理通过</el-radio>
                      <el-radio  :label="2">不予受理</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><b>意见说明：</b></td>
                <td colspan="3">
                  {{acceptForm.acceptOpinionDesc}}
                </td>
              </tr>
            </table>
            <div class="btn-wrap">
              <div class="btn-list mt10">
                <el-button type="primary" icon="el-icon-circle-check" @click="next(3,-1)">上一步</el-button>
              </div>
            </div>
          </el-form>
        </div>-->
        <el-dialog title="电子表单预览" :visible.sync="iframVieweState" width="80%" :modal-append-to-body="true" append-to-body height="100%">
          <iframe-url-view :outLink="iframeUrlView" @closeIframeView="closeIframeView"></iframe-url-view>
        </el-dialog>
      </div>
    <!--</el-dialog>-->
  </div>
</template>
<script>
  import {getCaseSituationList,getCertificateType} from "@/api/onelicence/industryManager/industryCaseAccept/updateIndustryCase";
  import IframeUrlView from "@/views/iframe/formIndexView";
  import {queryDistrictSimpleTree} from "@/api/sys/district";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import {VueCropper} from "vue-cropper";
  import {
    selectBySxSerForm,
    caseFormByCase
  } from "@/api/onelicence/industryManager/formConfig";
  import {CodeToText} from "element-china-area-data";
  export default {
    inheritAttrs: false,
    components: {Treeselect, VueCropper,IframeUrlView},
    name: "viewCaseInfo",
    //定义获取父类传过来值的格式
    props:["industryCaseOid"],
    data() {
      return {
        i: 0,
        num: 0,
        num1: 1,
        num2: 2,
        show_0: true,
        show_1: false,
        show_2: false,
        show_3: false,
        show_4: false,
        rightShow: true,
        address_show: true,
        proxy_show: false,
        compare_show: false,
        radio: "",
        radio1: [],
        radio2: "1",
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 事项情形数据
        situationList: [],
        // 事项情形与选项关系数据
        direSituationOptionTitles: [],
        // 事项选项值数据
        situationOptionVals: [],
        //材料办件业务主键
        caseMaterialOids: [],
        //登记类型 法人1 自然人0
        cegisterType: null,
        //证件类型
        certificateTypeList: [],
        //事项材料
        comboDireMaterials: [],
        //办件材料
        industryCaseMaterials:[],
        mateOptRels: [],
        materialOptList: [],
        situationOid: null,
        situationName: '默认自定情形',
        fileList: [],
        showFileList:false,
        accept: {
          type: String,
          default: '.jpg,.jpeg,.png,.pdf,.PDF,.doc,.docx,.DOC,.DOCX'
        },
        //附件集合
        attaList: [],
        //材料与上传成功的附件集合
        materialAttaList: [],
        //材料与上传成功的附件集合
        materialAttaOidList: [],
        //收取材料方式
        collectionTypeList:[],
        // 弹出层标题
        title: "办件查看",
        // 显示弹出层
        openInit: true,
        formConfig_show:false,//标识表单是否显示
        iframeState:false,
        iframVieweState:false,
        iframeUrlView:{},//查看
        sxSerFormList:[],//多个表单信息
        caseForm:[],
        tempFormDataId:[],//临时存放表单填报的返回值
        indexForm:0,
        // 查看显示弹出层
        //openView: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10
        },
        queryForm: {
        },
        // 表单参数
        ruleForm: {
        },
        comboDireForm: {},
        optionForm:{},
        formData: {},
        materialForm:{},
        acceptForm:{},
        labelPosition: "top",
        checkList: [],
        stepData:  [{index: "0", title: "情形选择",}, {index: "1", title: "材料核验",}, {index: "2", title: "信息登记",},
          {index: "3", title: "收取材料",}],
        selectData: [],
        // 表单校验
        rules: {},
      };
    },
    watch: {
    },
    computed: {
      reversedIfCharge: function () {
        if (this.comboDireForm.ifCharge) {
          if(this.comboDireForm.ifCharge==0){
            return '否';
          }else if(this.comboDireForm.ifCharge==1){
            return '是';
          }
        }
        return ''
      }
    },
    created() {
      //一件事情形获取
      this.getIndustryCaseSituationList();
    },
    methods: {
      next(index, count){
        if (count > 0) {
          this.i = index;
          if (index == 1) {
            this.show_0 = false;
            this.show_1 = true;
          }
          if (index == 2) {
            this.show_1 = false;
            this.show_2 = true;
          }
          if (index == 3) {
            this.show_2=false;
            this.show_3=true;
          }
          if (index == 4) {
            this.show_3 = false;
            this.show_4 = true;
          }
        } else {
          this.i = index;
          if (index == 0) {
            this.show_0 = true;
            this.show_1 = false;
          }
          if (index == 1) {
            this.show_1 = true;
            this.show_2 = false;
          }
          if (index == 2) {
            this.show_2 = true;
            this.show_3 = false;
          }
          if (index == 3) {
            this.show_3 = true;
            //this.show_4 = false;
          }
        }
      },
      /** 获取证件类型 */
      getSelectCertificateType() {
        getCertificateType(this.cegisterType).then(response => {
          this.certificateTypeList = response.data;
        });
      },
      /** 事项情形获取 */
      getIndustryCaseSituationList() {
        //查询事项情形
        getCaseSituationList(this.industryCaseOid).then(response => {
         // this.situationName = "默认自定情形";
          let situations = response.data.comboSituations;
          let optionTitles = response.data.comboOptionTitleList;
          this.getSituationOpinion(situations);
          this.getSituationOpinionTitle(optionTitles);
          //给办件相关赋值 data.industryCase
          this.comboDireOid = response.data.industryCase.comboDireOid;
          this.cegisterType = response.data.industryCase.applyUserType;
          //获取证照类型
          this.getSelectCertificateType();
          //获取一件事目录信息
          this.comboDireForm = response.data.comboDirectory;
          this.ruleForm = response.data.industryCase;
          //获取一件事材料
          this.comboDireMaterials = response.data.directoryMaterials;
          //办件材料信息
          this.industryCaseMaterials = response.data.industryCase.industryCaseMaterials;
          this.changeDeliveryWay(response.data.industryCase.resultDeliveryWay);
          this.changeProxyFlag(response.data.industryCase.proxyFlag);
          this.getcaseFormInfo();
          this.handleChange();
        });
      },
      // 编辑格式化地址
      handleChange() {
        let str=this.ruleForm.addresseeAddress;
        str=str.substring(1,str.length-1);
        let self = str.toString().replace(/"/g,'');
        var selfArr = self.split(",");
        if (null != selfArr) {
          var provinceCode = '';
          var zhixia = '';
          var city = '';
          if (null != selfArr[0]) {
            provinceCode = CodeToText[selfArr[0]];
          }
          if (null != selfArr[1]) {
            zhixia = '/' + CodeToText[selfArr[1]];
          }
          if (null != selfArr[2]) {
            city = '/' + CodeToText[selfArr[2]];
          }
          this.ruleForm.addresseeAddress = provinceCode + zhixia + city;
        }
      },
      /** 填充选项 */
      getSituationOpinion(situation) {
        if(situation){
          this.situationName=situation.situationName;
            this.selectData.push(situation);
        }
      },
      /** 填充标题和选项值 */
      getSituationOpinionTitle(optionTitles) {
        if(optionTitles){
          optionTitles.forEach(optionTitle => {
            let titleValues = {};
            titleValues.titleName=optionTitle.name;
            titleValues.titleOid=optionTitle.oid;
            titleValues.comboOptionVals=optionTitle.comboOptionVals;
            this.direSituationOptionTitles.push(titleValues);
          });
        }
      },
      /** 送达方式 */
      changeDeliveryWay(val) {
        this.address_show = (val === '1') ? true : false;
      },
      /** 是否代理人 */
      changeProxyFlag(val) {
        this.proxy_show = (val === 1) ? true : false;
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      viewDialog(){
        this.$emit('view-case');
      },
      /** 表单重置 */
      reset() {
        Object.assign(this.queryForm, this.$options.data().queryForm)
        this.resetForm("queryForm");
      },
      /** 取消按钮 */
      cancel() {
        this.openInit = false;
        this.reset();
      },
      getcaseFormInfo(){
        let tempForm=[];
        selectBySxSerForm(this.comboDireOid).then(response => {
          if(response.data){
            this.formConfig_show=true;
            this.sxSerFormList=response.data;
            //查询办件表单是否存在
            caseFormByCase(this.comboDireOid,this.industryCaseOid).then(response => {
              if(response.data !=null){
                tempForm=response.data;
              }
              if(tempForm){//循环事项表单配置添加表单关联信息
                this.sxSerFormList.forEach((items,i)=>{
                  tempForm.forEach((childItem,j)=>{
                    if(childItem.serFormOid==items.oid){
                      this.caseForm[i]=childItem;
                      if(childItem.formDataId ==null){
                         this.sxSerFormList.splice(this.sxSerFormList.indexOf(items), 1)
                      }
                    }
                  })
                })
                if(this.sxSerFormList.length ==0){
                  this.formConfig_show=false;
                }
              }
            });
          }
        });
      },
      //查看电子表单
      viewFormFilling(items,index){
        let _that=this;
        if(_that.sxSerFormList[index]){
          if(_that.sxSerFormList[index].formType==0){//自定义表单
            if(_that.sxSerFormList[index].formAddr){
              window.open(_that.sxSerFormList[index].formAddr,'width=1200px;height=800px;');
            }else{
              _that.$message.error("请配置表单地址！");
            }
          }else if(_that.sxSerFormList[index].formType==1){//电子表单
            if(items==null){
              _that.$message.error("未查询到表单！");
            }else {
              _that.iframVieweState = true;
              _that.iframeUrlView = process.env.VUE_APP_DZBD_CK_ROUTE_PATH + "&reportOid=" + items.formDataId;
            }
          }
        }
      },
      //关闭表单预览
      closeIframeView(){
        this.iframVieweState=false;
      },
    }
  };
</script>
<style lang="scss" scoped>
  .el-tree > .el-tree-node {
    min-width: 100%;
    display: inline-block;
  }

  .process-box {
    padding: 15px;
    box-sizing: border-box;
    text-align: left;
  }

  .process-box .step-title {
    font-size: 14px;
    color: #333;
  }

  .process-box .step-title span {
    display: inline-block;
    vertical-align: middle;
    background: url(../../../../assets/image/step-lastbg.png) no-repeat center;
    width: 100px;
    height: 30px;
    line-height: 30px;
    text-align: center;
    cursor: pointer;
  }

  .process-box .step-title span:first-child {
    background: url(../../../../assets/image/step-firstbg.png) no-repeat center;
    width: 100px;
    height: 30px;
  }

  .process-box .step-title span.active {
    background: url(../../../../assets/image/step-lastbg-active.png) no-repeat center;
    width: 100px;
    height: 30px;
    color: #fff;
  }

  .process-box .step-title span:first-child.active {
    background: url(../../../../assets/image/step-firstbg-active.png) no-repeat center;
    width: 100px;
    height: 30px;
    color: #fff;
  }

  .process-box .title {
    font-size: 12px;
    color: #333;
    font-weight: normal;
    margin-top: 30px;
    text-align: left;
  }

  .select-list span {
    display: inline-block;
    vertical-align: middle;
    color: #47657d;
    font-size: 14px;
    background-color: #f1f5f9;
    height: 40px;
    line-height: 40px;
    margin: 0px 10px 10px 0px;
    padding: 0px 35px 0px 20px;
    border-radius: 5px;
    cursor: pointer;
  }

  .select-list span:first-child {
    background-color: #fff6f1;
    color: #2d506b;
  }

  .select-list span.current {
    background-color: #4d87b5;
    color: #fff;
    position: relative;
  }

  .select-list span.current:after {
    content: "";
    position: absolute;
    width: 19px;
    height: 13px;
    right: 10px;
    top: 13px;
    background: url(../../../../assets/image/check-icon.png) no-repeat center;
    background-size: cover;
  }

  .option-box .option-title {
    margin-top: 20px;
    font-size: 12px;
    color: #333;
  }

  .option-box .option-item {
    margin-right: 10px;
  }

  .option-box .option-item,
  .option-box .chose-item {
    display: inline-block;
    vertical-align: top;
  }

  .option-box .chose-item {
    color: #4d87b5;
  }

  .process-box table {
    width: 100%;
  }

  .process-box table {
    border: 1px solid #dfe6ec;
    border-collapse: collapse;
  }

  .process-box table tr td,
  .process-box table tr th {
    border: 1px solid #dfe6ec;
    padding: 17px 10px;
    font-size: 12px;
    color: #515a6e;
    text-align: right;
  }

  .process-box table tr td:nth-of-type(2n + 1) {
    background-color: #f8f8f9;
  }

  .process-box table tr td:nth-of-type(2n) {
    color: #606266;
    text-align: left;
  }

  .process-box table.data-table tr td,
  .process-box table.data-table tr th {
    text-align: center;
    padding: 12px 10px;
  }

  .process-box table.data-table tr th {
    background-color: #f8f9fb;
  }

  .process-box table.data-table tr td:nth-of-type(2n + 1) {
    background: none;
  }

  .process-box table.data-table .bage-necessery {
    color: #ff3000;
    background-color: #fff2f2;
    height: 30px;
    line-height: 30px;
    padding: 0px 20px;
    border-radius: 20px;
    display: inline-block;
  }

  .process-box .check-list {
    border-bottom: 1px solid #e5e5e5;
    margin: 0px 20px;
    padding-left: 10px;
    box-sizing: border-box;
    padding-bottom: 20px;
  }

  .process-box .check-list h3 {
    font-size: 12px;
    color: #333;
  }

  .process-box .el-form-item {
    margin-bottom: 0;
  }

  .require {
    color: #ff0000;
    font-style: normal;
    font-size: 14px;
    display: inline-block;
    vertical-align: middle;
    margin-right: 5px;
  }

  .process-box .next-btn {
    display: block;
    margin: 30px auto;
    font-size: 14px;
  }

  .process-box .data-box h4 {
    font-size: 12px;
    color: #333;
    margin: 20px 0px 10px 0px;
  }

  .process-box .btn-wrap {
    text-align: center;
    margin: 30px 0;
  }

  .process-box .form-box-inline {
    display: inline-block;
    vertical-align: middle;
    border: 1px solid #2d506b;
    width: 100%;
    padding: 30px;
    box-sizing: border-box;
  }

  .process-box .el-button--info {
    background-color: #3b5f7b;
  }

  .process-box .el-button--info:hover {
    background-color: #426886;
  }

  .process-box .form-box-inline {
    position: relative;
  }

  .process-box .form-box-inline .btn-write {
    position: absolute;
    right: 20px;
    top: 20px;
    font-size: 12px;
    padding: 8px 15px;
  }

  .process-box .form-box-inline ul {
    margin: 0;
    padding: 0;
  }

  .process-box .form-box-inline ul li {
    padding: 10px 20px;
    background-color: #f3f6f9;
    border-radius: 10px;
    margin-bottom: 10px;
    list-style: none;
  }

  .process-box .form-box-inline ul li > .icon {
    display: inline-block;
    vertical-align: middle;
    width: 36px;
    height: 36px;
    line-height: 36px;
    text-align: center;
    border-radius: 100%;
    color: #fff;
    font-size: 20px;
  }

  .process-box .form-box-inline ul li > .bdbm-icon {
    background-color: #a8cfec;
  }

  .process-box .form-box-inline ul li > .bdsm-icon {
    background-color: #e6b893;
  }

  .process-box .form-box-inline .input-text {
    display: inline-block;
    vertical-align: middle;
    padding-left: 10px;
    font-size: 12px;
    color: #333;
  }

  .process-box .form-box-inline > h4 {
    font-weight: normal;
    color: #003259;
    font-size: 14px;
  }

  .process-box .form-box-inline .input-text > h4 {
    margin: 0px 0px 5px 0px;
    color: #3b5f7b;
    font-weight: normal;
  }

  .process-box .form-box-inline .input-text > p {
    margin: 0px;
  }

  .process-box .form-btn-group {
    position: absolute;
    right: 20px;
    top: 20px;
  }

  .process-box .form-btn-group .btn {
    border: 1px solid #097dd6;
    color: #097dd6;
    font-size: 12px;
    padding: 8px 15px;
  }

  .handle-data {
    padding: 25px 40px;
    background-color: #f8f9fb;
    position: relative;
  }

  .handle-data ul {
    padding: 0px;
    margin: 0;
  }

  .handle-data ul li {
    list-style: none;
    text-align: left;
    margin-top: 10px;
  }

  .right-btn-group {
    position: absolute;
    right: 130px;
    top: 10px;
  }

  .right-btn-group-two {
    position: absolute;
    right: 10px;
    top: 10px;
  }

  .right-btn-group .el-button {
    padding: 6px 8px;
    font-size: 12px;
    margin-left: 5px;
    background-color: #0ba2b8;
    border: 1px solid #0ba2b8;
  }

  .right-btn-group .el-button:last-child {
    background-color: #47657d;
    border: 1px solid #47657d;
  }

  .right-btn-group .el-button:last-child:hover {
    background-color: #4d708b;
    border: 1px solid #4d708b;
  }

  .qdcg-success {
    font-size: 12px;
    color: #00b45e;
    margin-top: 5px;
    text-decoration: underline;
  }

  .qdcg-fail {
    font-size: 12px;
    color: #ff0000;
    margin-top: 5px;
    text-decoration: underline;
  }

  .qdcg-success > img,
  .qdcg-fail > img {
    display: inline-block;
    vertical-align: middle;
    margin: -2px 5px 0 0;
  }

  .qdcg-text img,
  .qdcg-text p {
    display: inline-block;
    vertical-align: middle;
  }

  .qdcg-text img {
    margin-right: 5px;
  }

  .qdcg-text p {
    margin: 0;
  }

  .qdcg-text p > span {
    font-size: 12px;
    color: #999;
    padding-left: 20px;
  }

  .qdcg-btn .el-button {
    color: #333;
  }

  .qdcg-btn {
    margin-top: -5px;
  }

  .input-number {
    text-align: left;
    margin-top: 20px;
    margin-bottom: 10px;
  }

  .input-number-label {
    display: inline-block;
    vertical-align: middle;
    margin-right: 20px;
  }

  .rightSideBar {
    position: fixed;
    right: 0px;
    bottom: 100px;
    box-shadow: -1px 1px 10px #ddd;
    border-top-left-radius: 5px;
    border-bottom-left-radius: 5px;
  }

  .rightSideBar .sideItem {
    width: 42px;
    height: 42px;
    line-height: 42px;
    text-align: center;
    color: #0b76c7;
    background-color: #fff;
    border-top-left-radius: 5px;
    cursor: pointer;
    font-size: 20px;
    position: relative;
    -webkit-transition: all 0.3s ease-in-out 0.1s;
    transition: all 0.3s ease-in-out 0.1s;
    z-index: 2;
  }

  .rightSideBar .sideItem:last-child {
    border-top-left-radius: 0px;
    border-bottom-left-radius: 5px;
  }

  .rightSideBar .sideItem:hover {
    background-color: #0b76c7;
    color: #fff;
    border-top-left-radius: 0px;
    border-bottom-left-radius: 0px;
  }

  .rightSideBar .sideItem .sideText {
    position: absolute;
    left: 42px;
    top: 0px;
    height: 42px;
    line-height: 42px;
    padding: 0px 10px 0px 20px;
    border-top-left-radius: 30px;
    border-bottom-left-radius: 30px;
    color: #fff;
    background-color: #0b76c7;
    font-size: 14px;
    -webkit-transition: left 0.3s ease-in-out 0.1s;
    transition: left 0.3s ease-in-out 0.1s;
    z-index: 1;
  }

  .rightSideBar .sideItem:hover .sideText {
    left: -86px;
  }

  .title-detail {
    text-align: center;
  }
</style>
