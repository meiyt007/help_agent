<template>
  <el-tabs type="border-card" v-model="activeName">
    <el-tab-pane label="存储结构" name="first">
  <div class="app-containerView">
    <el-form :model="form"  label-width="0px" class="formView" >
      <el-table v-if="1 == soucrce" v-loading="loading" :data="form.columnList" >
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{scope.$index + 1}}</span>
          </template>
        </el-table-column>
        <el-table-column label="对象属性" prop="objectProperty" align="center" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="备注" align="center" prop="demo" :show-overflow-tooltip="true"/>
      </el-table>

      <el-table v-if="0 == soucrce" v-loading="loading" :data="form.columnList" border>
        <!--<el-table-column type="selection" width="55" align="center"  />-->
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{scope.$index + 1}}</span>
          </template>
        </el-table-column>
        <el-table-column label="列名称" align="center"  prop="columnName" :show-overflow-tooltip="true"/>
        <el-table-column label="列类型" align="center"  width="100" prop="columnType" :show-overflow-tooltip="true" />
        <el-table-column label="是否必填" align="center"  width="80" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <el-switch
              disabled
              v-model="scope.row.notNull"
              :active-value="1"
              :inactive-value="0"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="最大长度" width="100"  align="center" prop="maxLenth":show-overflow-tooltip="true"/>
        <el-table-column label="对象属性" align="center" width="160"  prop="objectProperty" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="数据存储类型"  width="150"   align="center" prop="dataType">
          <template slot-scope="scope">
            <span v-for="data in dataTypeMap" v-if="data.id == scope.row.dataType">{{data.value}}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="demo" :show-overflow-tooltip="true">
        </el-table-column>
      </el-table>
    </el-form>
  </div>
    </el-tab-pane>
    <el-tab-pane label="关联对象" name="second">
      <div class="app-containerView">
        <el-form :model="form"  label-width="0px" class="formView" >
          <el-table v-if="1 == soucrce" :data="form.objectExtandList">
            <el-table-column type="expand">
              <template slot-scope="props">
                <el-table :data="props.row.columnList">
                  <el-table-column label="对象属性" align="center" prop="objectProperty" :show-overflow-tooltip="true"/>
                  <el-table-column label="备注" align="center" prop="demo" :show-overflow-tooltip="true"/>
                </el-table>
              </template>
            </el-table-column>
            <el-table-column align="center" label="对象名称" prop="variableName"></el-table-column>
            <el-table-column align="center" label="类型" prop="type" :formatter="typeFormat"></el-table-column>
            <el-table-column align="center" label="备注" prop="demo"></el-table-column>
          </el-table>

          <el-table v-if="0 == soucrce" v-loading="loading" :data="form.objectExtandList" border>
            <el-table-column align="center" label="对象属性名称" prop="variableName"></el-table-column>
            <el-table-column align="center" label="类型" prop="type" :formatter="typeFormat"></el-table-column>
            <el-table-column align="center" label="关联对象" prop="secondaryObjectOid" :formatter="objectFormat"></el-table-column>
            <el-table-column align="center" label="关联外键" prop="foreignKey"></el-table-column>
            <el-table-column align="center" label="备注" prop="demo" :show-overflow-tooltip="true"></el-table-column>
          </el-table>

        </el-form>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import {queryFormColumnListByObjectOid } from "@/api/form/column";
  import {queryFormObjectExtandList} from '@/api/form/objectExtand';
  import {dataSourceObjectlist} from '@/api/form/object'
  export default {
    name: "Column",
    props:['params'],
    data() {
      return {
        //授权id
        // 遮罩层
        loading: false,
        activeName: 'first',
        dataSourceObjectList: [],
        objectColumnMap: {},
        objectTypeMap: [{id: 1, value: '数组'}, {id: 2, value: '对象'}],
        // 总条数
        total: 0,
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        // 数据存储类型(0 字符串 1数组 2对象 3文件类 默认字符串 )
        dataTypeMap:[{id:0,value:'字符串'},{id:1,value:'数组'},{id:2,value:'对象'},{id:3,value:'文件类'}, {id: 4, value: 'Boolean'}],
        soucrce:0,
        // 表单参数
        form: { columnList: [], objectExtandList: [] },
      };
    },
    created() {
      debugger
      if(undefined == this.params.datasourceOid || '' == this.params.datasourceOid){
        this.soucrce = 1;
      }else {
        this.soucrce = 0;
      }
      if(undefined != this.params.objectOid && '' != this.params.objectOid){
        this.getList();
        this.getObjcetExtandList(this.params.objectOid);
        this.getDataSourceObjectList();
        console.log(this.soucrce)
      }

    },
    methods: {
      /** 查询列表 */
      getList() {
        this.loading = true;
        let that = this;
        queryFormColumnListByObjectOid(this.params).then(response => {
          this.form.columnList = response.data;
          this.loading = false;
        }).catch(function () {
          that.loading = false;
        });
      },
      typeFormat(row) {
        for (let i = 0; i < this.objectTypeMap.length; i++) {
          if (this.objectTypeMap[i].id === row.type) {
            return this.objectTypeMap[i].value;
          }
        }
      },
      objectFormat(row){
        for (let i = 0; i < this.dataSourceObjectList.length; i++) {
          if (this.dataSourceObjectList[i].objectOid === row.secondaryObjectOid) {
            return this.dataSourceObjectList[i].objectName;
          }
        }
      },
      getObjcetExtandList(objectOid) {
        queryFormObjectExtandList(objectOid).then(response => {
          if (response.code === 200) {
            this.form.objectExtandList = response.data;
          }
        });
      },
      getDataSourceObjectList() {
        let par = {authorizeKey: this.params.authorizeKey, moduleOid: null, saveType: "0"};
        dataSourceObjectlist(par).then(response => {
          this.dataSourceObjectList = response.data;
          let map = new Map();
          for (let i = 0; i < response.data.length; i++) {
            queryFormColumnListByObjectOid(response.data[i]).then(response => {
              map.set(this.dataSourceObjectList[i].objectOid, response.data)
            })
          }
          this.objectColumnMap = map;
        });
      },
    }
  };
</script>
<style>
  .app-containerView{
    padding: 0px;
    padding-top: 10px;
  }
  .app-containerView .formView table tr td{
    background-color: #fff !important;
    text-align: center !important;
  }
</style>
