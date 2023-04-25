/** * 智能登记 * @author: liling * @date: 2021-06-09 */
<template>
  <div class="app-container">
    <el-row :gutter="25" class="scan-picture" v-if="show_1">
      <el-col :span="24" :xl="12">
        <el-upload
          class="upload-demo"
          action=""
          multiple
          :limit="5"
          :data="item"
          :http-request="uploadFiles"
          :before-upload="beforeUpload"
          :on-error="uploadError"
          :show-file-list="showFileList"
          accept="accept"
        >
          <el-button size="mini" type="primary" icon="el-icon-upload"
            >点击上传</el-button
          >
        </el-upload>

        <span class="title">正在拍照：</span>
        <div class="scan-area">
          <img
            class="scan-pic"
            src="../../../../assets/image/findPassword-bg.png"
          />
          <div class="scan-btn">
            <img
              @mouseover="showHintInfo"
              @mouseleave="hideHintInfo"
              src="../../../../assets/image/scan-btn.png"
            />
          </div>

          <p
            class="hint"
            :class="{ active: showHint }"
            @mouseover="showHintInfo"
            @mouseleave="hideHintInfo"
          >
            拍照按钮（ 快捷键：Enter ）
          </p>
        </div>
      </el-col>
      <el-col :span="24" :xl="12">
        <span class="title">扫描结果如下：</span>
        <div class="scan-result">
          <ul class="scan-list clearfix">
            <li>
              <i @click="deleteScanList($event)"></i>
              <img src="../../../../assets/image/moudle-pic.png" />
              <p>点击预览</p>
            </li>
            <li>
              <i @click="deleteScanList($event)"></i>
              <img src="../../../../assets/image/moudle-pic.png" />
              <p>点击预览</p>
            </li>
          </ul>
          <div class="scan-btn">
            <el-button type="info">重置</el-button>
            <el-button type="primary">智能分类</el-button>
          </div>
        </div>
      </el-col>
    </el-row>

    <p class="ic-hint">
      系统已自动完成材料分类，未能分类的<span> 支持手动移动 </span>到对应材料
    </p>
    <el-row :gutter="25" class="intelligent-classification" v-if="show_2">
      <el-col :span="24" :xl="14" :lg="12" :xs="14">
        <span class="title">系统自动分类：</span>
        <div class="ic-sort">
          <template v-for="(item, index) in caseMaterials">
            <div class="sort-item">
              <p>{{ index + 1 }}.{{ item.materialName }}</p>
              <ul class="sort-list clearfix">
                <template
                  v-for="(classifyRec, index) in item.industryClassifyRecList"
                >
                  <li>
                    <i @click="deleteSortOne(item, classifyRec)"></i>
                    <!--                 {{classifyRec.industryCaseAtta.originName}}
                 {{classifyRec.industryCaseAtta.fastdfsNginxUrl}}-->
                    <img :src="classifyRec.industryCaseAtta.fastdfsNginxUrl" />
                    <!--                  <img src="../../../../assets/image/moudle-pic.png">-->
                    <p>点击预览</p>
                  </li>
                </template>
              </ul>
            </div>
          </template>
        </div>
      </el-col>
      <el-col :span="24" :xl="10" :lg="12" :xs="10">
        <span class="title"
          >未能自动分类：（鼠标放到需要移动的材料上选择要移动到的分类）</span
        >
        <div class="ic-result">
          <ul class="ic-list clearfix">
            <template v-for="(unClassifyRec, index) in unClassifyRecList">
              <li
                @mouseover="showMenuInfoOne($event, unClassifyRec, '0', 0)"
                @mouseleave="hideMenuInfo"
              >
                <i @click="deleteSortTwo(unClassifyRec)"></i>
                <img :src="unClassifyRec.industryCaseAtta.fastdfsNginxUrl" />
                <!--                <img src="../../../../assets/image/moudle-pic.png">-->
                <p>点击预览</p>
              </li>
            </template>
          </ul>
          <div
            class="ic-menu"
            :style="{ top: topVal, left: leftVal }"
            @mouseover="showMenuInfo($event, '1')"
            @mouseleave="hideMenuInfo"
            v-show="showMenu"
          >
            <div class="menu-hd">移动到</div>
            <ul class="menu-cont">
              <template v-for="(item, index) in caseMaterials">
                <li @click="moveToSort(item, index + 1)">
                  {{ index + 1 }}.{{ item.materialName }}
                </li>
              </template>
            </ul>
          </div>
        </div>
      </el-col>
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" icon="el-icon-circle-check" @click="mcNext"
        >下一步</el-button
      >
    </div>
  </div>
