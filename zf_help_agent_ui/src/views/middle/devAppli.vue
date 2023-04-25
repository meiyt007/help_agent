<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="应用分类" prop="appliTypeOid">
        <treeselect
          v-model="queryParams.appliTypeOid"
          :options="devAppliTypeNameOne"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入类别名称"
        />
      </el-form-item>
      <el-form-item label="应用名称" prop="appliName">
        <el-input
          v-model.trim="queryParams.appliName"
          placeholder="请输入应用名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="启禁用">
        <el-select
          v-model="queryParams.ableStatus"
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="(value, key) in isAbleMap"
            :key="key"
            :label="value"
            :value="key"
          />
        </el-select>
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
          class="ml5"
        >重置</el-button
        >
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-delete"
          size="mini"
          @click="submitBatchForm"
        >删除</el-button
        >
      </el-col>
    </el-row>

    <el-table
      ref="multipleTable"
      :data="devAppliDtoList"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="60" align="center"> </el-table-column>
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="用户名称"
        align="center"
        prop="middleUser.userName"
        show-overflow-tooltip
      />
      <el-table-column
        label="应用名称"
        align="center"
        prop="appliName"
        show-overflow-tooltip
      />
      <el-table-column
        label="联系电话"
        align="center"
        prop="mobile"
        show-overflow-tooltip
      />
      <el-table-column
        label="应用分类"
        align="center"
        prop="devAppliType.typeName"
        show-overflow-tooltip
      />
      <el-table-column
        label="联系邮箱"
        align="center"
        prop="email"
        show-overflow-tooltip
      />
      <el-table-column
        label="启禁用状态"
        align="center"
        width="100"
        prop="ableStatus"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.ableStatus"
            :active-value="'Y'"
            :inactive-value="'N'"
            @change="handleAble(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column
        label="服务数量"
        align="center"
        prop="num"
        show-overflow-tooltip
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
            v-hasPermi="['im:devAppli:view']"
          >查看</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNumber"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 查看应用信息详细 -->
    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="70%"
      append-to-body
      height="800px"
      scrollbar
    >
      <div class="zf-zc-table--title">应用基本信息</div>
        <!--<h3>事项标题</h3>-->
        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          class="zf-zc-table"
        >
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>用户名称：</b></td>
            <td>
              {{ middleUser.userName }}
            </td>
            <td><b>应用名称：</b></td>
            <td>
              {{ devAppli.appliName }}
            </td>
          </tr>
          <tr>
            <td><b>应用图标：</b></td>
            <td>
              <div>
                <img width="100px" :src="previewImageUrl+devAppli.fastdfsNginxUrl" />
              </div>
            </td>
            <td><b>应用联系人：</b></td>
            <td>
              {{ devAppli.personName }}
            </td>
          </tr>
          <tr>
            <td><b>联系电话：</b></td>
            <td>
              {{ devAppli.mobile }}
            </td>
            <td><b>联系邮箱：</b></td>
            <td>
              {{ devAppli.email }}
            </td>
          </tr>
          <tr>
            <td><b>APP_ID：</b></td>
            <td>
              {{ devAppli.appId }}
            </td>
            <td><b>APP_KEY：</b></td>
            <td>
              {{ devAppli.appKey }}
            </td>
          </tr>
          <tr>
            <td><b>APP_SIGNATURE：</b></td>
            <td colspan="3">
              {{ devAppli.appSignature }}
            </td>
          </tr>
          <tr>
            <td><b>应用截图：</b></td>
            <td>
              <div v-for="(attaJSONObject, index) in attaJsonArray" :key="index">
                <div v-show="null != attaJSONObject.oid && '' != attaJSONObject.oid">
                  <img width="100px" :src="previewImageUrl+attaJSONObject.fastdfsNginxUrl" />
                </div>
              </div>
            </td>
            <td><b>应用分类：</b></td>
            <td>
              {{ devAppliType.typeName }}
            </td>
          </tr>
          <tr>
            <td><b>应用关键字：</b></td>
            <td>
              {{ devAppli.appliKeys }}
            </td>
            <td><b>启禁用状态：</b></td>
            <td>
              {{ devAppli.ableStatus == 'N'?'禁用':'启用' }}
            </td>
          </tr>
          <tr>
            <td><b>应用介绍：</b></td>
            <td colspan="3">
              {{ devAppli.appliMemo }}
            </td>
          </tr>
          <tr>
            <td><b>用户状态：</b></td>
            <td>
              {{ middleUser.userStatus | userStatusHandle }}
            </td>
            <td><b>用户企业名称：</b></td>
            <td>
              {{ middleUser.companyName }}
            </td>
          </tr>
          <tr>
            <td><b>用户手机：</b></td>
            <td>
              {{ middleUser.mobile }}
            </td>
            <td><b>用户电子邮件：</b></td>
            <td>
              {{ middleUser.email }}
            </td>
          </tr>
        </table>
      <div class="zf-zc-table--title">服务信息列表</div>
      <el-table v-loading="loading" :data="devAppliServerRelList">
        <el-table-column label="序号" width="50" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="用户名称"
          align="center"
          width="150"
          prop="server.middleUser.userName"
        />
        <el-table-column
          label="服务名称"
          align="center"
          width="500"
          prop="server.serverName"
        />
        <el-table-column
          label="服务版本"
          width="100"
          align="center"
          prop="server.serverVersion"
        />
        <el-table-column
          label="服务分类"
          width="300"
          align="center"
          prop="server.serverType.typeName"
        >
          <template slot-scope="scope">
            <p>{{ scope.row.server.serverType == null ? scope.row.server.serverTypeParent.typeName : scope.row.server.serverType.typeName}}</p>
          </template>
        </el-table-column>
        <el-table-column
          label="服务状态"
          width="100"
          align="center"
          prop="server.serverStatus"
        >
          <template slot-scope="scope">
            <p v-if="scope.row.server.serverStatus==0">未提交</p>
            <p v-if="scope.row.server.serverStatus==1">待审核</p>
            <p v-if="scope.row.server.serverStatus==2">审核通过</p>
            <p v-if="scope.row.server.serverStatus==3">审核不通过</p>
            <p v-if="scope.row.server.serverStatus==4">停用</p>
            <p v-if="scope.row.server.serverStatus==5">已发布</p>
            <p v-if="scope.row.server.serverStatus==6">待升级</p>
            <p v-if="scope.row.server.serverStatus==7">升级待审核</p>
            <p v-if="scope.row.server.serverStatus==8">升级审核未通过</p>
            <p v-if="scope.row.server.serverStatus==9">升级审核通过</p>
            <p v-if="scope.row.server.serverStatus==10">停用待审核</p>
            <p v-if="scope.row.server.serverStatus==11">停用审核不通过</p>
          </template>
        </el-table-column>
        <el-table-column
          label="启禁用状态"
          width="100"
          align="center"
          prop="server.ableStatus"
        >
          <template slot-scope="scope">
            <p v-if="scope.row.server.ableStatus=='N'">禁用</p>
            <p v-if="scope.row.server.ableStatus=='Y'">启用</p>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  queryDevAppliTypeJson,
  batchDelete,
  getOne,
  able
} from "@/api/middle/devAppli";
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  name: "DevAppli",
  components: { Treeselect},
  data () {
    return {
      // 遮罩层
      loading: true,
      isAbleMap: { "N": "禁用", "Y": "启用" },
      // 总条数
      total: 0,
      title: "",
      // 数据表格
      devAppliDtoList: [],
      // 分类数据
      devAppliTypeNameOne: [],
      multipleSelection: [],
      // 查看显示弹出层
      openView: false,
      devAppliServerRelList: [],
      // 启用状态
      ableMap: { "Y": "启用", "N": "禁用" },
      // 预览接口
      previewImageUrl: '/case-api/pic/previewImage?fastdfsNginxUrl=',
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        ableStatus: "",
        appliName: "",
        appliTypeOid: null
      },
      devAppli: {},
      middleUser: {},
      devAppliType: {},
      attaJsonArray: {},
      viewDialogOptions: [],
      rules: {
        fieldTypeName: [
          { required: true, message: "类别名称不能为空", trigger: "blur" }
        ],
        sort: [{ required: true, message: "排序号不能为空", trigger: "blur" }]
      }
    };
  },
  watch: {},
  created () {
    this.getList();
    this.queryDevAppliTypeJson();
  },
  methods: {
    /** 查询业务层级列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.devAppliDtoList = response.data.datas;
        this.total = response.data.totalRows;
        this.loading = false;
      });
    },
    handleSelectionChange(val){
      this.multipleSelection = val;
    },
    queryDevAppliTypeJson(){
      queryDevAppliTypeJson().then(response => {
        this.devAppliTypeNameOne = response.data;
      });
    },
    /** 查看按钮操作 */
    handleView (row) {
      const oid = row.oid;
      getOne(oid).then(response => {
        this.devAppli = response.data.devAppli;
        this.middleUser = response.data.devAppli.middleUser;
        this.devAppliType = response.data.devAppli.devAppliType;
        this.devAppliServerRelList = response.data.devAppliServerRelList;
        this.attaJsonArray = response.data.attaJsonArray;
        this.openView = true;
        this.title = "查看应用信息";
      });
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNumber = 1;
      this.queryParams.pageSize = 10;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.queryParams = {};
      this.handleQuery();
    },
    // 取消按钮
    cancel () {
      this.openView = false;
    },
    /** 删除按钮操作 */
    submitBatchForm: function() {
      let _that = this;
      var oid = [];
      if (_that.multipleSelection.length <= 0) {
        _that.$message.error("请选择删除!");
        return false;
      }
      for (let ss = 0; ss < _that.multipleSelection.length; ss++) {
        oid.push(_that.multipleSelection[ss].oid);
      }
      var oids = oid.join(",");
      batchDelete(oids).then(response => {
        if (response.code === 200) {
          _that.msgSuccess("删除成功");
          this.getList();
        }
      });
    },
    /** 启禁用按钮操作 */
    handleAble (row) {
      const oid = row.oid;
      let ableMessage = "";
      let msg = "";
      if (row.ableStatus === 'Y') {
        ableMessage = "你确定要启用吗？";
        msg = "启用";
      } else {
        ableMessage = "禁用类别可能会导致对应标签数据出错，是否确认禁用?";
        msg = "禁用";
      }
      this.$confirm(ableMessage, "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return able(oid);
        })
        .then(() => {
          this.msgSuccess(msg + "成功");
        })
        .catch(function () {
          row.ableStatus = row.ableStatus === 'N' ? 'Y' : 'N';
        });
    }
  },
  filters:{
    userStatusHandle(data) {
      if(!data) {
        return '待审核'
      }
      const statusMap = {
        0: '新建',
        1: '待审核',
        2: '审核成功',
        3: '审核失败'
      }
      return statusMap[data]
    }
  }
};
</script>
<style></style>
