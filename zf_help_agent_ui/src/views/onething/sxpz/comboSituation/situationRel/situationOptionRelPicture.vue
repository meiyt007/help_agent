<template>
  <div>
    <el-row type="flex" justify="space-between">
      <el-col :span="15">
        <div class="title-service">
          <div class="title-service-name">{{ serviceName }}</div>
          <el-popover
            placement="right"
            width="320"
            trigger="hover"
            content="橙色：情形要满足前置条件才能触发展示；  绿色：情形没有关系或选项值有后置关系；"
          >
            <el-button slot="reference"></el-button>
          </el-popover>
        </div>
        <el-collapse
          v-model="activeNames"
          v-if="situationList.length"
          @change="viewFrontRelationship"
          accordion
          style="padding-right: 15px"
        >
          <el-collapse-item
            v-for="(situ, ind) in situationList"
            :name="situ.situationOid"
            :class="{ existPre: !situ.checkSituation }"
          >
            <template slot="title">
              <span>{{ ind + 1 }}</span>
              <p :title="situ.situationName">{{ situ.situationName }}</p>
            </template>
            <!--<div class="inner" :class="{'existPre-inner':situ.checkVal}" v-if="situ.checkVal">
              <ul>
                <li v-for=" optionVal in situ.optionValsList" :class="{item:optionVal.checkVal}"
                  @click="viewPostposition(optionVal)">{{optionVal.name}}</li>
              </ul>
            </div>-->
          </el-collapse-item>
        </el-collapse>
      </el-col>
      <el-col :span="9">
        <div class="front-relationship">
          <p v-html="frontRrelationshipItem"></p>
          <div class="fron-main">
            <div class="fron-box">
              <div>
                <p>前置关系</p>
                <el-popover
                  placement="right"
                  width="325"
                  trigger="hover"
                  content="前置关系：选择了某个选项值以后才会显示；"
                >
                  <el-button slot="reference"></el-button>
                </el-popover>
              </div>
              <section v-html="frontRrelationshipCon1"></section>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import {
  querySituationPictureList
} from "@/api/onething/comboSituation/comboOptionRel.js";
export default {
  name: "OptionDiagram",
  props: ["comboDirectoryOid"],
  data () {
    return {
      activeNames: [],
      title: '',
      frontRrelationshipItem: '',
      frontRrelationshipCon1: '',
      frontRrelationshipCon2: '',
      situationList: [],
      serviceName: ''
    };
  },
  created () {
    this.getTitleList();
  },
  methods: {
    /** 查询情形列表 */
    getTitleList () {
      this.loading = true;
      querySituationPictureList(this.comboDirectoryOid).then(response => {
        this.situationList = response.data.situationList;
        console.log(response.data.comboDirectory)
        this.serviceName = response.data.comboDirectory.comboDirectoryName;
        this.loading = false;
      });
    },

    viewFrontRelationship (val) {
      // this.title = "前置关系";
      this.situationList.forEach((item, idx) => {
        if (val == item.situationOid) {
          if (item.checkSituation) { // 有前置关系
            this.frontRrelationshipItem = item.situationName;
            let constr = '';
            if (item.valOidList) {
              item.valOidList.forEach(vv => {
                constr += vv + "<br>";
              });
            }
            this.frontRrelationshipCon1 = constr;
          } else {
            this.frontRrelationshipItem = '';
            this.frontRrelationshipCon1 = '';
          }
        }
      })
    },
    /*viewPostposition(val) {
      this.title = "后置关系";
      if (val.ifAfter) {
        this.frontRrelationshipItem = val.name;
        let constr = '';
        if (val.afterTitles) {
          val.afterTitles.forEach(vv => {
            constr += vv + "<br>";
          });
        }
        this.frontRrelationshipCon1 = constr;
      } else {
        this.frontRrelationshipItem = '';
        this.frontRrelationshipCon1 = '';
      }
    }*/
  },
};