</template>
<script>
import {
  materialClassifyPrePrial,
  deleteClassifiler,
  updateClassifiler,
  initAutoClassifierCaseFileRecUploadList
} from "@/api/onelicence/industryManager/industryCaseAccept/materialClassifiler";
import { uploadCaseMaterialFile } from "@/api/onelicence/industryManager/industryCaseAccept/updateIndustryCase";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import TempComboAccept from "../../../onething/comboManager/comboAccept/tempComboAccept";

export default {
  components: { TempComboAccept },
  inheritAttrs: false,
  name: "WindowAcceptance",
  props: ["caseOid", "classifierId"],
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      show_1: true,
      show_2: false,
      // 弹出层标题
      title: "",
      caseMaterials: [],
      classifyRecList: [],
      unClassifyRecList: [],
      //待分配的附件
      dfpAtta: {},
      fileList: [],
      labelPosition: "top",
      value: true,
      // 人工服务
      showHint: false,
      showMenu: false,
      topVal: "0px",
      leftVal: "0px",
      orderNum: 1
    };
  },
  created () {
    //this.initAutoClassifierCaseFileRecUploadList();
  },
  methods: {
    showMenuInfoOne (e, unClassifyRec, menuMark, index) {
      let _that = this;
      _that.dfpAtta = unClassifyRec;
      // console.log("showMenuInfoOne"+"**dydAtta="+JSON.stringify(unClassifyRec))
      if (menuMark == "0") {
        let mTop = e.path[0].offsetParent.offsetTop;
        let mWidth = e.path[0].offsetParent.offsetWidth;
        let mLeft = e.path[0].offsetParent.offsetLeft;
        this.topVal = mTop + mWidth / 2 + "px";
        this.leftVal = mLeft + mWidth / 2 + "px";
        this.orderNum = index;
      }
      if (e.path[3].className == "ic-result" || menuMark == "1") {
        this.showMenu = true;
      }
    },
    showMenuInfo (e, menuMark, index) {
      if (menuMark == "0") {
        let mTop = e.path[0].offsetParent.offsetTop;
        let mWidth = e.path[0].offsetParent.offsetWidth;
        let mLeft = e.path[0].offsetParent.offsetLeft;
        this.topVal = mTop + mWidth / 2 + "px";
        this.leftVal = mLeft + mWidth / 2 + "px";
        this.orderNum = index;
      }
      if (e.path[3].className == "ic-result" || menuMark == "1") {
        this.showMenu = true;
      }
    },
    //移动
    moveToSort (material, idx) {
      let _that = this;
      let classifyRec = _that.dfpAtta;
      _that.caseMaterials.forEach(caseMaterial => {
        if (caseMaterial.id == material.id) {
          let industryClassifyRecList = caseMaterial.industryClassifyRecList;
          industryClassifyRecList.push(classifyRec);
        }
      });

      var index = _that.unClassifyRecList.findIndex(classify => {
        if (classify.id == classifyRec.id) {
          return true;
        }
      });
      _that.unClassifyRecList.splice(index, 1);
      /*console.log("moveToSort"+"**dydAtta="+JSON.stringify(_that.dfpAtta));
         console.log("moveToSort"+"**materia="+JSON.stringify(e))*/
    },
    //已分类的改动到未分类处
    deleteSortOne (material, classifyRec) {
      /* console.log("deleteSortOne"+"**Material="+JSON.stringify(material));
        console.log("deleteSortOne"+"***classifyRec="+JSON.stringify(classifyRec));*/
      let _that = this;
      _that.caseMaterials.forEach(caseMaterial => {
        if (caseMaterial.id == material.id) {
          let industryClassifyRecList = caseMaterial.industryClassifyRecList;
          var index = industryClassifyRecList.findIndex(classify => {
            if (classify.id == classifyRec.id) {
              return true;
            }
          });
          industryClassifyRecList.splice(index, 1);
        }
      });
      _that.unClassifyRecList.push(classifyRec);
    },
    //删除未分类的附件
    deleteSortTwo (classifyRec) {
      let _that = this;
      var index = _that.unClassifyRecList.findIndex(classify => {
        if (classify.id == classifyRec.id) {
          return true;
        }
      });
      _that.unClassifyRecList.splice(index, 1);
    },
    //关闭分类页面把数据带到材料上传页面
    mcNext () {
      let _that = this;
      let industryClassifyRecVo = {};
      industryClassifyRecVo.caseOid = _that.caseOid;
      industryClassifyRecVo.classifierId = _that.classifierId;
      industryClassifyRecVo.caseMaterials = _that.caseMaterials;
      industryClassifyRecVo.classifyRecList = _that.classifyRecList;
      industryClassifyRecVo.unClassifyRecList = _that.unClassifyRecList;

      let unLength = _that.unClassifyRecList.length;
      if (unLength > 0) {
        this.$confirm(
          "还有附件未分类，是否确认离开,离开后未分类的材料将会被删除？",
          "警告",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
        )
          .then(() => {
            deleteClassifiler(industryClassifyRecVo).then(response => {
              console.log("updateClassifiler****" + JSON.stringify(response));
              this.$emit("father-click", JSON.stringify(_that.caseMaterials)); //调用父页面关闭子页面的功能
            });
          })
          .catch(() => { });
      } else {
        deleteClassifiler(industryClassifyRecVo).then(response => {
          console.log("updateClassifiler****" + JSON.stringify(response));
          this.$emit("father-click", JSON.stringify(_that.caseMaterials)); //调用父页面关闭子页面的功能
        });
      }

      /* return;
        updateClassifiler(industryClassifyRecVo).then(response => {
          console.log("updateClassifiler****"+JSON.stringify(response))
           this.$emit('father-click',JSON.stringify(_that.caseMaterials));//调用父页面关闭子页面的功能
        })*/
    },
    deleteScanList (e) {
      e.path[1].remove();
    },
    hideMenuInfo () {
      this.showMenu = false;
    },

    showHintInfo () {
      this.showHint = true;
    },
    hideHintInfo () {
      this.showHint = false;
    },
    /** 查询窗口受理事项列表 */
    initAutoClassifierCaseFileRecUploadList () {
      let _that = this;
      initAutoClassifierCaseFileRecUploadList(_that.caseOid).then(response => {
        // console.log(JSON.stringify(response))
        _that.classifyRecList = response.data.classifyRecList;
        _that.unClassifyRecList = response.data.unClassifyRecList;
        _that.caseMaterials = response.data.caseMaterials;
      });
    },

    /** 失败后返回 */
    uploadError (resp) {
      this.msgError("文件上传失败");
    },

    /** 上传附件 */
    uploadFiles (file) {
      let formData = new FormData();
      formData.append("files", file.file);
      uploadCaseMaterialFile(formData).then(response => {
        if (response.data != "") {
          console.log("uploadCaseMaterialFile" + JSON.stringify(response.data));
          alert(JSON.stringify(response.data));
        }
      });
    },
    /** 上传附件请求操作 */
    beforeUpload (file) {
      let isRightSize = file.size / 1024 / 1024 < 10;
      if (!isRightSize) {
        this.$message.error("文件大小超过 10MB");
        return false;
      }
      if (file.size == 0) {
        this.$message.error("不允许上传空文件");
        return false;
      }
      this.fileList.push(file);
      return isRightSize;
    },
    //预览附件
    viewFile (attaOid) {
      let item = {
        show: true,
        attaOid: attaOid
      };
      this.viewDialogOptions.push(item);
    },
    //关闭预览附件
    closeFileView () {
      this.viewDialogOptions.pop();
    }
  }
};
</script>
<style lang="scss" scoped>
.el-tree > .el-tree-node {
  min-width: 100%;
  display: inline-block;
}

