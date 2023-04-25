<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="110px"
    >
      <el-form-item label="所属区划">
        <treeselect
          class="treeselect"
          :defaultExpandLevel="1"
          noOptionsText="暂无数据"
          noResultsText="暂无数据"
          :show-count="true"
          v-model="queryParams.districtOidSelect"
          :options="districtOptions"
          placeholder="请输入所属区划"
        />
      </el-form-item>
      <el-form-item label="组织机构名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入组织机构名称"
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
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          class="btn-reset"
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
          @click="handleInit"
          v-hasPermi="['sys:organ:init']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['sys:organ:listExp']"
          >导出</el-button
        >
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="organList" border>
      <!--row-key="organOid"
            :tree-props="{children: 'children', hasChildren: 'hasChildren'}"-->
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
      >
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" placement="top">
            <div v-html="scope.row.districtName" slot="content"></div>
            <div class="oneLine">{{ scope.row.districtName }}</div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        label="组织机构名称"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="组织机构全称"
        align="center"
        prop="fullName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="组织机构代码"
        align="center"
        prop="code"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="组织机构电话" align="center" prop="telphone" />
      <el-table-column
        label="组织机构类别"
        align="center"
        prop="typeDictName"
      />
      <!--<el-table-column label="状态" align="center"  width="100" prop="isAble" :formatter="statusFormat"/>-->
      <el-table-column
        label="启用状态"
        align="center"
        width="100"
        prop="isAble"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isAble"
            :active-value="1"
            :inactive-value="0"
            @change="handleAble(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        width="325"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakanyonghu"
            @click="handleUserList(scope.row)"
            v-hasPermi="['sys:organ:userListByOrgan']"
            >查看用户</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-zijigou"
            @click="
              handleChildView(
                scope.row.name,
                scope.row.organOid,
                scope.row.districtName,
                scope.row.districtOid
              )
            "
            v-hasPermi="['sys:organ:view']"
            >子机构</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:organ:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:organ:update']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sys:organ:delete']"
            >删除</el-button
          >
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

    <!-- 添加或修改组织机构信息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      width="70%"
      append-to-body
    >
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.organOid" />
          <el-input v-show="false" v-model="form.isAble" />
          <el-input v-show="false" v-model="form.isDelete" />
          <el-input v-show="false" v-model="form.parentOid" />
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>上级组织机构：</b></td>
              <td>
                <el-form-item>
                  {{ form.parentName }}
                </el-form-item>
              </td>

              <td><i class="require">*</i><b>所属区划：</b></td>
              <td>
                <el-form-item prop="districtOidSelect">
                  <treeselect
                    class="treeselect240"
                    :defaultExpandLevel="1"
                    noOptionsText="暂无数据"
                    noResultsText="暂无数据"
                    :show-count="true"
                    v-model="form.districtOidSelect"
                    :options="districtOptions"
                    placeholder="请输入所属区划"
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>组织机构名称：</b></td>
              <td>
                <el-form-item prop="name">
                  <el-input
                    v-model.trim="form.name"
                    placeholder="请输入组织机构名称"
                    maxlength="100"
                    show-word-limit
                  />
                </el-form-item>
              </td>

              <td><i class="require">*</i><b>组织机构全称：</b></td>
              <td>
                <el-form-item prop="fullName">
                  <el-input
                    v-model.trim="form.fullName"
                    placeholder="请输入组织机构全称"
                    maxlength="50"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require"></i><b>组织机构类别：</b></td>
              <td>
                <el-form-item prop="typeDictOid">
                  <el-select v-model="form.typeDictOid" placeholder="请选择">
                    <el-option
                      v-for="dict in typeDictOptions"
                      :key="dict.dictOid"
                      :label="dict.name"
                      :value="dict.dictOid"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </td>

              <td><i class="require"></i><b>组织机构代码：</b></td>
              <td>
                <el-form-item prop="code">
                  <el-input
                    v-model.trim="form.code"
                    placeholder="请输入组织机构代码"
                    maxlength="9"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require"></i><b>组织机构简码：</b></td>
              <td>
                <el-form-item prop="simpleCode">
                  <el-input
                    v-model.trim="form.simpleCode"
                    placeholder="请输入组织机构简码"
                    maxlength="25"
                    show-word-limit
                  />
                </el-form-item>
              </td>

              <td><i class="require"></i><b>组织机构地址：</b></td>
              <td>
                <el-form-item prop="address">
                  <el-input
                    v-model.trim="form.address"
                    placeholder="请输入组织机构地址"
                    maxlength="200"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>社会信用代码：</b></td>
              <td>
                <el-form-item prop="uniteCode">
                  <el-input
                    v-model.trim="form.uniteCode"
                    placeholder="请输入社会信用代码"
                    maxlength="18"
                    show-word-limit
                  />
                </el-form-item>
              </td>

              <td><i class="require"></i><b>组织机构电话：</b></td>
              <td>
                <el-form-item prop="telphone">
                  <el-input
                    v-model.trim="form.telphone"
                    placeholder="请输入组织机构电话"
                    maxlength="13"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>排序号：</b></td>
              <td colspan="3">
                <el-form-item prop="sort">
                  <el-input-number v-model="form.sort" :min="1" :max="9999" />
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
    </el-dialog>

    <!-- 查看组织机构信息详细 -->
    <el-dialog
      :title="title"
      :visible.sync="openView"
      width="70%"
      append-to-body
    >
      <div class="el-table__header-wrapper dialog-table">
        <el-form :model="form" label-width="0px">
          <table cellspacing="0" cellpadding="0" border="0">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>上级组织机构：</b></td>
              <td>
                {{ form.parentName }}
              </td>
              <td><b>所属区划：</b></td>
              <td>
                {{ form.districtName }}
              </td>
            </tr>
            <tr>
              <td><b>组织机构名称：</b></td>
              <td>
                {{ form.name }}
              </td>
              <td><b>组织机构全称：</b></td>
              <td>
                {{ form.fullName }}
              </td>
            </tr>
            <tr>
              <td><b>组织机构类别：</b></td>
              <td>
                {{ form.typeDictName }}
              </td>
              <td><b>组织机构代码：</b></td>
              <td>
                {{ form.code }}
              </td>
            </tr>
            <tr>
              <td><b>组织机构简码：</b></td>
              <td>
                {{ form.simpleCode }}
              </td>
              <td><b>组织机构地址：</b></td>
              <td>
                {{ form.address }}
              </td>
            </tr>
            <tr>
              <td><b>社会信用代码：</b></td>
              <td>
                {{ form.uniteCode }}
              </td>
              <td><b>组织机构电话：</b></td>
              <td>
                {{ form.telphone }}
              </td>
            </tr>
            <tr>
              <td><b>排序号：</b></td>
              <td colspan="3">
                {{ form.sort }}
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in dialogOptions"
      @close="outerVisible"
      :title="item.title"
      width="90%"
      append-to-body
    >
      <el-scrollbar style="height: 500px">
        <index-child
          :childParentOid="item.parentOid"
          :childOrganName="item.title"
          :childDistrictOid="item.districtOid"
          :childDistrictName="item.districtName"
          @father-click="handleChildView"
        ></index-child>
      </el-scrollbar>
    </el-dialog>

    <el-dialog
      v-dialog-drag
      :visible.sync="user.show"
      title="查看用户列表"
      v-for="user in userDialogOptions"
      @close="closeUserView"
      width="70%"
      scrollbar
      height="600px"
      append-to-body
    >
      <index-user :organOid="user.organOid"></index-user>
    </el-dialog>
  </div>
