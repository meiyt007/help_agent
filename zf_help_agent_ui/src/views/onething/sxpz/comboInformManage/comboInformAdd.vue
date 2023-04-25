/*
* @Description:一件事告知单新增和修改页面
* @Author: dxl
**/
<template>
  <div>
    <el-form ref="form" :model="form" :rules="rules" label-width="0">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table" v-loading="loading">
          <colgroup>
            <col width="20%" />
            <col width="45%" />
            <col width="10%" />
            <col width="25%" />
          </colgroup>
          <tr>
            <td>
              <i class="require">*</i><b>所属主题：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="comboDirectoryOid">
                <span v-if="showDirectoryName" style="margin-right: 20px">{{comboDirectoryName}}</span>
                <el-button type="primary" size="mini" @click="selectComboDirectory">点击选择主题</el-button>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <i class="require">*</i><b>告知单标题：</b>
            </td>
            <td>
              <el-form-item prop="informTitle">
                <el-input v-model.trim="form.informTitle" placeholder="请输入告知单标题" maxlength="100" show-word-limit />
              </el-form-item>
            </td>
            <td>
              <i class="require">*</i><b>排序号：</b>
            </td>
            <td>
              <el-form-item prop="sort">
                <el-input-number v-model="form.sort" :min="1" :max="9999" />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <i class="require">*</i><b>告知单内容：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="informContent" style="height: 250px">
                <quill-editor :value="form.informContent" v-model="form.informContent">
                </quill-editor>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
    <div slot="footer" align="center">
      <el-button type="primary" @click="submitForm">确 定
      </el-button>
      <el-button @click="cancel">关 闭</el-button>
    </div>

    <!--一件事主题选择弹窗开始-->
    <el-dialog v-dialog-drag :visible.sync="openComboDirectorySelect" v-if="openComboDirectorySelect"
      :close-on-click-modal="false" :title="title" width="1100px" height="700px" scrollbar append-to-body>
        <combo-directory-select @select-directory="selectDirectory" :industryType="industryType"
          @combo-directory-select-close="closeComboDirectorySelect">
        </combo-directory-select>
    </el-dialog>
    <!--一件事主题选择弹窗结束-->
  </div>
</template>

<script>
  import {
    save,
    init
  } from "@/api/onething/sxpz/inform/comboInform.js";
  import {
    queryAllDirectoryPublishByIndustryType
  } from "@/api/onething/sxpz/comboDirectory";
  import quillEditor from "@/components/Editor/index.vue";
  import comboDirectorySelect from "@/views/onething/sxpz/comboInformManage/comboDirectorySelect.vue";

  export default {
    components: {
      quillEditor,
      comboDirectorySelect
    },
    name: "ComboInformAdd",
    //定义获取父类传过来值的格式
    props: ["comboInformOid","industryType"],
    data() {
      let validateComboDirectorySlect = (rule, value, callback) => {
        if (this.form.comboDirectoryOid === "") {
          callback(new Error('请选择主题'));
        } else {
          callback();
        }
      };
      return {
        // 遮罩层
        loading: false,
        //选项值列表
        factorOptValList: [],
        comboDirectoryOid: "",
        comboDirectoryList: "",
        //
        // 表单参数
        form: {
          sort: "",
          informContent: "",
          comboDirectoryOid: "",
        },
        // 一件事主题选择
        openComboDirectorySelect: false,
        // 显示主题
        showDirectoryName: false,
        // 弹窗主题
        title: "",
        // 主題名称
        comboDirectoryName: "",
        rules: {
          comboDirectoryOid: [{
            required: true,
            validator: validateComboDirectorySlect,
            trigger: 'blur'
          }],
          informContent: [{
            required: true,
            message: "告知单内容不能为空",
            trigger: "blur"
          }, ],
          informTitle: [{
            required: true,
            message: "告知单标题不能为空",
            trigger: "blur"
          }, ],
          sort: [{
            required: true,
            message: "排序不能为空",
            trigger: "blur"
          }, ]
        },
        //富文本内容
        content: "",
      };
    },
    watch: {
      "form.preFactorOid": function (val) {
        //跟随要素选项值
       // this.getPreFactorOptValOptions(val);
      },
    },
    created() {
     // this.getComboDirectoryList();

      if (this.comboInformOid !== undefined) {
        this.getInit();
      }
    },
    methods: {
      /** 查询一件事主题列表 */
      getComboDirectoryList() {
        this.loading = true;
        queryAllDirectoryPublishByIndustryType(this.industryType).then(response => {
          this.comboDirectoryList = response.data;
          this.loading = false;
        });
      },
      getInit() {
        this.loading = true;
        init(this.comboInformOid).then((response) => {
          this.form = response.data;
          this.comboDirectoryName = this.form.comboDirectoryName;
          this.loading = false;
          this.showDirectoryName = true;
        });
      },
      /*getPreFactorOptions() {
        this.loading = true;
        getPreFactorOptions(this.lableOid, this.elementOid).then((response) => {
          this.preFactorOptions = response.data;
          this.loading = false;
        });
      },
      getPreFactorOptValOptions(preFactorOid) {
        if (preFactorOid) {
          getPreFactorOptValOptions(preFactorOid).then((response) => {
            this.preFactorOptValOptions = response.data;
          });
        } else {
          this.preFactorOptValOptions = [];
          this.form.preFactorOptVal = null;
        }
      },*/
      // 单选类型只能选择当前一个
      /*changeCheckFlag(index, elementType) {
        if (elementType == 4 || elementType == 3) {
          this.factorOptValList.forEach((ser) => {
            ser.defaultCheckedFlag = false;
          });
          this.factorOptValList[index].defaultCheckedFlag = true;
        }
      },*/
      // 关闭按钮
      cancel() {
        this.$emit("init-close");
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate((valid) => {
          if (valid) {
            save(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功！");
                this.cancel();
              }
            });
          }
        });
      },
      /*主题选择*/
      selectComboDirectory() {
        this.openComboDirectorySelect = true;
        this.title = "主题选择";
      },
      /*关闭主题选择弹窗*/
      closeComboDirectorySelect() {
        this.openComboDirectorySelect = false;
      },
      /*选择主题*/
      selectDirectory(comboDirectoryOid, comboDirectoryName) {
        this.form.comboDirectoryOid = comboDirectoryOid;
        this.comboDirectoryName = comboDirectoryName;
        this.showDirectoryName = true;
      }
    },
  };

</script>
<style lang="scss" scoped>
  .el-form-item ::v-deep .editor {
    height: auto;
  }

  .el-form-item ::v-deep .ql-toolbar.ql-snow+.ql-container.ql-snow {
    height: 182px;
  }

</style>