.process-box {
  padding: 15px;
  box-sizing: border-box;
  text-align: left;
}

.process-box .step-title {
  font-size: 14px;
  color: #333;
}

.process-box .step-title span {
  display: inline-block;
  vertical-align: middle;
  background: url(../../../../assets/image/step-lastbg.png) no-repeat center;
  width: 100px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  cursor: pointer;
}

.process-box .step-title span:first-child {
  background: url(../../../../assets/image/step-firstbg.png) no-repeat center;
  width: 100px;
  height: 30px;
}

.process-box .step-title span.active {
  background: url(../../../../assets/image/step-lastbg-active.png) no-repeat
    center;
  width: 100px;
  height: 30px;
  color: #fff;
}

.process-box .step-title span:first-child.active {
  background: url(../../../../assets/image/step-firstbg-active.png) no-repeat
    center;
  width: 100px;
  height: 30px;
  color: #fff;
}

.process-box .title {
  font-size: 12px;
  color: #333;
  font-weight: normal;
  margin-top: 30px;
  text-align: left;
}

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

.select-list span:first-child {
  background-color: #fff6f1;
  color: #2d506b;
}

.select-list span.current {
  background-color: #4d87b5;
  color: #fff;
  position: relative;
}

.select-list span.current:after {
  content: '';
  position: absolute;
  width: 19px;
  height: 13px;
  right: 10px;
  top: 13px;
  background: url(../../../../assets/image/check-icon.png) no-repeat center;
  background-size: cover;
}

