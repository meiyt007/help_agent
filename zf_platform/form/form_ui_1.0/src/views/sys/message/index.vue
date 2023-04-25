<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="消息状态">
        <el-select
          v-model="queryParams.readStatus"
          placeholder="消息状态"
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="(value, key) in readStatusMap" :key="key"
            :label="value"
            :value="key"
          />
        </el-select>
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
          icon="iconfont zfsoft-yijianshezhi"
          size="mini"
          @click="handleInit"
          v-hasPermi="['sys:message:init']"
        >发送消息</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="iconfont zfsoft-shenchashouli"
          size="mini"
          @click="handleSendPage"
          v-hasPermi="['sys:message:listExp']"
        >已发送消息</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="messageList" border>
      <!--<el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="标题" align="center" prop="title" :show-overflow-tooltip="true"/>
      <el-table-column label="发送时间" align="center" prop="createDate" />
      <el-table-column label="消息状态" align="center"  width="100" :formatter="readStatusFormat" />
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


    <!-- 添加或修改应用信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <!--<treeselect
            class="treeselect"
            :defaultExpandLevel="1"
            noOptionsText = "暂无数据"
            noResultsText="暂无数据"
            v-model="form.districtOid"
            :options="districtOptions" placeholder="请选择所属区划" />-->
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>所属区划：</b></td>
              <td>
                <el-form-item prop="districtOidSelect">
                  <treeselect
                    class="treeselect"
                    :defaultExpandLevel="1"
                    noOptionsText = "暂无数据"
                    noResultsText="暂无数据"
                    v-model="form.districtOidSelect"
                    :options="districtOptions" placeholder="请选择所属区划" />
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>所属机构：</b></td>
              <td>
                <el-form-item prop="organOids">
                  <treeselect
                    :multiple="true"
                    :options="organOptions"
                    noOptionsText = "暂无数据"
                    noResultsText="暂无数据"
                    placeholder="请选择所属机构"
                    :defaultExpandLevel="1"
                    v-model="organOids"
                    :limit="3"
                    :limitText="limitText"
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>接受人：</b></td>
              <td>
                <el-form-item prop="userOids" >
                  <!--<treeselect class="treeselect"
                              v-model="form.userOids"
                              :options="userOptions"
                              :multiple="true"
                              noOptionsText="暂无数据"
                              noResultsText="暂无数据"
                              :show-count="true"
                              :defaultExpandLevel="1"
                              :limit="3"
                              :limitText="limitText"
                              placeholder="请选择接受人"/>-->
                  <el-select  style="width: 100%" v-model="form.userOids" multiple collapse-tags
                              @change="select_status"
                              @remove-tag="removeTag"
                              clearable
                              filterable
                              placeholder="请选择">
                    <el-option
                      v-for="dict in userOptions"
                      :key="dict.id"
                      :label="dict.label"
                      :value="dict.id"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </td>
              <td><b>外部链接：</b></td>
              <td>
                <el-form-item>
                  <el-input v-model.trim="form.url" placeholder="请输入外部链接" maxlength="100" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>标题：</b></td>
              <td colspan="3">
                <el-form-item prop="title">
                  <el-input v-model.trim="form.title" placeholder="请输入标题" maxlength="100" show-word-limit />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>内容：</b></td>
              <td colspan="3">
                <el-form-item prop="content">
                  <el-input v-model.trim="form.content" type="textarea" placeholder="请输入内容" maxlength="1000" show-word-limit></el-input>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>添加附件：</b></td>
              <td colspan="3">
                <el-form-item prop="iconName">
                  <el-upload
                    class="upload-demo"
                    :action="uploadUrl()"
                    :headers="getToken()"
                    :on-error="uploadError"
                    :before-upload="beforeUpload"
                    :file-list="fileList"
                    :on-success="uploadSuccess">
                    <el-button type="primary" size="small">上传附件<i class="el-icon-upload el-icon--right"></i></el-button>
                  </el-upload>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          附件列表
        </el-col>
      </el-row>
      <el-table
        :data="messageFileList"
        border
        style="width: 100%">
        <el-table-column align="center"
                         label="附件名称"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center"
                         label="附件大小"
                         width="180">
          <template slot-scope="scope">
            <span>{{ scope.row.size }}</span>
          </template>
        </el-table-column>
        <el-table-column width="180" label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini"
                       type="success"
                       @click="downloadFile(scope.row.oid)">下载</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleAttaDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" @close="openViewClose" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <el-form  :model="form" label-width="0px" size="mini">
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="20%" />
              <col width="80%" />
            </colgroup>
            <tr>
              <td><b>发送人：</b></td>
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
              <td><b>外部链接：</b></td>
              <td>
                {{ form.url }}
              </td>
            </tr><tr>
            <td><b>标题：</b></td>
            <td>
              {{ form.title }}
            </td>
          </tr>
            <tr>
              <td><b>内容：</b></td>
              <td>
                {{ form.content }}
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <el-table
        :data="messageFileList"
        style="width: 100%">
        <el-table-column
          label="附件名称"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="附件大小"
          width="180">
          <template slot-scope="scope">
            <span>{{ scope.row.size }}</span>
          </template>
        </el-table-column>
        <el-table-column width="180" label="操作">
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

    <el-dialog v-dialog-drag :visible.sync="openSendView"  title="查看已发送消息列表" width="70%" append-to-body>
      <el-scrollbar style="height:500px;">
        <index-sended></index-sended>
      </el-scrollbar>
    </el-dialog>

  </div>
