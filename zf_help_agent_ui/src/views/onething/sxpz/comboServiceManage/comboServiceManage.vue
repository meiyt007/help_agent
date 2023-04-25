/**
* @Author: liangxm
* @Date: 2020-1-21 18:21:27
* @Last Modified by: liangxm
* @Last Modified time: 2020-1-21 10:54:27
* @Description: 服务管理
*/
<template>
  <div>
    <!-- <el-row :gutter="20">
      <el-col :span="24" :xs="24" class="app-right"> -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px" @submit.native.prevent>
      <el-form-item label="百科词条名称" prop="caryopterisName">
        <el-input v-model.trim="queryParams.caryopterisName" placeholder="请输入百科词条名称" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit"
          v-hasPermi="['sys:pbpjInformation:update']">新增</el-button>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="dictList" border>
      <!--<el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="序号" width="50" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="所属区划" width="150"  align="left" prop="" />-->
      <el-table-column label="百科词条名称" align="center" prop="caryopterisName" :show-overflow-tooltip="true" />
      <el-table-column label="百科词条类型" align="center" :formatter="getPublishName" prop="caryopterisType"
        :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createDate" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"
            v-hasPermi="['sys:pbpjInformation:view']">查看</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleInit(scope.row)"
            v-hasPermi="['sys:pbpjInformation:update']">修改</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)"
            v-hasPermi="['sys:pbpjInformation:delete']">删除</el-button>
          <!--<el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-jinyong"
                @click="handleAble(scope.row)"
                v-hasPermi="['sys:pbpjInformation:able']"
              >启禁用</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
    <!-- </el-col>
    </el-row> -->
    <!-- 添加或修改数字平板信息信息对话框 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="1100px"
      scrollbar append-to-body class="el-dialog-type">
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <el-input v-show="false" v-model="form.id" />

          <el-input v-show="false" v-model="form.deleteStatus" />
          <el-input v-show="false" v-model="form.createDate" />
          <table cellspacing="0" cellpadding="0" border="0" class='zf-zc-table'>
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i>
                <b>百科词条名称：</b>
              </td>
              <td colspan="3">

                <el-row>

                  <el-col :span="24">
                    <el-form-item prop="caryopterisName">
                      <el-input v-model.trim="form.caryopterisName" placeholder="请输入百科词条名称" maxlength="200"
                        show-word-limit />
                    </el-form-item>
                  </el-col>
                </el-row>
              </td>
            </tr>
            <tr>
              <td>
                <b><i class="require">*</i>词条类型：</b>
              </td>
              <td colspan="">

                <el-form-item>
                  <el-radio label="1" v-model="form.caryopterisType" @change="chooseCollectionType('1')">文本内容</el-radio>
                  <el-radio label="2" v-model="form.caryopterisType" @change="chooseCollectionType('2')">图片</el-radio>
                </el-form-item>


              </td>
              <td>
                <b><i class="require">*</i>排序号：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sort">
                  <el-col :span="24">
                    <el-input-number v-model="form.sort" :min="1" :max="9999" />
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr v-show="show_upload">
              <td colspan="4">
                <el-col :span="24">
                  <el-form-item label="">
                    <el-upload class="upload-demo" :action="uploadUrl()" :before-upload="beforeUpload"
                      :on-error="uploadError" :show-file-list="false" :on-success="uploadSuccess">
                      <el-button size="small" type="primary">点击上传<i class="iconfont zfsoft-wenjianshangchuan"></i>
                      </el-button>
                    </el-upload>
                    <div v-show="null!=form.attaOid && ''!=form.attaOid">
                      <span>{{form.attaName}}{{attaTitle}}</span>
                      <el-link type="primary" @click="downloadFile(form.attaOid)">下载</el-link> |
                      <el-link type="primary" @click="viewFileNew(form.attaOid)">预览</el-link>
                    </div>
                  </el-form-item>
                </el-col>
              </td>
            </tr>
            <tr v-show="show_text">
              <td>
                <b>内容：</b>
              </td>
              <td colspan="3">

                <el-col :span="24">
                  <el-form-item prop="caryopterisContext">
                    <el-input v-model.trim="form.caryopterisContext" type="textarea" maxlength="1000" show-word-limit
                      placeholder="请输入内容"></el-input>
                  </el-form-item>
                </el-col>
              </td>
            </tr>
            <tr>
              <td><i class="require"></i>
                <b>引用出处链接：</b>
              </td>
              <td colspan="3">

                <el-row>

                  <el-col :span="24">
                    <el-form-item prop="linkAddress">
                      <el-input v-model.trim="form.linkAddress" placeholder="请输入百科词条名称" maxlength="200"
                        show-word-limit />
                    </el-form-item>
                  </el-col>
                </el-row>
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
    <!-- 数字平板信息信息详细 -->
    <el-dialog v-dialog-drag :title="title" :visible.sync="openView" width="1100px" height='700px' scrollbar
      append-to-body>
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <el-input v-show="false" v-model="form.id" />

          <el-input v-show="false" v-model="form.deleteStatus" />
          <el-input v-show="false" v-model="form.createDate" />
          <table cellspacing="0" cellpadding="0" border="0" class='zf-zc-table'>
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i>
                <b>百科词条名称：</b>
              </td>
              <td colspan="3">

                <el-form-item prop="caryopterisName">
                  <el-col :span="24">
                    {{ form.caryopterisName }}
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b><i class="require">*</i>词条类型：</b>
              </td>
              <td colspan="">
                <template v-if="form.caryopterisType===1">
                  文本内容
                </template>
                <template v-else>
                  图片
                </template>


              </td>
              <td>
                <b><i class="require">*</i>排序号：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sort">
                  <el-col :span="24">
                    {{ form.sort }}
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <template v-if="form.caryopterisType===2">
              <tr>
                <td colspan="4">
                  <span>{{attaTitle}}</span>
                  <el-link type="primary" @click="downloadFile(form.attaOid)">下载</el-link> |
                  <el-link type="primary" @click="viewFileNew(form.attaOid)">预览</el-link>
                </td>
              </tr>
            </template>
            <template v-if="form.caryopterisType===1">
              <tr>
                <td>
                  <b>内容：</b>
                </td>
                <td colspan="3">
                  <el-form-item prop="caryopterisContext">
                    {{ form.caryopterisContext }}
                  </el-form-item>
                </td>
              </tr>
            </template>
            <tr>
              <td><i class="require"></i>
                <b>引用出处链接：</b>
              </td>
              <td colspan="3">

                <el-row>

                  <el-col :span="24">
                    <el-form-item prop="linkAddress">
                      {{form.linkAddress}}
                    </el-form-item>
                  </el-col>
                </el-row>
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
      :key='view.attaOid' @close="closeFileView" width="1100px" height='700px' scrollbar append-to-body>
      <file-view :attaOid="view.attaOid" @father-click="closeFileView"></file-view>
    </el-dialog>
  </div>
