<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--系统参数树数据-->
      <el-col :span="4" :xs="24" class="app-left">
        <div class="tree-title">系统配置管理</div>
        <div class="head-container">
          <el-input
            v-model="configName"
            placeholder="请输入系统参数名称"
            clearable
            size="small"
            prefix-icon="iconfont zfsoft-chakan"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="configOptions"
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
               <span v-if="node.label.length < 9" style="font-size: 14px">{{ node.label}} </span>
            </span>
          </el-tree>
        </div>
      </el-col>
      <!--系统参数配置项数据-->
      <el-col :span="20" :xs="24" class="app-right">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="110px">
          <el-form-item label="配置项代码" prop="code">
            <el-input
              v-model="queryParams.code"
              placeholder="请输入配置项代码"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="配置项名称" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入配置项名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="ml5">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit"
              v-hasPermi="['sys:config:init']"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['sys:config:listExp']"
            >导出</el-button>
          </el-col>
        </el-row>

        <el-table  v-loading="loading" :data="configList" border>
          <!--<el-table-column type="selection" width="55" align="center" />-->
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="配置项代码" :show-overflow-tooltip="true" align="center" prop="code" />
          <el-table-column label="配置项名称"  align="center" prop="name" :show-overflow-tooltip="true"/>
          <el-table-column label="配置项值" align="center" prop="value"  :show-overflow-tooltip="true" />
          <el-table-column label="配置项描述" :show-overflow-tooltip="true"  align="center" prop="memo" />
          <!--<el-table-column label="启用状态" width="100" align="center" prop="isAble" :formatter="statusFormat"/>-->
          <el-table-column label="启用状态" align="center"  width="100" prop="isAble" >
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.isAble"
                :active-value="1"
                :inactive-value="0"
                @change="handleAble(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                v-hasPermi="['sys:config:view']"
              >查看</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleInit(scope.row)"
                v-hasPermi="['sys:config:update']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleDelete(scope.row)"
                v-hasPermi="['sys:config:delete']"
              >删除</el-button>
              <!--<el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-jinyong"
                @click="handleAble(scope.row)"
                v-hasPermi="['sys:config:able']"
              >启禁用</el-button>-->
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
    <!-- 添加或修改系统参数配置项信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.configOid" />
          <el-input v-show="false" v-model="form.isAble" />
          <el-input v-show="false" v-model="form.parentOid" />
          <el-input v-show="false" v-model="form.isDelete" />
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>上级系统配置代码：</b></td>
              <td>
                <el-form-item >
                  <span v-if="'' == parentSysConfig.code">上级系统配置名称</span>
                  <span v-if="parentSysConfig.code">{{parentSysConfig.code }}</span>
                </el-form-item>
              </td>
              <td><b>上级系统配置名称：</b></td>
              <td>
                <el-form-item >
                  <span v-if="'' == parentSysConfig.name">上级系统配置代码</span>
                  <span v-if="parentSysConfig.name">{{parentSysConfig.name }}</span>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>配置项名称：</b></td>
              <td>
                <el-form-item prop="name">
                  <el-input v-model.trim="form.name" placeholder="请输入配置项名称" maxlength="30" show-word-limit/>
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>配置项代码：</b></td>
              <td>
                <el-form-item prop="code">
                  <el-input v-model.trim="form.code" placeholder="请输入配置项代码" maxlength="50" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>配置项值：</b></td>
              <td  colspan="3">
                <el-form-item prop="value">
                  <el-input v-model.trim="form.value" placeholder="请输入配置项值" maxlength="100" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>配置项描述：</b></td>
              <td colspan="3">
                <el-form-item prop="memo">
                  <el-input v-model.trim="form.memo" type="textarea" placeholder="请输入配置项描述"  maxlength="500" show-word-limit></el-input>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>配置项附件：</b></td>
              <td colspan="3">
                <el-form-item >
                  <el-input v-show="false" v-model="form.attaOid" />
                  <el-input v-show="false" v-model="form.attaName" />
                  <el-button type="success" size="mini" @click="selectAttas">配置项附件</el-button>
                  <el-button type="danger" size="mini" @click="clearAtta">清理</el-button>
                  <div v-show="null != form.attaOid && '' != form.attaOid">
                    <span>{{form.attaName}}</span>
                    <el-link type="primary" @click="downloadFile(form.attaOid)">下载</el-link> |
                    <el-link type="primary" @click="viewFile(form.attaOid)">预览</el-link>
                  </div>
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


    <!-- 查看配置项信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <el-form :model="form" label-width="0px" size="mini">
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>上级系统配置名称：</b></td>
              <td>
                {{ form.parentName }}
              </td>
              <td><b>配置项名称：</b></td>
              <td>
                {{ form.name }}
              </td>
            </tr>
            <tr>
              <td><b>配置项代码：</b></td>
              <td>
                {{ form.code }}
              </td>
              <td><b>配置项值：</b></td>
              <td>
                {{ form.value }}
              </td>
            </tr>
            <tr>
              <td><b>配置项描述：</b></td>
              <td colspan="3">
                {{ form.memo }}
              </td>
            </tr>
            <tr>
              <td><b>附件下载：</b></td>
              <td colspan="3">
                <el-form-item v-show="null!=form.attaOid && ''!=form.attaOid">
                  <div>
                    <span>{{form.attaName}}</span>
                    <el-link type="primary" @click="downloadFile(form.attaOid)">下载</el-link> |
                    <el-link type="primary" @click="viewFile(form.attaOid)">预览</el-link>
                  </div>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

   <!-- 选择配置项附件view-->
    <el-dialog title="选择配置项附件" :visible.sync="openAttaListView" width="1000px" append-to-body>
      <div class="app-container">
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">

            <el-upload
              class="upload-demo"
              :action="uploadUrl()"
              :headers="getToken()"
              :on-error="uploadError"
              :file-list="fileList"
              :before-upload="beforeUpload"
              :on-success="uploadSuccess">
              <!--<el-button size="small" type="primary">点击上传<i class="iconfont zfsoft-wenjianshangchuan"></i></el-button>-->
              <el-button type="primary" size="small">点击上传<i class="el-icon-upload el-icon--right"></i></el-button>
            </el-upload>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="attaList">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="原始文件名" align="center" prop="originName" />
          <el-table-column label="文件名" align="center" prop="name" />
          <el-table-column label="上传时间" align="center" prop="uploadDate" width="180"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-link type="primary" @click="selectFile(scope.row.attaOid,scope.row.originName)">选择</el-link> |
              <el-link type="primary" @click="downloadFile(scope.row.attaOid)">下载</el-link> |
              <el-link type="primary" @click="viewFile(scope.row.attaOid)">预览</el-link>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="attatotal>0"
          :total="attatotal"
          :page.sync="attaQueryParams.pageNum"
          :limit.sync="attaQueryParams.pageSize"
          @pagination="getAttaList"
        />

      </div>
    </el-dialog>

    <!--引入文件的预览弹出层-->
    <el-dialog v-dialog-drag title="文件预览" :visible.sync="view.show" v-for="view in viewDialogOptions"
               @close="closeFileView" width="60%"  append-to-body>
      <file-view :attaOid="view.attaOid" @father-click="closeFileView"></file-view>
    </el-dialog>
  </div>
