<!--一件事细化材料和审核要点配置-->
<template>
  <div>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['material:split:init']"
        >新增</el-button
        >
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="sxMaterialList" border :fit="true" >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="材料名称" align="center" prop="refinedMaterialName" :show-overflow-tooltip="true"/>
      <el-table-column label="材料类型" align="center" prop="materialType" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.materialType==0">原件</span>
          <span v-if="scope.row.materialType==1">复印件</span>
          <span v-if="scope.row.materialType==2">原件和复印件</span>
        </template>
      </el-table-column>
      <el-table-column label="是否必要" align="center" prop="needStatus" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.needStatus==0">否</span>
          <span v-if="scope.row.needStatus==1">是</span>
        </template>
      </el-table-column>
      <el-table-column label="来源渠道" align="center" prop="materialSource" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.materialSource==0">申请人自备</span>
          <span v-if="scope.row.materialSource==1">政府部门核发</span>
          <span v-if="scope.row.materialSource==2">其他</span>
        </template>
      </el-table-column>
      <el-table-column label="排序" align="center" prop="serialNumber" :show-overflow-tooltip="true"/>
      <el-table-column
        label="操作"
        align="center"
        width="250"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['material:details:view']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['material:details:init']"
          >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDel(scope.row)"
            v-hasPermi="['material:details:del']"
          >删除</el-button
          >

        </template>
      </el-table-column>
    </el-table>

    <!-- 新增修改 -->
    <el-dialog v-dialog-drag :visible.sync="addUpdateFlag" v-if="addUpdateFlag"  :title="addOrUpdateTitle" width="1100px" height='700px' scrollbar append-to-body>

        <div class="zf-zc-table--title">颗粒化材料</div>
        <el-form ref="form" :model="form" :rules="rules" label-width="0px" >
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>材料名称：</b></td>
              <td colspan="3">
                <el-form-item prop="refinedMaterialName">
                  <el-input
                    v-model.trim="form.refinedMaterialName"
                    placeholder="请输入材料名称"
                    maxlength="50"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>材料类型：</b></td>
              <td colspan="3">
                <el-form-item prop="materialType">
                  <el-radio-group v-model="form.materialType">
                    <el-radio :label="0">原件</el-radio>
                    <el-radio :label="1">复印件</el-radio>
                    <el-radio :label="2">原件和复印件</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>

            </tr>
            <tr>
              <td><b>来源渠道：</b></td>
              <td colspan="3">
                <el-form-item prop="materialSource">
                  <el-radio-group v-model="form.materialSource">
                    <el-radio label="0">申请人自备</el-radio>
                    <el-radio label="1">政府部门核发</el-radio>
                    <el-radio label="2">其它</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
