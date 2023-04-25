<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
      <el-form-item label="表单名称" prop="formName">
        <el-input
          v-model="queryParams.formName"
          placeholder="请输入表单名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="表单编码" prop="formCode">
        <el-input
          v-model="queryParams.formCode"
          placeholder="请输入表单编码"
          clearable
          size="small"
        />
      </el-form-item>
      <!--<el-form-item label="表单状态">
        <el-select
          v-model="queryParams.formStatus"
          placeholder="请选择表单状态"
          clearable
          size="small"
        >
          <el-option
            v-for="(value, key) in formStatusMap" :key="key"
            :label="value"
            :value="key"
          />
        </el-select>
      </el-form-item>-->
      <el-form-item label="关联存储对象">
        <el-select v-model="queryParams.objectOid" clearable  filterable  placeholder="请选择">
          <el-option
            v-for="ob in objectQueryOptions"
            :key="ob.objectOid"
            :label="ob.objectName"
            :value="ob.objectOid"
          ></el-option>
        </el-select>
      </el-form-item>
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

    <el-table v-loading="loading" :data="formMainList" border>
      <!--<el-table-column type="selection" width="55" align="center"  />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="表单名称" align="center" prop="formName" :show-overflow-tooltip="true" />
      <el-table-column label="表单编码" align="center" prop="formCode" :show-overflow-tooltip="true"/>
      <el-table-column label="关联存储对象" align="center" prop="objectName" :show-overflow-tooltip="true"/>
      <!--<el-table-column label="表单状态" align="center"  width="80" prop="formStatus" :formatter="formStatusFormat" :show-overflow-tooltip="true"/>-->
      <el-table-column label="表单状态" align="center"  width="80" prop="formStatus" >
        <template slot-scope="scope">
          <span v-if="0 == scope.row.formStatus">未设计</span>
          <span class="redSpan" v-if="1==scope.row.formStatus">草稿</span>
          <span class="greenSpan" v-if="2==scope.row.formStatus">已发布</span>
          <span class="require" v-if="3==scope.row.formStatus">已变更</span>
        </template>
      </el-table-column>

      <el-table-column label="最新版本" align="center"  width="80" prop="version" :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createDate" :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center"  width="70" prop="isAble" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isAble"
            :active-value="1"
            :inactive-value="0"
            @change="handleAble(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240" align="center" class-name="small-padding fixed-width">
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
            @click="handleInit(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-zaixianshenbao"
            @click="handleEdit(scope.row)"
          >设计表单</el-button>
          <el-button
            v-show="undefined != scope.row.formStatus && 1 == scope.row.formStatus "
            size="mini"
            type="text"
            icon="icon iconfont zfsoft-fabu"
            @click="handleDeploy(scope.row)"
          >发布</el-button>
          <el-button
            v-show="undefined != scope.row.formStatus && 0 != scope.row.formStatus "
            size="mini"
            type="text"
            icon="icon iconfont zfsoft-fangdichanpinggu"
            @click="handleFormView(scope.row)"
          >查看表单</el-button>
          <el-button
            v-show="undefined != scope.row.formStatus && 0 != scope.row.formStatus "
            size="mini"
            type="text"
            icon="icon iconfont zfsoft-fuzhi"
            @click="handleCopy(scope.row)"
          >复制</el-button>
          <el-button
            size="mini"
            type="text"
            icon="icon iconfont zfsoft-biangengjilu"
            @click="handleDesign(scope.row)"
          >历史版本</el-button>
          <el-button
            size="mini"
            type="text"
            v-show="undefined == scope.row.formStatus || 0 == scope.row.formStatus || 1 == scope.row.formStatus"
            icon="iconfont zfsoft-shanchu"
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


    <!-- 添加或修改表单信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="90%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>标题</h3>-->
        <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="demo-ruleForm">
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.isAble" />
          <el-input v-show="false" v-model="form.isDelete" />
          <el-input v-show="false" v-model="form.createDate" />
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>表单名称：</b></td>
              <td colspan="3">
                <el-form-item prop="formName">
                  <el-input v-model.trim="form.formName" placeholder="请输入表单名称" maxlength="30" show-word-limit/>
                </el-form-item>
              </td>
              <!--<td><i class="require">*</i><b>表单编码：</b></td>
              <td>
                <el-form-item prop="formCode">
                  <el-input v-model.trim="form.formCode" placeholder="请输入表单编码" maxlength="30" show-word-limit/>
                </el-form-item>
              </td>-->
            </tr>
            <tr>
              <td><i class="require">*</i><b>所属模块：</b></td>
              <td>
                <el-form-item prop="moduleOid">
                  <el-select v-model="form.moduleOid" @change="moduleOidChange"  clearable placeholder="请选择">
                    <el-option
                      v-for="ty in moduleOptions"
                      :key="ty.moduleOid"
                      :label="ty.moduleName"
                      :value="ty.moduleOid"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>关联存储对象：</b></td>
              <td>
                <el-form-item prop="objectOid">
                  <el-select v-model="form.objectOid" @change="objectOidChange" clearable  placeholder="请选择">
                    <el-option
                      v-for="ob in objectOptions"
                      :key="ob.objectOid"
                      :label="ob.objectName"
                      :value="ob.objectOid"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>数据存储类型：</b></td>
              <td colspan="3">
                <el-form-item prop="saveDataType">
                  <el-radio-group v-model="form.saveDataType">
                    <el-radio
                      v-for="item in saveDataTypeMap"
                      :label="item.key"
                      :key="item.key"
                      :disabled="item.disabled"
                    >{{item.value}}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr v-if="1 == form.saveDataType">
              <td><i class="require">*</i><b>API接口地址：</b></td>
              <td colspan="3">
                <el-form-item  prop="apiUrl">
                  <el-input v-model.trim="form.apiUrl" placeholder="请输入API接口地址,必须是post请求" maxlength="200" show-word-limit/>
                  <span style="color: red;">注意：接口地址必须是POST请求，formData为接收参数</span>
                  <br/>
                  <span>测试连接：http://网关地址:端口/form/manager/testData</span>
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


    <!-- 查看表单信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" width="90%" append-to-body>
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
              <td><b>表单名称：</b></td>
              <td>
                {{ form.formName }}
              </td>
              <td><b>表单编码：</b></td>
              <td>
                {{ form.formCode }}
              </td>
            </tr>
            <tr>
              <td><b>所属模块：</b></td>
              <td>
                {{ form.moduleName }}
              </td>
              <td><b>关联存储对象：</b></td>
              <td>
                {{ form.objectName }}
              </td>
            </tr>
            <tr>
              <td><b>表单状态：</b></td>
              <td>
                {{formStatusMap[form.formStatus]}}
              </td>
              <td><b>数据存储类型：</b></td>
              <td>
                <!--{{saveDataTypeMap[form.saveDataType].value}}-->
                <span  v-for="item in saveDataTypeMap"
                       :label="item.key"
                       :key="item.key" v-if="form.saveDataType == item.key">
                  {{item.value}}
                </span>
              </td>
            </tr>
            <tr v-if="1 == form.saveDataType">
              <td><b>API接口地址：</b></td>
              <td colspan="3">
                {{form.apiUrl}}
              </td>
            </tr>
            <tr>

              <td><b>表单版本：</b></td>
              <td>
                {{ form.version }}
              </td>
              <td><b>创建时间：</b></td>
              <td>
                {{ form.createDate }}
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!--设计表单历史列表->-->
    <el-dialog v-dialog-drag :visible.sync="design.show"  title="历史版本列表" v-for="design in designDialogOptions" @close="colseDesignView" width="90%" append-to-body>
      <!--<el-scrollbar style="height:500px;">
      </el-scrollbar>-->
      <Design :formMainOid="design.formMainOid" :authorizeKey="design.authorizeKey"></Design>
    </el-dialog>

    <!--查看表单-->
    <el-dialog class="canvas_box" v-dialog-drag :visible.sync="view.designShow"  v-for="view in formViewOptions" title="查看表单" @close="closeFormView" width="90%" append-to-body>
      <FormView :key="'view'+view.formMainOid" :formMainOid="view.formMainOid" :authorizeKey="view.authorizeKey" :disabled="true" :isShowDefaultVal="true"></FormView>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeFormView">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 选择模块列表 -->
    <el-dialog title="选择模板"  :close-on-click-modal="false" :visible.sync="openTemplateView" width="90%" append-to-body>
      <SelectTemplate @closeTemplateView="closeTemplateView" :key="'select'+tempalte.formMainOid" :formMainOid="tempalte.formMainOid" :authorizeKey="tempalte.authorizeKey" ></SelectTemplate>
    </el-dialog>
  </div>
