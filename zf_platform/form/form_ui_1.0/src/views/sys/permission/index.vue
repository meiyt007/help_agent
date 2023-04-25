<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--权限应用数据-->
      <el-col :span="4" :xs="24" class="app-left">
        <div class="tree-title">权限管理</div>
        <div class="head-container">
          <el-input
            v-model="name"
            placeholder="请输入权限名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
            @keyup.enter.native="handleTreeQuery"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="appPermissionOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            :highlight-current="true"
            node-key="id"
            ref="tree"
            :default-expanded-keys="ArrData"
            @node-click="handleNodeClick"
          >
            <span class="custom-tree-node" slot-scope="{ node, data }">
               <el-tooltip v-if="node.label.length > 10" class="item" effect="dark" :content="node.label" placement="right-start">
                  <span style="font-size: 14px">{{ node.label | ellipsis(10) }} </span>
               </el-tooltip>
               <span v-if="node.label.length < 11" style="font-size: 14px">{{ node.label}} </span>
            </span>
          </el-tree>
        </div>
      </el-col>
      <!--权限数据-->
      <el-col :span="20" :xs="24" class="app-right">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
          <el-form-item label="权限名称" prop="name">
            <el-input
              v-model.trim="queryParams.name"
              placeholder="请输入权限名称"
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
            >新增
            </el-button>
          </el-col>
        </el-row>
        <el-table v-loading="loading" :data="permissionList" border>
          <el-table-column label="序号" align="center" type="index"/>
          <el-table-column label="权限名称" align="center" prop="name" :show-overflow-tooltip="true"/>
          <!--<el-table-column label="路由地址" align="center" prop="router" :show-overflow-tooltip="true"/>-->
          <el-table-column label="访问路径" align="center" prop="hyperLink" :show-overflow-tooltip="true"/>
          <el-table-column label="权限标识" align="center" prop="str" :show-overflow-tooltip="true"/>
          <el-table-column label="类型" align="center" prop="functionType" :formatter="typeFormat" :show-overflow-tooltip="true"/>
          <el-table-column label="图标" align="center" prop="iconName" >
            <template slot-scope="scope">
              <i :class="scope.row.iconName"/>
            </template>
          </el-table-column>
          <el-table-column label="状态" prop="isAble"  align="center">
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
            width="180"
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
                icon="iconfont zfsoft-xiugai"
                @click="handleUpdate(scope.row)"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleDelete(scope.row)"
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
      </el-col>
    </el-row>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" :close-on-click-modal="false" width="70%" append-to-body>
      <el-tabs v-model="activeTab" :before-leave="tabBeforeLeave">
        <el-tab-pane label="权限信息" name="info">
          <div class="el-table__header-wrapper dialog-table">
            <el-form ref="formInfo" :model="form.info" :rules="rules" label-width="0px">
              <table cellspacing="0" cellpadding="0" border="0" >
                <el-input v-show="false" v-model="form.info.permissionOid"/>
                <el-input v-show="false" v-model="form.info.id" />
                <colgroup>
                  <col width="20%" />
                  <col width="30%" />
                  <col width="20%" />
                  <col width="30%" />
                </colgroup>
                <tr>
                  <td><b>应用名称：</b></td>
                  <td>
                    {{form.info.appName}}
                  </td>
                  <td><b>父级权限：</b></td>
                  <td>
                    {{form.info.parentName}}
                  </td>
                </tr>
                <tr>
                  <td><i class="require">*</i><b>权限名称：</b></td>
                  <td>
                    <el-form-item prop="name">
                      <el-input placeholder="请输入权限名称" v-model.trim="form.info.name" maxlength="10" show-word-limit/>
                    </el-form-item>
                  </td>
                  <td><i class="require">*</i><b>图标：</b></td>
                  <td>
                    <el-form-item prop="iconName">
                      <el-input v-show="false" v-model="form.info.iconName"/>
                      <i style="font-size: 30px;" :class="form.info.iconName"/>
                      <el-button
                        type="primary"
                        size="mini"
                        @click="handleIconSelect"
                      >选择图标
                      </el-button>
                    </el-form-item>
                  </td>
                </tr>
                <tr>
                  <td><b>权限类型：</b></td>
                  <td>
                    <el-form-item prop="functionType">
                      <el-radio-group @change="functionTypeChange" v-model="form.info.functionType">
                        <el-radio label="L">菜单</el-radio>
                        <el-radio label="B">按钮</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </td>
                  <td><b>是否外链：</b></td>
                  <td>
                    <el-form-item prop="isFrame">
                      <el-radio-group v-model="form.info.isFrame" @change="isFrameChange">
                        <el-radio :label="1">是</el-radio>
                        <el-radio :label="0">否</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </td>
                </tr>
                <tr id="tr1" v-if="form.info.isFrame=='0' && form.info.functionType=='L'">
                  <td><i class="require">*</i><b>路由地址：</b></td>
                  <td  colspan="3">
                    <el-col :span="12" style="width: 100%">
                      <el-form-item prop="router" ref="routerForm">
                        <el-input placeholder="请输入路由地址" v-model.trim="form.info.router" maxlength="100" show-word-limit/>
                      </el-form-item>
                    </el-col>
                  </td>
                </tr>
                <tr  id="tr2" v-if="form.info.isFrame=='0' && form.info.functionType=='L' && form.info.parentName!=null">
                  <td><i class="require">*</i><b>访问路径：</b></td>
                  <td colspan="3">
                    <el-col :span="12" style="width: 100%">
                      <el-form-item prop="hyperLink">
                        <el-input placeholder="请输入访问路径" v-model.trim="form.info.hyperLink" maxlength="250" show-word-limit/>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12" v-if="form.info.isFrame=='Y'" >
                      <el-input v-show="false" v-model.trim="form.info.hyperLink" />
                    </el-col>
                  </td>
                </tr>
                <tr  id="tr3" v-if="form.info.isFrame=='0' && form.info.functionType=='B'">
                  <td><b>访问路径：</b></td>
                  <td colspan="3">
                    <el-col :span="12" style="width: 100%">
                      <el-form-item prop="hyperLink1" ref="hyperLink1Form">
                        <el-input placeholder="请输入访问路径" v-model.trim="form.info.hyperLink" maxlength="250" show-word-limit/>
                      </el-form-item>
                    </el-col>
                  </td>
                </tr>
                <tr  id="tr4" v-if="form.info.isFrame=='1'">
                  <td><i class="require">*</i><b>外链地址：</b></td>
                  <td  colspan="3">
                    <el-input v-show="false" v-model.trim="form.info.router" />
                    <el-col :span="12"  style="width: 100%">
                      <el-form-item prop="outLink" ref="outLinkForm">
                        <el-input placeholder="请输入外链地址" v-model.trim="form.info.outLink" maxlength="200" show-word-limit/>
                      </el-form-item>
                    </el-col>
                  </td>
                </tr>
                <tr>
                  <td><i class="require" v-if="form.info.functionType=='B'">*</i><b>权限标识：</b></td>
                  <td>
                    <el-form-item prop="str" key="key" v-if="form.info.functionType=='B'">
                      <el-input placeholder="请输入权限标识" v-model.trim="form.info.str" maxlength="200" show-word-limit/>
                    </el-form-item>
                    <el-form-item v-if="form.info.functionType=='L'">
                      <el-input placeholder="请输入权限标识" v-model.trim="form.info.str" maxlength="200" show-word-limit/>
                    </el-form-item>
                  </td>
                  <td><i class="require">*</i><b>排序号：</b></td>
                  <td>
                    <el-form-item prop="sort">
                      <el-input-number placeholder="请输入排序号" v-model="form.info.sort" :min="1" :max="9999"/>
                    </el-form-item>
                  </td>
                </tr>
                <tr>
                  <td><b>备注：</b></td>
                  <td colspan="3">
                    <el-form-item prop="memo">
                      <el-input type="textarea" :rows="3" v-model.trim="form.info.memo" maxlength="500" show-word-limit></el-input>
                    </el-form-item>
                  </td>
                </tr>
              </table>
              <el-form-item style="padding-top: 40px; float: right">
                <el-button type="primary"  @click="saveInfo">确认</el-button>
                <el-button  @click="open = false">取消</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>

        <el-tab-pane label="权限链接" name="link">
          <el-form ref="formLink" :model="form" :rules='links.rules'>
            <el-row :gutter="10" class="mb8">
              <el-col :span="1.5">
                <el-button
                  type="primary"
                  icon="el-icon-plus"
                  size="mini"
                  @click="addLink"
                >新增
                </el-button>
              </el-col>
            </el-row>
            <el-table v-loading="loading" :data="form.links" border>
              <el-table-column label="链接地址" align="center" min-width="10%">
                <template slot-scope="scope">
                  <el-form-item :prop="'links.' + scope.$index + '.perLink'" :rules='links.rules.perLink'>
                    <el-input placeholder="请输入链接地址" v-model.trim="scope.row.perLink" maxlength="250" show-word-limit/>
                  </el-form-item>
                </template>
              </el-table-column>
              <el-table-column label="备注" align="center" min-width="10%">
                <template slot-scope="scope">
                  <el-form-item prop="memo">
                    <el-input placeholder="请输入备注" v-model.trim="scope.row.memo" maxlength="500" show-word-limit/>
                  </el-form-item>
                  <!--<el-input v-model="scope.row.memo" maxlength="1000"></el-input>-->
                </template>
              </el-table-column>
              <el-table-column
                label="操作"
                align="center"
                width="90"
                class-name="small-padding fixed-width"
              >
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    icon="iconfont zfsoft-shanchu"
                    @click="deleteLink(scope.$index, form.links)"
                  >删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-form-item style="padding-top: 40px; float: right">
              <el-button size="medium" type="primary" @click="saveLink">确认</el-button>
              <el-button size="medium" @click="open = false">取消</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <!--引入图标选择弹出层-->
        <el-dialog title="选择按钮图标" :visible.sync="openIconSelectView" :close-on-click-modal="false" width="900px" append-to-body>
          <el-scrollbar style="height:500px;">
            <iconfont  @father-click="getSelectClass" ></iconfont>
          </el-scrollbar>
        </el-dialog>
      </el-tabs>


    </el-dialog>
    <!--查看对话框-->
    <el-dialog :title="title" :visible.sync="openView" :close-on-click-modal="false" width="70%" append-to-body>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="权限信息" name="info">
          <div class="el-table__header-wrapper dialog-table">
            <el-form  :model="form.info" label-width="0px">
              <table cellspacing="0" cellpadding="0" border="0" >
                <colgroup>
                  <col width="20%" />
                  <col width="30%" />
                  <col width="20%" />
                  <col width="30%" />
                </colgroup>
                <tr>
                  <td><b>应用名称：</b></td>
                  <td>
                    {{form.info.appName}}
                  </td>
                  <td><b>父级权限：</b></td>
                  <td>
                    {{form.info.parentName}}
                  </td>
                </tr>
                <tr>
                  <td><b>权限名称：</b></td>
                  <td>
                    {{form.info.name}}
                  </td>
                  <td><b>图标：</b></td>
                  <td>
                    <i style="font-size: 30px;" :class="form.info.iconName"/>
                  </td>
                </tr>
                <tr>
                  <td><b>权限类型：</b></td>
                  <td>
                    {{form.info.functionType == 'L' ?'菜单':'按钮'}}
                  </td>
                  <td><b>是否外链：</b></td>
                  <td>
                    {{form.info.isFrame == 1 ?'是':'否'}}
                  </td>
                </tr>
                <tr v-if="form.info.isFrame == 0">
                  <td><b>路由地址：</b></td>
                  <td>
                    {{form.info.router}}
                  </td>
                  <td><b>访问路径：</b></td>
                  <td>
                    {{form.info.hyperLink}}
                  </td>
                </tr>
                <tr v-if="form.info.isFrame == 1">
                  <td><b>外链地址：</b></td>
                  <td colspan="3">
                    {{form.info.outLink}}
                  </td>
                </tr>
                <tr>
                  <td><b>权限标识：</b></td>
                  <td>
                    {{form.info.str}}
                  </td>
                  <td><b>排序号：</b></td>
                  <td>
                    {{form.info.sort}}
                  </td>
                </tr>
                <tr>
                  <td><b>备注：</b></td>
                  <td colspan="3">
                    {{form.info.memo}}
                  </td>
                </tr>
              </table>
            </el-form>
          </div>
        </el-tab-pane>


        <el-tab-pane label="权限链接" name="link">
          <el-form :model="form">
            <el-table v-loading="loading" :data="form.links" border>
              <el-table-column label="链接地址" align="center" min-width="10%">
                <template slot-scope="scope">
                  {{scope.row.perLink}}
                </template>
              </el-table-column>
              <el-table-column label="备注" align="center" min-width="10%">
                <template slot-scope="scope">
                  {{scope.row.memo}}
                </template>
              </el-table-column>
            </el-table>
          </el-form>
        </el-tab-pane>

      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { appPermissionTree, save, page, init, saveLink, able, del, get } from '@/api/sys/permission'
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'
  import iconfont from '@/views/common/iconfontSelect'
  import { validIntNoZero } from '@/utils/validate'
  export default {
    name: 'Permission',
    components: { Treeselect, iconfont },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 权限表格数据
        permissionList: [],
        // 弹出层标题
        title: '',
        // 权限树选项
        appPermissionOptions: [],
        // 是否显示弹出层
        open: false,
        openView: false,
        activeTab: 'info',
        //图标样式弹出层
        openIconSelectView: false,
        // 权限名称
        name: undefined,
        // 表单参数
        form: { info: {}, links: [] },
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        //消息
        ableMap: { '1': '启用', '0': '禁用' },
        //菜单类型状态
        typeMap: { 'L': '菜单', 'B': '按钮' },
        ArrData:[],
        defaultAppOid:'',
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: '',
          appOid: null,
          parentOid: null
        },
        // 表单校验
        rules: {
          name: [
            { required: true, message: '权限名称不能为空', trigger: 'blur' }
          ],
          iconName: [
            { required: true, message: '图标不能为空', trigger: 'blur' }
          ],
          router: [
            { required: true, message: '路由地址不能为空', trigger: 'blur' }
          ],
          hyperLink: [
            { required: true, message: '访问路径不能为空', trigger: 'blur' }
          ],
          hyperLink1: [
          ],
          str:[
            { required: true, message: '权限标识不能为空', trigger: 'blur' }
          ],
          outLink:[
            { required: true, message: '外链地址不能为空', trigger: 'blur' }
          ],
          sort: [
            { required: true, message: '排序号不能为空', trigger: 'blur' },
            { validator: validIntNoZero, trigger: 'blur' }
          ]
        },
        links: {
          rules: {
            perLink: [
              { required: true, message: '链接地址不能为空', trigger: 'blur' }
            ]
          }
        }
      }
    },
    watch: {
      // 根据名称筛选部门树
      name(val) {
        if(null!=val){
          val = val.replace(/^\s+|\s+$/g,"");
        }
        this.$refs.tree.filter(val)
      }
    },
    created() {
      this.getTreeselect()
    },
    methods: {
      /** 查询权限列表 */
      getList() {
        this.loading = true
        let that =this;
        page(this.queryParams).then(response => {
            this.permissionList = response.data.data
            this.total = response.data.total
            this.loading = false
          }
        ).catch(function() {that.loading = false;});
      },
      /** 选择图标 */
      handleIconSelect() {
        this.openIconSelectView = true
      },
      // 消息
      statusFormat(row) {
        return this.selectMapLabel(this.ableMap, row.isAble)
      },
      // 类型状态
      typeFormat(row) {
        return this.selectMapLabel(this.typeMap, row.functionType)
      },
      //从子组件中获取选择的图标
      getSelectClass(className) {
        this.form.info.iconName = className
        this.openIconSelectView = false
      },
      tabBeforeLeave(activeName, oldActiveName) {
        if ('link' === activeName) {
          if (!this.form.info.id) {
            this.activeTab = 'info'
            this.msgWarning('请先保存权限信息！')
            return false
          }
        }
      },
      /** 查询下拉树结构 */
      getTreeselect() {
        appPermissionTree(this.name).then(response => {
          this.appPermissionOptions = response.data
          this.defaultFrist(response.data);
          if (this.appPermissionOptions.length>0){
            this.defaultAppOid = this.appPermissionOptions[0].id.replace('APP-','');
            this.queryParams.appOid = this.defaultAppOid;
          }
          this.getList()
        }).then(res => {
          let id = ''
          if (!this.queryParams.appOid && !this.queryParams.parentOid) {
            id = this.appPermissionOptions[0].id
            this.queryParams.appOid = id
            this.queryParams.parentOid = null
          }
          if (this.queryParams.appOid) {
            id = this.queryParams.appOid
            this.queryParams.appOid = id
            this.queryParams.parentOid = null
          }
          if (this.queryParams.parentOid) {
            id = this.queryParams.parentOid
            this.queryParams.appOid = null
            this.queryParams.parentOid = id
          }
          this.$nextTick(function() {
            this.$refs.tree.setCurrentKey('APP-'+id)
          })
        })
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true
        return data.label.indexOf(value) !== -1
      },
      // 节点单击事件
      handleNodeClick(data) {
        data.id = null!=data.id?data.id.substring(data.id.lastIndexOf('-')+1,data.id.length):'';
        if (undefined != data.identity && data.identity.includes('APP')) {
          this.queryParams.appOid = data.id
          this.queryParams.parentOid = null
        } else {
          this.queryParams.parentOid = data.id
          this.queryParams.appOid = null
        }
        this.getList()
      },
      selectParent(node, id) {
        if (node.identity.includes('APP')) {
          this.form.info.appOid = node.id
          this.form.info.parentOid = null
        } else {
          this.form.info.parentOid = node.id
          this.form.info.appOid = null
        }
      },
      reset() {
        this.formInfo = {}
        this.activeTab = 'info'
        this.resetForm('formInfo')
        this.resetForm('formLink')
      },
      // 消息修改
      handleStatusChange(row) {
        let text = row.isAble == 1 ? '启用' : '禁用'
        this.$confirm('确认要"' + text + '""' + row.name + '"权限吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function() {
          return able(row.id)
        }).then(() => {
          this.msgSuccess(text + '成功')
        }).catch(function() {
          row.isAble = row.isAble == 0 ? 1 : 0
        })
      }
      ,
      // 取消按钮
      cancel() {
        this.open = false
        this.reset()
      }
      ,
      handleTreeQuery() {
        this.getTreeselect()
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
        this.dateRange = []
        this.resetForm('queryForm')
        this.handleQuery()
      }
      ,
      /** 新增按钮操作 */
      handleAdd: function() {
        this.reset()
        const initData = {}
        initData.parentOid = this.queryParams.parentOid
        initData.appOid = this.queryParams.appOid
        this.open = true
        this.title = '新增权限信息'
        init(initData).then(response => {
          this.form.info = response.data
          this.form.links = response.data.childrenLink
          this.form.info.functionType = 'L'
          this.form.info.isFrame = 0
        })
      }
      ,
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        const initData = {}
        initData.parentOid = this.queryParams.parentOid
        initData.appOid = this.queryParams.appOid
        initData.id = row.id
        init(initData).then(response => {
          this.form.info = response.data
          this.form.links = response.data.childrenLink
          this.open = true
          this.title = '修改权限信息'
        })
      },
      /** 保存权限信息 */
      saveInfo: function() {
        this.$refs['formInfo'].validate(valid => {
          if (valid) {
            let permissionOid = this.form.info.permissionOid;
            save(this.form.info).then(res => {
              if (res.code === 200) {
                this.form.info = res.data
                this.msgSuccess('保存成功')
                this.open = false
                if(permissionOid == undefined || null == permissionOid){
                  //新增一个节点
                  const node = this.$refs.tree.getCurrentNode();
                  this.append(node,'PERMISSION-' + this.form.info.permissionOid,this.form.info.name);
                }else {
                  //更新一个节点
                  const node = this.$refs.tree.getCurrentNode();
                  if(node){
                    this.update(node,'PERMISSION-' + this.form.info.permissionOid,this.form.info.name);
                  }
                }
                this.getList()
                //this.getTreeselect()
              }
            })
          }
        })
      },
      /** 保存权限链接 */
      saveLink: function() {
        this.$refs['formLink'].validate(valid => {
          if (valid) {
            for (var val of this.form.links) {
              val.permissionOid = this.form.info.permissionOid
            }
            let links = [];
            if(this.form.links.length != 0 ){
              links = JSON.parse(JSON.stringify(this.form.links));
            }else {
              links.push({
                "permissionOid": this.form.info.permissionOid
              });
            }
            saveLink(links).then(res => {
              if (res.code === 200) {
                this.msgSuccess('保存成功')
                this.open = false
              }
            })
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
        }).then(function() {
          return del(row.id)
        }).then(() => {
          this.getList()
          //this.getTreeselect()
          if(''!=this.queryParams.parentOid && ''!=this.queryParams.appOid){
            const node = this.$refs.tree.getCurrentNode();
            this.remove(node,'PERMISSION-'+row.permissionOid);
          }else {
            this.getTreeselect();
          }
          this.msgSuccess('删除成功')
        }).catch(function() {
        })
      },
      //新增一个节点
      append(node,newId,label) {
        const newChild = { id:newId, label: label, children: [] };
        if (!node.children) {
          this.$set(node, 'children', []);
        }
        node.children.push(newChild);
      },
      //更新一个节点
      update(node,oid,name){
        const children = node.children || node;
        const updateNode = children.find(d => d.id === oid);
        updateNode.label = name;
        this.$refs.tree.updateKeyChildren(updateNode.id,updateNode);
      },
      //移除节点
      remove(node, id) {
        const children = node.children || node;
        const index = children.findIndex(d => d.id === id);
        children.splice(index, 1);
      },
      handleView(row) {
        this.reset()
        get(row.id).then(response => {
          this.form.info = response.data
          this.form.links = response.data.childrenLink
          this.openView = true
          this.title = '查看权限'
        })
      },
      /** 添加链接*/
      addLink() {
        let link = {}
        if (!this.form.links) {
          this.form.links = []
        }
        this.form.links.push(link)
      },
      /** 删除链接*/
      deleteLink(index, rows) {
        rows.splice(index, 1)
      },
      //是否外连接切换
      isFrameChange(data){
        var timestamp=new Date().getTime();
        if('1' == data){
          this.form.info.hyperLink='iframe/index';
          this.form.info.router='outLink/'+timestamp;
        }else{
          this.$set(this.form.info, "hyperLink", '');
          this.$set(this.form.info, "router", '');
          this.$set(this.form.info, "outLink", '');
          this.$refs['outLinkForm'] && this.$refs['outLinkForm'].clearValidate();
          this.$refs['routerForm'] &&this.$refs['routerForm'].clearValidate();
          this.$refs['hyperLink1Form'] &&this.$refs['hyperLink1Form'].clearValidate();

        }
        this.$refs['formInfo'].clearValidate();
      },
      functionTypeChange(){
        this.$refs['formInfo'].clearValidate('hyperLink');
        this.$refs['formInfo'].clearValidate('router');
        this.$refs['formInfo'].clearValidate('outLink');
        this.$refs['outLinkForm'] && this.$refs['outLinkForm'].clearValidate();
        this.$refs['routerForm'] &&this.$refs['routerForm'].clearValidate();
        this.$refs['hyperLink1Form'] &&this.$refs['hyperLink1Form'].clearValidate();
      },
      //默认展开第一级
      defaultFrist(permissionTreeData){
        var arr = [];
        for (let i = 0; i < permissionTreeData.length; i++) {
          if ('undefined' != typeof(permissionTreeData[i].children) && permissionTreeData[i].children.length>0) {
            arr.push(permissionTreeData[i].id)
          }
          this.ArrData=arr;
        }
      }

    }
  }

</script>
