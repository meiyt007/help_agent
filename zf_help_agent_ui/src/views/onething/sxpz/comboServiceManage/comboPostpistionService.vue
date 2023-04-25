<template>
  <div>
    <!--授权-->
    <div>
      <el-form ref="roleForm" :model="roleForm" :rules="roleRules" label-width="120px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="80%" />
          </colgroup>
          <tr>
            <td>
              <i class="require">*</i>
              <b>选择事项：</b>
            </td>
            <td>
              <el-form-item label prop="serviceOids">
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
                  appendToBody
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>选择目录：</b>
            </td>
            <td colspan="3">
              <el-form-item label prop="comboDirectoryOids">
                <treeselect
                  :multiple="true"
                  :disable-branch-nodes="true"
                  ref="userTree"
                  :defaultExpandLevel="1"
                  noOptionsText="暂无数据"
                  noResultsText="暂无数据"
                  :show-count="true"
                  v-model="roleForm.comboDirectoryOids"
                  :options="comboOptions"
                  placeholder="请选择事项"
                  appendToBody
                />
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
    </div>
    <div slot="footer" class="zf-text-center">
      <el-button type="primary" @click="submitRole">确 定</el-button>
      <el-button @click="cancel()">取 消</el-button>
    </div>
  </div>
</template>

<script>
import { save, getOne, queryComboDirectoryTree, queryServiceTree } from "@/api/onething/sxpz/comboPostpositionService";
import { get } from '@/api/sys/user'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import iconfont from '@/views/common/iconfontSelect'
import { queryOrganTree } from "@/api/sys/organ";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { getDictList } from "@/api/sys/dict";
export default {
  name: 'sxServicePersonRegistrar',
  components: { Treeselect, iconfont },
  //定义获取父类传过来值的格式
  props: ["comboDireOid"],
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
      //目录
      comboOptions: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        districtOid: null,
        organOid: null
      },
      // 表单参数
      form: {
        comboDirectoryOid: "",
        comboDirectoryOids: "",
        serviceOids: '',
        moreStatus: 0
      },
      roleForm: { serviceOids: '', comboDirectoryOids: '' },
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
    'roleForm.serviceOids': function (val) {
      if (this.roleForm.serviceOids && this.roleForm.serviceOids > 0) {
        this.roleForm.alidRoleOids = this.roleForm.serviceOids[0];
      }
    }
  },
  computed: {
  },
  created () {
    this.handleRole()
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
    reset () {
      this.form = { sex: 'M' }
      this.roleForm = {}
      this.resetForm('form')
      this.resetForm('roleForm')
    },
    // 取消按钮
    cancel () {
      this.reset();
      this.$emit('close-materView');
    }
    ,
    handleView (row) {
      this.reset()
      get(row.id).then(res => {
        this.form = res.data
        this.openView = true
        this.title = '查看用户'
      })
    },
    handleRole (row) {
      this.reset()
      let oids = [];
      let comboOids = [];
      getOne(this.comboDireOid).then(res => {
        this.form = res.data;
        let serviceOidArr = this.form.serviceOids ? this.form.serviceOids.split(',') : [];
        for (let service of serviceOidArr) {
          oids.push(service)
        }
        let comboOidArr = this.form.comboDirectoryOids ? this.form.comboDirectoryOids.split(',') : [];
        for (let comboOid of comboOidArr) {
          comboOids.push(comboOid)
        }
        queryComboDirectoryTree().then(res => {
          this.comboOptions = res.data;
          console.log(this.comboOptions);
        })
        queryServiceTree().then(res => {
          this.appRoleOptions = res.data;
          this.openRole = true;
          this.roleForm.serviceOids = oids;
          this.roleForm.comboDirectoryOids = comboOids;
        })
      })
    },
    submitRole () {
      let _that = this;
      this.form.serviceOids = '';
      this.form.comboDirectoryOids = '';
      this.roleForm.serviceOids.forEach(ser => {
        this.form.serviceOids += ser + ",";
      }
      );
      this.roleForm.comboDirectoryOids.forEach(serC => {
        this.form.comboDirectoryOids += serC + ",";
      }
      );
      if (this.form.serviceOids != '') {
        this.form.serviceOids = this.form.serviceOids.slice(0, this.form.serviceOids.length - 1);
      }
      if (this.form.comboDirectoryOids != '') {
        this.form.comboDirectoryOids = this.form.comboDirectoryOids.slice(0, this.form.comboDirectoryOids.length - 1);
      }
      _that.form.comboDirectoryOid = this.comboDireOid;
      /*_that.$refs["form"].validate(valid => {
        if (valid) {*/
      if (!this.form.serviceOids) {
        _that.msgWarning("先选择事项");
        return;
      }
      save(_that.form).then(response => {
        if (response.code === 200) {
          _that.msgSuccess("保存成功");
          this.reset();
          this.$emit('close-materView');
        }
      });
    }
    /*  });
    }*/
  }
}

</script>
<style lang='scss' scoped >
table {
  border-collapse: collapse;
}
::v-deep .el-form-item__content {
  margin-left: 0 !important;
}
</style>
