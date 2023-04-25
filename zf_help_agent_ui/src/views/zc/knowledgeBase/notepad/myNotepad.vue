<template>
  <div class="notePad">
    <el-row :gutter="20">
      <el-col :span="10">
        <div class="grid-content grid-left">
          <h3>全部记事</h3>
          <div
            class="bread-list"
            v-for="(item, index) in myNoteList"
            :key="index"
          >
            <template v-if="item.shareFlag === 0">
              <div @click="handleView(item)" class="bread-item">
                <p>
                  {{ item.knowledgeTitle }}:
                  {{ item.knowledgeContent }}
                </p>
                <span> {{ item.createDate }}</span>
              </div>
            </template>
            <template v-else-if="item.shareFlag === 1">
              <div
                @click="handleView(item)"
                class="bread-item bread-item-share"
              >
                <p>
                  {{ item.knowledgeTitle }}:
                  {{ item.knowledgeContent }}
                </p>
                <span> {{ item.createDate }}</span>
              </div>
            </template>
          </div>
        </div>
      </el-col>
      <el-col :span="14">
        <!-- 便签编辑 -->
        <div class="grid-content grid-right">
          <div class="bread-input" v-show="setedit">
            <div class="tr btn-qrjl">
              <el-button
                @click="submitForm"
                type="primary"
                icon="el-icon-s-promotion"
                class="el-button-sure"
                >确认记录</el-button
              >
            </div>
            <el-form
              class="dialog-table"
              ref="form"
              :model="form"
              :rules="rules"
              label-width="0px"
            >
              <div class="note-input">
                <div class="mt0">
                  <el-form-item prop="knowledgeTitle">
                    <el-input
                      placeholder="请填写标题"
                      v-model.trim="form.knowledgeTitle"
                    ></el-input>
                  </el-form-item>
                </div>
                <div class="mt20">
                  <el-form-item prop="knowledgeContent">
                    <el-input
                      type="textarea"
                      :autosize="{ minRows: 3, maxRows: 10 }"
                      placeholder="内容..."
                      maxlength="300"
                      show-word-limit
                      v-model.trim="form.knowledgeContent"
                    >
                    </el-input>
                  </el-form-item>
                </div>
                <div class="handle-title mt20">
                  <el-checkbox v-model="form.checked">是否共享</el-checkbox>
                  <div class="more-set" @click="showInput">
                    <i class="el-icon-s-operation"></i>更多设置
                  </div>
                </div>
                <div class="setShow" v-show="setShow">
                  <div class="mt10">
                    <el-select
                      @change="selectChanged"
                      v-model="dictOidvalue"
                      placeholder="请选择记事类别"
                    >
                      <el-option
                        v-for="dict in typeDictOptions"
                        :key="dict.oid"
                        :label="dict.name"
                        :value="{ value: dict.dictOid, name: dict.name }"
                      ></el-option>
                    </el-select>
                  </div>
                  <div class="mt10">
                    <el-input
                      type="textarea"
                      :autosize="{ minRows: 2, maxRows: 5 }"
                      placeholder="备注"
                      v-model.trim="form.note"
                      maxlength="100"
                      show-word-limit
                    >
                    </el-input>
                  </div>
                </div>
              </div>
            </el-form>
          </div>

          <!-- 便签详情 -->
          <div class="bread-detail" v-show="showOff">
            <div class="tr btn-write">
              <el-button
                @click="initSave"
                type="text"
                icon="el-icon-s-promotion"
                >写一下</el-button
              >
            </div>
            <div class="note-input">
              <div class="note-content">
                <div class="note-detail-text">
                  <h3>{{ form.knowledgeTitle }}</h3>
                  <p>{{ form.knowledgeContent }}</p>
                  <p>{{ form.note }}</p>
                  <p>{{ form.zslbDictName }}</p>
                </div>
                <div class="tr bottom-txt">
                  <span>
                    <template v-if="form.shareFlag === 0">
                      未分享
                    </template>
                    <template v-else-if="form.shareFlag === 1">
                      已分享
                    </template></span
                  >&nbsp;&nbsp;|&nbsp;&nbsp;{{ form.createDate }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  presonalpage,
  getOne,
  saveOrUpdate
} from "@/api/zc/knowledgeBase/notepad.js";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  data() {
    return {
      activeClass: "active",
      breadClass: "bread-item",
      shareClass: "bread-item-share",
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      myNoteList: [],
      //知识类别
      typeDictOptions: {},
      //知识分类字典oid
      dictOidvalue: "",
      // 弹出层标题
      title: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        zslbDictOid: null,
        knowledgeTitle: null
      },
      // 表单校验
      rules: {
        knowledgeTitle: [
          { required: true, message: "标题不能为空", trigger: "blur" }
        ],
        knowledgeContent: [
          { required: true, message: "内容不能为空", trigger: "blur" }
        ]
      },

      form: {
        id: "", //逻辑主键
        knowledgeTitle: "", // 知识标题
        zslbDictOid: "",
        zslbDictName: "", // 知识类别
        knowledgeContent: "", // 知识内容
        note: "", // 备注
        shareFlag: 0, // 分享标识
        createDate: "", // 登记时间
        sort: 0, // 排序号
        checked: false,
        /*  dialogImageUrl: "",
        dialogVisible: false,*/
        value: ""
      },
      setShow: false,
      showOff: false,
      setedit: true,
      defaultZslbDictOid: "",
      defaultZslbDictName: ""
    };
  },
  created() {
    this.getList();
    /** 数据字典中保存知识类别的数据项 */
    this.getDictList("ZSLB").then(response => {
      this.typeDictOptions = response.data;
      if (this.typeDictOptions.length > 0) {
        this.defaultZslbDictOid = this.typeDictOptions[0].oid;
        this.defaultZslbDictName = this.typeDictOptions[0].name;
      }
    });
  },
  methods: {
    //选择知识类别
    selectChanged(value) {
      this.form.zslbDictOid = value.value;
      this.form.zslbDictName = value.name;
      this.dictOidvalue = value.name;
    },
    /** 查询我的记事本列表 */
    getList() {
      this.loading = true;
      presonalpage(this.queryParams).then(response => {
        this.myNoteList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      let _that = this;
      /*  _that.addClass('active')*/
      /* alert(JSON.stringify(_that)    )*/
      /* _that.classList.add('active');*/
      const oid = row.id;
      getOne(oid).then(response => {
        _that.form = response.data;
        _that.setedit = false;
        _that.showOff = true;
      });
    },

    /** 切换到写记事的页面*/
    initSave() {
      let _that = this;
      _that.form = {};
      _that.setedit = true;
      _that.showOff = false;
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      if (row.id) {
        getOne(row.id).then(response => {
          _that.openInit = true;
          _that.form = response.data;
          this.dictOidvalue = response.data.zslbDictName;
        });
      } else {
        _that.openInit = true;
      }
      _that.title = row.id ? "修改记事本信息" : "新增记事本信息";
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      if (this.form.checked) {
        //判断是否分享
        this.form.shareFlag = 1;
      } else {
        this.form.shareFlag = 0;
      }
      if (this.form.zslbDictOid == "") {
        this.form.zslbDictOid = this.defaultZslbDictOid;
        this.form.zslbDictName = this.defaultZslbDictName;
      }
      _that.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdate(_that.form).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("保存成功");
              _that.openInit = false;
              setTimeout(() => {
                _that.form = {
                  id: "", //逻辑主键
                  knowledgeTitle: "", // 知识标题
                  zslbDictOid: "",
                  zslbDictName: "", // 知识类别
                  knowledgeContent: "", // 知识内容
                  note: "", // 备注
                  shareFlag: 0, // 分享标识
                  createDate: "", // 登记时间
                  sort: 0, // 排序号
                  checked: false
                };
                _that.getList();
              }, 10);
            }
          });
        }
      });
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    showInput() {
      this.setShow = true;
    }
  }
};
</script>

