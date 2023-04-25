<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--区划树数据-->
      <el-col :span="4" :xs="24" class="app-left">
        <div class="tree-title">区划管理</div>
        <div class="head-container">
          <el-input
            v-model="districtName"
            placeholder="请输入区划名称"
            clearable
            size="small"
            prefix-icon="iconfont zfsoft-chakan"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="districtOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :highlight-current ="true"
            :filter-node-method="filterNode"
            ref="tree"
            node-key="id"
            :default-expanded-keys="ArrData"
            @node-click="handleNodeClick"
          >
            <span class="custom-tree-node" slot-scope="{ node, data }">
               <el-tooltip v-if="node.label.length > 8" class="item" effect="dark" :content="node.label" placement="right-start">
                  <span style="font-size: 14px">{{ node.label | ellipsis(8) }} </span>
               </el-tooltip>
               <span v-if="node.label.length < 9" style="font-size: 14px">{{ node.label}} </span>
            </span>
          </el-tree>

        </div>
      </el-col>
      <!--区划数据-->
      <el-col :span="20" :xs="24" class="app-right">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
          <el-form-item label="区划代码" prop="code">
            <el-input
              v-model="queryParams.code"
              placeholder="请输入区划代码"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="区划名称" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入区划名称"
              clearable
              size="small"
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
              @click="handleInit"
              v-hasPermi="['sys:district:init']"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['sys:district:listExp']"
            >导出</el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="districtList" border>
          <!--<el-table-column type="selection" width="55" align="center" />-->
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="区划代码" align="center" prop="code" :show-overflow-tooltip="true"/>
          <el-table-column label="区划名称"  align="center" prop="name" :show-overflow-tooltip="true"/>
          <el-table-column label="上级区划名称"  align="center" prop="parentName" :show-overflow-tooltip="true"/>
          <el-table-column label="级别" :show-overflow-tooltip="true" align="center" prop="levelDictName" />
          <!--<el-table-column label="状态" align="center"  width="100" prop="isAble" :formatter="statusFormat"/>-->
          <el-table-column label="启用状态" align="center" prop="isAble" >
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.isAble"
                :active-value="1"
                :inactive-value="0"
                @change="handleAble(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="排序号"  width="70"  align="center" prop="sort" />
          <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                v-hasPermi="['sys:district:view']"
              >查看</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleInit(scope.row)"
                v-hasPermi="['sys:district:update']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleDelete(scope.row)"
                v-hasPermi="['sys:district:delete']"
              >删除</el-button>
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
    <!-- 添加或修改区划信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="70%" append-to-body>
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="0px" >
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.districtOid" />
          <el-input v-show="false" v-model="form.isAble" />
          <el-input v-show="false" v-model="form.parentOid" />
          <el-input v-show="false" v-model="form.isDelete" />
          <el-input v-show="false" v-model="form.createDate" />
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>父级区划名称：</b></td>
              <td>
                <el-form-item><span>{{form.parentName}}</span></el-form-item>
              </td>

              <td><i class="require">*</i><b>区划名称：</b></td>
              <td>
                <el-form-item prop="name">
                  <el-input v-model.trim="form.name" placeholder="请输入区划名称" maxlength="100" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>区划代码：</b></td>
              <td>
                <el-form-item prop="code">
                  <el-input v-model.trim="form.code" placeholder="请输入区划代码" maxlength="25" show-word-limit/>
                </el-form-item>
              </td>

              <td><i class="require">*</i><b>级别：</b></td>
              <td style="width: 100%">
                <el-form-item prop="levelDictOid">
                  <el-select v-model="form.levelDictOid" placeholder="请选择">
                    <el-option
                      v-for="dict in levelDictOptions"
                      :key="dict.dictOid"
                      :label="dict.name"
                      :value="dict.dictOid"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>排序号：</b></td>
              <td>
                <el-form-item prop="sort">
                  <el-input-number v-model="form.sort" :min="1" :max="9999"/>
                </el-form-item>
              </td>
              <td><b>展示图片：</b></td>
              <td>
                <el-form-item>
                  <el-upload
                    class="upload-demo"
                    accept=".jpg,.png,.jpeg,.gif,.bmp"
                    :headers="getToken()"
                    :action="uploadUrl()"
                    :on-error="uploadError"
                    :before-upload="beforeUpload"
                    :file-list="fileList"
                    :on-success="uploadSuccess">
                    <el-button type="primary" size="small">上传图片<i class="el-icon-upload el-icon--right"></i></el-button>
                  </el-upload>
                  <el-input v-show="false" v-model="form.imageAttaOid" />
                  <div v-show="null!=form.imageAttaOid && ''!=form.imageAttaOid">
                    <span>{{form.attaName}}</span>
                    <el-link type="primary" @click="downloadFile(form.imageAttaOid)">下载</el-link> |
                    <el-link type="primary" @click="viewFile(form.imageAttaOid)">预览</el-link> |
                    <el-link type="danger" @click="clearAtta">删除</el-link>
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


    <!-- 查看区划信息 -->
    <el-dialog :title="title" :visible.sync="openView" width="70%" append-to-body>
      <div>
        <el-form :model="form" label-width="0px" >
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table" >
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>父级区划名称：</b></td>
              <td>
                {{ form.parentName }}
              </td>
              <td><b>区划名称：</b></td>
              <td>
                {{ form.name }}
              </td>
            </tr>
            <tr>
              <td><b>区划代码：</b></td>
              <td>
                {{ form.code }}
              </td>
              <td><b>状态：</b></td>
              <td v-for="(value, key) in ableMap" :key="key" v-if="form.isAble == key ">
                {{value}}
              </td>
            </tr>
            <tr>
              <td><b>级别：</b></td>
              <td>
                {{ form.levelDictName }}
              </td>
              <td><b>排序号：</b></td>
              <td>
                {{ form.sort }}
              </td>
            </tr>
            <tr>
              <td><b>展示图片：</b></td>
              <td colspan="3">
                <el-form-item>
                  <div v-show="null!=form.imageAttaOid && ''!=form.imageAttaOid">
                    <span>{{form.attaName}}</span>
                    <el-link type="primary" @click="downloadFile(form.imageAttaOid)">下载</el-link> |
                    <el-link type="primary" @click="viewFile(form.imageAttaOid)">预览</el-link>
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

    <!--引入文件的预览弹出层-->
    <el-dialog v-dialog-drag title="文件预览" :visible.sync="view.show" v-for="view in viewDialogOptions"
               @close="closeFileView" width="60%"  append-to-body>
      <file-view :attaOid="view.attaOid" @father-click="closeFileView"></file-view>
    </el-dialog>
  </div>
