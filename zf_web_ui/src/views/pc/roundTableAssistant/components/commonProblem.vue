<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-09-07 15:25:51
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-09-09 10:03:15
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\commonProblem.vue
 * @Description: 常见问题列表
-->
<template>
  <div class="commonProblem">
    <div class="tab-header">
      <div class="left">
        <p>问题</p>
        <el-input
          v-model="question"
          clearable
          @keyup.enter.native="getListQuestion"
        ></el-input>
      </div>
      <div class="right">
        <el-button @click="getListQuestion" type="primary" icon="el-icon-search"
          >搜索</el-button
        >
        <el-button icon="el-icon-plus" @click="toAddQuestion">新增</el-button>
      </div>
    </div>
    <div class="tableArea" ref="tableArea" v-loading="loadingTableData">
      <el-table
        ref="table"
        :height="tableHeight"
        :data="questionList"
        :cell-style="{ 'text-align': 'center' }"
        :header-cell-style="{ 'text-align': 'center' }"
        border
      >
        <el-table-column type="index" width="50" label="序号">
        </el-table-column>
        <template v-for="(item, index) in columnList">
          <el-table-column
            :key="index"
            :label="item.label"
            :prop="item.prop"
            v-if="item.prop === 'operate'"
            show-overflow-tooltip
            :width="item.width"
          >
            <template slot-scope="scope">
              <el-button type="danger" @click="toDeleteQuestion(scope.row)"
                >删除</el-button
              >
              <el-button type="primary" @click="updateQuestion(scope.row)"
                >修改</el-button
              >
            </template>
          </el-table-column>

          <el-table-column
            :key="index"
            :label="item.label"
            :prop="item.prop"
            v-else
            show-overflow-tooltip
            :width="item.width"
          >
          </el-table-column>
        </template>
      </el-table>
    </div>
    <el-dialog
      title="新增问题"
      :visible.sync="showAddQuestion"
      width="60%"
      class="questionDialog"
      v-dialogDrag
    >
      <div class="content-Body">
        <el-form
          :model="questionInfo"
          class="questionArea"
          :rules="questionRules"
          ref="questionInfo"
          label-width="10rem"
          ><el-form-item label="问题：" prop="question">
            <el-input v-model="questionInfo.question"></el-input>
          </el-form-item>
          <el-form-item label="答案：" prop="answer">
            <el-input
              type="textarea"
              :autosize="{ minRows: 5, maxRows: 4 }"
              v-model="questionInfo.answer"
            ></el-input> </el-form-item
        ></el-form>
        <div class="dialog-footer">
          <p @click="showAddQuestion = false">取消</p>
          <p @click="submit">确定</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  listQuestion,
  getQuestionInfo,
  saveQuestionInfo,
  deleteQuestion,
} from "@/api/modules/commonProblem";
export default {
  name: "CommonProblem",
  data() {
    return {
      question: "",
      showAddQuestion: false,
      loadingTableData: false,
      questionList: [],
      questionInfo: {
        id: "",
        question: "",
        answer: "",
      },
      tableHeight: 300,
      questionRules: {
        question: [{ required: true, message: "请输入问题", trigger: "blur" }],
        answer: [{ required: true, message: "请输入答案", trigger: "blur" }],
      },
      columnList: [
        { label: "问题", prop: "question", width: "200" },
        { label: "答案", prop: "answer", width: "" },
        { label: "操作", prop: "operate", width: "200" },
      ],
    };
  },
  mounted() {
    this.getListQuestion();
  },
  methods: {
    //新增问题
    toAddQuestion() {
      this.showAddQuestion = true;
      this.questionInfo.question = '';
      this.questionInfo.answer = '';
      this.questionInfo.id = '';
    },
    //修改问题
    updateQuestion(data) {
      this.showAddQuestion = true;
      this.questionInfo.question = data.question;
      this.questionInfo.answer = data.answer;
      this.questionInfo.id = data.id;
    },
    //删除问题
    toDeleteQuestion(data) {
      this.$confirm("确定删除该问题?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        appendToBody: false,
      })
        .then(() => {
          this.deleteQuestion(data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //删除问题
    deleteQuestion(data) {
      const params = {
        ids: data.id,
      };
      deleteQuestion(params).then((res) => {
        if (res.code === 200) {
          this.$message.success("删除成功");
          this.getListQuestion();
        }
      });
    },
    //新增问题
    submit() {
      const params = {
        id: this.questionInfo.id,
        question: this.questionInfo.question,
        answer: this.questionInfo.answer,
      };
      saveQuestionInfo(params).then((res) => {
        if (res.code === 200) {
          this.showAddQuestion = false;
          this.$message.success("新增成功");
          this.getListQuestion();
        }
      });
    },
    //获取问题列表
    getListQuestion() {
      const data = {
        question: this.question,
      };
      this.loadingTableData = true;
      listQuestion(data)
        .then((res) => {
          this.loadingTableData = false;
          if (res.code === 200) {
            this.tableHeight = this.$refs.tableArea.clientHeight;
            this.questionList = res.data;
          }
        })
        .catch((err) => {
          console.log(err);
          this.loadingTableData = false;
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.commonProblem {
  width: 100%;
  height: 100%;
  .tab-header {
    width: 100%;
    height: 5.7143rem;
    display: flex;
    align-content: center;
    justify-content: space-between;
    .left {
      width: 50%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      .el-input {
        width: calc(100% - 5rem);
        margin-left: 1.5rem;
      }
    }
    .right {
      width: 50%;
      display: flex;
      align-items: center;
      justify-content: flex-end;
    }
  }
  .tableArea {
    width: 100%;
    height: calc(100% - 8rem);
  }
}
.questionDialog {
  height: 100vh !important;
  ::v-deep .el-dialog {
    height: 60vh !important;
    .el-dialog__body {
      height: calc(100% - 3.75rem) !important;
      max-height: 100%;
      .content-Body {
        width: 100%;
        height: auto;
        .el-form {
          width: 100%;
          height: auto;
          margin-top: 2.625rem;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          flex-wrap: wrap;

          .el-form-item {
            height: auto;
            width: 100%;

            ::v-deep .el-form-item__label {
              font-size: 1.5rem;
              font-family: Source Han Sans CN;
              font-weight: 500;
              color: #373737;
            }

            ::v-deep .el-form-item__content {
              height: 100%;
            }
          }
        }
      }
    }
  }
}
.dialog-footer {
  width: 100%;
  height: auto;
  padding: 2rem 0;
  display: flex;
  align-items: center;
  justify-content: center;

  p {
    height: 4.8571rem;
    padding: 0 4.4375rem;
    border-radius: 1.5625rem;
    font-size: 1.625rem;
    display: flex;
    align-items: center;
    justify-content: center;

    font-family: Microsoft YaHei;
    font-weight: 400;

    &:nth-child(1) {
      background: #ffffff;
      border: 1px solid #4988f2;
      box-shadow: 0px 0px 1.8125rem 0px rgba(204, 177, 121, 0.31);
      color: #2473ff;
      cursor: pointer;
      margin-right: 2.1875rem;
    }

    &:nth-child(2) {
      background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
      box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
      color: #ffffff;
    }
  }
}
</style>