<style lang="scss" scoped>
.notePad .bread-list .bread-item {
  padding: 15px;
  box-sizing: border-box;
  border: 1px solid #e5e5e5;
  border-radius: 5px;
  margin-bottom: 10px;
  font-size: 12px;
  position: relative;
  cursor: pointer;
}
.notePad .bread-list .bread-item:hover,
.notePad .bread-list .bread-item.active {
  border: 1px solid #16466a;
  color: #16466a;
}
.notePad .bread-list .bread-item-share:after {
  position: absolute;
  right: 10px;
  bottom: 10px;
  content: "";
  background: url(../../../../assets/image/share-icon.png) no-repeat center;
  background-size: cover;
  width: 15px;
  height: 15px;
}
.notePad .grid-left h3 {
  font-size: 14px;
  margin: 15px 0px;
  font-weight: normal;
  position: relative;
  display: inline-block;
  cursor: pointer;
}
.notePad .grid-left h3:after {
  position: absolute;
  right: -15px;
  top: 5px;
  content: "";
  background: url(../../../../assets/image/down-icon.png) no-repeat center;
  width: 11px;
  height: 6px;
  background-size: cover;
}
.notePad .bread-item p {
  margin: 0 0 5px 0px;
  line-height: 1.5;
}
.notePad .bread-item span {
  color: #aaa;
}
.notePad .grid-right .el-button-sure {
  font-size: 12px;
  background-color: #0b61a3;
  border: 1px solid #0b61a3;
}
.notePad .grid-right {
  position: relative;
}
.notePad .grid-right .tr {
  text-align: right;
}
.notePad .grid-right .el-button-sure:hover {
  background-color: #1271ba;
  border: 1px solid #1271ba;
}
.notePad .note-input {
  margin-top: 13px;
  border: 1px solid #acb2bd;
  padding: 40px 20px 20px 20px;
  box-sizing: border-box;
  border-radius: 5px;
  position: relative;
}
.notePad .note-input:before {
  position: absolute;
  left: 5px;
  top: 5px;
  content: "";
  background: url(../../../../assets/image/note-icon.png) no-repeat left top;
  background-size: cover;
  width: 98%;
  height: 13px;
}
.notePad .more-set {
  color: #333333;
  display: inline-block;
  vertical-align: middle;
  margin-left: 15px;
  cursor: pointer;
  font-size: 12px;
}
.notePad .more-set i {
  color: #999;
  display: inline-block;
  vertical-align: top;
  margin-right: 5px;
}
.notePad .el-select {
  width: 100%;
}
.notePad .note-detail-text h3 {
  text-align: center;
  color: #16466a;
  font-size: 16px;
  margin-bottom: 20px;
}
.notePad .note-detail-text p {
  font-size: 12px;
  color: #666;
  margin-bottom: 5px;
  line-height: 1.5;
  text-indent: 24px;
}
.notePad .bottom-txt {
  margin-top: 10px;
  color: #aaa;
  font-size: 12px;
}
.notePad .bottom-txt > span {
  color: #0b61a3;
}

.dialog-table .el-form-item {
  margin-bottom: 0;
}
.el-form-item--small .el-form-item_content {
  margin-left: 0px !important;
}
.mt20 {
  margin-top: 20px;
}
.notePad >>> .el-textarea .el-input__count {
  background-color: inherit !important;
}
</style>