</template>

<script>
  import {
    save,
    del,
    getOne,
    page,
    getOneFile
  } from "@/api/onething/sxpz/comboServiceManage";
  import {
    pageFile,
    uploadFile
  } from "@/api/sys/atta";
  import {
    validIntNoZero
  } from '@/utils/validate';
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import fileView from '@/views/common/fileView';
  export default {
    components: {
      Treeselect,
      fileView
    },
    name: "ComboServiceManage",
    //定义获取父类传过来值的格式
    props: ["comboDireOid"],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 应用表格数据
        dictList: [],
        //查询平板信息名称参数
        dictName: '',
        // 区划树
        districtOptions: [],
        fileList: [],
        // 弹出层标题
        title: "",
        attaTitle: "",
        show_upload: "",
        show_text: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        viewDialogOptions: [],
        //启禁用状态
        ableMap: {
          'Y': '启用',
          'N': '禁用'
        },
        // 数字平板信息树选项
        dictOptions: undefined,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          caryopterisName: '',
          comboDirectoryOid: ''
        },
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 表单参数
        form: {
          id: '', //逻辑主键
          caryopterisName: '', //主题
          caryopterisType: '',
          caryopterisContext: '',
          comboDirectoryOid: '',
          attaOid: ''
        },
        // 表单校验
        rules: {
          caryopterisName: [{
            required: true,
            message: "百科词条名称不能为空",
            trigger: "blur"
          }],
          sort: [{
            required: true,
            message: "排序号不能为空",
            trigger: "blur"
          }],
          caryopterisType: [{
            required: true,
            message: "百科词条类型不能为空",
            trigger: "blur"
          }]
        },
      };
    },
    watch: {
      // 根据名称筛选部门树
      dictName(val) {
        this.$refs.tree.filter(val);
      }
    },
    created() {
      this.getList();

    },
    methods: {
      districtSel(node, instanceId) {
        this.form.districtName = node.label;
      },
      /** 查询数字平板信息列表 */
      getList() {
        this.dictList = []
        this.loading = true;
        this.queryParams.comboDirectoryOid = this.comboDireOid;
        page(this.queryParams).then(response => {
          this.dictList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },
      chooseCollectionType(caryopterisType) {
        if (caryopterisType == "2") {
          this.show_upload = true;
          this.show_text = false;
        } else {
          this.show_upload = false;
          this.show_text = true;
        }
      },
      getPublishName(val) {
        if (val.caryopterisType == 1) {
          return '文本内容';
        } else {
          return '图片';
        }
      },
      //选择配置项附件
      selectAttas() {
        this.getAttaList();
        this.openAttaListView = true;
      },
      //下载附件
      downloadFile(attaOid) {
        this.download(attaOid);
      },
      //预览附件
      viewFileNew(attaOid) {

        let item = {
          show: true,
          attaOid: attaOid
        };
        this.viewDialogOptions.push(item);
      },
      //关闭预览附件
      closeFileView() {
        this.viewDialogOptions.pop();
      },
      //清空附件
      clearAtta() {
        this.form.attaOid = '';
        this.form.attaName = '';
        this.attaTitle = '';
      },
      //成功后返回
      uploadSuccess(resp) {
        // this.form.attaOid = resp.data.oid;
        this.$set(this.form, 'attaOid', resp.data.oid);
        // this.form.attaName = resp.data.name;
        this.$set(this.form, 'attaName', resp.data.name);
        this.attaTitle = '';
        this.fileList = [];
      },
      //失败后返回
      uploadError(resp) {
        this.msgError("文件上传失败");
      },
      uploadUrl() {
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
      // 启禁用状态
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
        const oid = row.manageOid;
        getOne(oid).then(response => {
          this.form = response.data.comboServiceManage;
          this.getFilename();
          this.openView = true;
          this.title = "查看百科词条信息";
        });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.reset();
        let _that = this;
        let oid = row.manageOid;
        if (row.manageOid) {
          getOne(oid).then(response => {
            _that.openInit = true;
            _that.form = response.data.comboServiceManage;
            if (_that.form.caryopterisType == 2) {
              _that.form.caryopterisType = "2";
              this.show_upload = true;
              this.show_text = false;
            } else {
              _that.form.caryopterisType = "1";
              this.show_upload = false;
              this.show_text = true;
            }
            this.getFilename();
          });

        } else {
          _that.openInit = true;
          _that.form.caryopterisType = '1';
          this.chooseCollectionType('1');
        }
        // this.getFilename();
        _that.title = row.id ? "修改百科词条信息" : "新增百科词条信息";
      },
      getFilename() {
        if (this.form.attaOid) {
          getOneFile(this.form.attaOid).then(response1 => {
            this.attaTitle = response1.data.originName;
          });
        }
      },
      /** 提交按钮 */
      submitForm: function () {
        console.log('提交');
        let _that = this;
        _that.form.comboDirectoryOid = this.comboDireOid;
        if (_that.form.caryopterisType == '1') {
          this.clearAtta();
          this.validateInfo();
        } else {
          _that.form.content = '';
          if (_that.form.attaOid) {
            _that.validateInfo();
          }else{
            _that.$message.warning('附件不能为空，请上传附件！')
          }
        }

      },
      /** 提交表单信息 */
      validateInfo() {
        let _that=this;
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
        const oid = row.id;
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return del(oid);
        }).then(response => {

          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {});
      },
      /** 启禁用按钮操作 */
      handleAble(row) {
        const oid = row.id;
        let ableMessage = row.ableFlag == 0 ? '启用' : '禁用'
        this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          able(oid);
        }).then(response => {
          this.getList();
          this.msgSuccess(ableMessage + "成功");
        }).catch(function () {

        });
      }

    }
  };

</script>
<style lang="scss" scoped>
  .el-tree>.el-tree-node {
    min-width: 100%;
    display: inline-block;
  }

</style>
