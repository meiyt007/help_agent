/**
* 一次性告知单页面
* @author: wangxl
* @date: 2020-12-2
*/
<template>
  <div>
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
                <div v-if="direSituationOptionTitles.length > 0">
                  <div v-for="(data, index) in direSituationOptionTitles">
                    <div class="check-list">
                      <h3>{{ data.titleName }}</h3>
                      <template v-if="data.moreStatus == '0'">
                        <el-radio-group v-model="checkList[index]">
                          <el-radio
                            v-for="item in data.comboOptionVals"
                            :disabled="ifDis"
                            :label="item.valOid"
                            @change="
                              changeTitle(
                                data.titleName,
                                item.name,
                                data.titleOid,
                                item.valOid,
                                index
                              )
                            "
                            >{{ item.name }}</el-radio
                          >
                        </el-radio-group>
                        <p v-if="zwbk[index]">
                          所选项有相关政务百科信息，<a
                            @click="zwbkInfoPage(zwbkInfo[index])"
                            style="color: #0e9aef"
                            >点击查看</a
                          >
                        </p>
                      </template>
                      <template v-if="data.moreStatus == '1'">
                        <el-checkbox-group v-model="checkList">
                          <el-checkbox
                            :disabled="ifDis"
                            v-for="item in data.comboOptionVals"
                            :key="item.valOid"
                            @change="
                              changeTitleBox(
                                data.titleName,
                                item.name,
                                item,
                                item.valOid,
                                index
                              )
                            "
                            :id="item.valOid"
                            :label="item.valOid"
                            >{{ item.name }}</el-checkbox
                          >
                        </el-checkbox-group>
                        <p v-if="zwbk[index]">
                          所选项有相关政务百科信息，<a
                            @click="zwbkInfoPage(zwbkInfo[index])"
                            style="color: #0e9aef"
                            >点击查看</a
                          >
                        </p>
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
          >
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
              {{ ruleForm.comboDirectory.comboDirectoryName }}告知单
            </h3>
            <div id="print">
              <table
                cellspacing="0"
                cellpadding="0"
                border="0"
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
                    <b>一件事目录名称：</b>
                  </td>
                  <td colspan="3" style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.comboDirectory.comboDirectoryName }}
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
                    <b>所属一件事分类：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.comboDirectory.themeName }}
                  </td>
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>所属区划：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.comboDirectory.districtName }}
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
                    <b>服务对象：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.comboDirectory.comboServiceObjectName }}
                  </td>
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>主办部门：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.comboDirectory.mainOrganName }}
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
                    <b>协办部门：</b>
                  </td>
                  <td colspan="3" style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.comboDirectory.assistOrganName }}
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
                    <b>线下跑动次数：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.comboDirectory.countToScence }}
                  </td>
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>是否收费：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    <span v-if="ruleForm.comboDirectory.ifCharge == 1">是</span>
                    <span v-else>否</span>
                  </td>
                </tr>
                <tr
                  v-if="ruleForm.comboDirectory.ifCharge == 1"
                  style="line-height: 1.15"
                >
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>收费标准：</b>
                  </td>
                  <td colspan="3" style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.comboDirectory.chargeStandard }}
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
                    <b>承诺时限(工作日)：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.comboDirectory.promiseLimit }}
                  </td>
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>法定时限(工作日)：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.comboDirectory.legalLimit }}
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
                    <b>办理形式：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    <span v-if="ruleForm.comboDirectory.handleForm == 0"
                      >窗口办理</span
                    >
                    <span v-if="ruleForm.comboDirectory.handleForm == 1"
                      >网上办理</span
                    >
                    <span v-if="ruleForm.comboDirectory.handleForm == 2"
                      >一体化办理</span
                    >
                  </td>
                  <td
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: right;
                    "
                  >
                    <b>支持物流快递：</b>
                  </td>
                  <td style="font-size: 12px; color: #515a6e">
                    <span v-if="ruleForm.comboDirectory.expressFlag == 0"
                      >否</span
                    >
                    <span v-if="ruleForm.comboDirectory.expressFlag == 1"
                      >是</span
                    >
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
                    <b>咨询方式：</b>
                  </td>
                  <td colspan="3" style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.comboDirectory.zixunType }}
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
                  <td colspan="3" style="font-size: 12px; color: #515a6e">
                    {{ ruleForm.comboDirectory.manageTime }}
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
                    {{ ruleForm.comboDirectory.declareNeedKnow }}
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
                    <b>相关事项：</b>
                  </td>
                  <td colspan="3" style="font-size: 12px; color: #515a6e">
                    <div
                      v-for="(ser, k) in comboServices"
                      :label="ser.serviceName"
                      :key="k"
                    >
                      <span v-if="comboServices.length > 1">
                        {{ k + 1 }}、{{ ser.serviceName }}
                      </span>
                      <span v-else>
                        {{ ser.serviceName }}
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
                    {{ ruleForm.comboDirectory.handleDesc }}
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
                    <b>证照名称：</b>
                  </td>
                  <td colspan="3" style="font-size: 12px; color: #515a6e">
                    {{ jgInfo }}
                  </td>
                </tr>
              </table>
              <table
                cellspacing="0"
                cellpadding="0"
                border="0"
                class="el-table__body"
                style="border-collapse: collapse; border: 1px solid #dfe6ec"
                v-if="
                  null != comboDireMaterials && comboDireMaterials.length > 0
                "
              >
                <tr>
                  所需材料列表
                </tr>
                <colgroup>
                  <col width="10%" />
                  <col width="30%" />
                  <col width="15%" />
                  <col width="15%" />
                  <col width="15%" />
                </colgroup>
                <tr>
                  <th
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: center;
                    "
                  >
                    序号
                  </th>
                  <th
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: center;
                    "
                  >
                    材料名称
                  </th>
                  <th
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: center;
                    "
                  >
                    材料类型
                  </th>
                  <th
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: center;
                    "
                  >
                    材料形式
                  </th>
                  <th
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: center;
                    "
                  >
                    份数
                  </th>
                  <th
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: center;
                    "
                  >
                    必要性
                  </th>
                </tr>
                <tbody>
                  <tr v-for="(data, index) in comboDireMaterials">
                    <td
                      style="
                        font-size: 12px;
                        color: #515a6e;
                        text-align: center;
                      "
                    >
                      {{ index + 1 }}
                    </td>
                    <td
                      style="
                        font-size: 12px;
                        color: #515a6e;
                        text-align: center;
                      "
                    >
                      {{ data.materialName }}
                    </td>
                    <td
                      style="
                        font-size: 12px;
                        color: #515a6e;
                        text-align: center;
                      "
                    >
                      <template>
                        <span v-if="data.materialType == 0">原件</span>
                        <span v-if="data.materialType == 1">复印件</span>
                        <span v-if="data.materialType == 2"
                          >原件或者复印件</span
                        >
                      </template>
                    </td>
                    <td
                      style="
                        font-size: 12px;
                        color: #515a6e;
                        text-align: center;
                      "
                    >
                      <template>
                        <span v-if="data.materialFormat == 1">纸质</span>
                        <span v-if="data.materialFormat == 2">电子版</span>
                      </template>
                    </td>
                    <td
                      style="
                        font-size: 12px;
                        color: #515a6e;
                        text-align: center;
                      "
                    >
                      {{ data.paperNumber }}
                    </td>
                    <td
                      style="
                        font-size: 12px;
                        color: #515a6e;
                        text-align: center;
                      "
                    >
                      <template>
                        <span v-if="data.mustFlag == 0">非必须</span>
                        <span v-if="data.mustFlag == 1"
                          ><span class="bage-necessery">必须</span></span
                        >
                        <span v-if="data.mustFlag == 2">容缺后补</span>
                      </template>
                    </td>
                  </tr>
                </tbody>
              </table>
              <table
                cellspacing="0"
                cellpadding="0"
                border="0"
                class="el-table__body"
                style="border-collapse: collapse; border: 1px solid #dfe6ec"
                v-if="null != serviceSpecial && serviceSpecial.length > 0"
              >
                <tr>
                  特别程序
                </tr>
                <colgroup>
                  <col width="10%" />
                  <col width="30%" />
                  <col width="30%" />
                  <col width="30%" />
                </colgroup>
                <tr>
                  <th
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: center;
                    "
                  >
                    序号
                  </th>
                  <th
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: center;
                    "
                  >
                    特别程序类型
                  </th>
                  <th
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: center;
                    "
                  >
                    时限
                  </th>
                  <th
                    style="
                      padding: 17px 10px;
                      font-size: 12px;
                      color: #515a6e;
                      text-align: center;
                    "
                  >
                    时限类型
                  </th>
                </tr>
                <tbody>
                  <tr v-for="(data, index) in serviceSpecial">
                    <td
                      style="
                        font-size: 12px;
                        color: #515a6e;
                        text-align: center;
                      "
                    >
                      {{ index + 1 }}
                    </td>
                    <td
                      style="
                        font-size: 12px;
                        color: #515a6e;
                        text-align: center;
                      "
                    >
                      <template>
                        {{ specialName(data.specialTypeOid) }}
                      </template>
                    </td>
                    <td
                      style="
                        font-size: 12px;
                        color: #515a6e;
                        text-align: center;
                      "
                    >
                      {{ data.specialTime }}
                    </td>
                    <td
                      style="
                        font-size: 12px;
                        color: #515a6e;
                        text-align: center;
                      "
                    >
                      <template>
                        <span v-if="data.specialTimeType == 'W'">工作日</span>
                        <span v-if="data.specialTimeType == 'N'">自然日</span>
                      </template>
                    </td>
                  </tr>
                </tbody>
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
    </div>
    <!--  政务百科查看  -->
    <el-dialog
      :title="title"
      :visible.sync="openView"
      :close-on-click-modal="false"
      width="70%"
      append-to-body
    >
      <el-form :model="form">
        <el-table :data="zwbkList" border>
          <el-table-column label="序号" width="50" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="百科词条名称"
            width="200"
            align="center"
            prop="caryopterisName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="百科词条类型"
            width="100"
            align="center"
            :formatter="getPublishName"
            prop="caryopterisType"
          />
          <el-table-column
            label="创建时间"
            width="200"
            align="center"
            prop="createDate"
          />
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                v-hasPermi="['sys:pbpjInformation:view']"
                >查看</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="zwbkParam.pageNum"
          :limit.sync="zwbkParam.pageSize"
          @pagination="zwbkInfoPage"
        />
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!-- 政务百科信息详细 -->
    <el-dialog
      :title="title"
      :visible.sync="openInfoView"
      width="800px"
      append-to-body
    >
      <div class="el-table__header-wrapper dialog-table">
        <el-form ref="form" :model="form" label-width="0px">
          <table cellspacing="0" cellpadding="0" border="0">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td>
                <i class="require">*</i>
                <b>百科词条名称：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="caryopterisName">
                  <el-col :span="24">
                    {{ form.caryopterisName }}
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b><i class="require">*</i>词条类型：</b>
              </td>
              <td colspan="">
                <template v-if="form.caryopterisType === 1">
                  文本内容
                </template>
                <template v-else> 图片 </template>
              </td>
              <td>
                <b><i class="require">*</i>排序号：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sort">
                  <el-col :span="24">
                    {{ form.sort }}
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <template v-if="form.caryopterisType === 2">
              <tr>
                <td colspan="4">
                  <span>{{ attaTitle }}</span>
                  <el-link type="primary" @click="downloadFile(form.attaOid)"
                    >下载</el-link
                  >
                  |
                  <el-link type="primary" @click="viewFileNew(form.attaOid)"
                    >预览</el-link
                  >
                </td>
              </tr>
            </template>
            <template v-if="form.caryopterisType === 1">
              <tr>
                <td>
                  <b>内容：</b>
                </td>
                <td colspan="3">
                  <el-form-item prop="caryopterisContext">
                    {{ form.caryopterisContext }}
                  </el-form-item>
                </td>
              </tr>
            </template>
            <tr>
              <td>
                <i class="require"></i>
                <b>引用出处链接：</b>
              </td>
              <td colspan="3">
                <el-row>
                  <el-col :span="24">
                    <el-form-item prop="linkAddress">
                      {{ form.linkAddress }}
                    </el-form-item>
                  </el-col>
                </el-row>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openInfoView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!--引入文件的预览弹出层-->
    <el-dialog
      v-dialog-drag
      title="文件预览"
      :visible.sync="view.show"
      v-for="view in viewDialogOptions"
      @close="closeFileView"
      width="60%"
      append-to-body
    >
      <file-view
        :attaOid="view.attaOid"
        @father-click="closeFileView"
      ></file-view>
    </el-dialog>
  </div>
