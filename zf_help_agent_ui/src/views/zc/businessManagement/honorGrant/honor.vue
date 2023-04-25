<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="所属区划">
        <treeselect
          v-model="queryParams.districtOidSelect"
          :options="districtOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入所属区划"
        />
      </el-form-item>
      <el-form-item label="所属机构">
        <treeselect
          v-model="queryParams.organOidSelect"
          :options="listOrganOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入所属机构"
        />
      </el-form-item>
      <el-form-item label="姓名">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入姓名"
          clearable
          size="small"
          style="width: 240px"
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
    <el-table
      v-loading="loading"
      :data="userList"
      border
      height="calc(100% - 120px)"
    >
      <el-table-column label="序号" align="center" type="index" />
      <el-table-column
        label="所属区划"
        align="center"
        prop="districtName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="所属机构"
        align="center"
        prop="organName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="姓名"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        width="260"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:user:view']"
            >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shouquan"
            @click="handleRole(scope.row)"
            v-hasPermi="['sys:user:shouquan']"
            >配置
          </el-button>
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
    <!-- 查看用户配置对话框 -->
    <el-dialog
      v-dialog-drag
      :title="title"
      :visible.sync="openView"
      :close-on-click-modal="false"
      width="900px"
      append-to-body
    >
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td><b>所属区划：</b></td>
          <td>
            {{ form.districtName }}
          </td>
          <td><b>所属机构：</b></td>
          <td>
            {{ form.organName }}
          </td>
        </tr>
        <tr>
          <td><b>姓名：</b></td>
          <td>
            {{ form.name }}
          </td>
          <td><b>出生年月：</b></td>
          <td>
            {{ form.birthdate }}
          </td>
        </tr>
        <tr>
          <td><b>用户性别：</b></td>
          <td>
            {{ reversedSex }}
          </td>
          <td><b>职务：</b></td>
          <td>
            {{ form.position }}
          </td>
        </tr>
        <tr>
          <td><b>身份证号：</b></td>
          <td>
            {{ form.cardNo }}
          </td>
          <td><b>邮箱：</b></td>
          <td>
            {{ form.email }}
          </td>
        </tr>
        <tr>
          <td><b>手机：</b></td>
          <td>
            {{ form.mobile }}
          </td>
          <td><b>座机：</b></td>
          <td>
            {{ form.telphone }}
          </td>
        </tr>
        <!-- <tr>
              <td><b>类型：</b></td>
              <td>
                {{ form.typeName }}
              </td>
              <td><b>数据权限：</b></td>
              <td>
                <el-form-item
                  v-for="(data, index) in dataDuthorityMap"
                  :key="index"
                  v-if="data.id == form.dataAuthority"
                >
                  {{ data.value }}
                </el-form-item>
              </td>
            </tr> -->
        <!-- <tr>
              <td><b>排序号：</b></td>
              <td colspan="3">
                {{ form.sort }}
              </td>
            </tr> -->
        <!-- <tr>
              <td><b>所属岗位：</b></td>
              <td colspan="3">
                <span
                  v-for="(post, index) in postList"
                  :key="index"
                  v-if="
                    !form.postOids && form.postOids.indexOf(post.postOid) != -1
                  "
                >
                  {{ post.name }}&nbsp;&nbsp;
                </span>
              </td>
            </tr> -->
        <tr>
          <td><b>员工荣誉：</b></td>
          <td colspan="3">
            <span v-for="(data, index) in manHonors" :key="index">
              {{ data.name }}&nbsp;&nbsp;
            </span>
          </td>
        </tr>
      </table>

      <div slot="footer" class="zf-text-center">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--授权角色-->
    <el-dialog
      :title="title"
      :visible.sync="openRole"
      v-if="openRole"
      :close-on-click-modal="false"
      width="900px"
      v-dialog-drag
    >
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td><b>所属区划：</b></td>
          <td>
            {{ roleForm.districtName }}
          </td>
          <td><b>所属机构：</b></td>
          <td>
            {{ roleForm.organName }}
          </td>
        </tr>
        <tr>
          <td><b>姓名：</b></td>
          <td>
            {{ roleForm.name }}
          </td>
          <td><b>职务：</b></td>
          <td>
            {{ roleForm.position }}
          </td>
        </tr>
        <tr>
          <td><b>类型：</b></td>
          <td colspan="3">
            {{ roleForm.typeName }}
          </td>
        </tr>
        <tr>
          <td><b>荣誉：</b></td>
          <td colspan="3">
            <el-checkbox-group v-model="honorCheckList" size="medium">
              <el-checkbox
                v-for="(item, index) in honorList"
                :key="index"
                :label="item.code"
                >{{ item.name }}</el-checkbox
              >
            </el-checkbox-group>
          </td>
        </tr>
      </table>

      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitRole">确 定</el-button>
        <el-button @click="openRole = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  save,
  saveEncrypt,
  page,
  able,
  del,
  get,
  init,
  getUserRole,
  saveUserRole,
  getHonorList,
  getOneHonorList,
  saveOrUpdateWorkerHonor
} from "@/api/sys/user";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import iconfont from "@/views/common/iconfontSelect";
import {
  validIntNoZero,
  validIDCard,
  validatePhoneTwo,
  validatePassword,
  validateEmail
} from "@/utils/validate";
import { queryOrganTree } from "@/api/sys/organ";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { getDictList } from "@/api/sys/dict";
import { roleTree } from "@/api/sys/role";
import { getKey } from "@/api/login";
import { encryptPwd, encryptedData } from "@/utils/jsencrypt";
import { list } from "@/api/sys/post";
export default {
  name: "Honor",
  components: { Treeselect, iconfont },

  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 用户数据
      userList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openView: false,
      openRole: false,
      headImageSrc: undefined,
      disable: true,
      // 表单参数
      form: { sex: "M", districtOidSelect: "" },
      checkList: [],
      //组织机构集合
      postList: [],
      //区划数据
      districtOptions: [],
      //列表的机构数据
      listOrganOptions: [],
      //修改的机构数据
      editOrganOptions: [],
      //用户类型数据
      typeOptions: [],
      //角色
      appRoleOptions: [],
      defaultProps: {
        children: "children",
        label: "label"
      },
      //数据权限map
      dataDuthorityMap: {},
      //消息
      ableMap: { "1": "启用", "0": "禁用" },
      sexList: [{ key: "M", label: "男" }, { key: "W", label: "女" }],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: "",
        districtOid: null,
        organOid: null,
        districtOidSelect: null,
        organOidSelect: null
      },
      roleForm: {},
      dialogHeight: "450px",
      publicKey: "",
      config: "",
      //我的荣誉
      manHonors: [],
      honorList: [],
      honorCheckList: [],
      honorForm: {
        id: "",
        userOid: "",
        manHonors: this.manHonors
      },
      roleRules: {
        roleOids: [
          {
            type: "array",
            required: true,
            message: "所选角色不能为空",
            trigger: "change"
          }
        ]
      }
    };
  },
  watch: {
    "queryParams.districtOidSelect": {
      handler (val, oldVal) {
        let dataId =
          null != val
            ? val.substring(val.lastIndexOf("-") + 1, val.length)
            : "";
        this.queryParams.districtOid = dataId;
        //机构加载
        this.$set(this.queryParams, "organOid", null);
        this.$set(this.queryParams, "organOidSelect", null);
        this.getListOrganTree(dataId);
      }
    },
    "queryParams.organOidSelect": {
      handler (val, oldVal) {
        let dataId =
          null != val
            ? val.substring(val.lastIndexOf("-") + 1, val.length)
            : "";
        this.queryParams.organOid = dataId;
      }
    },
    "form.districtOidSelect": {
      handler (val, oldVal) {
        let dataId =
          null != val
            ? val.substring(val.lastIndexOf("-") + 1, val.length)
            : "";
        this.form.districtOid = dataId;
        //机构加载
        if (undefined != val && undefined != oldVal && val != oldVal) {
          this.getEditOrganTree(dataId, true);
        } else {
          this.getEditOrganTree(dataId, false);
        }
      }
    },
    "form.organOidSelect": {
      handler (val, oldVal) {
        let dataId =
          null != val
            ? val.substring(val.lastIndexOf("-") + 1, val.length)
            : "";
        this.form.organOid = dataId;
        //根据组织机构查询岗位
        if ("" != this.form.organOid) {
          this.getPostList(this.form.districtOid, this.form.organOid);
        }
      }
    },
    "roleForm.roleOids": function (val) {
      if (this.roleForm.roleOids && this.roleForm.roleOids > 0) {
        this.roleForm.alidRoleOids = this.roleForm.roleOids[0];
      }
    },
    "roleForm.roleOidsSelect": function (val) {
      if (this.roleForm.roleOids && this.roleForm.roleOids > 0) {
        this.roleForm.alidRoleOids = this.roleForm.roleOids[0];
      }
    }
  },
  computed: {
    // 计算属性的 getter
    reversedSex: function () {
      if (this.form.sex) {
        return this.form.sex == "W" ? "女" : "男";
      }
      return "";
    }
  },
  created () {
    this.getList();
    this.getDistrictTree();
    this.getUserTypt();
    this.getHonorList();
  },
  methods: {
    getHonorList () {
      getHonorList().then(res => {
        this.honorList = res;
      });
    },
    /** 获取区划树 */
    getDistrictTree (districtOid) {
      queryDistrictSimpleTree(districtOid).then(response => {
        this.districtOptions = response.data;
      });
    },
    /** 获取机构数*/
    getListOrganTree (districtOid) {
      this.listOrganOptions = [];
      this.queryParams.organOid = null;
      if (districtOid) {
        queryOrganTree(districtOid).then(response => {
          this.listOrganOptions = response.data;
        });
      }
    },
    async getEditOrganTree (districtOid, initFlag) {
      if (districtOid) {
        await queryOrganTree(districtOid).then(response => {
          if (initFlag) {
            //this.form.organOid = null;
            //this.form.organOidSelect = null;
            this.$set(this.form, "organOid", null);
            this.$set(this.form, "organOidSelect", null);
          }
          this.editOrganOptions = [];
          this.editOrganOptions = response.data;
        });
      }
    },
    //用户类型
    getUserTypt () {
      getDictList("YHLX").then(response => {
        this.typeOptions = response.data;
      });
    },
    /** 查询用户列表 */
    getList () {
      this.loading = true;
      let that = this;
      page(this.queryParams)
        .then(response => {
          this.userList = response.data.sysUserPageResult.data;
          this.total = response.data.sysUserPageResult.total;
          this.dataDuthorityMap = response.data.dataDuthorityMap;
          this.loading = false;
        })
        .catch(function () {
          that.loading = false;
        });
    },
    ////根据组织机构oid和区划oid查询岗位列表
    getPostList (districtOid, organOid) {
      const data = {
        districtOid: districtOid,
        organOid: organOid
      };
      list(data).then(response => {
        this.postList = response.data;
      });
    },
    reset () {
      this.checkList = [];
      this.form = { sex: "M", dataAuthority: 1 };
      this.roleForm = {};
      this.resetForm("form");
      this.resetForm("roleForm");
    },
    // 取消按钮
    cancel () {
      this.open = false;
      this.openView = false;
      this.reset();
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.queryParams.name = "";
      this.queryParams.districtOid = null;
      this.queryParams.organOid = null;
      this.queryParams.districtOidSelect = null;
      this.queryParams.organOidSelect = null;
      this.getList();
    },
    handleView (row) {
      this.reset();
      this.manHonors = [];
      get(row.id).then(res => {
        this.form = res.data;
        this.getPostList(this.form.districtOid, this.form.organOid);
        if (null != this.form.postOids && "" != this.form.postOids) {
          this.checkList = this.form.postOids.split(",");
        }
        this.openView = true;
        this.title = "查看用户";
      });
      getOneHonorList(row.userOid).then(res => {
        if (res != null && res != "") {
          this.manHonors = res;
        }
      });
    },
    handleRole (row) {
      this.reset();
      this.honorCheckList = [];
      get(row.id).then(res => {
        this.roleForm = res.data;
      });
      getOneHonorList(row.userOid).then(res => {
        if (res != null && res != "") {
          this.manHonors = res;
        }
      });
      getOneHonorList(row.userOid).then(res => {
        this.manHonors = res;
        for (var i = 0; i < res.length; i++) {
          this.honorCheckList.push(res[i].code);
        }
        this.openRole = true;
        this.title = "荣誉配置";
        this.honorForm.id = this.manHonors.id;
        this.honorForm.userOid = row.userOid;
        this.honorForm.manHonors = this.manHonors;
      });
    },
    submitRole () {
      var honorCode = "";
      if (!(this.honorCheckList.length > 0)) {
        var data = {
          id: this.honorForm.id == undefined ? "" : this.honorForm.id,
          userOid: this.honorForm.userOid,
          honorOid: ""
        };
        saveOrUpdateWorkerHonor(data)
          .then(res => {
            if (res.code === 200) {
              this.msgSuccess("保存成功");
              this.openRole = false;
              this.getList();
            }
          })
          .catch(function () { });
      } else {
        if (this.honorCheckList != null && this.honorCheckList.length > 0) {
          for (var i = 0; i < this.honorCheckList.length; i++) {
            honorCode += this.honorCheckList[i] + ",";
          }
        }
        var data = {
          id: this.honorForm.id == undefined ? "" : this.honorForm.id,
          userOid: this.honorForm.userOid,
          honorOid: honorCode
        };
        saveOrUpdateWorkerHonor(data)
          .then(res => {
            if (res.code === 200) {
              this.msgSuccess("保存成功");
              this.openRole = false;
              this.getList();
            }
          })
          .catch(function () { });
      }
    }
  }
};
</script>
<style scoped>
table {
  border-collapse: collapse;
}
.postClass .treeselect {
  width: 98% !important;
}
</style>
