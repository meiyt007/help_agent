/**
* @Author: liangxm
* @Date: 2020-11-2 18:21:27
* @Last Modified by: liangxm
* @Last Modified time: 2020-11-1 10:54:27
* @Description: 文书模板管理
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--数字字段树数据-->

      <!--数字文书模板数据-->
      <el-col :span="24" :xs="24" class="app-right">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px" @submit.native.prevent>

          <el-form-item label="文书模板名称" prop="docuTemplateName">
            <el-input
              v-model.trim="queryParams.docuTemplateName"
              placeholder="请输入文书模板名称"
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
            >新增</el-button>
          </el-col>
        <!--  v-hasPermi="['sys:sxDocuTemplate:init']"-->
        </el-row>

        <el-table  v-loading="loading" :data="dictList" border :fit="true">
          <!--<el-table-column type="selection" width="55" align="center" />-->
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <!--<el-table-column label="配置" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span><a href="javascript:POBrowser.openWindowModeless('word','width=1200px;height=800px;')">模板配置</a></span>
            </template>
          </el-table-column>-->

          <el-table-column label="文书模板名称"   align="left" prop="docuTemplateName" :show-overflow-tooltip="true"/>

          <!--<el-table-column label="启用状态" align="left"  width="100" prop="enabledFlag" :formatter="statusFormat"/>-->
          <el-table-column label="启用状态" align="center"   prop="enabledFlag" >
            <template slot-scope="scope">
              <el-switch :value="scope.row.enabledFlag == 1" @change="handleAble(scope.row)"></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="创建时间"    align="left" prop="createDate" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                v-hasPermi="['sys:sxDocuTemplate:view']"
              >查看</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleInit(scope.row)"
                v-hasPermi="['sys:sxDocuTemplate:update']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleDelete(scope.row)"
                v-hasPermi="['sys:sxDocuTemplate:delete']"
              >删除</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-jinyong"
                @click="handleMode(scope.row)"
                v-hasPermi="['sys:sxDocuTemplate:able']"
              >模板配置</el-button>
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
    <!-- 添加或修改数字文书模板信息对话框 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="900px" append-to-body  class="el-dialog-type">
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b><i class="require">*</i>文书模板名称：</b>
            </td>
            <td colspan="3">

                <el-form-item prop="docuTemplateName">

                  <el-input v-model.trim="form.docuTemplateName" placeholder="请输入文书模板名称" maxlength="50" show-word-limit/>
                </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>文书模板类型：</b>
            </td>
            <td colspan="3">

              <el-form-item prop="docuTypeOid">
                <el-select v-model="form.docuTypeOid" >
                  <el-option v-for="dict in inFloorList" :key="dict.oid" :label="dict.name" :value="dict.dictOid"></el-option>
                </el-select>

              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>应用类型：</b>
            </td>
            <td colspan="3">

              <el-form-item prop="applicationType">
                <el-select v-model="form.applicationType" >
                  <el-option v-for="dict in yylxList" :key="dict.oid" :label="dict.name" :value="dict.dictOid"></el-option>
                </el-select>

              </el-form-item>
            </td>
          </tr>




        </table>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 数字文书模板信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" width="800px" append-to-body>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>文书模板名称：</b>
            </td>
            <td colspan="3">
              {{ form.docuTemplateName }}
            </td>
          </tr>
          <tr>
            <td>
              <b>文书模板类型：</b>
            </td>
            <td colspan="3">
                <el-select v-model="form.docuTypeOid" :disabled="true">
                  <el-option v-for="dict in inFloorList" :key="dict.oid" :label="dict.name" :value="dict.dictOid"></el-option>
                </el-select>
            </td>
          </tr>
          <tr>
            <td>
              <b>应用类型：</b>
            </td>
            <td colspan="3">
                <el-select v-model="form.applicationType" :disabled="true">
                  <el-option v-for="dict in yylxList" :key="dict.oid" :label="dict.name" :value="dict.dictOid"></el-option>
                </el-select>
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
import { save, del ,getOne, page,able,uploadDocuTemplate,downloadPrintFile } from "@/api/zc/sysRunConfiguration/sxDocuTemplate";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { getDictList } from "@/api/sys/common";
import {importScript} from "@/api/sys/util";
import Treeselect from "@riophae/vue-treeselect";

export default {
  components: { Treeselect },
  name: "SxDocuTemplate",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      dictList: [],
      //查询文书模板名称参数
      dictName:'',
      // 楼层数据列表
      inFloorList: [],
      //模板应用类型
      yylxList:[],
      // 区划树
      districtOptions: [],
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      //启禁用状态
      ableMap:{'1':'启用','0':'禁用'},
      // 数字文书模板树选项
      dictOptions: undefined,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        runCode:'',
        docuTemplateName:''
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {
        id: '', //逻辑主键
        docuTemplateName: '',//主题
        docuTypeOid:'',
        applicationType:''
      },
      // 表单校验
      rules: {
        docuTemplateName: [
          { required: true, message: "文书模板名称不能为空", trigger: "blur" }
        ]
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
    this.getDictList();
  },
  methods: {
    districtSel(node, instanceId) {
      this.form.districtName = node.label;
    },
    /** 查询数字文书模板列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.dictList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },


    /** 获取文书模板数据 */
    getDictList() {
      let _that = this;
      // 查询文书模板数据
      getDictList('TZSLX').then(response => {
        _that.inFloorList = response.data;
      });

      getDictList('YYLX').then(response => {
        _that.yylxList = response.data;
       /* alert(JSON.stringify(_that.yylxList ))*/
      });
    },
    /** 获取区划树 */
    getDistrictTree(districtOid) {
      let _that = this;
      queryDistrictSimpleTree(districtOid).then(response => {
        _that.districtOptions = response.data;
      });
    },
    // 启禁用状态
    statusFormat(row) {
      return this.selectMapLabel(this.ableMap, row.enabledFlag);
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
        this.title = "查看文书模板信息";
      });
    },

    handleMode(row) {
      let _that=this;
      this.reset();
      const oid = row.id;
      //判断系统
      var sUserAgent = navigator.userAgent;
      var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows")
      if(!isWin){
        getOne(oid).then(response => {
          //如果存在则打开，不存在则创建新文件并上传服务器
          if(response.data){
            uploadDocuTemplate(response.data.attaOid).then(res=>{
              let sysatta=res.data;
              //重新保存模板信息
              _that.form=response.data;
              if(!response.data.attaOid){

                _that.form.attaOid=sysatta.attaOid;
                save(_that.form).then(response => {});
              }
              if(sysatta.fastdfsNginxUrl){
                _that.getCServiceOpenFile(sysatta.fastdfsNginxUrl,sysatta.attaOid);
              }
            })
          }
        });

      }else{
        const baseURL = process.env.VUE_APP_BASE_API_PAGE;
        POBrowser.openWindowModeless(process.env.VUE_APP_BASE_API_PAGE+'/manage/zhuozheng/word?oid='+oid,'width=1200px;height=800px;')
      }

    },
    //调用c++服务的模板
    getCServiceOpenFile(url,attaOid){
      downloadPrintFile(url,attaOid).then(res => {

      });
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      this.reset();
      let _that = this;

      let oid = row.id;

      if(row.id) {
        getOne(row.id).then(response => {
          _that.openInit = true;
          _that.form = response.data;

        });
      } else {
        _that.openInit = true;
      }
      _that.title = row.id ? "修改文书模板信息" : "新增文书模板信息";
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
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
        }).then(function() {
          del(oid);
        }).then(() => {

          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 启禁用按钮操作 */
    handleAble(row) {
      const oid = row.id;
      let ableMessage = row.enabledFlag == '0' ? '启用' : '禁用'
      this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
         able(oid);
      }).then(() => {
        this.msgSuccess(ableMessage+"成功");
        this.getList();
      }).catch(function() {
        row.enabledFlag = row.enabledFlag == '0' ? '1' : '0'
      });
    }

  }
};
</script>
<style lang="scss" scoped>
  .el-tree>.el-tree-node{
    min-width:100%;
    display: inline-block;
  }
</style>