.option-box .option-title {
  margin-top: 20px;
  font-size: 12px;
  color: #333;
}

.option-box .option-item {
  margin-right: 10px;
}

.option-box .option-item,
.option-box .chose-item {
  display: inline-block;
  vertical-align: top;
}

.option-box .chose-item {
  color: #4d87b5;
}

.process-box table {
  width: 100%;
}

.process-box table {
  border: 1px solid #dfe6ec;
  border-collapse: collapse;
}

.process-box table tr td,
.process-box table tr th {
  border: 1px solid #dfe6ec;
  padding: 17px 10px;
  font-size: 12px;
  color: #515a6e;
  text-align: right;
}

.process-box table tr td:nth-of-type(2n + 1) {
  background-color: #f8f8f9;
}

.process-box table tr td:nth-of-type(2n) {
  color: #606266;
  text-align: left;
}

.process-box table.data-table tr td,
.process-box table.data-table tr th {
  text-align: center;
  padding: 12px 10px;
}

.process-box table.data-table tr th {
  background-color: #f8f9fb;
}

.process-box table.data-table tr td:nth-of-type(2n + 1) {
  background: none;
}

.process-box table.data-table .bage-necessery {
  color: #ff3000;
  background-color: #fff2f2;
  height: 30px;
  line-height: 30px;
  padding: 0px 20px;
  border-radius: 20px;
  display: inline-block;
}

.process-box .check-list {
  border-bottom: 1px solid #e5e5e5;
  margin: 0px 20px;
  padding-left: 10px;
  box-sizing: border-box;
  padding-bottom: 20px;
}

.process-box .check-list h3 {
  font-size: 12px;
  color: #333;
}

.process-box .el-form-item {
  margin-bottom: 0;
}

.require {
  color: #ff0000;
  font-style: normal;
  font-size: 14px;
  display: inline-block;
  vertical-align: middle;
  margin-right: 5px;
}

