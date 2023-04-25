<template>
  <div class="yscase">
    <el-table
      v-el-table-infinite-scroll="loadMore"
      :data="ysCase"
      border
      style="width: 100%"
      height="330px"
      @cell-click="waitTaskClick"
    >
      <el-table-column prop="serviceName" label="事项名称" />
      <el-table-column prop="applyUserName" label="申请人" width="180" />
      <el-table-column prop="createDate" label="申请时间" width="200" />
    </el-table>
  </div>
</template>

<script>
import { getWorkTaskCase } from "@/api/zc/index/index";
const debounce = require('lodash/debounce');
export default {
  name: 'yscase',
  props: ['activeName'],
  data () {
    return {
      ysCase: [],
      pageNumber: 1,
      pageSize: 10,
      isOver: false,
    }
  },
  methods: {
    /* 待预审单元格点击跳转 */
    waitTaskClick (row) {
      this.$router.push({
        path: "businessManagement/internetCasePre",
        query: {
          caseOid: row.caseOid,
          caseNumber: row.caseNumber
        }
      });
    },
    loadMore: debounce(function () {
      if (this.isOver || this.activeName != '1') return;
      this.$getResponse(getWorkTaskCase({ type: this.activeName, pageNum: this.pageNumber, pageSize: this.pageSize }), (err, res) => {
        if (err || res.code !== 200) {
          return false;
        }
        this.ysCase = this.ysCase.concat(res.data.ysCase.data.data);
        if (this.ysCase.length >= res.data.ysCase.data.total) {
          this.isOver = true;
        } else {
          this.pageNumber++;
        }
      });
    }, 200)
  }
}
</script>