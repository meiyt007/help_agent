/** * 窗口受理管理 * @author: wangwg * @date: 2020-10-29 */
<template>
  <div class="app-container">
    <el-row :gutter="20"></el-row>
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
                <div v-for="(data, index) in sxServiceOptionTitles">
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
                          >{{ item.name }}
                        </el-checkbox>
                      </el-checkbox-group>
                    </template>
                  </div>
                </div>
              </div>
              <div v-else>暂无数据</div>
            </td>
          </tr>
        </table>

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
        <el-form
          :model="serviceForm"
          ref="ruleForm"
          label-width="0px"
          class="demo-ruleForm"
          :label-position="labelPosition"
        >
          <h3 class="title"><i class="el-icon-s-grid"></i>材料核验列表</h3>
          <div class="data-box">
            <h4>事项基本信息</h4>
            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="el-table__body"
            >
              <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
              </colgroup>
              <tr>
                <td><b>事项名称：</b></td>
                <td>{{ serviceForm.serviceName }}</td>
                <td><b>事项类型：</b></td>
                <td>{{ serviceForm.serviceTypeName }}</td>
              </tr>
              <tr>
                <td><b>实施机构：</b></td>
                <td>{{ serviceForm.organName }}</td>
                <td><b>是否收费：</b></td>
                <td>{{ sxService.chargeFlagName }}</td>
              </tr>
              <tr v-if="sxService.chargeFlag == 1">
                <td><b>收费依据：</b></td>
                <td>{{ serviceForm.sxServiceExtend.chargeAccord }}</td>
                <td><b>收费标准：</b></td>
                <td>{{ serviceForm.sxServiceExtend.chargeStandard }}</td>
              </tr>
              <tr>
                <td><b>办理方式：</b></td>
                <td>{{ sxService.handleFormName }}</td>
                <td><b>领取方式：</b></td>
                <td>{{ sxService.resultDeliveryWayName }}</td>
              </tr>
              <tr>
                <td><b>承诺时限：</b></td>
                <td>
                  <template>
                    {{ serviceForm.sxServiceExtend.promiseLimit }}
                    <span
                      v-if="serviceForm.sxServiceExtend.promiseLimitType == 'W'"
                    >
                      (工作日)</span
                    >
                    <span
                      v-if="serviceForm.sxServiceExtend.promiseLimitType == 'N'"
                    >
                      (自然日)</span
                    >
                    <span
                      v-if="serviceForm.sxServiceExtend.promiseLimitType == 'H'"
                    >
                      (小时)</span
                    >
                  </template>
                </td>
                <td><b>法定时限：</b></td>
                <td>
                  <template>
                    {{ serviceForm.sxServiceExtend.legalLimit }}
                    <span
                      v-if="serviceForm.sxServiceExtend.legalLimitType == 'W'"
                    >
                      (工作日)</span
                    >
                    <span
                      v-if="serviceForm.sxServiceExtend.legalLimitType == 'N'"
                    >
                      (自然日)</span
                    >
                    <span
                      v-if="serviceForm.sxServiceExtend.legalLimitType == 'H'"
                    >
                      (小时)</span
                    >
                  </template>
                </td>
              </tr>
              <tr>
                <td><b>办理时间：</b></td>
                <td>
                  <div
                    v-for="(data, index) in serviceForm.sxServiceLocations"
                    :label="data.acceptDate"
                    :key="index"
                  >
                    {{ index + 1 }}、{{ data.acceptDate }}
                  </div>
                </td>
                <td><b>办理地点：</b></td>
                <td>
                  <div
                    v-for="(data, index) in serviceForm.sxServiceLocations"
                    :label="data.locationName"
                    :key="index"
                  >
                    {{ index + 1 }}、{{ data.locationName }}
                  </div>
                </td>
              </tr>
              <tr>
                <td><b>监督电话：</b></td>
                <td>{{ serviceForm.tsDhText }}</td>
                <td><b>咨询电话：</b></td>
                <td>{{ serviceForm.zxDhText }}</td>
              </tr>
              <tr>
                <td><b>申请受理条件：</b></td>
                <td colspan="3">
                  <div
                    v-for="(data, index) in serviceForm.sxAcceptConditions"
                    :label="data.conditionText"
                    :key="index"
                  >
                    <p v-if="serviceForm.sxAcceptConditions.length > 1">
                      {{ index + 1 }}、 {{ data.conditionText }}
                    </p>
                    <p v-else>
                      {{ data.conditionText }}
                    </p>
                  </div>
                </td>
              </tr>
              <tr>
                <td><b>办事流程：</b></td>
                <td colspan="3">
                  <div
                    v-for="(data, index) in serviceForm.sxServiceLinks"
                    :label="data.linkName"
                    :key="index"
                  >
                    <p v-if="serviceForm.sxServiceLinks.length > 1">
                      {{ index + 1 }}、{{ data.linkName }}
                    </p>
                    <p v-else>
                      {{ data.linkName }}
                    </p>
                  </div>
                </td>
              </tr>
            </table>
          </div>
          <div class="data-box">
            <h4>材料列表</h4>
            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="data-table"
            >
              <colgroup>
                <col width="10%" />
                <col width="30%" />
                <col width="15%" />
                <col width="15%" />
                <col width="15%" />
              </colgroup>
              <tr>
                <th>序号</th>
                <th>材料名称</th>
                <th>材料类型</th>
                <th>材料形式</th>
                <th>份数</th>
                <th>必要性</th>
              </tr>
              <tbody v-if="sxServiceMaterialList.length > 0">
                <tr
                  v-for="(data, index) in sxServiceMaterialList"
                  empty-text="暂无我的待办"
                >
                  <td>{{ index + 1 }}</td>
                  <td>{{ data.materialName }}</td>
                  <td>
                    <template>
                      <span v-if="data.materialType == '0'">原件</span>
                      <span v-if="data.materialType == '1'">复印件</span>
                      <span v-if="data.materialType == '2'"
                        >原件或者复印件</span
                      >
                    </template>
                  </td>
                  <td>
                    <template>
                      <span v-if="data.materialFormat == '1'">纸质</span>
                      <span v-if="data.materialFormat == '2'">电子版</span>
                    </template>
                  </td>
                  <td>{{ data.paperNumber }}</td>
                  <td>
                    <template>
                      <span
                        class="bage-necessery"
                        v-if="data.materialMustFlag == '1'"
                        >必要</span
                      >
                      <span
                        class="bage-necessery"
                        v-if="data.materialMustFlag == '0'"
                        >不必要</span
                      >
                      <span
                        class="bage-necessery"
                        v-if="data.materialMustFlag == '2'"
                        >容缺后补</span
                      >
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
          <div class="btn-wrap">
            <el-checkbox v-model="radio" label="1" @change="checkedBox"
              >我已核验上述材料并确定齐全</el-checkbox
            >
            <div class="btn-list mt10">
              <el-button type="primary" icon="el-icon-back" @click="next(0, -1)"
                >上一步</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-circle-check"
                @click="next(2, 1)"
                >下一步</el-button
              >
            </div>
          </div>
        </el-form>
      </div>
      <!-- 第三步 -->
      <div class="step-content step-third" v-if="show_2">
        <el-button
          type="primary"
          :loading="false"
          @click="clzs(4, 1)"
          class="data-btn"
          >材料智审</el-button
        >
        <el-form
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="0px"
          class="demo-ruleForm"
          :label-position="labelPosition"
        >
          <h3 class="title"><i class="el-icon-s-grid"></i>申请材料信息</h3>
          <table cellspacing="0" cellpadding="0" border="0" class="data-table">
            <colgroup>
              <col width="5%" />
              <col width="25%" />
              <col width="10%" />
              <col width="13%" />
              <col width="47%" />
            </colgroup>
            <tr>
              <th>序号</th>
              <th>材料名称</th>
              <th>参考份数</th>
              <th>材料样本</th>
              <th>操作</th>
            </tr>
            <tbody v-for="(item, index) in sxServiceMaterialAttaList">
              <tr>
                <td>{{ index + 1 }}</td>
                <td>{{ item.materialName }}</td>
                <td>
                  <template>
                    <span v-if="item.sxMaterials.materialType == '0'"
                      >原件</span
                    >
                    <span v-if="item.sxMaterials.materialType == '1'"
                      >复印件</span
                    >
                    <span v-if="item.sxMaterials.materialType == '2'"
                      >原件和复印件</span
                    >({{ item.sxMaterials.paperNumber }})
                  </template>
                </td>
                <td>
                  <div class="handle-btn">
                    <el-button
                      type="text"
                      icon="el-icon-download"
                      size="small"
                      @click="
                        downLoadMaterialAddr(
                          item.sxMaterials.materialSimpleAddr
                        )
                      "
                      >下载
                    </el-button>
                    <el-button
                      type="text"
                      icon="el-icon-view"
                      size="small"
                      @click="
                        viewMaterialAddr(item.sxMaterials.materialSimpleAddr)
                      "
                      >查看
                    </el-button>
                  </div>
                </td>
                <td>
                  <el-radio
                    label="1"
                    v-model="radio1[item.caseMaterialOid]"
                    @change="chooseCollectionType('1', index, item)"
                    >纸质收取
                  </el-radio>
                  <el-radio
                    label="2"
                    v-model="radio1[item.caseMaterialOid]"
                    @change="chooseCollectionType('2', index, item)"
                    >附件上传
                  </el-radio>
                  <el-radio
                    label="3"
                    v-model="radio1[item.caseMaterialOid]"
                    @change="chooseCollectionType('3', index, item)"
                    >扫描
                  </el-radio>
                  <el-radio
                    label="5"
                    v-model="radio1[item.caseMaterialOid]"
                    @change="chooseCollectionType('5', index, item)"
                    >证照库
                  </el-radio>
                  <el-radio
                    v-if="
                      isSxPersonFlg == false &&
                        item.sxMaterials.materialMustFlag == '2'
                    "
                    label="4"
                    v-model="radio1[item.caseMaterialOid]"
                    @change="chooseCollectionType('4', index, item)"
                    >容缺后补
                  </el-radio>
                </td>
              </tr>
              <tr v-show="show_upload[index]">
                <td colspan="5">
                  <div class="handle-data">
                    <el-row class="right-btn-group">
                      <el-button
                        type="primary"
                        @click="
                          openMaterialComparison(
                            item.materialOid,
                            item.caseMaterialOid
                          )
                        "
                        icon="el-icon-document"
                        size="mini"
                        >材料比对</el-button
                      >
                    </el-row>
                    <el-row class="right-btn-group-two">
                      <el-form
                        ref="formForm"
                        :model="formData"
                        :rules="rules"
                        size="medium"
                        label-width="100px"
                      >
                        <el-form-item>
                          <el-upload
                            class="upload-demo"
                            action=""
                            multiple
                            :data="item"
                            :http-request="uploadFiles"
                            :before-upload="beforeUpload"
                            :on-error="uploadError"
                            :show-file-list="showFileList"
                            :file-list="fileList"
                            accept="accept"
                          >
                            <el-button
                              size="mini"
                              type="primary"
                              icon="el-icon-upload"
                              @click="pushMaterialOid(item.caseMaterialOid)"
                              >点击上传</el-button
                            >
                          </el-upload>
                        </el-form-item>
                      </el-form>
                    </el-row>
                    <ul v-for="data in item.attaList">
                      <li>
                        <el-row :gutter="24">
                          <el-col :span="9">
                            <div class="grid-content qdcg-text">
                              <img src="../../../../assets/image/icons05.png" />
                              <p :title="data.originName">
                                {{ data.originName }}
                              </p>
                            </div>
                          </el-col>
                          <el-col
                            :span="5"
                            v-if="
                              data.extensionName &&
                                (data.extensionName.indexOf('png') > -1 ||
                                  data.extensionName.indexOf('jpg') > -1)
                            "
                          >
                            <span v-for="catalog in item.materialCatalogList">
                              <el-radio
                                :label="catalog.materialCatalogOid"
                                v-model="catalogCheckList[data.attaOid]"
                                @change="
                                  chooseCatalog(
                                    item.caseMaterialOid,
                                    data.attaOid,
                                    catalog.materialCatalogOid
                                  )
                                "
                              >
                                {{ catalog.catalogName }}
                              </el-radio>
                            </span>
                          </el-col>
                          <el-col :span="5">
                            <div class="grid-content qdcg-btn">
                              <el-button
                                type="text"
                                icon="el-icon-view"
                                @click="viewFile(data.attaOid)"
                              ></el-button>
                              <el-button
                                type="text"
                                icon="el-icon-delete"
                                @click="deleteAtta(data)"
                              ></el-button>
                            </div>
                          </el-col>
                        </el-row>
                      </li>
                    </ul>
                  </div>
                </td>
              </tr>
              <tr v-show="show_scan[index]">
                <td colspan="5">
                  <div class="handle-data">
                    <el-row class="right-btn-group-two">
                      <el-form
                        ref="formForm"
                        :model="formData"
                        :rules="rules"
                        size="medium"
                        label-width="100px"
                      >
                        <el-button
                          size="mini"
                          type="primary"
                          icon="el-icon-upload"
                          @click="scanPicture(item.caseMaterialOid, index)"
                          >点击扫描</el-button
                        >
                      </el-form>
                    </el-row>
                    <ul v-for="data in item.attaList">
                      <li>
                        <el-row :gutter="24">
                          <el-col :span="9">
                            <div class="grid-content qdcg-text">
                              <img src="../../../../assets/image/icons05.png" />
                              <p :title="data.originName">{{ data.name }}</p>
                            </div>
                          </el-col>
                          <el-col :span="5">
                            <div class="grid-content qdcg-btn">
                              <el-button
                                type="text"
                                icon="el-icon-view"
                                @click="viewFile(data.attaOid)"
                              ></el-button>
                              <el-button
                                type="text"
                                icon="el-icon-delete"
                                @click="deleteAtta(data)"
                              ></el-button>
                            </div>
                          </el-col>
                        </el-row>
                      </li>
                    </ul>
                  </div>
                </td>
              </tr>
              <tr v-show="show_elem[index]">
                <td colspan="5">
                  <div class="handle-data">
                    <el-row class="right-btn-group-two">
                      <el-button
                        size="mini"
                        type="primary"
                        icon="el-icon-upload"
                        @click="
                          getElecLicenInfo(
                            item.materialOid,
                            item.caseMaterialOid,
                            index
                          )
                        "
                        >获取证照</el-button
                      >
                    </el-row>
                    <ul v-for="data in attaList[index]">
                      <li>
                        <el-row :gutter="24">
                          <el-col :span="9">
                            <div class="grid-content qdcg-text">
                              <p :title="data.originName">
                                {{ data.originName }}
                              </p>
                            </div>
                          </el-col>
                          <el-col :span="5">
                            <div class="grid-content qdcg-btn">
                              <el-button
                                type="text"
                                icon="el-icon-view"
                                @click="viewElemsInfo(data.attaOid)"
                              >
                              </el-button>
                            </div>
                          </el-col>
                        </el-row>
                      </li>
                    </ul>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <h3 class="title" v-if="radioNotice">
            <i class="el-icon-s-grid"></i>补正信息
          </h3>
          <table
            v-if="radioNotice"
            cellspacing="0"
            cellpadding="0"
            border="0"
            class="el-table__body"
          >
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>承诺补正时间：</b></td>
              <td colspan="3">
                <el-date-picker
                  v-model="bqbzCaseForm.dueDate"
                  type="date"
                  value-format="yyyy-MM-dd"
                  start=""
                  :picker-options="optionDate"
                  placeholder="选择承诺补正时间"
                >
                </el-date-picker>
                <!--                <el-button type="primary" v-if="bqbzCaseForm.dueDate" @click="printNotice()">告知承诺书</el-button>-->
              </td>
            </tr>
          </table>

          <div class="btn-wrap">
            <el-checkbox
              v-if="checkFlag == true"
              :disabled="isCheckedDisable"
              v-model="radioNotice"
              label="1"
              @change="checkedBoxNotice"
              >当前事项支持告知承诺方式进行材料提交，是否需要？</el-checkbox
            >
            <div class="btn-list mt10">
              <el-button type="primary" icon="el-icon-back" @click="next(1, -1)"
                >上一步</el-button
              >
              <el-button type="primary" icon="el-icon-right" @click="next(3, 1)"
                >下一步</el-button
              >
            </div>
          </div>
        </el-form>
      </div>
      <!-- 第四步 -->
      <div class="step-content step-fourth" v-if="show_3">
        <el-form
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="0px"
          class="demo-ruleForm"
          :label-position="labelPosition"
        >
          <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="办件申请基础信息" name="first">
              <div class="step-third-box">
                <h3 class="title">
                  <i class="el-icon-s-grid"></i>申请人/申请单位信息
                  <el-button
                    type="primary"
                    @click="scanCard('0')"
                    style="float: right"
                    v-if="cegisterType == 1"
                    >✚ 扫描身份证
                  </el-button>
                </h3>
                <table
                  cellspacing="0"
                  cellpadding="0"
                  border="0"
                  class="el-table__body"
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
                      <span v-if="cegisterType == 0">
                        <b>申请人姓名：</b>
                      </span>
                      <span v-else>
                        <b>申请单位名称：</b>
                      </span>
                    </td>
                    <td colspan="3">
                      <el-form-item prop="applyUserName" style="width: 70%">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.applyUserName"
                            name="applyUserName"
                            maxlength="25"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                      <el-button
                        type="primary"
                        @click="getUserInfo(ruleForm.credentialNumber)"
                        style="float: right;margin-top: -36px;"
                        >✚ 提取申请人信息</el-button
                      >
                    </td>
                  </tr>
                  <tr>
                    <td><i class="require">*</i><b>证件类型：</b></td>
                    <td>
                      <el-form-item prop="credentialType">
                        <el-col :span="24">
                          <el-select
                            v-model="ruleForm.credentialType"
                            placeholder="请选择证件类型"
                            :popper-append-to-body="false"
                            @change="changeType"
                          >
                            <el-option
                              v-for="certificateType in certificateTypeList"
                              :key="certificateType.dictOid"
                              :label="certificateType.name"
                              :value="certificateType.dictOid"
                              class="category_style"
                            >
                            </el-option>
                          </el-select>
                        </el-col>
                      </el-form-item>
                    </td>
                    <td><i class="require">*</i><b>证件号码：</b></td>
                    <td>
                      <el-form-item prop="credentialNumber">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.credentialNumber"
                            name="credentialNumber"
                            maxlength="25"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <i class="require">*</i>
                      <span v-if="cegisterType == 0">
                        <b>申请人手机：</b>
                      </span>
                      <span v-else>
                        <b>申请单位手机：</b>
                      </span>
                    </td>
                    <td>
                      <el-form-item prop="applyUserPhone">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.applyUserPhone"
                            name="applyUserPhone"
                            maxlength="11"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                    <td>
                      <span v-if="cegisterType == 0">
                        <b>申请人电话：</b>
                      </span>
                      <span v-else>
                        <b>申请单位电话：</b>
                      </span>
                      <br />(格式：xxxx-xxxxxxxx)
                    </td>
                    <td>
                      <el-form-item prop="applyUserTel">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.applyUserTel"
                            name="applyUserTel"
                            maxlength="15"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr v-if="cegisterType == 0">
                    <td><b>通讯地址：</b></td>
                    <td colspan="3">
                      <el-form-item prop="applyUserAddress">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.applyUserAddress"
                            name="applyUserAddress"
                            maxlength="150"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr v-else>
                    <td><i class="require">*</i><b>法定代表人：</b></td>
                    <td colspan="3">
                      <el-form-item prop="legalPersonName">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.legalPersonName"
                            name="legalPersonName"
                            maxlength="25"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
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
                    <col width="15%" />
                    <col width="35%" />
                    <col width="15%" />
                    <col width="35%" />
                  </colgroup>
                  <tr>
                    <td><i class="require">*</i><b>申请项目名称：</b></td>
                    <td colspan="3">
                      <el-form-item prop="projectName">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.projectName"
                            name="projectName"
                            maxlength="250"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td><i class="require">*</i><b>申请数量：</b></td>
                    <td>
                      <el-form-item prop="applyNumber">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.applyNumber"
                            name="applyNumber"
                            maxlength="3"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                    <td><i class="require">*</i><b>邮政编码：</b></td>
                    <td>
                      <el-form-item prop="applyPostCode">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.applyPostCode"
                            name="applyPostCode"
                            maxlength="6"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td><i class="require">*</i><b>业务管辖地：</b></td>
                    <td>
                      <el-form-item prop="bussVenueDistrictOid">
                        <el-col :span="24">
                          <treeselect
                            @input="changeValue"
                            :multiple="true"
                            :options="districtOptions"
                            :flat="true"
                            :default-expand-level="1"
                            placeholder="请选择业务管辖地"
                            v-model="ruleForm.bussVenueDistrictOid"
                          />
                        </el-col>
                      </el-form-item>
                    </td>
                    <td><i class="require">*</i><b>受理具体地点：</b></td>
                    <td>
                      <el-form-item prop="specificLocation">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.specificLocation"
                            name="specificLocation"
                            maxlength="150"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td><b>投资项目名称：</b></td>
                    <td>
                      <el-form-item prop="investProjecName">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.investProjecName"
                            name="investProjecName"
                            maxlength="150"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                    <td><b>投资项目编号：</b></td>
                    <td>
                      <el-form-item prop="investProjectCode">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.investProjectCode"
                            name="investProjectCode"
                            maxlength="100"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td><b>摘要内容：</b></td>
                    <td colspan="3">
                      <el-form-item prop="projectAbstract">
                        <el-col :span="24">
                          <el-input
                            type="textarea"
                            v-model.trim="ruleForm.projectAbstract"
                            name="projectAbstract"
                            maxlength="500"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td><i class="require">*</i><b>是否为代理人：</b></td>
                    <td colspan="3">
                      <el-radio-group
                        v-model.trim="ruleForm.proxyFlag"
                        @change="changeProxyFlag"
                      >
                        <el-radio label="1">是</el-radio>
                        <el-radio label="0">否</el-radio>
                      </el-radio-group>
                    </td>
                  </tr>
                </table>
              </div>
              <!-- 代理人信息 -->
              <div class="step-third-box" v-if="proxy_show">
                <h3 class="title">
                  <i class="el-icon-s-grid"></i>联系人/代理人相关信息
                </h3>
                <el-button
                  type="primary"
                  @click="scanCard('1')"
                  style="float: right"
                  >✚ 扫描身份证</el-button
                >
                <table
                  cellspacing="0"
                  cellpadding="0"
                  border="0"
                  class="el-table__body"
                >
                  <colgroup>
                    <col width="15%" />
                    <col width="35%" />
                    <col width="15%" />
                    <col width="35%" />
                  </colgroup>
                  <tr>
                    <td><i class="require">*</i><b>联系人/代理人姓名：</b></td>
                    <td colspan="3">
                      <el-form-item prop="contactUserName">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.contactUserName"
                            name="contactUserName"
                            maxlength="25"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <i class="require">*</i><b>联系人/代理人身份证号码：</b>
                    </td>
                    <td>
                      <el-form-item prop="contactCredentialNumber">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.contactCredentialNumber"
                            name="contactCredentialNumber"
                            maxlength="18"
                            show-word-limit
                          >
                          </el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                    <td><b>电子邮箱：</b></td>
                    <td>
                      <el-form-item prop="contactEmail">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.contactEmail"
                            name="contactEmail"
                            maxlength="50"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td><i class="require">*</i><b>手机号：</b></td>
                    <td>
                      <el-form-item prop="contactUserPhone">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.contactUserPhone"
                            name="contactUserPhone"
                            maxlength="11"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                    <td><b>固定电话：</b></td>
                    <td>
                      <el-form-item prop="contactUserTel">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.contactUserTel"
                            name="contactUserTel"
                            maxlength="20"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td><b>备注：</b></td>
                    <td colspan="3">
                      <el-form-item prop="contactRemark">
                        <el-col :span="24">
                          <el-input
                            type="textarea"
                            v-model.trim="ruleForm.contactRemark"
                            name="contactRemark"
                            maxlength="500"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                  </tr>
                </table>
              </div>
              <!-- 送达信息 -->
              <div class="step-third-box">
                <h3 class="title"><i class="el-icon-s-grid"></i>送达信息</h3>
                <table
                  cellspacing="0"
                  cellpadding="0"
                  border="0"
                  class="el-table__body"
                >
                  <colgroup>
                    <col width="15%" />
                    <col width="35%" />
                    <col width="15%" />
                    <col width="35%" />
                  </colgroup>
                  <tr>
                    <td><i class="require">*</i><b>送达方式：</b></td>
                    <td colspan="3">
                      <el-radio-group
                        v-model="ruleForm.resultDeliveryWay"
                        @change="changeDeliveryWay"
                      >
                        <el-radio label="1">快递送达</el-radio>
                        <el-radio label="2">自行取件</el-radio>
                        <el-radio label="3">其他</el-radio>
                      </el-radio-group>
                    </td>
                  </tr>
                  <tr v-if="address_show">
                    <td><i class="require">*</i><b>收件人名称：</b></td>
                    <td>
                      <el-form-item
                        prop="addresseeName"
                        style="float:left;margin-bottom:2px;width:75%"
                      >
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.addresseeName"
                            name="addresseeName"
                            maxlength="25"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                      <el-button type="primary" @click="getApplyInfo"
                        >提取</el-button
                      >
                    </td>
                    <td><i class="require">*</i><b>收件人邮政编码：</b></td>
                    <td>
                      <el-form-item prop="addresseePostCode">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.addresseePostCode"
                            name="addresseePostCode"
                            maxlength="6"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr v-if="address_show">
                    <td><i class="require">*</i><b>收件人手机：</b></td>
                    <td>
                      <el-form-item prop="addresseePhone">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.addresseePhone"
                            name="addresseePhone"
                            maxlength="11"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                    <td><b>收件人电话：</b></td>
                    <td>
                      <el-form-item prop="addresseeTel">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.addresseeTel"
                            name="addresseeTel"
                            maxlength="25"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr v-if="address_show">
                    <td><i class="require">*</i><b>收件人地址：</b></td>
                    <td>
                      <el-form-item prop="addresseeAddress">
                        <el-col :span="24">
                          <el-cascader
                            size="large"
                            :options="provinceCityOptions"
                            v-model="ruleForm.addresseeAddress"
                            @change="handleChange"
                          >
                          </el-cascader>
                        </el-col>
                      </el-form-item>
                    </td>
                    <td><i class="require">*</i><b>收件人详细地址：</b></td>
                    <td>
                      <el-form-item prop="addresseeDetailAddress">
                        <el-col :span="24">
                          <el-input
                            v-model.trim="ruleForm.addresseeDetailAddress"
                            name="addresseeDetailAddress"
                            maxlength="250"
                            show-word-limit
                          ></el-input>
                        </el-col>
                      </el-form-item>
                    </td>
                  </tr>
                </table>
              </div>
            </el-tab-pane>
            <el-tab-pane
              label="道路运输信息"
              name="second"
              v-if="serviceOid == '2c287b8b79f39489017a1cc9c78c166d'"
            >
              <div class="app-container">
                <el-form ref="form" :model="form" label-width="0px">
                  <div class="el-table__header-wrapper dialog-table">
                    <h3 class="title">
                      <i class="el-icon-s-grid"></i>基本信息
                    </h3>
                    <table cellspacing="0" cellpadding="0" border="0">
                      <colgroup>
                        <col width="15%" />
                        <col width="35%" />
                        <col width="15%" />
                        <col width="35%" />
                      </colgroup>
                      <tr>
                        <td><b>业户名称：</b></td>
                        <td colspan="3">
                          <el-form-item prop="companyName">
                            <el-input
                              v-model.trim="form.companyName"
                              placeholder="请输入业户名称"
                              maxlength="50"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                      </tr>
                      <tr>
                        <td><b>地址：</b></td>
                        <td colspan="3">
                          <el-form-item prop="address">
                            <el-input
                              v-model.trim="form.address"
                              placeholder="请输入地址"
                              maxlength="50"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                      </tr>
                      <tr>
                        <td><b>法定代表人身份证号：</b></td>
                        <td>
                          <el-form-item prop="legalIdcard">
                            <el-input
                              v-model.trim="form.legalIdcard"
                              placeholder="请输入法定代表人身份证号"
                              maxlength="20"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                        <td><b>联系电话：</b></td>
                        <td>
                          <el-form-item prop="mobile">
                            <el-input
                              v-model.trim="form.mobile"
                              placeholder="请输入联系电话"
                              maxlength="6"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                      </tr>
                    </table>
                  </div>

                  <div class="el-table__header-wrapper dialog-table">
                    <el-button
                      type="primary"
                      @click="addCarInfo"
                      size="mini"
                      class="data-btn"
                      >增加</el-button
                    >
                    <h3 class="title">
                      <i class="el-icon-s-grid"></i>货运车辆基本情况
                    </h3>
                    <table
                      cellspacing="0"
                      cellpadding="0"
                      border="0"
                      class="data-table"
                    >
                      <colgroup>
                        <col width="5%" />
                        <col width="10%" />
                        <col width="10%" />
                        <col width="10%" />
                        <col width="10%" />
                        <col width="10%" />
                        <col width="10%" />
                        <col width="10%" />
                        <col width="15%" />
                        <col width="10%" />
                      </colgroup>
                      <tr>
                        <th>序号</th>
                        <th>运输证号</th>
                        <th>车辆牌照</th>
                        <th>厂牌型号</th>
                        <th>车辆类型</th>
                        <th>吨位（吨）</th>
                        <th>燃料</th>
                        <th>轴数</th>
                        <th>技术等级</th>
                        <th>操作</th>
                      </tr>
                      <tbody v-for="(item, index) in form.carList">
                        <tr>
                          <td>{{ index + 1 }}</td>
                          <td>
                            <el-form-item prop="transportLicenseNo">
                              <el-input
                                v-model.trim="item.transportLicenseNo"
                                placeholder="请输入运输证号"
                                maxlength="50"
                                show-word-limit
                              />
                            </el-form-item>
                          </td>
                          <td>
                            <el-form-item prop="carLicense">
                              <el-input
                                v-model.trim="item.carLicense"
                                placeholder="请输入车辆牌照"
                                maxlength="20"
                                show-word-limit
                              />
                            </el-form-item>
                          </td>
                          <td>
                            <el-form-item prop="cpxhNum">
                              <el-input
                                v-model.trim="item.cpxhNum"
                                placeholder="请输入厂牌型号"
                                maxlength="20"
                                show-word-limit
                              />
                            </el-form-item>
                          </td>
                          <td>
                            <el-form-item prop="carType">
                              <el-input
                                v-model.trim="item.carType"
                                placeholder="请输入车辆类型"
                                maxlength="50"
                                show-word-limit
                              />
                            </el-form-item>
                          </td>
                          <td>
                            <el-form-item prop="weightNum">
                              <el-input
                                v-model.trim="item.weightNum"
                                placeholder="请输入吨位（吨）"
                                maxlength="5"
                                show-word-limit
                              />
                            </el-form-item>
                          </td>
                          <td>
                            <el-form-item prop="fuel">
                              <el-input
                                v-model.trim="item.fuel"
                                placeholder="请输入燃料"
                                maxlength="10"
                                show-word-limit
                              />
                            </el-form-item>
                          </td>
                          <td>
                            <el-form-item prop="axesNum">
                              <el-input
                                v-model.trim="item.axesNum"
                                placeholder="请输入轴数"
                                maxlength="5"
                                show-word-limit
                              />
                            </el-form-item>
                          </td>
                          <td>
                            <el-form-item prop="technicalLevel">
                              <el-input
                                v-model.trim="item.technicalLevel"
                                placeholder="请输入技术等级"
                                maxlength="20"
                                show-word-limit
                              />
                            </el-form-item>
                          </td>
                          <td>
                            <el-button
                              size="mini"
                              type="text"
                              icon="iconfont zfsoft-shanchu"
                              @click="delCarInfo(index)"
                              >删除
                            </el-button>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>

                  <div class="el-table__header-wrapper dialog-table">
                    <el-button
                      type="primary"
                      @click="addDirverInfo"
                      size="mini"
                      class="data-btn"
                      >增加</el-button
                    >
                    <h3 class="title">
                      <i class="el-icon-s-grid"></i>驾驶员基本情况
                    </h3>
                    <table
                      cellspacing="0"
                      cellpadding="0"
                      border="0"
                      class="data-table"
                    >
                      <colgroup>
                        <col width="5%" />
                        <col width="15%" />
                        <col width="10%" />
                        <col width="10%" />
                        <col width="10%" />
                        <col width="20%" />
                        <col width="10%" />
                        <col width="10%" />
                        <col width="10%" />
                      </colgroup>
                      <tr>
                        <th>序号</th>
                        <th>姓名</th>
                        <th>性别</th>
                        <th>年龄</th>
                        <th>文化程度</th>
                        <th>取得驾驶证时间</th>
                        <th>准驾车型</th>
                        <th>从业资格证号</th>
                        <th>操作</th>
                      </tr>
                      <tbody v-for="(item, index) in form.driverList">
                        <tr>
                          <td>{{ index + 1 }}</td>
                          <td>
                            <el-form-item prop="driverName">
                              <el-input
                                v-model.trim="item.driverName"
                                placeholder="请输入驾驶员姓名"
                                maxlength="50"
                                show-word-limit
                              />
                            </el-form-item>
                          </td>
                          <td>
                            <el-form-item prop="sex">
                              <el-input
                                v-model.trim="item.sex"
                                placeholder="请输入性别"
                                maxlength="5"
                                show-word-limit
                              />
                            </el-form-item>
                          </td>
                          <td>
                            <el-form-item prop="age">
                              <el-input
                                v-model.trim="item.age"
                                placeholder="请输入年龄"
                                maxlength="5"
                                show-word-limit
                              />
                            </el-form-item>
                          </td>
                          <td>
                            <el-form-item prop="eduLevel">
                              <el-input
                                v-model.trim="item.eduLevel"
                                placeholder="请输入文化程度"
                                maxlength="50"
                                show-word-limit
                              />
                            </el-form-item>
                          </td>
                          <td>
                            <el-form-item prop="linceseTime">
                              <el-input
                                v-model.trim="item.linceseTime"
                                placeholder="请输入取得驾驶证时间"
                                maxlength="5"
                                show-word-limit
                              />
                            </el-form-item>
                          </td>
                          <td>
                            <el-form-item prop="carTypeUse">
                              <el-input
                                v-model.trim="item.carTypeUse"
                                placeholder="请输入准驾车型"
                                maxlength="20"
                                show-word-limit
                              />
                            </el-form-item>
                          </td>
                          <td>
                            <el-form-item prop="cyzgNum">
                              <el-input
                                v-model.trim="item.cyzgNum"
                                placeholder="请输入从业资格证号"
                                maxlength="50"
                                show-word-limit
                              />
                            </el-form-item>
                          </td>
                          <td>
                            <el-button
                              size="mini"
                              type="text"
                              icon="iconfont zfsoft-shanchu"
                              @click="delDriverInfo(index)"
                              >删除
                            </el-button>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </el-form>
              </div>
            </el-tab-pane>
            <!--            <el-tab-pane label="变更法人登记信息" name="third" v-if="serviceOid=='2c287b8b79f39489017a190d7f8c13ff'">
              <el-form ref="form" :model="form" label-width="0px">
                <div class="el-table__header-wrapper dialog-table">
                  <h3 class="title"><i class="el-icon-s-grid"></i>基本信息</h3>
                  <table cellspacing="0" cellpadding="0" border="0" >
                    <colgroup>
                      <col width="15%" />
                      <col width="35%" />
                      <col width="15%" />
                      <col width="35%" />
                    </colgroup>
                    <tr>
                      <td><i class="require">*</i><b>名称：</b></td>
                      <td colspan="3">
                        <el-form-item prop="companyName">
                          <el-input v-model.trim="form.companyName" placeholder="请输入名称(集团母公司需填写：集团名称：分情形，有集团填写)" maxlength="50" show-word-limit/>
                        </el-form-item>
                      </td>
                    </tr>
                    <tr>
                      <td><i class="require">*</i><b>统一社会信用代码：</b></td>
                      <td colspan="3">
                        <el-form-item prop="socialCreditCode">
                          <el-input v-model.trim="form.socialCreditCode" placeholder="请输入统一社会信用代码" maxlength="50" show-word-limit/>
                        </el-form-item>
                      </td>
                    </tr>
                    <tr>
                      <td><i class="require">*</i><b>联系电话：</b></td>
                      <td>
                        <el-form-item prop="mobile">
                          <el-input v-model.trim="form.mobile" placeholder="请输入联系电话" maxlength="20" show-word-limit/>
                        </el-form-item>
                      </td>
                      <td><i class="require">*</i><b>邮政编码：</b></td>
                      <td>
                        <el-form-item prop="code">
                          <el-input v-model.trim="form.code" placeholder="请输入邮政编码" maxlength="6" show-word-limit/>
                        </el-form-item>
                      </td>
                    </tr>
                  </table>
                </div>

                <div class="el-table__header-wrapper dialog-table">
                  <h3 class="title"><i class="el-icon-s-grid"></i>指定代表/委托代理人</h3>
                  <table cellspacing="0" cellpadding="0" border="0" >
                    <colgroup>
                      <col width="15%" />
                      <col width="35%" />
                      <col width="15%" />
                      <col width="35%" />
                    </colgroup>
                    <tr>
                      <td><i class="require">*</i><b>姓名：</b></td>
                      <td colspan="3">
                        <el-form-item prop="contactUserName">
                          <el-input v-model.trim="form.contactUserName" placeholder="请输入姓名" maxlength="50" show-word-limit/>
                        </el-form-item>
                      </td>
                    </tr>
                    <tr>
                      <td><i class="require">*</i><b>身份证件类型：</b></td>
                      <td>
                        <el-form-item prop="contactCredentialType">
                          <el-input v-model.trim="form.contactCredentialType" placeholder="请输入身份证件类型" maxlength="50" show-word-limit/>
                        </el-form-item>
                      </td>
                      <td><i class="require">*</i><b>身份证件号码：</b></td>
                      <td>
                        <el-form-item prop="contactCredentialNumber">
                          <el-input v-model.trim="form.contactCredentialNumber" placeholder="请输入身份证件号码" maxlength="50" show-word-limit/>
                        </el-form-item>
                      </td>
                    </tr>
                    <tr>
                      <td><i class="require">*</i><b>固定电话：</b></td>
                      <td>
                        <el-form-item prop="contactMobile">
                          <el-input v-model.trim="form.contactMobile" placeholder="请输入固定电话" maxlength="25" show-word-limit/>
                        </el-form-item>
                      </td>
                      <td><i class="require">*</i><b>移动电话：</b></td>
                      <td>
                        <el-form-item prop="contactPhone">
                          <el-input v-model.trim="form.contactPhone" placeholder="请输入移动电话" maxlength="11" show-word-limit/>
                        </el-form-item>
                      </td>
                    </tr>
                    <tr>
                      <td><i class="require">*</i><b>委托权限：</b></td>
                      <td colspan="3">
                        <el-form-item prop="checkAuthority">1、
                          <el-radio-group v-model.trim="form.checkAuthority" @change="changeFlag">
                            <el-radio label="1">同意</el-radio>
                            <el-radio label="0">不同意</el-radio>
                          </el-radio-group>
                          <span>核对登记材料中的复印件并签署核对意见</span>
                        </el-form-item>
                        <el-form-item prop="updateErrorFile">2、
                          <el-radio-group v-model.trim="form.updateErrorFile" @change="changeFlag">
                            <el-radio label="1">同意</el-radio>
                            <el-radio label="0">不同意</el-radio>
                          </el-radio-group>
                          <span>修改企业自备文件的错误</span>
                        </el-form-item>
                        <el-form-item prop="updateTableFile">3、
                          <el-radio-group v-model.trim="form.updateTableFile" @change="changeFlag">
                            <el-radio label="1">同意</el-radio>
                            <el-radio label="0">不同意</el-radio>
                          </el-radio-group>
                          <span>修改有关表格的填写错误</span>
                        </el-form-item>
                        <el-form-item prop="receiveAuthority">4、
                          <el-radio-group v-model.trim="form.receiveAuthority" @change="changeFlag">
                            <el-radio label="1">同意</el-radio>
                            <el-radio label="0">不同意</el-radio>
                          </el-radio-group>
                          <span>领取营业执照和有关文书</span>
                        </el-form-item>
                      </td>
                    </tr>
                  </table>
                </div>
              </el-form>
            </el-tab-pane>-->
            <el-tab-pane
              label="变更事项"
              name="fourth"
              v-if="serviceOid == '2c287b8b79f39489017a190d7f8c13ff'"
            >
              <div class="el-table__header-wrapper dialog-table">
                <el-button
                  type="primary"
                  style="float:right"
                  @click="addBgService"
                  class="data-btn"
                  >增加</el-button
                >
                <h3 class="title"><i class="el-icon-s-grid"></i>变更事项</h3>
                <el-form ref="bgForm" :model="form" label-width="0px">
                  <table
                    cellspacing="0"
                    cellpadding="0"
                    border="0"
                    class="data-table"
                  >
                    <colgroup>
                      <col width="20%" />
                      <col width="35%" />
                      <col width="35%" />
                      <col width="10%" />
                    </colgroup>
                    <tr>
                      <th>变更事项</th>
                      <th>原登记内容</th>
                      <th>变更后登记内容</th>
                      <th>操作</th>
                    </tr>
                    <tbody v-for="(item, index) in form.bglist">
                      <tr>
                        <td>
                          <el-form-item prop="serviceName">
                            <el-input
                              v-model.trim="item.serviceName"
                              placeholder="请输入变更事项"
                              maxlength="100"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                        <td>
                          <el-form-item prop="oldService">
                            <el-input
                              v-model.trim="item.oldService"
                              placeholder="请输入原登记内容"
                              maxlength="1500"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                        <td>
                          <el-form-item prop="newService">
                            <el-input
                              v-model.trim="item.newService"
                              placeholder="请输入变更后登记内容"
                              maxlength="1500"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                        <td>
                          <el-button
                            size="mini"
                            type="text"
                            icon="iconfont zfsoft-shanchu"
                            @click="delBg(index)"
                            >删除
                          </el-button>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </el-form>
              </div>
            </el-tab-pane>
            <el-tab-pane
              label="新法定代表人信息"
              name="five"
              v-if="serviceOid == '2c287b8b79f39489017a190d7f8c13ff'"
            >
              <div class="el-table__header-wrapper dialog-table">
                <h3 class="title">
                  <i class="el-icon-s-grid"></i>新法定代表人信息
                </h3>
                <el-form
                  ref="formLegal"
                  :model="form"
                  :rules="rulesNzgs"
                  label-width="0px"
                >
                  <table cellspacing="0" cellpadding="0" border="0">
                    <colgroup>
                      <col width="15%" />
                      <col width="35%" />
                      <col width="15%" />
                      <col width="35%" />
                    </colgroup>
                    <tr>
                      <td><i class="require">*</i><b>姓名：</b></td>
                      <td>
                        <el-form-item prop="legalPersonName">
                          <el-input
                            v-model.trim="form.legalPersonName"
                            placeholder="请输入姓名"
                            maxlength="50"
                            show-word-limit
                          />
                        </el-form-item>
                      </td>
                      <td><i class="require">*</i><b>国别(地区)：</b></td>
                      <td>
                        <el-form-item prop="national">
                          <el-input
                            v-model.trim="form.national"
                            placeholder="请输入国别(地区)"
                            maxlength="50"
                            show-word-limit
                          />
                        </el-form-item>
                      </td>
                    </tr>
                    <tr>
                      <td><i class="require">*</i><b>职务：</b></td>
                      <td>
                        <el-form-item prop="legalPersonPost">
                          <el-radio-group
                            v-model.trim="form.legalPersonPost"
                            @change="changeFlag"
                          >
                            <el-radio label="0">董事长</el-radio>
                            <el-radio label="1">执行董事</el-radio>
                            <el-radio label="2">经理</el-radio>
                          </el-radio-group>
                        </el-form-item>
                      </td>
                      <td><i class="require">*</i><b>产生方式：</b></td>
                      <td>
                        <el-form-item prop="prodMethod">
                          <el-input
                            v-model.trim="form.prodMethod"
                            placeholder="请输入产生方式"
                            maxlength="100"
                            show-word-limit
                          />
                        </el-form-item>
                      </td>
                    </tr>
                    <tr>
                      <td><i class="require">*</i><b>身份证件类型：</b></td>
                      <td>
                        <el-form-item prop="legalPersonZjType">
                          <el-input
                            v-model.trim="form.legalPersonZjType"
                            placeholder="请输入身份证件类型"
                            maxlength="50"
                            show-word-limit
                          />
                        </el-form-item>
                      </td>
                      <td><i class="require">*</i><b>身份证件号码：</b></td>
                      <td>
                        <el-form-item prop="legalPersonNumber">
                          <el-input
                            v-model.trim="form.legalPersonNumber"
                            placeholder="请输入身份证件号码"
                            maxlength="50"
                            show-word-limit
                          />
                        </el-form-item>
                      </td>
                    </tr>
                    <tr>
                      <td><i class="require">*</i><b>固定电话：</b></td>
                      <td>
                        <el-form-item prop="legalPersonMobile">
                          <el-input
                            v-model.trim="form.legalPersonMobile"
                            placeholder="请输入固定电话"
                            maxlength="25"
                            show-word-limit
                          />
                        </el-form-item>
                      </td>
                      <td><i class="require">*</i><b>移动电话：</b></td>
                      <td>
                        <el-form-item prop="legalPersonPhone">
                          <el-input
                            v-model.trim="form.legalPersonPhone"
                            placeholder="请输入移动电话"
                            maxlength="11"
                            show-word-limit
                          />
                        </el-form-item>
                      </td>
                    </tr>
                    <tr>
                      <td><i class="require">*</i><b>住所：</b></td>
                      <td>
                        <el-form-item prop="legalPersonAddress">
                          <el-input
                            v-model.trim="form.legalPersonAddress"
                            placeholder="请输入住所"
                            maxlength="100"
                            show-word-limit
                          />
                        </el-form-item>
                      </td>
                      <td><i class="require">*</i><b>电子邮箱：</b></td>
                      <td>
                        <el-form-item prop="legalPersonEmail">
                          <el-input
                            v-model.trim="form.legalPersonEmail"
                            placeholder="请输入电子邮箱"
                            maxlength="25"
                            show-word-limit
                          />
                        </el-form-item>
                      </td>
                    </tr>
                  </table>
                </el-form>
              </div>
            </el-tab-pane>
            <el-tab-pane
              label="股东(发起人)、外国投资者出资情况"
              name="six"
              v-if="serviceOid == '2c287b8b79f39489017a190d7f8c13ff'"
            >
              <div class="el-table__header-wrapper dialog-table">
                <el-button
                  type="primary"
                  style="float:right"
                  @click="addCzqk"
                  class="data-btn"
                  >增加</el-button
                >
                <h3 class="title">
                  <i class="el-icon-s-grid"></i>股东(发起人)、外国投资者出资情况
                </h3>
                <el-form ref="czform" :model="form" label-width="0px">
                  <table
                    cellspacing="0"
                    cellpadding="0"
                    border="0"
                    class="data-table"
                  >
                    <colgroup>
                      <col width="10%" />
                      <col width="10%" />
                      <col width="10%" />
                      <col width="15%" />
                      <col width="10%" />
                      <col width="10%" />
                      <col width="10%" />
                      <col width="10%" />
                      <col width="10%" />
                      <col width="10%" />
                    </colgroup>
                    <tr>
                      <th>姓名</th>
                      <th>国别(地区)</th>
                      <th>证件类型</th>
                      <th>证件号码</th>
                      <th>认缴出资额</th>
                      <th>实缴出资额</th>
                      <th>出资(认缴)时间</th>
                      <th>出资方式</th>
                      <th>出资比例</th>
                      <th>操作</th>
                    </tr>
                    <tbody v-for="(czItem, dz) in form.czqkList">
                      <tr>
                        <td>
                          <el-form-item>
                            <el-input
                              v-model.trim="czItem.username"
                              placeholder="请输入姓名"
                              maxlength="50"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                        <td>
                          <el-form-item>
                            <el-input
                              v-model.trim="czItem.national"
                              placeholder="请输入国别(地区)"
                              maxlength="50"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                        <td>
                          <el-form-item>
                            <el-input
                              v-model.trim="czItem.zjType"
                              placeholder="请输入证件类型"
                              maxlength="20"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                        <td>
                          <el-form-item>
                            <el-input
                              v-model.trim="czItem.zjNumber"
                              placeholder="请输入证件号码"
                              maxlength="50"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                        <td>
                          <el-form-item>
                            <el-input
                              v-model.trim="czItem.rjczNum"
                              placeholder="请输入认缴出资额"
                              maxlength="10"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                        <td>
                          <el-form-item>
                            <el-input
                              v-model.trim="czItem.sjczNum"
                              placeholder="请输入实缴出资额"
                              maxlength="10"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                        <td>
                          <el-form-item>
                            <el-input
                              v-model.trim="czItem.czrjTime"
                              placeholder="请输入出资(认缴)时间"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                        <td>
                          <el-form-item>
                            <el-input
                              v-model.trim="czItem.czfs"
                              placeholder="请输入出资方式"
                              maxlength="100"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                        <td>
                          <el-form-item>
                            <el-input
                              v-model.trim="czItem.czbl"
                              placeholder="请输入出资比例"
                              maxlength="10"
                              show-word-limit
                            />
                          </el-form-item>
                        </td>
                        <td>
                          <el-button
                            size="mini"
                            type="text"
                            icon="iconfont zfsoft-shanchu"
                            @click="delBgCz(dz)"
                            >删除
                          </el-button>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </el-form>
              </div>
            </el-tab-pane>
          </el-tabs>

          <div class="btn-wrap">
            <div class="btn-list mt10">
              <el-button type="primary" @click="getImageCamera"
                >人证比对</el-button
              >
              <el-button type="primary" icon="el-icon-back" @click="next(2, -1)"
                >上一步</el-button
              >
              <el-button
                type="info"
                icon="el-icon-video-pause"
                :disabled="isDisable"
                class="print-btn"
                @click="saveApplyCaseForm('')"
                >暂存</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-circle-check"
                @click="next(4, 1)"
                >下一步
              </el-button>
            </div>
          </div>
        </el-form>
      </div>
      <!-- 第五步 -->
      <div class="step-content step-last" v-if="show_4">
        <h3 class="title"><i class="el-icon-s-grid"></i>申请者相关信息</h3>
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
            class="el-table__body"
          >
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tr>
              <td><b>审批部门：</b></td>
              <td colspan="3">{{ loginUser.organName }}</td>
            </tr>
            <tr>
              <td><b>是否受理：</b></td>
              <td colspan="3">
                <el-radio-group v-model="ruleForm.acceptradio">
                  <el-radio label="1">受理通过</el-radio>
                  <el-radio label="2">不予受理</el-radio>
                </el-radio-group>
              </td>
            </tr>
            <tr v-if="isRqslFlag == true">
              <td><i class="require">*</i><b>承诺补正时间：</b></td>
              <td colspan="3">
                <el-date-picker
                  v-model="ruleForm.bqbzDueDate"
                  type="date"
                  value-format="yyyy-MM-dd"
                  :picker-options="optionDate"
                  placeholder="选择承诺补正时间"
                ></el-date-picker>
              </td>
            </tr>
            <tr>
              <td>
                <i class="require" v-if="ruleForm.acceptradio == 2">*</i
                ><b>意见说明：</b>
              </td>
              <td colspan="3">
                <el-form-item label="" prop="desc">
                  <el-col :span="24">
                    <el-input
                      type="textarea"
                      v-model="ruleForm.finalOpinionDesc"
                      maxlength="500"
                      show-word-limit
                    >
                    </el-input>
                  </el-col>
                </el-form-item>
              </td>
            </tr>
          </table>

          <div class="btn-wrap">
            <div class="btn-list mt10">
              <el-button
                type="primary"
                icon="el-icon-circle-check"
                @click="next(3, -1)"
                >上一步</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-video-pause"
                v-if="isRqslFlag == false"
                @click="pushPjCaseService"
              >
                受理</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-video-pause"
                v-if="isRqslFlag == true"
                @click="pushPjCaseService"
              >
                容缺受理</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-video-pause"
                @click="caseSign"
              >
                签名</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-video-pause"
                v-if="flagCc"
                @click="qlCaseCharge"
              >
                收费</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-video-pause"
                v-if="chakanCharge"
                @click="viewCharge"
              >
                查看缴费</el-button
              >
              <el-button
                type="primary"
                v-if="bqbzCaseForm.dueDate"
                @click="printNotice()"
                >告知承诺书</el-button
              >
            </div>
          </div>
        </el-form>
      </div>
    </div>
    <!--表单嵌套页面-->
    <el-dialog
      title="电子表单"
      :visible.sync="iframeState"
      v-if="iframeState"
      width="80%"
      height="100%"
      :modal-append-to-body="true"
      append-to-body
    >
      <iframe-url :outLink="iframeUrl" @closeIframe="closeIframe"></iframe-url>
    </el-dialog>

    <el-dialog
      title="电子表单预览"
      :visible.sync="iframVieweState"
      v-if="iframVieweState"
      width="80%"
      height="100%"
      :modal-append-to-body="true"
      append-to-body
    >
      <iframe-url-view
        :outLink="iframeUrlView"
        @closeIframeView="closeIframeView"
      ></iframe-url-view>
    </el-dialog>

    <!--引入文件的预览弹出层-->
    <el-dialog
      title="文件预览"
      :visible.sync="showSxFile"
      v-if="showSxFile"
      @close="closeFileView"
      width="60%"
      append-to-body
      :modal-append-to-body="true"
    >
      <material-file-view
        :attaOid="fileSxAttaOid"
        @father-colose="closeFileView"
      ></material-file-view>
    </el-dialog>
    <!--引入办件文件的预览弹出层-->
    <el-dialog
      v-dialog-drag
      title="文件预览"
      :visible.sync="showFile"
      v-if="showFile"
      @close="closeCaseFileView"
      width="60%"
      append-to-body
    >
      <case-file-view
        :attaOid="fileAttaOid"
        @father-click="closeCaseFileView"
      ></case-file-view>
    </el-dialog>
    <!--材料比对-->
    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in materialComparisonOptions"
      @close="outerVisible"
      :title="item.title"
      width="80%"
      height="700px"
      append-to-body
    >
      <el-scrollbar style="height:650px;">
        <material-comparison
          :sampleInfoOid="item.sampleInfoOid"
          :comboDirectoryOid="item.comboDirectoryOid"
          :materiaOid="item.materiaOid"
          :caseOid="item.caseOid"
          :fileViewurl="item.fileViewurl"
          :attaOids="item.attaOids"
          :title="item.title"
          @father-click="openTempletePic"
        >
        </material-comparison>
      </el-scrollbar>
    </el-dialog>

    <!-- 材料智审-->
    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in intelligentPretrialOptions"
      @close="outerVisible"
      :title="item.title"
      width="50%"
      append-to-body
    >
      <el-scrollbar style="height:500px;">
        <intelligent-pretrial-new
          :caseOid="item.caseOid"
          :caseMaterialOid="item.caseMaterialOid"
          :caseFileRecOid="item.caseFileRecOid"
          :cataOid="item.cataOid"
          :title="item.title"
          @father-click="handleChildView"
        ></intelligent-pretrial-new>
      </el-scrollbar>
    </el-dialog>

    <!-- 获取申请人信息-->
    <el-dialog
      v-dialog-drag
      :visible.sync="userInfoShow"
      v-if="userInfoShow"
      title="申请人信息列表"
      width="60%"
      append-to-body
    >
      <el-scrollbar style="height:500px;">
        <apply-user-list
          :applyCardNum="applyCardNum"
          @selectUserOk="closeUserInfoShow"
        >
        </apply-user-list>
      </el-scrollbar>
    </el-dialog>

    <!-- 获取收费信息-->
    <el-dialog
      v-dialog-drag
      :visible.sync="chargeShow"
      v-if="chargeShow"
      title="办件收费"
      width="60%"
      append-to-body
    >
      <case-charge @case-close="closeCaseCharge"></case-charge>
    </el-dialog>

    <!-- 获取收费信息-->
    <el-dialog
      v-dialog-drag
      :visible.sync="viewchargeShow"
      v-if="viewchargeShow"
      title="查看收费信息"
      width="60%"
      append-to-body
    >
      <view-charge @case-close="closeView"></view-charge>
    </el-dialog>

    <!-- 人证比对 -->
    <div v-if="dialogVisible">
      <el-dialog
        :visible.sync="dialogVisible"
        width="80%"
        :before-close="handleClose"
        :close-on-click-modal="false"
        append-to-body
      >
        <el-scrollbar>
          <hardware-scan ref="scanForm"></hardware-scan>
        </el-scrollbar>
      </el-dialog>
    </div>

    <!-- 内资子公司变更表单 -->
    <div v-if="bgForm">
      <el-dialog
        :visible.sync="bgForm"
        width="80%"
        title="公司登记(备案)申请书"
        :close-on-click-modal="false"
        append-to-body
      >
        <el-scrollbar>
          <nzgsbg-form
            ref="nzgsbgForm"
            :caseOid="caseOid"
            @close-bgForm="closebgForm"
          ></nzgsbg-form>
        </el-scrollbar>
      </el-dialog>
    </div>

    <div v-if="nzgsbgViewForm">
      <el-dialog
        :visible.sync="nzgsbgViewForm"
        width="80%"
        title="公司登记(备案)申请书"
        :close-on-click-modal="false"
        append-to-body
      >
        <el-scrollbar style="height:860px;">
          <nzgsbg-form-view
            :caseOid="caseOid"
            @close-bgnzForm="closebgnzForm"
          ></nzgsbg-form-view>
        </el-scrollbar>
      </el-dialog>
    </div>

    <!-- 道路运输许可证表单 -->
    <div v-if="dlysForm">
      <el-dialog
        :visible.sync="dlysForm"
        width="80%"
        title="道路货物运输经营许可证新办审批申请表(个体)"
        :close-on-click-modal="false"
        append-to-body
      >
        <el-scrollbar style="height:860px;">
          <dlysxkz-form
            ref="dlysxkzForm"
            :caseOid="caseOid"
            @close-bgForm="closedlForm"
          ></dlysxkz-form>
        </el-scrollbar>
      </el-dialog>
    </div>

    <div v-if="dlysViewForm">
      <el-dialog
        :visible.sync="dlysViewForm"
        width="80%"
        title="道路货物运输经营许可证新办审批申请表(个体)"
        :close-on-click-modal="false"
        append-to-body
      >
        <el-scrollbar style="height:860px;">
          <dlysxkz-form-view
            :caseOid="caseOid"
            @close-bgViewForm="closedlViewForm"
          ></dlysxkz-form-view>
        </el-scrollbar>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import {
  getSxService,
  save,
  getQlCaseByOid,
  getCertificateType,
  getloginUser,
  saveCaseAccpet,
  getSituationOpinionList,
  getAllSituationList,
  getSxServiceOptionAllTitleValRelation,
  getSituationMaterialListByOids,
  saveOut,
  changeCredentialType,
  downloadAttaSimple,
  getInformPromiseByServiceOid,
  getDishonestPerson,
  saveOrUpdateGzBqbz,
  getCaseFormInfo,
  saveOrUpdateInfo,
  getFileDownPath,
  downloadPrintFile
} from "@/api/zc/businessManagement/windowAcceptance";
import IframeUrl from "@/views/iframe/formIndex";
import IframeUrlView from "@/views/iframe/formIndexView";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import {
  validatePhone,
  validateTel,
  validIDCard,
  validatePostCode,
  validateEmails,
  validUnifiedCredit,
  validateNumberNoPonint
} from "@/utils/validate";
import {
  uploadCaseMaterialFile,
  saveQlCaseMaterialAtta,
  getSxServiceMaterialAttaList
} from "@/api/zc/businessManagement/caseMaterialAtta";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { VueCropper } from "vue-cropper";
import { regionData } from "element-china-area-data";
import {
  getElecLicenUrl,
  queryElecLicenseByDirCode
} from "@/api/zc/businessManagement/elemLice";
import {
  sxSerFormByServiceOid,
  saveOrUpdateCaseForm
} from "@/api/zc/businessManagement/formConfig";
import {
  getPjServiceSystem,
  showCallMessage
} from "@/api/zc/businessManagement/doubleScreenInteraction";
import {
  getCertificateTypeByOid,
  intelligentPretrialmaterialPrePrial,
  queryAllCaseByOid
} from "@/api/zc/businessManagement/temporaryAcceptance";
import materialComparison from "@/views/zc/businessManagement/materialCheckKeyManage/materialComparison";
import intelligentPretrialNew from "@/views/zc/clzs/directoryManagement/intelligentPretrial";
import materialFileView from "@/views/zc/businessManagement/windowAcceptance/materialFileView";
import caseFileView from "@/views/zc/businessManagement/caseBqbz/caseFileView";
import applyUserList from "@/views/zc/businessManagement/temporaryAcceptance/applyUserList";
import caseCharge from "@/views/zc/businessManagement/charge/caseCharge";
import viewCharge from "@/views/zc/businessManagement/charge/viewCharge";
import hardwareScan from "@/views/common/hardwareScan";
import NzgsbgForm from "@/views/zc/businessManagement/windowAcceptance/nzgsbgForm";
import NzgsbgFormView from "@/views/zc/businessManagement/windowAcceptance/nzgsbgFormView";
import DlysxkzForm from "@/views/zc/businessManagement/windowAcceptance/dlysxkzForm";
import DlysxkzFormView from "@/views/zc/businessManagement/windowAcceptance/dlysxkzFormView";
export default {
  inheritAttrs: false,
  components: {
    NzgsbgForm,
    Treeselect,
    VueCropper,
    IframeUrl,
    IframeUrlView,
    materialFileView,
    caseFileView,
    materialComparison,
    intelligentPretrialNew,
    applyUserList,
    caseCharge,
    viewCharge,
    hardwareScan,
    DlysxkzForm,
    DlysxkzFormView,
    NzgsbgFormView
  },
  props: ["cegisterType", "serviceOid"], //登记类型 法人1 自然人0
  data() {
    return {
      provinceCityOptions: regionData,
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
      formConfig_show: false, //标识表单是否显示
      iframeState: false,
      iframVieweState: false,
      showSxFile: false,
      fileSxAttaOid: "",
      iframeUrl: {}, //表单地址
      iframeUrlView: {}, //查看
      sxSerForm: {}, //配置表单的信息
      show_scan: [],
      show_elem: [],
      show_upload: [],
      bqbzCaseForm: {},
      radio: "",
      radio1: [],
      radio2: "1",
      isDisable: false,
      isNextDisable: false,
      ifDis: false,
      //上传按钮、扫描按钮切换
      changeIndex: "",
      changeButton: "1",
      msg_val: {},
      checkFlag: false,
      radioNotice: "",
      // 遮罩层
      loading: true,
      isCheckedDisable: false,
      collectionArray: [],
      isRqslFlag: false, //是否容缺受理
      isSxPersonFlg: false, //是否为失信人员
      // 业务办理事项数据
      serviceList: [],
      // 事项情形与选项关系数据
      situationOptions: [],
      // 事项选项值数据
      situationOptionVals: [],
      //材料比对页面
      materialComparisonOptions: [],
      //材料智审
      intelligentPretrialOptions: [],
      //登录信息
      loginUser: {},
      fileAttaOid: "",
      showFile: false,
      //评价类型
      pjType: "",
      //评价服务系统
      systemType: "",
      //查询办件主键
      id: "",
      //查询办件业务主键
      caseOid: "",
      //查询办件申请信息业务主键
      applyOid: "",
      //查询办件扩展信息业务主键
      extOid: "",
      //办件状态
      caseStatus: "",
      //办件编码
      caseNumber: "",
      createDate: "",
      //事项类型名称
      serviceTypeName: "",
      //材料办件业务主键
      caseMaterialOids: [],
      //环节信息主键
      linkResultOid: "",
      //证件类型
      certificateTypeList: [],
      // 机构
      listOrganOptions: [],
      // 区划
      districtOptions: [],
      // 事项类型
      serviceTypeOptions: [],
      //事项
      sxService: null,
      //办理地点
      sxServiceLocations: [],
      //事项材料
      sxServiceMaterialList: [],
      sxServiceMaterialAttaList: [],
      //情形标题
      sxServiceOptionTitles: [],
      //情形标题--必选
      sxServiceOptionTitlesMust: [],
      //办件与情形标题选项关系
      qlCaseTitleValList: [],
      //事项问题
      sxServiceQuestions: [],
      //事项环节
      sxServiceLinks: [],
      //事项扩展
      sxServiceExtend: null,
      //事项情形
      sxServiceSituations: [],
      //材料上传方式
      materialCollectionTypeList: [],
      situationOid: null,
      situationName: "默认自定情形",
      fileList: [],
      showFileList: false,
      cardType: "",
      chargeFlag: "",
      accept: {
        type: String,
        default: ".jpg,.jpeg,.png,.pdf,.PDF,.doc,.docx,.DOC,.DOCX"
      },
      //附件集合
      attaList: [],
      //材料业务主键
      caseMaterialOid: null,
      indexFlag: null,
      //材料与上传成功的附件集合
      materialAttaList: [],
      //材料与上传成功的附件集合
      materialAttaOidList: [],
      elemLicenseList: [],
      catalogCheckList: [],
      catalogList: [],
      caseForm: {},
      // 表单参数
      ruleForm: {
        applyUserPhone: "",
        applyUserTel: "",
        applyUserAddress: "",
        legalPersonName: "",
        projectName: "",
        applyPostCode: "",
        specificLocation: "",
        investProjecName: "",
        investProjectCode: "",
        projectAbstract: "",
        contactEmail: "",
        contactUserPhone: "",
        contactUserTel: "",
        contactRemark: "",
        addresseeName: "",
        addresseePostCode: "",
        addresseePhone: "",
        addresseeTel: "",
        addresseeAddress: "",
        addresseeDetailAddress: "",
        finalOpinionDesc: "",
        applyNumber: "1",
        resultDeliveryWay: "1",
        proxyFlag: "0",
        sourceType: 1,
        sourceApp: 1,
        acceptradio: "1",
        credentialNumber: "",
        applyUserName: "",
        credentialType: "",
        contactCredentialNumber: "",
        contactUserName: "",
        bussVenueDistrictOid: []
      },
      serviceForm: {
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
      formData: {},
      labelPosition: "top",
      checkList: [],
      checkBoxList: [],
      viewchargeShow: false,
      chakanCharge: false,
      flagCc: true,
      stepData: [
        {
          index: "0",
          title: "智能问答"
        },
        {
          index: "1",
          title: "一次告知"
        },
        {
          index: "2",
          title: "材料电子化"
        },
        {
          index: "3",
          title: "信息登记"
        },
        {
          index: "4",
          title: "业务办理"
        }
      ],
      selectData: [],
      // 表单校验
      rules: {
        projectName: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            min: 3,
            max: 100,
            message: "长度在 3 到 100 个字符",
            trigger: "blur"
          }
        ],
        applyNumber: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validateNumberNoPonint,
            trigger: "blur"
          }
        ],
        applyPostCode: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validatePostCode,
            trigger: "blur"
          }
        ],
        bussVenueDistrictOid: [
          {
            required: true,
            message: "请选择业务管辖地",
            trigger: "input"
          }
        ],
        specificLocation: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        applyUserName: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],

        applyUserPhone: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validatePhone,
            trigger: "blur"
          }
        ],
        applyUserTel: [
          {
            validator: validateTel,
            message: "请输入正确的申请人/申请单位号码",
            trigger: "blur"
          }
        ],
        credentialType: [
          {
            required: true,
            message: "请选择证件类型",
            trigger: "change"
          }
        ],
        credentialNumber: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        legalPersonName: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        contactUserName: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            min: 3,
            max: 100,
            message: "长度在 3 到 100 个字符",
            trigger: "blur"
          }
        ],
        contactCredentialNumber: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validIDCard,
            trigger: "blur"
          }
        ],
        contactUserPhone: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validatePhone,
            trigger: "blur"
          }
        ],
        contactUserTel: [
          {
            validator: validateTel,
            message: "请输入正确的固定电话",
            trigger: "blur"
          }
        ],
        contactEmail: [
          {
            validator: validateEmails,
            trigger: "blur"
          }
        ],
        addresseeName: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        addresseePostCode: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validatePostCode,
            trigger: "blur"
          }
        ],
        addresseePhone: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validatePhone,
            trigger: "blur"
          }
        ],
        addresseeTel: [
          {
            validator: validateTel,
            message: "请输入正确的收件人电话",
            trigger: "blur"
          }
        ],
        addresseeAddress: [
          {
            required: true,
            message: "请选择收件人地址",
            trigger: "change"
          }
        ],
        addresseeDetailAddress: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ]
      },
      optionDate: {
        disabledDate(time) {
          return time.getTime() < Date.now(); // 选当前时间之后的时间
        }
      },
      materialCatalogAttaList: [],
      userInfoShow: false,
      chargeShow: false,
      applyCardNum: "",
      dialogVisible: false,
      bgForm: false,
      dlysForm: false,
      dlysViewForm: false,
      nzgsbgViewForm: false,
      activeName: "first",
      form: {
        carList: [
          {
            transportLicenseNo: "",
            carLicense: "",
            cpxhNum: "",
            carType: "",
            weightNum: "",
            fuel: "",
            axesNum: "",
            technicalLevel: ""
          }
        ],
        driverList: [
          {
            driverName: "",
            sex: "",
            age: "",
            eduLevel: "",
            linceseTime: "",
            carTypeUse: "",
            cyzgNum: ""
          }
        ],
        bglist: [{ serviceName: "", oldService: "", newService: "" }],
        czqkList: [
          {
            username: "",
            national: "",
            zjType: "",
            zjNumber: "",
            rjczNum: "",
            sjczNum: "",
            czrjTime: "",
            czfs: "",
            czbl: ""
          }
        ]
      },
      rulesNzgs: {
        legalPersonName: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        national: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        legalPersonPost: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        prodMethod: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        legalPersonZjType: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        legalPersonNumber: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        legalPersonMobile: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        legalPersonPhone: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validatePhone,
            trigger: "blur"
          }
        ],
        legalPersonAddress: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        legalPersonEmail: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validateEmails,
            trigger: "blur"
          }
        ]
      },
      allMaterialNames: "",
      rqbzMaterials: ""
    };
  },
  created() {
    this.getServiceSituationList();
    this.getServiceDetail();
    this.getDistrictTree();
    this.queryLoginInfo();
    this.queryPjSystem();
    this.getSelectCertificateType();
  },
  destroyed: function() {
    //在离开此页面的时候主动关闭socket
    this.socketApi.webSocketClose();
  },
  methods: {
    closeCaseCharge(obj) {
      if (obj.flag == "1" || obj.flag == "2") {
        this.chargeShow = false;
        this.chakanCharge = true;
        this.flagCc = false;
      }
      if (obj == "") {
        this.chargeShow = false;
      }
    },
    closeView() {
      this.viewchargeShow = false;
      this.chakanCharge = true;
      this.flagCc = false;
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    qlCaseCharge() {
      console.log(this.chargeFlag);
      /*  if(this.chargeFlag =="0"){

          } */
      this.chargeShow = true;
    },
    viewCharge() {
      /*  if(this.chargeFlag =="0"){

          } */
      this.viewchargeShow = true;
    },
    changeValue() {
      this.$refs.ruleForm.validateField("bussVenueDistrictOid");
    },
    next(index, count) {
      if (count > 0) {
        if (index == 1) {
          let bixuan = true;
          //判断是否必选
          for (let oneTitle of this.sxServiceOptionTitlesMust) {
            if (bixuan) {
              let chooseFlag = oneTitle.chooseFlag; //是否必选  1是  0否
              let titleShow = oneTitle.titleName; //显示名称
              let isHavingCorrelation = oneTitle.isHavingCorrelation; //是否隐藏
              if (
                chooseFlag == "0" &&
                (isHavingCorrelation == "0" || isHavingCorrelation == "2")
              ) {
                this.msgWarning(titleShow + "必选！");
                bixuan = false;
              }
            }
          }
          if (bixuan) {
            this.getMaterialList();
            this.i = index;
            this.show_0 = false;
            this.show_1 = true;
          }
        }
        if (index == 2) {
          this.getCheckbox(index);
        }
        if (index == 3) {
          this.catalogList = [];
          this.saveMaterialAtta(index);
        }
        if (index == 4) {
          this.attaList = [];
          this.show_upload = [];
          this.isNextDisable = true;
          this.checkPbpjInfo(index);
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
          this.attaList = [];
          this.catalogList = [];
          this.show_2 = true;
          this.show_3 = false;
          this.isNextDisable = false;
        }
        if (index == 3) {
          this.show_3 = true;
          this.show_4 = false;
        }
      }
    },
    /** 登录信息 */
    queryLoginInfo() {
      getloginUser().then(response => {
        this.loginUser = response.data;
      });
    },
    /** 初始化评价服务 */
    queryPjSystem() {
      getPjServiceSystem().then(response => {
        //1超级综窗柜台2其他
        this.systemType = response.data;
      });
    },
    handleChange(value) {
      console.log(value);
    },
    /** 选择情形 */
    selectChange(index, item) {
      this.num = index;
      this.situationName = item.title;
      this.checkList = [];
      this.checkBoxList = [];
      this.sxServiceMaterials = [];
      this.sxServiceOptionTitles = [];
      this.sxServiceOptionTitlesMust = [];
      this.sxServiceMaterialList = [];
      this.qlCaseTitleValList = [];
      if (item.oid == undefined) {
        this.getServiceSituationList();
        this.ifDis = false;
      } else {
        this.ifDis = true;
        getSituationOpinionList(item.oid).then(response => {
          //情形
          let optionTitle =
            response.data.sxServiceSituationOption.sxServiceOptionTitles;
          const situationOid =
            response.data.sxServiceSituationOption.situationOid;
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
              this.sxServiceOptionTitles.push(titleValues);
              if (item.fillFlag == "1") {
                titleValues.chooseFlag = "0";
                //this.sxServiceOptionTitlesMust.push(titleValues);
              }
              if (
                item.sxServiceOptionVals != null &&
                item.sxServiceOptionVals != ""
              ) {
                item.sxServiceOptionVals.forEach(data => {
                  if (data.isSelected == "1") {
                    //情形标题选项关系
                    let relations = {};
                    relations.situationOid = situationOid;
                    relations.titleOid = item.oid;
                    relations.valueOid = data.oid;
                    this.qlCaseTitleValList.push(relations);
                    //按钮被选中
                    this.checkList.push(data.oid);
                    //改变选择的状态为已选择
                    if (item.fillFlag == "1") {
                      titleValues.chooseFlag = "1";
                    }
                  }
                });
              }
              if (item.fillFlag == "1") {
                this.sxServiceOptionTitlesMust.push(titleValues);
              }
            });
          }
        });
      }
    },
    /** 勾选核验材料选项 */
    checkedBox(val) {
      this.radio = val;
    },
    /** 核验材料勾选 */
    getCheckbox(index) {
      if (this.radio) {
        this.initCase(index);
        //判断是否需要表单显示
        sxSerFormByServiceOid(this.serviceOid).then(response => {
          if (response.data) {
            this.formConfig_show = true;
            this.sxSerForm = response.data;
            this.sxSerForm.regOid = this.caseOid;
          }
        });
      } else {
        this.msgWarning("请勾选我已核验上述材料并确定齐全！");
        return false;
      }
    },
    /** 获取区划树 */
    getDistrictTree(districtOid) {
      queryDistrictSimpleTree(districtOid).then(response => {
        this.districtOptions = response.data;
      });
    },
    /** 获取证件类型 */
    getSelectCertificateType() {
      getCertificateType(this.cegisterType).then(response => {
        this.certificateTypeList = response.data;
      });
    },
    /** 改变证件类型 */
    changeType(item) {
      changeCredentialType(item).then(response => {
        this.cardType = response.data.code;
        let type = {};
        this.rules.credentialNumber.forEach((item, index) => {
          if (this.cardType == "SFZ") {
            type.validator = validIDCard;
            type.trigger = "blur";
            this.rules.credentialNumber.push(type);
          } else if (this.cardType == "XYDMZ") {
            type.validator = validUnifiedCredit;
            type.trigger = "blur";
            this.rules.credentialNumber.push(type);
          } else {
            if (index == 1) {
              this.rules.credentialNumber.splice(
                this.rules.credentialNumber.indexOf(item),
                1
              );
            }
          }
        });
      });
    },
    /** 事项情形获取 */
    getServiceSituationList() {
      //查询事项情形
      getAllSituationList(this.serviceOid).then(response => {
        let situations = response.data.sxServiceSituations;
        let optionTitles = response.data.sxServiceOptionTitles;
        //情形
        this.getSituationOpinion(situations);
        //标题和选项
        this.getSituationOpinionTitle(optionTitles);
        //材料
        this.getSxServiceMaterials();
      });
    },
    /** 填充选项 */
    getSituationOpinion(situations) {
      this.selectData = [
        {
          index: "0",
          title: "默认自定情形"
        }
      ];
      situations.forEach((item, key) => {
        let situation = {};
        situation.index = key + 1;
        situation.title = item.situationName;
        situation.oid = item.oid;
        this.selectData.push(situation);
      });
    },
    /** 填充标题和选项值 */
    getSituationOpinionTitle(optionTitles) {
      optionTitles.forEach(optionTitle => {
        let titleValues = {};
        titleValues.titleName = optionTitle.name;
        titleValues.titleOid = optionTitle.oid;
        titleValues.moreStatus = optionTitle.moreStatus;
        titleValues.fillFlag = optionTitle.fillFlag;
        titleValues.isHavingCorrelation = optionTitle.isHavingCorrelation;
        titleValues.sxServiceOptionVals = optionTitle.sxServiceOptionVals;
        this.sxServiceOptionTitles.push(titleValues);
        if (optionTitle.fillFlag == "1") {
          titleValues.chooseFlag = "0";
          this.sxServiceOptionTitlesMust.push(titleValues);
        }
      });
    },
    /** 获取事项 */
    getSxServiceMaterials() {
      getSituationMaterialListByOids(this.serviceOid, "").then(response => {
        if (response.data.length > 0) {
          response.data.forEach(item => {
            this.sxServiceMaterialList.push(item);
          });
        }
      });
    },
    /** 改变选项值 */
    changeTitle(titleOid, optionValOid) {
      for (let oneTitle of this.sxServiceOptionTitlesMust) {
        if (oneTitle.titleOid == titleOid) {
          oneTitle.chooseFlag = "1";
        }
      }

      this.sxServiceMaterialList = [];
      //情形标题选项关系
      let relations = {};
      relations.titleOid = titleOid;
      relations.valueOid = optionValOid;
      this.getCheckTitleValList(titleOid);
      this.qlCaseTitleValList.push(relations);
      //点击选项值 展示隐藏对应的标题
      getSxServiceOptionAllTitleValRelation(this.serviceOid, titleOid).then(
        response => {
          response.data.forEach(data => {
            //当前点击的选项下的有关联的标题设置显示
            if (data.valOid == optionValOid) {
              data.titleList.forEach(title => {
                this.sxServiceOptionTitles.forEach(optionTitle => {
                  if (optionTitle.titleOid == title.oid) {
                    optionTitle.isHavingCorrelation = 2;
                    this.checkList.push(optionValOid);
                  }
                });
              });
            } else {
              //设置除了点击的标题选项之外的选项有关联的标题设置隐藏
              data.titleList.forEach(title => {
                this.sxServiceOptionTitles.forEach(optionTitle => {
                  if (optionTitle.titleOid == title.oid) {
                    optionTitle.isHavingCorrelation = 1;
                    this.getCheckTitleValList(title.oid);
                    this.getCheckValList(data.valOid);
                  }
                });
              });
            }
          });
        }
      );
    },
    // getCheckTitleValListBox(titleOid, relationsP, hbType) {
    //
    //   //合并
    //   if (this.qlCaseTitleValList.length > 0) {
    //     this.qlCaseTitleValList.forEach(itemGet => {
    //       let item = itemGet;
    //       this.getCheckTitleValList(itemGet.titleOid);
    //       if (item.titleOid == titleOid) {
    //         let relations = {};
    //         relations.titleOid = item.titleOid;
    //         if (hbType == 1) {
    //           relations.valueOid = item.valueOid + "," + relationsP.valueOid;
    //           this.qlCaseTitleValList.push(relations);
    //         } else {
    //           relations.valueOid = item.valueOid.replace("," + relationsP.valueOid, '');
    //           relations.valueOid = relations.valueOid.replace(relationsP.valueOid, '');
    //           if (relations.valueOid != '') {
    //             this.qlCaseTitleValList.push(relations);
    //           } else {
    //             this.getCheckTitleValList(relations.titleOid);
    //           }

    //         }
    //       }
    //     })
    //   } else {
    //     this.qlCaseTitleValList.push(relationsP);
    //   }
    // },
    changeTitleBox(item, val) {
      //每个多选项都单独保存
      if (this.checkBoxList.indexOf(val) > -1) {
        //如果多选里面包含就添加，否则就删除
        let relations = {};
        relations.titleOid = item.titleOid;
        relations.valueOid = val;
        this.qlCaseTitleValList.push(relations);
      } else {
        this.qlCaseTitleValList.forEach((relations, j) => {
          if (relations.valueOid == val) {
            this.qlCaseTitleValList.splice(j, 1);
          }
        });
      }
      //判断是否是必须选择
      for (let oneTitle of this.sxServiceOptionTitlesMust) {
        if (oneTitle.titleOid == item.titleOid) {
          if (oneTitle.chooseFlag.indexOf(item.titleOid + val) != -1) {
            oneTitle.chooseFlag = oneTitle.chooseFlag.replace(
              "," + item.titleOid + val,
              ""
            );
          } else {
            oneTitle.chooseFlag =
              oneTitle.chooseFlag + "," + item.titleOid + val;
          }
        }
      }
      //console.log(JSON.stringify(this.qlCaseTitleValList))
    },
    getCheckTitleValList(titleOid) {
      //去除相同标题下的选项
      if (this.qlCaseTitleValList.length > 0) {
        this.qlCaseTitleValList.forEach(item => {
          if (item.titleOid == titleOid) {
            this.qlCaseTitleValList.splice(
              this.qlCaseTitleValList.indexOf(item),
              1
            );
          }
        });
      }
    },
    getCheckValList(valOid) {
      //去除相同标题下的选项
      if (this.checkList.length > 0) {
        this.checkList.forEach(oid => {
          if (valOid == oid) {
            this.checkList.splice(this.qlCaseTitleValList.indexOf(oid), 1);
          }
        });
      }
    },
    /** 点击选项值 选项有精细化材料展示精细化材料 */
    getMaterialList() {
      let allCheckItem = [];
      allCheckItem = this.checkBoxList.concat(this.checkList);
      //根据被选中的选项获取关联材料
      getSituationMaterialListByOids(this.serviceOid, allCheckItem).then(
        response => {
          if (response.data.length > 0) {
            this.sxServiceMaterialList = response.data;
          }
          this.showGuidence();
        }
      );
    },
    //超级综窗柜台双屏显示办事指南信息
    showGuidence() {
      let url = window.location.origin;
      let pushUrl = url + "/serviceHall/guidence.html";
      let pushForm = {};
      let data = {};
      let sxService = {};
      data.sxServiceMaterialList = this.sxServiceMaterialList;
      sxService.serviceName = this.sxService.serviceName;
      sxService.chargeFlagName = this.sxService.chargeFlagName;
      sxService.chargeFlag = this.sxService.chargeFlag;
      sxService.zxDhText = this.sxService.zxDhText;
      sxService.tsDhText = this.sxService.tsDhText;
      sxService.promiseLimit = this.serviceForm.sxServiceExtend.promiseLimit;
      sxService.legalLimit = this.serviceForm.sxServiceExtend.legalLimit;
      sxService.handleFormName = this.sxService.handleFormName;
      data.sxService = sxService;
      pushForm.pushUrl = pushUrl + "?jsonObject=" + JSON.stringify(data);
      window.open(pushUrl + "?jsonObject=" + JSON.stringify(data));
      /*    showCallMessage(JSON.stringify(pushForm)).then(response => {
        console.log(response.data);
      })*/
    },
    //材料附件
    getAttaList(caseOid) {
      this.sxServiceMaterialAttaList = [];
      getSxServiceMaterialAttaList(caseOid).then(response => {
        if (response.data != "") {
          this.sxServiceMaterialAttaList = response.data;
          if (
            this.sxServiceMaterialList &&
            this.sxServiceMaterialList.length > 0
          ) {
            this.sxServiceMaterialList.forEach((item, index) => {
              this.sxServiceMaterialAttaList.forEach((ite, i) => {
                if (item.materialOid == ite.materialOid) {
                  ite.sxMaterials = item;
                }
              });
            });
          }
        }
      });
    },
    /** 获取事项详细信息 */
    getServiceDetail() {
      getSxService(this.serviceOid).then(response => {
        console.log(response.data);
        this.sxService = response.data.sxService;
        this.serviceForm.serviceName = response.data.sxService.serviceName;
        this.serviceForm.serviceTypeName =
          response.data.sxService.serviceTypeName;
        this.serviceForm.organName = response.data.sxService.organName;
        this.serviceForm.sxServiceLocations = response.data.sxServiceLocations;
        this.serviceForm.zxDhText = response.data.sxService.zxDhText;
        this.serviceForm.tsDhText = response.data.sxService.tsDhText;
        this.serviceForm.sxServiceExtend = response.data.sxServiceExtend;
        this.serviceForm.sxAcceptConditions = response.data.sxAcceptConditions;
        this.serviceForm.sxServiceLinks = response.data.sxServiceLinks;
        this.serviceForm.sxServiceQuestions = response.data.sxServiceQuestions;
        this.chargeFlag = response.data.sxService.chargeFlag;
      });
    },
    //验证表单以及办件评价信息
    checkPbpjInfo(index) {
      this.$refs["ruleForm"].validate(valid => {
        if (valid) {
          //查询是否已保存 返回id用于更新
          if (this.caseOid != "") {
            getQlCaseByOid(this.caseOid).then(response => {
              this.id = response.data.id;
              this.caseOid = response.data.caseOid;
            });
          }
          //验证表单是否必填
          if (this.sxSerForm) {
            if (this.sxSerForm.isFormFlag == 1) {
              //必填
              if (!this.caseForm.formDataId) {
                this.$message.error("表单未填写,请填写表单！");
                return false;
              }
            }
          }
          if (this.systemType == "1") {
            this.confirmScreenInteraction(index);
          } else {
            this.saveApplyCaseForm(index);
          }
        } else {
          this.isNextDisable = false;
        }
      });
    },

    /** 办件信息下一步保存 */
    initCase(index) {
      this.ruleForm.id = this.id;
      this.ruleForm.caseOid = this.caseOid;
      this.ruleForm.applyOid = this.applyOid;
      this.ruleForm.extOid = this.extOid;
      this.ruleForm.applyUserType = this.cegisterType;
      this.ruleForm.serviceOid = this.serviceOid;
      this.ruleForm.caseNumber = this.caseNumber;
      this.ruleForm.sxServiceMaterialList = this.sxServiceMaterialList;
      this.ruleForm.qlCaseTitleValList = this.qlCaseTitleValList;
      save(this.ruleForm).then(response => {
        if (response.data != "") {
          this.caseOid = response.data.caseOid;
          this.caseNumber = response.data.caseNumber;
          this.serviceTypeName = response.data.serviceTypeName;
          this.caseMaterialOids = response.data.caseMaterialOids;
          this.createDate = response.data.createDate;
          this.msgSuccess("保存办件成功！");
          //验证是否是承诺清单和申请人是否失信
          this.checkServiceAndUser();
          //加载材料附件
          this.getAttaList(response.data.caseOid);
          this.i = index;
          this.show_1 = false;
          this.show_2 = true;
        } else {
          this.msgError("保存办件失败！");
          return false;
        }
      });
    },
    /** 办件信息下一步保存 */
    saveApplyCaseForm(index) {
      this.$refs["ruleForm"].validate(valid => {
        if (valid) {
          this.isDisable = true;
          setTimeout(() => {
            this.ruleForm.caseOid = this.caseOid;
            this.ruleForm.caseStatus = "0";
            this.ruleForm.applyUserType = this.cegisterType;
            this.ruleForm.serviceOid = this.serviceOid;
            this.ruleForm.caseNumber = this.caseNumber;
            save(this.ruleForm).then(response => {
              if (response.data != "") {
                this.saveCaseForm();
                this.form.caseOid = this.caseOid;
                this.submitForm(); //保存道路运输
                this.msgSuccess("申请信息保存成功！");
                if (index != "") {
                  this.i = index;
                  this.show_3 = false;
                  this.show_4 = true;
                } else {
                  //关闭页面调用列表页面
                  this.$emit("case-close");
                  return false;
                }
              } else {
                this.msgError("申请信息保存失败！");
                this.isDisable = false;
                this.isNextDisable = false;
                return false;
              }
            });
          }, 500);
        }
      });
    },
    /** 综窗保存材料出库信息 */
    saveMaterialOut(materialOids) {
      let outMaterial = {};
      outMaterial.regOid = this.caseOid;
      outMaterial.caseNumber = this.caseNumber;
      outMaterial.applyUserName = this.ruleForm.applyUserName;
      outMaterial.serviceTypeName = this.serviceTypeName;
      outMaterial.cardNumber = this.ruleForm.credentialNumber;
      outMaterial.materialOids = materialOids;
      saveOut(outMaterial).then(response => {
        if (response.data == "") {
          this.msgError("材料出库信息保存失败！");
          return false;
        }
      });
    },
    /** 保存材料附件信息 */
    saveMaterialAtta(index) {
      let dataForm = {};
      let flag = false;
      dataForm.materialAttaList = this.materialAttaList;
      dataForm.collectionTypeList = this.materialCollectionTypeList;
      dataForm.materialCatalogAttaList = this.materialCatalogAttaList;
      dataForm.elemLicenseList = this.elemLicenseList;
      /*this.materialAttaList.forEach(atta => {
          this.catalogList.push(this.catalogCheckList[atta.attaOid]);
        });
        this.catalogList.forEach((cata, num) => {
          this.catalogList.forEach((log, count) => {
            if (cata && cata != null && cata != 'undefined') {
              if (num != count) {
                if (cata == log) {
                  flag = true;
                }
              }
            }
          });
        });*/
      if (
        this.materialCatalogAttaList &&
        this.materialCatalogAttaList.length > 0
      ) {
        this.materialCatalogAttaList.forEach((fir, i) => {
          dataForm.materialCatalogAttaList.forEach(second => {
            //如果办件材料一样附件不一样看选择的智审目录是否相同
            if (
              fir.caseMaterialOid == second.caseMaterialOid &&
              fir.attaOid != second.attaOid
            ) {
              if (fir.materialCatalogOid == second.materialCatalogOid) {
                flag = true;
              }
            }
          });
        });
      }
      if (flag) {
        this.msgError("上传文件请选择不同的目录材料！");
        return false;
      } else {
        let noticeFlag = false; //标识是否允许缺省材料验证
        //是否存在 必须 材料,未收取
        for (let i = 0; i < this.sxServiceMaterialAttaList.length; i++) {
          let mustTypeFlag = false;
          //必要材料收取附件
          let mustAttaFlag = false;
          //是否选择上传附件
          let uploadFlag = false;
          let materialName = "";
          const cmMaterial = this.sxServiceMaterialAttaList[i];
          this.allMaterialNames =
            this.allMaterialNames +
            (i + 1) +
            "、" +
            cmMaterial.materialName +
            ";";
          //必须，看是否有收取
          if (cmMaterial.sxMaterials.materialMustFlag == 1) {
            for (let j = 0; j < this.materialCollectionTypeList.length; j++) {
              const type = this.materialCollectionTypeList[j];
              if (type.caseMaterialOid == cmMaterial.caseMaterialOid) {
                if (type.collectionType) {
                  mustTypeFlag = true;
                  //已经点击收取类型，后遍历收取的材料
                  //看是否为纸质，则无材料
                  if (type.collectionType == 1) {
                    //无材料
                    mustAttaFlag = true;
                  } else if (
                    type.collectionType == 2 ||
                    type.collectionType == 3
                  ) {
                    if (this.materialAttaList.length > 0) {
                      for (let k = 0; k < this.materialAttaList.length; k++) {
                        const atta = this.materialAttaList[k];
                        if (
                          atta.caseMaterialOid == cmMaterial.caseMaterialOid
                        ) {
                          mustAttaFlag = true;
                          break;
                        }
                      }
                    }
                  } else if (type.collectionType == 4) {
                    //容缺后补
                    mustAttaFlag = true;
                  } else if (type.collectionType == 5) {
                    //电子证照
                    if (this.elemLicenseList.length > 0) {
                      for (let k = 0; k < this.elemLicenseList.length; k++) {
                        const atta = this.elemLicenseList[k];
                        if (atta.materialOid == cmMaterial.caseMaterialOid) {
                          mustAttaFlag = true;
                          break;
                        }
                      }
                    }
                  }
                } else {
                  mustTypeFlag = false;
                }
              }
            }
          } else {
            mustTypeFlag = true;
            mustAttaFlag = true;
          }
          //选择上传附件 扫描 证照判断是否有附件上传
          if (this.materialCollectionTypeList.length > 0) {
            for (let k = 0; k < this.materialCollectionTypeList.length; k++) {
              const coll = this.materialCollectionTypeList[k];
              if (
                coll.collectionType != "1" &&
                coll.collectionType != "4" &&
                coll.collectionType != null
              ) {
                if (coll.attaOid == null || coll.attaOid == "") {
                  uploadFlag = true;
                  materialName = coll.materialName;
                  break;
                }
              }
            }
          }
          //如果为true，给出提示
          if (uploadFlag) {
            this.msgError("[" + materialName + "]必须上传材料！");
            return false;
          }
          //如果不为true，给出提示
          if (mustTypeFlag == false) {
            if (!this.checkFlag) {
              this.msgError(
                "[" + cmMaterial.materialName + "]必须材料的收取方式未选择！"
              );
              return false;
            } else {
              noticeFlag = true;
            }
          }
          if (mustAttaFlag == false) {
            if (!this.checkFlag) {
              this.msgError(
                "[" + cmMaterial.materialName + "]必须材料的未收取！"
              );
              return false;
            } else {
              noticeFlag = true; //允许缺省材料
            }
          }
        }
        //如果有容缺后补材料则不验证承诺告知
        if (this.isRqslFlag == true) {
          //容缺受理
          this.saveMaterialInfo(index, dataForm);
        } else {
          if (this.checkFlag == true && noticeFlag == true) {
            //判断是否勾选承诺告知
            if (this.radioNotice) {
              //保存材料和补齐补正
              if (!this.bqbzCaseForm.dueDate) {
                this.$message.error("请选择承诺补正时间!");
                return false;
              }
              this.saveMaterialInfo(index, dataForm);
              //this.saveCaseCorrection();
            } else {
              this.$message.error("当前支持承诺告知方式提交材料,请勾选");
              return false;
            }
          } else if (this.radioNotice) {
            //保存材料和补齐补正
            if (!this.bqbzCaseForm.dueDate) {
              this.$message.error("请选择承诺补正时间!");
              return false;
            }
            this.saveMaterialInfo(index, dataForm);
            //this.saveCaseCorrection();
          } else {
            this.saveMaterialInfo(index, dataForm);
          }
        }
      }
    },
    /** 办件受理 */
    caseAccpet() {
      if (this.caseOid != "") {
        let formData = new FormData();
        formData.append("caseOid", this.caseOid);
        formData.append("userOid", this.loginUser.userOid);
        formData.append("userName", this.loginUser.userName);
        formData.append("finalOpinion", this.ruleForm.acceptradio);
        formData.append("finalOpinionDesc", this.ruleForm.finalOpinionDesc);
        if (this.ruleForm.acceptradio == 2) {
          if (!this.ruleForm.finalOpinionDesc) {
            this.$message.error("意见说明不能为空！");
            return false;
          }
        }
        if (this.isRqslFlag == true) {
          formData.append("rqbzDueDate", this.ruleForm.bqbzDueDate);
          if (!this.ruleForm.bqbzDueDate) {
            this.$message.error("承诺补正时间不能为空!");
            return false;
          }
        }
        saveCaseAccpet(formData).then(response => {
          if (response.data != "") {
            this.linkResultOid = response.data.linkResultOid;
            let noticeMes = {};
            if (this.cegisterType == 0) {
              noticeMes.applyUserName = this.ruleForm.applyUserName;
              noticeMes.applyCompany = "";
            } else {
              noticeMes.applyCompany = this.ruleForm.applyUserName;
              noticeMes.applyUserName = "";
            }
            noticeMes.phone = this.ruleForm.applyUserPhone;
            noticeMes.serviceName = this.serviceForm.serviceName;
            noticeMes.zxPhone = this.serviceForm.zxDhText;
            noticeMes.caseNumber = this.caseNumber;
            noticeMes.slTime = response.data.accpectTime;
            noticeMes.jbrName = response.data.personName;

            if (this.ruleForm.acceptradio == 2) {
              noticeMes.resonDesc = this.ruleForm.finalOpinionDesc;
              noticeMes.materials = this.allMaterialNames;
              noticeMes.isRqslFlag = "";
              noticeMes.fileName = "bysltzs.doc";
              noticeMes.isSl = "false";
              this.msgSuccess("办件不予受理保存成功！");
              this.$emit("case-close", noticeMes); //不予受理通知书
            } else {
              this.msgSuccess("办件进入受理成功！");
              //保存补齐补正信息
              if (this.bqbzCaseForm.dueDate && this.radioNotice) {
                this.saveCaseCorrection();
              }
              if (this.collectionArray.indexOf("1") > -1) {
                //只有纸质的材料进行出库信息保存
                let materialOids = "";
                this.materialCollectionTypeList.forEach((ite, i) => {
                  if (ite.collectionType == 1) {
                    materialOids += ite.caseMaterialOid + ";";
                  }
                });
                this.saveMaterialOut(materialOids);
              }
              if (this.chargeFlag == "1") {
                this.chargeShow = true;
              }
              if (this.isRqslFlag == true) {
                //打印容缺承诺书
                noticeMes.materials = this.rqbzMaterials;
                noticeMes.fileName = "rqsltzs.doc"; //通知书名称必须
                noticeMes.isRqslFlag = "true";
                this.$emit("case-close", noticeMes);
              } else {
                //受理通知书
                noticeMes.materials = this.allMaterialNames;
                noticeMes.fileName = "sltzs.doc"; //通知书名称必须
                noticeMes.isRqslFlag = "";
                noticeMes.isSl = "true";
                this.$emit("case-close", noticeMes);
              }
            }
          }
        });
      } else {
        this.msgError("办件信息未保存，请先保存！");
        return false;
      }
    },
    saveMaterialInfo(index, dataForm) {
      saveQlCaseMaterialAtta(dataForm).then(response => {
        if (response.code == 200) {
          /*this.msgSuccess("保存材料附件成功！");
            this.i = index;
            this.show_3 = false;
            this.show_4 = true;*/
          var letList = response.data;
          var size = letList.length;
          if (letList && size > 0) {
            /*材料预审start*/
            letList.forEach((item, num) => {
              let caseOid = this.caseOid;
              let caseMaterialOid = item.caseMaterialOid;
              let caseFileRecOid = item.materialAttaOid;
              let cataOid = item.materialCatalogOid;
              intelligentPretrialmaterialPrePrial(
                caseOid,
                caseFileRecOid,
                caseMaterialOid,
                cataOid
              ).then(response => {
                if (num + 1 == letList.length) {
                  this.msgSuccess("保存材料附件成功！");
                  this.i = index;
                  this.loading = false;
                  this.show_2 = false;
                  this.show_3 = true;
                }
              });
              /*材料预审end*/
            });
          } else {
            this.msgSuccess("保存材料附件成功！");
            this.i = index;
            this.loading = false;
            this.show_2 = false;
            this.show_3 = true;
          }
        } else {
          this.msgError("保存材料附件失败！");
          return false;
        }
      });
    },
    /** 勾选告知承诺清单 */
    checkedBoxNotice(val) {},
    //验证当前事项是否是告知承诺清单和当前申请人是否在失信名单中
    checkServiceAndUser() {
      getInformPromiseByServiceOid(this.serviceOid).then(response => {
        if (response.data) {
          getDishonestPerson(
            this.ruleForm.applyUserName,
            this.ruleForm.credentialNumber
          ).then(response => {
            if (response.data) {
              this.checkFlag = false;
              this.isSxPersonFlg = true; //失信人
            } else {
              this.checkFlag = true;
            }
          });
        } else {
          this.checkFlag = false;
          getDishonestPerson(
            this.ruleForm.applyUserName,
            this.ruleForm.credentialNumber
          ).then(response => {
            if (response.data) {
              //失信人
              this.isSxPersonFlg = true; //失信人
            } else {
              this.isSxPersonFlg = false;
            }
          });
        }
      });
    },
    //保存补齐补正信息
    saveCaseCorrection() {
      this.bqbzCaseForm.caseOid = this.caseOid;
      this.bqbzCaseForm.caseNumber = this.caseNumber;
      this.bqbzCaseForm.applyUserName = this.ruleForm.applyUserName;
      this.bqbzCaseForm.applyProjectName = this.ruleForm.projectName;
      this.bqbzCaseForm.userPhone = this.ruleForm.applyUserPhone;
      this.bqbzCaseForm.bzType = "0";
      saveOrUpdateGzBqbz(this.bqbzCaseForm).then(response => {
        if (response.code == 200) {
        }
      });
    },
    //超级综窗柜台双屏互动信息确认
    confirmScreenInteraction(index) {
      let url = window.location.origin;
      let pushForm = {};
      let data = {};
      let content = {};
      let pushUrl = url + "/serviceHall/confirmation.html";
      content.projectName = this.ruleForm.projectName;
      content.applyUserType = this.ruleForm.applyUserType;
      if (this.ruleForm.applyUserType == "0") {
        content.applyUserType = "个人";
      } else {
        content.applyUserType = "法人";
      }
      content.applyUserName = this.ruleForm.applyUserName;
      content.credentialNumber = this.ruleForm.credentialNumber;
      content.applyPostCode = this.ruleForm.applyPostCode;
      content.applyUserAddress = this.ruleForm.applyUserAddress;
      content.applyUserPhone = this.ruleForm.applyUserPhone;
      //getCertificateTypeByOid(this.ruleForm.credentialType).then(response => {
      content.credentialName = "身份证";
      // });
      data.content = content;
      let jsonObject = JSON.stringify(data);
      pushForm.pushUrl = pushUrl + "?jsonObject=" + jsonObject;
      this.$confirm("你确定要进行办件信息确认吗？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          window.open(pushUrl + "?jsonObject=" + jsonObject);
          /*      showCallMessage(JSON.stringify(pushForm)).then(response => {
          console.log(response.data);
        });*/
        })
        .then(function() {
          //保存办件
          this.saveApplyCaseForm(index);
        })
        .catch(action => {
          if (action === "cancel") {
            //保存办件
            this.saveApplyCaseForm(index);
          }
        });
    },
    //办件平板评价
    pushPjCaseService() {
      if (this.systemType == "1") {
        this.pushInteractionPj();
      } else {
        this.caseAccpet();
      }
    },
    //超级综窗柜台双屏互动办件评价
    pushInteractionPj() {
      let url = window.location.origin;
      let pushUrl = url + "/serviceHall/evaluation.html";
      let pushForm = {};
      let data = {};
      data.projectName = this.ruleForm.projectName;
      data.caseNumber = this.caseNumber;
      data.createDate = this.createDate
        ? this.createDate.substring(0, 10)
        : this.createDate;
      pushForm.pushUrl = pushUrl + "?jsonObject=" + JSON.stringify(data);
      this.$confirm("你确定要进行办件评价吗？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          showCallMessage(JSON.stringify(pushForm)).then(response => {
            if (response.data) {
              this.msgWarning("办件评价中...");
            }
          });
        })
        .then(() => {
          //保存办件
          this.caseAccpet();
        })
        .catch(action => {
          if (action === "cancel") {
            this.caseAccpet();
          }
        });
    },
    caseSign() {},
    /** 送达方式 */
    changeDeliveryWay(val) {
      this.address_show = val === "1" ? true : false;
    },
    /** 是否代理人 */
    changeProxyFlag(val) {
      this.proxy_show = val === "1" ? true : false;
    },
    /** 材料形式 */
    chooseCollectionType(val, index, item) {
      if (val == "2") {
        this.show_upload[index] = true;
        this.show_scan[index] = false;
        this.show_elem[index] = false;
      } else if (val == "3") {
        this.show_scan[index] = true;
        this.show_upload[index] = false;
        this.show_elem[index] = false;
      } else if (val == "5") {
        this.show_elem[index] = true;
        this.show_scan[index] = false;
        this.show_upload[index] = false;
      } else {
        this.show_upload[index] = false;
        this.show_elem[index] = false;
        this.show_scan[index] = false;
      }
      this.radio1.push(item.caseMaterialOid);
      this.collectionArray[index] = val;
      if (this.collectionArray.indexOf("4") > -1) {
        this.isCheckedDisable = true;
        this.isRqslFlag = true;
        this.rqbzMaterials = this.rqbzMaterials + item.materialName + ";"; //打印容缺补正通知书用
        this.radioNotice = "";
      } else {
        this.isCheckedDisable = false;
        this.isRqslFlag = false;
      }
      //变量存入材料和上传方式
      let collection = {};
      collection.caseMaterialOid = item.caseMaterialOid;
      collection.collectionType = val;
      collection.collectNumber = item.sxMaterials.paperNumber;
      collection.materialName = item.materialName;
      if (this.materialCollectionTypeList.length > 0) {
        this.materialCollectionTypeList.forEach(data => {
          if (data.caseMaterialOid == item.caseMaterialOid) {
            this.materialCollectionTypeList.splice(
              this.materialCollectionTypeList.indexOf(data),
              1
            );
          }
        });
      }
      this.materialCollectionTypeList.push(collection);
      //当收取方式为纸质、附件、扫描、荣却后补的时候清空证照
      if (val == "1" || val == "2" || val == "3" || val == "4") {
        this.elemLicenseList.forEach((ite, i) => {
          if (ite.materialOid == item.caseMaterialOid) {
            this.elemLicenseList.splice(i, 1);
          }
        });
        //删除证照
        this.$set(this.attaList, index, []);
      }
      //当为纸质、证照、荣却后补的时候清空附件
      //if (val == '1' || val == '4' || val == '5') {
      this.sxServiceMaterialAttaList.forEach((itemAtta, ak) => {
        if (itemAtta.caseMaterialOid == item.caseMaterialOid) {
          itemAtta.attaList = [];
        }
      });
      //删除材料附件关系
      this.materialAttaList.forEach((attItem, j) => {
        if (attItem.caseMaterialOid == item.caseMaterialOid) {
          //删除材料智审关联
          if (this.materialCatalogAttaList != "") {
            this.materialCatalogAttaList.forEach((catalog, z) => {
              if (catalog.attaOid == attItem.attaOid) {
                this.materialCatalogAttaList.splice(z, 1);
              }
            });
          }
          //删除附件关系
          this.materialAttaList.splice(j, 1);
        }
      });
      //}
    },
    /** 附件目录材料 */
    chooseCatalog(caseMaterialOid, attaOid, materialCatalogOid) {
      let catalog = {};
      catalog.attaOid = attaOid;
      catalog.materialCatalogOid = materialCatalogOid;
      catalog.caseMaterialOid = caseMaterialOid;
      if (
        this.materialCatalogAttaList &&
        this.materialCatalogAttaList.length > 0
      ) {
        this.materialCatalogAttaList.forEach(catalog => {
          if (catalog.attaOid == attaOid) {
            this.materialCatalogAttaList.splice(
              this.materialCatalogAttaList.indexOf(catalog),
              1
            );
          }
        });
      }
      this.materialCatalogAttaList.push(catalog);
    },
    /** 加载送达信息 */
    getApplyInfo() {
      this.ruleForm.addresseeName = this.ruleForm.applyUserName;
      this.ruleForm.addresseePostCode = this.ruleForm.applyPostCode;
      this.ruleForm.addresseePhone = this.ruleForm.applyUserPhone;
      this.ruleForm.addresseeTel = this.ruleForm.applyUserTel;
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 隐藏右侧 */
    hiddenRightSideBar() {
      this.rightShow = false;
    },
    /** 显示右侧 */
    showRightSideBar() {
      this.rightShow = true;
    },
    /** 表单重置 */
    reset() {
      Object.assign(this.queryForm, this.$options.data().queryForm);
      this.resetForm("queryForm");
    },
    /** 取消按钮 */
    cancel() {
      this.reset();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    pushMaterialOid(caseMaterialOid) {
      this.caseMaterialOid = caseMaterialOid;
    },
    /** 失败后返回 */
    uploadError(resp) {
      this.msgError("文件上传失败");
    },
    /** 上传附件 */
    uploadFiles(file) {
      let formData = new FormData();
      formData.append("files", file.file);
      uploadCaseMaterialFile(formData).then(response => {
        if (response.data != "") {
          response.data.forEach(data => {
            let list = {};
            file.data.attaList.push(data);
            list.caseMaterialOid = this.caseMaterialOid;
            list.attaOid = data.attaOid;
            this.materialAttaList.push(list);
            if (this.materialCollectionTypeList.length > 0) {
              this.materialCollectionTypeList.forEach(collection => {
                if (collection.caseMaterialOid == this.caseMaterialOid) {
                  collection.attaOid = data.attaOid;
                }
              });
            }
          });
        } else {
          this.$message.error("不允许上传空文件!");
        }
      });
    },
    /** 上传附件请求操作 */
    beforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 100;
      if (!isRightSize) {
        this.$message.error("文件大小超过 100MB");
      }
      this.fileList.push(file);
      return isRightSize;
    },
    base64ToFile(urlData, fileName) {
      let arr = urlData.split(",");
      // let mime = arr[0].match(/:(.*?);/)[1];
      let bytes = atob(arr[0]); // 解码base64
      let n = bytes.length;
      let ia = new Uint8Array(n);
      while (n--) {
        ia[n] = bytes.charCodeAt(n);
      }
      return new File([ia], fileName, {
        type: "image/jpeg"
      });
    },
    // 接收socket回调函数返回数据的方法
    getConfigResult(data, index) {
      if (data.status == 0) {
        //读卡
        if (data.device == "IdCard") {
          //申请人模块--0
          //联系人模块--1
          if (data.type == "0") {
            this.ruleForm.credentialNumber = data.content.CardNum;
            this.ruleForm.applyUserName = data.content.CardBelongName;
            this.ruleForm.credentialType = "身份证";
          }
          if (data.type == "1") {
            this.ruleForm.contactCredentialNumber = data.content.CardNum;
            this.ruleForm.contactUserName = data.content.CardBelongName;
          }
        }

        //扫描
        if (data.device == "HighCamera") {
          if (data.type) {
            let materialOid = data.type;
            if (data.content.Cameras64) {
              let base64s = data.content.Cameras64.split(",");
              for (let i = 0; i < base64s.length; i++) {
                if (!base64s[i]) {
                  continue;
                }
                let file = this.base64ToFile(
                  base64s[i],
                  "scanPicture" + i + ".jpg"
                );
                let formD = new FormData();
                formD.append("files", file);
                uploadCaseMaterialFile(formD).then(response => {
                  if (response.data != "") {
                    response.data.forEach(attaInfo => {
                      this.sxServiceMaterialAttaList[index].attaList.push(
                        attaInfo
                      );
                      let list = {};
                      list.caseMaterialOid = materialOid;
                      list.attaOid = attaInfo.attaOid;
                      this.materialAttaList.push(list);
                      //塞入上传附件
                      if (this.materialCollectionTypeList.length > 0) {
                        this.materialCollectionTypeList.forEach(collection => {
                          if (collection.caseMaterialOid == materialOid) {
                            collection.attaOid = attaInfo.attaOid;
                          }
                        });
                      }
                    });
                  } else {
                    this.$message.error("不允许上传空文件!");
                  }
                });
              }
            }
          }
        }
      }
      if (data.status == 1) {
        this.$message.error(data.message);
      }
    },
    //初始化socket发生错误回调
    socketError() {
      this.$message.error("请检查设备或连接是否正常");
    },
    scanCard(scanType) {
      //申请人模块--0
      //联系人模块--1
      //Device：设备类型、
      let info = '{"device":"IdCard", "type":"' + scanType + '"}';
      //建立socket连接
      this.socketApi.initWebSocket(this.socketError);
      this.socketApi.sendSock(info, this.getConfigResult);
    },
    scanPicture(caseMaterialOid, index) {
      //caseMaterialOid--所属材料主键
      //index--所属操作行索引
      //Device：设备类型、
      let info = '{"device":"HighCamera", "type":"' + caseMaterialOid + '"}';
      //建立socket连接
      this.socketApi.initWebSocket(this.socketError);
      this.socketApi.sendSock(info, this.getConfigResult, index);
    },
    getElecLicenInfo(materialOid, caseMaterialOid, index) {
      let userName = "";
      let idCard = "";
      idCard = this.ruleForm.credentialNumber;
      userName = this.ruleForm.applyUserName;
      /*if (this.cegisterType == "0"){
          idCard=this.ruleForm.credentialNumber;
          userName= this.ruleForm.applyUserName ;
        }
        if (this.cegisterType == "1"){
          idCard=this.ruleForm.contactCredentialNumber
          userName=this.ruleForm.contactUserName
        }*/
      if (userName && idCard) {
        queryElecLicenseByDirCode(materialOid, userName, idCard, "").then(
          response => {
            if (response.data) {
              let res = {};
              let eleArr = [];
              res.originName = response.data.licenseNumber;
              res.attaOid = response.data.elecLicenOid;
              res.materialOid = caseMaterialOid;
              eleArr[0] = res;
              this.$set(this.attaList, index, eleArr);
              //判断elemLicenseList是否已经存在办件的证照信息
              if (this.elemLicenseList) {
                this.elemLicenseList.forEach((ite, i) => {
                  if (ite.materialOid == caseMaterialOid) {
                    //移除原来的重新赋值
                    this.elemLicenseList.splice(i, 1);
                  }
                });
              }
              this.elemLicenseList.push(res);
              //塞入上传附件
              if (this.materialCollectionTypeList.length > 0) {
                this.materialCollectionTypeList.forEach(collection => {
                  if (collection.caseMaterialOid == caseMaterialOid) {
                    collection.attaOid = response.data.elecLicenOid;
                  }
                });
              }
            } else {
              this.$message.error("暂无证照,请检查证照相关配置！");
              return;
            }
          }
        );
      } else {
        this.$message.error("申请人/申请单位和证件号不能为空！");
        return;
      }
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
    //材料查看
    viewMaterialAddr(attaOid) {
      if (attaOid) {
        this.fileSxAttaOid = attaOid;
        this.showSxFile = true;
      } else {
        this.$message.warning("暂无材料查看！");
      }
    },
    deleteAtta(list, atta) {
      this.sxServiceMaterialAttaList.forEach(item => {
        if (item.attaList.length > 0) {
          item.attaList.forEach((ite, i) => {
            if (ite.attaOid == list.attaOid) {
              item.attaList.splice(i, 1);
            }
          });
        }
      });
      //删除材料附件关系
      this.materialAttaList.forEach((attItem, j) => {
        if (attItem.attaOid == list.attaOid) {
          this.materialAttaList.splice(j, 1);
        }
      });
      //删除材料智审关联
      if (this.materialCatalogAttaList != "") {
        this.materialCatalogAttaList.forEach((catalog, z) => {
          if (catalog.attaOid == list.attaOid) {
            this.materialCatalogAttaList.splice(z, 1);
          }
        });
      }
    },
    //关闭预览附件
    closeFileView() {
      this.showSxFile = false;
    },
    //关闭预览附件
    closeCaseFileView() {
      this.showFile = false;
    },
    //预览附件
    viewFile(attaOid) {
      this.fileAttaOid = attaOid;
      this.showFile = true;
    },
    downLoadMaterialAddr(attaOid) {
      if (attaOid) {
        downloadAttaSimple(attaOid).then(response => {});
      } else {
        this.$message.warning("暂无材料！");
      }
    },
    //表单填报
    formFilling() {
      if (this.sxSerForm) {
        if (this.sxSerForm.formType == 0) {
          //自定义表单
          if (this.sxSerForm.formAddr) {
            window.open(this.sxSerForm.formAddr, "width=1200px;height=800px;");
          } else {
            this.$message.error("请配置表单地址！");
          }
        } else if (this.sxSerForm.formType == 1) {
          //电子表单
          this.iframeState = true;
          //查询电子表单地址配置
          // "http://172.168.249.2:8081/form//loadInitFormReport.do?authorizeKey=00b98ec063984da0afca427d3637fe2a&formOid=FORM20180927GOXSMMER";
          this.iframeUrl =
            process.env.VUE_APP_DZBD_TB_ROUTE_PATH +
            "&formOid=" +
            this.sxSerForm.formCode;
          if (this.caseForm.formDataId) {
            this.iframeUrl += "&reportOid=" + this.caseForm.formDataId;
          }
        }
      }
    },
    //修改表单关闭
    closeIframe(reportOid) {
      if (reportOid) {
        this.caseForm.formDataId = reportOid;
      }
      this.iframeState = false;
    },
    //关闭表单预览
    closeIframeView() {
      this.iframVieweState = false;
    },
    //保存电子表单
    saveCaseForm() {
      this.caseForm.regOid = this.caseOid;
      this.caseForm.serFormOid = this.sxSerForm.oid;
      if (this.caseForm.formDataId) {
        saveOrUpdateCaseForm(this.caseForm).then(response => {
          if (response.data) {
            this.caseForm = response.data;
          }
        });
      }
    },
    //查看电子表单
    viewFormFilling() {
      if (this.sxSerForm) {
        if (this.sxSerForm.formType == 0) {
          //自定义表单
          if (this.sxSerForm.formAddr) {
            window.open(this.sxSerForm.formAddr, "width=1200px;height=800px;");
          } else {
            this.$message.error("请配置表单地址！");
          }
        } else if (this.sxSerForm.formType == 1) {
          //电子表单
          this.iframVieweState = true;
          this.iframeUrlView =
            process.env.VUE_APP_DZBD_CK_ROUTE_PATH +
            "&reportOid=" +
            this.caseForm.formDataId;
        }
      }
    },
    //下载附件
    downloadFile(attaOid) {
      this.download(attaOid);
    },

    //材料智审功能
    clzs(index, count) {
      const loadingnew = this.$loading({
        lock: true,
        text: "材料审核中,请耐心等待...",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });

      let dataForm = {};
      let flag = false;
      dataForm.materialAttaList = this.materialAttaList;
      dataForm.collectionTypeList = this.materialCollectionTypeList;
      dataForm.materialCatalogAttaList = this.materialCatalogAttaList;
      dataForm.elemLicenseList = this.elemLicenseList;

      if (
        !this.materialCatalogAttaList ||
        this.materialCatalogAttaList.length <= 0
      ) {
        loadingnew.close();
        this.msgError("上传文件未关联目录信息不可进行智审！");
        return false;
      } else {
        this.materialCatalogAttaList.forEach((fir, i) => {
          dataForm.materialCatalogAttaList.forEach(second => {
            //如果办件材料一样附件不一样看选择的智审目录是否相同
            if (
              fir.caseMaterialOid == second.caseMaterialOid &&
              fir.attaOid != second.attaOid
            ) {
              if (fir.materialCatalogOid == second.materialCatalogOid) {
                flag = true;
              }
            }
          });
        });
      }
      if (flag) {
        loadingnew.close();
        this.msgError("上传文件请选择不同的目录材料！");
        return false;
      } else {
        saveQlCaseMaterialAtta(dataForm).then(response => {
          this.loading = true;
          if (response.code == 200) {
            this.catalogList = [];
            var letList = response.data;
            var size = letList.length;
            if (letList && size > 0) {
              console.log("材料智审保存附件返回值：" + JSON.stringify(letList));
              for (let i = 0; i < letList.length; i++) {
                let item = letList[i];
                let caseOid = this.caseOid;
                let caseMaterialOid = item.caseMaterialOid;
                let caseFileRecOid = item.materialAttaOid;
                let cataOid = item.materialCatalogOid;
                intelligentPretrialmaterialPrePrial(
                  caseOid,
                  caseFileRecOid,
                  caseMaterialOid,
                  cataOid
                ).then(response => {
                  let str = "";
                  if (!response.data) {
                    this.$message.error("智审连接超时!");
                    loadingnew.close();
                    return false;
                  }
                  if (response.data.success == false) {
                    str = response.data.message;
                    this.$message.error(str);
                    loadingnew.close();
                    return false;
                  }
                  if (i + 1 == letList.length) {
                    loadingnew.close();
                    let item = {
                      caseOid: caseOid,
                      caseMaterialOid: caseMaterialOid,
                      caseFileRecOid: caseFileRecOid,
                      cataOid: cataOid,
                      show: true,
                      title: "智能审核结果"
                    };
                    this.intelligentPretrialOptions.push(item);
                  }
                });
              }
            }
          } else {
            loadingnew.close();
            this.msgError("智审失败！");
            return false;
          }
        });
      }
    },
    openMaterialComparison(materiaId, caseMaterialOid) {
      let attaOids = "";
      if (this.materialAttaList && this.materialAttaList.length > 0) {
        this.materialAttaList.forEach((ite, i) => {
          if (ite.caseMaterialOid == caseMaterialOid) {
            attaOids += ite.attaOid + ";";
          }
        });
      }
      let sampleInfoOid = this.ruleForm.serviceOid;
      let comboDirectoryOid = "";
      let materiaOid = materiaId;
      let caseOid = this.caseOid;
      let fileViewurls =
        process.env.BASE_URL +
        "picture/prviewList.html?materiaOid=" +
        materiaOid +
        "&sampleInfoOid=" +
        sampleInfoOid +
        "&comboDirectoryOid=" +
        comboDirectoryOid;
      let item = {
        show: true,
        title: "材料比对",
        sampleInfoOid: sampleInfoOid,
        comboDirectoryOid: comboDirectoryOid,
        materiaOid: materiaOid,
        fileViewurl: fileViewurls,
        caseOid: caseOid,
        attaOids: attaOids
      };
      this.materialComparisonOptions.push(item);
    },
    printNotice() {
      this.$confirm("是否打印告知承诺书?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.handleGzcnPrint();
        })
        .catch(() => {});
    },
    handleGzcnPrint() {
      let noticeMes = {};
      if (this.cegisterType == 0) {
        noticeMes.applyUserName = this.ruleForm.applyUserName;
        noticeMes.applyCompany = "";
      } else {
        noticeMes.applyCompany = this.ruleForm.applyUserName;
        noticeMes.applyUserName = "";
      }
      noticeMes.phone = this.ruleForm.applyUserPhone;
      noticeMes.serviceName = this.serviceForm.serviceName;
      noticeMes.zxPhone = this.serviceForm.zxDhText;
      noticeMes.caseNumber = this.caseNumber;
      noticeMes.bzDate = this.bqbzCaseForm.dueDate;
      noticeMes.fileName = "gzcns.doc";
      noticeMes.materials = this.allMaterialNames;
      this.handlePrintAll(noticeMes);
    },
    //调用c++服务打印
    handlePrintAll(obj) {
      getFileDownPath(obj).then(response => {
        if (response.data) {
          downloadPrintFile(encodeURIComponent(response.data));
        }
      });
    },
    //调用pageoffice的打印
    handleGzcnPrint1() {
      let caseName = encodeURIComponent(this.ruleForm.projectName);
      let applyUserName = encodeURIComponent(this.ruleForm.applyUserName);
      let sqTime = this.bqbzCaseForm.dueDate;
      POBrowser.openWindowModeless(
        process.env.VUE_APP_BASE_API_PAGE +
          "/manage/zhuozheng/cngzNoticePrint?caseName=" +
          caseName +
          "&applyUserName=" +
          applyUserName +
          "&sqTime=" +
          sqTime,
        "width=1200px;height=800px;"
      );
    },
    getUserInfo(credentialNumber) {
      if (!credentialNumber) {
        this.$message.error("证件号不能为空!");
        return false;
      }
      this.applyCardNum = credentialNumber;
      this.userInfoShow = true;
    },
    closeUserInfoShow(userCase) {
      this.queryQlCaseInfo(userCase);
      this.userInfoShow = false;
    },
    //查询保存的办件数据 并回填数据
    queryQlCaseInfo(userCase) {
      if (userCase != "") {
        queryAllCaseByOid(userCase).then(response => {
          let qlCase = response.data.qlCase;
          let qlCaseApplay = response.data.qlCaseApplay;
          let qlCaseExt = response.data.qlCaseExt;
          this.ruleForm.projectName = qlCase.projectName;
          this.ruleForm.specificLocation = qlCaseApplay.specificLocation;
          this.ruleForm.applyNumber = qlCaseApplay.applyNumber;
          this.ruleForm.applyUserName = qlCaseApplay.applyUserName;
          this.ruleForm.applyUserPhone = qlCaseApplay.applyUserPhone;
          this.ruleForm.applyUserTel = qlCaseApplay.applyUserTel;
          this.ruleForm.applyPostCode = qlCaseApplay.applyPostCode;
          this.ruleForm.credentialNumber = qlCaseApplay.credentialNumber;
          this.ruleForm.legalPersonName = qlCaseApplay.legalPersonName;
          this.ruleForm.applyUserAddress = qlCaseApplay.applyUserAddress;
          this.ruleForm.addresseeName = qlCaseApplay.addresseeName;
          this.ruleForm.addresseePhone = qlCaseApplay.addresseePhone;
          this.ruleForm.addresseeTel = qlCaseApplay.addresseeTel;
          this.ruleForm.addresseePostCode = qlCaseApplay.addresseePostCode;
          this.ruleForm.addresseeDetailAddress =
            qlCaseApplay.addresseeDetailAddress;
          this.ruleForm.contactUserName = qlCaseApplay.contactUserName;
          this.ruleForm.contactCredentialNumber =
            qlCaseApplay.contactCredentialNumber;
          this.ruleForm.contactEmail = qlCaseApplay.contactEmail;
          this.ruleForm.contactUserPhone = qlCaseApplay.contactUserPhone;
          this.ruleForm.contactUserTel = qlCaseApplay.contactUserTel;
          this.ruleForm.contactRemark = qlCaseApplay.contactRemark;
          this.ruleForm.investProjecName = qlCaseExt.investProjecName;
          this.ruleForm.investProjectCode = qlCaseExt.investProjectCode;
          this.ruleForm.projectAbstract = qlCaseExt.projectAbstract;
          this.ruleForm.resultDeliveryWay = qlCaseExt.resultDeliveryWay;
          this.ruleForm.proxyFlag = qlCaseExt.proxyFlag;
          this.changeDeliveryWay(qlCaseExt.resultDeliveryWay);
          this.changeProxyFlag(qlCaseExt.proxyFlag);
          this.ruleForm.credentialType = qlCaseApplay.credentialType;
          this.getDistrictTreeNew(qlCaseApplay.bussVenueDistrictOid);
          this.getAddressTree(qlCaseApplay.addresseeAddress);
        });
      } else {
        this.msgError("办件查询失败！");
        return false;
      }
    },
    getAddressTree(addresseeAddress) {
      let address = [];
      if (addresseeAddress != "" && addresseeAddress != null) {
        addresseeAddress = addresseeAddress.replace('["', "").replace('"]', "");
        if (addresseeAddress != "") {
          let addressIds =
            addresseeAddress != "" ? addresseeAddress.split('","') : [];
          for (let oid of addressIds) {
            if (oid != "") {
              address.push(oid);
            }
          }
        }
      }
      this.ruleForm.addresseeAddress = address;
    },
    /** 获取区划 */
    getDistrictTreeNew(bussVenueDistrictOids) {
      let oids = [];
      if (bussVenueDistrictOids != "") {
        let districtOids =
          bussVenueDistrictOids != "" ? bussVenueDistrictOids.split(",") : [];
        for (let oid of districtOids) {
          if (oid != "") {
            oids.push(oid);
          }
        }
        this.ruleForm.bussVenueDistrictOid = oids;
      }
    },
    handleClose() {
      this.$refs.scanForm.getImageRes();
      this.dialogVisible = false;
    },

    //调用摄像头获取图像信息
    getImageCamera() {
      this.dialogVisible = true;
    },
    //内资公司变更法定代表人表单
    getNzgsbgFormInfo() {
      this.bgForm = true;
    },
    closebgForm() {
      this.bgForm = false;
    },
    nzgsbgFormView() {
      getCaseFormInfo(this.caseOid).then(response => {
        let res = response.data;
        if (res) {
          this.nzgsbgViewForm = true;
        } else {
          this.$message.error("表单未填写!");
        }
      });
    },
    closebgnzForm() {
      this.nzgsbgViewForm = false;
    },
    //道路运输许可证
    getDlysxkzFormInfo() {
      this.dlysForm = true;
    },
    closedlForm() {
      this.dlysForm = false;
    },
    closedlViewForm() {
      this.dlysViewForm = false;
    },
    openDlysView() {
      getCaseFormInfo(this.caseOid).then(response => {
        let res = response.data;
        if (res) {
          this.dlysViewForm = true;
        } else {
          this.$message.error("表单未填写!");
        }
      });
    },
    printNzgsbg() {
      POBrowser.openWindowModeless(
        process.env.VUE_APP_BASE_API_PAGE +
          "/manage/zhuozheng/printGrdzqybg?caseOid=" +
          this.caseOid,
        "width=1200px;height=800px;"
      );
    },

    //车辆信息
    addCarInfo() {
      let carInfo = {
        transportLicenseNo: "",
        carLicense: "",
        cpxhNum: "",
        carType: "",
        weightNum: "",
        fuel: "",
        axesNum: "",
        technicalLevel: ""
      };
      this.form.carList.push(carInfo);
    },
    delCarInfo(index) {
      this.form.carList.splice(index, 1);
    },

    //驾驶员信息
    addDirverInfo() {
      let driver = {
        driverName: "",
        sex: "",
        age: "",
        eduLevel: "",
        linceseTime: "",
        carTypeUse: "",
        cyzgNum: ""
      };
      this.form.driverList.push(driver);
    },

    delDriverInfo(index) {
      this.form.driverList.splice(index, 1);
    },
    submitForm() {
      saveOrUpdateInfo(this.form).then(response => {
        let res = response.data;
        if (res.status == "true") {
          this.cancel();
        } else {
          this.$message.error("保存失败！");
        }
      });
    },
    addBgService() {
      let service = { serviceName: "", oldService: "", newService: "" };
      this.form.bglist.push(service);
    },
    delBg(index) {
      this.form.bglist.splice(index, 1);
    },
    addCzqk() {
      let czqk = {
        username: "",
        national: "",
        zjType: "",
        zjNumber: "",
        rjczNum: "",
        sjczNum: "",
        czrjTime: "",
        czfs: "",
        czbl: ""
      };
      this.form.czqkList.push(czqk);
    },
    delBgCz(index) {
      this.form.czqkList.splice(index, 1);
    },
    changeFlag(val) {}
  }
};
</script>
<style lang="scss" scoped>
.dialog-table {
  position: relative;
}
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
  width: 477px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
