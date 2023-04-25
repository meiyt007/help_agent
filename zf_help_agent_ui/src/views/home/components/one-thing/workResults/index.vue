<template>
  <div class="workResults">
    <div class="card-title">
      <img src="@/assets/image/work-icon.png" alt srcset /> 我的工作成效
    </div>
    <div class="card-box">
      <div class="card-item">
        <p>总计受理量</p>
        <h4>{{ caseTjInfoObj.slCount }}</h4>
      </div>
      <div class="card-item">
        <p>企业服务数</p>
        <h4>{{ caseTjInfoObj.frCount }}</h4>
      </div>
      <div class="card-item">
        <p>个人服务数</p>
        <h4>{{ caseTjInfoObj.grCount }}</h4>
      </div>
      <div class="card-item">
        <p>平均受理时效</p>
        <h4>
          {{ average }}
          <em>min</em>
        </h4>
      </div>
    </div>
  </div>
</template>

<script>
import { getAveragePrescription } from '@/api/home/common';
export default {
  name: "workResultsIndex",
  data () {
    return {
      average: 0
    };
  },
  props: {
    caseTjInfoObj: {
      type: Object,
      require: true,
    },
  },
  created () {
    this.getAveragePrescription();
  },
  methods: {
    getAveragePrescription () {
      getAveragePrescription().then(data => {
        this.average = data;
      })
    }
  },
};
</script>

<style lang="less" scoped>
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
.card-box {
  display: flex;
  justify-content: space-between;
  margin-top: 0px;
  margin-bottom: 20px;
  padding: 15px 5px;
  background-color: #fff;
  height: 134px;
  box-sizing: border-box;
  .card-item {
    padding: 10px 25px;
    box-sizing: border-box;
    flex: 1;
    p {
      font-size: 14px;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #ffffff;
    }
    h4 {
      font-size: 32px;
      font-family: DIN Black;
      font-weight: 600;
      color: #ffffff;
      margin: 0;
      padding-left: 40px;
      margin-top: -3px;
      em {
        font-size: 14px;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #ffffff;
        display: inline-block;
        vertical-align: middle;
        font-style: normal;
        margin: 7px 0 0 -5px;
      }
    }
  }
  .card-item:nth-of-type(1) {
    background: url(~@/assets/image/card-bg01.png) no-repeat center top;
    background-size: 100% 100%;
  }
  .card-item:nth-of-type(2) {
    background: url(~@/assets/image/card-bg02.png) no-repeat center top;
    background-size: 100% 100%;
  }
  .card-item:nth-of-type(3) {
    background: url(~@/assets/image/card-bg03.png) no-repeat center top;
    background-size: 100% 100%;
  }
  .card-item:nth-of-type(4) {
    background: url(~@/assets/image/card-bg04.png) no-repeat center top;
    background-size: 100% 100%;
  }
}
</style>