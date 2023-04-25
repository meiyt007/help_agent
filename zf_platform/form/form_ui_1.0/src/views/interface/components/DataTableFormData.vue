<template>
    <div>
      <el-form ref="form" class="formEdit" label-width="0px" >
        <el-row :gutter="10" class="mb8"  v-if="editable">
          <h3 v-text="title" style="display: inline;margin-left: 10px">
          </h3>
          <el-tooltip v-if="isPre" class="el-tooltip-content-div" placement="top-start" effect="dark">
            <div slot="content">
              1.Value值支持动态获取，例如配置为{{valueDefalut}}
              <br/>
              2.动态获取的值可以从前置请求配置中的关联配置获取
              <br/>
              3.设置动态获取，如未获取到值，返回空
            </div>
            <i class="el-icon-question"></i>
          </el-tooltip>
          <el-col :span="1.5" style="float: right;margin-right: 15px">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
            >
            </el-button>
          </el-col>
        </el-row>
        <el-table :data="localData" border>
          <el-table-column label="Key" prop="key" align="left" >
            <template slot-scope="scope">
             <el-form-item prop="key" v-if="editable && 'form-data' != title">
                <el-input placeholder="请输入Key" v-model.trim="scope.row.key" maxlength="50" show-word-limit/>
              </el-form-item>
              <el-form-item prop="key" v-if="editable && 'form-data' == title">
                <el-input placeholder="请输入Key" v-model.trim="scope.row.key" maxlength="50" show-word-limit >
                  <el-select v-model="scope.row.inputType" slot="append">
                    <el-option label="Text" value="Text"></el-option>
                    <el-option label="File" value="File"></el-option>
                  </el-select>
                </el-input>
              </el-form-item>
              <!--<div v-else>{{scope.row.key}}</div>-->

           </template>
          </el-table-column>
          <el-table-column label="Value" prop="value" align="left">
            <template slot-scope="scope" >
             <el-form-item prop="value" v-if="editable && 'Text' == scope.row.inputType">
               <el-input placeholder="请输入Value"  v-model.trim="scope.row.value" />
             </el-form-item>
              <el-form-item v-if="editable && 'File' == scope.row.inputType">
                <el-upload
                  class="upload-demo"
                  :on-change="handleChange"
                  :on-remove="handleRemove"
                  :file-list="scope.row.fileList"
                  :action="uploadUrl()"
                  :on-error="uploadError"
                  :on-success="uploadSuccess"
                >
                  <el-button slot="trigger" size="small" @click="uploadIndexFn(scope.$index)" type="primary">选取文件</el-button>
                 <!-- <el-button size="small" type="info">点击上传</el-button>-->
                </el-upload>
              </el-form-item>
              <!--<div v-else>{{scope.row.value}}</div>-->
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" v-if="editable" >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleDelete(scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </div>
</template>
<script>
    import EditableCell from './EditableCell'
    import { uploadFile } from "@/api/interface/manager";
    export default {
        props: ['title', 'dataSource', 'editable','isPre'],
        components: {
            EditableCell,
        },
        data() {
            let data = this.object2Array(this.dataSource);
            return {
                uploadIndex:-1,
                count: 0,
                filePath:'',
                valueDefalut:"{{value}}",
                localData: data
            }
        },
        methods: {
          //成功后返回
          uploadSuccess(resp,file, fileList){
            if(200 !== resp.code){
              fileList.shift();
              return this.msgError(resp.message);
            }else {
              let fileUrl = resp.data.name +'~'+ this.mediaType + '~'+ resp.data.url;
              if(-1 != this.uploadIndex){
                this.localData[this.uploadIndex].value = fileUrl;
              }
            }

          },
          //失败后返回
          uploadError(resp){
            this.msgError("文件上传失败");
          },
          uploadUrl(){
            return uploadFile();
          },
          handleChange(file, fileList) {
            if (fileList.length > 1) {
              fileList.shift();
            }
            this.mediaType = file.raw.type;
          },
          handleRemove(file, fileList) {
            fileList.shift();
            if(-1 != this.uploadIndex){
              this.localData[this.uploadIndex].value = '';
            }

          },
          uploadIndexFn(uploadIndex){
           this.uploadIndex = uploadIndex;
          },
            handleAdd() {
                const newData = {
                    key: '',
                    value: '',
                    inputType:'Text'
                };
                this.localData = [...this.localData, newData];
            },
            object2Array: function (obj) {
                let array = [];
                for (let attr in obj) {
                    array.push({
                        key: attr,
                        value: obj[attr]
                    });
                }
                return array;
            },
            array2Object: function (arr) {
                let obj = {};

                for (let index in arr) {
                    let item = arr[index];
                    let type = undefined == item.inputType?'Text':item.inputType;
                    obj[item.key] = type + item.value;
                }
                return obj;
            },
            isNull: function (obj) {
                for (let key in obj) {
                    return true;
                }
                return false;
            },
            /** 删除按钮操作 */
            handleDelete(row) {
              let that = this;
              this.$confirm('是否确认删除吗?', "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
              }).then(function() {
                for(let i=0;i<that.localData.length;i++){
                  if(row.key == that.localData[i].key){
                    that.localData.splice(i, 1);
                    break;
                  }
                }
              }).catch(function() {});
            },
        },
        watch: {
          localData: {
              handler: function(val, oldVal) {
                //console.log("change",this.array2Object(this.localData));
                this.$emit('dataChanged', this.array2Object(this.localData));
              },
              deep: true
            },
        },
        created() {

            if (this.localData == null) {
                this.localData = [];
            }
          for (let index in this.localData) {
            let item = this.localData[index];
            if (item.value.indexOf('File') > -1 || item.value.indexOf("Text") > -1) {
              let type = item.value.substring(0, 4);
              if (type == 'Text') {
                item.value = item.value.substring(4);
                item.inputType = type;
              } else if (type == 'File') {
                item.inputType = type;
                let valueArr = item.value.split('~');
                // [{name: 'food.jpg', url: 'https://xxx.cdn.com/xxx.jpg'}]
                item.fileList = [{ name: valueArr[0].substring(4), url: valueArr[2] }];
              } else {
                item.inputType = 'Text';
              }
            }
          }
        },

    }
</script>
<style scoped>
.el-tooltip-content-div{
  font-size: 20px;
}
</style>