</template>

<script>
  import { init, save, del ,getOne, page, able,deploy,copy } from "@/api/form/main";
  import Design from '@/views/form/design';
  import { getToken} from '@/utils/auth';
  import { modulelist } from "@/api/form/module";
  import { objectlist,getFormObjectByObjectOid } from "@/api/form/object";
  import {getFormDesignByFormMainOid} from "@/api/form/design";
  import iconfont from '@/views/common/iconfontSelect';
  import SelectTemplate from '@/views/form/template/selectIndex.vue';
  export default {
    name: "FormMain",
    components: {Design,SelectTemplate,iconfont },
    props:['authorizeKey'],
    data() {
      let validateURL = (rule, value, callback) => {
        //console.log(value)
        if (value === '') {
          callback(new Error('API接口地址不能为空'));
        }else {
          const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
          const rsCheck = reg.test(value);
          if (!rsCheck) {
            callback(new Error('输入正确的API接口地址'));
          } else {
            callback();
          }
        }
      };
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 表单表格数据
        formMainList: [],
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        openTemplateView:false,
        tempalte:{},
        // 启用状态
        ableMap:{'1':'启用','0':'禁用'},
        // 表单状态 {'0':'未设计','1':'草稿','2':'发布','3':'变更'},
        formStatusMap:{'0':'未设计','1':'草稿','2':'发布','3':'变更'},
        //数据存储类型  0本地  1API  2数据库
        saveDataTypeMap:[{key:0,value:'表单系统',disabled:false},{key:1,value:'API',disabled:false},{key:2,value:'数据库',disabled:false}],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: ''
        },
        // 表单参数
        form: {
          saveDataType:'0'
        },
        moduleOptions:[],
        formTypeOptions:[],
        designDialogOptions:[],
        formViewOptions:[],
        objectOptions:[],
        objectQueryOptions:[],
        intervalId:'',
        // 表单校验
        rules: {
          formName: [
            { required: true, message: "表单名称不能为空", trigger: "blur" },
          ],
          formCode: [
            { required: true, message: "表单编码不能为空", trigger: "blur" },
          ],
          moduleOid: [
            { required: true, message: "请选择所属模块", trigger: "blur" },
          ],
          objectOid: [
            { required: true, message: "请选择关联存储对象", trigger: "blur" },
          ],
          saveDataType: [
            { required: true, message: "请选择数据存储类型", trigger: "blur" },
          ],
          apiUrl:[
            { required: true, message: "API接口地址不能为空", trigger: "blur" },
            {validator:validateURL, trigger: 'blur'}
          ],
        },
      };
    },
    created() {
      this.getList();
      this.queryMoudleList();
      this.queryObjectOptions();
      //如果没有配置数据库则，数据保存方式去除数据库
    },
    destroyed() {
      if('' != this.intervalId){
        clearInterval(this.intervalId); //清除计时器
        this.intervalId = ''; //设置为空
      }
    },
    methods: {
      init() {
        this.queryObjectOptions();
        this.getList();
      },
      //查询列表存储对象
      queryObjectOptions(){
        let par = {authorizeKey:this.authorizeKey,moduleOid:null};
        objectlist(par).then(response => {
          this.objectQueryOptions = response.data;
      });
      },
      //存储对象改变
      objectOidChange(data){
        if(''==data){
          this.saveDataTypeMap[2].disabled = false;
          return;
        }
        getFormObjectByObjectOid(data).then(response => {
          //不存在数据源 ，去除数据库的选项
          if(null == response.data.datasourceOid){
          this.saveDataTypeMap[2].disabled = true;
          this.form.saveDataType = 0;
        }else{
          this.saveDataTypeMap[2].disabled = false;
        }
      })
      },
      //列表刷新
      startListEventListener(){
        let formDesignFlag = localStorage.getItem("formDesignFlag");
        if(undefined!=formDesignFlag && ''!=formDesignFlag  && null!=formDesignFlag  ){
          console.log('列表刷新.....')
          this.getList();
          //监听到true 清空
          localStorage.setItem('formDesignFlag','');
          clearInterval(this.intervalId); //清除计时器
          this.intervalId = ''; //设置为空
        }
      },
      /** 查询表单列表 */
      getList() {
        this.loading = true;
        let that = this;
        if(undefined == this.queryParams.authorizeKey)
          this.queryParams.authorizeKey = this.authorizeKey;
        page(this.queryParams).then(response => {
          this.formMainList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      }).catch(function () {
          that.loading = false;
        });
      },
      queryMoudleList(){
        let par = {authorizeKey:this.authorizeKey};
        modulelist(par).then(response => {
          this.moduleOptions = response.data;
      });
      },
      //模块的切换
      moduleOidChange(moduleOid){
        this.$set(this.form, "objectOid", null);
        this.objectOptions = [];
        if(''!=moduleOid){
          this.queryObjectList(moduleOid);
        }
      },
      //存储对象集合
      queryObjectList(moduleOid){
        let par = {authorizeKey:this.authorizeKey,moduleOid:moduleOid};
        this.objectOptions = [];
        objectlist(par).then(response => {
          this.objectOptions = response.data;
      });

      },
      // 启用状态
      statusFormat(row) {
        return this.selectMapLabel(this.ableMap, row.isAble);
      },
      // 表单状态
      formStatusFormat(row) {
        return this.selectMapLabel(this.formStatusMap, row.formStatus);
      },
      // 取消按钮
      cancel() {
        this.openInit = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          saveDataType:'0'
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
        this.queryParams = {};
        this.queryParams.pageNum = 1;
        this.queryParams.pageSize = 10;
        this.handleQuery();
      },

      //设计表单
      handleEdit(row){
        const formMainOid = row.formMainOid;
        getFormDesignByFormMainOid(formMainOid).then(response => {
          const designOid = null!=response.data?response.data.designOid:'';
          if(''== designOid || null==designOid){
            this.tempalte.formMainOid = formMainOid;
            this.tempalte.authorizeKey = this.authorizeKey;
            this.openTemplateView = true;
          }else{
            let token = getToken();
            token = (token && token != undefined) ? token : null;
            let routeData = this.$router.resolve({
              path:'/formDesign',
              query:{formMainOid:formMainOid,designOid:designOid,authorizeKey:this.authorizeKey, token}
            });
            //增加列表刷新监听
            if(''==this.intervalId){
              window.intervalId = this.intervalId = setInterval(() => {
                this.startListEventListener();
              }, 3000);
            }
            window.open(routeData.href, '_blank');
          }
      });

      },
      //发布表单
      handleDeploy(row) {
        const formMainOid = row.formMainOid;
        this.$confirm('是否确认发布吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return deploy(formMainOid);
        }).then(() => {
          this.getList();
        this.msgSuccess("发布成功");
      }).catch(function() {});
      },
      //复制表单
      handleCopy(row) {
        const formMainOid = row.formMainOid;
        this.$confirm('是否确认复制吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return copy(formMainOid);
        }).then(() => {
          this.getList();
        this.msgSuccess("复制成功");
      }).catch(function() {});
      },
      // 表单查看
      handleFormView(row) {
        let formView = {'designShow':true,
          'formMainOid':row.formMainOid,authorizeKey:this.authorizeKey};
        this.formViewOptions.push(formView);
      },
      closeFormView(){
        this.formViewOptions.pop();
      },
      //关闭
      colseDesignView(){
        this.designDialogOptions.pop();
      },
      closeTemplateView(initInterFlag){
          this.openTemplateView = false;
          if(initInterFlag && ''==this.intervalId){
            window.intervalId = this.intervalId = setInterval(() => {
              this.startListEventListener();
            }, 3000);
          }
      },
      //历史版本
      handleDesign(row){
        let design = {'show':true,'formMainOid':row.formMainOid,authorizeKey:this.authorizeKey}
        this.designDialogOptions.push(design);
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.reset();
        const oid = row.id;
        getOne(oid).then(response => {
          this.form = response.data;
        this.openView = true;
        this.title = "查看表单信息";
      });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.reset();
        this.queryMoudleList();
        const oid = row.id;
        this.objectOptions = [];
        if(undefined == this.form.authorizeKey){
          this.form.authorizeKey = this.authorizeKey;
        }
        if(oid == undefined){
          this.form = {saveDataType:0};
          this.params = {};
          this.openInit = true;
          this.title = "新增表单信息";
        }else {
          init(oid).then(response => {
            this.form = response.data;
          this.queryObjectList(this.form.moduleOid);
          this.openInit = true;
          this.title = "修改表单信息";
        });
        }
      },
      /** 提交按钮 */
      async submitForm(){
    if(null == this.form.authorizeKey)
      this.form.authorizeKey = this.authorizeKey;
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
  //数据源切换
  dataSourceChange(data){
    if('' == data){
      this.$set(this.form, "datasourceOid", '');
      this.$set(this.form, "formForm", '');
    }
    this.params.datasourceOid = data;
    this.$refs['form'].clearValidate();
  },
  //打开表结构弹框
  openColumnView(row){
    let item = {show:true,columnOid:row.columnOid};
    this.columnDialogOptions.push(item);
  },
  //关闭表结构弹框
  closeColumnView(){
    this.columnDialogOptions.pop();
  }
  }
  };
</script>
<style scoped>
  .greenSpan{
    color: green;
  }
  .redSpan{
    color: red;
  }
  .el-select{
    width: 100% !important;
  }
</style>
