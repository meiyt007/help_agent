<template>
  <div>
    <!--用户数据-->
    <el-table v-loading="loading" :data="userList" border>
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="所属区划" align="center" prop="districtName" :show-overflow-tooltip="true"/>
      <el-table-column label="所属机构" align="center" prop="organName" :show-overflow-tooltip="true"/>
      <el-table-column label="姓名" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="身份证号" align="center" prop="cardNo" :show-overflow-tooltip="true" />
      <el-table-column label="手机" align="center" prop="mobile" :show-overflow-tooltip="true" />
      <el-table-column label="状态" width="100" align="center" prop="isAble" :formatter="statusFormat"/>
      <el-table-column label="排序号" align="center" prop="sort"/>

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
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 查看用户配置对话框 -->
    <el-dialog :title="title" :visible.sync="openView" :close-on-click-modal="false" width="1100px" height="700px" scrollbar append-to-body>
      <div>
        <el-form :model="form" label-width="0px" >
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
                {{form.districtName}}
              </td>
              <td><b>所属机构：</b></td>
              <td>
                {{form.organName}}
              </td>
            </tr>
            <tr>
              <td><b>姓名：</b></td>
              <td>
                {{form.name}}
              </td>
              <td><b>出生年月：</b></td>
              <td>
                {{form.birthdate}}
              </td>
            </tr>
            <tr>
              <td><b>用户性别：</b></td>
              <td>
                {{reversedSex}}
              </td>
              <td><b>职务：</b></td>
              <td>
                {{form.position}}
              </td>
            </tr>
            <tr>
              <td><b>身份证号：</b></td>
              <td>
                {{form.cardNo}}
              </td>
              <td><b>邮箱：</b></td>
              <td>
                {{form.email}}
              </td>
            </tr>
            <tr>
              <td><b>手机：</b></td>
              <td>
                {{form.mobile}}
              </td>
              <td><b>座机：</b></td>
              <td>
                {{form.telphone}}
              </td>
            </tr>
            <tr>
              <td><b>类型：</b></td>
              <td>
                {{form.typeName}}
              </td>
              <td><b>排序号：</b></td>
              <td>
                {{form.sort}}
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {get} from '@/api/sys/user';
  import {userListPage} from '@/api/sys/post';

  export default {
    name: 'SysUserView',
    props:['postOid'],
    data() {
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
        headImageSrc: undefined,
        // 表单参数
        form: {},
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          organOid:''
        },
        //用户类型数据
        typeOptions: [],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        //消息
        ableMap: {'1': '启用', '0': '禁用'},
        sexList: [{key: 'M', label: '男'}, {key: 'W', label: '女'}]

      }
    },
    computed: {
      // 计算属性的 getter
      reversedSex: function () {
        if (this.form.sex) {
          return this.form.sex=="W"?'女':'男'
        }
        return ''
      }

    },
    created() {
      this.getList()
      this.getDictList('YHLX').then(response => {
          this.typeOptions = response.data
        }
      )
    },
    methods: {
      /** 查询用户列表 */
      getList() {
        this.loading = true
        let that = this;
        this.queryParams.postOid = this.postOid;
        userListPage(this.queryParams).then(response => {
            this.userList = response.data.data
            this.total = response.data.total
            this.loading = false
          }
        ).catch(function() {that.loading = false;});
      },
      // 消息
      statusFormat(row) {
        return this.selectMapLabel(this.ableMap, row.isAble)
      },
      reset() {
        this.form = {}
        this.resetForm('form')
      },
      // 取消按钮
      cancel() {
        this.open = false
        this.openView = false
        this.reset()
      },
      handleView(row) {
        this.reset()
        get(row.id).then(res => {
          this.form = res.data
          this.openView = true
          this.title = '查看用户'
        })
      }

    }
  }

</script>
