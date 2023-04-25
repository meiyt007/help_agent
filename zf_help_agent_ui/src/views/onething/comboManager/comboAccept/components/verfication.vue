<template>
  <div class="verfication">
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="sort" width="50" label="序号" align="center"></el-table-column>
      <el-table-column prop="precheckName" label="核验名称" show-overflow-tooltip></el-table-column>
      <el-table-column prop="precheckAddress" label="核验地址" show-overflow-tooltip></el-table-column>
      <el-table-column label="点击核验" align="center" width="100">
        <template slot-scope="{row}">
          <el-link type="primary" @click="handleValidate(row)">核验</el-link>
        </template>
      </el-table-column>
      <el-table-column label="核验通过按钮" align="center" width="150">
        <template slot-scope="{row}">
          <el-switch v-model="row.switch"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="核验附件" align="center" width="100">
        <template slot-scope="{row}">
          <el-button size="mini" type="primary" @click="handlePreview(row)" :disabled="!row.attaOid">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      v-dialog-drag
      title="文件预览"
      v-if="visible"
      :visible.sync="visible"
      width="1158px"
      height="800px"
      append-to-body
    >
      <sx-file-view :attaOid="attaOid" @father-click="closeFileViewNew" />
    </el-dialog>
  </div>
</template>

<script>
// import sxFileView from "@/views/onething/sxpz/comboDirectory/comboDireFileView";
import sxFileView from "@/views/zc/qdgl/sxFileView";
import { queryComboDirectoryPrecheckList } from "@/api/onething/sxpz/comboDirectory";
export default {
  name: 'verfication',
  props: {
    comboDirectoryOid: {
      type: String,
      default: ""
    },
  },
  components: { sxFileView },
  data () {
    return {
      tableData: [],
      visible: false,
      attaOid: ''
    }
  },
  created () {
    this.queryComboDirectoryPrecheckList();
  },
  methods: {
    queryComboDirectoryPrecheckList () {
      this.$getResponse(queryComboDirectoryPrecheckList({ comboDirectoryOid: this.comboDirectoryOid, }), (error, res) => {
        if (error || res.code !== 200) return;
        this.tableData = res.data.sort((a, b) => a.sort - b.sort).map(item => ({ ...item, switch: false }));
      })
    },

    handleValidate (row) {
      try {
        window.open(row.precheckAddress);
      } catch (error) {
        console.log(error);
      }
    },

    handlePreview (row) {
      this.attaOid = row.attaOid;
      this.visible = true;
    },

    closeFileViewNew () {
      this.visible = false;
    },

    validate () {
      return this.tableData.find(item => !item.switch);
    }
  }
}
</script>