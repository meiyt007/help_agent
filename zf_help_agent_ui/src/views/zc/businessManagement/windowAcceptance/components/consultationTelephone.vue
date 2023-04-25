<template>
  <div class="consultation-telephone">
    <!-- <div class="consulation-telephone__context" @click="showPopover = true">
      <div class="consulation-telephone__context-img">
        <img src="@/assets/image/telephone.png" alt />
      </div>
      <div class="consulation-telephone__context-text">
        <span>咨询电话</span>
      </div>
    </div> -->

    <div class="consulation-telephone__popover" v-show="showTelephone">
      <div class="consulation-telephone__popover-close" @click="hiddleTelephone">
        <i class="el-icon-close"></i>
      </div>
      <div class="consulation-telephone__popover-context">
        <span class="consulation-telephone__popover-context--title">人员名称：</span>
        <span class="consulation-telephone__popover-context--value">{{ userName }}</span>
      </div>
      <div class="consulation-telephone__popover-context">
        <span class="consulation-telephone__popover-context--title">电话号码：</span>
        <span class="consulation-telephone__popover-context--value">{{ mobile }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import { get } from '@/api/zc/qdgl/handleLink.js';
export default {
  name: 'ConsultationTelephone',
  props:{
    showPopover:{
      type:Boolean,
      default:false
    }
  },
  
  data () {
    return {
      mobile: '',
      userName: '',
      showTelephone:false
    }
  },
  watch:{
    showPopover:{
      handler(val){
        this.showTelephone = val
      },
      deep:true
    }
  },
  created () {
    if (this.$store.state.user.userOid) {
      this.$getResponse(get(this.$store.state.user.userOid), (err, res) => {
        if (err || res.code !== 200) return;
        this.mobile = res.data.mobile;
        this.userName = res.data.name;
      })
    }
  },
  methods:{
    hiddleTelephone(){
      this.showTelephone = false
      this.$emit("hideShowPopover",false)
    }
  }
}
</script>

<style scoped lang="scss">
.consultation-telephone {
  position: absolute;
  right: 30px;
  bottom: 25px;

  .consulation-telephone__context {
    width: 125px;
    height: 34px;
    background: #e3e8eb;
    border-radius: 17px;
    position: relative;
    cursor: pointer;
    z-index: 1;

    .consulation-telephone__context-img {
      width: 38px;
      height: 38px;
      background: #2e7dff;
      box-shadow: 0px 0px 7px 0px rgba(46, 125, 255, 0.5);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      position: absolute;
      top: -3px;
    }

    .consulation-telephone__context-text {
      font-size: 15px;
      font-family: Microsoft YaHei;
      font-weight: bold;
      color: #2e7dff;
      position: absolute;
      top: 8px;
      left: 48px;
    }
  }

  .consulation-telephone__popover {
    width: 250px;
    height: 160px;
    background: #ffffff;
    box-shadow: 0px 0px 8px 0px rgba(4, 0, 0, 0.08);
    border-radius: 10px 10px 0px 0px;
    position: absolute;
    right: 0;
    top: -260px;
    padding: 40px 0px 0 10px;
    animation: consulation 0.5s;
    overflow: hidden;

    .consulation-telephone__popover-close {
      width: 24px;
      height: 24px;
      background: #e6ebf3;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      position: absolute;
      right: 10px;
      top: 10px;
    }
    .consulation-telephone__popover-context {
      line-height: 30px;
      white-space: nowrap;
      .consulation-telephone__popover-context--title {
        font-size: 16px;
        font-weight: 400;
        color: #999999;
      }

      .consulation-telephone__popover-context--value {
        font-size: 16px;
        font-weight: bold;
        color: #2a344c;
      }
    }
  }
}

@keyframes consulation {
  0% {
    width: 0px;
    height: 0px;
    opacity: 0;
  }

  100% {
    width: 250px;
    height: 160px;
    opacity: 1;
  }
}
</style>
