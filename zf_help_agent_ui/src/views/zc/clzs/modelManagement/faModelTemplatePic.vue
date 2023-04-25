/**
* @Author: liangss
* @Date: 2020-11-07 18:25:48
* @Description: 模板底图
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--咨询数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px">
          <el-form-item label="原始文件名" prop="originName">
            <el-input v-model.trim="queryParams.originName" placeholder="请输入原始文件名" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>


        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit()"
            >选择本地底图文件</el-button>
          </el-col>

          <el-col :span="1.5">

              <!--   <el-input v-show="false" v-model="form.attaOid" />
                 <el-input v-show="false" v-model="form.attaName" />-->
              <el-upload
                class="upload-demo"
                :action="uploadUrl()"
                :on-error="uploadError"
                :file-list="fileList"
                :on-success="uploadSuccess">
                <el-button size="small" type="primary">选择本地底图文件<i class="iconfont zfsoft-wenjianshangchuan"></i></el-button>
              </el-upload>
              <!--<el-button type="success" size="mini" @click="selectAttas">配置项附件</el-button>-->
              <!-- <el-button type="danger" size="mini" @click="clearAtta">清理</el-button>-->
         <!--     <div v-show="null!=form.attaOid && ''!=form.attaOid">
                <span>{{form.attaName}}</span>
                <el-link type="primary" @click="downloadFile(form.attaOid)">下载</el-link> |
                <el-link type="primary" @click="viewFile(form.attaOid)">预览</el-link>
              </div>-->

          </el-col>



        </el-row>

        <el-table v-loading="loading" :data="advisoryRegistrationList"  border>
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>

          <el-table-column label="原始文件名" width="300" align="center" prop="originName"/>
          <el-table-column label="文件名" width="300" align="center" prop="name"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"  >查看</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="viewFile(scope.row.attaOid)" >预览</el-button>
             <!-- <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)" v-hasPermi="['sys:district:delete']">删除</el-button>-->
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>

    <!--引入文件的预览弹出层-->
    <el-dialog v-dialog-drag title="文件预览" :visible.sync="view.show" v-for="view in viewDialogOptions"
               @close="closeFileView" width="60%"  append-to-body>
      <file-view :attaOid="view.attaOid" @father-click="closeFileView"></file-view>
    </el-dialog>

  </div>
</template>

<script>
import { page,getOne,saveOrUpdate,del} from "@/api/zc/clzs/modelManagement/faModelTemplatePic.js";
import { pageFile, uploadFile } from "@/api/sys/atta";
import Treeselect from '@riophae/vue-treeselect';
import fileView from '@/views/common/fileView';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
export default {
  name: "advisoryRegistration",
  components: {Treeselect,fileView},
  data() {
    return {
      selectArr:[],
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      advisoryRegistrationList: [],
      //查询区划名称参数
      districtName: "",
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      fileList:[],
      attaList:[],
      viewDialogOptions:[],

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        advisoryCode: "",
        name:"",
        advisoryStartDate:null,
        advisoryEndDate:null
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {
        id: '', //逻辑主键
        createDate: '', // 登记时间
        sort: 0,  // 排序号
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        telePhone: [
          { required: true, message: "电话不能为空", trigger: "blur" }
        ],
        cardNumber: [
          { required: true, message: "身份证号不能为空", trigger: "blur" }
        ],
        advisoryContent: [
          { required: true, message: "咨询内容不能为空", trigger: "blur" }
        ],
        result: [
          { required: true, message: "处理结果不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  /*  this.getDistrictTree();*/
  },
  methods: {
    /** 查询咨询列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.advisoryRegistrationList = response.data.data;
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
      Object.assign(this.form, this.$options.data().form)
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
      let _that = this;
      _that.reset();
      const oid = row.id;
      getOne(oid).then(response => {
        _that.form = response.data;
        _that.openView = true;
        _that.title = "查看咨询信息";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      _that.reset();
      if(row.id) {
        getOne(row.id).then(response => {
          _that.openInit = true;
          _that.form = response.data;
         /* alert(JSON.stringify(response.data));*/
        });
      } else {
        _that.openInit = true;
      }
      _that.title = row.id ? "修改咨询信息" : "新增咨询信息";
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdate(_that.form).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("保存成功");
              _that.openInit = false;
              setTimeout(() => {
                _that.getList();
              }, 10);

            }
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const oid = row.id;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return del(oid);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    },
    //选择配置项附件
    selectAttas(){
      this.getAttaList();
      this.openAttaListView = true ;
    },
    //下载附件
    downloadFile(attaOid){
      this.download(attaOid);
    },
    //预览附件
    viewFileNew(attaOid){
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
    getAttaList() {
      pageFile(this.attaQueryParams).then(response => {
        this.attaList = response.data.data;
        this.attatotal = response.data.total;
      });
    },

    //成功后返回
    uploadSuccess(resp){
      this.fileList=[];
      this.form.attaOid = resp.data.oid;
      this.form.originName = resp.data.name;
      this.form.name = resp.data.name;
      this.form.filePath= resp.data.url;

      saveOrUpdate(this.form).then(response => {
        if (response.code === 200) {
          this.msgSuccess("保存成功");
          setTimeout(() => {
            this.getList();
          }, 10);

        }
      });
    },
    //失败后返回
    uploadError(resp){
      this.msgError("文件上传失败");
    },
    uploadUrl(){
      return uploadFile();
    }

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
  text-align: left;
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
