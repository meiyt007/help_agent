/** * @Author: wangxl */
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--目录列表数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px">
          <el-form-item label="目录名称" prop="comboDirectoryName">
            <el-input
              v-model.trim="queryParams.comboDirectoryName"
              placeholder="请输入目录名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="所属分类">
            <treeselect
              class="treeselect"
              :defaultExpandLevel="1"
              noOptionsText="暂无数据"
              noResultsText="暂无数据"
              :show-count="true"
              v-model="queryParams.themeOidSelect"
              :options="themeOptions"
              placeholder="请输入所属分类"
            />
          </el-form-item>
          <el-form-item label="发布状态">
            <el-radio-group v-model="queryParams.status" @change="handleRowChange">
              <el-radio v-for="(status, key) in statusOptions" :key="key" :label="key">{{ status }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>
        <label
          style="color: #bd2c00; margin-top: 5px"
          class="mb10 inlineBlock"
        >&nbsp;&nbsp;&nbsp;&nbsp;（目录必须为已发布、已授权、已上架才能登记办件！）</label>
        <el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit"
              v-hasPermi="['combo:directory:init']"
            >新增</el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="comboDirectoryList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="分类名称" align="center" prop="themeName" :show-overflow-tooltip="true" />
          <el-table-column label="目录名称" align="center" prop="comboDirectoryName" :show-overflow-tooltip="true" />
          <el-table-column label="所属区划" width="100" align="center" prop="districtName" :show-overflow-tooltip="true" />
          <el-table-column label="主办部门" width="100" align="center" prop="mainOrganName" :show-overflow-tooltip="true" />
          <el-table-column
            label="状态"
            width="80"
            align="center"
            :formatter="getPublishName"
            prop="status"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="操作" width="400" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                v-hasPermi="['combo:directory:view']"
              >查看</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleInit(scope.row)"
                v-if="scope.row.status == 0 || scope.row.status == 4"
                v-hasPermi="['combo:directory:update']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleDelete(scope.row)"
                v-if="scope.row.status == 0"
                v-hasPermi="['combo:directory:delete']"
              >删除</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-penzhi"
                @click="queryServiceList(scope.row)"
                v-if="scope.row.status != 1"
                v-hasPermi="['combo:directory:service']"
              >事项</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-penzhi"
                @click="queryMaterialList(scope.row)"
                v-if="scope.row.status !== 1"
                v-hasPermi="['combo:directory:service']"
              >材料</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-penzhi"
                @click="queryResultList(scope.row)"
                v-if="scope.row.status != 1"
                v-hasPermi="['combo:directory:service']"
              >证照</el-button>
              <el-button
                size="mini"
                type="text"
                v-if="
                  (scope.row.ifPublish == 1 && scope.row.status == 0) ||
                  (scope.row.ifPublish == 1 && scope.row.status == 4)
                "
                icon="iconfont zfsoft-fabu"
                @click="publishDire(scope.row)"
                v-hasPermi="['combo:directory:delete']"
              >发布</el-button>
              <el-button
                size="mini"
                type="text"
                v-if="scope.row.status == 1"
                icon="iconfont zfsoft-fabu"
                @click="publishCancel(scope.row)"
                v-hasPermi="['combo:directory:service']"
              >取消发布</el-button>
              <el-button
                size="mini"
                type="text"
                v-if="scope.row.status == 1"
                icon="iconfont zfsoft-fabu"
                @click="comboDirectoryCopy(scope.row)"
                v-hasPermi="['combo:directory:copy']"
              >一键复制</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>
    <!-- 添加或修改分类信息对话框 -->
    <el-dialog
      v-if="openInit"
      v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="openInit"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <el-tabs v-model="activeName">
        <el-tab-pane label="基本信息" name="first">
          <el-form ref="form" :model="form" :rules="rules" label-width="0">
            <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
              <el-input v-show="false" v-model="form.id" />
              <el-input v-show="false" v-model="form.comboDirectoryOid" />
              <el-input v-show="false" v-model="form.delFlag" />
              <el-input v-show="false" v-model="form.status" />
              <el-input v-show="false" v-model="form.createUser" />
              <el-input v-show="false" v-model="form.comboDirectoryCode" />
              <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
              </colgroup>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>目录名称：</b>
                </td>
                <td>
                  <el-form-item prop="comboDirectoryName">
                    <el-input
                      v-model.trim="form.comboDirectoryName"
                      placeholder="请输入目录名称"
                      maxlength="100"
                      show-word-limit
                    />
                  </el-form-item>
                </td>
                <td>
                  <i class="require">*</i>
                  <b>目录图标：</b>
                </td>
                <td>
                  <el-form-item prop="directoryIcon">
                    <el-input v-show="false" v-model="form.directoryIcon" />
                    <i style="font-size: 30px" :class="form.directoryIcon" />
                    <el-button type="primary" size="mini" @click="handleIconSelect">选择图标</el-button>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>所属区划：</b>
                </td>
                <td>
                  <el-form-item prop="districtOid">
                    <treeselect
                      class="treeselect240"
                      :defaultExpandLevel="1"
                      noOptionsText="暂无数据"
                      noResultsText="暂无数据"
                      :show-count="true"
                      v-model="form.districtOid"
                      :options="districtOptions"
                      placeholder="请输入所属区划"
                    />
                  </el-form-item>
                </td>
                <td>
                  <i class="require">*</i>
                  <b>所属分类：</b>
                </td>
                <td>
                  <!--:disable-branch-nodes="true"//禁用可打开的节点 -->
                  <el-form-item prop="themeOid">
                    <treeselect
                      class="themeTreeselect"
                      :defaultExpandLevel="1"
                      noOptionsText="暂无数据"
                      noResultsText="暂无数据"
                      :show-count="true"
                      v-model="form.themeOid"
                      :disable-branch-nodes="true"
                      :options="themeOptions"
                      placeholder="请输入所属分类"
                    />
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <b>事项大类：</b>
                </td>
                <td>
                  <el-form-item prop="direType">
                    <el-select v-model="form.direType" placeholder="请选择" @change="getDireType">
                      <el-option v-for="item in direTypes" :key="item.value" :label="item.label" :value="item.value"></el-option>
                    </el-select>
                  </el-form-item>
                </td>
                <td>
                  <i class="require">*</i>
                  <b>主办部门：</b>
                </td>
                <td>
                  <el-form-item prop="mainOrganOid">
                    <treeselect
                      class="treeselect240"
                      :defaultExpandLevel="1"
                      noOptionsText="暂无数据"
                      noResultsText="暂无数据"
                      :show-count="true"
                      v-model="form.mainOrganOid"
                      @select="selectXb"
                      :options="mainOrganOptions"
                      placeholder="请输入主办部门"
                    />
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>协办部门：</b>
                </td>
                <td colspan="3" class="xbbm">
                  <el-form-item prop="assistOrganOid" v-if="form.direType != 4 && form.direType != 5">
                    <treeselect
                      :multiple="true"
                      :options="assistOrganOptions"
                      :flat="true"
                      :default-expand-level="1"
                      placeholder="请输入协办部门"
                      :disabled="ifDis"
                      v-model="form.assistOrganOid"
                    />
                  </el-form-item>
                  <el-form-item prop="districtOid" v-if="form.direType == 4 || form.direType == 5">
                    <treeselect
                      :multiple="true"
                      :disable-branch-nodes="true"
                      ref="userTree"
                      :defaultExpandLevel="1"
                      noOptionsText="暂无数据"
                      noResultsText="暂无数据"
                      :show-count="true"
                      v-model="form.assistOrganOid"
                      :options="assistOrganOptions"
                      placeholder="请输入协办部门"
                    />
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>服务对象：</b>
                </td>
                <td colspan="3">
                  <!--<el-input v-show="false" v-model="form.comboServiceObject" />
              <el-checkbox-group v-model="checkList" >
                <el-checkbox
                  v-for="comboObj in comboServiceObjects"
                  :key="comboObj.key"
                  :label="comboObj.key"
                  >{{comboObj.name}}</el-checkbox>
                  </el-checkbox-group>-->
                  <el-form-item prop="comboServiceObject">
                    <el-radio-group v-model="form.comboServiceObject">
                      <el-radio
                        v-for="(comboObj, index) in comboServiceObjects"
                        :key="index"
                        :label="comboObj.key"
                      >{{ comboObj.name }}</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>申报须知：</b>
                </td>
                <td colspan="3">
                  <el-form-item prop="declareNeedKnow">
                    <el-input
                      v-model.trim="form.declareNeedKnow"
                      type="textarea"
                      maxlength="500"
                      show-word-limit
                      placeholder="请输入申报须知"
                    ></el-input>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require"></i>
                  <b>前置事项：</b>
                </td>

                <td colspan="3">
                  <el-form-item label prop="serviceOids">
                    <treeselect
                      :multiple="true"
                      :disable-branch-nodes="true"
                      ref="userTree"
                      :defaultExpandLevel="1"
                      noOptionsText="暂无数据"
                      noResultsText="暂无数据"
                      :show-count="true"
                      v-model="roleForm.serviceOids"
                      :options="appRoleOptions"
                      placeholder="请选择事项"
                    />
                  </el-form-item>
                </td>
              </tr>
              <tr v-if="roleForm.serviceOids != ''">
                <td>
                  <b>事项系统类型：</b>
                </td>
                <td colspan>
                  <el-form-item prop="servicextlx">
                    <el-input
                      v-model.trim="form.servicextlx"
                      type="textarea"
                      maxlength="500"
                      show-word-limit
                      placeholder="请输入前置事项的事项系统类型"
                    ></el-input>
                  </el-form-item>
                </td>
                <td>
                  <b>审批系统名称：</b>
                </td>
                <td colspan>
                  <el-form-item prop="servicespxt">
                    <el-input
                      v-model.trim="form.servicespxt"
                      type="textarea"
                      maxlength="500"
                      show-word-limit
                      placeholder="请输入前置事项的审批系统名称"
                    ></el-input>
                  </el-form-item>
                </td>
              </tr>
              <tr v-if="roleForm.serviceOids != ''">
                <td>
                  <b>申报地址链接：</b>
                </td>
                <td colspan="3">
                  <el-form-item prop="serviceApplyAddr">
                    <el-input
                      v-model.trim="form.serviceApplyAddr"
                      type="textarea"
                      maxlength="500"
                      show-word-limit
                      placeholder="请输入前置事项的申报地址链接"
                    ></el-input>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>是否收费：</b>
                </td>
                <td>
                  <el-form-item>
                    <el-radio-group v-model="form.ifCharge">
                      <el-radio :label="0">否</el-radio>
                      <el-radio :label="1">是</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </td>
                <td>
                  <i class="require">*</i>
                  <b>是否需要人证核验：</b>
                </td>
                <td>
                  <el-form-item>
                    <el-radio-group v-model="form.showRzhs">
                      <el-radio :label="0">否</el-radio>
                      <el-radio :label="1">是</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </td>
              </tr>

              <tr v-if="form.ifCharge == 1">
                <td>
                  <b>收费标准：</b>
                </td>
                <td colspan="3">
                  <el-form-item prop="chargeStandard">
                    <el-input
                      v-model.trim="form.chargeStandard"
                      type="textarea"
                      maxlength="500"
                      show-word-limit
                      placeholder="请输入收费标准"
                    ></el-input>
                  </el-form-item>
                </td>
              </tr>
              <tr v-if="form.ifCharge == 1">
                <td>
                  <b>收费依据：</b>
                </td>
                <td colspan="3">
                  <el-form-item prop="chargeGist">
                    <el-input
                      v-model.trim="form.chargeGist"
                      type="textarea"
                      maxlength="500"
                      show-word-limit
                      placeholder="请输入收费依据"
                    ></el-input>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>网办地址：</b>
                </td>
                <td colspan="3">
                  <el-form-item prop="webUrl">
                    <el-input v-model.trim="form.webUrl" placeholder="请输入网办地址" maxlength="100" show-word-limit />
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>办理地址：</b>
                </td>
                <td colspan="3">
                  <el-form-item prop="manageAddr">
                    <el-input
                      v-model.trim="form.manageAddr"
                      type="textarea"
                      maxlength="250"
                      show-word-limit
                      placeholder="请输入办理地址"
                    ></el-input>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>办理时间：</b>
                </td>
                <td colspan="3">
                  <el-form-item prop="manageTime">
                    <el-input
                      v-model.trim="form.manageTime"
                      type="textarea"
                      maxlength="200"
                      show-word-limit
                      placeholder="请输入办理时间"
                    ></el-input>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>办事流程图：</b>
                </td>
                <td>
                  <el-form-item prop="handleFlow">
                    <el-input v-show="false" v-model="form.handleFlow" />
                    <el-input v-show="false" v-model="form.handleFlowName" />
                    <el-button type="success" size="mini" @click="selectAttas">办理流程图附件</el-button>
                    <el-button type="danger" size="mini" @click="clearAtta">清理</el-button>
                    <div v-if="handleFlowFlag">
                      <span>{{ form.handleFlowName }}</span>
                      <el-link type="primary" @click="downloadFile(form.handleFlow)">下载</el-link>|
                      <el-link type="primary" @click="viewFile(form.handleFlow)">预览</el-link>
                    </div>
                  </el-form-item>
                </td>
                <td>
                  <b>有无中介服务：</b>
                </td>
                <td>
                  <el-form-item>
                    <el-radio-group v-model="form.isZjfw">
                      <el-radio :label="0">否</el-radio>
                      <el-radio :label="1">是</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <b>办理流程图说明：</b>
                </td>
                <td colspan="3">
                  <el-form-item prop="handleDesc">
                    <el-input
                      v-model.trim="form.handleDesc"
                      type="textarea"
                      maxlength="250"
                      show-word-limit
                      placeholder="请输入办理流程图说明"
                    ></el-input>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <b>设定依据：</b>
                </td>
                <td colspan="3">
                  <el-form-item prop="setAccord">
                    <el-input
                      v-model.trim="form.setAccord"
                      type="textarea"
                      maxlength="500"
                      show-word-limit
                      placeholder="请输入设定依据"
                    ></el-input>
                  </el-form-item>
                </td>
              </tr>
              <tr v-if="form.isZjfw == 1">
                <td>
                  <b>中介名称：</b>
                </td>
                <td colspan="3">
                  <el-form-item prop="zjfwName">
                    <el-input v-model.trim="form.zjfwName" placeholder="请输中介名称" maxlength="100" show-word-limit />
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>咨询方式：</b>
                </td>
                <td colspan="3">
                  <el-form-item prop="zixunType">
                    <el-input
                      v-model.trim="form.zixunType"
                      type="textarea"
                      maxlength="250"
                      show-word-limit
                      placeholder="请输入咨询方式"
                    ></el-input>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>监督方式：</b>
                </td>
                <td colspan="3">
                  <el-form-item prop="jianduType">
                    <el-input
                      v-model.trim="form.jianduType"
                      type="textarea"
                      maxlength="250"
                      show-word-limit
                      placeholder="请输入监督方式"
                    ></el-input>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>联办能力：</b>
                </td>
                <td>
                  <el-form-item>
                    <el-radio-group v-model="form.unionOrganFlag">
                      <el-radio :label="0">否</el-radio>
                      <el-radio :label="1">是</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </td>
                <td>
                  <i class="require">*</i>
                  <b>是否支持预约办理：</b>
                </td>
                <td>
                  <el-form-item>
                    <el-radio-group v-model="form.appointmentFlag">
                      <el-radio :label="0">否</el-radio>
                      <el-radio :label="1">是</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>办理形式：</b>
                </td>
                <td colspan="3">
                  <el-form-item>
                    <el-radio-group v-model="form.handleForm">
                      <el-radio label="0">窗口办理</el-radio>
                      <el-radio label="1">网上办理</el-radio>
                      <el-radio label="2">一体化办理</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <b>办理端要素：</b>
                </td>
                <td colspan="3">
                  <el-checkbox-group v-model="elements">
                    <el-checkbox label="1" disabled>窗口端</el-checkbox>
                    <el-checkbox label="3">移动端</el-checkbox>
                    <el-checkbox label="2">网站端</el-checkbox>
                    <el-checkbox label="4">自助终端</el-checkbox>
                  </el-checkbox-group>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>承诺时限(工作日)：</b>
                </td>
                <td>
                  <el-form-item prop="promiseLimit">
                    <el-input-number v-model="form.promiseLimit" :min="1" :max="9999" />
                  </el-form-item>
                </td>
                <td>
                  <i class="require">*</i>
                  <b>法定时限(工作日)：</b>
                </td>
                <td>
                  <el-form-item prop="legalLimit">
                    <el-input-number v-model="form.legalLimit" :min="1" :max="9999" />
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>是否网上支付：</b>
                </td>
                <td>
                  <el-form-item>
                    <el-radio-group v-model="form.onlinePayFlag">
                      <el-radio :label="0">否</el-radio>
                      <el-radio :label="1">是</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </td>
                <td>
                  <i class="require">*</i>
                  <b>是否支持物流快递：</b>
                </td>
                <td>
                  <el-form-item>
                    <el-radio-group v-model="form.expressFlag">
                      <el-radio :label="0">否</el-radio>
                      <el-radio :label="1">是</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>线下跑动次数：</b>
                </td>
                <td colspan="3">
                  <el-form-item>
                    <el-radio-group v-model="form.countToScence">
                      <el-radio :label="0">0次</el-radio>
                      <el-radio :label="1">1次</el-radio>
                      <el-radio :label="2">2次</el-radio>
                      <el-radio :label="3">多次</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </td>
              </tr>
              <tr v-if="form.countToScence != 0">
                <td>
                  <b>线下跑动的原因和环节：</b>
                </td>
                <td colspan="3">
                  <el-form-item prop="reasonToScence">
                    <el-input
                      v-model.trim="form.reasonToScence"
                      type="textarea"
                      maxlength="250"
                      show-word-limit
                      placeholder="请输入线下跑动的原因和环节"
                    ></el-input>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <b>咨询用户：</b>
                </td>
                <td colspan="3">
                  <el-tree
                    show-checkbox
                    node-key="id"
                    ref="userTree"
                    highlight-current
                    :props="props"
                    :data="userTreeData"
                    :default-expanded-keys="userExpandedKeys"
                    :default-checked-keys="userCheckedKeys"
                    check-strictly
                    @check="handleUserTreeCheck"
                  />
                </td>
              </tr>
            </table>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="前置核验" name="second">
          <PreVerification :comboDirectoryOid="comboDirectoryOid" ref="preVerification" />
        </el-tab-pane>
      </el-tabs>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 分类信息详细 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="openView"
      v-if="openView"
      title="目录信息"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <combo-directory-view :comboDirectoryOid="comboDirectoryOidView" @combo-directory="comboDirectoryClose"></combo-directory-view>
      <div slot="footer" class="zf-text-center">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!-- 选择配置项附件view-->
    <el-dialog
      v-dialog-drag
      title="选择办理流程图附件"
      :visible.sync="openAttaListView"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <div>
        <!-- <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">-->
        <div style="margin-bottom:10px">
          <el-upload
            class="upload-demo"
            :action="uploadUrl()"
            :on-error="uploadError"
            :file-list="fileList"
            :before-upload="beforeUpload"
            :on-success="uploadSuccess"
          >
            <el-button size="small" type="primary">
              点击上传
              <i class="iconfont zfsoft-wenjianshangchuan"></i>
            </el-button>
          </el-upload>
        </div>

        <!-- </el-col>
        </el-row>-->

        <el-table v-loading="loading" :data="attaList">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="原始文件名" align="center" prop="originName" />
          <el-table-column label="文件名" align="center" prop="name" />
          <el-table-column label="上传时间" align="center" prop="uploadDate" width="180" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-link type="primary" @click="selectFile(scope.row.attaOid, scope.row.originName)">选择</el-link>|
              <el-link type="primary" @click="downloadFile(scope.row.attaOid)">下载</el-link>|
              <el-link type="primary" @click="viewFile(scope.row.attaOid)">预览</el-link>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="attatotal > 0"
          :total="attatotal"
          :page.sync="attaQueryParams.pageNum"
          :limit.sync="attaQueryParams.pageSize"
          @pagination="getAttaList"
        />
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button @click="openAttaListView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!--事项关联开始-->
    <el-dialog
      v-dialog-drag
      class="dialog-header"
      :visible.sync="combo.show"
      title="事项关联"
      v-for="combo in comboDialogOptions"
      :key="combo.comboDirectoryOid"
      @close="closeUserView"
      :close-on-click-modal="false"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <combo-service :comboDireOid="combo.comboDirectoryOid"></combo-service>
      <div slot="footer" class="zf-text-center">
        <el-button @click="closeUserView">关 闭</el-button>
      </div>
    </el-dialog>
    <!--事项关联结束-->

    <!--目录公共材料开始-->
    <el-dialog
      v-dialog-drag
      class="dialog-header"
      :visible.sync="service.show"
      title="目录材料"
      v-for="service in materialDialogOptions"
      :key="service.comboDirectoryOid"
      @close="closeMaterView"
      :close-on-click-modal="false"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <combo-directory-material :comboDireOid="service.comboDirectoryOid"></combo-directory-material>
      <div slot="footer" class="zf-text-center">
        <el-button @click="closeMaterView">关 闭</el-button>
      </div>
    </el-dialog>
    <!--目录公共材料结束-->
    <!--目录统一证照开始-->
    <el-dialog
      v-dialog-drag
      class="dialog-header"
      :visible.sync="result.show"
      title="目录证照"
      v-for="result in resultDialogOptions"
      :key="result.comboDirectoryOid"
      @close="closeResultView"
      :close-on-click-modal="false"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <combo-directory-result :comboDireOid="result.comboDirectoryOid"></combo-directory-result>
      <div slot="footer" class="zf-text-center">
        <el-button @click="closeResultView">关 闭</el-button>
      </div>
    </el-dialog>
    <!--目录统一证照结束-->
    <!--引入文件的预览弹出层-->
    <el-dialog
      v-dialog-drag
      title="文件预览"
      :visible.sync="view.show"
      v-for="view in viewDialogOptions"
      :key="view.attaOid"
      :close-on-click-modal="false"
      @close="closeFileView"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <combo-dire-file-view :attaOid="view.attaOid" @father-click="closeFileView"></combo-dire-file-view>
      <div slot="footer" class="zf-text-center">
        <el-button @click="closeFileView">关 闭</el-button>
      </div>
    </el-dialog>
    <!--引入图标选择弹出层-->
    <el-dialog
      v-dialog-drag
      title="选择按钮图标"
      :visible.sync="openIconSelectView"
      :close-on-click-modal="false"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <iconfont @father-click="getSelectClass"></iconfont>
      <div slot="footer" class="zf-text-center">
        <el-button @click="openIconSelectView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 一键复制 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="copyFlag"
      v-if="copyFlag"
      title="一键复制"
      width="500px"
      height="250px"
      scrollbar
      append-to-body
    >
      <combo-directory-copy :comboDirectoryOid="comboDirectoryOid" @copyClose="copyClose"></combo-directory-copy>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryServiceTree,
  init,
  save,
  del,
  getOne,
  pagemulu,
  getComboServiceObject,
  publishDire,
  publishCancel,
  getParentOidByDistrictOid,
  queryDistrictAndOrganTree,
  saveOrUpdateList
} from "@/api/onething/sxpz/comboDirectory";
import { queryComboThemeSimpleTree } from "@/api/onething/sxpz/comboTheme";
import {
  pageFile,
  uploadFile,
  downloadFile,
} from "@/api/onething/sxpz/comboAtta";
import { verifyUnconfirmedFlag } from "@/api/onething/sxpz/comboDirectoryMaterial";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import iconfont from "@/views/common/iconfontSelect";
import comboService from "@/views/onething/sxpz/comboDirectory/comboService";
import comboDirectoryMaterial from "@/views/onething/sxpz/comboDirectory/comboDirectoryMaterial";
import comboDirectoryResult from "@/views/onething/sxpz/comboDirectory/comboDirectoryResult";
import comboDireFileView from "@/views/onething/sxpz/comboDirectory/comboDireFileView";
import comboDirectoryView from "@/views/onething/sxpz/comboDirectory/comboDirectoryView";
import ComboDirectoryCopy from '@/views/onething/sxpz/comboDirectory/comboDirectoryCopy';
import { queryUserTree } from "@/api/sxpt/serviceType";
import PreVerification from './preVerification.vue';
import {validUrlAddress} from '@/utils/validate';

