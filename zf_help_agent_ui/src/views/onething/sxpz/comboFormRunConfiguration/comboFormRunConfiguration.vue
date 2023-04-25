/**
* @Author: wangns
*/

<template>
  <div class="app-container">
    <!--普通事项-->
    <el-row :gutter="20" v-show="str==='first'" >
      <!--数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParamsForCombo" ref="queryFormForCombo" :inline="true" label-width="108px">
          <el-form-item label="目录名称" prop="comboDirectoryName">
            <el-input v-model.trim="queryParamsForCombo.comboDirectoryName" placeholder="请输入目录名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="编码" prop="comboDirectoryCode">
            <el-input v-model.trim="queryParamsForCombo.comboDirectoryCode" placeholder="请输入编码" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery('tcQuery')">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery('tcQuery')" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>

        <el-table  v-loading="loading" :data="comboDirectories" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="所属区划" align="center" width="110" prop="districtName" :show-overflow-tooltip="true"/>
          <el-table-column label="主办部门" align="center"  prop="mainOrganName" :show-overflow-tooltip="true"/>
          <el-table-column label="目录名称"  align="center" prop="comboDirectoryName" :show-overflow-tooltip="true"/>
          <el-table-column label="编码"  align="center" prop="comboDirectoryCode" :show-overflow-tooltip="true"/>
          <el-table-column label="状态"    align="center" :formatter="getPublishName" prop="ifPublish"  :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleViewForComboForm(scope.row.comboDirectoryOid,'true')" v-hasPermi="['sys:form:view']" >查看</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInitForComboForm(scope.row.comboDirectoryOid,'false')" v-hasPermi="['sys:form:update']">表单配置</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="comboTotal > 0" :total="comboTotal" :page.sync="queryParamsForCombo.pageNum" :limit.sync="queryParamsForCombo.pageSize" @pagination="getListForCombo"/>
      </el-col>
    </el-row>


    <!-- 修改表单配置信息对话框 -->
    <el-dialog v-dialog-drag :title="comboTitle" :visible.sync="openComboFormInit"   width="900px" append-to-body>
      <!--信息-->
      <div class="zf-zc-table--title">已配置的表单信息</div>
      <el-button icon="el-icon-plus" type="primary" @click="addFormForSxForm(null,formForComboBaseMessage.comboDirectoryOid,null,'true')"  style="margin-left: 470px;margin-bottom: 10px;" v-hasPermi="['sys:form:update']">新增表单</el-button>
      <el-table  v-loading="loading" :data="comboFormList" border>
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" width="150" prop="formUseStatusName" :show-overflow-tooltip="true"/>
        <el-table-column label="表单类型" align="center" width="150" prop="formTypeName" :show-overflow-tooltip="true"/>
        <el-table-column label="表单名称" width="300" align="center" prop="formName" :show-overflow-tooltip="true"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text"  v-if="scope.row.formUseStatus === 1" @click="handleFormUseStatus(scope.row,'true')" v-hasPermi="['sys:form:update']" >禁用</el-button>
            <el-button size="mini" type="text" v-else  @click="handleFormUseStatus(scope.row,'true')" v-hasPermi="['sys:form:update']" >启用</el-button>
           <el-button size="mini" type="text"  icon="iconfont zfsoft-xiugai"  @click="addFormForSxForm(null,formForComboBaseMessage.comboDirectoryOid,scope.row.oid,'true')" v-hasPermi="['sys:form:update']">修改</el-button>
           <!-- <el-button size="mini" type="text" v-if="scope.row.formSource === 1" icon="iconfont zfsoft-xiugai"  @click="addFormForSxForm(null,null,scope.row.oid,'false')" v-hasPermi="['sys:form:view']">查看</el-button>
            <el-button size="mini" type="text" v-else icon="iconfont zfsoft-xiugai"  @click="addFormForSxForm(null,null,scope.row.oid,'false')" v-hasPermi="['sys:form:view']">查看</el-button>-->
            <el-button size="mini" type="text" icon="iconfont zfsoft-chakan"  @click="viewFormForSxForm(scope.row.oid)" v-hasPermi="['sys:form:view']">查看</el-button>
            <el-button size="mini" type="text"v-if="scope.row.formUseStatus !== 1"  @click="delFormForSxForm(scope.row)" v-hasPermi="['sys:form:update']" >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="comboFormTotal > 0" :total="comboFormTotal" :page.sync="queryParamsForCombo.pageNumber" :limit.sync="queryParamsForCombo.pageSize" @pagination="getComboSerFormList(queryParamsForCombo.comboDirectoryOid)"/>
      <!--关联的事项信息-->
      <!-- <label ></label > -->
      <div class="zf-zc-table--title">已生效的实施清单表单</div>

      <label style="color: #bd2c00;">（ 如您希望当前中关联事项的表单需要生效的话，请您进行关联配置 ）</label>
      <el-button icon="el-icon-plus" type="primary" @click="getSerFormsOfCombo(queryParamsForCombo.comboDirectoryOid,'queryIneffective')"  style="margin-bottom: 10px;margin-top: 5px;" v-hasPermi="['sys:form:update']">新增生效表单</el-button>
      <template  v-for="sxSerFormOfComboOfTakeEffect in sxSerFormOfComboListOfTakeEffect.sxServices">
      <div style="margin-top: 11px;margin-bottom: 20px;">
        <span><i class="el-icon-document"></i>{{ sxSerFormOfComboOfTakeEffect.serviceName}}</span>
      </div>
        <el-table  style="margin-top: 10px;" v-loading="loading" :data="sxSerFormOfComboOfTakeEffect.sxSerForms" border>
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <!--<el-table-column label="状态" align="center" width="150" prop="formUseStatusName" :show-overflow-tooltip="true"/>-->
        <el-table-column label="表单类型" align="center" width="150" prop="formTypeName" :show-overflow-tooltip="true"/>
        <el-table-column label="表单名称" width="300" align="center" prop="formName" :show-overflow-tooltip="true"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="invalidFormOfComboSx(scope.row.oid,'false')" v-hasPermi="['sys:form:update']">失效</el-button>
          </template>
        </el-table-column>
      </el-table>
      </template>
      <pagination v-show="comboSerFormTitleOfTakeEffect > 0" :total="comboSerFormTitleOfTakeEffect" :page.sync="queryParamsForCombo.pageNumber" :limit.sync="queryParamsForCombo.pageSize" @pagination="getSerFormsOfCombo(queryParamsForCombo.comboDirectoryOid,null)"/>
      <div style="text-align: center;margin-top: 20px;font-size: 20px;">
        <label v-show="comboSerFormTitleOfTakeEffect ===0" >暂无已生效的实施清单表单，请点击新增生效表单按钮进行配置！</label>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button @click="openComboFormInit = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 修改表单配置信息对话框 -->
    <el-dialog v-dialog-drag :title="comboTitle" :visible.sync="openComboFormInitView"   width="900px" append-to-body>
      <!--信息-->
      <!-- <label >已配置的表单信息</label > -->
      <div class="zf-zc-table--title">已配置的表单信息</div>
      <el-table  v-loading="loading" :data="comboFormList" border class="mt20 mb20">
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" width="150" prop="formUseStatusName" :show-overflow-tooltip="true"/>
        <el-table-column label="表单类型" align="center" width="150" prop="formTypeName" :show-overflow-tooltip="true"/>
        <el-table-column label="表单名称" width="300" align="center" prop="formName" :show-overflow-tooltip="true"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="iconfont zfsoft-chakan"  @click="viewFormForSxForm(scope.row.oid)" v-hasPermi="['sys:form:view']">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="comboFormTotal > 0" :total="comboFormTotal" :page.sync="queryParamsForCombo.pageNumber" :limit.sync="queryParamsForCombo.pageSize" @pagination="getComboSerFormList(queryParamsForCombo.comboDirectoryOid)"/>
      <!--关联的事项信息-->
      <!-- <label ></label > -->
      <div class="zf-zc-table--title">已生效的实施清单表单</div>
      
      <template  v-for="sxSerFormOfComboOfTakeEffect in sxSerFormOfComboListOfTakeEffect.sxServices">
        <div style="margin-top: 11px;margin-bottom: 20px;">
          <!-- <span><i class="el-icon-document"></i>{{ sxSerFormOfComboOfTakeEffect.serviceName}}</span> -->
          <div class="zf-zc-table--title">{{ sxSerFormOfComboOfTakeEffect.serviceName}}</div>
        </div>
        <el-table  style="margin-top: 10px;" v-loading="loading" :data="sxSerFormOfComboOfTakeEffect.sxSerForms" :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center"  prop="formUseStatusName" :show-overflow-tooltip="true"/>
          <el-table-column label="表单类型" align="center" prop="formTypeName" :show-overflow-tooltip="true"/>
          <el-table-column label="表单名称"  align="center" prop="formName" :show-overflow-tooltip="true"/>
        </el-table>
      </template>
      <pagination v-show="comboSerFormTitleOfTakeEffect > 0" :total="comboSerFormTitleOfTakeEffect" :page.sync="queryParamsForCombo.pageNumber" :limit.sync="queryParamsForCombo.pageSize" @pagination="getSerFormsOfCombo(queryParamsForCombo.comboDirectoryOid,null)"/>
      <div style="text-align: center;margin-top: 20px;font-size: 20px;">
        <label v-show="comboSerFormTitleOfTakeEffect ===0" >暂无已生效的实施清单表单，请点击新增生效表单按钮进行配置！</label>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button @click="openComboFormInitView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 表单配置信息 - 查看 -->
    <el-dialog v-dialog-drag :title="sxSerFormTitle" :visible.sync="openFormEditView" width="900px"  v-if="openFormEditView" append-to-body>
      <el-form ref="formForSxSerForm" :rules="rules" :model="formForSxSerForm" label-width="0" size="mini">
        <table  cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b><i class="required">*</i>表单类型：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="formType">
                <el-radio-group style="margin-left: 20px;" v-model="formForSxSerForm.formType.toString()">
                  <el-radio label="1"   @change="eleInitForEdit('1',formForSxSerForm,'view')">电子表单配置</el-radio>
                  <el-radio label="0"   @change="customizeInitForEdit('0',formForSxSerForm,'view')">自定义表单配置</el-radio>
                </el-radio-group>
              </el-form-item>
            </td>
          </tr>
          <!--自定义表单配置 start -->
          <tr v-if="customizeShow === 'true'">
            <td>
              <b><i class="required">*</i>表单名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="formName">
                <el-col :span="24">
                  <el-input v-model.trim="formForSxSerForm.formName" placeholder="请输入表单名称" maxlength="25" show-word-limit/>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="customizeShow === 'true'">
            <td>
              <b><i class="required">*</i>表单地址：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="formAddr">
                <el-col :span="24">
                  <el-input v-model.trim="formForSxSerForm.formAddr" placeholder="请输入表单地址" maxlength="100" show-word-limit/>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <!--自定义表单配置 end -->

          <!--电子表单配置 start -->
          <tr v-if="eleShow === 'true'">
            <td>
              <b><i class="required">*</i>表单名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="formName">
                <el-input v-model.trim="formForSxSerForm.formName" placeholder="请输入表单名称"  maxlength="25" show-word-limit/>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="eleShow === 'true'">
            <td>
              <b>是否必填：</b>
            </td>
            <td colspan="3">
              <el-radio-group style="margin-left: 20px;" v-model="formForSxSerForm.isFormFlag.toString()">
                <el-radio label="1"   @change="isFormFlagInitForEdit('1',formForSxSerForm,'edit','view')">是</el-radio>
                <el-radio label="0"   @change="isFormFlagInitForEdit('0',formForSxSerForm,'edit','view')">否</el-radio>
              </el-radio-group>
            </td>
          </tr>

          <tr v-if="eleShow === 'true'">
            <td>
              <b><i class="required">*</i>表单编码：</b>
            </td>
            <td colspan="2">
              <el-form-item prop="formCode">
                <el-input v-model.trim="formForSxSerForm.formCode" placeholder="请输入表单编码" maxlength="32" show-word-limit/>
              </el-form-item>

            </td>
            <td>
            </td>
          </tr>

          <tr v-if="eleShow === 'true'">
            <td>
              <b><i class="required">*</i>表单说明：</b>
            </td>
            <td colspan="3">
              <el-row>
                <el-col :span="24">
                  <el-form-item prop="formText">
                    <el-input type="textarea" :rows="8" v-model.trim="formForSxSerForm.formText" placeholder="请输入表单说明" maxlength="500" show-word-limit/>
                  </el-form-item>
                </el-col>
              </el-row>
            </td>
          </tr>
          <!--电子表单配置 start -->
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openFormEditView = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!--已配置的事项表单 查看-->
      <el-dialog v-dialog-drag :title="title" :visible.sync="openForSxSerView" width="900px" append-to-body>
        <div>
      <el-form ref="viewForSxSerForm"  :model="viewForSxSerForm" label-width="0" size="mini" >
        <table  cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>表单类型：</b>
            </td>
            <td colspan="3">
                <span v-if="viewForSxSerForm.formType == 1">电子表单配置</span>
                <span v-else-if="viewForSxSerForm.formType == 0">自定义表单配置</span>
            </td>
          </tr>
          <tr v-if="viewForSxSerForm.formType === 0">
            <td>
              <b>表单名称：</b>
            </td>
            <td colspan="3">
                {{viewForSxSerForm.formName}}
            </td>
          </tr>
          <tr v-if="viewForSxSerForm.formType === 0">
            <td>
              <b>表单地址：</b>
            </td>
            <td colspan="3">
                {{viewForSxSerForm.formAddr}}
            </td>
          </tr>
          <!--自定义表单配置 end -->
          <!--电子表单配置 start -->
          <tr v-if="viewForSxSerForm.formType === 1">
            <td>
              <b>表单名称：</b>
            </td>
            <td colspan="3">
                {{viewForSxSerForm.formName}}
            </td>
          </tr>
          <tr v-if="viewForSxSerForm.formType === 1">
            <td>
              <b>是否必填：</b>
            </td>
            <td colspan="3">
              <span v-if="viewForSxSerForm.isFormFlag == 0">否</span>
              <span v-if="viewForSxSerForm.isFormFlag == 1">是</span>
            </td>
          </tr>

          <tr v-if="viewForSxSerForm.formType === 1">
            <td>
              <b>表单编码：</b>
            </td>
            <td colspan="3">
                {{viewForSxSerForm.formCode}}
            </td>
            <td>
            </td>
          </tr>

          <tr v-if="viewForSxSerForm.formType === 1">
            <td>
              <b>表单说明：</b>
            </td>
            <td colspan="3">
                {{viewForSxSerForm.formText}}
            </td>
          </tr>
          <tr>
            <td>
              <b>表单样本：</b>
            </td>
            <td colspan="3">
              {{viewForSxSerForm.attaName}} &nbsp;&nbsp;&nbsp;&nbsp;
              <span v-if="viewForSxSerForm.simpleAtta != '' && viewForSxSerForm.simpleAtta != null" @click="downloadFile(viewForSxSerForm.simpleAtta)">下载 </span>
            </td>
          </tr>
          <!--电子表单配置 start -->
        </table>
      </el-form>
        </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openForSxSerView=false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 表单配置信息 - 修改 -->
    <el-dialog v-dialog-drag :title="sxSerFormTitle" :visible.sync="openFormEdit" width="900px" v-if="openFormEdit" append-to-body>
      <el-form ref="formForSxSerForm" :rules="rules" :model="formForSxSerForm" label-width="0" size="mini" >
        <table  cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b><i class="required">*</i>表单类型：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="formType">
                <el-radio-group style="margin-left: 20px;" v-model="formForSxSerForm.formType.toString()">
                    <el-radio label="1"   @change="eleInitForEdit('1',formForSxSerForm)">电子表单配置</el-radio>
                    <el-radio label="0"   @change="customizeInitForEdit('0',formForSxSerForm)">自定义表单配置</el-radio>
                </el-radio-group>
              </el-form-item>
            </td>
          </tr>
          <!--自定义表单配置 start -->
          <tr v-if="customizeShow === 'true'">
            <td>
              <b><i class="required">*</i>表单名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="formName">
                <el-col :span="24">
                  <el-input v-model.trim="formForSxSerForm.formName" placeholder="请输入表单名称" maxlength="25" show-word-limit/>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="customizeShow === 'true'">
            <td>
              <b><i class="required">*</i>表单地址：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="formAddr">
                <el-col :span="24">
                  <el-input v-model.trim="formForSxSerForm.formAddr" placeholder="请输入表单地址" maxlength="100" show-word-limit/>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <!--自定义表单配置 end -->

          <!--电子表单配置 start -->
          <tr v-if="eleShow === 'true'">
            <td>
              <b><i class="required">*</i>表单名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="formName">
                <el-input v-model.trim="formForSxSerForm.formName" placeholder="请输入表单名称"  maxlength="25" show-word-limit/>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="eleShow === 'true'">
            <td>
              <b>是否必填：</b>
            </td>
            <td colspan="3">
              <el-radio-group style="margin-left: 20px;" v-model="formForSxSerForm.isFormFlag.toString()">
                <el-radio label="1"   @change="isFormFlagInitForEdit('1',formForSxSerForm,'edit')">是</el-radio>
                <el-radio label="0"   @change="isFormFlagInitForEdit('0',formForSxSerForm,'edit')">否</el-radio>
              </el-radio-group>
            </td>
          </tr>

          <tr v-if="eleShow === 'true'">
            <td>
              <b><i class="required">*</i>表单编码：</b>
            </td>
            <td colspan="2">
              <el-form-item prop="formCode">
                <el-input v-model.trim="formForSxSerForm.formCode" placeholder="请输入表单编码" maxlength="32" show-word-limit/>
              </el-form-item>

            </td>
            <td>
              <el-button icon="el-icon-edit-outline" v-if="isFormUse ==='true'" type="primary" @click="linkOutsideUrl" size="20px" style="width: 200px;" v-hasPermi="['sys:form:update']">设计填报表单</el-button>
            </td>
          </tr>

          <tr v-if="eleShow === 'true'">
            <td>
              <b><i class="required">*</i>表单说明：</b>
            </td>
            <td colspan="3">
              <el-row>
                <el-col :span="24">
                  <el-form-item prop="formText">
                    <el-input type="textarea" :rows="8" v-model.trim="formForSxSerForm.formText" placeholder="请输入表单说明" maxlength="1000" show-word-limit/>
                  </el-form-item>
                </el-col>
              </el-row>
            </td>
          </tr>

          <!--电子表单配置 start -->

        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :disabled="isDisable ==='false'"  @click="submitRole('formForSxSerForm')">确 定</el-button>
        <el-button @click="openFormEdit = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 表单配置信息 -->
    <el-dialog v-dialog-drag :title="sxSerFormTitle" :visible.sync="openFormAdd" width="900px" v-if="openFormAdd" append-to-body>
      <el-form ref="formForSxSerFormForAdd" :model="formForSxSerFormForAdd" :rules="rules" label-width="0" size="mini" >
        <table  cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b><i class="required">*</i>表单类型：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="formType">
                <el-radio-group style="margin-left: 20px;" v-model="formForSxSerFormForAdd.formType">
                  <el-radio label="1"  @change="eleInit('1',formForSxSerFormForAdd)">电子表单配置</el-radio>
                  <el-radio label="0"  @change="customizeInit('0',formForSxSerFormForAdd)">自定义表单配置</el-radio>
                </el-radio-group>
                <!--                  <el-button v-show="eleShow === 'true'" @click="handleFormInitForSxForm" type="primary" size="20px" style="width: 118px;margin-left: 116px;">设计电子表单</el-button>-->
              </el-form-item>
            </td>
          </tr>
          <!--自定义表单配置 start -->
          <tr v-if="customizeShow === 'true'">
            <td>
              <b><i class="required">*</i>表单名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="formName">
                <el-col :span="24">
                  <el-input v-model.trim="formForSxSerFormForAdd.formName" placeholder="请输入表单名称" maxlength="25" show-word-limit/>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="customizeShow === 'true'">
            <td>
              <b><i class="required">*</i>表单地址：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="formAddr">
                <el-col :span="24">
                  <el-input v-model.trim="formForSxSerFormForAdd.formAddr" placeholder="请输入表单地址" maxlength="100" show-word-limit/>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <!--自定义表单配置 end -->

          <!--电子表单配置 start -->
          <tr v-if="eleShow === 'true'">
            <td>
              <b><i class="required">*</i>表单名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="formName">
                <el-input v-model.trim="formForSxSerFormForAdd.formName" placeholder="请输入表单名称"  maxlength="25" show-word-limit/>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="eleShow === 'true'">
            <td>
              <b>是否必填：</b>
            </td>
            <td colspan="3">
              <el-radio-group style="margin-left: 20px;" v-model="formForSxSerFormForAdd.isFormFlag.toString()">
                <el-radio label="1"   @change="isFormFlagInitForEdit('1',formForSxSerFormForAdd,'add')">是</el-radio>
                <el-radio label="0"   @change="isFormFlagInitForEdit('0',formForSxSerFormForAdd,'add')">否</el-radio>
              </el-radio-group>
            </td>
          </tr>

          <tr v-if="eleShow === 'true'">
            <td>
              <b><i class="required">*</i>表单编码：</b>
            </td>
            <td colspan="2">
              <el-form-item prop="formCode">
                <el-input v-model.trim="formForSxSerFormForAdd.formCode" placeholder="请输入表单编码" maxlength="32" show-word-limit/>
              </el-form-item>

            </td>
            <td>
              <el-button icon="el-icon-edit-outline" v-if="isFormUse ==='true'" type="primary" @click="linkOutsideUrl" size="20px" style="width: 200px;" v-hasPermi="['sys:form:update']">设计填报表单</el-button>
            </td>
          </tr>

          <tr v-if="eleShow === 'true'">
            <td>
              <b><i class="required">*</i>表单说明：</b>
            </td>
            <td colspan="3">
              <el-row>
                <el-col :span="24">
                  <el-form-item prop="formText">
                    <el-input type="textarea" :rows="8" v-model.trim="formForSxSerFormForAdd.formText" placeholder="请输入表单说明" maxlength="1000" show-word-limit/>
                  </el-form-item>
                </el-col>
              </el-row>
            </td>
          </tr>
          <tr>
            <td><b>表单样本：</b></td>
            <td colspan="3">
              <el-form-item prop="iconName">
                <el-upload
                  class="upload-demo"
                  :showFileList="false"
                  :action="uploadUrl()"
                  :on-error="uploadError"
                  :before-upload="beforeUpload"
                  :on-success="uploadSuccess">
                  <el-button type="primary" size="small">上传附件<i class="el-icon-upload el-icon--right"></i></el-button>
                </el-upload>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="messageFileList && messageFileList.oid">
            <td><b>附件：</b></td>
            <td colspan="3">
                <el-col>
                  {{messageFileList.name}}
                  <span @click="downloadFile(messageFileList.oid)">下载 </span> &nbsp;&nbsp;&nbsp;&nbsp;
                  <span @click="handleAttaDelete">删除 </span>
                </el-col>
            </td>
          </tr>
          <!--电子表单配置 start -->
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" :disabled="isDisable ==='false'" @click="submitRole('formForSxSerFormForAdd')">确 定</el-button>
        <el-button @click="openFormAdd = false;">关 闭</el-button>
      </div>
    </el-dialog>

    <!--下所有事项的关联表单列表  -  未生效-->
    <el-dialog v-dialog-drag :title="sxSerFormOfComboTitleOfIneffective" :visible.sync="openSxSerFormOfComboOfIneffective" width="900px" v-if="openSxSerFormOfComboOfIneffective" append-to-body>
      <template  v-for="sxSerFormOfComboOfIneffective in sxSerFormOfComboListOfIneffective.sxServices">
        <div style="margin-top: 11px;margin-bottom: 20px;">
          <!-- <span><i class="el-icon-document"></i>{{ sxSerFormOfComboOfIneffective.serviceName}}</span> -->
          <div class="zf-zc-table--title">{{ sxSerFormOfComboOfIneffective.serviceName}}</div>
        </div>
        <el-table  ref="multipleTable"  :data="sxSerFormOfComboOfIneffective.sxSerForms" tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55"> </el-table-column>
          <el-table-column label="序号" width="55">
            <template slot-scope="scope"><span>{{ scope.$index + 1 }}</span></template>
          </el-table-column>
          <el-table-column prop="formUseStatusName" label="状态" align="center" width="170" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column prop="formTypeName" label="表单类型" align="center" width="170" :show-overflow-tooltip="true"> </el-table-column>
          <el-table-column prop="formName" align="center" label="表单名称"  :show-overflow-tooltip="true"> </el-table-column>
        </el-table>
      </template>
      <pagination v-show="comboSerFormTitleOfIneffective > 0" :total="comboSerFormTitleOfIneffective" :page.sync="queryParamsForCombo.pageNumber" :limit.sync="queryParamsForCombo.pageSize" @pagination="getSerFormsOfCombo(queryParamsForCombo.comboDirectoryOid,null)"/>
      <div style="text-align: center;">
        <label v-show="comboSerFormTitleOfIneffective ===0" >暂无可选的表单数据，请先配置实施清单的关联表单！</label>
      </div>
      <div slot="footer" class="dialog-footer" >
        <el-button type="primary" v-show="comboSerFormTitleOfIneffective > 0" @click="confirmSelection()">确认选择</el-button>