.process-box .next-btn {
  display: block;
  margin: 30px auto;
  font-size: 14px;
}

.process-box .data-box h4 {
  font-size: 12px;
  color: #333;
  margin: 20px 0px 10px 0px;
}

.process-box .btn-wrap {
  text-align: center;
  margin: 30px 0;
}

.process-box .form-box-inline {
  display: inline-block;
  vertical-align: middle;
  border: 1px solid #2d506b;
  width: 100%;
  padding: 30px;
  box-sizing: border-box;
}

.process-box .el-button--info {
  background-color: #3b5f7b;
}

.process-box .el-button--info:hover {
  background-color: #426886;
}

.process-box .form-box-inline {
  position: relative;
}

.process-box .form-box-inline .btn-write {
  position: absolute;
  right: 20px;
  top: 20px;
  font-size: 12px;
  padding: 8px 15px;
}

.process-box .form-box-inline ul {
  margin: 0;
  padding: 0;
}

.process-box .form-box-inline ul li {
  padding: 10px 20px;
  background-color: #f3f6f9;
  border-radius: 10px;
  margin-bottom: 10px;
  list-style: none;
}

.process-box .form-box-inline ul li > .icon {
  display: inline-block;
  vertical-align: middle;
  width: 36px;
  height: 36px;
  line-height: 36px;
  text-align: center;
  border-radius: 100%;
  color: #fff;
  font-size: 20px;
}

.process-box .form-box-inline ul li > .bdbm-icon {
  background-color: #a8cfec;
}

.process-box .form-box-inline ul li > .bdsm-icon {
  background-color: #e6b893;
}

.process-box .form-box-inline .input-text {
  display: inline-block;
  vertical-align: middle;
  padding-left: 10px;
  font-size: 12px;
  color: #333;
}

.process-box .form-box-inline > h4 {
  font-weight: normal;
  color: #003259;
  font-size: 14px;
}

.process-box .form-box-inline .input-text > h4 {
  margin: 0px 0px 5px 0px;
  color: #3b5f7b;
  font-weight: normal;
}

.process-box .form-box-inline .input-text > p {
  margin: 0px;
}

.process-box .form-btn-group {
  position: absolute;
  right: 20px;
  top: 20px;
}

.process-box .form-btn-group .btn {
  border: 1px solid #097dd6;
  color: #097dd6;
  font-size: 12px;
  padding: 8px 15px;
}

.handle-data {
  padding: 25px 40px;
  background-color: #f8f9fb;
  position: relative;
}

.handle-data ul {
  padding: 0px;
  margin: 0;
}

.handle-data ul li {
  list-style: none;
  text-align: left;
  margin-top: 10px;
}

.right-btn-group {
  position: absolute;
  right: 130px;
  top: 10px;
}

.right-btn-group-two {
  position: absolute;
  right: 10px;
  top: 10px;
}

.right-btn-group .el-button {
  padding: 6px 8px;
  font-size: 12px;
  margin-left: 5px;
  background-color: #0ba2b8;
  border: 1px solid #0ba2b8;
}

.right-btn-group .el-button:last-child {
  background-color: #47657d;
  border: 1px solid #47657d;
}

.right-btn-group .el-button:last-child:hover {
  background-color: #4d708b;
  border: 1px solid #4d708b;
}

.qdcg-success {
  font-size: 12px;
  color: #00b45e;
  margin-top: 5px;
  text-decoration: underline;
}

.qdcg-fail {
  font-size: 12px;
  color: #ff0000;
  margin-top: 5px;
  text-decoration: underline;
}

.qdcg-success > img,
.qdcg-fail > img {
  display: inline-block;
  vertical-align: middle;
  margin: -2px 5px 0 0;
}

.qdcg-text img,
.qdcg-text p {
  display: inline-block;
  vertical-align: middle;
}

.qdcg-text img {
  margin-right: 5px;
}

.qdcg-text p {
  margin: 0;
}

