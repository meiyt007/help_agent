<template>
  <div class="outOfstockCase">
    <el-table
      v-el-table-infinite-scroll="loadMore"
      :data="outOfstockCase"
      border
      style="width: 100%"
      height="330px"
      @cell-click="outOfstockCaseClick"
    >
      <el-table-column prop="serviceName" label="事项名称" />
      <el-table-column prop="applyUserName" label="申请人" width="180" />
      <el-table-column prop="createDate" label="申请时间" width="200" />
    </el-table>
  </div>
</template>

<script>
import { getOneThingWorkTaskCase } from "@/api/zc/index/index";
const debounce = require('lodash/debounce');
export default {
  name: 'outOfstockCase',
  props: ['activeName'],
  data () {
    return {
      outOfstockCase: [],
      pageNumber: 1,
      pageSize: 10,
      isOver: false
    }
  },
  methods: {
    /* 待出库单元格点击跳转 */
    outOfstockCaseClick (row) {
      this.$router.push({
        path: "comboManager/materialOut",
        query: {
          regOid: row.regOid,
          caseNumber: row.caseNumber
        }
      });
    },
    loadMore: debounce(function () {
      if (this.isOver || this.activeName != '4') return;
      this.$getResponse(getOneThingWorkTaskCase({ type: this.activeName, pageNum: this.pageNumber, pageSize: this.pageSize }), (err, res) => {
        if (err || res.code !== 200) {
          return false;
        }
        this.outOfstockCase = this.outOfstockCase.concat(res.data.outOfstockCase.data);
        if (this.outOfstockCase.length >= res.data.outOfstockCase.total) {
          this.isOver = true;
        } else {
          this.pageNumber++;
        }
      });
    }, 200),
  }
}
</script>