@media screen and (max-width: 1600px) {
  .process-box .form-box-inline .input-text > p {
    width: 275px;
  }
}

.process-box .form-btn-group {
  position: absolute;
  right: 20px;
  top: 10px;
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

.handle-data .el-col-9 {
  margin-top: -6px;
}

.right-btn-group {
  position: absolute;
  right: 130px;
  top: 11px;
}

.right-btn-group-two {
  position: absolute;
  right: 10px;
  top: 10px;
}

.right-btn-group-two .el-form > .el-button {
  margin-top: 0;
}

.right-btn-group .el-button {
  // padding: 6px 8px;
  font-size: 12px;
  // margin-left: 5px;
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

.qdcg-text img {
  margin-right: 5px;
  margin-top: -4px;
  float: left;
}

.qdcg-text p {
  width: 86%;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
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

.category_style {
  height: 45px; // 设置高度，覆盖组件中原option的高度
  overflow: auto; // 内容超出，显示滚动条
  line-height: 45px;
  top: -7px;
}

.btn-download {
  position: absolute;
  right: 150px;
  top: 20px;
  padding: 8px 15px;
  font-size: 12px;
}

.data-btn {
  position: absolute;
  top: 25px;
  right: 11px;
}

.el-radio {
  margin-right: 12px;
}
</style>
