<template>
  <div class="app-container">
    <!--角色数据-->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="所属应用">
        <el-select
          v-model="queryParams.appOid"
          placeholder="所属应用"
          size="small"
          style="width: 240px"
          clearable
        >
          <el-option
            v-for="app in appList"
            :key="app.appOid"
            :label="app.name"
            :value="app.appOid"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="角色名称">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入角色名称"
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
          v-hasPermi="['sys:role:addUpdate']"
        >新增
        </el-button>
      </el-col>

    </el-row>
    <el-table v-loading="loading" :data="roleList" border>
      <el-table-column label="序号" align="center" type="index"/>
      <el-table-column label="所属应用" align="center" prop="appName" :show-overflow-tooltip="true"/>
      <el-table-column label="角色名称" align="center" prop="name" :show-overflow-tooltip="true"/>
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
            v-hasPermi="['sys:role:view']"
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
            icon="iconfont zfsoft-shouquan"
            @click="handleRole(scope.row)"
          >授权
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sys:role:detele']"
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
    <!-- 添加或修改角色配置对话框 -->
    <el-dialog :title="title" v-dialog-drag :visible.sync="open" :close-on-click-modal="false" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <table cellspacing="0" cellpadding="0">
            <el-input v-show="false" v-model="form.roleOid"/>
            <el-input v-show="false" v-model="form.id" />
            <colgroup>
              <col width="20%" />
              <col width="80%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>角色名称：</b></td>
              <td>
                <el-form-item prop="name">
                  <el-input v-model.trim="form.name" placeholder="请输入角色名称" maxlength="50" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>所属应用：</b></td>
              <td>
                <el-form-item prop="appOid">
                  <el-select v-model="form.appOid" placeholder="请选择">
                    <el-option
                      v-for="app in appList"
                      :key="app.appOid"
                      :label="app.name"
                      :value="app.appOid"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>排序号：</b></td>
              <td>
                <el-form-item prop="sort">
                  <el-input-number v-model="form.sort" :step="1" :min="1" :max="9999"/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>菜单权限：</b></td>
              <td>
                <el-form-item>
                  <el-tree
                    :data="permissionOptions"
                    show-checkbox
                    ref="permissionElTree"
                    node-key="id"
                    :check-strictly="true"
                    :default-expanded-keys="expandedKeys"
                    :empty-text="permissionText"
                    :props="defaultProps"
                    @check="clickDeal"
                  ></el-tree>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>备注：</b></td>
              <td>
                <el-form-item>
                  <el-input v-model.trim="form.note" type="textarea" placeholder="请输入内容" maxlength="300" show-word-limit></el-input>
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


    <!-- 查看角色配置对话框 -->
    <el-dialog :title="title" :visible.sync="openView" :close-on-click-modal="false" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <el-form ref="viewform" :model="form" label-width="0px" >
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="20%" />
              <col width="80%" />
            </colgroup>
            <tr>
              <td><b>角色名称：</b></td>
              <td>
                {{form.name}}
              </td>
            </tr>
            <tr>
              <td><b>所属应用：</b></td>
              <td>
                {{ form.appName }}
              </td>
            </tr>
            <tr>
              <td><b>排序号：</b></td>
              <td>
                {{form.sort}}
              </td>
            </tr>
            <tr>
              <td><b>菜单权限：</b></td>
              <td>
                <el-form-item>
                  <el-tree
                    :data="permissionOptions"
                    show-checkbox
                    ref="viewPermissionElTree"
                    node-key="id"
                    :check-strictly="true"
                    :empty-text="permissionText"
                    :default-expanded-keys="expandedKeys"
                    :props="defaultProps"
                  ></el-tree>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>备注：</b></td>
              <td>
                {{form.note}}
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--授权角色--><!--:rules="authorizeRules" :disable-branch-nodes="true"  :defaultExpandLevel="1"-->
    <el-dialog :title="title" v-dialog-drag :visible.sync="openAuthorize" :close-on-click-modal="false" width="70%">
      <div class="el-table__header-wrapper dialog-table">
        <el-form ref="authorizeForm" :model="authorize" label-width="0px" >
          <table>
            <colgroup>
              <col width="20%" />
              <col width="80%" />
            </colgroup>
            <tr>
              <td><b>所属应用：</b></td>
              <td>
                {{authorize.appName}}
              </td>
            </tr>
            <tr>
              <td><b>角色名称：</b></td>
              <td>
                {{authorize.name}}
              </td>
            </tr>
            <tr>
              <td><b>选择用户：</b></td>
              <td>
                <!--<el-input
                  v-model="selectUserName"
                  placeholder="请输入用户名称"
                  clearable
                  size="small"
                  prefix-icon="iconfont zfsoft-chakan"
                />-->
                <el-form-item prop="userOids">
                  <el-tree
                    show-checkbox
                    node-key="id"
                    ref="userTree"
                    :load="loadNode"
                    :filter-node-method="filterNode"
                    lazy
                    highlight-current
                    :check-strictly="true"
                    :default-checked-keys="[]"
                    :default-expanded-keys="userExpandedKeys"
                    @check="clickUserDeal"
                    @check-change="handleCheckChange"
                    :expand-on-click-node="false"
                    :props="props">
                    <span class="custom-tree-node" slot-scope="{ node, data }">
                       <el-tooltip v-if="node.label.length > 8" class="item" effect="dark" :content="node.label" placement="right-start">
                          <span style="font-size: 14px">{{ node.label | ellipsis(8) }} </span>
                       </el-tooltip>
                       <span v-if="node.label.length < 9" style="font-size: 14px">{{ node.label}} </span>
                    </span>
                  </el-tree>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitRole">确 定</el-button>
        <el-button @click="openAuthorize=false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {save, page, able, del, get,queryRoleUser,saveRoleUser} from '@/api/sys/role'
  import {applist} from '@/api/sys/app'
  import {permissionTree} from '@/api/sys/permission'
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'
  import iconfont from '@/views/common/iconfontSelect'

  export default {
    name: 'Role',
    components: {Treeselect, iconfont},
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 角色数据
        roleList: [],
        //应用数据
        appList: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        selectUserName:null,
        openView: false,
        disable: false,
        openAuthorize: false,
        userOids:[],
        // 表单参数
        form: {},
        permissionOptions: [],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        props: {
          label: "label",//这里是树结构中需显示的数据（即接口返回的需展示在页面上的参数）
          children: 'children',
          isLeaf: "leaf"
        },
        permissionText: '选择应用后才会加载菜单权限',
        //消息
        ableMap: {'1': '启用', '0': '禁用'},
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: '',
          appOid: null
        },
        expandedKeys: [],
        userExpandedKeys:[],
        // 表单校验
        rules: {
          name: [
            {required: true, message: '角色名称不能为空', trigger: 'blur'}
          ],
          appOid: [
            {required: true, message: '所属应用不能为空', trigger: 'change'}
          ],
          sort: [
            {required: true, message: '排序号不能为空', trigger: 'change'}
          ]
        },
        userOptions:[],
        authorize:{userOids:[]},
      }
    },
    watch: {
      'form.appOid': function (val, oldVal) {
        this.getPermissionTreeSelect(val)
      },
      // 根据名称筛选树
      selectUserName(val) {
        //暂时不开放，只能查询展开的tree数据
        //this.$refs.userTree.filter(val);
      }
    },
    created() {
      this.getList()
      this.getAppList();
    },
    methods: {
      /** 应用列表 */
      getAppList() {
        applist().then(response => {
            this.appList = response.data
          }
        )
      },
      /** 查询角色列表 */
      getList() {
        this.loading = true
        let that =this;
        page(this.queryParams).then(response => {
            this.roleList = response.data.data
            this.total = response.data.total
            this.loading = false
          }
        ).catch(function() {that.loading = false;});
      },
      /** 获取应用权限*/
      getPermissionTreeSelect(appOid) {
        let treeQuery = {
          appOid: appOid,
          disable: this.disable
        }
        if (appOid) {
          permissionTree(treeQuery).then(response => {
              this.permissionOptions = response.data
              this.permissionText = '数据为空！'
            }
          )
        } else {
          this.permissionOptions = []
        }
      },
      /** 根据角色ID查询菜单树结构 */
      getRolePermissionTreeselect(roleId) {
        get(roleId).then(response => {
          this.form = response.data.sysRole
          let initApp = this.appList.find(item => item.appOid == this.form.appOid)
          if(undefined == initApp || null ==initApp){
            this.form.appOid = '';
          }
          for(var i=0; i<response.data.permissionOids.length; i++){
            let  permissionId = response.data.permissionOids[i];
            permissionId = 'PERMISSION-'+permissionId;
            response.data.permissionOids[i] = permissionId;
          }
          this.$refs.permissionElTree.setCheckedKeys(response.data.permissionOids)
          if (this.permissionOptions && this.permissionOptions.length>0) {
            this.expandedKeys.push(this.permissionOptions[0].id)
          }
        })
      },
      /** 根据角色ID查询菜单树结构 */
      getViewRolePermissionTreeselect(roleId) {
        get(roleId).then(response => {
          this.form = response.data.sysRole
          for(var i=0; i<response.data.permissionOids.length; i++){
            let  permissionId = response.data.permissionOids[i];
            permissionId = 'PERMISSION-'+permissionId;
            response.data.permissionOids[i] = permissionId;
          }
          this.$refs.viewPermissionElTree.setCheckedKeys(response.data.permissionOids)
          if (this.permissionOptions && this.permissionOptions.length>0) {
            this.expandedKeys.push(this.permissionOptions[0].id)
          }
        })
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 所有权限选择节点数据
      getPermissionAllCheckedKeys() {
        // 目前被选中的节点
        let checkedKeys = this.$refs.permissionElTree.getCheckedKeys()
        return checkedKeys
      },
      // 消息
      statusFormat(row) {
        return this.selectMapLabel(this.ableMap, row.isAble)
      },
      reset() {
        this.authorize={userOids:[]}
        this.expandedKeys = []
        this.disable = false;
        this.form = {}
        this.resetForm('form')
        this.resetForm('authorizeForm')
      },
      // 状态修改
      handleStatusChange(row) {
        let text = row.isAble == 1 ? '启用' : '禁用'
        this.$confirm('确认要"' + text + '""' + row.name + '"角色吗?', '警告', {
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
        this.queryParams.appOid=null
        this.queryParams.name=''
        this.getList();
      }
      ,
      /** 新增按钮操作 */
      handleAdd: function () {
        this.reset()
        this.$set(this.form, 'sort', 1);
        this.open = true
        this.title = '新增角色信息'
      }
      ,
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        this.getPermissionTreeSelect(row.appOid)
        this.$nextTick(() => {
          this.getRolePermissionTreeselect(row.id, row.appOid)
        })
        this.open = true
        this.title = '修改角色信息'
      },
      /** 保存角色信息 */
      submitForm: function () {
        this.form.permissionIds = this.getPermissionAllCheckedKeys();
        for(var i=0; i<this.form.permissionIds.length; i++){
          let  permissionId = this.form.permissionIds[i];
          permissionId = null!=permissionId?permissionId.substring(permissionId.lastIndexOf('-')+1,permissionId.length):'';
          this.form.permissionIds[i] = permissionId;
        }
        this.$refs['form'].validate(valid => {
          if (valid) {
            if(0 == this.form.permissionIds.length){
              this.msgError('请选择菜单权限！')
              return false;
            }
            save(this.form).then(res => {
              if (res.code === 200) {
                this.msgSuccess('保存成功')
                this.open = false
                this.getList()
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
        this.disable = true;
        this.getPermissionTreeSelect(row.appOid)
        this.$nextTick(() => {
          this.getViewRolePermissionTreeselect(row.id, row.appOid)
        })
        this.openView = true
        this.title = '查看角色'
      },
      getRoleUser(roleId){
        queryRoleUser(roleId).then(response => {
          let oids=[];
          if(response.data){
            this.userOids = [];
            this.authorize.userOids=[];
            for(let user of response.data){
              /*oids.push('USER-'+user.userOid)
              oids.push('DISTRICT-'+user.districtOid)
              oids.push('ORGAN-'+user.organOid)
              this.userOids.push('USER-'+user.userOid);*/
              oids.push(user)
              if(user.indexOf('USER')>-1){
                this.userOids.push(user);
              }
            }
          }
          this.authorize.userOids=oids;
          this.setUserTreeChecked();
        })
      },
      //数组去重
      unique(arr){
        for (var i = 0, len = arr.length; i < len; i++) {
          for (var j = i + 1, len = arr.length; j < len; j++) {
            if (arr[i] === arr[j]) {
              arr.splice(j, 1);
              j--;        // 每删除一个数j的值就减1
              len--;      // j值减小时len也要相应减1（减少循环次数，节省性能）
              // console.log(j,len)

            }
          }
        }
        return arr;
      },
      setUserTreeChecked(){
        this.$nextTick(() => {
          if(null!=this.authorize.userOids){
           let nodes=this.$refs.userTree.getCheckedNodes();
            if(nodes){
              for(let user of nodes){
                this.authorize.userOids.push(user.id);
              }
            }
            this.authorize.userOids = this.unique(this.authorize.userOids);
            this.$refs.userTree.setCheckedKeys(this.authorize.userOids);
          }
        })
      },
      handleRole(row) {
        this.openAuthorize=true
        this.title="授权"
        this.$nextTick(() => {
          this.$refs.userTree.setCheckedKeys([]);
        });

        get(row.id).then(response => {
          this.authorize = response.data.sysRole
          this.getRoleUser(row.roleOid);
        })

      },
      submitRole(){
        let nodes=this.$refs.userTree.getCheckedNodes();
        let oids=[];
        if(nodes){
          for(let user of nodes){
            if(user.identity==="USER"){
              oids.push(user.id)
            }
          }
        }
        for(var i=0; i<this.userOids.length; i++){
          let dataOid = this.userOids[i];
          if(dataOid.indexOf('USER-')>-1){
            dataOid = null!=dataOid?dataOid.substring(dataOid.lastIndexOf('-')+1,dataOid.length):'';
            this.userOids[i] = dataOid;
          }
        }
        this.authorize.userOids = this.userOids;
        /*if(0 == this.userOids.length){
          this.msgError('请选择用户！')
          return false;
        }*/
        saveRoleUser(this.authorize).then(res => {
          if (res.code === 200) {
            this.msgSuccess('保存成功')
            this.openAuthorize = false
            this.getList()
          }
        })
      },
      loadNode(node, resolve) {
        // 一级节点处理
        if (node.level === 0) {
          this.getIndex(node,resolve);
        }
        // 其余节点处理
        if (node.level >= 1) {
          // 注意！把resolve传到你自己的异步中去
          this.getTree(node, resolve);
        }
      },
      getIndex(node,resolve){
        let query={
          oid:'',
          identity: 'DISTRICT'
        }
        this.queryDistrictOrganUserTree(query).then(response => {
          this.userExpandedKeys = [];
          if(undefined!=response.data[0]){
            this.userExpandedKeys.push(response.data[0].id)
            resolve(response.data);
          }
        });
      },
      getTree(node,resolve){
        let dataId = null!=node.data.id?node.data.id.substring(node.data.id.lastIndexOf('-')+1,node.data.id.length):'';
        if(node.data.identity==="USER"){
          resolve([])
        }
        let query={
          oid:dataId,
          identity: node.data.identity
        }
        this.queryDistrictOrganUserTree(query).then(response => {
          if(undefined != response){
            node.data.children = response.data;
            this.userExpandedKeys.push(node.data.children.id)
            this.$refs.userTree.updateKeyChildren(node.data.id,node.data);
            resolve(response.data);
          }
        });
        this.setUserTreeChecked();
      },

      handleCheckChange(data, checked, indeterminate) {
        if(data.id.indexOf('USER-')>-1){
          if (checked) {
            if(this.userOids.indexOf(data.id) == -1){
              this.userOids.push(data.id);
            }
          } else {
            //表示先获取这个元素的下标，然后从这个下标开始计算，删除长度为1的元素
            this.userOids.splice(this.userOids.indexOf(data.id),1);

            /*getSysUserListByOrganOid(this.queryParams).then(response => {
              this.roleList = response.data.data
              this.total = response.data.total
              this.loading = false
            });*/
          }
        }
      },
      //权限
      clickDeal (currentObj, treeStatus) {
        // 用于：父子节点严格互不关联时，父节点勾选变化时通知子节点同步变化，实现单向关联。
        let selected = treeStatus.checkedKeys.indexOf(currentObj.id) // -1未选中
        // 选中
        var permissionElTree = this.$refs.permissionElTree;
        if (selected !== -1) {
          // 子节点只要被选中父节点就被选中
          this.selectedParent(currentObj,permissionElTree)
          // 统一处理子节点为相同的勾选状态
          this.uniteChildSame(currentObj, true,permissionElTree)
        } else {
          // 未选中 处理子节点全部未选中
          if (currentObj.children !== undefined && currentObj.children.length !== 0) {
            this.uniteChildSame(currentObj, false,permissionElTree)
          }
        }
      },
      initClickDeal (currentObj, treeStatus) {
        // 用于：父子节点严格互不关联时，父节点勾选变化时通知子节点同步变化，实现单向关联。
        let selected = treeStatus.checkedKeys.indexOf(currentObj.id) // -1未选中
        // 选中
        var permissionElTree = this.$refs.permissionElTree;
        if (selected !== -1) {
          // 子节点只要被选中父节点就被选中
          this.selectedParent(currentObj,permissionElTree)
          // 统一处理子节点为相同的勾选状态
          this.uniteChildSame(currentObj, true,permissionElTree)
        } else {
          // 未选中 处理子节点全部未选中
          if (currentObj.children !== undefined && currentObj.children.length !== 0) {
            this.uniteChildSame(currentObj, false,permissionElTree)
          }
        }
      },
      userOidsPush(currentObj){
        if (currentObj.children !== undefined && currentObj.children.length !== 0) {
          for(var i=0; i<currentObj.children.length; i++){
            let dataOid = currentObj.children[i];
            if(dataOid.indexOf('USER-')>-1){
              dataOid = null!=dataOid?dataOid.substring(dataOid.lastIndexOf('-')+1,dataOid.length):'';
              if(this.userOids.indexOf(dataOid) == -1){
                this.userOids.push(dataOid);
              }
            }
          }
        }
      },
      //选择用户
      clickUserDeal (currentObj, treeStatus) {
        // 用于：父子节点严格互不关联时，父节点勾选变化时通知子节点同步变化，实现单向关联。
        let selected = treeStatus.checkedKeys.indexOf(currentObj.id) // -1未选中
        // 选中
        var userTree = this.$refs.userTree;
        if (selected !== -1) {
          // 子节点只要被选中父节点就被选中
          this.selectedParent(currentObj,userTree)
          // 统一处理子节点为相同的勾选状态
          this.uniteChildSame(currentObj, true,userTree)
        } else {
          // 未选中 处理子节点全部未选中
          if (currentObj.children !== undefined && currentObj.children.length !== 0) {
            this.uniteChildSame(currentObj, false,userTree);
            //表示先获取这个元素的下标，然后从这个下标开始计算，删除长度为1的元素
            //this.users.splice(this.users.indexOf(user),1);
          }
        }
      },
      uniteChildSame (treeList, isSelected,treeObject) {
        treeObject.setChecked(treeList.id, isSelected)
        if (treeList.children && null!=treeList.children && treeList.children.length !== undefined) {
          for (let i = 0; i < treeList.children.length; i++) {
            this.uniteChildSame(treeList.children[i], isSelected,treeObject)
          }
        }

      },
      // 统一处理父节点为选中
      selectedParent (currentObj,treeObject) {
        let currentNode = treeObject.getNode(currentObj)
        if (currentNode.parent != null && currentNode.parent.key !== undefined) {
          treeObject.setChecked(currentNode.parent, true)
          this.selectedParent(currentNode.parent,treeObject)
        }
      },
    }
  }
</script>
<style>
  .el-checkbox__input.is-disabled.is-checked .el-checkbox__inner{
    background-color: #919294 !important;
    border-color: #919294 !important;
  }
  .dialog-table{
    width: 100%;
    height: 700px;
    overflow-y: scroll;
  }
</style>

