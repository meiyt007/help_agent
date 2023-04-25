<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-20 19:19:07
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-07-20 19:29:58
 * @FilePath: \hpNewHall\src\views\operationFlow\components\serviceList.vue
 * 服务列表
-->
<template>
  <div class="service-container">
    <div class="tab-content display_flex" :style="{ height: ulHeight }">
      <span class="text-left">委办局：</span>
      <ul class="list-none tab-content-list flex_1">
        <li
          class="tab-content-item click-element"
          v-for="(item, index) in tabContentList"
          :key="index"
          :class="{ active: activeContentTabIndex === item.organOid }"
          @click="changeContentTab(item.organOid)"
        >
          {{ item.name }}
        </li>
      </ul>
      <span class="text-right click-element" @click="expand()"
        ><span v-if="isExpand">收缩</span><span v-else>展开</span></span
      >
    </div>

    <!--  主体列表-->
    <el-row
      type="flex"
      class="main-list"
      justify="flex-start"
      v-loading="loading"
    >
      <el-col :span="6" v-for="(item, index) in mainList" :key="index">
        <div
          class="main-list-item display_flex flex-direction_column align-items_center"
        >
          <!--            <i class="el-icon-office-building item-icon"></i>-->
          <span class="item-title">{{ item.serviceName }}</span>
          <div class="item-flag">{{ item.organName }}</div>
          <div class="edit-row">
            <div
              class="edit-row-btn address-btn click-element"
              @click="openAddressDialog(item)"
            >
              <img src="../assets/address-icon.png" alt="" />
              <span>办理地址</span>
            </div>
            <div
              class="edit-row-btn onlinePro-btn click-element"
              @click="easyHandle(item)"
            >
              立即办理
            </div>
          </div>
        </div>
      </el-col>
      <no-data v-if="!mainList.length"></no-data>
    </el-row>

    <!-- 分页器 -->
    <el-pagination
      class="page-pagination"
      background
      prev-text="上一页"
      next-text="下一页"
      @prev-click="prevClick"
      @next-click="nextClick"
      @current-change="handleCurrentChange"
      :current-page="pagination.pageNum"
      :page-size="pagination.pageSize"
      layout="prev, pager, next, total, jumper"
      :total="total"
      v-if="mainList.length"
    >
    </el-pagination>
  </div>
</template>
<script>
export default {
  name: "ServiceList",
  data() {
    return {
      activeTabIndex: 1,
    };
  },
  mounted() {
    this.getDistrict();
  },
  methods: {
    // 据区划及事项办理类型查询机构列表
    getDistrict() {
      this.$api
        .listOrganByDistrictAndService({
          handleType: this.activeTabIndex,
          districtOid: "",
        })
        .then((res) => {
          this.tabContentList = [
            { id: 0, name: "全部", organOid: "" },
            ...res.data,
          ];
        });
    },
  },
};
</script>
<style lang="scss" scoped></style>
