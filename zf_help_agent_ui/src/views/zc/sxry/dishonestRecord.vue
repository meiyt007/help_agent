/**
* @Author: liyanqing
*/
<template>
  <div>
    <div>
      <el-row :gutter="10" class="mb8 fl">
        <el-col :span="1.5">
          <el-button
            type="default"
            icon="el-icon-plus"
            size="mini"
            @click="handleInit"
            v-hasPermi="['sys:dishonest:init']"
            v-if="status == 0"
          >新增</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="recordList" border>
        <el-table-column label="序号" width="80" type="index" align="center">
          <template slot-scope="scope">
            <span>{{scope.$index + 1}}</span>
          </template>
        </el-table-column>
        <el-table-column  prop="dishonestRecordTitle" label="记录标题" align="center" width="300"/>
        <el-table-column  prop="caseNumber" label="关联办件编号" align="center" width="300"/>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-xiugai"
              @click="handleInit(scope.row)"
              v-hasPermi="['sys:dishonest:init']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-shanchu"
              @click="handleDelete(scope.row)"
              v-hasPermi="['sys:dishonest:delete']"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getRecordList"
      />
    </div>

    <!--  新增或修改  -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" v-if="addDialogShow" :visible.sync="addDialogShow" width="900px"  append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="13%" />
            <col width="37%" />
            <col width="13%" />
            <col width="37%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i>
              <b>标题：</b>
            </td>
            <td>
              <el-form-item prop="dishonestRecordTitle">
                <el-col :span="24">
                  <el-input v-model.trim="form.dishonestRecordTitle" placeholder="请输入标题" maxlength="100" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i>
              <b>关联办件编号：</b>
            </td>
            <td>
              <el-form-item prop="caseNumber">
                <el-col :span="24">
                  <el-input v-model.trim="form.caseNumber" placeholder="请输入关联办件编号" maxlength="100" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i>
              <b>失信记录：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="dishonestRecord">
                <el-col :span="24">
                  <el-input type="textarea" v-model.trim="form.dishonestRecord" placeholder="请输入失信记录" maxlength="500" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="() => {addDialogShow = false; reset()}">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {deepClone} from "@/utils";
import {page, deletes, saveOrUpdate,getOne} from "@/api/zc/sxry/dishonestRecord";

export default {
  components: {},
  name: "dishonestRecord",
  //定义获取父类传过来值的格式
  props:["dishonestOid", "status"],
  data() {
    return {
      // 遮罩层
      loading: true,
      total: 0,
      title: '',
      addDialogShow: false,
      // 查询参数
      queryParams: {
        dishonestOid:this.dishonestOid,
        pageNum: 1,
        pageSize: 10,
      },
      // 表单参数
      form: {dishonestOid:this.dishonestOid},
      rules: {
        dishonestRecordTitle: [
          { required: true, message: "请输入失信记录标题", trigger: "blur" }
        ],
        caseNumber: [
          { required: true, message: "请输入关联办件编号", trigger: "blur" }
        ],
        dishonestRecord: [
          { required: true, message: "请输入失信记录", trigger: "blur" }
        ]
      },
      //失信记录新增或修改页面
      dialogOptions:[],
      //表格数据
      recordList:[],
    };
  },
  created() {
    //失信记录List
    this.getRecordList();
  },
  methods: {
    // 表单重置
    reset() {
      this.form = {};
      this.form.dishonestOid = this.dishonestOid;
      this.resetForm("form")
    },
    //获取表格数据
    getRecordList(){
      this.loading = true;
      page(this.queryParams).then(response => {
        this.recordList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      _that.reset();
      if(row.id) {
        getOne(row.id).then(response => {
          _that.addDialogShow = true;
          _that.form = deepClone(response.data);
        });
      } else {
        _that.addDialogShow = true;
      }
      _that.title = row.id ? "修改" : "新增";
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let id = row.id;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return deletes(id);
      }).then(() => {
        this.getRecordList();
        this.msgSuccess("删除成功");
      }).catch(function() {});
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdate(_that.form).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("保存成功");
              _that.addDialogShow = false;
              setTimeout(() => {
                _that.getRecordList();
              }, 10);
            }
          });
        }
      });
    },
  }
};
</script>
<style scoped>
 /* .dialog-table{
    padding: 0;
  }
  .dialog-table table{
    border: 0 !important;
  } */
</style>
