<template>
  <div class="bqbzCase">
    <el-table
      v-el-table-infinite-scroll="loadMore"
      :data="bqbzCase"
      border
      style="width: 100%"
      height="330px"
      @cell-click="bqbzClick"
    >
      <el-table-column prop="applyProjectName" label="事项名称" />
      <el-table-column prop="applyUserName" label="申请人" width="180" />
      <el-table-column prop="createDate" label="申请时间" width="200" />
    </el-table>
  </div>
</template>

<script>
import { getWorkTaskCase } from "@/api/zc/index/index";
const debounce = require('lodash/debounce');
export default {
  name: 'bqbzCase',
  props: ['activeName'],
  data () {
    return {
      bqbzCase: [],
      pageNumber: 1,
      pageSize: 10,
      isOver: false
    }
  },
  methods: {
    /* 补齐补证单元格点击跳转 */
    bqbzClick (row) {
      this.$router.push({
        path: "businessManagement/caseBqbz",
        query: {
          caseOid: row.caseOid,
          caseNumber: row.caseNumber
        }
      });
    },
    loadMore: debounce(function () {
      if (this.isOver || this.activeName != '2') return;
      this.$getResponse(getWorkTaskCase({ type: this.activeName, pageNum: this.pageNumber, pageSize: this.pageSize }), (err, res) => {
        if (err || res.code !== 200) {
          return false;
        }
        this.bqbzCase = this.bqbzCase.concat(res.data.bqbzCase.data);
        if (this.bqbzCase.length >= res.data.bqbzCase.total) {
          this.isOver = true;
        } else {
          this.pageNumber++;
        }
      });
    }, 200)
  }
}
</script>