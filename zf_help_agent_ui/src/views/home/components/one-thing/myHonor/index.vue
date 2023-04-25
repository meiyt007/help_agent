<template>
  <div class="myHonor">
    <div class="card-title">
      <img src="@/assets/image/honor-icon.png" alt srcset /> 我的荣誉
    </div>
    <div class="card-box">
      <template v-if="honorList.length > 0">
        <div
          class="card-item"
          v-for="item in honorList"
          :class="generateClassName(item.isShow)"
          :key="item.code"
        >
          <img :src="item.url" alt srcset />
          <p>
            {{ item.name }}
          </p>
        </div>
      </template>
      <el-empty
        v-else
        style="margin: 0 auto"
        :image-size="60"
        description="暂无荣誉"
      />
    </div>
  </div>
</template>

<script>
import { getHonorList, getPersonalHonors } from "@/api/sys/user";
export default {
  name: "myHonorIndex",
  data() {
    return {
      honorList: [],
      honors: []
    };
  },
  created() {
    Promise.all([getHonorList(), getPersonalHonors()]).then(
      ([allHonorList, myHonorList]) => {
        if (allHonorList.length > 0) {
          allHonorList.forEach((data, index) => {
            let flag = false;
            let light = "";
            myHonorList.forEach((item, count) => {
              if (data.code == item.code) {
                flag = true;
              }
            });
            if (flag) {
              light = "-light";
            }
            this.honorList.push({
              code: data.code,
              name: data.name,
              isShow: flag,
              url: require(`@/assets/image/${allHonorList[
                index
              ].code.toLowerCase()}-img` +
                light +
                `.png`)
            });
          });
        }
      }
    );
  },
  methods: {
    generateClassName(isShow) {
      if (isShow) {
        // 调用方法，动态生成index
        return `card-item-active`;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.card-title {
  height: 48px;
  line-height: 48px;
  background: #e4e9ec;
  border-radius: 5px 5px 0px 0px;
  padding-left: 9px;
  box-sizing: border-box;
  font-size: 16px;
  color: #2a344c;
  font-weight: bold;
  img {
    display: inline-block;
    vertical-align: middle;
    margin: 0 7px 0 0;
  }
}
.myHonor {
  .card-box {
    display: flex;
    // justify-content: space-between;
    margin-top: 0px;
    margin-bottom: 20px;
    padding: 5px 5px;
    background-color: #fff;
    height: 134px;
    overflow: hidden;
    .card-item {
      padding: 5px 20px 0px 20px;
      box-sizing: border-box;
      img {
        width: 86px;
        height: 84px;
      }
      p {
        font-size: 12px;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #999999;
        width: 62px;
        height: 19px;
        line-height: 19px;
        background: #e5eaed;
        border-radius: 10px;
        text-align: center;
        margin: 5px auto;
      }
      &.card-item-active {
        p {
          color: #ff7800;
          background-color: rgba(253, 193, 78, 0.3);
        }
      }
    }
  }
}
</style>
