/**
* @Author: liyanqing
* @Description: 服务优惠政策梳理
*/
<template>
  <div>
    <!-- <el-row :gutter="20">
      <el-col :span="24" :xs="24" class="app-right"> -->
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px" @submit.native.prevent>
          <el-form-item label="优惠政策标题" prop="discountTitle">
            <el-input
              v-model.trim="queryParams.discountTitle"
              placeholder="请输入优惠政策标题"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit"
              v-hasPermi="['sys:serviceDiscount:init']"
            >新增</el-button>
          </el-col>
        </el-row>
        <el-table  v-loading="loading" :data="dictList" border>
          <el-table-column label="序号" width="50" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="优惠政策标题" width="380"  align="center" prop="discountTitle" :show-overflow-tooltip="true"/>
          <el-table-column label="创建时间"  width="180"  align="center" prop="createDate"  :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                v-hasPermi="['sys:serviceDiscount:view']"
              >查看</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleInit(scope.row)"
                v-hasPermi="['sys:serviceDiscount:init']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleDelete(scope.row)"
                v-hasPermi="['sys:serviceDiscount:delete']"
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
      <!-- </el-col>
    </el-row> -->
    <!-- 添加或修改数字平板信息信息对话框 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="1100px" scrollbar append-to-body  class="el-dialog-type">
      <div>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class='zf-zc-table'>
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i>
              <b>优惠政策标题：</b>
            </td>
            <td colspan="3">
              <el-row>
                <el-col :span="24">
                  <el-form-item  prop="discountTitle">
                    <el-input v-model.trim="form.discountTitle" placeholder="请输入优惠政策标题" maxlength="100" show-word-limit/>
                  </el-form-item>
                </el-col>
              </el-row>
            </td>
          </tr>
          <tr>
            <td>
              <b>优惠政策来源：</b>
            </td>
            <td colspan="3">
              <el-row>
                <el-col :span="24">
                  <el-form-item  prop="discountSource">
                    <el-input v-model.trim="form.discountSource" placeholder="请输入优惠政策来源" maxlength="100" show-word-limit/>
                  </el-form-item>
                </el-col>
              </el-row>
            </td>
          </tr>
          <tr>
            <td>
              <b>优惠政策条件：</b>
            </td>
            <td colspan="3">
              <el-row>
                <el-col :span="24">
                  <el-form-item  prop="discountCondition">
                    <el-input v-model.trim="form.discountCondition" placeholder="请输入优惠政策条件" maxlength="200" show-word-limit/>
                  </el-form-item>
                </el-col>
              </el-row>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i>
              <b>内容：</b>
            </td>
            <td colspan="3">
              <el-col :span="24">
                <el-form-item  prop="discountContent">
                  <el-input v-model.trim="form.discountContent" type="textarea" maxlength="1000" show-word-limit placeholder="请输入内容"></el-input>
                </el-form-item>
              </el-col>
            </td>
          </tr>
          <tr>
            <td>
              <b>图片信息：</b>
            </td>
            <td colspan="4">
              <el-col :span="24">
                <el-form-item label="" >
                      <el-upload
                        class="upload-demo"
                        :action="uploadUrl()"
                        :before-upload="beforeUpload"
                        :on-error="uploadError"
                        :file-list="fileList"
                        :show-file-list="false"
                        :on-success="uploadSuccess">
                        <el-button size="small" type="primary">点击上传<i class="iconfont zfsoft-wenjianshangchuan"></i></el-button>
                      </el-upload>
                      <div v-show="null!=form.attaOid && ''!=form.attaOid">
                        <span>{{attaTitle}}</span>
                        <el-link type="primary" @click="downloadFile(form.attaOid)">下载</el-link> |
                        <el-link type="primary" @click="viewFileNew(form.attaOid)">预览</el-link>
                      </div>
                    </el-form-item>
              </el-col>
            </td>
          </tr>
        </table>
      </el-form>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 信息详细 -->
    <el-dialog v-dialog-drag :title="title" :visible.sync="openView" width="900px" append-to-body>
      <div>

          <table cellspacing="0" cellpadding="0" border="0" class='zf-zc-table'>
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td>
                <b>优惠政策标题：</b>
              </td>
              <td colspan="3">
                <el-col :span="24">
                  {{ form.discountTitle }}
                </el-col>
              </td>
            </tr>
            <tr>
              <td>
                <b>优惠政策来源：</b>
              </td>
              <td colspan="3">
                <el-col :span="24">
                  {{ form.discountSource }}
                </el-col>
              </td>
            </tr>
            <tr>
              <td>
                <b>优惠政策条件：</b>
              </td>
              <td colspan="3">
                <el-col :span="24">
                  {{ form.discountCondition }}
                </el-col>
              </td>
            </tr>
            <tr>
              <td>
                <b>内容：</b>
              </td>
              <td colspan="3">
                <el-col :span="24">
                  {{ form.discountContent }}
                </el-col>
              </td>
            </tr>
            <tr>
              <td>
                <b>图片信息：</b>
              </td>
              <td colspan="3">
                <span>{{attaTitle}}</span>
                <template v-if="attaTitle">
                <el-link type="primary" @click="downloadFile(form.attaOid)">下载</el-link> |
                <el-link type="primary" @click="viewFileNew(form.attaOid)">预览</el-link>
                </template>
                <template v-else>暂无</template>
              </td>
            </tr>
          </table>

      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!--引入文件的预览弹出层-->
    <el-dialog v-dialog-drag title="文件预览" :visible.sync="view.show" v-for="view in viewDialogOptions" :key='view.attaOid'
               @close="closeFileView" width="900px"  append-to-body>
      <file-view :attaOid="view.attaOid" @father-click="closeFileView"></file-view>
    </el-dialog>
  </div>