<!--              <td><b>是否必要：</b></td>
              <td>
                <el-form-item prop="needStatus">
                  <el-radio-group v-model="form.needStatus">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>-->
              <td><i class="require">*</i><b>排序号：</b></td>
              <td colspan="3">
                <el-form-item prop="serialNumber">
                  <el-input-number
                    v-model="form.serialNumber"
                    :min="1"
                    :max="999"
                  />
                </el-form-item>
                <i class="require">*请注意配置多个材料时，请依次填写序号，例如，第一个材料排序号是1，第二个材料是2</i>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>材料样本：</b></td>
              <td colspan="3">
                <el-form-item prop="materialSampleOid">
                  <el-upload
                    class="upload-demo"
                    action=""
                    :multiple='false'
                    :http-request="uploadFilesFlow"
                    :before-upload="beforeUpload"
                    :on-error="uploadError"
                    :file-list="fileFlowList"
                    :show-file-list="false"
                    accept="accept">
                    <el-button size="mini" type="primary" icon="el-icon-upload">点击上传</el-button>
                  </el-upload>
                </el-form-item>
                <el-form-item v-if="form.materialSampleOid">
                  <span>{{form.materialSampleOriginName}}</span>
                  <el-link type="primary" @click="downLoadFileInfo(form.materialSampleOid)">下载</el-link> |
                  <el-link type="primary" @click="delFlowFile(form.materialSampleOid)">删除</el-link>
                </el-form-item>
              </td>
            </tr>
          </table>
          <el-button type="default" icon="el-icon-plus" size="mini" @click="addHtml" style="float: right;margin-right: 20px;margin-bottom: 10px;">新增</el-button>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table zf-zc-table--common zf-zc-table--td-center">
            <colgroup>
              <col width="10%" />
              <col width="75%" />
              <col width="15%" />
            </colgroup>
            <tr>
              <th align="center">序号</th>
              <th align="center">审查要点</th>
              <th align="center">操作</th>
            </tr>
            <template v-for="(data,index) in form.reviewPointsList">
              <tr>
                <td>
                    <el-input-number v-model.trim="data.serialNumber" :min="1" :max="9999"></el-input-number>
                </td>
                <td>
                    <el-input v-model.trim="data.reviewPoints"></el-input>
                </td>
                <td style='text-align:center'>
                    <el-button style="border: 0px;" icon="el-icon-delete" @click="delHtml(index)" class="handle-btn">删除</el-button>
                </td>
              </tr>
            </template>
          </table>
        </el-form>

      <div slot="footer" class="dialog-footer" style="text-align:center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看细化材料对话框 -->
    <el-dialog v-dialog-drag title="查看颗粒化材料信息" :visible.sync="detailView" v-if="detailView" :close-on-click-modal="false" width="1100px" height='700px' scrollbar append-to-body>
      <div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>材料名称：</b></td>
            <td colspan="3">
              {{form.refinedMaterialName}}
            </td>
          </tr>
          <tr>
            <td><b>材料类型：</b></td>
            <td colspan="3">
              <template v-if="form.materialType=== 0">
                原件
              </template>
              <template v-if="form.materialType=== 1">
                复印件
              </template>
              <template v-if="form.materialType=== 2">
                原件或复印件
              </template>
            </td>

          </tr>
          <tr>
            <td><b>来源渠道：</b></td>
            <td colspan="3">
              <template v-if="form.materialSource=== 0 ||form.materialSource=== '0'">
                申请人自备
              </template>
              <template v-if="form.materialSource=== 1  ||form.materialSource=== '1'">
                政府部门核发
              </template>
              <template v-if="form.materialSource=== 2  ||form.materialSource=== '2'">
                其它
              </template>
            </td>
          </tr>
          <tr>
<!--            <td><b>是否必要：</b></td>
            <td>
              <template v-if="form.needStatus=== 0">
                否
              </template>
              <template v-if="form.needStatus=== 1">
                是
              </template>
            </td>-->
            <td><b>排序号：</b></td>
            <td colspan="3">
              {{form.serialNumber}}
            </td>
          </tr>
          <tr>
            <td><b>材料样本：</b></td>
            <td colspan="3">
              {{form.materialSampleOriginName}}
              <el-link type="primary" @click="downLoadFileInfo(form.materialSampleOid)">下载</el-link>
            </td>
          </tr>
        </table>
      </div>
      <div>
        <el-table v-loading="loading" :data="form.reviewPointsList" border>
          <el-table-column label="序号" align="center" type="index" :show-overflow-tooltip="true"/>
          <el-table-column label="审查要点" align="center" prop="reviewPoints" :show-overflow-tooltip="true"/>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer" style="text-align:center">
        <el-button @click="viewClose">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { pageMaterialSplitList,uploadsxFile,downLoadFile,saveOrUpdateMaterialXh,getRefinedMaterialDetail,deleteMaterialSplit} from "@/api/zc/qdgl/sxService.js";
