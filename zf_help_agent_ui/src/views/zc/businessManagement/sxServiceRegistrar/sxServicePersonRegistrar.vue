<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
    >
      <el-form-item label="所属区划" prop="districtOid">
        <treeselect
          v-model="queryParams.districtOidSelect"
          :options="districtOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入所属区划"
        />
      </el-form-item>
      <el-form-item label="所属机构" prop="organOid">
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
        >
          重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="userList"
      border
      :fit="true"
      height="calc(100% - 120px)"
    >
      <el-table-column label="序号" width="55" align="center" type="index" />
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
      <!--      <el-table-column label="排序号" align="center" prop="sort"/>-->

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
            v-hasPermi="['sys:person:view']"
            >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shouquan"
            @click="handleRole(scope.row)"
            v-hasPermi="['sys:person:init']"
            >授权
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-quxiao"
            @click="deleteSQ(scope.row)"
            v-hasPermi="['sys:person:init']"
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
            <i class="require">*</i><b>选择事项：</b>
          </td>
          <td colspan="3">
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
import { getToken } from '@/utils/auth'
import { pageList, getOneByUserOid, queryServiceTree, saveOrUpdatePersonReg, delUserService } from "@/api/zc/businessManagement/sxServiceRegistrar";
import { get } from '@/api/sys/user'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import iconfont from '@/views/common/iconfontSelect'
import { queryOrganTree } from "@/api/sys/organ";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { getDictList } from "@/api/sys/dict";
export default {
  name: 'SxServicePersonRegistrar',
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
        organOid: null,
        districtOidSelect:null,
        organOidSelect:null
      },
      roleForm: {},
      // 表单校验
      rules: {
      },
      roleRules: {
        serviceOids: [
          { type: 'array', required: true, message: '所选事项不能为空', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    'form.districtOid': function (val, oldVal) {
      //区划加载
      this.getEditOrganTree(val)
    },
    'queryParams.districtOidSelect': function (val, oldVal) {
      //机构加载
      this.getListOrganTree(val)
    },
    'form.headImageAttaOid': function (val, oldVal) {
      //机构加载
      //this.getListOrganTree(val)
    },
    'roleForm.serviceOids': function (val) {
      if (this.roleForm.serviceOids && this.roleForm.serviceOids > 0) {
        this.roleForm.alidRoleOids = this.roleForm.serviceOids[0];
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
      this.loading = true
      if(this.queryParams.districtOidSelect){
        this.queryParams.districtOid=this.queryParams.districtOidSelect.split("-")[1];
      }
      if(this.queryParams.organOidSelect){
        this.queryParams.organOid=this.queryParams.organOidSelect.split("-")[1];
      }
      pageList(this.queryParams).then(response => {
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
    },
    // 取消按钮
    cancel () {
      this.open = false
      this.openView = false
      this.reset()
    }
    ,
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.page = 1;
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.queryParams.districtOidSelect=null;
      this.queryParams.name = '';
      this.queryParams.organOidSelect=null;
      this.handleQuery();
    },

    handleView (row) {
      this.reset()
      get(row.id).then(res => {
        this.form = res.data
        this.openView = true
        this.title = '查看用户'
      })
    },
    deleteSQ (row) {//删除授权
      const oid = row.userOid;
      this.$confirm('确认取消授权吗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delUserService(oid);
      }).then(() => {
        this.msgSuccess("取消成功");
        this.getList();
      }).catch(function () { });
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
            if (service) {
              oids.push(service)
            }
          }
        }
        queryServiceTree(treeQuery).then(res => {
          this.appRoleOptions = res.data;
          this.openRole = true;
          this.roleForm.serviceOids = oids;
          this.title = "用户授权"
        })

      })
    },
    submitRole () {
      if (this.roleForm.serviceOids && this.roleForm.serviceOids.length > 0) {
        this.roleForm.userOids = this.roleForm.serviceOids.join(';');
        saveOrUpdatePersonReg(this.roleForm).then(res => {
          if (res.code === 200) {
            this.msgSuccess('保存成功')
            this.openRole = false
            this.getList()
          }
        })
      } else {
        this.$message.error("请选择事项!")
      }

    }

  }
}

</script>
<style lang="scss">
table {
  border-collapse: collapse;
}
</style>
