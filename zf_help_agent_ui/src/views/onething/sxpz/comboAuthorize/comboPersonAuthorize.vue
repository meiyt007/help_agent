<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="所属区划" prop="districtOid">
        <treeselect
          v-model="queryParams.districtOid"
          :options="districtOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入所属区划"
        />
      </el-form-item>
      <el-form-item label="所属机构" prop="organOid">
        <treeselect
          v-model="queryParams.organOid"
          :options="listOrganOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入所属机构"
        />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model.trim="queryParams.name"
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

    <el-table v-loading="loading" :data="userList" border :fit="true">
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
        label="身份证号"
        align="center"
        prop="cardNo"
        :formatter="cardNoFormat"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="手机或座机"
        align="center"
        prop="mobile"
        :formatter="mobileFormat"
        :show-overflow-tooltip="true"
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
            >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shouquan"
            @click="handleRole(scope.row)"
            >授权
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-quxiao"
            @click="deleteSQ(scope.row)"
            >取消
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
      <div>
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
          <tr>
            <td><b>类型：</b></td>
            <td>
              {{ form.typeName }}
            </td>
            <td><b>排序号：</b></td>
            <td>
              {{ form.sort }}
            </td>
          </tr>
        </table>
      </div>

      <div slot="footer" class="zf-text-center">
        <el-button @click="cancel">关闭</el-button>
      </div>
    </el-dialog>
    <!--授权-->
    <el-dialog
      v-dialog-drag
      :title="title"
      :visible.sync="openRole"
      :close-on-click-modal="false"
      width="900px"
    >
      <div>
        <el-form
          ref="roleForm"
          :model="roleForm"
          :rules="roleRules"
          label-width="120px"
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
              <td>
                <b>选择：</b>
              </td>
              <td colspan="3">
                <el-form-item label="" prop="comboDirectoryOids">
                  <treeselect
                    :multiple="true"
                    :disable-branch-nodes="true"
                    ref="userTree"
                    :defaultExpandLevel="1"
                    noOptionsText="暂无数据"
                    noResultsText="暂无数据"
                    :show-count="true"
                    v-model="roleForm.comboDirectoryOids"
                    :options="appRoleOptions"
                    placeholder="请选择"
                    append-to-body
                  />
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitRole">确 定</el-button>
        <el-button @click="openRole = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'
