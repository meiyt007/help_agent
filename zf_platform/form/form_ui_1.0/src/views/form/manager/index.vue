<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--树数据-->
      <el-col :span="4" :xs="24" class="app-left">
        <div class="tree-title">表单管理</div>
        <div class="head-container">
          <el-input
            v-model="systemName"
            placeholder="请输入系统名称"
            clearable
            size="small"
            prefix-icon="iconfont zfsoft-chakan"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="formOptions"
            :props="defaultProps"
            :expand-on-click-node="true"
            :highlight-current ="true"
            :filter-node-method="filterNode"
            ref="tree"
            accordion
            node-key="id"
            :default-expanded-keys="ArrData"
            @node-click="handleNodeClick">
            <span class="custom-tree-node" slot-scope="{ node, data }">
               <el-tooltip v-if="node.label.length > 8" class="item" effect="dark" :content="node.label" placement="right-start">
                  <span style="font-size: 14px">{{ node.label | ellipsis(8) }} </span>
               </el-tooltip>
               <span v-if="node.label.length < 9" style="font-size: 14px;font-weight: 500;">{{ node.label}} </span>
            </span>
          </el-tree>
        </div>
      </el-col>
      <!--数据-->
      <el-col :span="20" :xs="24" class="app-right">
          <Authorize v-if="dataFlag"
                     @appendAuthorize="appendAuthorize"
                     @updateAuthorize="updateAuthorize"
                     @removeAuthorize="removeAuthorize"
                     :authorizeKey="authorizeKey"  :isAdminUser="isAdminUser" ></Authorize>
          <IndexTab :key="authorizeKey" v-if="!dataFlag" :authorizeKey="authorizeKey" :userOid="userOid"></IndexTab>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryAuthorizeTree } from "@/api/form/authorize";
import Treeselect from "@riophae/vue-treeselect";
import Authorize from '@/views/form/authorize';
import IndexTab from '@/views/form/manager/indexTab.vue';
import Module from '@/views/form/module';
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  components: { Treeselect,Authorize,Module,IndexTab },
  name: "Manager",
  data() {
    return {
      // 树选项
      formOptions: undefined,
      ArrData:[],
      defaultProps: {
        children: "children",
        label: "label"
      },
      dataFlag:true,
      isAdminUser:false,
      authorizeKey:null,
      systemName:'',
      userOid:'',
      //tab
      authorizeOptions:[]
    };
  },
  watch: {
    // 根据名称筛选部门树
    systemName(val) {
      if(null!=val){
        val = val.replace(/^\s+|\s+$/g,"");
      }
      this.$refs.tree.filter(val);
    }
  },

  created() {
    const queryPar = this.$route.query;
    this.userOid = queryPar.userOid;
    this.getFormTree();
  },
  methods: {
    init() {
      this.getFormTree();
    },
    /** 获取表单树 */
    getFormTree() {
      queryAuthorizeTree(this.userOid).then(response => {
        this.formOptions = JSON.parse(JSON.stringify(response.data.treeSelectList).replace(/AUTHORIZE-/g,""));
        this.authorizeKey = response.data.authorizeKey;
        this.isAdminUser = response.data.isAdminUser;
        this.dataFlag = true;
        this.defaultFrist(this.formOptions);
      }).then(res => {
          let id = this.formOptions[0].id;
          this.$nextTick(function () {
            this.$refs.tree.setCurrentKey(id);
          })
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      //业务系统o
     if('FORM'==data.identity) {
          this.dataFlag = true;
          this.authorizeKey = null;
     }else{
        this.authorizeKey = data.id;
        this.dataFlag = false;
      }
    },
    appendAuthorize(authorizeKey,systemName){
      //新增一个节点
      const node = this.$refs.tree.getCurrentNode();
      if (node) {
        this.append(node, authorizeKey, systemName);
        this.authorizeKey = authorizeKey;
      }
    },
    updateAuthorize(authorizeKey,systemName){
      //更新一个节点
      const node = this.$refs.tree.getCurrentNode();
      if (node) {
        this.update(node, authorizeKey, systemName);
      }
    },
    removeAuthorize(authorizeKey){
      //移除节点
      const node = this.$refs.tree.getCurrentNode();
      if (node) {
        this.remove(node, authorizeKey);
      }
    },
    //新增一个节点
    append(node, newId, label) {
      const newChild = {id: newId, label: label, children: []};
      if (!node.children) {
        this.$set(node, 'children', []);
      }
      node.children.push(newChild);
    },
    //更新一个节点
    update(node, oid, name) {
      const children = node.children;
      const updateNode = children.find(d => d.id == oid);
      updateNode.label = name;
      this.$refs.tree.updateKeyChildren(updateNode.id, updateNode);
    },
    //移除节点
    remove(node, id) {
      const children = node.children || node;
      if(undefined!=id){
        const index = children.findIndex(d => d.id == id);
        children.splice(index, 1);
      }
    },
    //默认展开第一级
    defaultFrist(treeData) {
      var arr = [];
      for (let i = 0; i < treeData.length; i++) {
        if ('undefined' != typeof (treeData[i].children) && treeData[i].children.length > 0) {
          arr.push(treeData[i].id)
        }
        this.ArrData = arr;
      }
    },
    getUrlKey: function (name) {
      return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
    },
  }
}
</script>
<style>
  .el-tree>.el-tree-node{
    min-width:100%;
    display: inline-block;
  }
</style>

