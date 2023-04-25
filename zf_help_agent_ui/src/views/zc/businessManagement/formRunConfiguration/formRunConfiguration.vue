/**
* @Author: wangns
*/

<template>
  <div class="app-container">
    <!--普通事项-->
    <!--事项数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
    >
      <el-form-item label="事项名称" prop="serviceName">
        <el-input
          v-model.trim="queryParams.serviceName"
          placeholder="请输入事项名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实施编码" prop="implementCode">
        <el-input
          v-model.trim="queryParams.implementCode"
          placeholder="请输入实施编码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery('sxQuery')"
          >搜索</el-button
        >
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery('sxQuery')"
          class="btn-reset"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="districtList"
      border
      :fit="true"
      height="calc(100% - 120px)"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="所属区划"
        align="center"
        :show-overflow-tooltip="true"
        prop="districtName"
      />
      <el-table-column
        label="所属机构"
        align="center"
        :show-overflow-tooltip="true"
        prop="organName"
      />
      <el-table-column
        label="事项名称"
        align="center"
        :show-overflow-tooltip="true"
        prop="serviceName"
      />
      <el-table-column
        label="实施编码"
        align="center"
        :show-overflow-tooltip="true"
        prop="implementCode"
      />
      <el-table-column
        label="事项类型"
        align="center"
        :show-overflow-tooltip="true"
        prop="serviceTypeName"
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
            @click="handleViewForSxForm(scope.row.serviceOid)"
            v-hasPermi="['sys:form:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInitForSxForm(scope.row.serviceOid)"
            v-hasPermi="['sys:form:update']"
            >表单配置</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      :total="sxTotal"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!--查看-->
    <el-dialog
    v-dialog-drag
      :title="sxTitle"
      :visible.sync="openSxFormView"
      width="900px"
      append-to-body
    >
      <div class="zf-zc-table--title">事项基本信息</div>
   
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="18%" />
            <col width="29%" />
            <col width="18%" />
            <col width="35%" />
          </colgroup>
          <tr>
            <td>
              <b>事项名称：</b>
            </td>
            <td colspan="3">
     
                <el-col>
                  {{ formForSxBaseMessage.serviceName }}
                </el-col>
        
            </td>
          </tr>
          <tr>
            <td>
              <b>事项类型：</b>
            </td>
            <td>
       
                <el-col>
                  {{ formForSxBaseMessage.serviceTypeName }}
                </el-col>
         
            </td>
            <td>
              <b>事项编码：</b>
            </td>
            <td>
         
                <el-col>
                  {{ formForSxBaseMessage.implementCode }}
                </el-col>
      
            </td>
          </tr>
        </table>
 
      <div class="zf-zc-table--title">已配置的事项表单</div>
      <el-table v-loading="loading" :data="formList" border>
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="状态"
          align="center"
          width="150"
          prop="formUseStatusName"
        />
        <el-table-column
          label="表单类型"
          align="center"
          width="150"
          prop="formTypeName"
        />
        <el-table-column
          label="表单名称"
          width="300"
          align="center"
          prop="formName"
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
              @click="viewFormForSxForm(scope.row.oid)"
              v-hasPermi="['sys:form:view']"
              >查看</el-button
            >
            <!--<el-button size="mini" type="text" v-if="scope.row.formSource === 0"  icon="iconfont zfsoft-chakan"  @click="addFormForSxForm(null,null,scope.row.oid,'false')" v-hasPermi="['sys:form:view']">查看</el-button>-->
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="sxSerFormTotal > 0"
        :total="sxSerFormTotal"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getSerFormList(queryParams.serviceOid)"
      />
      <div slot="footer" class="zf-text-center">
        <el-button @click="openSxFormView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 修改事项表单配置信息对话框 -->
    <el-dialog
    v-dialog-drag
      :title="sxTitle"
      :visible.sync="openSxFormInit"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <div class="zf-zc-table--title">事项基本信息</div>
 
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="18%" />
            <col width="29%" />
            <col width="18%" />
            <col width="35%" />
          </colgroup>
          <tr>
            <td>
              <b>事项名称：</b>
            </td>
            <td colspan="3">
     
                <el-col>
                  {{ formForSxBaseMessage.serviceName }}
                </el-col>
          
            </td>
          </tr>
          <tr>
            <td>
              <b>事项类型：</b>
            </td>
            <td>
          
                <el-col>
                  {{ formForSxBaseMessage.serviceTypeName }}
                </el-col>
           
            </td>
            <td>
              <b>事项编码：</b>
            </td>
            <td>
        
                <el-col>
                  {{ formForSxBaseMessage.implementCode }}
                </el-col>
     
            </td>
          </tr>
        </table>

      <div class="zf-zc-table--title">已配置的事项表单</div>
      <el-button
        icon="el-icon-plus"
        type="primary"
        @click="
          addFormForSxForm(formForSxBaseMessage.serviceOid, null, null, 'true')
        "
        class="add-sx-form"
        v-hasPermi="['sys:form:update']"
        >新增事项表单</el-button
      >
      <el-table v-loading="loading" :data="formList" border>
        <el-table-column label="序号" width="50" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="状态"
          align="center"
          width="100"
          prop="formUseStatusName"
        />
        <el-table-column
          label="表单类型"
          align="center"
          width="150"
          prop="formTypeName"
        />
        <el-table-column
          label="表单名称"
          width="300"
          align="center"
          prop="formName"
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
              v-if="scope.row.formUseStatus === 1"
              @click="handleFormUseStatus(scope.row, 'false')"
              v-hasPermi="['sys:form:update']"
              >禁用</el-button
            >
            <el-button
              size="mini"
              type="text"
              v-else
              @click="handleFormUseStatus(scope.row, 'false')"
              v-hasPermi="['sys:form:update']"
              >启用</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-xiugai"
              @click="
                addFormForSxForm(
                  formForSxBaseMessage.serviceOid,
                  null,
                  scope.row.oid,
                  'true'
                )
              "
              v-hasPermi="['sys:form:update']"
              >修改</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-chakan"
              @click="viewFormForSxForm(scope.row.oid)"
              v-hasPermi="['sys:form:view']"
              >查看</el-button
            >
            <el-button
              size="mini"
              type="text"
              v-if="scope.row.formUseStatus !== 1"
              @click="delFormForSxForm(scope.row)"
              v-hasPermi="['sys:form:update']"
              >删除</el-button
            >
            <!--<el-button size="mini" type="text" v-if="scope.row.formSource === 0" icon="iconfont zfsoft-chakan"  @click="addFormForSxForm(null,null,scope.row.oid,'false')" v-hasPermi="['sys:form:view']">查看</el-button>-->
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="sxSerFormTotal > 0"
        :total="sxSerFormTotal"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getSerFormList(queryParams.serviceOid)"
      />
      <div slot="footer" class="zf-text-center">
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 表单配置信息 - 查看 -->
    <el-dialog
      :title="sxSerFormTitle"
      :visible.sync="openFormEditView"
      width="900px"
      height="600px"
      v-if="openFormEditView"
      append-to-body
      v-dialog-drag
      scrollbar
    >
      <el-form
        ref="formForSxSerForm"
        :rules="rules"
        :model="formForSxSerForm"
        label-width="0"
        size="mini"
      >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
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
                <el-radio-group
                  style="margin-left: 20px"
                  v-model="formForSxSerForm.formType.toString()"
                >
                  <el-radio
                    label="1"
                    @change="eleInitForEdit('1', formForSxSerForm, 'view')"
                    >电子表单配置</el-radio
                  >
                  <el-radio
                    label="0"
                    @change="
                      customizeInitForEdit('0', formForSxSerForm, 'view')
                    "
                    >自定义表单配置</el-radio
                  >
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
                  <el-input
                    v-model.trim="formForSxSerForm.formName"
                    placeholder="请输入表单名称"
                    maxlength="25"
                    show-word-limit
                  />
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
                  <el-input
                    v-model.trim="formForSxSerForm.formAddr"
                    placeholder="请输入表单地址"
                    maxlength="100"
                    show-word-limit
                  />
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
                <el-input
                  v-model.trim="formForSxSerForm.formName"
                  placeholder="请输入表单名称"
                  maxlength="25"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr v-if="eleShow === 'true'">
            <td>
              <b><i class="required">*</i>表单编码：</b>
            </td>
            <td colspan="2">
              <el-form-item prop="formCode">
                <el-input
                  v-model.trim="formForSxSerForm.formCode"
                  placeholder="请输入表单编码"
                  maxlength="32"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td></td>
          </tr>

          <tr v-if="eleShow === 'true'">
            <td>
              <b><i class="required">*</i>表单说明：</b>
            </td>
            <td colspan="3">
              <el-row>
                <el-col :span="24">
                  <el-form-item prop="formText">
                    <el-input
                      type="textarea"
                      :rows="8"
                      v-model.trim="formForSxSerForm.formText"
                      placeholder="请输入表单说明"
                      maxlength="500"
                      show-word-limit
                    />
                  </el-form-item>
                </el-col>
              </el-row>
            </td>
          </tr>
          <!--电子表单配置 start -->
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button @click="openFormEditView = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!--已配置的事项表单 查看-->
    <el-dialog
      :title="title"
      :visible.sync="openForSxSerView"
      width="900px"
      append-to-body
      v-dialog-drag
      scrollbar
    >

        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
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
              <span v-else-if="viewForSxSerForm.formType == 0"
                >自定义表单配置</span
              >
            </td>
          </tr>
          <tr v-if="viewForSxSerForm.formType === 0">
            <td>
              <b>表单名称：</b>
            </td>
            <td colspan="3">
              {{ viewForSxSerForm.formName }}
            </td>
          </tr>
          <tr v-if="viewForSxSerForm.formType === 0">
            <td>
              <b>表单地址：</b>
            </td>
            <td colspan="3">
              {{ viewForSxSerForm.formAddr }}
            </td>
          </tr>
          <!--自定义表单配置 end -->
          <!--电子表单配置 start -->
          <tr v-if="viewForSxSerForm.formType === 1">
            <td>
              <b>表单名称：</b>
            </td>
            <td colspan="3">
              {{ viewForSxSerForm.formName }}
            </td>
          </tr>
          <tr v-if="viewForSxSerForm.formType === 1">
            <td>
              <b>表单编码：</b>
            </td>
            <td colspan="3">
              {{ viewForSxSerForm.formCode }}
            </td>
            <td></td>
          </tr>

          <tr v-if="viewForSxSerForm.formType === 1">
            <td>
              <b>表单说明：</b>
            </td>
            <td colspan="3">
              {{ viewForSxSerForm.formText }}
            </td>
          </tr>
          <tr>
            <td>
              <b>表单样本：</b>
            </td>
            <td colspan="3">
              {{ viewForSxSerForm.attaName }} &nbsp;&nbsp;&nbsp;&nbsp;
              <span @click="downloadFile(viewForSxSerForm.simpleAtta)"
                >下载
              </span>
            </td>
          </tr>
          <!--电子表单配置 start -->
        </table>

      <div slot="footer" class="zf-text-center">
        <el-button @click="openForSxSerView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 表单配置信息 - 修改 -->
    <el-dialog
    v-dialog-drag
      :title="sxSerFormTitle"
      :visible.sync="openFormEdit"
      width="900px"
      v-if="openFormEdit"
      append-to-body
    >
      <el-form
        ref="formForSxSerForm"
        :rules="rules"
        :model="formForSxSerForm"
        label-width="0"
        size="mini"
      >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
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
                <el-radio-group
                  style="margin-left: 20px"
                  v-model="formForSxSerForm.formType.toString()"
                >
                  <el-radio
                    label="1"
                    @change="eleInitForEdit('1', formForSxSerForm)"
                    >电子表单配置</el-radio
                  >
                  <el-radio
                    label="0"
                    @change="customizeInitForEdit('0', formForSxSerForm)"
                    >自定义表单配置</el-radio
                  >
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
                  <el-input
                    v-model.trim="formForSxSerForm.formName"
                    placeholder="请输入表单名称"
                    maxlength="25"
                    show-word-limit
                  />
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
                  <el-input
                    v-model.trim="formForSxSerForm.formAddr"
                    placeholder="请输入表单地址"
                    maxlength="100"
                    show-word-limit
                  />
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
                <el-input
                  v-model.trim="formForSxSerForm.formName"
                  placeholder="请输入表单名称"
                  maxlength="25"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>

          <tr v-if="eleShow === 'true'">
            <td>
              <b><i class="required">*</i>表单编码：</b>
            </td>
            <td colspan="2">
              <el-form-item prop="formCode">
                <el-input
                  v-model.trim="formForSxSerForm.formCode"
                  placeholder="请输入表单编码"
                  maxlength="32"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td style="text-align: center; background-color: #fff">
              <el-button
                icon="el-icon-edit-outline"
                v-if="isFormUse === 'true'"
                type="primary"
                @click="linkOutsideUrl"
                size="20px"
                style="width: 200px"
                v-hasPermi="['sys:form:update']"
                >设计填报表单</el-button
              >
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
                    <el-input
                      type="textarea"
                      :rows="8"
                      v-model.trim="formForSxSerForm.formText"
                      placeholder="请输入表单说明"
                      maxlength="500"
                      show-word-limit
                    />
                  </el-form-item>
                </el-col>
              </el-row>
            </td>
          </tr>

          <!--电子表单配置 start -->
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button
          type="primary"
          :disabled="isDisable === 'false'"
          @click="submitRole('formForSxSerForm')"
          >确 定</el-button
        >
        <el-button @click="openFormEdit = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 表单配置信息 -->
    <el-dialog
      :title="sxSerFormTitle"
      :visible.sync="openFormAdd"
      width="1100px"
      height="700px"
      scrollbar
      v-dialog-drag
      v-if="openFormAdd"
      append-to-body
    >
      <el-form
        ref="formForSxSerFormForAdd"
        :model="formForSxSerFormForAdd"
        :rules="rules"
        label-width="0"
        size="mini"
      >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b><i class="required">*</i>表单类型1：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="formType">
                <el-radio-group
                  style="margin-left: 20px"
                  v-model="formForSxSerFormForAdd.formType"
                >
                  <el-radio
                    label="1"
                    @change="eleInit('1', formForSxSerFormForAdd)"
                    >电子表单配置</el-radio
                  >
                  <el-radio
                    label="0"
                    @change="customizeInit('0', formForSxSerFormForAdd)"
                    >自定义表单配置</el-radio
                  >
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
                  <el-input
                    v-model.trim="formForSxSerFormForAdd.formName"
                    placeholder="请输入表单名称"
                    maxlength="25"
                    show-word-limit
                  />
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
                  <el-input
                    v-model.trim="formForSxSerFormForAdd.formAddr"
                    placeholder="请输入表单地址"
                    maxlength="100"
                    show-word-limit
                  />
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
                <el-input
                  v-model.trim="formForSxSerFormForAdd.formName"
                  placeholder="请输入表单名称"
                  maxlength="25"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr v-if="eleShow === 'true'">
            <td>
              <b><i class="required">*</i>表单编码：</b>
            </td>
            <td colspan="2">
              <el-form-item prop="formCode">
                <el-input
                  v-model.trim="formForSxSerFormForAdd.formCode"
                  placeholder="请输入表单编码"
                  maxlength="32"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td style="text-align: center; background-color: #fff">
              <el-button
                icon="el-icon-edit-outline"
                v-if="isFormUse === 'true'"
                type="primary"
                @click="querySerFormPage"
                size="20px"
                style="width: 200px"
                v-hasPermi="['sys:form:update']"
                >设计填报表单</el-button
              >
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
                    <el-input
                      type="textarea"
                      :rows="8"
                      v-model.trim="formForSxSerFormForAdd.formText"
                      placeholder="请输入表单说明"
                      maxlength="500"
                      show-word-limit
                    />
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
                  :on-success="uploadSuccess"
                >
                  <el-button type="primary" size="small"
                    >上传附件<i class="el-icon-upload el-icon--right"></i
                  ></el-button>
                </el-upload>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="messageFileList && messageFileList.oid">
            <td><b>附件：</b></td>
            <td colspan="3">
              <el-col>
                {{ messageFileList.name }}
                <span @click="downloadFile(messageFileList.oid)">下载 </span>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <span @click="handleAttaDelete">删除 </span>
              </el-col>
            </td>
          </tr>
          <!--电子表单配置 start -->
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button
          type="primary"
          :disabled="isDisable === 'false'"
          @click="submitRole('formForSxSerFormForAdd')"
          >确 定</el-button
        >
        <el-button @click="openFormAdd = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 获取电子表单的发布的表单列表 -->
    <el-dialog
    v-dialog-drag
      :close-on-click-modal="false"
      :title="formTitle"
      :visible.sync="openPublishForm"
      v-if="openPublishForm"
      width="1100px"
      height="700px"
      append-to-body
      scrollbar
    >
      <el-col :span="24" :xs="24">
        <el-form :model="formParams" :inline="true" label-width="70px">
          <el-form-item label="表单名称" prop="queryName">
            <el-input
              v-model.trim="formParams.queryName"
              placeholder="请输入表单名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQueryFrom()"
              >搜索</el-button
            >
            <el-button
              type="warning"
              icon="el-icon-refresh"
              size="mini"
              @click="resetQueryFrom()"
              class="btn-reset"
              >重置</el-button
            >
          </el-form-item>
        </el-form>

        <el-table v-loading="loading" :data="formPublishList">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="表单名称"
            align="center"
            width="280"
            prop="formName"
          />
          <el-table-column
            label="表单编码"
            align="center"
            width="280"
            prop="formCode"
          />
          <el-table-column
            label="版本"
            width="150"
            align="center"
            prop="version"
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
                icon="iconfont zfsoft-xiugai"
                @click="chooseForm(scope.row)"
                v-hasPermi="['sys:bill:update']"
                >选择</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="serFormListTotal > 0"
          :total="serFormListTotal"
          :page.sync="formParams.pageCurrentPage"
          :limit.sync="formParams.pageCurrentSize"
          @pagination="querySerFormPage()"
        />
      
      </el-col>
        <div slot="footer" class="zf-text-center">
          <el-button @click="colseForm">关 闭</el-button>
        </div>
    </el-dialog>
  </div>
