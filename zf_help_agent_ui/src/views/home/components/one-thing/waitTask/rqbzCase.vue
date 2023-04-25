<template>
  <div class="rqbzCase">
    <el-table
      v-el-table-infinite-scroll="loadMore"
      :data="rqbzCase"
      border
      style="width: 100%"
      height="330px"
      @cell-click="rqbzClick"
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
  name: 'rqbzCase',
  props: ['activeName'],
  data () {
    return {
      rqbzCase: [],
      pageNumber: 1,
      pageSize: 10,
      isOver: false
    }
  },
  methods: {
    /* 容缺补证单元格点击跳转 */
    rqbzClick (row) {
      this.$router.push({
        path: "comboManager/combocaseRqbz",
        query: {
          caseOid: row.caseOid,
          caseNumber: row.caseNumber
        }
      });
    },
    loadMore: debounce(function () {
      if (this.isOver || this.activeName != '3') return;
      this.$getResponse(getOneThingWorkTaskCase({ type: this.activeName, pageNum: this.pageNumber, pageSize: this.pageSize }), (err, res) => {
        if (err || res.code !== 200) {
          return false;
        }
        this.rqbzCase = this.rqbzCase.concat(res.data.rqbzCase.data);
        if (this.rqbzCase.length >= res.data.rqbzCase.total) {
          this.isOver = true;
        } else {
          this.pageNumber++;
        }
      });
    }, 200)
  }
}
</script>