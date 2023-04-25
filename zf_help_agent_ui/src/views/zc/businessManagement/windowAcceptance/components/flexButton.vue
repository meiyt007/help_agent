<template>
  <div style="width:100%;height:100%;position:relative;">
    <input type="checkbox" id="click" />
    <label for="click" class="share-btn">
      <img
        class="fas fa-share-alt img1"
        v-show="!flexOptions"
        @click="changeFlex"
        :src="require('@/assets/image/intelligent/QA/default.png')"
        alt=""
      />
      <img
        class="fas fa-share-alt img2"
        v-show="flexOptions"
        @click="clickHatchery"
        :src="require('@/assets/image/intelligent/QA/new.png')"
        alt=""
      />

      <a href="#"
        ><p class="fab" @click="handleView"><span>查看事项</span></p></a
      >
      <a href="#"
        ><p class="fab" @click="showTelePhone"><span>咨询电话</span></p></a
      >
      <a href="#"
        ><p class="fab" @click="saveComponent"><span>暂存登记</span></p></a
      >
    </label>
        <!-- 信息详细 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="sxServiceInfo.show"
      v-for="(sxServiceInfo, index) in serviceDialogOptions"
      :key="index"
      title="查看实施清单详情"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <view-sx-service-info :sxServiceOid="serviceOid" />
      <div slot="footer" style="text-align:center">
        <el-button @click="viewServiceClose">
          关闭
        </el-button>
      </div>
    </el-dialog>
    <ConsultationTelephone @hideShowPopover="hideShowPopover" :showPopover="showPopover"/>
  </div>
</template>
<script>
import ConsultationTelephone from "../components/consultationTelephone.vue";
import viewSxServiceInfo from "@/views/zc/businessSupervise/viewSxServiceInfo";

export default {
  name: "flexBtn",
    components: {viewSxServiceInfo,ConsultationTelephone },
    props:['serviceOid','componentIndex'],
  data() {
    return {
      flexOptions: false,
      serviceDialogOptions: [],
      showPopover:false
    };
  },
  mounted() {},
  methods: {
        /** 查看事项按钮操作 */
    handleView() {
      this.sxServiceOid = this.serviceOid;
      let item = {
        show: true,
        sxServiceOid: this.serviceOid
      };
      this.serviceDialogOptions.push(item);
    },
        viewServiceClose() {
      this.serviceDialogOptions.pop();
    },
    showTelePhone(){
      this.showPopover = true
    },
    hideShowPopover(data){
       this.showPopover = false
    },
    saveComponent(){
      this.$store.commit("setSaveComponentIndex",this.componentIndex)
    },
    clickHatchery() {
      this.flexOptions = false;
       this.showPopover = false
    },
    changeFlex() {
      this.flexOptions = true;
    }
  }
};
</script>

<style lang="scss" scoped>
.share-btn {
  display: block;
  width: 60px;
  height: 60px;
  position: absolute!important;
  bottom: 0;
  right: 0;
  z-index: 100;
  position: relative;
}
.share-btn {
  .img1 {
    height: 60px;
    width: 60px;
    cursor: pointer;
    position: absolute;
    bottom: 0px;
    right: 14px;
    z-index: 1000;
  }
}
.share-btn {
  .img2 {
    height: 70px;
    width: 70px;
    cursor: pointer;
    position: absolute;
    bottom: 0px;
    right: 14px;
    z-index: 1000;
  }
}
// .share-btn:hover span {
//   background: #159d82;
//   border-color: #12876f;
// }
.share-btn a p {
  padding: 0;
  margin: 0;
  position: absolute;
  bottom: 0px;
  right: 14px;
  z-index: -1;
  opacity: 0;
  pointer-events: none;
  transition: 0.3s;
    display: flex;
    align-items: center;
    justify-content: center;
}


.share-btn a {
  &:nth-child(3) p {
    width: 69px;
    height: 64px;
    background: url(~@/assets/image/intelligent/QA/leftOpen.png) no-repeat;
    background-size: 100%;
    span {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 30px;
      height: 100%;
      font-size: 14px;
      font-family: Microsoft YaHei;
      font-weight: bold;
      color: #128b82;
    }
  }
  &:nth-child(4) p {
  width: 65px;
  height: 65px;
  background: url(~@/assets/image/intelligent/QA/obliqueupwardOpen.png)
    no-repeat;
  background-size: 100%;
  span {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 30px;
    height: 100%;
    font-size: 14px;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: #2575b7;
  }
}
&:nth-child(5) p {
  width: 64px;
  height: 69px;
  background: url(~@/assets/image/intelligent/QA/topOpen.png) no-repeat;
  background-size: 100%;
  span {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 30px;
    height: 100%;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: #a77021;
  }
}
}


#click:checked ~ .share-btn a {
  p {
    opacity: 1;
    pointer-events: auto;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
#click:checked ~ .share-btn a {
  &:nth-child(3) p {
    bottom:-10px;
    right: 80px;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 69px;
    height: 64px;
    font-size: 14px;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: #128b82;
    background: url(~@/assets/image/intelligent/QA/leftOpen.png) no-repeat;
    background-size: 100%;
    span {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 30px;
      height: 100%;
      font-size: 14px;
      font-family: Microsoft YaHei;
      font-weight: bold;
      color: #128b82;
      line-height: 18px;
    }
  }
}
#click:checked ~ .share-btn a:nth-child(4) p {
  bottom: 43px;
  right: 62px;
  width: 65px;
  height: 65px;
  background: url(~@/assets/image/intelligent/QA/obliqueupwardOpen.png)
    no-repeat;
  background-size: 100%;
  span {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 30px;
    height: 100%;
    font-size: 14px;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: #2575b7;
    line-height: 18px;
  }
}
.share-btn a:nth-child(4) p {
  // transition-delay: 0.2s;
}
#click:checked ~ .share-btn a:nth-child(5) p {
  bottom: 61px;
  right: 10px;
  width: 64px;
  height: 69px;
  background: url(~@/assets/image/intelligent/QA/topOpen.png) no-repeat;
  background-size: 100%;
  span {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 30px;
    height: 100%;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: #a77021;
    line-height: 18px;
  }
}
.share-btn a:nth-child(5) p {
  // transition-delay: 0.4s;
}
#click {
  display: none;
}
</style>
