<template>
  <div class="depart-container">
    <div class="content">
      <el-radio-group v-model="tableradioGroup" class="chooseOption">
        <el-radio
            :key="data" name="group1"  @change="changeService(index)" v-for="(data,index) in nameArr" :label="data"
        >{{ data}}
        </el-radio>
      </el-radio-group>
    </div>
<!--    <div class="dialog-footer center">-->
<!--      <p @click="cancelDialog">取消</p>-->
<!--      <p @click="serviceTurn">确定</p>-->
<!--    </div>-->
  </div>
</template>
<script>
import { listOrganByDistrictAndService,queryVideoPage } from "@/api/modules/business";
import chooseWorker from './chooseWorker.vue'
export default{
  name:'policyNext',
  data(){
    return{
      listArray:[],
      tableradioGroup:'',
      workerShow:false,
      url:'',
      nameArr:[],
      urlArr:[]
    }
  },
  props:{
    listName: {
      type: String,
      default: () => "",
    },
    listUrl: {
      type: String,
      default: () => "",
    },
  },
  mounted(){
    console.log(this.listName+"---------------"+this.listUrl)
    this.nameArr = this.listName.split(";");
    this.urlArr = this.listUrl.split(";");

  },
  methods:{
    changeService(index){
      let url = this.urlArr[index];
      console.log("Url"+url)
      android.setWebUrl(url);
    },
    cancelDialog(){
      this.$emit('closeDepart');
    },
    closeWorker(){
      this.workerShow = false;
    },
    serviceTurn(){
      console.log("Url"+this.url)
      android.setWebUrl(this.url);
    }
  }
}
</script>
<style lang="scss" scoped>
.depart-container{
  height:100%;
  .content{
    height: calc(100% - 50px);
    overflow: auto;
  }
}
.chooseOption {
  width: 100%;
  height: auto;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-wrap: wrap;

  .el-radio {
    width:calc((100% - 3.875rem)/2);
    height: 3.9375rem;
    background: #edf0f4;
    border: 1px solid #c8cdd3;
    box-shadow: 0px 0.5rem 0px 0px rgba(106, 159, 231, 0.31);
    border-radius: 2.2143rem;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    padding: 0 3rem 0 1.3125rem;
    font-size: 1.5rem;
    font-family: Source Han Sans CN;
    font-weight: 500;
    color: #3f3f3f;
    margin-right: 2.875rem;
    margin-bottom: 1.5rem;

    ::v-deep .el-radio__inner {
      display: inline-block;
      width: 1.625rem;
      height: 1.625rem;
      background: url("@/assets/images/pad/choose.png") no-repeat;
      background-size: 100% 100%;
    }

    // img {
    //   width: 1.625rem;
    //   margin-right: 0.6875rem;
    // }

    span {
      color: #3f3f3f;
    }
  }
  &>label:nth-child(2n){
    margin-right: 0;
  }
  .is-checked {
    color: #ffffff;
    background: linear-gradient(
            90deg,
            #6d93e8 0%,
            #77b0fe 100%
    ) !important;
    // box-shadow: 0px 0.5rem 0px 0px rgba(106, 159, 231, 0.31);
    box-shadow: 0px 0.4375rem 0px 0px rgb(106 159 231 / 31%),
    inset 0 0.4375rem 0.4375rem 0 rgb(188 212 251),
    inset -0.4375rem 0 0 0 rgb(114 173 249),
    inset 7px 0 0 0 rgb(107 146 230) !important;
    border: none !important;

    ::v-deep .el-radio__label {
      color: #fff;
    }

    ::v-deep .el-checkbox__label {
      color: #fff;
    }

    ::v-deep .el-checkbox__inner {
      background: #fff;

      &:after {
        border-color: #6090e3;
      }
    }

    ::v-deep .el-radio__inner {
      background: url("@/assets/images/pad/chooseActivate.png") no-repeat;
      background-size: 100% 100%;

      &:after {
        display: none;
      }
    }
  }

  .el-checkbox {
    min-width: 13.875rem;
    height: 3.9375rem;
    background: #edf0f4;
    border: 1px solid #c8cdd3;
    box-shadow: 0px 0.5rem 0px 0px rgba(106, 159, 231, 0.31);
    border-radius: 2.2143rem;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    padding: 0 3rem 0 1.3125rem;
    font-size: 1.5rem;
    font-family: Source Han Sans CN;
    font-weight: 500;
    margin-right: 2.875rem;
    margin-bottom: 1.5rem;
  }
}
.dialog-footer {
  justify-content: center !important;
  display: flex;
  p {
    &:nth-child(1) {
      cursor: pointer;
      padding: 0 3.75rem;
      width: auto;
      height: 5.1667rem;
      background: #ffffff;
      border: 1px solid #4988f2;
      box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
      border-radius: 2.5833rem;
      font-size: 1.8333rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #2473ff;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 2.1875rem;
    }

    &:nth-child(2) {
      cursor: pointer;
      padding: 0 3.75rem;
      width: auto;
      height: 5.1667rem;
      background: #ffffff;
      border: 1px solid #4988f2;
      box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
      border-radius: 2.5833rem;
      font-size: 1.8333rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #2473ff;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 2.1875rem;
      color: #ffffff !important;
      background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%) !important;
      box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%) !important;
    }
  }
}
</style>