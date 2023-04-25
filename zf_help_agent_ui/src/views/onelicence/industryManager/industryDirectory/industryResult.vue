/**
* @Author: wangxl
*/
<template>
  <div>
    <div class="el-table__header-wrapper dialog-table">
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <el-input v-show="false" v-model="form.id"/>
          <el-input v-show="false" v-model="form.comboDirectoryOid"/>
          <el-input v-show="false" v-model="form.results"/>
          <el-input v-show="false" v-model="form.status"/>
          <el-input v-show="false" v-model="form.delFlag"/>
          <el-input v-show="false" v-model="form.createUser"/>
          <colgroup>
            <col width="20%"/>
            <col width="30%"/>
            <col width="20%"/>
            <col width="30%"/>
          </colgroup>
          <tr>
            <td>
              <b>证照名称：</b>
            </td>
            <td>
              <el-form-item prop="resultName">
                <el-input v-model.trim="form.resultName" placeholder="请输入证照名称" maxlength="50" show-word-limit/>
              </el-form-item>
            </td>
            <td>
              <b>是否综合许可证：</b>
            </td>
            <td>
              <el-form-item>
                <el-radio-group v-model="formStatus" @change="changeStatus">
                  <el-radio :label="0">否</el-radio>
                  <el-radio :label="1">是</el-radio>
                </el-radio-group>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td>
              <b>证照结果样表：</b>
            </td>
            <td colspan="3">
              <el-form-item>
                <el-input v-show="false" v-model="form.resultSampleAddr"/>
                <el-input v-show="false" v-model="form.resultSampleName"/>
                <el-button type="success" size="mini" @click="selectAttas">结果样本附件</el-button>
                <el-button type="danger" size="mini" @click="clearAtta">清理</el-button>
                <div v-show="null!=form.resultSampleAddr && ''!=form.resultSampleAddr">
                  <span>{{form.resultSampleName}}</span>
                  <el-link type="primary" @click="downloadFile(form.resultSampleAddr)">下载</el-link>
                  |
                  <el-link type="primary" @click="viewFile(form.resultSampleAddr)">预览</el-link>
                </div>
              </el-form-item>
            </td>
          </tr>
        </table>

        <el-button type="primary" @click="addHtml" class="add-btn">增加</el-button>
        <table cellspacing="0" cellpadding="0" border="0" class="tc">
          <colgroup>
            <col width="50"/>
            <col width="250"/>
            <col width="250"/>
            <col width="250"/>
            <col />
          </colgroup>
          <tr>
            <th>序号</th>
            <th>要素名称</th>
            <th>要素标识</th>
            <th>要素字段类型</th>
            <th>操作</th>
          </tr>
          <template v-for="(ruleForm,index) in form.subList">
            <template v-if="ruleForm.delFlag===0 ">
              <tr>
                <td>
                  <el-form-item prop="xuhao">
                    {{index + 1}}
                  </el-form-item>
                </td>
                <td>
                  <el-form-item :prop="'subList.'+ index +'.elementName'" :rules="rules.elementName">
                    <el-input v-model.trim="ruleForm.elementName"></el-input>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item>
                    <el-input v-model.trim="ruleForm.elementCode" placeholder="自动生成" v-bind:disabled="true"></el-input>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item :prop="'subList.'+ index +'.elementType'" :rules="rules.elementType">
                    <el-select v-model="ruleForm.elementType" placeholder="要素字段类型">
                      <el-option v-for="(value, key) in LICE_OCR_TYPE_MAP" :key="key" :label="value" :value="key"/>
                    </el-select>
                  </el-form-item>
                </td>
                <!-- <td>
             <el-form-item>
               <el-input v-model="ruleForm.sealsNumber"></el-input>
             </el-form-item>
           </td> -->

                <td>
                  <el-form-item>
                    <!--  <template slot-scope="scopew">-->
                    <el-button style="border: 0px;" icon="el-icon-delete" @click="delHtml(index)"
                               class="handle-btn"></el-button>
                    <!-- </template>-->
                  </el-form-item>
                </td>
              </tr>
            </template>
          </template>
        </table>
        <el-button type="primary" class="add-btn" v-if="formStatus == '1'">综合许可证固有要素</el-button>
        <table v-if="formStatus == '1'" cellspacing="0" cellpadding="0" border="0" class="tc">
          <colgroup>
            <col width="50"/>
            <col/>
            <col/>
            <col/>
          </colgroup>
          <tr>
            <th>序号</th>
            <th>要素名称</th>
            <th>要素标识</th>
            <th>要素字段类型</th>
          </tr>
          <template v-for="(ruleForm,index) in form.zongheList">
            <template v-if="ruleForm.delFlag===0 ">
              <tr>
                <td>
                  <el-form-item prop="xuhao">
                    {{index + 1}}
                  </el-form-item>
                </td>
                <td>
                  <el-form-item :prop="'zongheList.'+index +'.elementName'" :rules="rules.elementName">
                    <el-input v-model.trim="ruleForm.elementName" v-bind:disabled="true"></el-input>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item>
                    <el-input v-model.trim="ruleForm.elementCode" placeholder="自动生成" v-bind:disabled="true"></el-input>
                  </el-form-item>
                </td>
                <td>
                  <el-form-item :prop="'zongheList.'+index +'.elementType'" :rules="rules.elementType"
                                v-bind:disabled="true">
                    <el-select v-model="ruleForm.elementType" placeholder="要素字段类型" v-bind:disabled="true">
                      <el-option v-for="(value, key) in LICE_OCR_TYPE_MAP" :key="key" :label="value" :value="key"/>
                    </el-select>
                  </el-form-item>
                </td>
                <!-- <td>
             <el-form-item>
               <el-input v-model="ruleForm.sealsNumber"></el-input>
             </el-form-item>
           </td> -->


              </tr>
            </template>
          </template>
        </table>


      </el-form>
    </div>
    <div slot="footer" class="zf-text-center">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
    <!-- 选择配置项附件view-->
    <el-dialog v-dialog-drag title="选择结果样本附件" :visible.sync="openAttaListView" width="1100px" height="700px" scrollbar append-to-body>
      <div>
        <!-- <el-row :gutter="10" class="mb8">
          <el-col :span="1.5"> -->
            <el-upload
              class="upload-demo"
              :action="uploadUrl()"
              :on-error="uploadError"
              :file-list="fileList"
              :on-success="uploadSuccess">
              <el-button size="small" type="primary">点击上传<i class="iconfont zfsoft-wenjianshangchuan"></i></el-button>
            </el-upload>
          <!-- </el-col>
        </el-row> -->

        <el-table v-loading="loading" :data="attaList">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="原始文件名" align="center" prop="originName"/>
          <el-table-column label="文件名" align="center" prop="name"/>
          <el-table-column label="上传时间" align="center" prop="uploadDate" width="180"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-link type="primary" @click="selectFile(scope.row.attaOid,scope.row.originName)">选择</el-link>
              |
              <el-link type="primary" @click="downloadFile(scope.row.attaOid)">下载</el-link>
              |
              <el-link type="primary" @click="viewFile(scope.row.attaOid)">预览</el-link>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="attatotal>0"
          :total="attatotal"
          :page.sync="attaQueryParams.pageNum"
          :limit.sync="attaQueryParams.pageSize"
          @pagination="getAttaList"
        />
      </div>
    </el-dialog>
    <!--引入文件的预览弹出层-->
    <el-dialog v-dialog-drag title="文件预览" :visible.sync="fileShow"  width="900px" height='500px' scrollbar @close="closeFileView" append-to-body>
      <combo-dire-file-view :attaOid="attaOid" v-if="fileShow"></combo-dire-file-view>
    </el-dialog>
  </div>
