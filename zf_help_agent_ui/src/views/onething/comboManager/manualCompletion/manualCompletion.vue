<!--author:ningzz-->
<template>
  <div class="app-container">

    <!-- 首页 -->
    <el-row :gutter="20">
      <el-col :span="24" :xs="24">

        <!-- 过滤表单 -->
        <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="70px">
          <el-form-item label="办件编号" prop="caseNumber">
            <div class="card-item">
              <el-input
                v-model.trim="queryParams.caseNumber"
                placeholder="请输入办件编号"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              >
                <!-- <template slot="append"
                ><el-button
                  class="scan-btn"
                  type="primary"
                  @click="openScan"
                ></el-button
                ></template> -->
              </el-input>
            </div>
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
        <el-table v-loading="loading" :data="comboCaseList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>

          <el-table-column label="一件事目录名称" align="center" prop="comboDirectoryName" :show-overflow-tooltip="true"/>
          <el-table-column label="办件编号" align="center" prop="caseNumber" :show-overflow-tooltip="true"/>
          <el-table-column label="项目名称" align="center" prop="projectName" :show-overflow-tooltip="true"/>
          <el-table-column label="申请人" align="center" prop="applyUserName" :show-overflow-tooltip="true"/>
          <el-table-column label="登记日期" align="center" prop="createDate" :show-overflow-tooltip="true"/>
          <el-table-column label="办件状态" align="center" prop="caseStatus" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <p  v-if="scope.row.caseStatus == 1">待预审</p>
              <p  v-if="scope.row.caseStatus == 9">预审不通过</p>
              <p v-if="scope.row.caseStatus == 2">办理中</p>
              <p  v-if="scope.row.caseStatus == 3">办结</p>
              <p  v-if="scope.row.caseStatus == -1">已作废</p>
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

    <!-- 一件事办件查看开始 -->
    <el-dialog v-dialog-drag :visible.sync="caseOpenView" v-if="caseOpenView" @close="closeOpenView" :title="title" width="1100px" height='700px' scrollbar append-to-body>

        <view-combo-case-new :caseOid="indexCaseOid" @view-case="closeOpenView"></view-combo-case-new>
        <div slot="footer" class="zf-text-center">
        <el-button @click="closeOpenView">关 闭</el-button>
      </div>

    </el-dialog>
    <!--一件事办件查看结束-->

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
            <el-table-column label="事项名称" align="center" prop="serviceName" :show-overflow-tooltip="true"/>
            <el-table-column label="事项类型" align="center" prop="serviceTypeName" :show-overflow-tooltip="true"/>
            <el-table-column label="办件编号" align="center" prop="caseNumber" :show-overflow-tooltip="true"/>
            <el-table-column label="办件状态" align="center" prop="caseStatus">
              <template slot-scope="scope">
                <p v-if="scope.row.caseStatus==1">待预审</p>
                <p v-if="scope.row.caseStatus==2">办理中</p>
                <p v-if="scope.row.caseStatus==3">办结</p>
              </template>
            </el-table-column>
            <el-table-column label="应办结时间" align="center" prop="shouldConcludeDate" :show-overflow-tooltip="true"/>
            <el-table-column label="办结时间" align="center" prop="concludeDate" :show-overflow-tooltip="true"/>
            <el-table-column label="操作" align="center" width='150'  class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleChildCaseInfo(scope.row)" v-hasPermi="['sys:done:view']" >查看</el-button>
                <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInitChild(scope.row)" v-hasPermi="['sys:manual:init']">办结</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button @click="initcancel">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 关联办件查看弹窗 -->
    <el-dialog v-dialog-drag :visible.sync="childCaseOpenView" v-if="childCaseOpenView" @close="closeChildView"   :title="title" width="1100px" height='700px' scrollbar append-to-body>

        <view-case-info-by-one-case :caseNumber="indexCaseNumber" @view-case="closeChildView"></view-case-info-by-one-case>
        <div slot="footer" class="zf-text-center">
        <el-button @click="closeChildView">关 闭</el-button>
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

  </div>
</template>

