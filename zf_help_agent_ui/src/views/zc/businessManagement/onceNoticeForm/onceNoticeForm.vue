/** * 一次性告知单页面 * @author: wangwg * @date: 2020-11-21 */
<template>
  <div class="app-container">
    <!-- 步骤 -->
    <div class="process-box">
      <div class="step-title">
        <span
          v-for="(item, index) in stepData"
          :key="item.index"
          :class="{ active: index == i }"
        >
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
              @click="selectChange(index, item)"
              >{{ item.title }}</span
            >
          </div>
        </div>
        <div class="option-box">
          <div class="option-title">
            <div class="option-item">
              <i class="el-icon-s-grid"></i>选项信息
            </div>
            <div class="option-item">
              <i class="el-icon-s-grid"></i>
              <div class="chose-item">
                [<span>{{ situationName }}</span
                >]
              </div>
              情景选项信息
            </div>
          </div>
        </div>
        <el-form
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="0px"
          class="demo-ruleForm"
          :label-position="labelPosition"
        >
          <table
            cellspacing="0"
            cellpadding="0"
            border="0"
            class="el-table__body mt20"
          >
            <colgroup>
              <col width="15%" />
            </colgroup>
            <tr>
              <td><b>选项名称：</b></td>
              <td colspan="3">
                <div v-if="sxServiceOptionTitles.length > 0">
                  <div
                    v-for="(data, index) in sxServiceOptionTitles"
                    :key="index"
                  >
                    <div
                      class="check-list"
                      v-if="
                        data.isHavingCorrelation == 0 ||
                          data.isHavingCorrelation == 2
                      "
                    >
                      <h3>{{ data.titleName }}</h3>
                      <template v-if="data.moreStatus == '0'">
                        <el-radio-group v-model="checkList[index]">
                          <el-radio
                            v-for="item in data.sxServiceOptionVals"
                            :key="item.oid"
                            :disabled="ifDis"
                            :label="item.oid"
                            @change="changeTitle(data.titleOid, item.oid)"
                            >{{ item.name }}</el-radio
                          >
                        </el-radio-group>
                      </template>
                      <template v-if="data.moreStatus == '1'">
                        <el-checkbox-group v-model="checkBoxList">
                          <el-checkbox
                            v-for="item in data.sxServiceOptionVals"
                            :disabled="ifDis"
                            :key="item.oid"
                            @change="changeTitleBox(item, item.oid)"
                            :id="item.oid"
                            :label="item.oid"
                            >{{ item.name }}</el-checkbox
                          >
                        </el-checkbox-group>
                      </template>
                    </div>
                  </div>
                </div>
                <div v-else>暂无数据</div>
              </td>
            </tr>
          </table>
        </el-form>
        <el-button
          type="primary"
          icon="el-icon-circle-check"
          class="next-btn"
          @click="next(1, 1)"
          >下一步
        </el-button>
      </div>

      <!-- 第二步 -->
      <div class="step-content step-second" v-if="show_1">
        <div class="situation-box">
          <el-form
            :model="ruleForm"
            ref="ruleForm"
            label-width="0px"
            class="demo-ruleForm"
            :label-position="labelPosition"
            style="border-bottom: 1px solid #eeeeee; margin-bottom: 10px"
          >
            <div id="print">
              <h3
                class="title-detail"
                style="
                  display: block;
                  font-size: 0.9em;
                  margin-block-start: 1em;
                  margin-block-end: 1em;
                  margin-inline-start: 0px;
                  margin-inline-end: 0px;
                  font-weight: bold;
                "
              >
                {{ ruleForm.serviceName }}告知单
              </h3>
              <table
                cellspacing="0"
                cellpadding="0"
                class="el-table__body"
                style="border-collapse: collapse; border: 1px solid #dfe6ec"
              >
                <colgroup>
                  <col width="15%" />
                  <col width="35%" />
                  <col width="15%" />
                  <col width="35%" />
                </colgroup>
                <tr style="line-height: 1.15">
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>事项名称：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.serviceName }}
                  </td>
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>事项类型：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.serviceTypeName }}
                  </td>
                </tr>
                <tr style="line-height: 1.15">
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>实施机构：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.organName }}
                  </td>
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>办理地点：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    <div
                      v-for="(data, index) in ruleForm.sxServiceLocations"
                      :label="data.locationName"
                      :key="index"
                    >
                      {{ index + 1 }}、{{ data.locationName }}
                    </div>
                  </td>
                </tr>
                <tr style="line-height: 1.15">
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>办理时间：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    <div
                      v-for="(data, index) in ruleForm.sxServiceLocations"
                      :label="data.acceptDate"
                      :key="index"
                    >
                      {{ index + 1 }}、{{ data.acceptDate }}
                    </div>
                  </td>
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>咨询电话：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.zxDhText }}
                  </td>
                </tr>
                <tr style="line-height: 1.15">
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>承诺时限：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    <template>
                      {{ ruleForm.sxServiceExtend.promiseLimit }}
                      <span
                        v-if="ruleForm.sxServiceExtend.promiseLimitType == 'W'"
                      >
                        (工作日)</span
                      >
                      <span
                        v-if="ruleForm.sxServiceExtend.promiseLimitType == 'N'"
                      >
                        (自然日)</span
                      >
                      <span
                        v-if="ruleForm.sxServiceExtend.promiseLimitType == 'H'"
                      >
                        (小时)</span
                      >
                    </template>
                  </td>
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>法定时限：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    <template>
                      {{ ruleForm.sxServiceExtend.legalLimit }}
                      <span
                        v-if="ruleForm.sxServiceExtend.legalLimitType == 'W'"
                      >
                        (工作日)</span
                      >
                      <span
                        v-if="ruleForm.sxServiceExtend.legalLimitType == 'N'"
                      >
                        (自然日)</span
                      >
                      <span
                        v-if="ruleForm.sxServiceExtend.legalLimitType == 'H'"
                      >
                        (小时)</span
                      >
                    </template>
                  </td>
                </tr>
                <tr style="line-height: 1.15">
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>申请受理条件：</b>
                  </td>
                  <td colspan="3" style="font-size: 12px; color: #515a6e">
                    <div
                      v-for="(data, index) in ruleForm.sxAcceptConditions"
                      :label="data.conditionText"
                      :key="index"
                    >
                      <p v-if="ruleForm.sxAcceptConditions.length > 1">
                        {{ index + 1 }}、{{ data.conditionText }}
                      </p>
                      <p v-else>
                        {{ data.conditionText }}
                      </p>
                    </div>
                  </td>
                </tr>
                <tr style="line-height: 1.15">
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>事项情形：</b>
                  </td>
                  <td colspan="3" style="font-size: 12px; color: #515a6e">
                    {{ serviceSituationName }}
                  </td>
                </tr>
                <tr style="line-height: 1.15">
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>情形选项：</b>
                  </td>
                  <td colspan="3" style="font-size: 12px; color: #515a6e">
                    <div v-for="(data, idx) in serviceTitleValList" :key="idx">
                      {{ data.titleName }}【{{ data.valueName }}】
                    </div>
                  </td>
                </tr>
                <tr style="line-height: 1.15">
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>申请材料：</b>
                  </td>
                  <td colspan="3" style="font-size: 12px; color: #515a6e">
                    <div
                      v-for="(data, index) in sxServiceMaterialList"
                      :label="data.materialName"
                      :key="index"
                    >
                      <span v-if="sxServiceMaterialList.length > 1">
                        {{ index + 1 }}、{{ data.materialName }}
                      </span>
                      <span v-else>
                        {{ data.materialName }}
                      </span>
                    </div>
                  </td>
                </tr>
                <tr style="line-height: 1.15">
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>办事流程：</b>
                  </td>
                  <td colspan="3" style="font-size: 12px; color: #515a6e">
                    <div
                      v-for="(data, index) in ruleForm.sxServiceLinks"
                      :label="data.linkName"
                      :key="index"
                    >
                      <p v-if="ruleForm.sxServiceLinks.length > 1">
                        {{ index + 1 }}、{{ data.linkName }}
                      </p>
                      <p v-else>
                        {{ data.linkName }}
                      </p>
                    </div>
                  </td>
                </tr>
                <tr style="line-height: 1.15">
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>常见问题：</b>
                  </td>
                  <td colspan="3" style="font-size: 12px; color: #515a6e">
                    <div
                      v-for="(data, index) in ruleForm.sxServiceQuestions"
                      :label="data.title"
                      :key="index"
                    >
                      <p v-if="ruleForm.sxServiceQuestions.length > 1">
                        {{ index + 1 }}、{{ data.title }}
                      </p>
                      <p v-else>
                        {{ data.title }}
                      </p>
                    </div>
                  </td>
                </tr>
              </table>
            </div>
          </el-form>
          <div class="btn-wrap">
            <div class="btn-list mt10">
              <el-button
                type="primary"
                v-show="step_show"
                icon="el-icon-circle-check"
                @click="next(0, -1)"
                >上一步
              </el-button>
              <el-button
                type="info"
                icon="el-icon-video-pause"
                class="print-btn"
                v-print="printObj"
                >打印一次性告知单
              </el-button>
            </div>
          </div>
        </div>
      </div>
      <!-- 右侧边栏 -->
      <!--<div class="rightSideBar" v-if="rightShow">
        <div class="sideItem">
          <i class="el-icon-edit-outline"></i>
          <div class="sideText">查看事项</div>
        </div>
      </div>-->
    </div>
  </div>