</script>
<style lang="scss" scoped>
.title-service {
  display: flex;
  align-items: center;
  height: 45px;
  .title-service-name {
    color: #3052e8;
    font-size: 22px;
    font-weight: bold;
    // margin-bottom: 17px;
    margin-top: 0;
    float: left;
    white-space: nowrap;
    max-width: 93%;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .el-button {
    border: 0;
    padding: 0;
    width: 18px;
    height: 18px;
    margin-top: 3px;
    margin-left: 10px;
    float: left;
    background: url(../../../../../assets/image/tipMsg.png);
    background-size: 100% 100%;
  }

  .el-button:hover {
    background: url(../../../../../assets/image/tipMsg_active.png);
    background-size: 100% 100%;
  }

  &::after {
    content: '';
    display: block;
    clear: both;
  }
}

>>> .el-collapse {
  border-top: unset;
}

.el-collapse-item {
  border-top-left-radius: 6px;
  border-top-right-radius: 6px;
  overflow: hidden;
  box-shadow: 0 4px 10px #eaffdf;
  width: 100%;

  &:not(:last-child) {
    margin-bottom: 20px;
  }

  & >>> .el-collapse-item__header {
    background-color: #e6a23d;
    color: #fff !important;
    font-size: 16px;
    height: 50px;

    span {
      display: inline-block;
      width: 49px;
      height: 49px;
      background-color: #cf8d2b;
      text-align: center;
      margin-right: 15px;
      font-size: 20px;
    }

    p {
      width: 100%;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }

  & >>> .el-collapse-item__arrow {
    display: none;
  }

  & >>> .el-collapse-item__wrap {
    font-size: 14px;
    border: 0;
    padding-left: 40px;
    padding-right: 20px;
    display: none;
  }

  & >>> .el-collapse-item__content {
    padding-bottom: 30px;
    padding-top: 20px;
  }

  &.existPre {
    box-shadow: 0 4px 10px #fbf2e4;

    & >>> .el-collapse-item__header {
      background-color: #67c23a;

      &.is-active {
        border-bottom-color: #fff;
      }

      span {
        background-color: #499d20;
      }
    }

    &.is-active >>> .el-collapse-item__arrow {
      transform: rotate(-90deg);
    }

    & >>> .el-collapse-item__arrow {
      display: none;
      font-size: 20px;
      font-weight: bold;
      transform: rotate(90deg);
      margin-right: 20px;
    }
  }

  .inner {
    border: 1px dashed #67c23a;
    padding: 5px 12px;

    ul {
      padding-left: 0;
      margin: 0;
      font-size: 14px;
      color: #353535;

      li {
        list-style: none;

        &.item {
          color: #e6a23d;
          font-weight: bold;
          cursor: pointer;

          &::after {
            content: '';
            display: inline-block;
            width: 16px;
            height: 16px;
            vertical-align: middle;
            margin-bottom: 4px;
            margin-left: 5px;
            background: url(../../../../../assets/image/star.png) no-repeat;
            background-size: 100% 100%;
          }
        }
      }
    }
  }

  .existPre-inner {
    border: 1px dashed #f3d19e;
  }
}

.front-relationship {
  margin-top: 42px;

  & > p {
    font-size: 18px;
    color: #3052e8;
    font-weight: bold;
    height: 50px;
    line-height: 50px;
    padding: 0 10px;
    margin: 0;
    background-color: #d7dbe4;

    &::before {
      content: '';
      width: 5px;
      height: 22px;
      border-radius: 20px;
      margin-right: 7px;
      display: inline-block;
      margin-bottom: -4px;
      background-color: #3052e8;
    }
  }

  .fron-main {
    background-color: #f3f6f9;
    overflow: hidden;
    padding: 15px 25px;

    .fron-box {
      div {
        &::after {
          content: '';
          display: block;
          clear: both;
        }

        p {
          font-size: 16px;
          color: #3052e8;
          font-weight: bold;
          float: left;
        }

        .el-button {
          border: 0;
          padding: 0;
          width: 18px;
          height: 18px;
          margin-top: 15px;
          margin-left: 7px;
          background: url(../../../../../assets/image/tipMsg.png);
          background-size: 100% 100%;
        }

        .el-button:hover {
          background: url(../../../../../assets/image/tipMsg_active.png);
          background-size: 100% 100%;
        }
      }

      section {
        color: #353535;
        font-size: 16px;
        line-height: 26px;
      }
    }
  }
}
</style>
