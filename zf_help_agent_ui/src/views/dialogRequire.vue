<template>
  <div class="el-table__header-wrapper dialog-table require-table">
    <h3><i class="el-icon-document"></i>验证规则配置</h3>
    <div class="select-list">
      <span
        v-for="(item, index) in selectData"
        :key="item.index"
        :class="{ current: index == num }"
        @click="selectChange(index)"
        >{{ item.title }}</span
      >
    </div>
    <div class="require-box">
      <i class="el-icon-warning-outline"></i
      >请点击目录子项中的区块名称，选择不能为空的区块。
    </div>
    <table cellspacing="0" cellpadding="0" border="0">
      <colgroup>
        <col width="5%" />
        <col width="10%" />
        <col width="20%" />
        <col width="9%" />
        <col width="9%" />
        <col width="9%" />
        <col width="9%" />
        <col width="9%" />
        <col width="9%" />
      </colgroup>
      <tr>
        <th>序号</th>
        <th>材料名称</th>
        <th>目录子项名称（目标编码）</th>
        <th>不能为空</th>
        <th>必须为空</th>
        <th>文本验证</th>
        <th>文本包含</th>
        <th>数字范围</th>
        <th>日期比较</th>
        <th>手写验证</th>
      </tr>
      <tr>
        <td>1</td>
        <td>城镇污水排入排水管网许可申请表</td>
        <td>
          <div class="add-box">
            <span>城镇污水排入排水管网许可申请表2(01252)许可申请表</span>
            <ul>
              <li>
                <span>申请单位(sqb0001601001)</span>
                <el-button
                  type="default"
                  icon="el-icon-circle-plus-outline"
                  class="btn-add"
                ></el-button>
              </li>
              <li>
                <span>申请单位(sqb0001601001)</span>
                <el-button
                  type="default"
                  icon="el-icon-circle-plus-outline"
                  class="btn-add"
                ></el-button>
              </li>
              <li>
                <span>申请单位(sqb0001601001)</span>
                <el-button
                  type="default"
                  icon="el-icon-circle-plus-outline"
                  class="btn-add"
                ></el-button>
              </li>
              <li>
                <span>申请单位(sqb0001601001)</span>
                <el-button
                  type="default"
                  icon="el-icon-circle-plus-outline"
                  class="btn-add"
                ></el-button>
              </li>
              <li>
                <span>申请单位(sqb0001601001)</span>
                <el-button
                  type="default"
                  icon="el-icon-circle-plus-outline"
                  class="btn-add"
                ></el-button>
              </li>
              <li>
                <span>申请单位(sqb0001601001)</span>
                <el-button
                  type="default"
                  icon="el-icon-circle-plus-outline"
                  class="btn-add"
                ></el-button>
              </li>
            </ul>
          </div>
        </td>
        <td>
          <div class="require-item">
            单位地址(sqb0001601002)<el-button
              type="default"
              icon="el-icon-error"
              class="btn-close"
            ></el-button>
          </div>
          <div class="require-item">
            单位地址(sqb0001601002)<el-button
              type="default"
              icon="el-icon-error"
              class="btn-close"
            ></el-button>
          </div>
          <div class="require-item">
            单位地址(sqb0001601002)<el-button
              type="default"
              icon="el-icon-error"
              class="btn-close"
            ></el-button>
          </div>
        </td>
        <td></td>
        <td>
          <div class="require-item">
            单位地址(sqb0001601002)<el-button
              type="default"
              icon="el-icon-error"
              class="btn-close"
            ></el-button>
          </div>
          <div class="require-item">
            单位地址(sqb0001601002)<el-button
              type="default"
              icon="el-icon-error"
              class="btn-close"
            ></el-button>
          </div>
        </td>
        <td></td>
        <td>
          <div class="require-item">
            单位地址(sqb0001601002)<el-button
              type="default"
              icon="el-icon-error"
              class="btn-close"
            ></el-button>
          </div>
        </td>
        <td>1</td>
        <td>
          <div class="require-item">
            单位地址(sqb0001601002)<el-button
              type="default"
              icon="el-icon-error"
              class="btn-close"
            ></el-button>
          </div>
        </td>
      </tr>
      <tr>
        <td>2</td>
        <td>排水户内部排水管网、专用检测井、污水排放口位置和口径</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
    </table>
    <div class="mt20 set-table">
      <h3><i class="el-icon-document"></i>验证规则配置</h3>
      <table cellspacing="0" cellpadding="0" border="0">
        <colgroup>
          <col width="12%" />
          <col width="12%" />
          <col width="12%" />
          <col width="12%" />
          <col width="12%" />
          <col width="12%" />
          <col width="12%" />
        </colgroup>
        <tr>
          <th>序号</th>
          <th>材料名称</th>
          <th>目录子项名称（目标编码）</th>
          <th>不能为空</th>
          <th>必须为空</th>
          <th>文本验证</th>
          <th>文本包含</th>
        </tr>
        <tr>
          <td>关联对比</td>
          <td>城镇污水排入排水管网许可申请表</td>
          <td>城镇污水排入排水管网许可申请表2</td>
          <td>sqb4000015210</td>
          <td>城镇污水排入排水管网许可申请表</td>
          <td>城镇污水排入排水管网许可申请表2</td>
          <td>
            <el-button
              type="default"
              icon="el-icon-error"
              class="btn-close btn-close-position"
            ></el-button>
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      num: 0,
      selectData: [
        {
          index: "0",
          title: "不能为空",
        },
        {
          index: "1",
          title: "必须为空",
        },
        {
          index: "2",
          title: "文本验证",
        },
        {
          index: "3",
          title: "文本包含",
        },
        {
          index: "4",
          title: "数字范围",
        },
        {
          index: "5",
          title: "手写验证",
        },
        {
          index: "6",
          title: "日期比较",
        },
        {
          index: "7",
          title: "关联对比",
        },
        {
          index: "8",
          title: "相似度",
        },
      ],
    };
  },
  methods: {
    selectChange(index) {
      this.num = index;
    },
  },
};
</script>

