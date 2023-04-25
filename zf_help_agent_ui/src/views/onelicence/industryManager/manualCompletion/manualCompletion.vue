<!--author:ningzz-->
<template>
  <div class="app-container">

    <!-- 首页 -->
    <el-row :gutter="20">
      <el-col :span="24" :xs="24">

        <!-- 过滤表单 -->
        <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="70px">
          <el-form-item label="办件编号">
            <el-input v-model.trim="queryParams.caseNumber" placeholder="请输入办件编号" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="申请人" prop="name">
            <el-input v-model.trim="queryParams.applyUserName" placeholder="请输入申请人名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="登记日期">
            <!--<el-date-picker
              v-model="queryParams.caseStartDate"
              type="date" value-format="yyyy-MM-dd"
              placeholder="请选择开始时间">
            </el-date-picker>
            -
            <el-date-picker
              v-model="queryParams.caseEndDate"
              type="date" value-format="yyyy-MM-dd"
              placeholder="请选择结束时间">
            </el-date-picker>-->
            <el-date-picker
              v-model="queryParams.caseStartDate"
              type="date" value-format="yyyy-MM-dd"
              :picker-options="pickerOptionsStart"
              placeholder="请选择开始日期">
            </el-date-picker> -
            <el-date-picker
              v-model="queryParams.caseEndDate"
              type="date" value-format="yyyy-MM-dd"
              :picker-options="pickerOptionsEnd"
              placeholder="请选择结束日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>

        <!-- 搜索列表 -->
        <el-table v-loading="loading" :data="industryCaseList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>

          <el-table-column label="业态目录名称" align="center" prop="comboDirectoryName" :show-overflow-tooltip="true"/>
          <el-table-column label="办件编号" align="center" prop="caseNumber" :show-overflow-tooltip="true"/>
          <el-table-column label="项目名称" align="center" prop="projectName" :show-overflow-tooltip="true"/>
          <el-table-column label="申请人" align="center" prop="applyUserName" :show-overflow-tooltip="true"/>
          <el-table-column label="登记日期" align="center" prop="createDate" :show-overflow-tooltip="true"/>
          <el-table-column label="办件状态" align="center" prop="caseStatus" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <p  v-if="scope.row.caseStatus==1">待预审</p>
              <p v-if="scope.row.caseStatus==2">办理中</p>
              <p  v-if="scope.row.caseStatus==3">办结</p>
              　　　　　　　　</template>
          </el-table-column>

          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)" v-hasPermi="['sys:manual:view']" >查看</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInit(scope.row)" v-hasPermi="['sys:manual:init']">办结</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>

    <!--&lt;!&ndash; 列表查看弹窗 &ndash;&gt;
    <el-dialog :title="title" :visible.sync="openView" :close-on-click-modal="false" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <el-form ref="info" :model="form.info" label-width="0px">
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>业态目录名称：</b></td>
              <td>
                {{form.info.comboDirectoryName}}
              </td>
              <td><b>办件编号：</b></td>
              <td>
                {{form.info.caseNumber}}
              </td>
            </tr>
            <tr>
              <td><b>申请人名称：</b></td>
              <td>{{form.info.applyUserName}}</td>
              <td><b>申请人邮政编码：</b></td>
              <td>{{form.info.applyPostCode}}</td>
            </tr>
            <tr>
              <td><b>证件号码：</b></td>
              <td>{{form.info.credentialNumber}}</td>
              <td><b>申请人地址：</b></td>
              <td>{{form.info.applyUserAddress}}</td>
            </tr>
            <tr>
              <td><b>申请人电话：</b></td>
              <td>{{form.info.applyUserTel}}</td>
              <td><b>申请人手机：</b></td>
              <td>{{form.info.applyUserPhone}}</td>
            </tr>
            <tr>
              <td><b>法定代表人：</b></td>
              <td colspan="3">{{form.info.legalPersonName}}</td>
            </tr>
            <tr>
              <td><b>收件人姓名：</b></td>
              <td>{{form.info.addresseeName}}</td>
              <td><b>收件人邮政编码：</b></td>
              <td>{{form.info.addresseePostCode}}</td>
            </tr>
            <tr>
              <td><b>收件人手机：</b></td>
              <td>{{form.info.addresseePhone}}</td>
              <td><b>收件人电话：</b></td>
              <td>{{form.info.addresseeTel}}</td>
            </tr>
            <tr>
              &lt;!&ndash;<td><b>收件人地址：</b></td>
              <td>{{form.info.addresseeAddress}}</td>&ndash;&gt;
              <td><b>收件人详细地址：</b></td>
              <td colspan="3">{{form.info.addresseeDetailAddress}}</td>
            </tr>
            <tr>
              <td><b>联系人名称：</b></td>
              <td>{{form.info.contactUserName}}</td>
              <td><b>联系人身份证号码：</b></td>
              <td>{{form.info.contactCredentialNumber}}</td>
            </tr>
            <tr>
              <td><b>联系人手机：</b></td>
              <td>{{form.info.contactUserPhone}}</td>
              <td><b>联系人电话：</b></td>
              <td>{{form.info.contactUserTel}}</td>
            </tr>
            <tr>
              <td><b>联系人EMAIL：</b></td>
              <td>{{form.info.contactEmail}}</td>
              <td><b>联系人备注：</b></td>
              <td>{{form.info.contactRemark}}</td>
            </tr>
            <tr>
              <td><b>所属业务管辖地：</b></td>
              <td>{{form.info.bussVenueDistrictOid}}</td>
              <td><b>受理具体地点：</b></td>
              <td>{{form.info.specificLocation}}</td>
            </tr>
            <tr>
              <td><b>项目名称：</b></td>
              <td>{{form.info.projectName}}</td>
              <td><b>修改时间：</b></td>
              <td>{{form.info.updateDate}}</td>
            </tr>
            &lt;!&ndash;<tr>
              <td><b>办件状态：</b></td>
              <td>
                <p v-if="form.info.caseStatus==1">待预审</p>
                <p v-if="form.info.caseStatus==2">办理中</p>
                <p v-if="form.info.caseStatus==3">办结</p>
              </td>
            </tr>&ndash;&gt;
            <tr>
              <td><b>投资项目编号：</b></td>
              <td>{{form.info.investProjectCode}}</td>
              <td><b>投资项目名称：</b></td>
              <td>{{form.info.investProjecName}}</td>
            </tr>
            <tr>
              <td><b>办件摘要：</b></td>
              <td>{{form.info.projectAbstract}}</td>
              <td><b>寄送日期：</b></td>
              <td>{{form.info.sendDate}}</td>
            </tr>
            &lt;!&ndash;<tr>
              <td><b>主办单位：</b></td>
               <td>{{form.info.belongsystem}}</td>
              <td><b>送达方式：</b></td>
              <td>{{form.info.resultDeliveryWay}}</td>
            </tr>&ndash;&gt;
            <tr>
              <td><b>送达状态：</b></td>
              <td>{{form.info.investProjectCode===1 ? '是' : '否'}}</td>
              <td><b>是否代理人申请：</b></td>
              <td>{{form.info.proxyFlag===1 ? '是' : "否"}}</td>
            </tr>
            <tr>
              <td><b>受理日期：</b></td>
              <td>{{form.info.acceptanceDate}}</td>
              <td><b>是否受理：</b></td>
              <td>{{form.info.ifAccept===1 ? '是' : '否'}}</td>
            </tr>
            <tr>
              <td><b>受理意见：</b></td>
              <td colspan="3">{{form.info.acceptOpinionDesc}}</td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>-->

    <!-- 列表办结弹窗 -->
    <el-dialog v-dialog-drag :title="title" :visible.sync="initOpenView" :close-on-click-modal="false" width="1100px" append-to-body>
      <div>
        <el-form :model="form">
          <el-table v-loading="loading" :data="form.qlCases" border>
            <el-table-column label="序号" header-align="cernter" width="70" type="index" align="center">
              <template slot-scope="scope">
                <span>{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <!--<el-table-column label="办件编号" align="center" prop="caseNumber"/>-->
            <el-table-column label="事项名称" align="center" prop="serviceName" show-overflow-tooltip/>
            <el-table-column label="事项类型" align="center" prop="serviceTypeName" show-overflow-tooltip/>
            <el-table-column label="办件状态" align="center" prop="caseStatus" show-overflow-tooltip>
              <template slot-scope="scope">
                <p v-if="scope.row.caseStatus==1">待预审</p>
                <p v-if="scope.row.caseStatus==2">办理中</p>
                <p v-if="scope.row.caseStatus==3">办结</p>
              </template>
            </el-table-column>
            <el-table-column label="应办结时间" align="center" prop="shouldConcludeDate" show-overflow-tooltip/>
            <el-table-column label="办结时间" align="center" prop="concludeDate" show-overflow-tooltip/>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleChildCaseInfo(scope.row)" v-hasPermi="['sys:done:view']" >查看</el-button>
                <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInitChild(scope.row)" v-hasPermi="['sys:manual:init']">办结</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
      </div>
      <div slot="footer" class="zf-text-center">
        <!--<el-button @click="initOpenView = false">关 闭</el-button>-->
        <el-button @click="initcancel">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 关联办件办结弹窗 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" v-if="addDialogShow" :visible.sync="addDialogShow" width="1100px" height='700px' scrollbar append-to-body>
      <div>
        <el-form ref="childForm" :model="childForm" :rules="rules" label-width="0px" class="demo-ruleForm">
          <table cellspacing="0" cellpadding="0" border="0" class='zf-zc-table'>
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>办结状态：</b></td>
              <td colspan="3">
                <el-form-item >
                  <el-radio-group v-model="childForm.finalStatus" @change="handleRowChange">
                    <el-radio :label="40">出证办结</el-radio>
                    <el-radio :label="41">不出证办结</el-radio>
                    <el-radio :label="44">不予批准</el-radio>
                  </el-radio-group>
                  <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(备注:出证办结需要配置结果证照)</span>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>办结意见：</b></td>
              <td colspan="3">
                <el-form-item >
                  <el-form-item prop="finalOpinion">
                    <el-input v-model="childForm.finalOpinion" type="textarea" placeholder="请输入办结意见" maxlength="100" show-word-limit></el-input>
                  </el-form-item>
                </el-form-item>
              </td>
            </tr>

            <tr>
              <td><b>办结意见说明：</b></td>
              <td colspan="3">
                <el-form-item prop="finalOpinionDesc">
                  <el-input v-model="childForm.finalOpinionDesc"
                            type="textarea" placeholder="请输入意见说明"
                            maxlength="200"
                            show-word-limit></el-input>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>办结结果附件：</b></td>
              <td colspan="3">
                <el-form-item prop="iconName">
                  <el-upload
                    class="upload-demo"
                    :action="uploadUrl()"
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
      <!-- <el-row :gutter="10" class="mb8">
        <el-col :span="1.5"> -->
           <div style='margin-bottom:10px'> 附件列表</div>
        <!-- </el-col>
      </el-row> -->
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
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="addDialogShow=false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 子办件查看弹窗 -->
    <el-dialog v-dialog-drag :visible.sync="childCaseOpenView"  v-if="childCaseOpenView" :title="title" width="1100px" height='700px' scrollbar append-to-body>
        <detail-case-view @view-case="closeDetailView" :caseNumber="indexCaseNumber"></detail-case-view>
        <div slot="footer" class="zf-text-center">
        <el-button @click="closeDetailView">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 办件详细信息 -->
    <el-dialog v-dialog-drag :visible.sync="openView" v-if="openView"  :title="title" width="1100px" height="700px" scrollbar append-to-body>
        <view-case-info @view-case="closeView" :caseOid="industryCaseOid"></view-case-info>
        <div slot="footer" class="zf-text-center">
         <el-button @click="closeView">关 闭</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import {page,get, saveOrUpdate} from "@/api/onelicence/industryManager/manualCompletion/manualCompletion";
import viewCaseInfo from "@/views/onelicence/industryManager/industryCaseAccept/viewCaseInfo";
import detailCaseView from "@/views/onelicence/industryManager/industryCaseAccept/detailCaseView";
import {checkedCase} from "@/api/zc/businessManagement/manualCompletion.js";
import {uploadFile} from "@/api/sys/atta";
export default {
  name: "manualCompletion",
  components: {viewCaseInfo,detailCaseView},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      industryCaseList: [],
      // 弹出层标题
      title: "",
      // 列表查看弹窗
      openView: false,
      comboCaseOid:'',
      // 列表查看弹窗参数
      form: { info: {}, qlCases: [] },
      // 列表办结弹窗
      initOpenView: false,
      // 办件办结弹窗
      addDialogShow: false,
      childForm: {caseOid:'',finalStatus:40,finalOpinion:"",finalOpinionDesc:'',attaOid:""},
      rules: {
        finalOpinion: [
          { required: true, message: "办结意见不能为空", trigger: "blur" }
        ]
      },
      attaOids:[],
      messageFileList:[],
      fileList:[],
      industryCaseOid: "",
      // 关联办件查看
      childCaseOpenView: false,
      indexCaseNumber:"",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseStartDate:'',
        caseEndDate:''
      },
      pickerOptionsStart: {
        disabledDate: time => {
          const endDateVal = new Date(this.queryParams.caseEndDate).getTime()
          if (endDateVal) {
            return time.getTime() > endDateVal - 0
          }
        }
      },
      pickerOptionsEnd: {
        disabledDate: time => {
          const beginDateVal =new Date( (new Date(this.queryParams.caseStartDate)-24*60*60*1000)).getTime()
          if (beginDateVal) {
            return time.getTime() < beginDateVal-0
          }
        }
      },

    };
  },
  created() {
    this.getList();
  },
  methods: {

    handleRowChange(data) {
    },
    /** 查询已办业务列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.industryCaseList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    /** 重置按钮操作 */
    resetQuery() {
      // this.resetForm("queryForm");
      this.queryParams.applyUserName='';
      this.queryParams.caseNumber='';
      this.queryParams.caseEndDate=null;
      this.queryParams.caseStartDate=null;
      this.handleQuery()
    },

    /** 列表查看按钮操作 */
    handleView(row) {
      this.industryCaseOid=row.caseOid;
      this.openView = true;
      this.title = '业态信息';
      /*get(row.id).then(response => {
        this.form.info = response.data

      })*/
    },
    // 关闭按钮
    closeView() {
      let _that = this;
      _that.openView= false;
      _that.getList();
    },
    closeDetailView(){
      let _that = this;
      _that.childCaseOpenView= false;
    },
    /** 列表办结按钮操作 */
    handleInit(row){
      this.industryCaseId = row.id;
      get(row.id).then(response => {
        this.form.info = response.data
        this.form.qlCases = response.data.qlCases
        this.initOpenView = true
        this.title = '未办结子办件列表'
      })
    },
    /** 关闭列表办件弹窗 */
    initcancel() {
      this.initOpenView = false;
      this.resetQuery();
    },
    /** 关联办件办结按钮操作 */
    handleInitChild(row) {
      if(row.caseOid) {
        this.childForm.caseOid=row.caseOid;
        this.childForm.finalStatus=40;
        this.childForm.finalOpinionDesc="";
        this.childForm.finalOpinion="";
        this.addDialogShow = true;
      } else {
        this.addDialogShow = true;
      }
      this.title ="办结";
    },
    /** 关联办件查看按钮操作 */
    handleChildCaseInfo(row) {
      this.indexCaseNumber= row.caseNumber;
      this.childCaseOpenView = true;
      this.title = "办件信息";
    },
    // 提交按钮
    submitForm: function() {
      let _that = this;
      if(_that.childForm.finalStatus == "44"){
        _that.save();
      }else{
        //验证是否存在补齐补正和容缺后补材料未补
        checkedCase(_that.childForm.caseOid).then(response => {
          if (response.code === 200) {
            if(response.data){
              _that.$message.error(response.data);
              return false;
            }else{
              _that.save();
            }
          }
        });
      }
    },
    save(){
      let _that = this;
      _that.childForm.attaOid =  this.attaOids.join(',');
      this.$refs["childForm"].validate(valid => {
        if (valid) {
          saveOrUpdate(_that.childForm).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("办结成功");
              _that.messageFileList=[];
              _that.addDialogShow = false;
              setTimeout(() => {
                _that.handleInitRest(this.industryCaseId)
              }, 10);
            }
          });
        }
      });
    },
    handleInitRest(caseOid){
      get(caseOid).then(response => {
        this.initOpenView = false
        this.form.info = response.data
        this.form.qlCases = response.data.qlCases
        this.initOpenView = true
        this.title = '未办结子办件列表'
      })
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
  },
};
</script>
