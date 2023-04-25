/** * @Author: wangns */
<!-- 请求条件 -->
<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="80px"
    >
      <el-form-item label="标题" prop="title">
        <el-input
          v-model.trim="queryParams.title"
          placeholder="请输入标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      
      <el-form-item label="启禁用" prop="ableStatus">
                  <el-col :span="24">
                    <el-select
                      v-model.trim="queryParams.ableStatus"
                      placeholder="请选择启禁用类型"
                      size="small" >
                      <el-option label="启用" value="1"></el-option>
                      <el-option label="禁用" value="2"></el-option>
                    </el-select>
                  </el-col>
        </el-form-item>
        <el-form-item label="状态" prop="deleteStatus">
                  <el-col :span="24">
                    <el-select
                      v-model.trim="queryParams.deleteStatus"
                      placeholder="请选择状态类型"
                      size="small" >
                      <el-option label="未删除" value="1"></el-option>
                      <el-option label="已删除" value="2"></el-option>
                    </el-select>
                  </el-col>
        </el-form-item>
      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          class="btn-reset"
          >重置</el-button>
      </el-form-item>
    </el-form>
<!-- 工具条 -->
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['im:banner:save']"
        >新增</el-button
        >
      </el-col>
      <el-button type="danger" size="mini" @click="removeRows()">批量删除</el-button>
    </el-row>
     <!-- 表头 -->
    <el-table
      v-loading="loading"
      :data="districtList"
      stripe
      style="width: 100%" @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"/>
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="标题"
        align="center"
        prop="title"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="图片"
        align="center"
        prop="images"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="内容"
        align="center"
        prop="content"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="跳转地址"
        align="center"
        prop="url"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        width="340"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['im:banner:view']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['im:banner:update']"
          >修改</el-button>
          <el-button 
             size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu" 
             @click="removeBannerById(scope.row.id)"
             v-hasPermi="['im:banner:delete']">删除 
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNumber"
      :limit.sync="queryParams.pageSize"
      style="padding: 30px 0; text-align: center;"
      layout="total, sizes, prev, pager, next, jumper"
      @pagination="getList"
    />

    <!-- 信息详细 -->
     <!-- 添加或修改应用信息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      width="900px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <el-input v-show="false" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>标题：</b></td>
            <td>
              <el-form-item prop="title">
                <el-input
                  v-model.trim="form.title"
                  placeholder="请输入标题"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>

            <td rowspan="3"><i class="require">*</i><b>图片：</b></td>
            <td rowspan="3">
              <el-form-item prop="images">
                <el-upload
                  class="image-upload-pic"
                  :action="uploadImagerUrl"
                  :show-file-list="false"
                  accept=".jpg,.png,.jpeg,.bmp"
                  :on-success="handleAvatarSuccess"
                  :before-upload="beforeAvatarUpload">
                  <img v-if="imageUrl" :src="imageUrl" class="avatar">
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>内容：</b></td>
            <td>
              <el-form-item prop="content">
                <el-input
                  v-model.trim="form.content"
                  placeholder="请输入内容"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>跳转地址：</b></td>
            <td>
              <el-form-item prop="url">
                <el-input
                  v-model.trim="form.url"
                  placeholder="请输入跳转地址"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <!-- <td><i class="require">*</i><b>启禁用状态：</b></td>
            <td>
              <el-form-item  prop="ableStatus">
                  <el-col :span="24">
                    <el-select
                      v-model.trim="form.ableStatus"
                      placeholder="请选择启禁用状态"
                      size="small" >
                      <el-option label="启用" value="1"></el-option>
                      <el-option label="禁用" value="2"></el-option>
                    </el-select>
                  </el-col>
              </el-form-item>
            </td> -->
          </tr>
        </table>
      </el-form>
      <div slot="footer" align="center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 查看banner信息 -->
    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="900px"
      append-to-body
      height="800px"
      scrollbar
      >
      <div class="zf-zc-table--title">banner信息</div>
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="zf-zc-table"
      >
          <colgroup>
            <col width="20%" />
            <col width="20%" />
            <col width="20%" />
            <col width="20%" />
            <col width="20%" />
          </colgroup>
          <tr>
            <td><b>标题：</b></td>
            <td>
              {{ middleBanner.title }}
            </td>
            <td><b>图片：</b></td>
            <td>
              <img v-if="imageUrl" :src="imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </td>
            <!-- <td>
              {{ middleBanner.images}}
            </td> -->
          </tr>
          <tr>
            <td><b>内容：</b></td>
            <td>
              {{ middleBanner.content }}
            </td>
            <td><b>跳转地址：</b></td>
            <td>
              {{ middleBanner.url}}
            </td>
          </tr>
          <tr>
            <td><b>启禁用状态：</b></td>
            <td>
              {{ middleBanner.ableStatus }}
            </td>
          </tr>
        </table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { page,saveOrUpdateBanner,initBanner,deleteBannerById,batchRemoveBanner } from "@/api/ha/bannerManagement/bannerManagement.js";
