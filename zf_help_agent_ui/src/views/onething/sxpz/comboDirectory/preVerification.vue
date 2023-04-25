<template>
  <div class="pre-verification">
    <el-button type="primary" style="margin-bottom:10px;" @click="handleAdd">新增</el-button>
    <table class="zf-zc-table--common zf-zc-table--td-center">
      <colgroup>
        <col width="30%" />
        <col width="30%" />
        <col width="10%" />
        <col width="22%" />
        <col width="8%" />
      </colgroup>
      <tr>
        <th>核验名称(必填)</th>
        <th>核验地址(必填)</th>
        <th>核验参考附件</th>
        <th>核验排序</th>
        <th>操作</th>
      </tr>
      <tbody v-for="(item, index) in tableData" :key="index">
        <tr>
          <td>
            <el-input v-model="item.precheckName" placeholder></el-input>
          </td>
          <td>
            <el-input v-model="item.precheckAddress" placeholder></el-input>
          </td>
          <td>
            <el-upload
              class="upload-demo"
              :http-request="(file) => uploadFiles(file, item)"
              :before-upload="(file) => beforeUpload(file, item)"
              :on-error="uploadError"
              accept=".pdf, .PDF, .png, .jpp, .jpeg"
              :show-file-list="false"
              action=""
            >
              <el-button size="mini" type="primary">点击上传</el-button>
            </el-upload>
          </td>
          <td>
            <el-input-number v-model="item.sort" placeholder :max="999999" :min="1" :step="1"></el-input-number>
          </td>
          <td>
            <el-button type="danger" icon="el-icon-delete" circle @click="handleDelete(item)"></el-button>
          </td>
        </tr>
        <tr v-if="item.attaOid" class="pre-verification__file">
          <td colspan="4">
            <el-link type="primary" @click="downLoadFile(item.attaOid)">{{ item.attaName }}</el-link>
          </td>
          <td>
            <el-link type="primary" @click="viewFile(item)">预览</el-link>
            <el-link type="primary" @click="deleteFile(item)">删除</el-link>
          </td>
        </tr>
      </tbody>
    </table>
    <el-dialog title="预览附件" :visible.sync="openInit" :close-on-click-modal="false" append-to-body>
      <el-scrollbar style="height:calc(100vh - 330px)">
        <iframe :src="fileUrl" frameborder="0" style="height:calc(100vh - 330px);width:100%"></iframe>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>

<script>
import { downloadFile } from "@/api/onething/sxpz/comboAtta";
import { uploadsxFile, downLoadFile } from "@/api/zc/qdgl/sxService";
import { queryComboDirectoryPrecheckList } from "@/api/onething/sxpz/comboDirectory";
export default {
  name: 'PreVerification',
  props: ['comboDirectoryOid'],
  data () {
    return {
      tableData: [],
      openInit:false,
      fileUrl:''
    }
  },
  created () {
    this.queryComboDirectoryPrecheckList();
  },
  methods: {
    queryComboDirectoryPrecheckList () {
      this.$getResponse(queryComboDirectoryPrecheckList({ comboDirectoryOid: this.comboDirectoryOid, }), (error, res) => {
        if (error || res.code !== 200) return;
        this.tableData = res.data||[];
      })
    },

    handleAdd () {
      let sort = this.tableData[this.tableData.length - 1]?.sort;
      this.tableData.push({
        sort: sort ? sort + 1 : 1,
        precheckAddress: '',
        precheckName: '',
        _id: Date.now(),
        attaOid: '',
        fileName: '',
        comboDirectoryOid: this.comboDirectoryOid
      });
    },

    handleDelete (row) {
      if (row.id) {
        this.tableData = this.tableData.filter(item => item.id !== row.id);
      } else {
        this.tableData = this.tableData.filter(item => item._id !== row._id);
      }
    },

    uploadError () {
      // this.$message.error('上传附件失败, 请重新上传!');
    },

    beforeUpload (file, row) { 
      if (
        file.name.indexOf("%00") > -1 ||
        file.name.indexOf("./") > -1 ||
        file.name.indexOf(".\\") > -1
      ) {
        this.msgError("上传文件名称非法！");
        return false;
      }
      const fileSize = file.size;
      if (0 == fileSize) {
        this.msgError("上传文件不能为空！");
        return false;
      }
      // if (row.attaOid) return false;
      let isRightSize = file.size / 1024 / 1024 < 100;
      if (!isRightSize) {
        this.$message.error("文件大小超过 100MB");
      }
      return isRightSize;
    },

    /** 上传附件 */
    uploadFiles (file, row) { 
      let formData = new FormData();
      formData.append("file", file.file);
      this.$getResponse(uploadsxFile(formData), (error, response) => { 
        if (error || response.code !== 200) return this.uploadError();
        const { data } = response;
        row.attaName = data.name;
        row.attaOid = data.oid;
        row.attaUrl=data.attaUrl;
      })
    },

    downLoadFile (oid) {
      downLoadFile(oid);
    },

    deleteFile (item) {
      item.fileName = '';
      item.attaOid = '';
      item.attaUrl='';
    },

    validate () {
      if(this.tableData){
      let nameAll = this.tableData.some(item => !item.precheckName);
      let addressAll = this.tableData.some(item => !item.precheckAddress);
      let rule = /(http|https):\/\/([\w.]+\/?)\S*/;
      let addressFormat = this.tableData.some(item => !rule.test(item.precheckAddress));
      // let numAll=this.tableData.some(item => item.sort>999999);
      if (nameAll) {
        return '前置核验-【核验名称】不能为空';
      } else if (addressAll) {
        return '前置核验-【核验地址】不能为空';
      } else if (addressFormat) {
        return '前置核验-【核验地址】只能以https或者http开头';
      } else {
        return null;
      }
      }
    },

    viewFile(item){
      console.log(item,'item')
      this.fileUrl=item.attaUrl;
      this.openInit=true;
    }
  }
}
</script>

<style scoped lang="scss">
</style>
