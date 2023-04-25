<template>
  <div class="myHonor">
    <div class="card-title">
      <img src="@/assets/image/satisfaction.png" alt /> 满意度情况
    </div>
    <div class="card-box">
      <div id="myChart" :style="{ width: '100%', height: '200px' }"></div>
    </div>
  </div>
</template>

<script>
import { getSatisfaction } from '@/api/home/common';
const COLOR_MAP = ["#0172F6", "#FF6D00", "#FFD720", "#29CFFF", "#FF5A7B"];
const NAME_MAP = { 1: "非常不满意", 2: "不满意", 3: "基本满意", 4: "满意", 5: "非常满意" };
export default {
  name: "satisfactionIndex",
  data () {
    return {
      datas: [],
      echartsResize: null,
    };
  },
  mounted () {
    this.getSatisfaction().then(({ code, data }) => {
      if (code !== 200) return;
      this.datas = data.map((item, index) => {
        return {
          name: NAME_MAP[item.statisfactionMark],
          value: item.count,
          icon: "circle",
          percent: item.percent,
          textStyle: {
            fontSize: 12,
            color: COLOR_MAP[index],
          },
        }
      })

      this.drawLine();
    });
  },
  beforeDestroy () {
    window.onresize = null;
    this.echartsResize = null;
  },
  methods: {
    getSatisfaction () {
      return getSatisfaction();
    },

    drawLine () {
      // 基于准备好的dom，初始化echarts实例
      let myChart = this.$echarts.init(document.getElementById("myChart"));
      // 绘制图表
      myChart.setOption({
        color: COLOR_MAP,
        // title: {
        //   text: "满意度情况",
        //   left: "27.5%",
        //   top: "center",
        //   textStyle: {
        //     fontSize: 12,
        //     color: "#0172F6",
        //     fontWeight: "normal",
        //   },
        // },
        tooltip: {
          trigger: "item",
        },
        legend: {
          orient: "vertical",
          icon: "circle",
          itemWidth: 11,
          itemHeight: 11,
          itemGap: 15,
          right: "8%",
          top: 40,
          data: this.datas,
          formatter: (name) => {
            return `{font|${name}}   {per|${this.getPercent(name)}%}`;
          },
          textStyle: {
            rich: {
              font: {
                color: "#2C4577",
                fontSize: 12,
                padding: [0, 10, 0, 0]
              },
              per: {
                color: "#bbb",
                fontSize: 12,
                padding: [0, 10, 0, 0]
              },
            },
          },
        },
        series: [
          {
            type: "pie",
            roseType: "radius",
            center: ["32%", "50%"],
            radius: ["40%", "80%"],
            data: this.datas,
            labelLine: {
              length: 0,
            },
            label: {
              normal: {
                formatter: "{font|{c}个}",
                rich: {
                  font: {
                    fontSize: 12,
                    padding: [2, 0],
                  },
                  hr: {
                    height: 0,
                    borderWidth: 1,
                    width: "100%",
                  },
                },
              },
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0,0,0,0.5)",
              },
            },
          },
        ],
      });
      this.echartsResize = () => { myChart.resize() };
      window.onresize = this.echartsResize;
    },

    getPercent (name) {
      return this.datas.find(item => item.name === name).percent;
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
  font-weight: bold;
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
    justify-content: space-between;
    margin-top: 0px;
    padding: 5px 5px;
    background-color: #fff;
    height: 200px;
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
#main {
  width: 100%;
  height: 200px;
}
</style>
