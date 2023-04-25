<template>
  <div class="form-config-add-component">
    <FormConfigAddHeader
      ref="formConfigAddHeader"
      :comboDirectoryName="comboDirectoryName"
      :formType="formType"
      :isSingle="isSingle"
      :fieldTypeList="fieldTypeList"
      @setClassify="handleClassify"
    />

    <div class="form-config-add-component--container">
      <FormConfigAddTab
        ref="formConfigAddTab"
        :classifyList="classifyList"
        :isSingle="isSingle"
        :isSubForm="isSubForm"
        :formType="formType"
        @setCurrentFieldTypeOid="(data) => (currentFieldTypeOid = data)"
      />
      <template v-if="classifyList.length > 0">
        <keep-alive>
          <component
            :is="'FormConfigAddMain'"
            :ref="currentFormConfig.fieldTypeOid"
            :key="currentFormConfig.fieldTypeOid"
            :contentOid="currentFormConfig.contentOid"
            :fieldTypeOid="currentFormConfig.fieldTypeOid"
            :authorizeKey="currentFormConfig.authorizeKey"
            :formCode="currentFormConfig.formCode"
            :fieldTypeName="currentFormConfig.fieldTypeName"
            :isFormMain="currentFormConfig.isFormMain"
            :comboDirectoryOid="comboDirectoryOid"
            :isSubForm="isSubForm"
            :isLogicFormMain="isLogicFormMain"
            :isSingle="isSingle"
            :columnList="currentFormConfig.columnList"
            :formObjectExtand="currentFormConfig.formObjectExtand"
            :formData="currentFormConfig.dataForm"
            :isModify="isModify"
            :labelOid="currentFormConfig.labelOid"
            :typeOid="currentFormConfig.typeOid"
            :columnTypeMap="columnTypeMap"
            :columnDataTypeMap="columnDataTypeMap"
            :formOid="formOid"
          />
        </keep-alive>
      </template>
      <div class="form-config-add-main" v-else>
        <el-empty description="请先选择分类名称"></el-empty>
      </div>
    </div>

    <div class="form-config-add-component--footer">
      <el-button type="primary" @click="handleSubmit" :disabled="isntSubmit">
        保存
      </el-button>
      <el-button @click="$emit('close')">取消</el-button>
    </div>
  </div>
</template>

<script>
import FormConfigAddHeader from './formConfigAddHeader';
import FormConfigAddTab from './formConfigAddTab';
import FormConfigAddMain from './formConfigAddMain';

// api
import { queryComboFieldTypeList } from "@/api/onething/comboForm/fieldManage.js";

import { queryFormInfoExist, saveOrUpdateComboFormInfo } from "@/api/onething/comboForm/formManage";

import { saveTable, } from "@/api/form/manager";

import { handleSignleParams, handleMasterSalveParams, handleSubFormParams, handleColumnListLengthIsZero, mergeColumnList } from './utils';

const noop = () => { };

