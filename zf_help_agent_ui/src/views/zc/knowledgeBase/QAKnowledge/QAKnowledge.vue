/*
* @Author: kkfan
* @Date: 2020-10-29 10:36:55
* @Last Modified by: kkfan
* @Last Modified time: 2020-10-31 11:17:21
* @Description: 知识库管理
*/
<template>
  <div class="app-container">
    <el-row :gutter="20" style="height: 100%">
      <!--数字字段树数据-->
      <el-col :span="4" :xs="24" class="app-left" style="height: 100%">
        <div class="tree-title">知识库分类字典</div>
        <div class="head-container">
          <el-input
            v-model.trim="dictName"
            placeholder="请输入字典名称"
            clearable
            size="small"
            prefix-icon="iconfont zfsoft-chakan"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container" style="height: calc(100% - 52px - 35px)">
          <el-tree
            v-if="districtOptionsnew.length > 0"
            :data="districtOptionsnew"
            :props="defaultProps"
            :expand-on-click-node="true"
            :highlight-current="true"
            :filter-node-method="filterNode"
            ref="tree"
            accordion
            node-key="id"
            :default-expanded-keys="[1]"
            @node-click="handleNodeClick"
          >
            <template slot-scope="{ node }">
              <div
                style="
                  font-size: 14px;
                  width: 240px;
                  overflow: hidden;
                  white-space: nowrap;
                  text-overflow: ellipsis;
                "
                :title="node.label"
              >
                <span class="overflow-x: auto"> {{ node.label }}</span>
              </div>
            </template>
          </el-tree>
          <el-empty v-else style="height: 100%" />
        </div>
      </el-col>
      <!--数字字典数据-->
      <el-col :span="20" :xs="24" class="app-right" style="height: 100%">
        <el-form :model="queryParams" ref="queryForm" :inline="true">
          <el-form-item label="编号" prop="knowledgeNumber" label-width="50px">
            <el-input
              v-model.trim="queryParams.knowledgeNumber"
              placeholder="请输入编号"
              clearable
              size="32"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="事项名称" prop="serviceName">
            <el-input
              v-model.trim="queryParams.serviceName"
              placeholder="请输入事项名称"
              clearable
              size="100"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="知识库标题" prop="knowledgeTitle">
            <el-input
              v-model.trim="queryParams.knowledgeTitle"
              placeholder="请输入知识库标题"
              clearable
              size="100"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
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
              class="btn-reset"
              >重置
            </el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit"
              v-hasPermi="['sys:qaKnowle:init']"
              >新增
            </el-button>
          </el-col>
        </el-row>
        <el-table
          v-loading="loading"
          :data="humanEvidenceDeviceList"
          border
          :fit="true"
          height="calc(100% - 160px)"
        >
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="编号"
            width="100"
            align="center"
            prop="knowledgeNumber"
          />
          <el-table-column
            label="知识库标题"
            align="center"
            prop="knowledgeTitle"
            :show-overflow-tooltip="true"
          />
          <!-- <el-table-column label="关键字" width="300" align="center" prop="knowledgeKeyword"/> -->
          <el-table-column
            label="知识库分类"
            align="center"
            prop="zskDictTxt"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="事项名称"
            align="center"
            prop="serviceName"
            :show-overflow-tooltip="true"
          />
          <!--<el-table-column label="创建时间" width="160" align="center" prop="createDate"/>-->
          <!--<el-table-column label="更新时间" width="160" align="center" prop="modifyDate"/>-->
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
                v-hasPermi="['sys:qaKnowle:view']"
                >查看
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleInit(scope.row)"
                v-hasPermi="['sys:qaKnowle:init']"
                >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleDelete(scope.row)"
                v-hasPermi="['sys:qaKnowle:delete']"
                >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>
    <!-- 选择窗口弹窗页面 -->
    <el-dialog
    v-dialog-drag
      :close-on-click-modal="false"
      title="选择事项"
      :visible.sync="selWinShow"
      v-if="selWinShow"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <el-form
        :model="queryParams1"
        ref="queryForm1"
        :inline="true"
        label-width="108px"
      >
        <el-row>
          <el-form-item label="实施编码" prop="implementCode">
            <el-input
              v-model.trim="queryParams1.implementCode"
              placeholder="请输入实施编码"
              clearable
              size="32"
              @keyup.enter.native="handleQuery1"
            />
          </el-form-item>
          <el-form-item label="事项名称" prop="serviceName">
            <el-input
              v-model.trim="queryParams1.serviceName"
              placeholder="请输入事项名称"
              clearable
              size="100"
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
              class="btn-reset"
              >重置
            </el-button>
          </el-form-item>
        </el-row>
      </el-form>
      <el-table
        ref="multipleTable"
        :data="windowList"
        tooltip-effect="dark"
        height="255"
        style="width: 100%"
        @current-change="clickChange"
      >
        <el-table-column label="请选择" width="100" align="center">
          <template slot-scope="scope">
            <el-radio v-model="form.serviceOid" :label="scope.row.serviceOid"
              ><i></i
            ></el-radio>
          </template>
        </el-table-column>
        <el-table-column
          label="序号"
          width="80"
          header-align="center"
          align="center"
        >
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="serviceName"
          label="事项名称"
          header-align="center"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="basicCode"
          label="基本编码"
          header-align="center"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="implementCode"
          label="实施编码"
          header-align="center"
          align="center"
        ></el-table-column>
      </el-table>
      <pagination
        v-show="queryParams1.total > 0"
        :total="queryParams1.total"
        :page.sync="queryParams1.pageNum"
        :limit.sync="queryParams1.pageSize"
        @pagination="getListForWin"
      />
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitSelWin">确 定</el-button>
        <el-button
          @click="
            () => {
              selWinShow = false
            }
          "
          >取 消</el-button
        >
      </div>
    </el-dialog>
    <!-- 添加或修改对话框 -->
    <el-dialog
    v-dialog-drag
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
        class="dialog-table"
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
              <i class="require">*</i>
              <b>知识库分类：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="zskDictOid">
                <el-col :span="24">
                  <treeselect
                    :options="dictOptions"
                    :show-count="true"
                    :normalizer="normalizer"
                    v-model="form.zskDictOid"
                    placeholder="请选择知识库分类"
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <i class="require">*</i>
              <b>事项名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="serviceName">
                <el-col :span="24">
                  <el-input
                    :title="form.serviceName"
                    @focus="selWinShowEvt"
                    v-model.trim="form.serviceName"
                    placeholder="请选择事项名称"
                    maxlength="200"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <i class="require">*</i>
              <b>知识库标题：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="knowledgeTitle">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.knowledgeTitle"
                    placeholder="请输入知识库标题"
                    maxlength="100"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <i class="require">*</i>
              <b>知识库内容：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="knowledgeContent">
                <el-col :span="24">
                  <el-input
                    type="textarea"
                    v-model.trim="form.knowledgeContent"
                    placeholder="请输入知识库内容"
                    maxlength="500"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>政策依据：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="policyBasis">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.policyBasis"
                    placeholder="请输入政策依据"
                    maxlength="100"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>政策版本或发文字号：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="knowledgeVersion">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.knowledgeVersion"
                    placeholder="请输入政策版本或发文字号"
                    maxlength="32"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>发文机关：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="issuingAuthority">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.issuingAuthority"
                    placeholder="请输入发文机关"
                    maxlength="32"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button
          @click="
            () => {
              addDialogShow = false
              reset()
            }
          "
          >取 消</el-button
        >
      </div>
    </el-dialog>

    <!-- 详细 -->
    <el-dialog
    v-dialog-drag
      :title="dialogTitle"
      :visible.sync="detailDialogShow"
      v-if="detailDialogShow"
      width="900px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="dialog-table"
      >
        <table cellspacing="0" cellpadding="0" border="0">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>知识库分类：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="zskDictOid">
                <el-col :span="24">
                  <treeselect
                    :options="dictOptions"
                    :show-count="true"
                    :normalizer="normalizer"
                    v-model="form.zskDictOid"
                    placeholder="请选择知识库分类"
                    :disabled="true"
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>事项名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="serviceName">
                <el-col :span="24">
                  {{ form.serviceName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>知识库标题：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="knowledgeTitle">
                <el-col :span="24">
                  {{ form.knowledgeTitle }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>知识库内容：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="knowledgeContent">
                <el-col :span="24">
                  {{ form.knowledgeContent }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>政策依据：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="policyBasis">
                <el-col :span="24">
                  {{ form.policyBasis }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>政策版本或发文字号：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="knowledgeVersion">
                <el-col :span="24">
                  {{ form.knowledgeVersion }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>发文机关：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="issuingAuthority">
                <el-col :span="24">
                  {{ form.issuingAuthority }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button @click="detailDialogShow = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { page, deletes, saveOrUpdate, getOne } from "@/api/zc/knowledgeBase/QAKnowledge";
import { page as SxServicePage } from "@/api/zc/businessManagement/sxServiceRegistrar";
import { queryTree } from "@/api/zc/knowledgeBase/knowledgeBaseClassification";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { validateNumber, validIntNoZero } from "@/utils/validate";
import { deepClone } from "@/utils/index";
import { queryOrganTree } from "@/api/sys/organ";
import { getDictList } from "@/api/sys/common";

// import the component
import Treeselect from '@riophae/vue-treeselect';
// import the styles
import '@riophae/vue-treeselect/dist/vue-treeselect.css';

export default {
  name: "QAKnowledge",
  components: { Treeselect },
  data () {
    return {
      windowList: [], // 事项列表total
      tableRadio: null, // 事项选中行数据
      selWinShow: false, // 选择事项名称弹窗
      //查询字典名称参数
      dictName: '',
      // 总条数
      total: 0,
      // 选择事项名称弹窗查询参数
      queryParams1: {
        pageNum: 1,
        pageSize: 10,
        total: 0, // 分页参数
        implementCode: null,    // 实施编码
        serviceName: null,  // 事项名称
      },
      dictOptions: [],    // 分类树
      // 列表数据
      humanEvidenceDeviceList: [],
      // 弹窗标题
      dialogTitle: '',
      addDialogShow: false,
      detailDialogShow: false,
      // 楼层数据列表
      inFloorList: [],
      // 机构
      listOrganOptions: [],
      // 区划树
      districtOptions: [],
      districtOptionsnew: [],

      defaultProps: {
        children: "childrenList",
        label: "name"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        total: 0, // 分页参数
        knowledgeTitle: null,
        knowledgeNumber: null,
        serviceName: null,
        zskDictOid: null
      },
      // 表单参数
      form: {
        id: '', //逻辑主键
        zskDictOid: null, // 分类id
        serviceName: null, // 事项名称
        knowledgeTitle: null, // 知识库标题
        knowledgeContent: null,    // 知识库内容
        policyBasis: '', // 政策依据
        knowledgeVersion: '',  // 政策版本或发文字号
        issuingAuthority: '',    // 发文机关
        serviceOid: '',
      },
      // 表单校验
      rules: {
        zskDictOid: [
          { required: true, message: "请选择知识库分类", trigger: "change" }
        ],
        serviceName: [
          { required: true, message: "请选择事项名称", trigger: "change" }
        ],
        knowledgeTitle: [
          { required: true, message: "知识库标题不能为空", trigger: "change" }
        ],
        knowledgeContent: [
          { required: true, message: "知识库内容不能为空", trigger: "change" }
        ],
      }
    };
  },
  methods: {

    // 选择窗口确定按钮
    submitSelWin (row) {
      if (!this.tableRadio) {
        let oidGet = this.form.serviceOid;
        if (oidGet != '') {
          let nameGet = this.form.serviceName;
          this.tableRadio = new Object();
          this.tableRadio.serviceOid = oidGet;
          this.tableRadio.serviceName = nameGet;
        }
      }
      
      if (!this.tableRadio) {
        this.$message.error('尚未选择事项!');
        return;
      }
      this.form.serviceName = this.tableRadio.serviceName;
      this.form.serviceOid = this.tableRadio.serviceOid;
      this.selWinShow = false;
      this.tableRadio = null;
       

     

    },
    clickChange (item) {
      this.tableRadio = item
    },
    // 选择事项名称
    selWinShowEvt (e) {
      e.currentTarget.blur();
      Object.assign(this.queryParams1, this.$options.data().queryParams1)
      this.selWinShow = true;
      this.getListForWin();
    },
    /** 查询事项列表 */
    getListForWin () {
      let _that = this;
      SxServicePage(_that.queryParams1).then(response => {
        _that.windowList = response.data.data;
        _that.queryParams1.total = response.data.total;
        _that.loading = false;
      });
    },
    /** 自定义树显示属性 */
    normalizer (node) {
      if (node.childrenList && !node.childrenList.length) {
        delete node.childrenList;
      }
      return {
        id: node.zskDictOid,
        label: node.name,
        children: node.childrenList,
      }
    },
    /** 获取数字字典树 */
    getTree (parentOid, callback) {
      queryTree(parentOid).then(response => {
        this.dictOptions = response.data[0].childrenList;
        this.districtOptionsnew = response.data;
        callback && callback(response.data);
      });
    },
    // 筛选节点
    filterNode (value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick (data) {
      this.currentNode = data;
      if (data.parentOid == null) {
        this.queryParams.zskDictOid = null;
      } else {
        this.queryParams.zskDictOid = data.zskDictOid;
      }

      this.getList();
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 搜索按钮操作 */
    handleQuery1 () {
      this.queryParams1.pageNum = 1;
      this.getListForWin();
    },
    /** 查询受理辖区列表 */
    getList () {
      //alert(this.queryParams.zskDictOid +"***"+"05e6829e5c004d51bddf7fae2d9f3fe9" )
      this.loading = true;
      page(this.queryParams).then(response => {
        this.humanEvidenceDeviceList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 重置按钮操作 */
    resetQuery1 () {
      this.resetForm("queryForm1");
      this.handleQuery1();
    },
    // 表单重置
    reset () {
      Object.assign(this.form, this.$options.data().form)
      this.resetForm("form");
    },
    /** 查看按钮操作 */
    handleView (row) {
      let _that = this;
      _that.reset();
      const oid = row.id;
      getOne(oid).then(response => {
        if (response.data.districtOid) {
          _that.getListOrganTree(response.data.districtOid, () => {
            _that.form.organOid = response.data.organOid;
            _that.form.organName = response.data.organName;
          });
        }
        _that.form = deepClone(response.data);
        _that.detailDialogShow = true;
        _that.dialogTitle = "查看详情";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit (row) {
      let _that = this;
      _that.reset();
      if (row.id) {
        getOne(row.id).then(response => {
          _that.addDialogShow = true;
          if (response.data.districtOid) {
            _that.getListOrganTree(response.data.districtOid, () => {
              _that.form.organOid = response.data.organOid;
              _that.form.organName = response.data.organName;

            });
          }
          _that.form = deepClone(response.data);
        });
      } else {
        _that.currentTarget = null;
        _that.form.serviceOid = null;
        _that.form.serviceName = null;
        _that.addDialogShow = true;
      }
      _that.dialogTitle = row.id ? "修改" : "新增";
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      let id = row.id;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return deletes(id);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdate(_that.form).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("保存成功");
              _that.addDialogShow = false;
              setTimeout(() => {
                _that.getList();
              }, 10);
            }
          });
        }
      });
    },
  },
  watch: {
    // 根据名称筛选部门树
    dictName (val) {
      this.$refs.tree.filter(val);
    }
  },
  created () {
    let _that = this;
    _that.getList();
    _that.getTree(null, (data) => {
      if (!data) {
        return;
      }
      _that.$nextTick(() => {
        document.querySelector('.el-tree-node__content').click()
      })
    });
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
</style>
<style lang="scss" scoped>
.el-tree > .el-tree-node {
  min-width: 100%;
  display: inline-block;
}
.el-table__body tr td:first-child {
  text-align: right;
}
</style>
