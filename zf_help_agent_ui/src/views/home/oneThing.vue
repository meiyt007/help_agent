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
        <frequentList />
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
import workResults from "./components/one-thing/workResults/index";
import myHonor from "./components/one-thing/myHonor/index";
import waitTask from "./components/one-thing/waitTask/index";
import newsNotes from "./components/one-thing/newsNotes/index";
import handlingInquiry from "./components/one-thing/handlingInquiry/index";
import satisfaction from "./components/one-thing/satisfaction/index";
import frequentList from "./components/one-thing/frequentList/index"
import { getOneThingCaseTjInfo, getOneThingWorkTaskCase } from "@/api/zc/index/index";
import { mapMutations } from "vuex";
export default {
  name: "OneThingHomeIndex",
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
    console.log("one thing")
    this.onGetCaseTjInfo();
    this.getWorkTaskCaseList();
  },
  methods: {
    //我的工作成效
    onGetCaseTjInfo () {
      getOneThingCaseTjInfo()
        .then((res) => {
          if (res.code == 200) {
            this.caseTjInfoObj = res.data;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //待办任务
    getWorkTaskCaseList () {
      getOneThingWorkTaskCase()
        .then((res) => {
          this.workTaskCaseObj = res.data;
        })
        .catch((err) => {
          console.log(res);
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
