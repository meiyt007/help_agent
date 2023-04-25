<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-22 15:25:53
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-07 13:29:01
 * @FilePath: \zf_web_ui\src\views\pc\guideClothing\index.vue
 * @Description: 导服首页
-->
<template>
  <div class="container">
    <div class="leftBanner">
      <div class="catalogue">
        <div
          class="item-catalogue"
          @click="changeCatalogue('guideClothingHome')"
          :class="
            componentName === 'guideClothingHome' ? 'active-catalogue' : ''
          "
        >
          <img
            :src="
              componentName === 'guideClothingHome'
                ? require('@/assets/images/pad/homeActive.png')
                : require('@/assets/images/pad/home.png')
            "
            alt=""
          />
          <p>首页</p>
        </div>
        <div
          class="item-catalogue"
          @click="changeCatalogue('allocated')"
          :class="componentName === 'allocated' ? 'active-catalogue' : ''"
        >
          <img
            :src="
              componentName === 'allocated'
                ? require('@/assets/images/pad/serviceActive.png')
                : require('@/assets/images/pad/service.png')
            "
            alt=""
          />
          <p>待分配</p>
        </div>
      </div>
    </div>
    <div class="content">
      <component
        :is="activeCatalogue"
        :componentData="componentData"
        @userDetail="userDetail"
      ></component>
    </div>
  </div>
</template>
<script>
import guideClothingHome from "./components/guideClothingHome.vue";
import allocated from "./components/allocated.vue";
import baseInfo from "./components/baseInfo.vue";
export default {
  name: "RoundTableAssistant",
  components: {
    guideClothingHome,
    allocated,
    baseInfo,
  },
  data() {
    return {
      activeCatalogue: "guideClothingHome",
      componentName: "guideClothingHome",
      componentData: {},
    };
  },
  mounted() {},
  methods: {
    changeCatalogue(type) {
      this.activeCatalogue = type;
      this.componentName = type;
    },
    userDetail(type, data = {}) {
      this.activeCatalogue = type;
      this.componentName = type;
      if (type === "baseInfo") {
        this.componentName = "allocated";
      }
      if (data) {
        this.componentData = data;
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.container {
  width: 100%;
  height: 100%;
  display: flex;

  .leftBanner {
    width: 8.3333rem;
    height: 100%;
    background-color: #fff;

    .catalogue {
      width: 100%;
      height: auto;
      padding: 5rem 0;

      .item-catalogue {
        width: 100%;
        height: 7.6875rem;
        text-align: center;
        padding-top: 0.5625rem;
        margin-bottom: 2.375rem;

        img {
          width: 3.6875rem;
        }

        p {
          margin-top: 0.625rem;
          font-size: 1.5rem;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #61666b;
        }
      }

      .active-catalogue {
        // background: linear-gradient(90deg, #fbe8c2 0%, #fff 100%);
        background: rgba(255, 255, 255, 0.61);
        box-shadow: -1px 0px 10px 0px rgba(104, 144, 231, 0.66);

        p {
          color: #5093e4;
        }
      }
    }
  }

  .content {
    height: 100%;
    width: calc(100% - 8.3333rem);
    padding: 2.1875rem 1.75rem 2.5rem 2rem;
  }
}
</style>