</template>

<script>
import { save, del ,getOne, page ,getOneFile} from "@/api/onething/sxpz/comboDirectoryDiscount";
import {uploadFile } from "@/api/sys/atta";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import fileView from '@/views/common/fileView';
export default {
  components: { Treeselect ,fileView},
  name: "comboDirectoryDiscount",
  //定义获取父类传过来值的格式
  props:["comboDireOid"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      dictList: [],
      fileList:[],
      // 弹出层标题
      title: "",
      attaTitle:"",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      viewDialogOptions:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        discountTitle: '',
        comboDirectoryOid:''
      },
      // 表单参数
      form: {
        discountTitle: '',
        discountSource:'',
        discountCondition:'',
        discountContent:'',
        comboDirectoryOid:'',
        attaOid:''
      },
      // 表单校验
      rules: {
        discountTitle: [
          { required: true, message: "优惠政策标题不能为空", trigger: "blur" }
        ],
        discountContent: [
          { required: true, message: "优惠政策内容不能为空", trigger: "blur" }
        ]
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询数字平板信息列表 */
    getList() {
      this.dictList=[]
      this.loading = true;
      this.queryParams.comboDirectoryOid = this.comboDireOid;
      page(this.queryParams).then(response => {
        this.dictList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
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
      this.attaTitle='';
    },
    //成功后返回
    uploadSuccess(resp){
      this.form.attaOid = resp.data.oid;
      this.form.attaName = resp.data.name;
      this.attaTitle=resp.data.name;
      //this.fileList=resp;
    },
    //失败后返回
    uploadError(resp){
      this.msgError("文件上传失败");
    },
    uploadUrl(){
      return uploadFile();
    },
    /** 上传附件请求操作 */
    beforeUpload(file) {
      let fileName = file.name;
      let fileType = file.type;
      let isRightSize = file.size / 1024 / 1024 < 10
      if (fileName.indexOf("%00") > 0) {
        this.$message.error('图片名称非法');
        return false;
      }
      if (fileType != "image/png" && fileType != "image/jpeg") {
        this.$message.error('请上传图片');
        return false;
      }
      if (!isRightSize) {
        this.$message.error('文件大小超过 10MB');
        return false;
      }
      if (file.size == 0) {
        this.$message.error('不允许上传空文件');
        return false;
      }
      this.fileList.push(file);
      return isRightSize
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
      const id = row.id;
      getOne(id).then(response => {
        this.form = response.data.discount;
        this.getFilename();
        this.openView = true;
        this.title = "查看优惠政策信息";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      this.reset();
      let _that = this;
      if(row.id) {
        getOne(row.id).then(response => {
          _that.openInit = true;
          _that.form = response.data.discount;
          this.getFilename();
        });
      } else {
        _that.openInit = true;
      }
      _that.title = row.id ? "修改优惠政策信息" : "新增优惠政策信息";
    },
    getFilename(){
      if(this.form.attaOid) {
        getOneFile(this.form.attaOid).then(response1 => {
          this.attaTitle = response1.data.originName;
        });
      }
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      _that.form.comboDirectoryOid = this.comboDireOid;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          save(_that.form).then(response => {
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
      const id = row.id;
      this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return del(id);
        }).then(response => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },

  }
};
</script>
<style lang="scss" scoped>
  .el-tree>.el-tree-node{
    min-width:100%;
    display: inline-block;
  }
</style>

