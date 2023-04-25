<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="所属应用">
        <el-select
          v-model="queryParams.appOid"
          placeholder="请选择所属应用"
          size="small"
          filterable
          style="width: 240px"
          clearable
        >
          <el-option
            v-for="app in appList"
            :key="app.appOid"
            :label="app.name"
            :value="app.appOid"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属区划">
        <treeselect
          v-model="queryParams.districtOid"
          :options="districtOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          @input="districtSel"
          placeholder="请输入所属区划"
        />
      </el-form-item>
      <el-form-item label="所属机构">
        <treeselect
          v-model="queryParams.organOid"
          :options="listOrganOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入所属机构"
        />
      </el-form-item>
      <el-form-item label="流程名称" prop="workflowName">
        <el-input
          v-model="queryParams.workflowName"
          placeholder="请输入流程名称"
          clearable
          size="small"
          maxlength="50"
          show-word-limit
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流程编码" prop="infoCode">
        <el-input
          v-model="queryParams.infoCode"
          placeholder="请输入流程编码"
          clearable
          size="small"
          maxlength="50"
          show-word-limit
        />
      </el-form-item>

      <!--<el-form-item label="流程类型">
        <el-select
          v-model="queryParams.typeOid"
          placeholder="请选择流程类型"
          size="small"
          filterable
          style="width: 240px"
          clearable
        >
          <el-option
            v-for="type in typeList"
            :key="type.typeOid"
            :label="type.name"
            :value="type.typeOid"
          />
        </el-select>
      </el-form-item>-->
      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          class="ml5"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit(null, 1)"
          v-hasPermi="['workflow:bussinfo:init']"
          >新增</el-button
        >
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="bussinfoList" border>
      <!--<el-table-column type="selection" width="55" align="center"  />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="所属区划"
        align="center"
        prop="districtName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="所属组织机构"
        align="center"
        prop="organName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="流程名称"
        align="center"
        prop="workflowName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="流程编码"
        align="center"
        prop="infoCode"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="办理时限"
        align="center"
        prop="timeLimit"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <span>{{
            scope.row.timeLimit +
            (scope.row.timeUnit == 'N' ? '自然日' : '工作日')
          }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="所属类型"
        align="center"
        prop="typeName"
        :show-overflow-tooltip="true"
      />
      <!--<el-table-column label="版本" align="center" prop="version" width="100" />-->
      <el-table-column
        label="设计标识"
        align="center"
        width="80"
        prop="deploymentId"
      >
        <template slot-scope="scope">
          <span v-if="null == scope.row.modelId">未设计</span>
          <span v-if="null != scope.row.modelId">
            <span
              class="redSpan"
              v-if="null == scope.row.isPublish || '0' == scope.row.isPublish"
              >未部署</span
            >
            <span
              class="greenSpan"
              v-if="null != scope.row.isPublish && '1' == scope.row.isPublish"
              >已部署</span
            >
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="新增时间"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        width="330"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleInit(scope.row, 3)"
            v-hasPermi="['workflow:bussinfo:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row, 2)"
            v-hasPermi="['workflow:bussinfo:init']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-liuchengbianzhi"
            @click="handleEdit(scope.row)"
            v-hasPermi="['workflow:bussinfo:init']"
            >设计流程</el-button
          >
          <el-button
            v-show="undefined != scope.row.modelId"
            size="mini"
            type="text"
            icon="icon iconfont zfsoft-fabu"
            @click="handleDeploy(scope.row)"
            v-hasPermi="['workflow:bussinfo:init']"
            >部署</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-liuchengchakan"
            @click="handleProcessView(scope.row)"
            v-hasPermi="['workflow:bussinfo:init']"
            >查看流程图</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-fuzhi"
            @click="handleFlowCopyInit(scope.row)"
            v-hasPermi="['workflow:bussinfo:copyInit']"
            >流程复制</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-lishibanben"
            @click="handleFlowHistoryInit(scope.row)"
            v-hasPermi="['workflow:bussinfo:hisInit']"
            >历史流程</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['workflow:bussinfo:delete']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="addDialogShow"
      :visible.sync="addDialogShow"
      width="900px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="dialog-table dialog-table1"
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
              <b> <i class="require" v-if="!viewFlag">*</i>所属区划： </b>
            </td>
            <td>
              <el-form-item prop="districtOid">
                <el-col :span="24">
                  <treeselect
                    :options="districtOptions1"
                    :default-expand-level="1"
                    placeholder="请选择区划"
                    v-model="form.districtOid"
                    @input="
                      (id, instanceId) =>
                        districtSel1(id, () => (form.organOid = null))
                    "
                    :disabled="viewFlag"
                  />
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b> <i class="require" v-if="!viewFlag">*</i>所属组织机构： </b>
            </td>
            <td>
              <el-form-item prop="organOid">
                <el-col :span="24">
                  <treeselect
                    :options="listOrganOptions1"
                    noOptionsText="暂无数据"
                    :default-expand-level="1"
                    placeholder="请选择所属组织机构"
                    v-model="form.organOid"
                    :disabled="viewFlag"
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b> <i class="require" v-if="!viewFlag">*</i>流程名称： </b>
            </td>
            <td colspan="3">
              <el-form-item prop="workflowName" v-if="!viewFlag">
                <el-col :span="24">
                  <el-input
                    v-model="form.workflowName"
                    :disabled="viewFlag"
                    placeholder="请输入流程名称"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
              <el-form-item prop="workflowName" v-if="viewFlag">
                {{ form.workflowName }}
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td>
              <b> <i class="require" v-if="!viewFlag">*</i>流程编码： </b>
            </td>
            <td>
              <el-form-item prop="infoCode" v-if="!viewFlag">
                <el-col :span="24">
                  <el-input
                    v-model="form.infoCode"
                    :disabled="viewFlag"
                    placeholder="请输入流程编码"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
              <el-form-item prop="infoCode" v-if="viewFlag">
                <span>{{ form.infoCode }}</span>
              </el-form-item>
            </td>

            <td>
              <b> <i class="require" v-if="!viewFlag">*</i>办理时限： </b>
            </td>
            <td>
              <el-form-item prop="timeLimit" v-if="!viewFlag">
                <el-col :span="24">
                  <el-input-number
                    v-model="form.timeLimit"
                    :disabled="viewFlag"
                    placeholder="请输入办理时限"
                    controls-position="right"
                    :precision="0"
                    :min="1"
                    :max="999"
                  />
                </el-col>
              </el-form-item>
              <el-form-item prop="timeUnit" v-if="!viewFlag">
                <el-col :span="24">
                  <el-radio-group v-model="form.timeUnit">
                    <el-radio label="W">工作日</el-radio>
                    <el-radio label="N">自然日</el-radio>
                  </el-radio-group>
                </el-col>
              </el-form-item>
              <el-form-item prop="timeLimit" v-if="viewFlag">
                <el-col :span="24">
                  <el-form-item v-if="viewFlag">
                    <span>{{
                      form.timeLimit +
                      ' ' +
                      (form.timeUnit == 'N' ? '自然日' : '工作日')
                    }}</span>
                  </el-form-item>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b> <i class="require" v-if="!viewFlag">*</i>所属应用： </b>
            </td>
            <td>
              <el-form-item prop="appOid">
                <el-col :span="24">
                  <el-select
                    v-model="form.appOid"
                    placeholder="请选择所属应用"
                    filterable
                    style="width: 100%"
                    clearable
                    @change="changeAppOid"
                    :disabled="viewFlag"
                  >
                    <el-option
                      v-for="app in appList"
                      :key="app.appOid"
                      :label="app.name"
                      :value="app.appOid"
                    />
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b><i class="require" v-if="!viewFlag">*</i>所属流程类型：</b>
            </td>
            <td>
              <el-form-item prop="typeOid">
                <el-select
                  v-model="form.typeOid"
                  placeholder="请选择所属流程类型"
                  filterable
                  style="width: 100%"
                  clearable
                  :disabled="viewFlag"
                >
                  <el-option
                    v-for="type in typeList"
                    :key="type.typeOid"
                    :label="type.name"
                    :value="type.typeOid"
                  />
                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>备注：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="workflowMemo">
                <el-col :span="24">
                  <el-input
                    v-model="form.workflowMemo"
                    type="textarea"
                    v-show="!viewFlag"
                    maxlength="1000"
                    show-word-limit
                    placeholder="请输入备注"
                  ></el-input>
                  <span v-show="viewFlag">{{ form.workflowMemo }}</span>
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="!viewFlag" type="primary" @click="submitForm"
          >确 定</el-button
        >
        <el-button
          @click="
            () => {
              addDialogShow = false
            }
          "
          >{{ viewFlag ? '关闭' : '取 消' }}</el-button
        >
      </div>
    </el-dialog>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!--<el-dialog v-dialog-drag :fullscreen="true" :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="item.show" v-for="item in dialogEditOptions" @close="outerEditVisible"
               :title="item.title" width="90%" append-to-body>
      <ProcessDesign :dialogVisible="item.dialogVisible" :params="item.params" @handleVisiable="handleVisiable" @saveHandleClose="saveHandleClose"></ProcessDesign>
    </el-dialog>-->

    <!--<el-dialog v-dialog-drag :fullscreen="true" :visible.sync="item.show" v-for="item in dialogViewOptions" @close="outerViewVisible"
               :title="item.title" width="90%" append-to-body>
      <ProcessView :dialogViewModelVisible="item.dialogViewModelVisible" :params="params" @handleViewModelVisiable="handleViewModelVisiable"></ProcessView>
      <div slot="footer" class="dialog-footer">
        <el-button @click="outerViewVisible">关 闭</el-button>
      </div>
    </el-dialog>-->

    <!--流程复制弹窗-->
    <el-dialog
      :close-on-click-modal="false"
      title="流程复制"
      v-if="copyDialogShow"
      :visible.sync="copyDialogShow"
      width="900px"
      append-to-body
    >
      <el-form
        :model="queryParams1"
        ref="queryForm1"
        :inline="true"
        label-width="68px"
      >
        <el-form-item label="流程名称" prop="workflowName">
          <el-input
            v-model="queryParams1.workflowName"
            placeholder="请输入流程名称"
            clearable
            size="small"
            maxlength="50"
            show-word-limit
            @keyup.enter.native="handleQuery1"
          />
        </el-form-item>
        <el-form-item class="fr mr0">
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="handleQuery1"
            >搜索</el-button
          >
          <el-button
            type="warning"
            icon="el-icon-refresh"
            size="mini"
            @click="resetQuery1"
            class="ml5"
            >重置</el-button
          >
        </el-form-item>
      </el-form>
      <el-table v-loading="loading1" :data="bussinfoList1" border="">
        <!--<el-table-column type="selection" width="55" align="center"  />-->
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="流程名称"
          align="center"
          prop="workflowName"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="办理时限"
          align="center"
          prop="timeLimit"
          :show-overflow-tooltip="true"
        >
          <template slot-scope="scope">
            <span>{{
              scope.row.timeLimit +
              (scope.row.timeUnit == 'N' ? '自然日' : '工作日')
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="新增时间"
          align="center"
          prop="createDate"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="操作"
          width="140"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-fuzhi"
              @click="handleFlowCopy(scope.row)"
              v-hasPermi="['workflow:bussinfo:copy']"
              >流程复制</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        style="height: 85px"
        v-show="total1 > 0"
        :total="total1"
        :page.sync="queryParams1.pageNum"
        :limit.sync="queryParams1.pageSize"
        @pagination="getList1"
      />
    </el-dialog>

    <!--历史流程弹窗-->
    <el-dialog
      :close-on-click-modal="false"
      title="业务历史流程信息"
      v-if="hisDialogShow"
      :visible.sync="hisDialogShow"
      width="900px"
      append-to-body
    >
      <el-table v-loading="loading2" :data="bussinfoList2" border="">
        <!--<el-table-column type="selection" width="55" align="center"  />-->
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="流程名称"
          align="center"
          prop="name"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="版本"
          width="80"
          align="center"
          prop="version"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="创建时间"
          width="180"
          align="center"
          prop="deployTime"
          :show-overflow-tooltip="true"
        />
        <!--<el-table-column label="状态" width="120" align="center" prop="deployStatus" :show-overflow-tooltip="true"/>-->
        <el-table-column
          label="操作"
          width="200"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-chakan"
              @click="handleFlowHisView(scope.row)"
              v-hasPermi="['workflow:bussinfo:hisView']"
              >查看</el-button
            >
            <!--<el-button
              v-if="scope.row.deployStatus == '禁用'"
              size="mini"
              type="text"
              icon="iconfont zfsoft-qiyong"
              @click="handleFlowHisEnable(scope.row)"
              v-hasPermi="['workflow:bussinfo:hisEnable']"
            >启用</el-button>-->
            <!--<el-button
              v-if="scope.row.deployStatus == '禁用'"
              size="mini"
              type="text"
              icon="iconfont zfsoft-shanchu"
              @click="handleFlowHisDel(scope.row)"
              v-hasPermi="['workflow:bussinfo:hisDel']"
            >删除</el-button>-->
          </template>
        </el-table-column>
      </el-table>
      <pagination
        style="height: 85px"
        v-show="total2 > 0"
        :total="total2"
        :page.sync="queryParams2.pageNum"
        :limit.sync="queryParams2.pageSize"
        @pagination="getList2"
      />
    </el-dialog>
  </div>
</template>

<script>
import { deepClone } from "@/utils/index";
import { saveOrUpdate, del, getOne, page, deploy, pageByTypeOid, workflowCopy } from "@/api/workflow/bussinfo";
import { page as bussHisPage, del as bussHisDel } from "@/api/workflow/bussHistory";
import { queryOrganTree } from "@/api/sys/organ";
import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import { applist } from "@/api/sys/app";
import { typeList } from "@/api/workflow/type";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import Vue from "vue";
//import ProcessDesign from '@/views/workflowModeler/ProcessDesign'
//import ProcessView from '@/views/workflowModeler/ProcessView'

export default {
  components: {
    Treeselect,
    // ProcessDesign,
    // ProcessView,
  },
  name: "Bussinfo",
  data () {
    return {
      // 历史流程弹窗
      hisDialogShow: false,
      // 流程复制弹窗
      copyDialogShow: false,
      //区划数据
      districtOptions: [],
      districtOptions1: [],
      //列表的机构数据
      listOrganOptions: [],
      listOrganOptions1: [],
      intervalId: '',
      //所属应用
      appList: [],
      //所属类型
      typeList: [],
      // 遮罩
      addDialogShow: false,
      // 遮罩层
      loading: true,
      // 流程复制列表遮罩
      loading1: true,
      // 流程历史信息列表遮罩
      loading2: true,
      // 弹窗title
      dialogTitle: '',
      // 流程信息列表
      bussinfoList: [],
      // 流程复制待选流程信息列表
      bussinfoList1: [],
      // 流程历史信息列表
      bussinfoList2: [],
      // 查看标识
      viewFlag: false,
      // 总条数
      total: 0,
      // 流程复杂弹窗列表总条数
      total1: 0,
      // 历史流程条数
      total2: 0,
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 启用状态
      ableMap: { '1': '启用', '0': '禁用' },
      modelVisible: false,
      dialogVisible: false,
      dialogViewModelVisible: false,
      dialogFlowViewModelVisible: false,
      dialogEditOptions: [],
      dialogViewOptions: [],
      dialogFlowViewOptions: [],
      descp: '',
      // 查询参数
      queryParams: {
        pageNum: 1, // 当前页
        pageSize: 10, // 每页大小
        districtOid: null, // 区划id
        organOid: null,  // 机构id
        workflowName: '', // 流程名称
        appOid: null, // 应用id
        typeOid: null // 类型id
      },
      // 流程复制弹窗查询参数
      queryParams1: {
        pageNum: 1, // 当前页
        pageSize: 10, // 每页大小
        workflowName: '', // 流程名称
        typeOid: null // 类型id
      },
      queryParams2: {
        pageNum: 1, // 当前页
        pageSize: 10, // 每页大小
        processKey: '', // 流程key
        infoOid: '', // 流程信息业务主键
      },
      form: {
        districtOid: null,
        organOid: null,
        workflowName: '',
        timeLimit: '',
        timeUnit: 'W',
        appOid: null,
        typeOid: '',
        workflowMemo: ''
      },
      params: {
        row: ''
      },
      // 表单校验
      rules: {
        districtOid: [
          { required: true, message: "请选择区划", trigger: "blur" },
        ],
        workflowName: [
          { required: true, message: "请输入流程名称", trigger: "blur" },
        ],
        infoCode: [
          { required: true, message: "请输入流程编码", trigger: "blur" },
        ],
        timeLimit: [
          { required: true, message: "请输入办理时限", trigger: "blur" },
        ],
        timeUnit: [
          { required: true, message: "请选择时限类型", trigger: "blur" },
        ],
        appOid: [
          { required: true, message: "请选择所属应用", trigger: "blur" },
        ],
        typeOid: [
          { required: true, message: "请选择流程类型", trigger: "blur" },
        ],
        organOid: [
          { required: true, message: "请选择所属组织机构", trigger: "blur" },
        ],
      }
    };
  },
  created () {
    this.getList();
    this.getAppList();
    this.getDistrictTree();
    this.changeAppOid();
  },
  destroyed () {
    if ('' != this.intervalId) {
      clearInterval(this.intervalId); //清除计时器
      this.intervalId = ''; //设置为空
    }
  },
  methods: {
    //列表刷新
    startListEventListener () {
      let workflowStorage = localStorage.getItem("workflowStorage");
      if (undefined != workflowStorage && '' != workflowStorage) {
        this.getList();
        //监听到true 清空
        localStorage.setItem('workflowStorage', '');
        clearInterval(this.intervalId); //清除计时器
        this.intervalId = ''; //设置为空
      }
    },
    changeAppOid (appOid) {
      if (undefined != appOid && '' != appOid) {
        this.getTypeList(appOid);
      } else {
        this.typeList = [];
        this.form.typeOid = '';
      }

    },
    // 历史流程查看
    handleFlowHisView (row) {
      //console.log(row.processDefId)
      window.open("/processView?processDefId=" + row.processDefId, '_blank')
    },
    // 历史流程启用
    handleFlowHisEnable (row) {
      this.handleDeploy({ id: row.workflowInfoId }, row.modelId);
    },
    // 历史流程删除
    handleFlowHisDel (row) {
      let _that = this;
      this.$confirm('是否确认删除?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return bussHisDel(row.modelId);
      }).then(() => {
        _that.msgSuccess("删除成功！");
        _that.getList2();
      }).catch(function () { });
    },
    handleFlowHistoryInit (row) {
      let _that = this;
      if (!row.processKey) {
        this.msgSuccess("当前流程未配置流程图或未部署");
        return;
      }
      _that.queryParams2.infoOid = row.infoOid;
      _that.getList2();
      _that.hisDialogShow = true;
    },
    // 流程复制弹窗初始化
    handleFlowCopyInit (row) {
      let _that = this;
      _that.queryParams1.typeOid = row.typeOid;
      _that.queryParams1.infoOid = row.infoOid;
      _that.getList1();
      _that.copyDialogShow = true;
    },
    // 流程复制
    handleFlowCopy (row) {
      let _that = this;
      this.$confirm('是否确认复制?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        workflowCopy({
          infoOid: row.infoOid,
          newInfoOid: _that.queryParams1.infoOid
        }).then(response => {
          _that.msgSuccess("复制成功");
          _that.copyDialogShow = false;
          _that.getList();
        });
      }).catch(function () { });
    },
    districtSel (id, instanceId) {
      let _that = this;
      _that.queryParams.organOid = null;
      _that.listOrganOptions = [];
      if (!id) {
        return;
      }
      _that.getListOrganTree(id.replace('DISTRICT-', ''), (res) => {
        _that.listOrganOptions = res
      });
    },
    districtSel1 (id, callBack) {
      let _that = this;
      _that.listOrganOptions1 = [];
      callBack && callBack();
      if (!id) {
        return;
      }
      _that.getListOrganTree(id.replace('DISTRICT-', ''), (res) => {
        _that.listOrganOptions1 = res
      });
    },
    handleInit (row, operType) {
      this.viewFlag = false;
      if (operType == 1) {
        this.dialogTitle = '新增';
      } else if (operType == 2) {
        this.dialogTitle = '修改';
        this.form = this.dealRow(row);
      } else if (operType == 3) {
        this.viewFlag = true;
        this.dialogTitle = '查看';
        this.form = this.dealRow(row);
      }

      this.addDialogShow = true;

    },
    dealRow (row) {
      this.districtSel1(row.districtOid);
      this.changeAppOid(row.appOid);
      return deepClone(row);
    },
    /** 获取区划树 */
    getDistrictTree (districtOid) {
      queryDistrictSimpleTree(districtOid).then(response => {
        let districtList = JSON.parse(JSON.stringify(response.data).replace(/DISTRICT-/g, "").replace(/ORGAN-/g, ""));
        this.districtOptions = districtList;
        this.districtOptions1 = districtList;
      });
    },
    /** 获取机构数*/
    getListOrganTree (districtOid, callBack) {
      if (districtOid) {
        queryOrganTree(districtOid).then(response => {
          // this.listOrganOptions = response.data;
          callBack && callBack(JSON.parse(JSON.stringify(response.data).replace(/DISTRICT-/g, "").replace(/ORGAN-/g, "")));

        });
      }
    },
    //查询所属应用
    getAppList () {
      applist(this.queryParams).then(response => {
        this.appList = response.data;
      });
    },
    //查询所属类型
    getTypeList (appOid) {
      const parms = { appOid: appOid };
      typeList(parms).then(response => {
        this.typeList = response.data;
      });
    },
    //关闭弹框
    outerEditVisible () {
      this.dialogEditOptions.pop();
    },
    //关闭进行的流程弹框
    outerFlowViewVisible () {
      this.dialogFlowViewOptions.pop();
    },
    outerViewVisible () {
      this.dialogViewOptions.pop();
    },
    /** 查询应用列表 */
    getList () {
      let _that = this;
      _that.loading = true;
      page(_that.queryParams).then(response => {
        _that.bussinfoList = response.data.data;
        _that.total = response.data.total ? response.data.total : 0;
        _that.loading = false;
      }).catch(function () { _that.loading = false; });
    },
    /** 查询流程复制流程待选列表 */
    getList1 () {
      let _that = this;
      _that.loading1 = true;
      pageByTypeOid(_that.queryParams1).then(response => {
        _that.bussinfoList1 = response.data.data;
        _that.total1 = response.data.total ? response.data.total : 0;
        _that.loading1 = false;
      });
    },
    /** 查询流程复制流程待选列表 */
    getList2 () {
      let _that = this;
      _that.loading2 = true;
      bussHisPage(_that.queryParams2).then(response => {
        _that.bussinfoList2 = response.data.data;
        _that.total2 = response.data.total ? response.data.total : 0;
        _that.loading2 = false;
      });
    },
    // 启用状态
    statusFormat (row) {
      return this.selectMapLabel(this.ableMap, row.isAble);
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
    },
    //正常关闭
    handleVisiable () {
      this.dialogVisible = false;
      this.dialogEditOptions.pop();
    },
    //保存xml关闭
    saveHandleClose () {
      this.dialogVisible = false;
      this.dialogEditOptions.pop();
      this.getList();
    },
    handleViewModelVisiable () {
      this.dialogViewModelVisible = false
      this.dialogViewOptions.pop();
    },

    // 表单重置
    reset () {
      this.descp = '';
      this.form = {
        name: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 流程复制搜索按钮操作 */
    handleQuery1 () {
      this.queryParams1.pageNum = 1;
      this.getList1();
    },
    /** 重置按钮操作 */
    resetQuery () {
      Object.assign(this.queryParams, this.$options.data().queryParams)
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 重置按钮操作 */
    resetQuery1 () {
      this.queryParams1.workflowName = null;
      this.handleQuery1();
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.reset();
      const oid = row.id;
      getOne(oid).then(response => {
        this.form = response.data;
        this.descp = null != this.form.metaInfo ? JSON.parse(this.form.metaInfo).description : '';
        this.openView = true;
        this.title = "查看应用信息";
      });
    },
    /** 流程编辑操作 */
    handleEdit (row) {
      //增加列表刷新监听
      if ('' == this.intervalId) {
        this.intervalId = setInterval(() => {
          this.startListEventListener();
        }, 3000);
      }
      window.open("/processDesign?infoOid=" + row.infoOid, '_blank')
    },
    /** 查看流程图按钮操作 */
    handleProcessView (row) {
      window.open("/processView?infoOid=" + row.infoOid, '_blank')
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          var des = this.descp;
          this.form.descp = des;
          saveOrUpdate(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.addDialogShow = false;
              this.getList();
            }
          });
        } else {
          return false;
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const oid = row.id;
      this.$confirm('是否确认删除?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return del(oid);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () { });
    },
    /** 部署按钮操作 */
    handleDeploy (row, modelId) {
      const oid = row.id;
      this.$confirm('是否确认部署?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return deploy(oid, !modelId ? '' : modelId);
      }).then(() => {
        if (this.hisDialogShow) {
          this.getList2();
        }
        this.getList();
        this.msgSuccess("部署成功");
      }).catch(function () { });
    },
  },
  watch: {
    addDialogShow (newVal, oldVal) {
      if (!newVal) {
        this.form = this.$options.data().form;
      }
    },
    copyDialogShow (newVal, OldVal) {
      if (!newVal) {
        this.queryParams1 = this.$options.data().queryParams1;
        this.getList();
      }
    },
    hisDialogShow (newVal, OldVal) {
      if (!newVal) {
        this.queryParams2 = this.$options.data().queryParams2;
        this.getList();
      }
    }
  }
};
</script>
<style scoped>
.greenSpan {
  color: green;
}
.redSpan {
  color: red;
}
table {
  border-collapse: collapse;
}
</style>
