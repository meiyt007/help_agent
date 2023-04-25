/**
* @Author: wangns
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--事项数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="110px">
          <el-form-item label="业态目录名称" prop="comboDirectoryName">
            <el-input v-model.trim="queryParams.comboDirectoryName" placeholder="请输入业态目录名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="业态编码" prop="comboDirectoryCode">
            <el-input v-model.trim="queryParams.comboDirectoryCode" placeholder="请输入业态编码" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>

        <el-table v-loading="loading" :data="districtList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="所属区划" align="center"  prop="districtName" :show-overflow-tooltip="true"/>
          <el-table-column label="主办部门" align="center"  prop="mainOrganName" :show-overflow-tooltip="true"/>
          <el-table-column label="业态目录名称"  align="center" prop="comboDirectoryName" :show-overflow-tooltip="true"/>
          <el-table-column label="业态编码"  align="center" prop="comboDirectoryCode" :show-overflow-tooltip="true"/>
          <el-table-column label="发布状态"  align="center" :formatter="getPublishName" prop="ifPublish" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)" v-hasPermi="['sys:reg:view']" >查看</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInit(scope.row)" v-hasPermi="['sys:reg:update']">授权</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-quxiao" @click="deleteSQ(scope.row)">取消</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>
    <!-- 添加或修改信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" v-if="openInit" width="1100px" height='700px' scrollbar v-dialog-drag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>

          <tr>
            <td>
              <b>所属区划：</b>
            </td>
            <td>
              <el-form-item prop="districtName">
                <el-col :span="24">
                  {{ form.districtName }}
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b>主办部门：</b>
            </td>
            <td>
              <el-form-item prop="mainOrganName">
                <el-col :span="24">
                  {{ form.mainOrganName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>业态目录名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="comboDirectoryName">
                <el-col :span="24">
                  {{ form.comboDirectoryName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>业态编码：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="comboDirectoryCode">
                <el-col :span="24">
                  {{ form.comboDirectoryCode }}
                </el-col>
              </el-form-item>
            </td>
            <!--<td>
              <b>基础编码：</b>
            </td>
            <td>
              <el-form-item prop="basicCode">
                <el-col :span="24">
                  {{ form.basicCode }}
                </el-col>
              </el-form-item>
            </td>-->
          </tr>

          <tr>
            <td>
              <b>选择用户：</b>
            </td>
            <td colspan="3">
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
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 信息详细 -->
    <el-dialog v-dialog-drag :visible.sync="openView" v-if="openView"  title="目录信息"   width="1100px" height="700px" scrollbar append-to-body>
        <combo-directory-view :comboDirectoryOid="comboDirectoryOidView"></combo-directory-view>
              <div slot="footer" class="zf-text-center">
        <el-button @click="closeView">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import {page} from "@/api/onelicence/industryManager/industryManager";
import {getOneForAuthorize,saveOrUpdateForRegister,getIndustryOne,delIndustryDireReg} from "@/api/onelicence/industryAuthorize/industryAuthorize";
import comboDirectoryView from "@/views/onelicence/industryManager/industryDirectory/comboDirectoryView";
export default {
  name: "IndustryRegisterAuthorize",
  components: {Treeselect, comboDirectoryView},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      districtList: [],

      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      //目录查看使用
      comboDirectoryOidView:"",
      expandedKeys: [],
      userExpandedKeys:[],
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageNum: 1,
        pageSize: 10,
        comboDirectoryName: "",
        comboDirectoryCode:"",
        status:1,
        industryType: 1,
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      comboReg:{comboDirectoryOid:'',userOids:[],comboDirectoryName:'',mainOrganOid:'',mainOrganName:''},
      userOidArr:[],
      authorize:{userOids:[]},
      props: {
        label: "label",//这里是树结构中需显示的数据（即接口返回的需展示在页面上的参数）
        children: 'children',
        isLeaf: "leaf"
      },

    };
  },
  created() {
    this.getList();
  },
  methods: {

    /** 查询列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.districtList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.authorize={userOids:[]}
      Object.assign(this.form, this.$options.data().form)
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNumber = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.comboDirectoryOidView = row.comboDirectoryOid;
      this.openView = true;
      this.title = "查看业态目录信息";
    },
    closeView() {
      let _that = this;
      _that.openView= false;
      _that.getList();
    },
    deleteSQ(row){//删除授授
      const oid = row.comboDirectoryOid;
      this.$confirm('确认取消授权吗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delIndustryDireReg(oid);
      }).then(() => {
        this.msgSuccess("取消成功");
        this.getList();
      }).catch(function() {});
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      _that.reset();
      _that.userOids=[];
      if(row.comboDirectoryOid) {
        getIndustryOne(row.comboDirectoryOid).then(response => {
          _that.openInit = true;
          _that.form = response.data;
          this.comboReg.comboDirectoryOid=row.comboDirectoryOid;
          this.comboReg.comboDirectoryName=row.comboDirectoryName;
          this.comboReg.mainOrganName=row.mainOrganName;
          this.comboReg.mainOrganOid=row.mainOrganOid;

          getOneForAuthorize(row.comboDirectoryOid).then(response => {
            _that.openInit = true;
            let oids=[];
            if(response.data){
              for(let user of response.data){
               /* oids.push('USER-'+user.userOid)
                oids.push('DISTRICT-'+user.districtOid)
                oids.push('ORGAN-'+user.organOid)
                _that.userOids.push('USER-'+user.userOid);*/
                oids.push(user);
                if(user.indexOf('USER')>-1){
                  _that.userOids.push(user);
                }
              }
            }
            this.authorize.userOids=oids;
            this.setUserTreeChecked();
          });
        });
      } else {
        _that.openInit = true;
      }
      _that.title ="授权";

    },
    /** 提交按钮 */
    submitForm: function() {
      for(var i=0; i<this.userOids.length; i++){
        let dataOid = this.userOids[i];
        if(dataOid.indexOf('USER-')>-1){
          dataOid = null!=dataOid?dataOid.substring(dataOid.lastIndexOf('-')+1,dataOid.length):'';
          this.userOids[i] = dataOid;
        }
      }
      if(0 == this.userOids.length){
        this.msgError('请选择用户！')
        return false;
      }
      this.comboReg.userOids = this.userOids.toString();
      saveOrUpdateForRegister(this.comboReg).then(res => {
        if (res.code === 200) {
          this.msgSuccess("保存成功");
          this.openInit = false;
          setTimeout(() => {
            this.getList();
          }, 10);
        }
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
    getPublishName(val){
      if(val.status == 0){
        return '未发布';
      }else if(val.status == 1){
        return '已发布';
      }else{
        return '';
      }
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
        }
      }
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
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
  },
};
</script>
<style lang="scss" scoped>
.dialog-table {
  padding: 20px;
  box-sizing: border-box;
}
.dialog-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}
.dialog-table table {
  width: 100%;
}
.dialog-table table {
  border: 1px solid #dfe6ec;
  border-collapse: collapse;
}

.dialog-table table tr td {
  border: 1px solid #dfe6ec;
  padding: 17px 8px;
  font-size: 12px;
  color: #515a6e;
  text-align: right;
}
.dialog-table table tr td:nth-of-type(2n) {
  color: #606266;
  text-align: left !important;
}
.dialog-table .el-form-item {
  margin-bottom: 0;
}
.el-form-item--small .el-form-item_content {
  margin-left: 0px !important;
}
.line {
  text-align: center;
}
</style>