</template>

<script>
  import { send, del ,getOne, page, read } from "@/api/sys/message";
  import { queryAllDistrictSimpleTree } from "@/api/sys/district";
  import {queryUserTreeByOrganOid } from "@/api/sys/user";
  import {getAttaListByOids} from "@/api/sys/atta";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import indexSended from '@/views/sys/message/indexSended';
  import {uploadFile } from "@/api/sys/atta";
  import { getToken} from '@/utils/auth'
  export default {
    name: "Message",
    props:["barFlag"],
    components: { Treeselect,indexSended },
    watch:{
      'organOids':'organOidChange',
      'form.districtOidSelect': {
        handler(val, oldVal) {
          //机构加载
          if("undefined"!=typeof(val) && val!=oldVal){
            let dataId = null!=val?val.substring(val.lastIndexOf('-')+1,val.length):'';
            this.form.districtOid = dataId;
            this.districtOidChange(dataId)
          }
        }
      }
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 表格数据
        messageList: [],
        //区划数据
        districtOptions: [],
        //修改的机构数据
        organOptions: [],
        //接受人
        userOptions:[],
        noResultsOrganText:'暂无数据',
        noResultsUserText:'暂无数据',
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        //已发送
        openSendView:false,
        // 第三方系统状态
        readStatusMap:{'0':'未读','1':'已读'},
        fileList:[],
        //附件列表
        messageFileList:[],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          title:'',
          readStatus: '0'
        },
        organOids:[],
        // 表单参数
        form: {
          districtOid:undefined,
          userOids:[]
        },
        attaOids:[],
        oldOptions:[],
        value:[],
        // 表单校验
        rules: {
          districtOidSelect: [
            { required: true, message: "所属区划不能为空", trigger: "change" }
          ],
          organOids: [
            { required: true, message: "所属机构不能为空", trigger: "change" }
          ],
          userOids: [
            { required: true, message: "接受人不能为空", trigger: "change" }
          ],
          title: [
            { required: true, message: "标题不能为空", trigger: "blur" }
          ],
          content: [
            { required: true, message: "内容不能为空", trigger: "blur" }
          ]
        }
      };
    },
    created() {
      this.getDistrictTree();
      this.getList();
    },
    methods: {
      // 多选模式下移除tag时触发,val为移除的tag值
      removeTag(val) {
        if (val === "allSelect") {
          this.value = [];
        }
        console.log("removeTag===", this.value, val);
      },
      select_status(valArr){
        this.$forceUpdate();

        // 保存所有选项的id
        /*for (const user of this.userOptions) {
          if(this.form.userOids.indexOf(user.id) == -1 && 'allSelect' != user.id){
            this.form.userOids.push(user.id);
          }
        }*/
        const allIdArr = []
        // 保存所有选项的id
        for (const argumentId of this.userOptions) {
          allIdArr.push(argumentId.id)
        }
        const oldVal = this.oldOptions.length === 1 ? this.oldOptions[0] : []
        // 当前选中的有'全选'
        if (valArr.includes('allSelect')) {
          this.value = allIdArr
        }
        // 旧数据包含'全选'，当前选中数据不包含全选
        if (oldVal.includes('allSelect') && !valArr.includes('allSelect')) {
          this.value = []
        }
        // 旧数据包含'全选'，当前选中数据包含全选
        if (oldVal.includes('allSelect') && valArr.includes('allSelect')) {
          const index = valArr.indexOf('allSelect')
          valArr.splice(index, 1) // 排除全选选项
          this.value = valArr
        }
        // 旧数据不包含'全选'，当前选中数据不包含'全选'
        if (!oldVal.includes('allSelect') && !valArr.includes('allSelect')) {
          // 除了全选外 其他全部选中时
          if (valArr.length === allIdArr.length - 1) {
            this.value = ['allSelect'].concat(valArr)
          }else {
            this.value = valArr
          }
        }
        // 数据发生变化时保存数据，作为下次对比的旧数据
        this.oldOptions[0] = this.value
        this.form.userOids = this.value
      },
      limitText(count){
        return `更多+${count}`;
      },
      /** 获取所有区划树 */
      getDistrictTree(districtOid) {
        queryAllDistrictSimpleTree(districtOid).then(response => {
          this.districtOptions = response.data;
        });
      },
      /**  区划tree选择触发事件 并切换组织机构 */
      async districtOidChange(districtOid) {
        this.organOids = [];
        this.form.userOids = [];
        this.organOptions = [];
        if(undefined!=districtOid){
          this.organOptions = [];
          await this.queryOrganTree(districtOid).then(response => {
            this.form.userOids = [];
            this.userOptions = [];
            this.organOptions = response.data;
          });
        }
      },
      async organOidChange(organOids){
        this.form.organOids = this.organOids;
        this.userOptions = [];
        this.form.userOids = [];
        for(const i in organOids){
          let organOid = organOids[i];
          organOid = null!=organOid?organOid.substring(organOid.length-32,organOid.length):'';
          await queryUserTreeByOrganOid(organOid).then(response => {
            if(null!=response.data){
              const allSelect = {
                label: '全选',
                id: 'allSelect'
              }
              let flag = true;
              for (const user of this.userOptions) {
                if('allSelect' == user.id){
                  flag = false;
                  break;
                }
              }
              if(flag){
                this.userOptions.unshift(allSelect)
              }
              for (let j in response.data){
                this.userOptions.push(response.data[j]);
              }
            }
          }).catch(function() {
          });
        }
      },
      /** 查询应用列表 */
      getList() {
        this.loading = true;
        var that = this ;
        page(this.queryParams).then(response => {
          this.messageList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        }).catch(function() {
          that.loading = false;
        });
      },
      getToken() {
        return {Authorization: 'Bearer ' + getToken()}
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
        this.organOids=undefined;
        this.userOids=undefined;
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams.readStatus='0';
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 查看按钮操作 */
      async handleView(row) {
        this.reset();
        const id = row.id;
        await getOne(id).then(response => {
          this.form = response.data;
          this.messageFileList = [];
          if(undefined!=response.data.attaOids){
            getAttaListByOids(response.data.attaOids).then(resp => {
              this.messageFileList = resp.data;
            });
          }
          this.openView = true;
          this.title = "查看消息信息";
          //修改查看状态
          read(id);
        });
      },
      //查看关闭事件
      openViewClose(){
        this.openView = false;
        if(this.queryParams.readStatus=='0'){
          this.getList();
          if('undefined' != typeof (this.barFlag)){
            // 将子组件与父组件联合起来
            this.$emit('father-click');
          }
        }
      },
      /** 发送消息操作 */
      handleInit(row) {
        this.messageFileList = [];
        this.reset();
        this.openInit = true;
        this.title = "发送消息";
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            let formData =  JSON.parse(JSON.stringify(this.form));
            formData.userOids.forEach((item,index,arr) => {
              if(item == 'allSelect'){
                arr.splice(index,1)
              }
            });
            formData.attaOids =  this.attaOids.join(',');
            formData.organOids = this.organOids;
            for(var i=0; i<this.form.organOids.length; i++){
              let  dataOid = this.form.organOids[i];
              //dataOid = null!=dataOid?dataOid.substring(dataOid.lastIndexOf('-')+1,dataOid.length):'';
              dataOid = null!=dataOid?dataOid.replace('ORGAN-',''):'';
              formData.organOids[i] = dataOid;
            }
            for(var i=0; i<formData.userOids.length; i++){
              let  dataOid = formData.userOids[i];
              //dataOid = null!=dataOid?dataOid.substring(dataOid.lastIndexOf('-')+1,dataOid.length):'';
              dataOid = null!=dataOid?dataOid.replace('USER-',''):'';
              formData.userOids[i] = dataOid;
            }
            send(formData).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.attaOids = [];
                this.openInit = false;
                this.getList();
              }
            });
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const id = row.id;
        this.$confirm('是否确认删除吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return del(id);
        }).then(() => {
          this.getList();
          if('undefined' != typeof (this.barFlag)){
            // 将子组件与父组件联合起来
            this.$emit('father-click');
          }
          this.msgSuccess("删除成功");
        }).catch(function() {
        });
      },
      //查看已发送列表
      handleSendPage(){
        this.openSendView = true;
      },
      //删除附件
      handleAttaDelete(index, row) {
        this.messageFileList.splice(index,1);
        this.attaOids.splice(index,1);
      },
      //下载附件
      downloadFile(attaOid){
        this.download(attaOid);
      },
      //成功后返回
      uploadSuccess(resp){
        this.fileList=[];
        if(200 !== resp.code){
          return this.msgError(resp.message);
        }
        const filem = {
          'oid':resp.data.oid,
          'name':resp.data.name,
          'size':resp.data.size,
          'url':resp.data.url
        };
        this.attaOids.push(resp.data.oid);
        this.messageFileList.push(filem)
      },
      //失败后返回
      uploadError(resp){
        this.msgError("文件上传失败");
      },
      //上传之前
      beforeUpload(file) {
        if(file.name.indexOf("%00") > -1 || file.name.indexOf("./") > -1 || file.name.indexOf(".\\") > -1 ) {
          this.msgError('上传文件名称非法！');
          return false;
        }
        const fileSize = file.size;
        if (0 == fileSize) {
          this.msgError('上传文件不能为空！');
          return false;
        }
        const isLt2M = file.size / 1024 / 1024 < 100;
        if (!isLt2M) {
          this.msgError('上传文件大小不能超过 100MB！');
        }
        return isLt2M;
      },
      uploadUrl(){
        return uploadFile();
      }
    }
  };
</script>
<style scoped>
  textarea{
    min-height: 100px !important;
  }
  table {
    border-collapse: separate !important;
  }
  .treeselect{
    width: 100% !important;
  }
</style>
