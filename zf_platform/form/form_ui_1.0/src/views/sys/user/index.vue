<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="所属区划">
        <treeselect
          v-model="queryParams.districtOidSelect"
          :options="districtOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入所属区划"/>
      </el-form-item>
      <el-form-item label="所属机构">
        <treeselect
          v-model="queryParams.organOidSelect"
          :options="listOrganOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入所属机构"/>
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
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['sys:user:addUpdate']"
        >新增
        </el-button>
      </el-col>

    </el-row>
    <el-table v-loading="loading" :data="userList" border>
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="所属区划" align="center" prop="districtName" :show-overflow-tooltip="true"/>
      <el-table-column label="所属机构" align="center" prop="organName" :show-overflow-tooltip="true"/>
      <el-table-column label="姓名" align="center" prop="name" :show-overflow-tooltip="true"/>
      <el-table-column label="身份证号" align="center" prop="cardNo"  :show-overflow-tooltip="true"/>
      <el-table-column label="手机" align="center" prop="mobile"  :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isAble"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="排序号" align="center" prop="sort"/>

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
            icon="iconfont zfsoft-xiugai"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['sys:user:addUpdate']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shouquan"
            @click="handleRole(scope.row)"
            v-hasPermi="['sys:user:shouquan']"
          >授权
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sys:user:delete']"
          >删除
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
    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" :close-on-click-modal="false" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <el-form ref="form" :model="form" :rules="rules" label-width="0px" >
          <table cellspacing="0" cellpadding="0" border="0" >
            <el-input v-show="false" v-model="form.userOid"/>
            <el-input v-show="false" v-model="form.id" />
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>所属区划：</b></td>
              <td>
                <!--<el-form-item prop="districtOidSelect">
                  <treeselect
                    v-model="form.districtOidSelect"
                    :options="districtOptions"
                    noOptionsText="暂无数据"
                    :defaultExpandLevel="1"
                    placeholder="请输入所属区划"
                  />
                </el-form-item>-->
                <el-form-item prop="districtOidSelect">
                  <treeselect :defaultExpandLevel="1" noOptionsText = "暂无数据"  noResultsText="暂无数据" :show-count="true" v-model="form.districtOidSelect" :options="districtOptions" placeholder="请输入所属区划" />
                </el-form-item>
              </td>

              <td><i class="require">*</i><b>所属机构：</b></td>
              <td>
                <el-form-item prop="organOidSelect">
                  <treeselect
                    v-model="form.organOidSelect"
                    :options="editOrganOptions"
                    noOptionsText="暂无数据"
                    :defaultExpandLevel="1"
                    placeholder="请输入所属机构"/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>姓名：</b></td>
              <td>
                <el-form-item prop="name">
                  <el-input v-model.trim="form.name" placeholder="请输入用户姓名" maxlength="50" show-word-limit/>
                </el-form-item>
              </td>
              <td><b>出生年月：</b></td>
              <td>
                <el-form-item prop="birthdate">
                  <el-date-picker
                    v-model="form.birthdate"
                    type="date"
                    value-format="yyyy-MM-dd"
                    placeholder="选择出生年月">
                  </el-date-picker>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>用户性别：</b></td>
              <td>
                <el-form-item>
                  <el-radio v-model="form.sex" label="M">男</el-radio>
                  <el-radio v-model="form.sex" label="W">女</el-radio>
                </el-form-item>
              </td>

              <td><i class="require">*</i><b>职务：</b></td>
              <td>
                <el-form-item prop="position">
                  <el-input v-model.trim="form.position" placeholder="请输入职务" maxlength="50" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>身份证号：</b></td>
              <td>
                <el-form-item prop="cardNo">
                  <el-input v-model.trim="form.cardNo" placeholder="请输入身份证号" maxlength="18" show-word-limit/>
                </el-form-item>
              </td>

              <td><i class="require">*</i><b>邮箱：</b></td>
              <td>
                <el-form-item prop="email">
                  <el-input v-model.trim="form.email" placeholder="请输入邮箱" maxlength="100" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>手机：</b></td>
              <td>
                <el-form-item prop="mobile">
                  <el-input v-model.trim="form.mobile" placeholder="请输入手机" maxlength="11" show-word-limit/>
                </el-form-item>
              </td>

              <td><b>座机：</b></td>
              <td>
                <el-form-item prop="telphone">
                  <el-input v-model.trim="form.telphone" placeholder="请输入座机" maxlength="13" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>类型：</b></td>
              <td>
                <el-form-item prop="type">
                  <el-select v-model="form.type" placeholder="请选择类型">
                    <el-option
                      v-for="dict in typeOptions"
                      :key="dict.dictOid"
                      :label="dict.name"
                      :value="dict.dictOid"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </td>

              <td><i class="require">*</i><b>数据权限：</b></td>
              <td>
                <el-form-item prop="dataAuthority">
                  <el-select v-model="form.dataAuthority" placeholder="请选择数据权限" >
                    <el-option
                      v-for="data in dataDuthorityMap"
                      :key="data.id"
                      :label="data.value"
                      :value="data.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>所属岗位：</b></td>
              <td colspan="3">
                <el-form-item>
                  <el-checkbox-group ref="postRef" v-model="checkList">
                    <el-checkbox v-for="post in postList" :label="post.postOid">{{post.name}}</el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>排序号：</b></td>
              <td colspan="3">
                <el-form-item prop="sort">
                  <el-input-number placeholder="请输入排序号" v-model="form.sort" :min="1" :max="9999"/>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
        <el-form ref="formLogin" :model="form" v-if="form.id == undefined" :rules="loginRules" label-width="0px">
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="20%" />
              <col width="80%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>账号：</b></td>
              <td>
                <el-form-item   prop="account">
                  <el-input v-model.trim="form.account" placeholder="请输入账号" maxlength="50" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>用户密码：</b></td>
              <td>
                <el-form-item prop="password">
                  <el-input v-model.trim="form.password" placeholder="请输入用户密码" type="password" maxlength="18" show-word-limit/>
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


    <!-- 查看用户配置对话框 -->
    <el-dialog :title="title" :visible.sync="openView" :close-on-click-modal="false" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <el-form label-width="0px" >
          <table cellspacing="0" cellpadding="0" border="0" >
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
              <td><b>数据权限：</b></td>
              <td>
                <el-form-item v-for="data in dataDuthorityMap" v-if="data.id == form.dataAuthority">
                  {{data.value}}
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>排序号：</b></td>
              <td colspan="3">
                {{form.sort}}
              </td>
            </tr>
            <tr>
              <td><b>所属岗位：</b></td>
              <td colspan="3">
                <span v-for="post in postList" v-if="undefined !=form.postOids && form.postOids.indexOf(post.postOid) !=-1">
                  {{post.name}}&nbsp;&nbsp;
                </span>
              </td>
            </tr>
          </table>
        </el-form>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--授权角色-->
    <el-dialog :title="title" :visible.sync="openRole" v-if="openRole" :close-on-click-modal="false" width="70%">
      <div v-bind:style="{height:dialogHeight}"  class="el-table__header-wrapper dialog-table">
        <el-form ref="roleForm" :model="form" :rules="roleRules" label-width="0px" >
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>所属区划：</b></td>
              <td>
                {{roleForm.districtName}}
              </td>
              <td><b>所属机构：</b></td>
              <td>
                {{roleForm.organName}}
              </td>
            </tr>
            <tr>
              <td><b>姓名：</b></td>
              <td>
                {{roleForm.name}}
              </td>
              <td><b>职务：</b></td>
              <td>
                {{roleForm.position}}
              </td>
            </tr>
            <tr>
              <td><b>类型：</b></td>
              <td colspan="3">
                {{roleForm.typeName}}
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>角色：</b></td>
              <td colspan="3">
                <el-form-item prop="roleOids">
                  <treeselect :noChildrenText="`暂无角色`" :appendToBody="false" :max-height="245" :zIndex="9999" :multiple="true" :disable-branch-nodes="true" :defaultExpandLevel="1" noOptionsText = "暂无数据"  noResultsText="暂无数据" :show-count="true" v-model="roleForm.roleOids" :options="appRoleOptions" placeholder="请选择角色" />
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitRole">确 定</el-button>
        <el-button @click="openRole=false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {save,saveEncrypt, page, able, del, get,init,getUserRole,saveUserRole} from '@/api/sys/user'
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'
  import iconfont from '@/views/common/iconfontSelect'
  import {validIntNoZero, validIDCard, validatePhoneTwo,validatePassword,validateEmail} from '@/utils/validate'
  import {queryOrganTree} from "@/api/sys/organ";
  import {queryDistrictSimpleTree} from "@/api/sys/district";
  import {getDictList} from "@/api/sys/dict";
  import {roleTree} from "@/api/sys/role";
  import { getKey } from "@/api/login";
  import { encryptPwd,encryptedData } from '@/utils/jsencrypt'
  import { list } from "@/api/sys/post";
  export default {
    name: 'User',
    components: {Treeselect, iconfont},

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
        openRole:false,
        headImageSrc: undefined,
        disable:true,
        // 表单参数
        form: {sex:'M',districtOidSelect:''},
        checkList: [],
        //组织机构集合
        postList:[],
        //区划数据
        districtOptions: [],
        //列表的机构数据
        listOrganOptions: [],
        //修改的机构数据
        editOrganOptions: [],
        //用户类型数据
        typeOptions: [],
        //角色
        appRoleOptions:[],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        //数据权限map
        dataDuthorityMap: {},
        //消息
        ableMap: {'1': '启用', '0': '禁用'},
        sexList: [{key: 'M', label: '男'}, {key: 'W', label: '女'}],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: '',
          districtOid: null,
          organOid: null,
          districtOidSelect: null,
          organOidSelect: null
        },
        roleForm:{},
        dialogHeight:'450px',
        publicKey:'',
        config:'',
        // 表单校验
        rules: {
          districtOidSelect: [
            {required: true, message: '所属区划不能为空', trigger: 'blur'}
          ],
          organOidSelect: [
            {required: true, message: '所属机构不能为空', trigger: 'blur'}
          ],
          name: [
            {required: true, message: '姓名不能为空', trigger: 'blur'}
          ],
          cardNo: [
            {required: true, message: '身份证号码不能为空', trigger: 'blur'},
            {validator: validIDCard, trigger: 'blur'}
          ],
          mobile: [
            {required: true, message: '手机号码不能为空', trigger: 'blur'},
            {validator: validatePhoneTwo, message: '请输入正确的手机号码', trigger: 'blur'}
          ],
          telphone: [
            {validator: validatePhoneTwo, message: '请输入正确的座机号码', trigger: 'blur'}
          ],
          position: [
            {required: true, message: '职务不能为空', trigger: 'blur'}
          ],
          email: [
            {required: true, message: '邮箱地址不能为空', trigger: 'blur'},
            {validator: validateEmail, trigger: 'change'}
          ],
          dataAuthority: [
            {required: true, message: '请选择数据权限！', trigger: 'change'}
          ],
          sort: [
            {required: true, message: '排序号不能为空', trigger: 'change'},
            {validator: validIntNoZero, trigger: 'change'}
          ]
        },
        loginRules: {
          account: [
            {required: true, message: '账号不能为空', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '用户密码不能为空', trigger: 'blur'},
            {validator: validatePassword, trigger: 'blur'}
          ]
        },
        roleRules: {
          roleOids: [
            {type:'array',required: true, message: '所选角色不能为空', trigger: 'change'}
          ]
        }
      }
    },
    watch: {
      'queryParams.districtOidSelect': {
        handler(val,oldVal) {
          let dataId = null!=val?val.substring(val.lastIndexOf('-')+1,val.length):'';
          this.queryParams.districtOid = dataId;
          //机构加载
          this.$set(this.queryParams,'organOid',null);
          this.$set(this.queryParams,'organOidSelect',null);
          this.getListOrganTree(dataId)
        }
      },
      'queryParams.organOidSelect': {
        handler(val, oldVal) {
          let dataId = null!=val?val.substring(val.lastIndexOf('-')+1,val.length):'';
          this.queryParams.organOid = dataId;
        }
      },
      'form.districtOidSelect': {
        handler(val, oldVal) {
          let dataId = null!=val?val.substring(val.lastIndexOf('-')+1,val.length):'';
          this.form.districtOid = dataId;
          //机构加载
          if(undefined != val && undefined != oldVal &&  val != oldVal){
            this.getEditOrganTree(dataId,true);
          }else {
            this.getEditOrganTree(dataId,false);
          }
        }
      },
      'form.organOidSelect': {
        handler(val, oldVal) {
          let dataId = null!=val?val.substring(val.lastIndexOf('-')+1,val.length):'';
          this.form.organOid = dataId;
          //根据组织机构查询岗位
          if(''!=this.form.organOid){
            this.getPostList(this.form.districtOid,this.form.organOid);
          }
        }
      },
      'roleForm.roleOids':function(val){
        if(this.roleForm.roleOids&&this.roleForm.roleOids>0){
          this.roleForm.alidRoleOids= this.roleForm.roleOids[0];
        }
      },
      'roleForm.roleOidsSelect':function(val){
        if(this.roleForm.roleOids&&this.roleForm.roleOids>0){
          this.roleForm.alidRoleOids= this.roleForm.roleOids[0];
        }
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
      this.getDistrictTree()
      this.getUserTypt()
    },
    methods: {
      /** 获取区划树 */
      getDistrictTree(districtOid) {
        queryDistrictSimpleTree(districtOid).then(response => {
          this.districtOptions = response.data;
        });
      },
      /** 获取机构数*/
      getListOrganTree(districtOid) {
        this.listOrganOptions = [];
        this.queryParams.organOid = null;
        if (districtOid) {
          queryOrganTree(districtOid).then(response => {
            this.listOrganOptions = response.data;
          });
        }
      },
      async getEditOrganTree(districtOid,initFlag) {
        if (districtOid) {
          await queryOrganTree(districtOid).then(response => {
            if(initFlag){
              //this.form.organOid = null;
              //this.form.organOidSelect = null;
              this.$set(this.form,'organOid',null);
              this.$set(this.form,'organOidSelect',null);
            }
            this.editOrganOptions = [];
            this.editOrganOptions = response.data;
          });
        }
      },
      //用户类型
      getUserTypt() {
        getDictList('YHLX').then(response => {
            this.typeOptions = response.data
          }
        )
      },
      /** 查询用户列表 */
      getList() {
        this.loading = true
        let that = this;
        page(this.queryParams).then(response => {
            this.userList = response.data.sysUserPageResult.data;
            this.total = response.data.sysUserPageResult.total;
            this.dataDuthorityMap = response.data.dataDuthorityMap;
            this.loading = false;
          }
        ).catch(function() {that.loading = false;});
      },
      ////根据组织机构oid和区划oid查询岗位列表
      getPostList(districtOid,organOid) {
        const data = {
          districtOid:districtOid,
          organOid:organOid
        }
        list(data).then(response => {
            this.postList = response.data;
          }
        )
      },
      // 启用状态
      statusDataAuthorityFormat(dataDuthority) {
        return this.selectMapLabel(this.dataDuthorityMap,dataDuthority);
      },
      reset() {
        this.checkList=[]
        this.form = {sex:'M',dataAuthority:1}
        this.roleForm={}
        this.resetForm('form')
        this.resetForm('roleForm')
      },
      // 状态修改
      handleStatusChange(row) {
        let text = row.isAble == 1 ? '启用' : '禁用'
        this.$confirm('确认要"' + text + '""' + row.name + '"用户吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function () {
          return able(row.id)
        }).then(() => {
          this.msgSuccess(text + '成功')
        }).catch(function () {
          row.isAble = row.isAble == 0 ? 1 : 0
        })
      }
      ,
      // 取消按钮
      cancel() {
        this.open = false
        this.openView = false
        this.reset()
      }
      ,
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.page = 1
        this.getList()
      }
      ,
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.queryParams.name=''
        this.queryParams.districtOid=null
        this.queryParams.organOid=null
        this.queryParams.districtOidSelect=null;
        this.queryParams.organOidSelect=null;
        this.getList();
      }
      ,
      /** 新增按钮操作 */
      handleAdd: function () {
        this.reset()
        getKey().then(res => {
          this.publicKey = res.data.publicKey
          this.config = res.data.config
        })
        this.open = true
        this.title = '新增用户信息'
        this.editOrganOptions = []
      }
      ,
      /** 修改按钮操作 */
      handleUpdate(row) {
        var that = this ;
        this.reset()
        getKey().then(res => {
          this.publicKey = res.data.publicKey
          this.config = res.data.config
        })
        init(row.id).then(res => {
          let result=res.data;
          if(null!=result.districtOid && ''!=result.districtOid){
            result.districtOidSelect= 'DISTRICT-'+result.districtOid;
          }
          if(null!=result.organOid && ''!=result.organOid){
            result.organOidSelect='ORGAN-'+result.organOid;
          }
          if(null!=result.postOids && ''!=result.postOids){
            that.checkList = result.postOids.split(',');
          }
          this.form = result;
          this.open = true
          this.title = '修改用户信息'
        })
      },
      getArrEqual(arr1, arr2) {
        let newArr = [];
        for (let i = 0; i < arr2.length; i++) {
          for (let j = 0; j < arr1.length; j++) {
            if(arr1[j] === arr2[i].postOid){
              newArr.push(arr1[j]);
            }
          }
        }
        return newArr;
      },
      saveForm () {
        var that = this ;
        let checkPostArr = this.getArrEqual(this.checkList,this.postList);
        this.form.postOids = checkPostArr.toString();
        if("1"==this.config){
          const formJson = JSON.stringify(this.form);
          const formObject = JSON.parse(formJson);  //使用JSON.parse() 将JSON字符串转为JS对象;
          delete formObject.telphone;
          delete formObject.mobile;
          delete formObject.email;
          delete formObject.cardNo;
          formObject.password = encryptPwd(this.publicKey, formObject.password);
          const formObjectJson = JSON.stringify(formObject);
          const dataEncrypt = {
            telphone: this.form.telphone,
            mobile: this.form.mobile,
            email: this.form.email,
            cardNo: this.form.cardNo
          }
          let newForm = encryptedData(this.publicKey,JSON.stringify(dataEncrypt));
          const data = {
            sysUserEncrypt: newForm,
            formObjectJson:formObjectJson
          }
          saveEncrypt(data).then(res => {
            if (res.code === 200) {
              this.msgSuccess('保存成功')
              this.open = false
              this.getList()
            }
          }).catch(function () {
          })
        }else{
          save(this.form).then(res => {
            if (res.code === 200) {
              this.msgSuccess('保存成功')
              this.open = false
              this.getList()
            }
          }).catch(function () {
          })
        }
      },
      /** 保存用户信息 */
      submitForm() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            if (!this.form.id) {
              this.$refs['formLogin'].validate(valid => {
                if (valid) {
                  this.saveForm();
                }
              })
            }else {
              this.saveForm();
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const name = row.name
        this.$confirm('是否确认删除"' + name + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function () {
          return del(row.id)
        }).then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        }).catch(function () {
        })
      },
      handleView(row) {
        this.reset()
        get(row.id).then(res => {
          this.form = res.data
          this.getPostList(this.form.districtOid,this.form.organOid);
          if(null!=this.form.postOids && ''!=this.form.postOids){
            this.checkList = this.form.postOids.split(',');
          }
          this.openView = true
          this.title = '查看用户'
        })
      },
      handleRole(row){
        this.reset()
        get(row.id).then(res => {
          this.roleForm = res.data
        })
        getUserRole(row.userOid).then(res => {
          //处理已有角色
          let oids=[];
          if(res.data){
            var aa = setInterval(() => {
              if(this.appRoleOptions && this.appRoleOptions.length > 0) {
                for(let role of res.data){
                  oids.push('ROLE-'+role.roleOid);
                }
                this.roleForm.roleOids=oids;
                clearInterval(aa);
              }
            }, 200)
          }
        })
        let treeQuery = {
          appOid: '',
          disable: this.disable
        }
        roleTree(treeQuery).then(res => {
          res.data && res.data.forEach(item => {if(!item.children){item.children = []}})
          this.appRoleOptions=res.data;
          this.openRole=true;
          this.title="用户授权"
        })

      },
      submitRole(){
        if(this.roleForm.roleOids&&this.roleForm.roleOids.length>0){
          for(var i=0; i<this.roleForm.roleOids.length; i++){
            let  roleOid = this.roleForm.roleOids[i];
            roleOid = null!=roleOid?roleOid.substring(roleOid.length-32,roleOid.length):'';
            this.roleForm.roleOids[i] = roleOid;
          }
          saveUserRole(this.roleForm).then(res => {
            if (res.code === 200) {
              this.msgSuccess('保存成功')
              this.openRole = false
              this.getList()
            }
          })
        }else{
          this.$refs['roleForm'].validate(valid => {
          })
        }
      }

    }
  }

</script>
<style scoped>
  table {
    border-collapse: separate !important;
  }
  .postClass .treeselect{
    width:  98% !important;
  }

</style>
