<template>
  <div class="waitTask">
    <div class="card-title">
      <img src="@/assets/image/dbrw-icon.png" alt srcset /> 待办任务
    </div>
    <div class="card-box wait-content">
      <el-row>
        <el-col :span="24">
          <!-- 待办任务内容 -->
          <el-tabs v-model="activeName" :stretch="true" @tab-click="handleTabClick">
            <el-tab-pane name="1">
              <div slot="label">
                待预审
                <span>({{ ysCaseSize }})</span>
              </div>
              <Yscase ref="ysCase" :activeName="activeName" v-if="ysCaseSize > 0" />
              <el-empty v-else description="暂无数据" />
            </el-tab-pane>
            <el-tab-pane name="2">
              <div slot="label">
                待补齐补正
                <span>({{ bqbzCaseSize }})</span>
              </div>
              <BqbzCase ref="bqbzCase" :activeName="activeName" v-if="bqbzCaseSize > 0" />
              <el-empty v-else description="暂无数据" />
            </el-tab-pane>
            <el-tab-pane name="3">
              <div slot="label">
                待容缺补正
                <span>({{ rqbzCaseSize }})</span>
              </div>
              <RqbzCase ref="rqbzCase" :activeName="activeName" v-if="rqbzCaseSize > 0" />
              <el-empty v-else description="暂无数据" />
            </el-tab-pane>
            <el-tab-pane name="4">
              <div slot="label">
                待出库
                <span>({{ outOfstockCaseSize }})</span>
              </div>
              <OutOfstockCase ref="outOfstockCase" :activeName="activeName" v-if="outOfstockCaseSize > 0" />
              <el-empty v-else description="暂无数据" />
            </el-tab-pane>
            <el-tab-pane name="5">
              <div slot="label">
                待发证
                <span>({{ linceseCaseSize }})</span>
              </div>
              <LinceseCase ref="linceseCase" :activeName="activeName" v-if="linceseCaseSize > 0" />
              <el-empty v-else description="暂无数据" />
            </el-tab-pane>
          </el-tabs>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getWorkTaskCase } from "@/api/zc/index/index";
import Yscase from './yscase.vue';
import BqbzCase from './bqbzCase.vue';
import RqbzCase from './rqbzCase.vue';
import OutOfstockCase from './outOfstockCase.vue';
import LinceseCase from './linceseCase.vue';
const map = {
  '1': 'ysCase',
  '2': 'bqbzCase',
  '3': 'rqbzCase',
  '4': 'outOfstockCase',
  '5': 'linceseCase',
}
export default {
  name: "waitTaskIndex",
  components: { Yscase, LinceseCase, OutOfstockCase, BqbzCase, RqbzCase },
  data () {
    return {
      activeName: "1",
      ysCaseSize: 0,
      bqbzCaseSize: 0,
      rqbzCaseSize: 0,
      outOfstockCaseSize: 0,
      linceseCaseSize: 0,
    };
  },
  created () {
    this.getWorkTaskCaseList();
  },
  methods: {
    //待办任务
    getWorkTaskCaseList (type = "") {
      getWorkTaskCase({ type })
        .then((res) => {
          if (res.code === 200) {
            const { bqbzCaseSize, linceseCaseSize, outOfstockCaseSize, rqbzCaseSize, ysCaseSize, ysCase } = res.data;
            this.bqbzCaseSize = bqbzCaseSize;
            this.linceseCaseSize = linceseCaseSize;
            this.outOfstockCaseSize = outOfstockCaseSize;
            this.rqbzCaseSize = rqbzCaseSize;
            this.ysCaseSize = ysCaseSize;
          }
        });
    },

    handleTabClick () {
      this.$nextTick(() => {
        this.$refs[map[this.activeName]].loadMore();
      })
    }
  },
};
</script>

<style lang="less" scoped>
.card-title {
  height: 48px;
  line-height: 48px;
  background: #e4e9ec;
  border-radius: 5px 5px 0px 0px;
  padding-left: 9px;
  box-sizing: border-box;
  font-size: 16px;
  color: #2a344c;
  font-weight: bold;
  img {
    display: inline-block;
    vertical-align: middle;
    margin: 0 7px 0 0;
  }
}
.waitTask {
  .card-box {
    margin-top: 0px;
    margin-bottom: 20px;
    padding: 5px 5px;
    background-color: #fff;
    height: 418px;
    max-height: 418px;
    .card-item {
      padding: 5px 20px 0px 20px;
      box-sizing: border-box;
      img {
        width: 86px;
        height: 84px;
      }
      p {
        font-size: 12px;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #999999;
        width: 62px;
        height: 19px;
        line-height: 19px;
        background: #e5eaed;
        border-radius: 10px;
        text-align: center;
        margin: 5px auto;
      }
    }

    &.wait-content {
      padding: 12px;
      .el-tabs {
        width: 100%;
        ::v-deep .el-tabs__header {
          margin: 0;
          .el-tabs__nav-wrap.is-top::after {
            height: 1px;
          }
        }
        ::v-deep.el-tabs__nav-wrap.is-top {
          height: 40px;
          padding: 6px 6px 0px 6px;
          border-radius: 10px 10px 0px 0px;
          border: none;
          box-sizing: border-box;
          background-color: rgba(53, 123, 240, 1);
          .el-tabs__nav-scroll .el-tabs__item.is-top {
            height: 33px;
            line-height: 35px;
            color: #ffff;
            &.is-active {
              background: linear-gradient(
                180deg,
                rgba(255, 255, 255, 3),
                rgba(217, 227, 236, 0.8)
              );
              border-radius: 15px 15px 0px 0px;
              color: #005aff;
            }
          }
          .el-tabs__nav-next,
          .el-tabs__nav-prev {
            display: none;
          }
        }
      }
      ::v-deep.el-tabs__nav-wrap.is-top
        .el-tabs__nav-scroll
        .el-tabs__item.is-top.is-active::after {
        height: 0;
      }
      ::v-deep.el-table tr {
        th {
          color: #353c4b;
          background-color: #f7f9fd;
          font-weight: bold;
          text-align: center;
          &:first-child {
            text-align: left;
          }
        }
        td {
          color: #2c4577;
          cursor: pointer;
          text-align: center;
          &:first-child {
            text-align: left;
          }
        }
        &:nth-of-type(2n + 1) {
          background-color: #e7eef3;
        }
        &:nth-of-type(2n) {
          background-color: rgba(237, 240, 245, 0.2) !important;
        }
      }
    }
  }

  ::v-deep.el-row {
    height: 100%;
    .el-col {
      height: 100%;
      .el-tabs {
        height: 100%;
        .el-tabs__content {
          height: calc(100% - 40px);
          .el-tab-pane {
            height: 100%;
            .el-table {
              height: 100%;
              .el-table__body-wrapper {
                height: calc(100% - 48px);
              }
            }
          }
        }
      }
    }
  }
}
</style>
