<template>
  <div>
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="接收人" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入接收人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发送时间">
        <div class="block">
          <el-date-picker
            v-model="beginDateAndEndDate"
            type="daterange"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </div>
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="messageList" border>
      <!--<el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="接收人" align="center" prop="userName" />
      <el-table-column label="标题" align="center" prop="title" :show-overflow-tooltip="true"/>
      <el-table-column label="发送时间" align="center" prop="createDate" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:message:view']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sys:message:delete']"
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


    <!-- 查看已发送信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" width="1100px" height="600px" scrollbar append-to-body>
      <div>
        <el-form :model="form" label-width="0px" size="mini">
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="80%" />
            </colgroup>
            <tr>
              <td><b>接收人：</b></td>
              <td>
                {{ form.userName }}
              </td>
            </tr>
            <tr>
              <td><b>发送时间：</b></td>
              <td>
                {{ form.createDate }}
              </td>
            </tr>
            <tr>
              <td><b>标题：</b></td>
              <td>
                {{ form.title }}
              </td>
            </tr>
            <tr>
              <td><b>消息内容：</b></td>
              <td>
                {{ form.content }}
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <!--<el-form ref="form" :model="form" label-width="140px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="接收人：">{{ form.userName }}</el-form-item>
            <el-form-item label="发送时间：">{{ form.createDate }}</el-form-item>
            &lt;!&ndash;{{ parseTime(form.createDate) }}&ndash;&gt;
          </el-col>
          <el-col :span="24">
            <el-form-item label="标题：">{{ form.title }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="消息内容：">{{ form.content }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>-->
      <el-table
        border
        :data="messageFileList"
        style="width: 100%">
        <el-table-column align="center"
          label="附件名称"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="附件大小"
          width="180">
          <template slot-scope="scope">
            <span>{{ scope.row.size }}</span>
          </template>
        </el-table-column>
        <el-table-column width="180" label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="success"
              @click="downloadFile(scope.row.oid)">下载</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { sendPage, deleteSended, getOneSended} from "@/api/sys/message";
import {getAttaListByOids} from "@/api/sys/atta";
export default {
  name: "MessageSended",
  data() {
    return {

      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      messageList: [],
      //附件列表
      messageFileList:[],
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 第三方系统状态
      readStatusMap:{'0':'未读','1':'已读'},

      beginDateAndEndDate:[],

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title:'',
        userName:'',
        beginDate:'',
        endDate:''
      },
      // 表单参数
      form: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询应用列表 */
    getList() {
      this.loading = true;
      var that = this ;
      if(null!=this.beginDateAndEndDate){
        this.queryParams.beginDate=this.beginDateAndEndDate[0];
        this.queryParams.endDate=this.beginDateAndEndDate[1];
      }else {
        this.queryParams.beginDate=undefined;
        this.queryParams.endDate=undefined;
      }
      sendPage(this.queryParams).then(response => {
        this.messageList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      }).catch(function() {
        that.loading = false;
      });
    },
    // 消息状态
    readStatusFormat(row) {
      return this.selectMapLabel(this.readStatusMap, row.readStatus);
    },
    // 取消按钮
    cancel() {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {};
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
      this.beginDateAndEndDate = null
      this.handleQuery();

    },
    /** 查看按钮操作 */
    async handleView(row) {
      this.reset();
      const id = row.id;
      await getOneSended(id).then(response => {
        this.form = response.data;
        this.messageFileList = [];
        if(undefined!=response.data.attaOids){
          getAttaListByOids(response.data.attaOids).then(resp => {
            this.messageFileList = [];
            this.messageFileList = resp.data;
          });
        }
        this.openView = true;
        this.title = "查看已发送消息";
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
          return deleteSended(id);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    //下载附件
    downloadFile(attaOid){
      this.download(attaOid);
    },
  }
};
</script>