</template>


<script>
import {
  save,
  getSerFormList,
  getSxSerFormByOid,
  sxSerFormAble,
  getSerFormsOfCombo,
  saveSxSerFormByList,
  updateSxSerForm,
  sxServicePage,
  getSxServiceOne,
  sxSerFormDel,
  queryFormPageList
} from "@/api/zc/businessManagement/formRunConfiguration";
import {
  uploadFile
} from "@/api/sys/atta";
// import the styles
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
export default {
  name: "FormRunConfiguration",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 事项总条数
      sxTotal: 0,
      // 事项应用表格数据
      sxSerFormTotal: 0,
      //已配置的事项表单数据
      formList: [],
      messageFileList: {},

      //一件事总条数
      comboTotal: 0,
      //一件事关联的表单总条数
      comboFormList: [],
      //一件事应用表格数据- 一件事关联的表单
      comboFormTotal: 0,
      //一件事应用表格数据
      districtList: [],
      //已配置的一件事表单数据
      comboDirectories: [],
      //一件事关联的事项信息
      sxOfComboList: [],
      //一件事表单配置弹出层标题
      comboTitle: "",
      title: "",
      //一件事下所有事项的关联表单列表总数 - 已生效
      comboSerFormTitleOfTakeEffect: 0,
      //一件事下所有事项的关联表单列表总数 - 未生效
      comboSerFormTitleOfIneffective: 0,
      //一件事表单配置弹出层
      openComboFormInit: false,
      openComboFormInitView: false,
      //一件事下所有事项的关联表单列表dialog - 未生效
      openSxSerFormOfComboOfIneffective: false,
      //一件事下所有事项的关联表单列表dialog - 已生效
      openSxSerFormOfComboOfTakeEffect: false,
      //一件事下所有事项的关联表单列表dialog标题 - - 未生效
      sxSerFormOfComboTitleOfIneffective: "",
      //一件事下所有事项的关联表单列表 - 未生效
      sxSerFormOfComboListOfIneffective: [],
      //一件事下所有事项的关联表单列表 - 已生效
      sxSerFormOfComboListOfTakeEffect: [],

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

      formTitle: "",
      //打开电子表单发布信息
      openPublishForm: false,
      //电子表单发布列表
      formPublishList: [],

      serFormListTotal: 0,


      // 表单查询参数
      formParams: {
        pageCurrentPage: 1,
        pageCurrentSize: 10,
        queryName: "",
      },

      // 事项查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceName: "",
        serviceOid: "",
        implementCode: "",
        serviceStatus: "3",
        existChildItem: 0

      },
      //一件事查询参数
      queryParamsForCombo: {
        pageNum: 1,
        pageNumber: 1,
        pageSize: 10,
        comboDirectoryName: "",
        comboDirectoryOid: "",
        comboDirectoryCode: "",
        serviceOid: "",
        status: "1"

      },
      // 事项基本信息
      formForSxBaseMessage: {},
      //一件事基本信息
      formForComboBaseMessage: {},
      // 表单参数
      formForSxSerForm: {},
      formForSxSerFormForAdd: {},
      form: {},

      // 事项表单校验
      rules: {
        formName: [{
          required: true,
          message: "表单名称不能为空",
          trigger: "blur"
        }],
        formCode: [{
          required: true,
          message: "表单编码不能为空",
          trigger: "blur"
        }],
        formText: [{
          required: true,
          message: "表单说明不能为空",
          trigger: "blur"
        },],
        formType: [{
          required: true,
          message: "表单类型不能为空",
          trigger: "blur"
        },],
        formAddr: [{
          required: true,
          message: "表单地址不能为空",
          trigger: "blur"
        },],
      },
      rulesForCombo: {

      },
      //默认第一个选项卡
      activeName: "first",
      str: "first",
      isFormFlag: "1",
      //表单类型  1-电子表单  0-自定义表单   默认 电子表单
      formType: "1",
      //板件是否交换  1-是  0-否  默认 否
      exchangeFlag: "0",
      //电子表单显示 true - 电子  false - 自定义
      eleShow: 'true',
      //自定义表单展示 true - 电子  false - 自定义
      customizeShow: 'false',
      //标记设计表单按钮是否可用
      isFormUse: 'true',
      //防止表单重复提交
      isDisable: 'true',
      //多选数据集
      multipleSelection: [],
      queryIneffectiveFlag: ""
    };
  },
  created () {
    this.getList();
  },
  methods: {

    /** 事项查询列表 */
    getList () {
      this.loading = true;
      sxServicePage(this.queryParams).then(response => {
        this.districtList = response.data.data;
        this.sxTotal = response.data.total;
        this.loading = false;
      });
    },

    /** 查询事项表单联系列表 */
    getSerFormList (serviceOid) {
      this.loading = true;
      this.queryParams.serviceOid = serviceOid;
      getSerFormList(serviceOid, this.queryParams.pageNum, this.queryParams.pageSize).then(response => {
        this.formList = response.data.data;
        this.sxSerFormTotal = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel () {
      this.openSxFormInit = false;
    },
    /** 搜索按钮操作 */
    handleQuery (flag) {
      if (flag === 'sxQuery') {
        this.queryParams.pageNum = 1;
        this.getList();
      }
    },

    resetForm (formName) {
      this.$refs[formName].resetFields();
    },

    /** 重置按钮操作 */
    resetQuery (flag) {
      if (flag === 'sxQuery') {
        this.resetForm('queryForm');
        this.handleQuery(flag);
      } else {
        this.resetForm('queryFormForCombo');
        this.handleQuery(flag);
      }
    },

    //已配置的事项表单查看
    viewFormForSxForm (sxFormOid) {
      getSxSerFormByOid(sxFormOid).then(response => {
        this.viewForSxSerForm = response.data;
        this.openForSxSerView = true;
        this.title = "查看表单配置信息";
      });
    },


    /** 操作按钮操作 - - 普通事项或表单*/
    addFormForSxForm (serviceOid, comboDirectoryOid, sxFormOid, flag) {
      let _that = this;
      _that.messageFileList = {};
      if (serviceOid || comboDirectoryOid) {
        if (sxFormOid) {
          getSxSerFormByOid(sxFormOid).then(response => {
            _that.formForSxSerFormForAdd = response.data;
            _that.formForSxSerFormForAdd.formType = _that.formForSxSerFormForAdd.formType + '';
            _that.formForSxSerFormForAdd.isFormFlag = _that.formForSxSerFormForAdd.isFormFlag + '';
            _that.openFormAdd = true;
            _that.sxSerFormTitle = "表单配置信息";
            _that.customizeShow = "false";
            _that.eleShow = "true";
            _that.isFormUse = flag;
            if (_that.formForSxSerFormForAdd.formType == '0') {
              this.customizeInit('0', _that.formForSxSerFormForAdd);
            }
            if (_that.formForSxSerFormForAdd.attaName != null && _that.formForSxSerFormForAdd.simpleAtta != null) {
              const filem = {
                'oid': _that.formForSxSerFormForAdd.simpleAtta,
                'name': _that.formForSxSerFormForAdd.attaName
              };
              this.messageFileList = filem;
            }
          });
        } else {
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
      } else {
        getSxSerFormByOid(sxFormOid).then(response => {
          _that.formForSxSerForm = response.data;
          console.log(response.data)
          _that.openFormEditView = true;
          _that.sxSerFormTitle = "表单配置信息";
          _that.isFormUse = flag;
          if (response.data.formType === 1) {
            _that.customizeShow = "false";
            _that.eleShow = "true";
          } else {
            _that.customizeShow = "true";
            _that.eleShow = "false";
          }
        });
      }
    },

    /** 新增按钮操作  */
    handleViewForSxForm (serviceOid) {
      this.getSerFormList(serviceOid);
      let _that = this;
      if (serviceOid) {
        getSxServiceOne(serviceOid).then(response => {
          _that.openSxFormView = true;
          _that.formForSxBaseMessage = response.data.sxService;
        });

      } else {
        _that.openSxFormView = true;
      }
      _that.sxTitle = "事项表单配置";
    },

    /** 新增和修改按钮操作  - 普通事项*/
    handleInitForSxForm (serviceOid) {
      this.getSerFormList(serviceOid);
      let _that = this;
      if (serviceOid) {
        getSxServiceOne(serviceOid).then(response => {
          _that.openSxFormInit = true;
          _that.formForSxBaseMessage = response.data.sxService;
        });

      } else {
        _that.openSxFormInit = true;
      }
      _that.sxTitle = "事项表单配置";
    },

    //表单运行配置
    handleFormInitForSxForm (row) {
      let _that = this;
      if (row.serviceOid) {
        getSxServiceOne(row.serviceOid).then(response => {
          _that.openForm = true;
        });
      } else {
        _that.openForm = true;
      }
      _that.sxSerFormTitle = "表单运行配置";
    },

    submitRole (flag) {
      this.$refs[flag].validate(valid => {
        if (valid) {
          this.isDisable = 'false';
          if ('formForSxSerFormForAdd' === flag) {
            if (this.messageFileList.oid) {
              this.formForSxSerFormForAdd.simpleAtta = this.messageFileList.oid;
            }
            save(this.formForSxSerFormForAdd).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                setTimeout(() => {
                  if (this.formForSxSerFormForAdd.serviceOid) {
                    this.openSxFormInit = true;
                    this.openFormAdd = false;
                    this.openFormEdit = false;
                    this.getSerFormList(this.formForSxSerFormForAdd.serviceOid);
                  }
                  this.isDisable = 'true';
                }, 10);

              }
            });
          } else {
            save(this.formForSxSerForm).then(response => {
              console.log(response)
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                setTimeout(() => {
                  if (this.formForSxSerForm.serviceOid) {
                    this.openSxFormInit = true;
                    this.openFormAdd = false;
                    this.openFormEdit = false;
                    this.getSerFormList(this.formForSxSerForm.serviceOid);
                  }
                  this.isDisable = 'true';
                }, 10);

              }
            });
          }
        }
      })
    },

    //删除
    delFormForSxForm (row) {
      const oid = row.oid;
      this.$confirm('你确定要删除吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        sxSerFormDel(oid);
      }).then(() => {
        this.msgSuccess("删除成功");

        if (row.serviceOid) {
          this.getSerFormList(row.serviceOid);
        }
      }).catch(function () {
      });
    },
    /**
     * tab切换
     */
    handleClick (tab) {
      // 触发‘普通事项’事件
      this.str = "first";
      this.getList();
    },
    //电子表单新增初始化
    eleInit (radio, formForSxSerFormForAdd) {
      this.openFormAdd = true;
      this.formSwitchType(radio, formForSxSerFormForAdd);
    },
    //自定义表单新增初始化
    customizeInit (radio, formForSxSerFormForAdd) {
      this.openFormAdd = true;
      this.formSwitchType(radio, formForSxSerFormForAdd);
    },

    //类型切换
    formSwitchType (radio, paramOfForm) {
      if (radio === '1') {
        this.eleShow = "true";
        this.customizeShow = "false";
        paramOfForm.formType = '1';
      } else {
        this.eleShow = "false";
        this.customizeShow = "true";
        paramOfForm.formType = '0';
      }
    },

    //电子表单修改初始化
    eleInitForEdit (radio, formForSxSerForm, flag) {
      this.formSwitchType(radio, formForSxSerForm);
      this.formForSxSerForm = formForSxSerForm;
      if (flag == 'view') {
        this.openFormEditView = true;
      } else {
        this.openFormEdit = true;
      }
    },

    //自定义表单修改初始化
    customizeInitForEdit (radio, formForSxSerForm, flag) {
      this.formSwitchType(radio, formForSxSerForm);
      this.formForSxSerForm = formForSxSerForm;
      if (flag == 'view') {
        this.openFormEditView = true;
      } else {
        this.openFormEdit = true;
      }
    },

    //电子表单是否必填 修改  初始化
    isFormFlagInitForEdit (isFormFlag, formFlag, flag, viewFlag) {
      if (isFormFlag === '1') {
        formFlag.isFormFlag = 1;
      } else {
        formFlag.isFormFlag = 0;
      }
      if (flag === 'edit') {
        if (viewFlag == 'view') {
          this.openFormEditView = true;
        } else {
          this.openFormEdit = true;
        }
        this.formForSxSerForm = formFlag;
      } else {
        //刷新页面
        this.openFormAdd = false;
        this.openFormAdd = true;
        this.formForSxSerFormForAdd = formFlag;
      }

    },
    colseForm () {
      this.openPublishForm = false;
      this.formPublishList = [];
    },
    chooseForm (row) {
      console.log(row);
      this.formForSxSerFormForAdd.formCode = row.formCode;
      this.formForSxSerFormForAdd.designOid = row.designOid;
      this.formForSxSerFormForAdd.authorizeKey = row.authorizeKey;
      this.openPublishForm = false;
    },

    //打开电子表单设计页面
    linkOutsideUrl () {
      let url = process.env.VUE_APP_DZBD_SJ_ROUTE_PATH
      window.open(url, '_blank'); //新窗口打开外链接
    },
    //查询电子表单列表
    querySerFormPage () {
      queryFormPageList(this.formParams.queryName, this.formParams.pageCurrentPage, this.formParams.pageCurrentSize).then(response => {
        if (response.code === 200) {
          this.formTitle = "电子表单列表"
          this.openPublishForm = true;
          let json = JSON.parse(response.data);
          this.formPublishList = json.data.data;
          this.serFormListTotal = json.data.total;
        }
      });
    },
    handleQueryFrom () {
      this.querySerFormPage();

    },
    resetQueryFrom () {
      this.formParams.queryName = "";
    },
    //改变表单使用状态
    handleFormUseStatus (row, comboFlag) {
      //单个事项只能启用一个表单
      //if (!this.assertSxSerFormUseStatus(this.formList, row, comboFlag)) {
      //  this.msgWarning("该实施清单下已经有存在启用状态的关联表单，单个实施清单下只能同时存在一张生效的关联表单！");
      //  return false;
      // }
      const oid = row.oid;
      //检测一件事关联表单状态，一个表单只能同时启用一个关联表单
      let ableMessage = row.formUseStatus === 1 ? '禁用' : '启用'
      this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        sxSerFormAble(oid);
      }).then(() => {
        this.msgSuccess(ableMessage + "成功");
        if (row.serviceOid) {
          this.getSerFormList(row.serviceOid);
        }
      }).catch(function () {
        row.formUseStatus = row.formUseStatus === 0 ? 0 : 1
      });
    },


    //查询一件事下事项表单
    getSerFormsOfCombo (comboDirectoryOid, flag) {
      this.queryIneffectiveFlag = flag;
      this.loading = true;
      getSerFormsOfCombo(comboDirectoryOid, this.queryIneffectiveFlag, this.queryParamsForCombo.pageNumber, this.queryParamsForCombo
        .pageSize).then(response => {
          if ('queryIneffective' === flag) {
            this.sxSerFormOfComboListOfIneffective = response;
            this.comboSerFormTitleOfIneffective = Number(response.total);
            if (Number(response.total) !== 0) {
              this.sxSerFormOfComboTitleOfIneffective = '表单选择';
              this.openSxSerFormOfComboOfIneffective = true;
            } else {
              this.msgWarning("未查询到该一件事下实施清单所关联的已启用的表单数据！");
            }
          } else {
            this.sxSerFormOfComboListOfTakeEffect = response;
            this.comboSerFormTitleOfTakeEffect = Number(response.total);
          }
          this.loading = false;
        });
    },

    //使当前一件事中已生效的实施清单表单 失效
    invalidFormOfComboSx (oid) {
      updateSxSerForm(oid).then(response => {
        if (response.code === 200) {
          this.msgSuccess("保存成功");
          this.openComboFormInit = true;
          //查询一件事表单
          this.getComboSerFormList(this.queryParamsForCombo.comboDirectoryOid);
          //查询一件事下生效的事项表单
          this.getSerFormsOfCombo(this.queryParamsForCombo.comboDirectoryOid, null);
          //保存完清空
          this.multipleSelection = [];
        }
      });

    },


    //断言单个事项关联表单的启/禁用状态 单个事项下只能有一个启用的表单
    assertSxSerFormUseStatus (row, val, comboFlag) {
      if (!row) {
        return true;
      }
      let flag = true;
      //超过一条则提示
      let i = 0;
      //判断查询到的事项关联表单有没有启用状态的数据
      row.forEach(v => {
        if ((v.formUseStatus === 1) && comboFlag === 'false') {
          flag = false;
          i++;
        }
      });
      if (!flag && i > 0 && val.formUseStatus === 0) {
        i = 0;
        return false;
      } else {
        return true;
      }
    },

    //多选操作
    toggleSelection () {

      if (this.multipleSelection) {
        this.multipleSelection.forEach(row => {
          row.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },

    //保存多选操作
    confirmSelection () {
      if (this.multipleSelection.length !== 0) {
        let serForms = [];
        this.multipleSelection.forEach(select => {
          //将一件事与事项表单关联起来
          select.comboDirectoryOid = this.queryParamsForCombo.comboDirectoryOid;
          serForms.push(select);
        });
        saveSxSerFormByList(this.multipleSelection).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存成功");
            this.openComboFormInit = true;
            this.openSxSerFormOfComboOfIneffective = false;
            //查询一件事表单
            this.getComboSerFormList(this.queryParamsForCombo.comboDirectoryOid);
            //查询一件事下生效的事项表单
            this.getSerFormsOfCombo(this.queryParamsForCombo.comboDirectoryOid, null);
            this.multipleSelection = [];
          }
        });
      } else if (this.sxSerFormOfComboListOfIneffective.length === 0) {
        this.msgError("请先配置实施清单表单！");
      } else {
        this.msgError("请先勾选表单！");
      }
    },

    //保存多选框变化后的值
    handleSelectionChange (val) {
      val.forEach(v => {
        if (v) {
          this.multipleSelection.push(v);
        }
      });
    },
    getPublishName (val) {
      if (val.status == 0) {
        return '未发布';
      } else if (val.status == 1) {
        return '已发布';
      } else {
        return '';
      }
    },
    //成功后返回
    uploadSuccess (resp) {
      this.fileList = [];
      if (200 !== resp.code) {
        return this.msgError(resp.message);
      }
      const filem = {
        'oid': resp.data.oid,
        'name': resp.data.name,
        'size': resp.data.size,
        'url': resp.data.url
      };
      this.messageFileList = filem;
    },
    //失败后返回
    uploadError (resp) {
      this.msgError("文件上传失败");
    },
    //上传之前
    beforeUpload (file) {
      if (file.name.indexOf("%00") > -1 || file.name.indexOf("./") > -1 || file.name.indexOf(".\\") > -1) {
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
    uploadUrl () {
      return uploadFile();
    },
    //删除附件
    handleAttaDelete () {
      this.messageFileList = {};
    },
    //下载附件
    downloadFile (attaOid) {
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
  color: #ff0000;
  font-size: 20px;
  display: inline-block;
  vertical-align: middle;
  margin: 3px 5px 0px 0px;
}

.el-table-box {
  padding: 20px;
}

.add-sx-form {
  width: 140px;
  margin-right: 20px;
  margin-bottom: 10px;
  float: right;
}
</style>