<!--        <el-button type="danger" v-show="comboSerFormTitleOfIneffective > 0" @click="toggleSelection()">取消选择</el-button>-->
        <el-button @click="openSxSerFormOfComboOfIneffective = false">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>


<script>
import { save,getSerFormList,getSxSerFormByOid,sxSerFormAble,getComboFormList,getSerFormsOfCombo,
  saveSxSerFormByList,updateSxSerForm,getSxServiceOne,comboSxSerFormDel} from "@/api/onething/comboFormRunConfiguration/comboFormRunConfiguration";
import {pageOfCombo,getComboOne} from "@/api/onething/comboAuthorize/comboRegisterAuthorize";
import {uploadFile} from "@/api/sys/atta";
// import the styles
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
export default {
  name: "ComboFormRunConfiguration",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 事项总条数
      sxTotal: 0,
      // 事项应用表格数据
      sxSerFormTotal: 0,
      //已配置的事项表单数据
      formList: [],
      messageFileList:{},

      //总条数
      comboTotal: 0,
      //关联的表单总条数
      comboFormList: [],
      //应用表格数据- 关联的表单
      comboFormTotal: 0,
      //应用表格数据
      districtList: [],
      //已配置的表单数据
      comboDirectories: [],
      //关联的事项信息
      sxOfComboList:[],
      //表单配置弹出层标题
      comboTitle: "",
      title:"",
      //下所有事项的关联表单列表总数 - 已生效
      comboSerFormTitleOfTakeEffect: 0,
      //下所有事项的关联表单列表总数 - 未生效
      comboSerFormTitleOfIneffective: 0,
      //表单配置弹出层
      openComboFormInit: false,
      openComboFormInitView: false,
      //下所有事项的关联表单列表dialog - 未生效
      openSxSerFormOfComboOfIneffective:false,
      //下所有事项的关联表单列表dialog - 已生效
      openSxSerFormOfComboOfTakeEffect:false,
      //下所有事项的关联表单列表dialog标题 - - 未生效
      sxSerFormOfComboTitleOfIneffective:"",
      //下所有事项的关联表单列表 - 未生效
      sxSerFormOfComboListOfIneffective:[],
      //下所有事项的关联表单列表 - 已生效
      sxSerFormOfComboListOfTakeEffect:[],

      // 事项表单配置弹出层标题
      sxTitle: "",
      //事项关联的表单弹出层标题
      sxSerFormTitle: "",
      // 事项表单修改显示弹出层
      openSxFormInit: false,
      // 事项表单查看显示弹出层
      openSxFormView: false,
      // 查看显示弹出层
      openView: false,
      //配置表单修改页面弹出层
      openFormEdit: false,
      openFormEditView: false,

      //配置表单查看页面弹出层
      openForSxSerView: false,
      viewForSxSerForm: false,
      //配置表单添加页面弹出层
      openFormAdd: false,

      // 事项查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceName: "",
        serviceOid: "",
        implementCode:"",
        serviceStatus:"3",
        existChildItem:0

      },
      //查询参数
      queryParamsForCombo: {
        pageNum:1,
        pageNumber: 1,
        pageSize: 10,
        comboDirectoryName: "",
        comboDirectoryOid: "",
        comboDirectoryCode:"",
        serviceOid:"",
        status:"1"

      },
      // 事项基本信息
      formForSxBaseMessage: {},
      //基本信息
      formForComboBaseMessage: {},
      // 表单参数
      formForSxSerForm: {},
      formForSxSerFormForAdd: {},
      form: {},

      // 事项表单校验
      rules: {
        formName: [
          { required: true, message: "表单名称不能为空", trigger: "blur" }
        ],
        formCode: [
          { required: true, message: "表单编码不能为空", trigger: "blur" }
        ],
        formText: [
          { required: true, message: "表单说明不能为空", trigger: "blur" },
        ],
        formType: [
          { required: true, message: "表单类型不能为空", trigger: "blur" },
        ],
        formAddr: [
          { required: true, message: "表单地址不能为空", trigger: "blur" },
        ],},
      rulesForCombo: {

      },
      //默认第一个选项卡
      activeName: "first",
      str:"first",
      isFormFlag:"1",
      //表单类型  1-电子表单  0-自定义表单   默认 电子表单
      formType:"1",
      //板件是否交换  1-是  0-否  默认 否
      exchangeFlag:"0",
      //电子表单显示 true - 电子  false - 自定义
      eleShow:'true',
      //自定义表单展示 true - 电子  false - 自定义
      customizeShow:'false',
      //标记设计表单按钮是否可用
      isFormUse:'true',
      //防止表单重复提交
      isDisable :'true',
      //多选数据集
      multipleSelection:[],
    };
  },
  created() {
    this.getListForCombo();
  },
  methods: {
    /** 查询列表*/
    getListForCombo() {
      this.loading = true;
      pageOfCombo(this.queryParamsForCombo).then(response => {
        this.comboDirectories = response.data.data;
        this.comboTotal = response.data.total;
        this.loading = false;
      });
    },

    /** 查询事项表单联系列表 */
    getSerFormList(serviceOid) {
      this.loading = true;
      this.queryParams.serviceOid = serviceOid;
      getSerFormList(serviceOid,this.queryParams.pageNum,this.queryParams.pageSize).then(response => {
        this.formList = response.data.data;
        this.sxSerFormTotal = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.openSxFormInit = false;
    },
    /** 搜索按钮操作 */
    handleQuery(flag) {
        this.queryParamsForCombo.pageNumber = 1;
        this.getListForCombo();
    },

    resetForm(formName) {
      this.$refs[formName].resetFields();
    },

    /** 重置按钮操作 */
    resetQuery(flag) {
      if(flag === 'sxQuery'){
        this.resetForm('queryForm');
        this.handleQuery(flag);
      }else{
        this.resetForm('queryFormForCombo');
        this.handleQuery(flag);
      }
    },

    //已配置的事项表单查看
    viewFormForSxForm(sxFormOid){
      getSxSerFormByOid(sxFormOid).then(response => {
        this.viewForSxSerForm = response.data;
        this.openForSxSerView  = true;
        this.title = "查看表单配置信息";
      });
    },


    /** 操作按钮操作 - - 普通事项或表单*/
    addFormForSxForm(serviceOid,comboDirectoryOid,sxFormOid,flag) {
      let _that = this;
      _that.messageFileList={};

      if(serviceOid || comboDirectoryOid){
        if(sxFormOid){
          getSxSerFormByOid(sxFormOid).then(response => {
            _that.formForSxSerFormForAdd = response.data;
            _that.formForSxSerFormForAdd.formType = _that.formForSxSerFormForAdd.formType  +'';
            _that.formForSxSerFormForAdd.isFormFlag =_that.formForSxSerFormForAdd.isFormFlag +'';
            _that.openFormAdd = true;
            _that.sxSerFormTitle = "表单配置信息";
            _that.customizeShow = "false";
            _that.eleShow = "true";
            _that.isFormUse = flag;
            if(_that.formForSxSerFormForAdd.formType=='0'){
              this.customizeInit('0',_that.formForSxSerFormForAdd);
            }
            if(_that.formForSxSerFormForAdd.attaName!=null&&_that.formForSxSerFormForAdd.simpleAtta!=null){
              const filem = {
                'oid':_that.formForSxSerFormForAdd.simpleAtta,
                'name':_that.formForSxSerFormForAdd.attaName
              };
              this.messageFileList=filem;
            }

          });
        }else{
          _that.formForSxSerFormForAdd = {};
          _that.formForSxSerFormForAdd.formType = '1';
          _that.formForSxSerFormForAdd.isFormFlag = '1';
          _that.formForSxSerFormForAdd.serviceOid = serviceOid;
          _that.formForSxSerFormForAdd.comboDirectoryOid = comboDirectoryOid;
          _that.openFormAdd = true;
          _that.sxSerFormTitle = "表单配置信息";
          _that.customizeShow = "false";
          _that.eleShow = "true";
          _that.isFormUse = flag;
        }
      }else{
        getSxSerFormByOid(sxFormOid).then(response => {
          _that.formForSxSerForm = response.data;
          console.log(response.data)
          _that.openFormEditView = true;
          _that.sxSerFormTitle = "表单配置信息";
          _that.isFormUse = flag;
          if(response.data.formType === 1 ){
            _that.customizeShow = "false";
            _that.eleShow = "true";
          }else{
            _that.customizeShow = "true";
            _that.eleShow = "false";
          }
        });
      }
    },

    /** 新增按钮操作  */
    handleViewForSxForm(serviceOid) {
      this.getSerFormList(serviceOid);
      let _that = this;
      if(serviceOid) {
        getSxServiceOne(serviceOid).then(response => {
          _that.openSxFormView = true;
          _that.formForSxBaseMessage = response.data.sxService;
        });

      } else {
        _that.openSxFormView = true;
      }
      _that.sxTitle ="事项表单配置";
      this.isDisable = 'false';
    },

    //表单运行配置
    handleFormInitForSxForm(row) {
      let _that = this;
      if(row.serviceOid) {
        getSxServiceOne(row.serviceOid).then(response => {
          _that.openForm = true;
        });
      } else {
        _that.openForm = true;
      }
      _that.sxSerFormTitle ="表单运行配置";
      this.isDisable = 'false';
    },

    submitRole(flag){
      this.$refs[flag].validate(valid => {
        if (valid) {
          this.isDisable = 'false';
          if('formForSxSerFormForAdd' === flag){
            if(this.messageFileList.oid){
              this.formForSxSerFormForAdd.simpleAtta=this.messageFileList.oid;
            }
            save(this.formForSxSerFormForAdd).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                setTimeout(() => {
                  if(this.formForSxSerFormForAdd.serviceOid){
                    this.openSxFormInit = true;
                    this.openFormAdd = false;
                    this.openFormEdit = false;
                    this.getSerFormList(this.formForSxSerFormForAdd.serviceOid);
                  }else{
                    this.openComboFormInit = true;
                    this.openFormAdd = false;
                    this.openFormEdit = false;
                    this.getComboSerFormList(this.formForSxSerFormForAdd.comboDirectoryOid);
                  }
                  this.isDisable = 'true';
                }, 10);

              }
            });
          }else{
            save(this.formForSxSerForm).then(response => {
              console.log(response)
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                setTimeout(() => {
                  if(this.formForSxSerForm.serviceOid){
                    this.openSxFormInit = true;
                    this.openFormAdd = false;
                    this.openFormEdit = false;
                    this.getSerFormList(this.formForSxSerForm.serviceOid);
                  }else{
                    this.openComboFormInit = true;
                    this.openFormAdd = false;
                    this.openFormEdit = false;
                    this.getComboSerFormList(this.formForSxSerForm.comboDirectoryOid);
                  }
                  this.isDisable = 'true';
                }, 10);

              }
            });
          }
        }
      })
    },

    /**
     * tab切换
     */
    handleClick(tab) {
        // 触发‘事项’事件
        this.str = "second";
        this.getListForCombo();
    },
    //电子表单新增初始化
    eleInit(radio,formForSxSerFormForAdd){
      this.openFormAdd = true;
      this.formSwitchType(radio,formForSxSerFormForAdd);
    },
    //自定义表单新增初始化
    customizeInit(radio,formForSxSerFormForAdd){
      this.openFormAdd = true;
      this.formSwitchType(radio,formForSxSerFormForAdd);
    },

    //类型切换
    formSwitchType(radio,paramOfForm){
      if(radio === '1'){
        this.eleShow = "true";
        this.customizeShow = "false";
        paramOfForm.formType = '1';
      }else{
        this.eleShow = "false";
        this.customizeShow = "true";
        paramOfForm.formType = '0';
      }
    },

    //电子表单修改初始化
    eleInitForEdit(radio,formForSxSerForm,flag){
      this.formSwitchType(radio,formForSxSerForm);
      this.formForSxSerForm = formForSxSerForm;
      if(flag == 'view'){
        this.openFormEditView = true;
      }else{
        this.openFormEdit = true;
      }
    },

    //自定义表单修改初始化
    customizeInitForEdit(radio,formForSxSerForm,flag){
      this.formSwitchType(radio,formForSxSerForm);
      this.formForSxSerForm = formForSxSerForm;
      if(flag == 'view'){
        this.openFormEditView = true;
      }else{
        this.openFormEdit = true;
      }
    },

    //电子表单是否必填 修改  初始化
    isFormFlagInitForEdit(isFormFlag,formFlag,flag,viewFlag){
      if(isFormFlag === '1'){
        formFlag.isFormFlag = 1;
      }else{
        formFlag.isFormFlag = 0;
      }
      if(flag ==='edit'){
        if(viewFlag == 'view'){
          this.openFormEditView = true;
        }else{
          this.openFormEdit = true;
        }
        this.formForSxSerForm = formFlag;
      }else{
        //刷新页面
        this.openFormAdd = false;
        this.openFormAdd = true;
        this.formForSxSerFormForAdd = formFlag;
      }

    },

    //打开电子表单设计页面
    linkOutsideUrl(){
      let url = process.env.VUE_APP_DZBD_SJ_ROUTE_PATH
      window.open(url,'_blank'); //新窗口打开外链接
    },

    //改变表单使用状态
    handleFormUseStatus(row,comboFlag){
      //下的表单只能启用一个
      if(!this.assertComboFormUseStatus(this.comboFormList,row,comboFlag)){
        this.msgWarning("该下已存在启用状态的关联表单，同一下只能同时存在一张生效的关联表单！");
        return false;
      }
      //单个事项只能启用一个表单
      if(!this.assertSxSerFormUseStatus(this.formList,row,comboFlag)){
        this.msgWarning("该实施清单下已存在启用状态的关联表单，单个实施清单下只能同时存在一张生效的关联表单！");
        return false;
      }
      const oid = row.oid;
      //检测关联表单状态，一个表单只能同时启用一个关联表单
      let ableMessage = row.formUseStatus === 1 ? '禁用' : '启用'
      this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        sxSerFormAble(oid);
      }).then(() => {
        this.msgSuccess(ableMessage+"成功");
        if(row.serviceOid){
          this.getSerFormList(row.serviceOid);
        }else {
          this.getComboSerFormList(row.comboDirectoryOid);
        }
      }).catch(function() {
        row.formUseStatus = row.formUseStatus === 0 ? 0 : 1
      });
    },
    //删除
    delFormForSxForm(row){
      const oid = row.oid;
      this.$confirm('你确定要删除吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        comboSxSerFormDel(oid);
      }).then(() => {
        this.msgSuccess("删除成功");

        if(row.serviceOid){
          this.getSerFormList(row.serviceOid);
        }
        //if(row.comboDirectoryOid && row.serviceOid==null){
          this.getComboSerFormList(row.comboDirectoryOid);
        //}
      }).catch(function() {
        row.delFlag =  0;
      });
    },

    //表单配置 -- 查看
    handleViewForComboForm(comboDirectoryOid,flag){
      //查询表单
      this.getComboSerFormList(comboDirectoryOid);
      //查询下生效的事项表单
      this.getSerFormsOfCombo(comboDirectoryOid,'sx');
      let _that = this;
      if(comboDirectoryOid) {
        getComboOne(comboDirectoryOid).then(response => {
          _that.openComboFormInitView = true;
          _that.formForComboBaseMessage = response.data;
        });

      } else {
        _that.openComboFormInitView = true;
      }
      _that.comboTitle ="表单配置";
    },

    //表单配置
    handleInitForComboForm(comboDirectoryOid,flag){
      //查询表单
      this.getComboSerFormList(comboDirectoryOid);
      //查询下生效的事项表单
      this.getSerFormsOfCombo(comboDirectoryOid,'sx');
      let _that = this;
      if(comboDirectoryOid) {
        getComboOne(comboDirectoryOid).then(response => {
          _that.openComboFormInit = true;
          _that.formForComboBaseMessage = response.data;
        });

      } else {
        _that.openComboFormInit = true;
      }
      _that.comboTitle ="表单配置";
    },

    //查询表单
    getComboSerFormList(comboDirectoryOid){
      this.loading = true;
      this.queryParamsForCombo.comboDirectoryOid = comboDirectoryOid;
      getComboFormList(comboDirectoryOid,this.queryParamsForCombo.pageNumber,this.queryParamsForCombo.pageSize).then(response => {
        this.comboFormList = response.data.data;
        this.comboFormTotal = response.data.total;
        this.loading = false;
      });
    },

    //查询下事项表单
    getSerFormsOfCombo(comboDirectoryOid,flag){
      this.loading = true;
      getSerFormsOfCombo(comboDirectoryOid,flag,this.queryParamsForCombo.pageNumber,this.queryParamsForCombo.pageSize).then(response => {
        if ('queryIneffective' === flag) {
          this.sxSerFormOfComboListOfIneffective = response;
          this.comboSerFormTitleOfIneffective = Number(response.total);
          if(Number(response.total) !== 0){
            this.sxSerFormOfComboTitleOfIneffective = '表单选择';
            this.openSxSerFormOfComboOfIneffective = true;
          }else{
            this.msgWarning("未查询到该下实施清单所关联的已启用的表单数据！");
          }
        }else{
          this.sxSerFormOfComboListOfTakeEffect = response;
          this.comboSerFormTitleOfTakeEffect = Number(response.total);
        }
        this.loading = false;
      });
    },

    //使当前中已生效的实施清单表单 失效
    invalidFormOfComboSx(oid){
      updateSxSerForm(oid).then(response => {
        if (response.code === 200) {
          this.msgSuccess("保存成功");
          this.openComboFormInit = true;
          //查询表单
          this.getComboSerFormList(this.queryParamsForCombo.comboDirectoryOid);
          //查询下生效的事项表单
          this.getSerFormsOfCombo(this.queryParamsForCombo.comboDirectoryOid,'sx');
          //保存完清空
          this.multipleSelection = [];
        }
      });

    },

    //断言关联表单的启/禁用状态 一个下只能有一个启用的表单
    assertComboFormUseStatus(row,val,comboFlag){
      if(!row){
        return true;
      }
      let flag = true;
      //超过一条则提示
      let i = 0;
      //判断查询到的关联表单有没有启用状态的数据
      row.forEach(v =>{
        if((v.formUseStatus === 1 && !v.serviceOid) && comboFlag === 'true'){
          flag = false;
          i++;
        }
      });
      if(!flag && i>0 && val.formUseStatus === 0){
        i= 0;
        return false;
      }else{
        return true;
      }
    },

    //断言单个事项关联表单的启/禁用状态 单个事项下只能有一个启用的表单
    assertSxSerFormUseStatus(row,val,comboFlag){
      if(!row){
        return true;
      }
      let flag = true;
      //超过一条则提示
      let i = 0;
      //判断查询到的事项关联表单有没有启用状态的数据
      row.forEach(v =>{
        if((v.formUseStatus === 1) && comboFlag === 'false'){
          flag = false;
          i++;
        }
      });
      if(!flag && i>0 && val.formUseStatus === 0){
        i= 0;
        return false;
      }else{
        return true;
      }
    },

    //多选操作
    toggleSelection() {

      if (this.multipleSelection) {
        this.multipleSelection.forEach(row => {
          row.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },

    //保存多选操作
    confirmSelection(){
      if (this.multipleSelection.length !== 0) {
        let serForms = [];
        this.multipleSelection.forEach(select => {
          //将与事项表单关联起来
          select.comboDirectoryOid = this.queryParamsForCombo.comboDirectoryOid;
          serForms.push(select);
        });
        saveSxSerFormByList(this.multipleSelection).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存成功");
            this.openComboFormInit = true;
            this.openSxSerFormOfComboOfIneffective = false;
            //查询表单
            this.getComboSerFormList(this.queryParamsForCombo.comboDirectoryOid);
            //查询下生效的事项表单
            this.getSerFormsOfCombo(this.queryParamsForCombo.comboDirectoryOid,'sx');
            this.multipleSelection = [];
          }
        });
      }else if(this.sxSerFormOfComboListOfIneffective.length === 0){
        this.msgError("请先配置实施清单表单！");
      }else{
        this.msgError("请先勾选表单！");
      }
    },

    //保存多选框变化后的值
    handleSelectionChange(val) {
      val.forEach(v =>{
        if(v){
            this.multipleSelection.push(v);
          }
      });
    },
  getPublishName(val){
    if(val.status == 0){
      return '未发布';
    }else if(val.status == 1){
      return '已发布';
    }else{
      return '';
    }
  },
    //成功后返回
    uploadSuccess(resp){
      this.fileList=[];
      if(200 !== resp.code){
        return this.msgError(resp.message);
      }
      const filem = {
        'oid':resp.data.oid,
        'name':resp.data.name,
        'size':resp.data.size,
        'url':resp.data.url
      };
      this.messageFileList=filem;
    },
    //失败后返回
    uploadError(resp){
      this.msgError("文件上传失败");
    },
    //上传之前
    beforeUpload(file) {
      if(file.name.indexOf("%00") > -1 || file.name.indexOf("./") > -1 || file.name.indexOf(".\\") > -1 ) {
        this.msgError('上传文件名称非法！');
        return false;
      }
      const fileSize = file.size;
      if (0 == fileSize) {
        this.msgError('上传文件不能为空！');
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 100;
      if (!isLt2M) {
        this.msgError('上传文件大小不能超过 100MB！');
      }
      return isLt2M;
    },
    uploadUrl(){
      return uploadFile();
    },
    //删除附件
    handleAttaDelete() {
      this.messageFileList={};
      this.formForSxSerFormForAdd.attaName='';
      this.formForSxSerFormForAdd.simpleAtta='';
    },
    //下载附件
    downloadFile(attaOid){
      this.download(attaOid);
    },

  },

};
</script>

<style lang="scss" scoped>
.dialog-table {
  padding: 20px;
  box-sizing: border-box;
}
.dialog-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}
.dialog-table table {
  width: 100%;
}
.dialog-table table {
  border: 1px solid #dfe6ec;
  border-collapse: collapse;
}

.dialog-table table tr td {
  border: 1px solid #dfe6ec;
  padding: 17px 8px;
  font-size: 12px;
  color: #515a6e;
  text-align: right;
}
.dialog-table table tr td:nth-of-type(2n) {
  color: #606266;
  text-align: left !important;
}
.dialog-table .el-form-item {
  margin-bottom: 0;
}
.el-form-item--small .el-form-item_content {
  margin-left: 0px !important;
}
.line {
  text-align: center;
}
.required {
  color: #FF0000;
  font-size: 20px;
  display: inline-block;
  vertical-align: middle;
  margin: 3px 5px 0px 0px;
}

</style>
