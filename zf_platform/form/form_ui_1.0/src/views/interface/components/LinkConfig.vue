<template>
    <div>
      <el-form ref="form" class="formEdit" label-width="0px" >
        <el-row :gutter="10" class="mb8"  v-if="editable">
          <h3 v-text="title" style="display: inline;margin-left: 10px"></h3>
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
          <el-table-column label="请求配置参数值" prop="key" align="left" >
            <template slot-scope="scope">
              <el-form-item prop="key" v-if="editable">
                <el-input placeholder="请输入请求配置参数值" v-model.trim="scope.row.key" maxlength="50" show-word-limit/>
              </el-form-item>
              <div v-else>{{scope.row.key}}</div>
           </template>
          </el-table-column>
          <el-table-column label="前置请求返回值（获取多层级数据需要使用a.b.c的方式）" prop="value" align="left" >
            <template slot-scope="scope" >
             <el-form-item prop="value" v-if="editable">
               <el-input placeholder="请输入前置请求返回值"  v-model.trim="scope.row.value" />
             </el-form-item>
              <div v-else>{{scope.row.value}}</div>
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

    export default {
        props: ['title', 'dataSource', 'editable'],
        components: {
            EditableCell,
        },
        data() {
            let data = this.object2Array(this.dataSource);
            return {
                count: 0,
                localData: data
            }
        },
        methods: {
            handleAdd() {
                const newData = {
                    key: '',
                    value: ''
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
                    obj[item.key] = item.value;
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
            if (!this.editable) {

            }
        },

    }
</script>
<style scoped>

</style>
