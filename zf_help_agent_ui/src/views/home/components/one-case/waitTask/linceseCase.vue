<template>
  <div class="linceseCase">
    <el-table
      v-el-table-infinite-scroll="loadMore"
      :data="linceseCase"
      border
      style="width: 100%"
      height="330px"
      @cell-click="linceseCaseClick"
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
  name: 'linceseCase',
  props: ['activeName'],
  data () {
    return {
      linceseCase: [],
      pageNumber: 1,
      pageSize: 10,
      isOver: false
    }
  },
  methods: {
    /* 待发证单元格点击跳转 */
    linceseCaseClick (row) {
      this.$router.push({
        path: "caseLicenseSign/licenseIssued",
        query: {
          regOid: row.regOid,
          caseNumber: row.caseNumber
        }
      });
    },
    loadMore: debounce(function () {
      if (this.isOver || this.activeName != '5') return;
      this.$getResponse(getWorkTaskCase({ type: this.activeName, pageNum: this.pageNumber, pageSize: this.pageSize }), (err, res) => {
        if (err || res.code !== 200) {
          return false;
        }
        this.linceseCase = this.linceseCase.concat(res.data.linceseCase.data);
        if (this.linceseCase.length >= res.data.linceseCase.total) {
          this.isOver = true;
        } else {
          this.pageNumber++;
        }
      });
    }, 200)
  }
}
</script>