</template>

<script>
import { init, save, del ,getOne, page, able,listExp,queryConfigTree } from "@/api/sys/config";
import { pageFile, uploadFile } from "@/api/sys/atta";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css"
import fileView from '@/views/common/fileView';
import { getToken} from '@/utils/auth'
export default {
  components: { Treeselect,fileView },
  name: "Config",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      attatotal:0,
      viewDialogOptions:[],
      // 参数配置表格数据
      configList: [],
      //查询配置项名称参数
      configName:'',
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      ArrData:[],
      //选择附件项附件
      openAttaListView:false,
      fileList:[],
      //消息
      ableMap:{'1':'启用','0':'禁用'},
      // 参数配置配置项树选项
      configOptions: undefined,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        code:'',
        parentOid:''
      },
      //附件参数
      attaQueryParams: {
        pageNum: 1,
        pageSize: 10
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {},
      //父类表单
      parentFrom:{},
      //上级参数
      parentSysConfig:{},
      //附件列表
      attaList:[],
      // 表单校验
      rules: {
        name: [
          { required: true, message: "配置项名称不能为空", trigger: "blur" }
        ],
        code: [
          { required: true, message: "配置项代码不能为空", trigger: "blur" }
        ],
        value: [
          { required: true, message: "配置项值不能为空", trigger: "blur" }
        ]
      },
    };
  },
  watch: {
    // 根据名称筛选部门树
    configName(val) {
      if(null!=val){
        val = val.replace(/^\s+|\s+$/g,"");
      }
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getConfigTree();
  },
  methods: {
    /** 查询系统参数配置项列表 */
    getList() {
      this.loading = true;
      let that = this;
      page(this.queryParams).then(response => {
        this.configList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      }).catch(function() {that.loading = false;});
    },
    /** 获取系统参数树 */
    getConfigTree(parentOid) {
      queryConfigTree(parentOid).then(response => {
        this.configOptions = response.data;
        this.defaultFrist(response.data);
      }).then(res => {
        let id = ''
        if (!this.queryParams.parentOid) {
          id = this.configOptions[0].id;
          this.$nextTick(function() {
            this.$refs.tree.setCurrentKey(id);
            this.queryParams.parentOid = id;
            this.getList();
          })
        }
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.parentOid = data.id;
      this.getList();
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
      const updateNode = children.find(d => d.id == oid);
      updateNode.label = name;
      this.$refs.tree.updateKeyChildren(updateNode.id,updateNode);
    },
    //移除节点
    remove(node, id) {
      const children = node.children || node;
      const index = children.findIndex(d => d.id == id);
      children.splice(index, 1);
    },
    // 消息
    statusFormat(row) {
      return this.selectMapLabel(this.ableMap, row.isAble);
    },
    // 取消按钮
    cancel() {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        name: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.reset();
      const oid = row.id;
      getOne(oid).then(response => {
        this.form = response.data;
        this.openView = true;
        this.title = "查看配置项信息";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      this.reset();
      let oid = row.id;
      init(oid,this.queryParams.parentOid).then(response => {
        if(response.data.sysConfig != undefined){
          this.form = response.data.sysConfig;
        }else{
          this.form.parentOid = this.queryParams.parentOid;
        }
        if(response.data.parentSysConfig != undefined){
          this.parentSysConfig = response.data.parentSysConfig;
        }
        this.openInit = true;
        if (oid==undefined || oid==null){
          this.title = "新增系统参数信息";
        }else {
          this.title = "修改系统参数信息";
        }
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          save(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.openInit = false;
              if(this.form.configOid == undefined || null == this.form.configOid){
                //新增一个节点
                const node = this.$refs.tree.getCurrentNode();
                this.append(node,response.data.configOid,response.data.name);
              }else {
                //更新一个节点
                const node = this.$refs.tree.getCurrentNode();
                if(node){
                  this.update(node,response.data.configOid,response.data.name);
                }
              }
              this.getList();
            }
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const oid = row.id;
      const configOid = row.configOid;
      this.$confirm('是否确认删除吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return del(oid);
        }).then(() => {
          const node = this.$refs.tree.getCurrentNode();
          this.remove(node,configOid);
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 启禁用按钮操作 */
    handleAble(row) {
      const oid = row.id;
      let ableMessage = row.isAble === 1 ? '启用' : '禁用'
      this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return able(oid);
      }).then(() => {
        this.msgSuccess(ableMessage+"成功");
      }).catch(function(e) {
        row.isAble = row.isAble === 0 ? 1 : 0
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出列表数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          listExp(queryParams);
        }).catch(function() {});
    },
    //选择配置项附件
    selectAttas(){
      this.getAttaList();
      this.openAttaListView = true ;
    },
    /** 查询附件列表 */
    getAttaList() {
      pageFile(this.attaQueryParams).then(response => {
        this.attaList = response.data.data;
        this.attatotal = response.data.total;
      });
    },
    //选中附件
    selectFile(attaOid,attaName){
      this.form.attaOid = attaOid;
      this.form.attaName = attaName;
      this.openAttaListView = false ;
    },
    //下载附件
    downloadFile(attaOid){
      this.download(attaOid);
    },
    //预览附件
    viewFile(attaOid){
      let item = {show:true,attaOid:attaOid};
      this.viewDialogOptions.push(item);
    },
    //关闭预览附件
    closeFileView(){
      this.viewDialogOptions.pop();
    },
    //清空附件
    clearAtta(){
      this.form.attaOid = '';
      this.form.attaName = '';
    },
    //成功后返回
    uploadSuccess(resp){
      this.fileList=[];
      if(200 !== resp.code){
        return this.msgError(resp.message);
      }
      this.getAttaList();
    },
    //失败后返回
    uploadError(resp){
      this.msgError("文件上传失败");
    },
    uploadUrl(){
      return uploadFile();
    },
    getToken() {
      return {Authorization: 'Bearer ' + getToken()}
    },
    //上传之前
    beforeUpload(file) {
      if(file.name.indexOf("%00") > -1 || file.name.indexOf("./") > -1 || file.name.indexOf(".\\") > -1 ) {
        this.msgError('上传文件名称非法！');
        return false;
      }
      const fileSize = file.size;
      if (0 == fileSize) {
        this.msgError('上传文件不能为空！');
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 100;
      if (!isLt2M) {
        this.msgError('上传文件大小不能超过 100MB！');
      }
      return isLt2M;
    },
    //默认展开第一级
    defaultFrist(treeData){
      var arr = [];
      for (let i = 0; i < treeData.length; i++) {
        if ('undefined' != typeof(treeData[i].children) && treeData[i].children.length>0) {
          arr.push(treeData[i].id)
        }
        this.ArrData=arr;
      }
    }
  }
};
</script>