</template>

<script>
import {saveIndustryResult, getIndustryResultsByResultOid,getIndustryCommonResultsElements} from "@/api/onelicence/industryManager/industryResult";
import {pageFile, uploadFile, downloadFile} from "@/api/onething/sxpz/comboAtta";
import comboDireFileView from "@/views/onething/sxpz/comboDirectory/comboDireFileView";

export default {
  components: {comboDireFileView},
  name: "ServiceResult",
  //定义获取父类传过来值的格式
  props: ["comboDireOid", "resultOid"],
  data() {
    return {
      // 遮罩层
      loading: true,
      attatotal: 0,
      fileShow: false,
      comboDirectoryOid: "",
      //子项
      subList: [],
      zongheList: [],
      LICE_OCR_TYPE_MAP: {'chinese': '中文', 'english': '字母', 'int': '数字'},
      //业态要素
      industryElement: {},
      multipleSelection: [],
      // 表单参数
      form: {
        resultName: '',
      },
      formStatus :0,
      rules: {
        resultName: [
          {required: true, message: "必填项", trigger: "blur"},
          {min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur"},
        ],
        elementName: [
          {required: true, message: "必填项", trigger: "blur"},
          {min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur"},
        ],
        elementType: [
          {required: true, message: "必填项", trigger: "blur"}
        ],

      },
      checkList: [],
      fileList: [],
      openAttaListView: false,
      //附件参数
      attaQueryParams: {
        pageNum: 1,
        pageSize: 10
      },
      //附件列表
      attaList: [],
      attaOid: ""
    };
  },
  watch: {},
  created() {
    //统一结果详细信息

    this.initDirectoryResult();


  },
  methods: {
    // 取消按钮
    cancel() {
      this.reset();
      this.$emit('dialog-close');
    },
    // 表单重置
    reset() {
      this.resetForm("form")
    },

    //添加子项模块
    addHtml: function () {
      this.form.subList.push({
        delFlag: 0,

      })
    },
    //删除子项模块
    delHtml: function (index) {
      this.industryElement = {};
      this.industryElement = this.form.subList[index];
      this.industryElement.delFlag = 1;
      this.form.subList.splice(index, 1);
      if (this.industryElement.elementOid != undefined) {
        this.form.subList.push(this.industryElement);
      }
      //this.form.subList.splice(index,1);
    },
    changeStatus(val){
      if(val =="1"){
        getIndustryCommonResultsElements().then(response => {
          this.form.zongheList =response.data;
        })
      }
    },
    //获取统一结果详细
    initDirectoryResult() {
      this.reset();
      if (this.resultOid) {
        getIndustryResultsByResultOid(this.resultOid).then(response => {
          if (response.data.industryResult != undefined) {
            this.form = response.data.industryResult;
            if(this.form.status ==1 ){
             this.formStatus = this.form.status;
              getIndustryCommonResultsElements().then(response => {
                this.form.zongheList =response.data;
              })
            }
          }
          this.dialogTitle = this.resultOid ? "修改" : "新增";
        });
      } else {
        this.form = {
          id: '',
          resultOid: '',
          zongheList: [],
          subList: [],
        };
        this.dialogTitle = this.resultOid ? "修改" : "新增";
      }
    },
    /** 提交按钮 */
    submitForm: function () {
      this.form.comboDirectoryOid = this.comboDireOid;
      this.form.status = this.formStatus;
      this.$refs["form"].validate(valid => {
        if (valid) {

          saveIndustryResult(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.reset();
              this.$emit('dialog-close');
            }
          });
        }
      });
    },

    //选择配置项附件
    selectAttas() {
      this.getAttaList();
      this.openAttaListView = true;
    },
    /** 查询附件列表 */
    getAttaList() {
      pageFile(this.attaQueryParams).then(response => {
        this.attaList = response.data.data;
        this.attatotal = response.data.total;
        this.loading = false;
      });
    },
    //选中附件
    selectFile(attaOid, attaName) {
      console.log(attaOid);
      this.form.resultSampleAddr = attaOid;
      this.form.resultSampleName = attaName;
      this.openAttaListView = false;
    },
    //下载附件
    downloadFile(attaOid) { 
      this.loading = true;
      downloadFile(attaOid);
      this.loading = false;
    },
    //预览附件
    viewFile(attaOid) { 
      console.log("attaOid===="+attaOid);
      this.attaOid = attaOid;
      this.fileShow = true;
    },
    //关闭预览附件
    closeFileView() {
      this.attaOid = '';
      this.fileShow = false;
    },
    //清空附件
    clearAtta() {
      this.form.resultSampleAddr = '';
      this.form.resultSampleName = '';
      this.$forceUpdate();
    },
    //成功后返回
    uploadSuccess(resp) {
      this.fileList = [];
      this.getAttaList();
    },
    //失败后返回
    uploadError(resp) {
      this.msgError("文件上传失败");
    },
    uploadUrl() {
      return uploadFile();
    }
  }
};


