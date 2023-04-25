/**
* @Author: wangwg
*/
<template>
  <div class="app-container">
    <div class="el-table__header-wrapper dialog-table">
      <el-form ref="form" :model="form" :rules="rules" label-width="140px">
        <table cellspacing="0" cellpadding="0" border="0" class="data-table">
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.comboDirectoryOid" />
          <el-input v-show="false" v-model="form.delFlag" />
          <el-input v-show="false" v-model="form.status" />
          <el-input v-show="false" v-model="form.createUser" />
          <el-input v-show="false" v-model="form.comboDirectoryCode" />
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <i class="require">*</i>
              <b>一件事目录名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="comboDirectoryName">
                <el-input
                  v-model.trim="form.comboDirectoryName"
                  placeholder="请输入一件事目录名称"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
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
                  append-to-body
                />
              </el-form-item>
            </td>
            <td>
              <i class="require">*</i>
              <b>所属一件事分类：</b>
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
                  placeholder="请输入所属一件事分类"
                  append-to-body
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
                <el-select
                  v-model="form.direType"
                  placeholder="请选择"
                  @change="getDireType"
                >
                  <el-option
                    v-for="item in direTypes"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
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
                  append-to-body
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <i class="require">*</i>
              <b>协办部门：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="assistOrganOid">
                <treeselect
                  :multiple="true"
                  :options="assistOrganOptions"
                  :flat="true"
                  :default-expand-level="1"
                  placeholder="请输入协办部门"
                  :disabled="ifDis"
                  v-model="form.assistOrganOid"
                  append-to-body
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
                    v-for="comboObj in comboServiceObjects"
                    :label="comboObj.key"
                    >{{ comboObj.name }}</el-radio
                  >
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
            <td>
              <el-form-item label="" prop="serviceOids">
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
                  append-to-body
                />
              </el-form-item>
            </td>
          </tr>
          <tr v-if="roleForm.serviceOids != ''">
            <td>
              <b>事项系统类型：</b>
            </td>
            <td colspan="">
              <el-form-item prop="servicextlx">
                <el-input
                  v-model.trim="form.servicextlx"
                  type="textarea"
                  maxlength="500"
                  show-word-limit
                  placeholder="请输入前置事项的事项系统类型（业务系统）"
                ></el-input>
              </el-form-item>
            </td>
            <td>
              <b>审批系统名称：</b>
            </td>
            <td colspan="">
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
              <b>网办地址：</b>
            </td>
            <td>
              <el-form-item prop="webUrl">
                <el-input
                  v-model.trim="form.webUrl"
                  placeholder="请输入网办地址"
                  maxlength="100"
                  show-word-limit
                />
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
              <el-form-item>
                <el-input v-show="false" v-model="form.handleFlow" />
                <el-input v-show="false" v-model="form.handleFlowName" />
                <el-button type="success" size="mini" @click="selectAttas"
                  >办理流程图附件</el-button
                >
                <el-button type="danger" size="mini" @click="clearAtta"
                  >清理</el-button
                >
                <div v-if="handleFlowFlag">
                  <span>{{ form.handleFlowName }}</span>
                  <el-link type="primary" @click="downloadFile(form.handleFlow)"
                    >下载</el-link
                  >
                  |
                  <el-link type="primary" @click="viewFile(form.handleFlow)"
                    >预览</el-link
                  >
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
                <el-input
                  v-model.trim="form.zjfwName"
                  placeholder="请输中介名称"
                  maxlength="100"
                  show-word-limit
                />
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
                <el-checkbox label="2">移动端</el-checkbox>
                <el-checkbox label="3">网站端</el-checkbox>
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
                <el-input-number
                  v-model="form.promiseLimit"
                  :min="1"
                  :max="9999"
                />
              </el-form-item>
            </td>
            <td>
              <i class="require">*</i>
              <b>法定时限(工作日)：</b>
            </td>
            <td>
              <el-form-item prop="legalLimit">
                <el-input-number
                  v-model="form.legalLimit"
                  :min="1"
                  :max="9999"
                />
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
        </table>
      </el-form>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>

    <el-dialog
      title="选择办理流程图附件"
      :visible.sync="openAttaListView"
      width="1000px"
      append-to-body
    >
      <div class="app-container">
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-upload
              class="upload-demo"
              :action="uploadUrl()"
              :on-error="uploadError"
              :file-list="fileList"
              :before-upload="beforeUpload"
              :on-success="uploadSuccess"
            >
              <el-button size="small" type="primary"
                >点击上传<i class="iconfont zfsoft-wenjianshangchuan"></i
              ></el-button>
            </el-upload>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="attaList">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="原始文件名"
            align="center"
            prop="originName"
          />
          <el-table-column label="文件名" align="center" prop="name" />
          <el-table-column
            label="上传时间"
            align="center"
            prop="uploadDate"
            width="180"
          />
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-link
                type="primary"
                @click="selectFile(scope.row.attaOid, scope.row.originName)"
                >选择</el-link
              >
              |
              <el-link type="primary" @click="downloadFile(scope.row.attaOid)"
                >下载</el-link
              >
              |
              <el-link type="primary" @click="viewFile(scope.row.attaOid)"
                >预览</el-link
              >
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
    </el-dialog>
  </div>