</template>

<script>
  import { init, save, del ,getOne, page, able,listExp,queryDistrictSimpleTree } from "@/api/sys/district";
  import { validateNumberNoPonint,validIntNoZero } from '@/utils/validate';
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import fileView from '@/views/common/fileView';
  import { uploadCompressImage } from "@/api/sys/atta";
  import { getToken} from '@/utils/auth'
  export default {
    components: { Treeselect,fileView },
    name: "District",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 应用表格数据
        districtList: [],
        //查询区划名称参数
        districtName:'',
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        //消息
        ableMap:{'1':'启用','0':'禁用'},
        // 区划树选项
        districtOptions: undefined,
        // 级别字典
        levelDictOptions: [],
        viewDialogOptions:[],
        fileList:[],
        ArrData:[],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: '',
          code:'',
          parentOid:''
        },
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          name: [
            { required: true, message: "区划名称不能为空", trigger: "blur" }
          ],
          code: [
            { required: true, message: "区划代码不能为空", trigger: "blur" },
            {min:6,max:25,message: "请输入6-25位长度的数字"},
            {validator:validateNumberNoPonint, trigger: 'blur'}
          ],
          levelDictOid:[
            { required: true, message: "请选择级别", trigger: "blur" }
          ],
          sort: [
            { required: true, message: "排序号不能为空", trigger: "blur" },
            {validator:validIntNoZero, trigger: 'blur'}
          ]
        },
      };
    },
    watch: {
      // 根据名称筛选部门树
      districtName(val) {
        if(null!=val){
          val = val.replace(/^\s+|\s+$/g,"");
        }
        this.$refs.tree.filter(val);
      }
    },
    created() {
      this.getDistrictTree();
      this.getList();
      /** 数据字典中保存区划级别的数据项 */
      this.getDictList("QHJB").then(response => {
        this.levelDictOptions = response.data;
      });
    },
    methods: {
      getToken() {
        return {Authorization: 'Bearer ' + getToken()}
      },
      /** 查询区划列表 */
      getList() {
        this.loading = true;
        let that = this;
        page(this.queryParams).then(response => {
          this.districtList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        }).catch(function() {that.loading = false;});
      },
      /** 获取区划树 */
      getDistrictTree(districtOid) {
        queryDistrictSimpleTree(districtOid).then(response => {
          this.districtOptions = response.data;
          this.defaultFrist(response.data);
        })
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        data.id = null!=data.id?data.id.substring(data.id.lastIndexOf('-')+1,data.id.length):'';
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
      getToken() {
        return {Authorization: 'Bearer ' + getToken()}
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
          this.title = "查看区划信息";
        });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        let oid = row.id;
        if('undefined' == typeof(oid) && ''==this.queryParams.parentOid || null==this.queryParams.parentOid){
          this.msgWarning("请选择左侧区划信息！");
          return false;
        }
        this.reset();
        init(oid,this.queryParams.parentOid).then(response => {
          if(response.data.sysDistrict != undefined){
            this.form = response.data.sysDistrict;
          }else{
            this.form.parentOid = this.queryParams.parentOid;
          }
          if(response.data.parentSysDistrict != undefined){
            this.form.parentName = response.data.parentSysDistrict.name;
          }
          this.openInit = true;
          if (oid==undefined || oid==null){
            this.title = "新增区划信息";
          }else {
            this.title = "修改区划信息";
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
                if(this.form.districtOid == undefined || null == this.form.districtOid){
                  //新增一个节点
                  const node = this.$refs.tree.getCurrentNode();
                  this.append(node,response.data.districtOid,response.data.name);
                }else {
                  //更新一个节点
                  const node = this.$refs.tree.getCurrentNode();
                  if(node){
                    this.update(node,response.data.districtOid,response.data.name);
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
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return del(oid);
        }).then(() => {
          this.getList();
          if(''!=this.queryParams.parentOid){
            const node = this.$refs.tree.getCurrentNode();
            this.remove(node,oid);
          }else {
            this.getDistrictTree();
          }
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 启禁用按钮操作 */
      handleAble(row) {
        const oid = row.id;
        let ableMessage = row.isAble == 1 ? '启用' : '禁用'
        this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return able(oid);
        }).then(() => {
          this.msgSuccess(ableMessage+"成功");
        }).catch(function() {
          row.isAble = row.isAble == 0 ? 1 : 0
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
      //默认展开第一级
      defaultFrist(treeData){
        var arr = [];
        for (let i = 0; i < treeData.length; i++) {
          if ('undefined' != typeof(treeData[i].children) && treeData[i].children.length>0) {
            arr.push(treeData[i].id)
          }
          this.ArrData=arr;
        }
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
      //成功后返回
      async uploadSuccess(resp){
        this.fileList=[];
        if(200 !== resp.code){
          return this.msgError(resp.message);
        }
        this.form.imageAttaOid = resp.data.oid;
        this.form.attaName = resp.data.name;
      },
      //失败后返回
      uploadError(resp){
        this.msgError("文件上传失败");
      },
      uploadUrl(){
        return uploadCompressImage();
      },
      //上传之前
      beforeUpload(file) {
        if(file.name.indexOf("%00") > -1 || file.name.indexOf("./") > -1 || file.name.indexOf(".\\") > -1 ) {
          this.msgError('上传文件名称非法！');
          return false;
        }
        var type = this.fileByType(file.name);
        if(null != type && type !="jpg" && type !="png"&& type !="jpeg"
          && type != "gif" && type !="bmp" ) {
          this.msgError('请上传gif、jpg、jpeg、png或bmp格式的文件！');
          return false;
        }
        const fileSize = file.size;
        if (0 == fileSize) {
          this.msgError('上传文件不能为空！');
          return false;
        }
        const isLt2M = file.size / 1024 / 1024 < 10;
        if (!isLt2M) {
          this.msgError('上传文件大小不能超过 10MB！');
        }
        return isLt2M;
      },
      /**
       * 通过文件后缀返回文件
       */
      fileByType(path) {
        return path.substring(path.lastIndexOf(".") + 1, path.length);
      },
      //清空附件
      clearAtta(){
        this.form.imageAttaOid = '';
        this.form.attaName = '';
        this.$forceUpdate();

      },
    }
  };
</script>
<style scoped>
  .el-tree>.el-tree-node{
    min-width:100%;
    display: inline-block;
  }
</style>

