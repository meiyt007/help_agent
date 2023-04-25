<template>
  <div class="as-ask">
    <div class="generally-one">
      <img src="@/assets/image/generally-one.png" />
      <span>办事人</span>
    </div>
    <div class="cont">{{ content }}</div>
  </div>
</template>

<script>
export default {
  name: "LeftItem",
  props: ["id", "content"],
  methods: {},
};
</script>

<style scoped lang="scss">
.as-ask {
  .generally-one {
    img {
      vertical-align: middle;
      width: 34px;
      height: 34px;
      border-radius: 50%;
    }

    span {
      color: #808ca1;
      font-size: 14px;
    }
  }

  .cont {
    background-color: #fff;
    color: #201e1e;
    display: inline-block;
    font-size: 15px;
    padding: 12px 21px;
    margin-left: 45px;
    border-top-right-radius: 8px;
    border-bottom-left-radius: 8px;
    border-bottom-right-radius: 8px;
  }
}
</style>
