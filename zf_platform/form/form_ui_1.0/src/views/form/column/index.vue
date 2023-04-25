<template>
  <el-tabs type="border-card" v-model="activeName" class="column-container">
    <el-tab-pane label="存储结构" name="first">
      <div class="app-container-edit">
        <el-form ref="form" :model="form" :rules='columnList.rules' class="formEdit" label-width="0px">
          <el-row :gutter="10" class="mb8">
            <el-col v-show="0 == soucrce" :span="1.5">
              <el-button
                type="primary"
                icon="el-icon-plus"
                size="mini"
                @click="getDatasourceColumn"
              >获取存储结构
              </el-button>
            </el-col>
            <el-col v-show="1 == soucrce" :span="1.5">
              <el-button
                type="primary"
                icon="el-icon-plus"
                size="mini"
                @click="addColumn"
              >新增逻辑结构
              </el-button>
              <el-button
                type="success"
                icon="el-icon-upload2"
                size="mini"
                @click="addJsonData"
              >导入JSON数据
              </el-button>
            </el-col>
          </el-row>
          <el-table v-if="1 == soucrce" v-loading="loading" :data="form.columnList" border>
            <!--<el-table-column type="selection" width="55" align="center"  />-->
            <el-table-column label="序号" width="55" type="index" align="center">
              <template slot-scope="scope">
                <span>{{scope.$index + 1}}</span>
              </template>
            </el-table-column>
            <el-table-column label="对象属性" align="center">
              <template slot-scope="scope">
                <!--<i class="require">*</i>-->
                <el-form-item :prop="'columnList.' + scope.$index + '.objectProperty'"
                              :rules="columnList.rules.objectProperty">
                  <el-input placeholder="请输入对象属性" v-model.trim="scope.row.objectProperty" maxlength="50"
                            show-word-limit/>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="备注" align="center">
              <template slot-scope="scope">
                <el-form-item prop="demo">
                  <el-input placeholder="请输入备注" v-model.trim="scope.row.demo" maxlength="100" show-word-limit/>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="160" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="iconfont zfsoft-shanchu"
                  @click="handleDelete(scope.row, scope.$index)"
                >删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-table v-if="0 == soucrce" v-loading="loading" :data="form.columnList" border>
            <!--<el-table-column type="selection" width="55" align="center"  />-->
            <el-table-column label="序号" width="55" type="index" align="center">
              <template slot-scope="scope">
                <span>{{scope.$index + 1}}</span>
              </template>
            </el-table-column>
            <el-table-column label="列名称" align="center" prop="columnName" :show-overflow-tooltip="true"/>
            <el-table-column label="列类型" align="center" width="160" :show-overflow-tooltip="true">
              <template slot-scope="scope">
                {{scope.row.columnType}}
              </template>
            </el-table-column>
            <el-table-column label="是否必填" align="center" width="100" :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <el-switch
                  disabled
                  v-model="scope.row.notNull"
                  :active-value="1"
                  :inactive-value="0"
                ></el-switch>
              </template>
            </el-table-column>
            <el-table-column label="最大长度" width="100" align="center" prop="maxLenth" :show-overflow-tooltip="true"/>
            <el-table-column label="对象属性" align="center" width="130" prop="objectProperty"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column label="数据存储类型" width="150" align="center" prop="dataType">
              <template slot-scope="scope">
                <el-form-item prop="dataType">
                  <el-select v-model="scope.row.dataType">
                    <el-option
                      v-for="data in dataTypeMap"
                      :key="data.id"
                      :label="data.value"
                      :value="data.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="备注" align="center" prop="demo">
              <template slot-scope="scope">
                <el-form-item prop="demo">
                  <el-input placeholder="请输入备注" v-model.trim="scope.row.demo" maxlength="100" show-word-limit/>
                </el-form-item>
              </template>
            </el-table-column>
          </el-table>
        </el-form>

        <el-dialog :close-on-click-modal="false" title="导入json数据" :visible.sync="openJson" width="90%" append-to-body>
          <div class="el-table__header-wrapper dialog-table">
            <el-form label-width="0px" class="demo-ruleForm">
              <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
                <colgroup>
                  <col width="20%"/>
                  <col width="30%"/>
                  <col width="20%"/>
                  <col width="30%"/>
                </colgroup>
                <tr>
                  <td><i class="require">*</i><b>json数据：</b></td>
                  <td colspan="3">
                    <el-form-item class="json-data-dialog">
                      <el-input v-model.trim="jsonData" type="textarea" placeholder='请输入json数据,如:
{
  "name":"名称",
  "code":"编码"
}
                 ' maxlength="2000" show-word-limit></el-input>
                    </el-form-item>
                  </td>
                </tr>
              </table>
            </el-form>
          </div>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-dialog>
      </div>
    </el-tab-pane>
    <el-tab-pane label="关联对象" name="second">
      <div class="app-container-edit">
        <el-form ref="form1" :model="form" class="formEdit" label-width="0px">
          <el-row :gutter="10" class="mb8">
            <el-col v-show="0 == soucrce" :span="1.5">
              <el-button
                type="primary"
                icon="el-icon-plus"
                size="mini"
                @click="addDataSourceObject()"
              >新增关联对象
              </el-button>
            </el-col>
            <el-col v-show="1 == soucrce" :span="1.5">
              <el-button
                type="primary"
                icon="el-icon-plus"
                size="mini"
                @click="addObject()"
              >新增关联对象
              </el-button>
            </el-col>
          </el-row>

          <el-table v-if="1 == soucrce" :data="form.objectExtandList" :key="'soucrce' + soucrce">
            <el-table-column type="expand" v-if="1 == soucrce">
              <template slot-scope="props" v-if="1 == soucrce">
                <el-table :data="props.row.columnList" v-if="1 == soucrce">
                  <el-table-column label="对象属性" align="center" prop="objectProperty" :show-overflow-tooltip="true"/>
                  <el-table-column label="备注" align="center" prop="demo" :show-overflow-tooltip="true"/>
                </el-table>
              </template>
            </el-table-column>
            <el-table-column align="center" label="对象名称" prop="variableName"></el-table-column>
            <el-table-column align="center" label="类型" prop="type" :formatter="typeFormat"></el-table-column>
            <el-table-column align="center" label="备注1" prop="demo"></el-table-column>
            <el-table-column align="center" width="160" label="操作" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="iconfont zfsoft-xiugai"
                  @click="addObject(scope)"
                >修改
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="iconfont zfsoft-shanchu"
                  @click="deleteDataObject(scope.$index)"
                >删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-table v-if="0 == soucrce" v-loading="loading" :data="form.objectExtandList" border :key="'soucrce' + soucrce">
            <!--<el-table-column label="序号" width="55" type="index" align="center">
              <template slot-scope="scope">
                <span>{{scope.$index + 1}}</span>
              </template>
            </el-table-column>-->
            <el-table-column align="center" label="对象属性名称" prop="variableName"></el-table-column>
            <el-table-column align="center" label="类型" prop="type" :formatter="typeFormat"></el-table-column>
            <el-table-column align="center" label="关联对象" prop="secondaryObjectOid" :formatter="objectFormat"></el-table-column>
            <el-table-column align="center" label="关联外键" prop="foreignKey"></el-table-column>
            <el-table-column align="center" label="备注" prop="demo" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column align="center" width="160" label="操作" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="iconfont zfsoft-xiugai"
                  @click="addDataSourceObject(scope)"
                >修改
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="iconfont zfsoft-shanchu"
                  @click="deleteDataObject(scope.$index)"
                >删除
                </el-button>
              </template>
            </el-table-column>
            <!--<el-table-column label="对象属性名称" align="center" :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <el-form-item :prop="'objectExtandList[' + scope.$index + '].variableName'"
                              :rules="objectExtandList.rules.variableName">
                  <el-input v-model.trim="scope.row.variableName" maxlength="100" show-word-limit/>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="属性类型" align="center">
              <template slot-scope="scope">
                <el-form-item>{{scope.row.variableName}}</el-form-item>
                <el-form-item :prop="'objectExtandList.' + scope.$index + '.type'"
                              :rules="objectExtandList.rules.type">
                  <el-select v-model="scope.row.type">
                    <el-option
                      v-for="data in objectTypeMap"
                      :key="data.id"
                      :label="data.value"
                      :value="data.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="关联对象" width="240" align="center">
              <template slot-scope="scope">
                <el-form-item :prop="'objectExtandList.' + scope.$index + '.secondaryObjectOid'"
                              :rules="columnList.rules.secondaryObjectOid">
                  <el-select v-model="scope.row.secondaryObjectOid" @change="secondaryObjectOidChange"
                             placeholder="请选择">
                    <el-option
                      v-for="item in dataSourceObjectList"
                      :key="item.objectOid"
                      :label="item.objectName"
                      :value="item.objectOid"
                      :disabled="item.disabled">
                    </el-option>
                  </el-select>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="关联外键" width="200" align="center">
              <template slot-scope="scope">
                <el-form-item :prop="'objectExtandList.' + scope.$index + '.foreignKey'"
                              :rules="columnList.rules.foreignKey">
                  <el-select v-model="scope.row.foreignKey" placeholder="请选择">
                    <el-option
                      v-for="item in scope.row.columnList"
                      :key="item.objectProperty"
                      :label="item.objectProperty"
                      :value="item.objectProperty">
                    </el-option>
                  </el-select>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="备注" align="center" prop="demo">
              <template slot-scope="scope">
                <el-form-item prop="demo">
                  <el-input placeholder="请输入备注" v-model.trim="scope.row.demo" maxlength="100" show-word-limit/>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100" align="center">
              <template slot-scope="scope">
                <el-form-item align="center">
                  <el-button
                    size="mini"
                    type="text"
                    icon="iconfont zfsoft-shanchu"
                    @click="deleteDataObject(scope)"
                  >删除
                  </el-button>
                </el-form-item>
              </template>
            </el-table-column>-->
          </el-table>
        </el-form>

        <el-dialog :close-on-click-modal="false" :title="openDataChildTitle || '新增关联对象'" :visible.sync="openDataChild" width="80%" append-to-body>
          <div class="el-table__header-wrapper dialog-table" v-if="openDataChild">
            <el-form ref="childDataForm" :model="childDataObject" :rules="childDataRules" label-width="0px" class="demo-ruleForm">
              <table cellspacing="0" cellpadding="0" class="el-table__body">
                <colgroup>
                  <col width="20%"/>
                  <col width="30%"/>
                  <col width="20%"/>
                  <col width="30%"/>
                </colgroup>
                <tr>
                  <td><i class="require">*</i><b>对象名称：</b></td>
                  <td>
                    <el-form-item prop="variableName">
                      <el-input v-model.trim="childDataObject.variableName" type="input" placeholder='对象名称'
                                maxlength="50"
                                show-word-limit></el-input>
                    </el-form-item>
                  </td>
                  <td><i class="require">*</i><b>类型：</b></td>
                  <td>
                    <el-form-item prop="type">
                      <el-select v-model="childDataObject.type">
                        <el-option
                          v-for="data in objectTypeMap"
                          :key="data.id"
                          :label="data.value"
                          :value="data.id"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </td>
                </tr>
                <tr>
                  <td><i class="require">*</i><b>关联对象：</b></td>
                  <td>
                    <el-form-item prop="secondaryObjectOid">
                      <el-select v-model="childDataObject.secondaryObjectOid" @change="secondaryObjectOidChange"
                                 placeholder="请选择" filterable>
                        <el-option
                          v-for="item in dataSourceObjectList"
                          :key="item.objectOid"
                          :label="item.objectName"
                          :value="item.objectOid"
                          :disabled="item.disabled">
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </td>
                  <td><i class="require">*</i><b>关联外键：</b></td>
                  <td>
                    <el-form-item prop="foreignKey">
                      <el-select v-model="childDataObject.foreignKey" placeholder="请选择" filterable>
                        <el-option
                          v-for="item in childDataObject.columns"
                          :key="item.objectProperty"
                          :label="item.objectProperty"
                          :value="item.objectProperty">
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </td>
                </tr>
                <tr>
                  <td><b>备注：</b></td>
                  <td colspan="3">
                    <el-form-item>
                      <el-input v-model.trim="childDataObject.demo" type="textarea" placeholder='备注' maxlength="100"
                                show-word-limit></el-input>
                    </el-form-item>
                  </td>
                </tr>
              </table>
            </el-form>
          </div>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="saveDataChild">确 定</el-button>
            <el-button @click="cancelDataChild">取 消</el-button>
          </div>
        </el-dialog>

        <el-dialog :close-on-click-modal="false" title="新增对象" :visible.sync="openChild" width="80%" append-to-body class="child-column">
          <div class="el-table__header-wrapper dialog-table" v-if="openChild">
            <el-form ref="childForm" :model="childObject" :rules="childRules" label-width="0px" class="demo-ruleForm">
              <table cellspacing="0" cellpadding="0" class="el-table__body">
                <colgroup>
                  <col width="20%"/>
                  <col width="30%"/>
                  <col width="20%"/>
                  <col width="30%"/>
                </colgroup>
                <tr>
                  <td><i class="require">*</i><b>对象名称：</b></td>
                  <td>
                    <el-form-item prop="variableName">
                      <el-input v-model.trim="childObject.variableName" type="input" placeholder='对象名称' maxlength="50"
                                show-word-limit></el-input>
                    </el-form-item>
                  </td>
                  <td><i class="require">*</i><b>类型：</b></td>
                  <td>
                    <el-form-item prop="type">
                      <el-select v-model="childObject.type">
                        <el-option
                          v-for="data in objectTypeMap"
                          :key="data.id"
                          :label="data.value"
                          :value="data.id"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </td>
                </tr>
                <tr>
                  <td><b>备注：</b></td>
                  <td colspan="3">
                    <el-form-item>
                      <el-input v-model.trim="childObject.demo" type="input" placeholder='备注' maxlength="100"
                                show-word-limit></el-input>
                    </el-form-item>
                  </td>
                </tr>
              </table>
              <el-row :gutter="10" class="mb8">
                <el-col :span="1.5">
                  <el-button
                    type="primary"
                    icon="el-icon-plus"
                    size="mini"
                    @click="addChildColumn"
                  >增加对象属性
                  </el-button>
                </el-col>
              </el-row>
              <el-table :data="childObject.columnList">
                <el-table-column label="序号" width="55" align="center">
                  <template slot-scope="scope">
                    <span>{{scope.$index + 1}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="对象属性" align="center">
                  <template slot-scope="scope">
                    <el-form-item :prop="'columnList.' + scope.$index + '.objectProperty'"
                                  :rules="childRules.columnList.rules.objectProperty" style="height: 52px;">
                      <el-input placeholder="请输入对象属性" v-model.trim="scope.row.objectProperty" maxlength="50"
                                show-word-limit/>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="备注" align="center">
                  <template slot-scope="scope">
                    <el-form-item prop="demo" style="height: 52px;">
                      <el-input placeholder="请输入备注" v-model.trim="scope.row.demo" maxlength="100" show-word-limit/>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="160" align="center">
                  <template slot-scope="scope">
                    <el-form-item align="center">
                      <el-button
                        size="mini"
                        type="text"
                        icon="iconfont zfsoft-shanchu"
                        @click="deleteColumn(scope.$index)"
                      >删除
                      </el-button>
                    </el-form-item>
                  </template>
                </el-table-column>
              </el-table>
            </el-form>
          </div>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="saveChild">确 定</el-button>
            <el-button @click="cancelChild">取 消</el-button>
          </div>
        </el-dialog>


        <el-dialog :close-on-click-modal="false" title="导入json数据" :visible.sync="openJson" width="90%" append-to-body>
          <div class="el-table__header-wrapper dialog-table" v-if="openJson">
            <el-form label-width="0px" class="demo-ruleForm">
              <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
                <colgroup>
                  <col width="20%"/>
                  <col width="30%"/>
                  <col width="20%"/>
                  <col width="30%"/>
                </colgroup>
                <tr>
                  <td><i class="require">*</i><b>json数据：</b></td>
                  <td colspan="3">
                    <el-form-item class="json-data-dialog">
                      <el-input v-model.trim="jsonData" type="textarea" placeholder='请输入json数据,如:
{
  "name":"名称",
  "code":"编码"
}
                 ' maxlength="2000" show-word-limit></el-input>
                    </el-form-item>
                  </td>
                </tr>
              </table>
            </el-form>
          </div>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-dialog>
      </div>
    </el-tab-pane>
  </el-tabs>

</template>

<script>
  import {datasourceColumn, queryFormColumnListByObjectOid,getObjectFieldSaveTypeList} from "@/api/form/column";
  import {validateLegalStrNoNumber} from '@/utils/validate';
  import {queryFormObjectExtandList} from '@/api/form/objectExtand';
  import {dataSourceObjectlist} from '@/api/form/object'
  const cloneDeep = require("clone");
  export default {
    name: "Column",
    props: ['params'],
    data() {
      const validateRepeat = (rule, value, callback) => {
        if (value != '') {
          let columnList = this.form.columnList.filter(d => d.objectProperty == value);

          if (columnList.length > 1) {
            callback(new Error('当前对象属性已存在，请重新输入'));
          } else {
            callback();
          }
        } else {
          callback();
        }
      };
      let validataVariableName = (rule, value, callback) => {
        if (value != '') {
          let index=-1;
          if(this.soucrce==0){
            index=this.childDataIndex;
          }else{
            index=this.childIndex;
          }
          let list=this.form.objectExtandList.slice();
          if(index!=-1){
            list.splice(index, 1);
          }
          let nameList = list.filter(d => d.variableName == value);
          let columnList = this.form.columnList.filter(d => d.objectProperty == value);
          if (nameList.length > 0) {
            callback(new Error('当前对象名称已存在，请重新输入'));
          } else if (columnList.length > 0) {
            callback(new Error('当前对象名称已存在，请重新输入'));
          } else {
            callback();
          }
        } else {
          callback();
        }
      };
      let validateChildColumn = (rule, value, callback) => {
        if (value != '') {
          let columnList = this.childObject.columnList.filter(d => d.objectProperty == value);
          if (columnList.length > 1) {
            callback(new Error('当前对象属性已存在，请重新输入'));
          } else {
            callback();
          }
        } else {
          callback();
        }
      };
      return {
        editValue:"",
        openDataChildTitle: "",
        openDataChild: false,
        childDataObject: {},
        childDataIndex: -1,
        openChild: false,
        childObject: {},
        childIndex: -1,
        dataSourceObjectList: [],
        objectColumnMap: {},
        objectTypeMap: [{id: 1, value: '数组'}, {id: 2, value: '对象'}],
        activeName: 'first',
        //授权id
        // 遮罩层
        loading: false,
        // 总条数
        total: 0,
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        soucrce: 0,
        // 启用状态
        ableMap: {'1': '启用', '0': '禁用'},
        // 数据存储类型(0 字符串 1数组 2对象 默认字符串，3文件类)
        //dataTypeMap: [{id: 0, value: '字符串'}, {id: 1, value: '数组'}, {id: 2, value: '对象'}, {id: 3, value: '文件类'}, {id: 4, value: 'Boolean1'}],
        dataTypeMap: [],
        // 表单参数
        form: {columnList: [], objectExtandList: []},
        dataSourceOptions: [],
        openJson: false,
        jsonData: '',
        // 表单校验
        columnList: {
          rules: {
            objectProperty: [
              {required: true, message: '对象属性不能为空', trigger: 'blur'},
              {required: true, validator: validateLegalStrNoNumber, trigger: 'blur'},
              {required: true, validator: validateRepeat, trigger: 'blur'},
            ]
          }
        },
        childDataRules: {
          variableName: [
            {required: true, message: '对象属性名称不能为空', trigger: 'blur'},
            {required: true, validator: validateLegalStrNoNumber, trigger: 'blur'},
            {required: true, validator: validateRepeat, trigger: 'blur'},
            {required: true, validator: validataVariableName, trigger: 'blur'}
          ],
          secondaryObjectOid: [
            {required: true, message: '关联对象不能为空', trigger: 'change'}
          ],
          type: [
            {required: true, message: '属性类型不能为空', trigger: 'change'}
          ],
          foreignKey: [
            {required: true, message: '关联外键不能为空', trigger: 'change'}
          ]
        },
        childRules: {
          variableName: [
            {required: true, message: '对象名称不能为空', trigger: 'blur'},
            {required: true, validator: validateLegalStrNoNumber, trigger: 'blur'},
            {required: true, validator: validateRepeat, trigger: 'blur'},
            {required: true, validator: validataVariableName, trigger: 'blur'}
          ],
          type: [
            {required: true, message: '类型不能为空', trigger: 'change'}
          ],
          columnList: {
            rules: {
              objectProperty: [
                {required: true, message: '对象属性不能为空', trigger: 'blur'},
                {required: true, validator: validateLegalStrNoNumber, trigger: 'blur'},
                {required: true, validator: validateChildColumn, trigger: 'blur'},
              ]
            }
          }
        }
      };
    },
    created() {
      if (undefined != this.params.objectOid && '' != this.params.objectOid) {
        this.form = {};
        // throttle(300, () => {
          this.getList();
          this.getObjcetExtandList(this.params.objectOid);
          this.getDataSourceObjectList();
        // });
      }

      this.queryObjectFieldSaveTypeList();
    },
    watch: {
      params: {
        handler(neVal) {
          if (undefined == neVal?.datasourceOid || '' == neVal?.datasourceOid) {
            this.soucrce = 1;
          } else {
            this.soucrce = 0;
          }
        },
        immediate: true,
        deep: true
      }
    },
    methods: {
      /** 查询列表 */
      getList() {
        this.loading = true;
        let that = this;
        if('' != this.params.objectOid && undefined != this.params.objectOid){
          queryFormColumnListByObjectOid(this.params).then(response => {
            this.form.columnList = response.data;
            this.loading = false;
          }).catch(function () {
            that.loading = false;
          });
        }else {
          this.form.columnList = [];
          this.loading = false;
        }
      },
      // 启用状态
      statusFormat(row) {
        return this.selectMapLabel(this.ableMap, row.isAble);
      },
      //获取表单的内容
      getFormData() {
        return new Promise((resolve, reject) => {
          //你的逻辑代码
          this.$refs["form"].validate(valid => {
            if (valid) {
              this.$refs["form1"].validate(valid1 => {
                if (valid1) {
                  resolve(this.form)
                }
              });
            }
          });
        });
      },
      /** 删除按钮操作 */
      handleDelete(row, ind) {
        let that = this;
        this.$confirm('是否确认删除吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          if (row.objectProperty) {
            for (let i = 0; i < that.form.columnList.length; i++) {
              if (row.objectProperty == that.form.columnList[i].objectProperty) {
                that.form.columnList.splice(i, 1);
                break;
              }
            }
          } else {
            that.form.columnList.splice(ind, 1)
          }
          that.$refs['form'].clearValidate()
          /*let delIndex = that.form.columnList.findIndex(d => d.index == row.index)
          that.form.columnList.splice(delIndex, 1)*/
        }).catch(function () {
        });
      },
      handleAble(row) {
        const oid = row.id;
        let ableMessage = row.isAble === 1 ? '启用' : '禁用'
        this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return able(oid);
        }).then(() => {
          this.msgSuccess(ableMessage + "成功");
        }).catch(function () {
          row.isAble = row.isAble === 0 ? 1 : 0
        });
      },
      //增加框
      addColumn() {
        let column = {index: this.form.columnList.length, 'objectProperty': ''}
        if (!this.form.columnList) {
          this.form.columnList = []
        }
        this.form.columnList.push(column)
      },
      //导入json数据
      addJsonData() {
        this.openJson = true;
      },
      // 取消按钮
      cancel() {
        this.openJson = false;
        this.jsonData = '';
      },
      //提交按钮
      submitForm() {
        if ('' == this.jsonData) {
          this.msgError("请输入JSON数据");
          return;
        }
        if (this.isJSON(this.jsonData)) {
          const jsonDataOj = eval("(" + this.jsonData + ")");
          //遍历key和value
          const keys = Object.keys(jsonDataOj)
          for (let i = 0; i < keys.length; i++) {
            const key = keys[i]
            const value = jsonDataOj[key]
            let columnData = {
              'objectProperty': key,
              'demo': value
            }
            this.form.columnList.push(columnData);
          }
          this.openJson = false;
        } else {
          this.msgError("请输入正确的JSON数据");
          return;
        }
      },
      isJSON(str) {
        if (typeof str == 'string') {
          try {
            var obj = JSON.parse(str);
            if (typeof obj == 'object' && obj) {
              return true;
            } else {
              return false;
            }
          } catch (e) {
            return false;
          }
        }
      },
      //获取数据库的列表
      getDatasourceColumn() {
        let _that = this;
        if (undefined == _that.params.objectForm || '' == _that.params.objectForm) {
          _that.msgError("请输入数据库表名");
          return;
        }
        _that.loading = true;
        datasourceColumn(_that.params).then(response => {
          if (response.code === 200) {
            _that.msgSuccess("操作成功！");
            let tempArr = response.data || [];
            tempArr && tempArr.forEach(item => {
              const arr = _that.form.columnList || [];
              for(let i = 0, len = arr.length; i < len; i++) {
                if(arr[i].objectProperty === item.objectProperty) {
                  item.dataType = arr[i].dataType;
                }
              }
            });
            _that.$set(_that.form, 'columnList', tempArr);
          }
          _that.$nextTick(() => _that.loading = false);
        }).catch(function() {
          _that.loading = false;
          _that.$set(_that.form, 'columnList', null);
        });;
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
      queryObjectFieldSaveTypeList(){
        getObjectFieldSaveTypeList().then(response => {
          this.dataTypeMap = response.data;
        });
      },
      getObjectList() {
        let par = {authorizeKey: this.params.authorizeKey, moduleOid: null, saveType: "1"};
        dataSourceObjectlist(par).then(response => {
          this.objectList = response.data;
        });
      },
      getObjcetExtandList(objectOid) {
        queryFormObjectExtandList(objectOid).then(response => {
          if (response.code === 200) {
            this.form.objectExtandList = response.data;
          }
        });
      },
      addDataSourceObject(row) {
        this.openDataChild = true;
        if (row) {
          this.openDataChildTitle = "修改关联对象";
          this.childDataIndex = row.$index;
          this.childDataObject = cloneDeep(this.form.objectExtandList[row.$index]);
          this.childDataObject?.secondaryObjectOid && (this.childDataObject.columns = this.objectColumnMap.get(this.childDataObject?.secondaryObjectOid));
        } else {
          this.openDataChildTitle = "新增关联对象";
          this.childDataIndex = -1;
          this.$set(this, 'childDataObject', {
            "variableName": undefined,
            "type": undefined,
            "secondaryObjectOid": undefined,
            "foreignKey": undefined,
            "demo": undefined
          });
        }
        let oids = "-1";
        if (!this.dataSourceObjectList || this.dataSourceObjectList.length === 0) {
          this.getDataSourceObjectList();
        }
        for (let i = 0; i < this.form.objectExtandList.length; i++) {
          oids += this.form.objectExtandList[i].secondaryObjectOid;
        }
        for (let i = 0; i < this.dataSourceObjectList.length; i++) {
          if (-1 === oids.indexOf(this.dataSourceObjectList[i].objectOid)) {
            this.dataSourceObjectList[i].disabled = false;
          } else {
            this.dataSourceObjectList[i].disabled = true;
          }
        }
        if (row) {
          this.dataSourceObjectList.filter(item => item.objectOid === this.childDataObject?.secondaryObjectOid)[0].disabled = false
        }
      },
      addObject(row) {
        this.openChild = true;
        if (row) {
          this.childIndex = row.$index;
          this.childObject = cloneDeep(this.form.objectExtandList[row.$index]);
        } else {
          this.childIndex = -1;
          this.$set(this, 'childObject', {
            "variableName": undefined,
            "type": undefined,
            "demo": undefined,
            columnList: [{'objectProperty': ''}]
          });
        }
      },
      cancelChild() {
        this.openChild = false;
      },
      saveChild() {
        this.$refs["childForm"].validate(valid => {
          if (valid) {

            if (null == this.childObject || null == this.childObject.columnList || this.childObject.columnList.length == 0) {
              this.msgError("对象属性列表不能为空");
              return false;
            }
            this.openChild = false;
            if (-1 === this.childIndex) {
              this.form.objectExtandList.push(this.childObject)
            } else {
              this.$set(this.form.objectExtandList, this.childIndex, this.childObject)
            }
          } else {
            return false;
          }
        });
      },
      cancelDataChild() {
        this.openDataChild = false;
      },
      saveDataChild() {
        this.$refs["childDataForm"].validate(valid => {
          if (valid) {
            if (null == this.childDataObject) {
              this.msgError("对象不能为空");
              return false;
            }
            this.openDataChild = false;
            if (-1 === this.childDataIndex) {
              this.form.objectExtandList.push(this.childDataObject)
            } else {
              this.$set(this.form.objectExtandList, this.childDataIndex, this.childDataObject)
            }
          } else {
            return false;
          }
        });
      },
      addChildColumn() {
        let column = {'objectProperty': ''}
        let list = []
        if (this.childObject.columnList) {
          list = this.childObject.columnList;
        }
        list.push(column)
        this.$set(this.childObject, 'columnList', list)
      },
      handleChildColumn(row) {
        let obj = this.form.objectExtandList[row];
        let child = {'variableName': '', 'demo': ''};
        obj.children.push(child);
        this.form.objectExtandList[row] = obj;
      },
      secondaryObjectOidChange(value) {
        this.childDataObject.foreignKey=undefined;
        this.childDataObject.columns= this.objectColumnMap.get(value);
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
      deleteColumn(index) {
        this.childObject.columnList.splice(index, 1)
        this.$refs['childForm'].clearValidate();
      },
      deleteDataObject(index) {
        this.form.objectExtandList.splice(index, 1)
        this.$refs['form1'].clearValidate();
      }
    }
  };
</script>
<style>
  .app-container-edit {
    padding: 0px;
    padding-top: 10px;
  }

  .formEdit table tr td:nth-of-type(2n) {
    color: #606266;
    text-align: center !important;
  }

  .formEdit .el-form-item {
    margin-bottom: 15px !important;
  }

  .json-data-dialog .el-textarea__inner {
    height: 200px !important;
  }

  .dialog-table .column-container table tr td:nth-of-type(2n+1) {
    background-color: #fff;
  }
  .dialog-table .column-container table tr td:nth-of-type(2n) {
    background-color: #fff;
  }

  .child-column .el-table table tr td:nth-of-type(2n+1) {
    background-color: #fff;
  }
  .child-column .el-table table tr td:nth-of-type(2n) {
    background-color: #fff;
  }

</style>
