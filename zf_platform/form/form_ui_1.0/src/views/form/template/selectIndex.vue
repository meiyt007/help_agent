<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="模板名称"  prop="templateName">
        <el-input
          v-model="queryParams.templateName"
          placeholder="请输入模板名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="ml5">重置</el-button>
      </el-form-item>
    </el-form>
    <div class="moudle-title">
      <i class="el-icon-menu"></i>表单模板列表
    </div>
    <el-row :gutter="10" class="mb8">
      <el-col class="el-clo">
        <div class="creat-moudle" style="cursor: pointer" @click="designFormByTemplate('')">
          <div class="creat-item">
            <i class="el-icon-plus"></i>
          </div>
          <p>直接设计</p>
        </div>
      </el-col>
      <el-col class="el-clo" v-for="(item,index) in templateFixedList" >
        <div class="creat-box" @click="designFormByTemplate(item.templateOid)">
          <div class="creat-pic-list">
            <div class="img-padding">
              <el-image style="width: 140px;height: 153px" :src="item.fastdfsUrl">
                <div slot="error" class="image-slot">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
            </div>
            <div class="time-item"><i class="el-icon-timer"></i>{{item.createDate}}</div>
          </div>
          <el-tooltip class="item" effect="dark" :content="item.templateName" placement="top" v-if="item.templateName.length > 10 ">
            <p class="pic-title">{{item.templateName | ellipsis(10) }}</p>
          </el-tooltip>
          <p class="pic-title" v-if="item.templateName.length <= 10 ">{{item.templateName}}</p>
        </div>
      </el-col>
      <el-col class="el-clo" v-for="(item,index) in templateList" >
        <div class="creat-box" @click="designFormByTemplate(item.templateOid)">
          <div class="creat-pic-list">
            <div class="img-padding">
              <el-image style="width: 140px;height: 153px" :src="item.fastdfsUrl">
                <div slot="error" class="image-slot">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
            </div>
            <div class="time-item"><i class="el-icon-timer"></i>{{item.createDate}}</div>
          </div>
          <el-tooltip class="item" effect="dark" :content="item.templateName" placement="top" v-if="item.templateName.length > 10 ">
            <p class="pic-title">{{item.templateName | ellipsis(10) }}</p>
          </el-tooltip>
          <p class="pic-title" v-if="item.templateName.length <= 10 ">{{item.templateName}}</p>
        </div>
      </el-col>
    </el-row>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!--查看表单-->
    <el-dialog v-dialog-drag :visible.sync="view.designShow"  title="查看模板" v-for="view in viewDesignDialogOptions"@close="closeDesignView"   width="90%" append-to-body>
      <FormView :key="'view'+view.templateOid" :formDataConfig="view.templateConfig" :disabled="true"></FormView>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDesignView">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {getOne, selectPage,querylist } from "@/api/form/template";
  import fileView from '@/views/common/fileView';
  export default {
    name: "SelectTemplate",
    components:{fileView},
    props:["formMainOid","authorizeKey","designOid"],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        //授权id
        // 表格数据
        templateList: [],
        //固化数据
        templateFixedList:[],
        //附件列表
        fileList:[],
        viewDialogOptions:[],
        viewDesignDialogOptions:[],
        designShow:false,
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        // 启用状态
        ableMap:{'1':'启用','0':'禁用'},
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10
        },
        // 表单参数
        form: {},
        appBaseApi : '',
        // 表单校验
        rules: {
        }
      };
    },
    created() {
      //this.getFixedList();
      this.getList();
      this.appBaseApi = process.env.VUE_APP_BASE_API;
    },
    methods: {
      /** 查询列表 */
      getList() {
        this.loading = true;
        let that = this;
        this.templateList = [];
        selectPage(this.queryParams).then(response => {
          this.templateList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        }).catch(function () {
          that.loading = false;
        });
      },
      getFixedList(){
        let queryParams = {isFixed:1}
        querylist(queryParams).then(response => {
          this.templateFixedList = response.data;
        }).catch(function () {

        });
      },
      // 启用状态
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
        this.queryParams = {};
        this.queryParams.pageNum = 1;
        this.queryParams.pageSize = 10;
        this.handleQuery();
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.reset();
        const oid = row.id;
        getOne(oid).then(response => {
          this.form = response.data;
          this.openView = true;
          this.title = "查看";
        });
      },
      // 选择模板设计
      designFormByTemplate(templateOid) {
        let routeData = this.$router.resolve({
          path: '/formDesign',
          query: { formMainOid: this.formMainOid, designOid:this.designOid, authorizeKey: this.authorizeKey,templateOid:templateOid }
        });
        window.open(routeData.href, '_blank');
        //关闭当前弹框
        this.$emit('closeTemplateView', true);
      },
      //模板查看
      designView(){
        this.designShow = true;
      },
      //查看模板设计内容
      viewDesign(){
        let item = {designShow:true,templateOid:this.templateOid,templateConfig:this.templateConfig};
        this.viewDesignDialogOptions.push(item);
      },
      //关闭预览附件
      closeDesignView(){
        this.viewDesignDialogOptions.pop();
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

    }
  };
</script>
<style>
  .app-container .image-slot{
    font-size: 30px;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: #f5f7fa;
    color: #909399;
  }
</style>
<style scoped>
  .iconfont {
    padding-left: 4px !important;
    font-size: 12px !important;
  }
  .el-clo{
    width: 180px;
    display: inline-block;
    vertical-align: middle;
    margin-right: 50px;
  }
  .moudle-title {
    font-weight: normal;
    font-size: 14px;
    margin-bottom: 20px;
  }
  .creat-moudle {
    text-align: center;
    font-size: 14px;
    width: 180px;
    display: inline-block;
    vertical-align: middle;
    border: 1px solid #ddd;
    height: 210px;
    padding-top: 60px;
  }
  /*.creat-moudle .creat-item {*/
  /*  width: 180px;*/
  /*  height: 210px;*/
  /*  border: 1px solid #ddd;*/
  /*  line-height: 240px;*/
  /*  cursor: pointer;*/
  /*}*/
  .creat-moudle .creat-item > i {
    font-size: 60px;
    color: #c9c9c9;
  }
  .creat-box {
    width: 180px;
    height: 250px;
    display: inline-block;
    vertical-align: middle;
    cursor: pointer;
  }
  .creat-box .creat-pic-list {
    width: 180px;
    height: 210px;
    background-color: rgb(239, 241, 244);
  }
  .creat-box .creat-pic-list .img-padding {
    padding:20px 20px 5px 20px;
    box-sizing: border-box;
  }
  .creat-box .creat-pic-list .img-padding>img{
    width: 100%;
    min-height: 145px;
  }
  .creat-box .creat-pic-list .time-item{
    height: 30px;
    line-height: 30px;
    text-align: center;
    color: #333;
    margin: 0;
    font-size: 13px;
  }
  .creat-box .pic-title{
    text-align: center;
    font-size: 14px;
  }

</style>
