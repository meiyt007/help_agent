<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="图标名称" prop="iconName">
        <el-input
          v-model.trim="queryParams.iconName"
          placeholder="请输入图标名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="图标分类">
        <el-select
          v-model="queryParams.iconTypeCode"
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="(value, key) in iconTypeMap"
            :key="key"
            :label="value"
            :value="key"
          />
        </el-select>
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索
        </el-button
        >
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          class="ml5"
        >重置
        </el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['im:icon:save']"
        >新增
        </el-button
        >
      </el-col>
    </el-row>

    <el-table
      ref="multipleTable"
      :data="iconList"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="图标名称"
        align="center"
        prop="iconName"
        show-overflow-tooltip
      />
      <el-table-column
        label="图标附件"
        align="center"
        prop="fastdfsNginxUrl"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <img width="120px" :src="previewImageUrl+scope.row.fastdfsNginxUrl" />
        </template>
      </el-table-column>
      <el-table-column
        label="图标分类"
        align="center"
        prop="iconTypeCode"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-for="(value,key) in iconTypeMap" :key="key" v-if="scope.row.iconTypeCode==key">{{ value }}</p>
        </template>
      </el-table-column>
      <el-table-column
        label="排序"
        align="center"
        prop="sort"
        show-overflow-tooltip
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['im:icon:view']"
          >查看
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['im:icon:update']"
          >修改
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['im:icon:delete']"
          >删除
          </el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNumber"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改图标信息 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      width="900px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%"/>
            <col width="30%"/>
            <col width="20%"/>
            <col width="30%"/>
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>图标名称：</b></td>
            <td>
              <el-form-item prop="iconName">
                <el-input
                  v-model.trim="form.iconName"
                  placeholder="请输入图标名称"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>图标分类：</b></td>
            <td>
              <el-select
                v-model="form.iconTypeCode"
                size="small"
                style="width: 240px"
              >
                <el-option
                  v-for="(value, key) in iconTypeMap"
                  :key="key"
                  :label="value"
                  :value="key"
                />
              </el-select>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>排序号：</b></td>
            <td colspan="3">
              <el-form-item prop="sort">
                <el-input-number
                  v-model.trim="form.sort"
                  :max="999999999"
                  show-word-limit
                  oninput ="value=value.replace(/[^\d]/g,'')"
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>图标照片：</b></td>
            <td colspan="3">
              <el-upload class="upload-demo" :action="uploadUrl()" :on-error="uploadError" :file-list="fileList"
                         :before-upload="beforeUpload" :on-success="uploadSuccess">
                <el-button size="small" type="primary" icon="el-icon-upload">点击上传</el-button>
              </el-upload>
              <div v-show="null != form.iconAttaOid && '' != form.iconAttaOid">
                <span>{{ form.attaName }}</span>
                <el-link type="primary" @click="downloadFile(form.iconAttaOid)">下载</el-link>
                |
                <el-link type="primary" @click="viewFileNew(form.iconAttaOid)">预览</el-link>
                |
                <el-link type="primary" @click="deleteFile(form)">删除</el-link>
              </div>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" align="center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看图标信息 -->
    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="900px"
      append-to-body
    >
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="zf-zc-table"
      >
        <colgroup>
          <col width="20%"/>
          <col width="30%"/>
          <col width="20%"/>
          <col width="30%"/>
        </colgroup>
        <tr>
          <td><b>图标名称：</b></td>
          <td>
            {{ iconPojo.iconName }}
          </td>
          <td><b>图标类型：</b></td>
          <td>
            <p v-for="(value,key) in iconTypeMap" :key="key" v-if="iconPojo.iconTypeCode==key">{{ value }}</p>
          </td>
        </tr>
        <tr>
          <td><b>排序号：</b></td>
          <td>
            {{ iconPojo.sort }}
          </td>
          <td><b>图标附件：</b></td>
          <td>
            <div v-show="null != iconPojo.iconAttaOid && '' != iconPojo.iconAttaOid">
              <span>{{ iconPojo.attaName }}</span>
              <el-link type="primary" @click="downloadFile(iconPojo.iconAttaOid)">下载</el-link>
              |
              <el-link type="primary" @click="viewFileNew(iconPojo.iconAttaOid)">预览</el-link>
            </div>
          </td>
        </tr>
      </table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!--引入文件的预览弹出层-->
    <el-dialog v-dialog-drag title="文件预览" :visible.sync="view.show" v-for="(view, index) in viewDialogOptions"
               :key="index" @close="closeFileView" width="900px" append-to-body>
      <file-view :attaOid="view.attaOid" @father-click="closeFileView"></file-view>
    </el-dialog>

  </div>
