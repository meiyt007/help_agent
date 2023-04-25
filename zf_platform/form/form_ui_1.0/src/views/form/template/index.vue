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
       <!--<el-form-item label="授权key" prop="authorizeKey">
          <el-input
            v-model="queryParams.authorizeKey"
            placeholder="请输入授权key"
            clearable
            size="small"
          />
        </el-form-item>-->
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
        >新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="templateList" border>
      <!--<el-table-column type="selection" width="55" align="center"  />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="模板名称" align="center" prop="templateName" :show-overflow-tooltip="true" />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createDate" width="160" />
      <el-table-column label="设计状态" align="center"  width="150" prop="templateConfig" >
        <template slot-scope="scope">
          <span v-if="undefined == scope.row.templateConfig">未设计</span>
          <span style="color: green" v-if=" undefined != scope.row.templateConfig">已设计</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center"  width="120" prop="isAble" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isAble"
            :active-value="1"
            :inactive-value="0"
            @change="handleAble(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            v-if="isAdminUser || scope.row.isFixed != 1"
            @click="handleInit(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-zaixianshenbao"
            v-if="isAdminUser || scope.row.isFixed != 1"
            @click="designTemplate(scope.row)"
          >设计模板</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            v-if="isAdminUser || scope.row.isFixed != 1"
            @click="handleDelete(scope.row)"
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


    <!-- 添加或修改信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>事项标题</h3>-->
        <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="demo-ruleForm">
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.templateOid" />
          <el-input v-show="false" v-model="form.isAble" />
          <el-input v-show="false" v-model="form.isDelete" />
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
            <td><i class="require">*</i><b>模板名称：</b></td>
            <td colspan="3">
              <el-form-item prop="templateName">
                <el-input v-model.trim="form.templateName" placeholder="请输入模板名称" maxlength="20" show-word-limit/>
              </el-form-item>
            </td>
            </tr>
            <!--<tr>
              <td><b><i class="require">*</i>模板设计：</b></td>
              <td colspan="3">
                <el-form-item prop="templateConfig">
                  <el-input v-show="false" v-model="form.templateConfig" ></el-input>
                  <el-button @click="designTemplate" type="primary" size="mini">设计模板<i class="iconfont zfsoft-zaixianshenbao"></i></el-button>
                </el-form-item>
              </td>
            </tr>-->
            <tr>
              <td><b>模板样本：</b></td>
              <td colspan="3">
                <el-form-item prop="templateAtta">
                  <el-input v-show="false" v-model="form.templateAtta" />
                  <el-upload
                    class="upload-demo"
                    accept=".jpg,.png,.jpeg,.gif,.bmp"
                    :headers="getToken()"
                    :action="uploadUrl()"
                    :on-error="uploadError"
                    :before-upload="beforeUpload"
                    :file-list="fileList"
                    :on-success="uploadSuccess">
                    <el-button type="success" size="mini">上传样本图片<i class="el-icon-upload el-icon--right"></i></el-button>
                  </el-upload>
                  <!--<el-button v-if="undefined != form.templateConfig" type="success" size="mini" @click="getTemplateImage">生成样本图片<i class="el-icon-sold-out el-icon&#45;&#45;right"></i></el-button>-->
                  <div v-show="null!=form.templateAtta && ''!=form.templateAtta">
                    <span>{{form.attaName}}</span>
                    <el-link type="success"  @click="downloadFile(form.templateAtta)">下载</el-link> |
                    <el-link type="success" @click="viewFile(form.templateAtta)">预览</el-link> |
                    <el-link type="danger" @click="clearAtta">删除</el-link>
                  </div>
                 <!-- <div style="color: red">备注：样本图片，可在设计完成后，点击生成！</div>-->
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>备注：</b></td>
              <td colspan="3">
                <el-form-item prop="memo">
                  <el-input v-model.trim="form.remark" type="textarea" placeholder="请输入备注"  maxlength="500" show-word-limit></el-input>
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


    <!-- 查看信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>事项标题</h3>-->
        <el-form :model="form" label-width="0px" class="demo-ruleForm">
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>模板名称：</b></td>
              <td colspan="3">
                {{ form.templateName }}
              </td>
            </tr>
            <tr>
              <td><b>模板设计：</b></td>
              <td colspan="3">
                <el-button type="success" size="mini" @click="viewDesign">预览<i class="el-icon-upload el-icon--right"></i></el-button>
              </td>
            </tr>
            <tr>
              <td><b>模板样本：</b></td>
              <td colspan="3">
                <el-form-item>
                  <div v-show="null!=form.templateAtta && ''!=form.templateAtta">
                    <span>{{form.attaName}}</span>
                    <el-link type="success"  @click="downloadFile(form.templateAtta)">下载</el-link> |
                    <el-link type="success" @click="viewFile(form.templateAtta)">预览</el-link>
                  </div>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>备注：</b></td>
              <td colspan="3" >
                {{form.remark}}
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
      <file-view :attaOid="view.attaOid" :name="view.name" :downloadFileUrl="view.downloadFileUrl" @father-click="closeFileView"></file-view>
    </el-dialog>

    <!--预览表单-->
    <el-dialog  v-dialog-drag :visible.sync="view.designShow"  title="预览模板" v-for="view in viewDesignDialogOptions"@close="closeDesignView"   width="90%" append-to-body>
      <div style="min-height: 450px"  class="canvas_box" id="mycanvas" ref="mycanvas">
        <FormView :key="'view'+view.templateOid" :formDataConfig="view.templateConfig" :isShowDefaultVal="true" :disabled="view.disabled"></FormView>
      </div>
      <!--//如果需要合成按钮操作的话，如果是vue中想要页面出现就获取就要在create中调用-->
      <div slot="footer" class="dialog-footer">
        <el-button v-if="!view.disabled" type="success" @click="toImage">生成模板图片</el-button>
        <el-button @click="closeDesignView">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import { init, save, del ,getOne, page, able,uploadBase64Images } from "@/api/form/template";
  import fileView from '@/views/common/FormFileView';
  import { uploadImage,download } from "@/api/form/atta";
  import { getToken} from '@/utils/auth';
  import html2canvas from "html2canvas"
  export default {
    name: "Template",
    components:{fileView},
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        //授权id
        // 表格数据
        templateList: [],
        isAdminUser:false,
        //附件列表
        fileList:[],
        viewDialogOptions:[],
        viewDesignDialogOptions:[],
        designShow:false,
        fullscreenLoading:null,
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        intervalId:'',
        // 启用状态
        ableMap:{'1':'启用','0':'禁用'},
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          templateName: [
            { required: true, message: "模板名称不能为空", trigger: "blur" },
          ],
          /*templateConfig: [
            { required: true, message: "请设计模板", trigger: "blur" },
          ],*/
          /*templateAtta: [
            { required: true, message: "请上传样本图片", trigger: "blur" },
          ],*/
        }
      };
    },
    created() {
      this.getList();
    },
    destroyed() {
      if('' != this.intervalId){
        clearInterval(this.intervalId); //清除计时器
        this.intervalId = ''; //设置为空
      }
    },
    methods: {
      /** 查询列表 */
      getList() {
        this.loading = true;
        let that = this;
        page(this.queryParams).then(response => {
          this.isAdminUser = response.data.isAdminUser;
          this.templateList = response.data.pageResult.data;
          this.total = response.data.pageResult.total;
          this.loading = false;
        }).catch(function () {
          that.loading = false;
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
      toImage() {
        this.openFullScreen();
        window.pageYoffset = 0;
        document.documentElement.scrollTop = 0;
        document.body.scrollTop = 0;
        // 先获取你要转换为img的dom节点
        var node = document.getElementById("mycanvas"); //传入的id名称
        // console.log("node", node);
        var width = node.offsetWidth; //dom宽
        var height = node.offsetHeight; //dom高
        var scale = 2; //放大倍数 这个相当于清晰度 调大一点更清晰一点
        html2canvas(node, {
          width: 1100,
          heigth: 450,
          backgroundColor: "#ffffff", //背景颜色 为null是透明
          //dpi: window.devicePixelRatio * 2, //按屏幕像素比增加像素
          scale: scale,
          X: 0,
          Y: 0,
          //scrollX: -width*0.15, //如果左边多个白边 设置该偏移-3 或者更多
          scrollX:-3,
          scrollY: 0,
          //scrollY: 50,
          useCORS: true, //是否使用CORS从服务器加载图像 !!!
          allowTaint: true, //是否允许跨域图像污染画布  !!!
        }).then((canvas) => {
          // console.log("canvas", canvas);
          var url = canvas.toDataURL(); //这里上面不设值cors会报错
          let list = new Array();
          list.push(url);
          uploadBase64Images(list).then(response => {
            for (let i = 0; i < response.data.length; i++) {
              this.form.templateAtta = response.data[i].attaOid;
              this.form.attaName = response.data[i].name;
              //console.log(response.data[i])
            }
            this.closeFullScreen();
            this.closeDesignView();
          });

        /* var a = document.createElement("a"); //创建一个a标签 用来下载
          a.download = "名"; //设置下载的图片名称
          var event = new MouseEvent("click"); //增加一个点击事件
          //如果需要下载的话就加上这两句
          a.href = url;//此处的url为base64格式的图片资源
          a.dispatchEvent(event); //触发a的单击事件 即可完成下载
*/
        });
      },
      openFullScreen() {
        this.fullscreenLoading = this.$loading({
          lock: true,
          text: '生成样本图片中',
          spinner: 'el-icon-loading',
          background: 'rgba(179,176,175,0.02)'
        });
      },
      closeFullScreen() {
        this.fullscreenLoading.close();
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
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.reset();
        const oid = row.id;
        if(oid == undefined){
          this.form = {};
          this.openInit = true;
          this.title = "新增模板信息";
        }else {
          init(oid).then(response => {
            this.form = response.data;
            this.openInit = true;
            this.title = "修改模板信息";
          });
        }
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            save(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.openInit = false;
                this.getList();
              }
            });
          }else {
            return false;
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const oid = row.id;
        this.$confirm('是否确认删除吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return del(oid);
        }).then(() => {
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
        }).catch(function() {
          row.isAble = row.isAble === 0 ? 1 : 0
        });
      },
      getToken() {
        return {Authorization: 'Bearer ' + getToken()}
      },
      // 模板设计
      designTemplate(row) {
        //增加列表刷新监听
        if(''==this.intervalId && undefined == row.templateConfig){
          this.intervalId = setInterval(() => {
            this.startListEventListener();
          }, 3000);
        }

        let token = getToken();
        token = (token && token != 'undefined') ? token : null;
        let routeData = this.$router.resolve({
          path:'/formTemplate',
          query:{templateOid:row.templateOid, access_token:token}
        });
        window.open(routeData.href, '_blank');


        // let url = "/formTemplate?templateOid=" + row.templateOid,
        //   token = getToken();
        // if(token && token != 'undefined') {
        //   url += ('&access_token=' + token);
        // }
        // window.open(url, '_blank')
      },
      //模板查看
      designView(){
        this.designShow = true;
      },
      //查看模板设计内容
      viewDesign(){
        let item = {designShow:true,templateOid:this.form.templateOid,templateConfig:this.form.templateConfig || JSON.stringify({'formDesc': {}}),disabled:true};
        this.viewDesignDialogOptions.push(item);
      },
      //生成模板样本图片
      getTemplateImage(){
        let item = {designShow:true,templateOid:this.form.templateOid,templateConfig:this.form.templateConfig,disabled:false};
        this.viewDesignDialogOptions.push(item);
      },
      //关闭预览附件
      closeDesignView(){
        this.viewDesignDialogOptions.pop();
      },

      //下载附件
      downloadFile(attaOid){
        download(attaOid);
      },
      //预览附件
      viewFile(attaOid){
        let downloadFileUrl = process.env.VUE_APP_BASE_API + "/form/manager/downloadFile/";
        let item = {show:true,attaOid:attaOid,name:this.form.attaName
        ,downloadFileUrl:downloadFileUrl};
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
        this.form.templateAtta = resp.data.attaOid;
        this.form.attaName = resp.data.name;
        this.$refs['form'].clearValidate();
      },
      //失败后返回
      uploadError(resp){
        this.msgError("文件上传失败");
      },
      uploadUrl(){
        return uploadImage();
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
        //delFile(this.form.templateAtta);
        this.form.templateAtta = '';
        this.form.attaName = '';
        this.$forceUpdate();
      },
      //列表刷新
      startListEventListener(){
        let formTempateFlag = localStorage.getItem("formTempateFlag");
        if(undefined!=formTempateFlag && ''!=formTempateFlag  && null!=formTempateFlag  ){
          this.getList();
          //监听到true 清空
          localStorage.setItem('formTempateFlag','');
          clearInterval(this.intervalId); //清除计时器
          this.intervalId = ''; //设置为空
        }
      },
    }
  };
</script>
<style scoped>
 .iconfont{
  padding-left: 4px !important;
  font-size: 12px !important;
}
 .canvas_box .el-col-14{
   width: 100% !important;
 }
</style>