export default {
  components: {
    Treeselect,
    iconfont,
    comboService,
    comboDirectoryMaterial,
    comboDirectoryResult,
    comboDireFileView,
    comboDirectoryView,
    ComboDirectoryCopy,
    PreVerification
  },
  name: "ComboDirectory",
  data () {
    return {
      activeName: 'first',
      //复制标识
      copyFlag: false,
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      attatotal: 0,
      // 应用表格数据
      comboDirectoryList: [],
      viewDialogOptions: [],
      //事项查看
      comboDialogOptions: [],
      materialDialogOptions: [],
      resultDialogOptions: [],
      //图标样式弹出层
      openIconSelectView: false,
      ifDis: false,
      direTypes: [
        {
          value: 1,
          label: "单部门行政审批类"
        },
        {
          value: 2,
          label: "跨部门业务协同类"
        },
        {
          value: 3,
          label: "政府服务类"
        },
        {
          value: 4,
          label: "跨层级业务协同类"
        },
        {
          value: 5,
          label: "跨区域业务协同类"
        }
      ],
      //查询目录名称参数
      comboDirectoryName: "",
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      comboDirectoryOid: '',
      // 查看显示弹出层
      openView: false,
      openAttaListView: false,
      //选择附件项附件
      openComboService: false,
      comboDireOid: "",
      //目录查看使用
      comboDirectoryOidView: "",

      fileList: [],
      elements: ["1"],
      // 区划树选项
      //themeOptions: undefined,
      // 级别字典
      // comboThemes: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        comboDirectoryName: "",
        status: "0",
        themeOid: null,
        themeOidSelect: null
      },
      //附件参数
      attaQueryParams: {
        pageNum: 1,
        pageSize: 10
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {
        comboServiceObject: "1",
        ifCharge: 0,
        isZjfw: 0,
        appointmentFlag: 0,
        handleForm: "0",
        onlinePayFlag: 0,
        expressFlag: 0,
        unionOrganFlag: 0,
        countToScence: 0,
        showRzhs: 0,
        direType: "",
        serviceOids: "",
        servicextlx: "",
        servicespxt: "",
        serviceApplyAddr: "",
        handleFlow: "",
        districtOid: "",
        consultUserOid: ''
      },
      userExpandedKeys: [],
      userCheckedKeys: [],
      props: {
        label: "label",//这里是树结构中需显示的数据（即接口返回的需展示在页面上的参数）
        children: 'children',
      },
      userTreeData: [],
      userTreeDataFlat: [],
      //分类Tree
      themeOptions: [],
      roleForm: { serviceOids: [] },
      //区划Tree
      districtOptions: [],
      //角色
      appRoleOptions: [],
      //主办部门Tree
      mainOrganOptions: [],
      //协办部门Tree
      assistOrganOptions: [],
      //服务对象
      comboServiceObjects: [],
      assistOrganOids: [],
      handleFlowFlag: false,
      //附件列表
      attaList: [],
      //办理深度
      //handleForms:[],
      // 表单校验
      // checkList: [],
      // 发布状态
      statusOptions: { "0": "未发布", "1": "已发布" },
      rules: {
        comboDirectoryName: [
          { required: true, message: "目录名称不能为空", trigger: "blur" }
        ],
        /*directoryIcon: [
          { required: true, message: "目录图标不能为空", trigger: "blur" }
        ],*/
        themeOid: [
          { required: true, message: "所属分类不能为空", trigger: "blur" }
        ],
        comboServiceObject: [
          { required: true, message: "服务对象不能为空", trigger: "blur" }
        ],
        declareNeedKnow: [
          { required: true, message: "申报须知不能为空", trigger: "blur" }
        ],
        districtOid: [
          { required: true, message: "所属区划不能为空", trigger: "blur" }
        ],
        mainOrganOid: [
          { required: true, message: "主办部门不能为空", trigger: "blur" }
        ],
        assistOrganOid: [
          { required: true, message: "协办部门不能为空", trigger: "blur" }
        ],
        webUrl: [
          { required: true, message: "网办地址不能为空", trigger: "blur" },
          { validator: validUrlAddress, trigger: 'blur'}
        ],
        manageAddr: [
          { required: true, message: "办理地址不能为空", trigger: "blur" }
        ],
        manageTime: [
          { required: true, message: "办理时间不能为空", trigger: "blur" }
        ],
        /* handleFlow: [
           { required: true, message: "办理流程图不能为空", trigger: "blur" }
         ],*/
        zixunType: [
          { required: true, message: "咨询方式不能为空", trigger: "blur" }
        ],
        jianduType: [
          { required: true, message: "监督方式不能为空", trigger: "blur" }
        ],
        legalLimit: [
          { required: true, message: "法定时限不能为空", trigger: "blur" }
        ],
        promiseLimit: [
          { required: true, message: "承诺时限不能为空", trigger: "blur" }
        ],
        showRzhs: [{ required: true, message: "必填项", trigger: "blur" }]
      }
    };
  },
  watch: {
    //切换所属主题分类
    "queryParams.themeOidSelect": {
      handler (val, oldVal) {
        let dataId = null != val ? val : "";
        this.queryParams.themeOid = dataId;
      }
    },
    //区划加载
    "form.districtOid": {
      handler (newVal, oldVal) {
        if (oldVal) {
          if (newVal !== oldVal) {
            this.assistOrganOids = null;
            this.form.mainOrganOid = null;
            this.form.assistOrganOid = [];
          }
        }
        this.getOrganTree(newVal);
        this.getAssisOrganTree(newVal, this.assistOrganOids);
      }
    },
    "roleForm.serviceOids": function (val) {
      if (this.roleForm.serviceOids && this.roleForm.serviceOids > 0) {
        this.roleForm.alidRoleOids = this.roleForm.serviceOids[0];
      }
    }
  },

  computed: {
    // 计算属性的 getter
    reversedCountToScence: function () {
      if (this.form.countToScence == 0) {
        return "0次";
      } else if (this.form.countToScence == 1) {
        return "1次";
      } else if (this.form.countToScence == 2) {
        return "2次";
      } else if (this.form.countToScence == 3) {
        return "多次";
      }
      return "";
    },
    reversedExpressFlag: function () {
      if (this.form.expressFlag == 0) {
        return "否";
      } else if (this.form.expressFlag == 1) {
        return "是";
      }
      return "";
    },
    reversedOnlinePayFlag: function () {
      if (this.form.onlinePayFlag == 0) {
        return "否";
      } else if (this.form.onlinePayFlag == 1) {
        return "是";
      }
      return "";
    },
    reversedHandleForm: function () {
      if (this.form.handleForm) {
        if (this.form.handleForm == "0") {
          return "窗口办理";
        } else if (this.form.handleForm == "1") {
          return "网上办理";
        } else if (this.form.handleForm == "2") {
          return "一体化办理";
        }
      }
      return "";
    },
    reversedAppointmentFlag: function () {
      if (this.form.appointmentFlag == 0) {
        return "否";
      } else if (this.form.appointmentFlag == 1) {
        return "是";
      }
      return "";
    },
    reversedUnionOrganFlag: function () {
      if (this.form.unionOrganFlag == 0) {
        return "否";
      } else if (this.form.unionOrganFlag == 1) {
        return "是";
      }
      return "";
    },
    reversedIsZjfw: function () {
      if (this.form.isZjfw == 0) {
        return "否";
      } else if (this.form.isZjfw == 1) {
        return "是";
      }
      return "";
    },
    reversedIfCharge: function () {
      if (this.form.ifCharge == 0) {
        return "否";
      } else if (this.form.ifCharge == 1) {
        return "是";
      }
      return "";
    }
  },
  created () {
    this.getList();
    //分类树
    this.getThemeTree();
    //区划树
    this.getDistrictTree();
    queryServiceTree().then(res => {
      this.appRoleOptions = res.data;
    });
    //服务对象
    getComboServiceObject().then(response => {
      this.comboServiceObjects = response.data; //[{"key":1,"value":"自然人"},{"key":2,"value":"法人"}];
    });
  },
  methods: {

    comboDirectoryCopy (row) {
      this.comboDirectoryOid = row.comboDirectoryOid;
      this.copyFlag = true;
    },
    //关闭一键复制页面
    copyClose () { this.copyFlag = false; this.getList(); },
    comboDirectoryClose () {
      this.openView = false;
    },
    getServiceOids (val) {
      if (val != "") {
      }
    },
    /** 选择图标 */
    handleIconSelect () {
      this.openIconSelectView = true;
    },
    //从子组件中获取选择的图标
    getSelectClass (className) {
      this.form.directoryIcon = className;
      this.openIconSelectView = false;
    },
    selectXb (val) {
      if (this.form.direType == 1) {
        this.ifDis = true;
        // this.getAssisOrganTree(this.form.districtOid,this.form.mainOrganOid);
        let oids = [];
        let assistOrganOids = val.id;
        if (assistOrganOids) {
          let organOids =
            assistOrganOids != "" ? assistOrganOids.split(",") : [];
          for (let oid of organOids) {
            if (oid != "") {
              oids.push(oid);
            }
          }
          this.form.assistOrganOid = oids;
        }
      }
    },
    handleRowChange (data) { },
    /** 查询目录列表 */
    getList () {
      this.loading = true;
      pagemulu(this.queryParams).then(response => {
        this.comboDirectoryList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /**获取分类树**/
    getThemeTree (themeOid) {
      queryComboThemeSimpleTree(themeOid).then(response => {
        this.themeOptions = response.data;
      });
    },
    /** 获取区划树 */
    getDistrictTree (districtOid) {
      if (districtOid) {
        districtOid = districtOid.replace("DISTRICT-", "");
      }
      this.queryDistrictSimpleTree(districtOid).then(response => {
        this.districtOptions = response.data;
      });
    },
    /**获取主办部门树**/
    getOrganTree (districtOid) {
      if (districtOid) {
        districtOid = districtOid.replace("DISTRICT-", "");
        this.queryOrganTree(districtOid).then(response => {
          this.mainOrganOptions = response.data;
        });
      } else {
        this.mainOrganOptions = [];
        this.form.mainOrganOid = null;
      }
    },
    /** 获取协办部门 **/
    getAssisOrganTree (districtOid, assistOrganOids) {
      if (districtOid) {
        districtOid = districtOid.replace("DISTRICT-", "");
        let oids = [];
        if (assistOrganOids) {
          let organOids =
            assistOrganOids != "" ? assistOrganOids.split(",") : [];
          for (let oid of organOids) {
            if (oid != "") {
              oids.push(oid);
            }
          }
          this.form.assistOrganOid = oids;
        }
        //区划部门树
        this.queryOrganTree(districtOid).then(response => {
          this.assistOrganOptions = response.data;
          let _that = this;
          if (this.form.direType == 4) {
            //跨层级业务协同 （可以选择上级区划下部门）
            queryDistrictAndOrganTree(districtOid, _that.form.direType).then(
              responseForParentOid => {
                _that.assistOrganOptions = responseForParentOid.data;
              }
            );
          } else if (_that.form.direType == 5) {
            //跨区域业务协同（不能选择上级区划下部门）
            queryDistrictAndOrganTree(districtOid, _that.form.direType).then(
              responseForParentOid => {
                _that.assistOrganOptions = responseForParentOid.data;
              }
            );
          }
        });
      } else {
        this.assistOrganOptions = [];
        this.form.assistOrganOid = null;
      }
    },
    //选择配置项附件
    selectAttas () {
      this.getAttaList();
      this.openAttaListView = true;
    },
    /** 查询附件列表 */
    getAttaList () {
      pageFile(this.attaQueryParams).then(response => {
        this.attaList = response.data.data;
        this.attatotal = response.data.total;
      });
    },
    //选中附件
    selectFile (attaOid, attaName) {
      this.form.handleFlow = attaOid;
      this.form.handleFlowName = attaName;
      this.openAttaListView = false;
      this.handleFlowFlag = true;
    },
    //下载附件
    downloadFile (attaOid) {
      this.loading = true;
      downloadFile(attaOid);
      this.loading = false;
    },
    //预览附件
    viewFile (attaOid) {
      let item = { show: true, attaOid: attaOid };
      this.viewDialogOptions.push(item);
    },
    //关闭预览附件
    closeFileView () {
      this.viewDialogOptions.pop();
    },
    //清空附件
    clearAtta () {
      this.form.handleFlow = "";
      this.form.handleFlowName = "";
      this.handleFlowFlag = false;
    },
    //成功后返回
    uploadSuccess (resp) {
      this.fileList = [];
      if (200 !== resp.code) {
        return this.msgError(resp.message);
      }
      this.getAttaList();
    },
    //失败后返回
    uploadError (resp) {
      this.msgError("文件上传失败");
    },
    uploadUrl () {
      return uploadFile();
    },
    //上传之前
    beforeUpload (file) {
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
      const isLt2M = file.size / 1024 / 1024 < 10;
      if (!isLt2M) {
        this.msgError("上传文件大小不能超过 10MB！");
      }
      return isLt2M;
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset () {
      this.form = {
        comboServiceObject: "1",
        ifCharge: 0,
        isZjfw: 0,
        appointmentFlag: 0,
        handleForm: "0",
        onlinePayFlag: 0,
        expressFlag: 0,
        unionOrganFlag: 0,
        countToScence: 0,
        showRzhs: 0,
        mainOrganOid: null,
        assistOrganOid: []
      };
      this.elements = ["1"];
      this.assistOrganOptions = [];
      this.form.assistOrganOid = null;
      this.assistOrganOids = null;
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.queryParams.themeOid = null;
      this.queryParams.themeOidSelect = null;
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.comboDirectoryOidView = row.comboDirectoryOid;
      this.openView = true;
      this.title = "查看目录信息";
    },
    /** 新增和修改按钮操作 */
    handleInit: function (row) {
      this.ifDis = false;
      let _that = this;
      this.comboDirectoryOid = row.comboDirectoryOid;
      this.activeName = 'first';

      _that.reset();

      let oid = row.comboDirectoryOid;
      let oids = [];
      if (!oid) {
        _that.getDistrictTree();
      }

      if (oid) {
        // _that.getDistrictTree(row.districtOid);
        init(oid).then(response => {
          if (response.data.comboDirectory != undefined) {
            this.form = response.data.comboDirectory;
            let serviceOidArr = this.form.serviceOids
              ? this.form.serviceOids.split(",")
              : [];
            for (let service of serviceOidArr) {
              oids.push(service);
            }
            this.roleForm.serviceOids = oids;
            this.assistOrganOids = response.data.comboDirectory.assistOrganOid;
            if (
              null != response.data.comboDirectory.handleFlow &&
              "" != response.data.comboDirectory.handleFlow
            ) {
              this.handleFlowFlag = true;
            }
            if (response.data.comboDirectory.direType == 1) {
              this.ifDis = true;
            }
            // this.getDireType(response.data.comboDirectory.direType);
            //初始化服务终端要素
            let elements = response.data.comboDirectory.elements;
            if (elements && elements.length > 0) {
              let split = elements.split(",");
              this.elements = [];
              split.forEach(element => {
                this.elements.push(element);
              });
            }
          } else {
            this.assistOrganOids = [];
            this.handleFlowFlag = false;
          }
        });
      }
      _that.openInit = true;
      _that.dialogTitle = oid ? "修改目录" : "新增目录";
      this.queryUserTree();
    },
    /** 提交按钮 */
    submitForm: function () {
      if (this.form.legalLimit < this.form.promiseLimit) {
        this.msgError("法定时限应不小于承诺时限");
        return;
      }
      this.form.serviceOids = "";
      if (this.roleForm.serviceOids) {
        this.roleForm.serviceOids.forEach(ser => {
          this.form.serviceOids += ser + ",";
        });
      }
      if (this.form.serviceOids != "") {
        this.form.serviceOids = this.form.serviceOids.slice(
          0,
          this.form.serviceOids.length - 1
        );
      } else {
        this.form.servicextlx = "";
        this.form.servicespxt = "";
        this.form.serviceApplyAddr = "";
      }
      if (!this.form.directoryIcon) {
        this.msgError("目录图标不能为空!");
        return false;
      }
      if (!this.form.handleFlow) {
        this.msgError("办事流程图不能为空!");
        return false;
      }
      let usersOid = this.$refs.userTree.getCheckedNodes().find(i => i.identity === 'USER')?.id || '';
      this.form.consultUserOid = usersOid;
      if (!this.form.consultUserOid) {
        this.$message.error("请选择咨询用户信息!");
        return false;
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (
            undefined != this.form.assistOrganOid &&
            this.form.assistOrganOid.length > 0
          ) {
            let oid = "";
            for (let i = 0; i < this.form.assistOrganOid.length; i++) {
              oid += this.form.assistOrganOid[i] + ",";
            }
            this.form.assistOrganOid = oid;
          }
          //处理服务终端要素信息
          this.form.elements = this.elements.join(",");
          // 判断前置核验是否存在空数据
          const message = this.$refs.preVerification.validate();
          if (message) {
            return this.$message.error(message);
          }
          if (this.$refs.preVerification.tableData != null) {
            saveOrUpdateList(this.$refs.preVerification.tableData);
          }
          save(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.openInit = false;
              this.getList();
            }
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const oid = row.id;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return del(oid);
        })
        .then(() => {
          this.msgSuccess("删除成功");
          this.getList();
        })
        .catch(function () { });
    },

    //事项关联
    queryServiceList (row) {
      let item = { show: true, comboDirectoryOid: row.comboDirectoryOid };
      this.comboDialogOptions.push(item);
    },
    //目录材料整合
    queryMaterialList (row) {
      let item = { show: true, comboDirectoryOid: row.comboDirectoryOid };
      this.materialDialogOptions.push(item);
    },
    //目录证照
    queryResultList (row) {
      let item = { show: true, comboDirectoryOid: row.comboDirectoryOid };
      this.resultDialogOptions.push(item);
    },
    // 关闭按钮
    closeUserView () {
      this.comboDialogOptions.pop();
    },
    // 关闭按钮
    closeMaterView () {
      this.materialDialogOptions.pop();
    },
    // 关闭按钮
    closeResultView () {
      this.resultDialogOptions.pop();
    },
    //发布目录
    publishDire (row) {
      const oid = row.id;
      const comboDirectoryOid = row.comboDirectoryOid;
      //验证是否存在未确认的公共材料获取证照
      verifyUnconfirmedFlag(comboDirectoryOid).then(response => {
        if (response.data == "1") {
          this.msgWarning("存在未确认的公共材料，请先确认！");
          return false;
        }
        if (response.data == "2") {
          this.msgWarning("存在未确认的统一证照，请先确认！");
          return false;
        }
        if (response.data == "0") {
          this.$confirm("是否确认发布?", "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
            .then(function () { })
            .then(function () {
              return publishDire(oid);
            })
            .then(() => {
              this.msgSuccess("发布成功");
              this.getList();
            })
            .catch(function () { });
        }
      });
    },
    //取消发布
    publishCancel (row) {
      const oid = row.id;
      this.$confirm("是否确认取消发布?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return publishCancel(oid);
        })
        .then(() => {
          this.msgSuccess("已取消发布");
          this.getList();
        })
        .catch(function () { });
    },
    getPublishName (val) {
      if (val.status == 0 || val.status == 4) {
        return "未发布";
      } else if (val.status == 1) {
        return "已发布";
      } else {
        return "";
      }
    },
    /** 根据不同事项大类、获取协办部门 **/
    getDireType (val) {
      if (val === 1) {
        //单部门
        this.ifDis = true;
        this.getAssisOrganTree(this.form.districtOid, this.form.mainOrganOid);
      } else {
        //跨部门、政府服务、跨层级、跨区域
        this.ifDis = false;
        this.form.assistOrganOid = null;
        this.assistOrganOptions = [];
        this.getAssisOrganTree(this.form.districtOid);
      }
    },

    queryUserTree () {
      this.$getResponse(queryUserTree(), (error, res) => {
        if (error || res.code !== 200) return;
        this.userTreeData = res.data || [];
        // 扁平化数组
        this.userTreeDataFlat = this.flatTree(this.userTreeData);
        // 默认展开第一级
        this.userExpandedKeys.push(this.userTreeData?.[0]?.id);
        // 回显树形结构
        this.handleTreeExtend();
      })
    },

    flatTree (data, arr = []) {
      data.forEach(item => {
        arr.push({ ...item, children: [] });
        if (item.children) {
          this.flatTree(item.children, arr);
        }
      })
      return arr;
    },

    findId (parentId, arr = []) {
      let target = this.userTreeDataFlat.find(p => p.id === parentId);
      if (target.parentId) {
        arr.push(target.parentId);
        this.findId(target.parentId, arr);
      } else {
        arr.push(target.id);
      }
      return arr;
    },

    handleUserTreeCheck (data) {
      let arr = [data.id];
      if (data.parentId) {
        arr = this.findId(data.parentId, [...arr, data.parentId]);
      }
      // 找到父级id
      this.$refs.userTree.setCheckedKeys(arr);
    },

    handleTreeExtend () {
      let consultUserOid = this.form.consultUserOid;
      if (consultUserOid) {
        let target = this.userTreeDataFlat.find(item => item.id === consultUserOid);
        let arr = [consultUserOid];
        if (target.parentId) {
          arr = this.findId(target.parentId, [...arr, target.parentId]);
        }
        this.userExpandedKeys = arr;
        this.userCheckedKeys = arr;
      }
    }
  },
};
</script>
<style lang="scss" scoped>
.treeselect {
  width: 200px;
}
.treeselect240 {
  width: 240px;
}
table {
  border-collapse: collapse;
}
</style>
