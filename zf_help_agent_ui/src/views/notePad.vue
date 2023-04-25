<template>
  <div class="notePad">
    <el-row :gutter="20">
      <el-col :span="10">
        <div class="grid-content grid-left">
          <h3>全部便签</h3>
          <div class="bread-list">
            <div class="bread-item bread-item-share active">
              <p>
                这家美媒刊文:特朗普"已经赢了"香港法官头香港法官假发该摘下来了!
              </p>
              <span>2020年11月03日 15:03</span>
            </div>
            <div class="bread-item">
              <p>
                这家美媒刊文:特朗普"已经赢了"香港法官头香港法官假发该摘下来了!
              </p>
              <span>2020年11月03日 15:03</span>
            </div>
            <div class="bread-item bread-item-share">
              <p>
                这家美媒刊文:特朗普"已经赢了"香港法官头香港法官假发该摘下来了!
              </p>
              <span>2020年11月03日 15:03</span>
            </div>
            <div class="bread-item">
              <p>
                这家美媒刊文:特朗普"已经赢了"香港法官头香港法官假发该摘下来了!
              </p>
              <span>2020年11月03日 15:03</span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="14">
        <div class="grid-content grid-right">
          <!-- 便签编辑 -->
          <div class="bread-input">
            <div class="tr btn-qrjl">
              <el-button
                type="primary"
                icon="el-icon-s-promotion"
                class="el-button-sure"
                >确认记录</el-button
              >
            </div>
            <el-form ref="form" :model="form" label-width="0px">
              <div class="note-input">
                <div class="mt0">
                  <el-input
                    placeholder="请填写标题"
                    v-model="form.inputTitle"
                  ></el-input>
                </div>
                <div class="mt10">
                  <el-input
                    type="textarea"
                    :autosize="{ minRows: 3, maxRows: 10 }"
                    placeholder="我要记录..."
                    maxlength="300"
                    show-word-limit
                    v-model="form.textarea"
                  >
                  </el-input>
                </div>
                <div class="handle-title mt10">
                  <el-checkbox v-model="form.checked">是否共享</el-checkbox>
                  <div class="more-set" @click="showInput">
                    <i class="el-icon-s-operation"></i>更多设置
                  </div>
                </div>
                <div class="setShow" v-show="setShow">
                  <div class="mt10">
                    <el-select v-model="form.value" placeholder="请选择分类">
                      <el-option label="分类1" value="num01"></el-option>
                      <el-option label="分类2" value="num02"></el-option>
                    </el-select>
                  </div>
                  <div class="mt10">
                    <el-input
                      type="textarea"
                      :autosize="{ minRows: 2, maxRows: 5 }"
                      placeholder="备注"
                      v-model="form.textarea2"
                      maxlength="100"
                      show-word-limit
                    >
                    </el-input>
                  </div>
                  <div class="mt10">
                    <el-upload
                      action="https://jsonplaceholder.typicode.com/posts/"
                      list-type="picture-card"
                      :on-preview="handlePictureCardPreview"
                      :on-remove="handleRemove"
                    >
                      <i class="el-icon-plus"></i>
                    </el-upload>
                    <el-dialog :visible.sync="form.dialogVisible">
                      <img width="50%" :src="form.dialogImageUrl" alt="" />
                    </el-dialog>
                  </div>
                </div>
              </div>
            </el-form>
          </div>

          <!-- 便签详情 -->
          <div class="bread-detail" v-show="showOff">
            <div class="tr btn-write">
              <el-button type="text" icon="el-icon-s-promotion">写一下</el-button>
            </div>
            <div class="note-input">
               <div class="note-content">
                  <div class="note-detail-text">
                    <h3>特朗普"已经赢了"假发该摘下来了!</h3>
                    <p>提到伊一大家可能不太熟悉，现在的她也算上比较知名的女主持人了，而且经常和一些大牌主持合作，比如张国立老师，虽然她的主持水平还需要提高，但是总体来说挺不错的，相信经过几年的经验累积会有更大的进步。一般情况下女主持人如果身穿V字领口的礼服，那么她们的里面一般会配一个裹胸，要不然太过于暴露对节目会有不好的效果，可是伊一在主持节目的时候居然没有穿裹胸，对于主持过各种大型节目的主持人来说，还能犯这种低级错误确实有点让人感觉到无语，虽然满足了网友们的眼睛，可是对于一个主持。</p>
                    <p>前段时间在她的身上就发生了一个特别尴尬的事情，在她主持节目的时候，穿着的衣服就有点尴尬了，其实大家都知道主持人在主持节目的时候，衣服都是准备好的，毕竟他们的形象代表的是整个节目甚至整个电视台的形象。</p>
                    <p>提到伊一大家可能不太熟悉，现在的她也算上比较知名的女主持人了，而且经常和一些大牌主持合作，比如张国立老师，虽然她的主持水平还需要提高，但是总体来说挺不错的，相信经过几年的经验累积会有更大的进步。一般情况下女主持人如果身穿V字领口的礼服，那么她们的里面一般会配一个裹胸，要不然太过于暴露对节目会有不好的效果，可是伊一在主持节目的时候居然没有穿裹胸，对于主持过各种大型节目的主持人来说，还能犯这种低级错误确实有点让人感觉到无语，虽然满足了网友们的眼睛，可是对于一个主持。</p>                   
                  </div>
                  <div class="tr bottom-txt">
                    <span>未共享</span>&nbsp;&nbsp;|&nbsp;&nbsp;2020年11月03日 15:03
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
export default {
  data() {
    return {
      form: {
        inputTitle: "",
        textarea: "",
        textarea2: "",
        checked: false,
        dialogImageUrl: "",
        dialogVisible: false,
        value: "",
      },
      setShow: false,
      showOff: false
    };
  },
  methods: {
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    showInput() {
      this.setShow = true;
      console.log(11);
    },
  },
};
</script>

<style lang="scss" scoped>
.notePad {
  padding: 10px;
  box-sizing: border-box;
}
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
.notePad .bread-list .bread-item:hover,.notePad .bread-list .bread-item.active{
  border: 1px solid #16466a;
  color: #16466a;
}
.notePad .bread-list .bread-item-share:after {
  position: absolute;
  right: 10px;
  bottom: 10px;
  content: "";
  background: url(../assets/image/share-icon.png) no-repeat center;
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
  background: url(../assets/image/down-icon.png) no-repeat center;
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
  background: url(../assets/image/note-icon.png) no-repeat left top;
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
.notePad .note-detail-text h3{
  text-align: center;
  color: #16466a;
  font-size: 16px;
  margin-bottom: 20px;
}
.notePad .note-detail-text p{
  font-size: 12px;
  color: #666;
  margin-bottom: 5px;
  line-height: 1.5;
  text-indent: 24px;
}
.notePad .bottom-txt{
  margin-top: 10px;
  color: #aaa;
  font-size: 12px;
}
.notePad .bottom-txt>span{
  color: #0b61a3;
}




</style>