</template>
<script>
import { getSituationList, getSituationOpinionList, queryComboOptionTitleByValOid, getComboDireDetail, getSpecialName, pageZwbk, queryOptionResultListChoose, blockSituationOptionsInfo }
  from "@/api/onething/comboManager/comboAccept/initComboCase";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { VueCropper } from "vue-cropper";
import { getOne, getOneFile } from "@/api/onething/sxpz/comboServiceManage";
import fileView from '@/views/common/fileView';
import { queryIndustryOptionTitleByValOid } from "@/api/onelicence/industryManager/industryCaseAccept/initIndustryCase";
export default {
  inheritAttrs: false,
  name: "comboOnceNotice",
  components: { Treeselect, VueCropper, fileView },
  props: ['comboDireOid'],
  data () {
    return {
      printObj: {
        id: 'print',
        popTitle: '一次性告知单',
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
      ifDis: false,
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
      optionValOids: [],
      //登录信息
      loginUser: {},
      //办理地点
      sxServiceLocations: [],
      //事项材料
      sxServiceMaterials: [],
      mateOptRels: [],
      materialOptList: [],
      //事项情形
      sxServiceSituations: [],
      serviceTitleValList: [],
      comboDireMaterials: [],
      //特别程序
      serviceSpecial: [],
      comboServices: '',
      //情形标题
      // sxServiceOptionTitles: [],
      // 事项情形与选项关系数据
      direSituationOptionTitles: [],
      // 事项情形与选项关系数据--必选集合
      direSituationOptionTitlesMust: [],
      //情形标题和选项值
      situationOptionsTitleValues: [],
      tempTitleOids: [],
      situationName: '默认自定情形',
      // 弹出层标题
      title: "",
      labelPosition: "top",
      checkList: [],
      stepData: [],
      selectData: [],
      ruleForm: {
        comboDirectory: { comboDirectoryName: '' }
      },
      // 表单校验
      rules: {
      },
      //每组情形标题选项值的数据（radio）
      zwbk: [],
      zwbkInfo: [],
      //政务百科查询数据
      zwbkParam: {
        pageNum: 1,
        pageSize: 10,
        caryopterisName: '',
        comboDirectoryOid: this.comboDireOid,
        divisionFlag: 0
      },
      //政务百科列表数据
      zwbkList: [],
      //政务百科列表数据打开标识
      openView: false,
      //查看政务百科
      openInfoView: false,
      form: {},
      //图片预览
      viewDialogOptions: [],
      attaTitle: '',
      //出证结果信息
      jgInfo: ""
    };
  },
  watch: {},
  created () {
    this.getComboDireSituationList();
  },
  computed: {

  },
  methods: {
    next (index, count) {
      let _that = this;
      if (count > 0) {
        if (index == 1) {
          let bixuan = true;
          //判断是否必选
          for (let oneTitle of _that.direSituationOptionTitlesMust) {
            if (bixuan) {
              let tuichu = true;
              let chooseFlag = oneTitle.chooseFlag; //是否必选  1是  0否
              let titleShow = oneTitle.titleName; //显示名称
              let comboOptionVals = oneTitle.comboOptionVals;
              if (chooseFlag == '0') {
                for (let opt of comboOptionVals) {
                  if (opt.ifCheck == true) {
                    tuichu = false;
                    break;
                  }
                }
                if (tuichu) {
                  _that.msgWarning(titleShow + "必选！");
                  bixuan = false;
                }
              }
            }
          }
          if (bixuan) {
            let options = "";
            if (_that.checkList.length > 0) {
              _that.checkList.forEach(check => {
                options += check + ",";
              });
            }
            blockSituationOptionsInfo(options, this.comboDireOid).then(response => {
              if (response.data == null) {
                _that.i = index;
                this.show_0 = false;
                this.show_1 = true;
                this.getComboDireDetail();
              } else {
                _that.msgError("情形选项选择存在阻塞情形，请重新选择！");
              }
            });
          }
        }
      } else {
        this.i = index;
        this.show_0 = true;
        this.show_1 = false;
      }
    },
    /** 点击打印按钮 */
    clickPrinting () {
      //  找到需要隐藏的DOM节点
      let step_title = document.getElementsByClassName('step-title')[0];
      let btn_wrap = document.getElementsByClassName('btn-wrap')[0];
      //  给对应DOM添加class
      step_title.classList.add("printHideCss");
      btn_wrap.classList.add("printHideCss");
    },
    /** 选择情形 */
    selectChange (index, item) {
      this.num = index;
      this.situationName = item.title;
      this.checkList = [];
      this.comboDireMaterials = [];
      this.direSituationOptionTitles = [];
      this.direSituationOptionTitlesMust = [];
      this.comboDireSituationOid = "";
      //置空结果
      this.jgInfo = "";
      //清空政务百科
      this.zwbk = [];
      if (item.oid == undefined) {
        this.getComboDireSituationList();
        this.ifDis = false;
      } else {
        this.ifDis = true;
        this.comboDireSituationOid = item.oid;
        getSituationOpinionList(this.comboDireOid, item.oid).then(response => {
          //情形
          let titles = response.data;
          if (titles) {
            //情形选项值
            let titleValues;
            this.direSituationOptionTitles = [];
            titles.forEach(ti => {
              titleValues = {};
              titleValues.titleName = ti.name;
              titleValues.titleOid = ti.titleOid;
              titleValues.comboOptionVals = ti.comboOptionVals;
              titleValues.moreStatus = ti.moreStatus;
              titleValues.fillFlag = ti.fillFlag;
              this.addValTitle(ti.comboOptionVals);
              this.direSituationOptionTitles.push(titleValues);
              if (ti.fillFlag == '1') {
                titleValues.chooseFlag = '0';
                let comboOptionVals = ti.comboOptionVals;
                for (let opt of comboOptionVals) {
                  if (opt.ifCheck == true) {
                    titleValues.chooseFlag = titleValues.chooseFlag + "," + ti.titleOid + opt.valOid;
                  }
                }
                this.direSituationOptionTitlesMust.push(titleValues);
              }
              if (ti.comboOptionVals != null && ti.comboOptionVals != "") {
                ti.comboOptionVals.forEach(data => {
                  if (data.ifCheck == true) {
                    //放入选中的标题和选项值
                    let titlevalueset = {};
                    titlevalueset.titleName = ti.name;
                    titlevalueset.valueName = data.name;
                    titlevalueset.titleOid = ti.titleOid;
                    this.serviceTitleValList.push(titlevalueset);
                    queryOptionResultListChoose(this.comboDireOid, data.valOid).then(response => {
                      this.jgInfo = "";
                      if (response.data) {
                        for (let name of response.data) {
                          this.jgInfo += name;
                        }
                      }
                    });
                  }
                });
              }
            });
          }
        });
      }
    },
    addValTitle (comboOptionVals) {
      if (comboOptionVals) {
        comboOptionVals.forEach(va => {
          if (va.ifCheck) {
            this.checkList.push(va.valOid);
            /*queryComboOptionTitleByValOid(this.comboDireOid,va.valOid).then(response => {
              //情形
              let titles = response.data;
              if(titles && titles.length>0){
                //情形选项值
                let titleValues;
                let valObj;
                titles.forEach(tti => {
                  titleValues = {};
                  titleValues.titleName=tti.name;
                  titleValues.titleOid=tti.titleOid;
                  titleValues.moreStatus=tti.moreStatus;
                  titleValues.fillFlag=tti.fillFlag;
                  titleValues.comboOptionVals=tti.comboOptionVals;
                  this.addValTitle(titleValues.comboOptionVals);
                  valObj = {};
                  valObj.valOid=va.valOid;
                  valObj.titles = titleValues;
                  this.optionValOids.push(valObj);
                  this.direSituationOptionTitles.push(titleValues);
                });
              }
            });*/

          }
        });
      }
    },
    /** 一件事情形获取 */
    getComboDireSituationList () {
      let _that = this;
      //查询事项情形
      getSituationList(this.comboDireOid).then(response => {
        this.situationName = "默认自定情形";
        let situations = response.data.comboSituations;
        let optionTitles = response.data.comboOptionTitleList;
        _that.getSituationOpinion(situations);
        _that.getSituationOpinionTitle(optionTitles);
        /** 一次性告知 */
        if (_that.direSituationOptionTitles == undefined || _that.direSituationOptionTitles == "") {
          _that.stepData = [{ index: "0", title: "一次性告知", },];
          _that.show_0 = false;
          _that.show_1 = true;
          _that.step_show = false;
          _that.getComboDireDetail();
        } else {
          _that.show_0 = true;
          _that.show_1 = false;
          _that.step_show = true;
          _that.stepData = [{ index: "0", title: "情形选择", }, { index: "1", title: "一次性告知", },];
        }
      });
    },
    /** 填充选项 */
    getSituationOpinion (situations) {
      this.selectData = [{ index: "0", title: "默认自定情形", },];
      if (situations) {
        situations.forEach((item, key) => {
          let situation = {};
          situation.index = key + 1;
          situation.title = item.situationName;
          situation.oid = item.situationOid;
          this.selectData.push(situation);
        });
      }
    },
    /** 填充标题和选项值 */
    getSituationOpinionTitle (optionTitles) {
      if (optionTitles) {
        optionTitles.forEach((optionTitle, index) => {
          let titleValues = {};
          titleValues.titleName = optionTitle.name;
          titleValues.titleOid = optionTitle.titleOid;
          titleValues.moreStatus = optionTitle.moreStatus;
          titleValues.fillFlag = optionTitle.fillFlag;
          titleValues.comboOptionVals = optionTitle.comboOptionVals;
          this.direSituationOptionTitles.push(titleValues);
          let comboOptionVals = optionTitle.comboOptionVals;
          if (optionTitle.fillFlag == '1') {
            titleValues.chooseFlag = '0';
            for (let opt of comboOptionVals) {
              if (opt.ifCheck == true) {
                titleValues.chooseFlag = titleValues.chooseFlag + "," + optionTitle.titleOid + opt.valOid;
              }
            }
            this.direSituationOptionTitlesMust.push(titleValues);
          }
        });
        optionTitles.forEach((optionTitle, index) => {
          let comboOptionVals = optionTitle.comboOptionVals;
          //检查选项值是否默认选中
          for (let opt of comboOptionVals) {
            //单选默认
            if (optionTitle.moreStatus == 0 && opt.defaultFlag == 1) {
              this.checkList[index] = opt.valOid;
              this.changeTitle(optionTitle.titleOid, opt.valOid);
            }
            //多选默认
            if (optionTitle.moreStatus == 1 && opt.defaultFlag == 1) {
              this.checkBoxList.push(opt.valOid);
              this.changeTitleBox(opt, opt.valOid);
            }
          }
        });
      }
    },
    /**获取一件事目录详细**/
    getComboDireDetail () {
      let _that = this;
      getComboDireDetail(_that.comboDireOid, _that.checkList).then(response => {
        this.ruleForm = response.data;
        this.printObj.popTitle = this.ruleForm.comboDirectory.comboDirectoryName + '告知单';
        this.comboDireMaterials = response.data.directoryMaterials;
        this.comboServices = response.data.comboServices;
        this.serviceSpecial = response.data.serviceSpecial;
      });
    },
    /** 关闭方法 */
    closeDialog () {
      let _that = this;
      _that.sxServiceMaterials = [];
      _that.checkList = [];
      _that.materialOptList = [];
      _that.mateOptRels = [];
      _that.direSituationOptionTitles = [];
    },
    /** 改变选项值 */
    changeTitle (bigName, smallName, titleChooseOid, optionValOid, index) {
      for (let oneTitle of this.direSituationOptionTitlesMust) {
        if (oneTitle.titleOid == titleChooseOid) {
          oneTitle.chooseFlag = '1';
        }
      }
      if (this.serviceTitleValList.length == 0) {
        //放入选中的标题和选项值
        let titlevalueset = {};
        titlevalueset.titleName = bigName;
        titlevalueset.titleOid = titleChooseOid;
        titlevalueset.valueName = smallName;
        this.serviceTitleValList.push(titlevalueset);
      } else {
        for (let oneTitle of this.serviceTitleValList) {
          if (oneTitle.titleOid == titleChooseOid) {
            oneTitle.valueName = smallName;
          }
        }
      }

      let _that = this;
      if (_that.optionValOids && _that.optionValOids.length > 0) {
        _that.optionValOids.forEach(ov => {
          var ind = _that.checkList.findIndex(ck => {
            if (ov.valOid == ck) {
              return true;
            }
          });
          if (ind < 0) {
            if (ov.titles.titleOid) {
              //清除选项值下的标题
              var index = _that.direSituationOptionTitles.findIndex(tti => {
                if (ov.titles.titleOid == tti.titleOid) {
                  return true;
                }
              });
              if (index > 0) {
                _that.direSituationOptionTitles.forEach(tit => {
                  if (ov.titles.titleOid == tit.titleOid) {
                    tit.comboOptionVals.forEach(tti => {
                      var indd = _that.checkList.findIndex(cck => {
                        if (tti.valOid == cck) {
                          return true;
                        }
                      });
                      if (indd > 0) {
                        _that.checkList.splice(indd, 1);
                      }

                    });
                  }
                });
                _that.direSituationOptionTitles.splice(index, 1);
              }
              //清除选项
              var ind2 = _that.optionValOids.findIndex(ovd => {
                if (ov.valOid == ovd.valOid) {
                  return true;
                }
              });
              if (ind2 > 0) {
                _that.optionValOids.splice(ind2, 1);
              }
            }
          }
        });
      }
      queryComboOptionTitleByValOid(this.comboDireOid, optionValOid).then(response => {
        //情形
        let titles = response.data;
        if (titles && titles.length > 0) {
          //情形选项值
          let titleValues;
          let valObj;
          titles.forEach(ti => {
            titleValues = {};
            titleValues.titleName = ti.name;
            titleValues.titleOid = ti.titleOid;
            titleValues.moreStatus = ti.moreStatus;
            titleValues.fillFlag = ti.fillFlag;
            titleValues.comboOptionVals = ti.comboOptionVals;
            valObj = {};
            valObj.valOid = optionValOid;
            valObj.titles = titleValues;
            _that.optionValOids.push(valObj);
            _that.direSituationOptionTitles.push(titleValues);
          });
        } else {
          let valObj = {};
          valObj.valOid = optionValOid;
          valObj.titles = {};
          _that.optionValOids.push(valObj);
          console.log('direSituationOptionTitles== ' + _that.direSituationOptionTitles);
        }
      });
      //显示政务百科信息
      if (index >= 0) {
        //查询政务百科信息
        this.zwbkParam.caryopterisName = smallName;
        _that.zwbkParam.divisionFlag = 0;
        pageZwbk(this.zwbkParam).then(response => {
          //如果有数据，显示
          if (response.data.data && response.data.data.length > 0) {
            _that.zwbk[index] = true;
            //并且处理显示的内容数据
            _that.zwbkInfo[index] = {
              caryopterisName: smallName,
              comboDirectoryOid: this.comboDireOid,
              divisionFlag: 0
            };
          } else {
            _that.zwbk[index] = false;
          }
          _that.$forceUpdate();
        });
      }
      //查看所选项结果
      if (_that.checkList) {
        queryOptionResultListChoose(this.comboDireOid, _that.checkList).then(response => {
          this.jgInfo = "";
          if (response.data) {
            for (let name of response.data) {
              this.jgInfo += name;
            }
          }
        });
      }
    },
    changeTitleBox (bigName, smallName, item, val, index) {
      let _that = this;

      for (let oneTitle of this.direSituationOptionTitlesMust) {
        if (oneTitle.titleOid == item.titleOid) {
          if (oneTitle.chooseFlag.indexOf(item.titleOid + val) != -1) {
            oneTitle.chooseFlag = oneTitle.chooseFlag.replace("," + item.titleOid + val, '');
          } else {
            oneTitle.chooseFlag = oneTitle.chooseFlag + "," + item.titleOid + val;
          }
        }
      }
      //多选框勾选或者取消
      _that.checkList.forEach(box => {
        //多选框勾选显示与勾选的选项值的选项
        if (val == box) {
          queryIndustryOptionTitleByValOid(this.comboDireOid, box).then(response => {
            if (response.data != null) {
              //情形
              let titles = response.data;
              if (titles && titles.length > 0) {
                //情形选项值
                let titleValues;
                titles.forEach(ti => {
                  titleValues = {};
                  titleValues.titleName = ti.name;
                  titleValues.titleOid = ti.titleOid;
                  titleValues.moreStatus = ti.moreStatus;
                  titleValues.fillFlag = ti.fillFlag;
                  titleValues.comboOptionVals = ti.comboOptionVals;
                  _that.direSituationOptionTitles.push(titleValues);
                });
              }
            }
          });
        } else {
          //多选框取消隐藏与勾选的选项值的选项
          queryIndustryOptionTitleByValOid(this.comboDireOid, val).then(response => {
            //情形
            let titles = response.data;
            if (titles && titles.length > 0) {
              titles.forEach(ti => {
                _that.direSituationOptionTitles.forEach((tti, index) => {
                  if (tti.titleOid === ti.titleOid) {
                    _that.direSituationOptionTitles.splice(index, 1);
                  }
                });
              });
            }
          });
        }
      })
      //多选框取消时最后一个多选框点击
      if (_that.checkList.length == 0) {
        queryIndustryOptionTitleByValOid(this.comboDireOid, val).then(response => {
          //情形
          let titles = response.data;
          if (titles && titles.length > 0) {
            titles.forEach(ti => {
              _that.direSituationOptionTitles.forEach((tti, index) => {
                if (tti.titleOid === ti.titleOid) {
                  _that.direSituationOptionTitles.splice(index, 1);
                }
              });
            });
          }
        });
      }
      let valueName;
      if (this.serviceTitleValList.length == 0) {

        //放入选中的标题和选项值
        let titlevalueset = {};
        titlevalueset.titleName = bigName;
        titlevalueset.titleOid = item.titleOid;
        titlevalueset.valueName = smallName;
        this.serviceTitleValList.push(titlevalueset);
        valueName = smallName;
      } else {

        for (let oneTitle of this.serviceTitleValList) {
          if (oneTitle.titleOid == item.titleOid) {

            if (oneTitle.valueName.indexOf(smallName) != -1) {
              oneTitle.valueName = oneTitle.valueName.replace("," + smallName, '');
              if (oneTitle.valueName) {
                valueName = oneTitle.valueName;
              }
            } else {

              oneTitle.valueName = oneTitle.valueName + "," + smallName;
              if (oneTitle.valueName) {
                valueName = oneTitle.valueName;
              }
            }
          }
        }
      }
      //显示政务百科信息
      if (index >= 0 && valueName) {

        let _that = this;
        //查询政务百科信息
        _that.zwbkParam.caryopterisName = valueName;
        _that.zwbkParam.divisionFlag = 1;
        pageZwbk(_that.zwbkParam).then(response => {
          //如果有数据，显示
          if (response.data.data && response.data.data.length > 0) {
            _that.zwbk[index] = true;
            //并且处理显示的内容数据
            _that.zwbkInfo[index] = {
              caryopterisName: valueName,
              comboDirectoryOid: _that.comboDireOid,
              divisionFlag: 1
            };
          } else {
            _that.zwbk[index] = false;
          }
          _that.$forceUpdate();
        });
      }
      //查看所选项结果
      if (this.checkList) {
        queryOptionResultListChoose(this.comboDireOid, this.checkList).then(response => {
          this.jgInfo = "";
          if (response.data) {
            for (let name of response.data) {
              this.jgInfo += name;
            }
          }
        });
      }
    },
    specialName (val) {
      getSpecialName(val).then(response => {
        return response.data.name;
      });
    },
    //政务百科相关信息
    zwbkInfoPage (param) {
      let _that = this;
      if (param) {
        _that.zwbkParam.caryopterisName = param.caryopterisName;
        _that.zwbkParam.divisionFlag = param.divisionFlag;
      }
      pageZwbk(_that.zwbkParam).then(response => {
        //如果有数据，显示
        if (response.data.data && response.data.data.length > 0) {
          _that.zwbkList = response.data.data;
          _that.total = response.data.total;
          _that.openView = true;
        }
      });
    },
    getPublishName (val) {
      if (val.caryopterisType == 1) {
        return '文本内容';
      } else {
        return '图片';
      }
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.reset();
      const oid = row.manageOid;
      getOne(oid).then(response => {
        this.form = response.data.comboServiceManage;
        this.getFilename();
        this.openInfoView = true;
        this.title = "查看百科词条信息";
      });
    },
    getFilename () {
      if (this.form.attaOid) {
        getOneFile(this.form.attaOid).then(response1 => {
          this.attaTitle = response1.data.originName;
        });
      }
    },
    // 表单重置
    reset () {
      this.form = {
        name: null
      };
      this.resetForm("form");
    },
    //下载附件
    downloadFile (attaOid) {
      this.download(attaOid);
    },
    //预览附件
    viewFileNew (attaOid) {
      let item = { show: true, attaOid: attaOid };
      this.viewDialogOptions.push(item);
    },
    //关闭预览附件
    closeFileView () {
      this.viewDialogOptions.pop();
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
  content: '';
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