</template>

<script>
import { init, save, del, getOne, page, able, listExp } from "@/api/sys/organ";
import { validatePhoneTwo, validIntNoZero, validateLegalStr, validateUniteCode } from '@/utils/validate';
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import IndexChild from '@/views/sys/organ/indexChild';
import IndexUser from '@/views/sys/organ/indexUser';
export default {
  name: "Organ",
  components: { Treeselect, IndexChild, IndexUser },
  data () {
    return {
      //查看子组织机构弹框
      dialogOptions: [],
      //查看用户弹框
      userDialogOptions: [],
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 组织机构表格数据
      organList: [],
      // 弹出层标题
      title: "",
      //新增或者修改
      openInit: false,
      // 查看显示弹出层
      openView: false,
      //查看用户
      openUserView: false,
      //消息
      ableMap: { '1': '启用', '0': '禁用' },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        districtOid: null,
        parentOid: '',
        districtOidSelect: null
      },
      // 表单参数
      form: {},
      //父类参数
      parentOrgan: [],
      //类型集合
      typeDictOptions: {},
      //区划Tree
      districtOptions: [],
      // 表单校验
      rules: {
        districtOidSelect: [
          { required: true, message: "请选择所属区划", trigger: "blur" }
        ],
        name: [
          { required: true, message: "组织机构名称不能为空", trigger: "blur" }
        ],
        fullName: [
          { required: true, message: "组织机构全称不能为空", trigger: "blur" }
        ],
        // typeDictOid: [
        //   { required: true, message: "请选择组织机构类别ss", trigger: "blur" }
        // ],
        // code: [
        //   { required: true, message: "组织机构代码不能为空", trigger: "blur" },
        //   { min: 9, max: 9, message: "请输入9位长度的组织机构代码" },
        //   { validator: validateLegalStr, trigger: 'blur' }
        // ],
        // simpleCode: [
        //   { required: true, message: "组织机构简码不能为空", trigger: "blur" },
        //   { validator: validateLegalStr, trigger: 'blur' }
        // ],
        // address: [
        //   { required: true, message: "组织机构地址不能为空", trigger: "blur" }
        // ],
        // telphone: [
        //   { required: true, message: "组织机构电话不能为空", trigger: "blur" },
        //   { validator: validatePhoneTwo, trigger: 'blur' }
        // ],
        uniteCode: [
          { validator: validateUniteCode, trigger: 'blur' }
        ],
        sort: [
          { required: true, message: "排序号不能为空", trigger: "blur" },
          { validator: validIntNoZero, trigger: 'blur' }
        ]
      }
    };
  },
  watch: {
    'queryParams.districtOidSelect': {
      handler (val, oldVal) {
        let dataId = null != val ? val.substring(val.lastIndexOf('-') + 1, val.length) : '';
        this.queryParams.districtOid = dataId;
      }
    },
    'form.districtOidSelect': {
      handler (val, oldVal) {
        let dataId = null != val ? val.substring(val.lastIndexOf('-') + 1, val.length) : '';
        this.form.districtOid = dataId;
      }
    },
  },
  created () {
    this.getDistrictTree();
    this.getList();
    /** 数据字典中保存组织机构类型的数据项 */
    this.getDictList("ZZJGLX").then(response => {
      this.typeDictOptions = response.data;
    });
  },
  methods: {
    /** 查询组织机构列表 */
    getList () {
      this.loading = true;
      let that = this;
      page(this.queryParams).then(response => {
        //this.organList = handleTree(response.data.data, "organOid");
        this.organList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      }).catch(function () { that.loading = false; });
    },
    /** 获取区划树 */
    getDistrictTree (districtOid) {
      this.queryDistrictSimpleTree(districtOid).then(response => {
        this.districtOptions = response.data;
      });
    },
    // 消息
    statusFormat (row) {
      return this.selectMapLabel(this.ableMap, row.isAble);
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset () {
      this.form = {
        name: null,
        districtOid: null,
        districtName: null,
      };
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
      this.queryParams.districtOidSelect = undefined;
      this.queryParams.districtOid = '';
      this.handleQuery();
    },
    //查看用户
    handleUserList (row) {
      let item = { show: true, organOid: row.organOid };
      this.userDialogOptions.push(item);
    },
    //查看子机构
    handleChildView (name, oid, districtName, districtOid) {
      let item = { title: name, show: true, parentOid: oid, districtName: districtName, districtOid: districtOid };
      this.dialogOptions.push(item);
    },
    //关闭
    outerVisible () {
      this.dialogOptions.pop();
      this.getList();
    },
    // 关闭按钮
    closeUserView () {
      this.userDialogOptions.pop();
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.reset();
      const oid = row.id;
      getOne(oid).then(response => {
        this.form = response.data;
        this.openView = true;
        this.title = "查看组织机构信息";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit (row) {
      this.reset();
      const oid = row.id;
      if (oid == undefined) {
        this.openInit = true;
        this.title = "新增组织机构信息";
      } else {
        init(oid).then(response => {
          let result = response.data;
          if (null != result.districtOid && '' != result.districtOid) {
            result.districtOidSelect = 'DISTRICT-' + result.districtOid;
          }
          this.form = result;
          this.openInit = true;
          this.title = "修改组织机构信息";
        });
      }
    },
    /** 提交按钮 */
    submitForm: function () {
      debugger
      this.$refs["form"].validate(valid => {
        if (valid) {
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
    /** 启禁用按钮操作 */
    handleAble (row) {
      const oid = row.id;
      let ableMessage = row.isAble == 1 ? '启用' : '禁用'
      this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return able(oid);
      }).then(() => {
        this.msgSuccess(ableMessage + "成功");
      }).catch(function () {
        row.isAble = row.isAble == 0 ? 1 : 0
      });
    },
    /** 导出按钮操作 */
    handleExport () {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出列表数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        listExp(queryParams);
        if (queryParams.districtOid == '') {
          queryParams.districtOid = undefined;
        }
      }).catch(function () { });
    }
  }
};
</script>
<style scoped>
.treeselect {
  width: 200px !important;
}
/*.treeselect240{
    width: 240px !important;
  }*/
table {
  border-collapse: collapse;
}
.oneLine {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