<script>
import {page,get, saveOrUpdate, checkedCase} from "@/api/onething/comboManager/manualCompletion/manualCompletion";
import viewCaseInfoByOneCase from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfoByOneCase";
import {uploadFile} from "@/api/sys/atta";
import ViewComboCaseNew from "@/views/onething/comboManager/comboAccept/viewComboCaseNew";
import {activeScanningGun, openScanningGun} from "@/api/zc/businessManagement/charge";
export default {
  name: "ManualCompletion",
  components: {ViewComboCaseNew, viewCaseInfoByOneCase},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      comboCaseList: [],
      // 弹出层标题
      title: "",
      // 办件查看
      caseOpenView: false,
      indexCaseOid: '',
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
      comboCaseId: "",
      // 关联办件查看
      childCaseOpenView: false,
      indexCaseNumber:"",
      comboDireOid:"",//办件目录主键
      serviceOid:"",//子办件的事项材料
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
        this.comboCaseList = response.data.data;
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
    /** 办件查看按钮操作 */
    handleView(row) {
      this.indexCaseOid = row.caseOid;
      this.caseOpenView = true;
      this.title = "一件事办件信息";
    },
    /** 办件查看关闭按钮操作 */
    closeOpenView() {
      this.caseOpenView = false;
    },

    /** 列表办结按钮操作 */
    handleInit(row){
      this.comboDireOid=row.comboDireOid;
      this.comboCaseId = row.id;
      this.comboCaseOid = row.caseOid;
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
      this.serviceOid=row.serviceOid;
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
    /** 关联办件查看按钮操作 */
    closeChildView() {
      this.childCaseOpenView = false;
    },
    // 提交按钮
    submitForm: function() {
      let _that = this;
      if(_that.childForm.finalStatus!=44){
        //验证一件事办件是否存在补齐补正和容缺后补材料未补
        checkedCase(_that.comboCaseOid).then(response => {
          if (response.code === 200) {
            if(response.data == false){
              _that.$message.error('存在补齐补正或容缺后补材料未补');
              return false;
            }else{
              _that.saveManualInfo();
            }
          }
        });
      }else{////不予批准
        _that.saveManualInfo();
      }
    },
    saveManualInfo(){
      let _that=this;
      _that.childForm.attaOid =  this.attaOids.join(',');
      this.$refs["childForm"].validate(valid => {
        if (valid) {
          saveOrUpdate(_that.childForm).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("办结成功");
              _that.messageFileList=[];
              _that.attaOids=[];
              _that.addDialogShow = false;
              setTimeout(() => {
                _that.handleInitRest(this.comboCaseId)
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
    },
    //打开扫码枪
    openScan() {
      openScanningGun().then(res => {
        if (res.StateCode == 0) {
          //成功后激活扫码枪
          activeScanningGun()
            .then(response => {
              if (response.StateCode == 0) {
                this.$message.success("扫描成功");
                this.$set(this.queryParams,'caseNumber', response.data || '')
               // this.queryParams.caseNumber = response.data;
              } else if (response.StateCode == -1) {
                this.$message.error("扫码枪扫描超时");
              } else if (response.StateCode == -2) {
                this.$message.error("扫码枪没有打开");
              }
            })
            .catch(err => {
              console.log(err);
            });
        } else if (res.StateCode == -1) {
          this.$message.error(res.tips);
        } else if (res.StateCode == -4) {
          //扫码枪已打开
          activeScanningGun()
            .then(response => {
              if (response.StateCode == 0) {
                this.$message.success("扫描成功");
               // this.queryParams.caseNumber = response.data;
                this.$set(this.queryParams,'caseNumber', response.data || '')
              } else if (response.StateCode == -1) {
                this.$message.error("扫码枪扫描超时");
              } else if (response.StateCode == -2) {
                this.$message.error("扫码枪没有打开");
              }
            })
            .catch(err => {
              console.log(err);
            });
        } else {
          this.$message.error("加载配置失败");
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.card-item {
  .el-form-item {
    position: relative;
  }
  .el-input {
    margin-bottom: 0px;
    border-radius: 3px;
    &:last-child {
      margin-bottom: 0;
    }
  }
}
.scan-btn {
  position: absolute;
  height: 30px;
  background: #c1d0d9 url(~@/assets/image/scan-icon.png) no-repeat center center !important;
  border: none;
  box-shadow: none;
  top: 10px;
  border-radius: 1px;
}
</style>
