<template>
  <div class="home-index">
    <el-row :gutter="20">
      <el-col :span="14">
        <workResults :caseTjInfoObj="caseTjInfoObj" />
      </el-col>
      <el-col :span="10">
        <myHonor />
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="13">
        <waitTask />
      </el-col>
      <el-col :span="11">
        <frequentList/>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="13">
        <handlingInquiry @submitHandle="submitHandle" />
      </el-col>
      <el-col :span="11">
        <satisfaction />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import workResults from "./components/one-case/workResults/index";
import myHonor from "./components/one-case/myHonor/index";
import waitTask from "./components/one-case/waitTask/index";
import newsNotes from "./components/one-case/newsNotes/index";
import handlingInquiry from "./components/one-case/handlingInquiry/index";
import satisfaction from "./components/one-case/satisfaction/index";
import frequentList from "./components/one-case/frequentList/index"
import { getCaseTjInfo, getWorkTaskCase } from "@/api/zc/index/index";
import { mapMutations } from "vuex";
export default {
  name: "OneCaseHomeIndex",
  data () {
    return {
      caseTjInfoObj: {},
      workTaskCaseObj: {
        ysCase: [],
        bqbzCase: [],
        rqbzCase: [],
        outOfstockCase: [],
        linceseCase: [],
      },
    };
  },
  components: {
    workResults,
    myHonor,
    waitTask,
    newsNotes,
    handlingInquiry,
    satisfaction,
    frequentList,
  },
  computed: {},
  created () {
    this.onGetCaseTjInfo();
  },
  methods: {
    //我的工作成效
    onGetCaseTjInfo () {
      getCaseTjInfo()
        .then((res) => {
          if (res.code == 200) {
            this.caseTjInfoObj = res.data;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },

    //办件查询
    ...mapMutations(['CHANGE_CODE']),
    submitHandle (e) {
      this.CHANGE_CODE(e)
    },
  },
};
</script>

<style lang="less" scoped>
.home-index {
  padding: 15px 10px;
  box-sizing: border-box;
}
</style>