</template>

<script>
import {
  page,
  editIcon,
  getOne,
  saveIcon,
  del,
} from '@/api/middle/icon'
import {
  uploadFile
} from "@/api/sys/atta";
import fileView from '@/views/common/fileView';
import {validatePositiveNumber} from "@/utils/validate";

export default {
  components: { fileView },
  name: 'icon',
  data() {
    return {
      // 遮罩层
      loading: true,
      iconTypeMap: { '1': '金融', '2': '社区', '3': '食品', '4': '教育', '5': '健康', '6': '政府', '7': '城市', '8': '环境' },
      // 总条数
      total: 0,
      title: '',
      // 数据表格
      iconList: [],
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        iconName: '',
        iconTypeCode: ''
      },
      viewDialogOptions: [],
      previewImageUrl: '/case-api/pic/previewImage?fastdfsNginxUrl=',
      fileList: [],
      iconPojo: {},
      form: {
        iconName:'',
        iconTypeCode:'',
        sort:'',
        attaName:'',
        iconAttaOid:'',
        fastdfsNginxUrl:''
      },
      rules: {
        iconName:[
          {required:true,message:"必填项",trigger:"blur"}
        ],
        iconTypeCode:[
          {required:true,message:"必填项",trigger:"blur"}
        ],
        sort: [
          {required:true,message:"必填项",trigger:"blur"},
          { validator:validatePositiveNumber, trigger: 'blur' }
        ]
      }
    }
  },
  watch: {},
  created() {
    this.getList()
  },
  methods: {
    /** 查询服务图标列表 */
    getList() {
      this.loading = true
      page(this.queryParams).then(response => {
        this.iconList = response.data.datas
        this.total = response.data.totalRows
        this.loading = false
      })
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.reset()
      const oid = row.oid
      getOne(oid).then(response => {
        this.iconPojo = response.data
        this.openView = true
        this.title = '查看应用信息'
      })
    },

    //下载附件
    downloadFile(iconAttaOid) {
      this.download(iconAttaOid);
    },
    //预览附件
    viewFileNew(iconAttaOid) {
      let item = {
        show: true,
        attaOid: iconAttaOid
      };
      this.viewDialogOptions.push(item);
    },

    //关闭预览附件
    closeFileView() {
      this.viewDialogOptions.pop();
    },
    // 删除附件
    deleteFile(form) {
      form.attaName = '';
      form.iconAttaOid = '';
      form.fastdfsNginxUrl = '';
      this.$forceUpdate();
    },

    //成功后返回
    uploadSuccess(resp) {
      this.fileList = [];
      this.form.iconAttaOid = resp.data.oid;
      this.form.attaName = resp.data.name;
      this.form.fastdfsNginxUrl = resp.data.url;
    },
    //失败后返回
    uploadError(resp) {
      this.msgError("文件上传失败");
    },
    uploadUrl() {
      return uploadFile();
    },
    //上传之前
    beforeUpload(file) {
      if (file.name.indexOf("%00") > -1 || file.name.indexOf("./") > -1 || file.name.indexOf(".\\") > -1) {
        this.msgError('上传文件名称非法！');
        return false;
      }
      var type = this.fileByType(file.name);
      if (null != type && type != "jpg" && type != "png" && type != "jpeg" && type != "bmp" &&
        type != "JPG" && type != "PNG" && type != "JPEG" && type != "BMP") {
        this.msgError('请上传正确格式的文件！');
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

    // 初始化新增
    handleInit(row) {
      const oid = row.oid
      if (oid === undefined) {
        this.form = {}
        this.openInit = true
        this.title = '新增'
      } else {
        editIcon(oid).then(response => {
          this.form = response.data
          this.openInit = true
          this.title = '修改'
        })
      }
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (!this.form.iconTypeCode) {
            this.$message.error("请选择图标分类!");
            return false;
          }
          if (!this.form.iconAttaOid) {
            this.$message.error("请上图标照片!");
            return false;
          }
          saveIcon(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess('保存成功')
              this.openInit = false
              this.getList()
            }
          })
        } else {
          return false
        }
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNumber = 1
      this.queryParams.pageSize = 10
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParams = {}
      this.handleQuery()
    },
    // 表单重置
    reset() {
      this.form = {}
      this.resetForm('form')
    },
    // 取消按钮
    cancel() {
      this.openInit = false
      this.openView = false
      this.reset()
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const oid = row.oid
      this.$confirm(
        '是否确认删除?',
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
        .then(function() {
          return del(oid)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
        .catch(function() {
        })
    }
  },
  filters: {
  }
}
</script>
<style></style>