</template>

<script>
import { init, queryServiceTree, getComboServiceObject, getParentOidByDistrictOid, queryComboThemeSimpleTree, pageFile, uploadFile, downloadFile } from "@/api/onelicence/industryManager/industryManager";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { save } from "@/api/onething/sxpz/comboDirectory";
export default {
  components: { Treeselect },
  name: "ComboDirectory",
  props: ['comboDirectoryOid'],
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      attatotal: 0,
      viewDialogOptions: [],
      ifDis: false,
      direTypes: [{
        value: 1,
        label: '单部门行政审批类'
      }, {
        value: 2,
        label: '跨部门业务协同类'
      }, {
        value: 3,
        label: '政府服务类'
      }, {
        value: 4,
        label: '跨层级业务协同类'
      }, {
        value: 5,
        label: '跨区域业务协同类'
      }],
      //查询一件事目录名称参数
      comboDirectoryName: '',
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      openAttaListView: false,
      //选择附件项附件
      openComboService: false,
      comboDireOid: "",

      fileList: [],
      elements: ['1'],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        comboDirectoryName: '',
        status: '0',
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
        comboServiceObject: '1',
        ifCharge: 0,
        isZjfw: 0,
        appointmentFlag: 0,
        handleForm: '0',
        onlinePayFlag: 0,
        expressFlag: 0,
        unionOrganFlag: 0,
        countToScence: 0,
        direType: '',
        serviceOids: '',
        servicextlx: '',
        servicespxt: '',
        serviceApplyAddr: ''
      },
      //一件事分类Tree
      themeOptions: [],
      roleForm: { serviceOids: '' },
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
      statusOptions: { '0': '未发布', '1': '已发布' },
      rules: {
        comboDirectoryName: [
          { required: true, message: "一件事目录名称不能为空", trigger: "blur" }
        ],
        themeOid: [
          { required: true, message: "所属一件事分类不能为空", trigger: "blur" }
        ],
        comboServiceObject: [
          { required: true, message: "服务对象不能为空", trigger: "blur" }
        ],
        declareNeedKnow: [
          { required: true, message: "申报须知不能为空", trigger: "blur" }
        ],
        mainOrganOid: [
          { required: true, message: "主办部门不能为空", trigger: "blur" }
        ],
        assistOrganOid: [
          { required: true, message: "协办部门不能为空", trigger: "blur" }
        ],
        webUrl: [
          { required: true, message: "网办地址不能为空", trigger: "blur" }
        ],
        manageAddr: [
          { required: true, message: "办理地址不能为空", trigger: "blur" }
        ],
        manageTime: [
          { required: true, message: "办理时间不能为空", trigger: "blur" }
        ],
        handleFlow: [
          { required: true, message: "办理流程图不能为空", trigger: "blur" }
        ],
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
        ]
      }
    };
  },
  watch: {
    'form.districtOid': function (val) {
      //区划加载
      this.getOrganTree(val);
      this.getAssisOrganTree(val, this.assistOrganOids);
    },
    'roleForm.serviceOids': function (val) {
      if (this.roleForm.serviceOids && this.roleForm.serviceOids > 0) {
        this.roleForm.alidRoleOids = this.roleForm.serviceOids[0];
      }
    }
  },

  computed: {
    // 计算属性的 getter
    reversedCountToScence: function () {
      if (this.form.countToScence == 0) {
        return '0次';
      } else if (this.form.countToScence == 1) {
        return '1次';
      } else if (this.form.countToScence == 2) {
        return '2次';
      } else if (this.form.countToScence == 3) {
        return '多次';
      }
      return ''
    },
    reversedExpressFlag: function () {
      if (this.form.expressFlag == 0) {
        return '否';
      } else if (this.form.expressFlag == 1) {
        return '是';
      }
      return ''
    },
    reversedOnlinePayFlag: function () {
      if (this.form.onlinePayFlag == 0) {
        return '否';
      } else if (this.form.onlinePayFlag == 1) {
        return '是';
      }
      return ''
    },
    reversedHandleForm: function () {
      if (this.form.handleForm) {
        if (this.form.handleForm == "0") {
          return '窗口办理';
        } else if (this.form.handleForm == "1") {
          return '网上办理';
        } else if (this.form.handleForm == "2") {
          return '一体化办理';
        }
      }
      return ''
    },
    reversedAppointmentFlag: function () {
      if (this.form.appointmentFlag == 0) {
        return '否';
      } else if (this.form.appointmentFlag == 1) {
        return '是';
      }
      return ''
    },
    reversedUnionOrganFlag: function () {
      if (this.form.unionOrganFlag == 0) {
        return '否';
      } else if (this.form.unionOrganFlag == 1) {
        return '是';
      }
      return ''
    },
    reversedIsZjfw: function () {
      if (this.form.isZjfw == 0) {
        return '否';
      } else if (this.form.isZjfw == 1) {
        return '是';
      }
      return ''
    },
    reversedIfCharge: function () {
      if (this.form.ifCharge == 0) {
        return '否';
      } else if (this.form.ifCharge == 1) {
        return '是';
      }
      return ''
    }

  },
  created () {
    let _that = this;
    _that.initComboDirectory();
    //一件事分类树
    _that.getThemeTree();
    //区划树
    _that.getDistrictTree();
    queryServiceTree().then(res => {
      _that.appRoleOptions = res.data;
    })
    //服务对象
    getComboServiceObject().then(response => {
      _that.comboServiceObjects = response.data;//[{"key":1,"value":"自然人"},{"key":2,"value":"法人"}];
    });
  },
  methods: {
    initComboDirectory () {
      let oids = [];
      init(this.comboDirectoryOid).then(response => {
        if (response.data.comboDirectory != undefined) {
          this.form = response.data.comboDirectory;
          let serviceOidArr = this.form.serviceOids ? this.form.serviceOids.split(',') : [];
          for (let service of serviceOidArr) {
            oids.push(service)
          }
          this.roleForm.serviceOids = oids;
          this.assistOrganOids = response.data.comboDirectory.assistOrganOid;
          if (null != response.data.comboDirectory.handleFlow && '' != response.data.comboDirectory.handleFlow) {
            this.handleFlowFlag = true;
          }
          //初始化服务终端要素
          let elements = response.data.comboDirectory.elements;
          if (elements && elements.length > 0) {
            let split = elements.split(',');
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
    },
    selectXb (val) {
      if (this.form.direType == 1) {
        this.ifDis = true;
        // this.getAssisOrganTree(this.form.districtOid,this.form.mainOrganOid);
        let oids = [];
        let assistOrganOids = val.id;
        if (assistOrganOids) {
          let organOids = assistOrganOids != "" ? assistOrganOids.split(',') : [];
          for (let oid of organOids) {
            if (oid != "") {
              oids.push(oid)
            }
          }
          this.form.assistOrganOid = oids;
        }
      }
    },
    handleRowChange (data) {
    },
    /**获取一件事分类树**/
    getThemeTree (themeOid) {
      queryComboThemeSimpleTree(themeOid).then(response => {
        this.themeOptions = response.data;
      });
    },
    /** 获取区划树 */
    getDistrictTree (districtOid) {
      this.queryDistrictSimpleTree(districtOid).then(response => {
        this.districtOptions = response.data;
      });
    },
    /**获取主办部门树**/
    getOrganTree (districtOid) {
      districtOid = districtOid.replace("DISTRICT-", "");
      if (districtOid) {
        this.queryOrganTree(districtOid).then(response => {
          this.mainOrganOptions = response.data;
        }
        );
      } else {
        this.mainOrganOptions = [];
        this.form.mainOrganOid = null;
      }

    },
    /** 获取协办部门 **/
    getAssisOrganTree (districtOid, assistOrganOids) {
      districtOid = districtOid.replace("DISTRICT-", "");
      if (districtOid) {
        let oids = [];
        if (assistOrganOids) {
          let organOids = assistOrganOids != "" ? assistOrganOids.split(',') : [];
          for (let oid of organOids) {
            if (oid != "") {
              oids.push(oid)
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
            //获取上级区划下机构
            getParentOidByDistrictOid(districtOid, '0').then(responseForParentOid => {

              let parentOid = responseForParentOid;
              if (parentOid) {
                _that.queryOrganTree(parentOid).then(response => {

                  _that.assistOrganOptions = response.data.concat(_that.assistOrganOptions);
                }
                );
              }
            });
          } else if (_that.form.direType == 5) {

            //先清空（查询结果包含本身）
            _that.assistOrganOptions = [];
            //跨区域业务协同（不能选择上级区划下部门）
            getParentOidByDistrictOid(districtOid, '1').then(responseForPOids => {
              let districtOids = responseForPOids;
              if (districtOids) {
                let split = districtOids.split(',');
                split.forEach(disOid => {

                  if (disOid) {
                    _that.queryOrganTree(disOid).then(response => {
                      _that.assistOrganOptions = _that.assistOrganOptions.concat(response.data);
                    });
                  }
                });
              }
            });
          }
        }
        );
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
      this.form.handleFlow = '';
      this.form.handleFlowName = '';
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
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
    },
    /** 提交按钮 */
    submitForm: function () {
      if (this.form.legalLimit > this.form.promiseLimit) {
        this.msgError("法定时限应小于承诺时限");
        return
      }
      this.form.serviceOids = '';
      this.roleForm.serviceOids.forEach(ser => {
        this.form.serviceOids += ser + ",";
      }
      );
      if (this.form.serviceOids != '') {
        this.form.serviceOids = this.form.serviceOids.slice(0, this.form.serviceOids.length - 1);
      } else {
        this.form.servicextlx = '';
        this.form.servicespxt = '';
        this.form.serviceApplyAddr = '';

      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (undefined != this.form.assistOrganOid && this.form.assistOrganOid.length > 0) {
            let oid = "";
            for (let i = 0; i < this.form.assistOrganOid.length; i++) {
              oid += this.form.assistOrganOid[i] + ",";
            }
            this.form.assistOrganOid = oid;
          }
          //处理服务终端要素信息
          this.form.elements = this.elements.join(',');
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
    getPublishName (val) {
      if (val.status == 0 || val.status == 4) {
        return '未发布';
      } else if (val.status == 1) {
        return '已发布';
      } else {
        return '';
      }
    },
    /** 根据不同事项大类、获取协办部门 **/
    getDireType (val) {
      if (val === 1) {//单部门
        this.ifDis = true;
        this.getAssisOrganTree(this.form.districtOid, this.form.mainOrganOid);
      } else {
        //跨部门、政府服务、跨层级、跨区域
        this.ifDis = false;
        this.form.assistOrganOid = null;
        this.assistOrganOptions = [];
        this.getAssisOrganTree(this.form.districtOid);
      }
    }
  }
};
</script>
<style lang="scss" scoped>
.treeselect240 {
  width: 240px;
}
</style>