export default {
  name: 'FormConfigAddComponent',
  props: {
    comboDirectoryName: String,
    comboDirectoryOid: String,
    isModify: Boolean, // 是否是修改
    modifyObject: Object,
    formOid: String,
    columnTypeMap: {
      type: Array,
      default: () => []
    },
    columnDataTypeMap: {
      type: Array,
      default: () => []
    }
  },
  components: { FormConfigAddHeader, FormConfigAddTab, FormConfigAddMain },
  data () {
    return {
      classifyList: [], // 分类列表
      currentFieldTypeOid: '', // 当前分类oid
      fieldTypeList: [],
      formType: '',
      modifyFieldTypeOid: '',
      labelList: {}, // 标签对应分类列表
    }
  },
  computed: {
    // 判断是不是单表
    isSingle () {
      return this.formType === 'single';
    },

    // 判断是不是主从
    isMasterSlave () {
      return this.formType === 'masterSlave';
    },

    // 判断是不是子列表
    isSubForm () {
      return this.formType === 'subForm';
    },

    // 是否是逻辑主表
    isLogicFormMain ({ isSubForm, currentClassifyIndex, currentClassify }) {
      return isSubForm && currentClassifyIndex === 0 && currentClassify.isFormMain === '1';
    },

    currentFormConfig ({ currentClassify }) {
      if (currentClassify) {
        return currentClassify;
      } else {
        return {};
      }
    },

    currentClassify ({ classifyList, currentFieldTypeOid }) {
      return classifyList.find(item => item.fieldTypeOid === currentFieldTypeOid);
    },

    currentClassifyIndex ({ classifyList, currentFieldTypeOid }) {
      return classifyList.findIndex(item => item.fieldTypeOid === currentFieldTypeOid);
    },

    currentFormMainOid ({ classifyList }) {
      return classifyList.find(item => item.isFormMain === '1')?.fieldTypeOid ?? null;
    },

    // 是否可以点击保存
    isntSubmit ({ classifyList }) {
      if (this.isSubForm) {
        return classifyList.length <= 1;
      } else {
        return classifyList.length === 0;
      }
    }
  },
  watch: {
    // 由于keeo-alive无法访问缓存组件实例 所以需要在列表中缓存组件实例 用于保存
    'classifyList.length': {
      immediate: true,
      handler (value) {
        if (value) {
          this.setInstanceCached();
        }
      }
    },
    currentFieldTypeOid: {
      immediate: true,
      handler (value) {
        if (value) {
          this.setInstanceCached();
        }
      }
    }
  },
  mounted () {
    this.getFieldTypeList().then(() => {
      this.handleFormTable();
      this.handleIsModifyData();
    });
  },
  methods: {
    getFieldTypeList () {
      return queryComboFieldTypeList(this.comboDirectoryOid,this.formOid).then(({ code, data }) => {
        if (code === 200) {
          const list = [];
          this.labelList = data.reduce((prev, cur) => {
            const { sxFieldType, comboFillLabels } = cur;
            prev[sxFieldType.fieldTypeOid] = comboFillLabels;
            list.push(sxFieldType);
            return prev;
          }, {});

          this.fieldTypeList = list.map(item => ({
            createDate: item.createDate,
            isFormMain: '0',
            createUserOid: item.createUserOid,
            fieldTypeName: item.fieldTypeName,
            fieldTypeOid: item.fieldTypeOid,
            instance: null,
            columnList: [],
            formObjectExtand: {},
            dataForm: {}
          }));
        }
      });
    },

    async handleFormTable () {
      if (this.isModify) return;
      const { fieldTypeOid } = this.modifyObject;
      const value = this.fieldTypeList.find(item => item.fieldTypeOid === fieldTypeOid);
      const ret = await this.handleClassify(value, true);
      if (ret !== false) {
        this.$refs.formConfigAddHeader.fieldTypeOid = value.fieldTypeOid;
      }
    },

    handleClear (isClearFormType = true) {
      this.classifyList = [];
      this.currentFieldTypeOid = '';
      if (isClearFormType) {
        this.formType = '';
      }
    },

    handleClassify (value, isInit = false) {
      if (this.isModify && value.fieldTypeOid === this.modifyFieldTypeOid) {
        this.handleClear();
        return this.handleIsModifyData();
      }

      if (value.fieldTypeOid === this.currentFieldTypeOid) return;

      // 如果是null 则为清空或者重置操作
      if (!value) {
        this.handleClear();
        return;
      }

      return this.handleCreateTable(value, isInit, async () => {
        await this.$nextTick();

        const data = this.labelList[value.fieldTypeOid];

        if (data.length === 0) return this.$message.warning('未查询到分类标签信息');

        // 判断是单表丶主从丶子列表
        const dynamicLabelList = data.filter(item => item.isMovingFlag === 1);

        this.handleFormType(dynamicLabelList, data);

        // 清除上次记录
        this.handleClear(false);

        if (this.isSubForm) {
          this.handleSubFormInit(value);
        } else {
          this.handleMainStorage(value);
          this.currentFieldTypeOid = value.fieldTypeOid;
          this.$refs.formConfigAddTab.currentId = 0;
        }
        this.handleMasterSalveStorage(dynamicLabelList);
      });
    },

    handleFormType (dynamicLabelList, data) {
      if (dynamicLabelList.length > 0) {
        this.formType = 'masterSlave';
        if (dynamicLabelList.length === data.length) {
          this.formType = 'subForm';
        }
      } else {
        this.formType = 'single';
      }
    },

    // 设置存储
    handleCreateTable (value, isInit, callback = noop) {
      if (!value.fieldTypeOid) return this.$message.warning('请选择分类');
      // 如果已经存在 执行数据处理回调
      const isExit = this.classifyList.find(item => item.fieldTypeOid === value.fieldTypeOid);
      if (isExit) return callback();

      if (isInit) {
        return callback();
      }

      return queryFormInfoExist(this.comboDirectoryOid, value.fieldTypeOid)
        .then(({ data, code }) => {
          if (code === 200) {
            if (data && data > 0) {
              if (this.currentFieldTypeOid) {
                this.$refs.formConfigAddHeader.fieldTypeOid = this.currentFieldTypeOid;
              } else {
                this.currentFieldTypeOid = '';
                this.$refs.formConfigAddHeader.fieldTypeOid = '';
              }
              this.$message.warning("分类已存在设计表单");
              return Promise.resolve(false);
            }
            callback();
          } else {
            return Promise.resolve(false);
          }
        });
    },

    handleSubFormInit (value) {
      // 如果是子列表形式 默认添加逻辑主表
      const form = {
        authorizeKey: process.env.VUE_APP_FORM_AUTHORIZE_KEY,
        contentOid: this.comboDirectoryOid,
        formCode: "",
        fieldTypeName: value.fieldTypeName,
        labelName: "主存储",
        labelOid: "",
        typeOid: "",
        fieldTypeOid: value.fieldTypeOid, // 虚拟
        isFormMain: '1',
        instance: null,
        dataForm: {}
      };
      this.currentFieldTypeOid = value.fieldTypeOid;
      this.$refs.formConfigAddTab.currentId = 0;
      this.classifyList.push(form);
    },

    // 缓存实例
    setInstanceCached () {
      this.$nextTick(() => {
        this.classifyList[this.currentClassifyIndex].instance = this.$refs[this.currentFieldTypeOid];
      })
    },

    // 设置主存储
    handleMainStorage (value) {
      this.$set(this.classifyList, 0, {
        ...value,
        isFormMain: "1",
        authorizeKey: process.env.VUE_APP_FORM_AUTHORIZE_KEY,
        contentOid: this.comboDirectoryOid,
        fieldTypeOid: value.fieldTypeOid,
        labelName: "主存储",
        labelOid: "",
        typeOid: "",
        formCode: "",
      });
    },

    // 设置从表
    handleMasterSalveStorage (data) {
      data.forEach(item => {
        const { labelOid, labelName, labelCode, typeOid, typeName, isMovingFlag } = item;
        this.classifyList.push({
          labelOid, labelName, labelCode, typeOid, typeName, isMovingFlag,
          isFormMain: '0',
          fieldTypeOid: labelOid,
          fieldTypeName: labelName,
          instance: null,
          columnList: [],
          formObjectExtand: {},
          dataForm: {},
          authorizeKey: process.env.VUE_APP_FORM_AUTHORIZE_KEY,
          contentOid: this.comboDirectoryOid,
          formCode: "",
        })
      })
    },

    // 保存
    async handleSubmit () {
      // 先校验当前的 如果当前的通过校验 就校验其他的
      const valid = await this.currentClassify.instance.validate();
      if (!valid) return;

      // 从头开始校验
      let len = this.classifyList.length;
      for (let i = 0; i < len; i++) {
        const item = this.classifyList[i];
        const instance = item.instance;
        // debugger
        if (instance) {
          const valid = await instance.validate();
          if (!valid) {
            this.currentFieldTypeOid = item.fieldTypeOid;
            this.$refs.formConfigAddTab.currentId = i;
            return;
          }
        } else if (!this.isModify) {
          // 如果没有实例 说明没有点击过 必定校验不通过
          this.currentFieldTypeOid = item.fieldTypeOid;
          this.$refs.formConfigAddTab.currentId = i;
          await this.$nextTick();
          this.classifyList[i].instance = this.$refs[this.currentFieldTypeOid];
          const valid = await this.classifyList[i].instance.validate();
          if (!valid) return;
        } else if (this.isModify) {
          // 判断columnList是否有值
          if (!item.columnList.length || !item.dataForm.moduleOid) {
            // 说明没有缓存实力 是新增的标签
            this.currentFieldTypeOid = item.fieldTypeOid;
            this.$refs.formConfigAddTab.currentId = i;
            await this.$nextTick();
            this.classifyList[i].instance = this.$refs[this.currentFieldTypeOid];
            const valid = await this.classifyList[i].instance.validate();
            if (!valid) return;
          }
        }
      }

      // 保存
      const params = this.getSubmitParams();

      const result = handleColumnListLengthIsZero(params, this.formType);
      if (result?.relationObjectBusinessId) {
        const labelName = this.classifyList.find(item => item.fieldTypeOid === result.relationObjectBusinessId).labelName;
        return this.$message.warning(`【${labelName}】的存储对象表结构不能为空!`);
      }

      // 合并columnList
      const list = mergeColumnList(this.classifyList);

      const logicFormName = params.objectName + ',' + params?.children?.map?.(item => item.objectName).join(',');

      const loading = this.$loading({
        lock: true,
        text: '正在保存表单配置',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      saveTable(params).then(({ code, data }) => {
        if (code !== 200) {
          loading.close();
          return this.$message.warning('保存失败');
        }
        this.handleSave(data, loading, logicFormName, list);
      }).catch(() => {
        loading.close();
      })
    },

    handleSave (data, loading, logicFormName, list) {
      const { authorizeKey, formCode, designOid, relationObjectBusinessId } = data;
      saveOrUpdateComboFormInfo({
        authorizeKey,
        formCode,
        designOid,
        comboDirectoryOid: this.comboDirectoryOid,
        fieldTypeOid: this.currentFormMainOid || this.classifyList[0].fieldTypeOid,
        formName: this.classifyList[0].fieldTypeName,
        logicFormName,
        status: 3,
        relationObjectBusinessId,
        fieldList: list,
        id: this.isModify ? this.modifyObject.originId : null,
        formOid: this.formOid
      }).then(response => {
        if (response.code === 200) {
          this.$emit('save-close');
          loading.close();
          this.$message.success('保存成功');
        } else {
          loading.close();
          this.$message.success('保存失败');
        }
      }).catch(() => {
        loading.close();
        this.$message.success('保存失败');
      });
    },

    getSubmitParams () {
      if (this.isSingle) {
        return handleSignleParams(this.classifyList);
      }

      if (this.isMasterSlave) {
        return handleMasterSalveParams(this.classifyList);
      }

      if (this.isSubForm) {
        return handleSubFormParams(this.classifyList);
      }
    },

    async handleIsModifyData () {
      if (!this.isModify) return;

      const {
        objectName,
        authorizeKey,
        formCode,
        columnList,
        objectCode,
        id,
        children,
        moduleOid,
        datasourceOid,
        objectOid,
        objectForm,
        isSave,
        idIsVarchar,
        relationMap
      } = this.modifyObject;

      if (!relationMap.length) return;

      const dataForm = {
        objectName,
        moduleOid,
        datasourceOid,
        objectOid,
        objectForm
      };

      // 获取主表
      let mainTable = null;
      relationMap.forEach(item => { 
        const [fieldTypeOid, objectOid] = item;
        mainTable = this.fieldTypeList.find(item => item.fieldTypeOid === fieldTypeOid);
      });

      if (!mainTable) return this.$message.warning('未查询到主表分类');

      this.modifyFieldTypeOid = mainTable.fieldTypeOid;

      const data = this.labelList[mainTable.fieldTypeOid];

      if (data.length === 0) return this.$message.warning('未查询到分类标签信息');

      let dynamicLabelList = data.filter(item => item.isMovingFlag === 1);

      this.handleFormType(dynamicLabelList, data);

      if (this.isSubForm) {
        this.classifyList[0] = {
          fieldTypeName: mainTable.fieldTypeName,
          labelName: "主存储",
          labelOid: "",
          typeOid: "",
          fieldTypeOid: mainTable.fieldTypeOid, // 虚拟
          isFormMain: '1',
          authorizeKey,
          contentOid: this.comboDirectoryOid,
          formCode,
          objectCode,
          id,
          dataForm,
          isSave,
          instance: null
        };
        this.currentFieldTypeOid = mainTable.fieldTypeOid;
      } else {
        this.classifyList.push({
          ...mainTable,
          authorizeKey,
          contentOid: this.comboDirectoryOid,
          formCode,
          objectCode,
          id,
          columnList,
          isFormMain: '1',
          dataForm,
          isSave,
          idIsVarchar,
          labelName: "主存储",
          labelOid: "",
          typeOid: "",
        });
        this.currentFieldTypeOid = mainTable.fieldTypeOid;
      }

      this.$refs.formConfigAddHeader.fieldTypeOid = mainTable.fieldTypeOid;

      if ((this.isMasterSlave || this.isSubForm) && children && children.length > 0) {
        children.forEach((item) => {
          const { objectName, authorizeKey, columnList, objectCode, id, formObjectExtand, moduleOid, datasourceOid, objectOid, objectForm } = item;
          const _labelOid = relationMap.find(item => item[1] == objectOid)[0];
          const currentTable = dynamicLabelList.find(item => item.labelOid == _labelOid);
          if (!currentTable) return console.warn('未匹配到分类标签信息');
          dynamicLabelList = dynamicLabelList.filter(item => item.labelOid !== _labelOid);
          const { labelOid, labelName, labelCode, typeOid, typeName, isMovingFlag } = currentTable;

          let _formObjectExtand = null;

          if (formObjectExtand && Object.keys(formObjectExtand).length > 0) {
            const { extandOid, id, mainObjectOid, secondaryObjectOid, variableName, foreignKey, type, demo } = formObjectExtand;
            _formObjectExtand = { extandOid, id, mainObjectOid, secondaryObjectOid, variableName, foreignKey, type, demo };
          }

          this.classifyList.push({
            labelOid, labelName, labelCode, typeOid, typeName, isMovingFlag,
            fieldTypeOid: labelOid,
            fieldTypeName: labelName,
            authorizeKey,
            contentOid: this.comboDirectoryOid,
            formCode,
            objectCode,
            id,
            columnList,
            formObjectExtand: _formObjectExtand,
            isFormMain: '0',
            dataForm: {
              objectName,
              moduleOid,
              datasourceOid,
              objectOid,
              objectForm
            },
            instance: null
          });
        });
      }
      // 如果存在新增的标签
      if (dynamicLabelList.length > 0) {
        dynamicLabelList.forEach(item => {
          const { labelOid, labelName, labelCode, typeOid, typeName, isMovingFlag } = item;
          this.classifyList.push({
            labelOid, labelName, labelCode, typeOid, typeName, isMovingFlag,
            isFormMain: '0',
            fieldTypeOid: labelOid,
            fieldTypeName: labelName,
            instance: null,
            columnList: [],
            formObjectExtand: {},
            dataForm: {},
            authorizeKey,
            contentOid: this.comboDirectoryOid,
            formCode,
          })
        })
      }
    }
  }
}
</script>

<style scoped lang="scss">
.form-config-add-component {
  height: 100%;

  .form-config-add-component--container {
    height: calc(100% - 110px);
    display: flex;
    justify-content: space-between;

    .form-config-add-main {
      flex: 5;
      background: #ffffff;
      border-radius: 5px 5px 0 0;
      height: calc(100% - 10px);
      margin-top: 10px;
      margin-left: 10px;
    }
  }

  .form-config-add-component--footer {
    height: 60px;
    background: #ffffff;
    box-shadow: 0px 3px 13px 0px rgba(72, 98, 129, 0.49);
    border-radius: 0px 0px 10px 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    z-index: 1;
  }
}
</style>