</template>
<script>
import {
  getAllSituationList,
  getSxService,
  getSituationOpinionList,
  queryOptionTitleValStr,
  getSxServiceOptionAllTitleValRelation,
  getSituationMaterialListByOids,
  getSxMaterialByServiceOid
} from "@/api/zc/businessManagement/windowAcceptance";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { VueCropper } from "vue-cropper";

export default {
  inheritAttrs: false,
  components: { Treeselect, VueCropper },
  props: ["serviceOid"],
  data() {
    return {
      printObj: {
        id: "print",
        popTitle: "一次性告知单",
        extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>'
      },
      i: 0,
      num: 0,
      show_0: false,
      show_1: false,
      rightShow: true,
      step_show: true,
      show_title: [],
      radio: "",
      radio1: [],
      radio2: "1",
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 业务办理事项数据
      serviceList: [],
      // 事项情形标题数据
      situationTitleList: [],
      // 事项情形与选项关系数据
      situationOptions: [],
      // 事项选项值数据
      situationOptionVals: [],
      //事项材料
      sxServiceMaterialList: [],
      checkTitleValListBox: [],
      //登录信息
      loginUser: {},
      ifDis: false,
      //办理地点
      sxServiceLocations: [],
      //事项材料
      sxServiceMaterials: [],
      mateOptRels: [],
      materialOptList: [],
      //事项问题
      sxServiceQuestions: [],
      //事项环节
      sxServiceLinks: [],
      //事项扩展
      sxServiceExtend: null,
      //事项情形
      sxServiceSituations: [],
      //情形标题
      sxServiceOptionTitles: [],
      checkBoxList: [],
      //情形标题和选项值
      situationOptionsTitleValues: [],
      tempTitleOids: [],
      situationName: "默认自定情形",
      //事项情形显示信息
      serviceSituationName: "暂无",
      serviceTitleValList: [],
      serviceValueList: [],
      // 弹出层标题
      title: "",
      labelPosition: "top",
      checkList: [],
      stepData: [],
      selectData: [],
      ruleForm: {
        serviceName: "",
        serviceTypeName: "",
        organName: "",
        sxServiceLocations: {},
        zxDhText: "",
        sxServiceExtend: {},
        sxAcceptConditions: {},
        sxServiceLinks: {},
        sxServiceQuestions: {}
      },
      // 表单校验
      rules: {
        projectName: [
          { required: true, message: "必填项", trigger: "blur" },
          {
            min: 3,
            max: 100,
            message: "长度在 3 到 100 个字符",
            trigger: "blur"
          }
        ]
      }
    };
  },
  watch: {},
  created() {
    this.getServiceSituationList();
    this.getServiceDetail();
  },
  methods: {
    next(index, count) {
      this.i = index;
      if (count > 0) {
        this.show_0 = false;
        this.show_1 = true;
        this.getMaterialList();
      } else {
        this.show_0 = true;
        this.show_1 = false;
      }
    },
    /** 选择情形 */
    selectChange(index, item) {
      let _that = this;
      _that.num = index;
      _that.situationName = item.title;
      _that.checkList = [];
      _that.checkBoxList = [];
      _that.sxServiceMaterials = [];
      _that.sxServiceOptionTitles = [];
      _that.serviceTitleValList = [];
      if (item.oid == undefined) {
        _that.getServiceSituationList();
        _that.ifDis = false;
      } else {
        _that.ifDis = true;
        getSituationOpinionList(item.oid).then(response => {
          _that.serviceSituationName = item.title;
          //情形
          let optionTitle =
            response.data.sxServiceSituationOption.sxServiceOptionTitles;
          if (optionTitle != null && optionTitle != "") {
            optionTitle.forEach(item => {
              //情形选项值
              let titleValues = {};
              titleValues.titleName = item.name;
              titleValues.titleOid = item.oid;
              titleValues.moreStatus = item.moreStatus;
              titleValues.fillFlag = item.fillFlag;
              titleValues.isHavingCorrelation = item.isHavingCorrelation;
              titleValues.sxServiceOptionVals = item.sxServiceOptionVals;
              _that.sxServiceOptionTitles.push(titleValues);
              if (
                item.sxServiceOptionVals != null &&
                item.sxServiceOptionVals != ""
              ) {
                item.sxServiceOptionVals.forEach(data => {
                  if (data.isSelected == "1") {
                    _that.checkList.push(data.oid);
                    if (item.isHavingCorrelation == 0) {
                      _that.show_title[item.oid] = true;
                    } else {
                      _that.show_title[item.oid] = false;
                    }
                    //放入选中的标题和选项值
                    let titlevalue = {};
                    titlevalue.titleName = item.name;
                    titlevalue.valueName = data.name;
                    _that.serviceTitleValList.push(titlevalue);
                  }
                });
              }
            });
          }
        });
      }
    },
    /** 事项情形获取 */
    getServiceSituationList() {
      let _that = this;
      //查询事项情形
      getAllSituationList(_that.serviceOid).then(response => {
        let situations = response.data.sxServiceSituations;
        let optionTitles = response.data.sxServiceOptionTitles;
        _that.getSituationOpinion(situations);
        _that.getSituationOpinionTitle(optionTitles);
        console.log(JSON.stringify(optionTitles));
        /** 一次性告知 */
        if (
          _that.sxServiceOptionTitles == undefined ||
          _that.sxServiceOptionTitles == ""
        ) {
          _that.stepData = [{ index: "0", title: "一次性告知" }];
          _that.show_0 = false;
          _that.show_1 = true;
          _that.step_show = false;
          _that.getServiceDetail();
          _that.getMaterialList();
        } else {
          _that.show_0 = true;
          _that.show_1 = false;
          _that.step_show = true;
          _that.stepData = [
            { index: "0", title: "情形选择" },
            { index: "1", title: "一次性告知" }
          ];
        }
      });
    },
    /** 填充选项 */
    getSituationOpinion(situations) {
      let _that = this;
      _that.selectData = [{ index: "0", title: "默认自定情形" }];
      situations.forEach((item, key) => {
        let situation = {};
        situation.index = key + 1;
        situation.title = item.situationName;
        situation.oid = item.oid;
        _that.selectData.push(situation);
      });
    },
    /** 填充标题和选项值 */
    getSituationOpinionTitle(optionTitles) {
      let _that = this;
      optionTitles.forEach((optionTitle, index) => {
        let titleValues = {};
        titleValues.titleName = optionTitle.name;
        titleValues.titleOid = optionTitle.oid;
        titleValues.moreStatus = optionTitle.moreStatus;
        titleValues.fillFlag = optionTitle.fillFlag;
        titleValues.isHavingCorrelation = optionTitle.isHavingCorrelation;
        titleValues.sxServiceOptionVals = optionTitle.sxServiceOptionVals;
        _that.sxServiceOptionTitles.push(titleValues);
      });
    },
    /** 改变选项值 */
    changeTitle(titleOid, optionValOid) {
      let _that = this;
      _that.serviceTitleValList = [];
      let val = {};
      val.titleOid = titleOid;
      val.valueOid = optionValOid;
      _that.serviceValueList.forEach(data => {
        if (data.titleOid == titleOid) {
          _that.serviceValueList.splice(
            _that.serviceValueList.indexOf(data),
            1
          );
        }
      });
      _that.serviceValueList.push(val);
      _that.serviceValueList.forEach(data => {
        //获取标题选项信息
        queryOptionTitleValStr(data.valueOid).then(response => {
          let _that = this;
          _that.serviceTitleValList.push(response.data);
        });
      });
      //点击选项值 展示隐藏对应的标题
      getSxServiceOptionAllTitleValRelation(_that.serviceOid, titleOid).then(
        response => {
          response.data.forEach(data => {
            //当前点击的选项下的有关联的标题设置显示
            if (data.valOid == optionValOid) {
              data.titleList.forEach(title => {
                _that.sxServiceOptionTitles.forEach(optionTitle => {
                  if (optionTitle.titleOid == title.oid) {
                    optionTitle.isHavingCorrelation = 2;
                  }
                });
              });
            } else {
              //设置除了点击的标题选项之外的选项有关联的标题设置隐藏
              data.titleList.forEach(title => {
                _that.sxServiceOptionTitles.forEach(optionTitle => {
                  if (optionTitle.titleOid == title.oid) {
                    optionTitle.isHavingCorrelation = 1;
                  }
                });
              });
            }
          });
        }
      );
    },
    /** 获取事项详细信息 */
    getServiceDetail() {
      let _that = this;
      getSxService(_that.serviceOid).then(response => {
        _that.ruleForm.serviceName = response.data.sxService.serviceName;
        _that.ruleForm.serviceTypeName =
          response.data.sxService.serviceTypeName;
        _that.ruleForm.organName = response.data.sxService.organName;
        _that.ruleForm.sxServiceLocations = response.data.sxServiceLocations;
        _that.ruleForm.zxDhText = response.data.sxService.zxDhText;
        _that.ruleForm.sxServiceExtend = response.data.sxServiceExtend;
        _that.ruleForm.sxAcceptConditions = response.data.sxAcceptConditions;
        _that.ruleForm.sxServiceLinks = response.data.sxServiceLinks;
        _that.ruleForm.sxServiceQuestions = response.data.sxServiceQuestions;
      });
    },
    /** 填充材料 */
    getMaterialList() {
      let _that = this;
      if (this.checkBoxList.length > 0) {
        this.checkBoxList.forEach(checkBox => {
          this.checkList.push(checkBox);
        });
      }
      if (_that.checkList && _that.checkList.length > 0) {
        //根据被选中的选项获取关联材料
        getSituationMaterialListByOids(_that.serviceOid, _that.checkList).then(
          response => {
            if (response.data.length > 0) {
              this.sxServiceMaterialList = response.data;
            }
          }
        );
      } else {
        //查询事项材料
        getSxMaterialByServiceOid(_that.serviceOid).then(response => {
          if (response.data.length > 0) {
            this.sxServiceMaterialList = response.data;
          }
        });
      }
    },
    changeTitleBox(item, optionValOid) {
      let _that = this;
      let flag = true; //添加标题选项
      let val = {};
      val.titleOid = item.titleOid;
      val.valueOid = item.oid;
      _that.serviceValueList.forEach(data => {
        if (data.titleOid == item.titleOid && data.valueOid == item.oid) {
          _that.serviceValueList.splice(
            _that.serviceValueList.indexOf(data),
            1
          );
          flag = false;
          //获取标题选项信息
          queryOptionTitleValStr(data.valueOid).then(response => {
            let _that = this;
            _that.serviceTitleValList.forEach((ss, j) => {
              if (
                ss.titleName == response.data.titleName &&
                ss.valueName == response.data.valueName
              ) {
                _that.serviceTitleValList.splice(j, 1);
              }
            });
          });
        }
      });
      if (flag) {
        _that.serviceValueList.push(val);
        //获取标题选项信息
        queryOptionTitleValStr(val.valueOid).then(response => {
          let _that = this;
          _that.serviceTitleValList.push(response.data);
        });
        flag = true;
      }

      //点击选项值 展示隐藏对应的标题
      getSxServiceOptionAllTitleValRelation(
        _that.serviceOid,
        item.titleOid
      ).then(response => {
        response.data.forEach(data => {
          //当前点击的选项下的有关联的标题设置显示
          if (data.valOid == item.oid) {
            data.titleList.forEach(title => {
              _that.sxServiceOptionTitles.forEach(optionTitle => {
                if (optionTitle.titleOid == title.oid) {
                  optionTitle.isHavingCorrelation = 2;
                }
              });
            });
          } else {
            //设置除了点击的标题选项之外的选项有关联的标题设置隐藏
            data.titleList.forEach(title => {
              _that.sxServiceOptionTitles.forEach(optionTitle => {
                if (optionTitle.titleOid == title.oid) {
                  optionTitle.isHavingCorrelation = 1;
                }
              });
            });
          }
        });
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      let _that = this;
      _that.resetForm("queryForm");
      _that.handleQuery();
    },
    /** 隐藏右侧 */
    hiddenRightSideBar() {
      let _that = this;
      _that.rightShow = false;
    },
    /** 显示右侧 */
    showRightSideBar() {
      let _that = this;
      _that.rightShow = true;
    },
    /** 表单重置 */
    reset() {
      let _that = this;
      Object.assign(_that.queryForm, _that.$options.data().queryForm);
      _that.resetForm("queryForm");
    },
    /** 取消按钮 */
    cancel() {
      let _that = this;
      _that.openInit = false;
      _that.reset();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      let _that = this;
      _that.queryParams.pageNum = 1;
      _that.getList();
    },
    /** 关闭方法 */
    closeDialog() {
      let _that = this;
      _that.sxServiceMaterials = [];
      _that.checkList = [];
      _that.materialOptList = [];
      _that.mateOptRels = [];
      _that.sxServiceOptionTitles = [];
    },
    /** 点击打印按钮 */
    clickPrinting() {
      //  找到需要隐藏的DOM节点
      let step_title = document.getElementsByClassName("step-title")[0];
      let btn_wrap = document.getElementsByClassName("btn-wrap")[0];
      //  给对应DOM添加class
      step_title.classList.add("printHideCss");
      btn_wrap.classList.add("printHideCss");
    }
  }
};
</script>
<style lang="scss" scoped>
.el-tree > .el-tree-node {
  min-width: 100%;
  display: inline-block;
}
// 利用page去掉打印下面的链接
@page {
  margin-bottom: 1mm;
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
  background: url(../../../../assets/image/step-lastbg-active.png) no-repeat
    center;
  width: 100px;
  height: 30px;
  color: #fff;
}

.process-box .step-title span:first-child.active {
  background: url(../../../../assets/image/step-firstbg-active.png) no-repeat
    center;
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

.printHideCss {
  display: none;
}
</style>