</script>
<style lang="scss" scoped>
.primary-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}

.primary-table .handle-btn {
  border: none;
  background: none;
  padding: 0;
  font-size: 16px;
}

.primary-table .el-table,
.primary-table .el-table th {
  font-size: 13px;
}

.primary-table .el-form-item:first-child {
  margin-left: 8px;
}

.primary-table .el-form-item {
  margin-bottom: 0;
  margin-right: 0;
  margin-left: 0px !important;
  padding: 0;
  box-sizing: border-box;
  font-size: 13px;
}

.primary-table .el-form-item__content {
  font-size: 13px;
}

.primary-table .inputTable .el-form-item:first-child {
  margin-left: 0;
}

.primary-table .inputTable .el-form-item {
  width: 100px;
}

.ruleForm-table table {
  border: 1px solid #ebeef5;
  border-collapse: collapse;
}

.ruleForm-table table tr th {
  color: #515a6e;
  font-size: 13px;
  background-color: #f8f8f9;
  text-align: left;
}

.ruleForm-table table tr th,
.ruleForm-table table tr td {
  padding: 17px 10px;
  font-size: 13px;
  color: #606266;
  border: 1px solid #ebeef5;
}

.ruleForm-table .el-form-item {
  margin-left: 0px !important;
  width: 120px;
}

.dialog-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}

.dialog-table table {
  width: 100%;
}

.dialog-table table {
  border: 1px solid #dfe6ec;
  border-collapse: collapse;
}

.dialog-table table tr td {
  border: 1px solid #dfe6ec;
  padding: 17px 8px;
  font-size: 12px;
  color: #515a6e;
  text-align: right;
}

.dialog-table table tr td:nth-of-type(2n) {
  color: #606266;
  text-align: left;
}

.dialog-table .el-form-item {
  margin-bottom: 0;
}

.el-form-item--small .el-form-item_content {
  margin-left: 0px !important;
}

.line {
  text-align: center;
}

.add-btn {
  margin: 10px 0px;
  float: right;
}

.dialog-table table.tc tr td {
  text-align: center;
}
</style>
