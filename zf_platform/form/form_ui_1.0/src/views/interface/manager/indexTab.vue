<template>
  <div class="app-container app-container-config">
    <el-tabs type="border-card" v-model="activeName" @tab-click="handleClick" :before-leave="beforeLeave" >
      <el-tab-pane label="请求配置" name="first" >
        <div class="demo-input-suffix" v-if="show0">
          <!--接口描述：
          <el-input
            style="width: 92%"
            placeholder="请输入接口描述"
            v-model="desc">
          </el-input>-->
          <el-input placeholder="请输入接口描述" style="width: 98%" v-model="desc" class="input-with-select">
            <span slot="prepend">接口描述</span>
            <!--<el-button slot="append" @click="handleResponse" type="primary" icon="el-icon-position">发 送</el-button>-->
          </el-input>
        </div>
        <RequestPanel key="requestPanel" @handleResponse="handleResponse" @getPreRequestData="getPreRequestData" :requestJson="requestJson" :requestPreJson="requestPreJson" v-if="show0" ></RequestPanel>
      </el-tab-pane>
      <el-tab-pane label="前置请求配置" name="second" >
        <RequestPanel  key="preRequestPanel"  ref="preRequest" @handleResponse="handlePreResponse" :requestPreJson="requestPreJson" :isPre="true" v-if="show1" ></RequestPanel>
      </el-tab-pane>
      <el-tab-pane label="转换规则配置" name="three">
        <ChangeConfig
          :key="new Date().getTime()"
          :changeData="changeData" :requestJson="requestJson" :requestPreJson="requestPreJson"
                      :flag="flag"
                      :desc="desc"
                      :toConfig="toConfig"
                      @handleColse="handleColse"
                      @setFlag="setFlag"
                      v-if="show2"></ChangeConfig>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
  import RequestPanel from "../components/RequestPanel.vue";
  import ChangeConfig from "../changeConfig/changeConfig.vue";
  export default {
    components: {
      RequestPanel,ChangeConfig
    },
    props:{
      apiRuleEntity:{
        type:Object
      }
    },
    data() {
      return {
        activeName: 'first',
        show0:false,
        show1:false,
        show2:false,
        changeData:undefined,
        changePreData:undefined,
        toConfig:undefined,
        requestJson:'',
        requestPreJson:'',
        flag:'',
        desc:''
      };
    },
    created() {
      this.show0 = true;
      if(this.apiRuleEntity){
        this.requestJson = this.apiRuleEntity.requestJson;
        this.requestPreJson = this.apiRuleEntity.requestPreJson;
        this.toConfig = this.apiRuleEntity.rule?eval("(" + this.apiRuleEntity.rule + ")"):null;
        this.flag = this.apiRuleEntity.flag;
        this.desc = this.apiRuleEntity.desc;
      }
    },
    methods:{
      beforeLeave(activeName,oldActiveName){
        if(undefined == this.changeData && 'three' == activeName){
          this.msgError("请先编辑请求配置并发送！");
          return false;
        }
      },
      getPreRequestData(){
        return this.requestPreJson;
      },
      handleClick(tab, event) {
        //this.show1 = false;
        this.$set(this,'show'+tab.index,true);
      },
      handleResponse(res,requestJson){
        this.changeData = res;
        this.requestJson = requestJson;
      },
      handlePreResponse(res,requestPreJson){
        this.changePreData = res;
        this.requestPreJson = requestPreJson;
      },
      setFlag(data){
        this.flag = data;
      },
      handleColse(data){
        this.$emit('handleColse',data)
      }
    },
    mounted() {

    }
  };
</script>

<style>
  .app-container-config{
    margin-top: -20px !important;
  }
  .app-container-config .demo-input-suffix{
    margin-left: 22px;
  }
  .app-container-config .input-with-select .el-input-group{
    width: 98.21% !important;
  }

</style>