.qdcg-text p > span {
  font-size: 12px;
  color: #999;
  padding-left: 20px;
}

.qdcg-btn .el-button {
  color: #333;
}

.qdcg-btn {
  margin-top: -5px;
}

.input-number {
  text-align: left;
  margin-top: 20px;
  margin-bottom: 10px;
}

.input-number-label {
  display: inline-block;
  vertical-align: middle;
  margin-right: 20px;
}

.rightSideBar {
  position: fixed;
  right: 0px;
  bottom: 100px;
  box-shadow: -1px 1px 10px #ddd;
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
}

.rightSideBar .sideItem {
  width: 42px;
  height: 42px;
  line-height: 42px;
  text-align: center;
  color: #0b76c7;
  background-color: #fff;
  border-top-left-radius: 5px;
  cursor: pointer;
  font-size: 20px;
  position: relative;
  -webkit-transition: all 0.3s ease-in-out 0.1s;
  transition: all 0.3s ease-in-out 0.1s;
  z-index: 2;
}

.rightSideBar .sideItem:last-child {
  border-top-left-radius: 0px;
  border-bottom-left-radius: 5px;
}

.rightSideBar .sideItem:hover {
  background-color: #0b76c7;
  color: #fff;
  border-top-left-radius: 0px;
  border-bottom-left-radius: 0px;
}

.rightSideBar .sideItem .sideText {
  position: absolute;
  left: 42px;
  top: 0px;
  height: 42px;
  line-height: 42px;
  padding: 0px 10px 0px 20px;
  border-top-left-radius: 30px;
  border-bottom-left-radius: 30px;
  color: #fff;
  background-color: #0b76c7;
  font-size: 14px;
  -webkit-transition: left 0.3s ease-in-out 0.1s;
  transition: left 0.3s ease-in-out 0.1s;
  z-index: 1;
}

.rightSideBar .sideItem:hover .sideText {
  left: -86px;
}

.title-detail {
  text-align: center;
}

.tableInfo {
  width: 100%;
}

.as-img {
  width: 60px;
  height: 43px;
  position: absolute;
  top: 65px;
  right: 12px;
  cursor: pointer;
}