import { pageList } from "@/api/zc/businessManagement/sxServiceRegistrar.js";
import { saveOrUpdatePersonReg, queryComboTree, getOneByUserOid, delUserComboDire } from "@/api/onething/comboAuthorize/comboAuthorize";
import { get } from '@/api/sys/user'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import iconfont from '@/views/common/iconfontSelect'
import { queryOrganTree } from "@/api/sys/organ";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { getDictList } from "@/api/sys/dict";
export default {
  name: 'ComboPersonAuthorize',
  components: { Treeselect, iconfont },

  data () {
    return {
      listParam:{
        pageNum: 1,
        pageSize: 10,
        name: '',
        districtOid: null,
        organOid: null
      },
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 用户数据
      userList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      openView: false,
      openRole: false,
      headImageSrc: undefined,
      disable: true,
      // 表单参数
      form: { sex: 'M' },
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
        children: 'children',
        label: 'label'
      },
      //启禁用状态
      ableMap: { 'Y': '启用', 'N': '禁用' },
      sexList: [{ key: 'M', label: '男' }, { key: 'W', label: '女' }],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        districtOid: null,
        organOid: null
      },
      roleForm: { comboDirectoryOids: '' },
      // 表单校验
      rules: {
      },
      roleRules: {
        comboDirectoryOids: [
          { type: 'array', required: true, message: '所选不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    'form.districtOid': function (val, oldVal) {
      //区划加载
      this.getEditOrganTree(val)
    },
    'queryParams.districtOid': function (val, oldVal) {
      //机构加载
      this.getListOrganTree(val)
    },
    'form.headImageAttaOid': function (val, oldVal) {
      //机构加载
      //this.getListOrganTree(val)
    },
    'roleForm.comboDirectoryOids': function (val) {
      if (this.roleForm.comboDirectoryOids && this.roleForm.comboDirectoryOids > 0) {
        this.roleForm.alidRoleOids = this.roleForm.comboDirectoryOids[0];
      }
    }
  },
  computed: {
    // 计算属性的 getter
    reversedCardNo: function () {
      if (this.form.cardNo) {
        return this.form.cardNo.substring(0, 3) + "**********" + this.form.cardNo.substring(this.form.cardNo.length - 4, this.form.cardNo.length)
      }
      return ''
    },
    reversedMobile: function () {
      if (this.form.mobile) {
        return this.form.mobile.substring(0, 3) + "*****" + this.form.mobile.substring(this.form.mobile.length - 4, this.form.mobile.length)
      }
      return ''
    },
    reversedTelphone: function () {
      if (this.form.telphone) {
        return this.form.telphone.substring(0, 3) + "*****" + this.form.telphone.substring(this.form.telphone.length - 4, this.form.telphone.length)
      }
      return ''
    },
    reversedSex: function () {
      if (this.form.sex) {
        return this.form.sex == "W" ? '女' : '男'
      }
      return ''
    }

  },
  created () {
    this.getList()
    this.getDistrictTree()
    this.getUserTypt()
  },
  methods: {
    /** 获取区划树 */
    getDistrictTree (districtOid) {
      queryDistrictSimpleTree(districtOid).then(response => {
        this.districtOptions = response.data;
      });
    },
    /** 获取机构数*/
    getListOrganTree (districtOid) {
      if (districtOid) {
        queryOrganTree(districtOid).then(response => {
          this.listOrganOptions = response.data;
        });
      } else {
        this.listOrganOptions = []
        this.queryParams.organOid = null
      }

    },
    getEditOrganTree (districtOid) {
      if (districtOid) {
        queryOrganTree(districtOid).then(response => {
          this.editOrganOptions = response.data;
        });
      } else {
        this.editOrganOptions = []
        this.form.organOid = null
      }
    },
    //用户类型
    getUserTypt () {
      getDictList('YHLX').then(response => {
        this.typeOptions = response.data
      }
      )
    },
    /** 查询用户列表 */
    getList () {
      this.loading = true;
      //处理queryParams，去除值上面的冗余字符（DISTRICT-2c287b8b763cc1ea0176403909f8017a  ORGAN-2c287b8b763cc1ea01764039b19d017b）
      if (this.queryParams.districtOid) {
        var districtOid = this.queryParams.districtOid + "";
        this.listParam.districtOid = districtOid.replace(/DISTRICT-/, "");
      } else {
        this.listParam.districtOid = null;
      }
      if (this.queryParams.organOid) {
        var organOid = this.queryParams.organOid + "";
        this.listParam.organOid = organOid.replace(/ORGAN-/, "");
      } else {
        this.listParam.organOid = null;
      }
      if (this.queryParams.page) {
        this.listParam.page = this.queryParams.page;
      }
      this.listParam.pageNum = this.queryParams.pageNum;
      this.listParam.pageSize = this.queryParams.pageSize;
      this.listParam.name = this.queryParams.name;
      pageList(this.listParam).then(response => {
        this.userList = response.data.data
        this.total = response.data.total
        this.loading = false
      }
      )
    },
    mobileFormat (row) {
      let number = ""
      if (row.telphone) {
        number = row.telphone
      }
      if (row.mobile) {
        number = row.mobile
      }
      if (number) {
        return number.substring(0, 3) + "*****" + number.substring(number.length - 4, number.length)
      }
    },
    cardNoFormat (row) {
      if (row.cardNo) {
        return row.cardNo.substring(0, 3) + "**********" + row.cardNo.substring(row.cardNo.length - 4, row.cardNo.length)
      }
    },
    // 启禁用状态
    statusFormat (row) {
      return this.selectMapLabel(this.ableMap, row.isAble)
    },
    reset () {
      this.form = { sex: 'M' }
      this.roleForm = {}
      this.resetForm('form')
      this.resetForm('roleForm')
      this.resetForm('queryForm')
    },
    // 取消按钮
    cancel () {
      this.open = false
      this.openView = false
      // this.reset()
    }
    ,
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.page = 1
      this.getList()
    }
    ,
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm('queryForm')
      this.handleQuery()
    },

    handleView (row) {
      // this.reset()
      get(row.id).then(res => {
        this.form = res.data
        this.openView = true
        this.title = '查看用户'
      })
    },
    handleRole (row) {
      this.reset()
      get(row.id).then(res => {
        this.roleForm = res.data
      })

      let treeQuery = {
        appOid: '',
        disable: this.disable
      }

      getOneByUserOid(row.userOid).then(res => {
        //处理已有角色
        let oids = [];
        if (res.data) {
          let serviceOidArr = res.data.userOids ? res.data.userOids.split(';') : [];
          for (let service of serviceOidArr) {
            oids.push(service)
          }
        }
        queryComboTree(treeQuery).then(res => {
          this.appRoleOptions = res.data;
          this.openRole = true;
          this.roleForm.comboDirectoryOids = oids;
          this.title = "用户授权"
        })

      })
    },
    deleteSQ (row) {//删除授权
      const oid = row.userOid;
      this.$confirm('确认取消授权吗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delUserComboDire(oid);
      }).then(() => {
        this.msgSuccess("取消成功");
        this.getList();
      }).catch(function () { });
    },
    submitRole () {
      if (this.roleForm.comboDirectoryOids && this.roleForm.comboDirectoryOids.length > 0) {
        this.roleForm.userOids = this.roleForm.comboDirectoryOids.join(';');
        saveOrUpdatePersonReg(this.roleForm).then(res => {
          if (res.code === 200) {
            this.msgSuccess('保存成功')
            this.openRole = false
            this.getList()
          }
        })
      } else {
        this.$refs['roleForm'].validate(valid => {
        })
      }
    }

  }
}

</script>
<style lang="scss" scoped>
table {
  border-collapse: collapse;
}
.postClass .treeselect {
  width: 98% !important;
}
::v-deep .el-form-item__content {
  margin-left: 0 !important;
}
</style>