import {
  saveOrUpdateMaterialSample
} from "@/api/onething/sxpz/comboDirectoryMaterial";
export default {
  name: "refinedMaterials",
  components: {},
  props:["serviceOid","parentMaterialOid","comboDirectoryOid","pubMaterialOid"],
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      sxMaterialList: [],
      fileFlowList:[],//办理流程图

      // 弹出层标题
      title: "",
      // 查看显示弹出层
      detailView: false,
      openInit: false,
      addUpdateFlag:false,
      //simpleMaterialName:"",//样本材料名称
      reviewPoints:{},
      reviewPointsLast:[],
      addOrUpdateTitle: "",
      // 表单参数
      form: {
        comboDirectoryOid:this.comboDirectoryOid,
        materialOid: this.pubMaterialOid,
        materialType:0,
        materialSource:'0',
        needStatus:0,
        reviewPointsList:[],
        refinedMaterialName:"",
        serialNumber:"",
        materialSampleOid:"",
        materialSampleOriginName:"",
      },
      //A4
      SIZE_TYPE_A4_MAP: {},
      rules: {
        refinedMaterialName: [
          { required: true, message: '名称不能为空', trigger: 'blur' }
        ],
        serialNumber: [
          { required: true, message: '排序号不能为空', trigger: 'blur' }
        ],
        materialSampleOid: [
          { required: true, message: '样本不能为空', trigger: 'blur' }
        ],
      }
    };
  },
  computed: {
    reversedSex: function () {
      return this.form.enabledFlag == "1"?'启用':'禁用'
    }

  },
  created () {
    //"comboDirectoryOid","pubMaterialOid"
    //alert("*****"+this.comboDirectoryOid)
    this.getList();
  },
  //获取父页面的值
  mounted () {
    //this.getList();
  },
  methods: {
    //删除
    handleDel (row) {
      const oid = row.id;
      this.$confirm('是否确认删除?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return deleteMaterialSplit(oid);
      }).then(() => {
        this.msgSuccess("删除成功");
        this.getList();
      }).catch(function () { });
    },
    handleInit (row) {
      if(row.oid){
        this.form.oid=row.oid;
      }
      this.form.reviewPointsList = [];
      this.form.id = "";
      this.form.refinedMaterialName = "";
      this.form.materialType = 0;
      this.form.materialSource = "0";
      this.form.needStatus = 0;
      this.form.serialNumber = "";
      this.form.materialSampleOid = "";
      this.form.materialSampleOriginName = "";
      this.form.serialNumber = "1";
      this.addUpdateFlag = true;
      this.reviewPointsLast = [];
      this.addOrUpdateTitle = "新增颗粒化材料"
    },
    handleUpdate (row) {
      this.addUpdateFlag = true;
      this.addOrUpdateTitle = "修改颗粒化材料"
      this.reviewPointsLast = [];
      getRefinedMaterialDetail(row.oid).then(res => {
        if (res.code == 200) {
          this.form.id = res.data.id;
          this.form.oid = res.data.oid;
          this.form.reviewPointsList = res.data.reviewPointsList;
          this.form.refinedMaterialName = res.data.refinedMaterialName;
          this.form.materialType = res.data.materialType;
          this.form.materialSource = res.data.materialSource;
          this.form.needStatus = res.data.needStatus;
          this.form.serialNumber = res.data.serialNumber;
          this.form.materialSampleOid = res.data.materialSampleOid;
          this.form.materialSampleOriginName = res.data.materialSampleOriginName;
        }
      })
    },
    submitForm () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (!(this.form.reviewPointsList.length > 0)) {
            this.form.reviewPointsList = this.reviewPointsLast;
          }
          saveOrUpdateMaterialXh(JSON.stringify(this.form)).then(response=>{
            if(response.code == 200){
              this.msgSuccess("保存成功!");
              saveOrUpdateMaterialSample(this.form.materialOid).then(response => {
                this.loading = false;
              });
              this.addUpdateFlag = false;
              this.getList();
            }else{
              this.$message.error("保存失败！");
            }
          })

        }
      })
    },
    // 取消按钮
    cancel () {
      this.addUpdateFlag = false;
      this.form.id = "";
      this.form.oid = "";
      this.form.reviewPointsList = [];
      this.form.refinedMaterialName = "";
      this.form.materialType = "";
      this.form.materialSource = "";
      this.form.needStatus = "";
      this.form.serialNumber = "";
      this.form.materialSampleOid = "";
      this.form.materialSampleOriginName = "";
    },
    /** 上传附件 */
    uploadFilesFlow (file) {
      let _that = this;
      let formData = new FormData();
      formData.append("file", file.file);
      uploadsxFile(formData).then(respon => {
        if(respon.data){
          _that.form.materialSampleOid=respon.data.oid;
          _that.form.materialSampleOriginName=respon.data.name;
        }else{
          _that.$message.error("上传文件失败！")
        }
      })
    },
    /** 上传附件请求操作 */
    beforeUpload (file) {
      let _that = this;
      if(file.name.indexOf("%00") > -1 || file.name.indexOf("./") > -1 || file.name.indexOf(".\\") > -1 ) {
        _that.$message.error('上传文件名称非法！');
        return false;
      }
      const fileSize = file.size;
      if (0 == fileSize) {
        _that.$message.error('上传文件不能为空！');
        return false;
      }
      let isRightSize = file.size / 1024 / 1024 < 100
      if (!isRightSize) {
        _that.$message.error('文件大小超过 100MB')
      }
      _that.fileFlowList = []
      _that.fileFlowList.push(file)
      return isRightSize
    },
    /** 失败后返回 */
    uploadError (resp) {
      let _that = this;
      _that.msgError("文件上传失败");
    },
    downLoadFileInfo(attaOid){
      downLoadFile(attaOid);
    },
    delFlowFile(){
      this.form.materialSampleOid="";
      this.form.materialSampleOriginName="";
    },
    /** 查询列表 */
    getList () {
      this.loading = true;
      pageMaterialSplitList(this.pubMaterialOid).then(response => {
        this.sxMaterialList = response.data;
        this.loading = false;
      });
    },
    resetSxSituation () {
      var chServiceOid = this.form.serviceOid;
      this.form = {};
      this.resetForm('form');
      this.form.serviceOid = chServiceOid;
    },
    // 表单重置
    reset () {
      Object.assign(this.form, this.$options.data().form)
      this.resetForm("form");
    },
    /** 查看按钮操作 */
    handleView (row) {
      getRefinedMaterialDetail(row.oid).then(res => {
        if (res.code == 200) {
          this.form.reviewPointsList = res.data.reviewPointsList;
          this.form.refinedMaterialName = res.data.refinedMaterialName;
          this.form.materialType = res.data.materialType;
          this.form.materialSource = res.data.materialSource;
          this.form.needStatus = res.data.needStatus;
          this.form.serialNumber = res.data.serialNumber;
          this.form.materialSampleOid = res.data.materialSampleOid;
          this.form.materialSampleOriginName = res.data.materialSampleOriginName;
          this.detailView = true;
        }
      })
    },
    viewClose () {
      this.detailView = false;
      this.form.reviewPointsList = [];
      this.form.refinedMaterialName = "";
      this.form.materialType = "";
      this.form.materialSource = "";
      this.form.needStatus = "";
      this.form.serialNumber = "";
      this.form.materialSampleOid = "";
      this.form.materialSampleOriginName = "";
    },
    //添加子项模块
    addHtml() {
      this.form.reviewPointsList.push({
        id:"",
        Oid:"",
        refinedMaterialOid:"",
        reviewPoints:"",
        serialNumber:"1",
      })
    },
    //删除子项模块
    delHtml(index) {
      this.reviewPoints = {};
      this.reviewPoints = this.form.reviewPointsList[index];
      this.form.reviewPointsList.splice(index, 1);
      if (!(this.form.reviewPointsList.length > 0)) {
        this.reviewPoints.deleteStatus = 1;
        this.reviewPointsLast = [];
        this.reviewPointsLast.push(this.reviewPoints);
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.dialog-table {
  padding: 20px;
  box-sizing: border-box;
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
</style>