.artificial-service {
  background-color: #edf0f6;
  min-height: 500px;

  .as-hd {
    background-color: #d9dfea;
    height: 44px;
    line-height: 44px;
    padding: 0 16px 0 18px;

    .as-hd-lt {
      img {
        vertical-align: middle;
        cursor: pointer;
      }

      span {
        vertical-align: middle;
      }
    }

    .as-hd-rt {
      .el-switch {
        position: unset;

        >>> .el-switch__core {
          background: #adb6c6;
        }

        >>> .el-switch__label {
          position: absolute;
          color: #7e8aa1;
          display: none;
        }

        >>> .el-switch__label * {
          font-size: 15px !important;
        }

        >>> .el-switch__label--right {
          z-index: 1;
          right: 118px;
        }

        >>> .el-switch__label--left {
          z-index: 1;
          right: 108px;
        }

        >>> .el-switch__label.is-active {
          display: block;
        }
      }

      .el-switch.is-checked >>> .el-switch__core {
        background-image: linear-gradient(to right, #00bdfa, #0084e2, #0084e2);
      }

      img {
        margin-bottom: -8px;
        cursor: pointer;
        margin-left: 20px;
      }
    }
  }

  .as-main {
    padding: 15px;
    width: 100%;
    height: 632px;
    overflow-y: scroll;

    .as-ask {
      .generally-one {
        img {
          vertical-align: middle;
          width: 34px;
          height: 34px;
          border-radius: 50%;
        }

        span {
          color: #808ca1;
          font-size: 14px;
        }
      }

      .cont {
        background-color: #fff;
        color: #201e1e;
        display: inline-block;
        font-size: 15px;
        padding: 12px 21px;
        margin-left: 45px;
        border-top-right-radius: 8px;
        border-bottom-left-radius: 8px;
        border-bottom-right-radius: 8px;
      }
    }

    .as-answer {
      .system {
        width: 100%;
        text-align: right;

        img {
          vertical-align: middle;
          width: 34px;
          height: 34px;
          border-radius: 50%;
        }

        span {
          color: #1890ff;
          font-size: 14px;
        }
      }

      .items {
        background-color: #50abff;
        color: #fff;
        font-size: 15px;
        padding: 12px 21px;
        margin-right: 45px;
        border-top-left-radius: 8px;
        border-bottom-left-radius: 8px;
        border-bottom-right-radius: 8px;

        ul {
          padding-left: 0;

          li {
            list-style: none;
            background-color: #3e9df4;
            border-radius: 5px;
            padding: 10px;
            min-width: 160px;
            margin-top: 5px;
          }

          // li:hover {
          //   color: #ffc600;
          //   background-color: #1178f6;
          //   cursor: pointer;
          // }
        }
      }
    }
  }

  .as-main::-webkit-scrollbar {
    width: 6px;
  }

  .as-main::-webkit-scrollbar-track {
    background-color: #dfe2ea;
  }

  .as-main::-webkit-scrollbar-thumb {
    border-radius: 5px;
    background-color: #bec7d9;
  }
}

.scan-picture {
  padding: 0 10px 10px;

  .title {
    font-size: 18px;
    color: #333;
    margin-bottom: 20px;
    display: block;
  }

  .scan-area {
    background-color: #000;
    height: 700px;
    position: relative;

    .scan-pic {
      width: 100%;
      height: 587px;
    }

    .scan-btn {
      background-color: #030719;
      text-align: center;
      position: absolute;
      width: 100%;
      height: 114px;
      bottom: 0;
      line-height: 13;

      img {
        width: 80px;
        height: 80px;
        cursor: pointer;
      }
    }

    .hint {
      width: 253px;
      height: 49px;
      color: #fff;
      font-size: 17px;
      background: url(../../../../assets/image/scan-bg.png) no-repeat;
      background-size: 100% 100%;
      line-height: 55px;
      text-align: center;
      position: absolute;
      bottom: -22px;
      left: 50%;
      margin-left: -130px;
      display: none;
    }

    .hint.active {
      display: block;
    }
  }

  .scan-result {
    position: relative;
    height: 700px;
    padding-top: 38px;
    background-color: #eff1f5;

    .scan-list {
      padding: 0 32px;

      li {
        list-style: none;
        width: 86px;
        height: 86px;
        position: relative;
        float: left;
        margin-right: 25px;

        i {
          width: 22px;
          height: 23px;
          display: block;
          position: absolute;
          top: 0;
          right: 0;
          background: url(../../../../assets/image/delete.png) no-repeat;
          background-size: 100% 100%;
          cursor: pointer;
        }

        img {
          width: 84px;
          height: 84px;
        }

        p {
          background-color: #96989c;
          color: #fff;
          text-align: center;
          width: 84px;
          height: 22px;
          line-height: 22px;
          position: absolute;
          bottom: 0;
          margin: 0;
          cursor: pointer;
        }
      }

      li:nth-child(6n) {
        margin-right: 0;
      }

      li:hover {
        border: 1px solid #1890ff;
      }
    }

    .scan-btn {
      background-color: #e4e6ec;
      text-align: center;
      position: absolute;
      width: 100%;
      height: 114px;
      bottom: 0;
      line-height: 8;

      button {
        width: 101px;
        height: 35px;
      }
    }
  }
}

.ic-hint {
  background-color: #fbefde;
  display: inline-block;
  color: #965903;
  font-size: 13px;
  padding: 10px 15px;

  span {
    color: #6f340d;
  }
}

.ic-hint::before {
  content: '';
  display: inline-block;
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background-color: #ffb349;
  vertical-align: middle;
  margin-right: 8px;
}

.intelligent-classification {
  padding: 0 10px 10px;

  .title {
    font-size: 16px;
    color: #7a7d86;
    margin-bottom: 20px;
    display: block;
  }

  .ic-sort {
    background-color: #eef0f6;
    height: 540px;
    position: relative;
    border: 1px dashed #c5c9d4;
    padding: 20px 15px 20px 30px;
    overflow-y: scroll;

    .sort-item {
      margin-bottom: 12px;

      > p {
        color: #545868;
        font-size: 15px;
        display: inline-block;
        margin: 0;
        border-bottom: 28px solid #d7dae5;
        border-left: 7px solid #d7dae5;
        border-right: 10px solid transparent;
        height: 0;
        line-height: 2;
        padding-right: 18px;
      }

      .sort-list {
        padding: 7px 14px 17px;
        background-color: #fff;
        margin: 0;

        li {
          list-style: none;
          width: 86px;
          height: 86px;
          position: relative;
          float: left;
          margin-right: 5px;
          margin-top: 10px;

          i {
            content: '';
            width: 22px;
            height: 23px;
            display: block;
            position: absolute;
            top: 0;
            right: 0;
            background: url(../../../../assets/image/delete.png) no-repeat;
            background-size: 100% 100%;
            cursor: pointer;
          }

          img {
            width: 84px;
            height: 84px;
          }

          p {
            background-color: rgba(0, 0, 0, 0.3);
            color: #fff;
            text-align: center;
            width: 84px;
            height: 22px;
            line-height: 22px;
            position: absolute;
            bottom: 0;
            margin: 0;
            cursor: pointer;
          }
        }

        li:nth-child(8n) {
          margin-right: 0;
        }
      }
    }
  }

  .ic-sort::-webkit-scrollbar {
    width: 8px;
  }

  .ic-sort::-webkit-scrollbar-track {
    background-color: #eef0f6;
  }

  .ic-sort::-webkit-scrollbar-thumb {
    border-radius: 10px;
    background-color: #bec7d9;
  }

  .ic-result {
    position: relative;
    height: 540px;
    background-color: #eff1f5;
    border: 1px dashed #d1d4dd;

    .ic-list {
      padding: 15px 30px;
      margin: 0;

      li {
        list-style: none;
        width: 86px;
        height: 86px;
        position: relative;
        float: left;
        margin-right: 25px;
        margin-bottom: 20px;

        i {
          content: '';
          width: 22px;
          height: 23px;
          display: block;
          position: absolute;
          top: 0;
          right: 0;
          background: url(../../../../assets/image/delete.png) no-repeat;
          background-size: 100% 100%;
          cursor: pointer;
        }

        img {
          width: 84px;
          height: 84px;
        }

        p {
          background-color: #96989c;
          color: #fff;
          text-align: center;
          width: 84px;
          height: 22px;
          line-height: 22px;
          position: absolute;
          bottom: 0;
          margin: 0;
          cursor: pointer;
        }
      }

      li:nth-child(5n) {
        margin-right: 0;
      }

      li:hover {
        border: 1px solid #1890ff;
      }
    }

    .ic-menu {
      width: 195px;
      border: 1px solid #d1d4dd;
      font-size: 15px;
      position: absolute;
      top: 58px;
      left: 72px;

      .menu-hd {
        background-color: #eaecf1;
        padding: 15px 13px;
      }

      .menu-cont {
        background-color: #fff;
        border-top: 1px solid #d1d4dd;
        padding: 10px 0;
        margin: 0;
        overflow-y: scroll;
        height: 150px;

        li {
          list-style: none;
          padding: 13px 10px;
          cursor: pointer;
        }

        li:hover {
          background-color: #3c8aff;
          color: #fff;
        }
      }

      .menu-cont::-webkit-scrollbar {
        width: 6px;
      }

      .menu-cont::-webkit-scrollbar-track {
        background-color: #fff;
      }

      .menu-cont::-webkit-scrollbar-thumb {
        border-radius: 5px;
        background-color: #bec7d9;
      }
    }
  }
}

.dialog-footer {
  text-align: center;
}
</style>
