/**
* @Author: xldong
*/
<template>

  <div id="container" style="height: 100%;" ref="chart"></div>

</template>

<script>
import { getRelationJsonForPicture} from "@/api/zc/qdgl/sxService.js";
export default {
  name: "ViewRelationPicture",
  props:["serviceOid","serviceName"],
  data () {
    return {
      // 遮罩层
      loading: true,

    };
  },
  mounted () {
    this.echartfunc();
  },
  methods: {
  echartfunc() {
    let var_div=this.$refs.chart;
    let TimeFn = null;
      // 基于准备好的dom，初始化echarts实例
      var myChart = this.$echarts.init(var_div);
    getRelationJsonForPicture(this.serviceOid,'','').then(response=>{
      let mes=response.data;
      if(mes){
        // title
        let titleImg = "../../assets/image/workflow_blue.png";
        // value
        let valImg = "../../assets/image/ico_no5.png";
        myChart.hideLoading();
        let relationJson = mes.data1;
        let relationLineJson = mes.data2;
        let nodes = new Array();
        let moveLines = new Array();
        let titleCount = 0;
        relationJson.forEach(json=>{
          let data = {};
          data.oid = json.oid;
          data.type = json.type;
          if(json.name != null && json.name != '' ){
            data.name = json.name.substring(0,12);
          }
          data.value = [json.x, json.y, 4];
          if(json.type == '0') {
            titleCount++;
            data.symbol = 'image://' + titleImg;
          } else {
            data.symbol = 'image://' + valImg;
          }
          data.symbolSize = 40;
          nodes.push(data);
        })

        relationLineJson.forEach(json=>{
          let data = {};
          data.fromName = json.from;
          data.toName = json.to;
          data.coords = [
            [json.fromX, json.fromY],
            [json.toX, json.toY]
          ];
          data.color= json.color;
          moveLines.push(data);
        })
        let allData = {
          "nodes": nodes,
          "moveLines": moveLines
        };

        let option = {
          backgroundColor: '#020933',
          title: {
            text: this.serviceName,
            left: '20',
            top: '20',
            textStyle: {
              color: '#fff'
            }
          },
          geo: {},
          series: [{
            name: '地点',
            //type: 'effectScatter',
            type: 'scatter',
            coordinateSystem: 'geo',
            zlevel: 2,
            rippleEffect: {
              brushType: 'stroke',
              period: 7,
              scale: 26
            },
            label: {
              normal: {
                show: true,
                position: 'top',
                formatter: '{b}',
                color: 'white',
              },
              emphasis: {
                show: true,
                position: 'right',
                formatter: '{b}'
              }
            },
            symbolSize: 2,
            showEffectOn: 'render',
            itemStyle: {
              normal: {
                color: '#46bee9'
              }
            },
            data: allData.nodes
          },
            {
              name: '线路',
              type: 'lines',
              coordinateSystem: 'geo',
              zlevel: 2,
              large: true,
              effect: {
                show: true,
                constantSpeed: 30,
                symbol: 'arrow', //ECharts 提供的标记类型包括 'circle', 'rect', 'roundRect', 'triangle', 'diamond', 'pin', 'arrow'
                symbolSize: 6,
                trailLength: 0,
              },

              lineStyle: {
                normal: {
                  color: function(params) {
                    return params.data.color
                  },
                  width: 2,
                  opacity: 0.6,
                  curveness: 0.1
                }
              },
              data: allData.moveLines
            }
          ]
        };
        if(titleCount >= 6) {
          document.getElementById('container').style.height = (50 + titleCount*106) +'px'
          myChart.resize();//直接加这句即可
        }else{
          document.getElementById('container').style.height = '600px'
          myChart.resize();//直接加这句即可
        }
        myChart.setOption(option,true);
      }
    })
}

  },
};
</script>
