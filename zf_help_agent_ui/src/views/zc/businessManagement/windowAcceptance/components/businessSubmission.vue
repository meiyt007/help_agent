<template>
  <div  class="dialogContent">
    <div class="container">
      <img :src="require('@/assets/image/intelligent/success.png')" alt="">
      <p class="title">您申请的 <span>【{{serviceName}}】</span>事项，系统已智能审批通过！</p>
       <p class="reason">
        <span>{{message}}</span>
      </p>
    </div>
        <div class="footBtn">
      <el-button style="background-color:#D6DCE1;" @click="lastStep">关闭</el-button>
      <el-button type="primary" @click="nextStep"
        >打印审批材料</el-button
      >
    </div>
  </div>
</template>
<script>
import {getSxService} from  "@/api/zc/businessManagement/windowAcceptance";
export default{
  name:'businessSubmission',
  props:{
    serviceName:String,
    serviceOid:String
  },
  data(){
    return {
      message:''
    }
  },
    mounted() {
    this.getService();
  },
  activated(){
    this.getService()
  },
  methods:{
    getService(){
      getSxService(this.serviceOid).then(res=>{
        if(res.code===200){
          this.message = res.data.sxServiceExtend.mpPass
        }
      }).catch(err=>{

      })
    },
        lastStep() {
      this.$emit("lastStep", 4);
    }
  }
}
</script>
<style lang="scss" scoped>
.dialogContent {
  padding: 35px 35px 0 35px;
  .container {
    width: 100%;
    height: 458px;
    background: #f7f9fc;
    padding-top: 98px;
    display: flex;
    flex-direction: column;
    align-items: center;
    .title {
      padding: 0;
      margin: 0;
      font-size: 30px;
font-family: Microsoft YaHei;
font-weight: bold;
      color: #333333 ;
      margin-bottom: 23px;
      span{
        color:#3d7de6 ;
      }
    }
    .passTitle {
      margin-bottom: 65px;
    }
    .reason {
      font-size: 18px;
      padding: 0;
      margin: 0;
      font-family: Microsoft YaHei;
font-weight: 400;
color: #FF8213;
    }
      img {
        width: 85px;
        margin-bottom: 45px;
      }
    
  }
  .footBtn {
    width: 100%;
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 80px;
  }
}
</style>