import viewSxServiceInfo from "@/views/zc/businessSupervise/viewSxServiceInfo";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "ImplementationListQuery",
  components: { Treeselect, viewSxServiceInfo },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      districtList: [],
      multipleSelection: [],  //批量选择中选择的记录列表
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      // 查看显示弹出层
      openView: false,
      openInit: false,
      // 查看显示弹出层
      // openView: false,
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        title: "",
        content: "",
        ableStatus: "",
        deleteStatus: "",
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
          { required: true, message: "必填项", trigger: "blur" }
        ],
        account: [
          { required: true, message: '必填项', trigger: 'blur' },
          // { validator:validatePhone, trigger: 'blur' }
        ],
        password: [
          {required:true,message:'必填项',trigger:"blur"}
        ]

      },
      userOidArr: [],
      props: {
        label: "label", //这里是树结构中需显示的数据（即接口返回的需展示在页面上的参数）
        children: "children",
        isLeaf: "leaf"
      },

      middleBanner:{},

      imageUrl: '' ,//图片 
      uploadImagerUrl: 'http://127.0.0.1:9101/work/banner/uploadImage', 
      //  uploadImagerUrl: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/user/uploadImage', 
    };
  },
  created() {
    this.getList();
  },
  methods: {
     uploadUrl(){
        return uploadCompressImage();
      },
      //成功后返回
      handleAvatarSuccess(response) {
        this.imageUrl = response.data.url; // 请求成功之后赋给头像的URL
        this.form.images =response.data.url;
      },
      //上传之前
      beforeAvatarUpload(file) {
        if(file.name.indexOf("%00") > -1 || file.name.indexOf("./") > -1 || file.name.indexOf(".\\") > -1 ) {
          this.$message.error('上传文件名称非法！');
          return false;
        }
        const isJPG = file.type === 'image/jpeg'||'image/png';
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isJPG) {
          this.$message.error('请上传gif、jpg、jpeg、png或bmp格式的文件！');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      },


 //批量删除按钮
      removeRows(){
          this.$confirm('此操作将永久删除用户, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => { //确定执行then方法
            var idList =[]
            //遍历数组得到每个id值，设置到idlist中
            for(var i=0;i<this.multipleSelection.length;i++){
                var obj = this.multipleSelection[i]
                var id = obj.id
                idList.push(id)
            }
            //调用接口方法
            batchRemoveBanner(idList)
            .then(response => {
                //提示信息
                  this.$message({
                  type: 'success',
                  message: '批量删除成功!'
                })
                //刷新页面
                this.getList()
            })
          })
      },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdateBanner(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.openInit = false;
              this.getList();
            }
          });
        } else {
          return false;
        }
      });
    },

     /** 查看按钮操作 */
    handleView (row) {
      debugger
      this.reset();
      const id = row.id;
      initBanner(id).then(response => {
        console.log(response.data)
        this.middleBanner = response.data;
        this.imageUrl = response.data.images;
        this.openView = true;
        this.title = "查看应用信息";
      });
    },
    //删除
    removeBannerById(id){
        this.$confirm('此操作将该用户数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {//确定执行then方法
          //调用接口方法
           deleteBannerById(id)
           .then(response => {
               //提示信息
                this.$message({
                type: 'success',
                message: '删除成功!'
              })
               //刷新页面
               this.getList()
           })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });          
        });
    },
    /** 初始化新增或修改 */
    handleInit (row) {
      const id = row.id;
      if (id === undefined) {
        this.imageUrl ='';
        this.form = {};
        this.openInit = true;
        this.title = "新增";
      } else {
        initBanner(id).then(response => {
          this.form = response.data;
          this.imageUrl = response.data.images;
          this.openInit = true;
          this.title = "修改";
        });
      }
    },
    
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
      // this.reset();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form);
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
  
    viewServiceClose() {
      this.serviceDialogOptions.pop();
    },
    //获取选择复选框的id值
        handleSelectionChange(selection){
        this.multipleSelection = selection
    }
  },
};
</script>
<style lang="scss" scoped>
//头像样式start
  .avatar-uploader .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
      border-color: #409EFF;
    }
    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 178px;
      height: 178px;
      line-height: 178px;
      text-align: center;
    }
    .avatar {
      width: 178px;
      height: 178px;
      display: block;
    }
// end 
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
