<template>
  <div class="change-config">
    <div class="inCoderDiv">
      <h3>转换原数据</h3>
      <InCoder :readOnly="true" :value="undefined!=changeData?JSON.stringify(changeData, null, 2):''"></InCoder>
    </div>
    <div class="inCoderCode">
      <h3>转换规则配置<i class="require">*</i>
        <el-tooltip placement="top-start" effect="light">
          <div slot="content">
            1.规则配置可定义json对象或者数组
            <br/>
            2.定义为对象则对应原数据也是对象，同理，数组也一样
            <br/>
            3.获取多层级数据需要使用a.b.c的方式
            <br/>
            4.对于无限嵌套循环的数据，只需要配置第一层
            <br/>
            5.点击下方转换按钮，可预览转换的结果
          </div>
          <i class="el-icon-question"></i>
        </el-tooltip>

        <el-dropdown  @command="handleCommand">
          <span class="el-dropdown-link">
            默认配置示例<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="obj">对象</el-dropdown-item>
            <el-dropdown-item command="arr">数组</el-dropdown-item>
            <el-dropdown-item command="tree">区划树</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

      </h3>
      <InCoder :key="'inCoder'+key" class="code" @input="updateText"  theme="panda-syntax" :lineNumbers="false" :readOnly="false"
               :value="undefined!=changeConfig?JSON.stringify(changeConfig, null, 2):''"></InCoder>
    </div>
    <div class="inCoderBtn">
      <el-button type="primary" @click="change" ><i class="el-icon-bottom el-icon--left"></i>转换<i class="el-icon-bottom el-icon--right"></i></el-button>

      <el-button type="warning" style="margin-left: 10px" @click="save" >保存配置</el-button>

      <el-button style="margin-left: 10px" @click="$emit('handleColse')" >关  闭</el-button>

    </div>
    <div v-if="displayResponse" >
      <hr/>
      <h3>转换后数据</h3>
      <InCoder :key="'view'+new Date().getTime()" :value="JSON.stringify(changeSuccessData, null, 2)"></InCoder>
      <br/>
      <br/>
    </div>
  </div>
</template>

<script>
  import InCoder from "../components/InCoder.vue";
  import { changeRequestData,save } from "@/api/interface/manager";
  export default {
    name: "ChangeConfig",
    components: {InCoder},
    //props: ['nowValue', 'toConfig'],
    props:{
      changeData: {
        type: Object
      },
      toConfig: {
        type: Object | Array
      },
      requestJson:{
        type:String,
        default:''
      },
      requestPreJson:{
        type:String,
        default:''
      },
      flag:{
        type:String,
        default:''
      },
      desc:{
        type:String,
        default:''
      }
    },
    data: function () {
      return {
        key:new Date().getTime(),
        displayResponse:false,
        //changeData:undefined,
        changeConfig:undefined,
        changeSuccessData:''
      }
    },
    created() {
      this.changeConfig = this.toConfig;
      //this.changeConfig = {'aaaa':'bbbbbb'};
      //this.changeData = undefined!=nowValue?JSON.stringify(nowValue, null, 2):'';
    },
    methods: {
      handleCommand(command) {
        this.key = new Date().getTime();
        if('obj' == command){
          this.$set(this,'changeConfig',{'data':'data'})
          //this.changeConfig = {'aaaa':'bbbbbb'};
        }else if('arr' == command){
          this.$set(this,'changeConfig',[{'data':'data'}])
        }else if('tree' == command){
          this.$set(this,'changeConfig',[{'id':'id','label':'label','parentId':'parentId','children':'children'}])
        }
       // this.$message('click on item ' + command);
      },
      updateText: function (msg) {
        this.changeConfig = msg;
      },
      change(){
        if(!this.changeData){
          this.msgError("先发送请求配置！");
          return false;
        }
        //请输入配置
        if(!this.changeConfig){
          this.msgError("请输入转换规则配置");
          return false;
        }
        let requestJson = {
          changeData:this.changeData,
          changeConfig:this.changeConfig
        }
        changeRequestData(requestJson).then(res => {
          this.displayResponse = true;
          this.changeSuccessData = res.data.data;
          this.msgSuccess('转换成功！');
        }).catch(err => {
          //this.msgError('转换失败！');
          //console.log(err);
        });

      },
      save(){
        if(!this.changeSuccessData){
          this.msgError("请先点击转换按钮！");
          return false;
        }
        let reqJson = this.requestJson?eval("(" + this.requestJson + ")"):null;
        let saveJson = {
          "flag": this.flag,
          "url": reqJson?reqJson.url:'',
          "method": reqJson?reqJson.method:'GET',
          "rule": this.changeConfig,
          "desc": this.desc,
          "requestJson": reqJson?reqJson:null,
          "requestPreJson":this.requestPreJson
        }
       save(saveJson).then(res => {
          this.msgSuccess('保存成功！');
          this.$emit('handleColse',true);
          this.$emit('setFlag',res.data.data.flag);
        }).catch(err => {

        });
      }
    },
    watch: {
      /*local: function () {
        this.$emit('changed', this.local);
      }*/
    }
  }
</script>

<style scoped>
  .change-config{
    margin-top: -15px;
  }
  .change-config .inCoderDiv{
    width: 48%;float: left;
  }
  .change-config .inCoderCode{
    width: 48%;float: left;margin-left: 35px
  }
  .change-config .inCoderBtn{
    text-align: center;
  }
  .change-config .inCoderBtn button{
    margin-top: 15px;
  }

  .change-config .el-dropdown{
    margin-left: 45%;
  }
</style>