<style lang="scss" scoped>
.select-list span {
  display: inline-block;
  vertical-align: middle;
  color: #47657d;
  font-size: 14px;
  background-color: #f1f5f9;
  height: 40px;
  line-height: 40px;
  margin: 0px 10px 10px 0px;
  padding: 0px 35px 0px 20px;
  border-radius: 5px;
  cursor: pointer;
}
// .select-list span:first-child {
//   background-color: #fff6f1;
//   color: #2d506b;
// }
.select-list span.current {
  background-color: #4d87b5;
  color: #fff;
  position: relative;
}
.select-list span.current:after {
  content: "";
  position: absolute;
  width: 19px;
  height: 13px;
  right: 10px;
  top: 13px;
  background: url(../assets/image/check-icon.png) no-repeat center;
  background-size: cover;
}
.require-box {
  font-size: 12px;
  color: #ff0000;
  margin: 0px 0px 10px 0px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  background-color: #fff7f7;
}
.require-table table tr th {
  background-color: #f8f9fb;
}
.require-table table tr td {
  text-align: center;
  vertical-align: top;
}
.require-table table tr td:nth-of-type(2n + 1) {
  background-color: #fff;
}
.add-box ul {
  margin: 0;
  padding: 0;
}
.add-box ul li {
  list-style: none;
  text-align: left;
  height: 30px;
  line-height: 30px;
  background-color: #f8f9fb;
  border: 1px solid #f8f9fb;
  margin-top: 5px;
  text-indent: 5px;
  cursor: pointer;
  color: #0b61a3;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  position: relative;
}
.btn-add {
  display: inline-block;
  vertical-align: middle;
  border: none;
  padding: 0;
  position: absolute;
  right: 8px;
  top: 8px;
  cursor: pointer;
  display: none;
}
.add-box ul li:hover {
  border: 1px solid #0b61a3;
  background-color: #fff;
}
.add-box ul li:hover .btn-add {
  color: #0b61a3;
  display: block;
}
.require-item {
  vertical-align: top;
  padding: 20px 10px;
  box-sizing: border-box;
  background-color: #f8f9fb;
  border: 1px solid #f8f9fb;
  position: relative;
  cursor: pointer;
  margin-bottom: 5px;
}
.require-item:hover {
  background-color: #ffffff;
  border: 1px solid #0b61a3;
}
.btn-close {
  position: absolute;
  right: 3px;
  top: 3px;
  padding: 0;
  border: none;
  color: #ff0000;
}
.btn-close-position {
  position: inherit;
}
.set-table table tr td{
  vertical-align: middle;
}



</style>
