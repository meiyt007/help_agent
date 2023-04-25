<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-07-25 17:02:52
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-07 15:16:58
 * @FilePath: \zf_web_ui\src\views\pad\assistant\components\process\materialCompletion.vue
 * @Description: 材料完成
-->
<template>
  <div class="materialCompletion">
    <div class="content">
      <div class="header">
        <p>材料下载列表</p>
      </div>
      <div class="body-content">
        <div class="materialList">
          <el-checkbox-group
            v-model="checkeMaterialList"
            @change="handleCheckedMaterialChange"
            class="chooseBlock"
          >
            <el-checkbox
              v-for="material in materialList"
              :label="material.name"
              :key="material.id"
              >{{ material.name }}</el-checkbox
            >
          </el-checkbox-group>
        </div>
        <div class="showMaterial"></div>
      </div>
    </div>
    <div class="guidance-foot">
      <p @click="toLastStep">上一步</p>
      <p>下载材料</p>
      <p @click="toNextStep">已下载材料，下一步</p>
    </div>
    <el-dialog :visible.sync="complateVisible" width="35rem" center>
      <img :src="require('@/assets/images/pad/complateProgress.png')" alt="" />
      <p>材料准备已完成，是否继续办理其他业务？</p>
      <div slot="footer" class="dialog-footer">
        <p @click="toGoodBadComment">否，结束服务</p>
        <p type="primary" @click="complateVisible = false">是，继续办理业务</p>
      </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: "MaterialCompletion",
  data() {
    return {
      checkeMaterialList: [],
      materialList: [
        { name: "《增、减、补、换发证照申请书》", id: "1" },
        { name: "股东会决议书", id: "2" },
        { name: "董事会决议书", id: "3" },
      ],
      complateVisible: false,
    };
  },
  methods: {
    toLastStep() {
      this.$emit("nextStep", "intelligentFormFilling", "2");
    },
    toNextStep() {
      this.complateVisible = true;
    },
    handleCheckedMaterialChange() {},
    toGoodBadComment() {
      this.complateVisible = false;
      this.$emit("nextStep", "goodBadComment");
    },
  },
};
</script>
<style lang="scss" scoped>
.materialCompletion {
  width: 100%;
  height: 100%;
  background: #ffffff;
  border-radius: 1.75rem 2.125rem 2.125rem 1.75rem;
  padding: 1.625rem 2.8125rem 0 0.8125rem;
  padding: 2.5rem 1.8125rem 0 2.0625rem;
  .content {
    width: 100%;
    height: calc(100% - 9.375rem);
    background: rgba(199, 170, 111, 0.07);
    border-radius: 0.75rem;
    .header {
      width: 100%;
      height: 4.0625rem;
      background: rgba(199, 170, 111, 0.07);
      border-radius: 0.75rem 0.75rem 0px 0px;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      padding-left: 1.5rem;
      p {
        font-size: 1.625rem;
        font-family: Source Han Sans CN;
        font-weight: 500;
        color: #645c4b;
      }
    }
    .body-content {
      width: 100%;
      height: calc(100% - 4.0625rem);
      display: flex;
      align-items: flex-start;
      .materialList {
        width: 33%;
        height: 100%;
        overflow-y: auto;
        &::-webkit-scrollbar {
          width: 0.4375rem;
          background-color: #fff;
        }

        &::-webkit-scrollbar-thumb {
          width: 0.4375rem;
          height: 0.625rem !important;
          // background: linear-gradient(270deg, #bf9e63 0%, #dfca98 100%);
          background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
          border-radius: 4px;
        }
        .el-checkbox-group {
          width: 100%;
          height: 100%;
          padding: 1.8125rem 1.625rem;
          display: flex;
          flex-direction: column;
          align-items: flex-start;
          justify-content: flex-start;
          .el-checkbox {
            margin-bottom: 2.1875rem;
          }
        }
      }
      .showMaterial {
        flex: 1;
        height: 100%;
        overflow-y: auto;
      }
    }
  }
  .guidance-foot {
    width: 100%;
    height: 9.375rem;
    display: flex;
    align-items: center;
    justify-content: center;
    p {
      padding: 1.375rem 3.75rem;
      background: #ffffff;
      border: 1px solid #bd9d5f;
      box-shadow: 0px 0px 1.8125rem 0px rgba(204, 177, 121, 0.31);
      border-radius: 2.1875rem;
      font-size: 1.625rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #bd9e60;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 2.1875rem;
      &:nth-child(3) {
        background: linear-gradient(90deg, #bf9e63 0%, #dfca98 100%);
        box-shadow: 0px 0px 1.8125rem 0px rgba(204, 177, 121, 0.31);
        color: #ffffff;
      }
    }
  }
}
::v-deep .el-dialog {
  width: 40rem;
  height: 25.125rem;
  margin-top: calc(50vh - 12.5625rem) !important;
  .el-dialog__header {
    background-color: #fff;
  }
  .el-dialog__body {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 0 !important;
    img {
      width: 9rem;
    }
    p {
      font-size: 1.5rem;
      font-family: Source Han Sans CN;
      font-weight: 500;
      color: #373737;
      margin: 0.9375rem;
      padding: 0;
      margin: 0;
    }
  }
  .el-dialog__footer {
    height: 4.6875rem;
    margin-top: 5.9375rem;
    .dialog-footer {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      p {
        width: 50%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.5rem;
        font-family: Source Han Sans CN;
        font-weight: 500;
        padding: 0;
        margin: 0;
        &:nth-child(1) {
          background: #e1dacb;
          border-radius: 0px 0px 0px 0.625rem;
          color: #645c4b;
        }
        &:nth-child(2) {
          background: #c7aa6f;
          color: #ffffff;
          border-radius: 0px 0px 0.625rem 0;
        }
      }
    }
  }
}
</style>
