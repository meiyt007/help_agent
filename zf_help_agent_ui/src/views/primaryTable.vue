<template>
  <div class="primary-table">
    <h3><i class="el-icon-document"></i>事项标题</h3>
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item>
              <span>{{ props.row.num }}</span>
            </el-form-item>
            <el-form-item>
              <span>{{ props.row.code }}</span>
            </el-form-item>
            <el-form-item>
              <span>{{ props.row.name }}</span>
            </el-form-item>
            <el-form-item>
              <span>{{ props.row.width }}</span>
            </el-form-item>
            <el-form-item>
              <span>{{ props.row.height }}</span>
            </el-form-item>
            <el-form-item>
              <span>{{ props.row.belong }}</span>
            </el-form-item>
            <el-form-item>
              <span>{{ props.row.time }}</span>
            </el-form-item>
            <el-form-item>
              <span>
                <el-button
                  icon="el-icon-folder-add"
                  class="handle-btn"
                ></el-button>
              </span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column label="序号" prop="num" width="150"> </el-table-column>
      <el-table-column label="目录代码" prop="code" width="150">
      </el-table-column>
      <el-table-column label="目录名称" prop="name" width="150">
      </el-table-column>
      <el-table-column label="宽度" prop="width" width="150"> </el-table-column>
      <el-table-column label="高度" prop="height" width="150">
      </el-table-column>
      <el-table-column label="所属类别" prop="belong" width="150">
      </el-table-column>
      <el-table-column label="修改时间" prop="time" width="150">
      </el-table-column>
      <el-table-column label="操作" prop="handle">
        <el-button icon="el-icon-set-up" class="handle-btn"></el-button>
        <el-button icon="el-icon-edit-outline" class="handle-btn"></el-button>
        <el-button icon="el-icon-delete" class="handle-btn"></el-button>
      </el-table-column>
    </el-table>
    <!-- 表格表单 -->
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="0px"
      class="demo-ruleForm ruleForm-table mt20"
    >
      <table cellspacing="0" cellpadding="0" border="0">
        <colgroup>
          <col width="100" />
          <col width="100" />
          <col width="100" />
          <col width="100" />
          <col width="100" />
          <col width="100" />
          <col width="100" />
          <col width="100" />
          <col width="100" />
        </colgroup>
        <tr>
          <th>子项名称</th>
          <th>宽度</th>
          <th>高度</th>
          <th>存在内部矩形</th>
          <th>材料识别类型</th>
          <th>印章数量</th>
          <th>是否多页</th>
          <th>百度模板id</th>
          <th>备注</th>
        </tr>

        <tr>
          <td>
            <el-form-item prop="name">
              <el-input v-model="ruleForm.name"></el-input>
            </el-form-item>
          </td>
          <td>
            <el-form-item>
              <el-input v-model="ruleForm.width"></el-input>
            </el-form-item>
          </td>
          <td>
            <el-form-item>
              <el-input v-model="ruleForm.height"></el-input>
            </el-form-item>
          </td>
          <td>
            <el-form-item>
              <el-checkbox v-model="ruleForm.checked01"></el-checkbox>
            </el-form-item>
          </td>
          <td>
            <el-form-item>
              <el-select
                v-model="ruleForm.type"
                placeholder="请选择"
              >
                <el-option label="类型一" value="type01"></el-option>
                <el-option label="类型二" value="type02"></el-option>
              </el-select>
            </el-form-item>
          </td>
          <td>
            <el-form-item>
              <el-input v-model="ruleForm.number"></el-input>
            </el-form-item>
          </td>
          <td>
            <el-form-item>
              <el-checkbox v-model="ruleForm.checked02"></el-checkbox>
            </el-form-item>
          </td>
          <td>
            <el-form-item>
              <el-input v-model="ruleForm.id"></el-input>
            </el-form-item>
          </td>
          <td>
            <el-form-item>
              <el-input v-model="ruleForm.bz"></el-input>
            </el-form-item>
          </td>
        </tr>
      </table>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [
        {
          id: "1",
          num: "1",
          code: "2227000",
          name: "身份证",
          width: "200",
          height: "300",
          belong: "王小虎夫妻店",
          time: "2020-11-04",
          handle: "",
        },
        {
          id: "2",
          num: "2",
          code: "2227000",
          name: "身份证",
          width: "200",
          height: "300",
          belong: "王小虎夫妻店",
          time: "2020-11-04",
          handle: "",
        },
        {
          id: "3",
          num: "3",
          code: "2227000",
          name: "身份证",
          width: "200",
          height: "300",
          belong: "王小虎夫妻店",
          time: "2020-11-04",
          handle: "",
        },
        {
          id: "4",
          num: "4",
          code: "2227000",
          name: "身份证",
          width: "200",
          height: "300",
          belong: "王小虎夫妻店",
          time: "2020-11-04",
          handle: "",
        },
      ],
      ruleForm: {
        name: "",
        width: "",
        height: "",
        inside: "",
        type: "",
        number: "",
        page: "",
        id: "",
        bz: "",
        deal: "",
        checked01: "",
        checked02: "",
      },
      rules: {
        name: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
      },
    };
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
.primary-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}
.primary-table {
  padding: 20px;
  box-sizing: border-box;
}
.primary-table .handle-btn {
  border: none;
  background: none;
  padding: 0;
  font-size: 16px;
}
.primary-table .el-table,
.primary-table .el-table th {
  font-size: 13px;
}
.primary-table .el-form-item:first-child {
  margin-left: 8px;
}
.primary-table .el-form-item {
  margin-bottom: 0;
  width: 130px;
  margin-right: 0;
  margin-left: 14px;
  font-size: 13px;
}
.primary-table .el-form-item__content {
  font-size: 13px;
}
.primary-table .inputTable .el-form-item:first-child {
  margin-left: 0;
}
.primary-table .inputTable .el-form-item {
  width: 100px;
}

.ruleForm-table table {
  border: 1px solid #ebeef5;
  border-collapse: collapse;
}
.ruleForm-table table tr th {
  color: #515a6e;
  font-size: 13px;
  background-color: #f8f8f9;
  text-align: left;
}
.ruleForm-table table tr th,
.ruleForm-table table tr td {
  padding:17px 10px;
  font-size: 13px;
  color: #606266;
  border: 1px solid #ebeef5;
}
.ruleForm-table .el-form-item{
  margin-left: 0px!important;
  width: 120px;
}


</style>
