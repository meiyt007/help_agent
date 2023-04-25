<template>
  <div class="myHonor newsNotes">
    <div class="card-title">
      <img src="@/assets/image/xxjs-icon.png" alt srcset /> 记事日历
    </div>
    <div class="card-box">
      <calendar ref="calendar" :weeks="weeks" />
    </div>
  </div>
</template>

<script>
import calendar from "./components/calendar/calendar.vue";

const day = new Date();

export default {
  name: "newsNotesIndex",
  components: {
    calendar
  },
  data() {
    return {
      weeks: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"]
    };
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
.newsNotes {
  .card-box {
    margin-top: 0px;
    margin-bottom: 20px;
    padding: 20px;
    background-color: #fff;
    height: 418px;
    max-height: 418px;
    box-sizing: border-box;
    position: relative;
    &::before {
      position: absolute;
      content: "";
      height: 60px;
      box-shadow: 0px 6px 10px 0px #d1e8ff;
      border-radius: 37px;
      width: 90%;
      background-color: #fff;
      left: 5%;
      bottom: 22px;
      z-index: 2;
    }
    &::after {
      position: absolute;
      content: "";
      height: 60px;
      box-shadow: 0px 6px 10px 0px #d1e8ff;
      border-radius: 37px;
      width: 84%;
      left: 8%;
      bottom: 15px;
      z-index: 1;
    }
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
    }
  }
}
</style>
