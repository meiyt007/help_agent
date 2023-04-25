<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-09-07 17:26:24
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-08 09:45:55
 * @FilePath: \zf_web_ui\src\views\pc\roundTableAssistant\components\assistantServiceRecord.vue
 * @Description: 帮办历史
-->
<template>
  <div class="assistantServiceRecord"></div>
</template>
<script>
import { queryHelpServiceHistoryList } from "@/api/modules/workingGroup";
export default {
  name: "assistantServiceRecord",
  data() {
    return {
      temporaryStorageList: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
    };
  },
  mounted() {
    this.getQueryHelpServiceHistoryList();
  },
  methods: {
    //获取帮办服务记录
    getQueryHelpServiceHistoryList() {
      const params = {
        name: "",
        cardNo: "",
        companyName: "",
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        serviceStatus: "",
      };
      queryHelpServiceHistoryList(params).then((res) => {
        if (res.code === 200) {
          this.temporaryStorageList = res.data.data;
          this.total = res.data.total;
        }
      });
    },
  },
};
</script>
<style lang="sass" scoped></style>
